package org.chii2.mediaserver.http.bio;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.entity.AbstractHttpEntity;

import java.io.*;
import java.util.List;

/**
 * FileEntity with Http Range support
 */
public class RangeFileEntity extends AbstractHttpEntity implements Cloneable {
    // Buffer Size
    private final static int BUFFER_SIZE = 4096;
    // HTTP Range Begin
    private long rangeBegin;
    // HTTP Range End
    private long rangeEnd;
    // Total Size
    private long totalSize = 0;
    // File Index
    private int fileIndex = 0;
    // Files
    private List<File> files;

    /**
     * Constructor
     *
     * @param files       Files
     * @param contentType Content Type
     * @param range       HTTP Range Header
     */
    public RangeFileEntity(List<File> files, String contentType, Header range) {
        super();
        this.files = files;
        this.rangeBegin = getRangeBegin(range);
        this.rangeEnd = getRangeEnd(range);
        for (File file : files) {
            this.totalSize = this.totalSize + file.length();
        }
        if (this.rangeBegin >= 0 && this.rangeEnd < this.rangeBegin) {
            this.rangeEnd = this.totalSize - 1;
        }
        this.fileIndex = getFileIndex();
        setContentType(contentType);
    }

    /**
     * HTTP Content Range Header Response
     *
     * @return Content Range Header Value
     */
    public String getContentRange() {
        if (rangeBegin >= 0 && rangeEnd >= rangeBegin) {
            return String.format("bytes %s-%s/%s", rangeBegin, rangeEnd, rangeEnd - rangeBegin + 1);
        } else {
            return null;
        }
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public long getContentLength() {
        if (rangeBegin >= 0 && rangeEnd >= rangeBegin) {
            return rangeEnd - rangeBegin + 1;
        } else {
            return totalSize;
        }
    }

    @Override
    public InputStream getContent() throws IOException {
        if (fileIndex < files.size()) {
            return new FileInputStream(files.get(fileIndex));
        } else {
            return null;
        }
    }

    @SuppressWarnings({"ConstantConditions"})
    @Override
    public void writeTo(final OutputStream outStream) throws IOException {
        if (outStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }

        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(files.get(fileIndex));
            if (rangeBegin >= 0 && rangeEnd >= rangeBegin) {
                long offset = rangeBegin;
                for (int i = 0; i < fileIndex; i++) {
                    offset = offset - files.get(i).length();
                }
                long realOffset = inputStream.skip(offset);
                rangeBegin = rangeBegin - (offset - realOffset);
                long remaining = rangeEnd - rangeBegin + 1;
                int l;
                while (remaining > 0) {
                    l = inputStream.read(buffer, 0, (int) Math.min(BUFFER_SIZE, remaining));
                    if (l == -1) {
                        fileIndex++;
                        if (fileIndex < files.size()) {
                            inputStream.close();
                            inputStream = new FileInputStream(files.get(fileIndex));
                            continue;
                        } else {
                            break;
                        }
                    }
                    outStream.write(buffer, 0, l);
                    remaining -= l;
                }
            } else {
                int l;
                while (true) {
                    l = inputStream.read(buffer);
                    if (l == -1) {
                        fileIndex++;
                        if (fileIndex < files.size()) {
                            inputStream.close();
                            inputStream = new FileInputStream(files.get(fileIndex));
                            continue;
                        } else {
                            break;
                        }
                    }
                    outStream.write(buffer, 0, l);
                }
            }
            outStream.flush();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        // File instance is considered immutable
        // No need to make a copy of it
        return super.clone();
    }

    private int getFileIndex() {
        if (rangeBegin >= 0 && files != null && !files.isEmpty()) {
            int index;
            long size;
            for (index = 0; index < files.size(); index++) {
                size = files.get(index).length();
                if (rangeBegin < size) {
                    return index;
                }
            }
        }
        return 0;
    }

    private long getRangeBegin(Header range) {
        if (range != null) {
            HeaderElement[] elements = range.getElements();
            if (elements != null && elements.length > 0) {
                String rangeValue = elements[0].getValue();
                int index = rangeValue.indexOf("-");
                if (index > 0) {
                    return NumberUtils.toLong(StringUtils.trim(rangeValue.substring(0, index)), -1);
                }
            }
        }
        return -1;
    }

    private long getRangeEnd(Header range) {
        if (range != null) {
            HeaderElement[] elements = range.getElements();
            if (elements != null && elements.length > 0) {
                String rangeValue = elements[0].getValue();
                int index = rangeValue.indexOf("-");
                if (index > 0 && rangeValue.length() > index + 1) {
                    return NumberUtils.toLong(StringUtils.trim(rangeValue.substring(index + 1)), -1);
                }
            }
        }
        return -1;
    }
}
