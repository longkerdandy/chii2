package org.chii2.medialibrary.api.file;

import java.util.List;

/**
 * FileService provide watcher and scanner functionality
 */
public interface FileService {

    // Event Topic for movie scan
    public final static String MOVIE_SCAN_TOPIC = "org/chii2/medialibrary/file/movie/SCAN";

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
}
