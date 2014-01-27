package org.chii2.medialibrary.persistence;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.chii2.medialibrary.api.persistence.PersistenceService;
import org.chii2.medialibrary.api.persistence.entity.*;
import org.chii2.medialibrary.persistence.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Persistence layer
 */
public class PersistenceServiceImpl implements PersistenceService {
    // Entity Manager
    private EntityManager entityManager;
    // CriteriaBuilder
    private CriteriaBuilder builder;
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.persistence");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library PersistenceService init.");
        // init builder
        this.builder = this.entityManager.getCriteriaBuilder();
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library PersistenceService destroy.");
    }

    @Override
    public List<MovieImpl> getMovies(int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<MovieImpl> fromQuery = this.builder.createQuery(MovieImpl.class);
        Root<MovieImpl> movies = fromQuery.from(MovieImpl.class);
        // Select Query
        CriteriaQuery<MovieImpl> selectQuery = fromQuery.select(movies);
        // Order
        if (sorts != null) {
            List<Order> orders = new ArrayList<>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (field.startsWith("file.")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(builder.asc(movies.join("files", JoinType.LEFT).get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(builder.desc(movies.join("files", JoinType.LEFT).get(field.substring(5))));
                        }
                    } else if (field.startsWith("info.")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(this.builder.asc(movies.join("information", JoinType.LEFT).get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(this.builder.desc(movies.join("information", JoinType.LEFT).get(field.substring(5))));
                        }
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<MovieImpl> typedQuery = this.entityManager.createQuery(selectQuery);
        // First Result
        if (firstResult >= 0) {
            typedQuery.setFirstResult(firstResult);
        }
        // Max Results
        if (maxResults >= 0) {
            typedQuery.setMaxResults(maxResults);
        }
        return typedQuery.getResultList();
    }

    @Override
    public Movie getMovieById(String id) {
        List<MovieImpl> movies = this.entityManager.createQuery("SELECT m FROM MOVIE m WHERE m.id = ?1", MovieImpl.class).setParameter(1, id).getResultList();
        if (movies != null && movies.size() == 1) {
            return movies.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Movie getMovieByMovieFile(MovieFile movieFile) {
        return movieFile.getMovie();
    }


    @Override
    public List<? extends Movie> getMoviesByName(String movieName, int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<MovieImpl> fromQuery = this.builder.createQuery(MovieImpl.class);
        Root<MovieImpl> movies = fromQuery.from(MovieImpl.class);
        Path<String> infoMovieNameFiled = movies.join("information").get("name");
        Path<String> fileMovieNameFiled = movies.join("files").get("movieName");
        // Select Query
        CriteriaQuery<MovieImpl> selectQuery = fromQuery.select(movies);
        // Where Query
        selectQuery.where(this.builder.or(this.builder.like(this.builder.lower(infoMovieNameFiled), "%" + movieName.toLowerCase() + "%"), this.builder.like(this.builder.lower(fileMovieNameFiled), "%" + movieName.toLowerCase() + "%")));
        // Order
        if (sorts != null) {
            List<Order> orders = new ArrayList<>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (field.startsWith("file.")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(this.builder.asc(movies.join("files").get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(this.builder.desc(movies.join("files").get(field.substring(5))));
                        }
                    } else if (field.startsWith("info.")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(this.builder.asc(movies.join("information").get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(this.builder.desc(movies.join("information").get(field.substring(5))));
                        }
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<MovieImpl> typedQuery = this.entityManager.createQuery(selectQuery);
        // First Result
        if (firstResult >= 0) {
            typedQuery = typedQuery.setFirstResult(firstResult);
        }
        // Max Results
        if (maxResults >= 0) {
            typedQuery = typedQuery.setMaxResults(maxResults);
        }
        return typedQuery.getResultList();
    }

    @Override
    public List<? extends MovieFile> getMovieFiles(int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<MovieFileImpl> fromQuery = this.builder.createQuery(MovieFileImpl.class);
        Root<MovieFileImpl> movieFies = fromQuery.from(MovieFileImpl.class);
        // Select Query
        CriteriaQuery<MovieFileImpl> selectQuery = fromQuery.select(movieFies);
        // Order
        if (sorts != null) {
            List<Order> orders = new ArrayList<>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (sortType.equalsIgnoreCase("asc")) {
                        orders.add(this.builder.asc(movieFies.get(field)));
                    } else if (sortType.equalsIgnoreCase("desc")) {
                        orders.add(this.builder.desc(movieFies.get(field)));
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<MovieFileImpl> typedQuery = this.entityManager.createQuery(selectQuery);
        // First Result
        if (firstResult >= 0) {
            typedQuery.setFirstResult(firstResult);
        }
        // Max Results
        if (maxResults >= 0) {
            typedQuery.setMaxResults(maxResults);
        }
        return typedQuery.getResultList();
    }

    @Override
    public MovieFile getMovieFileById(String id) {
        List<MovieFileImpl> movieFiles = this.entityManager.createQuery("SELECT m FROM MOVIE_FILE m WHERE m.id = ?1", MovieFileImpl.class).setParameter(1, id).getResultList();
        if (movieFiles != null && movieFiles.size() == 1) {
            return movieFiles.get(0);
        } else {
            return null;
        }
    }

    @Override
    public MovieInfo getMovieInfoById(String id) {
        List<MovieInfoImpl> movieInfoList = this.entityManager.createQuery("SELECT m FROM MOVIE_INFO m WHERE m.id = ?1", MovieInfoImpl.class).setParameter(1, id).getResultList();
        if (movieInfoList != null && movieInfoList.size() == 1) {
            return movieInfoList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public MovieImage getMovieImageById(String id) {
        List<MovieImageImpl> movieImages = this.entityManager.createQuery("SELECT m FROM MOVIE_IMAGE m WHERE m.id = ?1", MovieImageImpl.class).setParameter(1, id).getResultList();
        if (movieImages != null && movieImages.size() == 1) {
            return movieImages.get(0);
        } else {
            return null;
        }
    }

    @Override
    public byte[] getMovieThumbnailById(String movieId) {
        List<? extends MovieInfo> movieInfoList = this.entityManager.createQuery("SELECT i FROM MOVIE_INFO i JOIN i.movie m WHERE m.id = ?1", MovieInfoImpl.class).setParameter(1, movieId).getResultList();
        if (movieInfoList != null && !movieInfoList.isEmpty()) {
            for (MovieImage movieImage : movieInfoList.get(0).getPosters()) {
                if ("thumb".equalsIgnoreCase(movieImage.getSize())) {
                    return movieImage.getImage();
                }
            }
        }
        return null;
    }

    @Override
    public long getMoviesCount() {
        return this.entityManager.createQuery("SELECT COUNT(m) FROM MOVIE m", Long.class).getSingleResult();
    }

    @Override
    public long getMovieFilesCount() {
        return this.entityManager.createQuery("SELECT COUNT(m) FROM MOVIE_FILE m", Long.class).getSingleResult();
    }

    @Override
    public void deleteMovie(String path) {
        List<MovieFileImpl> movieFiles;
        // No file extension, should be a directory
        if (StringUtils.isEmpty(FilenameUtils.getExtension(path))) {
            if (!path.endsWith(SystemUtils.FILE_SEPARATOR)) {
                path = path + SystemUtils.FILE_SEPARATOR;
            }
            path = FilenameUtils.separatorsToUnix(path);
            movieFiles = this.entityManager.createQuery("SELECT m FROM MOVIE_FILE m WHERE m.filePath LIKE ?1", MovieFileImpl.class).setParameter(1, path + "%").getResultList();
        }
        // Path is file
        else {
            path = FilenameUtils.separatorsToUnix(path);
            movieFiles = this.entityManager.createQuery("SELECT m FROM MOVIE_FILE m WHERE m.absolutePath = ?1", MovieFileImpl.class).setParameter(1, path).getResultList();
        }

        // Delete old movies
        if (movieFiles != null && !movieFiles.isEmpty()) {
            for (MovieFile movieFile : movieFiles) {
                Movie movie = movieFile.getMovie();
                if (movie != null && movie.getFilesCount() == 1) {
                    this.entityManager.remove(movie);
                } else {
                    this.entityManager.remove(movieFile);
                }
            }
        }
    }

    @Override
    public int deleteAllMovies() {
        return this.entityManager.createQuery("DELETE FROM MOVIE m").executeUpdate();
    }

    @Override
    public List<? extends Image> getImages(int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<ImageImpl> fromQuery = this.builder.createQuery(ImageImpl.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        // Select Query
        CriteriaQuery<ImageImpl> selectQuery = fromQuery.select(images);
        // Order
        if (sorts != null) {
            List<Order> orders = new ArrayList<>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (field.startsWith("file.")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(this.builder.asc(images.join("originalFile").get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(this.builder.desc(images.join("originalFile").get(field.substring(5))));
                        }
                    } else {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(this.builder.asc(images.get(field)));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(this.builder.asc(images.get(field)));
                        }
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<ImageImpl> typedQuery = this.entityManager.createQuery(selectQuery);
        // First Result
        if (firstResult >= 0) {
            typedQuery = typedQuery.setFirstResult(firstResult);
        }
        // Max Results
        if (maxResults >= 0) {
            typedQuery = typedQuery.setMaxResults(maxResults);
        }
        return typedQuery.getResultList();
    }

    @Override
    public Image getImageById(String id) {
        List<ImageImpl> images = this.entityManager.createQuery("SELECT i FROM IMAGE i WHERE i.id = ?1", ImageImpl.class).setParameter(1, id).getResultList();
        if (images != null && images.size() == 1) {
            return images.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<? extends Image> getImagesByField(String fieldName, String fieldValue, boolean strict, int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<ImageImpl> fromQuery = this.builder.createQuery(ImageImpl.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        Path<String> name;
        if (fieldName.startsWith("file.")) {
            name = images.join("originalFile").get(fieldName.substring(5));
        } else {
            name = images.get(fieldName);
        }

        // Select Query
        CriteriaQuery<ImageImpl> selectQuery = fromQuery.select(images);
        // Where Query
        if (strict) {
            selectQuery.where(this.builder.like(this.builder.lower(name), fieldValue.toLowerCase()));
        } else {
            selectQuery.where(this.builder.like(this.builder.lower(name), "%" + fieldValue.toLowerCase() + "%"));
        }
        // Order query
        if (sorts != null) {
            List<Order> orders = new ArrayList<>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (field.startsWith("file.")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(this.builder.asc(images.join("originalFile").get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(this.builder.desc(images.join("originalFile").get(field.substring(5))));
                        }
                    } else {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(this.builder.asc(images.get(field)));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(this.builder.asc(images.get(field)));
                        }
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<ImageImpl> typedQuery = this.entityManager.createQuery(selectQuery);
        // First Result
        if (firstResult >= 0) {
            typedQuery = typedQuery.setFirstResult(firstResult);
        }
        // Max Results
        if (maxResults >= 0) {
            typedQuery = typedQuery.setMaxResults(maxResults);
        }
        return typedQuery.getResultList();
    }

    @Override
    public List<String> getImageAlbums(int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<String> fromQuery = this.builder.createQuery(String.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        // Select Query
        CriteriaQuery<String> selectQuery = fromQuery.multiselect(images.get("album"));
        selectQuery.distinct(true);
        if (sorts != null) {
            List<Order> orders = new ArrayList<>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (field.equals("album")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(this.builder.asc(images.get(field)));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(this.builder.asc(images.get(field)));
                        }
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<String> typedQuery = this.entityManager.createQuery(selectQuery);
        // First Result
        if (firstResult >= 0) {
            typedQuery = typedQuery.setFirstResult(firstResult);
        }
        // Max Results
        if (maxResults >= 0) {
            typedQuery = typedQuery.setMaxResults(maxResults);
        }
        return typedQuery.getResultList();
    }

    @Override
    public long getImagesCount() {
        return this.entityManager.createQuery("SELECT COUNT(i) FROM IMAGE i", Long.class).getSingleResult();
    }

    @Override
    public long getImageAlbumsCount() {
        return this.entityManager.createQuery("SELECT COUNT(DISTINCT i.album) FROM IMAGE i", Long.class).getSingleResult();
    }

    @Override
    public long getImagesCountByAlbum(String album) {
        return this.entityManager.createQuery("SELECT COUNT(i) FROM IMAGE i WHERE i.album = ?1", Long.class).setParameter(1, album).getSingleResult();
    }

    @Override
    public ImageFile getImageFileById(String id) {
        return this.entityManager.find(ImageFileImpl.class, id);
    }

    @Override
    public void deleteImage(String path) {
        List<ImageFileImpl> imageFiles;
        // No file extension, should be a directory
        if (StringUtils.isEmpty(FilenameUtils.getExtension(path))) {
            if (!path.endsWith(SystemUtils.FILE_SEPARATOR)) {
                path = path + SystemUtils.FILE_SEPARATOR;
            }
            path = FilenameUtils.separatorsToUnix(path);
            imageFiles = this.entityManager.createQuery("SELECT i FROM IMAGE_FILE i WHERE i.filePath LIKE ?1", ImageFileImpl.class).setParameter(1, path + "%").getResultList();
        }
        // Path is file
        else {
            path = FilenameUtils.separatorsToUnix(path);
            imageFiles = this.entityManager.createQuery("SELECT i FROM IMAGE_FILE i WHERE i.absolutePath = ?1", ImageFileImpl.class).setParameter(1, path).getResultList();
        }

        // Delete old images
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (ImageFile imageFile : imageFiles) {
                Image image = imageFile.getImage();
                if (image != null) {
                    this.entityManager.remove(image);
                } else {
                    this.entityManager.remove(imageFile);
                }
            }
        }
    }

    @Override
    public int deleteAllImages() {
        return this.entityManager.createQuery("DELETE FROM IMAGE i").executeUpdate();
    }

    @Override
    public void synchronizeMovie(MovieFile movieFile) {
        // If the file already exist in DB, keep current Movie, replace MovieFile
        List<MovieFileImpl> movieFiles = this.entityManager.createQuery("SELECT m FROM MOVIE_FILE m WHERE m.absolutePath = ?1", MovieFileImpl.class).setParameter(1, movieFile.getAbsolutePath()).getResultList();
        if (movieFiles != null && movieFiles.size() == 1) {
            movieFiles.get(0).getMovie().addFile(movieFile);
        }

        // Delete old MovieFiles
        this.entityManager.createQuery("DELETE FROM MOVIE_FILE m WHERE m.absolutePath = ?1").setParameter(1, movieFile.getAbsolutePath()).executeUpdate();

        // Merge/Persist Movie/MovieFile into DB
        if (movieFile.getMovie() != null) {
            this.entityManager.merge(movieFile.getMovie());
        } else {
            // CD1 CD2 Part1 Part2 MovieFiles save into a same Movie
            List<MovieImpl> dbMovies = this.entityManager.createQuery("SELECT m FROM MOVIE m JOIN m.files f WHERE f.filePath = ?1 AND f.movieName = ?2", MovieImpl.class).setParameter(1, movieFile.getFilePath()).setParameter(2, movieFile.getMovieName()).getResultList();
            if (dbMovies != null && dbMovies.size() > 0) {
                Movie movie = dbMovies.get(0);
                movie.addFile(movieFile);
                this.entityManager.merge(movie);
            } else {
                // TODO: Should use factory here
                Movie movie = new MovieImpl();
                movie.addFile(movieFile);
                this.entityManager.persist(movie);
            }
        }

        // Delete empty movies
        this.entityManager.createQuery("DELETE FROM MOVIE m WHERE SIZE(m.files) = 0").executeUpdate();
    }

    @Override
    public void synchronizeMovie(String movieId, List<MovieInfo> movieInfoList) {
        Movie movie = this.entityManager.find(MovieImpl.class, movieId);
        if (movie != null) {
            List<? extends MovieInfo> existMovieInfoList = movie.getInfo();
            loop:
            for (MovieInfo movieInfo : movieInfoList) {
                if (existMovieInfoList != null && !existMovieInfoList.isEmpty()) {
                    for (MovieInfo existMovieInfo : existMovieInfoList) {
                        if (existMovieInfo.getProviderName().equalsIgnoreCase(movieInfo.getProviderName())) {
                            continue loop;
                        }
                    }
                }
                // Not exist in databased
                movie.addInfo(movieInfo);
            }
            this.entityManager.merge(movie);
        }
    }

    @Override
    public void synchronizeImage(ImageFile imageFile) {
        // If the file already exist in DB, keep current Image, replace ImageFile
        List<ImageFileImpl> imageFiles = this.entityManager.createQuery("SELECT i FROM IMAGE_FILE i WHERE i.absolutePath = ?1", ImageFileImpl.class).setParameter(1, imageFile.getAbsolutePath()).getResultList();
        if (imageFiles != null && imageFiles.size() == 1) {
            imageFiles.get(0).getImage().setOriginalFile(imageFile);
        }

        // Delete old ImageFiles
        this.entityManager.createQuery("DELETE FROM IMAGE_FILE i WHERE i.absolutePath = ?1").setParameter(1, imageFile.getAbsolutePath()).executeUpdate();

        // Merge/Persist Image/ImageFile into DB
        if (imageFile.getImage() != null) {
            this.entityManager.merge(imageFile.getImage());
        } else {
            // TODO: Should use factory here
            Image image = new ImageImpl();
            image.setOriginalFile(imageFile);
            image.setAlbum(FilenameUtils.getName(FilenameUtils.getFullPathNoEndSeparator(imageFile.getFilePath())));
            image.setTitle(FilenameUtils.removeExtension(imageFile.getFileName()));
            this.entityManager.merge(image);
        }

        // Delete empty Images
        this.entityManager.createQuery("DELETE FROM IMAGE i WHERE i.originalFile IS NULL").executeUpdate();
    }

    @Override
    public void persist(Object entity) {
        this.entityManager.persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return this.entityManager.merge(entity);
    }

    @Override
    public void remove(Object entity) {
        this.entityManager.remove(entity);
    }

    @Override
    public void refresh(Object entity) {
        this.entityManager.refresh(entity);
    }

    /**
     * Inject Entity Manager
     *
     * @param entityManager Entity Manager
     */
    @SuppressWarnings("unused")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
