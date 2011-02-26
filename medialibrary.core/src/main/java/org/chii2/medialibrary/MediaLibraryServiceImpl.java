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
    public List<? extends Movie> getAllMovies() {
        return persistenceService.getAllMovies();
    }

    @Override
    public Movie getMovieById(String id) {
        return persistenceService.getMovieById(id);
    }

    @Override
    public List<? extends Movie> getAllMoviesByName(String movieName) {
        return persistenceService.getAllMoviesByName(movieName);
    }

    @Override
    public Movie getSingleMovieByName(String movieName) {
        return persistenceService.getSingleMovieByName(movieName);
    }

    @Override
    public List<? extends Image> getAllImages() {
        return persistenceService.getAllImages();
    }

    @Override
    public Image getImageById(String id) {
        return persistenceService.getImageById(id);
    }

    @Override
    public List<? extends Image> getAllImagesByName(String imageName) {
        return persistenceService.getAllImagesByName(imageName);
    }

    @Override
    public Image getSingleImageByName(String imageName) {
        return persistenceService.getSingleImageByName(imageName);
    }

    @Override
    public List<String> getAllImageAlbums() {
        return persistenceService.getAllImageAlbums();
    }

    @Override
    public List<String> getAllImageAlbums(int firstResult, int maxResults) {
        return persistenceService.getAllImageAlbums(firstResult, maxResults, null);
    }

    @Override
    public List<String> getAllImageAlbums(int firstResult, int maxResults, Map<String, String> sorts) {
        return persistenceService.getAllImageAlbums(firstResult, maxResults, sorts);
    }

    @Override
    public long getImageAlbumsCount() {
        return persistenceService.getImageAlbumsCount();
    }

    @Override
    public List<? extends Image> getImagesByAlbum(String album) {
        return persistenceService.getImagesByAlbum(album);
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
