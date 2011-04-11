package org.chii2.medialibrary.provider.tmdb.handler;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;
import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;


/**
 * Image Response Handler, handle image binary file request reply
 */
public class ImageResponseHandler extends AsyncCompletionHandler<byte[]> {
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");

    /**
     * Constructor
     */
    public ImageResponseHandler() {
        // Call Super
        super();
    }

    @Override
    public byte[] onCompleted(Response response) throws Exception {
        if (response.getStatusCode() == 200 && response.hasResponseBody()) {
            // Get content
            InputStream stream = response.getResponseBodyAsStream();
            // Convert to image
            // Return
            return IOUtils.toByteArray(stream);
        } else {
            // Throw exception
            throw new Exception("Receive non successful status from remote site: " + response.getStatusText());
        }
    }
}
