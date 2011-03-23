package org.chii2.medialibrary.provider.tmdb.parser;

import org.chii2.medialibrary.api.persistence.entity.MovieInfo;

import java.util.List;

/**
 * Movie Information Parser
 */
public interface Parser {

    /**
     * Parse content into list of MovieInfo
     *
     * @param content  Content to be parsed
     * @param maxCount Max result returned
     * @param posterCount Max poster image count
     * @param backdropCount Max backdrop image count
     * @return Movie Information
     * @throws ParseException Parse Exception indicates parse failed
     */
    public List<MovieInfo> parseMovieInfo(String content, int maxCount, int posterCount, int backdropCount) throws ParseException;
}
