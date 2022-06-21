package dailyproject.moon.IO.getty.gettyUDP;

import com.gettyio.core.channel.AioChannel;
import com.gettyio.core.channel.SocketChannel;
import com.gettyio.core.channel.starter.AioServerStarter;
import com.gettyio.core.handler.codec.datagrampacket.DatagramPacketDecoder;
import com.gettyio.core.handler.codec.datagrampacket.DatagramPacketEncoder;
import com.gettyio.core.handler.codec.string.DelimiterFrameDecoder;
import com.gettyio.core.handler.codec.string.StringDecoder;
import com.gettyio.core.pipeline.ChannelInitializer;
import com.gettyio.core.pipeline.DefaultChannelPipeline;
import dailyproject.moon.IO.getty.gettyNormal.SimpleHandler;

public class GettyServer_UDP {

    public static AioServerStarter server;

    public static void initServer()throws Exception{
        server=new AioServerStarter(8989);

        server.socketChannel(SocketChannel.UDP).channelInitializer(new ChannelInitializer() {
            @Override
            public void initChannel(AioChannel aioChannel) throws Exception {
                DefaultChannelPipeline defaultChannelPipeline = aioChannel.getDefaultChannelPipeline();

                defaultChannelPipeline.addLast(new DatagramPacketEncoder());
                defaultChannelPipeline.addLast(new DatagramPacketDecoder());

                //添加自定义的简单消息处理器
                defaultChannelPipeline.addLast(new SimpleHandler_UDP());
            }
        }).start();
    }


    public static void main(String[] args)throws Exception {
        initServer();
    }

}
