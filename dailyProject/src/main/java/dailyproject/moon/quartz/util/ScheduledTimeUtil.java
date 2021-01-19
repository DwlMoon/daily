package dailyproject.moon.quartz.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.concurrent.ScheduledFuture;

/**
 * @program: spring 动态cron时间触发
 * @description:
 * @create: 2021-01-19 17:39
 **/

@Component
@EnableScheduling
public class ScheduledTimeUtil {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ScheduledFuture future;


    @Bean
    public ThreadPoolTaskScheduler trPoolTaskScheduler(){
        return new ThreadPoolTaskScheduler();
    }


    /**
     * 启动定时器
     * @return
     */

    public String StartTest(){
        /**
         * task:定时任务要执行的方法
         * trigger:定时任务执行的时间
         */
        future=threadPoolTaskScheduler.schedule(new myRunable(),new CronTrigger("0/5 * * * * *") );

        return "startTest";
    }

    /**
     * 停止定时任务
     * @return
     */
    public String endTask(){
        if(future!=null){
            future.cancel(true);
        }
        System.out.println("endTask");
        return "endTask";
    }

    /**
     * 改变调度的时间
     * 步骤：
     * 1,先停止定时器
     * 2,在启动定时器
     */
    public String changeTask(String corn){
        //停止定时器
        endTask();
        //定义新的执行时间
        future=threadPoolTaskScheduler.schedule(new myRunable(),new CronTrigger(corn));
        //启动定时器
        StartTest();
        System.out.println("changeTask");
        return "changeTask";
    }

    /**
     * 定义定时任务执行的方法
     * @author Admin
     *
     */
    public class myRunable implements Runnable{
        @Override
        public void run() {
            try {
                System.out.println("lalala");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("定时任务要执行的方法"+ LocalTime.now());
        }

    }

}
