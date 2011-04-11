package org.chii2.mediaserver.content.common.Item;

import org.chii2.mediaserver.api.content.item.VisualItem;
import org.teleal.cling.support.model.Person;
import org.teleal.cling.support.model.PersonWithRole;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.item.VideoItem;

import java.net.URI;
import java.util.List;

/**
 * Movie Item (Video Item)
 * Especially represent a movie
 */
public class MovieItem extends VideoItem implements VisualItem {

    /**
     * Constructor
     *
     * @param filter   Content Filter
     * @param id       Item ID
     * @param parentId Item Parent ID
     * @param title    Item Title
     */
    protected MovieItem(String filter, String id, String parentId, String title) {
        super();

        // Item ID
        setId(id);
        // Item Parent ID
        setParentID(parentId);
        // Item Title
        if (filter.contains("dc:title")) {
            setTitle(title);
        }
        //  Creator (part of UPnP protocol standard)
        if (filter.contains("dc:creator")) {
            setCreator("System");
        }
    }

    /**
     * @param filter          Content Filter
     * @param id              Item ID
     * @param parentId        Item Parent ID
     * @param title           Item Title
     * @param description     Description
     * @param longDescription Long Description
     * @param genres          Genres
     * @param rating          Rating
     * @param language        Language
     * @param producers       Producers
     * @param actors          Actors
     * @param directors       Directors
     * @param publishers      Publishers
     * @param resources       Resources
     */
    @SuppressWarnings({"ConstantConditions"})
    public MovieItem(String filter, String id, String parentId, String title, String description, String longDescription, String[] genres, String rating, String language, Person[] producers, PersonWithRole[] actors, Person[] directors, Person[] publishers, List<Res> resources) {

        this(filter, id, parentId, title);

        // Description
        if (filter.contains("dc:description")) {
            setDescription(description);
        }
        // Long Description
        if (filter.contains("upnp:longDescription")) {
            setLongDescription(longDescription);
        }
        // Genres
        if (filter.contains("upnp:genre") && genres != null) {
            setGenres(genres);
        }
        // Rating
        if (filter.contains("upnp:rating") && rating != null) {
            setRating(rating);
        }
        // Language
        if (filter.contains("dc:language ") && language != null) {
            setLanguage(language);
        }
        // Producers
        if (filter.contains("upnp:producer") && producers != null) {
            setProducers(producers);
        }
        // Actors
        if (filter.contains("upnp:actor") && actors != null) {
            setActors(actors);
        }
        // Publishers
        if (filter.contains("dc:publisher") && publishers != null) {
            setPublishers(publishers);
        }
        // Directors
        if (filter.contains("upnp:director") && directors != null) {
            setDirectors(directors);
        }

        // Resources
        for (Res resource : resources) {
            addResource(resource);
        }
    }
}
