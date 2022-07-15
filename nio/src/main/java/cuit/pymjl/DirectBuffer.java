package cuit.pymjl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/15 11:07
 **/
public class DirectBuffer {
    public static void main(String[] args) throws IOException {
        // 1.定位资源文件
        String src = "C:\\Users\\Admin\\JavaProjects\\blog-code-demo\\nio\\src\\main\\resources\\test.txt";
        String dest = "C:\\Users\\Admin\\JavaProjects\\blog-code-demo\\nio\\src\\main\\resources\\test_copy.txt";

        // 2.创建文件输入流
        FileInputStream fis = new FileInputStream(src);
        // 3.获取文件通道
        FileChannel fin = fis.getChannel();
        // 4.创建文件输出流
        FileOutputStream fos = new FileOutputStream(dest);
        // 5.获取文件通道
        FileChannel fout = fos.getChannel();

        // 6.分配一个指定大小的直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        // 7.将文件通道中的数据读到直接缓冲区中
        while (fin.read(buffer) != -1) {
            // 8.切换写模式
            buffer.flip();
            // 9.将直接缓冲区中的数据写到文件通道中
            fout.write(buffer);
            // 10.清空直接缓冲区(每次读完后一定需要clear)
            buffer.clear();
        }
    }
}
