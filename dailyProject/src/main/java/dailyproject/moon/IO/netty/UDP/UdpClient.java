package dailyproject.moon.IO.netty.UDP;

import io.lettuce.core.protocol.CommandEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

public class UdpClient {

    public static void startServer()throws Exception{
        NioEventLoopGroup boss = new NioEventLoopGroup();

        Bootstrap serverBootstrap = new Bootstrap();

        serverBootstrap.group(boss)
                .channel(NioDatagramChannel.class)
                .handler(new CommandEncoder())    ;
//                .handler(new ChannelInitializer<DatagramChannel>() {
//                    @Override
//                    protected void initChannel(DatagramChannel datagramChannel) throws Exception {
//                        datagramChannel.pipeline().addLast(new MessageToMessageDecoder<DatagramPacket>() {
//                            @Override
//                            protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
//                                ByteBuf data = datagramPacket.content().readBytes(datagramPacket.content().readableBytes());
//                                System.out.println("收到实时数据 | "+ByteBufUtil.hexDump(data));
//
//                            }
//                        });
//                    }
//                });


        Channel channel = serverBootstrap.bind(0).sync().channel();
        channel.writeAndFlush
                (new DatagramPacket(Unpooled.copiedBuffer("你好",CharsetUtil.UTF_8),
                        new InetSocketAddress(
                                "232.0.0.1",8989)));

    }


    public static void main(String[] args) {
        try {
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
