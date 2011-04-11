package org.chii2.medialibrary.api.provider;

/**
 * Movie Information Provider Service, each provider can provide movie information by download from internet or somewhere else
 */
public interface MovieInfoProviderService {

    // Event Topic for movie information request
    public final static String MOVIE_INFO_REQUEST_TOPIC = "org/chii2/medialibrary/provider/movie/INFO_REQUEST";
    // Event Topic for movie information provided
    public final static String MOVIE_INFO_PROVIDED_TOPIC = "org/chii2/medialibrary/provider/movie/INFO_PROVIDED";
    // Event Topic for movie information failed
    public final static String MOVIE_INFO_FAILED_TOPIC = "org/chii2/medialibrary/provider/movie/INFO_FAILED";
    // Movie ID property key in the movie info request / provided /failed events
    public final static String MOVIE_ID_PROPERTY = "movie_id";
    // Movie Name property key in the movie info request events
    public final static String MOVIE_NAME_PROPERTY = "movie_name";
    // Movie Year property key in the movie info request events
    public final static String MOVIE_YEAR_PROPERTY = "movie_year";
    // Movie Result Count property key in the movie info request events
    public final static String RESULT_COUNT_PROPERTY = "result_count";
    // Movie Poster Count property key in the movie info request events
    public final static String POSTER_COUNT_PROPERTY = "poster_count";
    // Movie Backdrop Count property key in the movie info request events
    public final static String BACKDROP_COUNT_PROPERTY = "backdrop_count";
    // Movie information property key in the movie info provided events
    public final static String MOVIE_INFO_PROPERTY = "movie_info";

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
     * @param movieId       Movie Id
     * @param movieName     Movie Name (Guessed Name)
     * @param resultCount   Max result count
     * @param posterCount   Result poster count
     * @param backdropCount Result backdrop count
     */
    public void getMovieInformationByName(String movieId, String movieName, int resultCount, int posterCount, int backdropCount);

    /**
     * Get movie information by movie name and use year to narrow result. This usually done by searching the movie name and fetch the first result.
     * This should be done in a asynchronous way
     *
     * @param movieId       Movie Id
     * @param movieName     Movie Name (Guessed Name)
     * @param movieYear     Movie Year (Guessed Year)
     * @param resultCount   Max result count
     * @param posterCount   Result poster count
     * @param backdropCount Result backdrop count
     */
    public void getMovieInformationByName(String movieId, String movieName, int movieYear, int resultCount, int posterCount, int backdropCount);
}
