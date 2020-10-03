package cn.edu.zut.chapter2;

import java.util.concurrent.Semaphore;

public class TestSemaphore {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        for (int i = 0; i < 20; i++) {
            new Thread("t_" + i) {
                public void run() {
                    System.out.println("线程" + getName() + "开始执行");
                    try {
                        semaphore.acquire();
                        System.out.println("线程" + getName() + "得到资源");
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("线程" + getName() + "释放资源");
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
            }.start();
        }
    }
}
