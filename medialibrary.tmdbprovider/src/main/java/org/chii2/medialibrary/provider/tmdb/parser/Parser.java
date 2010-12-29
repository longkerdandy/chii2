package org.chii2.medialibrary.provider.tmdb.parser;

import org.chii2.medialibrary.api.persistence.entity.MovieInfo;

import java.util.List;

/**
 * Movie Information Parser
 */
public interface Parser {

    /**
     * Parse content into single movie information
     *
     * @param content Content to be parsed
     * @return Movie Information
     */
    public MovieInfo getSingleMovieInfo(String content);

    /**
     * Parse content into list of movie information
     *
     * @param content Content to be parsed
     * @return List of Movie Information
     */
    public List<MovieInfo> getAllMovieInfo(String content);
}
