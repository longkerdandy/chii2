package org.chii2.medialibrary.file;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * FileScanner is used to scan directories for a give type of file
 */
public class FileScanner implements Runnable {

    // Directories to be scanned
    private List<File> directories;
    // File Name Filter
    private FileFilter filter;
    // EventAdmin
    private EventAdmin eventAdmin;
    // EventAdmin Topic
    private String topic;
    // Found Files
    List<File> files = new ArrayList<File>();
    // Logger
    private Logger logger;

    public FileScanner(List<File> directories, FileFilter filter, EventAdmin eventAdmin, String topic) {
        this.directories = directories;
        this.filter = filter;
        this.eventAdmin = eventAdmin;
        this.topic = topic;
        logger = LoggerFactory.getLogger("org.chii2.medialibrary.file");
    }

    @Override
    public void run() {
        logger.debug("File Scanner start.");
        if (directories != null && !directories.isEmpty()) {
            try {
                for (File directory : directories) {
                    if (directory != null && directory.exists() && directory.isDirectory()) {
                        scanFiles(directory, filter);
                    }
                }
            } catch (SecurityException e) {
                logger.warn("File access failed with exception: {}", e.getMessage());
            }
            if (files != null && !files.isEmpty()) {
                sendEvent(files, topic);
            }
        }
        logger.debug("File Scanner stop.");
    }

    /**
     * Scan a directory recursively
     *
     * @param directory Directory to be scanned
     * @param filter    File Name Filter
     */
    private void scanFiles(File directory, FileFilter filter) {
        for (File file : directory.listFiles(filter)) {
            if (file.isDirectory()) {
                scanFiles(file, filter);
            } else {
                files.add(file);
                logger.info("Found a new file: {}.", file.getName());
            }
        }
    }

    /**
     * Send a scan event asynchronously
     *
     * @param files Files discovered
     * @param topic Event topic
     */
    private void sendEvent(List<File> files, String topic) {
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put("files", files);
        Event event = new Event(topic, properties);
        logger.debug("Send a movie scan event with {} records.", files.size());
        eventAdmin.postEvent(event);
    }

}
