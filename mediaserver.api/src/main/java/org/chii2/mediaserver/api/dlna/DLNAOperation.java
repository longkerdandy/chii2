package org.chii2.mediaserver.api.dlna;

/**
 * DLNA support of advanced transport operations.
 */
public class DLNAOperation {

    /**
     * Get DLNA supported advanced transport operations
     *
     * @param enableTimeSeekRange   Seek by Time Range
     * @param enableHttpHeaderRange Seek by Size Range (HTTP Range Header)
     * @return Operation Flag
     */
    public static String getDLNAOperation(boolean enableTimeSeekRange, boolean enableHttpHeaderRange) {
        if (enableTimeSeekRange && enableHttpHeaderRange) {
            return "DLNA.ORG_OP=11";
        } else if (!enableTimeSeekRange && enableHttpHeaderRange) {
            return "DLNA.ORG_OP=01";
        } else if (enableTimeSeekRange && !enableHttpHeaderRange) {
            return "DLNA.ORG_OP=10";
        } else {
            return "DLNA.ORG_OP=00";
        }
    }
}
