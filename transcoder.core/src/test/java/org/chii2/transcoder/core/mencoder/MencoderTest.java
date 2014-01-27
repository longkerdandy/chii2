package org.chii2.transcoder.core.mencoder;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;
import regex2.Matcher;
import regex2.Pattern;

import java.io.File;


/**
 * Mencoder Test
 */
public class MencoderTest {

    @Test
    public void MencoderOutputTest() {
        // Mencoder Output on Ubuntu 11.04
        String ubuntuOutput = "Pos:   5.5s    135f ( 0%)  7.40fps Trem: 103min 262mb  A-V:0.084 [981:159]";

        // Regex Pattern
        Pattern pattern = Pattern.compile("^Pos:\\s*(?<position>\\d+\\.\\d+)s\\s*(?<frame>\\d+)f\\s*\\(\\s*(?<percent>\\d+)%\\)\\s+(?<fps>\\d+\\.\\d+)fps.*");

        // Test Match
        Matcher matcher = pattern.matcher(ubuntuOutput);
        assert matcher.find();
        assert matcher.groupCount() > 0;
        assert matcher.group("position").equals("5.5");
        assert matcher.group("frame").equals("135");
        assert matcher.group("percent").equals("0");
        assert matcher.group("fps").equals("7.40");
    }

    @Test
    public void MencoderXvidMP3ProfileTest() {
        File inputFile = new File("/input.avi");
        File outputFile = new File("/output.avi");

        MencoderXvidMP3Profile profile = new MencoderXvidMP3Profile(inputFile, outputFile);
        assert ("mencoder " + "\"" +  inputFile.getAbsolutePath() + "\"" + " -oac mp3lame -lameopts fast -ffourcc DX50 -ovc xvid -xvidencopts bitrate=3000:chromaOpt:vhq=4:quant_type=mpeg:max_bframes=1 -o " + "\"" + outputFile.getAbsolutePath() + "\"").equals(StringUtils.join(profile.getCommands(), " "));

    }
}
