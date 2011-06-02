package org.chii2.transcoder.core.avidemux;

/**
 * AviDemux Script Video Codec Copy
 */
public class VideoCopyConfig implements VideoConfig {
    @Override
    public String getVideoCodecScriptLine() {
        return "app.video.codec(\"Copy\", \"\", \"0\");";
    }
}
