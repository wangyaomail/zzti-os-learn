package cn.edu.zut.chapter2;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProduerConsumer_and {
    static class AndSignal {
        Lock lock=new ReentrantLock();
        Condition condition=lock.newCondition();
        public void Swait(String id,
                          Semaphore... list)
                throws InterruptedException{
            lock.lock();
            while(true){
                boolean towait=false;
                for(Semaphore semaphore: list){
                    if(semaphore.availablePermits()<=0){
                        towait=true;
                        break;
                    }
                }
                if(towait){
                    condition.await();
                }else{
                    break;
                }
            }
            for(Semaphore semaphore: list){
                semaphore.acquire();
            }
            lock.unlock();
        }
        public void Ssignal(String id,
                            Semaphore... list)
                throws InterruptedException{
            lock.lock();
            for(Semaphore semaphore: list){
                semaphore.release();
            }
            condition.signalAll();
            lock.unlock();
        }
    }
    public static Semaphore mutex=new Semaphore(1);
    public static Semaphore empty=new Semaphore(2);
    public static Semaphore full=new Semaphore(0);
    public static AndSignal andSignal=new AndSignal();
    LinkedList<Integer> items=new LinkedList<Integer>();
    static int num=1, blank=0, count=0;
    public static void main(String[] args) throws Exception{
        ProduerConsumer_and pc=new ProduerConsumer_and();
        ArrayList<Thread> allThreads=new ArrayList<Thread>();
        for(int i=0;i<10;i++){
            Thread p=pc.new Producer();
            Thread c=pc.new Consumer();
            p.start();
            c.start();
            allThreads.add(p);
            allThreads.add(c);
        }
        for(Thread t: allThreads){
            t.join();
        }
        System.out.println("count="+count);
        System.out.println("blank="+blank);
    }
    class Producer extends Thread {
        public void run(){
            int i=0;
            do{
                try{
                    andSignal.Swait(this.getName(), empty, mutex);
                    items.add(num++);
                    count++;
                    andSignal.Ssignal(this.getName(), mutex, full);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }while(++i<100);
        }
    }
    class Consumer extends Thread {
        public void run(){
            int i=0;
            do{
                try{
                    andSignal.Swait(this.getName(), full, mutex);
                    Integer pollNum=items.poll();
                    if(pollNum!=null){
                        count--;
                    }else{
                        blank++;
                    }
                    andSignal.Ssignal(this.getName(), mutex, empty);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }while(++i<100);
        }
    }
}
