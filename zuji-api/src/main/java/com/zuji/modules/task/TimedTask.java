package com.zuji.modules.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @author Ink足迹
 * @create 2020-06-18 17:47
 **/
@Component
@EnableScheduling
public class TimedTask {
    private final static Logger LOG = LoggerFactory.getLogger(TimedTask.class);

    /**
     * 测试
     * <p>
     * 每10s执行一次
     */
    @Async
    @Scheduled(cron = "0/10 * * * * *")
    public void timingTask() {
        LOG.info(" test ");
    }

}
