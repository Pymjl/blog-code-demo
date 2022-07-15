package cuit.pymjl.blog;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/12 23:09
 **/
public class RunnableTask implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": this is a child thread");
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": this is the main thread");
        //创建线程
        RunnableTask runnableTask = new RunnableTask();
        //启动线程
        new Thread(runnableTask).start();
        new Thread(runnableTask).start();
    }
}
