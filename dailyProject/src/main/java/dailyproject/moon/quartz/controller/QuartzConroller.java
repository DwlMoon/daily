package dailyproject.moon.quartz.controller;

import dailyproject.moon.quartz.service.quartzJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-01-08 19:48
 **/

@Controller
@RequestMapping("quartz")
public class QuartzConroller {


    @ResponseBody
    @RequestMapping(value="addjob", method = {RequestMethod.GET})
    public String addjob(){

        try {
            String jobClassName="haha";

            String count="16";

            Calendar beforeTime = Calendar.getInstance();
            beforeTime.add(Calendar.MINUTE, 3);// 3分钟之后的时间
            Date useDate = beforeTime.getTime();


            SchedulerFactory sf = new StdSchedulerFactory();

            Scheduler sched = sf.getScheduler();

            // 启动调度器
            sched.start();

            //设置任务名称和组别，也可以进行参数传递
            JobDetail a = newJob(quartzJob.class).withIdentity(jobClassName, "group1").usingJobData("su",count).build();

            //设置触发器的触发任务情况和触发时间
            Trigger trigger2 = newTrigger().withIdentity(jobClassName, "group1").startAt(useDate).build();
            sched.scheduleJob(a, trigger2);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return "success";

    }

}
