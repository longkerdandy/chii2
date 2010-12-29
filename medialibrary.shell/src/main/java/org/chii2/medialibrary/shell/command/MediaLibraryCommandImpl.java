package org.chii2.medialibrary.shell.command;

import org.apache.commons.lang.StringUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.shell.command.MediaLibraryCommand;

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
            mediaLibrary.scan();
        } else {
            System.out.println("Bad parameter for <scan> command, the correct usage is: \"scan movies\".");
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
                } else if (arguments.length == 2) {
                    UUID id = null;
                    try {
                        id = UUID.fromString(arguments[1]);
                    } catch (IllegalArgumentException e) {
                        // Do nothing
                    }
                    // Show single movie by movie id
                    if (id != null) {
                        Movie movie = mediaLibrary.getMovieById(id.toString());
                        printMovieTable(movie);
                    }
                    // Show movies by movie name
                    else {
                        List<? extends Movie> movies = mediaLibrary.getAllMoviesByName(StringUtils.trim(arguments[1]));
                        printMovieTable(movies);
                    }
                } else {
                    // Show movie by movie name with space
                    StringBuffer movieName = new StringBuffer(StringUtils.trim(arguments[1]));
                    for (int i = 2; i < arguments.length; i++) {
                        movieName.append(" ");
                        movieName.append(StringUtils.trim(arguments[i]));
                    }
                    List<? extends Movie> movies = mediaLibrary.getAllMoviesByName(StringUtils.trim(movieName.toString()));
                    printMovieTable(movies);
                }
            }
        } else {
            System.out.println("Bad parameter for <show> command, the correct usage is: \"show movies\".");
        }
    }

    /**
     * Print movie to command line in table format
     *
     * @param movie Movie Object
     */
    private void printMovieTable(Movie movie) {
        if (movie != null) {
            String[] headers = {"ID", "Name", "Year", "Rating", "Codec", "Source", "Format"};
            String[][] content = new String[1][7];
            content[0][0] = StringUtils.defaultString(movie.getId());
            content[0][1] = StringUtils.defaultString(movie.getMovieName());
            content[0][2] = StringUtils.defaultString(movie.getMovieYear());
            content[0][3] = String.valueOf(movie.getMovieRating());
            content[0][4] = StringUtils.defaultString(movie.getMovieCodec());
            content[0][5] = StringUtils.defaultString(movie.getMovieSource());
            content[0][6] = StringUtils.defaultString(movie.getMovieFormat());
            printTable(headers, content);
            System.out.println("Found 1 movie in Chii2 Media Library.");
        } else {
            System.out.println("No movie found in Chii2 Media Library.");
        }
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
     * Inject Media Library Service
     *
     * @param mediaLibrary Media Library Service
     */
    @SuppressWarnings("unused")
    public void setMediaLibrary(MediaLibraryService mediaLibrary) {
        this.mediaLibrary = mediaLibrary;
    }
}
