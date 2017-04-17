package io.home.assignment.buffer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


import java.nio.ByteBuffer;
import java.util.List;

public class ByteBufferBuilder {
    private final List<ByteBuffer> buffers;
    private ByteBuffer current;

    public ByteBufferBuilder(ByteBuffer current) {
        this.buffers = Lists.newArrayList();
        this.current = current;
    }

    public ByteBufferBuilder add(ByteBuffer val) {
        if (current.remaining() >= val.limit()) {
            current.put(val.asReadOnlyBuffer());
        } else {
            current.flip();
            buffers.add(current);
            current = ByteBuffer.allocate(current.capacity());
            if (current.capacity() > val.capacity()) {
                current.put(val);
            } else {
                buffers.add(val.asReadOnlyBuffer());
            }
        }
        return this;
    }

    public ByteBufferBuilder add(String val) {
        return add(Buffers.wrap(val));
    }

    public ByteBuffer[] build() {
        Preconditions.checkNotNull(current);
        current.flip();
        buffers.add(current);
        current = null;
        return buffers.toArray(new ByteBuffer[0]);
    }
}
