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

    // Original Image File
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private ImageFileImpl originalFile;

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
    public ImageFile getOriginalFile() {
        return originalFile;
    }

    @Override
    public void setOriginalFile(ImageFile imageFile) {
        if (imageFile.getClass() == ImageFileImpl.class) {
            this.originalFile = (ImageFileImpl) imageFile;
            this.originalFile.setImage(this);
        }
    }

    @Override
    public boolean isPhoto() {
        return StringUtils.isNotBlank(this.getCameraModel()) || StringUtils.isNotBlank(this.getCameraMaker());
    }

    @Override
    public String getFileName() {
        return originalFile.getFileName();
    }

    @Override
    public String getFilePath() {
        return originalFile.getFilePath();
    }

    @Override
    public String getAbsolutePath() {
        return originalFile.getAbsolutePath();
    }

    @Override
    public String getFileExtension() {
        return originalFile.getFileExtension();
    }

    @Override
    public String getType() {
        return originalFile.getType();
    }

    @Override
    public long getSize() {
        return originalFile.getSize();
    }

    @Override
    public int getWidth() {
        return originalFile.getWidth();
    }

    @Override
    public int getHeight() {
        return originalFile.getHeight();
    }

    @Override
    public int getColorDepth() {
        return originalFile.getColorDepth();
    }

    @Override
    public String getColorType() {
        return originalFile.getColorType();
    }

    @Override
    public String getCameraModel() {
        return originalFile.getCameraModel();
    }

    @Override
    public Date getDateTaken() {
        return originalFile.getDateTaken();
    }

    @Override
    public String getExposureTime() {
        return originalFile.getExposureTime();
    }

    @Override
    public int getISO() {
        return originalFile.getISO();
    }

    @Override
    public String getFocalLength() {
        return originalFile.getFocalLength();
    }

    @Override
    public String getUserComment() {
        return originalFile.getUserComment();
    }

    @Override
    public int getWidthDPI() {
        return originalFile.getWidthDPI();
    }

    @Override
    public int getHeightDPI() {
        return originalFile.getHeightDPI();
    }

    @Override
    public String getMimeType() {
        return originalFile.getMimeType();
    }

    @Override
    public String getCameraMaker() {
        return originalFile.getCameraMaker();
    }

    @Override
    public String getFNumber() {
        return originalFile.getFNumber();
    }

    @Override
    public String getShutterSpeed() {
        return originalFile.getShutterSpeed();
    }

    @Override
    public int getFlash() {
        return originalFile.getFlash();
    }
}
