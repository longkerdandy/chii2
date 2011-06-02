package org.chii2.mediaserver.provider.sohu.content.container;

import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.provider.sohu.OnlineVideoProviderServiceImpl;
import org.chii2.mediaserver.provider.sohu.factor.Catalog;
import org.chii2.mediaserver.provider.sohu.factor.series.*;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;

/**
 * Sohu Online Video Series Container
 */
public class SeriesContainer extends VisualContainer {

    /**
     * Constructor
     *
     * @param filter   Content Filter
     */
    public SeriesContainer(String filter) {
        super();

        this.filter = filter;

        // Container ID: OVC-SOHU-SERIES
        setId(OnlineVideoProviderServiceImpl.SOHU_SERIES);
        // Parent Container is Root
        setParentID(OnlineVideoProviderServiceImpl.SOHU_ROOT);
        // Title TODO: This should be I18N
        setTitle("电视剧");
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
        // TODO: Not Completed
        String catalogID = OnlineVideoProviderServiceImpl.forgeCatalogID(Catalog.SERIES, SeriesOrigin.KR, 1);
        addContainer(new CatalogContainer(filter, catalogID));
        setChildCount(1);
        setTotalChildCount(1);
    }
}