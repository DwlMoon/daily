package dailyproject.moon.IO.getty.gettyNormal;

import com.gettyio.core.channel.AioChannel;
import com.gettyio.core.handler.timeout.IdleState;
import com.gettyio.core.pipeline.ChannelHandlerAdapter;
import com.gettyio.core.pipeline.in.SimpleChannelInboundHandler;

public class SimpleHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelAdded(AioChannel aioChannel) throws Exception {
        super.channelAdded(aioChannel);
        System.out.println("连接过来了");
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
    public void channelRead0(AioChannel aioChannel, String s) throws Exception {
        System.out.println("读取消息:" + s);
        String back ="Yes";
        aioChannel.writeAndFlush(back.getBytes());
    }



}
