package org.chii2.transcoder.core.dlna.catalog;

import org.apache.commons.lang.ArrayUtils;
import org.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.chii2.transcoder.core.dlna.codec.Container;
import org.chii2.transcoder.core.dlna.codec.VideoCodec;
import org.chii2.transcoder.core.dlna.restriction.VideoRestriction;
import org.teleal.cling.support.model.dlna.DLNAProfiles;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Video Catalog
 */
public class VideoCatalog {
    // Profile - Restrictions Map
    protected LinkedHashMap<DLNAProfiles, VideoRestriction[]> profileMap = new LinkedHashMap<DLNAProfiles, VideoRestriction[]>();

    /**
     * Resolve DLNA Profile based on conditions
     *
     * @param container          Container
     * @param videoFormat        Video Format
     * @param videoFormatProfile Video Format Profile
     * @param videoFormatVersion Video Format Version
     * @param videoCodec         Video Codec
     * @param videoBitRate       BitRate
     * @param videoWidth         Video Width
     * @param videoHeight        Video Height
     * @param fps                Video FPS
     * @param audioFormat        Audio Format
     * @param audioFormatProfile Audio Format Profile
     * @param audioFormatVersion Audio Format Version
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Audio SampleBitRate
     * @param audioChannels      Audio Channels
     * @param audioProfile  Audio DLNA Profile
     * @return DLNA Profile, Null if not match
     */
    public DLNAProfiles resolve(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps,
                                String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
        loop_profile:
        for (Map.Entry<DLNAProfiles, VideoRestriction[]> entry : profileMap.entrySet()) {
            for (VideoRestriction videoRestriction : entry.getValue()) {
                //noinspection unchecked
                if (!videoRestriction.pass(videoRestriction.value(container, videoFormat, videoFormatProfile, videoFormatVersion, videoCodec, videoBitRate, videoWidth, videoHeight, fps,
                        audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, audioBitRate, audioSampleBitRate, audioChannels, audioProfile)))
                    continue loop_profile;
            }
            return entry.getKey();
        }
        return null;
    }

    // FPS 30
    public boolean isFPS30(float fps) {
        return fps == 30;
    }

    // FPS 30/1001
    public boolean isFPS2997(float fps) {
        return fps > 29.96 && fps <29.98;
    }

    // FPS  25
    public boolean isFPS25(float fps) {
        return fps == 25;
    }

    // FPS 24
    public boolean isFPS24(float fps) {
        return fps == 24;
    }

    // FPS 24/1001
    public boolean isFPS23976(float fps) {
        return fps > 23.975 && fps <23.977;
    }

    // FPS  15
    public boolean isFPS15(float fps) {
        return fps == 15;
    }

    /**
     * Container AudioRestriction
     */
    public class ContainerRestriction extends VideoRestriction<String> {
        // Target Container
        private Container targetContainer;

        public ContainerRestriction(Container targetContainer) {
            this.targetContainer = targetContainer;
        }


        @Override
        public String value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return container;
        }

        @Override
        public boolean pass(String value) {
            return Container.match(value, targetContainer);
        }
    }

    /**
     * Video Codec Restriction
     */
    public class VideoCodecRestriction extends VideoRestriction<Object[]> {
        // Target Codec
        private VideoCodec targetCodec;

        public VideoCodecRestriction(VideoCodec targetCodec) {
            this.targetCodec = targetCodec;
        }

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoFormat, videoFormatProfile, videoFormatVersion, videoCodec};
        }

        @Override
        public boolean pass(Object[] value) {
            String videoFormat = (String) value[0];
            String videoFormatProfile = (String) value[1];
            int videoFormatVersion = (Integer) value[2];
            String videoCodec = (String) value[3];
            return VideoCodec.match(videoFormat, videoFormatProfile, videoFormatVersion, videoCodec, targetCodec);
        }
    }

    /**
     * Audio Profile Restriction
     */
    public class AudioProfileRestriction extends VideoRestriction<DLNAProfiles> {
        // Acceptable Audio Profiles
        DLNAProfiles[] targetProfiles;

        public AudioProfileRestriction(DLNAProfiles[] targetProfiles) {
            this.targetProfiles = targetProfiles;
        }

        @Override
        public DLNAProfiles value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return audioProfile;
        }

        @Override
        public boolean pass(DLNAProfiles value) {
            return ArrayUtils.contains(targetProfiles, value);
        }
    }

    /**
     * Video BitRate Restriction
     */
    public class VideoBitRateRangeRestriction extends VideoRestriction<Long> {
        // Min
        protected long minValue;
        // Max
        protected long maxValue;

        public VideoBitRateRangeRestriction(long minValue, long maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        @Override
        public Long value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return videoBitRate;
        }

        @Override
        public boolean pass(Long value) {
            return value >= minValue & value <= maxValue;
        }
    }

    /**
     * Total BitRate Restriction
     */
    public class TotalBitRateRangeRestriction extends VideoRestriction<Long> {
        // Min
        protected long minValue;
        // Max
        protected long maxValue;

        public TotalBitRateRangeRestriction(long minValue, long maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        @Override
        public Long value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return videoBitRate + audioBitRate;
        }

        @Override
        public boolean pass(Long value) {
            return value >= minValue & value <= maxValue;
        }
    }

    /**
     * FPS Restriction
     */
    public class FPSRestriction extends VideoRestriction<Float> {
        // Target FPS
        protected float targetFPS;

        public FPSRestriction(float targetFPS) {
            this.targetFPS = targetFPS;
        }

        @Override
        public Float value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return fps;
        }

        @Override
        public boolean pass(Float value) {
            return value == targetFPS;
        }
    }

    /**
     * MP2
     */
    public class MP2Restriction extends VideoRestriction<Object[]> {

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
            if (!(audioSampleBitRate == 32000 || audioSampleBitRate == 44100 || audioSampleBitRate == 48000)) return false;
            if (audioBitRate < 32000 || audioBitRate > 448000) return false;
            if (audioChannels > 6) return false;
            return true;
        }
    }

    /**
     * AC3
     */
    public class AC3Restriction extends VideoRestriction<Object[]> {

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
            if (!AudioCodec.match(audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, AudioCodec.AC3)) return false;
            if (!(audioSampleBitRate == 32000 || audioSampleBitRate == 44100 || audioSampleBitRate == 48000)) return false;
            if (audioBitRate < 32000 || audioBitRate > 448000) return false;
            if (audioChannels > 6) return false;
            return true;
        }
    }

    /**
     * AC3 Extend
     */
    public class AC3XRestriction extends VideoRestriction<Object[]> {

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
            if (!AudioCodec.match(audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, AudioCodec.AC3)) return false;
            if (!(audioSampleBitRate == 32000 || audioSampleBitRate == 44100 || audioSampleBitRate == 48000)) return false;
            if (audioBitRate < 32000 || audioBitRate > 640000) return false;
            if (audioChannels > 6) return false;
            return true;
        }
    }

    /**
     * G726
     */
    public class G726Restriction extends VideoRestriction<Object[]> {

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
            if (!AudioCodec.match(audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, AudioCodec.AC3)) return false;
            if (audioSampleBitRate != 8000) return false;
            if (audioBitRate != 32000) return false;
            if (audioChannels != 1) return false;
            return true;
        }
    }
}
