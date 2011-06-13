package org.chii2.medialibrary.persistence.entity;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.ImageFile;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Represent a Image (which may contains a image file)
 */
@Entity(name = "IMAGE")
@Table(name = "IMAGE")
public class ImageImpl implements Image {

    // ID (Primary Key) in UUID format
    @Id
    @Column(name = "ID")
    private String id;

    // Image Title
    @Column(name = "TITLE")
    private String title;

    // Image Album
    @Column(name = "ALBUM")
    private String album;

    // Image Rating
    @Column(name = "Rating")
    private float rating;

    // Image File
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private ImageFileImpl file;

    /**
     * Constructor
     */
    public ImageImpl() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getAlbum() {
        return album;
    }

    @Override
    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public float getRating() {
        return rating;
    }

    @Override
    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public ImageFile getFile() {
        return file;
    }

    @Override
    public void setFile(ImageFile imageFile) {
        if (imageFile.getClass() == ImageFileImpl.class) {
            this.file = (ImageFileImpl) imageFile;
        }
    }

    @Override
    public boolean isPhoto() {
        return StringUtils.isNotBlank(this.getCameraModel()) || StringUtils.isNotBlank(this.getCameraMaker());
    }

    @Override
    public String getFileName() {
        return file.getFileName();
    }

    @Override
    public String getFilePath() {
        return file.getFilePath();
    }

    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    @Override
    public String getFileExtension() {
        return file.getFileExtension();
    }

    @Override
    public String getType() {
        return file.getType();
    }

    @Override
    public long getSize() {
        return file.getSize();
    }

    @Override
    public int getWidth() {
        return file.getWidth();
    }

    @Override
    public int getHeight() {
        return file.getHeight();
    }

    @Override
    public int getColorDepth() {
        return file.getColorDepth();
    }

    @Override
    public String getColorType() {
        return file.getColorType();
    }

    @Override
    public String getCameraModel() {
        return file.getCameraModel();
    }

    @Override
    public Date getDateTaken() {
        return file.getDateTaken();
    }

    @Override
    public String getExposureTime() {
        return file.getExposureTime();
    }

    @Override
    public int getISO() {
        return file.getISO();
    }

    @Override
    public String getFocalLength() {
        return file.getFocalLength();
    }

    @Override
    public String getUserComment() {
        return file.getUserComment();
    }

    @Override
    public int getWidthDPI() {
        return file.getWidthDPI();
    }

    @Override
    public int getHeightDPI() {
        return file.getHeightDPI();
    }

    @Override
    public String getMimeType() {
        return file.getMimeType();
    }

    @Override
    public String getCameraMaker() {
        return file.getCameraMaker();
    }

    @Override
    public String getFNumber() {
        return file.getFNumber();
    }

    @Override
    public String getShutterSpeed() {
        return file.getShutterSpeed();
    }

    @Override
    public int getFlash() {
        return file.getFlash();
    }
}
