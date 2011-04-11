package org.chii2.mediaserver.api.dlna;

/**
 * DLNA Profile
 */
public class DLNAProfile {

    /**
     * Audio Profile
     */
    public enum AudioProfile {
        AUDIO_PROFILE_INVALID,
        AUDIO_PROFILE_WMA_BASE,
        AUDIO_PROFILE_WMA_FULL,
        AUDIO_PROFILE_WMA_PRO,
        AUDIO_PROFILE_MP3,
        AUDIO_PROFILE_MP3_EXTENDED,
    }

    /**
     * Video Profile
     */
    public enum VideoProfile {
        VIDEO_PROFILE_INVALID,
        VIDEO_PROFILE_WMV_SIMPLE_LOW,
        VIDEO_PROFILE_WMV_SIMPLE_MEDIUM,
        VIDEO_PROFILE_WMV_MAIN_MEDIUM,
        VIDEO_PROFILE_WMV_MAIN_HIGH
    }

    /**
     * Profile
     */
    public enum Profile {
        PROFILE_INVALID,
        PROFILE_WMV_SPLL_BASE,
        PROFILE_WMV_SPML_BASE,
        PROFILE_WMV_SPML_MP3,
        PROFILE_WMV_MED_BASE,
        PROFILE_WMV_MED_FULL,
        PROFILE_WMV_MED_PRO,
        PROFILE_WMV_HIGH_FULL,
        PROFILE_WMV_HIGH_PRO
    }

    /**
     * Get Audio Profile
     *
     * @param audioCodec         Audio Codec
     * @param audioBitRate       BitRate
     * @param audioSampleBitRate Sample BitRate
     * @param audioChannels      Channels Count
     * @return Audio Profile
     */
    @SuppressWarnings({"ConstantConditions"})
    public static AudioProfile getAudioProfile(String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
        // WMA
        if ("160".equalsIgnoreCase(audioCodec) || "161".equalsIgnoreCase(audioCodec)) {
            if (audioSampleBitRate <= 48000) {
                if (audioBitRate <= 193000 && audioChannels <= 2) {
                    return AudioProfile.AUDIO_PROFILE_WMA_BASE;
                } else if (audioBitRate <= 385000 && audioChannels <= 2) {
                    return AudioProfile.AUDIO_PROFILE_WMA_FULL;
                }
            } else if (audioSampleBitRate <= 96000) {
                if (audioBitRate <= 1500000 && audioChannels <= 8) {
                    return AudioProfile.AUDIO_PROFILE_WMA_PRO;
                }
            }
        }
        // MP3
        else if ("55".equalsIgnoreCase(audioCodec)) {
            if ((audioSampleBitRate == 32000 || audioSampleBitRate == 44100 || audioSampleBitRate == 48000) && audioChannels <= 2) {
                if (audioBitRate == 32000 || audioBitRate == 40000 || audioBitRate == 48000 || audioBitRate == 56000 ||
                        audioBitRate == 64000 || audioBitRate == 80000 || audioBitRate == 96000 || audioBitRate == 112000 ||
                        audioBitRate == 128000 || audioBitRate == 160000 || audioBitRate == 192000 || audioBitRate == 224000 ||
                        audioBitRate == 256000 || audioBitRate == 320000) {
                    return AudioProfile.AUDIO_PROFILE_MP3;
                }
            } else if ((audioSampleBitRate == 16000 || audioSampleBitRate == 22050 || audioSampleBitRate == 24000) && audioChannels <= 2) {
                if (audioBitRate == 8000 || audioBitRate == 16000 || audioBitRate == 24000 ||
                        audioBitRate == 32000 || audioBitRate == 40000 || audioBitRate == 48000 || audioBitRate == 56000 ||
                        audioBitRate == 64000 || audioBitRate == 80000 || audioBitRate == 96000 || audioBitRate == 112000 ||
                        audioBitRate == 128000 || audioBitRate == 160000 || audioBitRate == 192000 || audioBitRate == 224000 ||
                        audioBitRate == 256000 || audioBitRate == 320000) {
                    return AudioProfile.AUDIO_PROFILE_MP3_EXTENDED;
                }
            }
        }

        // Invalid
        return AudioProfile.AUDIO_PROFILE_INVALID;
    }

    /**
     * Get Video Profile
     *
     * @param videoCodec   Video Codec
     * @param videoBitRate BitRate
     * @param videoWidth   Video Width
     * @param videoHeight  Video Height
     * @param fps          Video FPS
     * @return Video Profile
     */
    public static VideoProfile getVideoProfile(String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps) {
        // WMV
        if ("WMV1".equalsIgnoreCase(videoCodec) || "WMV2".equalsIgnoreCase(videoCodec) || "WMV3".equalsIgnoreCase(videoCodec) || "WVC1".equalsIgnoreCase(videoCodec)) {
            if (videoBitRate <= 96000 && videoWidth <= 176 && videoHeight <= 144 && fps <= 15) {
                return VideoProfile.VIDEO_PROFILE_WMV_SIMPLE_LOW;
            } else if (videoBitRate <= 384000 && ((videoWidth <= 240 && videoHeight <= 176 && fps <= 30) || (videoWidth <= 352 && videoHeight <= 288 && fps <= 15))) {
                return VideoProfile.VIDEO_PROFILE_WMV_SIMPLE_MEDIUM;
            } else if (videoBitRate <= 10000000 && ((videoWidth <= 720 && videoHeight <= 480 && fps <= 30) || (videoWidth <= 720 && videoHeight <= 576 && fps <= 25))) {
                return VideoProfile.VIDEO_PROFILE_WMV_MAIN_MEDIUM;
            } else if (videoBitRate <= 20000000 && videoWidth <= 1920 && videoHeight <= 1080 && fps <= 30) {
                return VideoProfile.VIDEO_PROFILE_WMV_MAIN_HIGH;
            }
        }

        // Invalid
        return VideoProfile.VIDEO_PROFILE_INVALID;
    }

    /**
     * Get DLNA Profile String
     *
     * @param videoCodec         Video Codec
     * @param videoBitRate       Video BitRate
     * @param videoWidth         Video Width
     * @param videoHeight        Video Height
     * @param fps                Video FPS
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Sample BitRate
     * @param audioChannels      Channels Count
     * @return DLNA Profile String
     */
    public static Profile getDLNAProfile(String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
        VideoProfile videoProfile = getVideoProfile(videoCodec, videoBitRate, videoWidth, videoHeight, fps);
        AudioProfile audioProfile = getAudioProfile(audioCodec, audioBitRate, audioSampleBitRate, audioChannels);
        return getDLNAProfile(videoProfile, audioProfile);
    }

    /**
     * Get DLNA Profile String
     *
     * @param videoProfile Video Profile
     * @param audioProfile Audio Profile
     * @return DLNA Profile String
     */
    public static Profile getDLNAProfile(VideoProfile videoProfile, AudioProfile audioProfile) {
        if (videoProfile == VideoProfile.VIDEO_PROFILE_WMV_SIMPLE_LOW && audioProfile == AudioProfile.AUDIO_PROFILE_WMA_BASE) {
            return Profile.PROFILE_WMV_SPLL_BASE;
        } else if (videoProfile == VideoProfile.VIDEO_PROFILE_WMV_SIMPLE_MEDIUM && audioProfile == AudioProfile.AUDIO_PROFILE_WMA_BASE) {
            return Profile.PROFILE_WMV_SPML_BASE;
        } else if (videoProfile == VideoProfile.VIDEO_PROFILE_WMV_SIMPLE_MEDIUM && (audioProfile == AudioProfile.AUDIO_PROFILE_MP3 || audioProfile == AudioProfile.AUDIO_PROFILE_MP3_EXTENDED)) {
            return Profile.PROFILE_WMV_SPML_MP3;
        } else if (videoProfile == VideoProfile.VIDEO_PROFILE_WMV_MAIN_MEDIUM && audioProfile == AudioProfile.AUDIO_PROFILE_WMA_BASE) {
            return Profile.PROFILE_WMV_MED_BASE;
        } else if (videoProfile == VideoProfile.VIDEO_PROFILE_WMV_MAIN_MEDIUM && audioProfile == AudioProfile.AUDIO_PROFILE_WMA_FULL) {
            return Profile.PROFILE_WMV_MED_FULL;
        } else if (videoProfile == VideoProfile.VIDEO_PROFILE_WMV_MAIN_MEDIUM && audioProfile == AudioProfile.AUDIO_PROFILE_WMA_PRO) {
            return Profile.PROFILE_WMV_MED_PRO;
        } else if (videoProfile == VideoProfile.VIDEO_PROFILE_WMV_MAIN_HIGH && (audioProfile == AudioProfile.AUDIO_PROFILE_WMA_BASE || audioProfile == AudioProfile.AUDIO_PROFILE_WMA_FULL)) {
            return Profile.PROFILE_WMV_HIGH_FULL;
        } else if (videoProfile == VideoProfile.VIDEO_PROFILE_WMV_MAIN_HIGH && audioProfile == AudioProfile.AUDIO_PROFILE_WMA_PRO) {
            return Profile.PROFILE_WMV_HIGH_PRO;
        } else {
            return DLNAProfile.Profile.PROFILE_INVALID;
        }
    }

    /**
     * Get DLNA Profile String
     *
     * @param profile DLNA Profile
     * @return DLNA Profile String
     */
    public static String getProfileString(Profile profile) {
        if (profile == Profile.PROFILE_WMV_SPLL_BASE) {
            return "DLNA.ORG_PN=WMVSPLL_BASE";
        } else if (profile == Profile.PROFILE_WMV_SPML_BASE) {
            return "DLNA.ORG_PN=WMVSPML_BASE";
        } else if (profile == Profile.PROFILE_WMV_SPML_MP3) {
            return "DLNA.ORG_PN=WMVSPML_MP3";
        } else if (profile == Profile.PROFILE_WMV_MED_BASE) {
            return "DLNA.ORG_PN=WMVMED_BASE";
        } else if (profile == Profile.PROFILE_WMV_MED_FULL) {
            return "DLNA.ORG_PN=WMVMED_FULL";
        } else if (profile == Profile.PROFILE_WMV_MED_PRO) {
            return "DLNA.ORG_PN=WMVMED_PRO";
        } else if (profile == Profile.PROFILE_WMV_HIGH_FULL) {
            return "DLNA.ORG_PN=WMVHIGH_FULL";
        } else if (profile == Profile.PROFILE_WMV_HIGH_PRO) {
            return "DLNA.ORG_PN=WMVHIGH_PRO";
        } else {
            return null;
        }
    }

    /**
     * Get DLNA Profile ID
     *
     * @param profile DLNA Profile
     * @return DLNA Profile ID
     */
    public static String getProfileID(Profile profile) {
        if (profile == Profile.PROFILE_WMV_SPLL_BASE) {
            return "wmvspllbase";
        } else if (profile == Profile.PROFILE_WMV_SPML_BASE) {
            return "wmvspmlbase";
        } else if (profile == Profile.PROFILE_WMV_SPML_MP3) {
            return "wmvspmlmp3";
        } else if (profile == Profile.PROFILE_WMV_MED_BASE) {
            return "wmvmedbase";
        } else if (profile == Profile.PROFILE_WMV_MED_FULL) {
            return "wmvmedfull";
        } else if (profile == Profile.PROFILE_WMV_MED_PRO) {
            return "wmvmedpro";
        } else if (profile == Profile.PROFILE_WMV_HIGH_FULL) {
            return "wmvhighfull";
        } else if (profile == Profile.PROFILE_WMV_HIGH_PRO) {
            return "wmvhighpro";
        } else {
            return "invalid";
        }
    }

    /**
     * Get DLNA Profile based on Profile ID
     *
     * @param id Profile ID
     * @return DLNA Profile
     */
    public static Profile getProfileById(String id) {
        if ("wmvspllbase".equalsIgnoreCase(id)) {
            return Profile.PROFILE_WMV_SPLL_BASE;
        } else if ("wmvspmlbase".equalsIgnoreCase(id)) {
            return Profile.PROFILE_WMV_SPML_BASE;
        } else if ("wmvspmlmp3".equalsIgnoreCase(id)) {
            return Profile.PROFILE_WMV_SPML_MP3;
        } else if ("wmvmedbase".equalsIgnoreCase(id)) {
            return Profile.PROFILE_WMV_MED_BASE;
        } else if ("wmvmedfull".equalsIgnoreCase(id)) {
            return Profile.PROFILE_WMV_MED_FULL;
        } else if ("wmvmedpro".equalsIgnoreCase(id)) {
            return Profile.PROFILE_WMV_MED_PRO;
        } else if ("wmvhighfull".equalsIgnoreCase(id)) {
            return Profile.PROFILE_WMV_HIGH_FULL;
        } else if ("wmvhighpro".equalsIgnoreCase(id)) {
            return Profile.PROFILE_WMV_HIGH_PRO;
        } else {
            return Profile.PROFILE_INVALID;
        }
    }

    /**
     * Get MIME information based on DLNA Profile
     *
     * @param profile DLNA Profile
     * @return MIME
     */
    public static String getMimeByProfile(Profile profile) {
        if (profile == Profile.PROFILE_WMV_SPLL_BASE || profile == Profile.PROFILE_WMV_SPML_BASE || profile == Profile.PROFILE_WMV_SPML_MP3 ||
                profile == Profile.PROFILE_WMV_MED_BASE || profile == Profile.PROFILE_WMV_MED_FULL || profile == Profile.PROFILE_WMV_MED_PRO ||
                profile == Profile.PROFILE_WMV_HIGH_FULL || profile == Profile.PROFILE_WMV_HIGH_PRO) {
            return "video/x-ms-wmv";
        } else {
            return null;
        }
    }
}
