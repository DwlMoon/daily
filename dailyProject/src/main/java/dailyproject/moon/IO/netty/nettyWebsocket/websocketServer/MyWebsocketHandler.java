package dailyproject.moon.IO.netty.nettyWebsocket.websocketServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-06-07 15:45
 **/

//TextWebSocketFrame 标识一个文本帧
public class MyWebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0 (ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

        System.out.println("服务器端收到消息"+textWebSocketFrame.text());

        //回复消息
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("服务器收到了"));
    }

    /**
     * 客户端连接后触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded (ChannelHandlerContext ctx) throws Exception {
        //表示唯一的值,
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved (ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生"+cause.getMessage());
        //关闭连接
        ctx.close();
    }

}
