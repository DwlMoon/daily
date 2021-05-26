package dailyproject.moon.IO.netty.groupchat.Client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端处理器
 * @program: daily_test
 * @description:
 * @create: 2021-05-26 20:07
 **/

public class GroupChathandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0 (ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s.trim());
    }
}
