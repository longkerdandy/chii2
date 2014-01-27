package org.chii2.medialibrary.shell.command;

import org.apache.commons.cli.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.shell.command.MediaLibraryCommand;
import org.chii2.medialibrary.shell.command.parser.OptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Media Library Shell Command
 */
public class MediaLibraryCommandImpl implements MediaLibraryCommand {
    // Injected Media Library Service
    private MediaLibraryService mediaLibrary;
    // Command Line Parser
    protected final CommandLineParser parser;
    // Scan Command
    protected final Options scanOptions;
    // Scan Command
    protected final Options showOptions;
    // Help
    protected final HelpFormatter helpFormatter;
    // Logger
    private final Logger logger = LoggerFactory.getLogger("org.chii2.medialibrary.shell.command");

    public MediaLibraryCommandImpl() {
        this.parser = new GnuParser();
        this.scanOptions = new Options();
        this.scanOptions.addOption("movie", false, "type option: movie.  scan movie from directories");
        this.scanOptions.addOption("image", false, "type option: image.  scan image from directories");
        this.scanOptions.addOption("all", false, "type option: all.  scan all support media from directories");
        this.scanOptions.addOption("help", false, "show this message");
        this.showOptions = new Options();
        this.showOptions.addOption("movie", false, "type option: movie.  show movies in the media library");
        this.showOptions.addOption("image", false, "type option: image.  show images in the media library");
        this.showOptions.addOption("help", false, "show this message");
        this.showOptions.addOption(OptionBuilder.withArgName("uuid")
                .hasArg()
                .withDescription("restrict option: uuid.  show media with specific uuid \n CAN NOT BE USED WITH OTHER RESTRICT OPTION")
                .create("id"));
        this.showOptions.addOption(OptionBuilder.withArgName("name")
                .hasArg()
                .withDescription("restrict option: name.  show media which name contains specific string")
                .create("name"));
        this.helpFormatter = new HelpFormatter();
    }

    /**
     * Life Cycle Init
     */
    @SuppressWarnings("unused")
    public void init() {
        logger.debug("Chii2 Media Library Shell Command init.");
    }

    /**
     * Life Cycle Destroy
     */
    @SuppressWarnings("unused")
    public void destroy() {
        logger.debug("Chii2 Media Library Shell Command destroy.");
    }

    @Override
    public void scan(String[] options) {
        try {
            CommandLine line = this.parser.parse(this.scanOptions, options);
            if (line.getOptions().length == 1) {
                if (line.hasOption("movie")) {
                    this.mediaLibrary.scanMovies();
                } else if (line.hasOption("image")) {
                    this.mediaLibrary.scanImages();
                } else if (line.hasOption("all")) {
                    this.mediaLibrary.scanAll();
                } else if (line.hasOption("help")) {
                    this.helpFormatter.printHelp("scan -type_option", this.scanOptions);
                } else {
                    throw new OptException("Invalid option count.");
                }
            } else {
                throw new OptException("Invalid option count.");
            }
        } catch (Exception e) {
            System.out.println(String.format("%s, use 'scan -help' for more information.", ExceptionUtils.getMessage(e)));
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void show(String[] options) {
        try {
            CommandLine line = this.parser.parse(this.showOptions, options);
            if (line.getOptions().length == 1) {
                if (line.hasOption("movie")) {
                    List<? extends Movie> movies = this.mediaLibrary.getMovies();
                    this.printMovieTable(movies);
                } else if (line.hasOption("image")) {
                    List<? extends Image> images = this.mediaLibrary.getImages();
                    this.printImageTable(images);
                } else if (line.hasOption("help")) {
                    this.helpFormatter.printHelp("show -type_option [-restrict_option]", this.showOptions);
                } else {
                    throw new OptException("Invalid option count.");
                }
            } else if (line.getOptions().length == 2) {
                if (line.hasOption("movie") && line.hasOption("id") && StringUtils.isNotEmpty(line.getOptionValue("id"))) {
                    Movie movie = this.mediaLibrary.getMovieById(line.getOptionValue("id"));
                    this.printMovieTable(movie);
                } else if (line.hasOption("movie") && line.hasOption("name") && StringUtils.isNotEmpty(line.getOptionValue("name"))) {
                    List<? extends Movie> movies = this.mediaLibrary.getMoviesByName(line.getOptionValue("name"));
                    this.printMovieTable(movies);
                } else if (line.hasOption("image") && line.hasOption("id") && StringUtils.isNotEmpty(line.getOptionValue("id"))) {
                    Image image = this.mediaLibrary.getImageById(line.getOptionValue("id"));
                    this.printImageTable(image);
                } else if (line.hasOption("image") && line.hasOption("name") && StringUtils.isNotEmpty(line.getOptionValue("name"))) {
                    List<? extends Image> images = mediaLibrary.getImagesByField("title", line.getOptionValue("name"), false);
                    this.printImageTable(images);
                } else {
                    throw new OptException("Invalid option count.");
                }
            } else {
                throw new OptException("Invalid option count.");
            }
        } catch (Exception e) {
            System.out.println(String.format("%s, use 'show -help' for more information.", ExceptionUtils.getMessage(e)));
        }
    }

    /**
     * Print movie to command line in table format
     *
     * @param movie Movie Object
     */
    private void printMovieTable(Movie movie) {
        List<Movie> movieList = new ArrayList<>();
        if (movie != null) {
            movieList.add(movie);
        }
        printMovieTable(movieList);
    }

    /**
     * Print movie list to command line in table format
     *
     * @param movies Movie List
     */
    private void printMovieTable(List<? extends Movie> movies) {
        if (movies != null && !movies.isEmpty()) {
            String[] headers = {"ID", "Name", "Year", "Files", "Info", "Rating", "Codec", "Format"};
            String[][] content = new String[movies.size()][8];
            for (int i = 0; i < movies.size(); i++) {
                content[i][0] = StringUtils.defaultString(movies.get(i).getId());
                content[i][1] = StringUtils.defaultString(movies.get(i).getTitle());
                Date releasedDate = movies.get(i).getReleasedDate();
                if (releasedDate != null) {
                    content[i][2] = StringUtils.defaultString((new SimpleDateFormat("yyyy")).format(releasedDate));
                } else {
                    content[i][2] = "";
                }
                content[i][3] = Integer.toString(movies.get(i).getFilesCount());
                content[i][4] = Integer.toString(movies.get(i).getInfoCount());
                content[i][5] = Double.toString(movies.get(i).getRating());
                content[i][6] = StringUtils.defaultString(movies.get(i).getVideoCodec()) + " " + StringUtils.defaultString(movies.get(i).getAudioCodec());
                content[i][7] = StringUtils.defaultString(movies.get(i).getFormat());
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
        List<Image> imageList = new ArrayList<>();
        if (image != null) {
            imageList.add(image);
        }
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
                content[i][1] = StringUtils.defaultString(images.get(i).getAbsolutePath());
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
