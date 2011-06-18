package org.chii2.transcoder.core.im4java;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.transcoder.api.core.ImageTranscoderProcess;
import org.im4java.core.ConvertCmd;
import org.im4java.core.GMOperation;
import org.im4java.core.IM4JavaException;
import org.im4java.core.Stream2BufferedImage;
import org.im4java.process.Pipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

/**
 * Image Transcoder Process base on im4java
 */
public class IM4JImageTranscoderProcess implements ImageTranscoderProcess {
    // Input File
    private File inputFile;
    // Buffered Image
    private File outputFile;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.im4java");

    /**
     * Constructor
     *
     * @param inputFile  Input File
     * @param outputFile Output File
     */
    public IM4JImageTranscoderProcess(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    @Override
    public void init() {
        try {
            // Use GM
            GMOperation op = new GMOperation();
            // Pipe
            op.addImage(this.inputFile.getAbsolutePath());
            op.addImage(this.outputFile.getAbsolutePath());
            // GM command
            ConvertCmd convert = new ConvertCmd(true);
            // Run
            convert.run(op);
        } catch (InterruptedException e) {
            logger.error("Image convert with error: {}.", ExceptionUtils.getMessage(e));
        } catch (IM4JavaException e) {
            logger.error("Image convert with error: {}.", ExceptionUtils.getMessage(e));
        } catch (IOException e) {
            logger.error("Image convert with error: {}.", ExceptionUtils.getMessage(e));
        }
    }

    @Override
    public void destroy() {
        if (this.outputFile  != null && this.outputFile.exists() && this.outputFile.isFile()) {
            try {
                boolean deleted = this.outputFile .delete();
                if (deleted) {
                    logger.info("Temp file {} has been deleted.", this.outputFile .getAbsolutePath());
                } else {
                    logger.warn("Temp file {} can not be deleted.", this.outputFile .getAbsolutePath());
                }
            } catch (SecurityException e) {
                logger.error("Try to delete temp file {} with Security Exception: {}", this.outputFile .getAbsolutePath(), ExceptionUtils.getMessage(e));
            }
        }
    }

    @Override
    public File getOutputFile() {
        return this.outputFile;
    }
}
