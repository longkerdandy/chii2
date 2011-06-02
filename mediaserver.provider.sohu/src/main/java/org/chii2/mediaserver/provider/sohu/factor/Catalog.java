package org.chii2.mediaserver.provider.sohu.factor;

/**
 * Sohu Online Video Catalog
 */
public enum Catalog {
    MOVIE("p11"),
    SERIES("p12"),
    ANIME("p116"),
    ART("p17"),
    DOCUMENTARY("p18"),
    EDUCATION("p121"),
    NEWS("p113"),
    MUSICAL("p124");

    private String value;

    Catalog (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Catalog parseValue(String value) {
        for (Catalog catalog : Catalog.values()) {
            if (catalog.getValue().equalsIgnoreCase(value)) {
                return catalog;
            }
        }
        return SERIES;
    }
}
