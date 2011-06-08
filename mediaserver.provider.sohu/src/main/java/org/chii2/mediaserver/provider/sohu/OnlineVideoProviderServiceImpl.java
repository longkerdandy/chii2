package org.chii2.mediaserver.provider.sohu;

import org.apache.commons.lang.StringUtils;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.provider.OnlineVideoProviderService;
import org.chii2.mediaserver.provider.sohu.content.container.*;
import org.chii2.mediaserver.provider.sohu.factor.Catalog;
import org.chii2.mediaserver.provider.sohu.factor.series.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sohu Online Video Provider Service
 */
public class OnlineVideoProviderServiceImpl implements OnlineVideoProviderService {
    public final static String PROVIDER_NAME = "Sohu";
    // SOHU Prefix
    public final static String SOHU_PREFIX = "SOHU-";
    // Catalog Prefix
    public final static String CATALOG_PREFIX = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "CATALOG-";
    // Page Prefix
    public final static String PAGE_PREFIX = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "PAGE-";
    // Collection Prefix
    public final static String COLLECTION_PREFIX = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "COLLECTION-";
    // Video Item Prefix
    public final static String VIDEO_ITEM_PREFIX = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "ITEM-";
    // Root Container ID
    public final static String SOHU_ROOT = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "ROOT";
    // Series Container ID
    public final static String SOHU_SERIES = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "SERIES";
    // Search URL
    public final static String SEARCH_URL = "http://so.tv.sohu.com/list";
    // Video URL
    public final static String VIDEO_URL = "http://tv.sohu.com/";
    // Number of results per page
    public final static int RESULTS_PRE_PAGE = 20;

    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.provider.sohu");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Server Online Video Provider Sohu init.");
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Server Online Video Provider Sohu destroy.");
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getVideoHostUrl() {
        return VIDEO_URL;
    }

    @Override
    public boolean isMatch(String id) {
        int length = ONLINE_VIDEO_CONTAINER_PREFIX.length() + SOHU_PREFIX.length();
        return id.length() > length && id.substring(0, length).equalsIgnoreCase(ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX);
    }

    @Override
    public VisualContainer getRootContainer(String filter) {
        return new RootContainer(filter);
    }

    @Override
    public VisualContainer getContainerByID(String filter, String id) {
        if (SOHU_ROOT.equalsIgnoreCase(id)) {
            return new RootContainer(filter);
        } else if (SOHU_SERIES.equalsIgnoreCase(id)) {
            return new SeriesContainer(filter);
        } else if (id.startsWith(CATALOG_PREFIX)) {
            return new CatalogContainer(filter, id);
        } else if (id.startsWith(PAGE_PREFIX)) {
            return new PageContainer(filter, id);
        } else if (id.startsWith(COLLECTION_PREFIX)) {
            return new CollectionContainer(filter, id);
        } else {
            return null;
        }
    }

    @Override
    public String getContainer(String url) {
        return "MPEG-4";
    }

    @Override
    public List<String> getRealAddress(String url) {
        List<String> urls = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("http://www.flvcd.com/parse.php?kw=" + url + "&format=high").get();
            Elements links = doc.select("td > a[href$=.mp4]");
            for (Element link : links) {
                String realUrl = StringUtils.trim(link.attr("href"));
                String protocol = realUrl.substring(0, realUrl.indexOf("://"));
                realUrl = realUrl.substring(protocol.length() + 3);
                String host = realUrl.substring(0, realUrl.indexOf("/"));
                realUrl = "?file=" + realUrl.substring(host.length() + 1);
                urls.add(protocol + "://" + host + "/" + realUrl);
            }
        } catch (IOException e) {
            logger.error("Analyze Sohu video real address with Flvcd with error: {}.", e.getMessage());
        }

        return urls;
    }

    @Override
    public List<String> getPipe(String url) {
        List<String> pipe = new ArrayList<String>();
        pipe.add("wget");
        pipe.add("-q");
        pipe.add("-O");
        pipe.add("-");
        pipe.add("-U");
        pipe.add("\"Mozilla/5.0 (X11; Linux x86_64; rv:2.2a1pre) Gecko/20110324 Firefox/4.2a1pre\"");
        pipe.add("\"" + url + "\"");
        return pipe;
    }

    @Override
    public String getVideoFormat(String url) {
        return "AVC";
    }

    @Override
    public String getVideoFormatProfile(String url) {
        return "High@L3.0";
    }

    @Override
    public int getVideoFormatVersion(String url) {
        return 0;
    }

    @Override
    public String getVideoCodec(String url) {
        return "avc1";
    }

    @Override
    public long getVideoBitRate(String url) {
        return 500000;
    }

    @Override
    public int getVideoWidth(String url) {
        return 640;
    }

    @Override
    public int getVideoHeight(String url) {
        return 368;
    }

    @Override
    public float getVideoFps(String url) {
        return 25;
    }

    @Override
    public String getAudioFormat(String url) {
        return "AAC";
    }

    @Override
    public String getAudioFormatProfile(String url) {
        return "";
    }

    @Override
    public int getAudioFormatVersion(String url) {
        return 0;
    }

    @Override
    public String getAudioCodec(String url) {
        return "AAC LC";
    }

    @Override
    public long getAudioBitRate(String url) {
        return 32000;
    }

    @Override
    public long getAudioSampleBitRate(String url) {
        return 44100;
    }

    @Override
    public int getAudioChannels(String url) {
        return 1;
    }

    public static String forgeCatalogID(Catalog catalog, SeriesOrigin origin, int page) {
        return OnlineVideoProviderServiceImpl.CATALOG_PREFIX + catalog.getValue() + "-" + origin.getValue() + "-" + page;
    }

    public static String forgeSearchUrl(Catalog catalog, SeriesStatus status, SeriesGenre genre, SeriesOrigin origin, SeriesYear year, SeriesPay pay, SeriesSort sort, int page) {
        return OnlineVideoProviderServiceImpl.SEARCH_URL + "_" + catalog.getValue() + "_" + genre.getValue() + "_" + origin.getValue() + "_" + year.getValue() + "_" + "p5" + "_" + "p6" + "_" + sort.getValue() + "_" + pay.getValue() + "_" + status.getValue() + "_" + "p10" + page + "_" + "p11" + ".html";
    }

    // TODO: Need I18N
    public static String getSeriesCatalogTitle(SeriesOrigin origin) {
        switch (origin) {
            case ALL:
                return "全部";
            case CN:
                return "内地电视剧";
            case HK:
                return "香港电视剧";
            case TW:
                return "台湾电视剧";
            case KR:
                return "韩国电视剧";
            case US:
                return "美国电视剧";
            case TAI:
                return "泰国电视剧";
            case OTHER:
                return "其他电视剧";
            default:
                return "全部";
        }
    }

    public static String forgePageID(Catalog catalog, SeriesOrigin origin, int page) {
        return OnlineVideoProviderServiceImpl.PAGE_PREFIX + catalog.getValue() + "-" + origin.getValue() + "-" + page;
    }

    // TODO: May not correct
    public static String forgeCollectionID(Catalog catalog, SeriesOrigin origin, int pageNumber, String url) {
        if (url.length() > VIDEO_URL.length() && url.substring(0, VIDEO_URL.length()).equalsIgnoreCase(VIDEO_URL)) {
            url = url.substring(VIDEO_URL.length());
        } else if (url.startsWith("/")) {
            url = url.substring(1);
        }

        String[] urlElements = url.split("/");
        if (urlElements.length >= 2) {
            return OnlineVideoProviderServiceImpl.COLLECTION_PREFIX + catalog.getValue() + "-" + origin.getValue() + "-" + pageNumber + "-" + urlElements[0] + "-" + urlElements[1];
        } else {
            return null;
        }
    }

    public static String forgeCollectionID(Catalog catalog, SeriesOrigin origin, int pageNumber, String codeYear, String codeName) {
        return OnlineVideoProviderServiceImpl.COLLECTION_PREFIX + catalog.getValue() + "-" + origin.getValue() + "-" + pageNumber + "-" + codeYear + "-" + codeName;
    }

    public static String forgeVideoItemID(Catalog catalog, SeriesOrigin origin, int pageNumber, String codeYear, String codeName, String codeTime, String codeId) {
        return OnlineVideoProviderServiceImpl.VIDEO_ITEM_PREFIX + catalog.getValue() + "-" + origin.getValue() + "-" + pageNumber + "-" + codeYear + "-" + codeName + "-" + codeTime + "-" + codeId;
    }
}
