package org.chii2.util;

import org.testng.annotations.Test;

/**
 * Utils Test
 */
public class UtilsTest {

    @Test
    public void ChineseEncodingTest() {
        assert " ".equals(EncodingUtils.convertS2T(" "));
        assert "English Words".equals(EncodingUtils.convertS2T("English Words"));
        assert "搜狐視頻".equals(EncodingUtils.convertS2T("搜狐视频"));
        assert "搜狐视频".equals(EncodingUtils.convertT2S("搜狐視頻"));
    }
}
