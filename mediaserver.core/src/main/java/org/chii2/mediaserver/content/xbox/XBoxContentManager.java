package org.chii2.mediaserver.content.xbox;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.mediaserver.api.content.item.VisualItem;
import org.chii2.mediaserver.api.dlna.DLNAFlags;
import org.chii2.mediaserver.api.dlna.DLNAIndicator;
import org.chii2.mediaserver.api.dlna.DLNAOperation;
import org.chii2.mediaserver.api.dlna.DLNAProfile;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.mediaserver.content.common.CommonContentManager;
import org.chii2.mediaserver.content.common.Item.MovieItem;
import org.chii2.transcoder.api.core.TranscoderService;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     */
    public XBoxContentManager(MediaLibraryService mediaLibrary, HttpServerService httpServer, TranscoderService transcoder) {
        super(mediaLibrary, httpServer, transcoder);

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
    public List<? extends VisualItem> getMovies(String parentId, String filter, long startIndex, long maxCount, SortCriterion[] orderBy) {
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
            for (MovieFile movieFile : movie.getFiles()) {
                // ID from library
                String libraryId = movie.getId();
                // Item ID
                String itemId = forgeItemId(libraryId, movieFile.getDiskNum(), parentId, MOVIE_ITEM_PREFIX);
                // Title
                String title = movie.getTitle();
                // Short Description TODO: not sure the overview is the best match
                String shortDescription = movie.getOverview();
                // Long Description
                String longDescription = movie.getOverview();
                // TODO: fake
                String[] genres = {"Unknown Genre"};
                // Rating TODO: not sure this means certification from the library, maybe should use rating
                String rating = movie.getCertification();
                // Language
                String language = movie.getLanguage();
                // Producers TODO: fake
                Person[] producers = {new Person("Unknown Producer")};
                // Actors TODO: fake
                PersonWithRole[] actors = {new PersonWithRole("Unknown Actor", "Performer")};
                // Directors TODO: fake
                Person[] directors = {new Person("Unknown Director")};
                // Publishers TODO: fake
                Person[] publishers = {new Person("Unknown Publisher")};

                // Resources
                List<Res> resources = new ArrayList<Res>();
                // Test whether original file format is acceptable, if not transcoding needed
                DLNAProfile.Profile originalProfile = DLNAProfile.getDLNAProfile(movie.getVideoCodec(), movie.getVideoBitRate(), movie.getVideoWidth(), movie.getVideoHeight(), movie.getVideoFps(), movie.getAudioCodec(), movie.getAudioBitRate(), movie.getAudioSampleBitRate(), movie.getAudioChannels());
                if (originalProfile != DLNAProfile.Profile.PROFILE_INVALID && transcoder.isValidProfile(getClientProfile(), originalProfile)) {
                    // Original Resource (Not Transcoded)
                    XBoxRes originalResource = new XBoxRes();
                    URI originalUri = httpServer.forgeUrl(getClientProfile(), "movie", false, movieFile.getDiskNum(), originalProfile, libraryId);
                    originalResource.setValue(originalUri.toString());
                    String mime = DLNAProfile.getMimeByProfile(originalProfile);
                    originalResource.setProtocolInfo(new ProtocolInfo(Protocol.HTTP_GET, "*", mime, DLNAProfile.getProfileString(originalProfile) + ";" + DLNAOperation.getDLNAOperation(false, true) + ";" + DLNAFlags.getDLNAFlags(DLNAFlags.DLNA_FLAG_STREAMING_TRANSFER_MODE, DLNAFlags.DLNA_FLAG_BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_FLAG_DLNA_V15)));
                    if (filter.contains("res@resolution")) {
                        originalResource.setResolution(movieFile.getVideoWidth(), movieFile.getVideoHeight());
                    }
                    if (filter.contains("res@duration")) {
                        originalResource.setDuration(DurationFormatUtils.formatDurationHMS(movieFile.getDuration()));
                    }
                    if (filter.contains("res@size")) {
                        originalResource.setSize(movieFile.getSize());
                    }
                    if (filter.contains("res@bitrate")) {
                        originalResource.setBitrate(movieFile.getBitRate());
                    }
                    if (filter.contains("res@nrAudioChannels")) {
                        originalResource.setNrAudioChannels((long) movieFile.getAudioChannelCount());
                    }
                    if (filter.contains("res@sampleFrequency ")) {
                        originalResource.setSampleFrequency(movieFile.getAudioSamplingRate());
                    }
                    if (filter.contains("res@bitsPerSample")) {
                        originalResource.setBitsPerSample((long) movieFile.getAudioBitDepth());
                    }
                    if (filter.contains("res@microsoft:codec")) {
                        String codec = MicrosoftCodec.getVideoCodecId(movieFile.getVideoCodec());
                        if (StringUtils.isNotBlank(codec)) {
                            originalResource.setMicrosoftCodec(codec);
                        }
                    }
                    // Add to resources
                    resources.add(originalResource);
                } else {
                    // Transcoded
                    DLNAProfile.Profile transcodedProfile = transcoder.getTranscodedProfile(getClientProfile(), movie.getVideoCodec(), movie.getVideoBitRate(), movie.getVideoWidth(), movie.getVideoHeight(), movie.getVideoFps(), movie.getAudioCodec(), movie.getAudioBitRate(), movie.getAudioSampleBitRate(), movie.getAudioChannels());
                    XBoxRes transcodedResource = new XBoxRes();
                    URI transcodedUri = httpServer.forgeUrl(getClientProfile(), "movie", true, movieFile.getDiskNum(), transcodedProfile, libraryId);
                    transcodedResource.setValue(transcodedUri.toString());
                    String mime = DLNAProfile.getMimeByProfile(transcodedProfile);
                    transcodedResource.setProtocolInfo(new ProtocolInfo(Protocol.HTTP_GET, "*", mime, DLNAProfile.getProfileString(transcodedProfile) + ";" + DLNAOperation.getDLNAOperation(true, false) + ";" + DLNAIndicator.getDLNAConversionIndicator(true) + ";" + DLNAFlags.getDLNAFlags(DLNAFlags.DLNA_FLAG_STREAMING_TRANSFER_MODE, DLNAFlags.DLNA_FLAG_BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_FLAG_DLNA_V15)));
                    // TODO: Since transcoded information should change
                    if (filter.contains("res@resolution")) {
                        transcodedResource.setResolution(movieFile.getVideoWidth(), movieFile.getVideoHeight());
                    }
                    if (filter.contains("res@duration")) {
                        transcodedResource.setDuration(DurationFormatUtils.formatDurationHMS(movieFile.getDuration()));
                    }
                    if (filter.contains("res@size")) {
                        transcodedResource.setSize(movieFile.getSize());
                    }
                    if (filter.contains("res@bitrate")) {
                        transcodedResource.setBitrate(movieFile.getBitRate());
                    }
                    if (filter.contains("res@nrAudioChannels")) {
                        transcodedResource.setNrAudioChannels((long) movieFile.getAudioChannelCount());
                    }
                    if (filter.contains("res@sampleFrequency ")) {
                        transcodedResource.setSampleFrequency(movieFile.getAudioSamplingRate());
                    }
                    if (filter.contains("res@bitsPerSample")) {
                        transcodedResource.setBitsPerSample((long) movieFile.getAudioBitDepth());
                    }
                    if (filter.contains("res@microsoft:codec")) {
                        String codec = MicrosoftCodec.getVideoCodecId(movieFile.getVideoCodec());
                        if (StringUtils.isNotBlank(codec)) {
                            transcodedResource.setMicrosoftCodec(codec);
                        }
                    }
                    // Add to resources
                    resources.add(transcodedResource);
                }
                // Create new movie item and add to result
                movieItems.add(new MovieItem(filter, itemId, parentId, title, shortDescription, longDescription, genres, rating, language, producers, actors, directors, publishers, resources));
            }
        }
        return movieItems;
    }
}
