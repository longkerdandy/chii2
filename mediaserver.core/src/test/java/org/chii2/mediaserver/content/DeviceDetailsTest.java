package org.chii2.mediaserver.content;

import org.teleal.cling.model.profile.HeaderDeviceDetailsProvider;
import org.testng.annotations.Test;

/**
 * Device Details Test
 */
public class DeviceDetailsTest {

    @Test
    public void parseDeviceDetailsTest() {
        HeaderDeviceDetailsProvider.Key key = new HeaderDeviceDetailsProvider.Key("User-Agent", ".*Windows-Media-Player.*");
        assert key.isValuePatternMatch("Microsoft-Windows/6.1 UPnP/1.0 Windows-Media-Player/12.0.7600.16667 DLNADOC/1.50 (MS-DeviceCaps/1024)");
    }
}
