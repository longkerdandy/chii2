package org.chii2.medialibrary.event;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.medialibrary.api.file.FileService;
import org.chii2.medialibrary.api.persistence.PersistenceService;
import org.chii2.medialibrary.api.persistence.entity.ImageFile;
import org.chii2.medialibrary.api.provider.ImageFileInfoProviderService;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
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
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.event");

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
            logger.error("ImageHandler fail to load configuration with exception: {}.", ExceptionUtils.getMessage(e));
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
    @SuppressWarnings("unchecked")
    public void handleEvent(Event event) {
        if (FileService.IMAGE_SCAN_PROVIDED_TOPIC.equals(event.getTopic())) {
            List<Path> files = (List<Path>) event.getProperty(FileService.SCAN_PATH_PROPERTY);
            logger.debug("Receive a image scan event with {} records.", files.size());
            this.postImageFileInfoRequestEvent(files);
        } else if (FileService.IMAGE_WATCH_CREATE_TOPIC.equals(event.getTopic())) {
            Path path = (Path) event.getProperty(FileService.WATCH_PATH_PROPERTY);
            List<Path> files = new ArrayList<>();
            files.add(path);
            logger.debug("Receive a image watch create event for: {}.", path);
            this.postImageFileInfoRequestEvent(files);
        } else if (FileService.IMAGE_WATCH_DELETE_TOPIC.equals(event.getTopic())) {
            Path path = (Path) event.getProperty(FileService.WATCH_PATH_PROPERTY);
            logger.debug("Receive a image watch delete event for: {}.", path);
            this.persistenceService.deleteImage(path.toString());
        } else if (FileService.IMAGE_WATCH_MODIFY_TOPIC.equals(event.getTopic())) {
            Path path = (Path) event.getProperty(FileService.WATCH_PATH_PROPERTY);
            List<Path> files = new ArrayList<>();
            files.add(path);
            logger.debug("Receive a image watch modify event for: {}.", path);
            this.postImageFileInfoRequestEvent(files);
        } else if (ImageFileInfoProviderService.IMAGE_FILE_INFO_PROVIDED_TOPIC.equals(event.getTopic())) {
            Path path = (Path) event.getProperty(ImageFileInfoProviderService.IMAGE_PATH_PROPERTY);
            ImageFile imageFile = (ImageFile) event.getProperty(ImageFileInfoProviderService.IMAGE_FILE_INFO_PROPERTY);
            logger.debug("Receive a image file information provided event for {}.", path);
            // Synchronize to DB
            this.persistenceService.synchronizeImage(imageFile);
        } else if (ImageFileInfoProviderService.IMAGE_FILE_INFO_FAILED_TOPIC.equals(event.getTopic())) {
            Path path = (Path) event.getProperty(ImageFileInfoProviderService.IMAGE_PATH_PROPERTY);
            logger.debug("Receive a image file information failed event for: {}.", path);
        }
    }

    /**
     * Send a image information request event
     *
     * @param files Files to be parsed
     */
    private void postImageFileInfoRequestEvent(List<Path> files) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(ImageFileInfoProviderService.IMAGE_PATH_PROPERTY, files);
        // Send a event
        Event event = new Event(ImageFileInfoProviderService.IMAGE_FILE_INFO_REQUEST_TOPIC, properties);
        logger.debug("Send a image information request event with {} files.", files.size());
        this.eventAdmin.postEvent(event);
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
