package org.chii2.medialibrary.file.watcher;

import name.pachler.nio.file.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.medialibrary.api.file.FileService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

/**
 * FileWatcher based on JPathWatch, File/Directory watch service
 */
public class FileWatcher implements Runnable {
    // Flag
    public volatile boolean shouldStop = false;
    // Directory to be watched
    private List<String> directories;
    // File Filter
    private FileFilter filter;
    // Create Event Topic
    private String createTopic;
    // Modify Event Topic
    private String modifyTopic;
    // Delete Event Topic
    private String deleteTopic;
    // EventAdmin
    private EventAdmin eventAdmin;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.file.watcher");

    /**
     * Constructor
     *
     * @param directories Directory to be watched
     * @param filter      File Name Filter
     * @param createTopic Create Event Topic
     * @param modifyTopic Modify Event Topic
     * @param deleteTopic Delete Event Topic
     * @param eventAdmin  EventAdmin
     */
    public FileWatcher(List<String> directories, FileFilter filter, String createTopic, String modifyTopic, String deleteTopic, EventAdmin eventAdmin) {
        this.directories = directories;
        this.filter = filter;
        this.createTopic = createTopic;
        this.modifyTopic = modifyTopic;
        this.deleteTopic = deleteTopic;
        this.eventAdmin = eventAdmin;
    }

    @Override
    public void run() {
        // New Watch Service
        WatchService watchService = FileSystems.getDefault().newWatchService();

        // Mapping Cache
        Map<WatchKey, Path> mapping = new HashMap<WatchKey, Path>();

        // Add directories to watch
        for (String directory : this.directories) {
            // Path
            Path path = Paths.get(directory);
            // Key
            WatchKey key = null;
            try {
                key = path.register(watchService, StandardWatchEventKind.ENTRY_CREATE, StandardWatchEventKind.ENTRY_MODIFY, StandardWatchEventKind.ENTRY_DELETE);
            } catch (UnsupportedOperationException e) {
                logger.error("Error when register watcher key: {}", ExceptionUtils.getMessage(e));
            } catch (IOException e) {
                logger.error("Error when register watcher key: {}", ExceptionUtils.getMessage(e));
            }
            // Add to mapping
            if (key != null) {
                mapping.put(key, path);
            }
        }


        // Loop event handling
        while (!this.shouldStop) {
            // take() will block until a file has been created/deleted
            WatchKey signalledKey = null;
            try {
                signalledKey = watchService.take();
            } catch (InterruptedException e) {
                logger.warn("Watcher thread has been interrupted: {}", ExceptionUtils.getMessage(e));
            } catch (ClosedWatchServiceException e) {
                // other thread closed watch service
                logger.warn("Watcher thread has been closed by other thread: {}", ExceptionUtils.getMessage(e));
                break;
            }

            if (signalledKey != null) {
                // Get Path
                Path directory = mapping.get(signalledKey);

                // get list of events from key
                List<WatchEvent<?>> events = signalledKey.pollEvents();

                // VERY IMPORTANT! call reset() AFTER pollEvents() to allow the
                // key to be reported again by the watch service
                signalledKey.reset();

                // Loop Event Type
                for (WatchEvent event : events) {
                    if (event.kind() == StandardWatchEventKind.ENTRY_CREATE) {
                        Path context = (Path) event.context();
                        logger.debug("Receive a Entry Create watch event for: {}", context.toString());
                        // File
                        File file = new File(directory.toString(), context.toString());
                        if (file.exists()) {
                            // Directory
                            if (file.isDirectory()) {
                                // Path
                                Path path = Paths.get(file.getAbsolutePath());
                                // Key
                                WatchKey key = null;
                                try {
                                    key = path.register(watchService, StandardWatchEventKind.ENTRY_CREATE, StandardWatchEventKind.ENTRY_MODIFY, StandardWatchEventKind.ENTRY_DELETE);
                                } catch (UnsupportedOperationException e) {
                                    logger.error("Error when register watcher key: {}", ExceptionUtils.getMessage(e));
                                } catch (IOException e) {
                                    logger.error("Error when register watcher key: {}", ExceptionUtils.getMessage(e));
                                }
                                // Add to mapping
                                if (key != null) {
                                    mapping.put(key, path);
                                }
                            }
                            // Filer
                            else if (file.isFile()) {
                                // Name Filter
                                if (this.filter.accept(file)) {
                                    // Post Event
                                    this.postEvent(file, this.createTopic);
                                }
                            }
                        }
                    } else if (event.kind() == StandardWatchEventKind.ENTRY_MODIFY) {
                        Path context = (Path) event.context();
                        logger.debug("Receive a Entry Modify watch event for: {}", context.toString());
                        // File
                        File file = new File(directory.toString(), context.toString());
                        if (file.exists() && file.isFile()) {
                            this.postEvent(file, this.modifyTopic);
                        }
                    } else if (event.kind() == StandardWatchEventKind.ENTRY_DELETE) {
                        Path context = (Path) event.context();
                        logger.debug("Receive a Entry Delete watch event for: {}", context.toString());
                        // File
                        File file = new File(directory.toString(), context.toString());
                        // Path
                        Path path = Paths.get(file.getAbsolutePath());
                        // Mapping Cache contains path, it's a directory
                        if (mapping.containsValue(path)) {
                            WatchKey cacheKey = this.getKeyByPath(mapping, path);
                            // Canceled
                            cacheKey.cancel();
                            // Remove from mapping cache
                            mapping.remove(cacheKey);
                        }
                        // it's a file
                        else {
                            // Name Filter
                            if (this.filter.accept(file)) {
                                // Post Event
                                this.postEvent(file, this.deleteTopic);
                            }
                        }
                    } else if (event.kind() == StandardWatchEventKind.OVERFLOW) {
                        logger.warn("OVERFLOW: more changes happened than we could retrieve");
                    }
                }
            }
        }
    }

    /**
     * Get WatchKey by Path from Key-Path Mapping Cache
     *
     * @param mapping Key-Path Mapping Cache
     * @param path    Path Value
     * @return WatchKey
     */
    private WatchKey getKeyByPath(Map<WatchKey, Path> mapping, Path path) {
        for (Map.Entry<WatchKey, Path> entry : mapping.entrySet()) {
            if (entry.getValue().equals(path)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Send a scanAll event asynchronously
     *
     * @param file  File discovered
     * @param topic Event topic
     */
    private void postEvent(File file, String topic) {
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(FileService.FILE_PROPERTY, file);
        Event event = new Event(topic, properties);
        logger.debug("Send a file {} watch event to topic {}.", file.getAbsolutePath(), topic);
        this.eventAdmin.postEvent(event);
    }
}
