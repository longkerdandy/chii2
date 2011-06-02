package org.chii2.transcoder.core.avidemux;

import org.chii2.transcoder.api.core.TranscoderProcess;
import org.chii2.transcoder.core.io.TranscodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * AviDemux Transcoder Process
 */
public class AViDemuxProcess implements TranscoderProcess {
    // Stopped
    public volatile boolean stopped = false;
    // Finished
    public volatile boolean finished = false;
    // Started
    public volatile boolean started = false;
    // AviDemux Script
    private ScriptTemplate script;
    // Real Thread Process
    private Process process;
    // Transcoded InputStream
    private TranscodedInputStream stream;

    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.avidemux");

    public AViDemuxProcess(ScriptTemplate script) {
        this.script = script;
    }

    @Override
    public void start() {
        try {
            if (script.generate()) {
                // Parameters
                List<String> commands = new ArrayList<String>();
                commands.add("avidemux2_cli");
                commands.add("--force-alt-h264");
                commands.add("--run");
                commands.add(script.getScriptFile().getAbsolutePath());
                // Process Start
                process = new ProcessBuilder(commands).redirectErrorStream(true).start();
                // Read text output
                AviDemuxOutputTextReader textReader = new AviDemuxOutputTextReader(this);
                Thread thread = new Thread(textReader);
                thread.setDaemon(false);
                thread.start();
            }
        } catch (IOException e) {
            this.stopped = true;
            logger.error("AviDemux Process with IO Exception: {}", e.getMessage());
        } finally {
            this.started = true;
        }
    }

    public Process getProcess() {
        return process;
    }

    @Override
    public void stop() {
        // To stop reader
        this.stopped = true;
        // Stop process
        if (this.process != null) {
            process.destroy();
        }
        // Delete script file
        File scriptFile = script.getScriptFile();
        if (scriptFile.exists()) {
            try {
                boolean deleted = scriptFile.delete();
                if (deleted) {
                    logger.info("Temp Script file {} deleted.", scriptFile.getAbsolutePath());
                } else {
                    logger.info("Temp Script file {} can not be deleted.", scriptFile.getAbsolutePath());
                }
            } catch (SecurityException e) {
                logger.error("AviDemux Process try to delete temp script file {} with Security Exception: {}", scriptFile.getAbsolutePath(), e.getMessage());
            }
        }
        // Delete video file
        File outputFile = getOutputFile();
        if (outputFile.exists()) {
            try {
                boolean deleted = outputFile.delete();
                if (deleted) {
                    logger.info("Temp Video file {} deleted.", outputFile.getAbsolutePath());
                } else {
                    logger.info("Temp Video file {} can not be deleted.", outputFile.getAbsolutePath());
                }
            } catch (SecurityException e) {
                logger.error("AviDemux Process try to delete temp video file {} with Security Exception: {}", outputFile.getAbsolutePath(), e.getMessage());
            }
        }
    }

    @Override
    public File getOutputFile() {
        return script.getOutputFile();
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
