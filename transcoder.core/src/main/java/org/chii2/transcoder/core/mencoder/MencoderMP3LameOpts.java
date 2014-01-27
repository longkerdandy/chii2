package org.chii2.transcoder.core.mencoder;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Mencoder MP3Lame Options
 */
public class MencoderMP3LameOpts implements MencoderAudioOption {
    // variable bitrate method
    //             0    cbr
    //             1    mt
    //             2    rh (default)
    //             3    abr
    //             4    mtrh
    private int vbr = 2;
    // average bitrate
    private boolean abr = false;
    // constant bitrate Also forces CBR mode encoding on subsequent ABR presets modes
    private boolean cbr = false;
    // bitrate in kbps (CBR and ABR only) <0-1024>
    private int br = -1;
    // quality (0 - highest, 9 - lowest) (VBR only)
    private int q = -1;
    // algorithmic quality (0 - best/slowest, 9 - worst/fastest)
    private int aq = -1;
    // compression ratio <1-100>
    private int ratio = 0;
    // audio input gain <0-10>
    private int vol = -1;
    // channel mode (default: auto)
    //              0    stereo
    //              1    joint-stereo
    //              2    dualchannel
    //              3    mono
    private int mode = -1;
    // padding <0-2>
    //              0    none
    //              1    all
    //              2    adjust
    private int padding = -1;
    // Switch on faster encoding on subsequent VBR presets modes.
    // This results in slightly lower quality and higher bitrates.
    private boolean fast = false;
    // Set  a  highpass  filtering  frequency in Hz.  Frequencies below the specified one will be cut off.
    // A value of -1 will disable filtering, a value of 0 will let LAME choose values automatically.
    private long highpassfreq = -1;
    // Set a lowpass filtering frequency in Hz.  Frequencies above the specified one will be cut off.
    // A value of -1 will disable filtering, a value of 0 will let LAME choose values automatically.
    private long lowpassfreq = -1;
    // preset values
    //              medium      VBR encoding, good quality, 150-180 kbps bitrate range
    //              standard    VBR encoding, high quality, 170-210 kbps bitrate range
    //              extreme     VBR encoding, very high quality, 200-240 kbps bitrate range
    //              insane      CBR encoding, highest preset quality, 320 kbps bitrate
    //              <8-320>     ABR encoding at average given kbps bitrate
    private String preset = null;

    @Override
    public String getAudioOptionName() {
        return "mp3lame";
    }

    @Override
    public List<String> getAudioOptionCommands() {
        List<String> options = new ArrayList<>();
        LinkedList<String> lameOptions = new LinkedList<>();

        if (this.abr) {
            lameOptions.add("abr");
            if (this.br >= 0) {
                lameOptions.add("br=" + this.br);
            }
        } else if (this.cbr) {
            lameOptions.add("cbr");
            if (this.br >= 0) {
                lameOptions.add("br=" + this.br);
            }
        } else if (this.vbr != 2 && this.vbr >= 0 && this.vbr <= 4) {
            lameOptions.add("vbr=" + this.vbr);
            if (this.q >= 0 && this.q <= 9) {
                lameOptions.add("q=" + this.q);
            }
        }
        if (this.aq >= 0 && this.aq <= 9) {
            lameOptions.add("aq=" + this.aq);
        }
        if (this.ratio >= 1 && this.ratio <= 100) {
            lameOptions.add("ratio=" + this.ratio);
        }
        if (this.vol >= 0 && this.vol <= 10) {
            lameOptions.add("vol=" + this.vol);
        }
        if (this.mode >= 0 && this.mode <= 3) {
            lameOptions.add("mode=" + this.mode);
        }
        if (this.padding >= 0 && this.padding <= 2) {
            lameOptions.add("padding=" + this.padding);
        }
        if (this.fast) {
            lameOptions.add("fast");
        }
        if (this.highpassfreq >= 0) {
            lameOptions.add("highpassfreq=" + this.highpassfreq);
        }
        if (this.lowpassfreq >= 0) {
            lameOptions.add("lowpassfreq=" + this.lowpassfreq);
        }

        StringBuilder lameOptionsBuilder = new StringBuilder();
        if (lameOptions.size() > 0) {
            lameOptionsBuilder.append(lameOptions.removeFirst());
            for (String option : lameOptions) {
                lameOptionsBuilder.append(":");
                lameOptionsBuilder.append(option);
            }
        }

        if (StringUtils.isNotBlank(lameOptionsBuilder.toString())) {
            options.add("-lameopts");
            options.add(lameOptionsBuilder.toString());
        }

        return options;
    }

    public int getVbr() {
        return vbr;
    }

    public void setVbr(int vbr) {
        this.vbr = vbr;
    }

    public boolean isAbr() {
        return abr;
    }

    public void setAbr(boolean abr) {
        this.abr = abr;
    }

    public boolean isCbr() {
        return cbr;
    }

    public void setCbr(boolean cbr) {
        this.cbr = cbr;
    }

    public int getBr() {
        return br;
    }

    public void setBr(int br) {
        this.br = br;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int getAq() {
        return aq;
    }

    public void setAq(int aq) {
        this.aq = aq;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getVol() {
        return vol;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public boolean isFast() {
        return fast;
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    public long getHighpassfreq() {
        return highpassfreq;
    }

    public void setHighpassfreq(long highpassfreq) {
        this.highpassfreq = highpassfreq;
    }

    public long getLowpassfreq() {
        return lowpassfreq;
    }

    public void setLowpassfreq(long lowpassfreq) {
        this.lowpassfreq = lowpassfreq;
    }

    public String getPreset() {
        return preset;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }
}

