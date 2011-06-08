package org.chii2.transcoder.core.io;

import org.chii2.transcoder.api.core.TranscoderProcess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Transcoded File Input Stream
 */
public class TranscodedInputStream extends FileInputStream {
    // Transcoded Process
    TranscoderProcess process;

    /**
     * Constructor
     *
     * @param file    File
     * @param process Transcoder Process
     * @throws FileNotFoundException Super Exception
     */
    public TranscodedInputStream(File file, TranscoderProcess process) throws FileNotFoundException {
        super(file);
        this.process = process;
    }

    public int available() throws IOException {
        int av = super.available();
        if ((av < 0) && !this.process.isFinished() && !this.process.isStopped()) {
            return 1;
        }
        return av;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int n = -1;
        while (n == -1) {
            n = super.read(b, off, len);
            if (n < 0 && !this.process.isFinished() && !this.process.isStopped()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }
                continue;
            }
            if (n < 0 && (this.process.isFinished() || this.process.isStopped())) {
                return n;
            }
        }
        return n;
    }
}
