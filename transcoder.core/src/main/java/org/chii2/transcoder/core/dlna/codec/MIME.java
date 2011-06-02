package org.chii2.transcoder.core.dlna.codec;

/**
 * MIME
 */
public class MIME {
    /**
     * Get Video MIME
     *
     * @param container          Container
     * @param videoFormat        Video Format
     * @param videoFormatProfile Video Format Profile
     * @param videoFormatVersion Video Format Version
     * @param videoCodec         Video Codec
     * @return MIME
     */
    public static String getVideoMime(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec) {
        if (Container.match(container, Container.AVI)) {
            return "video/avi";
        } else if (Container.match(container, Container.MKV)) {
            return "video/x-matroska";
        } else if (Container.match(container, Container.MPEG4)) {
            return "video/mp4";
        } else if (Container.match(container, Container.QUICK_TIME)) {
            return "video/quicktime";
        } else if (Container.match(container, Container.WM)) {
            return "video/x-ms-wmv";
        } else if (Container.match(container, Container.MPEG_PS) || Container.match(container, Container.MPEG_ES) || Container.match(container, Container.MPEG_TS)) {
            return "video/mpeg";
        } else {
            return null;
        }
    }
}
