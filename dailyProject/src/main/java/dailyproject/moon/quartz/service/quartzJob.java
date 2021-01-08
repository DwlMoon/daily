package dailyproject.moon.quartz.service;

import org.quartz.*;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-01-08 19:47
 **/

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class quartzJob implements Job {
    @Override
    public void execute (JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //获取触发器传递过来的参数
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Object su = (String)jobDataMap.get("su");
        System.out.println("quartz的执行时间是 : "+System.currentTimeMillis()+" __ "+su);
    }
}
