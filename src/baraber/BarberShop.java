package baraber;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class BarberShop {
    public final static int CHAIRS =3;
    public final static int CUSTOMERS = 5;
    public final static int SLEEP = 1;

    public static class Barber implements Runnable {
        Semaphore chair;
        Semaphore sleep;

        public Barber(Semaphore chair, Semaphore sleep) {
            this.chair = chair;
            this.sleep = sleep;
        }

        @Override
        public void run() {
            synchronized (this) {
                try {
                    sleep.acquire();//通过获取sleep信号量，模拟理发师正在睡觉
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                while (true) {
                    if(sleep.availablePermits() == 0) System.out.println("The barber is sleeping.The semaphore of sleeping:"+sleep.availablePermits());
                    while(sleep.availablePermits() == 0){}//睡觉过程中什么都不干
                    try {
                        Thread.sleep(1000); // 模拟理发师每秒完成一件工作
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Barber finished one of his jobs,current available chairs:"+chair.availablePermits());
                    chair.release();
                    if(chair.availablePermits() == CHAIRS){//完成所有工作，空闲
                        try {
                            sleep.acquire();
                            System.out.println("Finsih all the job, the barber can have a rest now.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }

    public static class Customer implements Runnable {
        Semaphore chair;
        Semaphore sleep;

        public Customer(Semaphore chair, Semaphore sleep) {
            this.chair = chair;
            this.sleep = sleep;
        }
        @Override
        public void run() {

            synchronized (this) {
                if (chair.tryAcquire())// 能获得信号量，说明有空椅子
                {
                    System.out.println("A customer come in, current available chairs: " + chair.availablePermits());
                    if (chair.availablePermits() == CHAIRS-1){
                        sleep.release();//第一个到的顾客唤醒理发师，释放信号量
                        System.out.println("The customer woke the barber up.The semaphore of sleeping:"+sleep.availablePermits());
                    }
                } else {
                    System.out.println("No chair available,customer left.");
                }

            }
        }
    }

    public static void main(String[] args) {
        Semaphore chair = new Semaphore(CHAIRS);// 空椅子数
        Semaphore sleep = new Semaphore(SLEEP);// 初始理发师在睡觉（相当于挂起，而不是执行睡觉）
        Thread barber = new Thread(new Barber(chair, sleep));//创建理发师线程
        barber.start();

        try {
            Thread.sleep(1000);//主线程休眠1秒
            System.out.println("Start generating customer.");
            for(int i = 0;i<CUSTOMERS;i++){
                Thread customer = new Thread(new Customer(chair, sleep));
                customer.start();
                Thread.sleep(100);//每0.1秒创建1个顾客线程
            }
            Thread.sleep(5000);
            System.out.println("To check whether the barber can be woke up again, we sent another customer.");
            Thread customer = new Thread(new Customer(chair, sleep));
            customer.start();
            System.out.println("No more customer will get in the shop today.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
