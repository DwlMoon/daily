package dailyproject.moon.IO.getty.gettyNormal;

import com.gettyio.core.channel.starter.AioClientStarter;
import com.gettyio.core.handler.codec.datagrampacket.DatagramPacketDecoder;
import com.gettyio.core.handler.codec.datagrampacket.DatagramPacketEncoder;
import com.gettyio.core.handler.codec.string.DelimiterFrameDecoder;
import com.gettyio.core.handler.codec.string.StringDecoder;
import com.gettyio.core.pipeline.DefaultChannelPipeline;
import dailyproject.moon.IO.getty.gettyNormal.SimpleHandler;

public class GettyClient {

    public static AioClientStarter aioClientStarter;

    public static void initClient()throws Exception{
        aioClientStarter=new AioClientStarter("127.0.0.1",8989);

        aioClientStarter.channelInitializer(aioChannel -> {
            DefaultChannelPipeline defaultChannelPipeline = aioChannel.getDefaultChannelPipeline();


            //指定结束符解码器
            defaultChannelPipeline.addLast(new DelimiterFrameDecoder(DelimiterFrameDecoder.lineDelimiter));
            //字符串解码器
            defaultChannelPipeline.addLast(new StringDecoder());
            //定义消息解码器
            defaultChannelPipeline.addLast(new SimpleHandler());
        }).start();
    }


    public static void main(String[] args)throws Exception {
        initClient();
    }
}
