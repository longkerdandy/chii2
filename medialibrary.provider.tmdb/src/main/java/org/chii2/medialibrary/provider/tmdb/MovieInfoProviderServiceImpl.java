package org.chii2.medialibrary.provider.tmdb;

import com.ning.http.client.*;
import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.chii2.medialibrary.api.provider.MovieInfoProviderService;
import org.chii2.medialibrary.provider.tmdb.consumer.RequestConsumer;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TMDb Movie Information Provider Service (www.themoviedb.org)
 */
public class MovieInfoProviderServiceImpl implements MovieInfoProviderService, EventHandler {
    // Request queue
    protected BlockingQueue<Map<String, Object>> queue;
    // Configuration FIle
    private final static String CONFIG_FILE = "org.chii2.medialibrary.provider.tmdb";
    // Configuration for the http client compression
    private final static String CONFIG_ENABLE_COMPRESSION = "tmdb.client.compression";
    // Configuration for the http client connection pooling
    private final static String CONFIG_ENABLE_POOLING_CONN = "tmdb.client.conn.pooling";
    // Configuration for the http client max connections per host
    private final static String CONFIG_MAX_CONN_PER_HOST = "tmdb.client.conn.max.host";
    // Configuration for the http client max connections total
    private final static String CONFIG_MAX_CONN_PER_TOTAL = "tmdb.client.conn.max.total";
    // Configuration for the http client idle connection in pool timeout
    private final static String CONFIG_IDLE_CONN_POOL_TIMEOUT = "tmdb.client.conn.pooling.idle.timeout";
    // Configuration for the http client request timeout
    private final static String CONFIG_REQUEST_TIMEOUT = "tmdb.client.request.timeout";
    // Http client compression
    private boolean compressionEnabled = true;
    // Http client connection pooling
    private boolean allowPoolingConn = true;
    // Http client max connections per host
    private int maximumConnectionsPerHost = 10;
    // Http client max connections total
    private int maximumConnectionsTotal = 100;
    // Http client idle connection in pool timeout in ms
    private int idleConnectionInPoolTimeout = 100;
    // Http client request timeout in ms
    private int requestTimeout = 30000;
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin service
    private EventAdmin eventAdmin;
    // Injected MovieFactory
    private MovieFactory movieFactory;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");


    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library TMDb Provider init.");
        Dictionary props = null;
        // Read properties from ConfigAdmin Service
        try {
            Configuration config = configAdmin.getConfiguration(CONFIG_FILE);
            props = config.getProperties();
        } catch (IOException e) {
            logger.error("TMDb Provider fail to load configuration with exception: {}.", e.getMessage());
        }
        // Load each configuration
        if (props == null || props.isEmpty()) {
            logger.error("TMDb Provider load configuration <{}> with error.", CONFIG_FILE);
        } else {
            // Load http client compression configuration
            String compressionEnabled = ConfigUtils.loadConfiguration(props, CONFIG_ENABLE_COMPRESSION);
            if (StringUtils.isNotBlank(compressionEnabled)) {
                this.compressionEnabled = Boolean.parseBoolean(compressionEnabled);
                logger.debug("TMDb Provider configuration <{}> loaded.", CONFIG_ENABLE_COMPRESSION);
            } else {
                logger.error("TMDb Provider configuration <{}> is not valid.", CONFIG_ENABLE_COMPRESSION);
            }
            // Load http client connection pooling configuration
            String allowPoolingConn = ConfigUtils.loadConfiguration(props, CONFIG_ENABLE_POOLING_CONN);
            if (StringUtils.isNotBlank(allowPoolingConn)) {
                this.allowPoolingConn = Boolean.parseBoolean(allowPoolingConn);
                logger.debug("TMDb Provider configuration <{}> loaded.", CONFIG_ENABLE_POOLING_CONN);
            } else {
                logger.error("TMDb Provider configuration <{}> is not valid.", CONFIG_ENABLE_POOLING_CONN);
            }
            // Load http client max connections per host configuration
            String maximumConnectionsPerHost = ConfigUtils.loadConfiguration(props, CONFIG_MAX_CONN_PER_HOST);
            if (StringUtils.isNotBlank(maximumConnectionsPerHost)) {
                this.maximumConnectionsPerHost = Integer.parseInt(maximumConnectionsPerHost);
                logger.debug("TMDb Provider configuration <{}> loaded.", CONFIG_MAX_CONN_PER_HOST);
            } else {
                logger.error("TMDb Provider configuration <{}> is not valid.", CONFIG_MAX_CONN_PER_HOST);
            }
            // Load http client max connections total configuration
            String maximumConnectionsTotal = ConfigUtils.loadConfiguration(props, CONFIG_MAX_CONN_PER_TOTAL);
            if (StringUtils.isNotBlank(maximumConnectionsTotal)) {
                this.maximumConnectionsTotal = Integer.parseInt(maximumConnectionsTotal);
                logger.debug("TMDb Provider configuration <{}> loaded.", CONFIG_MAX_CONN_PER_TOTAL);
            } else {
                logger.error("TMDb Provider configuration <{}> is not valid.", CONFIG_MAX_CONN_PER_TOTAL);
            }
            // Load http client idle connection in pool timeout configuration
            String idleConnectionInPoolTimeout = ConfigUtils.loadConfiguration(props, CONFIG_IDLE_CONN_POOL_TIMEOUT);
            if (StringUtils.isNotBlank(idleConnectionInPoolTimeout)) {
                this.idleConnectionInPoolTimeout = Integer.parseInt(idleConnectionInPoolTimeout);
                logger.debug("TMDb Provider configuration <{}> loaded.", CONFIG_IDLE_CONN_POOL_TIMEOUT);
            } else {
                logger.error("TMDb Provider configuration <{}> is not valid.", CONFIG_IDLE_CONN_POOL_TIMEOUT);
            }
            // Load http client request timeout configuration
            String requestTimeout = ConfigUtils.loadConfiguration(props, CONFIG_REQUEST_TIMEOUT);
            if (StringUtils.isNotBlank(requestTimeout)) {
                this.requestTimeout = Integer.parseInt(requestTimeout);
                logger.debug("TMDb Provider configuration <{}> loaded.", CONFIG_REQUEST_TIMEOUT);
            } else {
                logger.error("TMDb Provider configuration <{}> is not valid.", CONFIG_REQUEST_TIMEOUT);
            }
        }

        // Init Http Client
        AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder()
                .setCompressionEnabled(this.compressionEnabled)
                .setAllowPoolingConnection(this.allowPoolingConn)
                .setMaximumConnectionsPerHost(this.maximumConnectionsPerHost)
                .setMaximumConnectionsTotal(this.maximumConnectionsTotal)
                .setIdleConnectionInPoolTimeoutInMs(this.idleConnectionInPoolTimeout)
                .setRequestTimeoutInMs(this.requestTimeout)
                .build();

        // Init queue
        this.queue = new LinkedBlockingQueue<Map<String, Object>>();

        // Start Request Consumer
        new Thread(new RequestConsumer(queue, eventAdmin, movieFactory, config)).start();
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library TMDb Provider destroy.");
    }

    @Override
    public void handleEvent(Event event) {
        if (MovieInfoProviderService.MOVIE_INFO_REQUEST_TOPIC.equals(event.getTopic())) {
            Map<String, Object> properties = new Hashtable<String, Object>();
            properties.put(MovieInfoProviderService.MOVIE_ID_PROPERTY, event.getProperty(MovieInfoProviderService.MOVIE_ID_PROPERTY));
            properties.put(MovieInfoProviderService.MOVIE_NAME_PROPERTY, event.getProperty(MovieInfoProviderService.MOVIE_NAME_PROPERTY));
            properties.put(MovieInfoProviderService.MOVIE_YEAR_PROPERTY, event.getProperty(MovieInfoProviderService.MOVIE_YEAR_PROPERTY));
            properties.put(MovieInfoProviderService.RESULT_COUNT_PROPERTY, event.getProperty(MovieInfoProviderService.RESULT_COUNT_PROPERTY));
            properties.put(MovieInfoProviderService.POSTER_COUNT_PROPERTY, event.getProperty(MovieInfoProviderService.POSTER_COUNT_PROPERTY));
            properties.put(MovieInfoProviderService.BACKDROP_COUNT_PROPERTY, event.getProperty(MovieInfoProviderService.BACKDROP_COUNT_PROPERTY));
            logger.debug("Receive a {} movie information request event.", event.getProperty(MovieInfoProviderService.MOVIE_ID_PROPERTY));
            // Add request to queue
            try {
                this.queue.put(properties);
            } catch (InterruptedException e) {
                logger.error("Provider producer has been interrupted with error: {}.", e.getMessage());
            }
        }
    }

    @Override
    public String getProviderName() {
        return "TMDb";
    }

    @Override
    public void getMovieInformationByName(String movieId, String movieName, int count, int posterCount, int backdropCount) {
        getMovieInformationByName(movieId, movieName, 0, count, posterCount, backdropCount);
    }

    @Override
    public void getMovieInformationByName(String movieId, String movieName, int movieYear, int count, int posterCount, int backdropCount) {
        if (StringUtils.isBlank(movieId) || StringUtils.isBlank(movieName)) {
            logger.warn("Request movie information with empty movie id or movie name.");
            return;
        }

        Map<String, Object> properties = new Hashtable<String, Object>();
        properties.put(MovieInfoProviderService.MOVIE_ID_PROPERTY, movieId);
        properties.put(MovieInfoProviderService.MOVIE_NAME_PROPERTY, movieName);
        properties.put(MovieInfoProviderService.MOVIE_YEAR_PROPERTY, movieYear);
        properties.put(MovieInfoProviderService.RESULT_COUNT_PROPERTY, count);
        properties.put(MovieInfoProviderService.POSTER_COUNT_PROPERTY, posterCount);
        properties.put(MovieInfoProviderService.BACKDROP_COUNT_PROPERTY, backdropCount);
        // Add request to queue
        try {
            this.queue.put(properties);
        } catch (InterruptedException e) {
            logger.error("Provider producer has been interrupted with error: {}.", e.getMessage());
        }
    }

    /**
     * Inject ConfigurationAdmin service
     *
     * @param configAdmin ConfigurationAdmin service
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
     * Inject Movie Factory
     *
     * @param movieFactory Movie Factory
     */
    @SuppressWarnings("unused")
    public void setMovieFactory(MovieFactory movieFactory) {
        this.movieFactory = movieFactory;
    }
}
