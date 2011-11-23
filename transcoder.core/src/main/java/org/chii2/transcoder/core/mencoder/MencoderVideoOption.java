package org.chii2.transcoder.core.mencoder;

import java.util.List;

/**
 * Mencoder Video Option
 */
public interface MencoderVideoOption {

    /**
     * Get Video Option Name, which should be used with "-ovc"
     *
     * @return Video Option Name,
     */
    public String getVideoOptionName();

    /**
     * Get Video Option's Commands, like "-x264encopts preset=ultrafast:threads=auto"
     *
     * @return Video Option's Commands
     */
    public List<String> getVideoOptionCommands();
}
