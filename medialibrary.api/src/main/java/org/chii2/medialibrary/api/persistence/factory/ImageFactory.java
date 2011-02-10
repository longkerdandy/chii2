package org.chii2.medialibrary.api.persistence.factory;

import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.ImageFile;

/**
 * Image Factory
 */
public interface ImageFactory {

    /**
     * Image Factory method
     *
     * @return New Image object
     */
    public Image createImage();

    /**
     * Image File Factory method
     *
     * @return New Image File object
     */
    public ImageFile createImageFile();
}
