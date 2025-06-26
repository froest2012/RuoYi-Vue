package com.ruoyi.common.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author 洪刘涛
 * @date 2024/3/13 15:11
 */
@Configuration
@EnableAsync
@Slf4j
public class ThreadPoolExecutorConfig {

    /**
     * 跟 AI 交互的线程池
     */
    @Bean(name = "difyExecutor")
    public ThreadPoolTaskExecutor aiExecutor() {
        // TODO 线程池参数需要优化
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(21);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("difyExecutor-");
        executor.setRejectedExecutionHandler(new CustomerCallerRunsExecutionHandler());
        executor.initialize();
        return executor;
    }



    private static class CustomerCallerRunsExecutionHandler extends ThreadPoolExecutor.CallerRunsPolicy {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            log.warn("rejected execution of task [{}] with executor [{}]", r, e);
            super.rejectedExecution(r, e);
        }
    }

}
