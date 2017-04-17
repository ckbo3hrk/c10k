package io.home.assignment.http;


public class HttpException extends Exception {
    private final HttpCode httpCode;

    public HttpException(HttpCode httpCode) {
        super();
        this.httpCode = httpCode;
    }

    public HttpException(HttpCode httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }

    public HttpCode getHttpCode() {
        return httpCode;
    }
}
