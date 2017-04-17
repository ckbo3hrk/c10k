package io.home.assignment.http;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import io.home.assignment.ServiceConfig;
import io.home.assignment.Utils;
import io.home.assignment.buffer.ByteBufferFactory;
import io.home.assignment.fibo.FiboEvenSumServlet;
import io.home.assignment.fibo.FiboNthServlet;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;

public class FibonacciHttpServer extends AbstractHttpServer {
    private static final Logger LOGGER = Utils.getLogger(FibonacciHttpServer.class);
    private static final String FIB_NUMBER_PATH = "/fib/number/";
    private static final String FIB_EVEN_SUM_PATH = "/fib/evensum/";
    private static final Map<String, HttpServlet> servlets = ImmutableMap.of(FIB_NUMBER_PATH, new FiboNthServlet(FIB_NUMBER_PATH),
            FIB_EVEN_SUM_PATH, new FiboEvenSumServlet(FIB_EVEN_SUM_PATH));

    @Inject
    public FibonacciHttpServer(ServiceConfig config, ByteBufferFactory byteBufferFactory) throws IOException {
        super(servlets, config.getPort(), config.getThreads(), byteBufferFactory);
    }

    @Override
    public void start() {
        LOGGER.info("STARTING FIBONACCI SERVER WITH {} THREADS", poolSize);
        new Thread(this).start();
    }
}
