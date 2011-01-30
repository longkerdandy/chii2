package org.chii2.mediaserver.upnp.content.wmc;

import org.teleal.cling.support.model.WriteStatus;

/**
 * Image Container for Windows Media Connect (Windows Media Player) related devices
 * Represent folders in storage device, contain pictures or sub folder
 */
public class PicturesStorageFolderContainer extends StorageFolderContainer {

    public PicturesStorageFolderContainer(String id, String title) {
        super();

        // Pictures Storage Folder Container ID
        setId(id);
        // Parent container is Root Container
        setParentID("16");
        // Title TODO: This should be I18N
        setTitle(title);
        // Restricted
        setRestricted(true);
        // Searchable
        setSearchable(false);
        // Writable
        setWriteStatus(WriteStatus.NOT_WRITABLE);
    }

    @Override
    public void loadContents() {
        //TODO Fake
        PhotoItem samplePhoto = new PhotoItem("111111", "999999", "Koala", "Samples");
        this.addItem(samplePhoto);
        this.setChildCount(1);
    }
}
