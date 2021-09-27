package dailyproject.moon.IO.netty.ChatCToC.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @program: daily_test
 * @description: 单聊服务端
 * @create: 2021-05-26 16:31
 **/

public class CGroupChatServer {

    private int port;

    public CGroupChatServer (int port) {
        this.port = port;
    }

    //编写run方法，处理客户端请求
    public void run(){
        NioEventLoopGroup boss=null;
        NioEventLoopGroup worker=null;
        //创建两个线程组
        try {
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel (SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    //向pipeline加入解码器
                                    .addLast("decoder",new StringDecoder())
                                    //向pipeline中加入编码器
                                    .addLast("encoder",new StringEncoder())
                                    .addLast(new CGroupChatServerHandler())
                            ;
                        }
                    });

            System.out.println("netty 服务器已经启动");
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete (Future<? super Void> future) throws Exception {
                    if (future.isSuccess()){
                        System.out.println("连接成功");
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main (String[] args) {
        new CGroupChatServer(8989).run();
    }


}
