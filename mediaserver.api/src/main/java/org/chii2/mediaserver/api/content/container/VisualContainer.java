package org.chii2.mediaserver.api.content.container;

import org.chii2.mediaserver.api.content.ContentManager;
import org.teleal.cling.support.model.SortCriterion;

/**
 * UPnP / DLNA Visual Container
 */
public interface VisualContainer {

    /**
     * Get total child count (matches in database)
     *
     * @return Total Child Count
     */
    public long getTotalChildCount();

    /**
     * Set total child count (matches in database)
     */
    public void setTotalChildCount(long totalChildCount);

    /**
     * Load contents into container, this should be called before use/access container's item(or sub-container)
     *
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     * @param contentManager Content Manager
     */
    public abstract void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager);
}
