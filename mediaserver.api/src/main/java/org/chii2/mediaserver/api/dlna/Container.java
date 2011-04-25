package org.chii2.mediaserver.api.dlna;

/**
 * Media File Container (Format)
 */
public class Container {
    // Is MPEG4
    public static boolean isMPEG4(String container) {
        if ("m4v".equalsIgnoreCase(container) || "mov".equalsIgnoreCase(container) || "mp4".equalsIgnoreCase(container) ||
                "m4a".equalsIgnoreCase(container) || "3gp".equalsIgnoreCase(container) || "3g2".equalsIgnoreCase(container)) {
            return true;
        }
        return false;
    }

    // Is MPEG-PS
    public static boolean isMPEGPS(String container) {
        if ("dvd".equalsIgnoreCase(container) || "svcd".equalsIgnoreCase(container) || "vob".equalsIgnoreCase(container)) {
            return true;
        }
        return false;
    }

    // Is MPEG-ES
    public static boolean isMPEGES(String container) {
        if ("mpeg2video".equalsIgnoreCase(container)) {
            return true;
        }
        return false;
    }

    // Is MPEG-TS
    public static boolean isMPEGTS(String container) {
        if ("mpegts".equalsIgnoreCase(container) || "mpegtsraw".equalsIgnoreCase(container)) {
            return true;
        }
        return false;
    }

    // Is ASF
    public static boolean isASF(String container) {
        if ("asf".equalsIgnoreCase(container) || "asf_stream".equalsIgnoreCase(container)) {
            return true;
        }
        return false;
    }

    // Is 3GPP
    public static boolean is3GPP(String container) {
        if ("3gp".equalsIgnoreCase(container)) {
            return true;
        }
        return false;
    }
}
