package org.chii2.medialibrary.api.persistence;

import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;

import java.util.List;

/**
 * Persistence layer for media library
 */
public interface PersistenceService {

    /**
     * Get all the movie records from database
     *
     * @return List of movie records
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

    /**
     * Get movie file by id
     *
     * @param id Movie file id
     * @return Movie file
     */
    public MovieFile getMovieFileById(String id);

    /**
     * Get movie Information by id
     *
     * @param id Movie information id
     * @return Movie information
     */
    public MovieInfo getMovieInfoById(String id);

    /**
     * Get Movie Image by Image ID
     *
     * @param imageId Movie Image ID
     * @return Image
     */
    public MovieImage getMovieImageById(String imageId);

    /**
     * Persist a list of movies into database
     *
     * @param movies List of movies
     */
    public void persist(List<Movie> movies);

    /**
     * Persist a movie into database
     *
     * @param movie Movie
     */
    public void persist(Movie movie);

    /**
     * Persist a movie file into database
     *
     * @param movieFile Movie file
     */
    public void persist(MovieFile movieFile);

    /**
     * Persist a movie information into database
     *
     * @param movieInfo Movie information
     */
    public void persist(MovieInfo movieInfo);

    /**
     * Persist a movie image into database
     *
     * @param movieImage Movie image
     */
    public void persist(MovieImage movieImage);

    /**
     * Merge a movie into database
     *
     * @param movie Movie
     */
    public void merge(Movie movie);

    /**
     * Merge a movie file into database
     *
     * @param movieFile Movie File
     */
    public void merge(MovieFile movieFile);

    /**
     * Merge a movie information into database
     *
     * @param movieInfo Movie information
     */
    public void merge(MovieInfo movieInfo);

    /**
     * Merge a movie image into database
     *
     * @param movieImage Movie Image
     */
    public void merge(MovieImage movieImage);

    /**
     * Remove a movie from database
     *
     * @param movie Movie
     */
    public void remove(Movie movie);

    /**
     * Remove a movie file from database
     *
     * @param movieFile Movie File
     */
    public void remove(MovieFile movieFile);

    /**
     * Remove a movie information from database
     *
     * @param movieInfo Movie Information
     */
    public void remove(MovieInfo movieInfo);

    /**
     * Remove a movie image from database
     *
     * @param movieImage Movie Image
     */
    public void remove(MovieImage movieImage);
}
