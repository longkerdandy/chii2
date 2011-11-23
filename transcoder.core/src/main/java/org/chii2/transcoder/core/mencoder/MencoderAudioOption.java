package org.chii2.transcoder.core.mencoder;

import java.util.List;

/**
 * Mencoder Audio Option
 */
public interface MencoderAudioOption {

    /**
     * Get Audio Option Name, which should be used with "-oac"
     *
     * @return Audio Option Name,
     */
    public String getAudioOptionName();

    /**
     * Get Audio Option's Commands, like "-lameopts fast:preset=standard"
     *
     * @return Audio Option's Commands
     */
    public List<String> getAudioOptionCommands();
}
