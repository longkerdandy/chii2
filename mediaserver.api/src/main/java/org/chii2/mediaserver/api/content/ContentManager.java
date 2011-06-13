package org.chii2.mediaserver.api.content;

import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.content.item.VisualPictureItem;
import org.chii2.mediaserver.api.content.item.VisualVideoItem;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.dlna.DLNAProfiles;

import java.net.URI;
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
     * Get UPnP/DLNA Resource for Item
     *
     * @return Resource
     */
    public Res getResource();

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
     * Get pictures by album
     *
     * @param album      Picture Album
     * @param parentId   Parent Container ID
     * @param filter     Content Filter
     * @param startIndex Start Index
     * @param maxCount   Max Results Count
     * @param orderBy    Sort Criterion
     * @return List of Photo Item
     */
    public List<? extends VisualPictureItem> getPicturesByAlbum(String album, String parentId, String filter, long startIndex, long maxCount, SortCriterion[] orderBy);

    /**
     * Get total pictures count by album
     *
     * @param album Album
     * @return Count
     */
    public long getPicturesCountByAlbum(String album);

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

    /**
     * Forge Online Video URL
     * This likely to be a replacement for http host, and thus will use HTTP Server as proxy
     *
     * @param providerName Online Video Provider Name
     * @param url          Real Online Video URL
     * @return Proxy Online Video URL
     */
    public URI forgetOnlineVideoUrl(String providerName, String url);

    /**
     * Get Video DLNA Profile
     *
     * @param container          Container
     * @param videoFormat        Video Format
     * @param videoFormatProfile Video Format Profile
     * @param videoFormatVersion Video Format Version
     * @param videoCodec         Video Codec
     * @param videoBitRate       BitRate
     * @param videoWidth         Video Width
     * @param videoHeight        Video Height
     * @param fps                Video FPS
     * @param audioFormat        Audio Format
     * @param audioFormatProfile Audio Format Profile
     * @param audioFormatVersion Audio Format Version
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Audio SampleBitRate
     * @param audioChannels      Audio Channels
     * @return Video DLNA Profile
     */
    public DLNAProfiles getVideoTranscodedProfile(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps,
                                                  String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels);

    /**
     * Get Video MIME
     *
     * @param container          Container
     * @param videoFormat        Video Format
     * @param videoFormatProfile Video Format Profile
     * @param videoFormatVersion Video Format Version
     * @param videoCodec         Video Codec
     * @param videoBitRate       BitRate
     * @param videoWidth         Video Width
     * @param videoHeight        Video Height
     * @param fps                Video FPS
     * @param audioFormat        Audio Format
     * @param audioFormatProfile Audio Format Profile
     * @param audioFormatVersion Audio Format Version
     * @param audioCodec         Audio Codec
     * @param audioBitRate       Audio BitRate
     * @param audioSampleBitRate Audio SampleBitRate
     * @param audioChannels      Audio Channels
     * @return MIME
     */
    public String getVideoTranscodedMime(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps,
                                         String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels);
}
