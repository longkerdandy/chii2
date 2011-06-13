package org.chii2.medialibrary.persistence;

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
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.persistence");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library PersistenceService init.");
        // init builder
        builder = entityManager.getCriteriaBuilder();
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
        CriteriaQuery<MovieImpl> fromQuery = builder.createQuery(MovieImpl.class);
        Root<MovieImpl> movies = fromQuery.from(MovieImpl.class);
        // Select Query
        CriteriaQuery<MovieImpl> selectQuery = fromQuery.select(movies);
        // Order
        if (sorts != null) {
            List<Order> orders = new ArrayList<Order>();
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
                            orders.add(builder.asc(movies.join("information", JoinType.LEFT).get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(builder.desc(movies.join("information", JoinType.LEFT).get(field.substring(5))));
                        }
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<MovieImpl> typedQuery = entityManager.createQuery(selectQuery);
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
        return entityManager.find(MovieImpl.class, id);
    }

    @Override
    public List<? extends Movie> getMoviesByName(String movieName, int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<MovieImpl> fromQuery = builder.createQuery(MovieImpl.class);
        Root<MovieImpl> movies = fromQuery.from(MovieImpl.class);
        Path<String> infoMovieNameFiled = movies.join("information").get("name");
        Path<String> fileMovieNameFiled = movies.join("files").get("movieName");
        // Select Query
        CriteriaQuery<MovieImpl> selectQuery = fromQuery.select(movies);
        // Where Query
        selectQuery.where(builder.or(builder.like(builder.lower(infoMovieNameFiled), "%" + movieName.toLowerCase() + "%"), builder.like(builder.lower(fileMovieNameFiled), "%" + movieName.toLowerCase() + "%")));
        // Order
        if (sorts != null) {
            List<Order> orders = new ArrayList<Order>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (field.startsWith("file.")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(builder.asc(movies.join("files").get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(builder.desc(movies.join("files").get(field.substring(5))));
                        }
                    } else if (field.startsWith("info.")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(builder.asc(movies.join("information").get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(builder.desc(movies.join("information").get(field.substring(5))));
                        }
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<MovieImpl> typedQuery = entityManager.createQuery(selectQuery);
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
        CriteriaQuery<MovieFileImpl> fromQuery = builder.createQuery(MovieFileImpl.class);
        Root<MovieFileImpl> movieFies = fromQuery.from(MovieFileImpl.class);
        // Select Query
        CriteriaQuery<MovieFileImpl> selectQuery = fromQuery.select(movieFies);
        // Order
        if (sorts != null) {
            List<Order> orders = new ArrayList<Order>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (sortType.equalsIgnoreCase("asc")) {
                        orders.add(builder.asc(movieFies.get(field)));
                    } else if (sortType.equalsIgnoreCase("desc")) {
                        orders.add(builder.desc(movieFies.get(field)));
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<MovieFileImpl> typedQuery = entityManager.createQuery(selectQuery);
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
        return entityManager.find(MovieFileImpl.class, id);
    }

    @Override
    public MovieInfo getMovieInfoById(String id) {
        return entityManager.find(MovieInfoImpl.class, id);
    }

    @Override
    public MovieImage getMovieImageById(String id) {
        return entityManager.find(MovieImageImpl.class, id);
    }

    @Override
    public byte[] getMovieThumbnailById(String movieId) {
        List<? extends MovieInfo> movieInfoList = entityManager.createQuery("SELECT i FROM MOVIE_INFO i JOIN i.movie m WHERE m.id = ?1", MovieInfoImpl.class).setParameter(1, movieId).getResultList();
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
        return entityManager.createQuery("SELECT COUNT(m) FROM MOVIE m", Long.class).getSingleResult();
    }

    @Override
    public long getMovieFilesCount() {
        return entityManager.createQuery("SELECT COUNT(m) FROM MOVIE_FILE m", Long.class).getSingleResult();
    }

    @Override
    public List<? extends Image> getImages(int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<ImageImpl> fromQuery = builder.createQuery(ImageImpl.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        // Select Query
        CriteriaQuery<ImageImpl> selectQuery = fromQuery.select(images);
        // Order
        if (sorts != null) {
            List<Order> orders = new ArrayList<Order>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (field.startsWith("file.")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(builder.asc(images.join("files").get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(builder.desc(images.join("files").get(field.substring(5))));
                        }
                    } else {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(builder.asc(images.get(field)));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(builder.asc(images.get(field)));
                        }
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<ImageImpl> typedQuery = entityManager.createQuery(selectQuery);
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
        return entityManager.find(ImageImpl.class, id);
    }

    @Override
    public List<? extends Image> getImagesByField(String fieldName, String fieldValue, boolean strict, int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<ImageImpl> fromQuery = builder.createQuery(ImageImpl.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        Path<String> name;
        if (fieldName.startsWith("file.")) {
            name = images.join("file").get(fieldName.substring(5));
        } else {
            name = images.get(fieldName);
        }

        // Select Query
        CriteriaQuery<ImageImpl> selectQuery = fromQuery.select(images);
        // Where Query
        if (strict) {
            selectQuery.where(builder.like(builder.lower(name), fieldValue.toLowerCase()));
        } else {
            selectQuery.where(builder.like(builder.lower(name), "%" + fieldValue.toLowerCase() + "%"));
        }
        // Order query
        if (sorts != null) {
            List<Order> orders = new ArrayList<Order>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (field.startsWith("file.")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(builder.asc(images.join("files").get(field.substring(5))));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(builder.desc(images.join("files").get(field.substring(5))));
                        }
                    } else {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(builder.asc(images.get(field)));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(builder.asc(images.get(field)));
                        }
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<ImageImpl> typedQuery = entityManager.createQuery(selectQuery);
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
        CriteriaQuery<String> fromQuery = builder.createQuery(String.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        // Select Query
        CriteriaQuery<String> selectQuery = fromQuery.multiselect(images.get("album"));
        selectQuery.distinct(true);
        if (sorts != null) {
            List<Order> orders = new ArrayList<Order>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null) {
                    if (field.equals("album")) {
                        if (sortType.equalsIgnoreCase("asc")) {
                            orders.add(builder.asc(images.get(field)));
                        } else if (sortType.equalsIgnoreCase("desc")) {
                            orders.add(builder.asc(images.get(field)));
                        }
                    }
                }
            }
            selectQuery.orderBy(orders);
        }
        // Final Query
        TypedQuery<String> typedQuery = entityManager.createQuery(selectQuery);
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
    public long getImageAlbumsCount() {
        return entityManager.createQuery("SELECT COUNT(i) FROM IMAGE i", Long.class).getSingleResult();
    }

    @Override
    public long getImagesCountByAlbum(String album) {
        return entityManager.createQuery("SELECT COUNT(i) FROM IMAGE i WHERE i.album = ?1", Long.class).setParameter(1, album).getSingleResult();
    }

    @Override
    public ImageFile getImageFileById(String id) {
        return entityManager.find(ImageFileImpl.class, id);
    }

    @Override
    public int deleteImages() {
        return entityManager.createQuery("DELETE FROM IMAGE i").executeUpdate();
    }

    @Override
    public void deleteMovieInfo() {
        entityManager.createQuery("DELETE FROM MOVIE_INFO m").executeUpdate();
    }

    @Override
    public void synchronize(List<MovieFile> movieFiles) {
        // Mapping relations
        for (MovieFile movieFile : movieFiles) {
            List<MovieFileImpl> dbMovieFiles = entityManager.createQuery("SELECT m FROM MOVIE_FILE m WHERE m.absoluteName = ?1", MovieFileImpl.class).setParameter(1, movieFile.getAbsoluteName()).getResultList();
            if (dbMovieFiles != null && dbMovieFiles.size() == 1) {
                dbMovieFiles.get(0).getMovie().addFile(movieFile);
            }
        }

        // Delete old ones
        entityManager.createQuery("DELETE FROM MOVIE_FILE m").executeUpdate();

        // Insert new ones
        for (MovieFile movieFile : movieFiles) {
            if (movieFile.getMovie() != null) {
                entityManager.merge(movieFile.getMovie());
            } else {
                List<MovieImpl> dbMovies = entityManager.createQuery("SELECT m FROM MOVIE m JOIN m.files f WHERE f.filePath = ?1 AND f.movieName = ?2", MovieImpl.class).setParameter(1, movieFile.getFilePath()).setParameter(2, movieFile.getMovieName()).getResultList();
                if (dbMovies != null && dbMovies.size() > 0) {
                    Movie movie = dbMovies.get(0);
                    movie.addFile(movieFile);
                    entityManager.merge(movie);
                } else {
                    Movie movie = new MovieImpl();
                    movie.addFile(movieFile);
                    entityManager.persist(movie);
                }
            }
        }

        // Delete empty movies
        entityManager.createQuery("DELETE FROM MOVIE m WHERE SIZE(m.files) = 0").executeUpdate();
    }

    @Override
    public void synchronize(String movieId, List<MovieInfo> info) {
        Movie movie = entityManager.find(MovieImpl.class, movieId);
        if (movie != null) {
            for (MovieInfo movieInfo : info) {
                movie.addInfo(movieInfo);
            }
            entityManager.merge(movie);
        }
    }

    @Override
    public void persist(Object entity) {
        entityManager.persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void remove(Object entity) {
        entityManager.remove(entity);
    }

    @Override
    public void refresh(Object entity) {
        entityManager.refresh(entity);
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
