package dailyproject.moon.IO.netty.UDP;

import io.lettuce.core.protocol.CommandEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.channels.MulticastChannel;
import java.util.List;

public class UdpServer {

    public static void startServer()throws Exception{
        NioEventLoopGroup boss = new NioEventLoopGroup();

        Bootstrap serverBootstrap = new Bootstrap();

        serverBootstrap
                .group(boss)
                .channel(NioDatagramChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(
                                new MessageToMessageDecoder<DatagramPacket>() {
                                    @Override
                                    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
                                        String s = datagramPacket.content().toString(CharsetUtil.UTF_8);
                                        System.out.println("信息为："+s);
                                    }
                                }
                        );

                        MulticastChannel multicastChannel = (MulticastChannel)channel;
                        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                                InetAddress.getByName("127.0.0.1"));
                        multicastChannel.join(InetAddress.getByName("232.0.0.3"),networkInterface,InetAddress.getByName("172.20.0.77"));

                    }
                });

        serverBootstrap.bind(8989).sync().addListener(future -> {
            if (future.isSuccess()){
                System.out.println("连接成功");
            }
        });

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
