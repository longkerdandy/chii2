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
        AUDIO_PROFILE_AAC,
        AUDIO_PROFILE_AAC_320,
        AUDIO_PROFILE_AAC_MULT5,
        AUDIO_PROFILE_AAC_BSAC,
        AUDIO_PROFILE_AAC_BSAC_MULT5,
        AUDIO_PROFILE_AAC_HE_L2,
        AUDIO_PROFILE_AAC_HE_L2_320,
        AUDIO_PROFILE_AAC_HE_L3,
        AUDIO_PROFILE_AAC_HE_MULT5,
        AUDIO_PROFILE_AAC_HE_V2_L2,
        AUDIO_PROFILE_AAC_HE_V2_L2_320,
        AUDIO_PROFILE_AAC_HE_V2_L3,
        AUDIO_PROFILE_AAC_HE_V2_MULT5,
        AUDIO_PROFILE_AAC_LTP,
        AUDIO_PROFILE_AAC_LTP_MULT5,
        AUDIO_PROFILE_AAC_LTP_MULT7,
        AUDIO_PROFILE_AC3,
        AUDIO_PROFILE_AC3_EXTENDED,
        AUDIO_PROFILE_AMR,
        AUDIO_PROFILE_AMR_WB,
        AUDIO_PROFILE_ATRAC,
        AUDIO_PROFILE_G726,
        AUDIO_PROFILE_LPCM,
        AUDIO_PROFILE_MP2,
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
        VIDEO_PROFILE_MPEG1,
        VIDEO_PROFILE_MPEG_PS_ES_NTSC,
        VIDEO_PROFILE_MPEG_PS_ES_PAL,
        VIDEO_PROFILE_MPEG_TS_MP,
        VIDEO_PROFILE_MPEG_TS_EU_SD,
        VIDEO_PROFILE_MPEG_TS_NA_SD,
        VIDEO_PROFILE_MPEG_TS_NA_HD,
        VIDEO_PROFILE_MPEG4_H263,
        VIDEO_PROFILE_MPEG4_P2_SP_L0B,
        VIDEO_PROFILE_MPEG4_P2_SP_L2,
        VIDEO_PROFILE_MPEG4_P2_SP_L3,
        VIDEO_PROFILE_MPEG4_P2_SP_L3_VGA,
        VIDEO_PROFILE_MPEG4_P2_ASP_L4,
        VIDEO_PROFILE_MPEG4_P2_ASP_L5,
        VIDEO_PROFILE_AVC_BL_QCIF15,
        VIDEO_PROFILE_AVC_BL_L1B_QCIF,
        VIDEO_PROFILE_AVC_BL_L12_CIF15,
        VIDEO_PROFILE_AVC_BL_CIF15,
        VIDEO_PROFILE_AVC_BL_CIF15_520,
        VIDEO_PROFILE_AVC_BL_CIF15_540,
        VIDEO_PROFILE_AVC_BL_L2_CIF30,
        VIDEO_PROFILE_AVC_BL_CIF30,
        VIDEO_PROFILE_AVC_BL_CIF30_940,
        VIDEO_PROFILE_AVC_BL_L3L_SD,
        VIDEO_PROFILE_AVC_BL_L3_SD,
        VIDEO_PROFILE_AVC_MP_SD,
        VIDEO_PROFILE_AVC_MP_HD,
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
        PROFILE_MPEG1,
        PROFILE_MPEG_PS_NTSC,
        PROFILE_MPEG_PS_NTSC_XAC3,
        PROFILE_MPEG_PS_PAL,
        PROFILE_MPEG_PS_PAL_XAC3,
        PROFILE_MPEG_TS_MP_LL_AAC,
        PROFILE_MPEG_TS_MP_LL_AAC_T,
        PROFILE_MPEG_TS_MP_LL_AAC_ISO,
        PROFILE_MPEG_TS_SD_EU,
        PROFILE_MPEG_TS_SD_EU_T,
        PROFILE_MPEG_TS_SD_EU_ISO,
        PROFILE_MPEG_TS_SD_NA,
        PROFILE_MPEG_TS_SD_NA_T,
        PROFILE_MPEG_TS_SD_NA_ISO,
        PROFILE_MPEG_TS_SD_NA_XAC3,
        PROFILE_MPEG_TS_SD_NA_XAC3_T,
        PROFILE_MPEG_TS_SD_NA_XAC3_ISO,
        PROFILE_MPEG_TS_HD_NA,
        PROFILE_MPEG_TS_HD_NA_T,
        PROFILE_MPEG_TS_HD_NA_ISO,
        PROFILE_MPEG_TS_HD_NA_XAC3,
        PROFILE_MPEG_TS_HD_NA_XAC3_T,
        PROFILE_MPEG_TS_HD_NA_XAC3_ISO,
        PROFILE_MPEG_ES_PAL,
        PROFILE_MPEG_ES_NTSC,
        PROFILE_MPEG_ES_PAL_XAC3,
        PROFILE_MPEG_ES_NTSC_XAC3,
        PROFILE_MPEG4_P2_MP4_SP_AAC,
        PROFILE_MPEG4_P2_MP4_SP_HEAAC,
        PROFILE_MPEG4_P2_MP4_SP_ATRAC3plus,
        PROFILE_MPEG4_P2_MP4_SP_AAC_LTP,
        PROFILE_MPEG4_P2_MP4_SP_L2_AAC,
        PROFILE_MPEG4_P2_MP4_SP_L2_AMR,
        PROFILE_MPEG4_P2_MP4_SP_VGA_AAC,
        PROFILE_MPEG4_P2_MP4_SP_VGA_HEAAC,
        PROFILE_MPEG4_P2_MP4_ASP_AAC,
        PROFILE_MPEG4_P2_MP4_ASP_HEAAC,
        PROFILE_MPEG4_P2_MP4_ASP_HEAAC_MULT5,
        PROFILE_MPEG4_P2_MP4_ASP_ATRAC3plus,
        PROFILE_MPEG4_P2_MP4_ASP_L5_SO_AAC,
        PROFILE_MPEG4_P2_MP4_ASP_L5_SO_HEAAC,
        PROFILE_MPEG4_P2_MP4_ASP_L5_SO_HEAAC_MULT5,
        PROFILE_MPEG4_P2_MP4_ASP_L4_SO_AAC,
        PROFILE_MPEG4_P2_MP4_ASP_L4_SO_HEAAC,
        PROFILE_MPEG4_P2_MP4_ASP_L4_SO_HEAAC_MULT5,
        PROFILE_MPEG4_H263_MP4_P0_L10_AAC,
        PROFILE_MPEG4_H263_MP4_P0_L10_AAC_LTP,
        PROFILE_MPEG4_P2_TS_SP_AAC,
        PROFILE_MPEG4_P2_TS_SP_AAC_T,
        PROFILE_MPEG4_P2_TS_SP_AAC_ISO,
        PROFILE_MPEG4_P2_TS_SP_MPEG1_L3,
        PROFILE_MPEG4_P2_TS_SP_MPEG1_L3_T,
        PROFILE_MPEG4_P2_TS_SP_MPEG1_L3_ISO,
        PROFILE_MPEG4_P2_TS_SP_AC3,
        PROFILE_MPEG4_P2_TS_SP_AC3_T,
        PROFILE_MPEG4_P2_TS_SP_AC3_ISO,
        PROFILE_MPEG4_P2_TS_SP_MPEG2_L2,
        PROFILE_MPEG4_P2_TS_SP_MPEG2_L2_T,
        PROFILE_MPEG4_P2_TS_SP_MPEG2_L2_ISO,
        PROFILE_MPEG4_P2_TS_ASP_AAC,
        PROFILE_MPEG4_P2_TS_ASP_AAC_T,
        PROFILE_MPEG4_P2_TS_ASP_AAC_ISO,
        PROFILE_MPEG4_P2_TS_ASP_MPEG1_L3,
        PROFILE_MPEG4_P2_TS_ASP_MPEG1_L3_T,
        PROFILE_MPEG4_P2_TS_ASP_MPEG1_L3_ISO,
        PROFILE_MPEG4_P2_TS_ASP_AC3,
        PROFILE_MPEG4_P2_TS_ASP_AC3_T,
        PROFILE_MPEG4_P2_TS_ASP_AC3_ISO,
        PROFILE_MPEG4_P2_TS_CO_AC3,
        PROFILE_MPEG4_P2_TS_CO_AC3_T,
        PROFILE_MPEG4_P2_TS_CO_AC3_ISO,
        PROFILE_MPEG4_P2_TS_CO_MPEG2_L2,
        PROFILE_MPEG4_P2_TS_CO_MPEG2_L2_T,
        PROFILE_MPEG4_P2_TS_CO_MPEG2_L2_ISO,
        PROFILE_MPEG4_P2_ASF_SP_G726,
        PROFILE_MPEG4_P2_ASF_ASP_L5_SO_G726,
        PROFILE_MPEG4_P2_ASF_ASP_L4_SO_G726,
        PROFILE_MPEG4_H263_3GPP_P0_L10_AMR_WBplus,
        PROFILE_MPEG4_P2_3GPP_SP_L0B_AAC,
        PROFILE_MPEG4_P2_3GPP_SP_L0B_AMR,
        PROFILE_MPEG4_H263_3GPP_P3_L10_AMR,
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
        // AAC
        if (audioCodec.startsWith("AAC")) {
            if ("AAC LC".equalsIgnoreCase(audioCodec) && audioSampleBitRate >= 8000 && audioSampleBitRate <= 48000) {
                if (audioBitRate <= 320000 && audioChannels <= 2) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_320;
                } else if (audioBitRate <= 576000 && audioChannels <= 2) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC;
                } else if (audioBitRate <= 1440000 && audioChannels <= 6) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_MULT5;
                }
            } else if ("AAC LTP".equalsIgnoreCase(audioCodec) && audioSampleBitRate >= 8000) {
                if (audioSampleBitRate <= 48000 && audioBitRate <= 576000 && audioChannels <= 2) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_LTP;
                } else if (audioSampleBitRate <= 96000 && audioBitRate <= 2880000 && audioChannels <= 6) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_LTP_MULT5;
                } else if (audioSampleBitRate <= 96000 && audioBitRate <= 4032000 && audioChannels <= 8) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_LTP_MULT7;
                }
            } else if ("AAC HE".equalsIgnoreCase(audioCodec) && audioSampleBitRate >= 8000) {
                if (audioSampleBitRate <= 24000 && audioBitRate <= 320000 && audioChannels <= 2) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_HE_L2_320;
                } else if (audioSampleBitRate <= 24000 && audioBitRate <= 576000 && audioChannels <= 2) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_HE_L2;
                } else if (audioSampleBitRate <= 48000 && audioBitRate <= 576000) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_HE_L3;
                } else if (audioSampleBitRate <= 48000 && audioBitRate <= 1440000) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_HE_MULT5;
                }
            } else if ("AAC PARAM".equalsIgnoreCase(audioCodec) && audioSampleBitRate >= 8000) {
                if (audioSampleBitRate <= 24000 && audioBitRate <= 320000 && audioChannels <= 2) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L2_320;
                } else if (audioSampleBitRate <= 24000 && audioBitRate <= 576000 && audioChannels <= 2) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L2;
                } else if (audioSampleBitRate <= 48000 && audioBitRate <= 576000) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L3;
                } else if (audioSampleBitRate <= 48000 && audioBitRate <= 1440000) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_HE_V2_MULT5;
                }
            } else if ("AAC BSAC".equalsIgnoreCase(audioCodec) && audioSampleBitRate >= 8000 && audioSampleBitRate <= 48000) {
                if (audioBitRate <= 128000 && audioChannels <= 2) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_BSAC;
                } else if (audioBitRate <= 128000 && audioChannels <= 6) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AAC_BSAC_MULT5;
                }
            }
        }
        // AC3
        else if ("2000".equalsIgnoreCase(audioCodec)) {
            if ((audioSampleBitRate == 32000 || audioSampleBitRate == 44100 || audioSampleBitRate == 48000) && audioBitRate >= 32000 && audioChannels <= 6) {
                if (audioBitRate <= 448000) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AC3;
                } else if (audioBitRate <= 640000) {
                    return DLNAProfile.AudioProfile.AUDIO_PROFILE_AC3_EXTENDED;
                }
            }
        }
        // AMR
        else if ("samr".equalsIgnoreCase(audioCodec)) {
            if (audioSampleBitRate == 8000 && audioChannels == 1 && (audioBitRate == 4750 ||
                    audioBitRate == 5150 || audioBitRate == 5900 || audioBitRate == 6700 || audioBitRate == 7400 ||
                    audioBitRate == 7950 || audioBitRate == 10200 || audioBitRate == 12200)) {
                return DLNAProfile.AudioProfile.AUDIO_PROFILE_AMR;
            } else if (audioBitRate >= 5200 && audioBitRate <= 48000 && audioChannels <= 2 && (audioSampleBitRate == 8000 ||
                    audioSampleBitRate == 16000 || audioSampleBitRate == 24000 || audioSampleBitRate == 32000 || audioSampleBitRate == 48000)) {
                return DLNAProfile.AudioProfile.AUDIO_PROFILE_AMR_WB;
            }
        }
        // ATRAC
        else if ("270".equalsIgnoreCase(audioCodec)) {
            return DLNAProfile.AudioProfile.AUDIO_PROFILE_ATRAC;
        }
        // G726
        else if ("64".equalsIgnoreCase(audioCodec) || "85".equalsIgnoreCase(audioCodec) || "140".equalsIgnoreCase(audioCodec)) {
            return DLNAProfile.AudioProfile.AUDIO_PROFILE_G726;
        }
        // LPCM
        else if ("sowt".equalsIgnoreCase(audioCodec)) {
            if (audioSampleBitRate >= 8000 && audioSampleBitRate <= 48000 && audioChannels <= 2) {
                return DLNAProfile.AudioProfile.AUDIO_PROFILE_LPCM;
            }
        }
        // MP2
        else if ("50".equalsIgnoreCase(audioCodec)) {
            if (audioBitRate >= 32000 && audioBitRate <= 448000 && (audioSampleBitRate == 32000 || audioSampleBitRate == 44100 || audioSampleBitRate == 48000) && audioChannels <= 6) {
                return DLNAProfile.AudioProfile.AUDIO_PROFILE_MP2;
            }
        }
        // WMA
        else if ("160".equalsIgnoreCase(audioCodec) || "161".equalsIgnoreCase(audioCodec)) {
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
    @SuppressWarnings({"ConstantConditions"})
    public static VideoProfile getVideoProfile(String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps) {
        // MPEG 1
        if ("MPEG".equalsIgnoreCase(videoCodec)) {
            if (videoBitRate == 1150000 && videoWidth <= 352 && videoHeight <= 288 && fps <= 30) {
                return VideoProfile.VIDEO_PROFILE_MPEG1;
            }
        }
        // MPEG 2
        else if ("MPG2".equalsIgnoreCase(videoCodec) || "MP2V".equalsIgnoreCase(videoCodec) || "MP2T".equalsIgnoreCase(videoCodec)) {
            if (videoWidth == 352 && videoHeight == 288 && fps == 30 && videoBitRate <= 4000000) {
                return VideoProfile.VIDEO_PROFILE_MPEG_TS_MP;
            } else if ((videoWidth == 720 && videoHeight == 576 && fps == 25) || (videoWidth == 544 && videoHeight == 576 && fps == 25) ||
                    (videoWidth == 480 && videoHeight == 576 && fps == 25) || (videoWidth == 352 && videoHeight == 576 && fps == 25) ||
                    (videoWidth == 352 && videoHeight == 288 && fps == 25)) {
                return VideoProfile.VIDEO_PROFILE_MPEG_TS_EU_SD;
            } else if ((videoWidth == 720 && videoHeight == 480 && fps == 30) || (videoWidth == 704 && videoHeight == 480 && (fps == 30 || fps == 24)) ||
                    (videoWidth == 640 && videoHeight == 480 && (fps == 30 || fps == 24)) || (videoWidth == 544 && videoHeight == 480 && fps == 30) ||
                    (videoWidth == 480 && videoHeight == 480 && fps == 30) || (videoWidth == 352 && videoHeight == 480 && fps == 30)) {
                return VideoProfile.VIDEO_PROFILE_MPEG_TS_NA_SD;
            } else if ((videoWidth == 1920 && videoHeight == 1080 && (fps == 30 || fps == 24)) || (videoWidth == 1280 && videoHeight == 720 && (fps == 30 || fps == 24)) ||
                    (videoWidth == 1440 && videoHeight == 1080 && (fps == 30 || fps == 24)) || (videoWidth == 1280 && videoHeight == 1080 && (fps == 30 || fps == 24))) {
                return VideoProfile.VIDEO_PROFILE_MPEG_TS_NA_HD;
            } else if ((videoWidth == 720 && videoHeight == 480) || (videoWidth == 704 && videoHeight == 480) ||
                    (videoWidth == 544 && videoHeight == 480) || (videoWidth == 480 && videoHeight == 480) ||
                    (videoWidth == 352 && videoHeight == 480) || (videoWidth == 352 && videoHeight == 240)) {
                return VideoProfile.VIDEO_PROFILE_MPEG_PS_ES_NTSC;
            } else if ((videoWidth == 720 && videoHeight == 576) || (videoWidth == 704 && videoHeight == 576) ||
                    (videoWidth == 544 && videoHeight == 576) || (videoWidth == 480 && videoHeight == 576) ||
                    (videoWidth == 352 && videoHeight == 576) || (videoWidth == 352 && videoHeight == 288)) {
                return VideoProfile.VIDEO_PROFILE_MPEG_PS_ES_PAL;
            }
        }
        // MPEG4 H263
        else if ("H263".equalsIgnoreCase(videoCodec) || "X263".equalsIgnoreCase(videoCodec) || "D263".equalsIgnoreCase(videoCodec) ||
                "L263".equalsIgnoreCase(videoCodec) || "M263".equalsIgnoreCase(videoCodec)) {
            if (videoBitRate <= 64000) {
                if ((videoWidth == 176 && videoHeight == 144 && fps <= 15) || (videoWidth == 128 && videoHeight == 96 && fps <= 15)) {
                    return DLNAProfile.VideoProfile.VIDEO_PROFILE_MPEG4_H263;
                }
            }
        }
        // MPEG4 Part2
        else if ("M4S2".equalsIgnoreCase(videoCodec) || "MP4S".equalsIgnoreCase(videoCodec) || "MP42".equalsIgnoreCase(videoCodec) ||
                "DIV3".equalsIgnoreCase(videoCodec) || "DIV4".equalsIgnoreCase(videoCodec) || "DV50".equalsIgnoreCase(videoCodec) ||
                "DIV5".equalsIgnoreCase(videoCodec) || "DIVX".equalsIgnoreCase(videoCodec) || "MP43".equalsIgnoreCase(videoCodec) ||
                "MPG4".equalsIgnoreCase(videoCodec) || "XVID".equalsIgnoreCase(videoCodec)) {
            if (videoBitRate <= 128000) {
                if ((videoWidth == 176 && videoHeight == 144 && fps <= 15) || (videoWidth == 128 && videoHeight == 96 && fps <= 15)) {
                    return DLNAProfile.VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B;
                } else if ((videoWidth == 352 && videoHeight == 288 && fps <= 15) || (videoWidth == 320 && videoHeight == 240 && fps <= 15) ||
                        (videoWidth == 320 && videoHeight == 180 && fps <= 15) || (videoWidth == 128 && videoHeight == 96 && fps <= 30) ||
                        (videoWidth == 176 && videoHeight == 144 && fps <= 30)) {
                    return DLNAProfile.VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L2;
                }
            } else if (videoBitRate <= 384000) {
                if ((videoWidth == 352 && videoHeight == 288 && fps <= 30) || (videoWidth == 352 && videoHeight == 240 && fps <= 30) ||
                        (videoWidth == 320 && videoHeight == 240 && fps <= 30) || (videoWidth == 320 && videoHeight == 180 && fps <= 30) ||
                        (videoWidth == 240 && videoHeight == 180 && fps <= 30) || (videoWidth == 208 && videoHeight == 160 && fps <= 30) ||
                        (videoWidth == 176 && videoHeight == 144 && fps <= 30) || (videoWidth == 176 && videoHeight == 120 && fps <= 30) ||
                        (videoWidth == 160 && videoHeight == 120 && fps <= 30) || (videoWidth == 160 && videoHeight == 112 && fps <= 30) ||
                        (videoWidth == 160 && videoHeight == 90 && fps <= 30) || (videoWidth == 128 && videoHeight == 96 && fps <= 30)) {
                    return DLNAProfile.VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L3;
                }
            } else if (videoBitRate <= 2000000) {
                if ((videoWidth == 352 && videoHeight == 576 && fps <= 30) || (videoWidth == 352 && videoHeight == 480 && fps <= 30) ||
                        (videoWidth == 352 && videoHeight == 288 && fps <= 30) || (videoWidth == 352 && videoHeight == 240 && fps <= 30) ||
                        (videoWidth == 320 && videoHeight == 240 && fps <= 30) || (videoWidth == 320 && videoHeight == 180 && fps <= 30) ||
                        (videoWidth == 240 && videoHeight == 180 && fps <= 30) || (videoWidth == 208 && videoHeight == 160 && fps <= 30) ||
                        (videoWidth == 176 && videoHeight == 144 && fps <= 30) || (videoWidth == 176 && videoHeight == 120 && fps <= 30) ||
                        (videoWidth == 160 && videoHeight == 120 && fps <= 30) || (videoWidth == 160 && videoHeight == 112 && fps <= 30) ||
                        (videoWidth == 160 && videoHeight == 90 && fps <= 30) || (videoWidth == 128 && videoHeight == 96 && fps <= 30)) {
                    return DLNAProfile.VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L4;
                }
            } else if (videoBitRate <= 3000000) {
                if ((videoWidth == 640 && videoWidth == 480 && fps <= 30) || (videoWidth == 640 && videoWidth == 360 && fps <= 30)) {
                    return DLNAProfile.VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L3_VGA;
                }
            } else if (videoBitRate <= 8000000) {
                if ((videoWidth == 720 && videoHeight == 576 && fps <= 30) || (videoWidth == 720 && videoHeight == 480 && fps <= 30) ||
                        (videoWidth == 704 && videoHeight == 576 && fps <= 30) || (videoWidth == 704 && videoHeight == 480 && fps <= 30) ||
                        (videoWidth == 640 && videoHeight == 480 && fps <= 30) || (videoWidth == 640 && videoHeight == 360 && fps <= 30) ||
                        (videoWidth == 544 && videoHeight == 576 && fps <= 30) || (videoWidth == 544 && videoHeight == 480 && fps <= 30) ||
                        (videoWidth == 480 && videoHeight == 576 && fps <= 30) || (videoWidth == 480 && videoHeight == 480 && fps <= 30) ||
                        (videoWidth == 480 && videoHeight == 360 && fps <= 30) || (videoWidth == 480 && videoHeight == 270 && fps <= 30) ||
                        (videoWidth == 352 && videoHeight == 576 && fps <= 30) || (videoWidth == 352 && videoHeight == 480 && fps <= 30) ||
                        (videoWidth == 352 && videoHeight == 288 && fps <= 30) || (videoWidth == 352 && videoHeight == 240 && fps <= 30) ||
                        (videoWidth == 320 && videoHeight == 240 && fps <= 30) || (videoWidth == 320 && videoHeight == 180 && fps <= 30) ||
                        (videoWidth == 240 && videoHeight == 180 && fps <= 30) || (videoWidth == 208 && videoHeight == 160 && fps <= 30) ||
                        (videoWidth == 176 && videoHeight == 144 && fps <= 30) || (videoWidth == 176 && videoHeight == 120 && fps <= 30) ||
                        (videoWidth == 160 && videoHeight == 120 && fps <= 30) || (videoWidth == 160 && videoHeight == 112 && fps <= 30) ||
                        (videoWidth == 160 && videoHeight == 90 && fps <= 30) || (videoWidth == 128 && videoHeight == 96 && fps <= 30)) {
                    return DLNAProfile.VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L5;
                }
            }
        }
        // MPEG Part10
        else if ("H264".equalsIgnoreCase(videoCodec) || "X264".equalsIgnoreCase(videoCodec) || "LMP4".equalsIgnoreCase(videoCodec) || "AVC1".equalsIgnoreCase(videoCodec)) {
            if ((videoWidth == 352 && videoHeight == 288) || (videoWidth == 352 && videoHeight == 240) ||
                    (videoWidth == 320 && videoHeight == 240) || (videoWidth == 320 && videoHeight == 180) ||
                    (videoWidth == 240 && videoHeight == 180) || (videoWidth == 240 && videoHeight == 135) ||
                    (videoWidth == 208 && videoHeight == 160) || (videoWidth == 176 && videoHeight == 144) ||
                    (videoWidth == 176 && videoHeight == 120) || (videoWidth == 160 && videoHeight == 120) ||
                    (videoWidth == 160 && videoHeight == 112) || (videoWidth == 160 && videoHeight == 90) ||
                    (videoWidth == 128 && videoHeight == 96)) {
                if (videoBitRate <= 384000 && videoWidth == 320 && videoHeight == 240) {
                    return VideoProfile.VIDEO_PROFILE_AVC_BL_L12_CIF15;
                } else if (videoBitRate <= 128000) {
                    if (fps == 15) {
                        return VideoProfile.VIDEO_PROFILE_AVC_BL_QCIF15;
                    } else {
                        return VideoProfile.VIDEO_PROFILE_AVC_BL_L1B_QCIF;
                    }
                } else if (videoBitRate <= 384000) {
                    return VideoProfile.VIDEO_PROFILE_AVC_BL_CIF15;
                } else if (videoBitRate <= 520000) {
                    return VideoProfile.VIDEO_PROFILE_AVC_BL_CIF15_520;
                } else if (videoBitRate <= 540000) {
                    return VideoProfile.VIDEO_PROFILE_AVC_BL_CIF15_540;
                }  else if (videoBitRate <= 940000) {
                    return VideoProfile.VIDEO_PROFILE_AVC_BL_CIF30_940;
                }  else if (videoBitRate <= 1300000) {
                    return VideoProfile.VIDEO_PROFILE_AVC_BL_L2_CIF30;
                }  else if (videoBitRate <= 2000000) {
                    return VideoProfile.VIDEO_PROFILE_AVC_BL_CIF30;
                }
            }
            if ((videoWidth == 720 && videoHeight == 576) || (videoWidth == 720 && videoHeight == 480) ||
                    (videoWidth == 640 && videoHeight == 480) || (videoWidth == 640 && videoHeight == 360)) {
                if (videoBitRate <= 4000000) {
                    return VideoProfile.VIDEO_PROFILE_AVC_BL_L3_SD;
                }
            }
            if ((videoWidth == 720 && videoHeight == 576) || (videoWidth == 720 && videoHeight == 480) ||
                    (videoWidth == 704 && videoHeight == 576) || (videoWidth == 704 && videoHeight == 480) ||
                    (videoWidth == 640 && videoHeight == 480) || (videoWidth == 640 && videoHeight == 360) ||
                    (videoWidth == 544 && videoHeight == 576) || (videoWidth == 544 && videoHeight == 480) ||
                    (videoWidth == 480 && videoHeight == 576) || (videoWidth == 480 && videoHeight == 480) ||
                    (videoWidth == 480 && videoHeight == 360) || (videoWidth == 480 && videoHeight == 270) ||
                    (videoWidth == 352 && videoHeight == 576) || (videoWidth == 352 && videoHeight == 480) ||
                    (videoWidth == 352 && videoHeight == 288) || (videoWidth == 352 && videoHeight == 240) ||
                    (videoWidth == 320 && videoHeight == 240) || (videoWidth == 320 && videoHeight == 180) ||
                    (videoWidth == 240 && videoHeight == 180) || (videoWidth == 240 && videoHeight == 135) ||
                    (videoWidth == 208 && videoHeight == 160) || (videoWidth == 176 && videoHeight == 144) ||
                    (videoWidth == 176 && videoHeight == 120) || (videoWidth == 160 && videoHeight == 120) ||
                    (videoWidth == 160 && videoHeight == 112) || (videoWidth == 160 && videoHeight == 90) ||
                    (videoWidth == 128 && videoHeight == 96)) {
                if (videoBitRate <= 10000000) {
                    return VideoProfile.VIDEO_PROFILE_AVC_MP_SD;
                }
            }
            if ((videoWidth == 1920 && videoHeight == 1080) || (videoWidth == 1920 && videoHeight == 1152) ||
                    (videoWidth == 1920 && videoHeight == 540) || (videoWidth == 1280 && videoHeight == 720)) {
                if (videoBitRate <= 20000000) {
                    return VideoProfile.VIDEO_PROFILE_AVC_MP_HD;
                }
            }
        }
        // WMV
        else if ("WMV1".equalsIgnoreCase(videoCodec) || "WMV2".equalsIgnoreCase(videoCodec) || "WMV3".equalsIgnoreCase(videoCodec) || "WVC1".equalsIgnoreCase(videoCodec)) {
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
     * @param container          Container
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
    public static Profile getDLNAProfile(String container, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
        VideoProfile videoProfile = getVideoProfile(videoCodec, videoBitRate, videoWidth, videoHeight, fps);
        AudioProfile audioProfile = getAudioProfile(audioCodec, audioBitRate, audioSampleBitRate, audioChannels);
        return getDLNAProfile(container, videoProfile, audioProfile);
    }

    /**
     * Get DLNA Profile String
     *
     * @param container    Container
     * @param videoProfile Video Profile
     * @param audioProfile Audio Profile
     * @return DLNA Profile String
     */
    @SuppressWarnings({"ConstantConditions"})
    public static Profile getDLNAProfile(String container, VideoProfile videoProfile, AudioProfile audioProfile) {
        if (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG1 && audioProfile == AudioProfile.AUDIO_PROFILE_MP2) {
            return Profile.PROFILE_MPEG1;
        } else if (Container.isMPEGPS(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_PS_ES_NTSC && audioProfile == AudioProfile.AUDIO_PROFILE_MP2) {
            return Profile.PROFILE_MPEG_PS_NTSC;
        } else if (Container.isMPEGPS(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_PS_ES_NTSC && (audioProfile == AudioProfile.AUDIO_PROFILE_AC3 || audioProfile == DLNAProfile.AudioProfile.AUDIO_PROFILE_AC3_EXTENDED)) {
            return Profile.PROFILE_MPEG_PS_NTSC_XAC3;
        } else if (Container.isMPEGPS(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_PS_ES_PAL && audioProfile == AudioProfile.AUDIO_PROFILE_MP2) {
            return Profile.PROFILE_MPEG_PS_PAL;
        } else if (Container.isMPEGPS(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_PS_ES_PAL && (audioProfile == AudioProfile.AUDIO_PROFILE_AC3 || audioProfile == DLNAProfile.AudioProfile.AUDIO_PROFILE_AC3_EXTENDED)) {
            return Profile.PROFILE_MPEG_PS_PAL_XAC3;
        } else if (Container.isMPEGES(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_PS_ES_NTSC && audioProfile == AudioProfile.AUDIO_PROFILE_MP2) {
            return Profile.PROFILE_MPEG_ES_NTSC;
        } else if (Container.isMPEGES(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_PS_ES_NTSC && (audioProfile == AudioProfile.AUDIO_PROFILE_AC3 || audioProfile == DLNAProfile.AudioProfile.AUDIO_PROFILE_AC3_EXTENDED)) {
            return Profile.PROFILE_MPEG_ES_NTSC_XAC3;
        } else if (Container.isMPEGES(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_PS_ES_PAL && audioProfile == AudioProfile.AUDIO_PROFILE_MP2) {
            return Profile.PROFILE_MPEG_ES_PAL;
        } else if (Container.isMPEGES(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_PS_ES_PAL && (audioProfile == AudioProfile.AUDIO_PROFILE_AC3 || audioProfile == DLNAProfile.AudioProfile.AUDIO_PROFILE_AC3_EXTENDED)) {
            return Profile.PROFILE_MPEG_ES_PAL_XAC3;
        } else if (Container.isMPEGTS(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_TS_MP && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_MULT5)) {
            return Profile.PROFILE_MPEG_TS_MP_LL_AAC_ISO;
        } else if (Container.isMPEGTS(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_TS_EU_SD && audioProfile == AudioProfile.AUDIO_PROFILE_MP2) {
            return Profile.PROFILE_MPEG_TS_SD_EU_ISO;
        } else if (Container.isMPEGTS(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_TS_NA_SD && audioProfile == AudioProfile.AUDIO_PROFILE_MP2) {
            return Profile.PROFILE_MPEG_TS_SD_NA_ISO;
        } else if (Container.isMPEGTS(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_TS_NA_SD && (audioProfile == AudioProfile.AUDIO_PROFILE_AC3 || audioProfile == DLNAProfile.AudioProfile.AUDIO_PROFILE_AC3_EXTENDED)) {
            return Profile.PROFILE_MPEG_TS_SD_NA_XAC3_ISO;
        } else if (Container.isMPEGTS(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_TS_NA_HD && audioProfile == AudioProfile.AUDIO_PROFILE_MP2) {
            return Profile.PROFILE_MPEG_TS_HD_NA_ISO;
        } else if (Container.isMPEGTS(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG_TS_NA_HD && (audioProfile == AudioProfile.AUDIO_PROFILE_AC3 || audioProfile == DLNAProfile.AudioProfile.AUDIO_PROFILE_AC3_EXTENDED)) {
            return Profile.PROFILE_MPEG_TS_HD_NA_XAC3_ISO;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_MP4_SP_AAC;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC_LTP || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_LTP_MULT5 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_LTP_MULT7)) {
            return Profile.PROFILE_MPEG4_P2_MP4_SP_AAC_LTP;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L2 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L2_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L3 ||
                audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_MULT5 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L2 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L2_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L3 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_MP4_SP_HEAAC;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B && audioProfile == AudioProfile.AUDIO_PROFILE_ATRAC) {
            return Profile.PROFILE_MPEG4_P2_MP4_SP_ATRAC3plus;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L2 && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_MP4_SP_L2_AAC;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L2 && (audioProfile == AudioProfile.AUDIO_PROFILE_AMR || audioProfile == AudioProfile.AUDIO_PROFILE_AMR_WB)) {
            return Profile.PROFILE_MPEG4_P2_MP4_SP_L2_AMR;
        } else if (Container.isMPEG4(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L3 || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L3_VGA) && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_MP4_SP_VGA_AAC;
        } else if (Container.isMPEG4(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L3 || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L3_VGA) && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L2 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L2_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L3 ||
                audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_MULT5 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L2 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L2_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L3 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_MP4_SP_VGA_HEAAC;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L4 && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_MP4_ASP_L4_SO_AAC;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L4 && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L2 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L2_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L3 ||
                audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L2 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L2_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L3)) {
            return Profile.PROFILE_MPEG4_P2_MP4_ASP_L4_SO_HEAAC;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L4 && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_MULT5 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_MP4_ASP_L4_SO_HEAAC_MULT5;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L5 && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_MP4_ASP_L5_SO_AAC;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L5 && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L2 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L2_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_L3 ||
                audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L2 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L2_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_L3)) {
            return Profile.PROFILE_MPEG4_P2_MP4_ASP_L5_SO_HEAAC;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L5 && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_MULT5 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_HE_V2_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_MP4_ASP_L5_SO_HEAAC_MULT5;
        } else if (Container.isMPEG4(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L4 || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L5) && audioProfile == AudioProfile.AUDIO_PROFILE_ATRAC) {
            return Profile.PROFILE_MPEG4_P2_MP4_ASP_ATRAC3plus;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_H263 && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_MULT5)) {
            return Profile.PROFILE_MPEG4_H263_MP4_P0_L10_AAC;
        } else if (Container.isMPEG4(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_H263 && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC_LTP || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_LTP_MULT5 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_LTP_MULT7)) {
            return Profile.PROFILE_MPEG4_H263_MP4_P0_L10_AAC_LTP;
        } else if (Container.isMPEGTS(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L2) && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_TS_SP_AAC_ISO;
        } else if (Container.isMPEGTS(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L2) && (audioProfile == AudioProfile.AUDIO_PROFILE_MP3 || audioProfile == AudioProfile.AUDIO_PROFILE_MP3_EXTENDED)) {
            return Profile.PROFILE_MPEG4_P2_TS_SP_MPEG1_L3_ISO;
        } else if (Container.isMPEGTS(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L2) && (audioProfile == AudioProfile.AUDIO_PROFILE_AC3 || audioProfile == AudioProfile.AUDIO_PROFILE_AC3_EXTENDED)) {
            return Profile.PROFILE_MPEG4_P2_TS_SP_AC3_ISO;
        } else if (Container.isMPEGTS(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L2) && (audioProfile == AudioProfile.AUDIO_PROFILE_MP2)) {
            return Profile.PROFILE_MPEG4_P2_TS_SP_MPEG2_L2_ISO;
        } else if (Container.isMPEGTS(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L4 || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L5) && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_TS_ASP_AAC_ISO;
        } else if (Container.isMPEGTS(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L4 || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L5) && (audioProfile == AudioProfile.AUDIO_PROFILE_MP3 || audioProfile == AudioProfile.AUDIO_PROFILE_MP3_EXTENDED)) {
            return Profile.PROFILE_MPEG4_P2_TS_ASP_MPEG1_L3_ISO;
        } else if (Container.isMPEGTS(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L4 || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L5) && (audioProfile == AudioProfile.AUDIO_PROFILE_AC3 || audioProfile == AudioProfile.AUDIO_PROFILE_AC3_EXTENDED)) {
            return Profile.PROFILE_MPEG4_P2_TS_ASP_AC3_ISO;
        } else if (Container.isMPEGTS(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L3 || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L3_VGA) && (audioProfile == AudioProfile.AUDIO_PROFILE_AC3 || audioProfile == AudioProfile.AUDIO_PROFILE_AC3_EXTENDED)) {
            return Profile.PROFILE_MPEG4_P2_TS_CO_AC3_ISO;
        } else if (Container.isMPEGTS(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L3 || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L3_VGA) && (audioProfile == AudioProfile.AUDIO_PROFILE_MP2)) {
            return Profile.PROFILE_MPEG4_P2_TS_CO_MPEG2_L2_ISO;
        } else if (Container.isASF(container) && (videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B || videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L2) && audioProfile == AudioProfile.AUDIO_PROFILE_G726) {
            return Profile.PROFILE_MPEG4_P2_ASF_SP_G726;
        } else if (Container.isASF(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L4 && audioProfile == AudioProfile.AUDIO_PROFILE_G726) {
            return Profile.PROFILE_MPEG4_P2_ASF_ASP_L4_SO_G726;
        } else if (Container.isASF(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_ASP_L5 && audioProfile == AudioProfile.AUDIO_PROFILE_G726) {
            return Profile.PROFILE_MPEG4_P2_ASF_ASP_L5_SO_G726;
        } else if (Container.is3GPP(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B && (audioProfile == AudioProfile.AUDIO_PROFILE_AMR || audioProfile == AudioProfile.AUDIO_PROFILE_AMR_WB)) {
            return Profile.PROFILE_MPEG4_P2_3GPP_SP_L0B_AMR;
        } else if (Container.is3GPP(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_P2_SP_L0B && (audioProfile == AudioProfile.AUDIO_PROFILE_AAC || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_320 || audioProfile == AudioProfile.AUDIO_PROFILE_AAC_MULT5)) {
            return Profile.PROFILE_MPEG4_P2_3GPP_SP_L0B_AAC;
        } else if (Container.is3GPP(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_H263 && audioProfile == AudioProfile.AUDIO_PROFILE_AMR) {
            return Profile.PROFILE_MPEG4_H263_3GPP_P3_L10_AMR;
        } else if (Container.is3GPP(container) && videoProfile == VideoProfile.VIDEO_PROFILE_MPEG4_H263 && audioProfile == AudioProfile.AUDIO_PROFILE_AMR_WB) {
            return Profile.PROFILE_MPEG4_H263_3GPP_P0_L10_AMR_WBplus;
        } else if (videoProfile == VideoProfile.VIDEO_PROFILE_WMV_SIMPLE_LOW && audioProfile == AudioProfile.AUDIO_PROFILE_WMA_BASE) {
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
