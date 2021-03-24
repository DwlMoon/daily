package dailyproject.moon.NIO.nio.channel;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @program: daily_test
 * @description: 通道（文件复制）
 * @create: 2021-03-24 10:57
 **/

public class FileCopyChannel {

    /**
     * 分散读取和写入
     */
    @Test
    public void test4()throws Exception{
        FileChannel readChannel = FileChannel.open(Paths.get("D:\\123.xlsx"), StandardOpenOption.READ);

        ByteBuffer allocate1 = ByteBuffer.allocate(100);
        ByteBuffer allocate2 = ByteBuffer.allocate(1024);

        ByteBuffer[] buf={allocate1,allocate2};
        readChannel.read(buf);

        for (ByteBuffer buffer:buf){
            buffer.flip();
        }

        System.out.println(new String(buf[0].array(),0,buf[0].limit()));
        System.out.println(new String(buf[1].array(),0,buf[1].limit()));

        FileChannel writeChannel = FileChannel.open(Paths.get("D:\\444.xlsx"), StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);

        writeChannel.write(buf);

    }



    /**
     * 使用通道之间直接复制(直接缓冲区的方式)
     */
    @Test
    public void test3()throws Exception{
        //获取通道
        FileChannel readChannel = FileChannel.open(Paths.get("D:\\123.xlsx"), StandardOpenOption.READ);
        FileChannel writeChannel = FileChannel.open(Paths.get("D:\\456.xlsx"), StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);

        readChannel.transferTo(0,readChannel.size(),writeChannel);

        readChannel.close();
        writeChannel.close();

    }






    /**
     * 使用直接缓冲区完成文件的复制（内存映射文件）
     */
    @Test
    public void test2()throws Exception{
        //获取通道
        FileChannel readChannel = FileChannel.open(Paths.get("D:\\123.xlsx"), StandardOpenOption.READ);
        FileChannel writeChannel = FileChannel.open(Paths.get("D:\\456.xlsx"), StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);

        //内存映射文件（直接内存）
        MappedByteBuffer inMap = readChannel.map(FileChannel.MapMode.READ_ONLY, 0, readChannel.size());
        MappedByteBuffer outMap = writeChannel.map(FileChannel.MapMode.READ_WRITE, 0, writeChannel.size());



        //直接对缓冲区进行数据的读写
        byte[] bytes = new byte[inMap.limit()];
        inMap.get(bytes);
        outMap.put(bytes);

        readChannel.close();
        writeChannel.close();

    }





    /**
     * 利用通道完成文件的复制（非直接缓冲区）
     */
    @Test
    public void test1()throws Exception{
        FileInputStream fileInputStream = new FileInputStream("D:\\123.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\456.xlsx");

        //获取通道
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        //分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //将通道中的数据存入缓冲区
        while (inputStreamChannel.read(buffer) != -1){
            //切换到读数据模式
            buffer.flip();
            //将缓冲区的数据写入到通道中
            outputStreamChannel.write(buffer);
            //清空缓冲区，避免重复写
            buffer.clear();
        }

        inputStreamChannel.close();
        outputStreamChannel.close();
        fileInputStream.close();
        fileOutputStream.close();

    }


}
