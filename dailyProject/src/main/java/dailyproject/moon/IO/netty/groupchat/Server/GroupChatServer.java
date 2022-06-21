package dailyproject.moon.IO.netty.groupchat.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
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

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-05-26 16:31
 **/

public class GroupChatServer {

    static HashMap<String,ChannelFuture> map=new HashMap<>();

    static ServerBootstrap bootstrap=null;

    //编写run方法，处理客户端请求
    public void run(){
        NioEventLoopGroup boss=null;
        NioEventLoopGroup worker=null;
        //创建两个线程组
        try {
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();

            bootstrap = new ServerBootstrap();
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
                                    .addLast(new GroupChatServerHandler())
                            ;
                        }
                    });

            System.out.println("netty 服务器已经启动");

            //同步阻塞等待(
            // 该语句能让线程进入wait状态，也就是main线程暂时不会执行到finally里面，
            // nettyserver也持续运行，如果监听到关闭事件，可以优雅的关闭通道和nettyserver
            // )
            bootstrap.bind().channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static   GenericFutureListener<Future<? super Void>>  genericFutureListener (){
        return new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete (Future<? super Void> future) throws Exception {
                if (future.isSuccess()){
                    System.out.println("连接成功");
                }
            }
        };
    }

    public void shutChannel(){
        map.get("1").channel().close();
    }

    public static void main (String[] args)throws Exception {
        new GroupChatServer().run();
        //绑定多个端口并加入到集合中

        map.put("1",
                bootstrap.bind(new InetSocketAddress("172.20.0.216",8989)).sync()
                        .addListener(genericFutureListener()));
        map.put("2",
                bootstrap.bind(new InetSocketAddress("172.20.0.216",9090)).sync()
                        .addListener(genericFutureListener()));
    }


}
