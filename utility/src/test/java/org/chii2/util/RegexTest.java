package org.chii2.util;

import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java 7 Regex test
 */
public class RegexTest {

    @Test
    public void RegexGroupTest() {
        Pattern pattern = Pattern.compile("^(?<name>[\\w\\.\\-\\']+)\\.\\(?(?<year>\\d{4})\\)?(?<info>(\\.\\w+)+)\\-\\[?(?<group>\\w+)\\]?\\.((?<disk>\\w+)\\.)?(?<ext>[\\w\\-]+)$");
        Matcher matcher = pattern.matcher("Black.Swan.2010.DVDRiP.XViD-THC.avi");
        assert matcher.find();
        assert matcher.groupCount() > 0;
        assert matcher.group("name").equals("Black.Swan");
    }
}
