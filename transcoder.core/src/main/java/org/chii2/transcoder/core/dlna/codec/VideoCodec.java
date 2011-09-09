package org.chii2.transcoder.core.dlna.codec;

import org.apache.commons.lang.ArrayUtils;

/**
 * Video Codec
 */
public enum VideoCodec {
    MPEG1(new String[]{"MPEG Video"}, null, new int[]{0, 1}, null),
    MPEG2(new String[]{"MPEG Video"}, null, new int[]{2}, null),
    MPEG4_P2(new String[]{"MPEG-4 Visual"}, null, null, null),
    MPEG4_P10(new String[]{"AVC"}, null, null, null),
    WMV(new String[]{"WMV1", "WMV2", "WMV3", "WVC1", "WMVA", "WMVP", "WVP2"}),
    H263(new String[]{"H263"});

    // Acceptable Codec
    private String[] codecs;
    // Acceptable Format
    private String[] formats;
    // Acceptable Format Profile
    private String[] profiles;
    // Acceptable Format Version
    private int[] versions;

    VideoCodec(String[] codecs) {
        this.codecs = codecs;
    }

    VideoCodec(String[] formats, String[] profiles, int[] versions, String[] codecs) {
        this.formats = formats;
        this.profiles = profiles;
        this.versions = versions;
        this.codecs = codecs;
    }

    /**
     * Given fields match audio codec condition
     *
     * @param format  Video Format
     * @param profile Video Format Profile
     * @param version Video Format Version
     * @param codec   Video Codec
     * @param target  Target Codec Type
     * @return True if match
     */
    public static boolean match(String format, String profile, int version, String codec, VideoCodec target) {
        if (target.codecs != null && target.codecs.length > 0) {
            boolean codecMatch = false;
            for (String acceptableCodec : target.codecs) {
                if (acceptableCodec.equalsIgnoreCase(codec)) {
                    codecMatch = true;
                    break;
                }
            }
            if (!codecMatch) return false;
        }
        if (target.formats != null && target.formats.length > 0) {
            boolean formatMatch = false;
            for (String acceptableFormat : target.formats) {
                if (acceptableFormat.equalsIgnoreCase(format)) {
                    formatMatch = true;
                    break;
                }
            }
            if (!formatMatch) return false;
        }
        if (target.profiles != null && target.profiles.length > 0) {
            boolean profileMatch = false;
            for (String acceptableProfile : target.profiles) {
                if (acceptableProfile.equalsIgnoreCase(profile)) {
                    profileMatch = true;
                    break;
                }
            }
            if (!profileMatch) return false;
        }
        if (target.versions != null && target.versions.length > 0 && !ArrayUtils.contains(target.versions, version)) {
            return false;
        }
        return true;
    }
}
