package org.chii2.medialibrary.provider.mediainfo;

import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.chii2.medialibrary.api.provider.MovieFileInfoProviderService;
import org.chii2.medialibrary.provider.mediainfo.analyzer.VideoAnalyzer;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import regex2.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * Movie File Information Provider Service based on MediaInfo
 */
public class MovieFileInfoProviderServiceImpl implements MovieFileInfoProviderService {
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin service
    private EventAdmin eventAdmin;
    // Injected Movie Factory
    private MovieFactory movieFactory;
    // Movie File Patterns
    private List<Pattern> moviePatterns = new ArrayList<Pattern>() {{
        add(Pattern.compile("^(?<name>[\\w\\.\\-\\']+)\\.\\(?(?<year>\\d{4})\\)?(?<info>(\\.\\w+)+)\\-\\[?(?<group>\\w+)\\]?\\.((?<disk>\\w+)\\.)?(?<ext>[\\w\\-]+)$", Pattern.CASE_INSENSITIVE));
    }};
    // Movie Name Separator Pattern
    private Pattern movieSeparatorPattern = Pattern.compile("[\\._]", Pattern.CASE_INSENSITIVE);
    // Movie Source Pattern
    private Pattern movieSourcePattern = Pattern.compile("(?<source>BDRip|BluRay|HD-DVD|DVDRip|TVRip|HDTVRip|CAM|TS|DVDScr|Scr|R5)", Pattern.CASE_INSENSITIVE);
    // Movie Video Codec Pattern
    private Pattern movieVideoCodecPattern = Pattern.compile("(?<video_codec>XviD|DivX|DivX5|H264|X264)", Pattern.CASE_INSENSITIVE);
    // Movie Audio Codec Pattern
    private Pattern movieAudioCodecPattern = Pattern.compile("(?<audio_codec>AC3|DTS)", Pattern.CASE_INSENSITIVE);
    // Movie Video Resolution Pattern
    private Pattern movieResolutionPattern = Pattern.compile("(?<resolution>\\d+p)", Pattern.CASE_INSENSITIVE);
    // Extract Disk Number Pattern
    private Pattern diskNumPattern = Pattern.compile("\\w*(?<number>\\d+)");
    //Configuration FIle
    private final static String CONFIG_FILE = "org.chii2.medialibrary.provider.mediainfo";
    // Movie file name extract filter key from configuration file
    private final static String MOVIE_FILE_PATTERN = "movie.file.pattern";
    // Movie file name separator key from configuration file
    private final static String MOVIE_NAME_SEPARATOR_PATTERN = "movie.file.name.separator";
    // Movie file source block extract filter key from configuration file
    private final static String MOVIE_SOURCE_PATTERN = "movie.file.source.pattern";
    // Movie file video codec block extract filter key from configuration file
    private final static String MOVIE_VIDEO_CODEC_PATTERN = "movie.file.video.codec.pattern";
    // Movie file audio codec block extract filter key from configuration file
    private final static String MOVIE_AUDIO_CODEC_PATTERN = "movie.file.audio.codec.pattern";
    // Movie file video resolution block extract filter key from configuration file
    private final static String MOVIE_RESOLUTION_PATTERN = "movie.file.video.resolution.pattern";
    // Movie file disk number extract filter key from configuration file
    private final static String MOVIE_DISK_NUMBER_PATTERN = "movie.file.disk.number.pattern";
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.mediainfo");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("MediaInfo Provider (Movie File Information) init.");
        Dictionary props = null;
        // Read properties from ConfigAdmin Service
        try {
            Configuration config = configAdmin.getConfiguration(CONFIG_FILE);
            props = config.getProperties();
        } catch (IOException e) {
            logger.error("MediaInfo Provider fail to load configuration with exception: {}.", e.getMessage());
        }
        // Load each configuration
        if (props == null || props.isEmpty()) {
            logger.error("MediaInfo Provider load configuration <{}> with error.", CONFIG_FILE);
        } else {
            // Load movie file name parse patterns
            List<Pattern> namePatterns = ConfigUtils.loadPatterns(props, MOVIE_FILE_PATTERN);
            if (namePatterns != null && !namePatterns.isEmpty()) {
                moviePatterns = namePatterns;
                logger.debug("MediaInfo Provider configuration <{}> loaded.", MOVIE_FILE_PATTERN);
            } else {
                logger.error("MediaInfo Provider configuration <{}> is not valid.", MOVIE_FILE_PATTERN);
            }

            // Load movie name separator pattern
            Pattern fileSeparatorPattern = ConfigUtils.loadPattern(props, MOVIE_NAME_SEPARATOR_PATTERN);
            if (fileSeparatorPattern != null) {
                movieSeparatorPattern = fileSeparatorPattern;
                logger.debug("MediaInfo Provider configuration <{}> loaded.", MOVIE_NAME_SEPARATOR_PATTERN);
            } else {
                logger.error("MediaInfo Provider configuration <{}> is not valid.", MOVIE_NAME_SEPARATOR_PATTERN);
            }

            // Load movie source pattern
            Pattern sourcePattern = ConfigUtils.loadPattern(props, MOVIE_SOURCE_PATTERN);
            if (sourcePattern != null) {
                movieSourcePattern = sourcePattern;
                logger.debug("MediaInfo Provider configuration <{}> loaded.", MOVIE_SOURCE_PATTERN);
            } else {
                logger.error("MediaInfo Provider configuration <{}> is not valid.", MOVIE_SOURCE_PATTERN);
            }

            // Load movie video codec pattern
            Pattern videoCodecPattern = ConfigUtils.loadPattern(props, MOVIE_VIDEO_CODEC_PATTERN);
            if (videoCodecPattern != null) {
                movieVideoCodecPattern = videoCodecPattern;
                logger.debug("MediaInfo Provider configuration <{}> loaded.", MOVIE_VIDEO_CODEC_PATTERN);
            } else {
                logger.error("MediaInfo Provider configuration <{}> is not valid.", MOVIE_VIDEO_CODEC_PATTERN);
            }

            // Load movie audio codec pattern
            Pattern audioCodecPattern = ConfigUtils.loadPattern(props, MOVIE_AUDIO_CODEC_PATTERN);
            if (audioCodecPattern != null) {
                movieAudioCodecPattern = audioCodecPattern;
                logger.debug("MediaInfo Provider configuration <{}> loaded.", MOVIE_AUDIO_CODEC_PATTERN);
            } else {
                logger.error("MediaInfo Provider configuration <{}> is not valid.", MOVIE_AUDIO_CODEC_PATTERN);
            }

            // Load movie video resolution pattern
            Pattern videoResolutionPattern = ConfigUtils.loadPattern(props, MOVIE_RESOLUTION_PATTERN);
            if (videoResolutionPattern != null) {
                movieResolutionPattern = videoResolutionPattern;
                logger.debug("MediaInfo Provider configuration <{}> loaded.", MOVIE_RESOLUTION_PATTERN);
            } else {
                logger.error("MediaInfo Provider configuration <{}> is not valid.", MOVIE_RESOLUTION_PATTERN);
            }

            // Load movie disk number pattern
            Pattern diskNumPattern = ConfigUtils.loadPattern(props, MOVIE_DISK_NUMBER_PATTERN);
            if (diskNumPattern != null) {
                this.diskNumPattern = diskNumPattern;
                logger.debug("MediaInfo Provider configuration <{}> loaded.", MOVIE_DISK_NUMBER_PATTERN);
            } else {
                logger.error("MediaInfo Provider configuration <{}> is not valid.", MOVIE_DISK_NUMBER_PATTERN);
            }
        }
    }

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("MediaInfo Provider (Movie File Information) destroy.");
    }

    @Override
    public String getProviderName() {
        return "MediaInfo";
    }

    @Override
    public void getMovieFileInformation(File movieFile, List<String> excludeProviders) {
        List<File> fileList = new ArrayList<File>();
        fileList.add(movieFile);
        getMovieFileInformation(fileList, excludeProviders);
    }

    @Override
    public void getMovieFileInformation(List<File> movieFiles, List<String> excludeProviders) {
        logger.debug("MediaInfo Provider start a new MediaInfo Movie File Parser thread.");
        Thread movieParseThread = new Thread(new VideoAnalyzer(movieFiles, eventAdmin, movieFactory, excludeProviders, moviePatterns, movieSeparatorPattern, movieSourcePattern, movieVideoCodecPattern, movieAudioCodecPattern, diskNumPattern));
        movieParseThread.setDaemon(false);
        movieParseThread.start();
    }

    /**
     * Inject ConfigurationAdmin service
     *
     * @param configAdmin ConfigurationAdmin service
     */
    @SuppressWarnings("unused")
    public void setConfigAdmin(ConfigurationAdmin configAdmin) {
        this.configAdmin = configAdmin;
    }

    /**
     * Inject EventAdmin service
     *
     * @param eventAdmin EventAdmin service
     */
    @SuppressWarnings("unused")
    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    /**
     * Inject Movie Factory
     *
     * @param movieFactory Movie Factory
     */
    @SuppressWarnings("unused")
    public void setMovieFactory(MovieFactory movieFactory) {
        this.movieFactory = movieFactory;
    }
}
