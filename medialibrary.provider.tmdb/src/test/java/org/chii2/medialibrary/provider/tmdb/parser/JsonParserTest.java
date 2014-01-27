package org.chii2.medialibrary.provider.tmdb.parser;

import static org.mockito.Mockito.*;

import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.api.persistence.factory.MovieFactory;
import org.mockito.InOrder;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Json Parser Test
 */
public class JsonParserTest {

    @Test
    public void parseTest() throws ParseException {
        // Mock Movie
        MovieFactory movieFactory = mock(MovieFactory.class);
        MovieInfo movieInfo = mock(MovieInfo.class);
        MovieImage movieImage = mock(MovieImage.class);
        when(movieFactory.createMovieInfo()).thenReturn(movieInfo);
        when(movieFactory.createMovieImage()).thenReturn(movieImage);
        // Parse
        JsonParser parser = new JsonParser(movieFactory) {
            @SuppressWarnings("unused")
            private Logger logger = mock(Logger.class);
        };
        String content = "[{\"score\":null,\"popularity\":3,\"translated\":true,\"adult\":false,\"language\":\"en\",\"original_name\":\"Transformers\",\"name\":\"Transformers\",\"alternative_name\":\"Transformers: The IMAX Experience\",\"movie_type\":\"movie\",\"id\":1858,\"imdb_id\":\"tt0418279\",\"url\":\"http://www.themoviedb.org/movie/1858\",\"votes\":69,\"rating\":7.2,\"certification\":\"PG-13\",\"overview\":\"Young teenager Sam Witwicky becomes involved in the ancient struggle between two extraterrestrial factions of transforming robots, the heroic Autobots and the evil Decepticons.  Sam holds the clue to unimaginable power and the Decepticons will stop at nothing to retrieve it.\",\"released\":\"2007-07-04\",\"posters\":[{\"image\":{\"type\":\"poster\",\"size\":\"original\",\"height\":1500,\"width\":1000,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-original.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}},{\"image\":{\"type\":\"poster\",\"size\":\"mid\",\"height\":750,\"width\":500,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-mid.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}},{\"image\":{\"type\":\"poster\",\"size\":\"cover\",\"height\":278,\"width\":185,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-cover.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}},{\"image\":{\"type\":\"poster\",\"size\":\"thumb\",\"height\":138,\"width\":92,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-thumb.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}},{\"image\":{\"type\":\"poster\",\"size\":\"w342\",\"height\":513,\"width\":342,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-w342.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}},{\"image\":{\"type\":\"poster\",\"size\":\"w154\",\"height\":231,\"width\":154,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-w154.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}}],\"backdrops\":[{\"image\":{\"type\":\"backdrop\",\"size\":\"original\",\"height\":1080,\"width\":1920,\"url\":\"http://cf1.imgobject.com/backdrops/2ce/4bc91339017a3c57fe0072ce/transformers-original.jpg\",\"id\":\"4bc91339017a3c57fe0072ce\"}},{\"image\":{\"type\":\"backdrop\",\"size\":\"poster\",\"height\":439,\"width\":780,\"url\":\"http://cf1.imgobject.com/backdrops/2ce/4bc91339017a3c57fe0072ce/transformers-poster.jpg\",\"id\":\"4bc91339017a3c57fe0072ce\"}},{\"image\":{\"type\":\"backdrop\",\"size\":\"thumb\",\"height\":169,\"width\":300,\"url\":\"http://cf1.imgobject.com/backdrops/2ce/4bc91339017a3c57fe0072ce/transformers-thumb.jpg\",\"id\":\"4bc91339017a3c57fe0072ce\"}},{\"image\":{\"type\":\"backdrop\",\"size\":\"w1280\",\"height\":720,\"width\":1280,\"url\":\"http://cf1.imgobject.com/backdrops/2ce/4bc91339017a3c57fe0072ce/transformers-w1280.jpg\",\"id\":\"4bc91339017a3c57fe0072ce\"}}],\"version\":359,\"last_modified_at\":\"2011-03-11 02:44:55\"}]";
        List<MovieInfo> movieInfoList = parser.parseMovieInfo(content, 1, 3, 3);

        assert movieInfoList.size() == 1;
        verify(movieInfo).setScore(0);
        verify(movieInfo).setPopularity(3);
        verify(movieInfo).setAdult(false);
        verify(movieInfo).setLanguage("en");
        verify(movieInfo).setOriginalName("Transformers");
        verify(movieInfo).setAlternativeName("Transformers: The IMAX Experience");
        verify(movieInfo).setProviderId("1858");
        verify(movieInfo).setIMDbId("tt0418279");
        verify(movieInfo).setUrl("http://www.themoviedb.org/movie/1858");
        verify(movieInfo).setVotes(69);
        verify(movieInfo).setRating(7.2);
        verify(movieInfo).setCertification("PG-13");
        verify(movieInfo).setVersion(359);
        try {
            verify(movieInfo).setReleasedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2007-07-04"));
            verify(movieInfo).setLastModified(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-03-11 02:44:55"));
        } catch (Exception e) {
            assert false;
        }

        InOrder inOrder = inOrder(movieImage);
        inOrder.verify(movieImage).setContentType(MovieImage.POSTER_CONTENT_TYPE);
        inOrder.verify(movieImage).setImageType(MovieImage.JPEG_IMAGE_TYPE);
        inOrder.verify(movieImage).setSize("original");
        inOrder.verify(movieImage).setHeight(1500);
        inOrder.verify(movieImage).setWidth(1000);
        inOrder.verify(movieImage).setUrl("http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-original.jpg");
        inOrder.verify(movieImage).setProviderId("4c9ea5cf5e73d67049000233");


        inOrder.verify(movieImage).setContentType(MovieImage.BACKDROP_CONTENT_TYPE);
        inOrder.verify(movieImage).setImageType(MovieImage.JPEG_IMAGE_TYPE);
        inOrder.verify(movieImage).setSize("original");
        inOrder.verify(movieImage).setHeight(1080);
        inOrder.verify(movieImage).setWidth(1920);
        inOrder.verify(movieImage).setUrl("http://cf1.imgobject.com/backdrops/2ce/4bc91339017a3c57fe0072ce/transformers-original.jpg");
        inOrder.verify(movieImage).setProviderId("4bc91339017a3c57fe0072ce");
    }
}
