package dailyproject.moon.IO.netty.communication.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-05-20 17:57
 **/

public class MyServer {
    public static void main (String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel (SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new MyNettyServerHandler());
                    }
                });


        //绑定一个端口并同步（启动服务器）
        ChannelFuture sync = null;
        try {
            sync = bootstrap.bind(8989).sync();

            //给 ChannelFuture 注册监听器，监控我们关心的事件
            sync.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete (ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()){
                        System.out.println("监听端口 8989 成功");
                    }else {
                        System.out.println("监听端口 8989 失败");
                    }
                }
            });
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //对关闭通道进行监听(关闭通道时间发生以后才进行关闭并且进行监听)



    }
}
