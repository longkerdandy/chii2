package org.chii2.mediaserver.content.common.container;

import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.content.item.VisualPictureItem;
import org.chii2.mediaserver.content.common.CommonContentManager;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;

import java.util.List;

/**
 * Image Container
 * Represent folders in storage device, contain pictures or sub folder
 */
public class PicturesStorageFolderContainer extends VisualContainer {

    /**
     * Constructor
     * @param filter Content Filter
     * @param id Container ID
     * @param title Container Title
     */
    public PicturesStorageFolderContainer(String filter, String id, String title) {
        this(filter, id, CommonContentManager.PICTURES_FOLDERS_ID, title);
    }

    /**
     * Constructor
     * @param filter Content Filter
     * @param id Container ID
     * @param parentId Container Parent ID
     * @param title Container Title
     */
    public PicturesStorageFolderContainer(String filter, String id, String parentId, String title) {
        super();

        this.filter = filter;

        // Pictures Storage Folder Container ID
        setId(id);
        // Parent container is Root Container
        setParentID(parentId);
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
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        // Read from library
        List<? extends VisualPictureItem> photos = contentManager.getPhotosByAlbum(getTitle(), getId(), filter, startIndex, maxCount, orderBy);
        // Total count
        long count = contentManager.getPhotosCountByAlbum(getTitle());
        // Add children
        if (photos != null && count > 0) {
            for (VisualPictureItem photo : photos) {
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
