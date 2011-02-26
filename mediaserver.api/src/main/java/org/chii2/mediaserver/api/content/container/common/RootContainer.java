package org.chii2.mediaserver.api.content.container.common;

import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.library.Library;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;

/**
 * Root Container for XBox
 */
public class RootContainer extends VisualContainer {

    public RootContainer(Library library) {
        super(library);

        // Root Container ID: 0
        setId("0");
        // There is no parent container for Root
        setParentID("-1");
        // Title TODO: This should be I18N
        setTitle("Root");
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
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy) {
        addContainer(new PicturesContainer(library));
        setChildCount(1);
        setTotalChildCount(1);
    }
}
