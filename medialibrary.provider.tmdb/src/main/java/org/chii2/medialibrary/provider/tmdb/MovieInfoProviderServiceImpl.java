package org.chii2.medialibrary.provider.tmdb;

import com.ning.http.client.*;
import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.chii2.medialibrary.api.provider.MovieInfoProviderService;
import org.chii2.medialibrary.provider.tmdb.handler.ImageResponseHandler;
import org.chii2.medialibrary.provider.tmdb.handler.MovieResponseHandler;
import org.chii2.medialibrary.provider.tmdb.parser.JsonParser;
import org.chii2.medialibrary.provider.tmdb.parser.Parser;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Dictionary;

/**
 * TMDb Movie Information Provider Service (www.themoviedb.org)
 */
public class MovieInfoProviderServiceImpl implements MovieInfoProviderService {
    // Movie Search API URL
    private final static String MOVIE_SEARCH_API = "http://api.themoviedb.org/2.1/Movie.search/en/json/";
    // API Key
    private final static String API_KEY = "e032ad8a5fa69bb92c91ebb7b7e20c15";
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
    // Reusable Http Client
    private AsyncHttpClient client;
    // Reusable Parser
    private Parser parser;
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

        // init Parser
        this.parser = new JsonParser(this.movieFactory);

        // Init Http Client
        AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder()
                .setCompressionEnabled(this.compressionEnabled)
                .setAllowPoolingConnection(this.allowPoolingConn)
                .setMaximumConnectionsPerHost(this.maximumConnectionsPerHost)
                .setMaximumConnectionsTotal(this.maximumConnectionsTotal)
                .setIdleConnectionInPoolTimeoutInMs(this.idleConnectionInPoolTimeout)
                .setRequestTimeoutInMs(this.requestTimeout)
                .build();
        this.client = new AsyncHttpClient(config);
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library TMDb Provider destroy.");
        if (client != null) {
            client.close();
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
        // Forge http request
        try {
            // Escape movie name
            String encodedMovieName = URLEncoder.encode(movieName, "utf-8");
            // Prepare url
            String url;
            if (movieYear > 0) {
                url = MOVIE_SEARCH_API + API_KEY + "/" + encodedMovieName;
            } else {
                url = MOVIE_SEARCH_API + API_KEY + "/" + encodedMovieName + "+" + movieYear;
            }
            // Send request
            this.client.prepareGet(url).execute(new MovieResponseHandler(movieId, this.eventAdmin, this.parser, count, posterCount, backdropCount));
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding movie name with error: {}.", e.getMessage());
        } catch (IOException e) {
            logger.error("Http client transfer with error: {}.", e.getMessage());
        }
    }

    @Override
    public void getMovieImageByUrl(String imageId, String url) {
        if (StringUtils.isBlank(imageId) || StringUtils.isBlank(url)) {
            logger.warn("Request movie image with empty image id or image url.");
            return;
        }
        // Forge http request
        try {
            // Send request
            this.client.prepareGet(url).execute(new ImageResponseHandler(imageId, this.eventAdmin));
        } catch (IOException e) {
            logger.error("Http client transfer with error: {}.", e.getMessage());
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
