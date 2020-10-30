package com.zuji.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 配置定时任务线程池
 *
 * @author Ink足迹
 **/
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    private TaskScheduler myThreadPooleTaskScheduler;

    @Autowired
    public void setMyThreadPooleTaskScheduler(TaskScheduler myThreadPooleTaskScheduler) {
        this.myThreadPooleTaskScheduler = myThreadPooleTaskScheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(myThreadPooleTaskScheduler);
    }

    @Bean("myThreadPooleTaskScheduler")
    public TaskScheduler myThreadPooleTaskScheduler() {
        ThreadPoolTaskScheduler pool = new ThreadPoolTaskScheduler();
        pool.setPoolSize(15);
        pool.setAwaitTerminationSeconds(60);
        pool.setThreadNamePrefix("scheduler timer task - ");
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }

    @Bean("myThreadPooleTaskExecutor")
    public ThreadPoolTaskExecutor myThreadPooleTaskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(5);
        pool.setMaxPoolSize(20);
        pool.setKeepAliveSeconds(120);
        pool.setQueueCapacity(1000);
        pool.setAllowCoreThreadTimeOut(false);
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        pool.initialize();
        return pool;
    }
}
