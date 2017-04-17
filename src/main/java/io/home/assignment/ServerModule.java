package io.home.assignment;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.home.assignment.buffer.ByteBufferFactory;
import io.home.assignment.buffer.PoolingByteBufferFactory;

import java.io.File;

final class ServerModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(ByteBufferFactory.class).to(PoolingByteBufferFactory.class);
    }

    @Provides
    ServiceConfig config() {
        Config config = ConfigFactory.parseFile(new File("config.json"));
        int port = config.getInt("config.port");
        int threads = config.getInt("config.threads");
        return new ServiceConfig(port, threads);
    }
}
