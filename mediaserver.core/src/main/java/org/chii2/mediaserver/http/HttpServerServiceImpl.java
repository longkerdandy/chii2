package org.chii2.mediaserver.http;

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
import org.chii2.mediaserver.api.http.HttpServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

/**
 * HTTP Server for UPnP/DLNA Media Server
 */
public class HttpServerServiceImpl implements HttpServerService {

    // Logger
    private static Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.http");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Server Http Server Service init.");
        // Start Threads
        Thread t = null;
        try {
            t = new RequestListenerThread(8888);
            t.setDaemon(false);
            t.start();
        } catch (IOException e) {
        }
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Server Http Server Service destroy.");
    }

    /**
     * Http Request Handler for UPnP/DLNA
     */
    static class UPnPHttpHandler implements HttpRequestHandler {

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

            // TODO: This will be needed in future
            //String target = request.getRequestLine().getUri();

            // Check HTTP Request with content
            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                byte[] entityContent = EntityUtils.toByteArray(entity);
            }

            // TODO: This is fake
            final File file = new File("/home/longkerdandy/Pictures/Koala.jpg");
            if (!file.exists()) {
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

    }

    /**
     * Http Request Listener Thread
     */
    static class RequestListenerThread extends Thread {

        private final ServerSocket serversocket;
        private final HttpParams params;
        private final HttpService httpService;

        public RequestListenerThread(int port) throws IOException {
            // Server Socket bind to port
            this.serversocket = new ServerSocket(port);
            // Thread safe Http Params
            this.params = new SyncBasicHttpParams();
            this.params
                    .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
                    .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                    .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                    .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                    .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "Chii2MediaServerHttpServer/1.1");

            // Set up the HTTP protocol processor
            HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpResponseInterceptor[]{
                    new ResponseDate(),
                    new ResponseServer(),
                    new ResponseContent(),
                    new ResponseConnControl()
            });

            // Set up request handlers
            HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
            reqistry.register("*", new UPnPHttpHandler());

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
                    Thread t = new WorkerThread(this.httpService, conn);
                    t.setDaemon(true);
                    t.start();
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
    static class WorkerThread extends Thread {
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
