package org.chii2.medialibrary.api.persistence.entity;

import java.util.Date;

/**
 * Represent a image file on the disk
 */
public interface ImageFile {

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
     * Get Image File Name
     *
     * @return File Name
     */
    public String getFileName();

    /**
     * Set Image File Name
     *
     * @param fileName File Name
     */
    public void setFileName(String fileName);

    /**
     * Get Image File Path (Directory)
     *
     * @return File Path (Directory)
     */
    public String getFilePath();

    /**
     * Set  Image File Path (Directory)
     *
     * @param filePath Movie File Path (Directory)
     */
    public void setFilePath(String filePath);

    /**
     * Get Image Absolute File Name (Including Path)
     *
     * @return Absolute File Name (Including Path)
     */
    public String getAbsoluteName();

    /**
     * Set Image Absolute File Name (Including Path)
     *
     * @param absoluteName Absolute File Name (Including Path)
     */
    public void setAbsoluteName(String absoluteName);

    /**
     * Get Image File Extension
     *
     * @return Movie File Extension
     */
    public String getFileExtension();

    /**
     * Set Image File Extension
     *
     * @param fileExtension Movie File Extension
     */
    public void setFileExtension(String fileExtension);

    /**
     * Get Image Title
     *
     * @return Title
     */
    public String getTitle();

    /**
     * Set Image Title
     *
     * @param title Title
     */
    public void setTitle(String title);

    /**
     * Get Image Type
     *
     * @return Type
     */
    public String getType();

    /**
     * Set Image Type
     *
     * @param type Type
     */
    public void setType(String type);

    /**
     * Get Image Size
     *
     * @return Size
     */
    public long getSize();

    /**
     * Set Image Size
     *
     * @param size Size
     */
    public void setSize(long size);

    /**
     * Get Image Width
     *
     * @return Width
     */
    public int getWidth();

    /**
     * Set Image Width
     *
     * @param width Width
     */
    public void setWidth(int width);

    /**
     * Get Image Height
     *
     * @return Height
     */
    public int getHeight();

    /**
     * Set Image Height
     *
     * @param height Height
     */
    public void setHeight(int height);

    /**
     * Get Image Color Depth
     *
     * @return Color Depth
     */
    public int getColorDepth();

    /**
     * Set Image Color Depth
     *
     * @param colorDepth Color Depth
     */
    public void setColorDepth(int colorDepth);

    /**
     * Get Image Color Type
     *
     * @return Color Type
     */
    public String getColorType();

    /**
     * Set Image Color Type
     *
     * @param colorType Color Type
     */
    public void setColorType(String colorType);

    /**
     * Get Image Camera Model
     *
     * @return Camera Model
     */
    public String getCameraModel();

    /**
     * Set Image Camera Model
     *
     * @param model Camera Model
     */
    public void setCameraModel(String model);

    /**
     * Get Image taken Date
     *
     * @return Date
     */
    public Date getDateTaken();

    /**
     * Set Image taken Date
     *
     * @param date Date
     */
    public void setDateTaken(Date date);

    /**
     * Get Image Exposure Time (sec)
     *
     * @return Exposure Time
     */
    public String getExposureTime();

    /**
     * Set Image Exposure Time (sec)
     *
     * @param exposureTime Exposure Time
     */
    public void setExposureTime(String exposureTime);

    /**
     * Get Image ISO Speed Ratings
     *
     * @return ISO Speed Ratings
     */
    public int getISOSpeedRatings();

    /**
     * Set Image ISO Speed Ratings
     *
     * @param isoSpeedRatings ISO Speed Ratings
     */
    public void setISOSpeedRatings(int isoSpeedRatings);

    /**
     * Get Image Focal Length (mm)
     *
     * @return Focal Length
     */
    public String getFocalLength();

    /**
     * Set Image Focal Length (mm)
     *
     * @param focalLength Focal Length
     */
    public void setFocalLength(String focalLength);
}
