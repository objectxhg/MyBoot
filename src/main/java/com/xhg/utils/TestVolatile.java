package com.xhg.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile 内存可见性 不具备原子性
 * 用CAS算法保证原子性。
 */

public class TestVolatile {
    public static void main(String[] args){
        ThreadDemo threadDemo = new ThreadDemo();
        Thread thread = new Thread(threadDemo);
        //thread.start();
        for(int i = 0; i < 10; i++){
            new Thread(threadDemo).start();
        }
    }
}

@Data
class ThreadDemo implements Runnable{
    /**
     * JDK 1.5之后，Java提供了原子变量，在java.util.concurrent.atomic包下
     * List<Integer> list = Collections.synchronizedList(new ArrayList<>());
     */
    AtomicInteger i = new AtomicInteger();



    public int getI(){
        return i.getAndIncrement();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(getI());
    }
}

