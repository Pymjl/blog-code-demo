package cuit.pymjl;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

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
    }
}
