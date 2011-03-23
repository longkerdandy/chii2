package org.chii2.medialibrary.provider.tmdb.handler;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;
import org.apache.commons.io.IOUtils;
import org.chii2.medialibrary.api.provider.MovieInfoProviderService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Image Response Handler, handle image binary file request reply
 */
public class ImageResponseHandler extends AsyncCompletionHandler<Integer> {
    // Image ID
    private String imageId;
    // EventAdmin
    private EventAdmin eventAdmin;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");

    /**
     * Constructor
     *
     * @param imageId    Image ID
     * @param eventAdmin EventAdmin
     */
    public ImageResponseHandler(String imageId, EventAdmin eventAdmin) {
        // Call Super
        super();

        this.imageId = imageId;
        this.eventAdmin = eventAdmin;
    }

    @Override
    public Integer onCompleted(Response response) throws Exception {
        if (response.getStatusCode() == 200 && response.hasResponseBody()) {
            // Get content
            InputStream stream = response.getResponseBodyAsStream();
            // Convert to image
            byte[] image = IOUtils.toByteArray(stream);
            // Send event
            raiseImageProvidedEvent(image);
        } else {
            logger.warn("Receive non successful status from remote site: {}.", response.getStatusText());
        }
        // Return status code
        return response.getStatusCode();
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
        logger.debug("Send a movie image <{}> provided event.", imageId);
        eventAdmin.postEvent(event);
    }
}
