package org.chii2.medialibrary.api.file;

import java.util.List;

/**
 * FileService provide watcher and scanner functionality
 */
public interface FileService {
    // Event Topic for file movie scan request
    public final static String MOVIE_SCAN_REQUEST_TOPIC = "org/chii2/medialibrary/file/movie/SCAN_REQUEST";
    // Event Topic for file image scan request
    public final static String IMAGE_SCAN_REQUEST_TOPIC = "org/chii2/medialibrary/file/image/SCAN_REQUEST";
    // Event Topic for movie scan
    public final static String MOVIE_SCAN_PROVIDED_TOPIC = "org/chii2/medialibrary/file/movie/SCAN_PROVIDED";
    // Event Topic for image scan
    public final static String IMAGE_SCAN_PROVIDED_TOPIC = "org/chii2/medialibrary/file/image/SCAN_PROVIDED";
    // Event Topic for movie watch create
    public final static String MOVIE_WATCH_CREATE_TOPIC = "org/chii2/medialibrary/file/movie/WATCH_CREATE";
    // Event Topic for movie watch modify
    public final static String MOVIE_WATCH_MODIFY_TOPIC = "org/chii2/medialibrary/file/movie/WATCH_MODIFY";
    // Event Topic for movie watch delete
    public final static String MOVIE_WATCH_DELETE_TOPIC = "org/chii2/medialibrary/file/movie/WATCH_DELETE";
    // Event Topic for image watch create
    public final static String IMAGE_WATCH_CREATE_TOPIC = "org/chii2/medialibrary/file/image/WATCH_CREATE";
    // Event Topic for image watch modify
    public final static String IMAGE_WATCH_MODIFY_TOPIC = "org/chii2/medialibrary/file/image/WATCH_MODIFY";
    // Event Topic for image watch delete
    public final static String IMAGE_WATCH_DELETE_TOPIC = "org/chii2/medialibrary/file/image/WATCH_DELETE";
    // Directory property in the request event
    public final static String DIRECTORY_PROPERTY = "directories";
    // Filter property in the internal event
    public final static String FILTER_PROPERTY = "filter";
    // Topic property in the internal event
    public final static String TOPIC_PROPERTY = "topic";
    // File information property in the scan provided event, which should contains the scanned file list
    public final static String SCAN_PATH_PROPERTY = "scan_files";
    // File information property in the watch event, which should contains the single file
    public final static String WATCH_PATH_PROPERTY = "watch_file";

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
