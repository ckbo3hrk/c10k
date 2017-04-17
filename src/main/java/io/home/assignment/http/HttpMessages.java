package io.home.assignment.http;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import io.home.assignment.buffer.ByteBufferBuilder;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;

final class HttpMessages {
    private final static Charset DECODER = java.nio.charset.StandardCharsets.UTF_8;
    private final static Splitter LINE_SPLITTER = Splitter.on("/r/n").omitEmptyStrings();
    private final static Splitter WHITESPACE_SPLITTER = Splitter.on(' ').trimResults();

    private HttpMessages() {
    }

    public static HttpRequest parseHttpRequest(ByteBuffer byteBuf) throws HttpException {
        Preconditions.checkNotNull(byteBuf);
        CharBuffer charBuffer = DECODER.decode(byteBuf);
        String body = new String(charBuffer.array(), 0, byteBuf.limit()).trim();
        Iterable<String> lines = LINE_SPLITTER.split(body);
        Iterator<String> lineIt = lines.iterator();
        if (!lineIt.hasNext()) {
            throw new HttpParsingException("Empty request");
        }
        String headLine = lineIt.next();
        String url = extractUrl(headLine);
        return new HttpRequest(url);
    }

    public static ByteBuffer[] wrapHttpResponse(ByteBuffer byteBufferHeader, HttpResponse httpResponse) {
        byteBufferHeader.clear();
        ByteBufferBuilder builder = new ByteBufferBuilder(byteBufferHeader);
        builder.add("HTTP/1.1 ")
                .add(String.valueOf(httpResponse.getStatusCode()))
                .add(" ")
                .add(httpResponse.getStatus())
                .add("\r\nConnection: close\r\n\r\n");
        if (httpResponse.getBody() != null) {
            builder.add(httpResponse.getBody());
        }
        return builder.build();
    }

    private static String extractUrl(String line) throws HttpException {
        Iterator<String> partsIt = WHITESPACE_SPLITTER.split(line).iterator();
        if (!partsIt.hasNext()) {
            throw new HttpParsingException("Empty head");
        }
        String method = partsIt.next();
        if (!"GET".equals(method)) {
            throw new HttpParsingException("Unsupported method " + method);
        }
        String url = partsIt.next();
        if (Strings.isNullOrEmpty(url)) {
            throw new HttpParsingException("Url not specified");
        }
        return url;
    }
}
