package io.home.assignment.buffer;

import java.nio.ByteBuffer;

public interface ByteBufferFactory {
    ByteBuffer acquire();
    void release(ByteBuffer buffer);
}
