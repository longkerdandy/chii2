package org.chii2.transcoder.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.transcoder.api.core.BackgroundTranscoderService;
import org.chii2.transcoder.api.core.VideoTranscoderProfileSet;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Dictionary;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Background Transcoder Service
 */
public class BackgroundTranscoderServiceImpl implements BackgroundTranscoderService, EventHandler {
    // Request queue
    private BlockingQueue<VideoTranscoderProfileSet> queue;
    // Current Task
    private volatile Process currentTask;
    // Current Task Complement
    private volatile int currentTaskComplement;
    // Current Task Current Step
    private volatile int currentTaskCurrentStep;
    // Current Task Total Steps
    private volatile int currentTaskTotalSteps;
    // Current Task Description
    private volatile String currentTaskDescription;
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected EventAdmin service
    private EventAdmin eventAdmin;
    // Movie Transcoded Cache Directory
    private String movieTranscodedDirectory = System.getProperty("user.home") + "/Videos/.Transcoded/Movies";
    //Configuration File
    private final static String CONFIG_FILE = "org.chii2.transcoder.core";
    // Movie Transcoded Cache Directory Configuration Key
    private static final String MOVIE_TRANSCODED_DIRECTORY = "movie.transcoded.directory";
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.core");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Background Transcoder Service init.");
        Dictionary props = null;
        // Read properties from ConfigAdmin Service
        try {
            Configuration config = configAdmin.getConfiguration(CONFIG_FILE);
            props = config.getProperties();
        } catch (IOException e) {
            logger.error("Background Transcoder Service fail to load configuration with exception: {}.", ExceptionUtils.getMessage(e));
        }
        // Load each configuration
        if (props == null || props.isEmpty()) {
            logger.error("Background Transcoder Service load configuration <{}> with error.", CONFIG_FILE);
        } else {
            String movieTranscodedDirectory = ConfigUtils.loadConfiguration(props, MOVIE_TRANSCODED_DIRECTORY);
            if (StringUtils.isNotBlank(movieTranscodedDirectory)) {
                this.movieTranscodedDirectory = movieTranscodedDirectory;
                logger.debug("Background Transcoder Service configuration <{}> loaded.", MOVIE_TRANSCODED_DIRECTORY);
            } else {
                logger.error("Background Transcoder Service configuration <{}> is not valid.", MOVIE_TRANSCODED_DIRECTORY);
            }
        }

        // Init queue
        this.queue = new LinkedBlockingQueue<VideoTranscoderProfileSet>();
        // Start Background Transcoder Process Thread
        new Thread(new BackgroundVideoTranscoderProcessImpl(this.queue, this.eventAdmin, this)).start();
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Background Transcoder Service destroy.");
    }

    @Override
    public int getQueueLength() {
        return this.queue.size();
    }

    @Override
    public void setCurrentTask(Process process) {
        this.currentTask = process;
    }

    @Override
    public int getCurrentTaskStatus() {
        return this.currentTaskComplement;
    }

    @Override
    public void setCurrentTaskStatus(int percent) {
        this.currentTaskComplement = percent;
    }

    @Override
    public int getCurrentTaskCurrentStep() {
        return this.currentTaskCurrentStep;
    }

    @Override
    public void setCurrentTaskCurrentStep(int step) {
        this.currentTaskCurrentStep = step;
    }

    @Override
    public int getCurrentTaskTotalSteps() {
        return this.currentTaskTotalSteps;
    }

    @Override
    public void setCurrentTaskTotalSteps(int totalSteps) {
        this.currentTaskTotalSteps = totalSteps;
    }

    @Override
    public String getCurrentTaskDescription() {
        return this.currentTaskDescription;
    }

    @Override
    public void setCurrentTaskDescription(String description) {
        this.currentTaskDescription = description;
    }

    @Override
    public void stopCurrentTask() {
        if (this.currentTask != null) {
            this.currentTask.destroy();
        }
    }

    @Override
    public void transcoding(VideoTranscoderProfileSet profileSet) {
        try {
            this.queue.put(profileSet);
        } catch (InterruptedException e) {
            logger.error("Background Transcoder Service add new task with error: {}", ExceptionUtils.getMessage(e));
        }
    }

    @Override
    public void handleEvent(Event event) {
        // TODO: Handle more topics here
        if (MOVIE_TRANSCODED_FILE_REQUEST_TOPIC.equals(event.getTopic())) {
            VideoTranscoderProfileSet profileSet = (VideoTranscoderProfileSet) event.getProperty(TRANSCODER_PROFILE_PROPERTY);
            if (profileSet != null) {
                logger.debug("Receive a Video Transcoding Request for {}.", profileSet.getVideoID());
                this.transcoding(profileSet);
            }
        }
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
}
