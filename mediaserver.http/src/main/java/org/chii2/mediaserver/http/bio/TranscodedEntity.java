package org.chii2.mediaserver.http.bio;

import org.apache.http.entity.AbstractHttpEntity;
import org.chii2.transcoder.api.core.TranscoderProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Transcoded Entity
 */
public class TranscodedEntity extends AbstractHttpEntity {
    // Buffer Size
    private final static int BUFFER_SIZE = 4096;
    // Process
    private List<TranscoderProcess> processes;
    // Index
    private int index;
    // Logger
    private static Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.http");

    /**
     * Constructor
     * @param processes Transcoded Process
     * @param contentType Content Type
     */
    public TranscodedEntity(List<TranscoderProcess> processes, String contentType) {
        super();
        this.processes = processes;
        setContentType(contentType);
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public long getContentLength() {
        return 17554021;
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        if (index >= processes.size()) {
            return null;
        }
        if (!processes.get(index).isStarted()) {
            processes.get(index).init();
        }
        if (processes.get(index).isStarted() && !processes.get(index).isStopped()) {
            return processes.get(index).getOutputFileStream();
        } else {
            return null;
        }
    }

    @Override
    public void writeTo(OutputStream outStream) throws IOException {
        if (outStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }

        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream inputStream = null;
        try {
            if (!processes.get(index).isStarted()) {
                processes.get(index).init();
            }
            if (processes.get(index).isStarted() && !processes.get(index).isStopped()) {
                inputStream = processes.get(index).getOutputFileStream();
                {
                    int l;
                    while (true) {
                        l = inputStream.read(buffer);
                        if (l == -1) {
                            index++;
                            if (index < processes.size()) {
                                inputStream.close();
                                if (!processes.get(index).isStarted()) {
                                    processes.get(index).init();
                                }
                                if (processes.get(index).isStarted() && !processes.get(index).isStopped()) {
                                    inputStream = processes.get(index).getOutputFileStream();
                                    continue;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        outStream.write(buffer, 0, l);
                    }
                }
                outStream.flush();
            }
        } catch (Exception e) {
            logger.warn("Transcoded Entity error: {}.", e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            for (TranscoderProcess process : processes) {
                process.destroy();
            }
        }
    }

    @Override
    public boolean isStreaming() {
        return true;
    }
}
