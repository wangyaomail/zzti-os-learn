package cn.edu.zut.chapter4;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import javafx.util.Pair;

/**
 * 连续分配，最佳适应
 */
public class mem_c extends Thread {
    public char[] mem;

    public mem_c(int size) {
        mem = new char[size];
        for (int i = 0; i < size; i++) {
            mem[i] = '_';
        }
        updateFreeList();
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(Collections.nCopies(100, "\n")
                                              .stream()
                                              .reduce((a,
                                                       b) -> a + b)
                                              .get());
                StringBuffer s1 = new StringBuffer("[");
                for (int i = 0; i < mem.length; i++) {
                    s1.append(mem[i]);
                }
                s1.append("]");
                StringBuffer s2 = new StringBuffer("[1");
                for (int i = 1; i < mem.length - 1; i++) {
                    s2.append("_");
                    if (mem[i] != mem[i + 1] && i != 0) {
                        s2.append(i + 1);
                        i += (i + "").length();
                    }
                }
                s2.append(mem.length + "]");
                System.out.println(new Date());
                System.out.println(s1);
                System.out.println(s2);
                System.out.println(waitkey);
                System.out.println(waitSize);
                System.out.println(size_start);
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    LinkedList<Character> waitkey = new LinkedList<Character>();
    LinkedList<Integer> waitSize = new LinkedList<Integer>();
    LinkedList<Pair<Integer, Integer>> size_start = new LinkedList<Pair<Integer, Integer>>();

    public void updateFreeList() {
        size_start.clear();
        int freecount = 0;
        for (int i = 0; i < mem.length; i++) {
            if (mem[i] == '_') {
                freecount++;
            } else {
                if (freecount > 0) {
                    size_start.add(new Pair<Integer, Integer>(freecount, i - freecount));
                    freecount = 0;
                }
            }
        }
        if (freecount > 0) {
            size_start.add(new Pair<Integer, Integer>(freecount, mem.length - freecount));
            freecount = 0;
        }
        Collections.sort(size_start, new Comparator<Pair<Integer, Integer>>() {
            public int compare(Pair<Integer, Integer> o1,
                               Pair<Integer, Integer> o2) {
                return o1.getKey() - o2.getKey();
            };
        });
    }

    public boolean allocate(char key,
                            int size) {
        boolean quit = false;
        for (Pair<Integer, Integer> entry : size_start) {
            if (entry.getKey() >= size) {
                for (int i = 0; i < size; i++) {
                    mem[entry.getValue() + i] = key;
                }
                updateFreeList();
                quit = true;
            }
        }
        if (!quit) {
            waitkey.push(key);
            waitSize.push(size);
        }
        return quit;
    }

    public void free(char key) {
        for (int i = 0; i < mem.length; i++) {
            if (mem[i] == key) {
                mem[i] = '_';
            }
        }
        updateFreeList();
        int waitnum = waitkey.size();
        for (int i = 0; i < waitnum; i++) {
            allocate(waitkey.poll(), waitSize.poll());
        }
    }

    public static void main(String[] args) throws Exception {
        mem_c m = new mem_c(100);
        m.start();
        Thread.sleep(2000);
        m.allocate('a', 30);
        Thread.sleep(2000);
        m.allocate('b', 40);
        Thread.sleep(2000);
        m.allocate('c', 50);
        Thread.sleep(3000);
        m.allocate('d', 20);
        Thread.sleep(3000);
        m.free('a');
        Thread.sleep(3000);
        m.free('d');
        Thread.sleep(3000);
        m.allocate('e', 30);
    }

}
