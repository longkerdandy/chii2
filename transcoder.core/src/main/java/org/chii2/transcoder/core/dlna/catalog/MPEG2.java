package org.chii2.transcoder.core.dlna.catalog;

import org.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.chii2.transcoder.core.dlna.codec.Container;
import org.chii2.transcoder.core.dlna.codec.VideoCodec;
import org.chii2.transcoder.core.dlna.restriction.VideoRestriction;
import org.teleal.cling.support.model.dlna.DLNAProfiles;

/**
 * MPEG2
 */
public class MPEG2 extends VideoCatalog {
    public MPEG2() {
        // MPEG_PS_NTSC
        profileMap.put(DLNAProfiles.MPEG_PS_NTSC, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_PS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_NTSC_Restriction(),
                new MPEG_PS_ES_LPCM_Restriction()
        });
        // MPEG_PS_NTSC
        profileMap.put(DLNAProfiles.MPEG_PS_NTSC, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_PS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_NTSC_Restriction(),
                new MPEG_PS_ES_MP2_Restriction()
        });
        // MPEG_PS_NTSC
        profileMap.put(DLNAProfiles.MPEG_PS_NTSC, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_PS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_NTSC_Restriction(),
                new AC3Restriction()
        });
        // MPEG_PS_NTSC_XAC3
        profileMap.put(DLNAProfiles.MPEG_PS_NTSC_XAC3, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_PS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_NTSC_Restriction(),
                new AC3XRestriction()
        });
        // MPEG_PS_PAL
        profileMap.put(DLNAProfiles.MPEG_PS_PAL, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_PS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_PAL_Restriction(),
                new MPEG_PS_ES_LPCM_Restriction()
        });
        // MPEG_PS_PAL
        profileMap.put(DLNAProfiles.MPEG_PS_PAL, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_PS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_PAL_Restriction(),
                new MPEG_PS_ES_MP2_Restriction()
        });
        // MPEG_PS_PAL
        profileMap.put(DLNAProfiles.MPEG_PS_PAL, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_PS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_PAL_Restriction(),
                new AC3Restriction()
        });
        // MPEG_PS_PAL_XAC3
        profileMap.put(DLNAProfiles.MPEG_PS_PAL_XAC3, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_PS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_PAL_Restriction(),
                new AC3XRestriction()
        });

        // MPEG_ES_NTSC
        profileMap.put(DLNAProfiles.MPEG_ES_NTSC, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_ES),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_NTSC_Restriction(),
                new MPEG_PS_ES_LPCM_Restriction()
        });
        // MPEG_ES_NTSC
        profileMap.put(DLNAProfiles.MPEG_ES_NTSC, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_ES),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_NTSC_Restriction(),
                new MPEG_PS_ES_MP2_Restriction()
        });
        // MPEG_ES_NTSC
        profileMap.put(DLNAProfiles.MPEG_ES_NTSC, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_ES),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_NTSC_Restriction(),
                new AC3Restriction()
        });
        // MPEG_ES_NTSC_XAC3
        profileMap.put(DLNAProfiles.MPEG_ES_NTSC_XAC3, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_ES),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_NTSC_Restriction(),
                new AC3XRestriction()
        });
        // MPEG_ES_PAL
        profileMap.put(DLNAProfiles.MPEG_ES_PAL, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_ES),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_PAL_Restriction(),
                new MPEG_PS_ES_LPCM_Restriction()
        });
        // MPEG_ES_PAL
        profileMap.put(DLNAProfiles.MPEG_ES_PAL, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_ES),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_PAL_Restriction(),
                new MPEG_PS_ES_MP2_Restriction()
        });
        // MPEG_ES_PAL
        profileMap.put(DLNAProfiles.MPEG_ES_PAL, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_ES),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_PAL_Restriction(),
                new AC3Restriction()
        });
        // MPEG_ES_PAL_XAC3
        profileMap.put(DLNAProfiles.MPEG_ES_PAL_XAC3, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_ES),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_PS_ES_PAL_Restriction(),
                new AC3XRestriction()
        });

        // MPEG_TS_MP_LL_AAC_ISO
        profileMap.put(DLNAProfiles.MPEG_TS_MP_LL_AAC_ISO, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_TS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_TS_MP_Restriction(),
                new VideoBitRateRangeRestriction(0, 4000000)
        });
        // MPEG_TS_SD_EU_ISO
        profileMap.put(DLNAProfiles.MPEG_TS_SD_EU_ISO, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_TS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_TS_EU_SD_Restriction(),
                new MP2Restriction()
        });
        // MPEG_TS_SD_EU_ISO
        profileMap.put(DLNAProfiles.MPEG_TS_SD_EU_ISO, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_TS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_TS_EU_SD_Restriction(),
                new AC3Restriction()
        });
        // MPEG_TS_SD_NA_ISO
        profileMap.put(DLNAProfiles.MPEG_TS_SD_NA_ISO, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_TS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_TS_NA_SD_Restriction(),
                new AC3Restriction()
        });
        // MPEG_TS_SD_NA_XAC3_ISO
        profileMap.put(DLNAProfiles.MPEG_TS_SD_NA_XAC3_ISO, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_TS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_TS_NA_SD_Restriction(),
                new AC3XRestriction()
        });
        // MPEG_TS_HD_NA_ISO
        profileMap.put(DLNAProfiles.MPEG_TS_HD_NA_ISO, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_TS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_TS_NA_HD_Restriction(),
                new AC3Restriction()
        });
        // MPEG_TS_HD_NA_XAC3_ISO
        profileMap.put(DLNAProfiles.MPEG_TS_HD_NA_XAC3_ISO, new VideoRestriction[]{
                new ContainerRestriction(Container.MPEG_TS),
                new VideoCodecRestriction(VideoCodec.MPEG2),
                new MPEG_TS_NA_HD_Restriction(),
                new AC3XRestriction()
        });
    }

    /**
     * MPEG2 PS ES NTSC
     */
    public class MPEG_PS_ES_NTSC_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 720 && videoHeight == 480 && isFPS2997(fps)) return true;
            if (videoWidth == 704 && videoHeight == 480 && isFPS2997(fps)) return true;
            if (videoWidth == 544 && videoHeight == 480 && isFPS2997(fps)) return true;
            if (videoWidth == 480 && videoHeight == 480 && isFPS2997(fps)) return true;
            if (videoWidth == 352 && videoHeight == 480 && isFPS2997(fps)) return true;
            if (videoWidth == 352 && videoHeight == 240 && isFPS2997(fps)) return true;
            return false;
        }
    }

    /**
     * MPEG2 PS ES PAL
     */
    public class MPEG_PS_ES_PAL_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 720 && videoHeight == 576 && isFPS25(fps)) return true;
            if (videoWidth == 704 && videoHeight == 576 && isFPS25(fps)) return true;
            if (videoWidth == 544 && videoHeight == 576 && isFPS25(fps)) return true;
            if (videoWidth == 480 && videoHeight == 576 && isFPS25(fps)) return true;
            if (videoWidth == 352 && videoHeight == 576 && isFPS25(fps)) return true;
            if (videoWidth == 352 && videoHeight == 288 && isFPS25(fps)) return true;
            return false;
        }
    }

    /**
     * MPEG2 TS MP
     */
    public class MPEG_TS_MP_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps, audioBitRate};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            long audioBitRate = (Long) value[3];
            if (videoWidth == 352 && videoHeight == 288 && isFPS30(fps) && audioBitRate <= 256000) return true;
            return false;
        }
    }

    /**
     * MPEG2 TS EU SD
     */
    public class MPEG_TS_EU_SD_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 720 && videoHeight == 576 && isFPS25(fps)) return true;
            if (videoWidth == 544 && videoHeight == 576 && isFPS25(fps)) return true;
            if (videoWidth == 480 && videoHeight == 576 && isFPS25(fps)) return true;
            if (videoWidth == 352 && videoHeight == 576 && isFPS25(fps)) return true;
            if (videoWidth == 352 && videoHeight == 288 && isFPS25(fps)) return true;
            return false;
        }
    }

    /**
     * MPEG2 TS NA SD
     */
    public class MPEG_TS_NA_SD_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 720 && videoHeight == 480 && isFPS2997(fps)) return true;
            if (videoWidth == 704 && videoHeight == 480 && isFPS30(fps)) return true;
            if (videoWidth == 704 && videoHeight == 480 && isFPS2997(fps)) return true;
            if (videoWidth == 704 && videoHeight == 480 && isFPS24(fps)) return true;
            if (videoWidth == 704 && videoHeight == 480 && isFPS23976(fps)) return true;
            if (videoWidth == 640 && videoHeight == 480 && isFPS30(fps)) return true;
            if (videoWidth == 640 && videoHeight == 480 && isFPS2997(fps)) return true;
            if (videoWidth == 640 && videoHeight == 480 && isFPS24(fps)) return true;
            if (videoWidth == 640 && videoHeight == 480 && isFPS23976(fps)) return true;
            if (videoWidth == 544 && videoHeight == 480 && isFPS2997(fps)) return true;
            if (videoWidth == 480 && videoHeight == 480 && isFPS2997(fps)) return true;
            if (videoWidth == 352 && videoHeight == 480 && isFPS2997(fps)) return true;
            return false;
        }
    }

    /**
     * MPEG2 TS NA HD
     */
    public class MPEG_TS_NA_HD_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 1920 && videoHeight == 1080 && isFPS30(fps)) return true;
            if (videoWidth == 1920 && videoHeight == 1080 && isFPS2997(fps)) return true;
            if (videoWidth == 1920 && videoHeight == 1080 && isFPS24(fps)) return true;
            if (videoWidth == 1920 && videoHeight == 1080 && isFPS23976(fps)) return true;
            if (videoWidth == 1280 && videoHeight == 720 && isFPS30(fps)) return true;
            if (videoWidth == 1280 && videoHeight == 720 && isFPS2997(fps)) return true;
            if (videoWidth == 1280 && videoHeight == 720 && isFPS24(fps)) return true;
            if (videoWidth == 1280 && videoHeight == 720 && isFPS23976(fps)) return true;
            if (videoWidth == 1440 && videoHeight == 1080 && isFPS30(fps)) return true;
            if (videoWidth == 1440 && videoHeight == 1080 && isFPS2997(fps)) return true;
            if (videoWidth == 1440 && videoHeight == 1080 && isFPS24(fps)) return true;
            if (videoWidth == 1440 && videoHeight == 1080 && isFPS23976(fps)) return true;
            if (videoWidth == 1280 && videoHeight == 1080 && isFPS30(fps)) return true;
            if (videoWidth == 1280 && videoHeight == 1080 && isFPS2997(fps)) return true;
            if (videoWidth == 1280 && videoHeight == 1080 && isFPS24(fps)) return true;
            if (videoWidth == 1280 && videoHeight == 1080 && isFPS23976(fps)) return true;
            return false;
        }
    }

    /**
     * MPEG PS ES LPCM
     */
    public class MPEG_PS_ES_LPCM_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{audioProfile, audioBitRate, audioChannels};
        }

        @Override
        public boolean pass(Object[] value) {
            DLNAProfiles audioProfile = (DLNAProfiles) value[0];
            long audioBitRate = (Long) value[1];
            int audioChannels = (Integer) value[2];
            if (audioProfile != DLNAProfiles.LPCM && audioProfile != DLNAProfiles.LPCM_LOW) return false;
            if (audioChannels == 1 && audioBitRate > 768000) return false;
            if (audioChannels == 2 && audioBitRate > 1536000) return false;
            return true;
        }
    }

    /**
     * MPEG PS ES MP2
     */
    public class MPEG_PS_ES_MP2_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, audioSampleBitRate, audioBitRate, audioChannels};
        }

        @Override
        public boolean pass(Object[] value) {
            String audioFormat = (String) value[0];
            String audioFormatProfile = (String) value[1];
            int audioFormatVersion = (Integer) value[2];
            String audioCodec = (String) value[3];
            long audioSampleBitRate = (Long) value[4];
            long audioBitRate = (Long) value[5];
            int audioChannels = (Integer) value[6];
            if (!AudioCodec.match(audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, AudioCodec.MP2))
                return false;
            if (!(audioSampleBitRate == 32000 || audioSampleBitRate == 44100 || audioSampleBitRate == 48000))
                return false;
            if (audioChannels > 2) return false;
            if (audioChannels == 1 && (audioBitRate < 64000 || audioBitRate > 192000)) return false;
            if (audioChannels == 2 && (audioBitRate < 64000 || audioBitRate > 384000)) return false;
            return true;
        }
    }
}
