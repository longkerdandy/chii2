package org.chii2.medialibrary.provider.tmdb.handler;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.provider.tmdb.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Movie Response Handler, handle movie request replied by TMDb
 */
public class MovieResponseHandler extends AsyncCompletionHandler<List<MovieInfo>> {
    // Parser
    private final Parser parser;
    // Result count
    private final int count;
    // Result poster count
    private final int posterCount;
    // Result backdrop count
    private final int backdropCount;
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");

    /**
     * Constructor
     *
     * @param parser        Parser
     * @param count         Result Count
     * @param posterCount   Result poster count
     * @param backdropCount Result backdrop count
     */
    public MovieResponseHandler(Parser parser, int count, int posterCount, int backdropCount) {
        // Call Super
        super();
        // Init
        this.parser = parser;
        this.count = count;
        this.posterCount = posterCount;
        this.backdropCount = backdropCount;
    }

    @Override
    public List<MovieInfo> onCompleted(Response response) throws Exception {
        if (response.getStatusCode() == 200 && response.hasResponseBody()) {
            // Get content
            String content = response.getResponseBody("utf-8");
            // Return
            return this.parser.parseMovieInfo(content, this.count, this.posterCount, this.backdropCount);
        } else {
            // Throw exception
            throw new Exception("Receive non successful status from remote site: " + response.getStatusText());
        }
    }
}
