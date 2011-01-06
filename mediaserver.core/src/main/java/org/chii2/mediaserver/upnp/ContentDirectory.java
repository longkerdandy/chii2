package org.chii2.mediaserver.upnp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.teleal.cling.support.contentdirectory.ContentDirectoryErrorCode;
import org.teleal.cling.support.contentdirectory.ContentDirectoryException;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.BrowseFlag;
import org.teleal.cling.support.model.BrowseResult;
import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.SortCriterion;

/**
 * ContentDirectory Service for UPnP AV/DLNA Media Server
 */
public class ContentDirectory extends AbstractContentDirectoryService {

    // Logger
    private Logger logger;

    public ContentDirectory() {
        logger = LoggerFactory.getLogger("org.chii2.mediaserver.core");
    }

    @Override
    public BrowseResult browse(String objectID, BrowseFlag browseFlag,
                               String filter,
                               long firstResult, long maxResults,
                               SortCriterion[] orderBy) throws ContentDirectoryException {
        logger.debug("ContentDirectory receive browse request with ObjectID:<{}>, BrowseFlag:<{}>.", objectID, browseFlag);
        try {
            DIDLContent didl = new DIDLContent();
            return new BrowseResult(new DIDLParser().generate(didl), 0, 0);
        } catch (Exception e) {
            logger.error("ContentDirectory process browse request with exception:{}", e.getMessage());
            throw new ContentDirectoryException(ContentDirectoryErrorCode.CANNOT_PROCESS, e.getMessage());
        }
    }
}
