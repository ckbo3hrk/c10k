package io.home.assignment.socket;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

final public class SocketContext {
    private final ByteBuffer buffer;
    private final AsynchronousSocketChannel socket;

    public SocketContext(ByteBuffer buffer, AsynchronousSocketChannel socket) {
        this.buffer = buffer;
        this.socket = socket;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public AsynchronousSocketChannel getSocket() {
        return socket;
    }
}
