package dailyproject.moon.IO.netty.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @program: daily_test
 * @description: netty客户端
 * @create: 2021-04-21 11:12
 **/

public class NettyClient {
    public static void main (String[] args)throws Exception {
        //客户端需要一个时间循环组
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        //创建客户端启动对象
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap
                    //设置线程组
                    .group(eventExecutors)

                    //设置客户端通道类的反射
                    .channel(NioSocketChannel.class)

                    //设置处理器，添加自定义处理器
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel (SocketChannel socketChannel) throws Exception {
                            //加入自己的处理器
                            socketChannel.pipeline().addLast(new MyNettyClientHandler());
                        }
                    });

            System.out.println("client is ready");

            //启动客户端去连接服务器端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8989).sync();

            //给关闭通道进行监听(关闭通道时间发生以后才进行关闭并且进行监听)
            channelFuture.channel().closeFuture().sync();

        } finally {
            eventExecutors.shutdownGracefully();
        }

    }
}
