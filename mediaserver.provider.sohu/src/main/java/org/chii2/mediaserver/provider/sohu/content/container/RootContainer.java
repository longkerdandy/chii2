package org.chii2.mediaserver.provider.sohu.content.container;

import org.chii2.mediaserver.api.content.ContentManager;
import org.chii2.mediaserver.api.content.container.VisualContainer;
import org.chii2.mediaserver.api.provider.OnlineVideoProviderService;
import org.chii2.mediaserver.provider.sohu.OnlineVideoProviderServiceImpl;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.WriteStatus;

/**
 * Sohu Online Video Root Container
 */
public class RootContainer extends VisualContainer {
    /**
     * Constructor
     *
     * Please note the Parent ID is not correct, it should be set outside the provider.
     *
     * @param filter Content Filter
     */
    public RootContainer(String filter) {
        super();

        this.filter = filter;

        // Root Container ID: OVC-SOHU-ROOT
        setId( OnlineVideoProviderServiceImpl.SOHU_ROOT);
        // Parent ID
        setParentID("-1");
        // Title TODO: This should be I18N
        setTitle("搜狐视频");
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
        addContainer(new SeriesContainer(filter));
        setChildCount(1);
        setTotalChildCount(1);
    }
}
