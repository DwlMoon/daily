package dailyproject.moon.IO.getty.gettyUDP;

import com.gettyio.core.channel.AioChannel;
import com.gettyio.core.channel.SocketChannel;
import com.gettyio.core.channel.config.AioClientConfig;
import com.gettyio.core.channel.starter.AioClientStarter;
import com.gettyio.core.handler.codec.datagrampacket.DatagramPacketDecoder;
import com.gettyio.core.handler.codec.datagrampacket.DatagramPacketEncoder;
import com.gettyio.core.pipeline.DefaultChannelPipeline;
import dailyproject.moon.IO.getty.gettyNormal.SimpleHandler;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class GettyClient_UDP {

    public static AioClientStarter aioClientStarter;

    public static void initClient()throws Exception{

        AioClientConfig aioConfig = new AioClientConfig();
        //Host与port无需指定也没有关系，指定了也没有效果
        aioConfig.setHost("127.0.0.1");
        aioConfig.setPort(8989);
        aioConfig.setClientChunkSize(512 * 1024 * 1024);
        aioConfig.setBufferWriterQueueSize(2 * 1024 * 1024);


        aioClientStarter=new AioClientStarter(aioConfig);

        aioClientStarter.socketChannel(SocketChannel.UDP).channelInitializer(aioChannel -> {
            DefaultChannelPipeline defaultChannelPipeline = aioChannel.getDefaultChannelPipeline();

            defaultChannelPipeline.addLast(new DatagramPacketEncoder());
            defaultChannelPipeline.addLast(new DatagramPacketDecoder());

            //定义消息解码器
            defaultChannelPipeline.addLast(new SimpleHandler_UDP());
        }).start();
    }


    public static void main(String[] args)throws Exception {
        initClient();

        AioChannel aioChannel = aioClientStarter.getAioChannel();
        String s = "hello getty";
        byte[] msgBody = s.getBytes("utf-8");
        //UDP消息发送的是DatagramPacket 发送的目标服务器在DatagramPacket 中指定
        DatagramPacket datagramPacket = new DatagramPacket(msgBody, msgBody.length, new InetSocketAddress("127.0.0.1", 8989));
        aioChannel.writeAndFlush(datagramPacket);
    }
}
