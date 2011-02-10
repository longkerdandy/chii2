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
    // Movie Name property key in the movie info provided events
    public final static String MOVIE_NAME_PROPERTY = "movie_name";
    // Movie Year property key in the movie info provided events
    public final static String MOVIE_YEAR_PROPERTY = "movie_year";
    // Exclude providers property key in the movie info provided events
    public final static String EXCLUDE_PROVIDERS_PROPERTY = "exclude_providers";
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
     * @param excludeProviders Exclude Movie Providers (Which may already failed)
     */
    public void getMovieInformationByName(String movieId, String movieName, List<String> excludeProviders);

    /**
     * Get movie information by movie name and use year to narrow result. This usually done by searching the movie name and fetch the first result.
     * This should be done in a asynchronous way
     *
     * @param movieId          Movie Id
     * @param movieName        Movie Name (Guessed Name)
     * @param movieYear        Movie Year (Guessed Year)
     * @param excludeProviders Exclude Movie Providers (Which may already failed)
     */
    public void getMovieInformationByName(String movieId, String movieName, String movieYear, List<String> excludeProviders);

    /**
     * Get movie information by movie name. This usually done by searching the movie name and fetch all possible results.
     * This should be done in a asynchronous way
     *
     * @param movieId          Movie Id
     * @param movieName        Movie Name (Guessed Name)
     * @param excludeProviders Exclude Movie Providers (Which may already failed)
     */
    public void getAllMovieInformationByName(String movieId, String movieName, List<String> excludeProviders);

    /**
     * Get movie information by movie name and use year to narrow result. This usually done by searching the movie name and fetch all possible results.
     * This should be done in a asynchronous way
     *
     * @param movieId          Movie Id
     * @param movieName        Movie Name (Guessed Name)
     * @param movieYear        Movie Year (Guessed Year)
     * @param excludeProviders Exclude Movie Providers (Which may already failed)
     */
    public void getAllMovieInformationByName(String movieId, String movieName, String movieYear, List<String> excludeProviders);

    /**
     * Get Movie Image from image url.
     * This should be done in a asynchronous way.
     *
     * @param imageId Image ID
     * @param url     Image URI
     */
    public void getMovieImageByUrl(String imageId, String url);
}
