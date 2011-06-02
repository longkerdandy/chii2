package org.chii2.transcoder.core.avidemux;

/**
 * AviDemux FAAC Codec Config
 */
public class FaacConfig implements AudioConfig {

    // Codec Name
    private String codecName = "Faac";
    // Bitrate
    private String bitrate = "128";
    // Parameters
    private String parameters = "4";

    /**
     * Get Audio Codec Config
     * @return Audio Codec Config
     */
    public String getAudioCodecConf() {
        return "80 00 00 00";
    }

    @Override
    public String getAudioCodecScriptLine() {
        return "app.audio.codec(\""+ codecName + "\"," + bitrate + "," + parameters + ",\"" + getAudioCodecConf() + "\");";
    }

    public String getCodecName() {
        return codecName;
    }

    public void setCodecName(String codecName) {
        this.codecName = codecName;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
