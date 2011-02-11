package org.chii2.medialibrary.persistence.entity;

import org.chii2.medialibrary.api.persistence.entity.ImageFile;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Represent a image file on the disk
 */
@Entity(name = "IMAGE_FILE")
@Table(name = "IMAGE_FILE")
@NamedQueries({
        @NamedQuery(name = "ImageFile.findAll",
                query = "SELECT i FROM IMAGE_FILE i"),
        @NamedQuery(name = "ImageFile.findById",
                query = "SELECT i FROM IMAGE_FILE i WHERE i.id = :id"),
        @NamedQuery(name = "ImageFile.findAllAlbums",
                query = "SELECT DISTINCT i.album FROM IMAGE_FILE i")
})
public class ImageFileImpl implements ImageFile {

    // ID (Primary Key) in UUID format
    @Id
    @Column(name = "ID")
    private String id;

    // File Name
    @Column(name = "FILE_NAME")
    private String fileName;

    // File Path
    @Column(name = "FILE_PATH")
    private String filePath;

    // Absolute File Name
    @Column(name = "ABSOLUTE_NAME")
    private String absoluteName;

    // File Extension
    @Column(name = "FILE_EXTENSION")
    private String fileExtension;

    // Image Title
    @Column(name = "TITLE")
    private String title;

    // Image Album
    @Column(name = "ALBUM")
    private String album;

    // Image Type
    @Column(name = "IMAGE_TYPE")
    private String type;

    // Image File Size
    @Column(name = "SIZE")
    private long size;

    // Image Width
    @Column(name = "WIDTH")
    private int width;

    // Image Height
    @Column(name = "HEIGHT")
    private int height;

    // Image Color Depth
    @Column(name = "COLOR_DEPTH")
    private int colorDepth;

    // Image Color Type
    @Column(name = "COLOR_TYPE")
    private String colorType;

    // Image Camera Model
    @Column(name = "CAMERA_MODEL")
    private String cameraModel;

    // Image Date Taken
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_TAKEN")
    private Date dateTaken;

    // Image Exposure Time
    @Column(name = "EXPOSURE_TIME")
    private String exposureTime;

    // Image ISO Speed Ratings
    @Column(name = "ISO_SPEED_RATINGS")
    private int isoSpeedRatings;

    // Image Focal Length
    @Column(name = "FOCAL_LENGTH")
    private String focalLength;

    /**
     * Constructor
     */
    public ImageFileImpl() {
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
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getAbsoluteName() {
        return absoluteName;
    }

    @Override
    public void setAbsoluteName(String absoluteName) {
        this.absoluteName = absoluteName;
    }

    @Override
    public String getFileExtension() {
        return fileExtension;
    }

    @Override
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
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
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getColorDepth() {
        return colorDepth;
    }

    @Override
    public void setColorDepth(int colorDepth) {
        this.colorDepth = colorDepth;
    }

    @Override
    public String getColorType() {
        return colorType;
    }

    @Override
    public void setColorType(String colorType) {
        this.colorType = colorType;
    }

    @Override
    public String getCameraModel() {
        return cameraModel;
    }

    @Override
    public void setCameraModel(String cameraModel) {
        this.cameraModel = cameraModel;
    }

    @Override
    public Date getDateTaken() {
        return dateTaken;
    }

    @Override
    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    @Override
    public String getExposureTime() {
        return exposureTime;
    }

    @Override
    public void setExposureTime(String exposureTime) {
        this.exposureTime = exposureTime;
    }

    @Override
    public int getISOSpeedRatings() {
        return isoSpeedRatings;
    }

    @Override
    public void setISOSpeedRatings(int isoSpeedRatings) {
        this.isoSpeedRatings = isoSpeedRatings;
    }

    @Override
    public String getFocalLength() {
        return focalLength;
    }

    @Override
    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }
}
