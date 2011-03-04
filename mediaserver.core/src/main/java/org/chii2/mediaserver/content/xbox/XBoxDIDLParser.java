package org.chii2.mediaserver.content.xbox;

import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.DescMeta;
import org.teleal.cling.support.model.Res;

import org.teleal.cling.support.model.item.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.teleal.cling.model.XMLUtil.appendNewElement;
import static org.teleal.cling.model.XMLUtil.appendNewElementIfNotNull;

/**
 * DIDL Parser for XBox360
 */
public class XBoxDIDLParser extends DIDLParser {

    @Override
    protected void generateItem(Item item, Document descriptor, Element parent) {

        if (item.getTitle() == null) {
            throw new RuntimeException("Missing 'dc:title' element for item: " + item.getId());
        }
        if (item.getClazz() == null) {
            throw new RuntimeException("Missing 'upnp:class' element for item: " + item.getId());
        }

        Element itemElement = appendNewElement(descriptor, parent, "item");

        if (item.getId() == null)
            throw new NullPointerException("Missing id on item: " + item);
        itemElement.setAttribute("id", item.getId());

        if (item.getParentID() == null)
            throw new NullPointerException("Missing parent id on item: " + item);
        itemElement.setAttribute("parentID", item.getParentID());

        if (item.getRefID() != null)
            itemElement.setAttribute("refID", item.getRefID());

        // CHANGE: restricted "true" "false" to "1" "0"
        if (item.isRestricted()) {
            itemElement.setAttribute("restricted", "1");
        } else {
            itemElement.setAttribute("restricted", "0");
        }


        appendNewElementIfNotNull(
                descriptor,
                itemElement,
                "dc:title",
                item.getTitle(),
                DIDLObject.Property.DC.NAMESPACE.URI
        );

        appendNewElementIfNotNull(
                descriptor,
                itemElement,
                "dc:creator",
                item.getCreator(),
                DIDLObject.Property.DC.NAMESPACE.URI
        );

        appendNewElementIfNotNull(
                descriptor,
                itemElement,
                "upnp:writeStatus",
                item.getWriteStatus(),
                DIDLObject.Property.UPNP.NAMESPACE.URI
        );

        appendClass(descriptor, itemElement, item.getClazz(), "upnp:class", false);

        // ADD: upnp:albumArtUri
        appendAlbumArtUri(descriptor, itemElement, item);

        appendProperties(descriptor, itemElement, item, "upnp", DIDLObject.Property.UPNP.NAMESPACE.class, DIDLObject.Property.UPNP.NAMESPACE.URI);
        appendProperties(descriptor, itemElement, item, "dc", DIDLObject.Property.DC.NAMESPACE.class, DIDLObject.Property.DC.NAMESPACE.URI);

        for (Res resource : item.getResources()) {
            if (resource == null) continue;
            generateResource(resource, descriptor, itemElement);
        }

        for (DescMeta descMeta : item.getDescMetadata()) {
            if (descMeta == null) continue;
            generateDescMetadata(descMeta, descriptor, itemElement);
        }
    }

    protected void appendAlbumArtUri(Document descriptor, Element itemElement, Item item) {
        Element albumArtUriElement = appendNewElement(descriptor, itemElement, "upnp:albumArtURI", "http://192.168.1.3:8888/xboxthumb/262985495/0_e0ZBNzAwNEM4LTc4NjgtNDNFRC1CNTkyLTYxNDE1MTE1MzM4NX0uMC4yQzE5NTgxOQ.wmv?albumArt=true");
        albumArtUriElement.setAttribute("xmlns:dlna", "urn:schemas-dlna-org:metadata-1-0/");
        albumArtUriElement.setAttribute("dlna:profileID", "JPEG_SM");
    }

    @Override
    protected void generateResource(Res resource, Document descriptor, Element parent) {
        // Call super
        if (resource.getValue() == null) {
            throw new RuntimeException("Missing resource URI value" + resource);
        }
        if (resource.getProtocolInfo() == null) {
            throw new RuntimeException("Missing resource protocol info: " + resource);
        }

        Element resourceElement = appendNewElement(descriptor, parent, "res", resource.getValue());
        resourceElement.setAttribute("protocolInfo", resource.getProtocolInfo().toString());
        if (resource.getImportUri() != null)
            resourceElement.setAttribute("importUri", resource.getImportUri().toString());
        if (resource.getSize() != null)
            resourceElement.setAttribute("size", resource.getSize().toString());
        if (resource.getDuration() != null)
            resourceElement.setAttribute("duration", resource.getDuration());
        if (resource.getBitrate() != null)
            resourceElement.setAttribute("bitrate", resource.getBitrate().toString());
        if (resource.getSampleFrequency() != null)
            resourceElement.setAttribute("sampleFrequency", resource.getSampleFrequency().toString());
        if (resource.getBitsPerSample() != null)
            resourceElement.setAttribute("bitsPerSample", resource.getBitsPerSample().toString());
        if (resource.getNrAudioChannels() != null)
            resourceElement.setAttribute("nrAudioChannels", resource.getNrAudioChannels().toString());
        if (resource.getColorDepth() != null)
            resourceElement.setAttribute("colorDepth", resource.getColorDepth().toString());
        if (resource.getProtection() != null)
            resourceElement.setAttribute("protection", resource.getProtection());
        if (resource.getResolution() != null)
            resourceElement.setAttribute("resolution", resource.getResolution());

        // XBox Res
        if (resource instanceof XBoxRes) {
            XBoxRes xboxResource = (XBoxRes) resource;
            if (xboxResource.getMicrosoftCodec() != null) {
                resourceElement.setAttribute("microsoft:codec", xboxResource.getMicrosoftCodec());
                resourceElement.setAttribute("xmlns:microsoft", "urn:schemas-microsoft-com:WMPNSS-1-0/");
            }
        }
    }
}
