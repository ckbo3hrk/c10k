package io.home.assignment.fibo;

import io.home.assignment.http.HttpCode;
import io.home.assignment.http.HttpException;
import io.home.assignment.http.HttpRequest;
import io.home.assignment.http.HttpResponse;

import java.nio.ByteBuffer;

public final class FiboNthServlet extends AbstractFiboServlet {

    public FiboNthServlet(String path) {
        super(path);
    }

    @Override
    public HttpResponse process(HttpRequest httpRequest) throws HttpException {
        int num = extractParameter(httpRequest.getUrl());
        ByteBuffer result = evaluate(num, Fibonacci::getNth);
        return HttpResponse.create(HttpCode.HTTP_200_OK, result);
    }
}
