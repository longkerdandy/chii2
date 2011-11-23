package org.chii2.transcoder.api.core;

import java.io.File;
import java.util.List;

/**
 * Video Transcoder Profile Set
 */
public interface VideoTranscoderProfileSet {
    /**
     * Video Type
     */
    public enum VideoType {
        MOVIE,
        OTHER
    }

    /**
     * Transcoder
     */
    public enum Transcoder {
        MENCODER,
        OTHER
    }

    /**
     * Get Profile Set Video ID
     *
     * @return Video ID
     */
    public String getVideoID();

    /**
     * Get Profile Set Video Type
     *
     * @return Video Type
     */
    public VideoType getVideoType();

    /**
     * Get Profile Set Original Video Files
     *
     * @return Original Video Files
     */
    public List<File> getVideoFiles();

    /**
     * Get Video Transcoder Profiles
     *
     * @return Video Transcoder Profiles
     */
    public List<VideoTranscoderProfile> getProfiles();

    /**
     * Get Profile Set Transcoded Files
     *
     * @return Transcoded Files
     */
    public List<File> getTranscoderFiles();

    /**
     * Get Profile Set Description
     *
     * @return Profile Set Description
     */
    public String getDescription();
}
