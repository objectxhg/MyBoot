package com.xhg.utils.demo;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
     * AtomicInteger i = new AtomicInteger
     * List<Integer> list = Collections.synchronizedList(new ArrayList<>());
     */

    private Lock lock = new ReentrantLock();

    AtomicInteger i = new AtomicInteger(5);

     int a = 5;

    public int getI(){
        return i.getAndIncrement();
    }

    @Override
    public void run() {
//        lock.lock();//上锁
        try {
            Thread.sleep(200);
//            if(i.get() >0) {
                i.getAndDecrement();
                System.out.println(i.get());
//            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
//            lock.unlock();
        }

    }
}

