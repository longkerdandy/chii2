package org.chii2.mediaserver.api.dlna;

/**
 * DLNA Flags
 */
public class DLNAFlags {
    // Flags
    public static final int DLNA_FLAG_SENDER_PACED = (1 << 31);
    public static final int DLNA_FLAG_TIME_BASED_SEEK = (1 << 30);
    public static final int DLNA_FLAG_BYTE_BASED_SEEK = (1 << 29);
    public static final int DLNA_FLAG_PLAY_CONTAINER = (1 << 28);
    public static final int DLNA_FLAG_S0_INCREASE = (1 << 27);
    public static final int DLNA_FLAG_SN_INCREASE = (1 << 26);
    public static final int DLNA_FLAG_RTSP_PAUSE = (1 << 25);
    public static final int DLNA_FLAG_STREAMING_TRANSFER_MODE = (1 << 24);
    public static final int DLNA_FLAG_INTERACTIVE_TRANSFERT_MODE = (1 << 23);
    public static final int DLNA_FLAG_BACKGROUND_TRANSFERT_MODE = (1 << 22);
    public static final int DLNA_FLAG_CONNECTION_STALL = (1 << 21);
    public static final int DLNA_FLAG_DLNA_V15 = (1 << 20);

    public static final String TRAILING_ZEROS = "000000000000000000000000";

    /**
     * Get DLNA Flags
     *
     * @param flags Supported Flags
     * @return DLNA Flags
     */
    public static String getDLNAFlags(int... flags) {
        if (flags.length > 0) {
            String format;
            int flag = flags[0];
            for (int i = 1; i < flags.length; i++) {
                flag = flag | flags[i];
            }
            format = Integer.toHexString(flag);
            if (format.length() < 8) {
                format = "0" + format;
            }
            return "DLNA.ORG_FLAGS=" + format + TRAILING_ZEROS;
        } else {
            return null;
        }
    }
}
