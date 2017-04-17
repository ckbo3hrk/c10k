package io.home.assignment.fibo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.home.assignment.http.FibonacciHttpServer;
import io.home.assignment.Utils;
import io.home.assignment.buffer.Buffers;
import io.home.assignment.http.*;
import org.slf4j.Logger;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.IntFunction;

abstract class AbstractFiboServlet implements HttpServlet {
    private static final Logger LOGGER = Utils.getLogger(FibonacciHttpServer.class);
    private static final int MAX_ALLOWED_UNCACHED_NUMBER = 128;
    private final Cache<Integer, ByteBuffer> values;
    private final Set<Integer> inProcess;
    private final String path;

    AbstractFiboServlet(String path) {
        this.path = path;
        this.values = CacheBuilder.newBuilder().maximumSize(2048).build();
        this.inProcess = new ConcurrentSkipListSet<>();
   }

    protected ByteBuffer evaluate(int num, IntFunction<BigInteger> fibonacciOpEvaluator) throws HttpException {
        ByteBuffer result = values.getIfPresent(num);
        if (result == null) {
            if (isBusyWith(num, inProcess)) {
                throw new HttpException(HttpCode.HTTP_408_TIMEOUT, "Server is busy");
            }
            inProcess.add(num);
            BigInteger fiboNum = fibonacciOpEvaluator.apply(num);
            inProcess.remove(num);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("{} CACHING CALCULATED VALUE FOR {}", getPath(), num);
            }
            result = Buffers.wrap(fiboNum.toString()).asReadOnlyBuffer();
            values.put(num, result);
        }
        return result;
    }

    protected String getPath() {
        return path;
    }

    protected int extractParameter(String url) throws HttpException {
        String strVal = url.substring(getPath().length());
        int value;
        try {
            value = Integer.parseInt(strVal);
        } catch (NumberFormatException e) {
            throw new HttpException(HttpCode.HTTP_400_BAD_REQUEST, "invalid number passed");
        }
        if (value < 0) {
            throw new HttpException(HttpCode.HTTP_400_BAD_REQUEST, "negative numbers aren't supported");
        }
        return value;
    }

    private static boolean isBusyWith(int num, Set<Integer> inProcess) {
        return num > MAX_ALLOWED_UNCACHED_NUMBER && inProcess.contains(num);
    }
}
