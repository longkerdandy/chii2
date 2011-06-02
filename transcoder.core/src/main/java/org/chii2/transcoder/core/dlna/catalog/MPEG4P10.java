package org.chii2.transcoder.core.dlna.catalog;

import org.chii2.transcoder.core.dlna.codec.Container;
import org.chii2.transcoder.core.dlna.codec.VideoCodec;
import org.chii2.transcoder.core.dlna.restriction.VideoRestriction;
import org.teleal.cling.support.model.dlna.DLNAProfiles;

/**
 * MPEG Part10
 */
public class MPEG4P10 extends VideoCatalog {
    public MPEG4P10() {
        // AVC_MP4_BL_L12_CIF15_HEAAC
        profileMap.put(DLNAProfiles.AVC_MP4_BL_L12_CIF15_HEAAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_15_Restriction()
        });

        // AVC_MP4_BL_L1B_QCIF15_HEAAC
        profileMap.put(DLNAProfiles.AVC_MP4_BL_L1B_QCIF15_HEAAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000),
                new TotalBitRateRangeRestriction(0, 256000)
        });

        // AVC_MP4_BL_CIF15_AAC
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF15_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000),
                new TotalBitRateRangeRestriction(0, 600000)
        });
        // AVC_MP4_BL_CIF15_AAC_520
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF15_AAC_520, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new TotalBitRateRangeRestriction(0, 520000)
        });
        // AVC_MP4_BL_CIF15_AMR
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF15_AMR, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AMR}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000),
                new TotalBitRateRangeRestriction(0, 600000)
        });
        // AVC_MP4_BL_CIF15_BSAC
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF15_BSAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.BSAC_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000),
                new TotalBitRateRangeRestriction(0, 600000)
        });
        // AVC_MP4_BL_CIF15_AAC_LTP
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF15_AAC_LTP, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000),
                new TotalBitRateRangeRestriction(0, 600000)
        });
        // AVC_MP4_BL_CIF15_AAC_LTP_520
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF15_AAC_LTP_520, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new TotalBitRateRangeRestriction(0, 520000)
        });
        // AVC_MP4_BL_CIF15_HEAAC
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF15_HEAAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000),
                new TotalBitRateRangeRestriction(0, 600000)
        });

        // AVC_MP4_BL_L2_CIF30_AAC
        profileMap.put(DLNAProfiles.AVC_MP4_BL_L2_CIF30_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new TotalBitRateRangeRestriction(0, 1300000)
        });

        // AVC_MP4_BL_CIF30_AAC_MULT5
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF30_AAC_MULT5, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_MULT5_ISO, DLNAProfiles.AAC_MULT5_ADTS}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_MP4_BL_CIF30_HEAAC_L2
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF30_HEAAC_L2, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_MP4_BL_CIF30_AC3
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF30_AC3, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AC3Restriction(),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_MP4_BL_CIF30_MPEG1_L3
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF30_MPEG1_L3, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.MP3}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_MP4_BL_CIF30_BSAC
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF30_BSAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.BSAC_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_MP4_BL_CIF30_BSAC_MULT5
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF30_BSAC_MULT5, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.BSAC_MULT5_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_MP4_MP_SD_AAC_LTP
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF30_AAC_LTP, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_MP4_BL_CIF30_AAC_LTP_MULT5
        profileMap.put(DLNAProfiles.AVC_MP4_BL_CIF30_AAC_LTP_MULT5, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_MULT5_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });

        // AVC_MP4_BL_L3L_SD_AAC
        profileMap.put(DLNAProfiles.AVC_MP4_BL_L3L_SD_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MP_L3_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 4000000)
        });
        // AVC_MP4_BL_L3L_SD_HEAAC
        profileMap.put(DLNAProfiles.AVC_MP4_BL_L3L_SD_HEAAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MP_L3_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 4000000)
        });

        // AVC_MP4_MP_SD_AAC_MULT5
        profileMap.put(DLNAProfiles.AVC_MP4_MP_SD_AAC_MULT5, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_MULT5_ISO, DLNAProfiles.AAC_MULT5_ADTS}),
                new ContainerRestriction(Container.MPEG4),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_MP4_MP_SD_HEAAC_L2
        profileMap.put(DLNAProfiles.AVC_MP4_MP_SD_HEAAC_L2, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_MP4_MP_SD_AC3
        profileMap.put(DLNAProfiles.AVC_MP4_MP_SD_AC3, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AC3Restriction(),
                new ContainerRestriction(Container.MPEG4),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_MP4_MP_SD_MPEG1_L3
        profileMap.put(DLNAProfiles.AVC_MP4_MP_SD_MPEG1_L3, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.MP3}),
                new ContainerRestriction(Container.MPEG4),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_MP4_MP_SD_ATRAC3plus
        profileMap.put(DLNAProfiles.AVC_MP4_MP_SD_ATRAC3plus, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.ATRAC3}),
                new ContainerRestriction(Container.MPEG4),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_MP4_MP_SD_BSAC
        profileMap.put(DLNAProfiles.AVC_MP4_MP_SD_BSAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.BSAC_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_MP4_MP_SD_AAC_LTP
        profileMap.put(DLNAProfiles.AVC_MP4_MP_SD_AAC_LTP, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_MP4_MP_SD_AAC_LTP_MULT5
        profileMap.put(DLNAProfiles.AVC_MP4_MP_SD_AAC_LTP_MULT5, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_MULT5_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_MP4_MP_SD_AAC_LTP_MULT7
        profileMap.put(DLNAProfiles.AVC_MP4_MP_SD_AAC_LTP_MULT7, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_MULT7_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });

        // AVC_MP4_HP_HD_AAC
        profileMap.put(DLNAProfiles.AVC_MP4_HP_HD_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new MP_HD_Restriction(),
                new VideoBitRateRangeRestriction(0, 20000000)
        });


        // AVC_TS_BL_CIF15_AAC_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF15_AAC_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000),
                new TotalBitRateRangeRestriction(0, 600000)
        });
        // AVC_TS_BL_CIF15_AAC_520_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF15_AAC_540_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new TotalBitRateRangeRestriction(0, 540000)
        });

        // AVC_TS_BL_CIF15_BSAC_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF15_BSAC_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.BSAC_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000),
                new TotalBitRateRangeRestriction(0, 600000)
        });
        // AVC_TS_BL_CIF15_AAC_LTP_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF15_AAC_LTP_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000),
                new TotalBitRateRangeRestriction(0, 600000)
        });

        // AVC_TS_BL_CIF30_AAC_940_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF30_AAC_940_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 940000)
        });

        // AVC_TS_BL_CIF30_AAC_MULT5_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF30_AAC_MULT5_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_MULT5_ISO, DLNAProfiles.AAC_MULT5_ADTS}),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_TS_BL_CIF30_HEAAC_L2_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF30_HEAAC_L2_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_TS_BL_CIF30_AC3_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF30_AC3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AC3Restriction(),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_TS_BL_CIF30_MPEG1_L3_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF30_MPEG1_L3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.MP3}),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_TS_BL_CIF30_AAC_LTP_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF30_AAC_LTP_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });
        // AVC_TS_BL_CIF30_AAC_LTP_MULT5_ISO
        profileMap.put(DLNAProfiles.AVC_TS_BL_CIF30_AAC_LTP_MULT5_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_MULT5_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000)
        });

        // AVC_TS_MP_SD_AAC_MULT5_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_SD_AAC_MULT5_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_MULT5_ISO, DLNAProfiles.AAC_MULT5_ADTS}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_TS_MP_SD_HEAAC_L2_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_SD_HEAAC_L2_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_TS_MP_SD_AC3_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_SD_AC3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AC3Restriction(),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_TS_MP_SD_MPEG1_L3_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_SD_MPEG1_L3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.MP3}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_TS_MP_SD_BSAC_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_SD_BSAC_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.BSAC_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_TS_MP_SD_AAC_LTP_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_SD_AAC_LTP_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_TS_MP_SD_AAC_LTP_MULT5_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_SD_AAC_LTP_MULT5_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_MULT5_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });
        // AVC_TS_MP_SD_AAC_LTP_MULT7_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_SD_AAC_LTP_MULT7_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_MULT7_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_SD_Restriction(),
                new VideoBitRateRangeRestriction(0, 10000000)
        });

        // AVC_TS_MP_HD_AAC_MULT5_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_HD_AAC_MULT5_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_MULT5_ISO, DLNAProfiles.AAC_MULT5_ADTS}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_HD_Restriction(),
                new VideoBitRateRangeRestriction(0, 20000000)
        });
        // AVC_TS_MP_HD_HEAAC_L2_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_HD_HEAAC_L2_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_HD_Restriction(),
                new VideoBitRateRangeRestriction(0, 20000000)
        });
        // AVC_TS_MP_HD_AC3_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_HD_AC3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AC3Restriction(),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_HD_Restriction(),
                new VideoBitRateRangeRestriction(0, 20000000)
        });
        // AVC_TS_MP_HD_MPEG1_L3_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_HD_MPEG1_L3_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.MP3}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_HD_Restriction(),
                new VideoBitRateRangeRestriction(0, 20000000)
        });
        // AVC_TS_MP_HD_AAC_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_HD_AAC_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_HD_Restriction(),
                new VideoBitRateRangeRestriction(0, 20000000)
        });
        // AVC_TS_MP_HD_AAC_LTP_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_HD_AAC_LTP_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_HD_Restriction(),
                new VideoBitRateRangeRestriction(0, 20000000)
        });
        // AVC_TS_MP_HD_AAC_LTP_MULT5_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_HD_AAC_LTP_MULT5_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_MULT5_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_HD_Restriction(),
                new VideoBitRateRangeRestriction(0, 20000000)
        });
        // AVC_TS_MP_HD_AAC_LTP_MULT7_ISO
        profileMap.put(DLNAProfiles.AVC_TS_MP_HD_AAC_LTP_MULT7_ISO, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_MULT7_ISO}),
                new ContainerRestriction(Container.MPEG_TS),
                new MP_HD_Restriction(),
                new VideoBitRateRangeRestriction(0, 20000000)
        });

        // TODO: 3GPP Container can't be determined
        // AVC_3GPP_BL_QCIF15_AAC
        profileMap.put(DLNAProfiles.AVC_3GPP_BL_QCIF15_AAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_ISO, DLNAProfiles.AAC_ISO_320, DLNAProfiles.AAC_ADTS, DLNAProfiles.AAC_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000),
                new TotalBitRateRangeRestriction(0, 256000),
                new FPSRestriction(15)
        });
        // AVC_3GPP_BL_QCIF15_AAC_LTP
        profileMap.put(DLNAProfiles.AVC_3GPP_BL_QCIF15_AAC_LTP, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AAC_LTP_ISO}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000),
                new TotalBitRateRangeRestriction(0, 256000),
                new FPSRestriction(15)
        });
        // AVC_3GPP_BL_QCIF15_HEAAC
        profileMap.put(DLNAProfiles.AVC_3GPP_BL_QCIF15_HEAAC, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.HEAAC_L2_ISO, DLNAProfiles.HEAAC_L2_ISO_320, DLNAProfiles.HEAAC_L2_ADTS, DLNAProfiles.HEAAC_L2_ADTS_320}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000),
                new TotalBitRateRangeRestriction(0, 256000),
                new FPSRestriction(15)
        });
        // AVC_3GPP_BL_QCIF15_AMR
        profileMap.put(DLNAProfiles.AVC_3GPP_BL_QCIF15_AMR, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AMR}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000),
                new TotalBitRateRangeRestriction(0, 256000),
                new FPSRestriction(15)
        });
        // AVC_3GPP_BL_QCIF15_AMR_WBplus
        profileMap.put(DLNAProfiles.AVC_3GPP_BL_QCIF15_AMR_WBplus, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AMR_WBplus}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 128000),
                new TotalBitRateRangeRestriction(0, 256000),
                new FPSRestriction(15)
        });
        // AVC_3GPP_BL_CIF15_AMR_WBplus
        profileMap.put(DLNAProfiles.AVC_3GPP_BL_CIF15_AMR_WBplus, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AMR_WBplus}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 384000),
                new TotalBitRateRangeRestriction(0, 600000),
        });
        // AVC_3GPP_BL_CIF30_AMR_WBplus
        profileMap.put(DLNAProfiles.AVC_3GPP_BL_CIF30_AMR_WBplus, new VideoRestriction[]{
                new VideoCodecRestriction(VideoCodec.MPEG4_P10),
                new AudioProfileRestriction(new DLNAProfiles[]{DLNAProfiles.AMR_WBplus}),
                new ContainerRestriction(Container.MPEG4),
                new CIF_Restriction(),
                new VideoBitRateRangeRestriction(0, 2000000),
                new TotalBitRateRangeRestriction(0, 3000000),
        });
    }

    /**
     * CIF 15
     */
    public class CIF_15_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps, videoBitRate, audioBitRate};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            long videoBitRate = (Long) value[3];
            long totalBitRate = videoBitRate + (Long) value[4];
            if (videoWidth == 320 && videoHeight == 240 && videoBitRate <= 384000 && totalBitRate <= 600000)
                return true;
            return false;
        }
    }

    /**
     * CIF
     */
    public class CIF_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 352 && videoHeight == 288) return true;
            if (videoWidth == 352 && videoHeight == 240) return true;
            if (videoWidth == 320 && videoHeight == 240) return true;
            if (videoWidth == 320 && videoHeight == 180) return true;
            if (videoWidth == 240 && videoHeight == 180) return true;
            if (videoWidth == 240 && videoHeight == 135) return true;
            if (videoWidth == 208 && videoHeight == 160) return true;
            if (videoWidth == 176 && videoHeight == 144) return true;
            if (videoWidth == 176 && videoHeight == 120) return true;
            if (videoWidth == 160 && videoHeight == 120) return true;
            if (videoWidth == 160 && videoHeight == 112) return true;
            if (videoWidth == 160 && videoHeight == 90) return true;
            if (videoWidth == 128 && videoHeight == 96) return true;
            return false;
        }
    }

    /**
     * MP L3 SD
     */
    public class MP_L3_SD_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 720 && videoHeight == 576) return true;
            if (videoWidth == 720 && videoHeight == 480) return true;
            if (videoWidth == 640 && videoHeight == 480) return true;
            if (videoWidth == 640 && videoHeight == 360) return true;
            return false;
        }
    }

    /**
     * MP SD
     */
    public class MP_SD_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @SuppressWarnings({"ConstantConditions"})
        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            if (videoWidth == 720 && videoHeight == 576) return true;
            if (videoWidth == 720 && videoHeight == 480) return true;
            if (videoWidth == 704 && videoHeight == 576) return true;
            if (videoWidth == 704 && videoHeight == 480) return true;
            if (videoWidth == 640 && videoHeight == 480) return true;
            if (videoWidth == 640 && videoHeight == 360) return true;
            if (videoWidth == 544 && videoHeight == 576) return true;
            if (videoWidth == 544 && videoHeight == 480) return true;
            if (videoWidth == 480 && videoHeight == 576) return true;
            if (videoWidth == 480 && videoHeight == 480) return true;
            if (videoWidth == 480 && videoHeight == 360) return true;
            if (videoWidth == 480 && videoHeight == 270) return true;
            if (videoWidth == 352 && videoHeight == 576) return true;
            if (videoWidth == 352 && videoHeight == 480) return true;
            if (videoWidth == 352 && videoHeight == 288) return true;
            if (videoWidth == 352 && videoHeight == 240) return true;
            if (videoWidth == 320 && videoHeight == 240) return true;
            if (videoWidth == 320 && videoHeight == 180) return true;
            if (videoWidth == 240 && videoHeight == 180) return true;
            if (videoWidth == 208 && videoHeight == 160) return true;
            if (videoWidth == 176 && videoHeight == 144) return true;
            if (videoWidth == 176 && videoHeight == 120) return true;
            if (videoWidth == 160 && videoHeight == 120) return true;
            if (videoWidth == 160 && videoHeight == 112) return true;
            if (videoWidth == 160 && videoHeight == 90) return true;
            if (videoWidth == 128 && videoHeight == 96) return true;
            return false;
        }
    }

    /**
     * MP HD
     */
    public class MP_HD_Restriction extends VideoRestriction<Object[]> {

        @Override
        public Object[] value(String container, String videoFormat, String videoFormatProfile, int videoFormatVersion, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioFormat, String audioFormatProfile, int audioFormatVersion, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels, DLNAProfiles audioProfile) {
            return new Object[]{videoWidth, videoHeight, fps};
        }

        @Override
        public boolean pass(Object[] value) {
            int videoWidth = (Integer) value[0];
            int videoHeight = (Integer) value[1];
            float fps = (Float) value[2];
            return ((videoWidth == 1920 || videoWidth == 1280 || videoWidth == 720) && (videoHeight == 1080 || videoHeight == 720 || videoHeight == 480));
        }
    }
}
