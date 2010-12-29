package org.chii2.medialibrary.provider.tmdb.parser;

import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.yaml.snakeyaml.Yaml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * YAML Parser
 */
public class YamlParser implements Parser {

    // Movie Factory
    MovieFactory factory;

    /**
     * Constructor
     *
     * @param factory Movie Factory
     */
    public YamlParser(MovieFactory factory) {
        this.factory = factory;
    }

    /**
     * Parse the YAML content into a single MovieInfo record
     *
     * @param content YAML content
     * @return MovieInfo
     */
    @Override
    public MovieInfo getSingleMovieInfo(String content) {
        List<MovieInfo> infoList = parseMovieInfo(content);
        // Get the first one
        if (infoList != null && !infoList.isEmpty()) {
            return infoList.get(0);
        } else {
            return null;
        }
    }

    /**
     * Parse the YAML content into a MovieInfo records list
     *
     * @param content YAML content
     * @return MovieInfo List
     */
    @Override
    public List<MovieInfo> getAllMovieInfo(String content) {
        return parseMovieInfo(content);
    }

    /**
     * Parse yaml content into list of movie information
     *
     * @param content Yaml content
     * @return List of Movie Information
     */
    private List<MovieInfo> parseMovieInfo(String content) {
        // Result list
        List<MovieInfo> infoList = new ArrayList<MovieInfo>();
        // Load yaml content and parse into lost of maps
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) (new Yaml()).load(content);
        // If have records
        if (list != null && !list.isEmpty()) {
            // Loop
            for (Object obj : list) {
                // If not map, continue next loop
                if (obj.getClass() != LinkedHashMap.class) {
                    continue;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) obj;
                // New MovieInfo from factory
                MovieInfo info = factory.createMovieInfo();
                // Mapping into filed
                info.setScore(toDouble(map.get("score")));
                info.setPopularity(toInteger(map.get("popularity")));
                info.setAdult(toBoolean(map.get("adult")));
                info.setLanguage(toString(map.get("language")));
                info.setOriginalName(toString(map.get("original_name")));
                info.setName(toString(map.get("name")));
                info.setAlternativeName(toString(map.get("alternative_name")));
                info.setProviderId(toString(map.get("id")));
                info.setProviderName("TMDb");
                info.setIMDbId(toString(map.get("imdb_id")));
                info.setUrl(toString(map.get("url")));
                info.setVotes(toInteger(map.get("votes")));
                info.setRating(toDouble(map.get("rating")));
                info.setCertification(toString(map.get("certification")));
                info.setOverview(toString(map.get("overview")));
                info.setReleasedDate(toDate("yyyy-MM-dd", map.get("released")));
                info.setVersion(toInteger(map.get("version")));
                info.setLastModified(toDate(map.get("last_modified_at")));
                // Posters
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> posterList = (List<Map<String, Object>>) map.get("posters");
                if (posterList != null && !posterList.isEmpty()) {
                    for (Map<String, Object> posterMap : posterList) {
                        // Image map
                        @SuppressWarnings("unchecked")
                        Map<String, Object> imageMap = (Map<String, Object>) posterMap.get("image");
                        // Create movie image from factory
                        MovieImage image = factory.createMovieImage();
                        // Parse movie image
                        image.setType("poster");
                        image.setSize(toString(imageMap.get("size")));
                        image.setHeight(toInteger(imageMap.get("height")));
                        image.setWidth(toInteger(imageMap.get("width")));
                        image.setUrl(toString(imageMap.get("url")));
                        image.setProviderId(toString(imageMap.get("id")));
                        // Add to movie information
                        info.addImage(image);
                    }
                }
                // Backdrops
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> backdropList = (List<Map<String, Object>>) map.get("backdrops");
                if (backdropList != null && !backdropList.isEmpty()) {
                    for (Map<String, Object> backdropMap : backdropList) {
                        // Image map
                        @SuppressWarnings("unchecked")
                        Map<String, Object> imageMap = (Map<String, Object>) backdropMap.get("image");
                        // Create movie image from factory
                        MovieImage image = factory.createMovieImage();
                        // Parse movie image
                        image.setType("poster");
                        image.setSize(toString(imageMap.get("size")));
                        image.setHeight(toInteger(imageMap.get("height")));
                        image.setWidth(toInteger(imageMap.get("width")));
                        image.setUrl(toString(imageMap.get("url")));
                        image.setProviderId(toString(imageMap.get("id")));
                        // Add to movie information
                        info.addImage(image);
                    }
                }
                // Add to the result list
                infoList.add(info);
            }
        }
        // Return
        return infoList;
    }

    /**
     * When parsing yaml, convert yaml object to double
     *
     * @param obj Yaml obejct
     * @return Double
     */
    private double toDouble(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj.getClass() == Double.class) {
            return (Double) obj;
        } else {
            return 0;
        }
    }

    /**
     * When parsing yaml, convert yaml object to int
     *
     * @param obj Yaml obejct
     * @return Int
     */
    private int toInteger(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj.getClass() == Integer.class) {
            return (Integer) obj;
        } else {
            return 0;
        }
    }

    /**
     * When parsing yaml, convert yaml object to boolean
     *
     * @param obj Yaml obejct
     * @return Boolean
     */
    private boolean toBoolean(Object obj) {
        return obj != null && obj.getClass() == Boolean.class && (Boolean) obj;
    }

    /**
     * When parsing yaml, convert yaml object to string
     *
     * @param obj Yaml obejct
     * @return String
     */
    private String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    /**
     * When parsing yaml, convert yaml object to date
     *
     * @param obj Yaml obejct
     * @return Date
     */
    private Date toDate(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == Date.class) {
            return (Date) obj;
        } else {
            return null;
        }
    }

    /**
     * When parsing yaml, convert yaml object to date
     *
     * @param format Date format to be parsed
     * @param obj    Yaml obejct
     * @return Date
     */
    private Date toDate(String format, Object obj) {
        String date = toString(obj);
        if (date == null) {
            return null;
        } else {
            try {
                return new SimpleDateFormat(format).parse(date);
            } catch (ParseException e) {
                return null;
            }
        }
    }
}
