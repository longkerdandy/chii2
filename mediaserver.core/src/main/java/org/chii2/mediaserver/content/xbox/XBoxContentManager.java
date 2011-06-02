package org.chii2.mediaserver.content.xbox;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.content.item.VisualVideoItem;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.mediaserver.api.provider.OnlineVideoProviderService;
import org.chii2.mediaserver.content.common.CommonContentManager;
import org.chii2.mediaserver.content.common.Item.MovieItem;
import org.chii2.mediaserver.content.common.container.*;
import org.chii2.mediaserver.content.xbox.container.XBoxVideoFoldersContainer;
import org.chii2.transcoder.api.core.TranscoderService;
import org.chii2.util.EncodingUtils;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.*;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.dlna.*;
import org.teleal.cling.support.model.item.Item;

import java.net.URI;
import java.util.*;

/**
 * Content Manger for XBox360
 */
public class XBoxContentManager extends CommonContentManager {

    /**
     * Constructor
     *
     * @param mediaLibrary Media Library
     * @param httpServer   Http Server
     * @param transcoder   Transcoder
     * @param onlineVideos Online Video Providers
     */
    public XBoxContentManager(MediaLibraryService mediaLibrary, HttpServerService httpServer, TranscoderService transcoder, List<OnlineVideoProviderService> onlineVideos) {
        super(mediaLibrary, httpServer, transcoder, onlineVideos);
    }

    @Override
    public String getClientProfile() {
        return TranscoderService.PROFILE_XBOX;
    }

    @Override
    public DIDLParser getParser() {
        return new XBoxDIDLParser();
    }

    @Override
    public boolean isMatch(UpnpHeaders headers) {
        if (headers != null) {
            // If user agent contains XBox, is should be a XBox client
            List<String> userAgent = headers.get("User-agent");
            if (getClientProfile().equalsIgnoreCase(transcoder.getClientProfile(userAgent))) {
                return true;
            }
        }
        return false;
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
        // Photo Item
        else if (isPhotoItem(objectId)) {
            return getPhotoById(objectId, filter);
        }

        /** ------------------------- Video -------------------------**/
        // Video Container
        else if (isVideoContainer(objectId)) {
            VisualContainer container = new VideoContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            return container;
        }
        // Video Folders Container
        // For XBox360, this is the root video folder in dashboard
        else if (isVideoFoldersContainer(objectId)) {
            VisualContainer container = new XBoxVideoFoldersContainer(filter, this.onlineVideos);
            container.loadContents(startIndex, requestCount, orderBy, this);
            convertVideoContainerS2T(container);
            return container;
        }
        // Movie Base Storage Folder Container
        else if (isMovieBaseStorageFolderContainer(objectId)) {
            VisualContainer container = new MovieBaseStorageFolderContainer(filter);
            container.loadContents(startIndex, requestCount, orderBy, this);
            convertVideoContainerS2T(container);
            return container;
        }
        // Online Video Container
        else if (isOnlineVideoContainer(objectId)) {
            VisualContainer container = getOnlineVideoContainer(filter, objectId);
            if (container != null) {
                container.loadContents(startIndex, requestCount, orderBy, this);
                // If container is online video provider's Root Container, set its parent to Video Folders Container
                convertVideoContainerS2T(container);
                if (container.getParentID().equalsIgnoreCase("-1")) {
                    container.setParentID("15");
                }
            }
            return container;
        }
        // Invalid
        else {
            // TODO Maybe should throw a NO_SUCH_OBJECT exception, instead of null result
            return null;
        }
    }

    @Override
    public List<? extends VisualVideoItem> getMovies(String parentId, String filter, long startIndex, long maxCount, SortCriterion[] orderBy) {
        // Forge sort TODO: May need more filed in the future
        Map<String, String> sorts = new HashMap<String, String>();
        for (SortCriterion sort : orderBy) {
            String field = null;
            if ("dc:title".equalsIgnoreCase(sort.getPropertyName())) {
                field = "info.name";
            }
            if (field != null) {
                if (sort.isAscending()) {
                    sorts.put(field, "asc");
                } else {
                    sorts.put(field, "desc");
                }
            }
        }
        // Get movies from media library
        List<? extends Movie> movies;
        try {
            movies = mediaLibrary.getMovies((int) startIndex, (int) maxCount, sorts);
        } catch (IllegalArgumentException e) {
            movies = mediaLibrary.getMovies();
        }

        // Create and fill movie information
        List<MovieItem> movieItems = new ArrayList<MovieItem>();
        for (Movie movie : movies) {

            // ID from library
            String libraryId = movie.getId();
            // Item ID
            String itemId = forgeItemId(libraryId, parentId, MOVIE_ITEM_PREFIX);
            // Title
            String title = movie.getTitle();

            // New Movie Item
            MovieItem movieItem = new MovieItem(itemId, parentId, title);

            // Short Description TODO: not sure the overview is the best match
            String shortDescription = movie.getOverview();
            if (filter.contains("dc:description") && StringUtils.isNotBlank(shortDescription)) {
                movieItem.setDescription(shortDescription);
            }
            // Long Description
            String longDescription = movie.getOverview();
            if (filter.contains("upnp:longDescription") && StringUtils.isNotBlank(longDescription)) {
                movieItem.setLongDescription(longDescription);
            }
            // Genres TODO: fake
            String[] genres = {"Unknown Genre"};
            if (filter.contains("upnp:genre") && genres != null && genres.length > 0) {
                movieItem.setGenres(genres);
            }
            // Rating TODO: not sure this means certification from the library, maybe should use rating
            String rating = movie.getCertification();
            if (filter.contains("upnp:rating") && StringUtils.isNotBlank(rating)) {
                movieItem.setRating(rating);
            }
            // Language
            String language = movie.getLanguage();
            if (filter.contains("dc:language ") && StringUtils.isNotBlank(language)) {
                movieItem.setLanguage(language);
            }
            // Producers TODO: fake
            Person[] producers = {new Person("Unknown Producer")};
            if (filter.contains("upnp:producer") && producers != null && producers.length > 0) {
                movieItem.setProducers(producers);
            }
            // Actors TODO: fake
            PersonWithRole[] actors = {new PersonWithRole("Unknown Actor", "Performer")};
            if (filter.contains("upnp:actor") && actors != null && actors.length > 0) {
                movieItem.setActors(actors);
            }
            // Directors TODO: fake
            Person[] directors = {new Person("Unknown Director")};
            if (filter.contains("upnp:director") && directors != null && directors.length > 0) {
                movieItem.setDirectors(directors);
            }

            // Publishers TODO: fake
            Person[] publishers = {new Person("Unknown Publisher")};
            if (filter.contains("dc:publisher") && publishers != null && publishers.length > 0) {
                movieItem.setPublishers(publishers);
            }

            // Original | Not Transcoded Resources
            if (transcoder.isValidMedia(getClientProfile(), movie)) {
                // Original Resource (Not Transcoded)
                XBoxRes originalResource = new XBoxRes();
                // URL
                URI originalUri = httpServer.forgeUrl(getClientProfile(), "movie", false, libraryId);
                originalResource.setValue(originalUri.toString());
                // Original dlna profile
                DLNAProfiles originalProfile = transcoder.getVideoProfile(movie.getFormat(), movie.getVideoFormat(), movie.getVideoFormatProfile(), movie.getVideoFormatVersion(), movie.getVideoCodec(), movie.getVideoBitRate(), movie.getVideoWidth(), movie.getVideoHeight(), movie.getVideoFps(),
                        movie.getAudioFormat(), movie.getAudioFormatProfile(), movie.getAudioFormatVersion(), movie.getAudioCodec(), movie.getAudioBitRate(), movie.getAudioSampleBitRate(), movie.getAudioChannels());
                // MIME
                String mime = transcoder.getVideoMIME(movie.getFormat(), movie.getVideoFormat(), movie.getVideoFormatProfile(), movie.getVideoFormatVersion(), movie.getVideoCodec());
                // This should not happens
                if (StringUtils.isBlank(mime)) {
                    mime = movie.getMIME();
                    logger.warn("Can't determine movie original MIME type, use {} from file information.", mime);
                }

                EnumMap<DLNAAttribute.Type, DLNAAttribute> dlnaAttributes = new EnumMap<DLNAAttribute.Type, DLNAAttribute>(DLNAAttribute.Type.class);
                if (originalProfile != DLNAProfiles.NONE) {
                    dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute(originalProfile));
                }
                dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_OP, new DLNAOperationsAttribute(DLNAOperations.RANGE));
                dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_FLAGS, new DLNAFlagsAttribute(DLNAFlags.STREAMING_TRANSFER_MODE, DLNAFlags.BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_V15));
                originalResource.setProtocolInfo(new DLNAProtocolInfo(Protocol.HTTP_GET, ProtocolInfo.WILDCARD, mime, dlnaAttributes));
                if (filter.contains("res@resolution")) {
                    originalResource.setResolution(movie.getVideoWidth(), movie.getVideoHeight());
                }
                if (filter.contains("res@duration")) {
                    originalResource.setDuration(DurationFormatUtils.formatDurationHMS(movie.getDuration()));
                }
                if (filter.contains("res@size")) {
                    originalResource.setSize(movie.getSize());
                }
                if (filter.contains("res@bitrate")) {
                    originalResource.setBitrate(movie.getBitRate());
                }
                if (filter.contains("res@nrAudioChannels")) {
                    originalResource.setNrAudioChannels((long) movie.getAudioChannels());
                }
                if (filter.contains("res@sampleFrequency ")) {
                    originalResource.setSampleFrequency(movie.getAudioSampleBitRate());
                }
                if (filter.contains("res@bitsPerSample")) {
                    originalResource.setBitsPerSample((long) movie.getAudioBitDepth());
                }
                if (filter.contains("res@microsoft:codec")) {
                    String codec = MicrosoftCodec.getVideoCodecId(movie.getVideoCodec());
                    if (StringUtils.isNotBlank(codec)) {
                        originalResource.setMicrosoftCodec(codec);
                    }
                }
                // Add to resources
                movieItem.addResource(originalResource);
            }
            // Transcoded Resource
            else {
                // Transcoded Resource
                XBoxRes transcodedResource = new XBoxRes();
                // URL
                URI transcodedUri = httpServer.forgeUrl(getClientProfile(), "movie", true, libraryId);
                transcodedResource.setValue(transcodedUri.toString());
                // Transcoded Profile
                DLNAProfiles transcodedProfile = transcoder.getVideoTranscodedProfile(getClientProfile(), movie);
                String mime = transcoder.getTranscodedMIME(getClientProfile(), movie);
                // This should not happens
                if (StringUtils.isBlank(mime)) {
                    logger.warn("Can't determine movie transcoded MIME type, this likely to be a error.");
                }
                EnumMap<DLNAAttribute.Type, DLNAAttribute> dlnaAttributes = new EnumMap<DLNAAttribute.Type, DLNAAttribute>(DLNAAttribute.Type.class);
                if (transcodedProfile != DLNAProfiles.NONE) {
                    dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute(transcodedProfile));
                }
                dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_CI, new DLNAConversionIndicatorAttribute(DLNAConversionIndicator.TRANSCODED));
                dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_OP, new DLNAOperationsAttribute(DLNAOperations.TIMESEEK));
                dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_FLAGS, new DLNAFlagsAttribute(DLNAFlags.STREAMING_TRANSFER_MODE, DLNAFlags.BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_V15));
                transcodedResource.setProtocolInfo(new DLNAProtocolInfo(Protocol.HTTP_GET, ProtocolInfo.WILDCARD, mime, dlnaAttributes));
                // TODO: Since transcoded information should change
                if (filter.contains("res@resolution")) {
                    transcodedResource.setResolution(movie.getVideoWidth(), movie.getVideoHeight());
                }
                if (filter.contains("res@duration")) {
                    transcodedResource.setDuration(DurationFormatUtils.formatDurationHMS(movie.getDuration()));
                }
                if (filter.contains("res@size")) {
                    // Not available
                }
                if (filter.contains("res@bitrate")) {
                    transcodedResource.setBitrate(movie.getBitRate());
                }
                if (filter.contains("res@nrAudioChannels")) {
                    transcodedResource.setNrAudioChannels((long) movie.getAudioChannels());
                }
                if (filter.contains("res@sampleFrequency ")) {
                    transcodedResource.setSampleFrequency(movie.getAudioSampleBitRate());
                }
                if (filter.contains("res@bitsPerSample")) {
                    transcodedResource.setBitsPerSample((long) movie.getAudioBitDepth());
                }
                if (filter.contains("res@microsoft:codec")) {
                    String codec = MicrosoftCodec.getVideoCodecId(transcoder.getVideoTranscodedCodec(getClientProfile(), movie));
                    if (StringUtils.isNotBlank(codec)) {
                        transcodedResource.setMicrosoftCodec(codec);
                    }
                }
                // Add to resources
                movieItem.addResource(transcodedResource);
            }
            // Create new movie item and add to result
            movieItems.add(movieItem);

        }
        return movieItems;
    }

    /**
     * Convert Simplified Chinese to Traditional Chinese
     * This because XBox360 can't correctly display Simplified Chinese at the moment
     *
     * @param container Video Contain
     */
    public void convertVideoContainerS2T(VisualContainer container) {
        container.setTitle(EncodingUtils.convertS2T(container.getTitle()));
        for (Container subContainer : container.getContainers()) {
            subContainer.setTitle(EncodingUtils.convertS2T(subContainer.getTitle()));
        }
        for (Item item : container.getItems()) {
            VisualVideoItem subItem = (VisualVideoItem) item;
            subItem.setTitle(EncodingUtils.convertS2T(subItem.getTitle()));
            subItem.setDescription(EncodingUtils.convertS2T(subItem.getDescription()));
            subItem.setLongDescription(EncodingUtils.convertS2T(subItem.getLongDescription()));
            // Genres
            List<String> genres = new ArrayList<String>();
            for (String genre : subItem.getGenres()) {
                genres.add(EncodingUtils.convertS2T(genre));
            }
            subItem.setGenres(genres.toArray(new String[genres.size()]));
            // Producers
            List<Person> producers = new ArrayList<Person>();
            for (Person person : subItem.getProducers()) {
                producers.add(new Person(EncodingUtils.convertS2T(person.getName())));
            }
            subItem.setProducers(producers.toArray(new Person[producers.size()]));
            // Publishers
            List<Person> publishers = new ArrayList<Person>();
            for (Person person : subItem.getPublishers()) {
                publishers.add(new Person(EncodingUtils.convertS2T(person.getName())));
            }
            subItem.setPublishers(publishers.toArray(new Person[publishers.size()]));
            // Directors
            List<Person> directors = new ArrayList<Person>();
            for (Person person : subItem.getDirectors()) {
                directors.add(new Person(EncodingUtils.convertS2T(person.getName())));
            }
            subItem.setDirectors(directors.toArray(new Person[directors.size()]));
            // Actors
            List<PersonWithRole> actors = new ArrayList<PersonWithRole>();
            for (PersonWithRole person : subItem.getActors()) {
                actors.add(new PersonWithRole(EncodingUtils.convertS2T(person.getName()), EncodingUtils.convertS2T(person.getRole())));
            }
            subItem.setActors(actors.toArray(new PersonWithRole[actors.size()]));
        }
    }
}
