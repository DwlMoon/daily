package dailyproject.moon.IO.netty.ChatCToC.Server;

import dailyproject.moon.common.util.MoonJsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: daily_test
 * @description: netty服务端处理器
 * @create: 2021-05-26 19:18
 **/

public class CGroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个Channel map,管理客户端对应的Channel
    //GlobalEventExecutor.INSTANCE 是一个全局的时间执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);



    private static Map<String, Channel> channelMap=new HashMap<String, Channel>();

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
        String channelId = ctx.channel().id().asShortText();
        System.out.println("Channel-Id为： "+channelId);
        channelMap.put(channelId,channel);
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
        System.out.println(dateFormat.format(new Date())+"----"+ctx.channel().id().asShortText() + "已经上线");
    }



    /**
     * Channel处于非活动状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive (ChannelHandlerContext ctx) throws Exception {
        System.out.println(dateFormat.format(new Date())+"----"+ctx.channel().id().asShortText() + "离线");
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

        Map<String, Object> map = MoonJsonUtil.jsonToMap(s, Map.class);
        System.out.println("获取的map为："+map.toString());
        System.out.println(channelMap);

        System.out.println(channelMap.get(map.get("user")).id().asShortText());

        //遍历ChannelGroup，去除发送者Channel的干扰（发送者不收到自己发送的消息）
        if (map.get("user")!= null) {
            channelGroup.forEach(otherChannel -> {
                //获取要发送的Channel
                if (otherChannel == channelMap.get(map.get("user"))){
                    //将消息发给指定客户端
                    otherChannel.writeAndFlush(dateFormat.format(new Date())+"----"+"[ 客户 ]"+channel.remoteAddress() +"说： "+map.get("msg")+" \n");
                }
            });
        }
    }


    /**
     * 异常处理,关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        ctx.channel().close();
    }
}
