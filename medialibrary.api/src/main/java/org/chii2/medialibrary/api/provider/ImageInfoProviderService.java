package org.chii2.medialibrary.api.provider;

import java.io.File;
import java.util.List;

/**
 * Image Information Provider Service
 */
public interface ImageInfoProviderService {

    // Event Topic for image information request
    // This Event should contains a IMAGE_FILE_PROPERTY, which is a List of File
    public final static String IMAGE_INFO_REQUEST_TOPIC = "org/chii2/medialibrary/provider/image/INFO_REQUEST";
    // Event Topic for image information provided
    // This Event should contains a IMAGE_INFO_PROPERTY, which is a List of Image
    public final static String IMAGE_INFO_PROVIDED_TOPIC = "org/chii2/medialibrary/provider/image/INFO_PROVIDED";
    // Event Topic for image information failed (with parse or fetch)
    public final static String IMAGE_INFO_FAILED_TOPIC = "org/chii2/medialibrary/provider/image/INFO_FAILED";
    // Image file property key in the image info REQUEST & PROVIDED & FAIL events, this should be the file list from the request
    public final static String IMAGE_FILE_PROPERTY = "image_file";
    // Image information property key in the image info PROVIDED events, this should be the image information result from provider
    public final static String IMAGE_INFO_PROPERTY = "image_info";
    // Fail reason property key in the image info FAILED events
    public final static String FAIL_MESSAGE_PROPERTY = "fail_msg";

    /**
     * Get the provider name
     *
     * @return Provider name
     */
    public String getProviderName();

    /**
     * Get Image Information.
     * This should be done in a asynchronous way, through Event Admin
     *
     * @param imageFile Image File
     */
    public void getImageInformation(File imageFile);

    /**
     * Get Image Information.
     * This should be done in a asynchronous way, through Event Admin
     *
     * @param imageFiles Image File List
     */
    public void getImageInformation(List<File> imageFiles);
}
