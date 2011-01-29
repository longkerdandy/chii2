package org.chii2.mediaserver.api.upnp.content;

import org.teleal.cling.support.model.DIDLObject;

/*
 UPnP / DLNA content manager
 */
public interface ContentManager {

    /**
     * Find the object (may be container or item) by id
     *
     * @param id Object ID (usually provided from ContentDirectory Browse or Search action)
     * @return Object or Null if nothing found
     */
    DIDLObject findObjectWithId(String id);
}
