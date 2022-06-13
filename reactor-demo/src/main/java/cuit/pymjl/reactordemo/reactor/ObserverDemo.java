package cuit.pymjl.reactordemo.reactor;

import java.util.Observable;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/13 22:06
 **/
public class ObserverDemo extends Observable {
    public static void main(String[] args) {
        ObserverDemo observerDemo = new ObserverDemo();
        observerDemo.addObserver(((o, arg) -> {
            System.out.println("发生了变化");
        }));

        observerDemo.addObserver(((o, arg) -> {
            System.out.println("被观察者通知，准备改变");
        }));

        //设置数据变化
        observerDemo.setChanged();
        //通知
        observerDemo.notifyObservers();
    }
}
