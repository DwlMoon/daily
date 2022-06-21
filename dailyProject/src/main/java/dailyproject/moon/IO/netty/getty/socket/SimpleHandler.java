package dailyproject.moon.IO.netty.getty.socket;

import com.gettyio.core.channel.AioChannel;
import com.gettyio.core.pipeline.in.SimpleChannelInboundHandler;
import dailyproject.moon.IO.netty.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SimpleHandler extends SimpleChannelInboundHandler<byte[]> {

    public static SimpleHandler handler;


    public SimpleHandler() {
    }

    @Override
    public void channelRead0(AioChannel aioChannel, byte[] msg) throws Exception {
        System.out.println("123");
        System.out.println("读取消息:" + CheckUtil.bytesToHexString(msg));

    }


    @Override
    public void channelAdded(AioChannel aioChannel) throws Exception {
        System.out.println("连接过来了");
    }

    @Override
    public void channelClosed(AioChannel aioChannel) throws Exception {
        super.channelClosed(aioChannel);
        System.out.println("连接关闭了");
    }

    /**
     * 回执消息
     * @param aBoolean
     * @return
     */
    public String readBack(Boolean aBoolean){
        String returnMessage=null;
        if (aBoolean==true){
            returnMessage="AAAAAAAAAAAA00AAAA";
        }else {
            returnMessage="AAAAAAAAAAAA08AAAA";
        }
        return returnMessage;
    }


    public static void main(String[] args)throws Exception {
        GettyServer.startServer("172.20.0.216",9999);
    }



}
