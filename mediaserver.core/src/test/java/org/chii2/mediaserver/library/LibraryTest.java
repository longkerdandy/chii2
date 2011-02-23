package org.chii2.mediaserver.library;

import org.testng.annotations.Test;

/**
 * Chii2 Media Library for Chii2 Media Server
 * Test
 */
public class LibraryTest {

    // Library service
    private LibraryImpl library;

    /**
     * Constructor
     */
    public LibraryTest() {
        library = new LibraryImpl(null, null);
    }

    @Test
    public void getContainerTitleTest() {
        assert "Singapore".equals(library.getContainerTitle("PSFC-Singapore"));
        assert "Universal Studios".equals(library.getContainerTitle("PSFC-Universal Studios"));
        assert "Shanghai".equals(library.getContainerTitle("Shanghai"));
    }

    @Test
    public void getItemParentTest() {
        assert "PSFC-Singapore".equals(library.getItemParent("PI-PSFC-Singapore-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261"));
        assert "PSFC-Universal Studios".equals(library.getItemParent("PI-PSFC-Universal Studios-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261"));
    }

    @Test
    public void getItemLibraryIdTest() {
        assert "19f08e4e-b25e-4a2f-9e2b-55d2d40a4261".equals(library.getItemLibraryId("PI-PSFC-Singapore-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261"));
        assert "19f08e4e-b25e-4a2f-9e2b-55d2d40a4261".equals(library.getItemLibraryId("PI-PSFC-Universal Studios-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261"));
    }

    @Test
    public void forgeItemIdTest() {
        assert "PI-PSFC-Singapore-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261".equals(library.forgeItemId("19f08e4e-b25e-4a2f-9e2b-55d2d40a4261", "PSFC-Singapore", "PI-"));
        assert "PI-PSFC-Universal Studios-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261".equals(library.forgeItemId("19f08e4e-b25e-4a2f-9e2b-55d2d40a4261", "PSFC-Universal Studios", "PI-"));
    }

    @Test
    public void isPicturesStorageFolderTest() {
        assert library.isPicturesStorageFolder("PSFC-Singapore");
        assert library.isPicturesStorageFolder("PSFC-Universal Studios");
        assert !library.isPicturesStorageFolder("PI-PSFC-Singapore-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261");
        assert !library.isPicturesStorageFolder("PI-PSFC-Universal Studios-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261");
    }

    @Test
    public void isPhotoItemTest() {
        assert !library.isPhotoItem("PSFC-Singapore");
        assert !library.isPhotoItem("PSFC-Universal Studios");
        assert library.isPhotoItem("PI-PSFC-Singapore-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261");
        assert library.isPhotoItem("PI-PSFC-Universal Studios-19f08e4e-b25e-4a2f-9e2b-55d2d40a4261");
    }
}
