package org.chii2.mediaserver.api.library;

import org.chii2.mediaserver.api.content.container.common.PicturesStorageFolderContainer;
import org.chii2.mediaserver.api.content.item.common.PhotoItem;
import org.teleal.common.util.MimeType;

import java.util.List;

/**
 * Library for Media Server
 */
public interface Library {
    // Pictures Storage Folder Container ID Prefix
    public final static String PICTURES_STORAGE_FOLDER_PREFIX = "PSFC-";
    // Photo Item ID Prefix
    public final static String PHOTO_ITEM_PREFIX = "PI-";

    /**
     * Get all Pictures Storage Folder Containers in library
     *
     * @return List of Pictures Storage Folder Container
     */
    public List<PicturesStorageFolderContainer> getPicturesStorageFolders();

    /**
     * Get photos by photo album
     *
     * @param album    Photo Album
     * @param parentId Parent Container ID
     * @return List of Photo Item
     */
    public List<PhotoItem> getPhotosByAlbum(String album, String parentId);

    /**
     * Get photo by photo id
     *
     * @param id ID
     * @return Photo Item
     */
    public PhotoItem getPhotoById(String id);

    /**
     * Get Container's Title from Container's ID. (By removing the container prefix)
     *
     * @param id Container ID
     * @return Container Title
     */
    public String getContainerTitle(String id);

    /**
     * Get Item Parent Container ID
     *
     * @param id Item ID
     * @return Parent Container ID
     */
    public String getItemParent(String id);

    /**
     * Get Item Library ID, which used to query item in library
     *
     * @param id Item ID
     * @return Item Library ID
     */
    public String getItemLibraryId(String id);

    /**
     * Forge Item ID
     *
     * @param id       Library ID
     * @param parentId Parent ID
     * @param prefix   Item Prefix
     * @return Item ID
     */
    public String forgeItemId(String id, String parentId, String prefix);

    /**
     * ID is Pictures Storage Folder Container
     *
     * @param id ID
     * @return True if id is Pictures Storage Folder Container
     */
    public boolean isPicturesStorageFolder(String id);

    /**
     * ID is Photo Item
     *
     * @param id ID
     * @return True if id is Photo Item
     */
    public boolean isPhotoItem(String id);

    /**
     * Get image MIME Type
     *
     * @param type Image Type (like jpeg)
     * @return MIME Type (like image/jpeg)
     */
    public MimeType getImageMimeType(String type);

    /**
     * Get Trancoded Image MIME Type
     *
     * @param type Image Type (like jpeg)
     * @return Trancoded Image MIME Type
     */
    public MimeType getImageTranscodeType(String type);

    /**
     * Get Image URL
     *
     * @param id Image ID
     * @return Image URL
     */
    public String getImageUrl(String id);
}
