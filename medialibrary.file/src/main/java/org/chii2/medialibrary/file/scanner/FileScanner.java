package org.chii2.medialibrary.file.scanner;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.medialibrary.api.file.FileService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * FileScanner is used to scan all directories for a give type of file
 */
public class FileScanner implements Runnable {
    // Flag
    public volatile boolean shouldStop = false;
    // Request queue
    private final BlockingQueue<Map<String, Object>> queue;
    // EventAdmin
    private final EventAdmin eventAdmin;
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.file.scanner");

    /**
     * Constructor
     *
     * @param queue      Request Queue
     * @param eventAdmin EventAdmin
     */
    public FileScanner(BlockingQueue<Map<String, Object>> queue, EventAdmin eventAdmin) {
        this.queue = queue;
        this.eventAdmin = eventAdmin;
    }

    @Override
    public void run() {
        try {
            while (!this.shouldStop) {
                // Read request
                Map<String, Object> request = this.queue.take();
                @SuppressWarnings("unchecked")
                List<Path> requestDirectories = (List<Path>) request.get(FileService.DIRECTORY_PROPERTY);
                @SuppressWarnings("unchecked")
                final DirectoryStream.Filter<Path> filter = (DirectoryStream.Filter<Path>) request.get(FileService.FILTER_PROPERTY);
                String topic = (String) request.get(FileService.TOPIC_PROPERTY);
                // Result
                final List<Path> files = new ArrayList<>();
                // Scan
                logger.debug("File Scanner process start.");
                if (requestDirectories != null && !requestDirectories.isEmpty()) {
                    for (Path directory : requestDirectories) {
                        // Register directory and sub-directories
                        try {
                            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                                @Override
                                public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attributes) {
                                    // TODO: Hidden or SymbolicLink
                                    return FileVisitResult.CONTINUE;
                                }

                                @Override
                                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                                    try {
                                        if (filter.accept(file)) {
                                            // Add to result list
                                            files.add(file);
                                        }
                                    } catch (IOException e) {
                                        logger.error("I/O error when filter file {}: {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", file, ExceptionUtils.getMessage(e));
                                    }
                                    return FileVisitResult.CONTINUE;
                                }

                                @Override
                                public FileVisitResult visitFileFailed(Path path, IOException e) {
                                    // Log error, not throw exception
                                    logger.warn("I/O error when walk file tree: {}.", ExceptionUtils.getMessage(e));
                                    return FileVisitResult.CONTINUE;
                                }

                                @Override
                                public FileVisitResult postVisitDirectory(Path path, IOException e) {
                                    // Log error, not throw exception
                                    if (e != null) {
                                        logger.warn("I/O error when walk file tree: {}.", ExceptionUtils.getMessage(e));
                                    }
                                    return FileVisitResult.CONTINUE;
                                }
                            });
                        } catch (SecurityException e) {
                            logger.error("Security error when walk file tree: {}, please check access permission.", ExceptionUtils.getMessage(e));
                        } catch (IOException e) {
                            // Should not been here
                            logger.error("I/O error when walk file tree: {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", ExceptionUtils.getMessage(e));
                        }
                    }
                    if (!files.isEmpty()) {
                        postEvent(files, topic);
                    }
                }
                logger.debug("File Scanner process stop.");
            }
        } catch (InterruptedException e) {
            logger.error("File Scanner has been interrupted with error: {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Send a scanAll event asynchronously
     *
     * @param files Files discovered
     * @param topic Event topic
     */
    private void postEvent(List<Path> files, String topic) {
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(FileService.SCAN_PATH_PROPERTY, files);
        Event event = new Event(topic, properties);
        logger.debug("Send a file scan event with {} records to topic {}.", files.size(), topic);
        this.eventAdmin.postEvent(event);
    }
}
