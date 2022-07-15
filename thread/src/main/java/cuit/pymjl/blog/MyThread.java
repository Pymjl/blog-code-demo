package cuit.pymjl.blog;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/12 23:04
 **/
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": this is a child thread");
    }

    private void print() {
        System.out.println(Thread.currentThread().getName() + ": wewewewewew");
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": this is the main thread");
        //创建线程
        MyThread myThread = new MyThread();
        //启动线程
        myThread.start();
        Thread thread = new Thread(myThread::print);
        thread.start();
    }
}
