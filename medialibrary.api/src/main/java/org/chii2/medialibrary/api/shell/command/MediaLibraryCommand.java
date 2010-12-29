package org.chii2.medialibrary.api.shell.command;

import java.util.List;

/**
 * Media Library Shell Command
 */
public interface MediaLibraryCommand {
    /**
     * Command scan, scan all kinds of media and save them in library, this may also trigger information update from providers
     *
     * @param media Media type, currently supported type are: <movies>
     */
    public void scan(String media);

    /**
     * Command show, show all kinds of media in chii2 media library
     *
     * @param arguments Show command arguments, currently support arguments are: <movies>
     */
    public void show(String[] arguments);
}
