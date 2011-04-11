package org.chii2.medialibrary.api.persistence.entity;

import java.util.Date;
import java.util.List;

/**
 * Represent a movie (which may contains multiple files, like disk1 and disk2)
 */
public interface Movie {

    /**
     * Get the id
     *
     * @return id Id
     */
    public String getId();

    /**
     * Set the id
     *
     * @param id Id
     */
    public void setId(String id);

    /**
     * Get list of movie files
     *
     * @return Movie files
     */
    public List<? extends MovieFile> getFiles();

    /**
     * Set list of movie files
     *
     * @param files Movie files
     */
    public void setFiles(List<MovieFile> files);

    /**
     * Get number of files included
     *
     * @return Files count
     */
    public int getFilesCount();

    /**
     * Add a movie file
     *
     * @param file Movie file
     */
    public void addFile(MovieFile file);

    /**
     * Remove a movie file
     *
     * @param file Movie file
     */
    public void removeFile(MovieFile file);

    /**
     * Get File by disk number
     *
     * @param diskNum Disk Number
     * @return Movie File
     */
    public MovieFile getFile(int diskNum);

    /**
     * Get list of movie information
     *
     * @return Movie information
     */
    public List<? extends MovieInfo> getInfo();

    /**
     * Set list of movie information
     *
     * @param info Movie information
     */
    public void setInfo(List<MovieInfo> info);

    /**
     * Get movie information count
     *
     * @return Count
     */
    public int getInfoCount();

    /**
     * Add a new movie information
     *
     * @param info Movie information
     */
    public void addInfo(MovieInfo info);

    /**
     * Remove a movie information
     *
     * @param info Movie information
     */
    public void removeInfo(MovieInfo info);

    /**
     * Get default movie title (movie name)
     *
     * @return Movie Title
     */
    public String getTitle();

    /**
     * Get movie released date
     *
     * @return Movie Released Date
     */
    public Date getReleasedDate();

    /**
     * Get movie overview
     *
     * @return Overview
     */
    public String getOverview();

    /**
     * Get movie rating
     *
     * @return Rating
     */
    public double getRating();

    /**
     * Get movie certification
     *
     * @return Certification
     */
    public String getCertification();

    /**
     * Get movie language
     *
     * @return Language
     */
    public String getLanguage();

    /**
     * Get movie format
     *
     * @return Format
     */
    public String getFormat();

    /**
     * Get movie duration in milliseconds
     *
     * @return Duration
     */
    public long getDuration();

    /**
     * Get movie size in bytes
     *
     * @return Size
     */
    public long getSize();

    /**
     * Get movie bit rate in bytes
     *
     * @return Bit Rate
     */
    public long getBitRate();

    /**
     * Get video width
     *
     * @return Video Width
     */
    public int getVideoWidth();

    /**
     * Get video height
     *
     * @return Video Height
     */
    public int getVideoHeight();

    /**
     * Get video codec
     *
     * @return Video Codec
     */
    public String getVideoCodec();

    /**
     * Get video bit rate
     *
     * @return Video BitRate
     */
    public long getVideoBitRate();

    /**
     * Get video fps
     *
     * @return Video FPS
     */
    public float getVideoFps();

    /**
     * Get audio codec
     *
     * @return Audio Codec
     */
    public String getAudioCodec();

    /**
     * Get movie audio channels number
     *
     * @return Channels Number
     */
    public int getAudioChannels();

    /**
     * Get audio bit rate
     *
     * @return Audio BitRate
     */
    public long getAudioBitRate();

    /**
     * Get movie audio sample frequency in HZ (sample bit rate)
     *
     * @return Sample Frequency
     */
    public long getAudioSampleBitRate();

    /**
     * Get movie audio bits per sample (bit depth)
     *
     * @return Bits per Sample
     */
    public int getAudioBitDepth();
}
