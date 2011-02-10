package org.chii2.medialibrary.persistence.factory;

import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.chii2.medialibrary.persistence.entity.MovieFileImpl;
import org.chii2.medialibrary.persistence.entity.MovieImageImpl;
import org.chii2.medialibrary.persistence.entity.MovieImpl;
import org.chii2.medialibrary.persistence.entity.MovieInfoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Movie Factory
 */
public class MovieFactoryImpl implements MovieFactory {

    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.persistence");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library MovieFactory init.");
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library MovieFactory destroy.");
    }

    @Override
    public Movie createMovie() {
        return new MovieImpl();
    }

    @Override
    public MovieFile createMovieFile() {
        return new MovieFileImpl();
    }

    @Override
    public MovieInfo createMovieInfo() {
        return new MovieInfoImpl();
    }

    @Override
    public MovieImage createMovieImage() {
        return new MovieImageImpl();
    }
}
