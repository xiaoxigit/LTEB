package nettyServer;

import entity.DeliverySessionCreationType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.codec.Charsets;
import utils.HttpUtils;

import java.net.URI;
import java.util.Map;

@ChannelHandler.Sharable
public class MainHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private String msg;
    private String url;
    private byte[] body;

    public MainHandler( byte[] body, String msg, String url) {
        this.url = url;
        this.body = body;
        this.msg = msg;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HttpRequest request = null;
        if (msg.equals("GET"))
            System.out.println("send quest");
            request = HttpUtils.createReqGet("localhost", new URI(url), "text/json;charset=utf-8");
        if (msg.equals("POST"))
            request = HttpUtils.createReqPost(body, "localhost", new URI("url"), "text/xml;charset=utf-8");
        // 发送
        ctx.channel().writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
            }
        });

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        System.out.println("read start");
        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            // 字符数组
            ByteBuf buf = httpContent.content();
            // 返回
            String response = buf.toString(Charsets.UTF_8);
            System.out.println("http read is end");
            ctx.close();
        }
        System.out.println("read is end");
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}