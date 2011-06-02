package org.chii2.transcoder.core.dlna.catalog;

import org.chii2.transcoder.core.dlna.codec.Container;
import org.chii2.transcoder.core.dlna.codec.VideoCodec;
import org.chii2.transcoder.core.dlna.restriction.VideoRestriction;
import org.teleal.cling.support.model.dlna.DLNAProfiles;

/**
 * MPEG4 Part2
 */
public class MPEG4P2 extends VideoCatalog {
    public MPEG4P2() {
        // MPEG4_P2_MP4_SP_AAC
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_SP_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_SP_L0B_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000)
        });
        // MPEG4_P2_MP4_SP_AAC_LTP
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_SP_AAC_LTP, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_SP_L0B_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000)
        });
        // MPEG4_P2_MP4_SP_HEAAC
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_SP_HEAAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_SP_L0B_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000)
        });
        // MPEG4_P2_MP4_SP_ATRAC3plus
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_SP_ATRAC3plus, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.ATRAC3}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_SP_L0B_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000)
        });
        // MPEG4_P2_MP4_SP_L2_AAC
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_SP_L2_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_SP_L2_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000)
        });
        // MPEG4_P2_MP4_SP_L2_AMR
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_SP_L2_AMR, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AMR}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_SP_L2_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000)
        });
        // MPEG4_P2_MP4_SP_VGA_AAC
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_SP_VGA_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_SP_L3_Restriction(),
                new VideoBitRateRangeRestriction(0, 3000000)
        });
        // MPEG4_P2_MP4_SP_VGA_HEAAC
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_SP_VGA_HEAAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_SP_L3_Restriction(),
                new VideoBitRateRangeRestriction(0, 3000000)
        });
        // MPEG4_P2_MP4_ASP_AAC
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_ASP_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_ASP_L5_Restriction(),
                new VideoBitRateRangeRestriction(0, 8000000)
        });
        // MPEG4_P2_MP4_ASP_HEAAC
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_ASP_HEAAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_ASP_L5_Restriction(),
                new VideoBitRateRangeRestriction(0, 8000000)
        });
        // MPEG4_P2_MP4_ASP_HEAAC_MULT5
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_ASP_HEAAC_MULT5, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_MULT5_ISO, DLNAProfiles.HEAAC_MULT5_ADTS}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_ASP_L5_Restriction(),
                new VideoBitRateRangeRestriction(0, 8000000)
        });
        // MPEG4_P2_MP4_ASP_HEAAC_MULT5
        profileMap.put(DLNAProfiles.MPEG4_P2_MP4_ASP_ATRAC3plus, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.ATRAC3}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_ASP_L5_Restriction(),
                new VideoBitRateRangeRestriction(0, 8000000)
        });
        // MPEG4_H263_MP4_P0_L10_AAC
        profileMap.put(DLNAProfiles.MPEG4_H263_MP4_P0_L10_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.H263),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new H263Restriction(),
                new VideoBitRateRangeRestriction(0, 64000)
        });
        // MPEG4_H263_MP4_P0_L10_AAC_LTP
        profileMap.put(DLNAProfiles.MPEG4_H263_MP4_P0_L10_AAC_LTP, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.H263),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new H263Restriction(),
                new VideoBitRateRangeRestriction(0, 64000)
        });

        // MPEG4_P2_TS_SP_AAC_ISO
        profileMap.put(DLNAProfiles.MPEG4_P2_TS_SP_AAC_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG_TS),
                new MPEG4_L3_CO_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000)
        });
        // MPEG4_P2_TS_SP_MPEG1_L3_ISO
        profileMap.put(DLNAProfiles.MPEG4_P2_TS_SP_MPEG1_L3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.MP3}),
                new ContainerRestriction(Container.MPEG_TS),
                new MPEG4_L3_CO_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000)
        });
        // MPEG4_P2_TS_SP_AC3_ISO
        profileMap.put(DLNAProfiles.MPEG4_P2_TS_SP_AC3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AC3Restriction(),
                new ContainerRestriction(Container.MPEG_TS),
                new MPEG4_L3_CO_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000)
        });
        // MPEG4_P2_TS_SP_MPEG2_L2_ISO
        profileMap.put(DLNAProfiles.MPEG4_P2_TS_SP_MPEG2_L2_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new MP2Restriction(),
                new ContainerRestriction(Container.MPEG_TS),
                new MPEG4_L3_CO_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000)
        });
        // MPEG4_P2_TS_CO_MPEG2_L2_ISO
        profileMap.put(DLNAProfiles.MPEG4_P2_TS_CO_MPEG2_L2_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new MP2Restriction(),
                new ContainerRestriction(Container.MPEG_TS),
                new MPEG4_ASP_L4_SO_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000)
        });
        // MPEG4_P2_TS_CO_AC3_ISO
        profileMap.put(DLNAProfiles.MPEG4_P2_TS_CO_AC3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AC3Restriction(),
                new ContainerRestriction(Container.MPEG_TS),
                new MPEG4_ASP_L4_SO_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000)
        });
        // MPEG4_P2_TS_ASP_AAC_ISO
        profileMap.put(DLNAProfiles.MPEG4_P2_TS_ASP_AAC_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG_TS),
                new MPEG4_ASP_L5_Restriction(),
                new VideoBitRateRangeRestriction(0, 8000000)
        });
        // MPEG4_P2_TS_ASP_MPEG1_L3_ISO
        profileMap.put(DLNAProfiles.MPEG4_P2_TS_ASP_MPEG1_L3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.MP3}),
                new ContainerRestriction(Container.MPEG_TS),
                new MPEG4_ASP_L5_Restriction(),
                new VideoBitRateRangeRestriction(0, 8000000)
        });
        // MPEG4_P2_TS_ASP_AC3_ISO
        profileMap.put(DLNAProfiles.MPEG4_P2_TS_ASP_AC3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AC3Restriction(),
                new ContainerRestriction(Container.MPEG_TS),
                new MPEG4_ASP_L5_Restriction(),
                new VideoBitRateRangeRestriction(0, 8000000)
        });

        // MPEG4_P2_ASF_SP_G726
        profileMap.put(DLNAProfiles.MPEG4_P2_ASF_SP_G726, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new G726Restriction(),
                new ContainerRestriction(Container.WM),
                new MPEG4_L3_CO_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000)
        });

        // MPEG4_P2_ASF_ASP_L4_SO_G726
        profileMap.put(DLNAProfiles.MPEG4_P2_ASF_ASP_L4_SO_G726, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new G726Restriction(),
                new ContainerRestriction(Container.WM),
                new MPEG4_ASP_L4_SO_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000)
        });
        // MPEG4_P2_ASF_SP_G726
        profileMap.put(DLNAProfiles.MPEG4_P2_ASF_SP_G726, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new G726Restriction(),
                new ContainerRestriction(Container.WM),
                new MPEG4_ASP_L5_Restriction(),
                new VideoBitRateRangeRestriction(0, 8000000)
        });

        // TODO: 3GPP Container can't be determined
        // MPEG4_H263_3GPP_P3_L10_AMR
        profileMap.put(DLNAProfiles.MPEG4_H263_3GPP_P3_L10_AMR, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.H263),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AMR}),
                new ContainerRestriction(Container.MPEG4),
                new H263Restriction(),
                new VideoBitRateRangeRestriction(0, 64000)
        });
        // MPEG4_H263_3GPP_P0_L10_AMR_WBplus
        profileMap.put(DLNAProfiles.MPEG4_H263_3GPP_P0_L10_AMR_WBplus, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.H263),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AMR_WBplus}),
                new ContainerRestriction(Container.MPEG4),
                new H263Restriction(),
                new VideoBitRateRangeRestriction(0, 64000)
        });
        // MPEG4_P2_3GPP_SP_L0B_AMR
        profileMap.put(DLNAProfiles.MPEG4_P2_3GPP_SP_L0B_AMR, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AMR}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_SP_L0B_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000)
        });
        // MPEG4_P2_3GPP_SP_L0B_AAC
        profileMap.put(DLNAProfiles.MPEG4_P2_3GPP_SP_L0B_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P2),
                 new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MPEG4_SP_L0B_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000)
        });
    }

    /**
     * H263
     */
    public class H263Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 176 && videoHeight == 144 && fps <= 15) return true;
            if (videoWidth == 128 && videoHeight == 96 && fps <= 15) return true;
            return false;
        }
    }

    /**
     * MPEG-4 SP L0B
     */
    public class MPEG4_SP_L0B_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 176 && videoHeight == 144 && fps <= 15) return true;
            if (videoWidth == 128 && videoHeight == 96 && fps <= 15) return true;
            return false;
        }
    }

    /**
     * MPEG-4 SP L2
     */
    public class MPEG4_SP_L2_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 352 && videoHeight == 288 && fps <= 15) return true;
            if (videoWidth == 320 && videoHeight == 240 && fps <= 15) return true;
            if (videoWidth == 320 && videoHeight == 180 && fps <= 15) return true;
            if (videoWidth == 176 && videoHeight == 144 && fps <= 30) return true;
            if (videoWidth == 128 && videoHeight == 96 && fps <= 30) return true;
            return false;
        }
    }

    /**
     * MPEG-4 SP L3
     */
    public class MPEG4_SP_L3_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 640 && videoHeight == 480 && fps <= 30) return true;
            if (videoWidth == 640 && videoHeight == 360 && fps <= 30) return true;
            return false;
        }
    }

    /**
     * MPEG-4 L3 / CO
     */
    public class MPEG4_L3_CO_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 352 && videoHeight == 288 && fps <= 30) return true;
            if (videoWidth == 352 && videoHeight == 240 && fps <= 30) return true;
            if (videoWidth == 320 && videoHeight == 240 && fps <= 30) return true;
            if (videoWidth == 320 && videoHeight == 180 && fps <= 30) return true;
            if (videoWidth == 240 && videoHeight == 180 && fps <= 30) return true;
            if (videoWidth == 208 && videoHeight == 160 && fps <= 30) return true;
            if (videoWidth == 176 && videoHeight == 144 && fps <= 30) return true;
            if (videoWidth == 176 && videoHeight == 120 && fps <= 30) return true;
            if (videoWidth == 160 && videoHeight == 120 && fps <= 30) return true;
            if (videoWidth == 160 && videoHeight == 112 && fps <= 30) return true;
            if (videoWidth == 160 && videoHeight == 90 && fps <= 30) return true;
            if (videoWidth == 128 && videoHeight == 96 && fps <= 30) return true;
            return false;
        }
    }

    /**
     * MPEG-4 ASP L4 SO
     */
    public class MPEG4_ASP_L4_SO_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @SuppressWarnings({"ConstantConditions"})
        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 352 && videoHeight == 576 && fps <= 30) return true;
            if (videoWidth == 352 && videoHeight == 480 && fps <= 30) return true;
            if (videoWidth == 352 && videoHeight == 288 && fps <= 30) return true;
            if (videoWidth == 352 && videoHeight == 240 && fps <= 30) return true;
            if (videoWidth == 320 && videoHeight == 240 && fps <= 30) return true;
            if (videoWidth == 320 && videoHeight == 180 && fps <= 30) return true;
            if (videoWidth == 240 && videoHeight == 180 && fps <= 30) return true;
            if (videoWidth == 208 && videoHeight == 160 && fps <= 30) return true;
            if (videoWidth == 176 && videoHeight == 144 && fps <= 30) return true;
            if (videoWidth == 176 && videoHeight == 120 && fps <= 30) return true;
            if (videoWidth == 160 && videoHeight == 120 && fps <= 30) return true;
            if (videoWidth == 160 && videoHeight == 112 && fps <= 30) return true;
            if (videoWidth == 160 && videoHeight == 90 && fps <= 30) return true;
            if (videoWidth == 128 && videoHeight == 96 && fps <= 30) return true;
            return false;
        }
    }

    /**
     * MPEG-4 ASP L5
     */
    public class MPEG4_ASP_L5_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @SuppressWarnings({"ConstantConditions"})
        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 720 && videoHeight == 576 && fps <= 30) return true;
            if (videoWidth == 720 && videoHeight == 480 && fps <= 30) return true;
            if (videoWidth == 704 && videoHeight == 576 && fps <= 30) return true;
            if (videoWidth == 704 && videoHeight == 480 && fps <= 30) return true;
            if (videoWidth == 640 && videoHeight == 480 && fps <= 30) return true;
            if (videoWidth == 640 && videoHeight == 360 && fps <= 30) return true;
            if (videoWidth == 544 && videoHeight == 576 && fps <= 30) return true;
            if (videoWidth == 544 && videoHeight == 480 && fps <= 30) return true;
            if (videoWidth == 480 && videoHeight == 576 && fps <= 30) return true;
            if (videoWidth == 480 && videoHeight == 480 && fps <= 30) return true;
            if (videoWidth == 480 && videoHeight == 360 && fps <= 30) return true;
            if (videoWidth == 480 && videoHeight == 270 && fps <= 30) return true;
            if (videoWidth == 352 && videoHeight == 576 && fps <= 30) return true;
            if (videoWidth == 352 && videoHeight == 480 && fps <= 30) return true;
            if (videoWidth == 352 && videoHeight == 288 && fps <= 30) return true;
            if (videoWidth == 352 && videoHeight == 240 && fps <= 30) return true;
            if (videoWidth == 320 && videoHeight == 240 && fps <= 30) return true;
            if (videoWidth == 320 && videoHeight == 180 && fps <= 30) return true;
            if (videoWidth == 240 && videoHeight == 180 && fps <= 30) return true;
            if (videoWidth == 208 && videoHeight == 160 && fps <= 30) return true;
            if (videoWidth == 176 && videoHeight == 144 && fps <= 30) return true;
            if (videoWidth == 176 && videoHeight == 120 && fps <= 30) return true;
            if (videoWidth == 160 && videoHeight == 120 && fps <= 30) return true;
            if (videoWidth == 160 && videoHeight == 112 && fps <= 30) return true;
            if (videoWidth == 160 && videoHeight == 90 && fps <= 30) return true;
            if (videoWidth == 128 && videoHeight == 96 && fps <= 30) return true;
            return false;
        }
    }
}
