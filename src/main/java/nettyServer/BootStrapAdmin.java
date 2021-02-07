package nettyServer;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class BootStrapAdmin {

    private static final EventLoopGroup WORKER_GROUP = new NioEventLoopGroup();
    private static Bootstrap CLIENT_BOOTSTRAP ;

    public void start(){
        // 初始化
        initBootStrap();
        SessionRequest request = new SessionRequest(null);
        sendRequest(request);
    }
    public void stop(){
        WORKER_GROUP.shutdownGracefully();
    }

    public ChannelFuture sendRequest(SessionRequest request){
        CLIENT_BOOTSTRAP.handler(request);
        ChannelFuture f = CLIENT_BOOTSTRAP.connect("localhost", 8080);
        return f;
    }

    public Bootstrap initBootStrap() {
        if(CLIENT_BOOTSTRAP == null) {
            synchronized (BootStrapAdmin.class) {
                if(CLIENT_BOOTSTRAP == null) {
                    CLIENT_BOOTSTRAP = newBootStrap();
                }
            }
        }
        return CLIENT_BOOTSTRAP;
    }

    private static Bootstrap newBootStrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(WORKER_GROUP);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, false);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 200);
        return bootstrap;
    }
}



