package org.chii2.mediaserver.api.provider;

import org.chii2.mediaserver.api.content.container.VisualContainer;

import java.util.List;

/**
 * Online Video Provider Service
 */
public interface OnlineVideoProviderService {

    // Container Prefix
    public final static String ONLINE_VIDEO_CONTAINER_PREFIX = "OVC-";

    /**
     * Get the provider name
     *
     * @return Provider name
     */
    public String getProviderName();

    /**
     * Get Video URL Prefix
     * Basic format is: protocol://host_name:port
     * for eg: http://v.sample.com:8080
     *
     * @return Video URL Prefix
     */
    public String getVideoHostUrl();

    /**
     * Is given Object ID belongs to this provider
     *
     * @param id Object ID (Container or Item)
     * @return True if id belongs to this provider
     */
    public boolean isMatch(String id);

    /**
     * Get the Root Container
     *
     * @param filter Container Filter
     * @return Root Container
     */
    public VisualContainer getRootContainer(String filter);

    /**
     * Get the Container by ID
     *
     * @param filter Container Filter
     * @param id     Container ID
     * @return Container
     */
    public VisualContainer getContainerByID(String filter, String id);

    /**
     * Get Online Video Container Format
     *
     * @param url Video URL
     * @return Container Format
     */
    public String getContainer(String url);

    /**
     * Get Real URL Address (List)
     * An Online Video may be cut to multiple part by service provider, so we return a List of URL here
     *
     * @param url Video URL
     * @return Real URL Address (List)
     */
    public List<String> getRealAddress(String url);

    /**
     * Get Online Video Pipe Commands
     * Pipe Commands are used in Transcoder as stream input
     * for eg: ffmpeg -i - will be used to read from pipe.
     * Pipe input usually be done by Wget or customer downloader.
     * Please Note: this should be the REAL URL here
     *
     * @param url Real Video URL
     * @return Pipe Commands
     */
    public List<String> getPipe(String url);

    /**
     * Get Online Video Format
     *
     * @param url Video URL
     * @return Video Format
     */
    public String getVideoFormat(String url);

    /**
     * Get Online Video Format Profile
     *
     * @param url Video URL
     * @return Video Format Profile
     */
    public String getVideoFormatProfile(String url);

    /**
     * Get Online Video Format Version
     *
     * @param url Video URL
     * @return Video Format Version
     */
    public int getVideoFormatVersion(String url);

    /**
     * Get Online Video Codec
     *
     * @param url Video URL
     * @return Video Codec
     */
    public String getVideoCodec(String url);

    /**
     * Get Online Video BitRate
     *
     * @param url Video URL
     * @return Video BitRate
     */
    public long getVideoBitRate(String url);

    /**
     * Get Online Video Width
     *
     * @param url Video URL
     * @return Video Width
     */
    public int getVideoWidth(String url);

    /**
     * Get Online Video Height
     *
     * @param url Video URL
     * @return Video Height
     */
    public int getVideoHeight(String url);

    /**
     * Get Online Video FPS
     *
     * @param url Video URL
     * @return Video FPS
     */
    public float getVideoFps(String url);

    /**
     * Get Online Video Audio Format
     *
     * @param url Video URL
     * @return Audio Format
     */
    public String getAudioFormat(String url);

    /**
     * Get Online Video Audio Format Profile
     *
     * @param url Video URL
     * @return Audio Format Profile
     */
    public String getAudioFormatProfile(String url);

    /**
     * Get Online Video Audio Format Version
     *
     * @param url Video URL
     * @return Audio Format Version
     */
    public int getAudioFormatVersion(String url);

    /**
     * Get Online Video Audio Codec
     *
     * @param url Video URL
     * @return Audio Codec
     */
    public String getAudioCodec(String url);

    /**
     * Get Online Video Audio BitRate
     *
     * @param url Video URL
     * @return Audio BitRate
     */
    public long getAudioBitRate(String url);

    /**
     * Get Online Video Audio Sample BitRate
     *
     * @param url Video URL
     * @return Audio Sample BitRate
     */
    public long getAudioSampleBitRate(String url);

    /**
     * Get Online Video Audio Channels
     *
     * @param url Video URL
     * @return Audio Channels
     */
    public int getAudioChannels(String url);
}
