package cuit.pymjl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/15 10:56
 **/
public class BufferDemo {
    public static void main(String[] args) throws IOException {
        // 1.创建一个文件输入流
        FileInputStream fis = new FileInputStream("C:\\Users\\Admin\\JavaProjects\\blog-code-demo\\nio\\src\\main\\resources\\test.txt");
        // 2.获取文件通道
        FileChannel fc = fis.getChannel();
        // 3.分配一个指定大小的缓冲区，其实就是分配一个大小为10的数组
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        output("初始化", buffer);
        // 4.将文件通道中的数据读到缓冲区中
        fc.read(buffer);
        output("调用read", buffer);
        // 5.切换读取模式
        buffer.flip();
        output("调用flip", buffer);
        // 6.读取数据
        while (buffer.remaining() > 0) {
            buffer.get();
        }
        buffer.clear();
        output("调用clear", buffer);
        // 7.关闭文件通道
        fc.close();

    }

    public static void output(String step, Buffer buffer) {
        System.out.println(step + ": position=" + buffer.position() +
                " limit=" + buffer.limit() + " capacity=" + buffer.capacity());
    }
}
