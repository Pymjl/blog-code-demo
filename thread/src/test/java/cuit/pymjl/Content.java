package cuit.pymjl;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/4 10:45
 **/
public class Content {
    @Test
    void test() {
        // 1. 创建一个Random对象
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            // 2.输出10个[0,5)的随机数
            System.out.println(random.nextInt(5));
        }
    }

    @Test
    void testThreadLocalRandom() {
        // 1. 创建一个Random对象
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 10; i++) {
            // 2.输出10个[0,5)的随机数
            System.out.println(random.nextInt(5));
        }
    }

    @Test
    void testAtomic() {
        AtomicLong atomicLong = new AtomicLong();
        System.out.println(atomicLong.incrementAndGet());
        System.out.println(atomicLong.decrementAndGet());
        System.out.println(atomicLong.getAndIncrement());
        System.out.println(atomicLong.getAndDecrement());
    }

    @Test
    void testCopyOnWriteList() {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        list.add(1);
        list.get(0);
        list.add(2);
        list.set(1, 3);
        System.out.println(Arrays.toString(list.toArray()));
    }

    @Test
    void testTree() {
        Deque<Integer> deque = new ArrayDeque<>();

    }

    @Test
    void testLockSupport() throws InterruptedException {
        // 1. 创建一个子线程
        Thread thread = new Thread(() -> {
            System.out.println("child thread begin park!");
            // 2. 循环判断线程是否被中断，防止虚假唤醒
            while (!Thread.currentThread().isInterrupted()) {
                LockSupport.park();
            }
            // 3. 如果被中断，则打印提示信息
            System.out.println("child thread unpark!");
        });

        // 4. 启动子线程
        thread.start();
        // 5. 主线程睡眠1秒
        Thread.sleep(1000);
        // 6. 打印提示信息
        System.out.println("main thread begin unpark!");
        // 7. 打断子线程，让子线程立即返回
        thread.interrupt();
    }

}
