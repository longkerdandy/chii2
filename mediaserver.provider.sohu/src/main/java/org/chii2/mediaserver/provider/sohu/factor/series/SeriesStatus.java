package org.chii2.mediaserver.provider.sohu.factor.series;

/**
 * TV Series Status
 */
public enum SeriesStatus {
    ALL("p9-2"),
    NORMAL("p9-1"),
    PREVIEW("p9-3");

    private String value;

    SeriesStatus (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
