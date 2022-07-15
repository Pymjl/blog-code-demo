package cuit.pymjl.blog;

/**
 * 生产者
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/13 11:48
 **/
public class Provider extends Thread {
    private Storage mStorage;

    public Provider(Storage storage) {
        this.mStorage = storage;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            mStorage.produce(); // 不停的生产
        }
    }
}
