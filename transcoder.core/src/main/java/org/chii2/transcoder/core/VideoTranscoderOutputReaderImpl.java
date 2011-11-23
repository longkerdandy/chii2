package org.chii2.transcoder.core;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.transcoder.api.core.BackgroundTranscoderService;
import org.chii2.transcoder.api.core.VideoTranscoderOutputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Background Video Transcoder Output Reader Base Implementation
 */
public abstract class VideoTranscoderOutputReaderImpl implements VideoTranscoderOutputReader {
    // Transcoder Process
    private Process process;
    // Background Transcoder Service
    protected BackgroundTranscoderService service;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.core");

    /**
     * Constructor
     *
     * @param process Transcoder Process
     * @param service Background Transcoder Service
     */
    public VideoTranscoderOutputReaderImpl(Process process, BackgroundTranscoderService service) {
        this.process = process;
        this.service = service;
    }

    @Override
    public void run() {
        if (this.process == null) {
            return;
        }
        // Reader
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
        String line;
        try {
            // Loop while not stopped
            while ((line = reader.readLine()) != null) {
                // Parse Output
                this.parseOutput(line);
            }
        } catch (IOException e) {
            logger.warn("Parse transcoder output text with error: {}", ExceptionUtils.getMessage(e));
        } finally {
            // Call Back
            if (this.service != null) {
                this.service.setCurrentTaskStatus(0);
            }
            // Close Buffer Reader
            try {
                reader.close();
            } catch (IOException ignore) {
            }
        }
    }

    /**
     * Parse Command Line Output
     *
     * @param line Command Line Output (per line)
     */
    public abstract void parseOutput(String line);
}
