package dailyproject.moon.IO.netty.Server;

import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @program: daily_test
 * @description: netty客户端
 * @create: 2021-04-20 17:27
 **/

public class NettyServer {

    public static void main (String[] args)throws Exception {

        //创建BossGroup 和 workerGroup
        /**
         * 创建两个线程组 （两个都是无限循环）
         * boosGroup 只接受连接请求
         * workerGroup 负责业务处理
         * */
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();


            bootstrap
                    //设置两个线程组
                    .group(boosGroup,workerGroup)

                    //使用NioServerSocketChannel作为boosGroup 通道
                    .channel(NioServerSocketChannel.class)

                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG,128)

                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE,true)

                    //设置处理器，添加自定义处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        //给 pipeline设置处理器
                        @Override
                        protected void initChannel (SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new MyNettyServerHandler());
                        }
                    });

            System.out.println(".....服务器准备好了");

            //绑定一个端口并同步（启动服务器）
            ChannelFuture sync = bootstrap.bind(8989).sync();

            //对关闭通道进行监听(关闭通道时间发生以后才进行关闭并且进行监听)
            sync.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
