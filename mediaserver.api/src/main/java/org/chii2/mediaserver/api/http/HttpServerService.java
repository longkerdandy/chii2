package org.chii2.mediaserver.api.http;

import org.chii2.mediaserver.api.dlna.DLNAProfile;

import java.net.InetAddress;
import java.net.URI;

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
     * Forge Media Item URL
     *
     * @param clientProfile Client Profile
     * @param mediaType     Media Type
     * @param transcoded    Transcoded or not
     * @param seriesNumber  Series Number
     * @param dlnaProfile   DLNA Profile
     * @param mediaId       Movie ID
     * @return Movie URL
     */
    public URI forgeUrl(String clientProfile, String mediaType, boolean transcoded, int seriesNumber, DLNAProfile.Profile dlnaProfile, String mediaId);
}
