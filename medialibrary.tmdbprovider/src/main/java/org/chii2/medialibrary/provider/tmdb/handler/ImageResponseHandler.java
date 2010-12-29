package org.chii2.medialibrary.provider.tmdb.handler;

import org.apache.commons.io.IOUtils;
import org.chii2.medialibrary.api.provider.MovieInfoProviderService;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Image Response Handler, handle image binary file request reply
 */
public class ImageResponseHandler extends SimpleChannelUpstreamHandler {

    // Image ID
    private String imageId;
    // EventAdmin
    private EventAdmin eventAdmin;
    // Logger
    private Logger logger;

    public ImageResponseHandler(String imageId, EventAdmin eventAdmin) {
        // Call Super
        super();

        this.imageId = imageId;
        this.eventAdmin = eventAdmin;
        logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        // HttpResponse
        HttpResponse response = (HttpResponse) e.getMessage();

        // If response is OK, parse content, else raise failed event
        if (response.getStatus().equals(HttpResponseStatus.OK)) {
            logger.debug("Receive a successful http response when receive image <{}>.", imageId);
            // Read image object from input stream
            ChannelBuffer buffer = response.getContent();
            ChannelBufferInputStream stream = new ChannelBufferInputStream(buffer);
            byte[] image = IOUtils.toByteArray(stream);

            //Raise event
            raiseImageProvidedEvent(image);
        } else {
            logger.debug("Receive a unsuccessful http response when receive image <{}>.", imageId);
        }

        // Close the channel
        e.getChannel().close();
    }

    @Override
    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.debug("A exception occurred when receive movie <{}>: {}", imageId, e.getCause().getMessage());
    }

    /**
     * Raise a image provided event
     *
     * @param image Image binary content
     */
    private void raiseImageProvidedEvent(byte[] image) {
        // Prepare properties
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(MovieInfoProviderService.IMAGE_ID_PROPERTY, imageId);
        properties.put(MovieInfoProviderService.IMAGE_CONTENT_PROPERTY, image);

        // Send an asynchronous event
        Event event = new Event(MovieInfoProviderService.MOVIE_IMAGE_PROVIDED_TOPIC, properties);
        logger.debug("Send a image <{}> provided event.", imageId);
        eventAdmin.postEvent(event);
    }
}
