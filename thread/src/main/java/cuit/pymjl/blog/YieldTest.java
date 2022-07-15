package cuit.pymjl.blog;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/13 11:21
 **/
public class YieldTest implements Runnable {
    public static void main(String[] args) {
        Thread threadOne = new Thread(new YieldTest());
        Thread threadTwo = new Thread(new YieldTest());
        threadOne.start();
        threadTwo.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            //当i==0时让出时间片
            if (i % 5 == 0) {
                System.out.println(Thread.currentThread().getName() + "：yield cpu......");
                Thread.yield();
            }
        }
        System.out.println(Thread.currentThread().getName() + ": this thread is over");
    }
}
