package org.chii2.medialibrary.provider.mediainfo.analyzer;

import static org.mockito.Mockito.*;

import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.slf4j.Logger;
import org.testng.annotations.Test;
import regex2.Pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Video MovieAnalyzer Test
 */
public class AnalyzerTest {
    private MovieAnalyzer analyzer = new MovieAnalyzer(null, null, null, null, null, null, null, null, null) {
        @SuppressWarnings("unused")
        private Logger logger = mock(Logger.class);
    };

    @Test
    public void parseFormatVersion() {
        assert analyzer.parseFormatVersion("1") == 1;
        assert analyzer.parseFormatVersion("Version 2") == 2;
        assert analyzer.parseFormatVersion("v3") == 3;
    }

    @Test
    public void parseFileNameTest() {
        // Mock Patterns
        Pattern diskNumPattern = Pattern.compile("\\w*(?<number>\\d+)");
        List<Pattern> moviePatterns = new ArrayList<Pattern>() {{
            add(Pattern.compile("^(?<name>[\\w\\.\\-\\']+)\\.\\(?(?<year>\\d{4})\\)?(?<info>(\\.\\w+)+)\\-\\[?(?<group>\\w+)\\]?\\.((?<disk>\\w+)\\.)?(?<ext>[\\w\\-]+)$", Pattern.CASE_INSENSITIVE));
        }};
        Pattern movieSeparatorPattern = Pattern.compile("[\\._]", Pattern.CASE_INSENSITIVE);
        Pattern movieSourcePattern = Pattern.compile("(?<source>BDRip|BluRay|HD-DVD|DVDRip|TVRip|HDTVRip|CAM|TS|DVDScr|Scr|R5)", Pattern.CASE_INSENSITIVE);
        Pattern movieVideoCodecPattern = Pattern.compile("(?<video_codec>XviD|DivX|DivX5|H264|X264)", Pattern.CASE_INSENSITIVE);
        Pattern movieAudioCodecPattern = Pattern.compile("(?<audio_codec>AC3|DTS)", Pattern.CASE_INSENSITIVE);

        // Mock Movie File
        MovieFile movieFile = mock(MovieFile.class);
        when(movieFile.getFileName()).thenReturn("Black.Swan.2010.DVDRiP.XViD-THC.avi");

        // Parse File Name
        analyzer.parseFileName(movieFile, moviePatterns, movieSeparatorPattern, movieSourcePattern, movieVideoCodecPattern, movieAudioCodecPattern, diskNumPattern);

        // Verify
        verify(movieFile).setMovieName("Black Swan");
        verify(movieFile).setYear(2010);
        verify(movieFile).setSource("DVDRiP");
        verify(movieFile).setVideoCodec("XViD");
    }

    @Test
    public void diskNumTest() {
        // Mock Pattern
        Pattern diskNumPattern = Pattern.compile("\\w*(?<number>\\d+)");

        // Test disk number parsing
        assert 1 == analyzer.parseDiskNum("CD1", diskNumPattern);
        assert 2 == analyzer.parseDiskNum("CD2", diskNumPattern);
        assert 1 == analyzer.parseDiskNum("DiskA", diskNumPattern);
        assert 2 == analyzer.parseDiskNum("DiskB", diskNumPattern);
    }
}
