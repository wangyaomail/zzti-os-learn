package cn.edu.zut.chapter2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestCondition {
    public static Lock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();
    public static void main(String[] args) {
        new Thread("a") {
            public void run() {
                lock.lock();
                try {
                    System.out.println(getName() + "拿到锁了");
                    System.out.println(getName() + "等待信号");
                    condition.await();
                    System.out.println(getName() + "拿到信号");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            };
        }.start();
        new Thread("b") {
            public void run() {
                lock.lock();
                try {
                    Thread.sleep(5000);
                    System.out.println(getName() + "拿到锁了");
                    condition.signal();
                    System.out.println(getName() + "发出信号");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            };
        }.start();
    }
}
