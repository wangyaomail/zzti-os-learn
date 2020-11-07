package cn.edu.zut.chapter4;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

/**
 * 连续分配，循环首次适应
 */
public class mem_b extends Thread {
    public char[] mem;

    public mem_b(int size) {
        mem = new char[size];
        for (int i = 0; i < size; i++) {
            mem[i] = '_';
        }
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
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    int lastpos = 0;
    LinkedList<Character> waitkey = new LinkedList<Character>();
    LinkedList<Integer> waitSize = new LinkedList<Integer>();

    public boolean allocate(char key,
                            int size) {
        int freemem = 0, begin = lastpos;
        boolean quit = false;
        while (true) {
            if (mem[lastpos] == '_') {
                freemem++;
                if (freemem == size) {
                    lastpos++;
                    for (int i = 0; i < size; i++) {
                        mem[(i + lastpos - size + mem.length) % mem.length] = key;
                    }
                    return true;
                }
            } else {
                freemem = 0;
                if (quit) {
                    break;
                }
            }
            lastpos = (++lastpos) % mem.length;
            if (lastpos == begin) {
                quit = true;
            }
        }
        waitkey.push(key);
        waitSize.push(size);
        return false;
    }

    public void free(char key) {
        for (int i = 0; i < mem.length; i++) {
            if (mem[i] == key) {
                mem[i] = '_';
            }
        }
        int waitnum = waitkey.size();
        for (int i = 0; i < waitnum; i++) {
            allocate(waitkey.poll(), waitSize.poll());
        }
    }

    public static void main(String[] args) throws Exception {
        mem_b m = new mem_b(100);
        m.start();
        Thread.sleep(3000);
        m.allocate('a', 30);
        Thread.sleep(3000);
        m.allocate('b', 40);
        Thread.sleep(3000);
        m.allocate('c', 50);
        Thread.sleep(3000);
        m.allocate('d', 20);
        Thread.sleep(3000);
        m.free('a');
        Thread.sleep(3000);
        m.free('d');
    }

}
