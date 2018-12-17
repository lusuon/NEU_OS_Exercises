package producer_consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Solution{

        public static void main(String[] args) {
            WareHouse warehouse = new WareHouse();
            ExecutorService pool = Executors.newCachedThreadPool();
            pool.execute(new Customer(50, warehouse));
            pool.execute(new Producer(20, warehouse));
            pool.execute(new Producer(30, warehouse));
            pool.execute(new Producer(20, warehouse));

            pool.shutdown();
        }

        public static class WareHouse {
            int num = 0;
            final int maxnum = 100;

            // 当前线程不含有当前对象的锁资源的时候，调用obj.wait()方法;调用obj.notify()方法。调用obj.notifyAll()方法。会有异常
            public synchronized void product(int x) {
                while (num + x > maxnum) {
                    try {
                        System.out.println("超出容量，等待消费~");
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                num = num + x;
                System.out.println("已生产~");
                notifyAll();
            }

            public synchronized void consume(int x) {
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
            }

            public int getnum() {
                return num;
            }
        }

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
