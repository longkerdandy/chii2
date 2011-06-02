package org.chii2.transcoder.api.core;

import java.io.File;
import java.io.InputStream;

/**
 * Transcoder Process
 */
public interface TranscoderProcess {
    /**
     * Start Transcoding Process
     *
     */
    public void start();

    /**
     * Stop Transcoding Process
     */
    public void stop();

    /**
     * Get Output File
     *
     * @return Output File
     */
    public File getOutputFile();

    /**
     * Get Output File
     *
     * @return Output File
     */
    public InputStream getOutputFileStream();

    /**
     * Transcoded Process Started or Not
     *
     * @return True if Started
     */
    public boolean isStarted();

    /**
     * Transcoded Process Finished or Not
     *
     * @return True if Finished
     */
    public boolean isFinished();

    /**
     * Transcoded Process Stopped or Not
     *
     * @return True if Stopped
     */
    public boolean isStopped();
}
