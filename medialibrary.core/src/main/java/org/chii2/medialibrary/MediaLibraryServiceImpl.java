package org.chii2.medialibrary;

import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.file.FileService;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Media Library Core Interface, provide major functionality & operations.
 */
public class MediaLibraryServiceImpl implements MediaLibraryService {

    // Persistence Service
    private PersistenceService persistenceService;
    // File Service
    private FileService fileService;
    // Logger
    private Logger logger;

    public MediaLibraryServiceImpl() {
        logger = LoggerFactory.getLogger("org.chii2.medialibrary.core");
    }

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
