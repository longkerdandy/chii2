package org.chii2.medialibrary.provider.sanselan;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.medialibrary.api.persistence.factory.ImageFactory;
import org.chii2.medialibrary.api.provider.ImageFileInfoProviderService;
import org.chii2.medialibrary.provider.sanselan.analyzer.ImageAnalyzer;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Image Information Provider Service based on Sanselan
 */
public class ImageFileInfoProviderServiceImpl implements ImageFileInfoProviderService, EventHandler {
    // Request queue
    private BlockingQueue<Path> queue;
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin service
    private EventAdmin eventAdmin;
    // Image Factory
    private ImageFactory imageFactory;
    // Image Analyzer Thread
    private ImageAnalyzer imageAnalyzer;
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.sanselan");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Sanselan Provider (Image Information) init.");
        // Init queue
        this.queue = new LinkedBlockingQueue<>();
        // Start Image Analyzer
        this.imageAnalyzer = new ImageAnalyzer(this.queue, this.eventAdmin, this.imageFactory);
        Thread thread = new Thread(this.imageAnalyzer);
        thread.setDaemon(false);
        thread.start();
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Sanselan Provider (Image Information) destroy.");
        // Stop Image Analyzer
        this.imageAnalyzer.shouldStop = true;
    }

    @Override
    public String getProviderName() {
        return "Sanselan";
    }

    @Override
    public void getImageInformation(Path imageFile) {
        try {
            this.queue.put(imageFile);
        } catch (InterruptedException e) {
            logger.error("Provider producer has been interrupted with error: {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", ExceptionUtils.getMessage(e));
        }
    }

    @Override
    public void getImageInformation(List<Path> imageFiles) {
        try {
            for (Path imageFile : imageFiles) {
                this.queue.put(imageFile);
            }
        } catch (InterruptedException e) {
            logger.error("Provider producer has been interrupted with error: {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", ExceptionUtils.getMessage(e));
        }
    }

    @Override
    public void handleEvent(Event event) {
        if (ImageFileInfoProviderService.IMAGE_FILE_INFO_REQUEST_TOPIC.equals(event.getTopic())) {
            @SuppressWarnings("unchecked")
            List<Path> files = (List<Path>) event.getProperty(ImageFileInfoProviderService.IMAGE_PATH_PROPERTY);
            logger.debug("Receive a image information request event with {} records.", files.size());
            // Get Image Information
            this.getImageInformation(files);
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
    @SuppressWarnings("unused")
    public void setImageFactory(ImageFactory imageFactory) {
        this.imageFactory = imageFactory;
    }
}
