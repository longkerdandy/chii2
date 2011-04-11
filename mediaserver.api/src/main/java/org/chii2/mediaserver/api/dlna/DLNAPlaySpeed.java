package org.chii2.mediaserver.api.dlna;

/**
 * DLNA Play Speed
 */
public class DLNAPlaySpeed {

    /**
     * Get DLNA Play Speed
     *
     * @param speeds Support Speeds
     * @return DLNA Play Speed
     */
    public static String getDLNAPlaySpeed(int... speeds) {
        StringBuilder ps = new StringBuilder("DLNA.ORG_PS=");
        boolean hasValue = false;
        for (int speed : speeds) {
            if (speed != 1) {
                if (!hasValue) {
                    hasValue = true;
                    ps.append(speed);
                } else {
                    ps.append(",");
                    ps.append(speed);
                }
            }
        }
        if (hasValue) {
            return ps.toString();
        } else {
            return null;
        }
    }
}
