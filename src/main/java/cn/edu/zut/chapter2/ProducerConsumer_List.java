package cn.edu.zut.chapter2;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumer_List {
    LinkedList<Integer> items = new LinkedList<Integer>();

    public static void main(String args[]) {
        ProducerConsumer_List pc = new ProducerConsumer_List();
        Thread t1 = new Thread(pc.new Producer());
        Consumer consumer = pc.new Consumer();
        Thread t2 = new Thread(consumer);
        Thread t3 = new Thread(consumer);
        Thread t4 = new Thread(consumer);
        t1.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        t2.start();
        t3.start();
        t4.start();
        try {
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class Producer implements Runnable {
        public void produce(int i) {
            System.out.println("Producing " + i);
            items.add(new Integer(i));
        }

        @Override
        public void run() {
            int i = 0;
            while (i++ < 100) {
                produce(i);
                // items.notifyAll();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    class Consumer implements Runnable {
        AtomicInteger consumed = new AtomicInteger();

        public void consume() {
            System.out.println(Thread.currentThread().getName() + " Consuming " + items.poll()
                    + ", total comsume=" + consumed.incrementAndGet());
        }

        @Override
        public void run() {
            while (true) {
                if (!items.isEmpty()) {
                    consume();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
