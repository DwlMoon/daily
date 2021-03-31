package dailyproject.moon.IO.NIO.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @program: daily_test
 * @description: 服务端接收消息并转发
 * @create: 2021-03-31 19:15
 **/

public class BackServer {
    public  Selector selector=null;
    public  ServerSocketChannel serverSocketChannel=null;
    public  int port=8989;

    public BackServer () {
        try {
            selector=Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("初始化失败");
        }
    }

    public void myServer(){
        try {
            while (selector.select()>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isAcceptable()){
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                        System.out.println(socketChannel.getRemoteAddress()+"上线了");
                    }
                    if (selectionKey.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        String msg=null;
                        socketChannel.read(byteBuffer);

                        byteBuffer.flip();
                        msg=new String(byteBuffer.array());
                        System.out.println("服务端获取的消息为： "+msg);
                        byteBuffer.clear();

                        sendInfoToOtherClient(msg,socketChannel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 转发消息到其他客户端
     * @param msg
     * @param socketChannel
     */
    public void sendInfoToOtherClient(String msg,SocketChannel socketChannel)throws Exception{
        System.out.println("服务器转发消息中》》》");
        //遍历 所有注册到selector上的socketChannel，并排除自己
        for (SelectionKey selectionKey:selector.keys()){
            Channel channel = selectionKey.channel();

            //排除自己
            if (channel instanceof SocketChannel && channel !=socketChannel){
                //转发
                SocketChannel sendsocketChanne=(SocketChannel)channel;
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                byteBuffer.flip();
                System.out.println("lalal============"+new String(byteBuffer.array()));
                sendsocketChanne.write(byteBuffer);
            }
        }
    }


    public static void main (String[] args) {
        BackServer backServer = new BackServer();
        backServer.myServer();
    }

}
