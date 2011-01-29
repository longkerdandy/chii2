package org.chii2.mediaserver.upnp.content;

import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.container.Container;

/**
 * UPnP / DLNA Visual Container
 */
public abstract class VisualContainer extends Container {

    /**
     * Load contents into container, this should be called before use/access container's item(or sub-container)
     */
    public abstract void loadContents();
}
