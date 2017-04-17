package io.home.assignment.socket;

import io.home.assignment.Utils;
import io.home.assignment.buffer.ByteBufferFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class SocketServer implements Runnable {
    private static final Logger LOGGER = Utils.getLogger(SocketServer.class);

    private final AsynchronousChannelGroup asyncChannelGroup;
    private final AsynchronousServerSocketChannel listener;
    private final ByteBufferFactory byteBufferFactory;

    public SocketServer(int port, int poolSize, ByteBufferFactory byteBufferFactory) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        this.asyncChannelGroup = AsynchronousChannelGroup.withThreadPool(executor);
        this.listener = AsynchronousServerSocketChannel.open(asyncChannelGroup).bind(new InetSocketAddress(port), 1024);
        this.byteBufferFactory = byteBufferFactory;
    }

    protected abstract void onDataReceived(SocketContext context);

    public void sendAndClose(AsynchronousSocketChannel socket, ByteBuffer[] buffers) {
        socket.write(buffers, 0, buffers.length,
                Long.MAX_VALUE, TimeUnit.DAYS, socket, new CompletionHandler<Long, AsynchronousSocketChannel>() {
                    @Override
                    public void completed(Long result, AsynchronousSocketChannel socket) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            LOGGER.error("Unable to close socket", e);
                        }
                        byteBufferFactory.release(buffers[0]);
                    }

                    @Override
                    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                        LOGGER.error("Socket write failed");
                    }
                }
        );
    }

    @Override
    public void run() {
        listener.accept(listener, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, AsynchronousServerSocketChannel attachment) {
                try {
                    socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
                } catch (IOException ignored) {
                    LOGGER.error("Unable to set no-delay");
                }
                attachment.accept(attachment, this);

                ByteBuffer readBuffer = byteBufferFactory.acquire();

                socketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer readed, ByteBuffer byteBuffer) {
                        if (readed == -1) {
                            LOGGER.warn("End of stream");
                            byteBufferFactory.release(byteBuffer);
                        } else {
                            byteBuffer.flip();
                            onDataReceived(new SocketContext(byteBuffer, socketChannel));
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        byteBufferFactory.release(attachment);
                    }
                });
            }

            @Override
            public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
                LOGGER.error("Fail to accept connection");
            }
        });

        try {
            asyncChannelGroup.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            listener.close();
        } catch (IOException e) {
            LOGGER.info("Unable to close AsynchronousServerSocketChannel", e);
        }
    }

}
