package org.chii2.transcoder.api.core;

import java.io.File;
import java.util.List;

/**
 * Transcoder Server act as a main entrance for all kinds of transcoding engines.
 */
public interface TranscoderService {

    // Common Profile
    public final static String PROFILE_COMMON = "common";
    // XBox Profile
    public final static String PROFILE_XBOX = "xbox";
    // Image GIF Type
    public final static String IMAGE_TYPE_GIF = "gif";
    // Image JPEG Type
    public final static String IMAGE_TYPE_JPEG = "jpeg";
    // Image PNG Type
    public final static String IMAGE_TYPE_PNG = "png";
    // Image SVG Type
    public final static String IMAGE_TYPE_SVG = "svg+xml";
    // Image TIFF Type TODO should I use tiff-fx
    public final static String IMAGE_TYPE_TIFF = "tiff";
    // Image GIF Type
    public final static String IMAGE_MIME_GIF = "image/gif";
    // Image JPEG Type
    public final static String IMAGE_MIME_JPEG = "image/jpeg";
    // Image PNG Type
    public final static String IMAGE_MIME_PNG = "image/png";
    // Image SVG Type
    public final static String IMAGE_MIME_SVG = "image/svg+xml";
    // Image TIFF Type TODO should I use image/tiff-fx
    public final static String IMAGE_MIME_TIFF = "image/tiff";

    /**
     * Get Client Profile based on user agent information
     *
     * @param userAgent User Agent
     * @return Client Profile
     */
    public String getClientProfile(String userAgent);

    /**
     * Get Client Profile based on user agent information
     *
     * @param userAgent List of User Agent
     * @return Client Profile
     */
    public String getClientProfile(List<String> userAgent);

    /**
     * Get Transcoded Type for image on specific client
     *
     * @param client    Client Profile
     * @param imageType Original Image Type
     * @return Transcoded Type
     */
    public String getImageTranscodedType(String client, String imageType);

    /**
     * Get Transcoded Image MIME Type on specific client
     *
     * @param client    Client Profile
     * @param imageType Original Image Type
     * @return Transcoded MIME Type
     */
    public String getImageTranscodedMime(String client, String imageType);

    /**
     * Get Transcoded Image File on specific client
     *
     * @param client    Client Profile
     * @param imageType Original Image Type
     * @param imageFile Original Image File
     * @return Transcoded Image File
     */
    public File getImageTranscodedFile(String client, String imageType, File imageFile);
}
