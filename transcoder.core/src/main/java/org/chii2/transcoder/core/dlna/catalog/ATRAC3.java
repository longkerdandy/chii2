package org.chii2.transcoder.core.dlna.catalog;

import org.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.chii2.transcoder.core.dlna.restriction.AudioRestriction;
import org.teleal.cling.support.model.dlna.DLNAProfiles;

/**
 * ATRAC3
 */
public class ATRAC3 extends AudioCatalog {
    public ATRAC3() {
        // ATRAC3
        profileMap.put(DLNAProfiles.ATRAC3, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.ATRAC3)
        });
    }
}
