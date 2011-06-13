package org.chii2.mediaserver.api.content.item;

import org.teleal.cling.support.model.item.ImageItem;

/**
 * Visual Picture Item
 */
public abstract class VisualPictureItem extends ImageItem {
    public String getAlbum() {
        return getFirstPropertyValue(Property.UPNP.ALBUM.class);
    }

    public VisualPictureItem setAlbum(String album) {
        replaceFirstProperty(new Property.UPNP.ALBUM(album));
        return this;
    }
}
