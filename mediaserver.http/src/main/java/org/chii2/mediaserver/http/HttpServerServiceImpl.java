package org.chii2.mediaserver.http;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.mediaserver.api.http.HttpUrl;
import org.chii2.mediaserver.http.bio.RequestListenerThread;
import org.chii2.transcoder.api.core.TranscoderService;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Dictionary;
import java.util.Enumeration;

/**
 * HTTP Server for UPnP/DLNA Media Server
 */
public class HttpServerServiceImpl implements HttpServerService {

    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected Media Library Service
    private MediaLibraryService mediaLibrary;
    // Transcoder
    private TranscoderService transcoder;
    //Configuration FIle
    private final static String CONFIG_FILE = "org.chii2.mediaserver.http";
    // Server Host Address configuration
    private final static String SERVER_IP = "http.address";
    // Server Port configuration
    private final static String SERVER_PORT = "http.port";
    // Sever Thread configuration
    private final static String SERVER_THREAD = "http.thread";
    // Server Host
    private InetAddress host;
    // Server Port
    private int port = 8888;
    // Server Thread
    private int threadCount = 2;
    // Logger
    private static Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.http");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Server Http Server Service init.");
        Dictionary props = null;
        // Read properties from ConfigAdmin Service
        try {
            Configuration config = configAdmin.getConfiguration(CONFIG_FILE);
            props = config.getProperties();
        } catch (IOException e) {
            logger.error("HttpServer fail to load configuration with exception: {}.", e.getMessage());
        }
        // Load each configuration
        if (props == null || props.isEmpty()) {
            logger.error("HttpServer load configuration <{}> with error.", CONFIG_FILE);
        } else {
            // Load preferred http server host address
            String address = ConfigUtils.loadConfiguration(props, SERVER_IP);
            if (StringUtils.isNotBlank(address)) {
                try {
                    this.host = InetAddress.getByName(address);
                    logger.debug("HttpServer configuration <{}> loaded.", SERVER_IP);
                } catch (UnknownHostException e) {
                    logger.error("HttpServer configuration <{}> is not valid.", SERVER_IP);
                }
            }

            // Load preferred http server port
            String port = ConfigUtils.loadConfiguration(props, SERVER_PORT);
            if (StringUtils.isNotBlank(port)) {
                try {
                    this.port = Integer.parseInt(port);
                    logger.debug("HttpServer configuration <{}> loaded.", SERVER_IP);
                } catch (NumberFormatException e) {
                    logger.error("HttpServer configuration <{}> is not valid.", SERVER_IP);
                }
            } else {
                logger.debug("HttpServer load default port.");
            }

            // Load preferred http server thread number
            String count = ConfigUtils.loadConfiguration(props, SERVER_THREAD);
            if (StringUtils.isNotBlank(count)) {
                try {
                    this.threadCount = Integer.parseInt(count);
                    logger.debug("HttpServer configuration <{}> loaded.", SERVER_THREAD);
                } catch (NumberFormatException e) {
                    logger.error("HttpServer configuration <{}> is not valid.", SERVER_THREAD);
                }
            } else {
                logger.debug("HttpServer load default thread count.");
            }
        }

        // Load default ip address
        if (this.host == null) {
            this.host = getAddress();
            logger.debug("HttpServer load default host address.");
        }

        // Start Server
        if (this.host != null && this.port > 0) {
            try {
                Thread thread = new RequestListenerThread(port, host, mediaLibrary, transcoder);
                thread.setDaemon(false);
                thread.start();
            } catch (IOException e) {
                logger.error("I/O Error: {}", e.getMessage());
            }
        } else {
            logger.error("Http Server doesn't have a valid address/port, can't start http server.");
        }
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Server Http Server Service destroy.");
    }

    @Override
    public InetAddress getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public URI forgeUrl(String clientProfile, String mediaType, boolean transcoded, String mediaId) {
        return HttpUrl.forgeURL(getHost().getHostAddress(), getPort(), clientProfile, mediaType, transcoded, mediaId);
    }

    /**
     * Get host IP address
     *
     * @return IP Address
     */
    private InetAddress getAddress() {
        try {
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                if (addresses.hasMoreElements()) {
                    return addresses.nextElement();
                }
            }
        } catch (SocketException e) {
            logger.debug("Error when getting host ip address: <{}>.", e.getMessage());
        }
        return null;
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

    /**
     * Inject Media Library Service
     *
     * @param mediaLibrary Media Library Service
     */
    @SuppressWarnings("unused")
    public void setMediaLibrary(MediaLibraryService mediaLibrary) {
        this.mediaLibrary = mediaLibrary;
    }

    /**
     * Inject Transcoder Service
     *
     * @param transcoder Transcoder Service
     */
    @SuppressWarnings("unused")
    public void setTranscoder(TranscoderService transcoder) {
        this.transcoder = transcoder;
    }

}
