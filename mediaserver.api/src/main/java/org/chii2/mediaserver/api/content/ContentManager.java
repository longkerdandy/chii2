package org.chii2.mediaserver.api.content;

import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.content.item.VisualPictureItem;
import org.chii2.mediaserver.api.content.item.VisualVideoItem;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;

import java.util.List;

/**
 * Content Manager used for manage media content.
 * Content Directory Service use this to provide content based on different client.
 */
public interface ContentManager {
    /**
     * Get Content Manager target Client Profile
     *
     * @return Client Profile
     */
    public String getClientProfile();

    /**
     * Get DIDL Parser for current Content Manager
     *
     * @return DIDL Parser
     */
    public DIDLParser getParser();

    /**
     * Whether this Content Manager match client heads.
     * This method used for choose appropriate Content Manager.
     *
     * @param headers UPnP Headers (Http Headers)
     * @return True if client match the Content Manager
     */
    public boolean isMatch(UpnpHeaders headers);

    /**
     * Find Object (Container or Item) based on ID
     *
     * @param objectId     Object ID
     * @param filter       Filter
     * @param startIndex   Start Index
     * @param requestCount Request (Max) Count
     * @param orderBy      Sort Method
     * @return Object (Container or Item), null if not found
     */
    public DIDLObject findObject(String objectId, String filter, long startIndex, long requestCount, SortCriterion[] orderBy);

    /**
     * Get all Pictures Storage Folder Containers in library
     *
     * @param filter     Content Filter
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     * @return List of Pictures Storage Folder Container
     */
    public List<? extends VisualContainer> getPicturesStorageFolders(String filter, long startIndex, long maxCount, SortCriterion[] orderBy);

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
     * @param filter     Content Filter
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     * @return List of Photo Item
     */
    public List<? extends VisualPictureItem> getPhotosByAlbum(String album, String parentId, String filter, long startIndex, long maxCount, SortCriterion[] orderBy);

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
     * @param id     ID
     * @param filter Content Filter
     * @return Photo Item
     */
    public VisualPictureItem getPhotoById(String id, String filter);

    /**
     * Get movies
     *
     * @param filter     Content Filer
     * @param parentId   Parent Container ID
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     * @return List of Movie Item
     */
    public List<? extends VisualVideoItem> getMovies(String filter, String parentId, long startIndex, long maxCount, SortCriterion[] orderBy);

    /**
     * Get movies count
     *
     * @return Count
     */
    public long getMoviesCount();

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
    public String getItemParentId(String id);

    /**
     * Get Item Library ID, which used to query item in library
     *
     * @param id Item ID
     * @return Item Library ID
     */
    public String getItemLibraryId(String id);

    /**
     * Forge Item ID from Library ID
     *
     * @param id       Library ID
     * @param parentId Parent ID
     * @param prefix   Item Prefix
     * @return Item ID
     */
    public String forgeItemId(String id, String parentId, String prefix);

    /**
     * Forge Container ID from its Title
     *
     * @param title  Container Title
     * @param prefix Container Prefix
     * @return Container ID
     */
    public String forgeContainerId(String title, String prefix);

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
     * ID is Video Container
     *
     * @param id ID
     * @return True if ID is Video Container
     */
    public boolean isVideoContainer(String id);

    /**
     * ID is Video Folders Container
     *
     * @param id ID
     * @return True if ID is Video Folders Container
     */
    public boolean isVideoFoldersContainer(String id);

    /**
     * ID is Movie Base Storage Folder Container
     *
     * @param id ID
     * @return True if ID is Movie Base Storage Folder Container
     */
    public boolean isMovieBaseStorageFolderContainer(String id);

    /**
     * ID is Online Video related Container
     *
     * @param id ID
     * @return True if ID is Online Video related Container
     */
    public boolean isOnlineVideoContainer(String id);
}
