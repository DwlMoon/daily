package dailyproject.moon.IO.NIO.channel;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-03-26 20:08
 **/

public class MyNIO {

    @Test
    public void client () throws Exception {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8989));

        socketChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("haha".getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        socketChannel.close();
        buffer.clear();
        System.out.println("发送完毕");
    }

    @Test
    public void server () throws Exception {

        ServerSocketChannel open = ServerSocketChannel.open();
        open.configureBlocking(false);
        open.bind(new InetSocketAddress(8989));
        Selector selector = Selector.open();
        open.register(selector, SelectionKey.OP_ACCEPT);


        while (selector.select()>0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()){
                    System.out.println("接收事件");
                    SocketChannel socketChannel = open.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
                if (selectionKey.isReadable()){
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len=0;
                    while ((len=channel.read(byteBuffer))!=-1){
                        byteBuffer.flip();
                        System.out.println(byteBuffer.limit());
                        System.out.println(new String(byteBuffer.array(),0,len));
                        byteBuffer.clear();
                    }
                }

            }
            iterator.remove();
        }

    }

}
