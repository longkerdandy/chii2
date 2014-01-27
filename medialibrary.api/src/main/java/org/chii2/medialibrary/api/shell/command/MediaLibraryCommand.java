package org.chii2.medialibrary.api.shell.command;

/**
 * Media Library Shell Command
 */
public interface MediaLibraryCommand {
    /**
     * Command scan, scan all kinds of media and save them in library, this may also trigger different providers
     *
     * @param options Command Options
     */
    public void scan(String[] options);

    /**
     * Command show, show all kinds of media in chii2 media library
     *
     * @param options Command Options
     */
    public void show(String[] options);
}
