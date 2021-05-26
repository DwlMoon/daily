package dailyproject.moon.IO.netty.ChatCToC.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @program: daily_test
 * @description: 群聊客户端
 * @create: 2021-05-26 19:49
 **/

public class GroupChatClient {

    private  final String ip;

    private  final  int  port;

    public GroupChatClient (String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public  void run(){
        NioEventLoopGroup loopGroup=null;
        try {
            loopGroup = new NioEventLoopGroup();

            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel (SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    //向pipeline加入解码器
                                    .addLast("decoder",new StringDecoder())
                                    //向pipeline中加入编码器
                                    .addLast("encoder",new StringEncoder())
                                    .addLast(new CGroupChathandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            System.out.println("==============》"+channelFuture.channel().localAddress());

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                StringBuilder stringBuilder = new StringBuilder();


                //通过Channel发送到服务器端
                channelFuture.channel().writeAndFlush(msg +"\r\n");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            loopGroup.shutdownGracefully();
        }
    }

    public static void main (String[] args) {
        new GroupChatClient("127.0.0.1",8989).run();
    }
}
