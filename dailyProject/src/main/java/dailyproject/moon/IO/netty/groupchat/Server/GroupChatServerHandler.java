package dailyproject.moon.IO.netty.groupchat.Server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;


import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: daily_test
 * @description: netty服务端处理器
 * @create: 2021-05-26 19:18
 **/

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个Channel集合，管理所有的Channel
    //GlobalEventExecutor.INSTANCE 是一个全局的时间执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //时间格式化
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");


    /**
     * 标识连接一旦建立，第一个被执行
     * */
    @Override
    public void handlerAdded (ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        //将该客户加入聊天的信息推送给其他在线的客户端
        /**
         * 该方法会将channelGroup所有的Channel遍历并发送消息
         * 我们不需要自己遍历
         * */
        channelGroup.writeAndFlush(dateFormat.format(new Date())+"----"+"[ 客户端 ]"+channel.remoteAddress()+" 加入聊天");

        channelGroup.add(channel);
    }



    /**
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved (ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.writeAndFlush(dateFormat.format(new Date())+"----"+"[ 客户端 ]"+channel.remoteAddress()+" 离开了");

        //也可以不写，handlerRemoved方法会自动将当前Channel从集合中去除
        channelGroup.remove(channel);
    }



    /**
     * 标识Channel处于活动状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {
        System.out.println(dateFormat.format(new Date())+"----"+ctx.channel().remoteAddress() + "已经上线");
    }



    /**
     * Channel处于非活动状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive (ChannelHandlerContext ctx) throws Exception {
        System.out.println(dateFormat.format(new Date())+"----"+ctx.channel().remoteAddress() + "离线");
    }


    /**
     * 读取数据
     * @param channelHandlerContext
     * @param s
     * @throws Exception
     */
    @Override
    protected void channelRead0 (ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();

        //遍历ChannelGroup，去除发送者Channel的干扰（发送者不收到自己发送的消息）
        channelGroup.forEach(otherChannel -> {
            //不是当前的Channel
            if (otherChannel !=channel){
                //将消息发给除自己意外的其他客户端
                otherChannel.writeAndFlush(dateFormat.format(new Date())+"----"+"[ 客户 ]"+channel.remoteAddress() +"说： "+s+" \n");
            }else {
                //自己回显消息
                otherChannel.writeAndFlush(dateFormat.format(new Date())+"----"+"[ 自己 ]发送了消息："+s+"\n");
            }
        });
    }


    /**
     * 异常处理,关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
