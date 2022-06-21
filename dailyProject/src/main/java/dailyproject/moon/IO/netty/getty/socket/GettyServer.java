package dailyproject.moon.IO.netty.getty.socket;

import com.gettyio.core.channel.AioChannel;
import com.gettyio.core.channel.starter.AioServerStarter;
import com.gettyio.core.handler.codec.string.StringDecoder;
import com.gettyio.core.pipeline.ChannelInitializer;
import com.gettyio.core.pipeline.DefaultChannelPipeline;

public class GettyServer {

    public static AioServerStarter server;

    public static void startServer(String telecontrolIp,Integer telecontrolPort)throws Exception{
        server=new AioServerStarter(telecontrolIp,telecontrolPort);

        server.channelInitializer(new ChannelInitializer() {
            @Override
            public void initChannel(AioChannel aioChannel) throws Exception {
                DefaultChannelPipeline defaultChannelPipeline = aioChannel.getDefaultChannelPipeline();

                //添加字符串解码器
                defaultChannelPipeline.addLast(new StringDecoder());
                //添加自定义的简单消息处理器
                defaultChannelPipeline.addLast(new SimpleHandler());
            }
        }).start();
    }

    public static void closeServer(){
        if (server !=null){
            server.shutdown();
        }
    }

}
