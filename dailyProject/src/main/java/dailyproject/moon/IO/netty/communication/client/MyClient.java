package dailyproject.moon.IO.netty.communication.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-05-20 17:44
 **/

public class MyClient {

    public static void main (String[] args) {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel (SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new MyClientHandler());
                    }
                });

        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect("127.0.0.1", 8989).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete (Future<? super Void> future) throws Exception {
                if (future.isSuccess()){
                    System.out.println("连接成功");
                }
            }
        });

    }
}
