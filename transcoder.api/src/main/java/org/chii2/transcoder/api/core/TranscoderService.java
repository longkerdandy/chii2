package org.chii2.transcoder.api.core;

import org.chii2.mediaserver.api.dlna.DLNAProfile;

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
    // Video MPEG Type
    public final static String VIDEO_TYPE_MPEG = "mpeg";
    // Video MPEG4 Type
    public final static String VIDEO_TYPE_MP4 = "mp4";
    // Video Ogg Type
    public final static String VIDEO_TYPE_OGG = "ogg";
    // Video WebM Type
    public final static String VIDEO_TYPE_WEBM = "webm";
    // Video QuickTime Type
    public final static String VIDEO_TYPE_QUICKTIME = "quicktime";
    // Video ASF Type
    public final static String VIDEO_TYPE_ASF = "asf";
    // Video Microsoft Video (AVI?) Type
    public final static String VIDEO_TYPE_MSVIDEO = "msvideo";
    // Video Windows Media Video Type
    public final static String VIDEO_TYPE_WMV = "wmv";
    // Video MPEG Mime
    public final static String VIDEO_MIME_MPEG = "video/mpeg";
    // Video MPEG4 Mime
    public final static String VIDEO_MIME_MP4 = "video/mp4";
    // Video Ogg Mime
    public final static String VIDEO_MIME_OGG = "video/ogg";
    // Video WebM Mime
    public final static String VIDEO_MIME_WEBM = "video/webm";
    // Video QuickTime Mime
    public final static String VIDEO_MIME_QUICKTIME = "video/quicktime";
    // Video ASF Mime
    public final static String VIDEO_MIME_ASF = "video/x-ms-asf";
    // Video Microsoft Video (AVI?) Mime
    public final static String VIDEO_MIME_MSVIDEO = "video/x-msvideo";
    // Video Windows Media Video Mime
    public final static String VIDEO_MIME_WMV = "video/x-ms-wmv";
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
    // Image GIF Mime
    public final static String IMAGE_MIME_GIF = "image/gif";
    // Image JPEG Mime
    public final static String IMAGE_MIME_JPEG = "image/jpeg";
    // Image PNG Mime
    public final static String IMAGE_MIME_PNG = "image/png";
    // Image SVG Mime
    public final static String IMAGE_MIME_SVG = "image/svg+xml";
    // Image TIFF Mime TODO should I use image/tiff-fx
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
     * Whether DLNA profile is valid for the client
     *
     * @param client  Client
     * @param profile DLNA Profile
     * @return True if valid
     */
    public boolean isValidProfile(String client, DLNAProfile.Profile profile);

    /**
     * Get Transcoded DLNA Profile
     *
     * @param client             Client
     * @param videoCodec         Video Codec
     * @param videoBitRate       Video BitRate
     * @param videoWidth         Video Width
     * @param videoHeight        Video Height
     * @param fps                Video FPS
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Sample BitRate
     * @param audioChannels      Channels Count
     * @return DLNA Profile
     */
    public DLNAProfile.Profile getTranscodedProfile(String client, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels);

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
