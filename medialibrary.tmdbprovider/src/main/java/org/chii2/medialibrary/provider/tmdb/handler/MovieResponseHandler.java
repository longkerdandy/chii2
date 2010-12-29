package org.chii2.medialibrary.provider.tmdb.handler;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.api.provider.MovieInfoProviderService;
import org.chii2.medialibrary.provider.tmdb.MovieInfoProviderServiceImpl;
import org.chii2.medialibrary.provider.tmdb.parser.Parser;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;
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
public class MovieResponseHandler extends SimpleChannelUpstreamHandler {

    // Action type
    private String type;
    // Movie ID
    private String movieId;
    // Movie Name
    private String movieName;
    // Movie year
    private String movieYear;
    // Excluded Providers
    private List<String> excludeProviders;
    // EventAdmin
    private EventAdmin eventAdmin;
    // HTTP API Parser
    private Parser parser;
    // Logger
    private Logger logger;

    public MovieResponseHandler(String type, String movieId, String movieName, String movieYear, List<String> excludeProviders, EventAdmin eventAdmin, Parser parser) {
        // Call Super
        super();
        // Init
        this.type = type;
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieYear = movieYear;
        this.excludeProviders = excludeProviders;
        this.eventAdmin = eventAdmin;
        this.parser = parser;
        logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        // HttpResponse
        HttpResponse response = (HttpResponse) e.getMessage();

        // If response is OK, parse content, else raise failed event
        if (response.getStatus().equals(HttpResponseStatus.OK)) {
            logger.debug("Receive a successful http response when receive movie <{} {}>.", movieId, movieName);
            // Content
            ChannelBuffer content = response.getContent();
            List<MovieInfo> infoList;

            if (type.equals(MovieInfoProviderServiceImpl.SEARCH_FIRST_BY_NAME)) {
                logger.debug("Try to parse the http response into a single movie information.");
                // Parse
                MovieInfo info = parser.getSingleMovieInfo(content.toString(CharsetUtil.UTF_8));
                // Parse successful or not
                if (info != null) {
                    infoList = new ArrayList<MovieInfo>();
                    infoList.add(info);
                    // Raise provided event
                    raiseMovieInfoProvidedEvent(infoList);
                } else {
                    // Raise fail event
                    raiseMovieInfoFailedEvent();
                }
            } else if (type.equals(MovieInfoProviderServiceImpl.SEARCH_ALL_BY_NAME)) {
                logger.debug("Try to parse the http response into all possible movie information.");
                // Parse
                infoList = parser.getAllMovieInfo(content.toString(CharsetUtil.UTF_8));
                // Parse successful or not
                if (infoList != null && !infoList.isEmpty()) {
                    // Raise provided event
                    raiseMovieInfoProvidedEvent(infoList);
                } else {
                    // Raise fail event
                    raiseMovieInfoFailedEvent();
                }
            } else {
                // This should not happens
                raiseMovieInfoFailedEvent();
            }

        } else {
            logger.debug("Receive a unsuccessful http response when receive movie <{} {}>.", movieId, movieName);
            // Raise fail event
            raiseMovieInfoFailedEvent();
        }

        // Close the channel
        e.getChannel().close();
    }
   
    @Override
    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.debug("A exception occurred when receive movie <{}>: {}", movieId, e.getCause().getMessage());
        // Raise fail event
        raiseMovieInfoFailedEvent();
    }

    /**
     * Raise a movie information provided event
     *
     * @param infoList MovieInfo List
     */
    private void raiseMovieInfoProvidedEvent(List<MovieInfo> infoList) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(MovieInfoProviderService.MOVIE_ID_PROPERTY, movieId);
        properties.put(MovieInfoProviderService.MOVIE_NAME_PROPERTY, movieName);
        properties.put(MovieInfoProviderService.MOVIE_YEAR_PROPERTY, getMovieYear());
        properties.put(MovieInfoProviderService.EXCLUDE_PROVIDERS_PROPERTY, getExcludeProviders());
        properties.put(MovieInfoProviderService.MOVIE_INFO_PROPERTY, infoList);
        // Send a event
        Event event = new Event(MovieInfoProviderService.MOVIE_INFO_PROVIDED_TOPIC, properties);
        logger.debug("Send a movie <{} {}> information provided event.", movieId, movieName);
        eventAdmin.postEvent(event);
    }

    /**
     * Raise a movie information failed event
     */
    private void raiseMovieInfoFailedEvent() {
        // Add TMDb to excluded provider list, since it failed
        excludeProviders.add("TMDb");
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(MovieInfoProviderService.MOVIE_ID_PROPERTY, movieId);
        properties.put(MovieInfoProviderService.MOVIE_NAME_PROPERTY, movieName);
        properties.put(MovieInfoProviderService.MOVIE_YEAR_PROPERTY, getMovieYear());
        properties.put(MovieInfoProviderService.EXCLUDE_PROVIDERS_PROPERTY, getExcludeProviders());
        properties.put(MovieInfoProviderService.MOVIE_INFO_PROPERTY, new ArrayList<MovieInfo>());
        // Send a event
        Event event = new Event(MovieInfoProviderService.MOVIE_INFO_FAILED_TOPIC, properties);
        logger.debug("Send a movie <{} {}> information failed event.", movieId, movieName);
        eventAdmin.postEvent(event);
    }

    /**
     * Get movie year, if null return empty
     * @return Movie Year
     */
    private String getMovieYear() {
        if (StringUtils.isBlank(movieYear)) {
            return "";
        } else {
            return movieYear;
        }
    }

    /**
     * Get Excluded Provider List, if null return a empty list
     * @return Excluded Provider List
     */
    private List<String> getExcludeProviders() {
        if (getMovieYear() == null) {
            return new ArrayList<String>();
        } else {
            return excludeProviders;
        }
    }
}
