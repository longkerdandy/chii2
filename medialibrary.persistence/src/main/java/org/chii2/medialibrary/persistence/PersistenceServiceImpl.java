package org.chii2.medialibrary.persistence;

import org.chii2.medialibrary.api.persistence.PersistenceService;
import org.chii2.medialibrary.api.persistence.entity.*;
import org.chii2.medialibrary.persistence.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        // From Query
        CriteriaQuery<MovieImpl> fromQuery = builder.createQuery(MovieImpl.class);
        Root<MovieImpl> movies = fromQuery.from(MovieImpl.class);
        // Select Query
        CriteriaQuery<MovieImpl> selectQuery = fromQuery.select(movies);
        // Where Query
        selectQuery.where(builder.equal(movies.get("id"), id));
        // Final Query
        TypedQuery<MovieImpl> typedQuery = entityManager.createQuery(selectQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie file <{}> but not exist.", id);
            return null;
        }
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
    public Movie getMoviesContainFile(String fileId) {
        // From Query
        CriteriaQuery<MovieImpl> fromQuery = builder.createQuery(MovieImpl.class);
        Root<MovieImpl> movies = fromQuery.from(MovieImpl.class);
        Path<String> fileIdFiled = movies.join("files").get("id");
        // Select Query
        CriteriaQuery<MovieImpl> selectQuery = fromQuery.select(movies);
        // Where Query
        selectQuery.where(builder.equal(fileIdFiled, fileId));
        // Final Query
        TypedQuery<MovieImpl> typedQuery = entityManager.createQuery(selectQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie contains file <{}> but not exist.", fileId);
            return null;
        }
    }

    @Override
    public Movie getMoviesContainFile(String absolutePath, String fileMovieName) {
        // From Query
        CriteriaQuery<MovieImpl> fromQuery = builder.createQuery(MovieImpl.class);
        Root<MovieImpl> movies = fromQuery.from(MovieImpl.class);
        Path<String> filePathFiled = movies.join("files").get("filePath");
        Path<String> movieNameFiled = movies.join("files").get("movieName");
        // Select Query
        CriteriaQuery<MovieImpl> selectQuery = fromQuery.select(movies);
        // Where Query
        selectQuery.where(builder.and(builder.equal(filePathFiled, absolutePath), builder.equal(movieNameFiled, fileMovieName)));
        // Final Query
        TypedQuery<MovieImpl> typedQuery = entityManager.createQuery(selectQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie contains file with absolute name <{}> and movie name <{}> but not exist.", absolutePath, fileMovieName);
            return null;
        }
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
            typedQuery = typedQuery.setFirstResult(firstResult);
        }
        // Max Results
        if (maxResults >= 0) {
            typedQuery = typedQuery.setMaxResults(maxResults);
        }
        return typedQuery.getResultList();
    }

    @Override
    public MovieFile getMovieFileById(String id) {
        // From Query
        CriteriaQuery<MovieFileImpl> fromQuery = builder.createQuery(MovieFileImpl.class);
        Root<MovieFileImpl> movieFiles = fromQuery.from(MovieFileImpl.class);
        // Select Query
        CriteriaQuery<MovieFileImpl> selectQuery = fromQuery.select(movieFiles);
        // Where Query
        selectQuery.where(builder.equal(movieFiles.get("id"), id));
        // Final Query
        TypedQuery<MovieFileImpl> typedQuery = entityManager.createQuery(selectQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie file <{}> but not exist.", id);
            return null;
        }
    }

    @Override
    public MovieFile getMovieFileByAbsoluteName(String absoluteName) {
        // From Query
        CriteriaQuery<MovieFileImpl> fromQuery = builder.createQuery(MovieFileImpl.class);
        Root<MovieFileImpl> movieFiles = fromQuery.from(MovieFileImpl.class);
        // Select Query
        CriteriaQuery<MovieFileImpl> selectQuery = fromQuery.select(movieFiles);
        // Where Query
        selectQuery.where(builder.equal(movieFiles.get("absoluteName"), absoluteName));
        // Final Query
        TypedQuery<MovieFileImpl> typedQuery = entityManager.createQuery(selectQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie file <{}> but not exist.", absoluteName);
            return null;
        }
    }

    @Override
    public MovieInfo getMovieInfoById(String id) {
        // From Query
        CriteriaQuery<MovieInfoImpl> fromQuery = builder.createQuery(MovieInfoImpl.class);
        Root<MovieInfoImpl> movieInfo = fromQuery.from(MovieInfoImpl.class);
        // Select Query
        CriteriaQuery<MovieInfoImpl> selectQuery = fromQuery.select(movieInfo);
        // Where Query
        selectQuery.where(builder.equal(movieInfo.get("id"), id));
        // Final Query
        TypedQuery<MovieInfoImpl> typedQuery = entityManager.createQuery(selectQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie info <{}> but not exist.", id);
            return null;
        }
    }

    @Override
    public MovieImage getMovieImageById(String id) {
        // From Query
        CriteriaQuery<MovieImageImpl> fromQuery = builder.createQuery(MovieImageImpl.class);
        Root<MovieImageImpl> movieImages = fromQuery.from(MovieImageImpl.class);
        // Select Query
        CriteriaQuery<MovieImageImpl> selectQuery = fromQuery.select(movieImages);
        // Where Query
        selectQuery.where(builder.equal(movieImages.get("id"), id));
        // Final Query
        TypedQuery<MovieImageImpl> typedQuery = entityManager.createQuery(selectQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie image <{}> but not exist.", id);
            return null;
        }
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
                if (field != null && sortType != null && sortType.equalsIgnoreCase("asc")) {
                    orders.add(builder.asc(images.join("file").get(field)));
                } else if (field != null && sortType != null && sortType.equalsIgnoreCase("desc")) {
                    orders.add(builder.desc(images.join("file").get(field)));
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
        // From Query
        CriteriaQuery<ImageImpl> fromQuery = builder.createQuery(ImageImpl.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        // Select Query
        CriteriaQuery<ImageImpl> selectQuery = fromQuery.select(images);
        // Where Query
        selectQuery.where(builder.equal(images.get("id"), id));
        // Final Query
        TypedQuery<ImageImpl> typedQuery = entityManager.createQuery(selectQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get image <{}> but not exist.", id);
            return null;
        }
    }

    @Override
    public List<? extends Image> getImagesByName(String imageName, int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<ImageImpl> fromQuery = builder.createQuery(ImageImpl.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        Path<String> name = images.join("file").get("fileName");
        // Select Query
        CriteriaQuery<ImageImpl> selectQuery = fromQuery.select(images);
        // Where Query
        selectQuery.where(builder.like(builder.lower(name), "%" + imageName.toLowerCase() + "%"));
        // Order query
        if (sorts != null) {
            List<Order> orders = new ArrayList<Order>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null && sortType.equalsIgnoreCase("asc")) {
                    orders.add(builder.asc(images.join("file").get(field)));
                } else if (field != null && sortType != null && sortType.equalsIgnoreCase("desc")) {
                    orders.add(builder.desc(images.join("file").get(field)));
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
    public void deleteAllImages() {
        // Delete all images
        entityManager.createNamedQuery("Image.deleteAll", ImageImpl.class).executeUpdate();
    }

    @Override
    public ImageFile getImageFileById(String id) {
        // From Query
        CriteriaQuery<ImageFileImpl> fromQuery = builder.createQuery(ImageFileImpl.class);
        Root<ImageFileImpl> imageFiles = fromQuery.from(ImageFileImpl.class);
        // Select Query
        CriteriaQuery<ImageFileImpl> selectQuery = fromQuery.select(imageFiles);
        // Where Query
        selectQuery.where(builder.equal(imageFiles.get("id"), id));
        // Final Query
        TypedQuery<ImageFileImpl> typedQuery = entityManager.createQuery(selectQuery);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get image file <{}> but not exist.", id);
            return null;
        }
    }

    @Override
    public List<String> getImageAlbums(int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<String> fromQuery = builder.createQuery(String.class);
        Root<ImageFileImpl> imageFiles = fromQuery.from(ImageFileImpl.class);
        // Select Query
        CriteriaQuery<String> selectQuery = fromQuery.multiselect(imageFiles.get("album"));
        selectQuery.distinct(true);
        if (sorts != null) {
            List<Order> orders = new ArrayList<Order>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null && sortType.equalsIgnoreCase("asc")) {
                    orders.add(builder.asc(imageFiles.get(field)));
                } else if (field != null && sortType != null && sortType.equalsIgnoreCase("desc")) {
                    orders.add(builder.desc(imageFiles.get(field)));
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
        // From Query
        CriteriaQuery<Long> fromQuery = builder.createQuery(Long.class);
        Root<ImageFileImpl> imageFiles = fromQuery.from(ImageFileImpl.class);
        // Select Query
        CriteriaQuery<Long> selectQuery = fromQuery.select(builder.count(imageFiles));
        // Final Query
        TypedQuery<Long> typedQuery = entityManager.createQuery(selectQuery);
        return typedQuery.getSingleResult();
    }

    @Override
    public List<? extends Image> getImagesByAlbum(String album, int firstResult, int maxResults, Map<String, String> sorts) {
        // From Query
        CriteriaQuery<ImageImpl> fromQuery = builder.createQuery(ImageImpl.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        Path<String> albumField = images.join("file").get("album");
        // Select Query
        CriteriaQuery<ImageImpl> selectQuery = fromQuery.select(images);
        // Where Query
        selectQuery.where(builder.equal(albumField, album));
        // Order query
        if (sorts != null) {
            List<Order> orders = new ArrayList<Order>();
            for (Map.Entry<String, String> entry : sorts.entrySet()) {
                String field = entry.getKey();
                String sortType = entry.getValue();
                if (field != null && sortType != null && sortType.equalsIgnoreCase("asc")) {
                    orders.add(builder.asc(images.join("file").get(field)));
                } else if (field != null && sortType != null && sortType.equalsIgnoreCase("desc")) {
                    orders.add(builder.desc(images.join("file").get(field)));
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
    public long getImagesCountByAlbum(String album) {
        // From Query
        CriteriaQuery<Long> fromQuery = builder.createQuery(Long.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        Path<String> albumField = images.join("file").get("album");
        // Select Query
        CriteriaQuery<Long> selectQuery = fromQuery.select(builder.count(images));
        // Where Query
        selectQuery.where(builder.equal(albumField, album));
        // Final Query
        TypedQuery<Long> typedQuery = entityManager.createQuery(selectQuery);
        return typedQuery.getSingleResult();
    }

    @Override
    public void persist(List<Movie> movies) {
        for (Movie movie : movies) {
            if (movie.getClass() == MovieImpl.class) {
                entityManager.persist(movie);
            }
        }
    }

    @Override
    public void persist(Movie movie) {
        if (movie.getClass() == MovieImpl.class) {
            entityManager.persist(movie);
        }
    }

    @Override
    public void persist(MovieFile movieFile) {
        if (movieFile.getClass() == MovieFileImpl.class) {
            entityManager.persist(movieFile);
        }
    }

    @Override
    public void persist(MovieInfo movieInfo) {
        if (movieInfo.getClass() == MovieInfoImpl.class) {
            entityManager.persist(movieInfo);
        }
    }

    @Override
    public void persist(MovieImage movieImage) {
        if (movieImage.getClass() == MovieImageImpl.class) {
            entityManager.persist(movieImage);
        }
    }

    @Override
    public void persist(Image image) {
        if (image.getClass() == ImageImpl.class) {
            entityManager.persist(image);
        }
    }

    @Override
    public void persist(ImageFile imageFile) {
        if (imageFile.getClass() == ImageFileImpl.class) {
            entityManager.persist(imageFile);
        }
    }

    @Override
    public void merge(Movie movie) {
        if (movie.getClass() == MovieImpl.class) {
            entityManager.merge(movie);
        }
    }

    @Override
    public void merge(MovieFile movieFile) {
        if (movieFile.getClass() == MovieFileImpl.class) {
            entityManager.merge(movieFile);
        }
    }

    @Override
    public void merge(MovieInfo movieInfo) {
        if (movieInfo.getClass() == MovieInfoImpl.class) {
            entityManager.merge(movieInfo);
        }
    }

    @Override
    public void merge(MovieImage movieImage) {
        if (movieImage.getClass() == MovieImageImpl.class) {
            entityManager.merge(movieImage);
        }
    }

    @Override
    public void merge(Image image) {
        if (image.getClass() == ImageImpl.class) {
            entityManager.merge(image);
        }
    }

    @Override
    public void merge(ImageFile imageFile) {
        if (imageFile.getClass() == ImageFileImpl.class) {
            entityManager.merge(imageFile);
        }
    }

    @Override
    public void remove(Movie movie) {
        if (movie.getClass() == MovieImpl.class) {
            entityManager.remove(movie);
        }
    }

    @Override
    public void remove(MovieFile movieFile) {
        if (movieFile.getClass() == MovieFileImpl.class) {
            entityManager.remove(movieFile);
        }
    }

    @Override
    public void remove(MovieInfo movieInfo) {
        if (movieInfo.getClass() == MovieInfoImpl.class) {
            entityManager.remove(movieInfo);
        }
    }

    @Override
    public void remove(MovieImage movieImage) {
        if (movieImage.getClass() == MovieImageImpl.class) {
            entityManager.remove(movieImage);
        }
    }

    @Override
    public void remove(Image image) {
        if (image.getClass() == ImageImpl.class) {
            entityManager.remove(image);
        }
    }

    @Override
    public void remove(ImageFile imageFile) {
        if (imageFile.getClass() == ImageFileImpl.class) {
            entityManager.remove(imageFile);
        }
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
