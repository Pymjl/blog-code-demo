package cuit.pymjl.blog;

import java.util.concurrent.locks.LockSupport;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/13 11:54
 **/
public class LockSupportTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": child thread begin park");
            //调用park方法，阻塞当前线程，直到调用unpark方法，被唤醒后，继续执行下面的代码
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + ": child thread end park");
        });
        //启动子线程
        thread.start();
        //主线程休眠1秒
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getName() + ": main thread begin unpark");
        //调用unpark方法，唤醒子线程
        LockSupport.unpark(thread);
    }
}
