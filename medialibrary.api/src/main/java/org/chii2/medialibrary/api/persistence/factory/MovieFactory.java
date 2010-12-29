package org.chii2.medialibrary.api.persistence.factory;

import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;

/**
 * Movie Factory
 */
public interface MovieFactory {

    /**
     * Create a new Movie instance
     *
     * @return new Movie instance
     */
    public Movie createMovie();

    /**
     * Create a new MovieFile instance
     *
     * @return new MovieFile instance
     */
    public MovieFile createMovieFile();

    /**
     * Create a new MovieInfo instance
     *
     * @return new MovieInfo instance
     */
    public MovieInfo createMovieInfo();

    /**
     * Create a new MovieImage instance
     *
     * @return new MovieImage instance
     */
    public MovieImage createMovieImage();
}
