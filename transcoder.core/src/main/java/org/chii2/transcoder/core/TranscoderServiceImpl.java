package org.chii2.transcoder.core;

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
