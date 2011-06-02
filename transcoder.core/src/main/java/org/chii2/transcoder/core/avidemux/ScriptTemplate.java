package org.chii2.transcoder.core.avidemux;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * AviDemux Script Template
 */
public class ScriptTemplate {

    // Input Files
    private List<File> inputFiles;
    // Output File
    private File outputFile;
    // Script File
    private File scriptFile;
    // Video Codec Config
    private VideoConfig videoConfig;
    // Audio Codec Config
    private AudioConfig audioConfig;

    // Total Frames
    private int totalFrames;
    // Begin Frames
    private int beginFrames;
    // End Frames
    private int endFrames;
    // FPS * 1000
    private int fps1000;
    // Video Width
    private int videoWidth = -1;
    // Video Height
    private int videoHeight = -1;
    // Audio Normalize Mode 1 (normalize activated), 0 (not activated)
    private int audioNormalizeMode = 0;
    // Audio Normalize Value
    private int audioNormalizeValue = 0;
    // Audio Delay in ms
    private int audioDelay = 0;
    // Audio Mixer
    private String audioMixer = "NONE";
    // Audio ReSampling
    private int audioReSampling = -1;
    // Container
    private String container;

    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.avidemux");


    /**
     * Constructor
     *
     * @param inputFiles  Input Media Files
     * @param outputFile  Output Media Files
     * @param scriptFile  Script File
     * @param videoConfig Video Codec Config
     * @param audioConfig Audio Codec Config
     */
    public ScriptTemplate(List<File> inputFiles, File outputFile, File scriptFile, VideoConfig videoConfig, AudioConfig audioConfig) {
        this.inputFiles = inputFiles;
        this.outputFile = outputFile;
        this.scriptFile = scriptFile;
        this.videoConfig = videoConfig;
        this.audioConfig = audioConfig;
    }

    /**
     * Generate the AviDemux Script File
     */
    public boolean generate() {
        try {
            // Delete old file, if exist
            if (scriptFile.exists()) {
                FileUtils.forceDelete(scriptFile);
            }
            // Create Parent Directory
            if (scriptFile.getParentFile() != null && !scriptFile.getParentFile().exists()) {
                FileUtils.forceMkdir(scriptFile.getParentFile());
            }
            // Create Empty Script File
            if (!scriptFile.createNewFile()) {
                logger.error("AviDemux can't create script file: {}.", scriptFile.getAbsolutePath());
                return false;
            }
            // Write Script File
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(scriptFile), 4096);
            bufferedWriter.append("//AD  <- Needed to identify//\n");
            bufferedWriter.append("\n");
            bufferedWriter.append("app.load(\"" + inputFiles.get(0).getAbsolutePath() + "\");\n");
            for (int i = 1; i < inputFiles.size(); i++) {
                bufferedWriter.append("app.append(\"" + inputFiles.get(i).getAbsolutePath() + "\");\n");
            }
            bufferedWriter.append("app.clearSegments();\n");
            bufferedWriter.append("app.addSegment(0,0," + totalFrames + ");\n");
            bufferedWriter.append("app.markerA=" + beginFrames + ";\n");
            bufferedWriter.append("app.markerB=" + endFrames + ";\n");
            bufferedWriter.append("app.video.setPostProc(3,3,0);\n");
            bufferedWriter.append("app.video.fps1000=" + fps1000 + ";\n");
            if (videoWidth > 0 && videoHeight > 0)
                bufferedWriter.append("app.video.addFilter(\"mpresize\",\"w=" + videoWidth + "\",\"h=" + videoHeight + "\",\"algo=0\");\n");
            bufferedWriter.append(videoConfig.getVideoCodecScriptLine() + "\n");
            bufferedWriter.append("app.audio.reset();\n");
            bufferedWriter.append(audioConfig.getAudioCodecScriptLine() + "\n");
            bufferedWriter.append("app.audio.normalizeMode=" + audioNormalizeMode + ";\n");
            bufferedWriter.append("app.audio.normalizeValue=" + audioNormalizeValue + ";\n");
            bufferedWriter.append("app.audio.delay=" + audioDelay + ";\n");
            bufferedWriter.append("app.audio.mixer=\"" + audioMixer + "\";\n");
            if (audioReSampling > 0) bufferedWriter.append("app.audio.resample=" + audioReSampling + ";\n");
            bufferedWriter.append("app.setContainer(\"" + container + "\");\n");
            bufferedWriter.append("app.save(\"" + outputFile.getAbsolutePath() + "\");\n");
            bufferedWriter.append("setSuccess(1);\n");
            bufferedWriter.append("app.Exit();\n");
            // End
            bufferedWriter.flush();
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            logger.error("Generate AviDemux Script with IO Exception: {}.", e.getMessage());
            return false;
        }
    }

    public List<File> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(List<File> inputFiles) {
        this.inputFiles = inputFiles;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public File getScriptFile() {
        return scriptFile;
    }

    public void setScriptFile(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    public VideoConfig getVideoConfig() {
        return videoConfig;
    }

    public void setVideoConfig(VideoConfig videoConfig) {
        this.videoConfig = videoConfig;
    }

    public AudioConfig getAudioConfig() {
        return audioConfig;
    }

    public void setAudioConfig(AudioConfig audioConfig) {
        this.audioConfig = audioConfig;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public int getBeginFrames() {
        return beginFrames;
    }

    public void setBeginFrames(int beginFrames) {
        this.beginFrames = beginFrames;
    }

    public int getEndFrames() {
        return endFrames;
    }

    public void setEndFrames(int endFrames) {
        this.endFrames = endFrames;
    }

    public int getFps1000() {
        return fps1000;
    }

    public void setFps1000(int fps1000) {
        this.fps1000 = fps1000;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getAudioNormalizeMode() {
        return audioNormalizeMode;
    }

    public void setAudioNormalizeMode(int audioNormalizeMode) {
        this.audioNormalizeMode = audioNormalizeMode;
    }

    public int getAudioNormalizeValue() {
        return audioNormalizeValue;
    }

    public void setAudioNormalizeValue(int audioNormalizeValue) {
        this.audioNormalizeValue = audioNormalizeValue;
    }

    public int getAudioDelay() {
        return audioDelay;
    }

    public void setAudioDelay(int audioDelay) {
        this.audioDelay = audioDelay;
    }

    public String getAudioMixer() {
        return audioMixer;
    }

    public void setAudioMixer(String audioMixer) {
        this.audioMixer = audioMixer;
    }

    public int getAudioReSampling() {
        return audioReSampling;
    }

    public void setAudioReSampling(int audioReSampling) {
        this.audioReSampling = audioReSampling;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}
