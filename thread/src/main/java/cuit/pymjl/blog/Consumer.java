package cuit.pymjl.blog;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/13 11:49
 **/
public class Consumer extends Thread {
    private Storage mStorage;

    public Consumer(Storage mstorage) {
        this.mStorage = mstorage;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            mStorage.consume(); // 不停的消费
        }
    }
}
