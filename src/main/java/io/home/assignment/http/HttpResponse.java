package io.home.assignment.http;

import io.home.assignment.buffer.Buffers;
import java.nio.ByteBuffer;

public final class HttpResponse {
    private final int statusCode;
    private final String status;
    private final ByteBuffer body;

    public static HttpResponse create(HttpCode httpCode, String message) {
        if (message == null) {
            return create(httpCode);
        }
        return new HttpResponse(httpCode.getCode(), httpCode.getStatus(), Buffers.wrap(message));
    }

    public static HttpResponse create(HttpCode httpCode, ByteBuffer attachment) {
        return new HttpResponse(httpCode.getCode(), httpCode.getStatus(), attachment);
    }

    public static HttpResponse create(HttpCode httpCode) {
        return new HttpResponse(httpCode.getCode(), httpCode.getStatus(), null);
    }

    private HttpResponse(int statusCode, String status, ByteBuffer body) {
        this.statusCode = statusCode;
        this.status = status;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ByteBuffer getBody() {
        return body;
    }

    public String getStatus() {
        return status;
    }
}
