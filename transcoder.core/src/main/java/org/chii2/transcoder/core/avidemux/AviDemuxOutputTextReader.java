package org.chii2.transcoder.core.avidemux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * AviDemux Process Output Text Reader
 */
public class AviDemuxOutputTextReader implements Runnable {
    // AviDemux Process
    private AViDemuxProcess process;

    // Logger
    protected Logger logger = LoggerFactory.getLogger("org.chii2.transcoder.avidemux");

    public AviDemuxOutputTextReader(AViDemuxProcess process) {
        this.process = process;
    }

    @Override
    public void run() {
        // Read text output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getProcess().getInputStream()));
        try {
            // Loop
            String line;
            while (!process.stopped && (line = reader.readLine()) != null) {
                if ("Yes or No (Y/y or N/n) :".endsWith(line.trim())) {
                    process.getProcess().getOutputStream().write('Y');
                }
            }
        } catch (IOException e) {
            logger.error("AviDemux output text reading with error: {}", e.getMessage());
        } finally {
            process.finished = true;
            try {
                reader.close();
            } catch (IOException ignore) {
            }
        }
    }
}
