package dailyproject.moon.IO.netty.getty.socket;

import com.gettyio.core.channel.starter.AioClientStarter;
import com.gettyio.core.handler.codec.string.StringDecoder;
import com.gettyio.core.handler.codec.string.StringEncoder;
import com.gettyio.core.pipeline.DefaultChannelPipeline;

public class GettyClient {

    public static AioClientStarter aioClientStarter;

    public static void satrtClient(String SimulationIP,Integer SimulationPort){

        try {
            aioClientStarter=new AioClientStarter(SimulationIP,SimulationPort);

            aioClientStarter.channelInitializer(aioChannel -> {
                DefaultChannelPipeline defaultChannelPipeline = aioChannel.getDefaultChannelPipeline();

                defaultChannelPipeline.addLast(new StringEncoder());
                defaultChannelPipeline.addLast(new StringDecoder());

            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void clientSendMessage(String message){
        System.out.println("To 外部: "+message);
//        try {
//            aioClientStarter.getAioChannel().writeAndFlush(CheckUtil.hexString2Bytes(message));
//        } catch (Exception e) {
//            UJsToSendMessageService.socketFlag=false;
//            e.printStackTrace();
//        }
    }

    public static void closeClient(){
        if (aioClientStarter !=null){
            aioClientStarter.shutdown();
        }
    }


    public static void main(String[] args)throws Exception {
        String data="1ACFFC1D29A30101FFFF2C3333333318F4E74E000000000000020000000000000000080000000000061D0000000000061D0000001B18F4E74E000000000000000000000000000000000000000000000000061D0000001800000000000000000000000000000000000000000000030202AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA5EE9";
        satrtClient("172.20.0.216",8989);
        Thread.sleep(500);
        clientSendMessage(data);
        closeClient();
    }

}
