package org.chii2.medialibrary.persistence.entity;

import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(name = "MOVIE_INFO")
@Table(name = "MOVIE_INFO")
public class MovieInfoImpl implements MovieInfo {

    // ID (Primary Key) in UUID format
    @Id
    @Column(name = "ID")
    private String id;

    // Score
    @Column(name = "SCORE")
    private double score;

    // Popularity
    @Column(name = "POPULARITY")
    private int popularity;

    // Adult
    @Column(name = "ADULT")
    private boolean adult;

    // Language
    @Column(name = "LANGUAGE")
    private String language;

    // Original Name
    @Column(name = "ORIGINAL_NAME")
    private String originalName;

    // Name
    @Column(name = "NAME")
    private String name;

    // Alternative Name
    @Column(name = "ALTERNATIVE_NAME")
    private String alternativeName;

    // Provider ID
    @Column(name = "PROVIDER_ID")
    private String providerId;

    // Provider Name
    @Column(name = "PROVIDER_NAME")
    private String providerName;

    // IMDb ID
    @Column(name = "IMDB_ID")
    private String imdbId;

    // URL
    @Column(name = "URL")
    private String url;

    // Votes
    @Column(name = "VOTES")
    private int votes;

    // Rating
    @Column(name = "RATING")
    private double rating;

    // Certification
    @Column(name = "CERTIFICATION")
    private String certification;

    // Overview
    @Lob
    @Column(name = "OVERVIEW")
    private String overview;

    // Released Date
    @Temporal(TemporalType.DATE)
    @Column(name = "RELEASED_DATE")
    private Date releasedDate;

    // List of movie images
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "movieInfo")
    private List<MovieImageImpl> images = new ArrayList<>();

    // Version
    @Column(name = "VERSION")
    private int version;

    // Last Modified Date
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED")
    private Date lastModified;

    // Movie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private MovieImpl movie;

    /**
     * Constructor
     */
    public MovieInfoImpl() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int getPopularity() {
        return popularity;
    }

    @Override
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public boolean getAdult() {
        return adult;
    }

    @Override
    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String getOriginalName() {
        return originalName;
    }

    @Override
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAlternativeName() {
        return alternativeName;
    }

    @Override
    public void setAlternativeName(String alternativeName) {
        this.alternativeName = alternativeName;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public String getIMDbId() {
        return imdbId;
    }

    @Override
    public void setIMDbId(String imdbId) {
        this.imdbId = imdbId;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int getVotes() {
        return votes;
    }

    @Override
    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public double getRating() {
        return rating;
    }

    @Override
    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String getCertification() {
        return certification;
    }

    @Override
    public void setCertification(String certification) {
        this.certification = certification;
    }

    @Override
    public String getOverview() {
        return overview;
    }

    @Override
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public Date getReleasedDate() {
        return releasedDate;
    }

    @Override
    public void setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public List<? extends MovieImage> getImages() {
        return images;
    }

    @Override
    public List<? extends MovieImage> getPosters() {
        List<MovieImageImpl> posters = new ArrayList<MovieImageImpl>();
        for (MovieImageImpl image : images) {
            if (MovieImage.POSTER_CONTENT_TYPE.equalsIgnoreCase(image.getContentType())) {
                posters.add(image);
            }
        }
        return posters;
    }

    @Override
    public List<? extends MovieImage> getBackdrops() {
        List<MovieImageImpl> backdrops = new ArrayList<MovieImageImpl>();
        for (MovieImageImpl image : images) {
            if (MovieImage.BACKDROP_CONTENT_TYPE.equalsIgnoreCase(image.getContentType())) {
                backdrops.add(image);
            }
        }
        return backdrops;
    }

    @Override
    public int getImagesCount() {
        return images.size();
    }

    @Override
    public int getPostersCount() {
        List<MovieImageImpl> posters = new ArrayList<MovieImageImpl>();
        for (MovieImageImpl image : images) {
            if (MovieImage.POSTER_CONTENT_TYPE.equalsIgnoreCase(image.getContentType())) {
                posters.add(image);
            }
        }
        return posters.size();
    }

    @Override
    public int getBackdropsCount() {
        List<MovieImageImpl> backdrops = new ArrayList<MovieImageImpl>();
        for (MovieImageImpl image : images) {
            if (MovieImage.BACKDROP_CONTENT_TYPE.equalsIgnoreCase(image.getContentType())) {
                backdrops.add(image);
            }
        }
        return backdrops.size();
    }

    @Override
    public void setImages(List<MovieImage> images) {
        this.images.clear();
        for (MovieImage image : images) {
            if (image.getClass() == MovieImage.class) {
                this.images.add((MovieImageImpl) image);
                image.setMovieInfo(this);
            }
        }
    }

    @Override
    public void addImage(MovieImage image) {
        if (image.getClass() == MovieImageImpl.class) {
            MovieImageImpl movieImage = (MovieImageImpl) image;
            if (!images.contains(movieImage)) {
                images.add(movieImage);
                movieImage.setMovieInfo(this);
            }
        }
    }

    @Override
    public void removeImage(MovieImage image) {
        if (image.getClass() == MovieImageImpl.class) {
            MovieImageImpl movieImage = (MovieImageImpl) image;
            if (images.contains(movieImage)) {
                images.remove(movieImage);
                movieImage.setMovieInfo(null);
            }
        }
    }

    @Override
    public Movie getMovie() {
        return movie;
    }

    @Override
    public void setMovie(Movie movie) {
        if (movie.getClass() == MovieImpl.class) {
            this.movie = (MovieImpl) movie;
        }
    }
}
