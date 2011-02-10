package org.chii2.medialibrary.provider.tmdb;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.chii2.medialibrary.api.provider.MovieInfoProviderService;
import org.chii2.util.ConfigUtils;
import org.chii2.medialibrary.provider.tmdb.handler.ImageResponseHandler;
import org.chii2.medialibrary.provider.tmdb.handler.MovieResponseHandler;
import org.chii2.medialibrary.provider.tmdb.parser.Parser;
import org.chii2.medialibrary.provider.tmdb.parser.YamlParser;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * TMDb Movie Information Provider Service (www.themoviedb.org)
 */
public class MovieInfoProviderServiceImpl implements MovieInfoProviderService {

    // Operation type - Search first movie by movie name
    public final static String SEARCH_FIRST_BY_NAME = "search_first_by_name";
    // Operation type - Search all movies by movie name
    public final static String SEARCH_ALL_BY_NAME = "search_all_by_name";
    // TMDb api url
    private final static String REMOTE_HOST = "api.themoviedb.org";
    // Port
    private final static int REMOTE_PORT = 80;
    // API Key
    private final static String API_KEY = "e032ad8a5fa69bb92c91ebb7b7e20c15";
    // Configuration FIle
    private final static String CONFIG_FILE = "org.chii2.medialibrary.provider.tmdb";
    // Configuration for the API Parser type
    private final static String CONFIG_PARSER = "tmdb.api.parser";
    // Movie Search API URL
    private String MOVIE_SEARCH_API = "http://api.themoviedb.org/2.1/Movie.search/en/yaml/";
    // Http Client
    private ClientBootstrap bootstrap;
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin service
    private EventAdmin eventAdmin;
    // HTTP Api Parser
    private Parser parser;
    // Injected MovieFactory
    private MovieFactory movieFactory;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");

    /**
     * Constructor
     */
    public MovieInfoProviderServiceImpl() {
        // Create bootstrap
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
    }

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
            // Load TMDb api type configuration
            String parserType = ConfigUtils.loadConfiguration(props, CONFIG_PARSER);
            if (StringUtils.isNotBlank(parserType)) {
                if ("yaml".equalsIgnoreCase(parserType)) {
                    this.parser = new YamlParser(movieFactory);
                    MOVIE_SEARCH_API = "http://api.themoviedb.org/2.1/Movie.search/en/yaml/";
                    logger.debug("TMDb Provider configuration <{}> loaded.", CONFIG_PARSER);
                } else {
                    // Use YAML as default configuration
                    this.parser = new YamlParser(movieFactory);
                    MOVIE_SEARCH_API = "http://api.themoviedb.org/2.1/Movie.search/en/yaml/";
                    logger.error("TMDb Provider configuration <{}> is not valid.", CONFIG_PARSER);
                }
            } else {
                logger.error("TMDb Provider configuration <{}> is not valid.", CONFIG_PARSER);
            }
        }
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        bootstrap.releaseExternalResources();
        logger.debug("Chii2 Media Library TMDb Provider destroy.");
    }

    @Override
    public String getProviderName() {
        return "TMDb";
    }

    @Override
    public void getMovieInformationByName(String movieId, String movieName, List<String> excludeProviders) {
        getMovieInformationByName(movieId, movieName, null, excludeProviders);
    }

    @Override
    public void getMovieInformationByName(String movieId, String movieName, String movieYear, List<String> excludeProviders) {
        if (StringUtils.isNotBlank(movieId) && StringUtils.isNotBlank(movieName)) {
            String encodedMovieName = null;
            String url;
            try {
                encodedMovieName = URLEncoder.encode(movieName, CharsetUtil.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                // Should not happens
                logger.error("Chii2 Media Library TMDb Provider url parse error: {}.", e.getMessage());
            }
            if (encodedMovieName != null) {
                if (StringUtils.isBlank(movieYear)) {
                    url = MOVIE_SEARCH_API + API_KEY + "/" + encodedMovieName;
                } else {
                    url = MOVIE_SEARCH_API + API_KEY + "/" + encodedMovieName + "+" + movieYear;
                }
                this.postMovieRequest(url, SEARCH_FIRST_BY_NAME, movieId, movieName, movieYear, excludeProviders);
            }
        }
    }

    @Override
    public void getAllMovieInformationByName(String movieId, String movieName, List<String> excludeProviders) {
        getAllMovieInformationByName(movieId, movieName, null, excludeProviders);
    }

    @Override
    public void getAllMovieInformationByName(String movieId, String movieName, String movieYear, List<String> excludeProviders) {
        if (StringUtils.isNotBlank(movieId) && StringUtils.isNotBlank(movieName)) {
            String encodedMovieName = null;
            String url;
            try {
                encodedMovieName = URLEncoder.encode(movieName, CharsetUtil.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                // Should not happens
                logger.error("Chii2 Media Library TMDb Provider url parse error: {}.", e.getMessage());
            }
            if (encodedMovieName != null) {
                if (StringUtils.isBlank(movieYear)) {
                    url = MOVIE_SEARCH_API + API_KEY + "/" + encodedMovieName;
                } else {
                    url = MOVIE_SEARCH_API + API_KEY + "/" + encodedMovieName + "+" + movieYear;
                }
                this.postMovieRequest(url, SEARCH_ALL_BY_NAME, movieId, movieName, movieYear, excludeProviders);
            }
        }
    }

    @Override
    public void getMovieImageByUrl(String imageId, String url) {
        postImageRequest(url, imageId);
    }

    /**
     * Post HTTP request to remote provider in a asynchronous way.
     * The result will be handled in image response handler.
     *
     * @param url     URL of the image
     * @param imageId Image ID
     */
    private void postImageRequest(final String url, final String imageId) {
        // Prepare the HTTP request.
        final HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
        request.setHeader(HttpHeaders.Names.HOST, REMOTE_HOST);
        request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

        // Set up the event pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                // Create a default pipeline implementation.
                ChannelPipeline pipeline = Channels.pipeline();
                // Client codec
                pipeline.addLast("codec", new HttpClientCodec());
                // Content decompression
                pipeline.addLast("inflater", new HttpContentDecompressor());
                // HttpChunks, because cached for images, make it 4Mb
                pipeline.addLast("aggregator", new HttpChunkAggregator(4 * 1048576));
                // ResponseHandler
                pipeline.addLast("handler", new ImageResponseHandler(imageId, eventAdmin));
                return pipeline;
            }
        });

        // Start the connection attempt.
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(REMOTE_HOST, REMOTE_PORT));

        // If connected, send the request
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    logger.debug("Chii2 Media Library TMDb Provider send the image <{} {}> request to remote host.", imageId, url);
                    future.getChannel().write(request);
                } else {
                    logger.error("Chii2 Media Library TMDb Provider failed to connect to remote host <{}>.", url);
                }
            }
        });
    }

    /**
     * Post HTTP request to remote provider in a asynchronous way.
     * The result will be handled in movie response handler.
     *
     * @param url              HTTP URL
     * @param type             Request type
     * @param movieId          Movie ID
     * @param movieName        Movie Name
     * @param movieYear        Movie Year
     * @param excludeProviders Excluded providers
     */
    private void postMovieRequest(final String url, final String type, final String movieId, final String movieName, final String movieYear, final List<String> excludeProviders) {
        // Prepare the HTTP request.
        final HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
        request.setHeader(HttpHeaders.Names.HOST, REMOTE_HOST);
        request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

        // Set up the event pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                // Create a default pipeline implementation.
                ChannelPipeline pipeline = Channels.pipeline();
                // Client codec
                pipeline.addLast("codec", new HttpClientCodec());
                // Content decompression
                pipeline.addLast("inflater", new HttpContentDecompressor());
                // HttpChunks 1M
                pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
                // ResponseHandler
                pipeline.addLast("handler", new MovieResponseHandler(type, movieId, movieName, movieYear, excludeProviders, eventAdmin, parser));
                return pipeline;
            }
        });

        // Start the connection attempt.
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(REMOTE_HOST, REMOTE_PORT));

        // If connected, send the request
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    logger.debug("Chii2 Media Library TMDb Provider send the movie <{} {}> request to remote host.", movieId, movieName);
                    future.getChannel().write(request);
                } else {
                    logger.error("Chii2 Media Library TMDb Provider failed to connect to remote host <{}>.", url);
                }
            }
        });
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
