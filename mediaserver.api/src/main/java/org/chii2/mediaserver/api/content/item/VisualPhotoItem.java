package org.chii2.mediaserver.api.content.item;

import org.chii2.mediaserver.api.upnp.Filter;

/**
 * Visual Photo Item
 */
public class VisualPhotoItem extends VisualPictureItem {
    // Filter
    protected Filter filter;
    // Photo Class
    public static final Class CLASS = new Class("object.item.imageItem.photo");

    public VisualPhotoItem() {
        setClazz(CLASS);
    }
}
