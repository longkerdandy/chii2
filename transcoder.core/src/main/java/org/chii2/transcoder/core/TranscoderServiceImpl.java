package org.chii2.transcoder.core;

import org.chii2.mediaserver.api.dlna.DLNAProfile;
import org.chii2.transcoder.api.core.TranscoderService;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Transcoder Server act as a main entrance for all kinds of transcoding engines.
 */
public class TranscoderServiceImpl implements TranscoderService {
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.core");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Transcoder Core Service init.");
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Transcoder Core Service destroy.");
    }

    @Override
    public String getClientProfile(String userAgent) {
        if (userAgent != null && userAgent.contains("Xbox")) {
            return PROFILE_XBOX;
        } else {
            return PROFILE_COMMON;
        }
    }

    @Override
    public String getClientProfile(List<String> userAgent) {
        for (String info : userAgent) {
            if (info != null && info.contains("Xbox")) {
                return PROFILE_XBOX;
            }
        }
        return PROFILE_COMMON;
    }

    @Override
    public boolean isValidProfile(String client, DLNAProfile.Profile profile) {
        if (client.equals(PROFILE_XBOX)) {
            if (profile == DLNAProfile.Profile.PROFILE_WMV_SPLL_BASE || profile == DLNAProfile.Profile.PROFILE_WMV_SPML_BASE || profile == DLNAProfile.Profile.PROFILE_WMV_SPML_MP3 ||
                    profile == DLNAProfile.Profile.PROFILE_WMV_MED_BASE || profile == DLNAProfile.Profile.PROFILE_WMV_MED_FULL || profile == DLNAProfile.Profile.PROFILE_WMV_MED_PRO ||
                    profile == DLNAProfile.Profile.PROFILE_WMV_HIGH_FULL || profile == DLNAProfile.Profile.PROFILE_WMV_HIGH_PRO) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public DLNAProfile.Profile getTranscodedProfile(String client, String videoCodec, long videoBitRate, int videoWidth, int videoHeight, float fps, String audioCodec, long audioBitRate, long audioSampleBitRate, int audioChannels) {
        if (client.equals(PROFILE_XBOX)) {
            DLNAProfile.VideoProfile videoProfile = DLNAProfile.getVideoProfile("WVC1", videoBitRate, videoWidth, videoHeight, fps);
            DLNAProfile.AudioProfile audioProfile = DLNAProfile.getAudioProfile("161", audioBitRate, audioSampleBitRate, audioChannels);
            DLNAProfile.Profile profile = DLNAProfile.getDLNAProfile(videoProfile, audioProfile);
            if (profile != DLNAProfile.Profile.PROFILE_INVALID) {
                return profile;
            } else {
                if (videoProfile == DLNAProfile.VideoProfile.VIDEO_PROFILE_WMV_SIMPLE_LOW) {
                    return DLNAProfile.Profile.PROFILE_WMV_SPLL_BASE;
                } else if (videoProfile == DLNAProfile.VideoProfile.VIDEO_PROFILE_WMV_SIMPLE_MEDIUM) {
                    return DLNAProfile.Profile.PROFILE_WMV_SPML_BASE;
                } else if (videoProfile == DLNAProfile.VideoProfile.VIDEO_PROFILE_WMV_MAIN_MEDIUM) {
                    return DLNAProfile.Profile.PROFILE_WMV_MED_FULL;
                } else if (videoProfile == DLNAProfile.VideoProfile.VIDEO_PROFILE_WMV_MAIN_HIGH) {
                    return DLNAProfile.Profile.PROFILE_WMV_HIGH_FULL;
                } else {
                    return DLNAProfile.Profile.PROFILE_WMV_HIGH_FULL;
                }
            }
        } else {
            return DLNAProfile.Profile.PROFILE_INVALID;
        }
    }

    @Override
    public String getImageTranscodedType(String client, String imageType) {
        if ("GIF".equalsIgnoreCase(imageType) || "GIFf".equalsIgnoreCase(imageType)) {
            return IMAGE_TYPE_GIF;
        } else if ("JPEG".equalsIgnoreCase(imageType) || "JPG".equalsIgnoreCase(imageType)) {
            return IMAGE_TYPE_JPEG;
        } else if ("PNG".equalsIgnoreCase(imageType) || "PNGf".equalsIgnoreCase(imageType)) {
            return IMAGE_TYPE_PNG;
        } else {
            return IMAGE_TYPE_JPEG;
        }
    }

    @Override
    public String getImageTranscodedMime(String client, String imageType) {
        if ("GIF".equalsIgnoreCase(imageType) || "GIFf".equalsIgnoreCase(imageType)) {
            return IMAGE_MIME_GIF;
        } else if ("JPEG".equalsIgnoreCase(imageType) || "JPG".equalsIgnoreCase(imageType)) {
            return IMAGE_MIME_JPEG;
        } else if ("PNG".equalsIgnoreCase(imageType) || "PNGf".equalsIgnoreCase(imageType)) {
            return IMAGE_MIME_PNG;
        } else {
            return IMAGE_MIME_JPEG;
        }
    }

    @Override
    public File getImageTranscodedFile(String client, String imageType, File imageFile) {
        return imageFile;
    }

    /**
     * Inject Config Admin
     *
     * @param configAdmin Config Admin
     */
    @SuppressWarnings("unused")
    public void setConfigAdmin(ConfigurationAdmin configAdmin) {
        this.configAdmin = configAdmin;
    }
}
