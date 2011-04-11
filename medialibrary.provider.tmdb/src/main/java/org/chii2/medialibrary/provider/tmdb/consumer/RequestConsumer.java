package org.chii2.medialibrary.provider.tmdb.consumer;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.chii2.medialibrary.api.provider.MovieInfoProviderService;
import org.chii2.medialibrary.provider.tmdb.handler.ImageResponseHandler;
import org.chii2.medialibrary.provider.tmdb.handler.MovieResponseHandler;
import org.chii2.medialibrary.provider.tmdb.parser.JsonParser;
import org.chii2.medialibrary.provider.tmdb.parser.Parser;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

/**
 * Request Consumer
 */
public class RequestConsumer implements Runnable {
    // Request queue
    protected BlockingQueue<Map<String, Object>> queue;
    // Movie Search API URL
    private final static String MOVIE_SEARCH_API = "http://api.themoviedb.org/2.1/Movie.search/en/json/";
    // API Key
    private final static String API_KEY = "e032ad8a5fa69bb92c91ebb7b7e20c15";
    // Reusable Http Client
    private AsyncHttpClient client;
    // Reusable Parser
    private Parser parser;
    // EventAdmin service
    private EventAdmin eventAdmin;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");

    /**
     * Constructor
     *
     * @param queue        Request Queue
     * @param eventAdmin   EventAdmin
     * @param movieFactory Movie Factory
     * @param config       Http Client Config
     */
    public RequestConsumer(BlockingQueue<Map<String, Object>> queue, EventAdmin eventAdmin, MovieFactory movieFactory, AsyncHttpClientConfig config) {
        this.queue = queue;
        this.eventAdmin = eventAdmin;
        this.parser = new JsonParser(movieFactory);
        this.client = new AsyncHttpClient(config);
    }

    @SuppressWarnings({"InfiniteLoopStatement", "ConstantConditions"})
    @Override
    public void run() {
        try {
            while (true) {
                // Read request
                Map<String, Object> request = queue.take();
                @SuppressWarnings("unchecked")
                String movieId = (String) request.get(MovieInfoProviderService.MOVIE_ID_PROPERTY);
                @SuppressWarnings("unchecked")
                String movieName = (String) request.get(MovieInfoProviderService.MOVIE_NAME_PROPERTY);
                @SuppressWarnings("unchecked")
                int movieYear = (Integer) request.get(MovieInfoProviderService.MOVIE_YEAR_PROPERTY);
                @SuppressWarnings("unchecked")
                int count = (Integer) request.get(MovieInfoProviderService.RESULT_COUNT_PROPERTY);
                @SuppressWarnings("unchecked")
                int posterCount = (Integer) request.get(MovieInfoProviderService.POSTER_COUNT_PROPERTY);
                @SuppressWarnings("unchecked")
                int backdropCount = (Integer) request.get(MovieInfoProviderService.BACKDROP_COUNT_PROPERTY);

                if (StringUtils.isBlank(movieId) || StringUtils.isBlank(movieName)) {
                    logger.warn("Request movie information with empty movie id or movie name.");
                    continue;
                }

                try {
                    // Escape movie name
                    String encodedMovieName = URLEncoder.encode(movieName, "utf-8");
                    // Prepare url
                    String url;
                    if (movieYear > 1000) {
                        url = MOVIE_SEARCH_API + API_KEY + "/" + encodedMovieName + "+" + movieYear;
                    } else {
                        url = MOVIE_SEARCH_API + API_KEY + "/" + encodedMovieName;
                    }
                    // Send request
                    Future<List<MovieInfo>> future = this.client.prepareGet(url).execute(new MovieResponseHandler(this.parser, count, posterCount, backdropCount));
                    List<MovieInfo> infoList = future.get();
                    // Fetch images
                    for (MovieInfo movieInfo : infoList) {
                        List<Future<byte[]>> futureList = new LinkedList<Future<byte[]>>();
                        for (MovieImage movieImage : movieInfo.getImages()) {
                            Future<byte[]> imageFuture = this.client.prepareGet(movieImage.getUrl()).execute(new ImageResponseHandler());
                            futureList.add(imageFuture);
                        }
                        for (int i = 0; i < movieInfo.getImagesCount(); i++) {
                            movieInfo.getImages().get(i).setImage(futureList.get(i).get());
                        }
                    }
                    // Send event
                    postMovieInfoProvidedEvent(movieId, infoList);
                } catch (Exception e) {
                    logger.warn("Provider consumer with error: {}.", e.getMessage());
                    // Send fail event
                    postMovieInfoFailedEvent(movieId);
                }
            }
        } catch (InterruptedException e) {
            logger.error("Provider consumer has been interrupted with error: {}.", e.getMessage());
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Send a movie information provided event
     *
     * @param movieId   Movie ID
     * @param movieInfo Movie Information from response
     */
    private void postMovieInfoProvidedEvent(String movieId, List<MovieInfo> movieInfo) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(MovieInfoProviderService.MOVIE_ID_PROPERTY, movieId);
        properties.put(MovieInfoProviderService.MOVIE_INFO_PROPERTY, movieInfo);
        // Send a event
        Event event = new Event(MovieInfoProviderService.MOVIE_INFO_PROVIDED_TOPIC, properties);
        logger.debug("Send a movie <{}> information provided event.", movieId);
        eventAdmin.postEvent(event);
    }

    /**
     * Send a movie information failed event
     *
     * @param movieId Movie ID
     */
    private void postMovieInfoFailedEvent(String movieId) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(MovieInfoProviderService.MOVIE_ID_PROPERTY, movieId);
        // Send a event
        Event event = new Event(MovieInfoProviderService.MOVIE_INFO_FAILED_TOPIC, properties);
        logger.debug("Send a movie <{}> information failed event.", movieId);
        eventAdmin.postEvent(event);
    }
}
