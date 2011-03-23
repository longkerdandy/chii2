package org.chii2.medialibrary.provider.tmdb.handler;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.api.provider.MovieInfoProviderService;
import org.chii2.medialibrary.provider.tmdb.parser.ParseException;
import org.chii2.medialibrary.provider.tmdb.parser.Parser;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Movie Response Handler, handle movie request replied by TMDb
 */
public class MovieResponseHandler extends AsyncCompletionHandler<Integer> {
    // Movie ID
    private String movieId;
    // Parser
    private Parser parser;
    // Result count
    private int count;
    // Result poster count
    private int posterCount;
    // Result backdrop count
    private int backdropCount;
    // EventAdmin
    private EventAdmin eventAdmin;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");

    /**
     * Constructor
     *
     * @param movieId          Movie ID
     * @param eventAdmin       EventAdmin
     * @param parser           Parser
     * @param count            Result Count
     * @param posterCount      Result poster count
     * @param backdropCount    Result backdrop count
     */
    public MovieResponseHandler(String movieId, EventAdmin eventAdmin, Parser parser, int count, int posterCount, int backdropCount) {
        // Call Super
        super();
        // Init
        this.movieId = movieId;
        this.eventAdmin = eventAdmin;
        this.parser = parser;
        this.count = count;
        this.posterCount = posterCount;
        this.backdropCount = backdropCount;
    }

    @Override
    public Integer onCompleted(Response response) throws Exception {
        if (response.getStatusCode() == 200 && response.hasResponseBody()) {
            // Get content
            String content = response.getResponseBody("utf-8");
            try {
                // Parse
                List<MovieInfo> movieInfos = parser.parseMovieInfo(content, count, posterCount, backdropCount);
                // Raise provided event
                raiseMovieInfoProvidedEvent(movieInfos);
            } catch (ParseException e) {
                logger.error("Error when parsing response: {}.", e.getMessage());
                // Raise failed event
                raiseMovieInfoFailedEvent();
            }
        } else {
            logger.warn("Receive non successful status from remote site: {}.", response.getStatusText());
            // Raise failed event
            raiseMovieInfoFailedEvent();
        }
        // Return status code
        return response.getStatusCode();
    }

    /**
     * Raise a movie information provided event
     *
     * @param movieInfos Movie Information from response
     */
    private void raiseMovieInfoProvidedEvent(List<MovieInfo> movieInfos) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(MovieInfoProviderService.MOVIE_ID_PROPERTY, this.movieId);
        properties.put(MovieInfoProviderService.MOVIE_INFO_PROPERTY, movieInfos);
        // Send a event
        Event event = new Event(MovieInfoProviderService.MOVIE_INFO_PROVIDED_TOPIC, properties);
        logger.debug("Send a movie <{}> information provided event.", this.movieId);
        eventAdmin.postEvent(event);
    }

    /**
     * Raise a movie information failed event
     */
    private void raiseMovieInfoFailedEvent() {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(MovieInfoProviderService.MOVIE_ID_PROPERTY, this.movieId);
        properties.put(MovieInfoProviderService.MOVIE_INFO_PROPERTY, new ArrayList<MovieInfo>());
        // Send a event
        Event event = new Event(MovieInfoProviderService.MOVIE_INFO_FAILED_TOPIC, properties);
        logger.debug("Send a movie <{}> information failed event.", this.movieId);
        eventAdmin.postEvent(event);
    }
}
