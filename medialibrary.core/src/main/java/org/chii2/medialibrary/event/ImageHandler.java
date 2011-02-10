package org.chii2.medialibrary.event;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.file.FileService;
import org.chii2.medialibrary.api.persistence.PersistenceService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.provider.ImageInfoProviderService;
import org.chii2.util.ConfigUtils;
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
    // Injected list of Image Information Provider Service
    private List<ImageInfoProviderService> providerServices;
    //Configuration FIle
    private final static String CONFIG_FILE = "org.chii2.medialibrary.core";
    // Image provider configuration
    private final static String IMAGE_PROVIDER = "image.provider";
    // Preferred Image Information Provider, load from configuration file
    private String preferredProvider = "GraphicsMagick";
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
            // Load preferred image information provider
            String provider = ConfigUtils.loadConfiguration(props, IMAGE_PROVIDER);
            if (StringUtils.isNotBlank(provider)) {
                preferredProvider = provider;
                logger.debug("ImageHandler configuration <{}> loaded.", IMAGE_PROVIDER);
            } else {
                logger.error("ImageHandler configuration <{}> is not valid.", IMAGE_PROVIDER);
            }
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
        if (FileService.IMAGE_SCAN_TOPIC.equals(event.getTopic())) {
            // Get List of file names from event, this should be fine
            @SuppressWarnings("unchecked")
            List<File> files = (List<File>) event.getProperty("files");
            logger.debug("Receive a image scan event with {} records.", files.size());
            doScanProcess(files);
        } else if (ImageInfoProviderService.IMAGE_INFO_PROVIDED_TOPIC.equals(event.getTopic())) {
            // Get List of Image Information from event, this should be fine
            @SuppressWarnings("unchecked")
            List<Image> images = (List<Image>) event.getProperty(ImageInfoProviderService.IMAGE_INFO_PROPERTY);
            logger.debug("Receive a image information provided event with {} records.", images.size());
            doInfoProvidedProcess(images);
        } else if (ImageInfoProviderService.IMAGE_INFO_FAILED_TOPIC.equals(event.getTopic())) {
            // Get List of file names from event, this should be fine
            @SuppressWarnings("unchecked")
            List<File> files = (List<File>) event.getProperty(ImageInfoProviderService.IMAGE_FILE_PROPERTY);
            logger.debug("Receive a image information failed event with {} records.", files.size());
            doInfoFailedProcess(files);
        }
    }

    /**
     * Parse the scanned image files, (current implementation use GraphicsMagick to handle image metadata)
     * and sync to the database
     *
     * @param files Scanned Image Files
     */
    private void doScanProcess(List<File> files) {
        if (providerServices != null && !providerServices.isEmpty()) {
            // Whether provider match the preferred configuration
            boolean providerMatched = false;
            // Loop available providers
            for (ImageInfoProviderService provider : providerServices) {
                if (preferredProvider.equalsIgnoreCase(provider.getProviderName())) {
                    // Matched
                    // Purge current images
                    persistenceService.deleteAllImages();
                    // Use provider parse image information
                    provider.getImageInformation(files);
                    // Mark match found
                    providerMatched = true;
                    // Stop looping
                    break;
                }
            }
            // If not matched
            if (!providerMatched) {
                logger.error("The image information provider {} is not present.", preferredProvider);
            }
        } else {
            // This won't happens
            logger.error("No Image Information provider available");
        }
    }

    /**
     * Merge each image into database
     *
     * @param images Images
     */
    private void doInfoProvidedProcess(List<Image> images) {
        if (images != null && !images.isEmpty()) {
            logger.debug("Merge {} images into database", images.size());
            for (Image image : images) {
                persistenceService.merge(image);
            }
        }
    }

    /**
     * Image Information failed, do nothing atm
     *
     * @param files Image files
     */
    private void doInfoFailedProcess(List<File> files) {
        // Do nothing at the moment
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

    /**
     * Inject Movie Information Provider Services
     *
     * @param providerServices Movie Information Provider Services
     */
    @SuppressWarnings("unused")
    public void setProviderServices(List<ImageInfoProviderService> providerServices) {
        this.providerServices = providerServices;
    }
}
