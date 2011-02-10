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
@NamedQueries({
        @NamedQuery(name = "Movie.findAll",
                query = "SELECT m FROM MOVIE m"),
        @NamedQuery(name = "Movie.findById",
                query = "SELECT m FROM MOVIE m WHERE m.id = :id"),
        @NamedQuery(name = "Movie.findByName",
                query = "SELECT m FROM MOVIE m join m.information i WHERE LOWER(i.name) LIKE :name")
})
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
    public boolean shouldIncl(MovieFile movieFile) {
        if (movieFile.getClass() == MovieFileImpl.class) {
            MovieFileImpl file = (MovieFileImpl) movieFile;
            // If already contained, return false
            if (files.contains(file)) {
                return false;
            }

            // The file's path and movie name must be the same as the exist files
            for (MovieFileImpl currentFile : files) {
                if (!file.getFilePath().equals(currentFile.getFilePath()) || !file.getMovieName().equalsIgnoreCase(currentFile.getMovieName())) {
                    return false;
                }
            }

            // If all above passed, it should be included
            return true;
        }
        // This should not happen
        else {
            return false;
        }
    }

    @Override
    public String getFileIdByName(String absoluteName) {
        for (MovieFileImpl movieFile : files) {
            if (movieFile.getAbsoluteName().equals(absoluteName)) {
                return movieFile.getId();
            }
        }

        // Return null if not found
        return null;
    }

    @Override
    public String getGuessedMovieName() {
        if (files.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieFile file : files) {
                if (file.getMovieName() != null) {
                    return file.getMovieName();
                }
            }
            // If all files don't contain movie name information, return null
            return null;
        }
    }

    @Override
    public String getGuessedMovieYear() {
        if (files.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieFile file : files) {
                if (file.getYear() != null) {
                    return file.getYear();
                }
            }
            // If all files don't contain year information, return null
            return null;
        }
    }

    @Override
    public String getMovieSource() {
        // If no files, return null
        if (files.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieFile file : files) {
                if (file.getSource() != null) {
                    return file.getSource().toUpperCase();
                }
            }
            // If all files don't contain source information, return null
            return null;
        }
    }

    @Override
    public String getMovieResolution() {
        // If no files, return null
        if (files.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieFile file : files) {
                if (file.getResolution() != null) {
                    return file.getResolution().toUpperCase();
                }
            }
            // If all files don't contain resolution information, return null
            return null;
        }
    }

    @Override
    public String getMovieCodec() {
        StringBuilder codec = new StringBuilder();
        String videoCodec = getMovieVideoCodec();
        String audioCodec = getMovieAudioCodec();
        if (videoCodec != null) {
            codec.append(videoCodec);
        }
        if (!codec.toString().isEmpty() && audioCodec != null) {
            codec.append(" ");
        }
        if (audioCodec != null) {
            codec.append(audioCodec);
        }

        return codec.toString();
    }

    @Override
    public String getMovieVideoCodec() {
        // If no files, return null
        if (files.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieFile file : files) {
                if (file.getVideoCodec() != null) {
                    return file.getVideoCodec().toUpperCase();
                }
            }
            // If all files don't contain video codec information, return null
            return null;
        }
    }

    @Override
    public String getMovieAudioCodec() {
        // If no files, return null
        if (files.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieFile file : files) {
                if (file.getAudioCodec() != null) {
                    return file.getAudioCodec().toUpperCase();
                }
            }
            // If all files don't contain audio codec information, return null
            return null;
        }
    }

    @Override
    public String getMovieFormat() {
        // If no files, return null
        if (files.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieFile file : files) {
                if (file.getFileExtension() != null) {
                    return file.getFileExtension().toUpperCase();
                }
            }
            // If all files don't contain file extension information, return null
            return null;
        }
    }

    @Override
    public String getMovieReleaseGroup() {
        // If no files, return null
        if (files.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieFile file : files) {
                if (file.getGroup() != null) {
                    return file.getGroup();
                }
            }
            // If all files don't contain file release group information, return null
            return null;
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

    @Override
    public String getMovieName() {
        if (information.isEmpty()) {
            // Try return guessed movie name then
            return getGuessedMovieName();
        } else {
            // Loop and return the first value which not null
            for (MovieInfo info : information) {
                if (info.getName() != null) {
                    return info.getName();
                }
            }
            // If all information don't contain movie name information, try return guessed movie name then
            return getGuessedMovieName();
        }
    }

    @Override
    public String getMovieYear() {
        if (information.isEmpty()) {
            // Try return guessed movie year then
            return getGuessedMovieYear();
        } else {
            // Get year from the released date
            Date releasedDate = getMovieReleasedDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            if (releasedDate != null) {
                return format.format(releasedDate);
            }
            // If all information don't contain movie year information, try return guessed movie year then
            return getGuessedMovieYear();
        }
    }

    @Override
    public String getMovieOriginalName() {
        if (information.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieInfo info : information) {
                if (info.getOriginalName() != null) {
                    return info.getOriginalName();
                }
            }
            // If all information don't contain movie original name information, return null
            return null;
        }
    }

    @Override
    public String getMovieAlternativeName() {
        if (information.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieInfo info : information) {
                if (info.getAlternativeName() != null) {
                    return info.getAlternativeName();
                }
            }
            // If all information don't contain movie alternative name information, return null
            return null;
        }
    }

    @Override
    public String getProviderName() {
        if (information.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieInfo info : information) {
                if (info.getProviderName() != null) {
                    return info.getProviderName();
                }
            }
            // If all information don't contain provider name information, return null
            return null;
        }
    }

    @Override
    public int getMovieVotes() {
        if (information.isEmpty()) {
            return 0;
        } else {
            // Loop and return the first value which not 0
            for (MovieInfo info : information) {
                if (info.getVotes() != 0) {
                    return info.getVotes();
                }
            }
            // If all information don't contain movie votes information, return 0
            return 0;
        }
    }

    @Override
    public double getMovieRating() {
        if (information.isEmpty()) {
            return 0;
        } else {
            // Loop and return the first value which not 0
            for (MovieInfo info : information) {
                if (info.getRating() != 0) {
                    return info.getRating();
                }
            }
            // If all information don't contain movie rating information, return 0
            return 0;
        }
    }

    @Override
    public String getMovieCertification() {
        if (information.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieInfo info : information) {
                if (info.getCertification() != null) {
                    return info.getCertification();
                }
            }
            // If all information don't contain movie certification information, return null
            return null;
        }
    }

    @Override
    public String getMovieOverview() {
        if (information.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieInfo info : information) {
                if (info.getOverview() != null) {
                    return info.getOverview();
                }
            }
            // If all information don't contain movie overview information, return null
            return null;
        }
    }

    @Override
    public Date getMovieReleasedDate() {
        if (information.isEmpty()) {
            return null;
        } else {
            // Loop and return the first value which not null
            for (MovieInfo info : information) {
                if (info.getReleasedDate() != null) {
                    return info.getReleasedDate();
                }
            }
            // If all information don't contain movie released date information, return null
            return null;
        }
    }
}
