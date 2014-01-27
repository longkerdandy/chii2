package org.chii2.medialibrary.persistence.factory;

import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.ImageFile;
import org.chii2.medialibrary.api.persistence.factory.ImageFactory;
import org.chii2.medialibrary.persistence.entity.ImageFileImpl;
import org.chii2.medialibrary.persistence.entity.ImageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Image Factory
 */
public class ImageFactoryImpl implements ImageFactory {

    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.persistence");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library ImageFactory init.");
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library ImageFactory destroy.");
    }

    @Override
    public Image createImage() {
        return new ImageImpl();
    }

    @Override
    public ImageFile createImageFile() {
        return new ImageFileImpl();
    }
}
