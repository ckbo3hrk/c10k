package io.home.assignment.http;

import com.google.common.base.MoreObjects;

public class HttpRequest {
    private final String url;

    public HttpRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .toString();
    }
}
