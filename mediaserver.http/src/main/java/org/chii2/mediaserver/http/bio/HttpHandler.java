package org.chii2.mediaserver.http.bio;

import org.apache.commons.lang.BooleanUtils;
import org.apache.http.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;
import org.chii2.medialibrary.api.core.MediaLibraryService;
import org.chii2.medialibrary.api.persistence.entity.Image;
import org.chii2.medialibrary.api.persistence.entity.Movie;
import org.chii2.medialibrary.api.persistence.entity.MovieFile;
import org.chii2.mediaserver.api.dlna.DLNATransport;
import org.chii2.mediaserver.api.http.HttpUrl;
import org.chii2.transcoder.api.core.TranscoderProcess;
import org.chii2.transcoder.api.core.TranscoderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Http Request Handler
 */
public class HttpHandler implements HttpRequestHandler {

    private final MediaLibraryService mediaLibrary;
    private final TranscoderService transcoder;
    // Logger
    private static Logger logger = LoggerFactory.getLogger("org.chii2.mediaserver.http");

    public HttpHandler(MediaLibraryService mediaLibrary, TranscoderService transcoder) {
        super();
        this.mediaLibrary = mediaLibrary;
        this.transcoder = transcoder;
    }

    public void handle(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws HttpException, IOException {

        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
        if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported");
        }

        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            byte[] entityContent = EntityUtils.toByteArray(entity);
            logger.info("Incoming entity content (bytes): " + entityContent.length);
        }

        String target = request.getRequestLine().getUri();
        Header range = request.getFirstHeader(DLNATransport.RANGE);
        Header contentFeatures = request.getFirstHeader(DLNATransport.CONTENT_FEATURES_REQUEST);
        Header timeSeek = request.getFirstHeader(DLNATransport.TIME_SEEK_RANGE);
        Header playSpeed = request.getFirstHeader(DLNATransport.PLAY_SPEED);

        logger.debug(String.format("Receive HTTP %s request for target %s", method, target));
        if (range != null) logger.debug("Request with dlna http range header: {}", range.getValue());
        if (contentFeatures != null)
            logger.debug("Request with dlna content features header: {}", contentFeatures.getValue());
        if (timeSeek != null) logger.debug("Request with dlna time seek header: {}", timeSeek.getValue());
        if (playSpeed != null) logger.debug("Request with dlna play speed header: {}", playSpeed.getValue());

        //Parse URL
        Map<String, String> map = HttpUrl.parseURL(target);
        if (map == null) {
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            logger.debug("Chii2 Media Server Http Server requested url parse error.");
        } else {
            String clientProfile = map.get("client");
            String type = map.get("type");
            boolean transcoded = BooleanUtils.toBoolean(map.get("transcoded"), "1", "0");
            String id = map.get("id");

            // Query the library and get the entity
            HttpEntity entity = null;
            if ("image".equalsIgnoreCase(type)) {
                Image image = mediaLibrary.getImageById(id);
                if (image != null) {
                    File imageFile = new File(image.getAbsoluteName());
                    String imageType = image.getType();
                    String mime = transcoder.getImageTranscodedMime(clientProfile, imageType);
                    File file = transcoder.getImageTranscodedFile(clientProfile, imageType, imageFile);
                    entity = new FileEntity(file, mime);
                    response.setStatusCode(HttpStatus.SC_OK);
                }
            } else if ("movie".equalsIgnoreCase(type)) {
                Movie movie = mediaLibrary.getMovieById(id);
                if (movie != null) {
                    if (!transcoded) {
                        String mime = transcoder.getVideoMIME(movie.getFormat(), movie.getVideoFormat(), movie.getVideoFormatProfile(), movie.getVideoFormatVersion(), movie.getVideoCodec());
                        List<File> files = new ArrayList<File>();
                        for (MovieFile movieFile : movie.getFiles()) {
                            files.add(movieFile.getFile());
                        }
                        entity = new RangeFileEntity(files, mime, range);
                        if (range != null) {
                            response.setStatusCode(HttpStatus.SC_PARTIAL_CONTENT);
                        } else {
                            response.setStatusCode(HttpStatus.SC_OK);
                        }
                    } else {
                        String mime = transcoder.getTranscodedMIME(clientProfile, movie);
                        List<TranscoderProcess> processes = transcoder.getTranscodedProcesses(clientProfile, movie);
                        if (range == null) {
                            entity = new TranscodedEntity(processes, mime);
                            response.setStatusCode(HttpStatus.SC_OK);
                        }
                    }
                }
            } else if ("moviethumb".equalsIgnoreCase(type)) {
                byte[] thumb = mediaLibrary.getMovieThumbnailById(id);
                if (thumb != null) {
                    entity = new ByteArrayEntity(thumb);
                    response.setStatusCode(HttpStatus.SC_OK);
                }
            }

            if (entity == null) {
                response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                logger.debug("Chii2 Media Server Http Server requested object not found.");
            } else {
                response.setEntity(entity);
                logger.debug("Chii2 Media Server Http Server serving {} {}.", type, id);
            }
        }
    }
}