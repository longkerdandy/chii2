package org.chii2.medialibrary.api.persistence.entity;

import java.util.Date;
import java.util.List;

/**
 * Represent a movie (which may contains multiple files, like disk1 and disk2)
 */
public interface Movie {

    /**
     * Get the id
     *
     * @return id Id
     */
    public String getId();

    /**
     * Set the id
     *
     * @param id Id
     */
    public void setId(String id);

    /**
     * Get list of movie files
     *
     * @return Movie files
     */
    public List<? extends MovieFile> getFiles();

    /**
     * Set list of movie files
     *
     * @param files Movie files
     */
    public void setFiles(List<MovieFile> files);

    /**
     * Get number of files included
     *
     * @return Files count
     */
    public int getFilesCount();

    /**
     * Add a movie file
     *
     * @param file Movie file
     */
    public void addFile(MovieFile file);

    /**
     * Remove a movie file
     *
     * @param file Movie file
     */
    public void removeFile(MovieFile file);

    /**
     * Test whether a MovieFile belongs to this Movie group
     *
     * @param file MovieFile
     * @return True if it should be included to this Movie group
     */
    public boolean shouldIncl(MovieFile file);

    /**
     * Get the file id by a file's absolute name
     *
     * @param absoluteName Absolute file name
     * @return File id, Null if not found
     */
    public String getFileIdByName(String absoluteName);

    /**
     * Get movie name from file name
     *
     * @return Movie name
     */
    public String getGuessedMovieName();

    /**
     * Get movie release year from file name
     *
     * @return Movie Year
     */
    public String getGuessedMovieYear();

    /**
     * Get movie source from file name
     *
     * @return Movie source
     */
    public String getMovieSource();

    /**
     * Get movie resolution from file name
     *
     * @return Movie resolution
     */
    public String getMovieResolution();

    /**
     * Get movie codec (video codec + audio codec) from file name
     *
     * @return Movie codec
     */
    public String getMovieCodec();

    /**
     * Get movie video codec from file name
     *
     * @return Movie codec
     */
    public String getMovieVideoCodec();

    /**
     * Get movie audio codec from file name
     *
     * @return Movie codec
     */
    public String getMovieAudioCodec();

    /**
     * Get movie format (file extension) from file name
     *
     * @return Movie format
     */
    public String getMovieFormat();

    /**
     * Get movie release group (0day group) from file name
     *
     * @return Movie release group
     */
    public String getMovieReleaseGroup();

    /**
     * Get list of movie information
     *
     * @return Movie information
     */
    public List<? extends MovieInfo> getInfo();

    /**
     * Set list of movie information
     *
     * @param info Movie information
     */
    public void setInfo(List<MovieInfo> info);

    /**
     * Get movie information count
     *
     * @return Count
     */
    public int getInfoCount();

    /**
     * Add a new movie information
     *
     * @param info Movie information
     */
    public void addInfo(MovieInfo info);

    /**
     * Remove a movie information
     *
     * @param info Movie information
     */
    public void removeInfo(MovieInfo info);

    /**
     * Get movie name from provider
     *
     * @return Movie name
     */
    public String getMovieName();

    /**
     * Get movie year from provider
     *
     * @return Movie year
     */
    public String getMovieYear();

    /**
     * Get movie original name from provider
     *
     * @return Movie original name
     */
    public String getMovieOriginalName();

    /**
     * Get movie alternative name from provider
     *
     * @return Movie alternative name
     */
    public String getMovieAlternativeName();

    /**
     * Get movie provider name
     *
     * @return Movie provider name
     */
    public String getProviderName();

    /**
     * Get movie votes from provider
     *
     * @return Movie votes
     */
    public int getMovieVotes();

    /**
     * Get movie rating from provider
     *
     * @return Movie rating
     */
    public double getMovieRating();

    /**
     * Get movie certification from provider
     *
     * @return Movie certification
     */
    public String getMovieCertification();

    /**
     * Get movie overview from provider
     *
     * @return Movie overview
     */
    public String getMovieOverview();

    /**
     * Get movie released date
     *
     * @return Movie released date
     */
    public Date getMovieReleasedDate();
}
