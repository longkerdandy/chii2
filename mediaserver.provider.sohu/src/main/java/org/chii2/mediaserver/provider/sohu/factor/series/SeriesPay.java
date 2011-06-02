package org.chii2.mediaserver.provider.sohu.factor.series;

/**
 * TV Series Pay
 */
public enum SeriesPay {
    ALL("p80"),
    FREE("p82"),
    PAY("p81"),
    MONTH("p83"),
    VOD("p84");

    private String value;

    SeriesPay (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
