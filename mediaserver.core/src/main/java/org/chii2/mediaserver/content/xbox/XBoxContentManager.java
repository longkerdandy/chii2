package org.chii2.mediaserver.content.xbox;

import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.mediaserver.api.content.item.VisualItem;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.mediaserver.content.common.CommonContentManager;
import org.chii2.mediaserver.content.common.Item.MovieItem;
import org.chii2.transcoder.api.core.TranscoderService;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            // If user agent contains Xbox, is should be a Xbox client
            List<String> userAgent = headers.get("User-agent");
            if (getClientProfile().equalsIgnoreCase(transcoder.getClientProfile(userAgent))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<? extends VisualItem> getMovies(String parentId, String filter, long startIndex, long maxCount, SortCriterion[] orderBy) {
        List<MovieItem> movies = new ArrayList<MovieItem>();
        String id = forgeItemId(UUID.randomUUID().toString(), parentId, MOVIE_ITEM_PREFIX);
        String title = "Wildlife";
        URI uri = httpServer.forgeMovieUrl(getClientProfile(), id);
        //String url="http://192.168.1.3:8888/WMPNSSv4/262985495/0_e0ZBNzAwNEM4LTc4NjgtNDNFRC1CNTkyLTYxNDE1MTE1MzM4NX0uMC4yQzE5NTgxOQ.wmv";
        //URI thumbUrl = httpServer.forgeMovieThumbUrl(getClientProfile(), id);
        URI thumbUrl = null;
        try {
            thumbUrl = new URI("http://192.168.1.3:8888/WMPNSSv4/262985495/0_e0ZBNzAwNEM4LTc4NjgtNDNFRC1CNTkyLTYxNDE1MTE1MzM4NX0uMC4yQzE5NTgxOQ.wmv?albumArt=true");
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String[] genres = {"Unknown Genre"};
        PersonWithRole[] actors = {new PersonWithRole("Unknown Actor", "Performer")};
        // Resources
        List<Res> resources = new ArrayList<Res>();
        XBoxRes resource = new XBoxRes();
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
        if (filter.contains("res@microsoft:codec")) {
            resource.setMicrosoftCodec("{31435657-0000-0010-8000-00AA00389B71}");
        }
        resources.add(resource);

        movies.add(new MovieItem(filter, id, parentId, title, null, null, genres, null, null, null, actors, null, null, thumbUrl, resources));
        return movies;
    }
}
