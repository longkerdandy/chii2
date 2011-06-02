package org.chii2.mediaserver.provider.sohu.content.container;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.provider.sohu.OnlineVideoProviderServiceImpl;
import org.chii2.mediaserver.provider.sohu.factor.Catalog;
import org.chii2.mediaserver.provider.sohu.factor.series.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;

import java.io.IOException;

/**
 * Sohu Online Video Page Container
 */
public class PageContainer extends VisualContainer {
    // Catalog
    private Catalog catalog = Catalog.SERIES;
    // Origin
    private SeriesOrigin origin = SeriesOrigin.KR;
    // Page Number
    private int pageNumber = 1;

    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.provider.sohu");

    /**
     * Constructor
     *
     * @param filter Content Filter
     * @param id     Container ID
     */
    public PageContainer(String filter, String id) {
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

    @Override
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        // URL
        String url = OnlineVideoProviderServiceImpl.forgeSearchUrl(this.catalog, SeriesStatus.NORMAL, SeriesGenre.ALL, this.origin, SeriesYear.ALL, SeriesPay.FREE, SeriesSort.RELATED, this.pageNumber);
        // Fetch and parse from Sohu
        try {
            Document doc = Jsoup.connect(url).get();
            int count = 0;
            int totalCount = 0;
            Elements elements = doc.getElementsByClass("vTxt");
            if (elements != null && elements.size() > 0) {
                totalCount = elements.size();
                for (int i = (int) startIndex; i < elements.size(); i++) {
                    Element href = elements.get(i).select("h4 > a").first();
                    if (href != null) {
                        TextNode textNode = (TextNode) href.childNode(0);
                        String collectionUrl = StringUtils.trim(href.attr("href"));
                        String collectionTitle = StringUtils.trim(textNode.text());
                        String containerId = OnlineVideoProviderServiceImpl.forgeCollectionID(this.catalog, this.origin, this.pageNumber, collectionUrl);
                        if (containerId != null) {
                            CollectionContainer collectionContainer = new CollectionContainer(filter, containerId);
                            collectionContainer.setTitle(collectionTitle);
                            addContainer(collectionContainer);
                            count++;
                        }
                    }
                    if (count >= maxCount) {
                        break;
                    }
                }
            }
            setChildCount(count);
            setTotalChildCount(totalCount);
        } catch (IOException e) {
            logger.error("Load Sohu contents with error: {}.", e.getMessage());
        }
    }

    private void parseId(String id) {
        String[] elements = id.split("-");
        if (elements.length == 6) {
            this.catalog = Catalog.parseValue(elements[3]);
            this.origin = SeriesOrigin.parseValue(elements[4]);
            this.pageNumber = NumberUtils.toInt(elements[5], 1);
        }
        setParentID(OnlineVideoProviderServiceImpl.forgeCatalogID(this.catalog, this.origin, 1));
        // TODO: Not Completed
        switch (catalog) {
            case SERIES:
                setTitle(OnlineVideoProviderServiceImpl.getSeriesCatalogTitle(origin) + " " + getNumberDisplay(this.pageNumber));
                break;
            default:
                setTitle(OnlineVideoProviderServiceImpl.getSeriesCatalogTitle(origin) + " " + getNumberDisplay(this.pageNumber));
        }
    }

    private String getNumberDisplay(int pageNumber) {
        int numberBegin = OnlineVideoProviderServiceImpl.RESULTS_PRE_PAGE * (pageNumber - 1) + 1;
        int numberEnd = OnlineVideoProviderServiceImpl.RESULTS_PRE_PAGE * pageNumber;
        return String.format("%05d", numberBegin) + " ~ " + String.format("%05d", numberEnd);
    }
}
