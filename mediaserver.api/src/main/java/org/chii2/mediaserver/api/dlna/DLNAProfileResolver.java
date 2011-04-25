package org.chii2.mediaserver.api.dlna;

import org.teleal.cling.support.dlna.model.DLNAProfiles;

/**
 *  DLNA Profile Resolver
 */
public class DLNAProfileResolver {
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
    public static DLNAProfiles getAudioProfile(String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
        // AAC
        if (audioCodec.startsWith("AAC")) {
            if ("AAC LC".equalsIgnoreCase(audioCodec) && audioSampleBitRate >= 8000 && audioSampleBitRate <= 48000) {
                if (audioBitRate <= 320000 && audioChannels <= 2) {
                    return DLNAProfiles.AAC_ISO_320;
                } else if (audioBitRate <= 576000 && audioChannels <= 2) {
                    return DLNAProfiles.AAC_ISO;
                } else if (audioBitRate <= 1440000 && audioChannels <= 6) {
                    return DLNAProfiles.AAC_MULT5_ISO;
                }
            } else if ("AAC LTP".equalsIgnoreCase(audioCodec) && audioSampleBitRate >= 8000) {
                if (audioSampleBitRate <= 48000 && audioBitRate <= 576000 && audioChannels <= 2) {
                    return DLNAProfiles.AAC_LTP_ISO;
                } else if (audioSampleBitRate <= 96000 && audioBitRate <= 2880000 && audioChannels <= 6) {
                    return DLNAProfiles.AAC_LTP_MULT5_ISO;
                } else if (audioSampleBitRate <= 96000 && audioBitRate <= 4032000 && audioChannels <= 8) {
                    return DLNAProfiles.AAC_LTP_MULT7_ISO;
                }
            } else if ("AAC HE".equalsIgnoreCase(audioCodec) && audioSampleBitRate >= 8000) {
                if (audioSampleBitRate <= 24000 && audioBitRate <= 320000 && audioChannels <= 2) {
                    return DLNAProfiles.HEAAC_L2_ISO_320;
                } else if (audioSampleBitRate <= 24000 && audioBitRate <= 576000 && audioChannels <= 2) {
                    return DLNAProfiles.HEAAC_L2_ISO;
                } else if (audioSampleBitRate <= 48000 && audioBitRate <= 576000) {
                    return DLNAProfiles.HEAAC_L3_ISO;
                } else if (audioSampleBitRate <= 48000 && audioBitRate <= 1440000) {
                    return DLNAProfiles.HEAAC_MULT5_ISO;
                }
            } else if ("AAC PARAM".equalsIgnoreCase(audioCodec) && audioSampleBitRate >= 8000) {
                if (audioSampleBitRate <= 24000 && audioBitRate <= 320000 && audioChannels <= 2) {
                    return DLNAProfiles.HEAACv2_L2_320;
                } else if (audioSampleBitRate <= 24000 && audioBitRate <= 576000 && audioChannels <= 2) {
                    return DLNAProfiles.HEAACv2_L2;
                } else if (audioSampleBitRate <= 48000 && audioBitRate <= 576000) {
                    return DLNAProfiles.HEAACv2_L3;
                } else if (audioSampleBitRate <= 48000 && audioBitRate <= 1440000) {
                    return DLNAProfiles.HEAACv2_MULT5;
                }
            } else if ("AAC BSAC".equalsIgnoreCase(audioCodec) && audioSampleBitRate >= 8000 && audioSampleBitRate <= 48000) {
                if (audioBitRate <= 128000 && audioChannels <= 2) {
                    return DLNAProfiles.BSAC_ISO;
                } else if (audioBitRate <= 128000 && audioChannels <= 6) {
                    return DLNAProfiles.BSAC_MULT5_ISO;
                }
            }
        }
        // AC3
        else if ("2000".equalsIgnoreCase(audioCodec)) {
            if ((audioSampleBitRate == 32000 || audioSampleBitRate == 44100 || audioSampleBitRate == 48000) && audioBitRate >= 32000 && audioChannels <= 6) {
                if (audioBitRate <= 448000) {
                    return DLNAProfiles.AC3;
                //} else if (audioBitRate <= 640000) {
                //    return DLNAProfiles.AC3_EXTENDED;
                }
            }
        }
        // AMR
        else if ("samr".equalsIgnoreCase(audioCodec)) {
            if (audioSampleBitRate == 8000 && audioChannels == 1 && (audioBitRate == 4750 ||
                    audioBitRate == 5150 || audioBitRate == 5900 || audioBitRate == 6700 || audioBitRate == 7400 ||
                    audioBitRate == 7950 || audioBitRate == 10200 || audioBitRate == 12200)) {
                return DLNAProfiles.AMR;
            } else if (audioBitRate >= 5200 && audioBitRate <= 48000 && audioChannels <= 2 && (audioSampleBitRate == 8000 ||
                    audioSampleBitRate == 16000 || audioSampleBitRate == 24000 || audioSampleBitRate == 32000 || audioSampleBitRate == 48000)) {
                return DLNAProfiles.AMR_WBplus;
            }
        }
        // ATRAC
        else if ("270".equalsIgnoreCase(audioCodec)) {
            return DLNAProfiles.ATRAC3;
        }
        // G726
        //else if ("64".equalsIgnoreCase(audioCodec) || "85".equalsIgnoreCase(audioCodec) || "140".equalsIgnoreCase(audioCodec)) {
        //    return DLNAProfile.AudioProfile.AUDIO_PROFILE_G726;
        //}
        // LPCM
        else if ("sowt".equalsIgnoreCase(audioCodec)) {
            if (audioSampleBitRate >= 8000 && audioSampleBitRate <= 48000 && audioChannels <= 2) {
                return DLNAProfiles.LPCM;
            }
        }
        // MP2
        //else if ("50".equalsIgnoreCase(audioCodec)) {
        //    if (audioBitRate >= 32000 && audioBitRate <= 448000 && (audioSampleBitRate == 32000 || audioSampleBitRate == 44100 || audioSampleBitRate == 48000) && audioChannels <= 6) {
        //       return DLNAProfiles.MP2;
        //    }
        //}
        // WMA
        else if ("160".equalsIgnoreCase(audioCodec) || "161".equalsIgnoreCase(audioCodec)) {
            if (audioSampleBitRate <= 48000) {
                if (audioBitRate <= 193000 && audioChannels <= 2) {
                    return DLNAProfiles.WMABASE;
                } else if (audioBitRate <= 385000 && audioChannels <= 2) {
                    return DLNAProfiles.WMABASE;
                }
            } else if (audioSampleBitRate <= 96000) {
                if (audioBitRate <= 1500000 && audioChannels <= 8) {
                    return DLNAProfiles.WMAPRO;
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
                    return DLNAProfiles.MP3;
                }
            } else if ((audioSampleBitRate == 16000 || audioSampleBitRate == 22050 || audioSampleBitRate == 24000) && audioChannels <= 2) {
                if (audioBitRate == 8000 || audioBitRate == 16000 || audioBitRate == 24000 ||
                        audioBitRate == 32000 || audioBitRate == 40000 || audioBitRate == 48000 || audioBitRate == 56000 ||
                        audioBitRate == 64000 || audioBitRate == 80000 || audioBitRate == 96000 || audioBitRate == 112000 ||
                        audioBitRate == 128000 || audioBitRate == 160000 || audioBitRate == 192000 || audioBitRate == 224000 ||
                        audioBitRate == 256000 || audioBitRate == 320000) {
                    return DLNAProfiles.MP3X;
                }
            }
        }

        // Invalid
        return DLNAProfiles.NONE;
    }
}
