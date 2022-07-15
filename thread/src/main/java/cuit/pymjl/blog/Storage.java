package cuit.pymjl.blog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/13 11:44
 **/
public class Storage {
    /**
     * 仓库的最大容量
     */
    private static final int MAX_COUNT = 10;
    /**
     * 公共资源，需要互斥的地方
     */
    private List<Production> mProductions = new ArrayList<Production>();

    /**
     * 可重入锁
     */
    private ReentrantLock mReentrantLock = new ReentrantLock();
    /**
     * 条件变量
     */
    private Condition mCondition = mReentrantLock.newCondition();
    /**
     * 货物索引
     */
    private int mIndex;

    public void produce() {
        try {
            // 获取锁，再访问公共资源
            mReentrantLock.lock();
            if (mProductions.size() >= MAX_COUNT) {
                System.out.println("produce await");
                // 货物充足时停止生产
                mCondition.await();
            }
            // 生成的耗时
            Thread.sleep((long) (Math.random() * 1000));
            Production production = new Production(mIndex++);
            System.out.println("producer produce： " + production.toString());
            mProductions.add(production);
            // 发个信号告知消费者，即唤醒消费者
            mCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 放在finally块中保证一定会释放锁
            mReentrantLock.unlock();
        }
    }

    public void consume() {
        try {
            // 获取锁，再访问公共资源
            mReentrantLock.lock();
            if (mProductions.size() <= 0) {
                System.out.println("consume await");
                // 货物不足时停止消费，即阻塞挂起
                mCondition.await();
            }
            // 消费的耗时
            Thread.sleep((long) (Math.random() * 1000));
            Production production = mProductions.remove(0);
            System.out.println("consumer consume： " + production.toString());
            // 发个信号告知生产者，即唤醒生产者
            mCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 放在finally块中保证一定会释放锁
            mReentrantLock.unlock();
        }
    }

    public static class Production {
        public int index;

        public Production(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "Production [index=" + index + "]";
        }
    }
}
