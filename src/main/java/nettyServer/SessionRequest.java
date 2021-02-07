package nettyServer;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

public class SessionRequest extends ChannelInitializer<SocketChannel> {

    private ChannelInboundHandler handler;

    public SessionRequest(ChannelInboundHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HttpResponseDecoder());
        ch.pipeline().addLast(new HttpRequestEncoder());
        ch.pipeline().addLast(new HttpObjectAggregator(65535));
        ch.pipeline().addLast(handler);
    }
}