package cuit.pymjl.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/10 14:33
 **/
public class FactoryTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new NamedThreadFactory("test-provider"));
        executor.execute(() -> {
            System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: step1");
        });
        executor.execute(() -> {
            System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: step2");
        });

        ThreadPoolExecutor executor2 = new ThreadPoolExecutor(2, 2, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new NamedThreadFactory("test-consumer"));
        executor2.execute(() -> {
            System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: step1");
        });
        executor2.execute(() -> {
            System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: step2");
        });
        Thread.sleep(1000);
        executor.shutdown();
        executor2.shutdown();
    }
}
