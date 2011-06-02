package org.chii2.mediaserver.provider.sohu.content.container;

import org.apache.commons.lang.math.NumberUtils;
import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.provider.sohu.OnlineVideoProviderServiceImpl;
import org.chii2.mediaserver.provider.sohu.factor.Catalog;
import org.chii2.mediaserver.provider.sohu.factor.series.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;

import java.io.IOException;

/**
 * Sohu Online Video Catalog Container
 */
public class CatalogContainer extends VisualContainer {
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
    public CatalogContainer(String filter, String id) {
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
            int pageCount = 0;
            int count = 0;
            Element div = doc.select("div.jumpA > div").first();
            if (div != null && div.childNodes().size() > 0) {
                Node textNode = div.childNode(0);
                if (textNode instanceof TextNode) {
                    String text = ((TextNode) textNode).text();
                    String[] values = text.split("/");
                    if (values.length == 2) {
                        pageCount = NumberUtils.toInt(values[1], 0);
                    }
                    if (pageCount > 0) {
                        for (int i = (int) startIndex; i < pageCount; i++) {
                            addContainer(new PageContainer(filter, OnlineVideoProviderServiceImpl.forgePageID(this.catalog, this.origin, i + 1)));
                            count++;
                            if (count >= maxCount) {
                                break;
                            }
                        }
                    }
                }
            }
            setChildCount(count);
            setTotalChildCount(pageCount);
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
            // TODO: Not Completed
            switch (catalog) {
                case SERIES:
                    setParentID(OnlineVideoProviderServiceImpl.SOHU_SERIES);
                    setTitle(OnlineVideoProviderServiceImpl.getSeriesCatalogTitle(origin));
                    break;
                default:
                    setParentID(OnlineVideoProviderServiceImpl.SOHU_SERIES);
                    setTitle(OnlineVideoProviderServiceImpl.getSeriesCatalogTitle(origin));
            }
        }
    }
}