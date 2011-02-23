package org.chii2.mediaserver.api.content.container.common;

import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.library.Library;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.WriteStatus;

/**
 * Image Container for XBox
 * Contains all containers and items relating to pictures
 */
public class PicturesContainer extends VisualContainer {

    public PicturesContainer(Library library) {
        super(library);

        // Pictures Container ID: 3
        setId("3");
        // Parent container is Root Container
        setParentID("0");
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
    public void loadContents() {
        addContainer(new PicturesFoldersContainer(library));
        setChildCount(1);
    }
}
