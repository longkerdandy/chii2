package org.chii2.medialibrary.file.scanner;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.medialibrary.api.file.FileService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * FileScanner is used to scanAll directories for a give type of file
 */
public class FileScanner implements Runnable {
    // Request queue
    protected BlockingQueue<Map<String, Object>> queue;
    // EventAdmin
    private EventAdmin eventAdmin;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.file.scanner");

    /**
     * Constructor
     *
     * @param queue Request Queue
     * @param eventAdmin EventAdmin
     */
    public FileScanner(BlockingQueue<Map<String, Object>> queue, EventAdmin eventAdmin) {
        this.queue = queue;
        this.eventAdmin = eventAdmin;
    }

    @SuppressWarnings({"InfiniteLoopStatement"})
    @Override
    public void run() {
        try {
            while (true) {
                // Read request
                Map<String, Object> request = this.queue.take();
                @SuppressWarnings("unchecked")
                List<File> requestDirectories = (List<File>) request.get(FileService.DIRECTORY_PROPERTY);
                FileFilter filter = (FileFilter) request.get(FileService.FILTER_PROPERTY);
                String topic = (String) request.get(FileService.TOPIC_PROPERTY);
                // Result
                List<File> files = new ArrayList<File>();
                // Scan
                logger.debug("File Scanner start.");
                if (requestDirectories != null && !requestDirectories.isEmpty()) {
                    try {
                        for (File directory : requestDirectories) {
                            if (directory != null && directory.exists() && directory.isDirectory()) {
                                scanFiles(directory, filter, files);
                            }
                        }
                    } catch (SecurityException e) {
                        logger.warn("File access failed with exception: {}", ExceptionUtils.getMessage(e));
                    }
                    if (files != null && !files.isEmpty()) {
                        postEvent(files, topic);
                    }
                }
                logger.debug("File Scanner stop.");
            }
        } catch (InterruptedException e) {
            logger.error("Provider consumer has been interrupted with error: {}.", ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Scan a directory recursively
     *
     * @param directory Directory to be scanned
     * @param filter    File Name Filter
     * @param files     Found Files
     */
    private void scanFiles(File directory, FileFilter filter, List<File> files) {
        for (File file : directory.listFiles(filter)) {
            if (file.isDirectory()) {
                scanFiles(file, filter, files);
            } else {
                files.add(file);
                logger.info("Found a new file: {}.", file.getName());
            }
        }
    }

    /**
     * Send a scanAll event asynchronously
     *
     * @param files Files discovered
     * @param topic Event topic
     */
    private void postEvent(List<File> files, String topic) {
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(FileService.FILE_PROPERTY, files);
        Event event = new Event(topic, properties);
        logger.debug("Send a file scan event with {} records to topic {}.", files.size(), topic);
        eventAdmin.postEvent(event);
    }

}
