package org.chii2.mediaserver.http.bio;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Worker Thread
 */
public class WorkerThread extends Thread {

    private final HttpService httpservice;
    private final HttpServerConnection conn;
    // Logger
    private static Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.http");

    public WorkerThread(
            final HttpService httpservice,
            final HttpServerConnection conn) {
        super();
        this.httpservice = httpservice;
        this.conn = conn;
    }

    public void run() {
        logger.info("Create a new connection thread.");
        HttpContext context = new BasicHttpContext(null);
        try {
            while (!Thread.interrupted() && this.conn.isOpen()) {
                this.httpservice.handleRequest(this.conn, context);
            }
        } catch (ConnectionClosedException ex) {
            logger.error("Client closed connection: {}", ex.getMessage());
        } catch (IOException ex) {
            logger.warn("I/O error: {}", ex.getMessage());
        } catch (HttpException ex) {
            logger.error("Unrecoverable HTTP protocol violation: {}", ex.getMessage());
        } finally {
            try {
                this.conn.shutdown();
            } catch (IOException ignore) {
            }
        }
    }

}
