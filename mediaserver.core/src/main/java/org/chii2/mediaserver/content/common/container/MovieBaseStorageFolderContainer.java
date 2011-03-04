package org.chii2.mediaserver.content.common.container;

import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.content.item.VisualItem;
import org.chii2.mediaserver.content.common.CommonContentManager;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;

import java.util.List;

/**
 * Movie Storage Container
 * This is a fixed dynamic container for video storage folders. UPnP/DLNA doesn't tell difference between Movie/TV Show/Etc but we do.
 * Contains all containers and movies represent folders in storage
 */
public class MovieBaseStorageFolderContainer extends Container implements VisualContainer {

    // Filter
    private String filter;
    // Total Child Count
    private long totalChildCount;

     /**
     * Constructor
     *
     * @param filter Content Filter
     */
    public MovieBaseStorageFolderContainer(String filter) {
        this(filter, CommonContentManager.MOVIE_BASE_STORAGE_FOLDER_ID, CommonContentManager.VIDEO_FOLDERS_ID);
    }

    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Container ID
     * @param parentId Parent ID
     */
    public MovieBaseStorageFolderContainer(String filter, String id, String parentId) {
        super();

        this.filter = filter;

        // Movie Storage Folders Container ID: 1501
        setId(id);
        // Parent container is Video Folders Container
        setParentID(parentId);
        // Title TODO: This should be I18N
        setTitle("Movies");
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
        List<? extends VisualItem> movies = contentManager.getMovies(getId(), filter, startIndex, maxCount, orderBy);
        // Total count
        long count = 1;
        // Add children
        if (movies != null && count > 0) {
            for (VisualItem movie : movies) {
                addItem((Item) movie);
            }
            setChildCount(movies.size());
            setTotalChildCount(count);
        } else {
            setChildCount(0);
            setTotalChildCount(0);
        }
    }
}
