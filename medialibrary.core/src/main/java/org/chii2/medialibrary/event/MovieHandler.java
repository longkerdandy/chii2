package org.chii2.medialibrary.event;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.medialibrary.api.file.FileService;
import org.chii2.medialibrary.api.persistence.PersistenceService;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
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

import java.io.IOException;
import java.nio.file.Path;
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
    // Force to refresh/update movie's information
    private boolean forceInfoUpdate = true;
    // Poster fetch count
    private int posterCount = 3;
    // Backdrop fetch count
    private int backdropCount = 3;
    //Configuration FIle
    private final static String CONFIG_FILE = "org.chii2.medialibrary.core";
    // Force to refresh/update movie's information Config Key
    private final static String MOVIE_FORCE_INFORMATION_UPDATE = "movie.force.update";
    // Poster Fetch Count Config Key
    private final static String MOVIE_POSTER_COUNT = "movie.poster.count";
    // Backdrop Fetch Count Config Key
    private final static String MOVIE_BACKDROP_COUNT = "movie.backdrop.count";
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.event");

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
            logger.error("MovieHandler fail to load configuration with exception: {}.", ExceptionUtils.getMessage(e));
        }
        // Load each configuration
        if (props == null || props.isEmpty()) {
            logger.error("MovieHandler load configuration <{}> with error.", CONFIG_FILE);
        } else {
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
    @SuppressWarnings("unchecked")
    public void handleEvent(Event event) {
        // Movie Scan Event
        if (FileService.MOVIE_SCAN_PROVIDED_TOPIC.equals(event.getTopic())) {
            List<Path> files = (List<Path>) event.getProperty(FileService.SCAN_PATH_PROPERTY);
            logger.debug("Receive a movie scan event with {} records.", files.size());
            this.postMovieFileInfoRequestEvent(files);
        }
        // Movie Watch Create Event
        else if (FileService.MOVIE_WATCH_CREATE_TOPIC.equals(event.getTopic())) {
            Path path = (Path) event.getProperty(FileService.WATCH_PATH_PROPERTY);
            List<Path> files = new ArrayList<>();
            files.add(path);
            logger.debug("Receive a movie watch create event for: {}.", path);
            this.postMovieFileInfoRequestEvent(files);
        }
        // Movie Watch Delete Event
        else if (FileService.MOVIE_WATCH_DELETE_TOPIC.equals(event.getTopic())) {
            Path path = (Path) event.getProperty(FileService.WATCH_PATH_PROPERTY);
            logger.debug("Receive a movie watch delete event for: {}.", path);
            this.persistenceService.deleteMovie(path.toString());
        }
        // Movie Watch Modify Event
        else if (FileService.MOVIE_WATCH_MODIFY_TOPIC.equals(event.getTopic())) {
            Path path = (Path) event.getProperty(FileService.WATCH_PATH_PROPERTY);
            List<Path> files = new ArrayList<>();
            files.add(path);
            logger.debug("Receive a movie watch modify event for: {}.", path);
            this.postMovieFileInfoRequestEvent(files);
        }
        // Movie File Information Provided Event
        else if (MovieFileInfoProviderService.MOVIE_FILE_INFO_PROVIDED_TOPIC.equals(event.getTopic())) {
            Path path = (Path) event.getProperty(MovieFileInfoProviderService.MOVIE_PATH_PROPERTY);
            MovieFile movieFile = (MovieFile) event.getProperty(MovieFileInfoProviderService.MOVIE_FILE_INFO_PROPERTY);
            logger.debug("Receive a movie file information provided event for: {}.", path);
            // Synchronize
            this.persistenceService.synchronizeMovie(movieFile);
            // Get Movie (since it is lazy loading)
            Movie movie = this.persistenceService.getMovieByMovieFile(movieFile);
            // Request movie information
            if (forceInfoUpdate || movie.getInfoCount() == 0) {
                this.postMovieInfoRequestEvent(movie.getId(), movieFile.getMovieName(), movieFile.getYear(), 1, this.posterCount, this.backdropCount);
            }
        }
        // Movie File Information Failed Event
        else if (MovieFileInfoProviderService.MOVIE_FILE_INFO_FAILED_TOPIC.equals(event.getTopic())) {
            Path path = (Path) event.getProperty(MovieFileInfoProviderService.MOVIE_PATH_PROPERTY);
            logger.debug("Receive a movie file information failed event for: {}.", path);
        }
        // Movie Information Provided Event
        else if (MovieInfoProviderService.MOVIE_INFO_PROVIDED_TOPIC.equals(event.getTopic())) {
            String movieId = (String) event.getProperty(MovieInfoProviderService.MOVIE_ID_PROPERTY);
            List<MovieInfo> info = (List<MovieInfo>) event.getProperty(MovieInfoProviderService.MOVIE_INFO_PROPERTY);
            logger.debug("Receive a movie information provided event with {} information.", info.size());
            // Synchronize
            this.persistenceService.synchronizeMovie(movieId, info);
        }
        // Movie Information provided Failed Event
        else if (MovieInfoProviderService.MOVIE_INFO_FAILED_TOPIC.equals(event.getTopic())) {
            String movieId = (String) event.getProperty(MovieInfoProviderService.MOVIE_ID_PROPERTY);
            logger.debug("Receive a movie information failed event for: {}.", movieId);
        }
    }

    /**
     * Send a movie file information request event
     *
     * @param files Files to be parsed
     */
    private void postMovieFileInfoRequestEvent(List<Path> files) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(MovieFileInfoProviderService.MOVIE_PATH_PROPERTY, files);
        // Send a event
        Event event = new Event(MovieFileInfoProviderService.MOVIE_FILE_INFO_REQUEST_TOPIC, properties);
        logger.debug("Send a movie file information request event with {} files.", files.size());
        this.eventAdmin.postEvent(event);
    }

    /**
     * Send a movie information request event
     *
     * @param movieId       Movie Id
     * @param movieName     Movie Name (Guessed Name)
     * @param movieYear     Movie Year (Guessed Year)
     * @param resultCount   Max result count
     * @param posterCount   Result poster count
     * @param backdropCount Result backdrop count
     */
    private void postMovieInfoRequestEvent(String movieId, String movieName, int movieYear, int resultCount, int posterCount, int backdropCount) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(MovieInfoProviderService.MOVIE_ID_PROPERTY, movieId);
        properties.put(MovieInfoProviderService.MOVIE_NAME_PROPERTY, movieName);
        properties.put(MovieInfoProviderService.MOVIE_YEAR_PROPERTY, movieYear);
        properties.put(MovieInfoProviderService.RESULT_COUNT_PROPERTY, resultCount);
        properties.put(MovieInfoProviderService.POSTER_COUNT_PROPERTY, posterCount);
        properties.put(MovieInfoProviderService.BACKDROP_COUNT_PROPERTY, backdropCount);
        // Send a event
        Event event = new Event(MovieInfoProviderService.MOVIE_INFO_REQUEST_TOPIC, properties);
        logger.debug("Send a movie <{}> information request event.", movieId);
        this.eventAdmin.postEvent(event);
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
}

