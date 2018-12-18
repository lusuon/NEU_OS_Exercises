package producer_consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import producer_consumer.Solution.WareHouse;
import sun.invoke.empty.Empty;

public class Solution{
    public final static int MUTEX = 1;
    public final static int EMPTY = 1;
    public final static int FULL = 0;

    public static class WareHouse implements Runnable{
        int products = 0;
        final int maxCapacity = 100;
        Semaphore full;
        Semaphore mutex;
        Semaphore empty;
        
        /**
         * @return the products
         */
        public int getProducts() {
            return products;
        }
        /**
         * @param products the products to set
         */
        public void setProducts(int products) {
            this.products = products;
        }



        @Override
        public void run() {
            while(true){
                synchronized (this){
                    while (true) {
                        
                        

                    }
                }
                
            }
        }

        
    }

    /**
     * 生产者类，执行时，生产给定数量的产品
     */
    public static class Producer implements Runnable {
        Semaphore empty;
        Semaphore mutex;
        Semaphore full;
        int speed;
        WareHouse warehouse;

        public Producer(int speed, WareHouse warehouse) {
            this.speed = speed;
            this.warehouse = warehouse;
        }
            
        public synchronized void product(int x,WareHouse wareHouse) {
            while (true)  {
                if(mutex.tryAcquire()){
                    
                }else{

                }
                if(products == maxCapacity){
                    System.out.println("The warehouse is full now,producer stop producing.");
                }
                sleep(1000);
                

                //produce an item
                wait (empty);
                wait (mutex);
                //add the item to the  buffer
                signal (mutex);
                signal (full);
            }
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
                        if(products == 0){
                            System.out.println("The warehouse is empty now,consumer stop consuming.");
                        }
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
        @Override
        public void run() {
            warehouse.consume(speed);
        }
    }
    
    public static void main(String[] args) {
        WareHouse warehouse = new WareHouse();

        Semaphore mutex = new Semaphore(MUTEX);// 1个互斥锁
        Semaphore empty = new Semaphore(EMPTY);// 空信号量
        Semaphore full = new Semaphore(FULL);// 满信号量

        Thread barber = new Thread(new BarberShop.Barber(mutex, empty));//创建理发师线程
            barber.start();
        }
}
