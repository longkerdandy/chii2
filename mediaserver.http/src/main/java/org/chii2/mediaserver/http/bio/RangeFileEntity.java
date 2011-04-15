package org.chii2.mediaserver.http.bio;

import org.apache.http.entity.FileEntity;

import java.io.*;

/**
 * FileEntity with Http Range support
 */
public class RangeFileEntity extends FileEntity {
    private final static int BUFFER_SIZE = 4096;
    private long rangeBegin;
    private long rangeEnd;

    public RangeFileEntity(File file, String contentType, long rangeBegin, long rangeEnd) {
        super(file, contentType);
        this.rangeBegin = rangeBegin;
        this.rangeEnd = rangeEnd;
        if (rangeBegin > 0 && rangeEnd < rangeBegin) {
            this.rangeEnd = file.length() - 1;
        }
    }

    public String getContentRange() {
        if (rangeBegin > 0 && rangeEnd < rangeBegin) {
            return String.format("bytes %s-%s/%s", rangeBegin, rangeEnd, rangeEnd - rangeBegin + 1);
        } else {
            return null;
        }
    }

    @Override
    public long getContentLength() {
        return rangeEnd - rangeBegin + 1;
    }

    @Override
    public InputStream getContent() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override
    public void writeTo(final OutputStream outStream) throws IOException {
        if (outStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream inputStream = new FileInputStream(this.file);
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            if (rangeBegin > 0) {
                rangeBegin = inputStream.skip(rangeBegin);
                if (rangeEnd < rangeBegin) {
                    rangeEnd = file.length() - 1;
                }
                long remaining = rangeEnd - rangeBegin + 1;
                int l;
                while (remaining > 0) {
                    l = inputStream.read(buffer, 0, (int) Math.min(BUFFER_SIZE, remaining));
                    if (l == -1) {
                        break;
                    }
                    outStream.write(buffer, 0, l);
                    remaining -= l;
                }
            } else {
                int l;
                while ((l = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, l);
                }
            }
            outStream.flush();
        } finally {
            inputStream.close();
        }
    }
}
