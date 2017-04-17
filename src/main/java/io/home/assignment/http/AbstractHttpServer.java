package io.home.assignment.http;

import com.google.common.collect.ImmutableMap;
import io.home.assignment.Utils;
import io.home.assignment.buffer.ByteBufferFactory;
import io.home.assignment.socket.SocketContext;
import io.home.assignment.socket.SocketServer;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

abstract class AbstractHttpServer extends SocketServer {
    private static final Logger LOGGER = Utils.getLogger(AbstractHttpServer.class);
    private final Map<String, HttpServlet> servlets;
    protected final int poolSize;

    public AbstractHttpServer(Map<String, HttpServlet> mapping, int port, int poolSize, ByteBufferFactory byteBufferFactory) throws IOException {
        super(port, poolSize, byteBufferFactory);
        this.servlets = ImmutableMap.copyOf(mapping);
        this.poolSize = poolSize;

    }

    @Override
    public void onDataReceived(SocketContext context) {
        try {
            HttpRequest httpRequest = HttpMessages.parseHttpRequest(context.getBuffer());
            HttpResponse resp = dispatchRequest(httpRequest);
            sendResponse(context, resp);
        } catch (HttpException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("Unexpected error", e.getMessage());
            }
            sendResponse(context, HttpResponse.create(e.getHttpCode(), e.getMessage()));
        }
    }

    abstract public void start();

    private HttpResponse dispatchRequest(HttpRequest httpRequest) throws HttpException {
        for (Map.Entry<String, HttpServlet> entry : servlets.entrySet()) {
            if (httpRequest.getUrl().startsWith(entry.getKey())) {
                return entry.getValue().process(httpRequest);
            }
        }
        throw new HttpException(HttpCode.HTTP_404_NOT_FOUND, httpRequest.getUrl() + " not found");
    }

    private void sendResponse(SocketContext context, HttpResponse httpResponse) {
        ByteBuffer[] respBuf = HttpMessages.wrapHttpResponse(context.getBuffer(), httpResponse);
        sendAndClose(context.getSocket(), respBuf);
    }
}
