package org.chii2.mediaserver.upnp.content.wmc;

import org.chii2.mediaserver.upnp.content.VisualContainer;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.WriteStatus;

/**
 * Image Container for Windows Media Connect (Windows Media Player) related devices
 * Contains all containers and pictures represent folders in storage
 */
public class PicturesFoldersContainer extends VisualContainer {

    public PicturesFoldersContainer() {
        super();

        // Pictures Folders Container ID: 16
        setId("16");
        // Parent container is Root Container
        setParentID("3");
        // Title TODO: This should be I18N
        setTitle("Pictures/Folders");
        // May used in Container Property Creator (part of UPnP protocol standard)
        setCreator("System");
        // May used in Container Property Clazz (part of UPnP protocol standard)
        setClazz(new DIDLObject.Class("object.container"));
        // Restricted
        setRestricted(true);
        // Searchable
        setSearchable(false);
        // Writable
        setWriteStatus(WriteStatus.NOT_WRITABLE);
    }

    @Override
    public void loadContents() {
        addContainer(new PicturesStorageFolderContainer("99999", "Singapore"));
        setChildCount(1);
    }
}
