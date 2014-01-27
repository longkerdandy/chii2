package org.chii2.medialibrary.file;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.medialibrary.api.file.FileService;
import org.chii2.medialibrary.file.filter.FileExtensionFilter;
import org.chii2.medialibrary.file.scanner.FileScanner;
import org.chii2.medialibrary.file.watcher.AbstractFileWatcher;
import org.chii2.medialibrary.file.watcher.FileWatcher;
import org.chii2.medialibrary.file.watcher.WinFileWatcher;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * FileService Implement provide watcher and scanner functionality
 */
public class FileServiceImpl implements FileService, EventHandler {
    // Scanner Queue
    private BlockingQueue<Map<String, Object>> scannerQueue;
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin Service
    private EventAdmin eventAdmin;
    //Configuration File
    private static final String CONFIG_FILE = "org.chii2.medialibrary.file";
    // Movie Directory Configuration Key
    private static final String MOVIE_DIRECTORY = "movie.directory";
    // Video File Extension Configuration Key
    private static final String VIDEO_EXTENSION = "video.extension";
    // Image Directory Configuration Key
    private static final String IMAGE_DIRECTORY = "image.directory";
    // Image File Extension Configuration Key
    private static final String IMAGE_EXTENSION = "image.extension";
    // Movie directories
    private List<String> movieDirectories = Arrays.asList(System.getProperty("user.home") + SystemUtils.FILE_SEPARATOR + "Movies");
    // Movie file extension filter
    private List<String> movieExtFilters = Arrays.asList(".avi", ".mkv", ".mpeg", ".rm", ".rmvb", ".wmv");
    // Image directories
    private List<String> imageDirectories = Arrays.asList(System.getProperty("user.home") + SystemUtils.FILE_SEPARATOR + "Pictures");
    // Image file extension filter
    private List<String> imageExtFilters = Arrays.asList(".jpg", ".jpeg", ".tiff", ".tif", ".png", ".gif", ".bmp");
    // File Scanner
    private FileScanner fileScanner;
    // Movie Watcher
    private AbstractFileWatcher movieWatcher;
    // Image Watcher
    private AbstractFileWatcher imageWatcher;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.file");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library File Service init.");
        Dictionary props = null;
        try {
            Configuration config = this.configAdmin.getConfiguration(CONFIG_FILE);
            props = config.getProperties();
        } catch (IOException e) {
            logger.error("FileService fail to load configuration with exception: {}.", ExceptionUtils.getMessage(e));
        }
        // Load each configuration
        if (props == null || props.isEmpty()) {
            logger.error("FileService load configuration error for <{}>.", CONFIG_FILE);
        } else {
            // Load movie directories configuration
            List<String> movieDirectories = ConfigUtils.loadConfigurations(props, MOVIE_DIRECTORY);
            if (movieDirectories != null && !movieDirectories.isEmpty()) {
                this.movieDirectories = movieDirectories;
                logger.debug("FileService configuration <{}> loaded.", MOVIE_DIRECTORY);
            } else {
                logger.error("FileService configuration <{}> is not valid.", MOVIE_DIRECTORY);
            }

            // Load movie extension filter configuration
            List<String> movieExtFilters = ConfigUtils.loadConfigurations(props, VIDEO_EXTENSION);
            if (movieExtFilters != null && !movieExtFilters.isEmpty()) {
                this.movieExtFilters = movieExtFilters;
                logger.debug("FileService configuration <{}> loaded.", VIDEO_EXTENSION);
            } else {
                logger.error("FileService configuration <{}> is not valid.", VIDEO_EXTENSION);
            }

            // Load image directories configuration
            List<String> imageDirectories = ConfigUtils.loadConfigurations(props, IMAGE_DIRECTORY);
            if (imageDirectories != null && !imageDirectories.isEmpty()) {
                this.imageDirectories = imageDirectories;
                logger.debug("FileService configuration <{}> loaded.", IMAGE_DIRECTORY);
            } else {
                logger.error("FileService configuration <{}> is not valid.", IMAGE_DIRECTORY);
            }

            // Load image extension filter configuration
            List<String> imageExtFilters = ConfigUtils.loadConfigurations(props, IMAGE_EXTENSION);
            if (imageExtFilters != null && !imageExtFilters.isEmpty()) {
                this.imageExtFilters = imageExtFilters;
                logger.debug("FileService configuration <{}> loaded.", IMAGE_EXTENSION);
            } else {
                logger.error("FileService configuration <{}> is not valid.", IMAGE_EXTENSION);
            }
        }

        // Init Scanner Queue
        this.scannerQueue = new LinkedBlockingQueue<>();

        // Start File Scanner
        this.fileScanner = new FileScanner(this.scannerQueue, this.eventAdmin);
        Thread scanner = new Thread(this.fileScanner);
        scanner.setDaemon(false);
        scanner.start();

        // New Movie Watcher
        try {
            if (SystemUtils.IS_OS_WINDOWS) {
                this.movieWatcher = new WinFileWatcher(this.movieDirectories, true, this.createFilter(this.movieExtFilters), FileService.MOVIE_WATCH_CREATE_TOPIC, FileService.MOVIE_WATCH_MODIFY_TOPIC, FileService.MOVIE_WATCH_DELETE_TOPIC, this.eventAdmin);
            } else {
                this.movieWatcher = new FileWatcher(this.movieDirectories, true, this.createFilter(this.movieExtFilters), FileService.MOVIE_WATCH_CREATE_TOPIC, FileService.MOVIE_WATCH_MODIFY_TOPIC, FileService.MOVIE_WATCH_DELETE_TOPIC, this.eventAdmin);
            }
            Thread watcher = new Thread(this.movieWatcher);
            watcher.setDaemon(false);
            watcher.start();
        } catch (IOException e) {
            logger.error("I/O error when create movie watch service: {}.", ExceptionUtils.getMessage(e));
        }

        // New Image Watcher
        try {
            if (SystemUtils.IS_OS_WINDOWS) {
                this.imageWatcher = new WinFileWatcher(this.imageDirectories, true, this.createFilter(this.imageExtFilters), FileService.IMAGE_WATCH_CREATE_TOPIC, FileService.IMAGE_WATCH_MODIFY_TOPIC, FileService.IMAGE_WATCH_DELETE_TOPIC, this.eventAdmin);
            } else {
                this.imageWatcher = new FileWatcher(this.imageDirectories, true, this.createFilter(this.imageExtFilters), FileService.IMAGE_WATCH_CREATE_TOPIC, FileService.IMAGE_WATCH_MODIFY_TOPIC, FileService.IMAGE_WATCH_DELETE_TOPIC, this.eventAdmin);
            }
            Thread watcher = new Thread(this.imageWatcher);
            watcher.setDaemon(false);
            watcher.start();
        } catch (IOException e) {
            logger.error("I/O error when create image watch service: {}.", ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library File Service destroy.");
        // Stop Threads
        this.fileScanner.shouldStop = true;
        this.movieWatcher.shouldStop = true;
        this.imageWatcher.shouldStop = true;
    }

    @Override
    public void handleEvent(Event event) {
        if (FileService.MOVIE_SCAN_REQUEST_TOPIC.equals(event.getTopic())) {
            List<Path> directoryList = new ArrayList<>();
            for (String directory : movieDirectories) {
                if (StringUtils.isNotBlank(directory)) {
                    directoryList.add(Paths.get(StringUtils.trim(directory)));
                }
            }
            this.scanFiles(directoryList, createFilter(this.movieExtFilters), FileService.MOVIE_SCAN_PROVIDED_TOPIC);
        } else if (FileService.IMAGE_SCAN_REQUEST_TOPIC.equals(event.getTopic())) {
            List<Path> directoryList = new ArrayList<>();
            for (String directory : this.imageDirectories) {
                if (StringUtils.isNotBlank(directory)) {
                    directoryList.add(Paths.get(StringUtils.trim(directory)));
                }
            }
            this.scanFiles(directoryList, createFilter(this.imageExtFilters), FileService.IMAGE_SCAN_PROVIDED_TOPIC);
        }
    }

    @Override
    public void scanMovies() {
        this.scanMovies(movieDirectories);
    }

    @Override
    public void scanMovies(List<String> directories) {
        this.scanMovies(directories, movieExtFilters);
    }

    @Override
    public void scanMovies(List<String> directories, List<String> extensions) {
        if (directories != null && !directories.isEmpty()) {
            List<Path> directoryList = new ArrayList<>();
            for (String directory : directories) {
                if (StringUtils.isNotBlank(directory)) {
                    directoryList.add(Paths.get(StringUtils.trim(directory)));
                }
            }
            FileExtensionFilter filter;
            if (extensions != null && !extensions.isEmpty()) {
                filter = createFilter(extensions);
            } else {
                filter = createFilter(this.movieExtFilters);
            }
            this.scanFiles(directoryList, filter, FileService.MOVIE_SCAN_PROVIDED_TOPIC);
        }
    }

    @Override
    public void scanImages() {
        this.scanImages(imageDirectories);
    }

    @Override
    public void scanImages(List<String> directories) {
        this.scanImages(directories, imageExtFilters);
    }

    @Override
    public void scanImages(List<String> directories, List<String> extensions) {
        if (directories != null && !directories.isEmpty()) {
            List<Path> directoryList = new ArrayList<>();
            for (String directory : directories) {
                if (StringUtils.isNotBlank(directory)) {
                    directoryList.add(Paths.get(StringUtils.trim(directory)));
                }
            }
            FileExtensionFilter filter;
            if (extensions != null && !extensions.isEmpty()) {
                filter = createFilter(extensions);
            } else {
                filter = createFilter(this.imageExtFilters);
            }
            this.scanFiles(directoryList, filter, FileService.IMAGE_SCAN_PROVIDED_TOPIC);
        }
    }

    /**
     * Start a new thread to scanAll files
     *
     * @param directories Directories to be scanned
     * @param filter      File name filter
     * @param topic       Event topic
     */
    private void scanFiles(List<Path> directories, FileExtensionFilter filter, String topic) {
        Map<String, Object> properties = new Hashtable<>();
        properties.put(FileService.DIRECTORY_PROPERTY, directories);
        properties.put(FileService.FILTER_PROPERTY, filter);
        properties.put(FileService.TOPIC_PROPERTY, topic);
        // Add request to scannerQueue
        try {
            this.scannerQueue.put(properties);
        } catch (InterruptedException e) {
            logger.error("FileScanner Queue has been interrupted with error: {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Create a FileExtensionFilter based on given extension list
     *
     * @param extensions Accepted file extensions
     * @return FileExtensionFilter
     */
    private FileExtensionFilter createFilter(List<String> extensions) {
        List<String> filter = new ArrayList<>();
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

