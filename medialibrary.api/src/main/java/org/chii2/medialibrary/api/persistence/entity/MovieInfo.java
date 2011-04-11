package org.chii2.medialibrary.api.persistence.entity;

import java.util.Date;
import java.util.List;

/**
 * Represent Movie Information
 */
public interface MovieInfo {

    /**
     * Get ID
     *
     * @return ID
     */
    public String getId();

    /**
     * Set ID
     *
     * @param id ID
     */
    public void setId(String id);

    /**
     * Get Movie Score
     *
     * @return Score
     */
    public double getScore();

    /**
     * Set Movie Score
     *
     * @param score Score
     */
    public void setScore(double score);

    /**
     * Get Movie Popularity
     *
     * @return Popularity
     */
    public int getPopularity();

    /**
     * Set Movie Popularity
     *
     * @param popularity Popularity
     */
    public void setPopularity(int popularity);

    /**
     * Whether is Adult
     *
     * @return True if is Adult
     */
    public boolean getAdult();

    /**
     * Set whether is Adult
     *
     * @param adult True if is Adult
     */
    public void setAdult(boolean adult);

    /**
     * Get Movie Language
     *
     * @return Language
     */
    public String getLanguage();

    /**
     * Set Movie Language
     *
     * @param language Language
     */
    public void setLanguage(String language);

    /**
     * Get Movie Original Name
     *
     * @return Original Name
     */
    public String getOriginalName();

    /**
     * Set Movie Original Name
     *
     * @param originalName Original Name
     */
    public void setOriginalName(String originalName);

    /**
     * Get Movie Name
     *
     * @return name
     */
    public String getName();

    /**
     * Set Movie Name
     *
     * @param name Name
     */
    public void setName(String name);

    /**
     * Get Alternative Movie Name
     *
     * @return Alternative Movie Name
     */
    public String getAlternativeName();

    /**
     * Set Alternative Movie Name
     *
     * @param alternativeName Alternative Movie Name
     */
    public void setAlternativeName(String alternativeName);

    /**
     * Get Provider ID
     *
     * @return Provider ID
     */
    public String getProviderId();

    /**
     * Set Provider ID
     *
     * @param providerId Provider ID
     */
    public void setProviderId(String providerId);

    /**
     * Get Provider Name (TMDb)
     *
     * @return Provider Name (TMDb)
     */
    public String getProviderName();

    /**
     * Set Provider Name (TMDb)
     *
     * @param providerName Provider Name (TMDb)
     */
    public void setProviderName(String providerName);

    /**
     * Get IMDb ID
     *
     * @return IMDb ID
     */
    public String getIMDbId();

    /**
     * Set IMDb ID
     *
     * @param imdbId IMDb ID
     */
    public void setIMDbId(String imdbId);

    /**
     * Get Provider URL for this movie
     *
     * @return Provider URL
     */
    public String getUrl();

    /**
     * Set Provider URL for this movie
     *
     * @param url Provider URL
     */
    public void setUrl(String url);

    /**
     * Get Movie Votes Count
     *
     * @return Movie Votes
     */
    public int getVotes();

    /**
     * Set Movie Votes Count
     *
     * @param votes Movie Votes
     */
    public void setVotes(int votes);

    /**
     * Get Movie Rating
     *
     * @return Movie Rating
     */
    public double getRating();

    /**
     * Set Movie Rating
     *
     * @param rating Movie Rating
     */
    public void setRating(double rating);

    /**
     * Get Movie Certification
     *
     * @return Movie Certification
     */
    public String getCertification();

    /**
     * Set Movie Certification
     *
     * @param certification Movie Certification
     */
    public void setCertification(String certification);

    /**
     * Get Movie Overview
     *
     * @return Movie Overview
     */
    public String getOverview();

    /**
     * Set Movie Overview
     *
     * @param overview Movie Overview
     */
    public void setOverview(String overview);

    /**
     * Get Movie Released Date
     *
     * @return Movie Released Date
     */
    public Date getReleasedDate();

    /**
     * Set Movie Released Date
     *
     * @param releasedDate Movie Released Date
     */
    public void setReleasedDate(Date releasedDate);

    /**
     * Get Provider Information Version
     *
     * @return Version
     */
    public int getVersion();

    /**
     * Set Provider Information Version
     *
     * @param version Version
     */
    public void setVersion(int version);

    /**
     * Get Last Modified Date
     *
     * @return Last Modified Date
     */
    public Date getLastModified();

    /**
     * Set Last Modified Date
     *
     * @param lastModified Last Modified Date
     */
    public void setLastModified(Date lastModified);

    /**
     * Get Movie Images
     *
     * @return Movie Images
     */
    public List<? extends MovieImage> getImages();

    /**
     * Get Movie Posters
     *
     * @return Movie Posters
     */
    public List<? extends MovieImage> getPosters();

    /**
     * Get Movie Backdrops
     *
     * @return Movie Backdrops
     */
    public List<? extends MovieImage> getBackdrops();

    /**
     * Get Movie Images Count
     *
     * @return Count of Images
     */
    public int getImagesCount();

    /**
     * Get Movie Posters Count
     *
     * @return Count of Posters
     */
    public int getPostersCount();

    /**
     * Get Movie Backdrops Count
     *
     * @return Count of Backdrops
     */
    public int getBackdropsCount();

    /**
     * Set Movie Images
     *
     * @param images Movie Images
     */
    public void setImages(List<MovieImage> images);

    /**
     * Add a Movie Image
     *
     * @param image Movie Image
     */
    public void addImage(MovieImage image);

    /**
     * Remove a Movie Image
     *
     * @param image Movie Image
     */
    public void removeImage(MovieImage image);

    /**
     * Get Movie this information belong to
     *
     * @return Movie
     */
    public Movie getMovie();

    /**
     * Set Movie this information belong to
     *
     * @param movie Movie
     */
    public void setMovie(Movie movie);
}
