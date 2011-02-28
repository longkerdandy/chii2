package org.chii2.mediaserver.content.xbox;

import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.mediaserver.content.common.CommonContentManager;
import org.chii2.transcoder.api.core.TranscoderService;
import org.teleal.cling.model.message.UpnpHeaders;

import java.util.List;

/**
 * Content Manger for XBox360
 */
public class XBoxContentManager extends CommonContentManager{

    /**
     * Constructor
     *
     * @param mediaLibrary Media Library
     * @param httpServer   Http Server
     * @param transcoder   Transcoder
     */
    public XBoxContentManager(MediaLibraryService mediaLibrary, HttpServerService httpServer, TranscoderService transcoder) {
        super(mediaLibrary, httpServer, transcoder);

    }

    @Override
    public String getClientProfile() {
        return TranscoderService.PROFILE_XBOX;
    }

    @Override
    public boolean isMatch(UpnpHeaders headers) {
        if (headers != null) {
            // If user agent contains Xbox, is should be a Xbox client
            List<String> userAgent = headers.get("User-agent");
            if (getClientProfile().equalsIgnoreCase(transcoder.getClientProfile(userAgent))) {
                return true;
            }
        }
        return false;
    }
}
