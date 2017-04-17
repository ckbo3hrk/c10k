package io.home.assignment.http;

public enum HttpCode {
    HTTP_200_OK(200, "OK"),
    HTTP_400_BAD_REQUEST(400, "Bad Request"),
    HTTP_408_TIMEOUT(408, "Request Timeout"),
    HTTP_404_NOT_FOUND(404, "Not Found"),
    HTTP_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String status;

    HttpCode(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
