package org.chii2.medialibrary.shell.command;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.chii2.medialibrary.shell.command.parser.OptException;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

/**
 * Media Library Command Test
 */
public class MediaLibraryCommandTest {
    // Mock Command
    MediaLibraryCommandImpl command = new MediaLibraryCommandImpl() {
        @SuppressWarnings("unused")
        private Logger logger = mock(Logger.class);
    };

    @Test
    public void ScanCommandTest() throws OptException, ParseException {
        CommandLine line = this.command.parser.parse(this.command.scanOptions, new String[]{"-movie"});
        assert line.hasOption("movie");
        assert !line.hasOption("image");
    }

    @Test
    public void ShowCommandTest() throws OptException, ParseException {
        CommandLine line = this.command.parser.parse(this.command.showOptions, new String[]{"-image", "-id", "550e8400-e29b-41d4-a716-446655440000", "-name", "The Rock"});
        assert line.hasOption("image");
        assert line.hasOption("id");
        assert line.getOptionValue("id").equals("550e8400-e29b-41d4-a716-446655440000");
        assert line.hasOption("name");
        assert line.getOptionValue("name").equals("The Rock");
    }
}
