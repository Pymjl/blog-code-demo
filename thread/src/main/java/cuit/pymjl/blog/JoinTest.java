package cuit.pymjl.blog;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/13 11:16
 **/
public class JoinTest {
    public static void main(String[] args) {
        Thread threadOne = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("threadOne is over");
        });

        Thread threadTwo = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("threadTwo is over");
        });

        threadOne.start();
        threadTwo.start();
        System.out.println("wait all child thread over");
        try {
            threadOne.join();
            threadTwo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("all child thread over");
    }
}
