package org.chii2.mediaserver.upnp.content.wmc;

import org.chii2.mediaserver.upnp.content.VisualContainer;
import org.teleal.cling.support.model.DIDLObject;

/**
 * Container for Windows Media Connect (Windows Media Player) related devices
 * Represent folders in storage device
 */
public abstract class StorageFolderContainer extends VisualContainer {

    public StorageFolderContainer() {
        // May used in Container Property Creator (part of UPnP protocol standard)
        setCreator("System");
        // May used in Container Property Clazz (part of UPnP protocol standard)
        setClazz(new DIDLObject.Class("object.container.storageFolder"));
    }
}
