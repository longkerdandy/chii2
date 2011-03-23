package org.chii2.medialibrary.provider.mediainfo.analyzer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.chii2.medialibrary.api.provider.MovieFileInfoProviderService;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Video File Metadata Analyzer based on MediaInfo
 */
public class VideoAnalyzer implements Runnable {
    // Movie Files
    private List<File> files;
    // EventAdmin
    private EventAdmin eventAdmin;
    // Image Factory
    private MovieFactory movieFactory;
    // Exclude Providers
    private List<String> excludeProviders;
    // Movie File Name Patterns
    private List<Pattern> moviePatterns = new ArrayList<Pattern>() {{
        add(Pattern.compile("^(?<name>[\\w\\.\\-\\']+)\\.\\(?(?<year>\\d{4})\\)?(?<info>(\\.\\w+)+)\\-\\[?(?<group>\\w+)\\]?\\.((?<disk>\\w+)\\.)?(?<ext>[\\w\\-]+)$", Pattern.CASE_INSENSITIVE));
    }};
    // Movie Name Separator Pattern
    private Pattern movieSeparatorPattern = Pattern.compile("[\\._]", Pattern.CASE_INSENSITIVE);
    // Movie Source Pattern
    private Pattern movieSourcePattern = Pattern.compile("(?<source>BDRip|BluRay|HD-DVD|DVDRip|TVRip|HDTVRip|CAM|TS|DVDScr|Scr|R5)", Pattern.CASE_INSENSITIVE);
    // Movie Video Codec Pattern
    private Pattern movieVideoCodecPattern = Pattern.compile("(?<video_codec>XviD|DivX|DivX5|H264|X264)", Pattern.CASE_INSENSITIVE);
    // Movie Audio Codec Pattern
    private Pattern movieAudioCodecPattern = Pattern.compile("(?<audio_codec>AC3|DTS)", Pattern.CASE_INSENSITIVE);
    // Extract Disk Number Pattern
    private Pattern diskNumPattern = Pattern.compile("\\w*(?<number>\\d+)");
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.mediainfo");

    /**
     * Constructor
     *
     * @param files                  Movie Files
     * @param eventAdmin             Event Admin
     * @param movieFactory           Movie Factory
     * @param excludeProviders       Exclude Providers
     * @param moviePatterns          Movie File Name Patterns
     * @param movieSeparatorPattern  Movie Name Separator
     * @param movieSourcePattern     Movie File Source Type
     * @param movieVideoCodecPattern Video Codec
     * @param movieAudioCodecPattern Audio Codec
     * @param diskNumPattern         DIsk Number
     */
    public VideoAnalyzer(List<File> files, EventAdmin eventAdmin, MovieFactory movieFactory, List<String> excludeProviders, List<Pattern> moviePatterns, Pattern movieSeparatorPattern, Pattern movieSourcePattern, Pattern movieVideoCodecPattern, Pattern movieAudioCodecPattern, Pattern diskNumPattern) {
        this.files = files;
        this.eventAdmin = eventAdmin;
        this.movieFactory = movieFactory;
        this.excludeProviders = excludeProviders;
        this.moviePatterns = moviePatterns;
        this.movieSeparatorPattern = movieSeparatorPattern;
        this.movieSourcePattern = movieSourcePattern;
        this.movieVideoCodecPattern = movieVideoCodecPattern;
        this.movieAudioCodecPattern = movieAudioCodecPattern;
        this.diskNumPattern = diskNumPattern;
    }

    @Override
    public void run() {
        try {
            // Using MediaInfo to analyze video files
            List<MovieFile> movieFiles = analyzeWithMediaInfo(this.files, this.movieFactory);
            // Parse File Name information
            movieFiles = parseFileName(movieFiles, moviePatterns, movieSeparatorPattern, movieSourcePattern, movieVideoCodecPattern, movieAudioCodecPattern, diskNumPattern);
            // Raise event
            raiseMovieFileInfoProvidedEvent(files, movieFiles, excludeProviders);
        } catch (Exception e) {
            // Raise failed event
            raiseMovieFileInfoFailedEvent(files, excludeProviders);
        }
    }

    /**
     * Analyze video files with MediaInfo Command Line Interface
     *
     * @param files   Movie Files
     * @param factory Movie Factory
     * @return Parsed MovieFile List
     * @throws java.io.IOException IO Exception (Invoke MediaInfo)
     * @throws javax.xml.stream.XMLStreamException
     *                             XML Exception (Parse MediaInfo Output)
     */
    @SuppressWarnings({"ConstantConditions"})
    protected List<MovieFile> analyzeWithMediaInfo(List<File> files, MovieFactory factory) throws XMLStreamException, IOException {
        // Result
        LinkedList<MovieFile> movieFiles = new LinkedList<MovieFile>();

        // Prepare commands
        List<String> command = new ArrayList<String>();
        // MediaInfo Command Line Interface
        command.add("mediainfo");
        // Full Information
        command.add("-f");
        // Output in xml format
        command.add("--Output=XML");

        // Add movie files
        for (File file : files) {
            // Add to commands as arguments
            command.add(file.getAbsolutePath());

            // Create new movie file
            MovieFile movieFile = factory.createMovieFile();
            movieFile.setAbsoluteName(file.getAbsolutePath());
            movieFile.setFileName(file.getName());
            movieFile.setFilePath(file.getParent());
            movieFile.setSize(file.length());
            movieFile.setModificationDate(new Date(file.lastModified()));

            // Add to result
            movieFiles.add(movieFile);
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
            // Movie File Index
            int movieFileIndex = 0;
            fileLoop:
            while (reader.hasNext() && movieFileIndex < movieFiles.size()) {
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
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getFileName() == null) {
                                            movieFiles.get(movieFileIndex).setFileName(event.asCharacters().getData());
                                        }
                                    } else if ("File_extension".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getFileExtension() == null) {
                                            movieFiles.get(movieFileIndex).setFileExtension(event.asCharacters().getData());
                                        }
                                    } else if ("Format".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getFormat() == null) {
                                            movieFiles.get(movieFileIndex).setFormat(event.asCharacters().getData());
                                        }
                                    } else if ("File_size".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getSize() == 0) {
                                            movieFiles.get(movieFileIndex).setSize(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    } else if ("Duration".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getDuration() == 0) {
                                            movieFiles.get(movieFileIndex).setDuration(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    } else if ("Title".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getMovieName() == null) {
                                            movieFiles.get(movieFileIndex).setMovieName(event.asCharacters().getData());
                                        }
                                    } else if ("Overall_bit_rate".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getBitRate() == 0) {
                                            movieFiles.get(movieFileIndex).setBitRate(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    }
                                }
                                // Video
                                else if (event.isStartElement() && "Video".equalsIgnoreCase(type)) {
                                    if ("Count_of_stream_of_this_kind".equalsIgnoreCase(event.asStartElement().getName().toString()) && reader.hasNext()) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getVideoStreamCount() == 0) {
                                            movieFiles.get(movieFileIndex).setVideoStreamCount(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    } else if ("Format".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getVideoFormat() == null) {
                                            movieFiles.get(movieFileIndex).setVideoFormat(event.asCharacters().getData());
                                        }
                                    } else if ("Codec".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getVideoCodec() == null) {
                                            movieFiles.get(movieFileIndex).setVideoCodec(event.asCharacters().getData());
                                        }
                                    } else if ("Bit_rate".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getVideoBitRate() == 0) {
                                            movieFiles.get(movieFileIndex).setVideoBitRate(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    } else if ("Width".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getVideoWidth() == 0) {
                                            movieFiles.get(movieFileIndex).setVideoWidth(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    } else if ("Height".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getVideoHeight() == 0) {
                                            movieFiles.get(movieFileIndex).setVideoHeight(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    }
                                }
                                // Audio
                                else if (event.isStartElement() && "Audio".equalsIgnoreCase(type)) {
                                    if ("Count_of_stream_of_this_kind".equalsIgnoreCase(event.asStartElement().getName().toString()) && reader.hasNext()) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getAudioStreamCount() == 0) {
                                            movieFiles.get(movieFileIndex).setAudioStreamCount(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    } else if ("Format".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getAudioFormat() == null) {
                                            movieFiles.get(movieFileIndex).setAudioFormat(event.asCharacters().getData());
                                        }
                                    } else if ("Codec".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getAudioCodec() == null) {
                                            movieFiles.get(movieFileIndex).setAudioCodec(event.asCharacters().getData());
                                        }
                                    } else if ("Bit_rate".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getAudioBitRate() == 0) {
                                            movieFiles.get(movieFileIndex).setAudioBitRate(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    } else if ("Sampling_rate".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getAudioSamplingRate() == 0) {
                                            movieFiles.get(movieFileIndex).setAudioSamplingRate(NumberUtils.toLong(event.asCharacters().getData()));
                                        }
                                    } else if ("Channel_s".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getAudioChannelCount() == 0) {
                                            movieFiles.get(movieFileIndex).setAudioChannelCount(NumberUtils.toInt(event.asCharacters().getData()));
                                        }
                                    } else if ("Language".equalsIgnoreCase(event.asStartElement().getName().toString())) {
                                        event = reader.peek();
                                        if (event.isCharacters() && movieFiles.get(movieFileIndex).getAudioLanguage() == null) {
                                            movieFiles.get(movieFileIndex).setAudioLanguage(event.asCharacters().getData());
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
                            movieFileIndex++;
                            continue fileLoop;
                        }
                    }
                }
            }
        } catch (XMLStreamException e) {
            logger.warn("Analyze movie files using MediaInfo arise XML Exception:{}.", e.getMessage());
            throw e;
        } catch (IOException e) {
            logger.warn("Analyze movie files wih IO Exception:{}.", e.getMessage());
            throw e;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (XMLStreamException ignore) {
            }
        }

        // Return Result
        return movieFiles;
    }

    /**
     * Parse File Name based on given regex pattern, and fill the MovieFile information
     *
     * @param movieFiles             List of Movie File to be parsed
     * @param moviePatterns          Movie File Name Patterns
     * @param movieSeparatorPattern  Movie Name Separator
     * @param movieSourcePattern     Movie File Source Type
     * @param movieVideoCodecPattern Video Codec
     * @param movieAudioCodecPattern Audio Codec
     * @param diskNumPattern         DIsk Number
     * @return Parsed Movie Files
     */
    @SuppressWarnings({"ConstantConditions"})
    protected List<MovieFile> parseFileName(List<MovieFile> movieFiles, List<Pattern> moviePatterns, Pattern movieSeparatorPattern, Pattern movieSourcePattern, Pattern movieVideoCodecPattern, Pattern movieAudioCodecPattern, Pattern diskNumPattern) {
        // Regexp matcher to be used
        Matcher matcher = null;

        // Loop Movie Files
        for (MovieFile movieFile : movieFiles) {
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
                        movieFile.setDiskNum(parseDiskNum(disk, diskNumPattern));
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
        }

        return movieFiles;
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
     * @param files            List of Movie Files (from request)
     * @param movieFiles           Movie File Information
     * @param excludeProviders List of exclude Providers (from request)
     */
    private void raiseMovieFileInfoProvidedEvent(List<File> files, List<MovieFile> movieFiles, List<String> excludeProviders) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(MovieFileInfoProviderService.MOVIE_FILE_PROPERTY, files);
        properties.put(MovieFileInfoProviderService.MOVIE_FILE_INFO_PROPERTY, movieFiles);
        properties.put(MovieFileInfoProviderService.EXCLUDE_PROVIDERS_PROPERTY, excludeProviders);
        // Send a event
        Event event = new Event(MovieFileInfoProviderService.MOVIE_FILE_INFO_PROVIDED_TOPIC, properties);
        logger.debug("Send a movie files information provided event.");
        eventAdmin.postEvent(event);
    }

    /**
     * Raise a movie file information failed event
     *
     * @param files            List of Movie Files (from request)
     * @param excludeProviders List of exclude Providers (from request)
     */
    private void raiseMovieFileInfoFailedEvent(List<File> files, List<String> excludeProviders) {
        // Since failed, append this provider to exclude provider list
        excludeProviders.add("MediaInfo");
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(MovieFileInfoProviderService.MOVIE_FILE_PROPERTY, files);
        properties.put(MovieFileInfoProviderService.MOVIE_FILE_INFO_PROPERTY, new ArrayList<Movie>());
        properties.put(MovieFileInfoProviderService.EXCLUDE_PROVIDERS_PROPERTY, excludeProviders);
        // Send a event
        Event event = new Event(MovieFileInfoProviderService.MOVIE_FILE_INFO_FAILED_TOPIC, properties);
        logger.debug("Send a movie files information failed event.");
        eventAdmin.postEvent(event);
    }
}
