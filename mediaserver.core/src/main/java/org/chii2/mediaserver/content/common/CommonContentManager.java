package org.chii2.mediaserver.content.common;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.content.item.VisualItem;
import org.chii2.mediaserver.content.common.Item.MovieItem;
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
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.*;
import org.teleal.cling.support.model.container.Container;
import org.teleal.common.util.MimeType;

import java.net.URI;
import java.util.*;

/**
 * Content Manger for common clients
 */
public class CommonContentManager implements ContentManager {
    // Root Container ID
    public static String ROOT_ID = "0";
    // Video Container
    public static String VIDEO_ID = "2";
    // Pictures Container
    public static String PICTURES_ID = "3";
    // Video Folders Container
    public static String VIDEO_FOLDERS_ID = "15";
    // Pictures Folders Container
    public static String PICTURES_FOLDERS_ID = "16";
    // Movie Storage Folders Container as a Base Container
    public static String MOVIE_BASE_STORAGE_FOLDER_ID = "1501";
    // Pictures Storage Folder Container ID Prefix
    public static String PICTURES_STORAGE_FOLDER_PREFIX = "PSFC-";
    // Photo Item ID Prefix
    public static String PHOTO_ITEM_PREFIX = "PI-";
    // Movie Item ID Prefix
    public static String MOVIE_ITEM_PREFIX = "MI-";
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
            return (Container) container;
        }

        /** ------------------------- Pictures -------------------------**/
        // Pictures Container
        else if (isPicturesContainer(objectId)) {
            VisualContainer container = new PicturesContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return (Container) container;
        }
        // Pictures Folders Container
        else if (isPicturesFoldersContainer(objectId)) {
            VisualContainer container = new PicturesFoldersContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return (Container) container;
        }
        // Pictures Storage Folder Container (Dynamic)
        else if (isPicturesStorageFolderContainer(objectId)) {
            VisualContainer container = new PicturesStorageFolderContainer(filter, objectId, getContainerTitle(objectId));
            container.loadContents(startIndex, requestCount, orderBy, this);
            return (Container) container;
        }
        // Photo Item
        else if (isPhotoItem(objectId)) {
            return getPhotoById(objectId, filter);
        }

        /** ------------------------- Video -------------------------**/
        // Video Container
        else if (isVideoContainer(objectId)) {
            VisualContainer container = new VideoContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return (Container) container;
        }
        // Video Folders Container
        else if (isVideoFoldersContainer(objectId)) {
            VisualContainer container = new VideoFoldersContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return (Container) container;
        }
        // Movie Base Storage Folder Container
        else if (isMovieBaseStorageFolderContainer(objectId)) {
            VisualContainer container = new MovieBaseStorageFolderContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return (Container) container;
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
            albums = mediaLibrary.getImageAlbums((int) startIndex, (int) maxCount, sorts);
        } catch (IllegalArgumentException e) {
            albums = mediaLibrary.getImageAlbums();
        }

        // Add to result
        if (albums != null) {
            for (String album : albums) {
                if (StringUtils.isNotEmpty(album)) {
                    String id = forgeContainerId(album, PICTURES_STORAGE_FOLDER_PREFIX);
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
        return mediaLibrary.getImageAlbumsCount();
    }

    @Override
    public List<PhotoItem> getPhotosByAlbum(String album, String parentId, String filter, long startIndex, long maxCount, SortCriterion[] orderBy) {
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
                // Resources
                List<Res> resources = new ArrayList<Res>();
                Res resource = new Res();
                resource.setValue(url);
                resource.setProtocolInfo(new ProtocolInfo(mime));
                if (filter.contains("res@resolution")) {
                    resource.setResolution(image.getWidth(), image.getHeight());
                }
                if (filter.contains("res@colorDepth")) {
                    // TODO Only adjust True Color here, may need more condition
                    if ("TrueColor".equalsIgnoreCase(image.getColorType())) {
                        resource.setColorDepth((long) 24);
                    } else {
                        resource.setColorDepth((long) image.getColorDepth());
                    }
                }
                if (filter.contains("res@size")) {
                    resource.setSize(image.getSize());
                }
                resources.add(resource);

                if (StringUtils.isNotEmpty(id)) {
                    photos.add(new PhotoItem(filter, id, parentId, image.getTitle(), image.getDateTaken(), image.getAlbum(), null, null, resources));
                }
            }
        }
        return photos;
    }

    @Override
    public List<? extends VisualItem> getMovies(String parentId, String filter, long startIndex, long maxCount, SortCriterion[] orderBy) {
        List<MovieItem> movies = new ArrayList<MovieItem>();
        String id = forgeItemId(UUID.randomUUID().toString(), parentId, MOVIE_ITEM_PREFIX);
        String title = "Wildlife";
        URI uri = httpServer.forgeMovieUrl(getClientProfile(), id);
        String[] genres = {"Unknown Genre"};
        PersonWithRole[] actors = {new PersonWithRole("Unknown Actor")};
        // Resources
        List<Res> resources = new ArrayList<Res>();
        Res resource = new Res();
        resource.setValue(uri.toString());
        resource.setProtocolInfo(new ProtocolInfo(Protocol.HTTP_GET, "*", "video/x-ms-wmv", "DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01500000000000000000000000000000"));
        if (filter.contains("res@resolution")) {
            resource.setResolution(1280, 720);
        }
        if (filter.contains("res@duration")) {
            resource.setDuration("0:00:30.000");
        }
        if (filter.contains("res@colorDepth")) {
            resource.setColorDepth((long) 24);
        }
        if (filter.contains("res@size")) {
            resource.setSize((long) 26246026);
        }
        if (filter.contains("res@bitrate")) {
            resource.setBitrate((long) 766771);
        }
        if (filter.contains("res@nrAudioChannels")) {
            resource.setNrAudioChannels((long) 2);
        }
        if (filter.contains("res@sampleFrequency ")) {
            resource.setSampleFrequency((long) 44100);
        }
        if (filter.contains("res@bitsPerSample")) {
            resource.setBitsPerSample((long) 201);
        }
        resources.add(resource);

        movies.add(new MovieItem(filter, id, parentId, title, null, null, genres, null, null, null, actors, null, null, null, resources));
        return movies;
    }

    @Override
    public long getPhotosCountByAlbum(String album) {
        return mediaLibrary.getImagesCountByAlbum(album);
    }

    @Override
    public PhotoItem getPhotoById(String id, String filter) {
        String libraryId = getItemLibraryId(id);
        Image image = mediaLibrary.getImageById(libraryId);
        if (image != null) {
            String parentId = getItemParentId(id);
            String url = httpServer.forgeImageUrl(getClientProfile(), image.getId());
            String profile = getClientProfile();
            MimeType mime = new MimeType("image", transcoder.getImageTranscodedType(profile, image.getType()));
            // Resources
            List<Res> resources = new ArrayList<Res>();
            Res resource = new Res();
            resource.setValue(url);
            resource.setProtocolInfo(new ProtocolInfo(mime));
            if (filter.contains("res@resolution")) {
                resource.setResolution(image.getWidth(), image.getHeight());
            }
            if (filter.contains("res@colorDepth")) {
                // TODO Only adjust True Color here, may need more condition
                if ("TrueColor".equalsIgnoreCase(image.getColorType())) {
                    resource.setColorDepth((long) 24);
                } else {
                    resource.setColorDepth((long) image.getColorDepth());
                }
            }
            if (filter.contains("res@size")) {
                resource.setSize(image.getSize());
            }
            resources.add(resource);

            return new PhotoItem(filter, id, parentId, image.getTitle(), image.getDateTaken(), image.getAlbum(), null, null, resources);
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

}
