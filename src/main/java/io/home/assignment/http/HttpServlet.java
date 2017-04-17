package io.home.assignment.http;

public interface HttpServlet {
    HttpResponse process(HttpRequest httpRequest) throws HttpException;
}
