package org.chii2.transcoder.core.ffmpeg;

import org.apache.commons.lang.StringUtils;
import org.chii2.transcoder.api.core.TranscoderProcess;
import org.chii2.transcoder.core.io.TranscodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * FFmpeg Process Wrapper
 */
public class FFmpegProcess implements TranscoderProcess {
    // Size
    public volatile long size;
    // Duration
    public volatile float duration;
    // Bitrate
    public volatile long bitrate;
    // Stopped
    public volatile boolean stopped = false;
    // Finished
    public volatile boolean finished = false;
    // Successful
    public volatile boolean started = false;

    // FFmpeg parameter
    private FFmpegConverterParameter parameter;
    // Real Thread Process
    private Process process;
    // Stream
    private TranscodedInputStream stream;

    // Logger
    protected Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.ffmpeg");

    /**
     * Constructor
     *
     * @param parameter FFmpeg Parameter
     */
    public FFmpegProcess(FFmpegConverterParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public void start() {
        try {
            // Parameters
            List<String> commands = parameter.getParameters();
            // Start FFmpeg
            logger.info("Starting FFmpeg process with parameters: {}", commands);
            // ffmpeg process
            process = new ProcessBuilder(commands).redirectErrorStream(true).start();
            // Read text output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if ("Press [q] to stop encoding".equalsIgnoreCase(line)) {
                    break;
                }
            }
            // Make sure output file's size > 0
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("frame=")) {
                    if (processOutput(line)) {
                        break;
                    }
                }
            }
            // Output Reader Thread
            FFmpegOutputTextReader outputTextReader = new FFmpegOutputTextReader(reader, this);
            Thread thread = new Thread(outputTextReader);
            thread.setDaemon(false);
            thread.start();
        } catch (IOException e) {
            logger.error("FFmpeg Process with IO Exception: {}", e.getMessage());
            this.stopped = true;
        } finally {
            this.started = true;
        }
    }

    @Override
    public void stop() {
        // To stop reader
        stopped = true;
        // Stop ffmpeg
        if (this.process != null) {
            char quit = 'q';
            try {
                logger.info("Try to stop FFmpeg Process.");
                process.getOutputStream().write(quit);
            } catch (IOException e) {
                logger.error("FFmpeg Process with IO Exception: {}", e.getMessage());
            } finally {
                process.destroy();
            }
        }
        // Delete file
        File outputFile = getOutputFile();
        if (outputFile.exists()) {
            try {
                boolean deleted = outputFile.delete();
                if (deleted) {
                    logger.info("Temp file {} deleted.", outputFile.getAbsolutePath());
                } else {
                    logger.info("Temp file {} can not be deleted.", outputFile.getAbsolutePath());
                }
            } catch (SecurityException e) {
                logger.error("FFmpeg Process try to delete temp file {} with Security Exception: {}", outputFile.getAbsolutePath(), e.getMessage());
            }
        }
    }

    /**
     * Parse ffmpeg output to make sure output file size not zero
     * @param line Output Line
     * @return True if file size > 0
     */
    private boolean processOutput(String line) {
        int sizeIndex = line.indexOf("size=");
        int timeIndex = line.indexOf("time=");
        try {
            String size = line.substring(sizeIndex + 5, timeIndex);
            size = StringUtils.trim(size);
            return !(size.equalsIgnoreCase("0kB") || size.equalsIgnoreCase("0"));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public File getOutputFile() {
        return this.parameter.getOutputFile();
    }

    @Override
    public InputStream getOutputFileStream() {
        if (stream == null) {
            try {
                stream = new TranscodedInputStream(getOutputFile(), this);
            } catch (FileNotFoundException e) {
                logger.error("Create Transcoded InputStream with error: {}", e.getMessage());
            }
        }
        return stream;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }
}
