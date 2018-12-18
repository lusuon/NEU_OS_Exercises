package producer_consumer;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ExecutorService;


/**
 * 多生产者、消费者问题
 */
public class Solution{
    // 创建仓库类，管理资源
    static Warehouse wareHouse = new Warehouse();

    // 生产者
    static class Producer implements Runnable {
        static int num = 1;
        @Override
        public void run() {
            int n = num++;//每个生产者的产量与生成顺序成正比
            while (true) {
                try {
                    wareHouse.produce(n);
                    System.out.println("Produce " + n+" products.");
                    // 休息1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // 消费者
    static class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("Consumed " + wareHouse.consume() +" products.");
                    // 休息1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 仓库 
     *
     */
    static class Warehouse {
        // 非满锁，最多允许10个生产者
        final Semaphore notFull = new Semaphore(10);
        // 非空锁  
        final Semaphore notEmpty = new Semaphore(0);
        // 核心锁  
        final Semaphore mutex = new Semaphore(1);
        // 库存容量  
        final Object[] items = new Object[10];
        int producePointer, consumePointer, count;

        public void produce(Object x) throws InterruptedException {
            // 保证非满，无法得到就阻塞
            notFull.acquire();
            // 保证操作临界区时不发生冲突
            mutex.acquire();
            try {
                // 增加库存  
                items[producePointer] = x;
                producePointer++;
                //实现循环
                if (producePointer == items.length) producePointer = 0;
                count++;
            } finally {
                // 退出临界区
                mutex.release();
                // 释放非空信号量，允许消费
                notEmpty.release();
            }
        }

        public Object consume() throws InterruptedException {
            // 保证非空，如果无法得到就阻塞
            notEmpty.acquire();
            // 保证操作临界区时不发生冲突
            mutex.acquire();
            try {
                // 减少库存  
                Object x = items[consumePointer];
                consumePointer++;
                //实现循环队列，到最后时回到数组头
                if (consumePointer == items.length) consumePointer = 0;
                count--;
                return x;
            } finally {
                // 退出核心区  
                mutex.release();
                // 释放非满的信号量，允许生产
                notFull.release();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 5; i++) {
            // 创建生产者、消费者各5个
            new Thread(new Producer()).start();
            new Thread(new Consumer()).start();
        }
    }
}
