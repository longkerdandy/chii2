package org.chii2.mediaserver.content.common;

import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.content.container.common.PicturesContainer;
import org.chii2.mediaserver.api.content.container.common.PicturesFoldersContainer;
import org.chii2.mediaserver.api.content.container.common.PicturesStorageFolderContainer;
import org.chii2.mediaserver.api.content.container.common.RootContainer;
import org.chii2.mediaserver.api.library.Library;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;

/**
 * Content Manger for common clients
 */
public class CommonContentManager implements ContentManager{

    @Override
    public boolean isMatch(UpnpHeaders headers) {
        // Always return true
        return true;
    }

    @Override
    public DIDLObject findObject(String objectId, String filter, long startIndex, long requestCount, SortCriterion[] orderBy, Library library) {
        // Root Container
        if (library.isRootContainer(objectId)) {
            VisualContainer container = new RootContainer(library);
            container.loadContents(startIndex, requestCount, orderBy);
            return container;
        }
        // Pictures Container
        else if (library.isPicturesContainer(objectId)) {
            VisualContainer container = new PicturesContainer(library);
            container.loadContents(startIndex, requestCount, orderBy);
            return container;
        }
        // Pictures Folders Container
        else if (library.isPicturesFoldersContainer(objectId)) {
            VisualContainer container = new PicturesFoldersContainer(library);
            container.loadContents(startIndex, requestCount, orderBy);
            return container;
        }
        // Pictures Storage Folder Container (Dynamic)
        else if (library.isPicturesStorageFolderContainer(objectId)) {
            VisualContainer container = new PicturesStorageFolderContainer(objectId, library.getContainerTitle(objectId), library);
            container.loadContents(startIndex, requestCount, orderBy);
            return container;
        }
        // Photo Item
        else if (library.isPhotoItem(objectId)) {
            return library.getPhotoById(objectId);
        }
        // Invalid
        else {
            // TODO Maybe should throw a NO_SUCH_OBJECT exception, instead of null result
            return null;
        }
    }
}
