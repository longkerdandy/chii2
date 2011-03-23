package org.chii2.medialibrary.api.persistence.entity;

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
}
