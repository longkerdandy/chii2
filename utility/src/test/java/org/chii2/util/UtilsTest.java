package org.chii2.util;

import org.testng.annotations.Test;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Utils Test
 */
public class UtilsTest {

    @Test
    public void ChineseEncodingTest() {
        assert " ".equals(EncodingUtils.convertS2T(" "));
        assert "English Words".equals(EncodingUtils.convertS2T("English Words"));
        assert "\u641C\u72D0\u8996\u983B".equals(EncodingUtils.convertS2T("\u641C\u72D0\u89C6\u9891"));
        assert "\u641C\u72D0\u89C6\u9891".equals(EncodingUtils.convertT2S("\u641C\u72D0\u8996\u983B"));
    }

    @Test
    public void ConfigTest() {
        // Mock up
        Dictionary<String, String> dictionary = new Hashtable<>();
        dictionary.put("single", "C:\\Users\\LongkerDandy\\Pictures");
        dictionary.put("multiple", "C:\\Users\\LongkerDandy\\Pictures|C:\\Users\\Public\\Pictures");
        dictionary.put("regex", "^(?<name>[\\w\\.\\-\\']+)\\.\\(?(?<year>\\d{4})\\)?(?<info>(\\.\\w+)+)(\\-|\\.)\\[?(?<group>\\w+)\\]?\\.((?<disk>\\w+)\\.)?(?<ext>[\\w\\-]+)$");
        // Test
        assert ConfigUtils.loadConfigurations(dictionary, "single").size() == 1;
        assert ConfigUtils.loadConfigurations(dictionary, "multiple").size() == 2;
        assert ConfigUtils.loadPatterns(dictionary, "regex").size() == 1;
    }
}
