package org.chii2.mediaserver.api.content.item.common;

import org.teleal.cling.support.model.ProtocolInfo;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.item.Photo;
import org.teleal.common.util.MimeType;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Photo Item for Windows Media Connect (Windows Media Player) related devices
 * Especially represent a photo belong to a album
 */
public class PhotoItem extends Photo {

    public PhotoItem(String id, String parentId, String title, Date date, String album, String description, String longDescription, String url, MimeType mime, int width, int height, long colorDepth, long size) {
        super();

        // Item ID
        setId(id);
        // Item Parent ID
        setParentID(parentId);
        // Item Title
        setTitle(title);
        // May used in Container Property Creator (part of UPnP protocol standard)
        setCreator("System");
        //Picture Date
        setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        // Photo Album
        setAlbum(album);
        // Description
        setDescription(description);
        // Long Description
        setLongDescription(longDescription);

        // Resources
        Res resource = new Res();
        resource.setValue(url);
        resource.setProtocolInfo(new ProtocolInfo(mime));
        resource.setResolution(width, height);
        // TODO: Conditional by Filter
        // resource.setColorDepth(colorDepth);
        // resource.setSize(size);
        addResource(resource);
    }
}
