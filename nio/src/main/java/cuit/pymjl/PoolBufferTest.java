package cuit.pymjl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/15 14:43
 **/
public class PoolBufferTest {
    private static final int BUFFER_SIZE = 1024;
    private static final int COUNT = 1800000;
    private static final byte[] bytes = new byte[BUFFER_SIZE];

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ByteBuf poolBuffer = null;
        for (int i = 0; i < COUNT; i++) {
            poolBuffer = PooledByteBufAllocator.DEFAULT.directBuffer(BUFFER_SIZE);
            poolBuffer.writeBytes(bytes);
            poolBuffer.release();
        }
        long end = System.currentTimeMillis();
        System.out.println("内存池分配缓冲区耗时：" + (end - start) + " ms");

        start = System.currentTimeMillis();
        ByteBuf buffer = null;
        for (int i = 0; i < COUNT; i++) {
            buffer = Unpooled.directBuffer(BUFFER_SIZE);
            buffer.writeBytes(bytes);
        }
        end = System.currentTimeMillis();
        System.out.println("非内存池分配缓冲区耗时：" + (end - start) + " ms");

    }
}
