package org.chii2.medialibrary.event;

import org.chii2.medialibrary.api.file.FileService;
import org.chii2.medialibrary.api.persistence.PersistenceService;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.chii2.medialibrary.api.provider.MovieInfoProviderService;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import regex2.Matcher;
import regex2.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Movie Event Handler handle all kinds of event related to movies.
 */
public class MovieHandler implements EventHandler {

    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin Service
    private EventAdmin eventAdmin;
    // Injected Persistence Service
    private PersistenceService persistenceService;
    // Injected MovieFactory
    private MovieFactory movieFactory;
    // Injected list of Movie Information Provider Service
    private List<MovieInfoProviderService> providerServices;
    // Movie File Patterns
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
    // Movie Video Resolution Pattern
    private Pattern movieResolutionPattern = Pattern.compile("(?<resolution>\\d+p)", Pattern.CASE_INSENSITIVE);
    // Preferred Movie Providers
    private List<String> movieProviders = Arrays.asList("TMDb");
    //Configuration FIle
    private final static String CONFIG_FILE = "org.chii2.medialibrary.core";
    // Movie file name extract filter key from configuration file
    private final static String MOVIE_FILE_PATTERN = "movie.file.pattern";
    // Movie file name separator key from configuration file
    private final static String MOVIE_NAME_SEPARATOR_PATTERN = "movie.file.name.separator";
    // Movie file source block extract filter key from configuration file
    private final static String MOVIE_SOURCE_PATTERN = "movie.file.source.pattern";
    // Movie file video codec block extract filter key from configuration file
    private final static String MOVIE_VIDEO_CODEC_PATTERN = "movie.file.video.codec.pattern";
    // Movie file audio codec block extract filter key from configuration file
    private final static String MOVIE_AUDIO_CODEC_PATTERN = "movie.file.audio.codec.pattern";
    // Movie file video resolution block extract filter key from configuration file
    private final static String MOVIE_RESOLUTION_PATTERN = "movie.file.video.resolution.pattern";
    // Preferred Movie Provider
    private final static String MOVIE_PROVIDER = "movie.provider";
    // Extract Disk Number Pattern
    private Pattern diskNumPattern = Pattern.compile("\\w*(?<number>\\d+)");
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.event");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library MovieHandler init.");
        Dictionary props = null;
        // Read properties from ConfigAdmin Service
        try {
            Configuration config = configAdmin.getConfiguration(CONFIG_FILE);
            props = config.getProperties();
        } catch (IOException e) {
            logger.error("MovieHandler fail to load configuration with exception: {}.", e.getMessage());
        }
        // Load each configuration
        if (props == null || props.isEmpty()) {
            logger.error("MovieHandler load configuration <{}> with error.", CONFIG_FILE);
        } else {
            // Load movie file name parse patterns
            List<Pattern> namePatterns = ConfigUtils.loadPatterns(props, MOVIE_FILE_PATTERN);
            if (namePatterns != null && !namePatterns.isEmpty()) {
                moviePatterns = namePatterns;
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_FILE_PATTERN);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_FILE_PATTERN);
            }

            // Load movie name separator pattern
            Pattern fileSeparatorPattern = ConfigUtils.loadPattern(props, MOVIE_NAME_SEPARATOR_PATTERN);
            if (fileSeparatorPattern != null) {
                movieSeparatorPattern = fileSeparatorPattern;
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_NAME_SEPARATOR_PATTERN);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_NAME_SEPARATOR_PATTERN);
            }

            // Load movie source pattern
            Pattern sourcePattern = ConfigUtils.loadPattern(props, MOVIE_SOURCE_PATTERN);
            if (sourcePattern != null) {
                movieSourcePattern = sourcePattern;
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_SOURCE_PATTERN);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_SOURCE_PATTERN);
            }

            // Load movie video codec pattern
            Pattern videoCodecPattern = ConfigUtils.loadPattern(props, MOVIE_VIDEO_CODEC_PATTERN);
            if (videoCodecPattern != null) {
                movieVideoCodecPattern = videoCodecPattern;
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_VIDEO_CODEC_PATTERN);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_VIDEO_CODEC_PATTERN);
            }

            // Load movie audio codec pattern
            Pattern audioCodecPattern = ConfigUtils.loadPattern(props, MOVIE_AUDIO_CODEC_PATTERN);
            if (audioCodecPattern != null) {
                movieAudioCodecPattern = audioCodecPattern;
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_AUDIO_CODEC_PATTERN);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_AUDIO_CODEC_PATTERN);
            }

            // Load movie video resolution pattern
            Pattern videoResolutionPattern = ConfigUtils.loadPattern(props, MOVIE_RESOLUTION_PATTERN);
            if (videoResolutionPattern != null) {
                movieResolutionPattern = videoResolutionPattern;
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_RESOLUTION_PATTERN);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_RESOLUTION_PATTERN);
            }

            // Load movie providers configuration
            List<String> providers = ConfigUtils.loadConfigurations(props, MOVIE_PROVIDER);
            if (providers != null && !providers.isEmpty()) {
                movieProviders = providers;
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_PROVIDER);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_PROVIDER);
            }
        }
    }

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library MovieHandler destroy.");
    }

    @Override
    public void handleEvent(Event event) {
        // Movie Scan Event
        if (FileService.MOVIE_SCAN_TOPIC.equals(event.getTopic())) {
            // Get List of file names from event, this should be fine
            @SuppressWarnings("unchecked")
            List<File> files = (List<File>) event.getProperty("files");
            logger.debug("Receive a movie scan event with {} records.", files.size());
            doScanProcess(files);
        }
        // Movie Information Provided Event
        else if (MovieInfoProviderService.MOVIE_INFO_PROVIDED_TOPIC.equals(event.getTopic())) {
            String movieId = (String) event.getProperty(MovieInfoProviderService.MOVIE_ID_PROPERTY);
            @SuppressWarnings("unchecked")
            List<MovieInfo> info = (List<MovieInfo>) event.getProperty(MovieInfoProviderService.MOVIE_INFO_PROPERTY);
            logger.debug("Receive a movie information provided event with {} information.", info.size());
            doInfoProvidedProcess(movieId, info);
        }
        // Movie Information provided Failed Event
        else if (MovieInfoProviderService.MOVIE_INFO_FAILED_TOPIC.equals(event.getTopic())) {
            String movieId = (String) event.getProperty(MovieInfoProviderService.MOVIE_ID_PROPERTY);
            String movieName = (String) event.getProperty(MovieInfoProviderService.MOVIE_NAME_PROPERTY);
            String movieYear = (String) event.getProperty(MovieInfoProviderService.MOVIE_YEAR_PROPERTY);
            @SuppressWarnings("unchecked")
            List<String> excludedProviders = (List<String>) event.getProperty(MovieInfoProviderService.EXCLUDE_PROVIDERS_PROPERTY);
            logger.debug("Receive a movie information failed event.");
            // Try to re-fetch the movie information, through another provider maybe
            notifyProviderFetchInfo(movieId, movieName, movieYear, excludedProviders);
        }
        // Movie Image Provided Event
        else if (MovieInfoProviderService.MOVIE_IMAGE_PROVIDED_TOPIC.equals(event.getTopic())) {
            String imageId = (String) event.getProperty(MovieInfoProviderService.IMAGE_ID_PROPERTY);
            byte[] imageContent = (byte[]) event.getProperty(MovieInfoProviderService.IMAGE_CONTENT_PROPERTY);
            logger.debug("Receive a image provided event.");
            // Try to update image in database
            doImageProvidedProcess(imageId, imageContent);
        }
    }

    /**
     * Parse provided image and save into database
     *
     * @param imageId      Image ID
     * @param imageContent Image Content
     */
    private void doImageProvidedProcess(String imageId, byte[] imageContent) {
        // Get the image from database
        MovieImage image = persistenceService.getMovieImageById(imageId);
        // Add Image save back to database
        image.setImage(imageContent);

        logger.debug("Update image <{}>.", imageId);
        // Save back to database
        persistenceService.merge(image);
    }

    /**
     * Parse provided movie information and save into database
     *
     * @param movieId Movie ID
     * @param info    Movie Information
     */
    private void doInfoProvidedProcess(String movieId, List<MovieInfo> info) {
        // Get movie from database
        Movie movie = persistenceService.getMovieById(movieId);
        // Add information
        for (MovieInfo movieInfo : info) {
            movie.addInfo(movieInfo);
        }

        logger.debug("Update movie <{}> image.", movieId);
        // Save into database        
        persistenceService.merge(movie);

        // Fetch Posters and Backdrops
        for (MovieInfo movieInfo : info) {
            for (MovieImage image : movieInfo.getImages()) {
                notifyProviderFetchImage(image.getId(), image.getUrl(), movieInfo.getProviderName());
            }
        }
    }

    /**
     * Parse the scanned movie files and sync to the database
     *
     * @param files Scanned Movie Files
     */
    private void doScanProcess(List<File> files) {
        // List of movie files
        List<MovieFile> movieFiles = new ArrayList<MovieFile>();

        // Loop and parse the files
        for (File file : files) {
            // Parse file into movie file
            MovieFile movieFile = parseFile(file);
            // Add to list
            movieFiles.add(movieFile);
        }

        // Synchronize the scanned movies to the database
        synchronize(movieFiles);
        logger.debug("Try to synchronize {} movies files.", movieFiles.size());
    }

    /**
     * Synchronize to the database
     *
     * @param movieFiles Movie Files
     */
    private void synchronize(List<MovieFile> movieFiles) {
        // Movie List
        List<Movie> movies = new ArrayList<Movie>();

        // Loop and create a list of movies
        loop:
        for (MovieFile movieFile : movieFiles) {
            // Loop current movies seem it the file shouldIncl to any group
            for (Movie movie : movies) {
                if (movie.shouldIncl(movieFile)) {
                    movie.addFile(movieFile);
                    continue loop;
                }
            }

            // If not shouldIncl to a exist movie group, create a new one
            Movie movie = movieFactory.createMovie();
            movie.addFile(movieFile);
            movies.add(movie);
        }

        // Get current database movie records
        List<? extends Movie> dbMovies = persistenceService.getAllMovies();

        // Compare database and input list
        loop:
        for (Movie dbMovie : dbMovies) {
            for (Movie movie : movies) {
                if (compareMovie(movie, dbMovie)) {
                    // If they reference to the same files, fill the file id with the value from database, and make a merge
                    for (MovieFile movieFile : movie.getFiles()) {
                        movieFile.setId(dbMovie.getFileIdByName(movieFile.getAbsoluteName()));
                        logger.debug("Update movie file <{}>.", movieFile.getId());
                        // Update movie file information
                        persistenceService.merge(movieFile);
                        // If database doesn't contain movie information, try get it. The should not happen
                        if (dbMovie.getInfoCount() == 0) {
                            // Request provider to fetch movie information
                            notifyProviderFetchInfo(dbMovie.getId(), movie.getGuessedMovieName(), movie.getGuessedMovieYear(), new ArrayList<String>());
                        }
                    }

                    // Remove the matched ones from list
                    movies.remove(movie);

                    // Because match has been found, continue to loop next database movie file
                    continue loop;
                }
            }
            logger.debug("Remove movie <{}>.", dbMovie.getId());
            // If no match found, remove the movie from database
            persistenceService.remove(dbMovie);
        }
        logger.debug("Save <{}> movies.", movies.size());
        // After looping, only newly added movies left in the list, persist them
        persistenceService.persist(movies);

        // Request provider to fetch movie information
        for (Movie movie : movies) {
            notifyProviderFetchInfo(movie.getId(), movie.getGuessedMovieName(), movie.getGuessedMovieYear(), new ArrayList<String>());
        }
    }

    /**
     * Notify the provider to fetch movie information
     *
     * @param movieId          Movie Id
     * @param movieName        Movie Name (Guessed)
     * @param movieYear       Movie Released Year (Guessed)
     * @param excludeProviders Exclude Movie Providers (Which may already failed)
     */
    private void notifyProviderFetchInfo(String movieId, String movieName, String movieYear, List<String> excludeProviders) {
        if (!providerServices.isEmpty()) {
            // Find the provider based on configuration order and use it to fetch movie information
            loop:
            for (String preferredProvider : movieProviders) {
                // If the provider on the exclude list (which usually already failed), continue to next
                if (excludeProviders != null && excludeProviders.contains(preferredProvider)) {
                    continue;
                }
                // Loop the current provider see whether preferred provider is available
                for (MovieInfoProviderService provider : providerServices) {
                    if (preferredProvider.equalsIgnoreCase(provider.getProviderName())) {
                        logger.debug("Try to get movie <{}> information with <{}> provider.", movieName, provider.getProviderName());
                        // Try to get movie information from the internet provider (this tends to work background)
                        provider.getMovieInformationByName(movieId, movieName, movieYear, excludeProviders);
                        // Once matched, stop looping
                        break loop;
                    }
                }
            }
        }
    }

    /**
     * Notify the provider to fetch movie image
     *
     * @param imageId         Image ID
     * @param url              URL
     * @param originalProvider Original Provider
     */
    private void notifyProviderFetchImage(String imageId, String url, String originalProvider) {
        if (!providerServices.isEmpty()) {
            // Loop the current provider see whether original provider is available
            for (MovieInfoProviderService provider : providerServices) {
                if (originalProvider.equalsIgnoreCase(provider.getProviderName())) {
                    logger.debug("Try to get movie image <{}> with <{}> provider.", imageId, provider.getProviderName());
                    // Try to get movie image from the internet provider (this tends to work background)
                    provider.getMovieImageByUrl(imageId, url);
                    // Once matched, stop
                    return;
                }
            }

            // If original provider not presented, find the provider based on configuration order and use it to fetch image
            loop:
            for (String preferredProvider : movieProviders) {
                // Loop the current provider see whether preferred provider is available
                for (MovieInfoProviderService provider : providerServices) {
                    if (preferredProvider.equalsIgnoreCase(provider.getProviderName())) {
                        logger.debug("Try to get movie image <{}> with <{}> provider.", imageId, provider.getProviderName());
                        // Try to get movie image from the internet provider (this tends to work background)
                        provider.getMovieImageByUrl(imageId, url);
                        // Once matched, stop looping
                        break loop;
                    }
                }
            }
        }
    }

    /**
     * Compare whether two movie is the same (has same files)
     *
     * @param movie1 First movie
     * @param movie2 Second movie
     * @return True if two move is the same
     */
    private boolean compareMovie(Movie movie1, Movie movie2) {
        // If contains different number of files, not the same
        if (movie1.getFilesCount() != movie2.getFilesCount()) {
            return false;
        }

        // Loop to see whether all files are matched
        Loop:
        for (MovieFile movieFile1 : movie1.getFiles()) {
            for (MovieFile movieFile2 : movie2.getFiles()) {
                // If reference to the same file, loop another file
                if (movieFile1.getAbsoluteName().equals(movieFile2.getAbsoluteName())) {
                    continue Loop;
                }
            }

            // If no match found, two movie are not equal
            return false;
        }

        // If all files are matched, teo movie are equal
        return true;
    }

    /**
     * Parse file name and convert into MovieFile
     *
     * @param file File
     * @return MovieFile
     */
    private MovieFile parseFile(File file) {
        // Regexp matcher to be used
        Matcher matcher = null;

        // Create a MovieFile from factory
        MovieFile movieFile = movieFactory.createMovieFile();

        // Set MovieFile fields
        movieFile.setAbsoluteName(file.getAbsolutePath()); // Absolute File Name(Path)
        movieFile.setFilePath(file.getParent()); // File Path
        movieFile.setFileName(file.getName()); // File Name

        // Loop and parse file name
        for (Pattern pattern : moviePatterns) {
            if (matcher != null) {
                matcher.reset();
            }
            // Match file name
            matcher = pattern.matcher(file.getName());
            if (matcher.find() && matcher.groupCount() > 0) {

                // Match movie name
                String movieName = matcher.group("name");
                if (movieName != null && !movieName.isEmpty()) {
                    // Movie Name contains only words and spaces
                    movieFile.setMovieName(movieName.replaceAll(movieSeparatorPattern.toString(), " "));
                }

                // Match movie year
                movieFile.setYear(matcher.group("year"));

                //Match disk number
                String disk = matcher.group("disk");
                if (disk != null && !disk.isEmpty()) {
                    movieFile.setDiskNum(parseDiskNum(disk));
                }

                // Match release group
                movieFile.setGroup(matcher.group("group"));

                // Match file extension
                movieFile.setFileExtension(matcher.group("ext"));

                // A information block may contains other attribute
                String movieInfo = matcher.group("info");
                // If no movie info block matched, try to get each attribute
                if (movieInfo == null || movieInfo.isEmpty()) {
                    movieFile.setSource(matcher.group("source")); // Source
                    movieFile.setVideoCodec(matcher.group("video_codec")); // Video Codec
                    movieFile.setAudioCodec(matcher.group("audio_codec")); // Audio Codec
                    movieFile.setResolution(matcher.group("resolution")); // Resolution
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
                    matcher.reset();
                    matcher = movieVideoCodecPattern.matcher(movieInfo);
                    if (matcher.find() && matcher.groupCount() > 0) {
                        movieFile.setVideoCodec(matcher.group("video_codec"));
                    }

                    // Match movie audio codec
                    matcher.reset();
                    matcher = movieAudioCodecPattern.matcher(movieInfo);
                    if (matcher.find() && matcher.groupCount() > 0) {
                        movieFile.setAudioCodec(matcher.group("audio_codec"));
                    }

                    // Match video resolution
                    matcher.reset();
                    matcher = movieResolutionPattern.matcher(movieInfo);
                    if (matcher.find() && matcher.groupCount() > 0) {
                        movieFile.setResolution(matcher.group("resolution"));
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
     * @param disk Disk Information
     * @return Disk Number
     */
    private int parseDiskNum(String disk) {
        if (disk == null || disk.isEmpty()) {
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
     * Inject Config Admin
     *
     * @param configAdmin Config Admin
     */
    @SuppressWarnings("unused")
    public void setConfigAdmin(ConfigurationAdmin configAdmin) {
        this.configAdmin = configAdmin;
    }

    /**
     * Inject EventAdmin service
     *
     * @param eventAdmin EventAdmin service
     */
    @SuppressWarnings("unused")
    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    /**
     * Inject Persistence Service
     *
     * @param persistenceService Persistence Service
     */
    @SuppressWarnings("unused")
    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * Inject Movie Factory
     *
     * @param movieFactory Movie Factory
     */
    @SuppressWarnings("unused")
    public void setMovieFactory(MovieFactory movieFactory) {
        this.movieFactory = movieFactory;
    }

    /**
     * Inject Movie Information Provider Services
     *
     * @param providerServices Movie Information Provider Services
     */
    @SuppressWarnings("unused")
    public void setProviderServices(List<MovieInfoProviderService> providerServices) {
        this.providerServices = providerServices;
    }
}

