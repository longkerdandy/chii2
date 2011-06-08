package org.chii2.mediaserver.http.bio;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.entity.AbstractHttpEntity;
import org.chii2.transcoder.api.core.TranscoderProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Range Transcoded Entity
 */
public class RangeTranscodedEntity extends AbstractHttpEntity {
    // Buffer Size
    private final static int BUFFER_SIZE = 4096;
    // Process
    private List<TranscoderProcess> processes;
    // Index
    private int index;
    // HTTP Range Begin
    private long rangeBegin;
    // HTTP Range End
    private long rangeEnd;
    // Finished Size
    private long finishedSize;
    // Logger
    private static Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.http");

    /**
     * Constructor
     * @param processes Transcoded Process
     * @param contentType Content Type
     * @param range Http Range Header
     */
    public RangeTranscodedEntity(List<TranscoderProcess> processes, String contentType, Header range) {
        super();
        this.processes = processes;
        this.setContentType(contentType);
        this.rangeBegin = this.getRangeBegin(range);
        this.rangeEnd = this.getRangeEnd(range);
        this.index = this.getProcessIndex(this.rangeBegin, this.processes);
        this.finishedSize = this.getFinishedSize(this.processes);
    }

    /**
     * HTTP Content Range Header Response
     *
     * @return Content Range Header Value
     */
    public String getContentRange() {
        if (rangeBegin >= 0 && rangeEnd >= rangeBegin) {
            return String.format("bytes %s-%s/%s", rangeBegin, rangeEnd, rangeEnd - rangeBegin + 1);
        } else if (rangeBegin >= 0) {
            return String.format("bytes %s-%s/%s", rangeBegin, 12202218 - 1, 12202218);
        } else {
            return null;
        }
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public long getContentLength() {
        return 12202218;
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        if (index >= processes.size()) {
            return null;
        }
        if (!processes.get(index).isStarted() || (processes.get(index).isStopped() && !processes.get(index).isFinished())) {
            processes.get(index).init();
        }
        if ((processes.get(index).isStarted() && !processes.get(index).isStopped()) || (processes.get(index).isStarted() && processes.get(index).isFinished())) {
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
            logger.info("Start with index {}.", index);
            TranscoderProcess process = processes.get(index);
            if (!process.isStarted() || (process.isStopped() && !process.isFinished())) {
                process.init();
            }
            if ((process.isStarted() && !process.isStopped()) || (process.isStarted() && process.isFinished())) {
                inputStream = process.getOutputFileStream();
                long offset = rangeBegin - finishedSize;
                if (offset < 0) {
                    offset = 0;
                }
                long currentSize = process.getCurrentSize();
                if (currentSize < offset) {
                    offset = currentSize;
                }
                long realOffset = inputStream.skip(offset);
                logger.info("Skip offset {} bytes.", realOffset);
                rangeBegin = realOffset + finishedSize;
                long remaining = rangeEnd - rangeBegin + 1;
                if (remaining <= 0) {
                    remaining = Long.MAX_VALUE;
                }
                {
                    int l;
                    while (remaining > 0) {
                        l = inputStream.read(buffer, 0, (int) Math.min(BUFFER_SIZE, remaining));
                        if (l == -1) {
                            index++;
                            if (index < processes.size()) {
                                process = processes.get(index);
                                if (!process.isStarted() || (process.isStopped() && !process.isFinished()))  {
                                    process.init();
                                }
                                if ((process.isStarted() && !process.isStopped()) || (process.isStarted() && process.isFinished())) {
                                    inputStream = process.getOutputFileStream();
                                    continue;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        outStream.write(buffer, 0, l);
                        remaining -= l;
                    }
                }
                outStream.flush();
            }
        } finally {
            for (TranscoderProcess process : processes) {
                process.cache();
            }
        }
    }

    @Override
    public boolean isStreaming() {
        return true;
    }

    private long getFinishedSize(List<TranscoderProcess> processes) {
        long totalSize = 0;
        for (int i = 0; i < this.index; i++) {
            totalSize = totalSize + processes.get(i).getCurrentSize();
        }
        return totalSize;
    }

    private int getProcessIndex(long rangeBegin, List<TranscoderProcess> processes) {
        if (rangeBegin <= 0) {
            return 0;
        }

        int index = 0;
        long totalSize = 0;

        for (int i = 0; i < processes.size(); i++) {
            TranscoderProcess process = processes.get(i);
            // Process not even init, index from here
            if (!process.isStarted()) {
                index = i;
                break;
            }
            // Process stated and finished
            else if (process.isStarted() && process.isFinished()) {
                totalSize = totalSize + process.getCurrentSize();
                if (totalSize > rangeBegin) {
                    index = i;
                    break;
                }
            }
            // Process stated and stopped but not finished, this mean the process in a wrong state, and we restart from here
            else if (process.isStarted() && !process.isFinished() && process.isStopped()) {
                index = i;
                break;
            }
            // Process stated and running
            else if (process.isStarted() && !process.isFinished() && !process.isStopped()) {
                index = i;
                break;
            }
        }

        return index;
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
