package dailyproject.moon.IO.netty.demo.Server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;


import java.util.concurrent.TimeUnit;

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
//        System.out.println("server ctx = "+ctx);
//
//        //将msg转换成bytebuf（netty提供的，不是NIO本身的）
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("客户端发送的消息："+byteBuf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址："+ctx.channel().remoteAddress());


        /**验证阻塞情况下，将task交给TaskQueue去处理*/
//        Thread.sleep(10*1000);
//        System.out.println("事件处理中》》》》》");


        /**解决方案一： 用户自定义普通任务,将事件处理添加到TaskQueue中 */
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run () {
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("事件1处理中》》》》》");
            }
        });
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run () {
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("事件2处理中》》》》》");
            }
        });

        //用户自定义定时任务 ， 该任务是提交到 scheduleTaskQueue 中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run () {
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("事件3处理中》》》》》");
            }
        },5, TimeUnit.SECONDS);


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


    @Override
    public void userEventTriggered (ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            //将event 向下转型
            IdleStateEvent stateEvent = (IdleStateEvent) evt;

            switch (stateEvent.state()){
                //读空闲
                case READER_IDLE:
                    System.out.println("读 空闲");
                    break;
                //写空闲
                case WRITER_IDLE:
                    System.out.println("写 空闲");
                    break;
                //读写空闲
                case ALL_IDLE:
                    System.out.println("读 写 空闲");
                    break;
            }

            System.out.println(ctx.channel().remoteAddress()+"超时");
        }
    }
}
