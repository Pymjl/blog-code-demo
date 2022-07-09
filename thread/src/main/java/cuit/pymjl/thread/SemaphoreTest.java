package cuit.pymjl.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/9 19:31
 **/
public class SemaphoreTest {
    // 创建一个信号量，信号量的初始值为1
    private static final Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        executor.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis() + "[" +
                    Thread.currentThread().getName() + "]: Semaphore计数器加1");
            // 信号量加1，这个方法会唤醒之前因为调用aquire()而睡眠的线程（信号量达到要求才会被唤醒）
            // 被唤醒线程会重新尝试获取增加后的信号量，如果信号量还没有达到要求，则会继续睡眠，直到信号量达到要求
            semaphore.release();
        });

        executor.submit(() -> {
            try {
                semaphore.acquire();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis() + "[" +
                    Thread.currentThread().getName() + "]: Semaphore计数器加1");
            // 信号量加1
            semaphore.release();
        });

        System.out.println(System.currentTimeMillis() + "[" +
                Thread.currentThread().getName() + "]: main start");

        // 获取信号量，如果信号量的值小于等于0，则将当前线程放进AQS阻塞队列，参数是获取多少个，因为3-2=1>0,所以不会阻塞
        semaphore.acquire(2);
        System.out.println(System.currentTimeMillis() + "[" +
                Thread.currentThread().getName() + "]: main end");

        executor.shutdown();
    }
}
