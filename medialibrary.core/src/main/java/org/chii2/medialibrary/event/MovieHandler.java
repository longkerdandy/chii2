package org.chii2.medialibrary.event;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.file.FileService;
import org.chii2.medialibrary.api.persistence.PersistenceService;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.chii2.medialibrary.api.provider.MovieFileInfoProviderService;
import org.chii2.medialibrary.api.provider.MovieInfoProviderService;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    // Injected list of Movie Online Information Provider Service
    private List<MovieInfoProviderService> onlineProviderServices;
    // Injected list of Movie File Information Provider Service
    private List<MovieFileInfoProviderService> fileProviderServices;
    // Preferred Movie File Information Providers
    private List<String> movieFileInfoProviders = Arrays.asList("MediaInfo");
    // Preferred Movie Online Information Providers
    private List<String> movieOnlineInfoProviders = Arrays.asList("TMDb");
    // Force to refresh/update movie's information
    private boolean forceInfoUpdate = true;
    // Poster fetch count
    private int posterCount = 3;
    // Backdrop fetch count
    private int backdropCount = 3;
    //Configuration FIle
    private final static String CONFIG_FILE = "org.chii2.medialibrary.core";
    // Preferred Movie File Information Provider Config Key
    private final static String MOVIE_FILE_INFO_PROVIDER = "movie.file.info.provider";
    // Preferred Movie Online Information Provider Config Key
    private final static String MOVIE_ONLINE_INFO_PROVIDER = "movie.online.info.provider";
    // Force to refresh/update movie's information Config Key
    private final static String MOVIE_FORCE_INFORMATION_UPDATE = "movie.force.update";
    // Poster Fetch Count Config Key
    private final static String MOVIE_POSTER_COUNT = "movie.poster.count";
    // Backdrop Fetch Count Config Key
    private final static String MOVIE_BACKDROP_COUNT = "movie.backdrop.count";
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
            // Load movie file information providers configuration
            List<String> movieFileInfoProviders = ConfigUtils.loadConfigurations(props, MOVIE_FILE_INFO_PROVIDER);
            if (movieFileInfoProviders != null && !movieFileInfoProviders.isEmpty()) {
                this.movieFileInfoProviders = movieFileInfoProviders;
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_FILE_INFO_PROVIDER);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_FILE_INFO_PROVIDER);
            }

            // Load movie online information providers configuration
            List<String> movieOnlineInfoProviders = ConfigUtils.loadConfigurations(props, MOVIE_ONLINE_INFO_PROVIDER);
            if (movieOnlineInfoProviders != null && !movieOnlineInfoProviders.isEmpty()) {
                this.movieOnlineInfoProviders = movieOnlineInfoProviders;
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_ONLINE_INFO_PROVIDER);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_ONLINE_INFO_PROVIDER);
            }

            // Load force update information
            String forceInfoUpdate = ConfigUtils.loadConfiguration(props, MOVIE_FORCE_INFORMATION_UPDATE);
            if (StringUtils.isNotBlank(forceInfoUpdate)) {
                this.forceInfoUpdate = Boolean.parseBoolean(forceInfoUpdate);
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_FORCE_INFORMATION_UPDATE);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_FORCE_INFORMATION_UPDATE);
            }

            // Load force update information
            String posterCount = ConfigUtils.loadConfiguration(props, MOVIE_POSTER_COUNT);
            if (StringUtils.isNotBlank(posterCount)) {
                this.posterCount = Integer.parseInt(posterCount);
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_POSTER_COUNT);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_POSTER_COUNT);
            }

            // Load force update information
            String backdropCount = ConfigUtils.loadConfiguration(props, MOVIE_BACKDROP_COUNT);
            if (StringUtils.isNotBlank(backdropCount)) {
                this.backdropCount = Integer.parseInt(backdropCount);
                logger.debug("MovieHandler configuration <{}> loaded.", MOVIE_BACKDROP_COUNT);
            } else {
                logger.error("MovieHandler configuration <{}> is not valid.", MOVIE_BACKDROP_COUNT);
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
            // Get list of File from event, this should be fine
            @SuppressWarnings("unchecked")
            List<File> files = (List<File>) event.getProperty(FileService.FILE_PROPERTY);
            logger.debug("Receive a movie scan event with {} records.", files.size());
            notifyProviderFetchFileInfo(files, new ArrayList<String>());
        }
        // Movie FIle Information Provided Event
        else if (MovieFileInfoProviderService.MOVIE_FILE_INFO_PROVIDED_TOPIC.equals(event.getTopic())) {
            // Get list of Movie from event, this should be fine
            @SuppressWarnings("unchecked")
            List<MovieFile> movieFiles = (List<MovieFile>) event.getProperty(MovieFileInfoProviderService.MOVIE_FILE_INFO_PROPERTY);
            logger.debug("Receive a movie file information provided event with {} records.", movieFiles.size());
            synchronizeFileInfo(movieFiles, this.forceInfoUpdate);
        }
        // Movie FIle Information Failed Event
        else if (MovieFileInfoProviderService.MOVIE_FILE_INFO_FAILED_TOPIC.equals(event.getTopic())) {
            // Get list of Movie from event, this should be fine
            @SuppressWarnings("unchecked")
            List<File> files = (List<File>) event.getProperty(MovieFileInfoProviderService.MOVIE_FILE_PROPERTY);
            @SuppressWarnings("unchecked")
            List<String> excludeProviders = (List<String>) event.getProperty(MovieFileInfoProviderService.EXCLUDE_PROVIDERS_PROPERTY);
            logger.debug("Receive a movie file information failed event, will try for another provider.");
            notifyProviderFetchFileInfo(files, excludeProviders);
        }
        // Movie Information Provided Event
        else if (MovieInfoProviderService.MOVIE_INFO_PROVIDED_TOPIC.equals(event.getTopic())) {
            String movieId = (String) event.getProperty(MovieInfoProviderService.MOVIE_ID_PROPERTY);
            @SuppressWarnings("unchecked")
            List<MovieInfo> info = (List<MovieInfo>) event.getProperty(MovieInfoProviderService.MOVIE_INFO_PROPERTY);
            logger.debug("Receive a movie information provided event with {} information.", info.size());
            synchronizeInfo(movieId, info);
        }
        // Movie Information provided Failed Event
        else if (MovieInfoProviderService.MOVIE_INFO_FAILED_TOPIC.equals(event.getTopic())) {
            // Currently, do nothing here
        }
        // Movie Image Provided Event
        else if (MovieInfoProviderService.MOVIE_IMAGE_PROVIDED_TOPIC.equals(event.getTopic())) {
            String imageId = (String) event.getProperty(MovieInfoProviderService.IMAGE_ID_PROPERTY);
            byte[] imageContent = (byte[]) event.getProperty(MovieInfoProviderService.IMAGE_CONTENT_PROPERTY);
            logger.debug("Receive a movie image {} provided event.", imageId);
            // Try to update image in database
            synchronizeImage(imageId, imageContent);
        }
    }

    /**
     * Parse provided image and save into database
     *
     * @param imageId      Image ID
     * @param imageContent Image Content
     */
    private void synchronizeImage(String imageId, byte[] imageContent) {
        // Get the image from database
        MovieImage image = persistenceService.getMovieImageById(imageId);
        if (image != null) {
            // Add Image save back to database
            image.setImage(imageContent);
            // Save back to database
            persistenceService.merge(image);
            logger.debug("Movie image {} merged into database.", imageId);
        } else {
            logger.debug("Movie image {} not found in database.", imageId);
        }
    }

    /**
     * Parse provided movie information and save into database
     *
     * @param movieId Movie ID
     * @param info    Movie Information
     */
    private void synchronizeInfo(String movieId, List<MovieInfo> info) {
        // Get movie from database
        Movie movie = persistenceService.getMovieById(movieId);

        // Null, Return
        if (movie == null) {
            return;
        }

        // Remove current info
        if (movie.getInfoCount() > 0) {
            for (MovieInfo movieInfo : movie.getInfo()) {
                persistenceService.remove(movieInfo);
            }
        }

        // Set new info
        movie.setInfo(info);

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
     * Synchronize Movie Files to database
     *
     * @param movieFiles      Movie Files
     * @param forceInfoUpdate True to force movie information update (even information already existed)
     */
    private void synchronizeFileInfo(List<MovieFile> movieFiles, boolean forceInfoUpdate) {
        // Current Movie Files from database, and will be compared after looping
        List<? extends MovieFile> currentFiles = persistenceService.getMovieFiles(-1, -1, null);
        // Present Movie Files
        List<MovieFile> presentFiles = new ArrayList<MovieFile>();
        // Loop files
        for (MovieFile movieFile : movieFiles) {
            // Try to get the movie file in database which point to the same file in disk
            MovieFile dbMovieFile = persistenceService.getMovieFileByAbsoluteName(movieFile.getAbsoluteName());
            // Database doesn't contain current movie file
            if (dbMovieFile == null) {
                Movie movie = persistenceService.getMoviesContainFile(movieFile.getFilePath(), movieFile.getMovieName());
                // Database doesn't contain a movie this movie file belong to, create a new movie
                if (movie == null) {
                    movie = movieFactory.createMovie();
                    movie.addFile(movieFile);
                    persistenceService.merge(movie);
                    //
                }
                // Database already has a movie this movie file should be included, add file to movie
                else {
                    movie.addFile(movieFile);
                    persistenceService.merge(movie);
                }
            }
            // Database contains the movie file, we will update it
            else {
                movieFile.setId(dbMovieFile.getId());
                persistenceService.merge(movieFile);
                // Add to present movie file list
                presentFiles.add(dbMovieFile);
            }
        }

        // Remove unnecessary movie files from database
        loop:
        for (MovieFile currentFile : currentFiles) {
            for (MovieFile presentFile : presentFiles) {
                // Current Movie File is still present, continue to next one
                if (currentFile.getId().equals(presentFile.getId())) {
                    presentFiles.remove(presentFile);
                    continue loop;
                }
            }

            // Here means current Movie File is not present any more, remove it
            // Movie contains the movie file
            Movie movie = persistenceService.getMoviesContainFile(currentFile.getId());
            // Shouldn't be null, already been removed, is that possible?
            if (movie != null) {
                // Movie contains this file only, and since the file is going to be removed, we just removed the movie
                if (movie.getFilesCount() == 1) {
                    persistenceService.remove(movie);
                }
                // Movie contains other files
                else {
                    movie.removeFile(currentFile);
                    persistenceService.remove(currentFile);
                    // TODO: not sure this logic is correct, do I need this?
                    persistenceService.merge(movie);
                }
            }
        }

        // Notify providers fetch movie info (online information)
        List<? extends Movie> movies = persistenceService.getMovies(-1, -1, null);
        for (Movie movie : movies) {
            // Movie already contains information
            if (movie.getInfoCount() > 0) {
                // Force to update movie information
                if (forceInfoUpdate) {
                    notifyProviderFetchOnlineInfo(movie);
                }
            }
            // Movie without information
            else {
                notifyProviderFetchOnlineInfo(movie);
            }
        }
    }

    /**
     * Notify Movie File Information Provider to analyze movie files and extract metadata information
     *
     * @param files            Movie Files
     * @param excludeProviders Excluded Providers (which may already prove failed)
     */
    private void notifyProviderFetchFileInfo(List<File> files, List<String> excludeProviders) {
        if (!fileProviderServices.isEmpty()) {
            // Find the provider based on configuration order and use it to fetch movie information
            for (String preferredProvider : movieFileInfoProviders) {
                // If the provider on the exclude list (which usually already failed), continue to next
                if (excludeProviders != null && excludeProviders.contains(preferredProvider)) {
                    continue;
                }
                // Loop the current provider see whether preferred provider is available
                for (MovieFileInfoProviderService provider : fileProviderServices) {
                    if (preferredProvider.equalsIgnoreCase(provider.getProviderName())) {
                        logger.debug("Try to get movie file information with <{}> provider.", provider.getProviderName());
                        // Try to get movie file information from the provider (this tends to work background)
                        provider.getMovieFileInformation(files, excludeProviders);
                        // Once matched, stop looping
                        return;
                    }
                }
            }
        }

        // If nothing matched
        logger.error("No valid Movie File Information Provider presents, please check configuration and module status.");
    }

    /**
     * Notify the provider to fetch movie online information
     *
     * @param movie Movie
     */
    @SuppressWarnings({"LoopStatementThatDoesntLoop"})
    private void notifyProviderFetchOnlineInfo(Movie movie) {
        // Provider matched
        boolean hasMatched = false;

        if (!onlineProviderServices.isEmpty()) {
            String movieName = null;
            int movieYear = -1;
            // All the movie files should have the same movie name and year, we just use the first available one
            for (MovieFile movieFile : movie.getFiles()) {
                if (StringUtils.isNotBlank(movieFile.getMovieName())) {
                    movieName = movieFile.getMovieName();
                    movieYear = movieFile.getYear();
                }
                break;
            }

            // Find providers based on configuration and use each provider to fetch movie information
            for (String preferredProvider : movieOnlineInfoProviders) {
                // Loop the current provider see whether preferred provider is available
                for (MovieInfoProviderService provider : onlineProviderServices) {
                    if (preferredProvider.equalsIgnoreCase(provider.getProviderName())) {
                        // Mark as matched
                        hasMatched = true;

                        if (StringUtils.isNotBlank(movieName)) {
                            logger.debug("Try to get movie <{}> information with <{}> provider.", movieName, provider.getProviderName());
                            // Try to get movie information from the internet provider (this tends to work background)
                            if (movieYear > 0) {
                                provider.getMovieInformationByName(movie.getId(), movieName, movieYear, 1, this.posterCount, this.backdropCount);
                            } else {
                                provider.getMovieInformationByName(movie.getId(), movieName, 1, this.posterCount, this.backdropCount);
                            }
                        }
                    }
                }
            }
        }

        if (!hasMatched) {
            logger.error("No valid Movie Online Information Provider presents, please check configuration and module status.");
        }
    }

    /**
     * Notify the provider to fetch movie image
     *
     * @param imageId          Image ID
     * @param url              URL
     * @param originalProvider Original Provider
     */
    private void notifyProviderFetchImage(String imageId, String url, String originalProvider) {
        if (!onlineProviderServices.isEmpty()) {
            // Loop the current provider see whether original provider is available
            for (MovieInfoProviderService provider : onlineProviderServices) {
                if (originalProvider.equalsIgnoreCase(provider.getProviderName())) {
                    logger.debug("Try to get movie image <{}> with <{}> provider.", imageId, provider.getProviderName());
                    // Try to get movie image from the internet provider (this tends to work background)
                    provider.getMovieImageByUrl(imageId, url);
                    // Once matched, stop
                    return;
                }
            }

            // If original provider not presented, find the provider based on configuration order and use it to fetch image
            for (String preferredProvider : movieOnlineInfoProviders) {
                // Loop the current provider see whether preferred provider is available
                for (MovieInfoProviderService provider : onlineProviderServices) {
                    if (preferredProvider.equalsIgnoreCase(provider.getProviderName())) {
                        logger.debug("Try to get movie image <{}> with <{}> provider.", imageId, provider.getProviderName());
                        // Try to get movie image from the internet provider (this tends to work background)
                        provider.getMovieImageByUrl(imageId, url);
                        // Once matched, stop looping
                        return;
                    }
                }
            }
        }

        logger.error("No valid Movie Online Information Provider presents, please check configuration and module status.");
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
     * @param onlineProviderServices Movie Information Provider Services
     */
    @SuppressWarnings("unused")
    public void setOnlineProviderServices(List<MovieInfoProviderService> onlineProviderServices) {
        this.onlineProviderServices = onlineProviderServices;
    }

    /**
     * Inject Movie File Information Provider Services
     *
     * @param fileProviderServices Movie File Information Provider Services
     */
    @SuppressWarnings("unused")
    public void setFileProviderServices(List<MovieFileInfoProviderService> fileProviderServices) {
        this.fileProviderServices = fileProviderServices;
    }
}

