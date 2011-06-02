package org.chii2.mediaserver.content.xbox.container;

import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.provider.OnlineVideoProviderService;
import org.chii2.mediaserver.content.common.container.MovieBaseStorageFolderContainer;
import org.chii2.mediaserver.content.common.container.VideoFoldersContainer;
import org.teleal.cling.support.model.SortCriterion;

import java.util.List;

/**
 * XBox Video Folders Container.
 */
public class XBoxVideoFoldersContainer extends VideoFoldersContainer {

    // Online Videos
    private List<OnlineVideoProviderService> onlineVideos;

    /**
     * Constructor
     *
     * @param filter Content Filter
     */
    public XBoxVideoFoldersContainer(String filter) {
        super(filter);
    }

    /**
     * Constructor
     *
     * @param filter       Content Filter
     * @param onlineVideos Online Video Providers
     */
    public XBoxVideoFoldersContainer(String filter, List<OnlineVideoProviderService> onlineVideos) {
        super(filter);
        this.onlineVideos = onlineVideos;
    }

    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Container ID
     * @param parentId Parent ID
     */
    public XBoxVideoFoldersContainer(String filter, String id, String parentId) {
        super(filter, id, parentId);
    }

    /**
     * Constructor
     *
     * @param filter       Content Filter
     * @param id           Container ID
     * @param parentId     Parent ID
     * @param onlineVideos Online Video Providers
     */
    public XBoxVideoFoldersContainer(String filter, String id, String parentId, List<OnlineVideoProviderService> onlineVideos) {
        super(filter, id, parentId);
        this.onlineVideos = onlineVideos;
    }

    @Override
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        // Movie
        addContainer(new MovieBaseStorageFolderContainer(filter));
        // Add Online Videos
        if (onlineVideos != null && onlineVideos.size() > 0) {
            for (OnlineVideoProviderService onlineVideo : onlineVideos) {
                VisualContainer onlineVideoContainer = onlineVideo.getRootContainer(filter);
                // Change Online Video Container's Parent ID
                onlineVideoContainer.setParentID(getId());
                addContainer(onlineVideoContainer);
            }
            setChildCount(1 + onlineVideos.size());
            setTotalChildCount(1 + onlineVideos.size());
        } else {
            setChildCount(1);
            setTotalChildCount(1);
        }
    }
}
