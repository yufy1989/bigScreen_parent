package com.asiainfo.pmcs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.asiainfo.mybatis.mapper.PmScreenIndexmgrJobPoMapper;
import com.asiainfo.mybatis.po.PmScreenIndexmgrJobPo;

@Service
public class JobTaskService {
    private static Logger logger = LoggerFactory.getLogger(JobTaskService.class);
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private PmScreenIndexmgrJobPoMapper scheduleJobMapper;

    /**
     * 从数据库中取 区别于getAllJob
     *
     * @return
     */
    public List<PmScreenIndexmgrJobPo> getAllTask() {

        return scheduleJobMapper.getAll();
    }

    /**
     * 添加到数据库中 区别于addJob
     */
    public void addTask(PmScreenIndexmgrJobPo job) {
        job.setCreateTime(new Date());
        scheduleJobMapper.insertSelective(job);
    }

    /**
     * 从数据库中查询job
     */
    public PmScreenIndexmgrJobPo getTaskById(Long jobId) {
        return scheduleJobMapper.selectByPrimaryKey(jobId);
    }

    /**
     * 更改任务状态
     *
     * @throws SchedulerException
     */
    public void changeStatus(Long jobId, String cmd) throws SchedulerException {
        PmScreenIndexmgrJobPo job = getTaskById(jobId);
        if (job == null) {
            return;
        }
        if ("stop".equals(cmd)) {
            deleteJob(job);
            job.setJobStatus(PmScreenIndexmgrJobPo.STATUS_NOT_RUNNING);
        } else if ("start".equals(cmd)) {
            job.setJobStatus(PmScreenIndexmgrJobPo.STATUS_RUNNING);
            addJob(job);
        }
        scheduleJobMapper.updateByPrimaryKeySelective(job);
    }

    /**
     * 功能描述：开启所有任务
     *
     * @param @throws Exception
     * @return void
     * @author Administrator
     * @date 2017年5月19日 下午6:21:07
     */
    public void startAll() throws Exception {
        scheduleJobMapper.startAll();
        init();
    }

    /**
     * 功能描述：关闭所有任务
     *
     * @param @throws Exception
     * @return void
     * @author yfy
     * @date 2017年5月19日 下午6:21:07
     */
    public void stopAll() throws Exception {
        scheduleJobMapper.stopAll();
        List<PmScreenIndexmgrJobPo> jobList = scheduleJobMapper.getAll();
        for (PmScreenIndexmgrJobPo job : jobList) {
            deleteJob(job);
        }
    }

    /**
     * 更改任务 cron表达式
     *
     * @throws SchedulerException
     */
    public void updateCron(Long jobId, String cron) throws SchedulerException {
        PmScreenIndexmgrJobPo job = getTaskById(jobId);
        if (job == null) {
            return;
        }
        job.setCronExpression(cron);
        if (PmScreenIndexmgrJobPo.STATUS_RUNNING.equals(job.getJobStatus())) {
            updateJobCron(job);
        }
        scheduleJobMapper.updateByPrimaryKeySelective(job);

    }

    /**
     * 添加任务
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addJob(PmScreenIndexmgrJobPo job) throws SchedulerException {
        if (job == null || !PmScreenIndexmgrJobPo.STATUS_RUNNING.equals(job.getJobStatus())) {
            return;
        }

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        logger.info("************************add" + job.getJobName());
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        // 不存在，创建一个
        if (null == trigger) {
            Class clazz = PmScreenIndexmgrJobPo.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;

            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();

            jobDetail.getJobDataMap().put("scheduleJob", job);

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
        logger.info("任务" + job.getJobName() + "添加完毕,进入队列!!!!!!!!!");
    }

    @PostConstruct
    public void init() throws Exception {
//		Scheduler scheduler = schedulerFactoryBean.getScheduler();
        // 这里获取任务信息数据
        List<PmScreenIndexmgrJobPo> jobList = scheduleJobMapper.getAll();

        for (PmScreenIndexmgrJobPo job : jobList) {
            addJob(job);
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<PmScreenIndexmgrJobPo> getAllJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<PmScreenIndexmgrJobPo> jobList = new ArrayList<PmScreenIndexmgrJobPo>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                PmScreenIndexmgrJobPo job = new PmScreenIndexmgrJobPo();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public List<PmScreenIndexmgrJobPo> getRunningJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<PmScreenIndexmgrJobPo> jobList = new ArrayList<PmScreenIndexmgrJobPo>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            PmScreenIndexmgrJobPo job = new PmScreenIndexmgrJobPo();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setJobName(jobKey.getName());
            job.setJobGroup(jobKey.getGroup());
            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * 暂停一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void pauseJob(PmScreenIndexmgrJobPo scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void resumeJob(PmScreenIndexmgrJobPo scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void deleteJob(PmScreenIndexmgrJobPo scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.deleteJob(jobKey);

    }

    /**
     * 立即执行job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void runAJobNow(PmScreenIndexmgrJobPo scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新job时间表达式
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void updateJobCron(PmScreenIndexmgrJobPo scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(triggerKey, trigger);
    }
}
