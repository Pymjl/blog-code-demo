package cuit.pymjl;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/1 17:20
 **/
public class MyStack {
    private Queue<Integer> valueQueue;
    private Queue<Integer> auxiliaryQueue;


    public MyStack() {
        valueQueue = new LinkedList<>();
        auxiliaryQueue = new LinkedList<>();
    }

    public void push(int x) {
        valueQueue.offer(x);
    }

    public int pop() {
        int len = valueQueue.size();
        swap(len - 1, valueQueue, auxiliaryQueue);
        int result = valueQueue.poll();
        swap(len - 1, auxiliaryQueue, valueQueue);
        return result;
    }

    public int top() {
        int len = valueQueue.size();
        swap(len - 1, valueQueue, auxiliaryQueue);
        int result = valueQueue.element();
        swap(len - 1, auxiliaryQueue, valueQueue);
        return result;

    }

    public boolean empty() {
        return valueQueue.isEmpty();

    }

    private void swap(int len, Queue<Integer> source, Queue<Integer> dest) {
        for (int i = 0; i < len; i++) {
            dest.offer(source.poll());
        }
    }
}
