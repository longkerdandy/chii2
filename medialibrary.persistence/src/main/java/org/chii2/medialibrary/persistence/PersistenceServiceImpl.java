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
    // Logger
    private Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.persistence");

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library PersistenceService init.");
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library PersistenceService destroy.");
    }

    @Override
    public List<MovieImpl> getAllMovies() {
        // Get the current movie files list from database
        return entityManager.createNamedQuery("Movie.findAll", MovieImpl.class).getResultList();
    }

    @Override
    public Movie getMovieById(String id) {
        // Get movie by id, should be a only one result
        try {
            return entityManager.createNamedQuery("Movie.findById", MovieImpl.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie <{}> but not exist.", id);
            return null;
        }
    }

    @Override
    public List<? extends Movie> getAllMoviesByName(String movieName) {
        // Get movie by name, return all possible movies
        return entityManager.createNamedQuery("Movie.findByName", MovieImpl.class).setParameter("name", "%" + movieName.toLowerCase() + "%").getResultList();
    }

    @Override
    public Movie getSingleMovieByName(String movieName) {
        // Get movie by name, return single result
        try {
            return entityManager.createNamedQuery("Movie.findByName", MovieImpl.class).setParameter("name", "%" + movieName.toLowerCase() + "%").getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie with name <{}> but not exist.", movieName);
            return null;
        }
    }

    @Override
    public MovieFile getMovieFileById(String id) {
        // Get movie by id, should be a only one result
        try {
            return entityManager.createNamedQuery("MovieFile.findById", MovieFileImpl.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie <{}> but not exist.", id);
            return null;
        }
    }

    @Override
    public MovieInfo getMovieInfoById(String id) {
        // Get movie by id, should be a only one result
        try {
            return entityManager.createNamedQuery("MovieInfo.findById", MovieInfoImpl.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie <{}> but not exist.", id);
            return null;
        }
    }

    @Override
    public MovieImage getMovieImageById(String imageId) {
        // Get movie image by id, should be a only one result
        try {
            return entityManager.createNamedQuery("MovieImage.findById", MovieImageImpl.class).setParameter("id", imageId).getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Try to get movie image <{}> but not exist.", imageId);
            return null;
        }
    }

    @Override
    public List<? extends Image> getAllImages(int firstResult, int maxResults, Map<String, String> sorts) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
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
        if (firstResult >= 0 && maxResults >= 0) {
            return typedQuery.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
        } else {
            return typedQuery.getResultList();
        }
    }

    @Override
    public Image getImageById(String id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
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
    public List<? extends Image> getAllImagesByName(String imageName, int firstResult, int maxResults, Map<String, String> sorts) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        // From Query
        CriteriaQuery<ImageImpl> fromQuery = builder.createQuery(ImageImpl.class);
        Root<ImageImpl> images = fromQuery.from(ImageImpl.class);
        Path<String> name = images.join("file").get("name");
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
        if (firstResult >= 0 && maxResults >= 0) {
            return typedQuery.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
        } else {
            return typedQuery.getResultList();
        }
    }

    @Override
    public void deleteAllImages() {
        // Delete all images
        entityManager.createNamedQuery("Image.deleteAll", ImageImpl.class).executeUpdate();
    }

    @Override
    public ImageFile getImageFileById(String id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
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
    public List<String> getAllImageAlbums(int firstResult, int maxResults, Map<String, String> sorts) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
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
        if (firstResult >= 0 && maxResults >= 0) {
            return typedQuery.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
        } else {
            return typedQuery.getResultList();
        }
    }

    @Override
    public long getImageAlbumsCount() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
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
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
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
        if (firstResult >= 0 && maxResults >= 0) {
            return typedQuery.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
        } else {
            return typedQuery.getResultList();
        }
    }

    @Override
    public long getImagesCountByAlbum(String album) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
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
