package org.chii2.medialibrary.provider.mediainfo.consumer;

import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.medialibrary.persistence.entity.MovieFileImpl;
import org.testng.annotations.Test;
import regex2.Pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Video Analyzer Test
 */
public class RequestConsumerTest {

    @Test
    public void parseFileNameTest() {
        // Mock
        RequestConsumer analyzer = new RequestConsumer(null, null, null, null, null, null, null, null, null);
        Pattern diskNumPattern = Pattern.compile("\\w*(?<number>\\d+)");
        List<Pattern> moviePatterns = new ArrayList<Pattern>() {{
            add(Pattern.compile("^(?<name>[\\w\\.\\-\\']+)\\.\\(?(?<year>\\d{4})\\)?(?<info>(\\.\\w+)+)\\-\\[?(?<group>\\w+)\\]?\\.((?<disk>\\w+)\\.)?(?<ext>[\\w\\-]+)$", Pattern.CASE_INSENSITIVE));
        }};
        Pattern movieSeparatorPattern = Pattern.compile("[\\._]", Pattern.CASE_INSENSITIVE);
        Pattern movieSourcePattern = Pattern.compile("(?<source>BDRip|BluRay|HD-DVD|DVDRip|TVRip|HDTVRip|CAM|TS|DVDScr|Scr|R5)", Pattern.CASE_INSENSITIVE);
        Pattern movieVideoCodecPattern = Pattern.compile("(?<video_codec>XviD|DivX|DivX5|H264|X264)", Pattern.CASE_INSENSITIVE);
        Pattern movieAudioCodecPattern = Pattern.compile("(?<audio_codec>AC3|DTS)", Pattern.CASE_INSENSITIVE);
        List<MovieFile> movieFiles = new ArrayList<MovieFile>();
        MovieFileImpl movieFile = new MovieFileImpl();
        movieFile.setFileName("Black.Swan.2010.DVDRiP.XViD-THC.avi");
        movieFiles.add(movieFile);

        // Test file name parsing
        List<MovieFile> result = analyzer.parseFileName(movieFiles, moviePatterns, movieSeparatorPattern, movieSourcePattern, movieVideoCodecPattern, movieAudioCodecPattern, diskNumPattern);
        assert result.get(0).getMovieName().equals("Black Swan");
        assert result.get(0).getYear() == 2010;
        assert result.get(0).getSource().equals("DVDRiP");
        assert result.get(0).getVideoCodec().equals("XViD");
    }

    @Test
    public void diskNumTest() {
        // Mock
        RequestConsumer analyzer = new RequestConsumer(null, null, null, null, null, null, null, null, null);
        Pattern diskNumPattern = Pattern.compile("\\w*(?<number>\\d+)");

        // Test disk number parsing
        assert 1 == analyzer.parseDiskNum("CD1", diskNumPattern);
        assert 2 == analyzer.parseDiskNum("CD2", diskNumPattern);
        assert 1 == analyzer.parseDiskNum("DiskA", diskNumPattern);
        assert 2 == analyzer.parseDiskNum("DiskB", diskNumPattern);
    }
}
