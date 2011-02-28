package org.chii2.mediaserver.content.common.container;

import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;
import org.teleal.cling.support.model.container.Container;

import java.util.List;

/**
 * Image Container for XBox
 * Contains all containers and pictures represent folders in storage
 */
public class PicturesFoldersContainer extends Container implements VisualContainer {

    // Total Child Count
    private long totalChildCount;

    public PicturesFoldersContainer() {
        super();

        // Pictures Folders Container ID: 16
        setId("16");
        // Parent container is Pictures Container
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
    public long getTotalChildCount() {
        return this.totalChildCount;
    }

    @Override
    public void setTotalChildCount(long totalChildCount) {
        this.totalChildCount = totalChildCount;
    }

    @Override
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        // Load from library
        List<? extends VisualContainer> containers = contentManager.getPicturesStorageFolders(startIndex, maxCount, orderBy);
        long count = contentManager.getPicturesStorageFoldersCount();
        // Add children
        if (containers != null && count > 0) {
            for (VisualContainer container : containers) {
                addContainer((Container) container);
            }
            setChildCount(containers.size());
            setTotalChildCount(count);
        } else {
            setChildCount(0);
            setTotalChildCount(0);
        }
    }
}
