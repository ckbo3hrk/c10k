package io.home.assignment.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Buffers {
    private final static Charset ENCODER_DECODER = java.nio.charset.StandardCharsets.UTF_8;

    private Buffers() {
    }

    public static ByteBuffer wrap(String val) {
        return ENCODER_DECODER.encode(val);
    }
}
