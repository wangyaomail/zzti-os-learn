package cn.edu.zut.chapter4;

import java.util.Collections;
import java.util.Date;

/**
 * 原始内存，按地址分配
 */
public class mem_a extends Thread {
    public char[] mem;

    public mem_a(int size) {
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
                        s2.append(i+1);
                        i += (i + "").length();
                    }
                }
                s2.append(mem.length  + "]");
                System.out.println(new Date());
                System.out.println(s1);
                System.out.println(s2);
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void allocate(char key,
                         int start,
                         int length) {
        for (int i = start; i < start + length; i++) {
            mem[i] = key;
        }
    }

    public void free(char key) {
        for (int i = 0; i < mem.length; i++) {
            if (mem[i] == key) {
                mem[i] = '_';
            }
        }
    }

    public static void main(String[] args) throws Exception {
        mem_a m = new mem_a(100);
        m.start();
        Thread.sleep(2000);
        m.allocate('a', 15, 30);
        Thread.sleep(2000);
        m.allocate('b', 50, 10);
        Thread.sleep(4000);
        m.free('a');
    }

}
