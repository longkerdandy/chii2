package org.chii2.medialibrary.file.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 *  FileExtensionFilter is used to filer the file name with FileScanner
 */
public class FileExtensionFilter implements FileFilter {

    // Acceptable File Extensions
    private List<String> acceptableExtensions;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.file.filter");

    public FileExtensionFilter(List<String> acceptableExtensions) {
        this.acceptableExtensions = acceptableExtensions;
    }

    @Override
    public boolean accept(File file) {
        // If is directory, accept to go into
        if (file.isDirectory()) {
            return true;
        }
        // Empty file
        if (file.getName().isEmpty()) {
            logger.debug("File is rejected with empty name.");
            return false;
        }
        // Test file extension
        for (String ext : acceptableExtensions) {
            if(StringUtils.isNotEmpty(ext)) {
                String name = file.getName();
                int dotIndex = name.lastIndexOf('.');
                if (dotIndex > 0 && name.substring(dotIndex) .equalsIgnoreCase(ext)) {
                    logger.debug("File <{}> is accepted.", file.getName());
                    return  true;
                }
            }
        }
        // Should not reach, in case just reject
        logger.debug("File <{}> is rejected.", file.getName());
        return false;
    }

}
