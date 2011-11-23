package org.chii2.transcoder.api.core;

import java.util.List;

/**
 * Video Transcoder Process's Profile
 */
public interface VideoTranscoderProfile {
    /**
     * Get Profile Transcoder
     *
     * @return Transcoder
     */
    public VideoTranscoderProfileSet.Transcoder getTranscoder();

    /**
     * Get Process Output Reader
     * This reader will read standard stream (NOT error stream)
     * Please Notice: Each time calling this method will return a new reader instance
     *
     * @param process Process which will invoke Video Transcoder
     * @param service Background Transcoder Service
     * @return Process Output Reader
     */
    public VideoTranscoderOutputReader getOutputReader(Process process, BackgroundTranscoderService service);

    /**
     * Get Process Commands
     *
     * @return Process Commands
     */
    public List<String> getCommands();
}
