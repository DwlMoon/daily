package dailyproject.moon.IO.NIO.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: daily_test
 * @description: 客户端
 * @create: 2021-03-31 20:02
 **/

public class ClientOne {
    private final String host="127.0.0.1";
    private final int port=8989;
    private SocketChannel socketChannel;
    private Selector selector;
    private String username;

    public ClientOne ()throws Exception {
        selector=Selector.open();
        socketChannel=SocketChannel.open(new InetSocketAddress(host,port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+"准备好了");
    }

    public void sendMsgToServer(String msg){
        msg = username+"说： "+msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMsgFromServer(){
        try {
            if (selector.select()>0){
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()){
                    SelectionKey selectionKey = keyIterator.next();
                    keyIterator.remove();
                    if (selectionKey.isReadable()){
                        SocketChannel channel = (SocketChannel)selectionKey.channel();
                        ByteBuffer buffer=ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        buffer.flip();
                        String s = new String(buffer.array());
                        System.out.println("*********************************************");
                        System.out.println(new Date());
                        System.out.println(s);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main (String[] args) throws Exception{
        ClientOne clientOne = new ClientOne();

        //启动一个线程完成工作(每隔三秒读取一次)
        ExecutorService exec = Executors.newFixedThreadPool(5);
        exec.execute(new Runnable() {
            @Override
            public void run () {
                while (true){
                    try {
                        Thread.sleep(3000);
                        clientOne.readMsgFromServer();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            clientOne.sendMsgToServer(s);
        }


    }

}
