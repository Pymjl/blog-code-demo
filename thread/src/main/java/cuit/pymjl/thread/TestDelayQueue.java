package cuit.pymjl.thread;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/7 11:59
 **/
public class TestDelayQueue {
    static class DelayedEle implements Delayed {
        /**
         * 延迟时间
         */
        private final long delayTime;
        /**
         * 任务名称
         */
        private final String taskName;
        /**
         * 到期时间
         */
        private final long expireTime;

        public DelayedEle(long delayTime, String taskName) {
            this.delayTime = delayTime;
            this.taskName = taskName;
            this.expireTime = System.currentTimeMillis() + delayTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "DelayedEle{" +
                    "delayTime=" + delayTime +
                    ", taskName='" + taskName + '\'' +
                    ", expireTime=" + expireTime +
                    '}';
        }
    }

    public static void main(String[] args) {
        //1.创建一个延迟队列
        DelayQueue<DelayedEle> queue = new DelayQueue<>();
        //2.添加任务到队列中
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            queue.offer(new DelayedEle(random.nextInt(500), "task" + i));
        }
        //3.获取任务
        DelayedEle delayedEle = null;
        while (true) {
            while ((delayedEle = queue.poll()) != null) {
                System.out.println(delayedEle);
            }
        }
    }
}
