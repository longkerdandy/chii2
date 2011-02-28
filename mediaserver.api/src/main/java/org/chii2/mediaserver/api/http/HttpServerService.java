package org.chii2.mediaserver.api.http;

import java.net.InetAddress;

/**
 * Http Server Service for UPnP/DLNA Media Server
 */
public interface HttpServerService {

    /**
     * Get HTTP Server Host
     *
     * @return Host Name
     */
    public InetAddress getHost();

    /**
     * Get HTTP Server Post
     *
     * @return Port
     */
    public int getPort();

    /**
     * Forge Image Item URL
     *
     * @param profile Client Profile
     * @param imageId Image ID
     * @return Image URL
     */
    public String forgeImageUrl(String profile, String imageId);
}
