package com.xhg.utils.demo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author xiaoh
 * @create 2020/9/14 11:41
 */
public class TestLock {

    public static void main(String[] args) {

        Ticket td = new Ticket();
        new Thread(td, "窗口1").start();
        new Thread(td, "窗口2").start();
        new Thread(td, "窗口3").start();
        new Thread(td, "窗口4").start();
        new Thread(td, "窗口5").start();

        ConcurrentHashMap currMap = new ConcurrentHashMap();
        currMap.put("1", 1);
    }

}

class Ticket implements Runnable {

    /**
     * 创建lock锁
     */
    private Lock lock = new ReentrantLock();

    private  int  ticket = 3;

    @Override
    public void run() {
//        while (true) {
            lock.lock();//上锁
            try {
                if (ticket > 0) {
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {
                    }
                    System.out.println(Thread.currentThread().getName() + "完成售票，余票为：" + (--ticket));
                }else{
                    System.out.println(Thread.currentThread().getName() + "售票失败，余票已经为0了");
                }
            }finally {
                lock.unlock();//释放锁
            }

//        }
    }
}