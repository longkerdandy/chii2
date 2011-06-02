package org.chii2.transcoder.core.avidemux;

/**
 * AviDemux x264 codec config
 *
 * More Information: http://www.avidemux.org/admWiki/doku.php?id=tutorial:h.264
 */
public class X264Config implements VideoConfig {

    // Codec ID
    private String codecID = "32BCB447-21C9-4210-AE9A-4FCE6C8588AE";
    // Codec Name
    private String codecName = "x264";
    // x264 parameters
    private String parameters = "CQ=22";

    // Fast First Pass
    private String fastFirstPass = "true";
    // Threads
    private String threads = "0";
    // Deterministic
    private String deterministic = "true";
    // Slice Threading
    private String sliceThreading = "false";
    // Threaded Lookahead
    private String threadedLookahead = "-1";
    // IDC level
    private String idcLevel = "-1";
    // Same Ratio As Input
    private String sarAsInput = "true";
    // Overscan
    private String overscan = "undefined";
    // Video Format
    private String videoFormat = "undefined";
    // Full Range Samples
    private String fullRangeSamples = "false";
    // Color Primaries
    private String colorPrimaries = "undefined";
    // Transfer
    private String transfer = "undefined";
    // Color Matrix
    private String colorMatrix = "undefined";
    // Chroma Sample Location
    private String chromaSampleLocation = "0";
    // Reference Frames
    private String referenceFrames = "3";
    // Gop Maximum Size
    private String gopMaximumSize = "300";
    // Gop Minimum Size
    private String gopMinimumSize = "0";
    // Scenecut Threshold
    private String scenecutThreshold = "40";
    // Periodic Intra Refresh
    private String periodicIntraRefresh = "false";
    // B frames
    private String bFrames = "3";
    // Adaptive Bframe Decision
    private String adaptiveBframeDecision = "1";
    // B Frame Bias
    private String bFrameBias = "0";
    // B Frame References
    private String bFrameReferences = "none";
    // Loop Filter
    private String loopFilter = "true";
    // Loop Filter Alpha C0
    private String loopFilterAlphaC0 = "0";
    // Loop Filter Beta
    private String loopFilterBeta = "0";
    // Cabac
    private String cabac = "false";
    // Interlaced
    private String interlaced = "disabled";
    // Constrained Intra Prediction
    private String constrainedIntraPrediction = "false";
    // Partition I Frame 4x4
    private String partitionI4x4 = "false";
    // Partition I Frame 8x8
    private String partitionI8x8 = "false";
    // Partition P Frame 8x8
    private String partitionP8x8 = "true";
    // Partition P Frame 4x4
    private String partitionP4x4 = "false";
    // Partition B Frame 8x8
    private String partitionB8x8 = "true";
    // Dct 8x8
    private String dct8x8 = "false";
    // Weighted Prediction P Frames
    private String weightedPredictionPframes = "none";
    // Weighted Prediction
    private String weightedPrediction = "false";
    // Direct Prediction Mode
    private String directPredictionMode = "auto";
    // Chroma Luma Quantiser Difference
    private String chromaLumaQuantiserDifference = "0";
    // Motion Estimation Method
    private String motionEstimationMethod = "diamond";
    // Motion Vector Search Range
    private String motionVectorSearchRange = "16";
    // Motion Vector Length
    private String motionVectorLength = "-1";
    // Motion Vector Thread Buffer
    private String motionVectorThreadBuffer = "-1";
    // Subpixel Refinement
    private String subpixelRefinement = "2";
    // Chroma Motion Estimation
    private String chromaMotionEstimation = "false";
    // Mixed References
    private String mixedReferences = "false";
    // Trellis
    private String trellis = "disabled";
    // Fast P Skip
    private String fastPSkip = "true";
    // Dct Decimate
    private String dctDecimate = "true";
    // Psycho Rdo
    private String psychoRdo = "1";
    // Noise Reduction
    private String noiseReduction = "0";
    // Inter Luma Deadzone
    private String interLumaDeadzone = "21";
    // Intra Luma Deadzone
    private String intraLumaDeadzone = "11";
    // Quantiser Minimum
    private String quantiserMinimum = "10";
    // Quantiser Maximum
    private String quantiserMaximum = "51";
    // Quantiser Step
    private String quantiserStep = "4";
    // Maximum Constant Rate Factor
    private String maximumConstantRateFactor = "0";
    // Average Bitrate Tolerance
    private String averageBitrateTolerance = "1";
    // Vbv Maximum Bitrate
    private String vbvMaximumBitrate = "0";
    // Vbv Buffer Size
    private String vbvBufferSize = "0";
    // Vbv Initial Occupancy
    private String vbvInitialOccupancy = "0.9";
    // IP Frame Quantiser
    private String ipFrameQuantiser = "1.4";
    // PB Frame Quantiser
    private String pbFrameQuantiser = "1.3";
    // Adaptive Quantiser Mode
    private String adaptiveQuantiserMode = "variance";
    // Adaptive Quantiser Strength
    private String adaptiveQuantiserStrength = "1";
    // MB Tree
    private String mbTree = "true";
    // Frametype Lookahead
    private String frametypeLookahead = "40";
    // Quantiser Curve Compression
    private String quantiserCurveCompression = "0.6";
    // Reduce Flux Before Curve Compression
    private String reduceFluxBeforeCurveCompression = "20";
    // Reduce Flux After Curve Compression
    private String reduceFluxAfterCurveCompression = "0.5";
    // Access Unit Delimiters
    private String accessUnitDelimiters = "false";
    // Sps Identifier
    private String spsIdentifier = "0";
    // Slice Max Size
    private String sliceMaxSize = "0";
    // Slice Max Macroblocks
    private String sliceMaxMacroblocks = "0";
    // Slice Count
    private String sliceCount = "0";
    // HRD
    private String hrd = "none";

    /**
     * Video Codec Config
     *
     * @return Video Codec Config
     */
    @SuppressWarnings({"ConstantConditions"})
    public String getVideoCodecConf() {
        return "<?xml version='1.0'?>" +
                "<x264Config>" +
                "<x264Options>" +
                "<fastFirstPass>" + fastFirstPass + "</fastFirstPass>" +
                "<threads>" + threads + "</threads>" +
                "<deterministic>" + deterministic + "</deterministic>" +
                "<sliceThreading>" + sliceThreading + "</sliceThreading>" +
                "<threadedLookahead>" + threadedLookahead + "</threadedLookahead>" +
                "<idcLevel>" + idcLevel + "</idcLevel>" +
                "<vui>" +
                "<sarAsInput>" + sarAsInput + "</sarAsInput>" +
                "<overscan>" + overscan + "</overscan>" +
                "<videoFormat>" + videoFormat + "</videoFormat>" +
                "<fullRangeSamples>" + fullRangeSamples + "</fullRangeSamples>" +
                "<colorPrimaries>" + colorPrimaries + "</colorPrimaries>" +
                "<transfer>" + transfer + "</transfer>" +
                "<colorMatrix>" + colorMatrix + "</colorMatrix>" +
                "<chromaSampleLocation>" + chromaSampleLocation + "</chromaSampleLocation>" +
                "</vui>" +
                "<referenceFrames>" + referenceFrames + "</referenceFrames>" +
                "<gopMaximumSize>" + gopMaximumSize + "</gopMaximumSize>" +
                "<gopMinimumSize>" + gopMinimumSize + "</gopMinimumSize>" +
                "<scenecutThreshold>" + scenecutThreshold + "</scenecutThreshold>" +
                "<periodicIntraRefresh>" + periodicIntraRefresh + "</periodicIntraRefresh>" +
                "<bFrames>" + bFrames + "</bFrames>" +
                "<adaptiveBframeDecision>" + adaptiveBframeDecision + "</adaptiveBframeDecision>" +
                "<bFrameBias>" + bFrameBias + "</bFrameBias>" +
                "<bFrameReferences>" + bFrameReferences + "</bFrameReferences>" +
                "<loopFilter>" + loopFilter + "</loopFilter>" +
                "<loopFilterAlphaC0>" + loopFilterAlphaC0 + "</loopFilterAlphaC0>" +
                "<loopFilterBeta>" + loopFilterBeta + "</loopFilterBeta>" +
                "<cabac>" + cabac + "</cabac>" +
                "<interlaced>" + interlaced + "</interlaced>" +
                "<constrainedIntraPrediction>" + constrainedIntraPrediction + "</constrainedIntraPrediction>" +
                "<cqmPreset>flat</cqmPreset><intra4x4Luma><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value></intra4x4Luma><intraChroma><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value></intraChroma><inter4x4Luma><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value></inter4x4Luma><interChroma><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value></interChroma><intra8x8Luma><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value></intra8x8Luma><inter8x8Luma><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value><value>16</value></inter8x8Luma>" +
                "<analyse>" +
                "<partitionI4x4>" + partitionI4x4 + "</partitionI4x4>" +
                "<partitionI8x8>" + partitionI8x8 + "</partitionI8x8>" +
                "<partitionP8x8>" + partitionP8x8 + "</partitionP8x8>" +
                "<partitionP4x4>" + partitionP4x4 + "</partitionP4x4>" +
                "<partitionB8x8>" + partitionB8x8 + "</partitionB8x8>" +
                "<dct8x8>" + dct8x8 + "</dct8x8>" +
                "<weightedPredictionPframes>" + weightedPredictionPframes + "</weightedPredictionPframes>" +
                "<weightedPrediction>" + weightedPrediction + "</weightedPrediction>" +
                "<directPredictionMode>" + directPredictionMode + "</directPredictionMode>" +
                "<chromaLumaQuantiserDifference>" + chromaLumaQuantiserDifference + "</chromaLumaQuantiserDifference>" +
                "<motionEstimationMethod>" + motionEstimationMethod + "</motionEstimationMethod>" +
                "<motionVectorSearchRange>" + motionVectorSearchRange + "</motionVectorSearchRange>" +
                "<motionVectorLength>" + motionVectorLength + "</motionVectorLength>" +
                "<motionVectorThreadBuffer>" + motionVectorThreadBuffer + "</motionVectorThreadBuffer>" +
                "<subpixelRefinement>" + subpixelRefinement + "</subpixelRefinement>" +
                "<chromaMotionEstimation>" + chromaMotionEstimation + "</chromaMotionEstimation>" +
                "<mixedReferences>" + mixedReferences + "</mixedReferences>" +
                "<trellis>" + trellis + "</trellis>" +
                "<fastPSkip>" + fastPSkip + "</fastPSkip>" +
                "<dctDecimate>" + dctDecimate + "</dctDecimate>" +
                "<psychoRdo>" + psychoRdo + "</psychoRdo>" +
                "<noiseReduction>" + noiseReduction + "</noiseReduction>" +
                "<interLumaDeadzone>" + interLumaDeadzone + "</interLumaDeadzone>" +
                "<intraLumaDeadzone>" + intraLumaDeadzone + "</intraLumaDeadzone>" +
                "</analyse>" +
                "<rateControl>" +
                "<quantiserMinimum>" + quantiserMinimum + "</quantiserMinimum>" +
                "<quantiserMaximum>" + quantiserMaximum + "</quantiserMaximum>" +
                "<quantiserStep>" + quantiserStep + "</quantiserStep>" +
                "<maximumConstantRateFactor>" + maximumConstantRateFactor + "</maximumConstantRateFactor>" +
                "<averageBitrateTolerance>" + averageBitrateTolerance + "</averageBitrateTolerance>" +
                "<vbvMaximumBitrate>" + vbvMaximumBitrate + "</vbvMaximumBitrate>" +
                "<vbvBufferSize>" + vbvBufferSize + "</vbvBufferSize>" +
                "<vbvInitialOccupancy>" + vbvInitialOccupancy + "</vbvInitialOccupancy>" +
                "<ipFrameQuantiser>" + ipFrameQuantiser + "</ipFrameQuantiser>" +
                "<pbFrameQuantiser>" + pbFrameQuantiser + "</pbFrameQuantiser>" +
                "<adaptiveQuantiserMode>" + adaptiveQuantiserMode + "</adaptiveQuantiserMode>" +
                "<adaptiveQuantiserStrength>" + adaptiveQuantiserStrength + "</adaptiveQuantiserStrength>" +
                "<mbTree>" + mbTree + "</mbTree>" +
                "<frametypeLookahead>" + frametypeLookahead + "</frametypeLookahead>" +
                "<quantiserCurveCompression>" + quantiserCurveCompression + "</quantiserCurveCompression>" +
                "<reduceFluxBeforeCurveCompression>" + reduceFluxBeforeCurveCompression + "</reduceFluxBeforeCurveCompression>" +
                "<reduceFluxAfterCurveCompression>" + reduceFluxAfterCurveCompression + "</reduceFluxAfterCurveCompression>" +
                "</rateControl>" +
                "<accessUnitDelimiters>" + accessUnitDelimiters + "</accessUnitDelimiters>" +
                "<spsIdentifier>" + spsIdentifier + "</spsIdentifier>" +
                "<sliceMaxSize>" + sliceMaxSize + "</sliceMaxSize>" +
                "<sliceMaxMacroblocks>" + sliceMaxMacroblocks + "</sliceMaxMacroblocks>" +
                "<sliceCount>" + sliceCount + "</sliceCount>" +
                "<hrd>" + hrd + "</hrd>" +
                "</x264Options>" +
                "</x264Config>";
    }

    @Override
    public String getVideoCodecScriptLine() {
        return "app.video.codec(\"" + codecID + "\", \"" + codecName + "\", \"" + parameters + "\", \"" + getVideoCodecConf() + "\");";
    }

    public String getCodecID() {
        return codecID;
    }

    public void setCodecID(String codecID) {
        this.codecID = codecID;
    }

    public String getCodecName() {
        return codecName;
    }

    public void setCodecName(String codecName) {
        this.codecName = codecName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getFastFirstPass() {
        return fastFirstPass;
    }

    public void setFastFirstPass(String fastFirstPass) {
        this.fastFirstPass = fastFirstPass;
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    public String getDeterministic() {
        return deterministic;
    }

    public void setDeterministic(String deterministic) {
        this.deterministic = deterministic;
    }

    public String getSliceThreading() {
        return sliceThreading;
    }

    public void setSliceThreading(String sliceThreading) {
        this.sliceThreading = sliceThreading;
    }

    public String getThreadedLookahead() {
        return threadedLookahead;
    }

    public void setThreadedLookahead(String threadedLookahead) {
        this.threadedLookahead = threadedLookahead;
    }

    public String getIdcLevel() {
        return idcLevel;
    }

    public void setIdcLevel(String idcLevel) {
        this.idcLevel = idcLevel;
    }

    public String getSarAsInput() {
        return sarAsInput;
    }

    public void setSarAsInput(String sarAsInput) {
        this.sarAsInput = sarAsInput;
    }

    public String getOverscan() {
        return overscan;
    }

    public void setOverscan(String overscan) {
        this.overscan = overscan;
    }

    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    public String getFullRangeSamples() {
        return fullRangeSamples;
    }

    public void setFullRangeSamples(String fullRangeSamples) {
        this.fullRangeSamples = fullRangeSamples;
    }

    public String getColorPrimaries() {
        return colorPrimaries;
    }

    public void setColorPrimaries(String colorPrimaries) {
        this.colorPrimaries = colorPrimaries;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getColorMatrix() {
        return colorMatrix;
    }

    public void setColorMatrix(String colorMatrix) {
        this.colorMatrix = colorMatrix;
    }

    public String getChromaSampleLocation() {
        return chromaSampleLocation;
    }

    public void setChromaSampleLocation(String chromaSampleLocation) {
        this.chromaSampleLocation = chromaSampleLocation;
    }

    public String getReferenceFrames() {
        return referenceFrames;
    }

    public void setReferenceFrames(String referenceFrames) {
        this.referenceFrames = referenceFrames;
    }

    public String getGopMaximumSize() {
        return gopMaximumSize;
    }

    public void setGopMaximumSize(String gopMaximumSize) {
        this.gopMaximumSize = gopMaximumSize;
    }

    public String getGopMinimumSize() {
        return gopMinimumSize;
    }

    public void setGopMinimumSize(String gopMinimumSize) {
        this.gopMinimumSize = gopMinimumSize;
    }

    public String getScenecutThreshold() {
        return scenecutThreshold;
    }

    public void setScenecutThreshold(String scenecutThreshold) {
        this.scenecutThreshold = scenecutThreshold;
    }

    public String getPeriodicIntraRefresh() {
        return periodicIntraRefresh;
    }

    public void setPeriodicIntraRefresh(String periodicIntraRefresh) {
        this.periodicIntraRefresh = periodicIntraRefresh;
    }

    public String getbFrames() {
        return bFrames;
    }

    public void setbFrames(String bFrames) {
        this.bFrames = bFrames;
    }

    public String getAdaptiveBframeDecision() {
        return adaptiveBframeDecision;
    }

    public void setAdaptiveBframeDecision(String adaptiveBframeDecision) {
        this.adaptiveBframeDecision = adaptiveBframeDecision;
    }

    public String getbFrameBias() {
        return bFrameBias;
    }

    public void setbFrameBias(String bFrameBias) {
        this.bFrameBias = bFrameBias;
    }

    public String getbFrameReferences() {
        return bFrameReferences;
    }

    public void setbFrameReferences(String bFrameReferences) {
        this.bFrameReferences = bFrameReferences;
    }

    public String getLoopFilter() {
        return loopFilter;
    }

    public void setLoopFilter(String loopFilter) {
        this.loopFilter = loopFilter;
    }

    public String getLoopFilterAlphaC0() {
        return loopFilterAlphaC0;
    }

    public void setLoopFilterAlphaC0(String loopFilterAlphaC0) {
        this.loopFilterAlphaC0 = loopFilterAlphaC0;
    }

    public String getLoopFilterBeta() {
        return loopFilterBeta;
    }

    public void setLoopFilterBeta(String loopFilterBeta) {
        this.loopFilterBeta = loopFilterBeta;
    }

    public String getCabac() {
        return cabac;
    }

    public void setCabac(String cabac) {
        this.cabac = cabac;
    }

    public String getInterlaced() {
        return interlaced;
    }

    public void setInterlaced(String interlaced) {
        this.interlaced = interlaced;
    }

    public String getConstrainedIntraPrediction() {
        return constrainedIntraPrediction;
    }

    public void setConstrainedIntraPrediction(String constrainedIntraPrediction) {
        this.constrainedIntraPrediction = constrainedIntraPrediction;
    }

    public String getPartitionI4x4() {
        return partitionI4x4;
    }

    public void setPartitionI4x4(String partitionI4x4) {
        this.partitionI4x4 = partitionI4x4;
    }

    public String getPartitionI8x8() {
        return partitionI8x8;
    }

    public void setPartitionI8x8(String partitionI8x8) {
        this.partitionI8x8 = partitionI8x8;
    }

    public String getPartitionP8x8() {
        return partitionP8x8;
    }

    public void setPartitionP8x8(String partitionP8x8) {
        this.partitionP8x8 = partitionP8x8;
    }

    public String getPartitionP4x4() {
        return partitionP4x4;
    }

    public void setPartitionP4x4(String partitionP4x4) {
        this.partitionP4x4 = partitionP4x4;
    }

    public String getPartitionB8x8() {
        return partitionB8x8;
    }

    public void setPartitionB8x8(String partitionB8x8) {
        this.partitionB8x8 = partitionB8x8;
    }

    public String getDct8x8() {
        return dct8x8;
    }

    public void setDct8x8(String dct8x8) {
        this.dct8x8 = dct8x8;
    }

    public String getWeightedPredictionPframes() {
        return weightedPredictionPframes;
    }

    public void setWeightedPredictionPframes(String weightedPredictionPframes) {
        this.weightedPredictionPframes = weightedPredictionPframes;
    }

    public String getWeightedPrediction() {
        return weightedPrediction;
    }

    public void setWeightedPrediction(String weightedPrediction) {
        this.weightedPrediction = weightedPrediction;
    }

    public String getDirectPredictionMode() {
        return directPredictionMode;
    }

    public void setDirectPredictionMode(String directPredictionMode) {
        this.directPredictionMode = directPredictionMode;
    }

    public String getChromaLumaQuantiserDifference() {
        return chromaLumaQuantiserDifference;
    }

    public void setChromaLumaQuantiserDifference(String chromaLumaQuantiserDifference) {
        this.chromaLumaQuantiserDifference = chromaLumaQuantiserDifference;
    }

    public String getMotionEstimationMethod() {
        return motionEstimationMethod;
    }

    public void setMotionEstimationMethod(String motionEstimationMethod) {
        this.motionEstimationMethod = motionEstimationMethod;
    }

    public String getMotionVectorSearchRange() {
        return motionVectorSearchRange;
    }

    public void setMotionVectorSearchRange(String motionVectorSearchRange) {
        this.motionVectorSearchRange = motionVectorSearchRange;
    }

    public String getMotionVectorLength() {
        return motionVectorLength;
    }

    public void setMotionVectorLength(String motionVectorLength) {
        this.motionVectorLength = motionVectorLength;
    }

    public String getMotionVectorThreadBuffer() {
        return motionVectorThreadBuffer;
    }

    public void setMotionVectorThreadBuffer(String motionVectorThreadBuffer) {
        this.motionVectorThreadBuffer = motionVectorThreadBuffer;
    }

    public String getSubpixelRefinement() {
        return subpixelRefinement;
    }

    public void setSubpixelRefinement(String subpixelRefinement) {
        this.subpixelRefinement = subpixelRefinement;
    }

    public String getChromaMotionEstimation() {
        return chromaMotionEstimation;
    }

    public void setChromaMotionEstimation(String chromaMotionEstimation) {
        this.chromaMotionEstimation = chromaMotionEstimation;
    }

    public String getMixedReferences() {
        return mixedReferences;
    }

    public void setMixedReferences(String mixedReferences) {
        this.mixedReferences = mixedReferences;
    }

    public String getTrellis() {
        return trellis;
    }

    public void setTrellis(String trellis) {
        this.trellis = trellis;
    }

    public String getFastPSkip() {
        return fastPSkip;
    }

    public void setFastPSkip(String fastPSkip) {
        this.fastPSkip = fastPSkip;
    }

    public String getDctDecimate() {
        return dctDecimate;
    }

    public void setDctDecimate(String dctDecimate) {
        this.dctDecimate = dctDecimate;
    }

    public String getPsychoRdo() {
        return psychoRdo;
    }

    public void setPsychoRdo(String psychoRdo) {
        this.psychoRdo = psychoRdo;
    }

    public String getNoiseReduction() {
        return noiseReduction;
    }

    public void setNoiseReduction(String noiseReduction) {
        this.noiseReduction = noiseReduction;
    }

    public String getInterLumaDeadzone() {
        return interLumaDeadzone;
    }

    public void setInterLumaDeadzone(String interLumaDeadzone) {
        this.interLumaDeadzone = interLumaDeadzone;
    }

    public String getIntraLumaDeadzone() {
        return intraLumaDeadzone;
    }

    public void setIntraLumaDeadzone(String intraLumaDeadzone) {
        this.intraLumaDeadzone = intraLumaDeadzone;
    }

    public String getQuantiserMinimum() {
        return quantiserMinimum;
    }

    public void setQuantiserMinimum(String quantiserMinimum) {
        this.quantiserMinimum = quantiserMinimum;
    }

    public String getQuantiserMaximum() {
        return quantiserMaximum;
    }

    public void setQuantiserMaximum(String quantiserMaximum) {
        this.quantiserMaximum = quantiserMaximum;
    }

    public String getQuantiserStep() {
        return quantiserStep;
    }

    public void setQuantiserStep(String quantiserStep) {
        this.quantiserStep = quantiserStep;
    }

    public String getMaximumConstantRateFactor() {
        return maximumConstantRateFactor;
    }

    public void setMaximumConstantRateFactor(String maximumConstantRateFactor) {
        this.maximumConstantRateFactor = maximumConstantRateFactor;
    }

    public String getAverageBitrateTolerance() {
        return averageBitrateTolerance;
    }

    public void setAverageBitrateTolerance(String averageBitrateTolerance) {
        this.averageBitrateTolerance = averageBitrateTolerance;
    }

    public String getVbvMaximumBitrate() {
        return vbvMaximumBitrate;
    }

    public void setVbvMaximumBitrate(String vbvMaximumBitrate) {
        this.vbvMaximumBitrate = vbvMaximumBitrate;
    }

    public String getVbvBufferSize() {
        return vbvBufferSize;
    }

    public void setVbvBufferSize(String vbvBufferSize) {
        this.vbvBufferSize = vbvBufferSize;
    }

    public String getVbvInitialOccupancy() {
        return vbvInitialOccupancy;
    }

    public void setVbvInitialOccupancy(String vbvInitialOccupancy) {
        this.vbvInitialOccupancy = vbvInitialOccupancy;
    }

    public String getIpFrameQuantiser() {
        return ipFrameQuantiser;
    }

    public void setIpFrameQuantiser(String ipFrameQuantiser) {
        this.ipFrameQuantiser = ipFrameQuantiser;
    }

    public String getPbFrameQuantiser() {
        return pbFrameQuantiser;
    }

    public void setPbFrameQuantiser(String pbFrameQuantiser) {
        this.pbFrameQuantiser = pbFrameQuantiser;
    }

    public String getAdaptiveQuantiserMode() {
        return adaptiveQuantiserMode;
    }

    public void setAdaptiveQuantiserMode(String adaptiveQuantiserMode) {
        this.adaptiveQuantiserMode = adaptiveQuantiserMode;
    }

    public String getAdaptiveQuantiserStrength() {
        return adaptiveQuantiserStrength;
    }

    public void setAdaptiveQuantiserStrength(String adaptiveQuantiserStrength) {
        this.adaptiveQuantiserStrength = adaptiveQuantiserStrength;
    }

    public String getMbTree() {
        return mbTree;
    }

    public void setMbTree(String mbTree) {
        this.mbTree = mbTree;
    }

    public String getFrametypeLookahead() {
        return frametypeLookahead;
    }

    public void setFrametypeLookahead(String frametypeLookahead) {
        this.frametypeLookahead = frametypeLookahead;
    }

    public String getQuantiserCurveCompression() {
        return quantiserCurveCompression;
    }

    public void setQuantiserCurveCompression(String quantiserCurveCompression) {
        this.quantiserCurveCompression = quantiserCurveCompression;
    }

    public String getReduceFluxBeforeCurveCompression() {
        return reduceFluxBeforeCurveCompression;
    }

    public void setReduceFluxBeforeCurveCompression(String reduceFluxBeforeCurveCompression) {
        this.reduceFluxBeforeCurveCompression = reduceFluxBeforeCurveCompression;
    }

    public String getReduceFluxAfterCurveCompression() {
        return reduceFluxAfterCurveCompression;
    }

    public void setReduceFluxAfterCurveCompression(String reduceFluxAfterCurveCompression) {
        this.reduceFluxAfterCurveCompression = reduceFluxAfterCurveCompression;
    }

    public String getAccessUnitDelimiters() {
        return accessUnitDelimiters;
    }

    public void setAccessUnitDelimiters(String accessUnitDelimiters) {
        this.accessUnitDelimiters = accessUnitDelimiters;
    }

    public String getSpsIdentifier() {
        return spsIdentifier;
    }

    public void setSpsIdentifier(String spsIdentifier) {
        this.spsIdentifier = spsIdentifier;
    }

    public String getSliceMaxSize() {
        return sliceMaxSize;
    }

    public void setSliceMaxSize(String sliceMaxSize) {
        this.sliceMaxSize = sliceMaxSize;
    }

    public String getSliceMaxMacroblocks() {
        return sliceMaxMacroblocks;
    }

    public void setSliceMaxMacroblocks(String sliceMaxMacroblocks) {
        this.sliceMaxMacroblocks = sliceMaxMacroblocks;
    }

    public String getSliceCount() {
        return sliceCount;
    }

    public void setSliceCount(String sliceCount) {
        this.sliceCount = sliceCount;
    }

    public String getHrd() {
        return hrd;
    }

    public void setHrd(String hrd) {
        this.hrd = hrd;
    }
}
