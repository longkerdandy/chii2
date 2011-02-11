package org.chii2.medialibrary.shell.command;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.shell.command.MediaLibraryCommand;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Media Library Shell Command
 */
public class MediaLibraryCommandImpl implements MediaLibraryCommand {

    // Injected Media Library Service
    private MediaLibraryService mediaLibrary;

    @Override
    public void scan(String media) {
        if ("movie".equalsIgnoreCase(media) || "movies".equalsIgnoreCase(media)) {
            // Scan movies
            mediaLibrary.scanMovies();
        } else if ("image".equalsIgnoreCase(media) || "images".equalsIgnoreCase(media)) {
            // Scan images
            mediaLibrary.scanImages();
        } else {
            System.out.println("Bad parameter for <scan> command, the correct usage is: \"scan <movies|images>\".");
        }
    }

    @Override
    public void show(String[] arguments) {
        if (arguments.length > 0) {
            // Show Movies
            if ("movie".equalsIgnoreCase(arguments[0]) || "movies".equalsIgnoreCase(arguments[0])) {
                // Show all the movies
                if (arguments.length == 1) {
                    List<? extends Movie> movies = mediaLibrary.getAllMovies();
                    printMovieTable(movies);
                } else {
                    if (isUUID(getSubArrary(arguments))) {
                        Movie movie = mediaLibrary.getMovieById(getName(getSubArrary(arguments)));
                        printMovieTable(movie);
                    } else {
                        List<? extends Movie> movies = mediaLibrary.getAllMoviesByName(getName(getSubArrary(arguments)));
                        printMovieTable(movies);
                    }
                }
            } else if ("image".equalsIgnoreCase(arguments[0]) || "images".equalsIgnoreCase(arguments[0])) {
                // Show all the movies
                if (arguments.length == 1) {
                    List<? extends Image> images = mediaLibrary.getAllImages();
                    printImageTable(images);
                } else {
                    if (isUUID(getSubArrary(arguments))) {
                        Image image = mediaLibrary.getImageById(getName(getSubArrary(arguments)));
                        printImageTable(image);
                    } else {
                        List<? extends Image> images = mediaLibrary.getAllImagesByName(getName(getSubArrary(arguments)));
                        printImageTable(images);
                    }
                }
            }
        } else {
            System.out.println("Bad parameter for <show> command, the correct usage is: \"show <movies|images>\".");
        }
    }

    /**
     * Whether argument is UUID
     *
     * @param arguments Arguments
     * @return True if UUID
     */
    private boolean isUUID(String[] arguments) {
        if (arguments.length == 1) {
            try {
                @SuppressWarnings("unused")
                UUID id = UUID.fromString(arguments[0]);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Parse arguments and return name or UUID
     *
     * @param arguments Arguments
     * @return UUID or Name
     */
    private String getName(String[] arguments) {
        if (arguments.length == 1) {
            UUID id = null;
            try {
                id = UUID.fromString(arguments[1]);
            } catch (IllegalArgumentException e) {
                // Do nothing
            }
            // return id
            if (id != null) {
                return id.toString();
            }
            // return name
            else {
                return arguments[0];
            }
        } else {
            // return name with space
            StringBuffer name = new StringBuffer(StringUtils.trim(arguments[0]));
            for (int i = 1; i < arguments.length; i++) {
                name.append(" ");
                name.append(StringUtils.trim(arguments[i]));
            }
            return name.toString();
        }
    }

    /**
     * Print movie to command line in table format
     *
     * @param movie Movie Object
     */
    private void printMovieTable(Movie movie) {
        List<Movie> movieList = new ArrayList<Movie>();
        movieList.add(movie);
        printMovieTable(movieList);
    }

    /**
     * Print movie list to command line in table format
     *
     * @param movies Movie List
     */
    private void printMovieTable(List<? extends Movie> movies) {
        if (movies != null && !movies.isEmpty()) {
            String[] headers = {"ID", "Name", "Year", "Rating", "Codec", "Source", "Format"};
            String[][] content = new String[movies.size()][7];
            for (int i = 0; i < movies.size(); i++) {
                content[i][0] = StringUtils.defaultString(movies.get(i).getId());
                content[i][1] = StringUtils.defaultString(movies.get(i).getMovieName());
                content[i][2] = StringUtils.defaultString(movies.get(i).getMovieYear());
                content[i][3] = String.valueOf(movies.get(i).getMovieRating());
                content[i][4] = StringUtils.defaultString(movies.get(i).getMovieCodec());
                content[i][5] = StringUtils.defaultString(movies.get(i).getMovieSource());
                content[i][6] = StringUtils.defaultString(movies.get(i).getMovieFormat());
            }
            printTable(headers, content);
            System.out.printf("Found %1$d movies in Chii2 Media Library.\n", movies.size());
        } else {
            System.out.println("No movie found in Chii2 Media Library.");
        }
    }

    /**
     * Print image to command line in table format
     *
     * @param image Image
     */
    private void printImageTable(Image image) {
        List<Image> imageList = new ArrayList<Image>();
        imageList.add(image);
        printImageTable(imageList);
    }

    /**
     * Print image list to command line in table format
     *
     * @param images Image List
     */
    private void printImageTable(List<? extends Image> images) {
        if (images != null && !images.isEmpty()) {
            String[] headers = {"ID", "Path", "Album", "Title", "Size", "Date", "Type", "Resolution", "Camera"};
            String[][] content = new String[images.size()][9];
            for (int i = 0; i < images.size(); i++) {
                content[i][0] = StringUtils.defaultString(images.get(i).getId());
                content[i][1] = StringUtils.defaultString(images.get(i).getAbsoluteName());
                content[i][2] = StringUtils.defaultString(images.get(i).getAlbum());
                content[i][3] = StringUtils.defaultString(images.get(i).getTitle());
                content[i][4] = String.valueOf(images.get(i).getSize());
                content[i][5] = StringUtils.defaultString(getDate(images.get(i).getDateTaken()));
                content[i][6] = StringUtils.defaultString(images.get(i).getType());
                content[i][7] = StringUtils.defaultString(images.get(i).getWidth() + "x" + images.get(i).getHeight());
                content[i][8] = StringUtils.defaultString(images.get(i).getCameraModel());
            }
            printTable(headers, content);
            System.out.printf("Found %1$d images in Chii2 Media Library.\n", images.size());
        } else {
            System.out.println("No image found in Chii2 Media Library.");
        }
    }

    /**
     * Print command line content in table
     *
     * @param header  Table header
     * @param content Table content
     */
    private void printTable(String[] header, String[][] content) {
        // Find out what the maximum number of columns is in any row
        int maxColumns = header.length;
        for (String[] row : content) {
            maxColumns = Math.max(row.length, maxColumns);
        }

        // Find the maximum length of a string in each column
        int[] lengths = new int[maxColumns];
        for (int i = 0; i < header.length; i++) {
            lengths[i] = header[i].length();
        }
        for (String[] row : content) {
            for (int j = 0; j < row.length; j++) {
                lengths[j] = Math.max(row[j].length(), lengths[j]);
            }
        }

        // Calculate the total length
        int totalLength = 2 * lengths.length + (lengths.length + 1);
        for (int length : lengths) {
            totalLength = totalLength + length;
        }

        // Generate a format string for each column
        String[] formats = new String[lengths.length];
        for (int i = 0; i < lengths.length; i++) {
            formats[i] = "| %1$-" + lengths[i] + "s"
                    + (i + 1 == lengths.length ? " |\n" : " ");
        }

        // Print splitter
        for (int i = 0; i < totalLength; i++) {
            System.out.print("-");
        }
        System.out.print("\n");

        // Print header
        for (int i = 0; i < header.length; i++) {
            System.out.printf(formats[i], header[i]);
        }

        // Print splitter
        for (int i = 0; i < totalLength; i++) {
            System.out.print("-");
        }
        System.out.print("\n");

        // Print content
        for (String[] row : content) {
            for (int j = 0; j < row.length; j++) {
                System.out.printf(formats[j], row[j]);
            }
        }

        // Print splitter
        for (int i = 0; i < totalLength; i++) {
            System.out.print("-");
        }
        System.out.print("\n");
    }

    /**
     * Get the sub string arrary
     *
     * @param array Source Array
     * @return Sub String Array
     */
    private String[] getSubArrary(String[] array) {
        String[] result = new String[array.length - 1];
        System.arraycopy(array, 1, result, 0, result.length);
        return result;
    }

    /**
     * Get date string
     *
     * @param date Date
     * @return Date String
     */
    private String getDate(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }

    /**
     * Inject Media Library Service
     *
     * @param mediaLibrary Media Library Service
     */
    @SuppressWarnings("unused")
    public void setMediaLibrary(MediaLibraryService mediaLibrary) {
        this.mediaLibrary = mediaLibrary;
    }
}
