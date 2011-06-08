package org.chii2.mediaserver.provider.sohu.content.item;

import org.apache.commons.lang.math.NumberUtils;
import org.chii2.mediaserver.api.content.item.VisualVideoItem;
import org.chii2.mediaserver.provider.sohu.OnlineVideoProviderServiceImpl;
import org.chii2.mediaserver.provider.sohu.factor.Catalog;
import org.chii2.mediaserver.provider.sohu.factor.series.SeriesOrigin;

/**
 * Online Video Item
 */
public class OnlineVideoItem extends VisualVideoItem {
    // Catalog
    private Catalog catalog = Catalog.SERIES;
    // Origin
    private SeriesOrigin origin = SeriesOrigin.KR;
    // Page Number
    private int pageNumber = 1;
    // Parent Code Year
    private String parentCodeYear;
    // Parent Code Name
    private String parentCodeName;
    // Url Code Time Part
    private String urlCodeTime;
    // Url Code ID Part
    private String urlCodeID;

    /**
     * Constructor
     *
     * @param id       Item ID
     */
    public OnlineVideoItem(String id) {
        super();

        parseId(id);

        // Item ID
        setId(id);
        //  Creator (part of UPnP protocol standard)
        setCreator("System");
    }

    private void parseId(String id) {
        String[] elements = id.split("-");
        if (elements.length == 10) {
            this.catalog = Catalog.parseValue(elements[3]);
            this.origin = SeriesOrigin.parseValue(elements[4]);
            this.pageNumber = NumberUtils.toInt(elements[5], 1);
            this.parentCodeYear = elements[6];
            this.parentCodeName = elements[7];
            this.urlCodeTime = elements[8];
            this.urlCodeID = elements[9];
        }
        setParentID(OnlineVideoProviderServiceImpl.forgeCollectionID(this.catalog, this.origin, this.pageNumber, this.parentCodeYear, this.parentCodeName));
    }
}
