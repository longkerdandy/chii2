package org.chii2.transcoder.core.ffmpeg;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * FFmpeg Converter Parameter
 */
public class FFmpegConverterParameter {
    // Input File
    private File inputFile;
    // Output File
    private File outputFile;
    // Video Codec
    private String videoCodec;
    // Video Same Quantizer
    private boolean sameQuantizer = false;
    // Video Bit rate
    private long videoBitrate;
    // Video Max rate
    private long videoMaxrate;
    // Video Min rate
    private long videoMinrate;
    // Video Frame Rate
    private int videoFrameRate;
    // Video Buffer Size
    private long videoBufferSize;
    // Video Gop Size
    private int videoGopSize;
    // Video Bit Stream Filter
    private String videoBitStreamFilter;
    // Audio Codec
    private String audioCodec;
    // Audio Bit rate
    private long audioBitrate;
    // Audio Sampling Frequency
    private long audioSamplingFrequency;
    // Audio Channels
    private long audioChannels;

    /**
     * Constructor
     *
     * @param inputFile    Input File
     * @param videoCodec   Video Codec
     * @param videoBitrate Video Bit rate
     * @param audioCodec   Audio Codec
     * @param audioBitrate Audio Bit rate
     */
    public FFmpegConverterParameter(File inputFile, String videoCodec, long videoBitrate, String audioCodec, long audioBitrate) {
        this.inputFile = inputFile;
        this.videoCodec = videoCodec;
        this.videoBitrate = videoBitrate;
        this.audioCodec = audioCodec;
        this.audioBitrate = audioBitrate;
        // Random output file at temp directory TODO: not sure this is correct for all the platforms
        this.outputFile = new File(System.getProperty("java.io.tmpdir"), "chii2/" + UUID.randomUUID().toString() + ".avi");
    }

    /**
     * Constructor
     *
     * @param inputFile    Input File
     * @param outputFile   Output File
     * @param videoCodec   Video Codec
     * @param videoBitrate Video Bit rate
     * @param audioCodec   Audio Codec
     * @param audioBitrate Audio Bit rate
     */
    public FFmpegConverterParameter(File inputFile, File outputFile, String videoCodec, long videoBitrate, String audioCodec, long audioBitrate) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.videoCodec = videoCodec;
        this.videoBitrate = videoBitrate;
        this.audioCodec = audioCodec;
        this.audioBitrate = audioBitrate;
    }

    /**
     * Get FFmpeg parameters
     * @return Parameter List
     */
    public List<String> getParameters() {
        List<String> command = new ArrayList<String>();
        command.add("ffmpeg");
        command.add("-y");
        command.add("-i");
        command.add(inputFile.getAbsolutePath());
        command.add("-vcodec");
        command.add(videoCodec);
        if (!"copy".equalsIgnoreCase(videoCodec)) {
            if (sameQuantizer) {
                command.add("-sameq");
            } else {
                if (videoBitrate > 0) {
                    command.add("-b");
                    command.add(String.valueOf(videoBitrate));
                }
                if (videoMaxrate > 0) {
                    command.add("-maxrate");
                    command.add(String.valueOf(videoMaxrate));
                }
                if (videoMinrate > 0) {
                    command.add("-minrate");
                    command.add(String.valueOf(videoMinrate));
                }
            }
            if (videoFrameRate > 0) {
                command.add("-r");
                command.add(String.valueOf(videoFrameRate));
            }
            if (videoGopSize > 0) {
                command.add("-g");
                command.add(String.valueOf(videoGopSize));
            }
        }
        if (StringUtils.isNotBlank(videoBitStreamFilter)) {
            command.add("-vbsf");
            command.add(videoBitStreamFilter);
        }
        command.add("-acodec");
        command.add(audioCodec);
        if (!"copy".equalsIgnoreCase(audioCodec)) {
            if (audioBitrate > 0) {
                command.add("-ab");
                command.add(String.valueOf(audioBitrate));
            }
            if (audioSamplingFrequency > 0) {
                command.add("-ar");
                command.add(String.valueOf(audioSamplingFrequency));
            }
            if (audioChannels > 0) {
                command.add("-ac");
                command.add(String.valueOf(audioChannels));
            }
        }
        command.add(outputFile.getAbsolutePath());

        return command;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public boolean isSameQuantizer() {
        return sameQuantizer;
    }

    public void setSameQuantizer(boolean sameQuantizer) {
        this.sameQuantizer = sameQuantizer;
    }

    public long getVideoBitrate() {
        return videoBitrate;
    }

    public void setVideoBitrate(long videoBitrate) {
        this.videoBitrate = videoBitrate;
    }

    public long getVideoMaxrate() {
        return videoMaxrate;
    }

    public void setVideoMaxrate(long videoMaxrate) {
        this.videoMaxrate = videoMaxrate;
    }

    public long getVideoMinrate() {
        return videoMinrate;
    }

    public void setVideoMinrate(long videoMinrate) {
        this.videoMinrate = videoMinrate;
    }

    public int getVideoFrameRate() {
        return videoFrameRate;
    }

    public void setVideoFrameRate(int videoFrameRate) {
        this.videoFrameRate = videoFrameRate;
    }

    public long getVideoBufferSize() {
        return videoBufferSize;
    }

    public void setVideoBufferSize(long videoBufferSize) {
        this.videoBufferSize = videoBufferSize;
    }

    public int getVideoGopSize() {
        return videoGopSize;
    }

    public void setVideoGopSize(int videoGopSize) {
        this.videoGopSize = videoGopSize;
    }

    public String getVideoBitStreamFilter() {
        return videoBitStreamFilter;
    }

    public void setVideoBitStreamFilter(String videoBitStreamFilter) {
        this.videoBitStreamFilter = videoBitStreamFilter;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public long getAudioBitrate() {
        return audioBitrate;
    }

    public void setAudioBitrate(long audioBitrate) {
        this.audioBitrate = audioBitrate;
    }

    public long getAudioSamplingFrequency() {
        return audioSamplingFrequency;
    }

    public void setAudioSamplingFrequency(long audioSamplingFrequency) {
        this.audioSamplingFrequency = audioSamplingFrequency;
    }

    public long getAudioChannels() {
        return audioChannels;
    }

    public void setAudioChannels(long audioChannels) {
        this.audioChannels = audioChannels;
    }
}
