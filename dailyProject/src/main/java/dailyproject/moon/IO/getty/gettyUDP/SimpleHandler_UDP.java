package dailyproject.moon.IO.getty.gettyUDP;

import com.gettyio.core.channel.AioChannel;
import com.gettyio.core.channel.group.DefaultChannelGroup;
import com.gettyio.core.pipeline.in.SimpleChannelInboundHandler;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class SimpleHandler_UDP extends SimpleChannelInboundHandler<DatagramPacket> {

    //实例化一个group保存客户端连接
    private DefaultChannelGroup defaultChannelGroup = new DefaultChannelGroup();

    @Override
    public void channelAdded(AioChannel aioChannel) throws Exception {
        System.out.println("连接过来了");

        defaultChannelGroup.add(aioChannel);
        //可以通过AioChannel的channelId获取通道。比如与用户映射起来
        AioChannel tempChannel = defaultChannelGroup.find(aioChannel.getChannelId());

        String s = "你好";
        byte[] bytes = s.getBytes();
        tempChannel.writeAndFlush(bytes);

    }

    @Override
    public void channelClosed(AioChannel aioChannel) throws Exception {
        super.channelClosed(aioChannel);
        System.out.println("连接关闭了");
    }

    @Override
    public void exceptionCaught(AioChannel aioChannel, Throwable cause) throws Exception {
        super.exceptionCaught(aioChannel, cause);
        System.out.println("出错了");
    }

    @Override
    public void channelRead0(AioChannel aioChannel, DatagramPacket datagramPacket) throws Exception {
        System.out.println("读取消息了:" + new String(datagramPacket.getData()));
        System.out.println("客户端地址:" + datagramPacket.getAddress().getHostName() + ":" + datagramPacket.getPort());
    }
}
