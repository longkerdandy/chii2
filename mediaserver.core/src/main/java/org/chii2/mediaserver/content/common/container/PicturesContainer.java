package org.chii2.mediaserver.content.common.container;

import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.content.common.CommonContentManager;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;
import org.teleal.cling.support.model.container.Container;

/**
 * Image Container
 * Contains all containers and items relating to pictures
 */
public class PicturesContainer extends Container implements VisualContainer {

    // Filter
    private String filter;
    // Total Child Count
    private long totalChildCount;

    /**
     * Constructor
     *
     * @param filter Content Filter
     */
    public PicturesContainer(String filter) {
        this(filter, CommonContentManager.PICTURES_ID, CommonContentManager.ROOT_ID);
    }

    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Container ID
     * @param parentId Parent ID
     */
    public PicturesContainer(String filter, String id, String parentId) {
        super();

        this.filter = filter;

        // Pictures Container ID: 3
        setId(id);
        // Parent container is Root Container
        setParentID(parentId);
        // Title TODO: This should be I18N
        setTitle("Pictures");
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
    public long getTotalChildCount() {
        return this.totalChildCount;
    }

    @Override
    public void setTotalChildCount(long totalChildCount) {
        this.totalChildCount = totalChildCount;
    }

    @Override
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        addContainer(new PicturesFoldersContainer(filter));
        setChildCount(1);
        setTotalChildCount(1);
    }
}
