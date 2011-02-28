package org.chii2.mediaserver.content.common;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.content.common.Item.PhotoItem;
import org.chii2.mediaserver.content.common.container.PicturesContainer;
import org.chii2.mediaserver.content.common.container.*;
import org.chii2.mediaserver.content.common.container.PicturesStorageFolderContainer;
import org.chii2.mediaserver.content.common.container.PicturesFoldersContainer;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.transcoder.api.core.TranscoderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.container.Container;
import org.teleal.common.util.MimeType;

import java.util.*;

/**
 * Content Manger for common clients
 */
public class CommonContentManager implements ContentManager {
    // Root Container ID
    public static String ROOT_ID = "0";
    // Pictures Container
    public static String PICTURES_ID = "3";
    // Pictures Folders Container
    public static String PICTURES_FOLDERS_ID = "16";
    // Pictures Storage Folder Container ID Prefix
    public static String PICTURES_STORAGE_FOLDER_PREFIX = "PSFC-";
    // Photo Item ID Prefix
    public static String PHOTO_ITEM_PREFIX = "PI-";
    // Media Library
    protected MediaLibraryService mediaLibrary;
    // HTTP Server
    protected HttpServerService httpServer;
    // Transcoder
    protected TranscoderService transcoder;
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
     */
    public CommonContentManager(MediaLibraryService mediaLibrary, HttpServerService httpServer, TranscoderService transcoder) {
        this.mediaLibrary = mediaLibrary;
        this.httpServer = httpServer;
        this.transcoder = transcoder;
    }

    @Override
    public String getClientProfile() {
        return TranscoderService.PROFILE_COMMON;
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
            VisualContainer container = new RootContainer();
            container.loadContents(startIndex, requestCount, orderBy, this);
            return (Container) container;
        }
        // Pictures Container
        else if (isPicturesContainer(objectId)) {
            VisualContainer container = new PicturesContainer();
            container.loadContents(startIndex, requestCount, orderBy, this);
            return (Container) container;
        }
        // Pictures Folders Container
        else if (isPicturesFoldersContainer(objectId)) {
            VisualContainer container = new PicturesFoldersContainer();
            container.loadContents(startIndex, requestCount, orderBy, this);
            return (Container) container;
        }
        // Pictures Storage Folder Container (Dynamic)
        else if (isPicturesStorageFolderContainer(objectId)) {
            VisualContainer container = new PicturesStorageFolderContainer(objectId, getContainerTitle(objectId));
            container.loadContents(startIndex, requestCount, orderBy, this);
            return (Container) container;
        }
        // Photo Item
        else if (isPhotoItem(objectId)) {
            return getPhotoById(objectId);
        }
        // Invalid
        else {
            // TODO Maybe should throw a NO_SUCH_OBJECT exception, instead of null result
            return null;
        }
    }

    @Override
    public List<PicturesStorageFolderContainer> getPicturesStorageFolders(long startIndex, long maxCount, SortCriterion[] orderBy) {
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
            albums = mediaLibrary.getAllImageAlbums((int) startIndex, (int) maxCount, sorts);
        } catch (IllegalArgumentException e) {
            albums = mediaLibrary.getAllImageAlbums();
        }

        // Add to result
        if (albums != null) {
            for (String album : albums) {
                if (StringUtils.isNotEmpty(album)) {
                    String id = forgeContainerId(album, PICTURES_STORAGE_FOLDER_PREFIX);
                    containers.add(new PicturesStorageFolderContainer(id, album));
                }
            }
            return containers;
        } else {
            return null;
        }
    }

    @Override
    public long getPicturesStorageFoldersCount() {
        return mediaLibrary.getImageAlbumsCount();
    }

    @Override
    public List<PhotoItem> getPhotosByAlbum(String album, String parentId, long startIndex, long maxCount, SortCriterion[] orderBy) {
        // Forge sort
        Map<String, String> sorts = new HashMap<String, String>();
        for (SortCriterion sort : orderBy) {
            String field = null;
            if ("dc:title".equalsIgnoreCase(sort.getPropertyName())) {
                field = "title";
            } else if ("dc:date".equalsIgnoreCase(sort.getPropertyName())) {
                field = "date_taken";
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
        try {
            images = mediaLibrary.getImagesByAlbum(album, (int) startIndex, (int) maxCount, sorts);
        } catch (IllegalArgumentException e) {
            images = mediaLibrary.getImagesByAlbum(album);
        }
        // Results
        List<PhotoItem> photos = new ArrayList<PhotoItem>();
        // Create photo item and add to results
        for (Image image : images) {
            if (image != null) {
                String id = forgeItemId(image.getId(), parentId, PHOTO_ITEM_PREFIX);
                String url = httpServer.forgeImageUrl(getClientProfile(), image.getId());
                String profile = getClientProfile();
                MimeType mime = new MimeType("image", transcoder.getImageTranscodedType(profile, image.getType()));
                if (StringUtils.isNotEmpty(id)) {
                    photos.add(new PhotoItem(id, parentId, image.getTitle(), image.getDateTaken(), image.getAlbum(), null, null, url, mime, image.getWidth(), image.getHeight(), image.getColorDepth(), image.getSize()));
                }
            }
        }
        return photos;
    }

    @Override
    public long getPhotosCountByAlbum(String album) {
        return mediaLibrary.getImagesCountByAlbum(album);
    }

    @Override
    public PhotoItem getPhotoById(String id) {
        String libraryId = getItemLibraryId(id);
        Image image = mediaLibrary.getImageById(libraryId);
        if (image != null) {
            String parentId = getItemParentId(id);
            String url = httpServer.forgeImageUrl(getClientProfile(), image.getId());
            String profile = getClientProfile();
            MimeType mime = new MimeType("image", transcoder.getImageTranscodedType(profile, image.getType()));
            return new PhotoItem(id, parentId, image.getTitle(), image.getDateTaken(), image.getAlbum(), null, null, url, mime, image.getWidth(), image.getHeight(), image.getColorDepth(), image.getSize());
        } else {
            return null;
        }
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
        if (id != null && id.indexOf('-') > 0) {
            String subId = id.substring(id.indexOf('-') + 1);
            if (subId.length() > uuidLength + 1) {
                return subId.substring(subId.length() - uuidLength);
            }
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
        return id != null && id.length() > 4 && id.substring(0, 5).equalsIgnoreCase(PICTURES_STORAGE_FOLDER_PREFIX);
    }

    @Override
    public boolean isPhotoItem(String id) {
        return id != null && id.length() > 3 && id.substring(0, 3).equalsIgnoreCase(PHOTO_ITEM_PREFIX);
    }
}
