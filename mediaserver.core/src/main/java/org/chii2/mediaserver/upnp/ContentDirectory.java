package org.chii2.mediaserver.upnp;

import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.mediaserver.content.common.CommonContentManager;
import org.chii2.mediaserver.content.xbox.XBoxContentManager;
import org.chii2.transcoder.api.core.TranscoderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.protocol.sync.ReceivingAction;
import org.teleal.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.teleal.cling.support.contentdirectory.ContentDirectoryErrorCode;
import org.teleal.cling.support.contentdirectory.ContentDirectoryException;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.*;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;

import java.util.LinkedList;

/**
 * ContentDirectory Service for UPnP AV/DLNA Media Server
 */
public class ContentDirectory extends AbstractContentDirectoryService {
    // Media Library
    private MediaLibraryService mediaLibrary;
    // HTTP Server
    private HttpServerService httpServer;
    // Transcoder
    private TranscoderService transcoder;
    // Content Manger List
    private LinkedList<ContentManager> contentManagers;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.core");

    /**
     * Constructor
     *
     * @param mediaLibrary Media Library
     * @param httpServer   Http Server
     * @param transcoder   Transcoder
     */
    public ContentDirectory(MediaLibraryService mediaLibrary, HttpServerService httpServer, TranscoderService transcoder) {
        super();
        this.mediaLibrary = mediaLibrary;
        this.httpServer = httpServer;
        this.transcoder = transcoder;
        this.contentManagers = new LinkedList<ContentManager>();
        contentManagers.add(new XBoxContentManager(mediaLibrary, httpServer, transcoder));
        contentManagers.add(new CommonContentManager(mediaLibrary, httpServer, transcoder));
    }

    @Override
    public BrowseResult browse(String objectID, BrowseFlag browseFlag, String filter, long startIndex, long requestCount, SortCriterion[] orderBy) throws ContentDirectoryException {
        logger.debug(String.format("ContentDirectory receive browse request with ObjectID:%s, BrowseFlag:%s, Filter:%s, FirstResult:%s, MaxResults:%s, SortCriterion:%s.", objectID, browseFlag, filter, startIndex, requestCount, getSortCriterionString(orderBy)));
        // Client Headers
        UpnpHeaders headers = ReceivingAction.getRequestMessage().getHeaders();
        // Content Manager based on client
        ContentManager contentManager = getContentManager(headers);
        // Result
        DIDLContent didlContent = new DIDLContent();
        // Search and get the object from the given id
        DIDLObject didlObject = contentManager.findObject(objectID, filter, startIndex, requestCount, orderBy);

        // Not found, return empty result
        if (didlObject == null) {
            logger.info("Object not found with ObjectID:<{}>", objectID);
            // TODO I'm not sure this is correct, maybe should throw a NO_SUCH_OBJECT exception, instead of empty result
            try {
                return new BrowseResult(new DIDLParser().generate(didlContent), 0, 0);
            } catch (Exception e) {
                throw new ContentDirectoryException(ContentDirectoryErrorCode.CANNOT_PROCESS, e.getMessage());
            }
        }

        // Number of count returned
        long numReturned = 0;
        // Total matches
        long totalMatches = 0;

        // Browse metadata
        if (browseFlag.equals(BrowseFlag.METADATA)) {
            if (didlObject instanceof Container) {
                logger.info("Browsing metadata of container:<{}>" + didlObject.getId());
                didlContent.addContainer((Container) didlObject);
                numReturned = totalMatches = 1;
            } else if (didlObject instanceof Item) {
                logger.info("Browsing metadata of item:<{}>", didlObject.getId());
                didlContent.addItem((Item) didlObject);
                numReturned = totalMatches = 1;
            }
        }
        // Browse children
        else if (browseFlag.equals(BrowseFlag.DIRECT_CHILDREN)) {
            if (didlObject instanceof Container) {
                logger.info("Browsing children of container:<{}>", didlObject.getId());
                Container container = (Container) didlObject;
                if (container.getChildCount() <= requestCount) {
                    for (Container subContainer : container.getContainers()) {
                        didlContent.addContainer(subContainer);
                    }
                    for (Item item : container.getItems()) {
                        didlContent.addItem(item);
                    }
                    numReturned = container.getChildCount();
                    totalMatches = ((VisualContainer) container).getTotalChildCount();
                } else {
                    // TODO Maybe should cut the extra results
                    throw new ContentDirectoryException(ContentDirectoryErrorCode.CANNOT_PROCESS, "Returned results count exceeds max request count limit.");
                }
            }
        }

        // Return result
        logger.info("Browsing result numReturned: <{}> and total matches: <{}>", numReturned, totalMatches);
        try {
            return new BrowseResult(new DIDLParser().generate(didlContent), numReturned, totalMatches);
        } catch (Exception e) {
            throw new ContentDirectoryException(ContentDirectoryErrorCode.CANNOT_PROCESS, e.getMessage());
        }
    }

    /**
     * SortCriterion Array to String
     *
     * @param sorts SortCriterion Array
     * @return String
     */
    private String getSortCriterionString(SortCriterion[] sorts) {
        StringBuilder result = new StringBuilder();
        if (sorts != null) {
            for (SortCriterion sort : sorts) {
                result.append(sort);
            }
        }
        return result.toString();
    }

    /**
     * Get suitable Content Manager ofr client
     *
     * @param headers Client UPnP (Http) Headers
     * @return Content Manager
     */
    protected ContentManager getContentManager(UpnpHeaders headers) {
        for (ContentManager contentManager : this.contentManagers) {
            if (contentManager.isMatch(headers)) {
                return contentManager;
            }
        }
        return new CommonContentManager(mediaLibrary, httpServer, transcoder);
    }
}
