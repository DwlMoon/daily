package dailyproject.moon.IO.netty.demo.Client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-04-21 11:21
 **/

public class MyNettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪时触发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {
        System.out.println("cleint : "+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，服务器", CharsetUtil.UTF_8));
    }

    /**
     * 当通道有读取事件时触发
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead (ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端收到的消息 : "+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址 : "+ctx.channel().remoteAddress());
    }

    /**
     * 处理异常事件
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
