package org.chii2.mediaserver.api.content.container;

import org.chii2.mediaserver.api.library.Library;
import org.teleal.cling.support.model.container.Container;

/**
 * UPnP / DLNA Visual Container
 */
public abstract class VisualContainer extends Container {

    // Library
    protected Library library;

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
     * Load contents into container, this should be called before use/access container's item(or sub-container)
     */
    public abstract void loadContents();
}
