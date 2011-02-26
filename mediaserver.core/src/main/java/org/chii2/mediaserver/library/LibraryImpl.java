package org.chii2.mediaserver.library;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.mediaserver.api.content.container.common.PicturesStorageFolderContainer;
import org.chii2.mediaserver.api.content.item.common.PhotoItem;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.mediaserver.api.library.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.common.util.MimeType;

import java.util.*;

/**
 * Library Service for Chii2 Media Server, which using Chii2 Media Library as backend
 */
public class LibraryImpl implements Library {

    // Chii2 Media Library
    private MediaLibraryService mediaLibrary;
    // HTTP Server
    private HttpServerService httpServer;
    // UUID Length
    private int uuidLength = UUID.randomUUID().toString().length();
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.library");

    /**
     * Constructor
     *
     * @param mediaLibrary Media Library Service
     * @param httpServer   HTTP Server
     */
    public LibraryImpl(MediaLibraryService mediaLibrary, HttpServerService httpServer) {
        this.mediaLibrary = mediaLibrary;
        this.httpServer = httpServer;
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
                    containers.add(new PicturesStorageFolderContainer("PSFC-" + album, album, this));
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
                if (StringUtils.isNotEmpty(id)) {
                    photos.add(new PhotoItem(id, parentId, image.getTitle(), image.getDateTaken(), image.getAlbum(), null, null, getImageUrl(getItemLibraryId(id)), getImageMimeType(image.getType()), image.getWidth(), image.getHeight(), image.getColorDepth(), image.getSize()));
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
            return new PhotoItem(id, getItemParent(id), image.getTitle(), image.getDateTaken(), image.getAlbum(), null, null, getImageUrl(libraryId), getImageMimeType(image.getType()), image.getWidth(), image.getHeight(), image.getColorDepth(), image.getSize());
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
    public String getItemParent(String id) {
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
    public String forgeItemId(String id, String parentId, String prefix) {
        if (id != null && id.length() == uuidLength) {
            return prefix + parentId + "-" + id;
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

    @Override
    public MimeType getImageMimeType(String type) {
        if ("jpeg".equalsIgnoreCase(type) || "jpg".equalsIgnoreCase(type)) {
            return new MimeType("image", "jpeg");
        } else if ("gif".equalsIgnoreCase(type) || "giff".equalsIgnoreCase(type)) {
            return new MimeType("image", "gif");
        } else if ("gif".equalsIgnoreCase(type) || "pngf".equalsIgnoreCase(type)) {
            return new MimeType("image", "png");
        } else {
            return getImageTranscodeType(type);
        }
    }

    @Override
    public MimeType getImageTranscodeType(String type) {
        return new MimeType("image", "jpeg");
    }

    @Override
    public String getImageUrl(String id) {
        String url = "http://" + httpServer.getHost().getHostAddress() + ":" + httpServer.getPort() + "/image/" + id;
        logger.info("Forge image url: <{}>.", url);
        return url;
    }
}
