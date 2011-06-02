package org.chii2.mediaserver.provider.sohu.factor.series;

/**
 * TV Series Year
 */
public enum SeriesYear {
    ALL("p4"),
    Y2011("p42011"),
    Y2010("p42010"),
    Y2009("p42009"),
    Y2008("p42008"),
    Y2007("p42007"),
    Y2006("p42006"),
    Y2005("p42005"),
    Y2004("p42004"),
    Y2003("p42003"),
    Y2002("p42002"),
    Y2001("p42001"),
    Y1990("p490"),
    Y1980("p480"),
    EARLIER("p4100");

    private String value;

    SeriesYear (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
