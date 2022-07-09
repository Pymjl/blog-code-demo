package cuit.pymjl.thread;

import java.util.concurrent.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/9 19:03
 **/
public class CyclicBarrierTest {
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        executor.submit(() -> {
            try {
                System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: step1");
                cyclicBarrier.await();
                System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: step2");
                cyclicBarrier.await();
                System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: step3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.submit(() -> {
            try {
                System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: step1");
                cyclicBarrier.await();
                System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: step2");
                cyclicBarrier.await();
                System.out.println(System.currentTimeMillis() + "[" + Thread.currentThread().getName() + "]: step3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }

}
