package org.chii2.medialibrary.file;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.chii2.medialibrary.file.filter.FileExtensionFilter;
import org.testng.annotations.Test;

/**
 * Test for File Extension Filter
 */
public class FileExtensionFilterTest {

    @Test
    public void FileNameExtensionTest() {
        List<String> imageExt = new ArrayList<>();
        imageExt.add(".jpg");
        imageExt.add(".png");
        imageExt.add(".gif");
        FileExtensionFilter imageFilter = new FileExtensionFilter(imageExt);

        assert imageFilter.accept(Paths.get("sample_image.jpg"));
        assert imageFilter.accept(Paths.get("sample_image.png"));
        assert imageFilter.accept(Paths.get("sample_image.gif"));
        assert !imageFilter.accept(Paths.get("sample_image.svg"));
        assert !imageFilter.accept(Paths.get("sample_image"));
        assert !imageFilter.accept(Paths.get(".sample_image"));
        assert !imageFilter.accept(Paths.get("sample_image."));
        assert imageFilter.accept(Paths.get("sample image.jpg"));
        assert imageFilter.accept(Paths.get("SAMPLE_IMAGE.JPG"));
    }
}
