package dailyproject.moon.IO.BIO;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: daily_test
 * @description: BIO服务端
 * @create: 2021-03-26 16:04
 **/

public class BIOserver {


    /**
     * bio实例服务端
     */
    @Test
    public void bioServer()throws Exception{

        //创建线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();

        ServerSocket socket = new ServerSocket(6666);

        System.out.println("启动");

        while (true){
            //监听，等待客户端连接
            Socket accept = socket.accept();

            threadPool.execute(new Runnable() {
                @Override
                public void run () {
                    //可以和客户端通信
                    work(accept);
                }
            });

        }
    }


    //和客户端通信的方法
    public void work(Socket socket){
        try {
            byte[] bytes = new byte[1024];

            //获取输入流
            InputStream inputStream = socket.getInputStream();

            //循环读取客户端发送的数据
            //返回-1是对的啊，read字节数组的时候，读到最后一个没有的时候就数组越界异常，
            // -1就是不在数组的索引值范围内，所以开发人员就将它作为read完成后的返回值
            while (inputStream.read(bytes)!=-1){
                System.out.println(new String(bytes,0,bytes.length));
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }


}
