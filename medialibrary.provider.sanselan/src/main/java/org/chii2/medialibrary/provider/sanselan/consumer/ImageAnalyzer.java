package org.chii2.medialibrary.provider.sanselan.consumer;

import org.apache.commons.io.FilenameUtils;
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
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.ImageFile;
import org.chii2.medialibrary.api.persistence.factory.ImageFactory;
import org.chii2.medialibrary.api.provider.ImageInfoProviderService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Image Analyzer based on Sanselan
 */
public class ImageAnalyzer implements Runnable {
    // Request queue
    private BlockingQueue<List<File>> queue;
    // EventAdmin
    private EventAdmin eventAdmin;
    // Image Factory
    private ImageFactory imageFactory;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.sanselan");

    public ImageAnalyzer(BlockingQueue<List<File>> queue, EventAdmin eventAdmin, ImageFactory imageFactory) {
        this.queue = queue;
        this.eventAdmin = eventAdmin;
        this.imageFactory = imageFactory;
    }

    @SuppressWarnings({"InfiniteLoopStatement"})
    @Override
    public void run() {
        try {
            while (true) {
                // Read request
                List<File> requestFiles = this.queue.take();

                try {
                    // Analyze
                    List<Image> images = this.analyzeImageFiles(requestFiles, this.imageFactory);
                    // Raise event
                    this.postImageInfoProvidedEvent(requestFiles, images);
                } catch (Exception e) {
                    logger.error("Analyze image files with error: {}.", ExceptionUtils.getMessage(e));
                    // Raise fail event
                    this.postImageInfoFailedEvent(requestFiles, ExceptionUtils.getMessage(e));
                }
            }
        } catch (InterruptedException e) {
            logger.error("Image Analyzer has been interrupted with error: {}.", ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Analyze Image Files
     *
     * @param imageFiles   Image Files
     * @param imageFactory Image Factory
     * @return Image List
     * @throws IOException        IO Exception when reading Files
     * @throws ImageReadException Exception from Sanselan when parsing Image metadata
     */
    private List<Image> analyzeImageFiles(List<File> imageFiles, ImageFactory imageFactory) throws IOException, ImageReadException {
        List<Image> images = new ArrayList<Image>();

        for (File file : imageFiles) {
            if (file != null && file.isFile() && file.exists()) {
                // Create a new image
                Image image = imageFactory.createImage();
                ImageFile imageFile = imageFactory.createImageFile();
                image.setFile(imageFile);
                // Analyze
                imageFile.setFileName(file.getName());
                imageFile.setFilePath(file.getParent());
                imageFile.setAbsolutePath(file.getAbsolutePath());
                imageFile.setFileExtension(FilenameUtils.getExtension(file.getName()));
                imageFile.setSize(file.length());
                image.setAlbum(file.getParentFile().getName());
                image.setTitle(FilenameUtils.removeExtension(file.getName()));
                // Sanselan
                ImageInfo info = Sanselan.getImageInfo(file);
                imageFile.setType(info.getFormat().name);
                imageFile.setWidth(info.getWidth());
                imageFile.setHeight(info.getHeight());
                imageFile.setWidthDPI(info.getPhysicalWidthDpi());
                imageFile.setHeightDPI(info.getPhysicalHeightDpi());
                imageFile.setColorDepth(info.getBitsPerPixel());
                imageFile.setColorType(info.getColorTypeDescription());
                imageFile.setMimeType(info.getMimeType());
                imageFile.setUserComment(this.parseUserComment(info.getComments()));

                IImageMetadata metadata = Sanselan.getMetadata(file);
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
                    imageFile.setDateTaken(new Date(file.lastModified()));
                }

                // Add to results
                images.add(image);
            }
        }

        return images;
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
    private String parseUserComment(List<String> comments) {
        StringBuilder sb = new StringBuilder();
        if (comments != null) {
            for (String comment : comments) {
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
        try {
            return DateUtils.parseDate(date, new String[]{"yyyy:MM:dd HH:mm:ss"});
        } catch (ParseException e) {
            logger.warn("Parse image EXIF date field with error: {}.", ExceptionUtils.getMessage(e));
            return null;
        }
    }

    /**
     * Raise a image information provided event
     *
     * @param files  List of Image Files (from request)
     * @param images Image Information
     */
    private void postImageInfoProvidedEvent(List<File> files, List<Image> images) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(ImageInfoProviderService.IMAGE_FILE_PROPERTY, files);
        properties.put(ImageInfoProviderService.IMAGE_INFO_PROPERTY, images);
        // Send a event
        Event event = new Event(ImageInfoProviderService.IMAGE_INFO_PROVIDED_TOPIC, properties);
        logger.debug("Send a images information provided event.");
        eventAdmin.postEvent(event);
    }

    /**
     * Raise a image information failed event
     *
     * @param files       List of Image Files (from request)
     * @param failMessage Fail Message
     */
    private void postImageInfoFailedEvent(List<File> files, String failMessage) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(ImageInfoProviderService.IMAGE_FILE_PROPERTY, files);
        properties.put(ImageInfoProviderService.FAIL_MESSAGE_PROPERTY, failMessage);
        // Send a event
        Event event = new Event(ImageInfoProviderService.IMAGE_INFO_FAILED_TOPIC, properties);
        logger.debug("Send a images information failed event.");
        eventAdmin.postEvent(event);
    }
}
