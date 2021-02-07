package utils;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import org.apache.commons.codec.Charsets;

import java.net.URI;

public class HttpUtils {

    /**
     * 构造HTTP请求
     * @return
     * @throws Exception
     */
    public static HttpRequest createReqGet(String server, URI uri,String contentType) throws Exception{
        String req = "";
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                uri.toASCIIString(), Unpooled.wrappedBuffer(req.getBytes(Charsets.UTF_8)));
        // 构建HTTP请求
        request.headers().set(HttpHeaders.Names.HOST, server);
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set("accept-type", Charsets.UTF_8);
        request.headers().set(HttpHeaders.Names.CONTENT_TYPE, contentType);
        return request;
    }

    /**
     *
     * @param body
     * @param server
     * @param uri
     * @param contentType
     * @return
     * @throws Exception
     */
    public static HttpRequest createReqPost(byte [] body, String server, URI uri ,String contentType) throws Exception{

        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(), Unpooled.wrappedBuffer(body));
        // 构建HTTP请求
        request.headers().set(HttpHeaders.Names.HOST, server);
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set("accept-type", Charsets.UTF_8);
        request.headers().set(HttpHeaders.Names.CONTENT_TYPE, contentType);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
        // 返回
        return request;
    }
}

