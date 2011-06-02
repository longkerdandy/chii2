package org.chii2.transcoder.core.dlna.catalog;

import org.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.chii2.transcoder.core.dlna.codec.VideoCodec;
import org.chii2.transcoder.core.dlna.restriction.VideoRestriction;
import org.teleal.cling.support.model.dlna.DLNAProfiles;

/**
 * MPEG1
 */
public class MPEG1 extends VideoCatalog {
    public MPEG1() {
        profileMap.put(DLNAProfiles.MPEG1, new VideoRestriction[]{
                new MPEG1AudioRestriction(),
                new MPEG1VideoRestriction(),
                new VideoBitRateRangeRestriction(1150000, 1152000),
                new VideoCodecRestriction(VideoCodec.MPEG1)
        });
    }

    /**
     * MPEG1 Audio
     */
    public class MPEG1AudioRestriction extends VideoRestriction<Object[]> {

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
            if (!AudioCodec.match(audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, AudioCodec.MP2)) return false;
            if (audioSampleBitRate != 44100) return false;
            if (audioBitRate != 224000) return false;
            if (audioChannels != 2) return false;
            return true;
        }
    }

    /**
     * MPEG1 Video Restriction
     */
    public class MPEG1VideoRestriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 352 && videoHeight == 288 && isFPS25(fps)) return true;
            if (videoWidth == 352 && videoHeight == 240 && isFPS2997(fps)) return true;
            if (videoWidth == 352 && videoHeight == 240 && isFPS23976(fps)) return true;
            return false;
        }
    }
}
