package org.chii2.medialibrary.api.core;

import org.chii2.medialibrary.api.persistence.entity.Movie;

import java.util.List;

/**
 * Media Library Core Interface, provide major functionality & operations.
 */
public interface MediaLibraryService {
    
    /**
     * Scan for all kinds of media files in directories.
     * (Media type extensions and  Directories are configured in configuration file)
     */
    public void scan();

    /**
     * Get all the Movies in the Media Library.
     *
     * @return Movie List
     */
    public List<? extends Movie> getAllMovies();

    /**
     * Get Movie by Movie ID
     *
     * @param id Movie ID
     * @return Movie
     */
    public Movie getMovieById(String id);

    /**
     * Get all possible movie records by movie name
     *
     * @param movieName Movie Name
     * @return Movie List
     */
    public List<? extends Movie> getAllMoviesByName(String movieName);

    /**
     * Get single movie record by movie name, usually return first result
     *
     * @param movieName Movie name
     * @return Movie
     */
    public Movie getSingleMovieByName(String movieName);
}
