package org.chii2.transcoder.core.dlna.restriction;

import org.teleal.cling.support.model.dlna.DLNAProfiles;

/**
 * Video Restriction
 */
public abstract class VideoRestriction<T> {
    /**
     * Value compares to Restrictions
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
     * @param audioProfile       Audio DLNA Profile
     * @return Value
     */
    public abstract T value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps,
                            String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile);

    /**
     * VideoRestriction is passed or not
     *
     * @param value Value to be compared
     * @return True if restriction is passed
     */
    public abstract boolean pass(T value);
}
