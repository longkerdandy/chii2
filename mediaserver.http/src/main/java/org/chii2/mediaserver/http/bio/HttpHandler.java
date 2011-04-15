package org.chii2.mediaserver.http.bio;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
import org.chii2.mediaserver.api.dlna.DLNAProfile;
import org.chii2.mediaserver.api.http.HttpUrl;
import org.chii2.transcoder.api.core.TranscoderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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
        Header range = request.getFirstHeader("RANGE");
        long rangeBegin = getRangeBegin(range);
        long rangeEnd = getRangeEnd(range);
        logger.debug(String.format("Receive HTTP %s request for target %s with range %d-%d", method, target, rangeBegin, rangeEnd));

        //Parse URL
        Map<String, String> map = HttpUrl.parseURL(target);
        if (map == null) {
            response.setStatusCode(400);
            logger.debug("Chii2 Media Server Http Server requested url parse error.");
        } else {
            String clientProfile = map.get("client");
            String type = map.get("type");
            boolean transcoded = BooleanUtils.toBoolean(map.get("transcoded"), "1", "0");
            int seriesNumber = NumberUtils.toInt(map.get("series"), 1);
            DLNAProfile.Profile dlnaProfile = DLNAProfile.getProfileById(map.get("dlna"));
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
                    MovieFile movieFile = movie.getFile(seriesNumber);
                    File file = new File(movieFile.getAbsoluteName());
                    String mime = DLNAProfile.getMimeByProfile(dlnaProfile);
                    if (rangeBegin > 0) {
                        entity = new RangeFileEntity(file, mime, rangeBegin, rangeEnd);
                        response.setStatusCode(HttpStatus.SC_PARTIAL_CONTENT);
                    } else {
                        entity = new FileEntity(file, mime);
                        response.setStatusCode(HttpStatus.SC_OK);
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

    private static long getRangeBegin(Header range) {
        if (range != null) {
            HeaderElement[] elements = range.getElements();
            if (elements != null && elements.length > 0) {
                String rangeValue = elements[0].getValue();
                int index = rangeValue.indexOf("-");
                if (index > 0) {
                    return NumberUtils.toLong(StringUtils.trim(rangeValue.substring(0, index)), -1);
                }
            }
        }
        return -1;
    }

    private static long getRangeEnd(Header range) {
        if (range != null) {
            HeaderElement[] elements = range.getElements();
            if (elements != null && elements.length > 0) {
                String rangeValue = elements[0].getValue();
                int index = rangeValue.indexOf("-");
                if (index > 0 && rangeValue.length() > index + 1) {
                    return NumberUtils.toLong(StringUtils.trim(rangeValue.substring(index + 1)), -1);
                }
            }
        }
        return -1;
    }
}