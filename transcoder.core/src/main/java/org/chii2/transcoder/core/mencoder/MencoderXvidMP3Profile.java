package org.chii2.transcoder.core.mencoder;

import org.chii2.transcoder.api.core.BackgroundTranscoderService;
import org.chii2.transcoder.api.core.VideoTranscoderOutputReader;
import org.chii2.transcoder.api.core.VideoTranscoderProfile;
import org.chii2.transcoder.api.core.VideoTranscoderProfileSet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Transcoding to Xvid MP3 AVI format for XBox360
 */
public class MencoderXvidMP3Profile implements VideoTranscoderProfile {
    // Input Video File
    private File videoFile;
    // Output Video File
    private File transcoderFile;

    // Audio (MP3 Lame) Option
    private MencoderMP3LameOpts mp3Opt;
    // Video (Xvid) Option
    private MencoderXvidOpts xvidOpt;

    /**
     * Constructor
     *
     * @param videoFile  Input Video File
     * @param transcoderFile Output Video File
     */
    public MencoderXvidMP3Profile(File videoFile, File transcoderFile) {
        this.videoFile = videoFile;
        this.transcoderFile = transcoderFile;

        this.mp3Opt = new MencoderMP3LameOpts();
        this.mp3Opt.setFast(true);
        this.mp3Opt.setPreset("standard");

        this.xvidOpt = new MencoderXvidOpts();
        this.xvidOpt.setChromaOpt(true);
        this.xvidOpt.setVhq(4);
        this.xvidOpt.setMaxBframes(1);
        this.xvidOpt.setQuantType("mpeg");
        this.xvidOpt.setBitrate(3000);
    }

    @Override
    public VideoTranscoderProfileSet.Transcoder getTranscoder() {
        return VideoTranscoderProfileSet.Transcoder.MENCODER;
    }

    @Override
    public VideoTranscoderOutputReader getOutputReader(Process process, BackgroundTranscoderService service) {
        return new MencoderOutputTextReader(process, service);
    }

    public List<String> getCommands() {
        List<String> commands = new ArrayList<String>();

        commands.add("mencoder");
        commands.add("\"" + this.videoFile.getAbsolutePath() + "\"");
        commands.add("-oac");
        commands.add(this.mp3Opt.getAudioOptionName());
        commands.addAll(this.mp3Opt.getAudioOptionCommands());
        commands.add("-ffourcc");
        commands.add("DX50");
        commands.add("-ovc");
        commands.add(this.xvidOpt.getVideoOptionName());
        commands.addAll(this.xvidOpt.getVideoOptionCommands());
        commands.add("-o");
        commands.add("\"" + this.transcoderFile.getAbsolutePath() + "\"");

        return commands;
    }
}
