package org.chii2.transcoder.core.mencoder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.chii2.transcoder.api.core.BackgroundTranscoderService;
import org.chii2.transcoder.core.VideoTranscoderOutputReaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import regex2.Matcher;
import regex2.Pattern;

/**
 * Mencoder Output Text Reader
 */
public class MencoderOutputTextReader extends VideoTranscoderOutputReaderImpl {
    // Regex Pattern for Output Text
    private Pattern pattern = Pattern.compile("^Pos:\\s*(?<position>\\d+\\.\\d+)s\\s*(?<frame>\\d+)f\\s*\\(\\s*(?<percent>\\d+)%\\)\\s+(?<fps>\\d+\\.\\d+)fps.*");
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.mencoder");

    /**
     * Constructor
     *
     * @param process Transcoder Process
     * @param service Background Transcoder Service
     */
    public MencoderOutputTextReader(Process process, BackgroundTranscoderService service) {
        super(process, service);
    }

    @Override
    public void parseOutput(String line) {
        if (StringUtils.isEmpty(line)) {
            return;
        }
        // Test Match
        Matcher matcher = this.pattern.matcher(line);
        if (matcher.find() && matcher.groupCount() > 0) {
            // Call Back
            this.service.setCurrentTaskStatus(NumberUtils.toInt(matcher.group("percent"), 0));
        } else {
            logger.debug("Mencoder Output: {}", line);
        }
    }
}
