package org.chii2.mediaserver.api.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.chii2.mediaserver.api.dlna.DLNAProfile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * URL Utils used to help encoding or decoding url
 */
public class HttpUrl {

    /**
     * Forge URL for media
     *
     * @param host          Host Address
     * @param port          Port
     * @param clientProfile Client Profile
     * @param mediaType     Media Type
     * @param transcoded    Transcoded or not
     * @param seriesNumber Series Number (like movie disk number)
     * @param dlnaProfile   DLNA Profile
     * @param mediaId       Media ID
     * @return URL
     */
    public static URI forgeURL(String host, int port, String clientProfile, String mediaType, boolean transcoded, int seriesNumber, DLNAProfile.Profile dlnaProfile, String mediaId) {
        URI uri = null;
        String transcodedFlag;
        if (transcoded) {
            transcodedFlag = "1";
        } else {
            transcodedFlag = "0";
        }
        String profileID = DLNAProfile.getProfileID(dlnaProfile);
        try {
            uri = new URI("http://" + host + ":" + port + "/" + clientProfile + "/" + mediaType + "/" + transcodedFlag + "/" + seriesNumber + "/" + profileID + "/" + mediaId);
        } catch (URISyntaxException ignore) {
            // This should not happens since we create the url ourselves.
        }
        return uri;
    }

    /**
     * Parse url for media, fill result in a map like:
     * "clinet" --- Client Profile
     * "type"  --- Media Type
     * "transcoded" ---  Transcoded (1 or 0)
     * "dlna" --- DLNA Profile
     * "id"    --- Media ID
     *
     * @param urlTarget URL Target
     * @return HashMap
     */
    public static HashMap<String, String> parseURL(String urlTarget) {
        HashMap<String, String> map = new HashMap<String, String>();
        String thumb = "";

        // This specially doing for XBox thumbnail, since XBox won't works with AlbumArtURI, it just append albumArt=true in resource url
        try {
            for (NameValuePair nameValuePair : URLEncodedUtils.parse(new URI(urlTarget), "ISO-8859-1")) {
                if ("albumArt".equalsIgnoreCase(nameValuePair.getName()) && "true".equalsIgnoreCase(nameValuePair.getValue())) {
                    thumb = "thumb";
                    break;
                }
            }
        } catch (URISyntaxException ignore) {
            // This won't happens, and won't matters
        }

        if (urlTarget.indexOf("?") > 0) {
            urlTarget = urlTarget.substring(0, urlTarget.indexOf("?"));
        }

        if (urlTarget.startsWith("/")) {
            urlTarget = urlTarget.substring(1);
        }
        if (urlTarget.endsWith("/")) {
            urlTarget = urlTarget.substring(0, urlTarget.length() - 1);
        }
        String[] factors = urlTarget.split("/", 6);
        if (factors.length != 6) {
            return null;
        } else {
            map.put("client", factors[0]);
            map.put("type", factors[1] + thumb);
            map.put("transcoded", factors[2]);
            map.put("series", factors[3]);
            map.put("dlna", factors[4]);
            map.put("id", factors[5]);
            return map;
        }
    }
}
