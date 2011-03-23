package org.chii2.medialibrary.persistence.entity;

import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(name = "MOVIE")
@Table(name = "MOVIE")
public class MovieImpl implements Movie {

    // ID (Primary Key) in UUID format
    @Id
    @Column(name = "ID")
    private String id;

    // List of movie files
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @OrderBy("diskNum ASC")
    private List<MovieFileImpl> files = new ArrayList<MovieFileImpl>();

    // List of movie information
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<MovieInfoImpl> information = new ArrayList<MovieInfoImpl>();

    /**
     * Constructor
     */
    public MovieImpl() {
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
    public List<? extends MovieFile> getFiles() {
        return files;
    }

    @Override
    public void setFiles(List<MovieFile> movieFiles) {
        files.clear();
        for (MovieFile movieFile : movieFiles) {
            if (movieFile.getClass() == MovieFileImpl.class) {
                files.add((MovieFileImpl) movieFile);
            }
        }
    }

    @Override
    public int getFilesCount() {
        return files.size();
    }

    @Override
    public void addFile(MovieFile movieFile) {
        if (movieFile.getClass() == MovieFileImpl.class) {
            MovieFileImpl file = (MovieFileImpl) movieFile;
            if (!files.contains(file)) {
                files.add(file);
            }
        }
    }

    @Override
    public void removeFile(MovieFile movieFile) {
        if (movieFile.getClass() == MovieFileImpl.class) {
            MovieFileImpl file = (MovieFileImpl) movieFile;
            if (files.contains(file)) {
                files.remove(file);
            }
        }
    }

    @Override
    public List<? extends MovieInfo> getInfo() {
        return information;
    }

    @Override
    public void setInfo(List<MovieInfo> info) {
        information.clear();
        for (MovieInfo movieInfo : info) {
            if (movieInfo.getClass() == MovieInfoImpl.class) {
                information.add((MovieInfoImpl) movieInfo);
            }
        }
    }

    @Override
    public int getInfoCount() {
        return information.size();
    }

    @Override
    public void addInfo(MovieInfo info) {
        if (info.getClass() == MovieInfoImpl.class) {
            MovieInfoImpl movieInfo = (MovieInfoImpl) info;
            if (!information.contains(movieInfo)) {
                information.add(movieInfo);
            }
        }
    }

    @Override
    public void removeInfo(MovieInfo info) {
        if (info.getClass() == MovieInfoImpl.class) {
            MovieInfoImpl movieInfo = (MovieInfoImpl) info;
            if (information.contains(movieInfo)) {
                information.remove(movieInfo);
            }
        }
    }
}
