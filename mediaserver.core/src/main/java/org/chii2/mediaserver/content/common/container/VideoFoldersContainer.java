package org.chii2.mediaserver.content.common.container;

import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.content.common.CommonContentManager;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;

/**
 * Video Container
 * Contains all containers and video represent folders in storage
 */
public class VideoFoldersContainer extends VisualContainer {

     /**
     * Constructor
     *
     * @param filter Content Filter
     */
    public VideoFoldersContainer(String filter) {
        this(filter, CommonContentManager.VIDEO_FOLDERS_ID, CommonContentManager.VIDEO_ID);
    }

    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Container ID
     * @param parentId Parent ID
     */
    public VideoFoldersContainer(String filter, String id, String parentId) {
        super();

        this.filter = filter;

        // Video Folders Container ID: 15
        setId(id);
        // Parent container is Video Container
        setParentID(parentId);
        // Title TODO: This should be I18N
        setTitle("Video/Folders");
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
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        addContainer(new MovieBaseStorageFolderContainer(filter));
        setChildCount(1);
        setTotalChildCount(1);
    }
}
