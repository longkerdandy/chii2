package org.chii2.medialibrary.provider.tmdb.parser;

import org.chii2.medialibrary.api.persistence.entity.MovieImage;
import org.chii2.medialibrary.api.persistence.entity.MovieInfo;
import org.chii2.medialibrary.persistence.factory.MovieFactoryImpl;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Json Parser Test
 */
public class JsonParserTest {

    @Test
    public void parseTest() throws ParseException {
        JsonParser parser = new JsonParser(new MovieFactoryImpl());
        String content = "[{\"score\":null,\"popularity\":3,\"translated\":true,\"adult\":false,\"language\":\"en\",\"original_name\":\"Transformers\",\"name\":\"Transformers\",\"alternative_name\":\"Transformers: The IMAX Experience\",\"movie_type\":\"movie\",\"id\":1858,\"imdb_id\":\"tt0418279\",\"url\":\"http://www.themoviedb.org/movie/1858\",\"votes\":69,\"rating\":7.2,\"certification\":\"PG-13\",\"overview\":\"Young teenager Sam Witwicky becomes involved in the ancient struggle between two extraterrestrial factions of transforming robots, the heroic Autobots and the evil Decepticons.  Sam holds the clue to unimaginable power and the Decepticons will stop at nothing to retrieve it.\",\"released\":\"2007-07-04\",\"posters\":[{\"image\":{\"type\":\"poster\",\"size\":\"original\",\"height\":1500,\"width\":1000,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-original.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}},{\"image\":{\"type\":\"poster\",\"size\":\"mid\",\"height\":750,\"width\":500,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-mid.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}},{\"image\":{\"type\":\"poster\",\"size\":\"cover\",\"height\":278,\"width\":185,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-cover.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}},{\"image\":{\"type\":\"poster\",\"size\":\"thumb\",\"height\":138,\"width\":92,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-thumb.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}},{\"image\":{\"type\":\"poster\",\"size\":\"w342\",\"height\":513,\"width\":342,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-w342.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}},{\"image\":{\"type\":\"poster\",\"size\":\"w154\",\"height\":231,\"width\":154,\"url\":\"http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-w154.jpg\",\"id\":\"4c9ea5cf5e73d67049000233\"}}],\"backdrops\":[{\"image\":{\"type\":\"backdrop\",\"size\":\"original\",\"height\":1080,\"width\":1920,\"url\":\"http://cf1.imgobject.com/backdrops/2ce/4bc91339017a3c57fe0072ce/transformers-original.jpg\",\"id\":\"4bc91339017a3c57fe0072ce\"}},{\"image\":{\"type\":\"backdrop\",\"size\":\"poster\",\"height\":439,\"width\":780,\"url\":\"http://cf1.imgobject.com/backdrops/2ce/4bc91339017a3c57fe0072ce/transformers-poster.jpg\",\"id\":\"4bc91339017a3c57fe0072ce\"}},{\"image\":{\"type\":\"backdrop\",\"size\":\"thumb\",\"height\":169,\"width\":300,\"url\":\"http://cf1.imgobject.com/backdrops/2ce/4bc91339017a3c57fe0072ce/transformers-thumb.jpg\",\"id\":\"4bc91339017a3c57fe0072ce\"}},{\"image\":{\"type\":\"backdrop\",\"size\":\"w1280\",\"height\":720,\"width\":1280,\"url\":\"http://cf1.imgobject.com/backdrops/2ce/4bc91339017a3c57fe0072ce/transformers-w1280.jpg\",\"id\":\"4bc91339017a3c57fe0072ce\"}}],\"version\":359,\"last_modified_at\":\"2011-03-11 02:44:55\"}]";
        List<MovieInfo> movieInfo = parser.parseMovieInfo(content, 1, 3, 3);

        assert movieInfo.size() == 1;
        assert movieInfo.get(0).getScore() == 0;
        assert movieInfo.get(0).getPopularity() == 3;
        assert !movieInfo.get(0).getAdult();
        assert movieInfo.get(0).getLanguage().equals("en");
        assert movieInfo.get(0).getOriginalName().equals("Transformers");
        assert movieInfo.get(0).getName().equals("Transformers");
        assert movieInfo.get(0).getAlternativeName().equals("Transformers: The IMAX Experience");
        assert movieInfo.get(0).getProviderId().equals("1858");
        assert movieInfo.get(0).getIMDbId().equals("tt0418279");
        assert movieInfo.get(0).getUrl().equals("http://www.themoviedb.org/movie/1858");
        assert movieInfo.get(0).getVotes() == 69;
        assert movieInfo.get(0).getRating() == 7.2;
        assert movieInfo.get(0).getCertification().equals("PG-13");
        assert movieInfo.get(0).getVersion() == 359;
        assert new SimpleDateFormat("yyyy-MM-dd").format(movieInfo.get(0).getReleasedDate()).equals("2007-07-04");
        assert new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(movieInfo.get(0).getLastModified()).equals("2011-03-11 02:44:55");

        assert movieInfo.get(0).getPosters().get(0).getContentType().equals(MovieImage.POSTER_CONTENT_TYPE);
        assert movieInfo.get(0).getPosters().get(0).getSize().equals("original");
        assert movieInfo.get(0).getPosters().get(0).getHeight() == 1500;
        assert movieInfo.get(0).getPosters().get(0).getWidth() == 1000;
        assert movieInfo.get(0).getPosters().get(0).getImageType().equals(MovieImage.JPEG_IMAGE_TYPE);
        assert movieInfo.get(0).getPosters().get(0).getProviderId().equals("4c9ea5cf5e73d67049000233");
        assert movieInfo.get(0).getPosters().get(0).getUrl().equals("http://cf1.imgobject.com/posters/233/4c9ea5cf5e73d67049000233/transformers-original.jpg");

        assert movieInfo.get(0).getBackdrops().get(0).getContentType().equals(MovieImage.BACKDROP_CONTENT_TYPE);
        assert movieInfo.get(0).getBackdrops().get(0).getSize().equals("original");
        assert movieInfo.get(0).getBackdrops().get(0).getHeight() == 1080;
        assert movieInfo.get(0).getBackdrops().get(0).getWidth() == 1920;
        assert movieInfo.get(0).getBackdrops().get(0).getImageType().equals(MovieImage.JPEG_IMAGE_TYPE);
        assert movieInfo.get(0).getBackdrops().get(0).getProviderId().equals("4bc91339017a3c57fe0072ce");
        assert movieInfo.get(0).getBackdrops().get(0).getUrl().equals("http://cf1.imgobject.com/backdrops/2ce/4bc91339017a3c57fe0072ce/transformers-original.jpg");
    }
}
