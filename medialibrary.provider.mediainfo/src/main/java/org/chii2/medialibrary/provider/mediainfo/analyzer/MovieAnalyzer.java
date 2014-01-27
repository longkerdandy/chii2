package org.chii2.medialibrary.provider.mediainfo.analyzer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.chii2.medialibrary.api.provider.MovieFileInfoProviderService;
import org.chii2.medialibrary.provider.mediainfo.util.Xml10FilterReader;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import regex2.Matcher;
import regex2.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Video File Metadata MovieAnalyzer based on MediaInfo
 */
public class MovieAnalyzer implements Runnable {
    // Flag
    public volatile boolean shouldStop = false;
    // Request queue
    private final BlockingQueue<Path> queue;
    // EventAdmin
    private final EventAdmin eventAdmin;
    // Movie Factory
    private final MovieFactory movieFactory;
    // Format Version Pattern
    private final Pattern formatVersionPattern = Pattern.compile("\\.*(?<version>\\d+)");
    // Movie File Name Patterns
    private final List<Pattern> moviePatterns;
    // Movie Name Separator Pattern
    private final Pattern movieSeparatorPattern;
    // Movie Source Pattern
    private final Pattern movieSourcePattern;
    // Movie Video Codec Pattern
    private final Pattern movieVideoCodecPattern;
    // Movie Audio Codec Pattern
    private final Pattern movieAudioCodecPattern;
    // Extract Disk Number Pattern
    private final Pattern diskNumPattern;
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.mediainfo");

    /**
     * Constructor
     *
     * @param queue                  Request Queue
     * @param eventAdmin             Event Admin
     * @param movieFactory           Movie Factory
     * @param moviePatterns          Movie File Name Patterns
     * @param movieSeparatorPattern  Movie Name Separator
     * @param movieSourcePattern     Movie File Source Type
     * @param movieVideoCodecPattern Video Codec
     * @param movieAudioCodecPattern Audio Codec
     * @param diskNumPattern         DIsk Number
     */
    public MovieAnalyzer(BlockingQueue<Path> queue, EventAdmin eventAdmin, MovieFactory movieFactory, List<Pattern> moviePatterns, Pattern movieSeparatorPattern, Pattern movieSourcePattern, Pattern movieVideoCodecPattern, Pattern movieAudioCodecPattern, Pattern diskNumPattern) {
        this.queue = queue;
        this.eventAdmin = eventAdmin;
        this.movieFactory = movieFactory;
        this.moviePatterns = moviePatterns;
        this.movieSeparatorPattern = movieSeparatorPattern;
        this.movieSourcePattern = movieSourcePattern;
        this.movieVideoCodecPattern = movieVideoCodecPattern;
        this.movieAudioCodecPattern = movieAudioCodecPattern;
        this.diskNumPattern = diskNumPattern;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void run() {
        try {
            while (!this.shouldStop) {
                // Read request
                Path requestFile = this.queue.take();
                try {
                    // Using MediaInfo to analyze video files
                    MovieFile movieFile = this.analyzeWithMediaInfo(requestFile, movieFactory);
                    if (movieFile != null) {
                        // Parse File Name information
                        movieFile = this.parseFileName(movieFile, moviePatterns, movieSeparatorPattern, movieSourcePattern, movieVideoCodecPattern, movieAudioCodecPattern, diskNumPattern);
                        // Raise event
                        this.postMovieFileInfoProvidedEvent(requestFile, movieFile);
                    } else {
                        // Raise failed event
                        this.postMovieFileInfoFailedEvent(requestFile, "Not valid movie path");
                    }
                } catch (Exception e) {
                    logger.warn("MediaInfo Provider with error: {}.", ExceptionUtils.getMessage(e));
                    // Raise failed event
                    this.postMovieFileInfoFailedEvent(requestFile, ExceptionUtils.getMessage(e));
                }
            }
        } catch (InterruptedException e) {
            logger.error("MediaInfo Provider has been interrupted with error: {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Analyze video files with MediaInfo Command Line Interface
     *
     * @param path    Movie File
     * @param factory Movie Factory
     * @return Parsed MovieFile
     * @throws java.io.IOException IO Exception (Invoke MediaInfo)
     * @throws javax.xml.stream.XMLStreamException
     *                             XML Exception (Parse MediaInfo Output)
     */
    @SuppressWarnings({"ConstantConditions"})
    protected MovieFile analyzeWithMediaInfo(Path path, MovieFactory factory) throws XMLStreamException, IOException {
        //Result
        MovieFile movieFile;

        // Prepare commands
        List<String> command = new ArrayList<>();
        // MediaInfo Command Line Interface
        command.add("mediainfo");
        // Full Information
        command.add("-f");
        // Output in xml format
        command.add("--Output=XML");

        // Add movie file
        if (Files.isRegularFile(path)) {
            // Add to commands as arguments
            command.add(path.toAbsolutePath().toString());

            // Create new movie file
            movieFile = factory.createMovieFile();
            movieFile.setAbsolutePath(path.toAbsolutePath().toString());
            movieFile.setFileName(path.getFileName().toString());
            movieFile.setFilePath(path.getParent().toString());
            movieFile.setSize(Files.size(path));
            movieFile.setModificationDate(new Date(Files.getLastModifiedTime(path).toMillis()));
        } else {
            return null;
        }


        // Event reader
        XMLEventReader reader = null;

        try {
            // Invoke MediaInfo via command line interface
            Process process = new ProcessBuilder(command).start();
            // StAX Factory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Event reader
            reader = inputFactory.createXMLEventReader(new Xml10FilterReader(new InputStreamReader(process.getInputStream())));
            fileLoop:
            while (reader.hasNext()) {
                XMLEvent event = reader.nextTag();
                // File Loop
                if (event.isStartElement() && "File".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                    trackLoop:
                    while (reader.hasNext()) {
                        event = reader.nextTag();
                        if (event.isStartElement() && "track".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                            String type = null;
                            Iterator iterator = event.asStartElement().getAttributes();
                            while (iterator.hasNext()) {
                                Attribute attribute = (Attribute) iterator.next();
                                if ("type".equalsIgnoreCase(attribute.getName().toString())) {
                                    type = attribute.getValue();
                                }
                            }
                            while (reader.hasNext()) {
                                event = reader.nextEvent();
                                // General
                                if (event.isStartElement() && "General".equalsIgnoreCase(type)) {
                                    if ("File_name".equalsIgnoreCase(event.asStartElement().getName().toString()) && reader.hasNext()) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getFileName() == null) {
                                            movieFile.setFileName(event.asCharacters().getData());
                                        }
                                    } else if ("File_extension".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getFileExtension() == null) {
                                            movieFile.setFileExtension(event.asCharacters().getData());
                                        }
                                    } else if ("Format".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getFormat() == null) {
                                            movieFile.setFormat(event.asCharacters().getData());
                                        }
                                    } else if ("File_size".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getSize() == 0) {
                                            movieFile.setSize(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    } else if ("Duration".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getDuration() == 0) {
                                            movieFile.setDuration(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    } else if ("Title".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getMovieName() == null) {
                                            // Comment this because I found this is not reliable
                                            // movieFiles.get(movieFileIndex).setMovieName(event.asCharacters().getData());
                                        }
                                    } else if ("Internet_media_type".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getMime() == null) {
                                            movieFile.setMime(event.asCharacters().getData());
                                        }
                                    } else if ("Overall_bit_rate".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getBitRate() == 0) {
                                            movieFile.setBitRate(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    }
                                }
                                // Video
                                else if (event.isStartElement() && "Video".equalsIgnoreCase(type)) {
                                    if ("Count_of_stream_of_this_kind".equalsIgnoreCase(event.asStartElement().getName().toString()) && reader.hasNext()) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getVideoStreamCount() == 0) {
                                            movieFile.setVideoStreamCount(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    } else if ("Format".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getVideoFormat() == null) {
                                            movieFile.setVideoFormat(event.asCharacters().getData());
                                        }
                                    } else if ("Format_profile".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getVideoFormatProfile() == null) {
                                            movieFile.setVideoFormatProfile(event.asCharacters().getData());
                                        }
                                    } else if ("Format_version".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getVideoFormatVersion() == 0) {
                                            movieFile.setVideoFormatVersion(this.parseFormatVersion(event.asCharacters().getData()));
                                        }
                                    } else if ("Codec".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getVideoCodec() == null) {
                                            movieFile.setVideoCodec(event.asCharacters().getData());
                                        }
                                    } else if ("Codec_CC".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters()) {
                                            movieFile.setVideoCodec(event.asCharacters().getData());
                                        }
                                    } else if ("Bit_rate".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getVideoBitRate() == 0) {
                                            movieFile.setVideoBitRate(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    } else if ("Bit_depth".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getVideoBitDepth() == 0) {
                                            movieFile.setVideoBitDepth(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    } else if ("Frame_rate".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getVideoFrameRate() == 0) {
                                            movieFile.setVideoFrameRate(NumberUtils.toFloat(event.asCharacters().getData()));
                                        }
                                    } else if ("Width".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getVideoWidth() == 0) {
                                            movieFile.setVideoWidth(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    } else if ("Height".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getVideoHeight() == 0) {
                                            movieFile.setVideoHeight(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    }
                                }
                                // Audio
                                else if (event.isStartElement() && "Audio".equalsIgnoreCase(type)) {
                                    if ("Count_of_stream_of_this_kind".equalsIgnoreCase(event.asStartElement().getName().toString()) && reader.hasNext()) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getAudioStreamCount() == 0) {
                                            movieFile.setAudioStreamCount(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    } else if ("Format".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getAudioFormat() == null) {
                                            movieFile.setAudioFormat(event.asCharacters().getData());
                                        }
                                    } else if ("Format_profile".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getAudioFormatProfile() == null) {
                                            movieFile.setAudioFormatProfile(event.asCharacters().getData());
                                        }
                                    } else if ("Format_version".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getAudioFormatVersion() == 0) {
                                            movieFile.setAudioFormatVersion(this.parseFormatVersion(event.asCharacters().getData()));
                                        }
                                    } else if ("Codec".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getAudioCodec() == null) {
                                            movieFile.setAudioCodec(event.asCharacters().getData());
                                        }
                                    } else if ("Codec_CC".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters()) {
                                            movieFile.setAudioCodec(event.asCharacters().getData());
                                        }
                                    } else if ("Bit_rate".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getAudioBitRate() == 0) {
                                            movieFile.setAudioBitRate(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    } else if ("Bit_depth".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getAudioBitDepth() == 0) {
                                            movieFile.setAudioBitDepth(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    } else if ("Sampling_rate".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getAudioSamplingRate() == 0) {
                                            movieFile.setAudioSamplingRate(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    } else if ("Channel_s".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getAudioChannelCount() == 0) {
                                            movieFile.setAudioChannelCount(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    } else if ("Language".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFile.getAudioLanguage() == null) {
                                            movieFile.setAudioLanguage(event.asCharacters().getData());
                                        }
                                    }
                                }
                                // Continue to loop track (General / Video / Audio)
                                else if (event.isEndElement() && "track".equalsIgnoreCase(event.asEndElement().getName().toString())) {
                                    continue trackLoop;
                                }
                            }
                            // File Loop End
                        } else if (event.isEndElement() && "File".equalsIgnoreCase(event.asEndElement().getName().toString())) {
                            // Parse Multiple File Result
                            // continue fileLoop;
                            // Parse Single File Result
                            break fileLoop;
                        }
                    }
                }
            }
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (XMLStreamException ignore) {
            }
        }

        // Return Result
        return movieFile;
    }

    /**
     * Parse Format Version
     *
     * @param formatVersion Format Version
     * @return Version Number
     */
    protected int parseFormatVersion(String formatVersion) {
        Matcher matcher = formatVersionPattern.matcher(formatVersion);
        if (matcher.find() && matcher.groupCount() > 0) {
            String version = matcher.group("version");
            return Integer.parseInt(version);
        } else {
            return 0;
        }
    }

    /**
     * Parse File Name based on given regex pattern, and fill the MovieFile information
     *
     * @param movieFile              Movie File to be parsed
     * @param moviePatterns          Movie File Name Patterns
     * @param movieSeparatorPattern  Movie Name Separator
     * @param movieSourcePattern     Movie File Source Type
     * @param movieVideoCodecPattern Video Codec
     * @param movieAudioCodecPattern Audio Codec
     * @param diskNumPattern         DIsk Number
     * @return Parsed Movie File
     */
    @SuppressWarnings({"ConstantConditions"})
    protected MovieFile parseFileName(MovieFile movieFile, List<Pattern> moviePatterns, Pattern movieSeparatorPattern, Pattern movieSourcePattern, Pattern movieVideoCodecPattern, Pattern movieAudioCodecPattern, Pattern diskNumPattern) {
        // Regexp matcher to be used
        Matcher matcher = null;

        // Loop and parse file name
        for (Pattern pattern : moviePatterns) {
            if (matcher != null) {
                matcher.reset();
            }
            // Match file name
            matcher = pattern.matcher(movieFile.getFileName());
            if (matcher.find() && matcher.groupCount() > 0) {

                // Match movie name
                String movieName = matcher.group("name");
                if (movieFile.getMovieName() == null && movieName != null && !movieName.isEmpty()) {
                    // Movie Name contains only words and spaces
                    movieFile.setMovieName(movieName.replaceAll(movieSeparatorPattern.toString(), " "));
                }

                // Match movie year
                movieFile.setYear(NumberUtils.toInt(matcher.group("year")));

                //Match disk number
                String disk = matcher.group("disk");
                if (disk != null && !disk.isEmpty()) {
                    movieFile.setDiskNum(this.parseDiskNum(disk, diskNumPattern));
                } else {
                    movieFile.setDiskNum(1);
                }

                // Match release group
                movieFile.setGroup(matcher.group("group"));

                // A information block may contains other attribute
                String movieInfo = matcher.group("info");
                // If no movie info block matched, try to get each attribute
                if (movieInfo == null || movieInfo.isEmpty()) {
                    movieFile.setSource(matcher.group("source")); // Source
                    if (movieFile.getVideoCodec() == null) {
                        movieFile.setVideoCodec(matcher.group("video_codec")); // Video Codec
                    }
                    if (movieFile.getAudioCodec() == null) {
                        movieFile.setAudioCodec(matcher.group("audio_codec")); // Audio Codec
                    }
                }
                // If info matched, try to parse info into separate attribute
                else {
                    // Match file source
                    matcher.reset();
                    matcher = movieSourcePattern.matcher(movieInfo);
                    if (matcher.find() && matcher.groupCount() > 0) {
                        movieFile.setSource(matcher.group("source"));
                    }

                    // Match movie video codec
                    if (movieFile.getVideoCodec() == null) {
                        matcher.reset();
                        matcher = movieVideoCodecPattern.matcher(movieInfo);
                        if (matcher.find() && matcher.groupCount() > 0) {
                            movieFile.setVideoCodec(matcher.group("video_codec"));
                        }
                    }

                    // Match movie audio codec
                    if (movieFile.getAudioCodec() == null) {
                        matcher.reset();
                        matcher = movieAudioCodecPattern.matcher(movieInfo);
                        if (matcher.find() && matcher.groupCount() > 0) {
                            movieFile.setAudioCodec(matcher.group("audio_codec"));
                        }
                    }
                }
                // Once matched, Stop match loop
                break;
            }
        }

        return movieFile;
    }

    /**
     * Parse disk information into disk order number
     *
     * @param disk           Disk Information
     * @param diskNumPattern Disk Number Pattern
     * @return Disk Number
     */
    protected int parseDiskNum(String disk, Pattern diskNumPattern) {
        if (StringUtils.isBlank(disk)) {
            return 1;
        }

        // Match "CD1" or "DVD2" or "1"
        Matcher matcher = diskNumPattern.matcher(disk);
        if (matcher.find() && matcher.groupCount() > 0) {
            try {
                return Integer.valueOf(matcher.group("number"));
            } catch (NumberFormatException e) {
                // This should not happens, since regexp is matched
            }
        }

        // Get last char and turn it into a number
        String num = disk.substring(disk.length() - 1);
        if (num.equalsIgnoreCase("a")) return 1;
        else if (num.equalsIgnoreCase("b")) return 2;
        else if (num.equalsIgnoreCase("c")) return 3;
        else if (num.equalsIgnoreCase("d")) return 4;
        else if (num.equalsIgnoreCase("e")) return 5;
        else if (num.equalsIgnoreCase("f")) return 6;
        else if (num.equalsIgnoreCase("g")) return 7;
        else if (num.equalsIgnoreCase("h")) return 8;
        else if (num.equalsIgnoreCase("i")) return 9;
        else if (num.equalsIgnoreCase("j")) return 10;
        else if (num.equalsIgnoreCase("k")) return 11;
        else if (num.equalsIgnoreCase("l")) return 12;
        else if (num.equalsIgnoreCase("m")) return 13;
        else if (num.equalsIgnoreCase("n")) return 14;
        else if (num.equalsIgnoreCase("o")) return 15;
        else if (num.equalsIgnoreCase("p")) return 16;
        else if (num.equalsIgnoreCase("q")) return 17;
        else if (num.equalsIgnoreCase("r")) return 18;
        else if (num.equalsIgnoreCase("s")) return 19;
        else if (num.equalsIgnoreCase("t")) return 20;
        else if (num.equalsIgnoreCase("u")) return 21;
        else if (num.equalsIgnoreCase("v")) return 22;
        else if (num.equalsIgnoreCase("w")) return 23;
        else if (num.equalsIgnoreCase("x")) return 24;
        else if (num.equalsIgnoreCase("y")) return 25;
        else if (num.equalsIgnoreCase("z")) return 26;

        // None matched, just return 1
        return 1;
    }

    /**
     * Raise a movie file information provided event
     *
     * @param path      Movie File/Path (from request)
     * @param movieFile Movie File Information
     */
    private void postMovieFileInfoProvidedEvent(Path path, MovieFile movieFile) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(MovieFileInfoProviderService.MOVIE_PATH_PROPERTY, path);
        properties.put(MovieFileInfoProviderService.MOVIE_FILE_INFO_PROPERTY, movieFile);
        // Send a event
        Event event = new Event(MovieFileInfoProviderService.MOVIE_FILE_INFO_PROVIDED_TOPIC, properties);
        logger.debug("Send a movie file information provided event for: {}.", path);
        this.eventAdmin.postEvent(event);
    }

    /**
     * Raise a movie file information failed event
     *
     * @param path        Movie File/Path (from request)t)
     * @param failMessage Fail Message
     */
    private void postMovieFileInfoFailedEvent(Path path, String failMessage) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(MovieFileInfoProviderService.MOVIE_PATH_PROPERTY, path);
        properties.put(MovieFileInfoProviderService.FAIL_MESSAGE_PROPERTY, failMessage);
        // Send a event
        Event event = new Event(MovieFileInfoProviderService.MOVIE_FILE_INFO_FAILED_TOPIC, properties);
        logger.debug("Send a movie files information failed event for: {}.", path);
        this.eventAdmin.postEvent(event);
    }
}
