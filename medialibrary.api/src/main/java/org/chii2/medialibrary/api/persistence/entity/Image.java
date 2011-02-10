package org.chii2.medialibrary.api.persistence.entity;

import java.util.Date;

/**
 * Represent a Image (which may contains a image file)
 */
public interface Image {

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
     * Get the Image File
     *
     * @return Image File
     */
    public ImageFile getFile();

    /**
     * Set the Image File
     *
     * @param imageFile Image File
     */
    public void setFile(ImageFile imageFile);

    /**
     * Get Image File Name
     *
     * @return File Name
     */
    public String getFileName();

    /**
     * Get Image File Path (Directory)
     *
     * @return File Path (Directory)
     */
    public String getFilePath();

    /**
     * Get Image Absolute File Name (Including Path)
     *
     * @return Absolute File Name (Including Path)
     */
    public String getAbsoluteName();

    /**
     * Get Image File Extension
     *
     * @return Movie File Extension
     */
    public String getFileExtension();

    /**
     * Get Image Title
     *
     * @return Title
     */
    public String getTitle();

    /**
     * Get Image Type
     *
     * @return Type
     */
    public String getType();

    /**
     * Get Image Size
     *
     * @return Size
     */
    public long getSize();

    /**
     * Get Image Width
     *
     * @return Width
     */
    public int getWidth();

    /**
     * Get Image Height
     *
     * @return Height
     */
    public int getHeight();

    /**
     * Get Image Color Depth
     *
     * @return Color Depth
     */
    public int getColorDepth();

    /**
     * Get Image Color Type
     *
     * @return Color Type
     */
    public String getColorType();

    /**
     * Get Image Camera Model
     *
     * @return Camera Model
     */
    public String getCameraModel();

    /**
     * Get Image taken Date
     *
     * @return Date
     */
    public Date getDateTaken();

    /**
     * Get Image Exposure Time (sec)
     *
     * @return Exposure Time
     */
    public String getExposureTime();

    /**
     * Get Image ISO Speed Ratings
     *
     * @return ISO Speed Ratings
     */
    public int getISOSpeedRatings();

    /**
     * Get Image Focal Length (mm)
     *
     * @return Focal Length
     */
    public String getFocalLength();
}
