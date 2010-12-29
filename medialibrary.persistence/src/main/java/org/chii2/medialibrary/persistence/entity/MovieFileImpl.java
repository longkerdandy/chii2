package org.chii2.medialibrary.persistence.entity;

import org.chii2.medialibrary.api.persistence.entity.MovieFile;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "MOVIE_FILE")
@Table(name = "MOVIE_FILE")
@NamedQueries({
        @NamedQuery(name = "MovieFile.findAll",
                query = "SELECT m FROM MOVIE_FILE m"),
        @NamedQuery(name = "MovieFile.findById",
                query = "SELECT m FROM MOVIE_FILE m WHERE m.id = :id")
})
public class MovieFileImpl implements MovieFile {

    // ID (Primary Key) in UUID format
    @Id
    @Column(name = "ID")
    private String id;

    // File Name
    @Column(name = "FILE_NAME")
    private String fileName;

    // File Path
    @Column(name = "FILE_PATH")
    private String filePath;

    // Absolute File Name
    @Column(name = "ABSOLUTE_NAME")
    private String absoluteName;

    // Movie Name
    @Column(name = "MOVIE_NAME")
    private String movieName;

    // Release Year
    @Column(name = "RELEASE_YEAR")
    private String year;

    // Source Type
    @Column(name = "SOURCE")
    private String source;

    // Video Resolution
    @Column(name = "RESOLUTION")
    private String resolution;

    // Video Codec
    @Column(name = "VIDEO_CODEC")
    private String videoCodec;

    // Audio Codec
    @Column(name = "AUDIO_CODEC")
    private String audioCodec;

    // Release Group
    @Column(name = "RELEASE_GROUP")
    private String group;

    // Disk Number
    @Column(name = "DISK_NUM")
    private int diskNum;

    // File Extension
    @Column(name = "FILE_EXTENSION")
    private String fileExtension;

    /**
     * Constructor
     */
    public MovieFileImpl() {
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
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getAbsoluteName() {
        return absoluteName;
    }

    @Override
    public void setAbsoluteName(String absoluteName) {
        this.absoluteName = absoluteName;
    }

    @Override
    public String getMovieName() {
        return movieName;
    }

    @Override
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    @Override
    public String getYear() {
        return year;
    }

    @Override
    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public String getVideoCodec() {
        return videoCodec;
    }

    @Override
    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    @Override
    public String getAudioCodec() {
        return audioCodec;
    }

    @Override
    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public int getDiskNum() {
        return diskNum;
    }

    @Override
    public void setDiskNum(int diskNum) {
        this.diskNum = diskNum;
    }

    @Override
    public String getFileExtension() {
        return fileExtension;
    }

    @Override
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
