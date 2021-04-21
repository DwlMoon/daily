package dailyproject.moon.IO.netty.Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @program: daily_test
 * @description: 自定义handler处理器，为了加入到pipeline 使用
 * @create: 2021-04-21 10:55
 **/

public class MyNettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发来的信息
     * @param ctx ：上下文对象，含有 业务逻辑处理管道pipeline、数据通道、地址
     * @param msg ：客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead (ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = "+ctx);

        //将msg转换成bytebuf（netty提供的，不是NIO本身的）
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送的消息："+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+ctx.channel().remoteAddress());
    }

    /**
     * 读取数据完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete (ChannelHandlerContext ctx) throws Exception {

        //将数据写入到缓存并刷新
        //将发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello , client",CharsetUtil.UTF_8));
    }


    /**
     * 处理异常，当发生异常时需要将通道关闭
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
