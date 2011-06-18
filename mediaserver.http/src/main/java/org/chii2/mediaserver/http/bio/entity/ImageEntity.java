package org.chii2.mediaserver.http.bio.entity;

import org.apache.http.entity.AbstractHttpEntity;
import org.chii2.transcoder.api.core.ImageTranscoderProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Image HTTP Entity
 */
public class ImageEntity extends AbstractHttpEntity implements Cloneable {
    // Buffer Size
    private final static int BUFFER_SIZE = 4096;
    // Image File
    private File imageFile;
    // Transcoder Process
    private ImageTranscoderProcess process;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.http");

    /**
     * Constructor
     *
     * @param imageFile   Image File
     * @param contentType Content Type
     */
    public ImageEntity(File imageFile, String contentType) {
        this.imageFile = imageFile;
        // Content Type
        setContentType(contentType);
    }

    /**
     * Constructor
     *
     * @param process     Image Transcoder Process
     * @param contentType Content Type
     */
    public ImageEntity(ImageTranscoderProcess process, String contentType) {
        this.process = process;
        // Content Type
        setContentType(contentType);
    }

    @Override
    public boolean isRepeatable() {
        return this.process == null;
    }

    @Override
    public long getContentLength() {
        logger.info("Request for content length");
        if (this.imageFile != null && this.imageFile.exists() && this.imageFile.isFile()) {
            return this.imageFile.length();
        } else {
            return -1;
        }
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        if (this.imageFile != null && this.imageFile.exists() && this.imageFile.isFile()) {
            return new FileInputStream(this.imageFile);
        } else {
            return null;
        }
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }

        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream inputStream = null;
        try {
            // Transcoding
            if (this.process != null) {
                this.process.init();
                this.imageFile = this.process.getOutputFile();
            }

            if (this.imageFile != null && this.imageFile.exists() && this.imageFile.isFile()) {
                inputStream = new FileInputStream(this.imageFile);
                int line;
                while ((line = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, line);
                }
                outputStream.flush();
            }
        } finally {
            if (this.process != null) {
                this.process.destroy();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public boolean isStreaming() {
        return false;
    }
}
