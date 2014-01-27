package org.chii2.medialibrary.api.provider;

import java.nio.file.Path;
import java.util.List;

/**
 * Image Information Provider Service
 */
public interface ImageFileInfoProviderService {
    // Event Topic for image file information request
    // This Event should contains a IMAGE_PATH_PROPERTY, which is a File/Path
    public final static String IMAGE_FILE_INFO_REQUEST_TOPIC = "org/chii2/medialibrary/provider/image/FILE_INFO_REQUEST";
    // Event Topic for image file information provided
    // This Event should contains a IMAGE_FILE_INFO_PROPERTY, which is a ImageFile
    public final static String IMAGE_FILE_INFO_PROVIDED_TOPIC = "org/chii2/medialibrary/provider/image/FILE_INFO_PROVIDED";
    // Event Topic for image file information failed (with parse or fetch)
    public final static String IMAGE_FILE_INFO_FAILED_TOPIC = "org/chii2/medialibrary/provider/image/FILE_INFO_FAILED";
    // Image file property key in the image file info REQUEST & PROVIDED & FAIL events, this should be the file list from the request
    public final static String IMAGE_PATH_PROPERTY = "image_path";
    // Image information property key in the image file info PROVIDED events, this should be the image information result from provider
    public final static String IMAGE_FILE_INFO_PROPERTY = "image_file_info";
    // Fail reason property key in the image file info FAILED events
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
    public void getImageInformation(Path imageFile);

    /**
     * Get Image Information.
     * This should be done in a asynchronous way, through Event Admin
     *
     * @param imageFiles Image File List
     */
    public void getImageInformation(List<Path> imageFiles);
}
