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
        static int produce_num = 1;
        @Override
        public void run() {
            int n = produce_num++;//每个生产者的产量与生成顺序成正比
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
        // 非满锁
        final Semaphore notFull = new Semaphore(1);
        // 非空锁，用于允许消费者进行消费
        final Semaphore notEmpty = new Semaphore(0);
        // 核心锁  
        final Semaphore mutex = new Semaphore(1);
        // 库存容量  
        final Object[] items = new Object[10];
        // 指针，指定当前生产者消费者
        int producePointer, consumePointer;

        public void produce(Object x) throws InterruptedException {
            // 保证非满，无法得到就阻塞
            System.out.println("Requesting the notFull Semaphore.");
            notFull.acquire();
            System.out.println("Acquired notFull semaphore,requesting the mutex.");
            // 保证操作临界区时不发生冲突
            mutex.acquire();
            System.out.println("Mutex acquired,start producing");
            try {
                // 增加库存  
                items[producePointer] = x;
                producePointer++;
                //实现循环
                if (producePointer == items.length) producePointer = 0;
            } finally {
                // 退出临界区
                mutex.release();
                // 释放非空信号量，允许消费
                notEmpty.release();
            }
        }

        public Object consume() throws InterruptedException {
            // 保证非空，如果无法得到就阻塞
            System.out.println("Requesting the notEmpty semaphore.");
            notEmpty.acquire();
            System.out.println("Acquired notEmpty semaphore,requesting the mutex.");
            // 保证操作临界区时不发生冲突
            mutex.acquire();
            System.out.println("Mutex acquired,start consuming");
            try {
                // 减少库存  
                Object x = items[consumePointer];
                consumePointer++;
                //实现循环队列，到最后时回到数组头
                if (consumePointer == items.length) consumePointer = 0;
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
        for (int i = 0; i <= 3; i++) {
            // 创建生产者、消费者各3个
            new Thread(new Producer()).start();
            new Thread(new Consumer()).start();
        }
    }
}
