package io.home.assignment;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.home.assignment.http.FibonacciHttpServer;

public final class HttpServerLauncher {
    public static void main(String ... args) {
        Injector injector = Guice.createInjector(new ServerModule());
        FibonacciHttpServer server = injector.getInstance(FibonacciHttpServer.class);
        server.start();
    }
}
