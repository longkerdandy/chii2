package org.chii2.mediaserver.api.content.container;

import org.chii2.mediaserver.api.library.Library;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.container.Container;

/**
 * UPnP / DLNA Visual Container
 */
public abstract class VisualContainer extends Container {

    // Library
    protected Library library;
    // Total Matches
    private long totalChildCount;

    /**
     * Constructor
     *
     * @param library Library
     */
    public VisualContainer(Library library) {
        super();
        this.library = library;
    }

    /**
     * Get total child count (matches in database)
     *
     * @return Total Child Count
     */
    public long getTotalChildCount() {
        return totalChildCount;
    }

    /**
     * Set total child count (matches in database)
     */
    public void setTotalChildCount(long totalChildCount) {
        this.totalChildCount = totalChildCount;
    }

    /**
     * Load contents into container, this should be called before use/access container's item(or sub-container)
     *
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     */
    public abstract void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy);
}
