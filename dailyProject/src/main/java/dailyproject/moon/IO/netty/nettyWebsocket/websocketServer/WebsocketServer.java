package dailyproject.moon.IO.netty.nettyWebsocket.websocketServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-06-07 15:18
 **/

public class WebsocketServer {

    public static void main (String[] args) {

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
                            socketChannel.pipeline()

                                    // 因为是基于http协议，所以使用http编解码器
                                    .addLast(new HttpServerCodec())

                                    //是以块方式写的，添加chunkedwrite处理区
                                    .addLast(new ChunkedWriteHandler())

                                    /**因为http的数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合起来
                                     *
                                     * 这就是为什么当浏览器发送大量请求时会用多个http请求分段发送
                                    */
                                    .addLast(new HttpObjectAggregator(8192))

                                    /**
                                     * 1、对于websocket，它的数据是以帧的形势传递的
                                     * 2、浏览器请求时 ws：localhost：8989/hello , 请求uri
                                     * 3、 WebSocketServerProtocolHandler 核心功能：
                                     *      将http协议升级为ws协议，保持长链接（通过一个状态码101）
                                     * */
                                    .addLast(new WebSocketServerProtocolHandler("/hello"))

                                    //自定义handler，处理业务逻辑
                                    .addLast(new MyWebsocketHandler());
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
