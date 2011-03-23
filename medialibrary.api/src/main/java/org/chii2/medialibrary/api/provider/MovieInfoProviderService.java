package org.chii2.medialibrary.api.provider;

import java.util.List;

/**
 * Movie Information Provider Service, each provider can provide movie information by download from internet or somewhere else
 */
public interface MovieInfoProviderService {

    // Event Topic for movie information provided
    public final static String MOVIE_INFO_PROVIDED_TOPIC = "org/chii2/medialibrary/provider/movie/INFO_PROVIDED";
    // Event Topic for movie information failed
    public final static String MOVIE_INFO_FAILED_TOPIC = "org/chii2/medialibrary/provider/movie/INFO_FAILED";
    // Movie ID property key in the movie info provided events
    public final static String MOVIE_ID_PROPERTY = "movie_id";
    // Movie information property key in the movie info provided events
    public final static String MOVIE_INFO_PROPERTY = "movie_info";

    // Event Topic for movie image provided
    public final static String MOVIE_IMAGE_PROVIDED_TOPIC = "org/chii2/medialibrary/provider/movie/IMAGE_PROVIDED";
    // Image ID property key in the image provided events
    public final static String IMAGE_ID_PROPERTY = "image_id";
    // Image picture property key in the image provide events
    public final static String IMAGE_CONTENT_PROPERTY = "image_content";

    /**
     * Get the provider name
     *
     * @return Provider name
     */
    public String getProviderName();

    /**
     * Get movie information by movie name. This usually done by searching the movie name and fetch the first result.
     * This should be done in a asynchronous way
     *
     * @param movieId          Movie Id
     * @param movieName        Movie Name (Guessed Name)
     * @param resultCount      Max result count
     * @param posterCount      Result poster count
     * @param backdropCount    Result backdrop count
     */
    public void getMovieInformationByName(String movieId, String movieName, int resultCount, int posterCount, int backdropCount);

    /**
     * Get movie information by movie name and use year to narrow result. This usually done by searching the movie name and fetch the first result.
     * This should be done in a asynchronous way
     *
     * @param movieId          Movie Id
     * @param movieName        Movie Name (Guessed Name)
     * @param movieYear        Movie Year (Guessed Year)
     * @param resultCount      Max result count
     * @param posterCount      Result poster count
     * @param backdropCount    Result backdrop count
     */
    public void getMovieInformationByName(String movieId, String movieName, int movieYear, int resultCount, int posterCount, int backdropCount);

    /**
     * Get Movie Image from image url.
     * This should be done in a asynchronous way.
     *
     * @param imageId Image ID
     * @param url     Image URI
     */
    public void getMovieImageByUrl(String imageId, String url);
}
