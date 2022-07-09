package cuit.pymjl.thread;

import java.util.concurrent.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/9 18:16
 **/
public class Test {
    private static final CountDownLatch latch = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        executor.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
            System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: child thread 1 count down");
        });
        executor.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
            System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: child thread 2 count down");
        });
        System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: main thread start");
        latch.await();
        Thread.sleep(1);
        System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: all children thread end");
        executor.shutdown();
    }
}
