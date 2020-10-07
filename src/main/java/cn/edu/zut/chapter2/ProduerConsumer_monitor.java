package cn.edu.zut.chapter2;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ProduerConsumer_monitor {
    LinkedBlockingQueue<Integer> items=new LinkedBlockingQueue<Integer>();
    static int num=1, blank=0;
    static AtomicInteger count=new AtomicInteger(0);
    public static void main(String[] args) throws Exception{
        ProduerConsumer_monitor pc=new ProduerConsumer_monitor();
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
        System.out.println("count="+count.get());
        System.out.println("blank="+blank);
    }
    class Producer extends Thread {
        public void run(){
            int i=0;
            do{
                try{
                    items.put(num++);
                    count.incrementAndGet();
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
                    Integer pollNum=items.take();
                    if(pollNum!=null){
                        count.decrementAndGet();
                    }else{
                        blank++;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }while(++i<100);
        }
    }
}
