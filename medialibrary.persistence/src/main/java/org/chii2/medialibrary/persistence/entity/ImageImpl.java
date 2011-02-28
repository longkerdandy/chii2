package org.chii2.medialibrary.persistence.entity;

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
@NamedQueries({
        @NamedQuery(name = "Image.deleteAll",
                query = "DELETE FROM IMAGE i")
})
public class ImageImpl implements Image {

    // ID (Primary Key) in UUID format
    @Id
    @Column(name = "ID")
    private String id;

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
    public String getFileName() {
        return file.getFileName();
    }

    @Override
    public String getFilePath() {
        return file.getFilePath();
    }

    @Override
    public String getAbsoluteName() {
        return file.getAbsoluteName();
    }

    @Override
    public String getFileExtension() {
        return file.getFileExtension();
    }

    @Override
    public String getTitle() {
        return file.getTitle();
    }

    @Override
    public String getAlbum() {
        return file.getAlbum();
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
    public int getISOSpeedRatings() {
        return file.getISOSpeedRatings();
    }

    @Override
    public String getFocalLength() {
        return file.getFocalLength();
    }
}
