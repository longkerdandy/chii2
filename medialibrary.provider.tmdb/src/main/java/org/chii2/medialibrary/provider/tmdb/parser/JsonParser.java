package org.chii2.medialibrary.provider.tmdb.parser;

import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Json Parser for TMDb
 */
public class JsonParser implements Parser {
    // Movie Factory
    private MovieFactory movieFactory;
    // Json Mapper
    private ObjectMapper mapper;
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");

    /**
     * Constructor
     *
     * @param movieFactory Movie Factory
     */
    public JsonParser(MovieFactory movieFactory) {
        this.movieFactory = movieFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<MovieInfo> parseMovieInfo(String content, int maxCount, int posterCount, int backdropCount) throws ParseException {
        // Minus
        if (posterCount < 0) {
            posterCount = Integer.MAX_VALUE;
        }
        if (backdropCount < 0) {
            backdropCount = Integer.MAX_VALUE;
        }
        // Result
        List<MovieInfo> result = new ArrayList<MovieInfo>();
        try {
            // Root node
            JsonNode rootNode = this.mapper.readTree(content);
            // Loop and create movie info list
            for (int i = 0; i < rootNode.size() && i < maxCount; i++) {
                // New MovieInfo
                MovieInfo movieInfo = this.movieFactory.createMovieInfo();
                // Movie Node
                JsonNode movieNode = rootNode.path(i);
                movieInfo.setProviderName("TMDb");
                movieInfo.setScore(movieNode.path("score").getValueAsDouble());
                movieInfo.setPopularity(movieNode.path("popularity").getValueAsInt());
                movieInfo.setAdult(movieNode.path("adult").getValueAsBoolean());
                movieInfo.setLanguage(movieNode.path("language").getValueAsText());
                movieInfo.setOriginalName(movieNode.path("original_name").getValueAsText());
                movieInfo.setName(movieNode.path("name").getValueAsText());
                movieInfo.setAlternativeName(movieNode.path("alternative_name").getValueAsText());
                movieInfo.setProviderId(movieNode.path("id").getValueAsText());
                movieInfo.setIMDbId(movieNode.path("imdb_id").getValueAsText());
                movieInfo.setUrl(movieNode.path("url").getValueAsText());
                movieInfo.setVotes(movieNode.path("votes").getValueAsInt());
                movieInfo.setRating(movieNode.path("rating").getValueAsDouble());
                movieInfo.setCertification(movieNode.path("certification").getValueAsText());
                movieInfo.setOverview(movieNode.path("overview").getValueAsText());
                movieInfo.setVersion(movieNode.path("version").getValueAsInt());
                movieInfo.setReleasedDate(new SimpleDateFormat("yyyy-MM-dd").parse(movieNode.path("released").getValueAsText()));
                movieInfo.setLastModified(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(movieNode.path("last_modified_at").getValueAsText()));
                // Movie Poster Node
                JsonNode posterNode = movieNode.path("posters");
                List<String> posterIDs = new ArrayList<String>();
                for (int j = 0; j < posterNode.size(); j++) {
                    // Image Node
                    JsonNode imageNode = posterNode.path(j).path("image");
                    // Calculate poster count
                    if (!posterIDs.contains(imageNode.path("id").getValueAsText())) {
                        if (posterIDs.size() >= posterCount) {
                            break;
                        } else {
                            posterIDs.add(imageNode.path("id").getValueAsText());
                        }
                    }
                    // New MovieImage
                    MovieImage movieImage = this.movieFactory.createMovieImage();
                    movieImage.setContentType(imageNode.path("type").getValueAsText());
                    movieImage.setImageType(MovieImage.JPEG_IMAGE_TYPE);
                    movieImage.setSize(imageNode.path("size").getValueAsText());
                    movieImage.setHeight(imageNode.path("height").getValueAsInt());
                    movieImage.setWidth(imageNode.path("width").getValueAsInt());
                    movieImage.setUrl(imageNode.path("url").getValueAsText());
                    movieImage.setProviderId(imageNode.path("id").getValueAsText());
                    // Add to movie info
                    movieInfo.addImage(movieImage);
                }
                // Movie Backdrop Node
                JsonNode backdropNode = movieNode.path("backdrops");
                List<String> backdropIDs = new ArrayList<String>();
                for (int j = 0; j < backdropNode.size(); j++) {
                    // Image Node
                    JsonNode imageNode = backdropNode.path(j).path("image");
                    // Calculate poster count
                    if (!backdropIDs.contains(imageNode.path("id").getValueAsText())) {
                        if (backdropIDs.size() >= backdropCount) {
                            break;
                        } else {
                            backdropIDs.add(imageNode.path("id").getValueAsText());
                        }
                    }
                    // New MovieImage
                    MovieImage movieImage = this.movieFactory.createMovieImage();
                    movieImage.setContentType(imageNode.path("type").getValueAsText());
                    movieImage.setImageType(MovieImage.JPEG_IMAGE_TYPE);
                    movieImage.setSize(imageNode.path("size").getValueAsText());
                    movieImage.setHeight(imageNode.path("height").getValueAsInt());
                    movieImage.setWidth(imageNode.path("width").getValueAsInt());
                    movieImage.setUrl(imageNode.path("url").getValueAsText());
                    movieImage.setProviderId(imageNode.path("id").getValueAsText());
                    // Add to movie info
                    movieInfo.addImage(movieImage);
                }
                // Add to result
                result.add(movieInfo);
            }
        } catch (IOException e) {
            logger.error("JsonParser catch a IOException during parsing: {}.", e.getMessage());
            throw new ParseException(e.getMessage());
        } catch (java.text.ParseException e) {
            logger.error("JsonParser catch a ParseException during parsing: {}.", e.getMessage());
            throw new ParseException(e.getMessage());
        }
        return result;
    }
}
