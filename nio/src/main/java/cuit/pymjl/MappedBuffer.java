package cuit.pymjl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/15 11:13
 **/
public class MappedBuffer {
    public static void main(String[] args) throws IOException {
        // 1.定位资源文件
        String src = "C:\\Users\\Admin\\JavaProjects\\blog-code-demo\\nio\\src\\main\\resources\\mapped.txt";
        // 2.创建一个随机访问文件
        RandomAccessFile raf = new RandomAccessFile(src, "rw");
        // 3.获取文件通道
        FileChannel channel = raf.getChannel();
        // 4.把缓冲区跟文件系统做一个映射，只要操作缓冲区文件内容也会随着改变
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        // 5.修改缓冲区中的数据
        buffer.put(0, (byte) 'X');
        buffer.put(5, (byte) 'Y');
        // 6.关闭通道
        channel.close();
    }
}
