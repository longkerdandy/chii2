package org.chii2.mediaserver.api.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

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
     * @param mediaId       Media ID
     * @return URL
     */
    public static URI forgeURL(String host, int port, String mediaType, String clientProfile, boolean transcoded, String mediaId) {
        URI uri = null;
        String transcodedFlag;
        if (transcoded) {
            transcodedFlag = "1";
        } else {
            transcodedFlag = "0";
        }
        try {
            uri = new URI("http://" + host + ":" + port + "/" + mediaType + "/" + clientProfile + "/" + transcodedFlag + "/" + mediaId);
        } catch (URISyntaxException ignore) {
            // This should not happens since we create the url ourselves.
        }
        return uri;
    }

    /**
     * Forge URL for online media
     *
     * @param host          Host Address
     * @param port          Port
     * @param provider      Provider Name
     * @param clientProfile Client Profile
     * @param mediaType     Media Type
     * @param transcoded    Transcoded or not
     * @param url           Media URL
     * @return URL
     */
    public static URI forgeURL(String host, int port, String mediaType, String provider, String clientProfile, boolean transcoded, String url) {
        URI uri = null;
        String transcodedFlag;
        if (transcoded) {
            transcodedFlag = "1";
        } else {
            transcodedFlag = "0";
        }
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        try {
            uri = new URI("http://" + host + ":" + port + "/" + mediaType + "/" + provider + "/" + clientProfile + "/" + transcodedFlag + "/" + url);
        } catch (URISyntaxException ignore) {
            // This should not happens since we create the url ourselves.
        }
        return uri;
    }

    /**
     * Parse url for media, fill result in a map like:
     * "type"  --- Media Type
     * "provider" --- Online Provider Name
     * "client" --- Client Profile
     * "transcoded" ---  Transcoded (1 or 0)
     * "id"    --- Media ID
     * "url"   --- Online Media URL
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

        int typeIndex = urlTarget.indexOf("/");
        if (typeIndex > 0) {
            String type = urlTarget.substring(0, typeIndex);
            if ("onlinevideo".equalsIgnoreCase(type)) {
                String[] factors = urlTarget.split("/", 5);
                if (factors.length == 5) {
                    map.put("type", factors[0] + thumb);
                    map.put("provider", factors[1]);
                    map.put("client", factors[2]);
                    map.put("transcoded", factors[3]);
                    map.put("url", factors[4]);
                }
            } else {
                String[] factors = urlTarget.split("/", 4);
                if (factors.length == 4) {
                    map.put("type", factors[0] + thumb);
                    map.put("client", factors[1]);
                    map.put("transcoded", factors[2]);
                    map.put("id", factors[3]);
                }
            }
        }

        return map;
    }
}
