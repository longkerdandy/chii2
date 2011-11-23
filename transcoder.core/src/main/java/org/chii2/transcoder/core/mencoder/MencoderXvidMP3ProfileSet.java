package org.chii2.transcoder.core.mencoder;

import org.chii2.transcoder.api.core.VideoTranscoderProfile;
import org.chii2.transcoder.api.core.VideoTranscoderProfileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Transcoding to Xvid MP3 AVI format for XBox360
 */
public class MencoderXvidMP3ProfileSet implements VideoTranscoderProfileSet {
    // Video ID
    private String videoID;
    // Video Type
    private VideoType videoType;
    // Video Files
    private List<File> videoFiles;
    // Transcoder Files
    private List<File> transcoderFiles;
    // Profiles
    private List<VideoTranscoderProfile> profiles;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.mencoder");

    /**
     * Constructor
     *
     * @param videoID Video ID
     * @param videoType Video Type
     * @param videoFiles Video Files (Input Files)
     * @param trancoderDirectory Transcoder Directory (Output Directory)
     */
    public MencoderXvidMP3ProfileSet(String videoID, VideoType videoType, List<File> videoFiles, File trancoderDirectory) {
        if (videoFiles != null && videoFiles.size() > 0) {
            this.videoID = videoID;
            this.videoType = videoType;
            this.videoFiles = videoFiles;
            this.transcoderFiles = new ArrayList<File>();
            this.profiles = new ArrayList<VideoTranscoderProfile>();
            // Generate output files & profiles
            for (File videoFile : this.videoFiles) {
                File transcoderFile = new File(trancoderDirectory, UUID.randomUUID().toString() + ".avi");
                this.transcoderFiles.add(transcoderFile);
                this.profiles.add(new MencoderXvidMP3Profile(videoFile, transcoderFile));
            }
        } else {
            logger.warn("Video files not valid, transcoder process will not work.");
        }
    }

    @Override
    public String getVideoID() {
        return this.videoID;
    }

    @Override
    public VideoType getVideoType() {
        return this.videoType;
    }

    @Override
    public List<File> getVideoFiles() {
        return this.videoFiles;
    }

    @Override
    public List<VideoTranscoderProfile> getProfiles() {
        return this.profiles;
    }

    @Override
    public List<File> getTranscoderFiles() {
        return this.transcoderFiles;
    }

    @Override
    public String getDescription() {
        // TODO I18N
        return "Transcoded " + this.videoType + " " + this.videoID +" to Xvid + MP3 in AVI format with Mencoder.";
    }
}
