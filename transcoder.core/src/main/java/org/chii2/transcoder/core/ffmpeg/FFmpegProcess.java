package org.chii2.transcoder.core.ffmpeg;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.chii2.transcoder.api.core.TranscoderProcess;
import org.chii2.transcoder.core.cache.TranscodedCache;
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

    // Request ID
    private String requestId;
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
     * @param requestId  Request ID
     * @param parameter FFmpeg Parameter
     */
    public FFmpegProcess(String requestId, FFmpegConverterParameter parameter) {
        this.requestId = requestId;
        this.parameter = parameter;
    }

    @Override
    public void init() {
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

            // During Pipe, This will not display
            //while ((line = reader.readLine()) != null) {
            //    if ("Press [q] to destroy encoding".equalsIgnoreCase(line)) {
            //        break;
            //    }
            //}

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
            this.destroy();
        } finally {
            this.started = true;
        }
    }

    @Override
    public void destroy() {
        // Stopped
        this.stopped = true;
        // Stop ffmpeg
        if (this.process != null) {
            logger.info("Try to stop FFmpeg Process.");
            process.destroy();
        }
        // Close Stream
        if (this.stream != null) {
            try {
                this.stream.close();
            } catch (IOException ignore) {
            } finally {
                this.stream = null;
            }
        }
    }

    @Override
    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public void cache() {
        TranscodedCache.getInstance().cache(this);
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    /**
     * Parse ffmpeg output to make sure output file size not zero
     *
     * @param line Output Line
     * @return True if file size > 0
     */
    private boolean processOutput(String line) {
        int sizeIndex = line.indexOf("size=");
        int timeIndex = line.indexOf("time=");
        try {
            String size = StringUtils.trim(line.substring(sizeIndex + 5, timeIndex));
            if (size.endsWith("kB")) {
                size = size.substring(0, size.length() - 2);
            }
            this.size = NumberUtils.toLong(size);
            return !(this.size == 0);
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
    public long getCurrentSize() {
        // return size;
        return this.getOutputFile().length();
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
