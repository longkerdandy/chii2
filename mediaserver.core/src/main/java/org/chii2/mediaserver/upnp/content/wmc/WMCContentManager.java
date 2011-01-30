package org.chii2.mediaserver.upnp.content.wmc;

import org.chii2.mediaserver.api.upnp.content.ContentManager;
import org.chii2.mediaserver.upnp.content.VisualContainer;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;

/**
 * ContentManager for Windows Media Connect and Windows Media Player
 */
public class WMCContentManager implements ContentManager {

    @Override
    public DIDLObject findObjectWithId(String id) {
        if (id.equalsIgnoreCase("0")) {
            VisualContainer container = new RootContainer();
            container.loadContents();
            return container;
        } else if (id.equalsIgnoreCase("3")) {
            VisualContainer container = new PicturesContainer();
            container.loadContents();
            return container;
        } else if (id.equalsIgnoreCase("16")) {
            VisualContainer container = new PicturesFoldersContainer();
            container.loadContents();
            return container;
        } else if (id.equalsIgnoreCase("99999")) {
            VisualContainer container = new PicturesStorageFolderContainer("99999", "Singapore");
            container.loadContents();
            return container;
        } else if (id.equalsIgnoreCase("111111")) {
            Item item = new PhotoItem("111111", "999999", "Koala", "Samples");
            return item;
        } else {
            return null;
        }
    }
}
