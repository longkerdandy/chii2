package org.chii2.mediaserver.content.common.Item;

import org.chii2.mediaserver.api.content.item.VisualPictureItem;
import org.teleal.cling.support.model.Res;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Photo Item
 * Especially represent a photo belong to a album
 */
public class PhotoItem extends VisualPictureItem {

    // Filter
    private String filter;

    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Item ID
     * @param parentId Item Parent ID
     * @param title    Item Title
     */
    protected PhotoItem(String filter, String id, String parentId, String title) {
        super();

        this.filter = filter;

        // Item ID
        setId(id);
        // Item Parent ID
        setParentID(parentId);
        // Item Title
        if (filter.contains("dc:title")) {
            setTitle(title);
        }
        //  Creator (part of UPnP protocol standard)
        if (filter.contains("dc:creator")) {
            setCreator("System");
        }
    }

    /**
     * Constructor
     *
     * @param filter          Content Filter
     * @param id              Item ID
     * @param parentId        Item Parent ID
     * @param title           Item Title
     * @param date            Photo Date
     * @param album           Photo Album
     * @param description     Item Description
     * @param longDescription Item Long Description
     */
    public PhotoItem(String filter, String id, String parentId, String title, Date date, String album, String description, String longDescription, List<Res> resources) {

        this(filter, id, parentId, title);

        //Picture Date
        if (filter.contains("dc:date") && date != null) {
            setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        // Photo Album
        if (filter.contains("upnp:album")) {
            setAlbum(album);
        }
        // Description
        if (filter.contains("dc:description")) {
            setDescription(description);
        }
        // Long Description
        if (filter.contains("upnp:longDescription")) {
            setLongDescription(longDescription);
        }

        // Resources
        for (Res resource : resources) {
            addResource(resource);
        }
    }
}
