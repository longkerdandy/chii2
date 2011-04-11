package org.chii2.medialibrary.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.chii2.medialibrary.file.consumer.FileExtensionFilter;
import org.testng.annotations.Test;

/**
 * Test for File Extension Filter
 */
public class FileExtensionFilterTest {

    @Test
    public void FileNameExtensionTest() {
        List<String> imageExt = new ArrayList<String>();
        imageExt.add(".jpg");
        imageExt.add(".png");
        imageExt.add(".gif");
        FileExtensionFilter imageFilter = new FileExtensionFilter(imageExt);

        assert imageFilter.accept(new File("sample_image.jpg"));
        assert imageFilter.accept(new File("sample_image.png"));
        assert imageFilter.accept(new File("sample_image.gif"));
        assert !imageFilter.accept(new File("sample_image.svg"));
        assert !imageFilter.accept(new File("sample_image"));
        assert !imageFilter.accept(new File(".sample_image"));
        assert !imageFilter.accept(new File("sample_image."));
        assert imageFilter.accept(new File("sample image.jpg"));
        assert imageFilter.accept(new File("SAMPLE_IMAGE.JPG"));
    }
}
