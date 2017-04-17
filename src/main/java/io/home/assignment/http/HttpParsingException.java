package io.home.assignment.http;

class HttpParsingException extends HttpException {
    public HttpParsingException(String message) {
        super(HttpCode.HTTP_500_INTERNAL_SERVER_ERROR, message);
    }
}
