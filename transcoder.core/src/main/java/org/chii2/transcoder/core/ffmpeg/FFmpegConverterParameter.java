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
    // Video Parameters
    private List<String> videoParameters;
    // Video Same Quantizer
    private boolean sameQuantizer = false;
    // Video Bit rate
    private long videoBitrate;
    // CRF (x264)
    private int videoCRF = -1;
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
    // Video Preset
    private List<String> videoPresets;
    // Audio Codec
    private String audioCodec;
    // Audio Bit rate
    private long audioBitrate;
    // Audio Sampling Frequency
    private long audioSamplingFrequency;
    // Audio Channels
    private long audioChannels;
    // Audio Parameters
    private List<String> audioParameters;
    // Audio Preset
    private List<String> audioPresets;
    // Threads
    private int threads = -1;

    /**
     * Constructor
     *
     * @param inputFile    Input File
     * @param outputFile   Output File
     * @param videoCodec   Video Codec
     * @param audioCodec   Audio Codec
     */
    public FFmpegConverterParameter(File inputFile, File outputFile, String videoCodec, String audioCodec) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.videoCodec = videoCodec;
        this.audioCodec = audioCodec;
    }

    /**
     * Get FFmpeg parameters
     *
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
            } else if ("libx264".equalsIgnoreCase(videoCodec) && videoCRF >= 0) {
                command.add("-crf");
                command.add(String.valueOf(videoCRF));
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
            if (videoBufferSize > 0) {
                command.add("-bufsize");
                command.add(String.valueOf(videoBufferSize));
            }
            if (videoPresets != null && !videoPresets.isEmpty()) {
                for (String videoPreset : videoPresets) {
                    command.add("-vpre");
                    command.add(videoPreset);
                }
            }
            if (videoParameters != null && !videoParameters.isEmpty()) {
                for (String videoParameter : videoParameters) {
                    command.add(videoParameter);
                }
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
            if (audioPresets != null && !audioPresets.isEmpty()) {
                for (String audioPreset : audioPresets) {
                    command.add("-apre");
                    command.add(audioPreset);
                }
            }
            if (audioParameters != null && !audioParameters.isEmpty()) {
                for (String audioParameter : audioParameters) {
                    command.add(audioParameter);
                }
            }
        }
        if (threads >= 0) {
            command.add("-threads");
            command.add(String.valueOf(threads));
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

    public List<String> getVideoParameters() {
        return videoParameters;
    }

    public void setVideoParameters(List<String> videoParameters) {
        this.videoParameters = videoParameters;
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

    public int getVideoCRF() {
        return videoCRF;
    }

    public void setVideoCRF(int videoCRF) {
        this.videoCRF = videoCRF;
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

    public List<String> getVideoPresets() {
        return videoPresets;
    }

    public void setVideoPresets(List<String> videoPresets) {
        this.videoPresets = videoPresets;
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

    public List<String> getAudioParameters() {
        return audioParameters;
    }

    public void setAudioParameters(List<String> audioParameters) {
        this.audioParameters = audioParameters;
    }

    public List<String> getAudioPresets() {
        return audioPresets;
    }

    public void setAudioPresets(List<String> audioPresets) {
        this.audioPresets = audioPresets;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}
