package org.chii2.transcoder.api.core;

/**
 * Background Transcoder Service
 */
public interface BackgroundTranscoderService {
    // Movie Transcoded File Request Topic
    // This topic will be listened by Background Transcoder Service, and every request will be push into the queue
    // The transcoding result will be send to topic MOVIE_TRANSCODED_FILE_PROVIDED_TOPIC or topic MOVIE_TRANSCODED_FILE_FAILED_TOPIC
    public final static String MOVIE_TRANSCODED_FILE_REQUEST_TOPIC = "org/chii2/medialibrary/transcoder/movie/FILE_REQUEST";
    // Transcoder Profile property key in the Transcoded File REQUEST event, this should be a Video Transcoder Profile Set
    public final static String TRANSCODER_PROFILE_PROPERTY = "transcoder_profile";

    // Movie Transcoded File Provided Topic
    // Background Video Transcoder Process will send the transcoding result to this topic, if transcoding successful
    public final static String MOVIE_TRANSCODED_FILE_PROVIDED_TOPIC = "org/chii2/medialibrary/transcoder/movie/FILE_PROVIDED";
    // Movie Transcoded File Failed Topic
    // Background Video Transcoder Process will send the transcoding result to this topic, if transcoding failed
    public final static String MOVIE_TRANSCODED_FILE_FAILED_TOPIC = "org/chii2/medialibrary/transcoder/movie/FILE_FAILED";
    // Video ID property key in the Transcoded File PROVIDED & FAILED events, this should be a UUID String
    public final static String VIDEO_ID_PROPERTY = "video_id";
    // Video File property key in the Transcoded File PROVIDED & FAILED events, this should be a List of File
    public final static String VIDEO_FILE_PROPERTY = "video_file";
    // Transcoded File property key in the Transcoded File PROVIDED & FAILED event, this should be a List of File
    public final static String TRANSCODED_FILE_PROPERTY = "transcoded_file";

    /**
     * Get the length of the queue (the number of remaining tasks)
     *
     * @return Length of the Queue
     */
    public int getQueueLength();

    /**
     * Set Current Task
     *
     * @param process Current Task
     */
    public void setCurrentTask(Process process);

    /**
     * Get the complement of current task in percent (0 - 100)
     *
     * @return Complete percent (0 - 100)
     */
    public int getCurrentTaskStatus();

    /**
     * Set the complement of current task in percent (0 - 100)
     *
     * @param percent Complete percent (0 - 100)
     */
    public void setCurrentTaskStatus(int percent);

    /**
     * Get the Current Step of Current Task (current profile in the set)
     *
     * @return Current Step
     */
    public int getCurrentTaskCurrentStep();

    /**
     * Set the Current Step of Current Task (current profile in the set)
     *
     * @param step Current Step
     */
    public void setCurrentTaskCurrentStep(int step);

    /**
     * Get the Total Steps of Current Task (total profiles in the set)
     *
     * @return Total Steps
     */
    public int getCurrentTaskTotalSteps();

    /**
     * Set the Total Steps of Current Task (total profiles in the set)
     *
     * @param totalSteps Total Steps
     */
    public void setCurrentTaskTotalSteps(int totalSteps);

    /**
     * Get the current task description
     *
     * @return Current Task Description
     */
    public String getCurrentTaskDescription();

    /**
     * Set the current task description
     *
     * @param description Current Task Description
     */
    public void setCurrentTaskDescription(String description);

    /**
     * Stop Current task
     */
    public void stopCurrentTask();

    /**
     * Transcoding video in background thread, based on the Transcoder Profile
     *
     * @param profileSet Video Transcoder Profile Set
     */
    public void transcoding(VideoTranscoderProfileSet profileSet);
}
