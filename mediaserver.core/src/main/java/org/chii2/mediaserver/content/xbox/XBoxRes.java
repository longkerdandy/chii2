package org.chii2.mediaserver.content.xbox;

import org.teleal.cling.support.model.Res;

/**
 * Extended Res for XBox36, major because XBox360 requires res@microsoft:codec
 */
public class XBoxRes extends Res {
    // res@microsoft:codec
    protected String microsoftCodec;

    public String getMicrosoftCodec() {
        return microsoftCodec;
    }

    public void setMicrosoftCodec(String microsoftCodec) {
        this.microsoftCodec = microsoftCodec;
    }
}
