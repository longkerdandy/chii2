package org.chii2.medialibrary.api.persistence;

import org.chii2.medialibrary.api.persistence.entity.*;

import java.util.List;
import java.util.Map;

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
     * Get all the Images
     *
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @param sorts       Sort (by <field, sortType>, sort type maybe "asc" or "desc")
     * @return Image List
     */
    public List<? extends Image> getAllImages(int firstResult, int maxResults, Map<String, String> sorts);

    /**
     * Get Image by Image ID
     *
     * @param id Image ID
     * @return Image
     */
    public Image getImageById(String id);

    /**
     * Get all possible image records by image name
     *
     * @param imageName   Image Name
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @param sorts       Sort (by <field, sortType>, sort type maybe "asc" or "desc")
     * @return Image List
     */
    public List<? extends Image> getAllImagesByName(String imageName, int firstResult, int maxResults, Map<String, String> sorts);

    /**
     * Get image albums from index with max limit
     *
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @param sorts       Sort (by <field, sortType>, sort type maybe "asc" or "desc")
     * @return Image Albums
     */
    public List<String> getAllImageAlbums(int firstResult, int maxResults, Map<String, String> sorts);

    /**
     * Get the count of total image albums
     *
     * @return Count
     */
    public long getImageAlbumsCount();

    /**
     * Get images by image album
     *
     * @param album       Image Album
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @param sorts       Sort (by <field, sortType>, sort type maybe "asc" or "desc")
     * @return Image Albums
     */
    public List<? extends Image> getImagesByAlbum(String album, int firstResult, int maxResults, Map<String, String> sorts);

    /**
     * Get count of images belong to  specific album
     *
     * @param album Image Album
     * @return Count
     */
    public long getImagesCountByAlbum(String album);

    /**
     * Delete all images in library
     */
    public void deleteAllImages();

    /**
     * Get Image File by ID
     *
     * @param id Image File ID
     * @return Image File
     */
    public ImageFile getImageFileById(String id);

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
     * Persist a image into database
     *
     * @param image Image
     */
    public void persist(Image image);

    /**
     * Persist a image file into database
     *
     * @param imageFile Image File
     */
    public void persist(ImageFile imageFile);

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
     * Merge a image into database
     *
     * @param image Image
     */
    public void merge(Image image);

    /**
     * Merge a image file into database
     *
     * @param imageFile Image File
     */
    public void merge(ImageFile imageFile);

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

    /**
     * Remove a image from database
     *
     * @param image Image
     */
    public void remove(Image image);

    /**
     * Remove a image file from database
     *
     * @param imageFile Image File
     */
    public void remove(ImageFile imageFile);
}
