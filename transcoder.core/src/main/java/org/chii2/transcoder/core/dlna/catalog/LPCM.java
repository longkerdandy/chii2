package org.chii2.transcoder.core.dlna.catalog;

import org.chii2.transcoder.core.dlna.codec.AudioCodec;
import org.chii2.transcoder.core.dlna.restriction.AudioRestriction;
import org.teleal.cling.support.model.dlna.DLNAProfiles;

/**
 * LPCM
 */
public class LPCM extends AudioCatalog {
    public LPCM() {
        // LPCM Low
        profileMap.put(DLNAProfiles.LPCM_LOW, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.PCM),
                new SampleBitRateRangeRestriction(8000, 32000),
                new ChannelRestriction(1, 2)
        });

        // LPCM
        profileMap.put(DLNAProfiles.LPCM, new AudioRestriction[]{
                new AudioCodecRestriction(AudioCodec.PCM),
                new SampleBitRateRangeRestriction(8000, 48000),
                new ChannelRestriction(1, 2)
        });
    }
}
