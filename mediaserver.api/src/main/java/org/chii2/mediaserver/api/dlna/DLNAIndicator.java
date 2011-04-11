package org.chii2.mediaserver.api.dlna;

/**
 * DLNA Conversion Indicator
 */
public class DLNAIndicator {

    /**
     * Get DLNA Conversion Indicator
     *
     * @param transcoded Content transcoded or not
     * @return DLNA Conversion Indicator
     */
    public static String getDLNAConversionIndicator(boolean transcoded) {
        if (transcoded) {
            return "DLNA.ORG_CI=1";
        } else {
            return null;
        }
    }
}
