package org.chii2.mediaserver.content.common.container;

import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.content.item.VisualItem;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;

import java.util.List;

/**
 * Image Container for Windows Media Connect (Windows Media Player) related devices
 * Represent folders in storage device, contain pictures or sub folder
 */
public class PicturesStorageFolderContainer extends Container implements VisualContainer {
    // Total Child Count
    private long totalChildCount;

    public PicturesStorageFolderContainer(String id, String title) {
        super();

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
    public long getTotalChildCount() {
        return this.totalChildCount;
    }

    @Override
    public void setTotalChildCount(long totalChildCount) {
        this.totalChildCount = totalChildCount;
    }

    @Override
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        // Read from library
        List<? extends VisualItem> photos = contentManager.getPhotosByAlbum(getTitle(), getId(), startIndex, maxCount, orderBy);
        // Total count
        long count = contentManager.getPhotosCountByAlbum(getTitle());
        // Add children
        if (photos != null && count > 0) {
            for (VisualItem photo : photos) {
                addItem((Item) photo);
            }
            setChildCount(photos.size());
            setTotalChildCount(count);
        } else {
            setChildCount(0);
            setTotalChildCount(0);
        }
    }
}
