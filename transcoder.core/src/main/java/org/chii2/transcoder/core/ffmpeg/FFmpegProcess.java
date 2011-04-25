package org.chii2.transcoder.core.ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.*;

/**
 * FFmpeg Process Wrapper
 */
public class FFmpegProcess {
    // FFmpeg parameter
    private FFmpegConverterParameter parameter;
    // Real Thread Process
    private Process process;
    // Start FFmpeg daemon successful or not
    private boolean successful = false;
    // Logger
    protected Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.ffmpeg");

    /**
     * Constructor
     * @param inputFile Input File
     * @param videoCodec Video Codec
     * @param videoBitrate Video Bit rate
     * @param audioCodec Audio Codec
     * @param audioBitrate Audio Bit rate
     */
    public FFmpegProcess(File inputFile, String videoCodec, long videoBitrate, String audioCodec, long audioBitrate) {
        this.parameter = new FFmpegConverterParameter(inputFile, videoCodec, videoBitrate, audioCodec, audioBitrate);
    }

    /**
     * Constructor
     * @param inputFile Input File
     * @param outputFile Output File
     * @param videoCodec Video Codec
     * @param videoBitrate Video Bit rate
     * @param audioCodec Audio Codec
     * @param audioBitrate Audio Bit rate
     */
    public FFmpegProcess(File inputFile, File outputFile, String videoCodec, long videoBitrate, String audioCodec, long audioBitrate) {
        this.parameter = new FFmpegConverterParameter(inputFile, outputFile, videoCodec, videoBitrate, audioCodec, audioBitrate);
    }

    /**
     * Start FFmpeg Process
     */
    public boolean start() {
        // Converter
        Callable<Process> converter = new FFmpegConverter(this.parameter);
        // Executor
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Future
        Future<Process> future = executor.submit(converter);
        // Reader
        BufferedReader reader = null;
        try {
            // Get process
            this.process = future.get();
            // Read text output
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if ("Press [q] to stop encoding".equalsIgnoreCase(line)) {
                    this.successful = true;
                    break;
                }
            }
        } catch (InterruptedException e) {
            logger.error("FFmpeg Process with Interrupted Exception: {}", e.getMessage());
            this.successful = false;
        } catch (ExecutionException e) {
            logger.error("FFmpeg Process with Execution Exception: {}", e.getMessage());
            this.successful = false;
        } catch (IOException e) {
            logger.error("FFmpeg Process with IO Exception: {}", e.getMessage());
            this.successful = false;
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException ignore) {
            }
        }
        return successful;
    }

    /**
     * Stop FFmpeg Process
     */
    public void stop() {
        if (this.process != null) {
            char quit = 'q';
            try {
                logger.info("Try to stop FFmpeg Process.");
                process.getOutputStream().write(quit);
                process.destroy();
                logger.info("FFmpeg Process with exit value: {}.", process.exitValue());
            } catch (IOException e) {
                logger.error("FFmpeg Process with IO Exception: {}", e.getMessage());
            }
        }
    }

    public File getOutputFile() {
        return this.parameter.getOutputFile();
    }

    public FFmpegConverterParameter getParameter() {
        return parameter;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
