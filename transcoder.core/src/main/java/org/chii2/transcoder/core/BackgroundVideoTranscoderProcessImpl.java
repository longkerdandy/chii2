package org.chii2.transcoder.core;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.transcoder.api.core.BackgroundTranscoderService;
import org.chii2.transcoder.api.core.VideoTranscoderOutputReader;
import org.chii2.transcoder.api.core.VideoTranscoderProfile;
import org.chii2.transcoder.api.core.VideoTranscoderProfileSet;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Background Video Transcoder Process
 */
public class BackgroundVideoTranscoderProcessImpl implements Runnable {
    // Background Video Transcoder Profile Queue
    private BlockingQueue<VideoTranscoderProfileSet> queue;
    // Event Admin
    private EventAdmin eventAdmin;
    // Background Transcoder Service
    private BackgroundTranscoderService service;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.core");


    /**
     * Constructor
     *
     * @param queue      Background Video Transcoder Profile Queue
     * @param eventAdmin Event Admin
     * @param service    Background Transcoder Service
     */
    public BackgroundVideoTranscoderProcessImpl(BlockingQueue<VideoTranscoderProfileSet> queue, EventAdmin eventAdmin, BackgroundTranscoderService service) {
        this.queue = queue;
        this.eventAdmin = eventAdmin;
        this.service = service;
    }

    @SuppressWarnings({"InfiniteLoopStatement", "NullableProblems", "ConstantConditions"})
    @Override
    public void run() {
        try {
            while (true) {
                // Take from queue
                VideoTranscoderProfileSet profileSet = this.queue.take();

                // Update Description & Total Steps
                this.service.setCurrentTaskDescription(profileSet.getDescription());
                this.service.setCurrentTaskTotalSteps(profileSet.getProfiles().size());

                // Current Step
                int step = 0;
                // Whole Process Failed
                boolean failed = false;
                // Loop Profiles
                for (VideoTranscoderProfile profile : profileSet.getProfiles()) {
                    // Current Step + 1
                    step++;
                    // Process
                    Process process = null;
                    try {
                        // Commands
                        List<String> commands = profile.getCommands();
                        // Log
                        logger.info("Start a new background video tanscoding process with commands: {}.", commands);
                        // Start new process
                        // TODO: Maybe should read the error stream in another reader
                        process = new ProcessBuilder(commands).redirectErrorStream(true).start();

                        // Update Current Task & Step & Status
                        this.service.setCurrentTask(process);
                        this.service.setCurrentTaskStatus(0);
                        this.service.setCurrentTaskCurrentStep(step);

                        // Start output reader in another thread
                        VideoTranscoderOutputReader reader = profile.getOutputReader(process, this.service);
                        Thread thread = new Thread(reader);
                        thread.setDaemon(false);
                        thread.start();
                        // Wait for process end
                        int exitValue = process.waitFor();
                        // Deal with Exit Value
                        if (exitValue != 0) {
                            failed = true;
                            break;
                        }
                    } catch (IOException e) {
                        logger.warn("Background Video Transcoding with error: {}.", ExceptionUtils.getMessage(e));
                    } finally {
                        // Destroy process
                        if (process != null) {
                            process.destroy();
                        }
                    }
                }

                // Send Event
                if (!failed) {
                    this.postBackgroundVideoTranscodedProvidedEvent(profileSet);
                } else {
                    this.postBackgroundVideoTranscodedFailedEvent(profileSet);
                }

                // Update All
                this.service.setCurrentTask(null);
                this.service.setCurrentTaskStatus(0);
                this.service.setCurrentTaskCurrentStep(0);
                this.service.setCurrentTaskTotalSteps(0);
                this.service.setCurrentTaskDescription(null);
            }
        } catch (InterruptedException e) {
            logger.error("Background Video Transcoder Process with error: {}.", ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Send Background Video Transcoded Provided Event
     *
     * @param profileSet Transcoder Profile
     */
    private void postBackgroundVideoTranscodedProvidedEvent(VideoTranscoderProfileSet profileSet) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(BackgroundTranscoderService.VIDEO_ID_PROPERTY, profileSet.getVideoID());
        properties.put(BackgroundTranscoderService.VIDEO_FILE_PROPERTY, profileSet.getVideoFiles());
        properties.put(BackgroundTranscoderService.TRANSCODED_FILE_PROPERTY, profileSet.getTranscoderFiles());
        // Event Topic
        String topic = null;
        switch (profileSet.getVideoType()) {
            case MOVIE:
                topic = BackgroundTranscoderService.MOVIE_TRANSCODED_FILE_PROVIDED_TOPIC;
                break;
            // TODO: Add other Video Type support
        }
        // Send a event
        Event event = new Event(topic, properties);
        logger.debug("Send a {} transcoded file provided event.", profileSet.getVideoType());
        eventAdmin.postEvent(event);
    }

    /**
     * Send Background Video Transcoded Failed Event
     *
     * @param profileSet Transcoder Profile
     */
    private void postBackgroundVideoTranscodedFailedEvent(VideoTranscoderProfileSet profileSet) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(BackgroundTranscoderService.VIDEO_ID_PROPERTY, profileSet.getVideoID());
        properties.put(BackgroundTranscoderService.VIDEO_FILE_PROPERTY, profileSet.getVideoFiles());
        properties.put(BackgroundTranscoderService.TRANSCODED_FILE_PROPERTY, profileSet.getTranscoderFiles());
        // Event Topic
        String topic = null;
        switch (profileSet.getVideoType()) {
            case MOVIE:
                topic = BackgroundTranscoderService.MOVIE_TRANSCODED_FILE_FAILED_TOPIC;
                break;
            // TODO: Add other Video Type support
        }
        // Send a event
        Event event = new Event(topic, properties);
        logger.debug("Send a {} transcoded file failed event.", profileSet.getVideoType());
        eventAdmin.postEvent(event);
    }
}
