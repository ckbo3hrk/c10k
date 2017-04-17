package io.home.assignment.fibo;

import io.home.assignment.http.HttpCode;
import io.home.assignment.http.HttpException;
import io.home.assignment.http.HttpRequest;
import io.home.assignment.http.HttpResponse;

import java.nio.ByteBuffer;

public final class FiboEvenSumServlet extends AbstractFiboServlet {

    public FiboEvenSumServlet(String path) {
        super(path);
    }

    @Override
    public HttpResponse process(HttpRequest httpRequest) throws HttpException {
        int num = extractParameter(httpRequest.getUrl());
        num = num / 3; //do the magic
        ByteBuffer result = evaluate(num, Fibonacci::getNthEvenSum);
        return HttpResponse.create(HttpCode.HTTP_200_OK, result);
    }
}
