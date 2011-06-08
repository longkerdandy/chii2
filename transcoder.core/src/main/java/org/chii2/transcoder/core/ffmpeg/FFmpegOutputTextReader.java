package org.chii2.transcoder.core.ffmpeg;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * FFmpeg Output Text Reader
 */
public class FFmpegOutputTextReader implements Runnable {
    // Buffer Reader
    private BufferedReader reader;
    // FFmpeg Process
    private FFmpegProcess process;
    // Logger
    protected Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.ffmpeg");

    /**
     * Constructor
     *
     * @param reader  Reader
     * @param process Process
     */
    public FFmpegOutputTextReader(BufferedReader reader, FFmpegProcess process) {
        this.reader = reader;
        this.process = process;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                // If process stopped, stop this
                if (!this.process.stopped) {
                    return;
                }
                // Output Information
                if (line.startsWith("frame=")) {
                    processOutput(line);
                }
            }
            // Mark as finish
            this.process.finished = true;
        } catch (IOException e) {
            logger.debug("FFmpeg output text reading with error: {}", e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException ignore) {
            }
        }
    }

    /**
     * Process FFmpeg output and set back to Process
     *
     * @param line Output Line
     */
    public void processOutput(String line) {
        int sizeIndex = line.indexOf("size=");
        int timeIndex = line.indexOf("time=");
        int bitrateIndex = line.indexOf("bitrate=");
        try {
            String size = StringUtils.trim(line.substring(sizeIndex + 5, timeIndex));
            String time = StringUtils.trim(line.substring(timeIndex + 5, bitrateIndex));
            String bitrate = StringUtils.trim(line.substring(bitrateIndex + 8));
            if (size.endsWith("kB")) {
                size = size.substring(0, size.length() - 2);
            }
            if (bitrate.endsWith("kbits/s")) {
                bitrate = bitrate.substring(0, bitrate.length() - 7);
            }
            process.size = NumberUtils.toLong(size);
            process.duration = NumberUtils.toFloat(time);
            process.bitrate = NumberUtils.toLong(bitrate);
        } catch (Exception e) {
            logger.error("FFmpeg output text parsing with error: {}", e.getMessage());
        }
    }
}
