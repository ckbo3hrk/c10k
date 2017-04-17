package io.home.assignment.buffer;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PoolingByteBufferFactory implements ByteBufferFactory {
    private final static int DEFAULT_BUFFER_SIZE = 2048;
    private final ConcurrentLinkedDeque<ByteBuffer> queue;

    public PoolingByteBufferFactory() {
        this.queue = new ConcurrentLinkedDeque<>();
    }

    @Override
    public ByteBuffer acquire() {
        ByteBuffer buffer = queue.poll();
        if (buffer == null) {
            buffer = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);
        }
        return buffer;
    }

    @Override
    public void release(ByteBuffer buffer) {
        buffer.clear();
        this.queue.offer(buffer);
    }
}
