package com.xhg.threadPool;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
@SuppressWarnings("all")
public class AsyncTaskConfig implements AsyncConfigurer{
	
	@Override
    @Bean("taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();

        //  线程名称前缀
        threadPool.setThreadNamePrefix("Async-Thread-");
        //设置核心线程数
        threadPool.setCorePoolSize(20);
        //设置最大线程数
        threadPool.setMaxPoolSize(200);
        //线程池所使用的缓冲队列
        threadPool.setQueueCapacity(25);
        //线程池维护线程所允许的空闲时间
        threadPool.setKeepAliveSeconds(200);
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPool.setAwaitTerminationSeconds(60);

        // 初始化线程
        threadPool.initialize();

        return threadPool;
    }

    /**
     * 异步方法执行的过程中抛出的异常捕获
     *
     * 支持springboot使用@Async的注解，@Async的注解是另外启一个线程去执行方法，无法被@RestControllerAdvice该注解获取异常，异常处理需要自己手动处理。
     */
    /*异步任务中异常处理*/
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable ex, Method method, Object... params)->{
            //todo 异步方法异常处理
            System.out.println("class#method: " + method.getDeclaringClass().getName() + "#" + method.getName());
            System.out.println("type        : " + ex.getClass().getName());
            System.out.println("exception   : " + ex.getMessage());
        };
    }

}
