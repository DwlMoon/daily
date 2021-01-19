package dailyproject.moon.quartz.controller;

import dailyproject.moon.quartz.service.quartzJob;
import dailyproject.moon.quartz.util.ScheduledTimeUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-01-08 19:48
 **/

@Controller
@RequestMapping("quartz")
public class QuartzConroller {

    @Autowired
    Scheduler sched;

    @Autowired
    ScheduledTimeUtil scheduledTimeUtil;

    @PostConstruct
    public void init(){
        try {
            sched.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    @ResponseBody
    @RequestMapping(value="addjob", method = {RequestMethod.GET})
    public String addjob(){

        try {
            String jobClassName="haha";

            String count="16";

            Calendar beforeTime = Calendar.getInstance();
            beforeTime.add(Calendar.SECOND, 30);// 3分钟之后的时间
            Date useDate = beforeTime.getTime();


//            SchedulerFactory sf = new StdSchedulerFactory();

//            Scheduler sched = StdSchedulerFactory.getDefaultScheduler();

//            Scheduler sched = sf.getScheduler();

            // 启动调度器
//            sched.start();

            //设置任务名称和组别，也可以进行参数传递
            JobDetail a = newJob(quartzJob.class).withIdentity(jobClassName, "group1").usingJobData("su",count).build();

            //设置触发器的触发任务情况和触发时间
            Trigger trigger2 = newTrigger()
                            .withIdentity(jobClassName, "group1")
                            .startAt(useDate)
                            .withSchedule(simpleSchedule()
                                        .withMisfireHandlingInstructionNowWithExistingCount()
                                        .withIntervalInSeconds(10)
                                        .withRepeatCount(5)).build();

            sched.scheduleJob(a, trigger2);
            System.out.println("触发器设置成功的时间："+ LocalTime.now());

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return "success";

    }


    @ResponseBody
    @RequestMapping(value="/deletejob", method = RequestMethod.GET)
    public void deletejob() throws Exception {
        String jobClassName= null;
        try {
            jobClassName = "haha";
            // 通过SchedulerFactory获取一个调度器实例
            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler sched = sf.getScheduler();

            //删除操作前应该暂停该任务的触发器，并且停止该任务的执行。
            sched.pauseTrigger(TriggerKey.triggerKey(jobClassName, "group1"));
            sched.unscheduleJob(TriggerKey.triggerKey(jobClassName, "group1"));
            sched.deleteJob(JobKey.jobKey(jobClassName, "group1"));
            System.out.println("删除任务："+jobClassName+"成功");
        } catch (SchedulerException e) {
            e.printStackTrace();
            System.out.println("删除任务："+jobClassName+"失败");
        }
    }


    /** spring 简单定时任务
     * @return
     */
    @ResponseBody
    @RequestMapping(value="lala", method = {RequestMethod.GET})
    public String lala(){
        String corn="0/5 * * * * ?";
        return scheduledTimeUtil.changeTask(corn);
    }

}
