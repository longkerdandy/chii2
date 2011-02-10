package org.chii2.medialibrary.provider.graphicsmagick;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.ImageFile;
import org.chii2.medialibrary.api.persistence.factory.ImageFactory;
import org.chii2.medialibrary.api.provider.ImageInfoProviderService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Image Metadata Analyzer based on GraphicsMagick
 */
public class ImageAnalyzer implements Runnable {

    // Directories to be scanned
    private List<File> files;
    // EventAdmin
    private EventAdmin eventAdmin;
    // Image Factory
    private ImageFactory imageFactory;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.graphicsmagick");

    /**
     * Constructor
     *
     * @param files        Image Files
     * @param eventAdmin   Event Admin
     * @param imageFactory Image Factory
     */
    public ImageAnalyzer(List<File> files, EventAdmin eventAdmin, ImageFactory imageFactory) {
        this.files = files;
        this.eventAdmin = eventAdmin;
        this.imageFactory = imageFactory;
    }

    @Override
    public void run() {
        // Prepare commands
        List<String> command = new ArrayList<String>();
        command.add("gm");
        command.add("-identify");
        command.add("-format");
        command.add("%i::%f::%e::%m::%b::%w::%h::%q::%r::%[EXIF:Model]::%[EXIF:DateTime]::%[EXIF:ExposureTime]::%[EXIF:ISOSpeedRatings]::%[EXIF:FocalLength]\\n");

        // Add image files
        for (File file : files) {
            command.add(file.getAbsolutePath());
        }

        try {
            // Result
            List<Image> images = new ArrayList<Image>();
            // Invoke GraphicMagick via command line interface
            Process process = new ProcessBuilder(command).start();
            BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = output.readLine()) != null) {
                String[] result = line.split("::");
                if (result.length == 14) {
                    Image image = imageFactory.createImage();
                    ImageFile imageFile = imageFactory.createImageFile();
                    // Parse and fill image file information
                    imageFile.setAbsoluteName(getString(result[0]));
                    imageFile.setFileName(getString(result[1]));
                    imageFile.setFileExtension(getString(result[2]));
                    imageFile.setType(getString(result[3]));
                    imageFile.setSize(getLong(result[4]));
                    imageFile.setWidth(getInt(result[5]));
                    imageFile.setHeight(getInt(result[6]));
                    imageFile.setColorDepth(getInt(result[7]));
                    imageFile.setColorType(getString(result[8]));
                    imageFile.setCameraModel(getString(result[9]));
                    imageFile.setDateTaken(getDate(result[10]));
                    imageFile.setExposureTime(getString(result[11]));
                    imageFile.setISOSpeedRatings(getInt(result[12]));
                    imageFile.setFocalLength(getString(result[13]));
                    imageFile.setFilePath(getFilePath(result[0]));
                    imageFile.setTitle(getFileTitle(result[1]));
                    // Add file
                    image.setFile(imageFile);
                    // Add to result
                    images.add(image);
                }
            }

            // The process should be done now, but wait to be sure.
            // If current thread is interrupted, following actions will not be invoked
            process.waitFor();

            // raise event
            raiseImageInfoProvidedEvent(images);
        } catch (IOException e) {
            logger.error("A IO Exception occurred when parsing image information with GraphicMagick:{}.", e.getMessage());
            raiseImageInfoFailedEvent();
        } catch (InterruptedException e) {
            logger.debug("Thread has been interrupted, action canceled. <{}>", e.getMessage());
        }
    }

    /**
     * Raise a image information provided event
     *
     * @param images Image Information
     */
    private void raiseImageInfoProvidedEvent(List<Image> images) {
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
     */
    private void raiseImageInfoFailedEvent() {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(ImageInfoProviderService.IMAGE_FILE_PROPERTY, files);
        properties.put(ImageInfoProviderService.IMAGE_INFO_PROPERTY, new ArrayList<Image>());
        // Send a event
        Event event = new Event(ImageInfoProviderService.IMAGE_INFO_FAILED_TOPIC, properties);
        logger.debug("Send a images information failed event.");
        eventAdmin.postEvent(event);
    }

    /**
     * Convert the GraphicMagick output String to Image File information String
     *
     * @param s Input String
     * @return Converted String
     */
    private String getString(String s) {
        String result = StringUtils.trim(s);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else if ("unknown".equalsIgnoreCase(result)) {
            return null;
        } else {
            return result;
        }
    }

    /**
     * Convert the GraphicMagick output String to Image File information String
     *
     * @param s Input String
     * @return Converted Long
     */
    private long getLong(String s) {
        String result = StringUtils.trim(s);
        if (StringUtils.isEmpty(result)) {
            return 0;
        } else if ("unknown".equalsIgnoreCase(result)) {
            return 0;
        } else {
            try {
                return Long.parseLong(result);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

    /**
     * Convert the GraphicMagick output String to Image File information String
     *
     * @param s Input String
     * @return Converted int
     */
    private int getInt(String s) {
        String result = StringUtils.trim(s);
        if (StringUtils.isEmpty(result)) {
            return 0;
        } else if ("unknown".equalsIgnoreCase(result)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(result);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

    /**
     * Convert the GraphicMagick output String to Image File information String
     *
     * @param s Input String
     * @return Converted Date
     */
    private Date getDate(String s) {
        String result = StringUtils.trim(s);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else if ("unknown".equalsIgnoreCase(result)) {
            return null;
        } else {
            try {
                return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(result);
            } catch (ParseException e) {
                return null;
            }
        }
    }

    /**
     * Convert the GraphicMagick output String to Image File information String
     *
     * @param absoluteName Absolute file name
     * @return File path
     */
    private String getFilePath(String absoluteName) {
        File file = new File(absoluteName);
        return file.getParent();
    }

    /**
     * Convert the GraphicMagick output String to Image File information String
     *
     * @param fileName File name
     * @return Title
     */
    private String getFileTitle(String fileName) {
        if (fileName != null) {
            int index = fileName.lastIndexOf('.');
            if (index >= 0) {
                return fileName.substring(0, index);
            } else {
                return fileName;
            }
        } else {
            return "";
        }
    }
}
