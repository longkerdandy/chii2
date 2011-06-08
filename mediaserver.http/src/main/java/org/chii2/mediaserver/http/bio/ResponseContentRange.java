package org.chii2.mediaserver.http.bio;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 *  Response Content Range Interceptor
 */
public class ResponseContentRange implements HttpResponseInterceptor {

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (response == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        if (response.containsHeader("Content-Range")) {
            throw new ProtocolException("Content-Range header already present");
        }
        ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
        int status = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        if (ver.compareToVersion(HttpVersion.HTTP_1_1) >= 0 && entity instanceof RangeFileEntity && status == HttpStatus.SC_PARTIAL_CONTENT) {
            RangeFileEntity rangeFileEntity = (RangeFileEntity) entity;
            String range = rangeFileEntity.getContentRange();
            if (range != null) {
                response.addHeader("Content-Range", range);
            }
        } else if (ver.compareToVersion(HttpVersion.HTTP_1_1) >= 0 && entity instanceof RangeTranscodedEntity && status == HttpStatus.SC_PARTIAL_CONTENT) {
            RangeTranscodedEntity rangeTranscodedEntity = (RangeTranscodedEntity) entity;
            String range = rangeTranscodedEntity.getContentRange();
            if (range != null) {
                response.addHeader("Content-Range", range);
            }
        }
    }
}
