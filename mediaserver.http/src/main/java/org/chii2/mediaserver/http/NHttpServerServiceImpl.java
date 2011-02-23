package org.chii2.mediaserver.http;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.nio.DefaultServerIOEventDispatch;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.entity.NFileEntity;
import org.apache.http.nio.protocol.EventListener;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.*;
import org.apache.http.nio.protocol.BufferingHttpServiceHandler;
import org.apache.http.util.EntityUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.*;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Locale;

/**
 * NIO HTTP Server for UPnP/DLNA Media Server
 */
public class NHttpServerServiceImpl implements HttpServerService {
    // Injected ConfigAdmin Service
    private ConfigurationAdmin configAdmin;
    // Injected Media Library Service
    private MediaLibraryService mediaLibrary;
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
            Thread thread = new RequestListenerThread(port, host, threadCount, mediaLibrary);
            thread.setDaemon(false);
            thread.start();
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
     * Request Handler
     */
    private static class UPnPHttpHandler implements HttpRequestHandler {

        private final MediaLibraryService mediaLibrary;

        public UPnPHttpHandler(MediaLibraryService mediaLibrary) {
            super();
            this.mediaLibrary = mediaLibrary;
        }

        public void handle(
                final HttpRequest request,
                final HttpResponse response,
                final HttpContext context) throws HttpException, IOException {

            // Check HTTP Methods
            String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
            if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
                throw new MethodNotSupportedException(method + " method not supported");
            }

            // Check HTTP Request with content
            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                byte[] entityContent = EntityUtils.toByteArray(entity);
                logger.debug("Incoming entity content (bytes): {}" + entityContent.length);
            }

            String target = request.getRequestLine().getUri();
            logger.debug("Receive HTTP request for target <{}>.", target);
            String type = getTypeFromTarget(target);
            String id = getIdFromTarget(target);
            // TODO Transcoding

            // Query the library and get the file
            File file = null;
            if ("image".equalsIgnoreCase(type)) {
                Image image = mediaLibrary.getImageById(id);
                if (image != null) {
                    file = new File(image.getAbsoluteName());
                }
            }
            if (file == null) {
                response.setStatusCode(HttpStatus.SC_NOT_FOUND);
                logger.debug("Chii2 Media Server Http Server request empty file.");
            } else if (!file.exists()) {
                response.setStatusCode(HttpStatus.SC_NOT_FOUND);
                logger.debug("Chii2 Media Server Http Server file <{}> not found.", file.getPath());
            } else if (!file.canRead() || file.isDirectory()) {
                response.setStatusCode(HttpStatus.SC_FORBIDDEN);
                logger.debug("Chii2 Media Server Http Server cannot read file <{}>.", file.getPath());
            } else {
                response.setStatusCode(HttpStatus.SC_OK);
                NFileEntity body = new NFileEntity(file, "image/jpeg");
                response.setEntity(body);
                logger.debug("Chii2 Media Server Http Server serving file <{}>.", file.getPath());
            }
        }

        /**
         * Get media type from url target
         *
         * @param target URL target
         * @return Media Type
         */
        private String getTypeFromTarget(String target) {
            if (target != null && target.startsWith("/") && target.length() > 1) {
                target = target.substring(1);
            }
            if (target != null && StringUtils.isNotEmpty(target)) {
                int index = target.indexOf('/');
                if (index > 0) {
                    return target.substring(0, index);

                }
            }
            return null;
        }

        /**
         * Get media id from url target
         *
         * @param target URL Target
         * @return Media Id
         */
        private String getIdFromTarget(String target) {
            if (target != null && target.endsWith("/") && target.length() > 1) {
                target = target.substring(0, target.length());
            }
            if (target != null && StringUtils.isNotEmpty(target)) {
                int index = target.lastIndexOf('/');
                if (index > 0) {
                    String id = target.substring(index + 1);
                    int transIndex = id.indexOf('#');
                    if (transIndex > 0) {
                        return id.substring(0, transIndex);
                    } else {
                        return id;
                    }
                }
            }
            return null;
        }
    }

    /**
     * Event Logger
     */
    private static class EventLogger implements EventListener {

        public void connectionOpen(final NHttpConnection conn) {
            logger.debug("Connection open: {}", conn);
        }

        public void connectionTimeout(final NHttpConnection conn) {
            logger.debug("Connection timed out: {}", conn);
        }

        public void connectionClosed(final NHttpConnection conn) {
            logger.debug("Connection closed: {}", conn);
        }

        public void fatalIOException(final IOException ex, final NHttpConnection conn) {
            logger.debug("I/O error: {}.", ex.getMessage());
        }

        public void fatalProtocolException(final HttpException ex, final NHttpConnection conn) {
            logger.debug("HTTP error: {}.", ex.getMessage());
        }

    }

    /**
     * Http Request Listener Thread
     */
    private static class RequestListenerThread extends Thread {

        private final int port;
        private final InetAddress address;
        private final int threadCount;
        private final HttpParams params;
        private final BufferingHttpServiceHandler handler;

        public RequestListenerThread(int port, InetAddress address, int threadCount, MediaLibraryService library) {

            this.port = port;
            this.address = address;
            this.threadCount = threadCount;

            // Parameters
            params = new SyncBasicHttpParams();
            params
                    .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
                    .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                    .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                    .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                    .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "Chii2DLNAHttpServer/1.1");

            HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpResponseInterceptor[]{
                    new ResponseDate(),
                    new ResponseServer(),
                    new ResponseContent(),
                    new ResponseConnControl()
            });

            handler = new BufferingHttpServiceHandler(
                    httpproc,
                    new DefaultHttpResponseFactory(),
                    new DefaultConnectionReuseStrategy(),
                    params);

            // Set up request handlers
            HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
            reqistry.register("*", new UPnPHttpHandler(library));
            handler.setHandlerResolver(reqistry);

            // Provide an event logger
            handler.setEventListener(new EventLogger());
        }

        @Override
        public void run() {
            IOEventDispatch ioEventDispatch = new DefaultServerIOEventDispatch(handler, params);
            try {
                ListeningIOReactor ioReactor = new DefaultListeningIOReactor(threadCount, params);
                ioReactor.listen(new InetSocketAddress(address, port));
                ioReactor.execute(ioEventDispatch);
            } catch (IOReactorException e) {
                logger.error("Http Server I/O Reactor error initializing listener thread, aborting: {}", e.getMessage());
            } catch (InterruptedIOException ex) {
                logger.error("Http Server I/O has been interrupted, stopping receiving loop, bytes transferred: {}", ex.bytesTransferred);
            } catch (IOException e) {
                logger.error("Http Server I/O error initializing listener thread, aborting: {}", e.getMessage());
            }
            logger.error("Http Server has shutdown.");
        }
    }
}
