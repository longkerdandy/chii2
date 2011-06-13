package org.chii2.mediaserver.api.content.item;

/**
 * Visual Photo Item
 */
public class VisualPhotoItem extends VisualPictureItem {
    public static final Class CLASS = new Class("object.item.imageItem.photo");

    public VisualPhotoItem() {
        setClazz(CLASS);
    }
}
