package dailyproject.moon.IO.NIO.channel;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

/**
 * @program: daily_test
 * @description: 网络通信NIO
 * @create: 2021-03-24 14:09
 **/

public class SocketNIO {


    /**=================================阻塞式网络IO=======================================*/

    /**
     * 客户端
     *
     * @throws Exception
     */
    @Test
    public void client () throws Exception {
//        获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8989));


//        分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

//        读取本地文件
        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\123.xlsx"), StandardOpenOption.READ);

        /**
         * fileChannel 读取文件到 缓冲区 ， socketChannel 将缓冲区的文件写入并发送
         * */
        while (fileChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        socketChannel.close();
        fileChannel.close();

    }


    /**
     * 服务端
     */
    @Test
    public void server () throws Exception {

        //获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //绑定连接
        serverSocketChannel.bind(new InetSocketAddress(8989));

        // 获取客户端连接的通道
        SocketChannel socketChannel = serverSocketChannel.accept();

        //接收客户端的数据
        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\ppp.xlsx"), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);


        /**
         * 将 socketChannel 中的文件读到 缓冲区 ， fileChannel 将缓冲区的文件写到本地
         * */
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (socketChannel.read(buffer) != -1) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }


    }


    /**
     * ================================非阻塞式网络IO====================================
     */


    @Test
    public void clientNIO () throws Exception {

        //获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8989));

        //切换成非阻塞模式
        socketChannel.configureBlocking(false);

        //分配指定大小的缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(1024);

        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\123.xlsx"), StandardOpenOption.READ);

        while (fileChannel.read(allocate) != -1) {
            allocate.flip();
            socketChannel.write(allocate);
            allocate.clear();
        }

        socketChannel.close();
        fileChannel.close();

    }


    @Test
    public void serverNIO () throws Exception {
        //获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //切换成非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //绑定连接
        serverSocketChannel.bind(new InetSocketAddress(8989));


        //获取选择器
        Selector selector = Selector.open();

        //将通道注册到选择器上,并指定监听事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //轮询式的获取选择器上已经准备就绪的事件
        while (selector.select() > 0) {

            //获取当前选择器中所有注册的选择键
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                //获取准备的时间
                SelectionKey key = iterator.next();

                //判断具体的事件是什么
                if (key.isAcceptable()) {
                    //接收就绪，获取客户端连接
                    SocketChannel accept = serverSocketChannel.accept();
                    //切换到非阻塞模式
                    accept.configureBlocking(false);

                    //注册到选择器（主要是为了切换到 read 事件）
                    accept.register(selector, SelectionKey.OP_READ);

                }
                if (key.isReadable()) {

                    //获取当前选择器上度就绪事件
                    SocketChannel socketChannel = (SocketChannel) key.channel();

                    //读取数据
                    ByteBuffer allocate = ByteBuffer.allocate(1024);
                    FileChannel fileChannel = FileChannel.open(Paths.get("D:\\ooo.xlsx"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

                    while (socketChannel.read(allocate) != 1) {
                        allocate.flip();
                        fileChannel.write(allocate);
                        allocate.clear();
                    }

                }

                iterator.remove();

            }


        }
    }




    @Test
    public void testClient()throws Exception{
        //获取网络通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 7878));
        //设置为非阻塞
        socketChannel.configureBlocking(false);
        //设置缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //写入缓冲区
        byteBuffer.put("haha".getBytes());
        //切换缓冲区为读模式
        byteBuffer.flip();
        //从缓冲区写入通道
        socketChannel.write(byteBuffer);

        byteBuffer.clear();
        socketChannel.close();
    }


    @Test
    public void testServer()throws Exception{
        //获取通道
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
//        设置非阻塞
        socketChannel.configureBlocking(false);
//        绑定端口
        socketChannel.bind(new InetSocketAddress(7878));

//        获取选择器
        Selector selector = Selector.open();

//        通道注册到选择器
        socketChannel.register(selector,SelectionKey.OP_ACCEPT);

//        如果选择器中有就绪的数据
        while (selector.select()>0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isAcceptable()){
                    SocketChannel channel = socketChannel.accept();
                    channel.configureBlocking(false);
                    channel.register(selector,SelectionKey.OP_READ);
                }else if (selectionKey.isReadable()){
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len=0;
                    while ((len=channel.read(buffer))>0){
                        buffer.flip();
                        System.out.println(new String(buffer.array(),0,len));
                        buffer.clear();
                    }
                }
            }
            iterator.remove();
        }
    }





}
