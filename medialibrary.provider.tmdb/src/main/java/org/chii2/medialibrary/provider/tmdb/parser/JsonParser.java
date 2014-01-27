package org.chii2.medialibrary.provider.tmdb.parser;

import org.apache.commons.lang.exception.ExceptionUtils;
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
    private final MovieFactory movieFactory;
    // Json Mapper
    private final ObjectMapper mapper;
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.provider.tmdb");

    /**
     * Constructor
     *
     * @param movieFactory Movie Factory
     */
    public JsonParser(MovieFactory movieFactory) {
        this.movieFactory = movieFactory;
        this.mapper = new ObjectMapper();
    }

    @SuppressWarnings("ConstantConditions")
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
        List<MovieInfo> result = new ArrayList<>();
        try {
            // Root node
            JsonNode rootNode = this.mapper.readTree(content);
            // Not Found
            if (rootNode.size() == 1 && "Nothing found.".equalsIgnoreCase(rootNode.path(0).asText())) {
                return result;
            }
            // Loop and create movie info list
            for (int i = 0; i < rootNode.size() && i < maxCount; i++) {
                // New MovieInfo
                MovieInfo movieInfo = this.movieFactory.createMovieInfo();
                // Movie Node
                JsonNode movieNode = rootNode.path(i);
                movieInfo.setProviderName("TMDb");
                movieInfo.setScore(movieNode.path("score").asDouble());
                movieInfo.setPopularity(movieNode.path("popularity").asInt());
                movieInfo.setAdult(movieNode.path("adult").asBoolean());
                movieInfo.setLanguage(movieNode.path("language").asText());
                movieInfo.setOriginalName(movieNode.path("original_name").asText());
                movieInfo.setName(movieNode.path("name").asText());
                movieInfo.setAlternativeName(movieNode.path("alternative_name").asText());
                movieInfo.setProviderId(movieNode.path("id").asText());
                movieInfo.setIMDbId(movieNode.path("imdb_id").asText());
                movieInfo.setUrl(movieNode.path("url").asText());
                movieInfo.setVotes(movieNode.path("votes").asInt());
                movieInfo.setRating(movieNode.path("rating").asDouble());
                movieInfo.setCertification(movieNode.path("certification").asText());
                movieInfo.setOverview(movieNode.path("overview").asText());
                movieInfo.setVersion(movieNode.path("version").asInt());
                try {
                    movieInfo.setReleasedDate(new SimpleDateFormat("yyyy-MM-dd").parse(movieNode.path("released").asText()));
                } catch (Exception e) {
                    logger.warn("JsonParser catch a Exception during parsing released date: {}.", ExceptionUtils.getMessage(e));
                }
                try {
                    movieInfo.setLastModified(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(movieNode.path("last_modified_at").asText()));
                } catch (Exception e) {
                    logger.warn("JsonParser catch a Exception during parsing last modified date: {}.", ExceptionUtils.getMessage(e));
                }
                // Movie Poster Node
                JsonNode posterNode = movieNode.path("posters");
                List<String> posterIDs = new ArrayList<>();
                for (int j = 0; j < posterNode.size(); j++) {
                    // Image Node
                    JsonNode imageNode = posterNode.path(j).path("image");
                    // Calculate poster count
                    if (!posterIDs.contains(imageNode.path("id").asText())) {
                        if (posterIDs.size() >= posterCount) {
                            break;
                        } else {
                            posterIDs.add(imageNode.path("id").asText());
                        }
                    }
                    // New MovieImage
                    MovieImage movieImage = this.movieFactory.createMovieImage();
                    movieImage.setContentType(imageNode.path("type").asText());
                    movieImage.setImageType(MovieImage.JPEG_IMAGE_TYPE);
                    movieImage.setSize(imageNode.path("size").asText());
                    movieImage.setHeight(imageNode.path("height").asInt());
                    movieImage.setWidth(imageNode.path("width").asInt());
                    movieImage.setUrl(imageNode.path("url").asText());
                    movieImage.setProviderId(imageNode.path("id").asText());
                    // Add to movie info
                    movieInfo.addImage(movieImage);
                }
                // Movie Backdrop Node
                JsonNode backdropNode = movieNode.path("backdrops");
                List<String> backdropIDs = new ArrayList<>();
                for (int j = 0; j < backdropNode.size(); j++) {
                    // Image Node
                    JsonNode imageNode = backdropNode.path(j).path("image");
                    // Calculate poster count
                    if (!backdropIDs.contains(imageNode.path("id").asText())) {
                        if (backdropIDs.size() >= backdropCount) {
                            break;
                        } else {
                            backdropIDs.add(imageNode.path("id").asText());
                        }
                    }
                    // New MovieImage
                    MovieImage movieImage = this.movieFactory.createMovieImage();
                    movieImage.setContentType(imageNode.path("type").asText());
                    movieImage.setImageType(MovieImage.JPEG_IMAGE_TYPE);
                    movieImage.setSize(imageNode.path("size").asText());
                    movieImage.setHeight(imageNode.path("height").asInt());
                    movieImage.setWidth(imageNode.path("width").asInt());
                    movieImage.setUrl(imageNode.path("url").asText());
                    movieImage.setProviderId(imageNode.path("id").asText());
                    // Add to movie info
                    movieInfo.addImage(movieImage);
                }
                // Add to result
                result.add(movieInfo);
            }
        } catch (IOException e) {
            logger.error("JsonParser catch a IOException during parsing: {}, UNEXPECTED BEHAVIOR! PLEASE REPORT THIS BUG!", ExceptionUtils.getMessage(e));
            throw new ParseException(ExceptionUtils.getMessage(e));
        }
        return result;
    }
}
