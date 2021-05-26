package dailyproject.moon.IO.netty.communication.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-05-20 17:49
 **/

public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("客户端已连接", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead (ChannelHandlerContext ctx, Object msg) throws Exception {
        //异步读服务端数据
        ctx.channel().eventLoop().execute(new Runnable() {
            ByteBuf byteBuf = (ByteBuf) msg;
            @Override
            public void run () {
                System.out.println("客户端读取的数据为： "+byteBuf.toString(CharsetUtil.UTF_8));
            }
        });
    }

    @Override
    public void channelReadComplete (ChannelHandlerContext ctx) throws Exception {
        System.out.println("向服务端应答消息>>>>>");
        ctx.writeAndFlush(Unpooled.wrappedBuffer("我收到了".getBytes()));
    }
}