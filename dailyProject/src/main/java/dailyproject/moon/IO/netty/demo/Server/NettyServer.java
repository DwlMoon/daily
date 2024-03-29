package dailyproject.moon.IO.netty.demo.Server;

import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

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
         * boosGroup和workerGroup含有的NIO线程数量： 默认的CPU核数*2
         *
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

                    //日志
                    .handler(new LoggingHandler(LogLevel.INFO))

                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG,128)

                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE,true)

                    //设置处理器，添加自定义处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        //给 pipeline设置处理器
                        @Override
                        protected void initChannel (SocketChannel socketChannel) throws Exception {
                            /**
                             * 每个用户都对应一个socketChannel，可以将对应的socketChannel储存起来
                             * 在推送消息时，将业务加到channel对用的eventloop对应的TaskQueue中执行
                             * */
                            socketChannel.pipeline()
                                    /**
                                     *  IdleStateHandler 是netty提供的处理空闲状态的处理器
                                     *
                                     *   readerIdleTime, 表示多久没有读操作，会发送一个心跳检测包检测是否连接
                                     *
                                     *   writerIdleTime, 表示多久没有写，会发送一个心跳检测包检测是否连接
                                     *
                                     *   allIdleTime, 表示多长时间既没有读，也没有写
                                     *
                                     *   当有对应的事件触发后，会传递给下一个管道进行处理
                                     *   通过调用下一个handler的 userEventTrigger ，在改方法中进行处理
                                     * */
                                    .addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS))
                                    //加入对空闲检测进一步处理的handler
//                                    .addLast(null)
                                    .addLast(new MyNettyServerHandler());
                        }
                    });

            System.out.println(".....服务器准备好了");

            //绑定一个端口并同步（启动服务器）
            ChannelFuture sync = bootstrap.bind(8989).sync();

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
