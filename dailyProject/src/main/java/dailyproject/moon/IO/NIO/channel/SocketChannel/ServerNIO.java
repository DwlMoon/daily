package dailyproject.moon.IO.NIO.channel.SocketChannel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-03-24 19:25
 **/

public class ServerNIO {

        // 端口号
        private static final int PORT = 8888;
        // 缓存大小
        private static final int BUFFER_SIZE = 1024;

        public void service() throws IOException {
            // 初始化服务器
            System.out.println("Initialize server...");
            // 1. 创建ServerSocketChannel通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 2. 设置通道为非阻塞式
            serverSocketChannel.configureBlocking(false);
            // 3. 创建选择器
            Selector selector = Selector.open();
            // 4. 绑定服务器
            serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(),PORT));
            // 5. 注册接收事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            // 与客户端建立连接
            while (selector.select() > 0) {
                // 1. 获得通道的迭代器
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                // 2. 遍历通道
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // 3. 获得该通道后，就将其从迭代器中删除
                    iterator.remove();
                    // 4. 根据不同的通道进行不同的业务
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            // 接收业务
                            accept(selector,key);
                        }
                        if (key.isReadable()) {
                            // 读取业务
                        /*
                            服务器端的读取业务和客户端的是一模一样的，
                            于是这里直接调用客户端的读取业务
                         */
//                            ClientNIO.read(selector,key);

                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int len=0;
                            while ((len=channel.read(buffer))>0){
                                buffer.flip();
                                System.out.println("获取的数据 ： "+new String(buffer.array(),0,len));
                                buffer.clear();
                            }
                        }
                        if (key.isWritable()) {
                            // 写出业务
                            write(selector,key);
                        }
                    }
                }
            }
        }

        /**
         * 接收业务
         * @param selector
         * @param key
         */
        private void accept(Selector selector, SelectionKey key) throws IOException {
            // 1. 获取服务器通道
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            // 2. 获取TCP协议传输通道
            SocketChannel socketChannel = serverSocketChannel.accept();
            // 3. 将传输通道设为非阻塞式
            socketChannel.configureBlocking(false);
            // 4. 绑定读取事件
            socketChannel.register(selector,SelectionKey.OP_READ);
            // 接收成功
            System.out.println("connect with a client from " + socketChannel.socket().getInetAddress().getHostAddress());
        }

        /**
         * 写出业务
         * @param selector
         * @param key
         * @throws IOException
         */
        private void write(Selector selector, SelectionKey key) throws IOException {
            // 1. 获得TCP协议通信的通道
            SocketChannel socketChannel = (SocketChannel) key.channel();
            // 2. 分配缓存区
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            // 3. 清空缓存区
            buffer.clear();
            // 4. 写入发送的信息
            buffer.put(ByteBuffer.wrap("服务端写出事件".getBytes()));
            // 5. 将缓存区的指针回到初始位置
            buffer.flip();
            // 6. 设置通道为非阻塞式
            socketChannel.configureBlocking(false);
            // 7. 不断写出到通道
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            // 8. 继续注册读取事件
            socketChannel.register(selector, SelectionKey.OP_READ);
        }

        public static void main(String[] args) throws IOException {
            new ServerNIO().service();
        }


}
