package org.chii2.medialibrary.api.core;

import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.Movie;

import java.util.List;
import java.util.Map;

/**
 * Media Library Core Interface, provide major functionality & operations.
 */
public interface MediaLibraryService {

    /**
     * Scan for all kinds of media files in directories.
     * (Media type extensions and  Directories are configured in configuration file)
     */
    public void scanAll();

    /**
     * Scan for all movie files in directories.
     */
    public void scanMovies();

    /**
     * Scan for all image files in directories.
     */
    public void scanImages();

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

    /**
     * Get all the Images
     *
     * @return Image List
     */
    public List<? extends Image> getAllImages();

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
     * @param imageName Image Name
     * @return Image List
     */
    public List<? extends Image> getAllImagesByName(String imageName);

    /**
     * Get single image record by image name, usually return first result
     *
     * @param imageName Image Name
     * @return Image
     */
    public Image getSingleImageByName(String imageName);

    /**
     * Get all image albums
     *
     * @return Image Albums
     */
    public List<String> getAllImageAlbums();

    /**
     * Get image albums from index with max limit
     *
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @return Image Albums
     */
    public List<String> getAllImageAlbums(int firstResult, int maxResults);

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
     * @param album Image Album
     * @return Images
     */
    public List<? extends Image> getImagesByAlbum(String album);

    /**
     * Get images by image album
     *
     * @param album       Image Album
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @return Images
     */
    public List<? extends Image> getImagesByAlbum(String album, int firstResult, int maxResults);

    /**
     * Get images by image album
     *
     * @param album       Image Album
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @param sorts       Sort (by <field, sortType>, sort type maybe "asc" or "desc")
     * @return Images
     */
    public List<? extends Image> getImagesByAlbum(String album, int firstResult, int maxResults, Map<String, String> sorts);

    /**
     * Get count of images belong to  specific album
     *
     * @param album Image Album
     * @return Count
     */
    public long getImagesCountByAlbum(String album);
}
