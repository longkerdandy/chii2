package org.chii2.medialibrary.file.watcher;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.medialibrary.api.file.FileService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.file.StandardWatchEventKinds.*;
import static com.sun.nio.file.ExtendedWatchEventModifier.*;
import static java.nio.file.LinkOption.*;

import java.nio.file.*;
import java.util.*;
import java.io.IOException;

/**
 * FileWatcher based on Java WatchService ( From JDK7 ), File/Directory watch service
 * For Windows Only
 */
public class WinFileWatcher extends AbstractFileWatcher {
    // Directory to be watched
    private final List<String> directories;
    // Recursive
    private final boolean recursive;
    // File Filter
    private final DirectoryStream.Filter<Path> filter;
    // Create Event Topic
    private final String createTopic;
    // Modify Event Topic
    private final String modifyTopic;
    // Delete Event Topic
    private final String deleteTopic;
    // EventAdmin
    private final EventAdmin eventAdmin;
    // WatchService
    private final WatchService watchService;
    // Key, Path Map
    private final Map<WatchKey, Path> keys;
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.file.watcher");

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Constructor
     *
     * @param directories Directory to be watched
     * @param recursive   Recursive
     * @param filter      File Name Filter
     * @param createTopic Create Event Topic
     * @param modifyTopic Modify Event Topic
     * @param deleteTopic Delete Event Topic
     * @param eventAdmin  EventAdmin
     * @throws IOException I/O error when create new WatchService
     */
    public WinFileWatcher(List<String> directories, boolean recursive, DirectoryStream.Filter<Path> filter, String createTopic, String modifyTopic, String deleteTopic, EventAdmin eventAdmin) throws IOException {
        this.directories = directories;
        this.recursive = recursive;
        this.filter = filter;
        this.createTopic = createTopic;
        this.modifyTopic = modifyTopic;
        this.deleteTopic = deleteTopic;
        this.eventAdmin = eventAdmin;

        // Init
        this.keys = new HashMap<>();
        this.watchService = FileSystems.getDefault().newWatchService();
    }

    @Override
    public void run() {
        // Add directories to watch
        for (String directory : this.directories) {
            // Path
            Path path;
            try {
                path = Paths.get(directory);
            } catch (InvalidPathException e) {
                logger.warn("Invalid directory: {}", directory);
                continue;
            }

            // Register path with watch service
            if (this.recursive) {
                this.registerAll(path);
            } else {
                this.register(path);
            }
        }

        // Loop event handling
        while (!this.shouldStop) {
            WatchKey key;
            try {
                // take() will block until a file has been created/deleted/modified
                key = this.watchService.take();
            } catch (InterruptedException e) {
                // interrupt
                logger.error("Watcher thread has been interrupted: {}.", ExceptionUtils.getMessage(e));
                break;
            } catch (ClosedWatchServiceException e) {
                // other thread closed watch service
                logger.error("Watcher thread has been closed by other thread: {}.", ExceptionUtils.getMessage(e));
                break;
            }

            if (key != null) {
                // Get Path
                Path directory = this.keys.get(key);

                for (WatchEvent<?> event : key.pollEvents()) {
                    // Event Kind
                    WatchEvent.Kind eventKind = event.kind();
                    // TODO: Find out what reason will cause OVERFLOW event and do corresponding handling
                    if (eventKind == OVERFLOW) {
                        continue;
                    }

                    // Context for directory entry event is the file name of entry
                    WatchEvent<Path> watchEvent = cast(event);
                    Path context = watchEvent.context();
                    Path path = directory.resolve(context);

                    // log
                    logger.debug("Receive {} event for {}.", event.kind().name(), path);

                    // TODO: Hidden or SymbolicLink
                    // Handle event
                    if (eventKind == ENTRY_CREATE) {
                        // File Create Event
                        if (Files.isRegularFile(path)) {
                            // Post Watch Event
                            this.postEvent(this.filter, path, this.createTopic);
                        }
                    } else if (eventKind == ENTRY_MODIFY) {
                        // File Modify Event
                        if (Files.isRegularFile(path, NOFOLLOW_LINKS)) {
                            // Post Event
                            this.postEvent(this.filter, path, this.modifyTopic);
                        }
                    } else if (eventKind == ENTRY_DELETE) {
                        // Key-Path Mapping contains this path, this is a directory already registered, cancel it
                        if (this.keys.containsValue(path)) {
                            key.cancel();
                        }
                        // This should a file or directory not in the mapping
                        else {
                            // Post Event
                            this.postDeleteEvent(this.filter, path, this.deleteTopic);
                        }
                    }
                }

                // reset key and remove from set if directory no longer accessible
                boolean valid = key.reset();
                if (!valid) {
                    this.keys.remove(key);
                    // all directories are inaccessible
                    if (this.keys.isEmpty()) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Register the given directory with the WatchService
     *
     * @param path Path
     */
    private void register(Path path) {
        try {
            // Register Path
            WatchKey key = path.register(this.watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            logger.debug("Register path {} to Watch Service.", path);
            // Save into the Key Path mapping
            this.keys.put(key, path);
        } catch (NotDirectoryException e) {
            logger.warn("Register path {} is not a directory.");
        } catch (IOException e) {
            logger.error("I/O error when register path: {}.", ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Register the given directory, and all its sub-directories, with the WatchService.
     *
     * @param path Base Pah
     */
    private void registerAll(Path path) {
        try {
            // Register Path
            WatchKey key = path.register(this.watchService, new WatchEvent.Kind<?>[]{ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY}, FILE_TREE);

            logger.debug("Register path {} to Watch Service.", path);
            // Save into the Key Path mapping
            this.keys.put(key, path);
        } catch (NotDirectoryException e) {
            logger.warn("Register path {} is not a directory.");
        } catch (IOException e) {
            logger.error("I/O error when register path: {}.", ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Send a watch event asynchronously
     *
     * @param filter Path filter
     * @param path   Path discovered
     * @param topic  Event topic
     */
    private void postEvent(DirectoryStream.Filter<Path> filter, Path path, String topic) {
        try {
            if (filter.accept(path)) {
                this.postEvent(path, topic);
            }
        } catch (IOException e) {
            logger.error("I/O error when try to filter path {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", path);
        }
    }

    /**
     * Send a watch event asynchronously, Specially apply to Delete Event
     *
     * @param filter Path filter
     * @param path   Path discovered
     * @param topic  Event topic
     */
    private void postDeleteEvent(DirectoryStream.Filter<Path> filter, Path path, String topic) {
        try {
            // Directory
            if (StringUtils.isEmpty(FilenameUtils.getExtension(path.getFileName().toString()))) {
                this.postEvent(path, topic);
            }
            // File
            else if (filter.accept(path)) {
                this.postEvent(path, topic);
            }
        } catch (IOException e) {
            logger.error("I/O error when try to filter path {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", path);
        }
    }

    /**
     * Send a watch event asynchronously
     *
     * @param path   Path discovered
     * @param topic  Event topic
     */
    private void postEvent(Path path, String topic) {
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(FileService.WATCH_PATH_PROPERTY, path);
        Event event = new Event(topic, properties);
        logger.debug("Send a file {} watch event to topic {}.", path, topic);
        this.eventAdmin.postEvent(event);
    }
}
