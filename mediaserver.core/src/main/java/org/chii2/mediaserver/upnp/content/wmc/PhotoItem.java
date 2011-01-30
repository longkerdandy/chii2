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

    public PhotoItem(String id, String parentId, String title, String album) {
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
        setDate("2008-02-11");
        // Photo Album
        setAlbum(album);

        // Resources
        Res resource = new Res() {
            @Override
            public String getValue() {
                return "http://192.168.1.3:8888/picture/111111";
            }
        };
        resource.setProtocolInfo(new ProtocolInfo(MimeType.valueOf("image/jpeg")));
        resource.setResolution(1024, 768);
        addResource(resource);
    }
}
