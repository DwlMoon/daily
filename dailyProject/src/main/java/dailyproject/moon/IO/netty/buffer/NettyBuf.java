package dailyproject.moon.IO.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @program: daily_test
 * @description: Netty缓冲区
 * @create: 2021-05-26 15:37
 **/

public class NettyBuf {
    public static void main (String[] args) {

        //创建一个ByteBuf
        // 创建对象，该对象包含一个数组，长度是10

         /**在Netty的buffer中，不需要使用flip 进行反转读取 ,
          * 因为其维护了一个 readerIndex 和 writeIndex
         */
        ByteBuf buffer = Unpooled.buffer(10);

        //输入
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        //输出 capacity()：数组的大小
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println("第 "+i+" 个数字为： "+buffer.getByte(i));
        }


        ByteBuf byteBuf = Unpooled.copiedBuffer("我是Netty", Charset.forName("utf-8"));

        if (byteBuf.hasArray()){
            byte[] array = byteBuf.array();
            System.out.println(new String(array,Charset.forName("utf-8")));
        }


    }
}
