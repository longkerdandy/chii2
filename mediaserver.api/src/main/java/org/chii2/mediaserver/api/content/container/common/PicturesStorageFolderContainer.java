package org.chii2.mediaserver.api.content.container.common;

import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.content.item.common.PhotoItem;
import org.chii2.mediaserver.api.library.Library;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;

import java.util.List;

/**
 * Image Container for Windows Media Connect (Windows Media Player) related devices
 * Represent folders in storage device, contain pictures or sub folder
 */
public class PicturesStorageFolderContainer extends VisualContainer {

    public PicturesStorageFolderContainer(String id, String title, Library library) {
        super(library);

        // Pictures Storage Folder Container ID
        setId(id);
        // Parent container is Root Container
        setParentID("16");
        // Title
        setTitle(title);
        // May used in Container Property Creator (part of UPnP protocol standard)
        setCreator("System");
        // May used in Container Property Clazz (part of UPnP protocol standard)
        setClazz(new DIDLObject.Class("object.container.storageFolder"));
        // Restricted
        setRestricted(true);
        // Searchable
        setSearchable(false);
        // Writable
        setWriteStatus(WriteStatus.NOT_WRITABLE);
    }

    @Override
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy) {
        // Read from library
        List<PhotoItem> photos = library.getPhotosByAlbum(getTitle(), getId(), startIndex, maxCount, orderBy);
        // Total count
        long count = library.getPhotosCountByAlbum(getTitle());
        // Add children
        if (photos != null && count > 0) {
            for (PhotoItem photo : photos) {
                addItem(photo);
            }
            setChildCount(photos.size());
            setTotalChildCount(count);
        } else {
            setChildCount(0);
            setTotalChildCount(0);
        }
    }
}
