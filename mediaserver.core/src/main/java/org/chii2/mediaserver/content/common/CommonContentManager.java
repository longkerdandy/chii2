package org.chii2.mediaserver.content.common;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.content.item.VisualPictureItem;
import org.chii2.mediaserver.api.content.item.VisualVideoItem;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.mediaserver.api.provider.OnlineVideoProviderService;
import org.chii2.mediaserver.content.common.Item.PhotoItem;
import org.chii2.mediaserver.content.common.Item.PictureItem;
import org.chii2.mediaserver.content.common.container.*;
import org.chii2.transcoder.api.core.TranscoderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.*;
import org.teleal.cling.support.model.dlna.*;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Content Manger for common clients
 */
public class CommonContentManager implements ContentManager {
    // Root Container ID
    public final static String ROOT_ID = "0";
    // Video Container
    public final static String VIDEO_ID = "2";
    // Pictures Container
    public final static String PICTURES_ID = "3";
    // Video Folders Container
    public final static String VIDEO_FOLDERS_ID = "15";
    // Pictures Folders Container
    public final static String PICTURES_FOLDERS_ID = "16";
    // Movie Storage Folders Container as a Base Container
    public final static String MOVIE_BASE_STORAGE_FOLDER_ID = "1501";
    // Pictures Storage Folder Container ID Prefix
    public final static String PICTURES_STORAGE_FOLDER_PREFIX = "PSFC-";
    // Picture Item ID Prefix
    public final static String PICTURE_ITEM_PREFIX = "PICI-";
    // Photo Item ID Prefix
    public final static String PHOTO_ITEM_PREFIX = "PI-";
    // Movie Item ID Prefix
    public final static String MOVIE_ITEM_PREFIX = "MI-";
    // Media Library
    protected MediaLibraryService mediaLibrary;
    // HTTP Server
    protected HttpServerService httpServer;
    // Transcoder
    protected TranscoderService transcoder;
    // Online Videos
    protected List<OnlineVideoProviderService> onlineVideos;
    // Logger
    protected Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.content");
    // UUID Length
    private int uuidLength = UUID.randomUUID().toString().length();

    /**
     * Constructor
     *
     * @param mediaLibrary Media Library
     * @param httpServer   Http Server
     * @param transcoder   Transcoder
     * @param onlineVideos Online Video Providers
     */
    public CommonContentManager(MediaLibraryService mediaLibrary, HttpServerService httpServer, TranscoderService transcoder, List<OnlineVideoProviderService> onlineVideos) {
        this.mediaLibrary = mediaLibrary;
        this.httpServer = httpServer;
        this.transcoder = transcoder;
        this.onlineVideos = onlineVideos;
    }

    @Override
    public String getClientProfile() {
        return TranscoderService.PROFILE_COMMON;
    }

    @Override
    public Res getResource() {
        return new Res();
    }

    @Override
    public DIDLParser getParser() {
        return new DIDLParser();
    }

    @Override
    public boolean isMatch(UpnpHeaders headers) {
        // Always return true
        return true;
    }

    @Override
    public DIDLObject findObject(String objectId, String filter, long startIndex, long requestCount, SortCriterion[] orderBy) {
        // Root Container
        if (isRootContainer(objectId)) {
            VisualContainer container = new RootContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return container;
        }

        /** ------------------------- Pictures -------------------------**/
        // Pictures Container
        else if (isPicturesContainer(objectId)) {
            VisualContainer container = new PicturesContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return container;
        }
        // Pictures Folders Container
        else if (isPicturesFoldersContainer(objectId)) {
            VisualContainer container = new PicturesFoldersContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return container;
        }
        // Pictures Storage Folder Container (Dynamic)
        else if (isPicturesStorageFolderContainer(objectId)) {
            VisualContainer container = new PicturesStorageFolderContainer(filter, objectId, getContainerTitle(objectId));
            container.loadContents(startIndex, requestCount, orderBy, this);
            return container;
        }

        /** ------------------------- Video -------------------------**/
        // Video Container
        else if (isVideoContainer(objectId)) {
            VisualContainer container = new VideoContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return container;
        }
        // Video Folders Container
        else if (isVideoFoldersContainer(objectId)) {
            VisualContainer container = new VideoFoldersContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return container;
        }
        // Movie Base Storage Folder Container
        else if (isMovieBaseStorageFolderContainer(objectId)) {
            VisualContainer container = new MovieBaseStorageFolderContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return container;
        }

        // Invalid
        else {
            // TODO Maybe should throw a NO_SUCH_OBJECT exception, instead of null result
            return null;
        }
    }

    @Override
    public List<PicturesStorageFolderContainer> getPicturesStorageFolders(String filter, long startIndex, long maxCount, SortCriterion[] orderBy) {
        // Forge sort
        Map<String, String> sorts = new HashMap<String, String>();
        for (SortCriterion sort : orderBy) {
            String field = null;
            if ("dc:title".equalsIgnoreCase(sort.getPropertyName())) {
                field = "album";
            }
            if (field != null) {
                if (sort.isAscending()) {
                    sorts.put(field, "asc");
                } else {
                    sorts.put(field, "desc");
                }
            }
        }
        // Result
        List<PicturesStorageFolderContainer> containers = new ArrayList<PicturesStorageFolderContainer>();
        // Get image albums from Chii2 Media Library
        List<String> albums;
        try {
            albums = this.mediaLibrary.getImageAlbums((int) startIndex, (int) maxCount, sorts);
        } catch (IllegalArgumentException e) {
            albums = this.mediaLibrary.getImageAlbums(-1, -1, null);
        }

        // Add to result
        if (albums != null) {
            for (String album : albums) {
                if (StringUtils.isNotEmpty(album)) {
                    String id = this.forgeContainerId(album, PICTURES_STORAGE_FOLDER_PREFIX);
                    containers.add(new PicturesStorageFolderContainer(filter, id, album));
                }
            }
            return containers;
        } else {
            return null;
        }
    }

    @Override
    public long getPicturesStorageFoldersCount() {
        return this.mediaLibrary.getImageAlbumsCount();
    }

    @Override
    public List<VisualPictureItem> getPicturesByAlbum(String album, String parentId, String filter, long startIndex, long maxCount, SortCriterion[] orderBy) {
        // Forge sort
        Map<String, String> sorts = new HashMap<String, String>();
        for (SortCriterion sort : orderBy) {
            String field = null;
            if ("dc:title".equalsIgnoreCase(sort.getPropertyName())) {
                field = "title";
            } else if ("dc:date".equalsIgnoreCase(sort.getPropertyName())) {
                field = "file.date_taken";
            }
            if (field != null) {
                if (sort.isAscending()) {
                    sorts.put(field, "asc");
                } else {
                    sorts.put(field, "desc");
                }
            }
        }
        // Get images from library
        List<? extends Image> images;

        int start = -1;
        int max = -1;
        try {
            start = (int) startIndex;
            max = (int) maxCount;
        } catch (Exception ignore) {
        }
        images = this.mediaLibrary.getImagesByField("album", album, true, start, max, sorts);

        // Results
        List<VisualPictureItem> pictures = new ArrayList<VisualPictureItem>();
        // Create picture item and add to results
        for (Image image : images) {
            // Item
            VisualPictureItem pictureItem;
            // ID from library
            String libraryId = image.getId();
            // ID
            String id;
            // Title
            String title = image.getTitle();
            // Photo?
            if (image.isPhoto()) {
                id = forgeItemId(libraryId, parentId, PHOTO_ITEM_PREFIX);
                pictureItem = new PhotoItem(filter, id, parentId, title, image.getAlbum());
            } else {
                id = forgeItemId(libraryId, parentId, PICTURE_ITEM_PREFIX);
                pictureItem = new PictureItem(filter, id, parentId, title, image.getAlbum());
            }

            //Picture Date
            Date date = image.getDateTaken();
            if (filter.contains("dc:date") && date != null) {
                pictureItem.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
            }
            String comment = image.getUserComment();
            // Description
            if (filter.contains("dc:description") && StringUtils.isNotBlank(comment)) {
                pictureItem.setDescription(comment);
            }
            // Long Description
            if (filter.contains("upnp:longDescription") && StringUtils.isNotBlank(comment)) {
                pictureItem.setLongDescription(comment);
            }
            // Rating
            float rating = image.getRating();
            if (filter.contains("upnp:rating") && rating > 0) {
                pictureItem.setRating(Float.toString(rating));
            }

            // Resource
            if (this.transcoder.isValidImage(this.getClientProfile(), image.getType(), image.getWidth(), image.getHeight())) {
                // Resource
                Res originalResource = this.getResource();
                // URL
                URI originalUri = this.httpServer.forgeUrl("image", getClientProfile(), false, libraryId);
                originalResource.setValue(originalUri.toString());
                // Profile
                DLNAProfiles originalProfile = this.transcoder.getImageTranscodedProfile(this.getClientProfile(), image.getType(), image.getWidth(), image.getHeight());
                // MIME
                String mime = this.transcoder.getImageTranscodedMime(this.getClientProfile(), image.getType(), image.getWidth(), image.getHeight());
                // This should not happens
                if (StringUtils.isBlank(mime)) {
                    mime = image.getMimeType();
                    logger.warn("Can't determine image MIME type, use {} from file information.", mime);
                }
                // DLNA Attribute
                EnumMap<DLNAAttribute.Type, DLNAAttribute> dlnaAttributes = new EnumMap<DLNAAttribute.Type, DLNAAttribute>(DLNAAttribute.Type.class);
                if (originalProfile != null && originalProfile != DLNAProfiles.NONE) {
                    dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute(originalProfile));
                }
                dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_OP, new DLNAOperationsAttribute(DLNAOperations.RANGE));
                dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_FLAGS, new DLNAFlagsAttribute(DLNAFlags.STREAMING_TRANSFER_MODE, DLNAFlags.BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_V15));
                originalResource.setProtocolInfo(new DLNAProtocolInfo(Protocol.HTTP_GET, ProtocolInfo.WILDCARD, mime, dlnaAttributes));
                // Resolution
                if (filter.contains("res@resolution")) {
                    originalResource.setResolution(image.getWidth(), image.getHeight());
                }
                // Color Depth
                if (filter.contains("res@colorDepth")) {
                    originalResource.setColorDepth((long) image.getColorDepth());
                }
                // Size
                if (filter.contains("res@size")) {
                    originalResource.setSize(image.getSize());
                }
                // Add Resource to item
                pictureItem.addResource(originalResource);
            } else {
                // TODO: Transcoded Image Handling
            }
            // Add to results
            pictures.add(pictureItem);
        }
        return pictures;
    }

    @Override
    public long getPicturesCountByAlbum(String album) {
        return mediaLibrary.getImagesCountByAlbum(album);
    }

    @Override
    public List<? extends VisualVideoItem> getMovies(String parentId, String filter, long startIndex, long maxCount, SortCriterion[] orderBy) {
        // TODO: finished this later
        return null;
    }

    @Override
    public long getMoviesCount() {
        return mediaLibrary.getMoviesCount();
    }

    @Override
    public String getContainerTitle(String id) {
        if (id != null && id.indexOf('-') > 0) {
            return id.substring(id.indexOf('-') + 1);
        } else {
            return id;
        }
    }

    @Override
    public String getItemParentId(String id) {
        if (id != null && id.indexOf('-') > 0) {
            String subId = id.substring(id.indexOf('-') + 1);
            if (subId.length() > uuidLength + 1) {
                return subId.substring(0, subId.length() - uuidLength - 1);
            }
        }

        return null;
    }

    @Override
    public String getItemLibraryId(String id) {
        if (id.length() > uuidLength + 1) {
            return id.substring(id.length() - uuidLength);
        }

        return null;
    }

    @Override
    public String forgeItemId(String libraryId, String parentId, String prefix) {
        if (libraryId != null && libraryId.length() == uuidLength) {
            return prefix + parentId + "-" + libraryId;
        } else {
            return null;
        }
    }

    @Override
    public String forgeContainerId(String title, String prefix) {
        if (StringUtils.isNotBlank(title)) {
            return prefix + title;
        } else {
            return null;
        }
    }

    @Override
    public boolean isRootContainer(String id) {
        return ROOT_ID.equalsIgnoreCase(id);
    }

    @Override
    public boolean isPicturesContainer(String id) {
        return PICTURES_ID.equalsIgnoreCase(id);
    }

    @Override
    public boolean isPicturesFoldersContainer(String id) {
        return PICTURES_FOLDERS_ID.equalsIgnoreCase(id);
    }

    @Override
    public boolean isPicturesStorageFolderContainer(String id) {
        return id != null && id.length() > 5 && id.substring(0, 5).equalsIgnoreCase(PICTURES_STORAGE_FOLDER_PREFIX);
    }

    @Override
    public boolean isVideoContainer(String id) {
        return VIDEO_ID.equalsIgnoreCase(id);
    }

    @Override
    public boolean isVideoFoldersContainer(String id) {
        return VIDEO_FOLDERS_ID.equalsIgnoreCase(id);
    }

    @Override
    public boolean isMovieBaseStorageFolderContainer(String id) {
        return MOVIE_BASE_STORAGE_FOLDER_ID.equalsIgnoreCase(id);
    }

    @Override
    public boolean isOnlineVideoContainer(String id) {
        return id != null && id.length() > 4 && id.substring(0, 4).equalsIgnoreCase(OnlineVideoProviderService.ONLINE_VIDEO_CONTAINER_PREFIX);
    }

    @Override
    public URI forgetOnlineVideoUrl(String providerName, String url) {
        OnlineVideoProviderService onlineVideo = this.getOnlineVideoProvider(providerName);
        if (onlineVideo != null) {
            url = url.replace(onlineVideo.getVideoHostUrl(), "");
            return httpServer.forgeUrl("onlinevideo", providerName, this.getClientProfile(), true, url);
        } else {
            return null;
        }
    }

    @Override
    public DLNAProfiles getVideoTranscodedProfile(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
        return transcoder.getVideoTranscodedProfile(getClientProfile(), container, videoFormat, videoFormatProfile, videoFormatVersion, videoCodec, videoBitRate, videoWidth, videoHeight, fps, audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, audioBitRate, audioSampleBitRate, audioChannels);
    }

    @Override
    public String getVideoTranscodedMime(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
        return transcoder.getVideoTranscodedMime(getClientProfile(), container, videoFormat, videoFormatProfile, videoFormatVersion, videoCodec, videoBitRate, videoWidth, videoHeight, fps, audioFormat, audioFormatProfile, audioFormatVersion, audioCodec, audioBitRate, audioSampleBitRate, audioChannels);
    }

    /**
     * Get Online Video Provider by Name
     *
     * @param providerName Provider Name
     * @return Online Video Provider
     */
    protected OnlineVideoProviderService getOnlineVideoProvider(String providerName) {
        for (OnlineVideoProviderService onlineVideo : onlineVideos) {
            if (onlineVideo.getProviderName().equalsIgnoreCase(providerName)) {
                return onlineVideo;
            }
        }
        return null;
    }

    /**
     * Get Online Video Container from Provider
     *
     * @param filter Filter
     * @param id     Object ID
     * @return Online Video Container
     */
    protected VisualContainer getOnlineVideoContainer(String filter, String id) {
        for (OnlineVideoProviderService onlineVideo : onlineVideos) {
            if (onlineVideo.isMatch(id)) {
                return onlineVideo.getContainerByID(filter, id);
            }
        }
        return null;
    }
}
