package org.chii2.medialibrary.api.persistence.entity;

import java.util.Date;

/**
 * Represent a movie file on the disk
 */
public interface MovieFile {

    /**
     * Get the id
     *
     * @return id
     */
    public String getId();

    /**
     * Set the id
     *
     * @param id
     */
    public void setId(String id);

    /**
     * Get Movie File Name
     *
     * @return File Name
     */
    public String getFileName();

    /**
     * Set Movie File Name
     *
     * @param fileName File Name
     */
    public void setFileName(String fileName);

    /**
     * Get Movie File Path (Directory)
     *
     * @return File Path (Directory)
     */
    public String getFilePath();

    /**
     * Set  Movie File Path (Directory)
     *
     * @param filePath Movie File Path (Directory)
     */
    public void setFilePath(String filePath);

    /**
     * Get Movie Absolute File Name (Including Path)
     *
     * @return Absolute File Name (Including Path)
     */
    public String getAbsoluteName();

    /**
     * Set Movie Absolute File Name (Including Path)
     *
     * @param absoluteName Absolute File Name (Including Path)
     */
    public void setAbsoluteName(String absoluteName);

    /**
     * Get Movie Name (Guessed from File Name)
     *
     * @return Movie Name
     */
    public String getMovieName();

    /**
     * Set Movie Name (Guessed from File Name)
     *
     * @param movieName Movie Name
     */
    public void setMovieName(String movieName);

    /**
     * Get Movie Release Year (Guessed from File Name)
     *
     * @return Year
     */
    public int getYear();

    /**
     * Set Movie Release Year (Guessed from File Name)
     *
     * @param year Movie Release Year
     */
    public void setYear(int year);

    /**
     * Get Movie Source (Guessed from File Name)
     *
     * @return Movie Source
     */
    public String getSource();

    /**
     * Set Movie Source (Guessed from File Name)
     *
     * @param source Movie Source
     */
    public void setSource(String source);

    /**
     * Get Movie Video Codec (Guessed from File Name)
     *
     * @return Movie Video Codec
     */
    public String getVideoCodec();

    /**
     * Set Movie Video Codec (Guessed from File Name)
     *
     * @param videoCodec Movie Video Codec
     */
    public void setVideoCodec(String videoCodec);

    /**
     * Get Movie Audio Codec (Guessed from File Name)
     *
     * @return Movie Audio Codec
     */
    public String getAudioCodec();

    /**
     * Set Movie Audio Codec (Guessed from File Name)
     *
     * @param audioCodec Movie Audio Codec
     */
    public void setAudioCodec(String audioCodec);

    /**
     * Get Movie Release Group (Guessed from File Name)
     *
     * @return Movie Release Group
     */
    public String getGroup();

    /**
     * Set Movie Release Group (Guessed from File Name)
     *
     * @param group Movie Release Group
     */
    public void setGroup(String group);

    /**
     * Get Movie Disk Number (Guessed from File Name)
     *
     * @return Movie Disk Number
     */
    public int getDiskNum();

    /**
     * Set Movie Disk Number (Guessed from File Name)
     *
     * @param diskNum Movie Disk Number
     */
    public void setDiskNum(int diskNum);

    /**
     * Get Movie File Extension
     *
     * @return Movie File Extension
     */
    public String getFileExtension();

    /**
     * Set Movie File Extension
     *
     * @param fileExtension Movie File Extension
     */
    public void setFileExtension(String fileExtension);

    /**
     * Get File Format
     *
     * @return File Format
     */
    public String getFormat();

    /**
     * Set File Format
     *
     * @param format File Format
     */
    public void setFormat(String format);

    /**
     * Get File Size
     *
     * @return File Size
     */
    public long getSize();

    /**
     * Set File Size
     *
     * @param size File Size
     */
    public void setSize(long size);

    /**
     * Get Duration
     *
     * @return Duration
     */
    public long getDuration();

    /**
     * Set Duration
     *
     * @param duration Duration
     */
    public void setDuration(long duration);

    /**
     * Get MIME
     *
     * @return MIME
     */
    public String getMime();

    /**
     * Set MIME
     *
     * @param mime MIME
     */
    public void setMime(String mime);

    /**
     * Get Bit Rate
     *
     * @return Bit Rate
     */
    public long getBitRate();

    /**
     * Set Bit Rate
     *
     * @param bitRate Bit Rate
     */
    public void setBitRate(long bitRate);

    /**
     * Get Video Stream Count
     *
     * @return Video Stream Count
     */
    public int getVideoStreamCount();

    /**
     * Set Video Stream Count
     *
     * @param videoStreamCount Video Stream Count
     */
    public void setVideoStreamCount(int videoStreamCount);

    /**
     * Get Modification Date
     *
     * @return Modification Date
     */
    public Date getModificationDate();

    /**
     * Set Modification Date
     *
     * @param modificationDate Modification Date
     */
    public void setModificationDate(Date modificationDate);

    /**
     * Get Video Format
     *
     * @return Video Format
     */
    public String getVideoFormat();

    /**
     * Set Video Format
     *
     * @param videoFormat Video Format
     */
    public void setVideoFormat(String videoFormat);

    /**
     * Get Video Bit Rate
     *
     * @return Video Bit Rate
     */
    public long getVideoBitRate();

    /**
     * Set Video Bit Rate
     *
     * @param videoBitRate Video Bit Rate
     */
    public void setVideoBitRate(long videoBitRate);

    /**
     * Get Video Bit Depth
     *
     * @return Video Bit Depth
     */
    public int getVideoBitDepth();

    /**
     * Set Video Bit Depth
     *
     * @param videoBitDepth Video Bit Depth
     */
    public void setVideoBitDepth(int videoBitDepth);

    /**
     * Get Video Frame Rate
     *
     * @return Video Frame Rate
     */
    public float getVideoFrameRate();

    /**
     * Set Video Frame Rate
     *
     * @param videoFrameRate Video Frame Rate
     */
    public void setVideoFrameRate(float videoFrameRate);

    /**
     * Get Video Width
     *
     * @return Video Width
     */
    public int getVideoWidth();

    /**
     * Set Video Width
     *
     * @param videoWidth Video Width
     */
    public void setVideoWidth(int videoWidth);

    /**
     * Get Video Height
     *
     * @return Video Height
     */
    public int getVideoHeight();

    /**
     * Set Video Height
     *
     * @param videoHeight Video Height
     */
    public void setVideoHeight(int videoHeight);

    /**
     * Get Audio Stream Count
     *
     * @return Audio Stream Count
     */
    public int getAudioStreamCount();

    /**
     * Set Audio Stream Count
     *
     * @param audioStreamCount Audio Stream Count
     */
    public void setAudioStreamCount(int audioStreamCount);

    /**
     * Get Audio Format
     *
     * @return Audio Format
     */
    public String getAudioFormat();

    /**
     * Set Audio Format
     *
     * @param audioFormat Audio Format
     */
    public void setAudioFormat(String audioFormat);

    /**
     * Get Audio Bit Rate
     *
     * @return Audio Bit Rate
     */
    public long getAudioBitRate();

    /**
     * Set Audio Bit Rate
     *
     * @param audioBitRate Audio Bit Rate
     */
    public void setAudioBitRate(long audioBitRate);

    /**
     * Get Audio Bit Depth
     *
     * @return Audio Bit Depth
     */
    public int getAudioBitDepth();

    /**
     * Set Audio Bit Depth
     *
     * @param audioBitDepth Audio Bit Depth
     */
    public void setAudioBitDepth(int audioBitDepth);

    /**
     * Get Audio Channel Count
     *
     * @return Audio Channel Count
     */
    public int getAudioChannelCount();

    /**
     * Set Audio Channel Count
     *
     * @param audioChannelCount Audio Channel Count
     */
    public void setAudioChannelCount(int audioChannelCount);

    /**
     * Get Audio Sampling Rate
     *
     * @return Audio Sampling Rate
     */
    public long getAudioSamplingRate();

    /**
     * Set Audio Sampling Rate
     *
     * @param audioSamplingRate Audio Sampling Rate
     */
    public void setAudioSamplingRate(long audioSamplingRate);

    /**
     * Get Audio Language
     *
     * @return Audio Language
     */
    public String getAudioLanguage();

    /**
     * Set Audio Language
     *
     * @param audioLanguage Audio Language
     */
    public void setAudioLanguage(String audioLanguage);

    /**
     * Get Movie this file belong to
     *
     * @return Movie
     */
    public Movie getMovie();

    /**
     * Set Movie this file belong to
     *
     * @param movie Movie
     */
    public void setMovie(Movie movie);
}
