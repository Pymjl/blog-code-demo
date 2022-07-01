package cuit.pymjl.thradlocal;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/1 10:56
 **/
public class MainTest {
    static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    static void print(String str) {
        //打印当前线程中本地内存中本地变量的值
        System.out.println(str + " :" + threadLocal.get());
        //清除本地内存中的本地变量
        threadLocal.remove();
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程1中本地变量的值
                threadLocal.set("thread1 local variable");
                //调用打印方法
                print("thread1");
                //打印本地变量
                System.out.println("after remove : " + threadLocal.get());
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程1中本地变量的值
                threadLocal.set("thread2 local variable");
                //调用打印方法
                print("thread2");
                //打印本地变量
                System.out.println("after remove : " + threadLocal.get());
            }
        });

        t1.start();
        t2.start();
    }
}
