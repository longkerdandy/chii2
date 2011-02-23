package org.chii2.mediaserver.http;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.mediaserver.api.http.HttpServerService;
import org.chii2.util.ConfigUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Locale;

/**
 * HTTP Server for UPnP/DLNA Media Server
 */
public class HttpServerServiceImpl implements HttpServerService {
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
    // Server Host
    private InetAddress host;
    // Server Port
    private int port = 8888;
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
                    logger.debug("HttpServer configuration <{}> loaded.", SERVER_PORT);
                } catch (NumberFormatException e) {
                    logger.error("HttpServer configuration <{}> is not valid.", SERVER_PORT);
                }
            } else {
                logger.error("HttpServer configuration <{}> is not valid.", SERVER_PORT);
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
                Thread thread = new RequestListenerThread(port, host, mediaLibrary);
                thread.setDaemon(false);
                thread.start();
            } catch (IOException ignored) {
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
     * Http Request Handler for UPnP/DLNA
     */
    private static class UPnPHttpHandler implements HttpRequestHandler {

        private final MediaLibraryService mediaLibrary;

        public UPnPHttpHandler(MediaLibraryService mediaLibrary) {
            super();
            this.mediaLibrary = mediaLibrary;
        }

        @Override
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
                FileEntity body = new FileEntity(file, "image/jpeg");
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
     * Http Request Listener Thread
     */
    private static class RequestListenerThread extends Thread {

        private final ServerSocket serversocket;
        private final HttpParams params;
        private final HttpService httpService;

        public RequestListenerThread(int port, InetAddress address, MediaLibraryService library) throws IOException {
            // Server Socket bind to port
            this.serversocket = new ServerSocket(port, 50, address);
            // Thread safe Http Params
            this.params = new SyncBasicHttpParams();
            this.params
                    .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
                    .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                    .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                    .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                    .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "Chii2DLNAHttpServer/1.1");

            // Set up the HTTP protocol processor
            HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpResponseInterceptor[]{
                    new ResponseDate(),
                    new ResponseServer(),
                    new ResponseContent(),
                    new ResponseConnControl()
            });

            // Set up request handlers
            HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
            reqistry.register("*", new UPnPHttpHandler(library));

            // Set up the HTTP service
            this.httpService = new HttpService(
                    httpproc,
                    new DefaultConnectionReuseStrategy(),
                    new DefaultHttpResponseFactory(),
                    reqistry,
                    this.params);
        }

        @Override
        public void run() {
            logger.debug("Chii2 Media Server Http Server listening on port:{}", this.serversocket.getLocalPort());
            while (!Thread.interrupted()) {
                try {
                    // Set up HTTP connection
                    Socket socket = this.serversocket.accept();
                    DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                    logger.debug("Chii2 Media Server Http Server incoming connection from {}", socket.getInetAddress());
                    conn.bind(socket, this.params);

                    // Start worker thread
                    Thread thread = new WorkerThread(this.httpService, conn);
                    thread.setDaemon(true);
                    thread.start();
                } catch (InterruptedIOException ex) {
                    logger.debug("Chii2 Media Server Http Server I/O has been interrupted, stopping receiving loop, bytes transferred: {}", ex.bytesTransferred);
                    break;
                } catch (IOException e) {
                    logger.debug("Chii2 Media Server Http Server I/O error initializing worker thread, aborting: {}", e.getMessage());
                    break;
                }
            }
        }
    }

    /**
     * Http Request Handler Thread
     */
    private static class WorkerThread extends Thread {
        // HTTP protocol handler
        private final HttpService httpservice;
        // HTTP connection
        private final HttpServerConnection conn;

        public WorkerThread(final HttpService httpservice, final HttpServerConnection conn) {
            super();

            this.httpservice = httpservice;
            this.conn = conn;
        }

        @Override
        public void run() {
            logger.debug("Chii2 Media Server Http Server start new connection thread.");
            HttpContext context = new BasicHttpContext(null);
            try {
                while (!Thread.interrupted() && this.conn.isOpen()) {
                    this.httpservice.handleRequest(this.conn, context);
                }
            } catch (ConnectionClosedException ex) {
                logger.debug("Chii2 Media Server Http Server client closed connection.");
            } catch (IOException ex) {
                logger.debug("Chii2 Media Server Http Server I/O error: {}", ex.getMessage());
            } catch (HttpException ex) {
                logger.debug("Chii2 Media Server Http Server unrecoverable HTTP protocol violation: {}", ex.getMessage());
            } finally {
                try {
                    this.conn.shutdown();
                } catch (IOException ignore) {
                }
            }
        }

    }
}
