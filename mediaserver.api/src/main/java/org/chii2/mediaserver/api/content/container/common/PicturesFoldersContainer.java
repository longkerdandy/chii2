package org.chii2.mediaserver.api.content.container.common;

import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.library.Library;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.WriteStatus;

import java.util.List;

/**
 * Image Container for XBox
 * Contains all containers and pictures represent folders in storage
 */
public class PicturesFoldersContainer extends VisualContainer {

    /**
     * Constructor
     *
     * @param library Library
     */
    public PicturesFoldersContainer(Library library) {
        super(library);

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
    public void loadContents() {
        // Load from library
        List<PicturesStorageFolderContainer> containers = library.getPicturesStorageFolders();
        // Add children
        if (containers != null) {
            for (PicturesStorageFolderContainer container : containers) {
                addContainer(container);
            }
            setChildCount(containers.size());
        } else {
            setChildCount(0);
        }
    }
}
