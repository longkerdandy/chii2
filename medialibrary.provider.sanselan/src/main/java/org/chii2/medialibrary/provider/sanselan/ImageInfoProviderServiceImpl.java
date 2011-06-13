package org.chii2.medialibrary.provider.sanselan;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.medialibrary.api.persistence.factory.ImageFactory;
import org.chii2.medialibrary.api.provider.ImageInfoProviderService;
import org.chii2.medialibrary.provider.sanselan.consumer.ImageAnalyzer;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Image Information Provider Service based on Sanselan
 */
public class ImageInfoProviderServiceImpl implements ImageInfoProviderService, EventHandler {
    // Request queue
    protected BlockingQueue<List<File>> queue;
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin service
    private EventAdmin eventAdmin;
    // Image Factory
    private ImageFactory imageFactory;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.sanselan");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Sanselan Provider (Image Information) init.");
        // Init queue
        this.queue = new LinkedBlockingQueue<List<File>>();
        // Start Image Analyzer
        Thread thread = new Thread(new ImageAnalyzer(this.queue, this.eventAdmin, this.imageFactory));
        thread.setDaemon(false);
        thread.start();
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Sanselan Provider (Image Information) destroy.");
    }

    @Override
    public String getProviderName() {
        return "Sanselan";
    }

    @Override
    public void getImageInformation(File imageFile) {
        List<File> fileList = new ArrayList<File>();
        fileList.add(imageFile);
        this.getImageInformation(fileList);
    }

    @Override
    public void getImageInformation(List<File> imageFiles) {
        try {
            this.queue.put(imageFiles);
        } catch (InterruptedException e) {
            logger.error("Provider producer has been interrupted with error: {}.", ExceptionUtils.getMessage(e));
        }
    }

    @Override
    public void handleEvent(Event event) {
        if (ImageInfoProviderService.IMAGE_INFO_REQUEST_TOPIC.equals(event.getTopic())) {
            @SuppressWarnings("unchecked")
            List<File> files = (List<File>) event.getProperty(ImageInfoProviderService.IMAGE_FILE_PROPERTY);
            logger.debug("Receive a image information request event with {} records.", files.size());
            // Add request to queue
            try {
                this.queue.put(files);
            } catch (InterruptedException e) {
                logger.error("Provider producer has been interrupted with error: {}.", ExceptionUtils.getMessage(e));
            }
        }
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

    /**
     * Inject Image Factory
     *
     * @param imageFactory Image Factory
     */
    public void setImageFactory(ImageFactory imageFactory) {
        this.imageFactory = imageFactory;
    }
}
