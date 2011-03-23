package org.chii2.medialibrary;

import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.file.FileService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Media Library Core Interface, provide major functionality & operations.
 */
public class MediaLibraryServiceImpl implements MediaLibraryService {

    // Persistence Service
    private PersistenceService persistenceService;
    // File Service
    private FileService fileService;
    // Logger
    private Logger logger= LoggerFactory.getLogger("org.chii2.medialibrary.core");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library MediaLibraryService (Core) init.");
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library MediaLibraryService (Core) destroy.");
    }

    @Override
    public void scanAll() {
        // TODO
    }

    @Override
    public void scanMovies() {
        fileService.scanMovies();
    }

    @Override
    public void scanImages() {
        fileService.scanImages();
    }

    @Override
    public List<? extends Movie> getMovies() {
        return persistenceService.getMovies(-1, -1, null);
    }

    @Override
    public List<? extends Movie> getMovies(int firstResult, int maxResults) {
        return persistenceService.getMovies(firstResult, maxResults, null);
    }

    @Override
    public List<? extends Movie> getMovies(int firstResult, int maxResults, Map<String, String> sorts) {
        return persistenceService.getMovies(firstResult, maxResults, sorts);
    }

    @Override
    public Movie getMovieById(String id) {
        return persistenceService.getMovieById(id);
    }

    @Override
    public List<? extends Movie> getMoviesByName(String movieName) {
        return persistenceService.getMoviesByName(movieName, -1, -1, null);
    }

    @Override
    public List<? extends Movie> getMoviesByName(String movieName, int firstResult, int maxResults) {
        return persistenceService.getMoviesByName(movieName, firstResult, maxResults, null);
    }

    @Override
    public List<? extends Movie> getMoviesByName(String movieName, int firstResult, int maxResults, Map<String, String> sorts) {
        return persistenceService.getMoviesByName(movieName, firstResult, maxResults, sorts);
    }

    @Override
    public List<? extends Image> getImages() {
        return persistenceService.getImages(-1, -1, null);
    }

    @Override
    public List<? extends Image> getImages(int firstResult, int maxResults) {
        return persistenceService.getImages(firstResult, maxResults, null);
    }

    @Override
    public List<? extends Image> getImages(int firstResult, int maxResults, Map<String, String> sorts) {
        return persistenceService.getImages(firstResult, maxResults, sorts);
    }

    @Override
    public Image getImageById(String id) {
        return persistenceService.getImageById(id);
    }

    @Override
    public List<? extends Image> getImagesByName(String imageName) {
        return persistenceService.getImagesByName(imageName, -1, -1, null);
    }

    @Override
    public List<? extends Image> getImagesByName(String imageName, int firstResult, int maxResults) {
        return persistenceService.getImagesByName(imageName, firstResult, maxResults, null);
    }

    @Override
    public List<? extends Image> getImagesByName(String imageName, int firstResult, int maxResults, Map<String, String> sorts) {
        return persistenceService.getImagesByName(imageName, firstResult, maxResults, sorts);
    }

    @Override
    public List<String> getImageAlbums() {
        return persistenceService.getImageAlbums(-1, -1, null);
    }

    @Override
    public List<String> getImageAlbums(int firstResult, int maxResults) {
        return persistenceService.getImageAlbums(firstResult, maxResults, null);
    }

    @Override
    public List<String> getImageAlbums(int firstResult, int maxResults, Map<String, String> sorts) {
        return persistenceService.getImageAlbums(firstResult, maxResults, sorts);
    }

    @Override
    public long getImageAlbumsCount() {
        return persistenceService.getImageAlbumsCount();
    }

    @Override
    public List<? extends Image> getImagesByAlbum(String album) {
        return persistenceService.getImagesByAlbum(album,-1 ,-1, null);
    }

    @Override
    public List<? extends Image> getImagesByAlbum(String album, int firstResult, int maxResults) {
        return persistenceService.getImagesByAlbum(album, firstResult, maxResults, null);
    }

    @Override
    public List<? extends Image> getImagesByAlbum(String album, int firstResult, int maxResults, Map<String, String> sorts) {
        return persistenceService.getImagesByAlbum(album, firstResult, maxResults, sorts);
    }

    @Override
    public long getImagesCountByAlbum(String album) {
        return persistenceService.getImagesCountByAlbum(album);
    }

    /**
     * Inject PersistenceService
     *
     * @param persistenceService PersistenceService
     */
    @SuppressWarnings("unused")
    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * Inject FileService
     *
     * @param fileService FileService
     */
    @SuppressWarnings("unused")
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
