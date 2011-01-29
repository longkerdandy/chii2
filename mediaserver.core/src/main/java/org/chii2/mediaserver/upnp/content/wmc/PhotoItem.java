package org.chii2.mediaserver.upnp.content.wmc;

import org.teleal.cling.support.model.ProtocolInfo;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.item.Photo;
import org.teleal.common.util.MimeType;

/**
 * Photo Item for Windows Media Connect (Windows Media Player) related devices
 * Especially represent a photo belong to a album
 */
public class PhotoItem extends Photo {

    public PhotoItem(String id, String parentId, String title, String date, String album, MimeType mimeType, Long sizeInBytes) {
        super();

        // Item ID
        setId(id);
        // Item Parent ID
        setParentID(parentId);
        // Item Title TODO: This should be I18N
        setTitle(title);
        // May used in Container Property Creator (part of UPnP protocol standard)
        setCreator("System");
        //Picture Date
        setDate(date);
        // Photo Album
        setAlbum(album);

        // Resources
        Res resource = new Res() {
            @Override
            public String getValue() {
                return "";
            }
        };
        resource.setProtocolInfo(new ProtocolInfo(mimeType));
        resource.setResolution(1024, 768);
        resource.setSize(sizeInBytes);
        addResource(resource);
    }
}
