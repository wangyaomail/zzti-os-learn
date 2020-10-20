package cn.edu.zut.chapter3;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 破坏死锁
 */
public class deadlock_c {
    public static Object m1 = new Object();
    public static Object m2 = new Object();
    public static Object m3 = new Object();
    public static AtomicInteger count = new AtomicInteger(0);

    static class Process extends Thread {
        public Object x, y;

        Process(Object x,
                Object y) {
            this.x = x;
            this.y = y;
        }

        public void run() {
            int i = 0;
            do {
                try {
                    synchronized (x) {
                        // 方式2
                        x.wait(1000);
                    }
                    System.out.println(count.incrementAndGet());
                    synchronized (y) {
                        y.notify();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (++i < 1000);
        }
    }

    public static void main(String[] args) {
        try {
            Process p1 = new Process(m1, m3);
            Process p2 = new Process(m2, m1);
            Process p3 = new Process(m3, m2);
            p1.start();
            p2.start();
            p3.start();
            p1.join();
            p2.join();
            p3.join();
            System.out.println("最终的count值=" + count.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
