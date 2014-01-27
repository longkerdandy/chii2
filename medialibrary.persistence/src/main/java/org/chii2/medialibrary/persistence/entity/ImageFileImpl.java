package org.chii2.medialibrary.persistence.entity;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.ImageFile;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Represent a image file on the disk
 */
@Entity(name = "IMAGE_FILE")
@Table(name = "IMAGE_FILE")
public class ImageFileImpl implements ImageFile {

    // ID (Primary Key) in UUID format
    @Id
    @Column(name = "ID")
    private String id;

    // File Name
    @Column(name = "FILE_NAME")
    private String fileName;

    // File Parent Path
    @Column(name = "FILE_PATH")
    private String filePath;

    // Absolute File Path
    @Column(name = "ABSOLUTE_PATH")
    private String absolutePath;

    // File Extension
    @Column(name = "FILE_EXTENSION")
    private String fileExtension;

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

    // Image Width DPI
    @Column(name = "WIDTH_DPI")
    private int widthDPI;

    // Image Height DPI
    @Column(name = "HEIGHT_DPI")
    private int heightDPI;

    // Image Color Depth
    @Column(name = "COLOR_DEPTH")
    private int colorDepth;

    // Image Color Type
    @Column(name = "COLOR_TYPE")
    private String colorType;

    // Image MIME Type
    @Column(name = "MIME_TYPE")
    private String mimeType;

    // Image Camera Company
    @Column(name = "CAMERA_MAKER")
    private String cameraMaker;

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

    // Image ISO
    @Column(name = "ISO")
    private int iso;

    // Image Focal Length
    @Column(name = "FOCAL_LENGTH")
    private String focalLength;

    // Image FNumber
    @Column(name = "F_NUMBER")
    private String fNumber;

    // Image Shutter Speed
    @Column(name = "SHUTTER_SPEED")
    private String shutterSpeed;

    // Image Flash
    @Column(name = "FLASH")
    private int flash;

    // Image User Comment
    // TODO: User should be able to modify the comment, and changes should be saved to physical file
    @Column(name = "USER_COMMENT")
    private String userComment;

    // Image Reference
    @OneToOne(fetch = FetchType.LAZY)
    private ImageImpl image;

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
        if (!filePath.endsWith(SystemUtils.FILE_SEPARATOR)) {
            filePath = filePath + SystemUtils.FILE_SEPARATOR;
        }
        this.filePath = FilenameUtils.separatorsToUnix(filePath);
    }

    @Override
    public String getAbsolutePath() {
        return absolutePath;
    }

    @Override
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = FilenameUtils.separatorsToUnix(absolutePath);
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
    public void setHeight(int height) {
        this.height = height;
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
    public int getISO() {
        return iso;
    }

    @Override
    public void setISO(int iso) {
        this.iso = iso;
    }

    @Override
    public String getFocalLength() {
        return focalLength;
    }

    @Override
    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    @Override
    public String getUserComment() {
        return userComment;
    }

    @Override
    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    @Override
    public int getWidthDPI() {
        return widthDPI;
    }

    @Override
    public void setWidthDPI(int widthDPI) {
        this.widthDPI = widthDPI;
    }

    @Override
    public int getHeightDPI() {
        return heightDPI;
    }

    @Override
    public void setHeightDPI(int heightDPI) {
        this.heightDPI = heightDPI;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String getCameraMaker() {
        return cameraMaker;
    }

    @Override
    public void setCameraMaker(String cameraMaker) {
        this.cameraMaker = cameraMaker;
    }

    @Override
    public String getFNumber() {
        return fNumber;
    }

    @Override
    public void setFNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    @Override
    public String getShutterSpeed() {
        return shutterSpeed;
    }

    @Override
    public void setShutterSpeed(String shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    @Override
    public int getFlash() {
        return flash;
    }

    @Override
    public void setFlash(int flash) {
        this.flash = flash;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void setImage(Image image) {
        if (image.getClass() == ImageImpl.class) {
            this.image = (ImageImpl) image;
        }
    }
}
