package org.chii2.medialibrary.provider.graphicsmagick;

import org.chii2.medialibrary.api.persistence.factory.ImageFactory;
import org.chii2.medialibrary.api.provider.ImageInfoProviderService;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Image Information Provider, this provider use GraphicsMagick software to analyze/convert/format image files.
 */
public class ImageInfoProviderServiceImpl implements ImageInfoProviderService {
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin service
    private EventAdmin eventAdmin;
    // Image Factory
    private ImageFactory imageFactory;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.graphicsmagick");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library GraphicsMagick Provider init.");
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library GraphicsMagick Provider destroy.");
    }

    @Override
    public String getProviderName() {
        return "GraphicsMagick";
    }

    @Override
    public void getImageInformation(File imageFile) {
        List<File> fileList = new ArrayList<File>();
        fileList.add(imageFile);
        getImageInformation(fileList);
    }

    @Override
    public void getImageInformation(List<File> imageFiles) {
        logger.debug("Chii2 Media Library start a new GraphicsMagick Image Parser thread.");
        Thread imageParseThread = new Thread(new ImageAnalyzer(imageFiles, eventAdmin, imageFactory));
        imageParseThread.setDaemon(false);
        imageParseThread.start();
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
     * Inject ImageFactory service
     *
     * @param imageFactory ImageFactory service
     */
    @SuppressWarnings("unused")
    public void setImageFactory(ImageFactory imageFactory) {
        this.imageFactory = imageFactory;
    }
}
