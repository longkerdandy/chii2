package org.chii2.transcoder.core.ffmpeg;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * FFmpeg Converter Parameter
 */
public class FFmpegConverterParameter {
    // Pipe Input
    private List<String> pipe;
    // Input
    private String input;
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
     * @param input      Input
     * @param outputFile Output
     * @param videoCodec Video Codec
     * @param audioCodec Audio Codec
     */
    public FFmpegConverterParameter(String input, File outputFile, String videoCodec, String audioCodec) {
        this.input = input;
        this.outputFile = outputFile;
        this.videoCodec = videoCodec;
        this.audioCodec = audioCodec;
    }

    /**
     * Constructor
     *
     * @param pipe       Pipe Input
     * @param outputFile Output
     * @param videoCodec Video Codec
     * @param audioCodec Audio Codec
     */
    public FFmpegConverterParameter(List<String> pipe, File outputFile, String videoCodec, String audioCodec) {
        this.pipe = pipe;
        this.input = "-";
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
        List<String> ffmpegCommands = new ArrayList<String>();
        // FFmpeg
        ffmpegCommands.add("ffmpeg");
        ffmpegCommands.add("-y");
        ffmpegCommands.add("-i");
        ffmpegCommands.add(input);
        ffmpegCommands.add("-vcodec");
        ffmpegCommands.add(videoCodec);
        if (!"copy".equalsIgnoreCase(videoCodec)) {
            if (sameQuantizer) {
                ffmpegCommands.add("-sameq");
            } else if ("libx264".equalsIgnoreCase(videoCodec) && videoCRF >= 0) {
                ffmpegCommands.add("-crf");
                ffmpegCommands.add(String.valueOf(videoCRF));
            } else {
                if (videoBitrate > 0) {
                    ffmpegCommands.add("-b");
                    ffmpegCommands.add(String.valueOf(videoBitrate));
                }
                if (videoMaxrate > 0) {
                    ffmpegCommands.add("-maxrate");
                    ffmpegCommands.add(String.valueOf(videoMaxrate));
                }
                if (videoMinrate > 0) {
                    ffmpegCommands.add("-minrate");
                    ffmpegCommands.add(String.valueOf(videoMinrate));
                }
            }
            if (videoFrameRate > 0) {
                ffmpegCommands.add("-r");
                ffmpegCommands.add(String.valueOf(videoFrameRate));
            }
            if (videoGopSize > 0) {
                ffmpegCommands.add("-g");
                ffmpegCommands.add(String.valueOf(videoGopSize));
            }
            if (videoBufferSize > 0) {
                ffmpegCommands.add("-bufsize");
                ffmpegCommands.add(String.valueOf(videoBufferSize));
            }
            if (videoPresets != null && !videoPresets.isEmpty()) {
                for (String videoPreset : videoPresets) {
                    ffmpegCommands.add("-vpre");
                    ffmpegCommands.add(videoPreset);
                }
            }
            if (videoParameters != null && !videoParameters.isEmpty()) {
                for (String videoParameter : videoParameters) {
                    ffmpegCommands.add(videoParameter);
                }
            }
        }
        if (StringUtils.isNotBlank(videoBitStreamFilter)) {
            ffmpegCommands.add("-vbsf");
            ffmpegCommands.add(videoBitStreamFilter);
        }
        ffmpegCommands.add("-acodec");
        ffmpegCommands.add(audioCodec);
        if (!"copy".equalsIgnoreCase(audioCodec)) {
            if (audioBitrate > 0) {
                ffmpegCommands.add("-ab");
                ffmpegCommands.add(String.valueOf(audioBitrate));
            }
            if (audioSamplingFrequency > 0) {
                ffmpegCommands.add("-ar");
                ffmpegCommands.add(String.valueOf(audioSamplingFrequency));
            }
            if (audioChannels > 0) {
                ffmpegCommands.add("-ac");
                ffmpegCommands.add(String.valueOf(audioChannels));
            }
            if (audioPresets != null && !audioPresets.isEmpty()) {
                for (String audioPreset : audioPresets) {
                    ffmpegCommands.add("-apre");
                    ffmpegCommands.add(audioPreset);
                }
            }
            if (audioParameters != null && !audioParameters.isEmpty()) {
                for (String audioParameter : audioParameters) {
                    ffmpegCommands.add(audioParameter);
                }
            }
        }
        if (threads >= 0) {
            ffmpegCommands.add("-threads");
            ffmpegCommands.add(String.valueOf(threads));
        }
        ffmpegCommands.add(outputFile.getAbsolutePath());

        if (pipe != null && pipe.size() > 0) {
            List<String> commands = new ArrayList<String>();
            commands.add("/bin/sh");
            commands.add("-c");
            StringBuilder sb = new StringBuilder();
            for (String pipeCommand : pipe) {
                sb.append(pipeCommand);
                sb.append(" ");
            }
            sb.append("|");
            sb.append(" ");
            for (String ffmpegCommand : ffmpegCommands) {
                sb.append(ffmpegCommand);
                sb.append(" ");
            }
            commands.add(sb.toString());
            return commands;
        } else {
            return ffmpegCommands;
        }
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
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
