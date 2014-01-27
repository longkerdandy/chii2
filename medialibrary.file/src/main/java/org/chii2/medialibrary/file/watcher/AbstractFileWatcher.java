package org.chii2.medialibrary.file.watcher;

/**
 * File Watcher
 */
public abstract class AbstractFileWatcher implements Runnable {
    // Flag
    public volatile boolean shouldStop = false;
}
