package cuit.pymjl.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/6 11:11
 **/
public class Main {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(false);
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        rwLock.writeLock().lock();
        rwLock.writeLock().unlock();
        rwLock.readLock().lock();
        rwLock.readLock().unlock();
        Condition condition = lock.newCondition();
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        queue.offer(1);
        Main main = new Main();
        main.test();
    }

    public void test() {
        LockSupport.park(this);
    }
}
