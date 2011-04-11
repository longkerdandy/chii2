package org.chii2.mediaserver.library;

import org.chii2.mediaserver.content.common.CommonContentManager;
import org.testng.annotations.Test;

/**
 * Chii2 Media Library for Chii2 Media Server
 * Test
 */
public class ContentManagerTest {

    // Library service
    private CommonContentManager contentManger;

    /**
     * Constructor
     */
    public ContentManagerTest() {
        contentManger = new CommonContentManager(null, null, null);
    }

    @Test
    public void getContainerTitleTest() {
        assert "Singapore".equals(contentManger.getContainerTitle("PSFC-Singapore"));
        assert "Universal Studios".equals(contentManger.getContainerTitle("PSFC-Universal Studios"));
        assert "Shanghai".equals(contentManger.getContainerTitle("Shanghai"));
    }

    @Test
    public void getItemParentTest() {
        assert "PSFC-Singapore".equals(contentManger.getItemParentId("PI-PSFC-Singapore-1-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261"));
        assert "PSFC-Universal Studios".equals(contentManger.getItemParentId("PI-PSFC-Universal Studios-1-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261"));
    }

    @Test
    public void getItemLibraryIdTest() {
        assert "19f08e4e-b25e-4a2f-9e2b-55d2d40a4261".equals(contentManger.getItemLibraryId("PI-PSFC-Singapore-1-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261"));
        assert "19f08e4e-b25e-4a2f-9e2b-55d2d40a4261".equals(contentManger.getItemLibraryId("PI-PSFC-Universal Studios-1-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261"));
    }

    @Test
    public void forgeItemIdTest() {
        assert "PI-PSFC-Singapore-1-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261".equals(contentManger.forgeItemId("19f08e4e-b25e-4a2f-9e2b-55d2d40a4261", 1, "PSFC-Singapore", "PI-"));
        assert "PI-PSFC-Universal Studios-1-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261".equals(contentManger.forgeItemId("19f08e4e-b25e-4a2f-9e2b-55d2d40a4261", 1, "PSFC-Universal Studios", "PI-"));
    }

    @Test
    public void isPicturesStorageFolderTest() {
        assert contentManger.isPicturesStorageFolderContainer("PSFC-Singapore");
        assert contentManger.isPicturesStorageFolderContainer("PSFC-Universal Studios");
        assert !contentManger.isPicturesStorageFolderContainer("PI-PSFC-Singapore-1-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261");
        assert !contentManger.isPicturesStorageFolderContainer("PI-PSFC-Universal Studios-1-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261");
    }

    @Test
    public void isPhotoItemTest() {
        assert !contentManger.isPhotoItem("PSFC-Singapore");
        assert !contentManger.isPhotoItem("PSFC-Universal Studios");
        assert contentManger.isPhotoItem("PI-PSFC-Singapore-1-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261");
        assert contentManger.isPhotoItem("PI-PSFC-Universal Studios-1-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261");
    }
}
