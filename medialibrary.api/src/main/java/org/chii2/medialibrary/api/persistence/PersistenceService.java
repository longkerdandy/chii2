package org.chii2.medialibrary.api.persistence;

import org.chii2.medialibrary.api.persistence.entity.*;

import java.util.List;
import java.util.Map;

/**
 * Persistence layer for media library
 */
public interface PersistenceService {

    /**
     * Get movie records from database
     *
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @param sorts       Sort (by <field, sortType>, sort type maybe "asc" or "desc")
     * @return List of movie records
     */
    public List<? extends Movie> getMovies(int firstResult, int maxResults, Map<String, String> sorts);

    /**
     * Get Movie by Movie ID
     *
     * @param id Movie ID
     * @return Movie
     */
    public Movie getMovieById(String id);

    /**
     * Get all possible movie records by movie name
     * The Sort Field must prefix with "file." or "info."
     * eg. "file.duration" will reference to MovieFile duration
     * "info.language" will reference to MovieInfo language
     *
     * @param movieName   Movie Name
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @param sorts       Sort (by <field, sortType>, sort type maybe "asc" or "desc")
     * @return Movie List
     */
    public List<? extends Movie> getMoviesByName(String movieName, int firstResult, int maxResults, Map<String, String> sorts);

    /**
     * Get Movie Files from database
     * The Sort Field must prefix with "file." or "info."
     * eg. "file.duration" will reference to MovieFile duration
     * "info.language" will reference to MovieInfo language
     *
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @param sorts       Sort (by <field, sortType>, sort type maybe "asc" or "desc")
     * @return List of Movie Files
     */
    public List<? extends MovieFile> getMovieFiles(int firstResult, int maxResults, Map<String, String> sorts);

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
     * Get Movie default thumbnail
     *
     * @param movieId Movie ID
     * @return Thumbnail
     */
    public byte[] getMovieThumbnailById(String movieId);

    /**
     * Get Movies Count
     *
     * @return Count
     */
    public long getMoviesCount();

    /**
     * Get Movie Files Count
     *
     * @return Count
     */
    public long getMovieFilesCount();

    /**
     * Get all the Images
     *
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @param sorts       Sort (by <field, sortType>, sort type maybe "asc" or "desc")
     * @return Image List
     */
    public List<? extends Image> getImages(int firstResult, int maxResults, Map<String, String> sorts);

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
    public List<? extends Image> getImagesByName(String imageName, int firstResult, int maxResults, Map<String, String> sorts);

    /**
     * Get image albums from index with max limit
     *
     * @param firstResult First Result
     * @param maxResults  Max Result
     * @param sorts       Sort (by <field, sortType>, sort type maybe "asc" or "desc")
     * @return Image Albums
     */
    public List<String> getImageAlbums(int firstResult, int maxResults, Map<String, String> sorts);

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
     * Delete all Movie Information
     */
    public void deleteMovieInfo();

    /**
     * Synchronize Movie Files to database
     *
     * @param movieFiles Movie Files
     */
    public void synchronize(List<MovieFile> movieFiles);

    /**
     * Synchronize Movie Information to database
     *
     * @param movieId Movie ID
     * @param info    Movie Information List
     */
    public void synchronize(String movieId, List<MovieInfo> info);

    /**
     * Persis entity into database, proxy to EntityManager.persist
     * If A is a new entity, it becomes managed.
     * If A is an existing managed entity, it is ignored. However, the persist operation cascades as defined below.
     * If A is a removed entity, it becomes managed.
     * If A is a detached entity, an IllegalArgumentException is thrown.
     * The persist operation recurses on all relation fields of A whose cascades include CascadeType.PERSIST.
     *
     * @param entity Entity
     */
    public void persist(Object entity);

    /**
     * Merge entity into database, proxy to EntityManager.merge
     * If A is a detached entity, its state is copied into existing managed instance A' of the same entity identity, or a new managed copy of A is created.
     * If A is a new entity, a new managed entity A' is created and the state of A is copied into A'.
     * If A is an existing managed entity, it is ignored. However, the merge operation still cascades as defined below.
     * If A is a removed entity, an IllegalArgumentException is thrown.
     * The merge operation recurses on all relation fields of A whose cascades include CascadeType.MERGE.
     *
     * @param entity Entity
     * @param <T>    Entity Class
     * @return Managed Entity
     */
    public <T> T merge(T entity);

    /**
     * Remove entity into database, proxy to EntityManager.remove
     * If A is a new entity, it is ignored. However, the remove operation cascades as defined below.
     * If A is an existing managed entity, it becomes removed.
     * If A is a removed entity, it is ignored.
     * If A is a detached entity, an IllegalArgumentException is thrown.
     * The remove operation recurses on all relation fields of A whose cascades include CascadeType.REMOVE.
     *
     * @param entity Entity
     */
    public void remove(Object entity);

    /**
     * Refresh entity with database, proxy to EntityManager.refresh
     * If A is a new entity, it is ignored. However, the refresh operation cascades as defined below.
     * If A is an existing managed entity, its state is refreshed from the datastore.
     * If A is a removed entity, it is ignored.
     * If A is a detached entity, an IllegalArgumentException is thrown.
     * The refresh operation recurses on all relation fields of A whose cascades include CascadeType.REFRESH.
     *
     * @param entity Entity
     */
    public void refresh(Object entity);
}
