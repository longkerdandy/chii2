package org.chii2.mediaserver.provider.sohu.content.container;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.provider.sohu.OnlineVideoProviderServiceImpl;
import org.chii2.mediaserver.provider.sohu.content.item.OnlineVideoItem;
import org.chii2.mediaserver.provider.sohu.factor.Catalog;
import org.chii2.mediaserver.provider.sohu.factor.series.SeriesOrigin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.support.model.*;
import org.teleal.cling.support.model.dlna.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Sohu Online Video Item Collection Container
 */
public class CollectionContainer extends VisualContainer {

    // Catalog
    private Catalog catalog = Catalog.SERIES;
    // Origin
    private SeriesOrigin origin = SeriesOrigin.KR;
    // Page Number
    private int pageNumber = 1;
    // URL Code Year Part
    private String codeYear;
    // URL Code Name Part
    private String codeName;

    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.provider.sohu");

    /**
     * Constructor
     *
     * @param filter Content Filter
     * @param id     Container ID
     */
    public CollectionContainer(String filter, String id) {
        super();

        this.filter = filter;

        parseId(id);

        // Container ID
        setId(id);

        // May used in Container Property Creator (part of UPnP protocol standard)
        setCreator("System");
        // May used in Container Property Clazz (part of UPnP protocol standard)
        setClazz(new DIDLObject.Class("object.container.storageFolder"));
        // Restricted
        setRestricted(true);
        // Searchable
        setSearchable(false);
        // Writable
        setWriteStatus(WriteStatus.NOT_WRITABLE);
    }

    @SuppressWarnings({"ConstantConditions"})
    @Override
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        // URL
        String url = getUrl(this.codeYear, this.codeName);
        int count = 0;
        int totalCount = 0;
        // Fetch and Parse
        try {
            Document doc = Jsoup.connect(url).get();
            // Basic Information Section
            Element divInfo = doc.select("div.info > div.r").first();
            // Video Item List Section
            Element listInfo = doc.select("div#similarLists").first();

            String longDescription = null;
            String shortDescription = null;
            List<String> genres = new ArrayList<String>();
            List<Person> directors = new ArrayList<Person>();
            List<PersonWithRole> actors = new ArrayList<PersonWithRole>();

            Elements dls = divInfo.select("dl > dd, dl > dt");
            for (Element dd_dt : dls) {
                if (dd_dt.childNodes().size() > 0 && dd_dt.childNode(0) instanceof TextNode) {
                    TextNode textNode = (TextNode) dd_dt.childNode(0);
                    if (StringUtils.trim(textNode.text()).startsWith("类型")) {
                        for (Element genreLink : dd_dt.select("a")) {
                            if (genreLink.childNodes().size() > 0 && genreLink.childNode(0) instanceof TextNode) {
                                String genre = ((TextNode) genreLink.childNode(0)).text();
                                if (StringUtils.isNotBlank(genre)) {
                                    genres.add(StringUtils.trim(genre));
                                }
                            }
                        }
                    } else if (StringUtils.trim(textNode.text()).startsWith("导演")) {
                        for (Element directorLink : dd_dt.select("a")) {
                            if (directorLink.childNodes().size() > 0 && directorLink.childNode(0) instanceof TextNode) {
                                String director = ((TextNode) directorLink.childNode(0)).text();
                                if (StringUtils.isNotBlank(director)) {
                                    directors.add(new Person(StringUtils.trim(director)));
                                }
                            }
                        }
                    } else if (StringUtils.trim(textNode.text()).startsWith("主演")) {
                        for (Element actorLink : dd_dt.select("a")) {
                            if (actorLink.childNodes().size() > 0 && actorLink.childNode(0) instanceof TextNode) {
                                String actor = ((TextNode) actorLink.childNode(0)).text();
                                if (StringUtils.isNotBlank(actor)) {
                                    actors.add(new PersonWithRole(StringUtils.trim(actor), "主演"));
                                }
                            }
                        }
                    }
                }
            }

            Element infoL = divInfo.select("span#infoL").first();
            if (infoL != null && infoL.childNodes().size() > 0 && infoL.childNode(0) instanceof TextNode) {
                longDescription = ((TextNode) infoL.childNode(0)).text();
            }

            Element infoS = divInfo.select("span#infoS").first();
            if (infoS != null && infoS.childNodes().size() > 0 && infoS.childNode(0) instanceof TextNode) {
                shortDescription = ((TextNode) infoS.childNode(0)).text();
            }

            // List of videos
            if (listInfo != null) {
                Elements listItems = listInfo.select("li");
                totalCount = listItems.size();
                for (Element listItem : listItems) {
                    String pictureUrl = null;
                    String videoUrl = null;
                    String title = null;
                    String codeTime = null;
                    String codeId = null;
                    long duration = 0;

                    Element picLink = listItem.select("a > img").first();
                    Element videoLink = listItem.select("span > a").first();
                    Element time = listItem.select("em").first();
                    // Picture Link
                    if (picLink != null) {
                        pictureUrl = StringUtils.trim(picLink.attr("src"));
                    }
                    // Video Link
                    if (videoLink != null) {
                        videoUrl = StringUtils.trim(videoLink.attr("href"));
                        if (videoLink.childNodes().size() > 0 && videoLink.childNode(0) instanceof TextNode) {
                            String titleText = ((TextNode) videoLink.childNode(0)).text();
                            if (StringUtils.isNotBlank(titleText)) {
                                title = StringUtils.trim(titleText);
                            }
                        }
                    }
                    // Time
                    if (time != null) {
                        if (time.childNodes().size() > 0 && time.childNode(0) instanceof TextNode) {
                            int minute = 0;
                            int second = 0;
                            TextNode timeText = (TextNode) time.childNode(0);
                            String trimmedTime = StringUtils.trim(timeText.text());
                            if (trimmedTime.startsWith("时长：") && trimmedTime.length() > 3) {
                                trimmedTime = trimmedTime.substring(3);
                            }
                            int mIndex = trimmedTime.indexOf("分");
                            int sIndex = trimmedTime.indexOf("秒");
                            if (mIndex >= 0) {
                                minute = NumberUtils.toInt(trimmedTime.substring(0 , mIndex));
                            }
                            if (mIndex >= 0 && sIndex > mIndex) {
                                second = NumberUtils.toInt(trimmedTime.substring(mIndex + 1, sIndex));
                            }
                            duration = (minute * 60 + second) * 1000;
                        }
                    }

                    if (StringUtils.isNotBlank(videoUrl)) {
                        String tempUrl = videoUrl;
                        if (tempUrl.length() > OnlineVideoProviderServiceImpl.VIDEO_URL.length() && tempUrl.substring(0, OnlineVideoProviderServiceImpl.VIDEO_URL.length()).equalsIgnoreCase(OnlineVideoProviderServiceImpl.VIDEO_URL)) {
                            tempUrl = tempUrl.substring(OnlineVideoProviderServiceImpl.VIDEO_URL.length());
                        } else if (tempUrl.startsWith("/")) {
                            tempUrl = tempUrl.substring(1);
                        }
                        String[] urlElements = tempUrl.split("/");
                        if (urlElements != null && urlElements.length >= 2) {
                            codeTime = urlElements[0];
                            codeId = urlElements[1];
                            if (codeId.endsWith(".shtml")) {
                                codeId = codeId.substring(0, codeId.length() - 6);
                            } else if (codeId.endsWith(".html")) {
                                codeId = codeId.substring(0, codeId.length() - 5);
                            }

                            // New Video Item
                            OnlineVideoItem videoItem = new OnlineVideoItem(OnlineVideoProviderServiceImpl.forgeVideoItemID(this.catalog, this.origin, this.pageNumber, this.codeYear, this.codeName, codeTime, codeId));
                            // Title
                            videoItem.setTitle(title);
                            // Short Description
                            if (filter.contains("dc:description") && StringUtils.isNotBlank(shortDescription)) {
                                videoItem.setDescription(shortDescription);
                            }
                            // Long Description
                            if (filter.contains("upnp:longDescription") && StringUtils.isNotBlank(longDescription)) {
                                videoItem.setLongDescription(longDescription);
                            }
                            // Genres
                            if (filter.contains("upnp:genre") && genres != null && genres.size() > 0) {
                                videoItem.setGenres(genres.toArray(new String[genres.size()]));
                            }
                            // Actors
                            if (filter.contains("upnp:actor") && actors != null && actors.size() > 0) {
                                videoItem.setActors(actors.toArray(new PersonWithRole[actors.size()]));
                            }
                            // Directors
                            if (filter.contains("upnp:director") && directors != null && directors.size() > 0) {
                                videoItem.setDirectors(directors.toArray(new Person[directors.size()]));
                            }

                            // Resource
                            Res resource = contentManager.getResource();
                            // URL
                            URI transcodedUri = contentManager.forgetOnlineVideoUrl(OnlineVideoProviderServiceImpl.PROVIDER_NAME, videoUrl);
                            resource.setValue(transcodedUri.toString());
                            // Profile
                            DLNAProfiles profile = contentManager.getVideoTranscodedProfile("MPEG-4", "AVC", "High@L3.0", 0, "avc1", 500000, 640, 368, 25, "AAC", "", 0, "AAC LC", 32000, 44100, 1);
                            String mime = contentManager.getVideoTranscodedMime("MPEG-4", "AVC", "High@L3.0", 0, "avc1", 500000, 640, 368, 25, "AAC", "", 0, "AAC LC", 32000, 44100, 1);
                            if (StringUtils.isBlank(mime)) {
                                logger.warn("Can't determine sohu online video MIME type, this likely to be a error.");
                            }
                            EnumMap<DLNAAttribute.Type, DLNAAttribute> dlnaAttributes = new EnumMap<DLNAAttribute.Type, DLNAAttribute>(DLNAAttribute.Type.class);
                            if (profile != DLNAProfiles.NONE) {
                                dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute(profile));
                            }
                            dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_CI, new DLNAConversionIndicatorAttribute(DLNAConversionIndicator.TRANSCODED));
                            dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_OP, new DLNAOperationsAttribute(DLNAOperations.RANGE));
                            dlnaAttributes.put(DLNAAttribute.Type.DLNA_ORG_FLAGS, new DLNAFlagsAttribute(DLNAFlags.STREAMING_TRANSFER_MODE, DLNAFlags.BACKGROUND_TRANSFERT_MODE, DLNAFlags.DLNA_V15));
                            // Resource
                            resource.setProtocolInfo(new DLNAProtocolInfo(Protocol.HTTP_GET, ProtocolInfo.WILDCARD, mime, dlnaAttributes));
                            if (filter.contains("res@resolution")) {
                                resource.setResolution(640, 368);
                            }
                            if (filter.contains("res@duration")) {
                                resource.setDuration(DurationFormatUtils.formatDurationHMS(duration));
                            }
                            if (filter.contains("res@bitrate")) {
                                resource.setBitrate(500000L);
                            }
                            if (filter.contains("res@nrAudioChannels")) {
                                resource.setNrAudioChannels(1L);
                            }
                            if (filter.contains("res@sampleFrequency ")) {
                                resource.setSampleFrequency(32000L);
                            }
                            if (filter.contains("res@bitsPerSample")) {
                                resource.setBitsPerSample(441000L);
                            }
                            // Add to resources
                            videoItem.addResource(resource);
                            // Add to Container
                            addItem(videoItem);
                            // Count Plus
                            count++;
                            // Exceed max count
                            if (count >= maxCount) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Load Sohu contents with error: {}.", e.getMessage());
        } finally {
            setChildCount(count);
            setTotalChildCount(totalCount);
        }
    }

    private void parseId(String id) {
        String[] elements = id.split("-");
        if (elements.length == 8) {
            this.catalog = Catalog.parseValue(elements[3]);
            this.origin = SeriesOrigin.parseValue(elements[4]);
            this.pageNumber = NumberUtils.toInt(elements[5], 1);
            this.codeYear = elements[6];
            this.codeName = elements[7];
        }
        setParentID(OnlineVideoProviderServiceImpl.forgePageID(this.catalog, this.origin, this.pageNumber));
    }

    private String getUrl(String codeYear, String codeName) {
        if (codeYear.startsWith("s")) {
            return OnlineVideoProviderServiceImpl.VIDEO_URL + codeYear + "/" + codeName + "/";
        } else {
            // Sometimes, Sohu mistaken the links between Series Collection and Series 1st Video
            // So if the url in fact the point to the 1st Video, we have to re-fetch its parent Series Collection
            try {
                Document doc = Jsoup.connect(OnlineVideoProviderServiceImpl.VIDEO_URL + codeYear + "/" + codeName + ".shtml").get();
                Element link = doc.select("em#specialID > a[href]").first();
                if (link != null) {
                    return StringUtils.trim(link.attr("href"));
                }
            } catch (IOException e) {
                logger.error("Load Sohu contents with error: {}.", e.getMessage());
            }
            // This shouldn't happens
            return OnlineVideoProviderServiceImpl.VIDEO_URL + codeYear + "/" + codeName + "/";
        }
    }
}