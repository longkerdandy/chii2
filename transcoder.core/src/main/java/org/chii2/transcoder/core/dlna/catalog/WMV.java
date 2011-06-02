package org.chii2.transcoder.core.dlna.catalog;

import org.chii2.transcoder.core.dlna.codec.Container;
import org.chii2.transcoder.core.dlna.codec.VideoCodec;
import org.chii2.transcoder.core.dlna.restriction.VideoRestriction;
import org.teleal.cling.support.model.dlna.DLNAProfiles;

/**
 * WMV
 */
public class WMV extends VideoCatalog {
    public WMV() {
        // WMVSPLL_BASE
        profileMap.put(DLNAProfiles.WMVSPLL_BASE, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.WMV),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.WMABASE}),
                new ContainerRestriction(Container.WM),
                new Simple_Low_Restriction()
        });
        // WMVSPML_BASE
        profileMap.put(DLNAProfiles.WMVSPML_BASE, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.WMV),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.WMABASE}),
                new ContainerRestriction(Container.WM),
                new Simple_Medium_Restriction()
        });
        // WMVSPML_MP3
        profileMap.put(DLNAProfiles.WMVSPML_MP3, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.WMV),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.MP3}),
                new ContainerRestriction(Container.WM),
                new Simple_Medium_Restriction()
        });
        // WMVMED_BASE
        profileMap.put(DLNAProfiles.WMVMED_BASE, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.WMV),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.WMABASE}),
                new ContainerRestriction(Container.WM),
                new Main_Medium_Restriction()
        });
        // WMVMED_FULL
        profileMap.put(DLNAProfiles.WMVMED_FULL, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.WMV),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.WMAFULL}),
                new ContainerRestriction(Container.WM),
                new Main_Medium_Restriction()
        });
        // WMVMED_PRO
        profileMap.put(DLNAProfiles.WMVMED_PRO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.WMV),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.WMAPRO}),
                new ContainerRestriction(Container.WM),
                new Main_Medium_Restriction()
        });
        // WMVHIGH_FULL
        profileMap.put(DLNAProfiles.WMVHIGH_FULL, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.WMV),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.WMAFULL}),
                new ContainerRestriction(Container.WM),
                new Main_High_Restriction()
        });
        // WMVHIGH_PRO
        profileMap.put(DLNAProfiles.WMVHIGH_PRO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.WMV),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.WMAPRO}),
                new ContainerRestriction(Container.WM),
                new Main_High_Restriction()
        });
    }

    /**
     * Simple_Low_Restriction
     */
    public class Simple_Low_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoBitRate, videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            long videoBitRate = (Long) value[0];
            int videoWidth = (Integer) value[1];
            int videoHeight = (Integer) value[2];
            float fps = (Float) value[3];
            if (videoWidth == 176 && videoHeight == 144 && isFPS15(fps) && videoBitRate <= 96000) return true;
            return false;
        }
    }

    /**
     * Simple_Medium_Restriction
     */
    public class Simple_Medium_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoBitRate, videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            long videoBitRate = (Long) value[0];
            int videoWidth = (Integer) value[1];
            int videoHeight = (Integer) value[2];
            float fps = (Float) value[3];
            if (videoWidth == 240 && videoHeight == 176 && isFPS30(fps) && videoBitRate <= 384000) return true;
            if (videoWidth == 240 && videoHeight == 176 && isFPS2997(fps) && videoBitRate <= 384000) return true;
            if (videoWidth == 352 && videoHeight == 288 && isFPS15(fps) && videoBitRate <= 384000) return true;
            return false;
        }
    }

    /**
     * Main_Medium_Restriction
     */
    public class Main_Medium_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoBitRate, videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            long videoBitRate = (Long) value[0];
            int videoWidth = (Integer) value[1];
            int videoHeight = (Integer) value[2];
            float fps = (Float) value[3];
            if (videoWidth == 720 && videoHeight == 480 && isFPS30(fps) && videoBitRate <= 10000000) return true;
            if (videoWidth == 720 && videoHeight == 480 && isFPS2997(fps) && videoBitRate <= 10000000) return true;
            if (videoWidth == 720 && videoHeight == 576 && isFPS25(fps) && videoBitRate <= 10000000) return true;
            return false;
        }
    }

    /**
     * Main_High_Restriction
     */
    public class Main_High_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoBitRate, videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            long videoBitRate = (Long) value[0];
            int videoWidth = (Integer) value[1];
            int videoHeight = (Integer) value[2];
            float fps = (Float) value[3];
            if (videoWidth == 1920 && videoHeight == 1080 && isFPS30(fps) && videoBitRate <= 20000000) return true;
            if (videoWidth == 1920 && videoHeight == 1080 && isFPS2997(fps) && videoBitRate <= 20000000) return true;
            return false;
        }
    }
}
