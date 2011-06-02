package org.chii2.mediaserver.provider.sohu.factor.series;

/**
 * TV Series Origin
 */
public enum SeriesOrigin {
    ALL("p3"),
    CN("p3_u5185_u5730"),
    HK("p3_u6e2f_u5267"),
    TW("p3_u53f0_u5267"),
    KR("p3_u97e9_u5267"),
    US("p3_u7f8e_u5267"),
    TAI("p3_u6cf0_u5267"),
    OTHER("p3_u5176_u4ed6");

    private String value;

    SeriesOrigin (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SeriesOrigin parseValue(String value) {
        for (SeriesOrigin origin : SeriesOrigin.values()) {
            if (origin.getValue().equalsIgnoreCase(value)) {
                return origin;
            }
        }
        return ALL;
    }
}
