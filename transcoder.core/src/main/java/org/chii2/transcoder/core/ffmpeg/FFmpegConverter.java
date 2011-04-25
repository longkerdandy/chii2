package org.chii2.transcoder.core.ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * FFmpeg Converter
 */
public class FFmpegConverter implements Callable<Process> {
    // FFmpeg parameter
    private FFmpegConverterParameter parameter;
    // Logger
    protected Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.ffmpeg");

    /**
     * Constructor
     * @param parameter FFmpeg Parameter
     */
    public FFmpegConverter(FFmpegConverterParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public Process call() throws Exception {
        // Parameters
        List<String> commands = parameter.getParameters();
        // Start FFmpeg
        logger.info("Starting FFmpeg process with parameters: {}", commands);
        Process process = new ProcessBuilder(commands).start();
        // Return process
        return process;
    }
}
