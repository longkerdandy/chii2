package org.chii2.mediaserver.provider.sohu;

import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.provider.OnlineVideoProviderService;
import org.chii2.mediaserver.provider.sohu.content.container.*;
import org.chii2.mediaserver.provider.sohu.factor.Catalog;
import org.chii2.mediaserver.provider.sohu.factor.series.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sohu Online Video Provider Service
 */
public class OnlineVideoProviderServiceImpl implements OnlineVideoProviderService {
    // SOHU Prefix
    public final static String SOHU_PREFIX = "SOHU-";
    // Catalog Prefix
    public final static String CATALOG_PREFIX = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "CATALOG-";
    // Page Prefix
    public final static String PAGE_PREFIX = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "PAGE-";
    // Collection Prefix
    public final static String COLLECTION_PREFIX = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "COLLECTION-";
    // Root Container ID
    public final static String SOHU_ROOT = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "ROOT";
    // Series Container ID
    public final static String SOHU_SERIES = ONLINE_VIDEO_CONTAINER_PREFIX + SOHU_PREFIX + "SERIES";
    // Search URL
    public final static String SEARCH_URL = "http://so.tv.sohu.com/list";
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
        return "Sohu";
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
        if (url.length() > 19 && url.substring(0, 19).equalsIgnoreCase("http://tv.sohu.com/")) {
            url = url.substring(19);
        } else if (url.length() > 12 && url.substring(0, 12).equalsIgnoreCase("tv.sohu.com/")) {
            url = url.substring(12);
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
}
