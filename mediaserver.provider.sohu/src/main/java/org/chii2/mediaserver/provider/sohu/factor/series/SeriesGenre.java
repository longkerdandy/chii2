package org.chii2.mediaserver.provider.sohu.factor.series;

/**
 * TV Series Genre
 */
public enum SeriesGenre {
    ALL("p2"),
    IDOL("p2_u5076_u50cf_u5267"),
    MORAL("p2_u5bb6_u5ead_u4f26_u7406_u5267"),
    HISTORY("p2_u5386_u53f2_u5267"),
    AGE("p2_u5e74_u4ee3_u5267"),
    LOVE("p2_u8a00_u60c5_u5267"),
    MARTIAL("p2_u6b66_u4fa0_u5267"),
    ANCIENT("p2_u53e4_u88c5_u5267"),
    CITY("p2_u90fd_u5e02_u5267"),
    COUNTRYSIDE("p2_u519c_u6751_u5267"),
    WAR("p2_u519b_u4e8b_u6218_u4e89_u5267"),
    SUSPENSE("p2_u60ac_u7591_u5267"),
    SF("p2_u5947_u5e7b_u79d1_u5e7b_u5267"),
    ACTION("p2_u52a8_u4f5c_u5267"),
    SPY("p2_u8c0d_u6218_u5267"),;

    private String value;

    SeriesGenre(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
