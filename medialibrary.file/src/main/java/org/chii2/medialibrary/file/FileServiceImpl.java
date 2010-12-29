package org.chii2.medialibrary.file;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.file.FileService;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

/**
 * FileService Implement provide watcher and scanner functionality
 */
public class FileServiceImpl implements FileService {

    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin Service
    private EventAdmin eventAdmin;
    //Configuration FIle
    private final static String CONFIG_FILE = "org.chii2.medialibrary.file";
    // Movie Directory Configuration Key
    private static final String MOVIE_DIRECTORY = "movie.directory";
    // Video File Extension Configuration Key
    private static final String VIDEO_EXTENSION = "video.extension";
    // Movie directories from configuration
    private List<String> movieDirectories = Arrays.asList("/Share/Movie");
    // Movie file extension filter
    private List<String> movieExtFilters = Arrays.asList(".avi", ".mkv", ".mpeg", ".rm", ".rmvb", ".wmv");
    // Files scanner thread
    private Thread scanThread;
    // Logger
    private Logger logger;

    public FileServiceImpl() {
        logger = LoggerFactory.getLogger("org.chii2.medialibrary.file");
    }

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library File Service init.");
        Dictionary props = null;
        try {
            Configuration config = configAdmin.getConfiguration(CONFIG_FILE);
            props = config.getProperties();
        } catch (IOException e) {
            logger.error("FileService fail to load configuration with exception: {}.", e.getMessage());
        }
        // Load each configuration
        if (props == null || props.isEmpty()) {
            logger.error("FileService load configuration error for <{}>.", CONFIG_FILE);
        } else {
            // Load movie directories configuration
            List<String> directories = ConfigUtils.loadConfigurations(props, MOVIE_DIRECTORY);
            if (directories != null && !directories.isEmpty()) {
                movieDirectories = directories;
                logger.debug("FileService configuration <{}> loaded.", MOVIE_DIRECTORY);
            } else {
                logger.error("FileService configuration <{}> is not valid.", MOVIE_DIRECTORY);
            }

            // Load movie extension filter configuration
            List<String> filters = ConfigUtils.loadConfigurations(props, VIDEO_EXTENSION);
            if (filters != null && !filters.isEmpty()) {
                movieExtFilters = filters;
                logger.debug("FileService configuration <{}> loaded.", VIDEO_EXTENSION);
            } else {
                logger.error("FileService configuration <{}> is not valid.", VIDEO_EXTENSION);
            }
        }
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library File Service destroy.");
    }

    @Override
    public void scanMovies() {
        scanMovies(movieDirectories);
    }

    @Override
    public void scanMovies(List<String> directories) {
        scanMovies(directories, null);
    }

    @Override
    public void scanMovies(List<String> directories, List<String> extensions) {
        if (directories != null && !directories.isEmpty()) {
            List<File> directoryList = new ArrayList<File>();
            for (String directory : directories) {
                if (StringUtils.isNotBlank(directory)) {
                    directoryList.add(new File(StringUtils.trim(directory)));
                }
            }
            FileExtensionFilter filter;
            if (extensions != null && !extensions.isEmpty()) {
                filter = createFilter(extensions);
            } else {
                filter = createFilter();
            }
            scanFiles(directoryList, filter, MOVIE_SCAN_TOPIC);
        }
    }

    /**
     * Start a new thread to scan files
     *
     * @param directories Directories to be scanned
     * @param filter      File name filter
     * @param topic       Event topic
     */
    private void scanFiles(List<File> directories, FileExtensionFilter filter, String topic) {
        if (scanThread != null && scanThread.isAlive()) {
            logger.debug("A File Scanner thread already running, request discard.");
        } else {
            logger.debug("File Service try to start a new File Scanner thread.");
            scanThread = new Thread(new FileScanner(directories, filter, eventAdmin, topic));
            scanThread.start();
        }
    }

    /**
     * Create a FileExtensionFilter based on configuration
     *
     * @return FileExtensionFilter
     */
    private FileExtensionFilter createFilter() {
        return createFilter(movieExtFilters);
    }

    /**
     * Create a FileExtensionFilter based on given extension list
     *
     * @param extensions Accepted file extensions
     * @return FileExtensionFilter
     */
    private FileExtensionFilter createFilter(List<String> extensions) {
        List<String> filter = new ArrayList<String>();
        for (String ext : extensions) {
            if (StringUtils.isNotBlank(ext)) {
                ext = StringUtils.trim(ext);
                // Must begin with '.'
                if (!ext.startsWith(".")) {
                    ext = "." + ext;
                }
                filter.add(ext);
            }
        }
        return new FileExtensionFilter(filter);
    }

    /**
     * Inject ConfigurationAdmin service
     *
     * @param configAdmin ConfigurationAdmin service
     */
    @SuppressWarnings("unused")
    public void setConfigAdmin(ConfigurationAdmin configAdmin) {
        this.configAdmin = configAdmin;
    }

    /**
     * Inject EventAdmin service
     *
     * @param eventAdmin EventAdmin service
     */
    @SuppressWarnings("unused")
    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }
}

