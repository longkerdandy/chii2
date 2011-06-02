package org.chii2.mediaserver.provider.sohu.factor.series;

/**
 * TV Series Sort
 */
public enum SeriesSort {
    RELATED("p7"),
    DAY("p75"),
    WEEK("p77"),
    TOTAL("p71"),
    NEW("p73"),
    RATE("p74");

    private String value;

    SeriesSort (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
