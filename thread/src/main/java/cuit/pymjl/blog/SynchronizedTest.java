package cuit.pymjl.blog;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/13 10:30
 **/
public class SynchronizedTest {
    //锁
    private final Object lock = new Object();
    //计数器
    private int count;
    //循环打印的次数
    private int limit;

    public SynchronizedTest(int count, int limit) {
        this.count = count;
        this.limit = limit;
    }

    private void print() {
        //同步代码块
        synchronized (lock) {
            //判断计数器是否达到了循环次数,循环打印
            while (count < limit) {
                try {
                    //打印后唤醒其他线程，再将自己阻塞挂起，循环打印
                    System.out.println(Thread.currentThread().getName() + ": " + count);
                    count++;
                    lock.notifyAll();
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //打印完后唤醒主线程，主线程结束
            lock.notifyAll();
        }
    }

    public static void main(String[] args) {
        SynchronizedTest synchronizedTest = new SynchronizedTest(0, 10);
        Thread thread1 = new Thread(synchronizedTest::print, "odd");
        Thread thread2 = new Thread(synchronizedTest::print, "even");
        thread2.start();
        thread1.start();

    }
}
