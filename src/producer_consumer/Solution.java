package producer_consumer;

import baraber.BarberShop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Solution{
    public final static int MUTEX = 1;
    public final static int EMPTY = 3;
    public final static int FULL = 0;
        public static void main(String[] args) {
            WareHouse warehouse = new WareHouse();
            ExecutorService pool = Executors.newCachedThreadPool();
            pool.execute(new Customer(50, warehouse));
            pool.execute(new Producer(20, warehouse));
            pool.execute(new Producer(30, warehouse));
            pool.execute(new Producer(20, warehouse));
            pool.shutdown();

            Semaphore mutex = new Semaphore(MUTEX);// 1个互斥锁
            Semaphore empty = new Semaphore(EMPTY);// 空信号量
            Semaphore full = new Semaphore(FULL);// 满信号量

            Thread barber = new Thread(new BarberShop.Barber(mutex, empty));//创建理发师线程
            barber.start();
        }

        /**
         * 仓库类，维护产品，管理生产和消费
         */
        public static class WareHouse {
            int num = 0;
            final int maxCapacity = 100;

            // 当前线程不含有当前对象的锁资源的时候，调用obj.wait()方法;调用obj.notify()方法。调用obj.notifyAll()方法。会有异常

            /**
             * 生产x个产品
             * @param x
             */
            public synchronized void product(int x) {
                while (num + x > maxCapacity) {
                    try {
                        System.out.println("超出容量，等待消费~");
                        
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                num = num + x;
                System.out.println("已生产~");
                notifyAll();//唤醒所有等待线程

                while (true)  {
                    //produce an item
                    wait (empty);
                    wait (mutex);
                    //add the item to the  buffer
                    signal (mutex);
                    signal (full);
                }
            }

            /**
             * 消费x个产品
             * @param x
             */
            public synchronized void consume(int x) {
                Semaphore full;
                Semaphore mutex;
                Semaphore empty;
                while (num - x < 0) {
                    try {
                        System.out.println("容量不足，等待生产~");
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                num = num - x;
                System.out.println("已消费~");
                notifyAll();
                while (true) {
                    wait (full);
                    wait (mutex);
                    //  remove an item from  buffer
                    signal (mutex);
                    signal (empty);
                    //  consume the removed item
                }
            }

            public int getnum() {
                return num;
            }
        }

        /**
         * 生产者类，执行时，生产给定数量的产品
         */
        public static class Producer implements Runnable {
                int speed;
                WareHouse warehouse;

                public Producer(int speed, WareHouse warehouse) {
                    this.speed = speed;
                    this.warehouse = warehouse;
                }

                @Override
                public void run() {
                    warehouse.product(speed);
                }
            }

        /**
         * 生产者类，执行时，生产给定数量的产品
         */
        public static class Customer implements Runnable {
            int speed;
            WareHouse warehouse;

            public Customer(int speed, WareHouse warehouse) {
                this.speed = speed;
                this.warehouse = warehouse;
            }

            @Override
            public void run() {
                warehouse.consume(speed);
            }
    }
}
