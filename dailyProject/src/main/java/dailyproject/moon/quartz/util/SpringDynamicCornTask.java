package dailyproject.moon.quartz.util;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * 动态修改推荐主播的更新时间
 */
@Slf4j
@Lazy(false)
@Component
@EnableScheduling
public class SpringDynamicCornTask implements SchedulingConfigurer {


    private static String cron;
    private static String timerHour;


    /**
     * timerHour没有使用
     */
    public void restartCornTask(String newcCore, String timerHour) {
        cron = newcCore;
        timerHour = timerHour; //设置缓存有效时间为24小时
        log.info("cron change to : " + cron);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {

            @Override
            public void run() {
                // 任务逻辑
                log.info("-----------------------------------定时任务 is running...");
                try {
                    System.out.println("haha");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Trigger() {

            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 任务触发，可修改任务的执行周期
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExecutionTime = trigger.nextExecutionTime(triggerContext);
                return nextExecutionTime;
            }
        });
    }
}