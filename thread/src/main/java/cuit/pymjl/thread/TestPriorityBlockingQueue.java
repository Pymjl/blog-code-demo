package cuit.pymjl.thread;

import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/7 11:41
 **/
public class TestPriorityBlockingQueue {
    static class Task implements Comparable<Task> {
        private int priority = 0;
        private String taskName;

        public Task(int priority, String taskName) {
            this.priority = priority;
            this.taskName = taskName;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public int compareTo(Task o) {
            return this.priority - o.priority;
        }

        @Override
        public String toString() {
            return "Task{" +
                    "priority=" + priority +
                    ", taskName='" + taskName + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            queue.offer(new Task(random.nextInt(10), "task" + i));
        }

        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }
}
