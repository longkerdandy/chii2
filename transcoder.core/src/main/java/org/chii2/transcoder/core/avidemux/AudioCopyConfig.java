package org.chii2.transcoder.core.avidemux;

/**
 * AviDemux Script Video Codec Copy
 */
public class AudioCopyConfig implements AudioConfig {
    @Override
    public String getAudioCodecScriptLine() {
        return "app.audio.codec(\"Copy\", 0, 0, \"\");";
    }
}
