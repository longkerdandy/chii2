package org.chii2.medialibrary.persistence.entity;

import org.chii2.medialibrary.api.persistence.entity.MovieFile;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity(name = "MOVIE_FILE")
@Table(name = "MOVIE_FILE")
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

    // File Extension
    @Column(name = "FILE_EXTENSION")
    private String fileExtension;

    // File Format (Video Container)
    @Column(name = "FORMAT")
    private String format;

    // File Size in bytes
    @Column(name = "FILE_SIZE")
    private long size;

    // Duration in ms
    @Column(name = "DURATION")
    private long duration;

    // Internet Media Type
    @Column(name = "MIME")
    private String mime;

    // Bit Rate
    @Column(name = "BITE_RATE")
    private long bitRate;

    // Movie Name
    @Column(name = "MOVIE_NAME")
    private String movieName;

    // Release Year
    @Column(name = "RELEASE_YEAR")
    private int year;

    // Source Type
    @Column(name = "SOURCE")
    private String source;

    // Release Group
    @Column(name = "RELEASE_GROUP")
    private String group;

    // Disk Number
    @Column(name = "DISK_NUM")
    private int diskNum;

    // Video Stream Count
    @Column(name = "VIDEO_STREAM_COUNT")
    private int videoStreamCount;

    // File last modification date
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFICATION_DATE")
    private Date modificationDate;

    // Video Format
    @Column(name = "VIDEO_FORMAT")
    private String videoFormat;

    // Video Codec
    @Column(name = "VIDEO_CODEC")
    private String videoCodec;

    // Video bit rate
    @Column(name = "VIDEO_BIT_RATE")
    private long videoBitRate;

    // Video Width
    @Column(name = "VIDEO_WIDTH")
    private int videoWidth;

    // Video Height
    @Column(name = "VIDEO_HEIGHT")
    private int videoHeight;

    // Audio Stream Count
    @Column(name = "AUDIO_STREAM_COUNT")
    private int audioStreamCount;

    // Audio Format
    @Column(name = "AUDIO_FORMAT")
    private String audioFormat;

    // Audio Codec
    @Column(name = "AUDIO_CODEC")
    private String audioCodec;

    // Audio bit rate
    @Column(name = "AUDIO_BIT_RATE")
    private long audioBitRate;

    // Audio Channel Count
    @Column(name = "AUDIO_CHANNEL_COUNT")
    private int audioChannelCount;

    // Audio sampling rate
    @Column(name = "AUDIO_SAMPLING_RATE")
    private long audioSamplingRate;

    // Audio Language
    @Column(name = "AUDIO_Language")
    private String audioLanguage;

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
    public String getFileExtension() {
        return fileExtension;
    }

    @Override
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String getMime() {
        return mime;
    }

    @Override
    public void setMime(String mime) {
        this.mime = mime;
    }

    @Override
    public long getBitRate() {
        return bitRate;
    }

    @Override
    public void setBitRate(long bitRate) {
        this.bitRate = bitRate;
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
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
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
    public int getVideoStreamCount() {
        return videoStreamCount;
    }

    @Override
    public void setVideoStreamCount(int videoStreamCount) {
        this.videoStreamCount = videoStreamCount;
    }

    @Override
    public Date getModificationDate() {
        return modificationDate;
    }

    @Override
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public String getVideoFormat() {
        return videoFormat;
    }

    @Override
    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
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
    public long getVideoBitRate() {
        return videoBitRate;
    }

    @Override
    public void setVideoBitRate(long videoBitRate) {
        this.videoBitRate = videoBitRate;
    }

    @Override
    public int getVideoWidth() {
        return videoWidth;
    }

    @Override
    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    @Override
    public int getVideoHeight() {
        return videoHeight;
    }

    @Override
    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    @Override
    public int getAudioStreamCount() {
        return audioStreamCount;
    }

    @Override
    public void setAudioStreamCount(int audioStreamCount) {
        this.audioStreamCount = audioStreamCount;
    }

    @Override
    public String getAudioFormat() {
        return audioFormat;
    }

    @Override
    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
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
    public long getAudioBitRate() {
        return audioBitRate;
    }

    @Override
    public void setAudioBitRate(long audioBitRate) {
        this.audioBitRate = audioBitRate;
    }

    @Override
    public int getAudioChannelCount() {
        return audioChannelCount;
    }

    @Override
    public void setAudioChannelCount(int audioChannelCount) {
        this.audioChannelCount = audioChannelCount;
    }

    @Override
    public long getAudioSamplingRate() {
        return audioSamplingRate;
    }

    @Override
    public void setAudioSamplingRate(long audioSamplingRate) {
        this.audioSamplingRate = audioSamplingRate;
    }

    @Override
    public String getAudioLanguage() {
        return audioLanguage;
    }

    @Override
    public void setAudioLanguage(String audioLanguage) {
        this.audioLanguage = audioLanguage;
    }
}
