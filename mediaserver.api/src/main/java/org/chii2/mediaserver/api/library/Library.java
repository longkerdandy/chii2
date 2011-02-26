package org.chii2.mediaserver.api.library;

import org.chii2.mediaserver.api.content.container.common.PicturesStorageFolderContainer;
import org.chii2.mediaserver.api.content.item.common.PhotoItem;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.common.util.MimeType;

import java.util.List;

/**
 * Library for Media Server
 */
public interface Library {
    // Root Container ID
    public final static String ROOT_ID = "0";
    // Pictures Container
    public final static String PICTURES_ID = "3";
    // Pictures Folders Container
    public final static String PICTURES_FOLDERS_ID = "16";
    // Pictures Storage Folder Container ID Prefix
    public final static String PICTURES_STORAGE_FOLDER_PREFIX = "PSFC-";
    // Photo Item ID Prefix
    public final static String PHOTO_ITEM_PREFIX = "PI-";

    /**
     * Get all Pictures Storage Folder Containers in library
     *
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     * @return List of Pictures Storage Folder Container
     */
    public List<PicturesStorageFolderContainer> getPicturesStorageFolders(long startIndex, long maxCount, SortCriterion[] orderBy);

    /**
     * Get the total Pictures Storage Folder Containers count in library
     *
     * @return Count
     */
    public long getPicturesStorageFoldersCount();

    /**
     * Get photos by photo album
     *
     * @param album      Photo Album
     * @param parentId   Parent Container ID
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     * @return List of Photo Item
     */
    public List<PhotoItem> getPhotosByAlbum(String album, String parentId, long startIndex, long maxCount, SortCriterion[] orderBy);

    /**
     * Get total photos count by photo album
     *
     * @param album Photo Album
     * @return Count
     */
    public long getPhotosCountByAlbum(String album);

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
     * ID is Root Container
     *
     * @param id ID
     * @return True if id is Root Container
     */
    public boolean isRootContainer(String id);

    /**
     * ID is Pictures Container
     *
     * @param id ID
     * @return True if id is Pictures Container
     */
    public boolean isPicturesContainer(String id);

    /**
     * ID is Pictures Folders Container
     *
     * @param id ID
     * @return True if id is Pictures Folders Container
     */
    public boolean isPicturesFoldersContainer(String id);

    /**
     * ID is Pictures Storage Folder Container
     *
     * @param id ID
     * @return True if id is Pictures Storage Folder Container
     */
    public boolean isPicturesStorageFolderContainer(String id);

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
     * Get Transcoded Image MIME Type
     *
     * @param type Image Type (like jpeg)
     * @return Transcoded Image MIME Type
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
