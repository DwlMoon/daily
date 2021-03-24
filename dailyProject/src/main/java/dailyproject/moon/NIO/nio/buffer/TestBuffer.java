package dailyproject.moon.NIO.nio.buffer;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @program: daily_test
 * @description: 缓冲区
 * @create: 2021-03-23 16:02
 **/

public class TestBuffer {

    @Test
    public void test1(){
        //获取buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String abc="abcde";

        //存数据
        byteBuffer.put(abc.getBytes());
        System.out.println("=============put=================");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        //切换数据读取模式
        byteBuffer.flip();

        //取数据
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes,0,bytes.length));

        System.out.println("=============get=================");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 可重复读
        byteBuffer.rewind();

        //清空缓冲区
        byteBuffer.clear();

    }

    @Test
    public void test2(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String s="abcd";
        byteBuffer.put(s.getBytes());

        System.out.println(byteBuffer.position());
        //切换
        byteBuffer.flip();

        byte[] bytes = new byte[byteBuffer.limit()];
        //获取两个字符
        byteBuffer.get(bytes,0,2);
        System.out.println(new String(bytes,0,2));
        System.out.println(byteBuffer.position());

        //标记
        byteBuffer.mark();

        //再次获取两个
        byteBuffer.get(bytes,2,2);
        System.out.println(new String(bytes,2,2));
        System.out.println(byteBuffer.position());

        //恢复到mark位置
        byteBuffer.reset();
        System.out.println(byteBuffer.position());

    }
}
