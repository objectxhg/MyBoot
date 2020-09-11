package com.xhg.utils;

import java.util.concurrent.Callable;
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
    }
}


class CallableDemo implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0;i<=100;i++){
            sum += i;
        }
        return sum;
    }
}