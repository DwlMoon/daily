package dailyproject.moon.IO.getty.gettyNormal;

import com.gettyio.core.channel.AioChannel;
import com.gettyio.core.channel.starter.AioServerStarter;
import com.gettyio.core.handler.codec.datagrampacket.DatagramPacketDecoder;
import com.gettyio.core.handler.codec.datagrampacket.DatagramPacketEncoder;
import com.gettyio.core.handler.codec.string.DelimiterFrameDecoder;
import com.gettyio.core.handler.codec.string.StringDecoder;
import com.gettyio.core.pipeline.ChannelInitializer;
import com.gettyio.core.pipeline.DefaultChannelPipeline;
import dailyproject.moon.IO.getty.gettyNormal.SimpleHandler;

public class GettyServer {

    public static AioServerStarter server;

    public static void initServer()throws Exception{
        server=new AioServerStarter(8989);

        server.channelInitializer(new ChannelInitializer() {
            @Override
            public void initChannel(AioChannel aioChannel) throws Exception {
                DefaultChannelPipeline defaultChannelPipeline = aioChannel.getDefaultChannelPipeline();


                //添加 分隔符字符串处理器  按 "\r\n\" 进行消息分割
//                defaultChannelPipeline.addLast(new DelimiterFrameDecoder(DelimiterFrameDecoder.lineDelimiter));
                //添加字符串解码器
                defaultChannelPipeline.addLast(new StringDecoder());
                //添加自定义的简单消息处理器
                defaultChannelPipeline.addLast(new SimpleHandler());
            }
        }).start();
    }


    public static void main(String[] args)throws Exception {
        initServer();
    }

}
