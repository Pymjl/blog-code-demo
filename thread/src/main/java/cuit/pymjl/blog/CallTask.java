package cuit.pymjl.blog;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/12 23:13
 **/
public class CallTask implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + ": Asynchronous task starts executing");
        //睡眠一秒
        Thread.sleep(1000);
        //返回结果
        return "The task is executed successfully and the result is returned";
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": this is the main thread");
        //创建异步任务
        FutureTask<String> futureTask = new FutureTask<>(new CallTask());
        //启动异步任务
        new Thread(futureTask).start();
        String result = null;
        try {
            //等待任务异步执行，获取异步任务结果
            result = futureTask.get();
            System.out.println(Thread.currentThread().getName() + ": " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }
}
