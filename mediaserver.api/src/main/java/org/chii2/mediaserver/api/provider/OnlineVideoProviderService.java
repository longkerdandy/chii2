package org.chii2.mediaserver.api.provider;

import org.chii2.mediaserver.api.content.container.VisualContainer;

/**
 * Online Video Provider Service
 */
public interface OnlineVideoProviderService {

    // Container Prefix
    public final static String ONLINE_VIDEO_CONTAINER_PREFIX = "OVC-";

    /**
     * Get the provider name
     *
     * @return Provider name
     */
    public String getProviderName();

    /**
     * Is given Object ID belongs to this provider
     *
     * @param id Object ID (Container or Item)
     * @return True if id belongs to this provider
     */
    public boolean isMatch(String id);

    /**
     * Get the Root Container
     *
     * @param filter Container Filter
     * @return Root Container
     */
    public VisualContainer getRootContainer(String filter);

    /**
     * Get the Container by ID
     *
     * @param filter Container Filter
     * @param id Container ID
     * @return Container
     */
    public VisualContainer getContainerByID(String filter, String id);
}
