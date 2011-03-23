package org.chii2.medialibrary.api.file;

import java.util.List;

/**
 * FileService provide watcher and scanner functionality
 */
public interface FileService {

    // Event Topic for movie scanAll
    public final static String MOVIE_SCAN_TOPIC = "org/chii2/medialibrary/file/movie/SCAN";
    // Event Topic for image scanAll
    public final static String IMAGE_SCAN_TOPIC = "org/chii2/medialibrary/file/image/SCAN";
    // File information property in the event, which should contains the scanned file list
    public final static String FILE_PROPERTY = "files";

    /**
     * Scan default directories (from configuration) for movies files
     */
    public void scanMovies();

    /**
     * Scan directories for movie files
     *
     * @param directories Directories to be scanned
     */
    public void scanMovies(List<String> directories);

    /**
     * Scan directories for movie files
     *
     * @param directories Directories to be scanned
     * @param extensions  File extensions to be accepted, like " .avi .mkv "
     */
    public void scanMovies(List<String> directories, List<String> extensions);

    /**
     * Scan default directories (from configuration) for image files
     */
    public void scanImages();

    /**
     * Scan directories for image files
     *
     * @param directories Directories to be scanned
     */
    public void scanImages(List<String> directories);

    /**
     * Scan directories for image files
     *
     * @param directories Directories to be scanned
     * @param extensions  File extensions to be accepted, like " .jpg .png "
     */
    public void scanImages(List<String> directories, List<String> extensions);
}
