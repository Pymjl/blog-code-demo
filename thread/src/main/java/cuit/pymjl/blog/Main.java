package cuit.pymjl.blog;


/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/13 11:49
 **/
public class Main {
    public static void main(String[] args) {
        Storage storage = new Storage(); // 创建一个仓库
        Provider provider = new Provider(storage); // 创建生产者线程
        Consumer consumer = new Consumer(storage); // 创建消费者线程
        provider.start();
        consumer.start();
    }
}
