package org.chii2.medialibrary.event;

import org.chii2.medialibrary.api.file.FileService;
import org.chii2.medialibrary.api.persistence.PersistenceService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.provider.ImageInfoProviderService;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Image Event Handler handle all kinds of event related to images.
 */
public class ImageHandler implements EventHandler {
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin Service
    private EventAdmin eventAdmin;
    // Injected Persistence Service
    private PersistenceService persistenceService;
    //Configuration FIle
    private final static String CONFIG_FILE = "org.chii2.medialibrary.core";
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.event");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library ImageHandler init.");
        Dictionary props = null;
        // Read properties from ConfigAdmin Service
        try {
            Configuration config = configAdmin.getConfiguration(CONFIG_FILE);
            props = config.getProperties();
        } catch (IOException e) {
            logger.error("ImageHandler fail to load configuration with exception: {}.", e.getMessage());
        }
        // Load each configuration
        if (props == null || props.isEmpty()) {
            logger.error("ImageHandler load configuration <{}> with error.", CONFIG_FILE);
        } else {
        }
    }

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library ImageHandler destroy.");
    }

    @Override
    public void handleEvent(Event event) {
        // Image Scan Event
        if (FileService.IMAGE_SCAN_PROVIDED_TOPIC.equals(event.getTopic())) {
            // Get List of file names from event, this should be fine
            @SuppressWarnings("unchecked")
            List<File> files = (List<File>) event.getProperty(FileService.FILE_PROPERTY);
            logger.debug("Receive a image scan event with {} records.", files.size());
            this.postImageInfoRequestEvent(files);
        } else if (ImageInfoProviderService.IMAGE_INFO_PROVIDED_TOPIC.equals(event.getTopic())) {
            // Get List of Image Information from event, this should be fine
            @SuppressWarnings("unchecked")
            List<Image> images = (List<Image>) event.getProperty(ImageInfoProviderService.IMAGE_INFO_PROPERTY);
            logger.debug("Receive a image information provided event with {} records.", images.size());
            // Purge current images
            persistenceService.deleteImages();
            // Merge into DB
            if (images != null) {
                logger.info("Merge {} images into database", images.size());
                for (Image image : images) {
                    persistenceService.merge(image);
                }
            }
        } else if (ImageInfoProviderService.IMAGE_INFO_FAILED_TOPIC.equals(event.getTopic())) {
            // Get List of file names from event, this should be fine
            @SuppressWarnings("unchecked")
            List<File> files = (List<File>) event.getProperty(ImageInfoProviderService.IMAGE_FILE_PROPERTY);
            logger.debug("Receive a image information failed event with {} records.", files.size());
        }
    }

    /**
     * Send a image information request event
     *
     * @param files Files to be parsed
     */
    private void postImageInfoRequestEvent(List<File> files) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(ImageInfoProviderService.IMAGE_FILE_PROPERTY, files);
        // Send a event
        Event event = new Event(ImageInfoProviderService.IMAGE_INFO_REQUEST_TOPIC, properties);
        logger.debug("Send a image information request event with {} files.", files.size());
        eventAdmin.postEvent(event);
    }

    /**
     * Inject Config Admin
     *
     * @param configAdmin Config Admin
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

    /**
     * Inject Persistence Service
     *
     * @param persistenceService Persistence Service
     */
    @SuppressWarnings("unused")
    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}
