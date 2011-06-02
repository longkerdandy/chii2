package org.chii2.mediaserver.provider.sohu.content.container;

import org.apache.commons.lang.math.NumberUtils;
import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.provider.sohu.OnlineVideoProviderServiceImpl;
import org.chii2.mediaserver.provider.sohu.factor.Catalog;
import org.chii2.mediaserver.provider.sohu.factor.series.SeriesOrigin;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;

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

    @Override
    public void loadContents(long startIndex, long maxCount, SortCriterion[] orderBy, ContentManager contentManager) {
        //To change body of implemented methods use File | Settings | File Templates.
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
}