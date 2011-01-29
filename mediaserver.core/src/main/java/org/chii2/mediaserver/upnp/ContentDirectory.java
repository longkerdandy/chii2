package org.chii2.mediaserver.upnp;

import org.chii2.mediaserver.api.upnp.content.ContentManager;
import org.chii2.mediaserver.upnp.content.wmc.WMCContentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.teleal.cling.support.contentdirectory.ContentDirectoryErrorCode;
import org.teleal.cling.support.contentdirectory.ContentDirectoryException;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.*;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;

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
            DIDLContent didlContent = new DIDLContent();

            // TODO: When the request is XBox or WMP, choose WMCContentManager
            ContentManager contentManager = new WMCContentManager();

            // Search and get the object from the given id
            DIDLObject didlObject = contentManager.findObjectWithId(objectID);

            // Not found, return empty result
            if (didlObject == null) {
                logger.info("Object not found with ObjectID:<{}>", objectID);
                return new BrowseResult(new DIDLParser().generate(didlContent), 0, 0);
            }

            int count = 0;
            int totalMatches = 0;
            // Browse metadata
            if (browseFlag.equals(BrowseFlag.METADATA)) {
                if (didlObject instanceof Container) {
                    logger.info("Browsing metadata of container:<{}>" + didlObject.getId());
                    didlContent.addContainer((Container) didlObject);
                    count++;
                    totalMatches++;
                } else if (didlObject instanceof Item) {
                    logger.info("Browsing metadata of item:<{}>", didlObject.getId());
                    didlContent.addItem((Item) didlObject);
                    count++;
                    totalMatches++;
                }
            }
            // Browse children
            else if (browseFlag.equals(BrowseFlag.DIRECT_CHILDREN)) {
                if (didlObject instanceof Container) {
                    logger.info("Browsing children of container:<{}>", didlObject.getId());
                    Container container = (Container) didlObject;
                    boolean maxReached = maxResults == 0;
                    totalMatches = totalMatches + container.getContainers().size();
                    for (Container subContainer : container.getContainers()) {
                        if (maxReached) break;
                        if (firstResult > 0 && count == firstResult) continue;
                        didlContent.addContainer(subContainer);
                        count++;
                        if (count >= maxResults) maxReached = true;
                    }
                    totalMatches = totalMatches + container.getItems().size();
                    for (Item item : container.getItems()) {
                        if (maxReached) break;
                        if (firstResult > 0 && count == firstResult) continue;
                        didlContent.addItem(item);
                        count++;
                        if (count >= maxResults) maxReached = true;
                    }
                }
            }
            logger.info("Browsing result count: <{}> and total matches: <{}>", count, totalMatches);
            return new BrowseResult(new DIDLParser().generate(didlContent), count, totalMatches);
        } catch (Exception e) {
            logger.error("ContentDirectory process browse request with exception:{}", e.getMessage());
            throw new ContentDirectoryException(ContentDirectoryErrorCode.CANNOT_PROCESS, e.getMessage());
        }
    }
}
