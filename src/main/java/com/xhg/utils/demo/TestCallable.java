package com.xhg.utils.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author xiaoh
 * @create 2020/9/11 17:05
 */
public class TestCallable {

    public static void main(String[] args) {

        CallableDemo callableDemo = new CallableDemo();
        FutureTask<Integer> futureTask = new FutureTask<>(callableDemo);
        Thread thread = new Thread(futureTask);
        System.out.println("--------- 线程启动执行中。。。。。");
        thread.start();

        try {
            System.out.println("------- 返回值：" + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}


class CallableDemo implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i <= 10; i++){
            sum += i;
        }
        Thread.sleep(3000);
        return sum;
    }
}