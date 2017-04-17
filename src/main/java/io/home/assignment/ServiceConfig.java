package io.home.assignment;

public final class ServiceConfig {
    private final int port;
    private final int threads;

    public ServiceConfig(int port, int threads) {
        this.port = port;
        this.threads = threads;
    }

    public int getPort() {
        return port;
    }

    public int getThreads() {
        return threads;
    }
}
