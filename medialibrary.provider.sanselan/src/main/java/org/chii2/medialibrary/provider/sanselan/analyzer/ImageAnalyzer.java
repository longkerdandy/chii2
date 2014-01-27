package org.chii2.medialibrary.provider.sanselan.analyzer;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.RationalNumber;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.chii2.medialibrary.api.persistence.entity.ImageFile;
import org.chii2.medialibrary.api.persistence.factory.ImageFactory;
import org.chii2.medialibrary.api.provider.ImageFileInfoProviderService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Image Analyzer based on Sanselan
 */
public class ImageAnalyzer implements Runnable {
    // Flag
    public volatile boolean shouldStop = false;
    // Request queue
    private final BlockingQueue<Path> queue;
    // EventAdmin
    private final EventAdmin eventAdmin;
    // Image Factory
    private final ImageFactory imageFactory;
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.sanselan");

    /**
     * Constructor
     *
     * @param queue        Request Queue
     * @param eventAdmin   EventAdmin
     * @param imageFactory Image Factory
     */
    public ImageAnalyzer(BlockingQueue<Path> queue, EventAdmin eventAdmin, ImageFactory imageFactory) {
        this.queue = queue;
        this.eventAdmin = eventAdmin;
        this.imageFactory = imageFactory;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void run() {
        try {
            while (!this.shouldStop) {
                // Read request
                Path requestFile = this.queue.take();

                try {
                    // Analyze
                    ImageFile imageFile = this.analyzeImageFiles(requestFile, this.imageFactory);
                    if (imageFile != null) {
                        // Raise event
                        this.postImageInfoProvidedEvent(requestFile, imageFile);
                    } else {
                        // Raise fail event
                        this.postImageInfoFailedEvent(requestFile, String.format("Not valid image path"));
                    }
                } catch (Exception e) {
                    logger.error("Analyze image files with error: {}.", ExceptionUtils.getMessage(e));
                    // Raise fail event
                    this.postImageInfoFailedEvent(requestFile, ExceptionUtils.getMessage(e));
                }
            }
        } catch (InterruptedException e) {
            logger.error("Image Analyzer has been interrupted with error: {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Analyze Image Files
     *
     * @param path         Path
     * @param imageFactory Image Factory
     * @return ImageFile
     * @throws IOException        IO Exception when reading Files
     * @throws ImageReadException Exception from Sanselan when parsing Image metadata
     */
    private ImageFile analyzeImageFiles(Path path, ImageFactory imageFactory) throws IOException, ImageReadException {
        // TODO: Hidden or SymbolicLink
        if (path != null && Files.isRegularFile(path) && path.isAbsolute()) {
            // Create a new image path
            ImageFile imageFile = imageFactory.createImageFile();
            // Analyze
            imageFile.setFileName(path.getFileName().toString());
            imageFile.setFilePath(path.getParent().toString());
            imageFile.setAbsolutePath(path.toString());
            imageFile.setFileExtension(FilenameUtils.getExtension(path.getFileName().toString()));
            imageFile.setSize(Files.size(path));
            // Sanselan
            ImageInfo info = Sanselan.getImageInfo(path.toFile());
            imageFile.setType(info.getFormat().name);
            imageFile.setWidth(info.getWidth());
            imageFile.setHeight(info.getHeight());
            imageFile.setWidthDPI(info.getPhysicalWidthDpi());
            imageFile.setHeightDPI(info.getPhysicalHeightDpi());
            imageFile.setColorDepth(info.getBitsPerPixel());
            imageFile.setColorType(info.getColorTypeDescription());
            imageFile.setMimeType(info.getMimeType());
            imageFile.setUserComment(this.parseUserComment(info.getComments()));

            IImageMetadata metadata = Sanselan.getMetadata(path.toFile());
            if (metadata instanceof JpegImageMetadata) {
                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                imageFile.setCameraMaker(this.getJPEGMetadataStringValue(jpegMetadata, TiffConstants.EXIF_TAG_MAKE));
                imageFile.setCameraModel(this.getJPEGMetadataStringValue(jpegMetadata, TiffConstants.EXIF_TAG_MODEL));
                imageFile.setISO(this.getJPEGMetadataIntValue(jpegMetadata, TiffConstants.EXIF_TAG_ISO));
                imageFile.setExposureTime(this.parseRationalNumber(this.getJPEGMetadataValue(jpegMetadata, TiffConstants.EXIF_TAG_EXPOSURE_TIME)));
                imageFile.setFlash(this.getJPEGMetadataIntValue(jpegMetadata, TiffConstants.EXIF_TAG_FLASH));
                imageFile.setFNumber(this.parseRationalNumber(this.getJPEGMetadataValue(jpegMetadata, TiffConstants.EXIF_TAG_FNUMBER)));
                imageFile.setFocalLength(this.parseRationalNumber(this.getJPEGMetadataValue(jpegMetadata, TiffConstants.EXIF_TAG_FOCAL_LENGTH)));
                imageFile.setShutterSpeed(this.parseRationalNumber(this.getJPEGMetadataValue(jpegMetadata, TiffConstants.EXIF_TAG_SHUTTER_SPEED_VALUE)));
                imageFile.setDateTaken(this.parseDateTimeString(this.getJPEGMetadataStringValue(jpegMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL)));
            } else if (metadata instanceof TiffImageMetadata) {
                TiffImageMetadata tiffMetadata = (TiffImageMetadata) metadata;
                imageFile.setCameraMaker(this.getTiffMetadataStringValue(tiffMetadata, TiffConstants.EXIF_TAG_MAKE));
                imageFile.setCameraModel(this.getTiffMetadataStringValue(tiffMetadata, TiffConstants.EXIF_TAG_MODEL));
                imageFile.setISO(this.getTiffMetadataIntValue(tiffMetadata, TiffConstants.EXIF_TAG_ISO));
                imageFile.setExposureTime(this.parseRationalNumber(this.getTiffMetadataValue(tiffMetadata, TiffConstants.EXIF_TAG_EXPOSURE_TIME)));
                imageFile.setFlash(this.getTiffMetadataIntValue(tiffMetadata, TiffConstants.EXIF_TAG_FLASH));
                imageFile.setFNumber(this.parseRationalNumber(this.getTiffMetadataValue(tiffMetadata, TiffConstants.EXIF_TAG_FNUMBER)));
                imageFile.setFocalLength(this.parseRationalNumber(this.getTiffMetadataValue(tiffMetadata, TiffConstants.EXIF_TAG_FOCAL_LENGTH)));
                imageFile.setShutterSpeed(this.parseRationalNumber(this.getTiffMetadataValue(tiffMetadata, TiffConstants.EXIF_TAG_SHUTTER_SPEED_VALUE)));
                imageFile.setDateTaken(this.parseDateTimeString(this.getTiffMetadataStringValue(tiffMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL)));
            } else {
                imageFile.setDateTaken(new Date(Files.getLastModifiedTime(path).toMillis()));
            }
            // Result
            return imageFile;
        } else {
            return null;
        }
    }

    private Object getJPEGMetadataValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) throws ImageReadException {
        if (jpegMetadata != null) {
            TiffField field = jpegMetadata.findEXIFValue(tagInfo);
            if (field != null) {
                return field.getValue();
            }
        }
        return null;
    }

    private String getJPEGMetadataStringValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) throws ImageReadException {
        if (jpegMetadata != null) {
            TiffField field = jpegMetadata.findEXIFValue(tagInfo);
            if (field != null) {
                String value = field.getStringValue();
                if (value != null) {
                    while (value.endsWith("\u0000")) {
                        value = value.substring(0, value.length() - 1);
                    }
                }
                return value;
            }
        }
        return null;
    }

    private int getJPEGMetadataIntValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) throws ImageReadException {
        if (jpegMetadata != null) {
            TiffField field = jpegMetadata.findEXIFValue(tagInfo);
            if (field != null) {
                return field.getIntValue();
            }
        }
        return 0;
    }

    private Object getTiffMetadataValue(TiffImageMetadata tiffMetadata, TagInfo tagInfo) throws ImageReadException {
        if (tiffMetadata != null) {
            TiffField field = tiffMetadata.findField(tagInfo);
            if (field != null) {
                return field.getValue();
            }
        }
        return null;
    }

    private String getTiffMetadataStringValue(TiffImageMetadata tiffMetadata, TagInfo tagInfo) throws ImageReadException {
        if (tiffMetadata != null) {
            TiffField field = tiffMetadata.findField(tagInfo);
            if (field != null) {
                String value = field.getStringValue();
                if (value != null) {
                    while (value.endsWith("\u0000")) {
                        value = value.substring(0, value.length() - 1);
                    }
                }
                return value;
            }
        }
        return null;
    }

    private int getTiffMetadataIntValue(TiffImageMetadata tiffMetadata, TagInfo tagInfo) throws ImageReadException {
        if (tiffMetadata != null) {
            TiffField field = tiffMetadata.findField(tagInfo);
            if (field != null) {
                return field.getIntValue();
            }
        }
        return 0;
    }

    /**
     * Parse List of User Comment into One Comment String
     *
     * @param comments User Comments
     * @return Comment
     */
    @SuppressWarnings("unchecked")
    private String parseUserComment(List comments) {
        StringBuilder sb = new StringBuilder();
        if (comments != null) {
            for (String comment : (List<String>) comments) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(comment);
            }
        }
        return sb.toString();
    }

    /**
     * Parse Rational Number to String like "numerator/divisor"
     *
     * @param value Rational Number
     * @return String
     */
    private String parseRationalNumber(Object value) {
        if (value instanceof RationalNumber) {
            if (((RationalNumber) value).isValid()) {
                return ((RationalNumber) value).numerator + "/" + ((RationalNumber) value).divisor;
            }
        }
        return null;
    }

    /**
     * Parse Date Time String to Date Object
     *
     * @param date Date Time String
     * @return Date
     */
    private Date parseDateTimeString(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }

        try {
            return DateUtils.parseDate(date, new String[]{"yyyy:MM:dd HH:mm:ss"});
        } catch (ParseException e) {
            logger.warn("Parse image EXIF date field with error: {}.", ExceptionUtils.getMessage(e));
            return null;
        }
    }

    /**
     * Raise a image file information provided event
     *
     * @param path      Path
     * @param imageFile ImageFile Information
     */
    private void postImageInfoProvidedEvent(Path path, ImageFile imageFile) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(ImageFileInfoProviderService.IMAGE_PATH_PROPERTY, path);
        properties.put(ImageFileInfoProviderService.IMAGE_FILE_INFO_PROPERTY, imageFile);
        // Send a event
        Event event = new Event(ImageFileInfoProviderService.IMAGE_FILE_INFO_PROVIDED_TOPIC, properties);
        logger.debug("Send a image file information provided event: {}.", path);
        this.eventAdmin.postEvent(event);
    }

    /**
     * Raise a image information failed event
     *
     * @param path        Path
     * @param failMessage Fail Message
     */
    private void postImageInfoFailedEvent(Path path, String failMessage) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(ImageFileInfoProviderService.IMAGE_PATH_PROPERTY, path);
        properties.put(ImageFileInfoProviderService.FAIL_MESSAGE_PROPERTY, failMessage);
        // Send a event
        Event event = new Event(ImageFileInfoProviderService.IMAGE_FILE_INFO_FAILED_TOPIC, properties);
        logger.debug("Send a image information failed event: {}.", path);
        this.eventAdmin.postEvent(event);
    }
}
