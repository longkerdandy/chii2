package org.chii2.medialibrary.file.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.file.LinkOption.*;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 *  FileExtensionFilter is used to filer the file name with FileScanner
 */
public class FileExtensionFilter implements DirectoryStream.Filter<Path> {
    // Acceptable File Extensions
    private final List<String> acceptableExtensions;
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.file.filter");

    /**
     * Constructor
     * @param acceptableExtensions Acceptable Extensions
     */
    public FileExtensionFilter(List<String> acceptableExtensions) {
        this.acceptableExtensions = acceptableExtensions;
    }

    @Override
    public boolean accept(Path path) {
        // Directory, accept to go into
        if (Files.isDirectory(path, NOFOLLOW_LINKS)) {
            return true;
        }

        // Test with acceptable file extensions
        for (String ext : acceptableExtensions) {
            if(StringUtils.isNotEmpty(ext)) {
                String name = path.getFileName().toString();
                int dotIndex = name.lastIndexOf('.');
                if (dotIndex > 0 && name.substring(dotIndex) .equalsIgnoreCase(ext)) {
                    logger.debug("File {} is accepted.", path);
                    return  true;
                }
            }
        }

        // Not match, reject
        logger.debug("File {} is rejected.", path);
        return false;
    }
}
