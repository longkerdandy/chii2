package org.chii2.mediaserver.api.content;

import org.chii2.mediaserver.api.library.Library;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;

/**
 * Content Manager used for manage media content.
 * Content Directory Service use this to provide content based on different client.
 */
public interface ContentManager {

    /**
     * Whether this Content Manager match client heads.
     * This method used for choose appropriate Content Manager.
     * @param headers UPnP Headers (Http Headers)
     * @return True if client match the Content Manager
     */
    public boolean isMatch(UpnpHeaders headers);

    /**
     * Find Object (Container or Item) based on ID
     * @param objectId Object ID
     * @return Object (Container or Item), null if not found
     */
    public DIDLObject findObject(String objectId, String filter, long startIndex, long requestCount, SortCriterion[] orderBy, Library library);
}
