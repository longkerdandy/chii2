package org.chii2.medialibrary.persistence.entity;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;

import javax.persistence.*;
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
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "movie")
    @OrderBy("diskNum ASC")
    private List<MovieFileImpl> files = new ArrayList<>();

    // List of movie information
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "movie")
    @OrderBy("providerName ASC")
    private List<MovieInfoImpl> information = new ArrayList<>();

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
                movieFile.setMovie(this);
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
                file.setMovie(this);
            }
        }
    }

    @Override
    public void removeFile(MovieFile movieFile) {
        if (movieFile.getClass() == MovieFileImpl.class) {
            MovieFileImpl file = (MovieFileImpl) movieFile;
            if (files.contains(file)) {
                files.remove(file);
                file.setMovie(null);
            }
        }
    }

    @Override
    public MovieFile getFile(int diskNum) {
        for (MovieFile movieFile : files) {
            if (movieFile.getDiskNum() == diskNum) {
                return movieFile;
            }
        }
        return null;
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
                movieInfo.setMovie(this);
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
                movieInfo.setMovie(this);
            }
        }
    }

    @Override
    public void removeInfo(MovieInfo info) {
        if (info.getClass() == MovieInfoImpl.class) {
            MovieInfoImpl movieInfo = (MovieInfoImpl) info;
            if (information.contains(movieInfo)) {
                information.remove(movieInfo);
                movieInfo.setMovie(null);
            }
        }
    }

    @Override
    public String getTitle() {
        if (getInfoCount() > 0) {
            return getInfo().get(0).getName();
        } else if (getFilesCount() > 0) {
            MovieFile file = getFiles().get(0);
            if (StringUtils.isNotBlank(file.getMovieName())) {
                return file.getMovieName();
            } else {
                return file.getFileName();
            }
        } else {
            // Not possible
            return null;
        }
    }

    @Override
    public Date getReleasedDate() {
        if (getInfoCount() > 0) {
            return getInfo().get(0).getReleasedDate();
        } else {
            return null;
        }
    }

    @Override
    public String getOverview() {
        if (getInfoCount() > 0) {
            return getInfo().get(0).getOverview();
        } else {
            return null;
        }
    }

    @Override
    public double getRating() {
        if (getInfoCount() > 0) {
            return getInfo().get(0).getRating();
        } else {
            return 0;
        }
    }

    @Override
    public String getCertification() {
        if (getInfoCount() > 0) {
            return getInfo().get(0).getCertification();
        } else {
            return null;
        }
    }

    @Override
    public String getLanguage() {
        if (getInfoCount() > 0) {
            return getInfo().get(0).getLanguage();
        } else if (getFilesCount() > 0) {
            MovieFile file = getFiles().get(0);
            if (StringUtils.isNotBlank(file.getAudioLanguage())) {
                return file.getAudioLanguage();
            } else {
                return null;
            }
        } else {
            // Not possible
            return null;
        }
    }

    @Override
    public String getFormat() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getFormat();
        } else {
            return null;
        }
    }

    @Override
    public long getDuration() {
        long duration = 0;
        for (MovieFile movieFile : getFiles()) {
            duration += movieFile.getDuration();
        }
        return duration;
    }

    @Override
    public long getSize() {
        long size = 0;
        for (MovieFile movieFile : getFiles()) {
            size += movieFile.getSize();
        }
        return size;
    }

    @Override
    public long getBitRate() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getVideoBitRate() + getFiles().get(0).getAudioBitRate();
        } else {
            return 0;
        }
    }

    @Override
    public String getMIME() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getMime();
        } else {
            return null;
        }
    }

    @Override
    public int getVideoWidth() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getVideoWidth();
        } else {
            return 0;
        }
    }

    @Override
    public int getVideoHeight() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getVideoHeight();
        } else {
            return 0;
        }
    }

    @Override
    public String getVideoFormat() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getVideoFormat();
        } else {
            return null;
        }
    }

    @Override
    public String getVideoFormatProfile() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getVideoFormatProfile();
        } else {
            return null;
        }
    }

    @Override
    public int getVideoFormatVersion() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getVideoFormatVersion();
        } else {
            return 0;
        }
    }

    @Override
    public String getVideoCodec() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getVideoCodec();
        } else {
            return null;
        }
    }

    @Override
    public long getVideoBitRate() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getVideoBitRate();
        } else {
            return 0;
        }
    }

    @Override
    public float getVideoFps() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getVideoFrameRate();
        } else {
            return 0;
        }
    }

    @Override
    public String getAudioFormat() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getAudioFormat();
        } else {
            return null;
        }
    }

    @Override
    public String getAudioFormatProfile() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getAudioFormatProfile();
        } else {
            return null;
        }
    }

    @Override
    public int getAudioFormatVersion() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getAudioFormatVersion();
        } else {
            return 0;
        }
    }

    @Override
    public String getAudioCodec() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getAudioCodec();
        } else {
            return null;
        }
    }

    @Override
    public int getAudioChannels() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getAudioChannelCount();
        } else {
            return 0;
        }
    }

    @Override
    public long getAudioBitRate() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getAudioBitRate();
        } else {
            return 0;
        }
    }

    @Override
    public long getAudioSampleBitRate() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getAudioSamplingRate();
        } else {
            return 0;
        }
    }

    @Override
    public int getAudioBitDepth() {
        if (getFilesCount() > 0) {
            return getFiles().get(0).getAudioBitDepth();
        } else {
            return 0;
        }
    }
}
