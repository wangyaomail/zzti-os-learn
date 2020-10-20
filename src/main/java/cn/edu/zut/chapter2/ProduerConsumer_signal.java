package cn.edu.zut.chapter2;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class ProduerConsumer_signal {
    public static Semaphore mutex=new Semaphore(1);
    public static Semaphore empty=new Semaphore(2);
    public static Semaphore full=new Semaphore(0);
    LinkedList<Integer> items=new LinkedList<Integer>();
    static int num=1, blank=0, count=0;
    public static void main(String[] args) throws Exception{
        ProduerConsumer_signal pc=new ProduerConsumer_signal();
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
                    empty.acquire();
                    mutex.acquire();
                    items.add(num++);
                    count++;
                    mutex.release();
                    full.release();
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
                    full.acquire();
                    mutex.acquire();
                    Integer pollNum=items.poll();
                    if(pollNum!=null){
                        count--;
                    }else{
                        blank++;
                    }
                    mutex.release();
                    empty.release();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }while(++i<100);
        }
    }
}
