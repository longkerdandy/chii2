package org.chii2.transcoder.core.mencoder;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Mencoder Xvid Options
 * There are three modes available: constant bitrate (CBR), fixed quantizer and two pass.
 */
public class MencoderXvidOpts implements MencoderVideoOption {
    // Specify the pass in two pass mode. <1|2>
    private int pass = -1;
    // (two pass only) Dramatically speeds up pass one using faster algorithms and disabling CPU-intensive options.
    // This will probably reduce global PSNR a little bit and change individual frame type and PSNR a little bit more.
    private boolean turbo = false;
    // (CBR or two pass mode) Sets the bitrate to be used in kbits/second if <16000 or in bits/second if >16000.
    // If <value> is negative, Xvid will use its absolute value as the target size (in kBytes) of the video and compute the associated bitrate automagically (default: 687 kbits/s).
    private long bitrate = 687;
    // <1-31> Switch to fixed quantizer mode and specify the quantizer to be used.
    private int fixedQuant = 0;
    // zones=<zone0>[/<zone1>[/...]] (CBR or two pass mode)
    // User specified quality for specific parts (ending, credits, ...).  Each zone is <start-frame>,<mode>,<value> where <mode> may be
    //              q    Constant quantizer override, where value=<2.0-31.0> represents the quantizer value.
    //              w    Ratecontrol weight override, where value=<0.01-2.00> represents the quality correction in %.
    private String zone;
    // <0-6> This option controls the motion estimation subsystem.  The higher the value, the more precise the estimation should be  (default:  6).
    // The more  precise the motion estimation is, the more bits can be saved.  Precision is gained at the expense of CPU time so decrease this setting if you need realtime encoding.
    private int meQuality = 6;
    // MPEG-4 uses a half pixel precision for its motion search by default.  The standard proposes a mode where encoders are allowed to use quarter
    // pixel  precision.   This option usually results in a sharper image.  Unfortunately it has a great impact on bitrate and sometimes the higher
    // bitrate use will prevent it from giving a better image quality at a fixed bitrate.  It is better to test with and without  this  option  and
    // see whether it is worth activating.
    private boolean qpel = false;
    // Enable Global Motion Compensation, which makes Xvid generate special frames (GMC-frames) which are well suited for Pan/Zoom/Rotating images.
    // Whether or not the use of this option will save bits is highly dependent on the source material.
    private boolean gmc = false;
    // Trellis Quantization is a kind of adaptive quantization method that saves bits by modifying quantized coefficients to make  them  more  com‐
    // pressible  by the entropy encoder.  Its impact on quality is good, and if VHQ uses too much CPU for you, this setting can be a good alterna‐
    // tive to save a few bits (and gain quality at fixed bitrate) at a lesser cost than with VHQ (default: on).
    private boolean trellis = true;
    // Activate this if your encoded sequence is an anime/cartoon.  It modifies some Xvid internal thresholds so Xvid  takes  better  decisions  on
    // frame types and motion vectors for flat looking cartoons.
    private boolean cartoon = false;
    // The  usual motion estimation algorithm uses only the luminance information to find the best motion vector.  However for some video material,
    // using the chroma planes can help find better vectors.  This setting toggles the use of chroma planes for motion estimation (default: on).
    private boolean chromaMe = true;
    // Enable a chroma optimizer prefilter.  It will do some extra magic on color information to minimize the stepped-stairs effect on  edges.   It
    // will  improve  quality at the cost of encoding speed.  It reduces PSNR by nature, as the mathematical deviation to the original picture will
    // get bigger, but the subjective image quality will raise.  Since it works with color information, you might want to turn it off when encoding
    // in grayscale.
    private boolean chromaOpt = false;
    // Activates high-quality prediction of AC coefficients for intra frames from neighbor blocks (default: on).
    private boolean hqAC = true;
    // The  motion  search algorithm is based on a search in the usual color domain and tries to find a motion vector that minimizes the difference
    // between the reference frame and the encoded frame.  With this setting activated, Xvid will also use the frequency domain (DCT) to search for
    // a motion vector that minimizes not only the spatial difference but also the encoding length of the block.  Fastest to slowest:
    //             0    off
    //             1    mode decision (inter/intra MB) (default)
    //             2    limited search
    //             3    medium search
    //             4    wide search
    private int vhq = 1;
    // Adaptive  quantization  allows  the  macroblock quantizers to vary inside each frame.  This is a 'psychosensory' setting that is supposed to
    // make use of the fact that the human eye tends to notice fewer details in very bright and very dark parts  of  the  picture.   It  compresses
    // those areas more strongly than medium ones, which will save bits that can be spent again on other frames, raising overall subjective quality
    // and possibly reducing PSNR.
    private boolean lumiMask = false;
    // Make Xvid discard chroma planes so the encoded video is grayscale only.  Note that this does not speed up encoding, it just prevents  chroma
    // data from being written in the last stage of encoding.
    private boolean grayscale = false;
    // Encode the fields of interlaced video material.  Turn this option on for interlaced content.
    // NOTE: Should you rescale the video, you would need an interlace-aware resizer, which you can activate with -vf scale=<width>:<height>:1.
    private boolean interlacing = false;
    // <0-31> minimum I-frame quantizer (default: 2)
    private int minIquant = 2;
    // <0-31> maximum I-frame quantizer (default: 31)
    private int maxIquant = 31;
    // <0-31> minimum P-frame quantizer (default: 2)
    private int minPquant = 2;
    // <0-31> maximum P-frame quantizer (default: 31)
    private int maxPquant = 31;
    // <0-31> minimum B-frame quantizer (default: 2)
    private int minBquant = 2;
    // <0-31> maximum B-frame quantizer (default: 31)
    private int maxBquant = 31;
    // (two pass only) minimum interval between keyframes (default: 0)
    private int minKeyInterval = 0;
    // maximum interval between keyframes (default: 10*fps)
    private int maxKeyInterval = 0;
    // <h263|mpeg> Sets  the  type  of quantizer to use.  For high bitrates, you will find that MPEG quantization preserves more detail.  For low bitrates, the
    // smoothing of H.263 will give you less block noise.  When using custom matrices, MPEG quantization must be used.
    private String quantType;
    // Load a custom intra matrix file.  You can build such a file with xvid4conf's matrix editor.
    private String quantIntraMatrix;
    // Load a custom inter matrix file.  You can build such a file with xvid4conf's matrix editor.
    private String quantInterMatrix;
    // <0-1000> (two pass mode only) Shift some bits from the pool for other frame types to intra frames, thus improving keyframe quality.  This amount is an  extra  percentage,
    // so a value of 10 will give your keyframes 10% more bits than normal (default: 0).
    private int keyframeBoost = 0;
    // (two pass mode only) Works  together  with  kfreduction.  Determines the minimum distance below which you consider that two frames are considered consecutive and
    // treated differently according to kfreduction (default: 10).
    private int kfthreshold = 10;
    // (two pass mode only) The above two settings can be used to adjust the size of keyframes that you consider too close to the first (in a  row).   kfthreshold  sets
    // the range in which keyframes are reduced, and kfreduction determines the bitrate reduction they get.  The last I-frame will get treated nor‐
    // mally (default: 30).
    private int kfreduction = 30;
    // <0-4> Maximum number of B-frames to put between I/P-frames (default: 2).
    private int maxBframes = 2;
    // <0-1000> quantizer ratio between B- and non-B-frames, 150=1.50 (default: 150)
    private int bquantRatio = 150;
    // <-1000-1000> quantizer offset between B- and non-B-frames, 100=1.00 (default: 100)
    private int bquantOffset = 100;
    // <-255-255> This setting allows you to specify what priority to place on the use of B-frames.  The higher the value, the higher the  probability  of  B-
    // frames  being  used  (default:  0).   Do not forget that B-frames usually have a higher quantizer, and therefore aggressive production of B-
    // frames may cause worse visual quality.
    private int bfThreshold = 0;
    // This option tells Xvid to close every GOP (Group Of Pictures bounded by two I-frames), which makes GOPs independent from each  other.   This
    // just  implies that the last frame of the GOP is either a P-frame or a N-frame but not a B-frame.  It is usually a good idea to turn this op‐
    // tion on (default: on).
    private boolean closedGop = true;
    // This option is meant to solve frame-order issues when encoding to container formats like AVI that cannot cope with out-of-order frames.   In
    // practice,  most decoders (both software and hardware) are able to deal with frame-order themselves, and may get confused when this option is
    // turned on, so you can safely leave if off, unless you really know what you are doing.
    // WARNING: This will generate an illegal bitstream, and will not be decodable by ISO-MPEG-4 decoders except DivX/libavcodec/Xvid.
    // WARNING: This will also store a fake DivX version in the file so the bug autodetection of some decoders might be confused.
    private boolean packed = false;
    // <0-100> (max_bframes=0 only) This setting allows the creation of variable framerate video streams.  The value of the setting specifies a threshold under  which,  if  the
    // difference  of  the  following frame to the previous frame is below or equal to this threshold, a frame gets not coded (a so called n-vop is
    // placed in the stream).  On playback, when reaching an n-vop the previous frame will be displayed.
    // WARNING: Playing with this setting may result in a jerky video, so use it at your own risks!
    private int frameDropRatio = -1;
    // This parameter controls the number of frames the CBR rate controller will wait before reacting to bitrate changes and compensating for  them
    // to obtain a constant bitrate over an averaging range of frames.
    private int rcReactionDelayFactor = 0;
    // Real CBR is hard to achieve.  Depending on the video material, bitrate can be variable, and hard to predict.  Therefore Xvid uses an averag‐
    // ing period for which it guarantees a given amount of bits (minus a small variation).  This settings expresses the  "number  of  frames"  for
    // which Xvid averages bitrate and tries to achieve CBR.
    private int rcAveragingPeriod = 0;
    // size of the rate control buffer
    private int rcBuffer = 0;
    // <0-100> This  setting  allows  Xvid to take a certain percentage of bits away from high bitrate scenes and give them back to the bit reservoir.  You
    // could also use this if you have a clip with so many bits allocated to high-bitrate scenes that the low(er)-bitrate scenes start to look  bad
    // (default: 0).
    private int curveCompressionHigh = 0;
    // <0-100> This setting allows Xvid to give a certain percentage of extra bits to the low bitrate scenes, taking a few bits from the entire clip.  This
    // might come in handy if you have a few low-bitrate scenes that are still blocky (default: 0).
    private int curveCompressionLow = 0;
    // <0-100> During pass one of two pass encoding, a scaled bitrate curve is computed.  The difference between that expected curve  and  the  result  ob‐
    // tained  during  encoding is called overflow.  Obviously, the two pass rate controller tries to compensate for that overflow, distributing it
    // over the next frames.  This setting controls how much of the overflow is distributed every time there is a new frame.  Low values allow lazy
    // overflow  control,  big  rate  bursts are compensated for more slowly (could lead to lack of precision for small clips).  Higher values will
    // make changes in bit redistribution more abrupt, possibly too abrupt if you set it too high, creating artifacts (default: 5).
    // NOTE: This setting impacts quality a lot, play with it carefully!
    private int overflowControlStrength = 5;
    // <0-100> During the frame bit allocation, overflow control may increase the frame size.  This parameter specifies the maximum percentage by which the
    // overflow control is allowed to increase the frame size, compared to the ideal curve allocation (default: 5).
    private int maxOverflowImprovement = 5;
    // <0-100> During the frame bit allocation, overflow control may decrease the frame size.  This parameter specifies the maximum percentage by which the
    // overflow control is allowed to decrease the frame size, compared to the ideal curve allocation (default: 5).
    private int maxOverflowDegradation = 5;
    // Specifies a frame average overhead per frame, in bytes.  Most of the time users express their target bitrate for video w/o  taking  care  of
    // the  video  container overhead.  This small but (mostly) constant overhead can cause the target file size to be exceeded.  Xvid allows users
    // to set the amount of overhead per frame the container generates (give only an average per frame).  0 has a special meaning, it lets Xvid use
    // its own default values (default: 24 - AVI average overhead).
    private int containerFrameOverhead = 24;
    // Restricts  options  and  VBV  (peak  bitrate over a short period) according to the Simple, Advanced Simple and DivX profiles.  The resulting
    // videos should be playable on standalone players adhering to these profile specifications.
    //             unrestricted
    //                  no restrictions (default)
    //             sp0
    //                  simple profile at level 0
    //             sp1
    //                  simple profile at level 1
    //             sp2
    //                  simple profile at level 2
    //             sp3
    //                  simple profile at level 3
    //             asp0
    //                  advanced simple profile at level 0
    //             asp1
    //                  advanced simple profile at level 1
    //             asp2
    //                  advanced simple profile at level 2
    //             asp3
    //                  advanced simple profile at level 3
    //             asp4
    //                  advanced simple profile at level 4
    //             asp5
    //                  advanced simple profile at level 5
    //             dxnhandheld
    //                  DXN handheld profile
    //             dxnportntsc
    //                  DXN portable NTSC profile
    //             dxnportpal
    //                  DXN portable PAL profile
    //             dxnhtntsc
    //                  DXN home theater NTSC profile
    //             dxnhtpal
    //                  DXN home theater PAL profile
    //             dxnhdtv
    //                  DXN HDTV profile
    //          NOTE: These profiles should be used in conjunction with an appropriate -ffourcc.  Generally DX50 is applicable, as some players do not  rec‐
    //          ognize Xvid but most recognize DivX.
    private String profile;
    // Specifies  the Pixel Aspect Ratio mode (not to be confused with DAR, the Display Aspect Ratio).  PAR is the ratio of the width and height of
    // a single pixel.  So both are related like this: DAR = PAR * (width/height).
    // MPEG-4 defines 5 pixel aspect ratios and one extended one, giving the opportunity to specify a specific  pixel  aspect  ratio.   5  standard
    // modes can be specified:
    //             vga11
    //                  It is the usual PAR for PC content.  Pixels are a square unit.
    //             pal43
    //                  PAL standard 4:3 PAR.  Pixels are rectangles.
    //             pal169
    //                  same as above
    //             ntsc43
    //                  same as above
    //             ntsc169
    //                  same as above (Do not forget to give the exact ratio.)
    //             ext
    //                 Allows you to specify your own pixel aspect ratio with par_width and par_height.
    //          NOTE: In general, setting aspect and autoaspect options is enough.
    private String par;
    // <1-255> (par=ext only) Specifies the width of the custom pixel aspect ratio.
    private int parWidth = 0;
    // <1-255> (par=ext only) Specifies the height of the custom pixel aspect ratio.
    private int parHeight = 0;
    // <x/y | f (float value)> Store  movie  aspect internally, just like MPEG files.  Much nicer solution than rescaling, because quality is not decreased.  MPlayer and a
    // few others players will play these files correctly, others will display them with the wrong aspect.  The aspect parameter can be given as  a
    // ratio or a floating point number.
    private String aspect;
    // Same  as  the aspect option, but automatically computes aspect, taking into account all the adjustments (crop/expand/scale/etc.) made in the filter chain.
    private boolean autoaspect = false;
    // The following option is only available in Xvid 1.1.x.
    // <0|1> This  setting  allows vector candidates for B-frames to be used for the encoding chosen using a rate distortion optimized operator, which is
    // what is done for P-frames by the vhq option.  This produces nicer-looking B-frames while incurring almost no performance  penalty  (default:1).
    private int bvhq = 1;
    // The following option is only available in the 1.2.x version of Xvid.
    // Create n threads to run the motion estimation (default: 0).  The maximum number of threads that can be used is the picture height divided by 16.
    private int threads = 0;

    @Override
    public String getVideoOptionName() {
        return "xvid";
    }

    @SuppressWarnings({"ConstantConditions"})
    @Override
    public List<String> getVideoOptionCommands() {
        List<String> options = new ArrayList<>();
        LinkedList<String> xvidOptions = new LinkedList<>();

        if (this.fixedQuant >= 1 && this.fixedQuant <= 31) {
            xvidOptions.add("fixed_quant=" + this.fixedQuant);
        } else {
            // TWO PASS
            if (this.pass == 1 || this.pass == 2) {
                xvidOptions.add("pass=" + this.pass);
                if (this.turbo) {
                    xvidOptions.add("turbo");
                }
                if (this.minKeyInterval > 0) {
                    xvidOptions.add("min_key_interval=" + this.minKeyInterval);
                }
                if (this.maxKeyInterval > 0) {
                    xvidOptions.add("max_key_interval=" + this.maxKeyInterval);
                }
                if (this.keyframeBoost > 0 && this.keyframeBoost <= 1000) {
                    xvidOptions.add("keyframe_boost=" + this.keyframeBoost);
                }
                if (this.kfthreshold > 0 && this.kfthreshold != 10) {
                    xvidOptions.add("kfthreshold=" + this.kfthreshold);
                }
                if (this.kfreduction >= 0 && this.kfreduction <= 100 && this.kfreduction != 30) {
                    xvidOptions.add("kfreduction=" + this.kfreduction);
                }
            }

            // CBR or TWO PASS
            if (this.bitrate > 0 && this.bitrate != 687) {
                xvidOptions.add("bitrate=" + this.bitrate);
            }
            if (StringUtils.isNotBlank(this.zone)) {
                xvidOptions.add("zone=" + this.zone);
            }
        }

        if (this.meQuality >= 0 && this.meQuality <= 5) {
            xvidOptions.add("me_quality=" + this.meQuality);
        }
        if (this.qpel) {
            xvidOptions.add("qel");
        }
        if (this.gmc) {
            xvidOptions.add("gmc");
        }
        if (!this.trellis) {
            xvidOptions.add("notrellis");
        }
        if (this.cartoon) {
            xvidOptions.add("cartoon");
        }
        if (!this.chromaMe) {
            xvidOptions.add("nochroma_me");
        }
        if (this.chromaOpt) {
            xvidOptions.add("chromaOpt");
        }
        if (!this.hqAC) {
            xvidOptions.add("nohq_ac");
        }
        if (this.vhq >= 0 & this.vhq <= 4) {
            xvidOptions.add("vhq=" + this.vhq);
        }
        if (this.lumiMask) {
            xvidOptions.add("lumi_mask");
        }
        if (this.grayscale) {
            xvidOptions.add("grayscale");
        }
        if (this.interlacing) {
            xvidOptions.add("interlacing");
        }
        if (this.minIquant >= 0 && this.minIquant <= 31 && this.minIquant != 2) {
            xvidOptions.add("min_iquant=" + this.minIquant);
        }
        if (this.maxIquant >= 0 && this.maxIquant <= 31 && this.maxIquant != 31) {
            xvidOptions.add("max_iquant=" + this.maxIquant);
        }
        if (this.minPquant >= 0 && this.minPquant <= 31 && this.minPquant != 2) {
            xvidOptions.add("min_pquant=" + this.minPquant);
        }
        if (this.maxPquant >= 0 && this.maxPquant <= 31 && this.maxPquant != 31) {
            xvidOptions.add("max_pquant=" + this.maxPquant);
        }
        if (this.minBquant >= 0 && this.minBquant <= 31 && this.minBquant != 2) {
            xvidOptions.add("min_bquant=" + this.minBquant);
        }
        if (this.maxBquant >= 0 && this.maxBquant <= 31 && this.maxBquant != 31) {
            xvidOptions.add("max_bquant=" + this.maxBquant);
        }
        if ("h263".equals(this.quantType) || "mpeg".equals(this.quantType)) {
            xvidOptions.add("quant_type=" + this.quantType);
        }
        if (StringUtils.isNotBlank(this.quantIntraMatrix)) {
            xvidOptions.add("quant_intra_matrix=" + this.quantIntraMatrix);
        }
        if (StringUtils.isNotBlank(this.quantInterMatrix)) {
            xvidOptions.add("quant_inter_matrix=" + this.quantInterMatrix);
        }
        if (this.maxBframes >= 0 && this.maxBframes <= 4 && this.maxBframes != 2) {
            xvidOptions.add("max_bframes=" + this.maxBframes);
        }
        if (this.bquantRatio >= 0 && this.bquantRatio <= 1000 && this.bquantRatio != 150) {
            xvidOptions.add("bquant_ratio=" + this.bquantRatio);
        }
        if (this.bquantOffset >= -1000 && this.bquantOffset <= 1000 && this.bquantOffset != 100) {
            xvidOptions.add("bquant_offset=" + this.bquantOffset);
        }
        if (this.bfThreshold >= -255 && this.bfThreshold <= 255 && this.bfThreshold != 0) {
            xvidOptions.add("bf_threshold=" + this.bfThreshold);
        }
        if (!this.closedGop) {
            xvidOptions.add("noclosed_gop");
        }
        if (this.packed) {
            xvidOptions.add("packed");
        }
        if (this.maxBframes == 0 && this.frameDropRatio >= 0 && this.frameDropRatio <= 100) {
            xvidOptions.add("frame_drop_ratio=" + this.frameDropRatio);
        }
        if (this.rcReactionDelayFactor > 0) {
            xvidOptions.add("rc_reaction_delay_factor=" + this.rcReactionDelayFactor);
        }
        if (this.rcAveragingPeriod > 0) {
            xvidOptions.add("rc_averaging_period=" + this.rcAveragingPeriod);
        }
        if (this.rcBuffer > 0) {
            xvidOptions.add("rc_buffer=" + this.rcBuffer);
        }
        if (this.curveCompressionHigh > 0 && this.curveCompressionHigh <= 100) {
            xvidOptions.add("curve_compression_high=" + this.curveCompressionHigh);
        }
        if (this.curveCompressionLow > 0 && this.curveCompressionLow <= 100) {
            xvidOptions.add("curve_compression_low=" + this.curveCompressionLow);
        }
        if (this.overflowControlStrength >= 0 && this.overflowControlStrength <= 100 && this.overflowControlStrength != 5) {
            xvidOptions.add("overflow_control_strength=" + this.overflowControlStrength);
        }
        if (this.maxOverflowImprovement >= 0 && this.maxOverflowImprovement <= 100 && this.maxOverflowImprovement != 5) {
            xvidOptions.add("max_overflow_improvement=" + this.maxOverflowImprovement);
        }
        if (this.maxOverflowDegradation >= 0 && this.maxOverflowDegradation <= 100 && this.maxOverflowDegradation != 5) {
            xvidOptions.add("max_overflow_degradation=" + this.maxOverflowDegradation);
        }
        if (this.containerFrameOverhead >= 0 && this.containerFrameOverhead != 24) {
            xvidOptions.add("container_frame_overhead=" + this.containerFrameOverhead);
        }
        if (StringUtils.isNotBlank(this.profile)) {
            xvidOptions.add("profile=" + this.profile);
        }
        if (StringUtils.isNotBlank(this.par)) {
            xvidOptions.add("par=" + this.par);
        }
        if ("ext".equals(this.par) && this.parWidth >= 1 && this.parWidth <= 255) {
            xvidOptions.add("par_width=" + this.parWidth);
        }
        if ("ext".equals(this.par) && this.parHeight >= 1 && this.parHeight <= 255) {
            xvidOptions.add("par_height=" + this.parHeight);
        }
        if (StringUtils.isNotBlank(this.aspect)) {
            xvidOptions.add("aspect=" + this.aspect);
        }
        if (this.autoaspect) {
            xvidOptions.add("autoaspect");
        }
        if (this.bvhq == 0) {
            xvidOptions.add("bvhq=" + this.bvhq);
        }
        if (this.threads > 0) {
            xvidOptions.add("threads=" + this.threads);
        }

        StringBuilder xvidOptionsBuilder = new StringBuilder();
        if (xvidOptions.size() > 0) {
            xvidOptionsBuilder.append(xvidOptions.removeFirst());
            for (String option : xvidOptions) {
                xvidOptionsBuilder.append(":");
                xvidOptionsBuilder.append(option);
            }
        }

        if (StringUtils.isNotBlank(xvidOptionsBuilder.toString())) {
            options.add("-xvidencopts");
            options.add(xvidOptionsBuilder.toString());
        }

        return options;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public boolean isTurbo() {
        return turbo;
    }

    public void setTurbo(boolean turbo) {
        this.turbo = turbo;
    }

    public long getBitrate() {
        return bitrate;
    }

    public void setBitrate(long bitrate) {
        this.bitrate = bitrate;
    }

    public int getFixedQuant() {
        return fixedQuant;
    }

    public void setFixedQuant(int fixedQuant) {
        this.fixedQuant = fixedQuant;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getMeQuality() {
        return meQuality;
    }

    public void setMeQuality(int meQuality) {
        this.meQuality = meQuality;
    }

    public boolean isQpel() {
        return qpel;
    }

    public void setQpel(boolean qpel) {
        this.qpel = qpel;
    }

    public boolean isGmc() {
        return gmc;
    }

    public void setGmc(boolean gmc) {
        this.gmc = gmc;
    }

    public boolean isTrellis() {
        return trellis;
    }

    public void setTrellis(boolean trellis) {
        this.trellis = trellis;
    }

    public boolean isCartoon() {
        return cartoon;
    }

    public void setCartoon(boolean cartoon) {
        this.cartoon = cartoon;
    }

    public boolean isChromaMe() {
        return chromaMe;
    }

    public void setChromaMe(boolean chromaMe) {
        this.chromaMe = chromaMe;
    }

    public boolean isChromaOpt() {
        return chromaOpt;
    }

    public void setChromaOpt(boolean chromaOpt) {
        this.chromaOpt = chromaOpt;
    }

    public boolean isHqAC() {
        return hqAC;
    }

    public void setHqAC(boolean hqAC) {
        this.hqAC = hqAC;
    }

    public int getVhq() {
        return vhq;
    }

    public void setVhq(int vhq) {
        this.vhq = vhq;
    }

    public boolean isLumiMask() {
        return lumiMask;
    }

    public void setLumiMask(boolean lumiMask) {
        this.lumiMask = lumiMask;
    }

    public boolean isGrayscale() {
        return grayscale;
    }

    public void setGrayscale(boolean grayscale) {
        this.grayscale = grayscale;
    }

    public boolean isInterlacing() {
        return interlacing;
    }

    public void setInterlacing(boolean interlacing) {
        this.interlacing = interlacing;
    }

    public int getMinIquant() {
        return minIquant;
    }

    public void setMinIquant(int minIquant) {
        this.minIquant = minIquant;
    }

    public int getMaxIquant() {
        return maxIquant;
    }

    public void setMaxIquant(int maxIquant) {
        this.maxIquant = maxIquant;
    }

    public int getMinPquant() {
        return minPquant;
    }

    public void setMinPquant(int minPquant) {
        this.minPquant = minPquant;
    }

    public int getMaxPquant() {
        return maxPquant;
    }

    public void setMaxPquant(int maxPquant) {
        this.maxPquant = maxPquant;
    }

    public int getMinBquant() {
        return minBquant;
    }

    public void setMinBquant(int minBquant) {
        this.minBquant = minBquant;
    }

    public int getMaxBquant() {
        return maxBquant;
    }

    public void setMaxBquant(int maxBquant) {
        this.maxBquant = maxBquant;
    }

    public int getMinKeyInterval() {
        return minKeyInterval;
    }

    public void setMinKeyInterval(int minKeyInterval) {
        this.minKeyInterval = minKeyInterval;
    }

    public int getMaxKeyInterval() {
        return maxKeyInterval;
    }

    public void setMaxKeyInterval(int maxKeyInterval) {
        this.maxKeyInterval = maxKeyInterval;
    }

    public String getQuantType() {
        return quantType;
    }

    public void setQuantType(String quantType) {
        this.quantType = quantType;
    }

    public String getQuantIntraMatrix() {
        return quantIntraMatrix;
    }

    public void setQuantIntraMatrix(String quantIntraMatrix) {
        this.quantIntraMatrix = quantIntraMatrix;
    }

    public String getQuantInterMatrix() {
        return quantInterMatrix;
    }

    public void setQuantInterMatrix(String quantInterMatrix) {
        this.quantInterMatrix = quantInterMatrix;
    }

    public int getKeyframeBoost() {
        return keyframeBoost;
    }

    public void setKeyframeBoost(int keyframeBoost) {
        this.keyframeBoost = keyframeBoost;
    }

    public int getKfthreshold() {
        return kfthreshold;
    }

    public void setKfthreshold(int kfthreshold) {
        this.kfthreshold = kfthreshold;
    }

    public int getKfreduction() {
        return kfreduction;
    }

    public void setKfreduction(int kfreduction) {
        this.kfreduction = kfreduction;
    }

    public int getMaxBframes() {
        return maxBframes;
    }

    public void setMaxBframes(int maxBframes) {
        this.maxBframes = maxBframes;
    }

    public int getBquantRatio() {
        return bquantRatio;
    }

    public void setBquantRatio(int bquantRatio) {
        this.bquantRatio = bquantRatio;
    }

    public int getBquantOffset() {
        return bquantOffset;
    }

    public void setBquantOffset(int bquantOffset) {
        this.bquantOffset = bquantOffset;
    }

    public int getBfThreshold() {
        return bfThreshold;
    }

    public void setBfThreshold(int bfThreshold) {
        this.bfThreshold = bfThreshold;
    }

    public boolean isClosedGop() {
        return closedGop;
    }

    public void setClosedGop(boolean closedGop) {
        this.closedGop = closedGop;
    }

    public boolean isPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    public int getFrameDropRatio() {
        return frameDropRatio;
    }

    public void setFrameDropRatio(int frameDropRatio) {
        this.frameDropRatio = frameDropRatio;
    }

    public int getRcReactionDelayFactor() {
        return rcReactionDelayFactor;
    }

    public void setRcReactionDelayFactor(int rcReactionDelayFactor) {
        this.rcReactionDelayFactor = rcReactionDelayFactor;
    }

    public int getRcAveragingPeriod() {
        return rcAveragingPeriod;
    }

    public void setRcAveragingPeriod(int rcAveragingPeriod) {
        this.rcAveragingPeriod = rcAveragingPeriod;
    }

    public int getRcBuffer() {
        return rcBuffer;
    }

    public void setRcBuffer(int rcBuffer) {
        this.rcBuffer = rcBuffer;
    }

    public int getCurveCompressionHigh() {
        return curveCompressionHigh;
    }

    public void setCurveCompressionHigh(int curveCompressionHigh) {
        this.curveCompressionHigh = curveCompressionHigh;
    }

    public int getCurveCompressionLow() {
        return curveCompressionLow;
    }

    public void setCurveCompressionLow(int curveCompressionLow) {
        this.curveCompressionLow = curveCompressionLow;
    }

    public int getOverflowControlStrength() {
        return overflowControlStrength;
    }

    public void setOverflowControlStrength(int overflowControlStrength) {
        this.overflowControlStrength = overflowControlStrength;
    }

    public int getMaxOverflowImprovement() {
        return maxOverflowImprovement;
    }

    public void setMaxOverflowImprovement(int maxOverflowImprovement) {
        this.maxOverflowImprovement = maxOverflowImprovement;
    }

    public int getMaxOverflowDegradation() {
        return maxOverflowDegradation;
    }

    public void setMaxOverflowDegradation(int maxOverflowDegradation) {
        this.maxOverflowDegradation = maxOverflowDegradation;
    }

    public int getContainerFrameOverhead() {
        return containerFrameOverhead;
    }

    public void setContainerFrameOverhead(int containerFrameOverhead) {
        this.containerFrameOverhead = containerFrameOverhead;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPar() {
        return par;
    }

    public void setPar(String par) {
        this.par = par;
    }

    public int getParWidth() {
        return parWidth;
    }

    public void setParWidth(int parWidth) {
        this.parWidth = parWidth;
    }

    public int getParHeight() {
        return parHeight;
    }

    public void setParHeight(int parHeight) {
        this.parHeight = parHeight;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public boolean isAutoaspect() {
        return autoaspect;
    }

    public void setAutoaspect(boolean autoaspect) {
        this.autoaspect = autoaspect;
    }

    public int getBvhq() {
        return bvhq;
    }

    public void setBvhq(int bvhq) {
        this.bvhq = bvhq;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}
