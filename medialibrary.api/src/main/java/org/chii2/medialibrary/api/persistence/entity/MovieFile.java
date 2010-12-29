package org.chii2.medialibrary.api.persistence.entity;

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
    public String getYear();

    /**
     * Set Movie Release Year (Guessed from File Name)
     *
     * @param year Movie Release Year
     */
    public void setYear(String year);

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
     * Get Movie Resolution (Guessed from File Name)
     *
     * @return Movie Resolution
     */
    public String getResolution();

    /**
     * Set Movie Resolution (Guessed from File Name)
     *
     * @param resolution Movie Resolution
     */
    public void setResolution(String resolution);

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

}
