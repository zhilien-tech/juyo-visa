package io.znz.jsite.core.service;

import io.znz.jsite.core.bean.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务 service
 * @author Chaly
 */
//@Service
public class ScheduleService {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	//    @Autowired
	private Scheduler scheduler;

	/**
	 * 添加定时任务
	 */
	public void add(Schedule schedule) {
		Class job = null;
		try {
			job = Class.forName(schedule.getClassName());
		} catch (ClassNotFoundException e1) {
			logger.error("任务类没找到");
			e1.printStackTrace();
		}
		JobDetail jobDetail = JobBuilder.newJob(job).withIdentity(schedule.getName(), schedule.getGroup()).build();
		jobDetail.getJobDataMap().put("scheduleJob", schedule);

		//表达式调度构建器（可判断创建SimpleScheduleBuilder）
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedule.getCronExpression());

		//按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(schedule.getName(), schedule.getGroup())
				.withSchedule(scheduleBuilder).build();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			logger.info("定时任务添加成功");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有JobDetail
	 * @return 结果集合
	 */
	public List<JobDetail> getJobs() {
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			List<JobDetail> jobDetails = new ArrayList<JobDetail>();
			for (JobKey key : jobKeys) {
				jobDetails.add(scheduler.getJobDetail(key));
			}
			return jobDetails;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取所有计划中的任务
	 * @return 结果集合
	 */
	public List<Schedule> getAllScheduleJob() {
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		try {
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			for (JobKey jobKey : jobKeys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					Schedule schedule = new Schedule();
					schedule.setName(jobKey.getName());
					schedule.setGroup(jobKey.getGroup());
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					schedule.setStatus(triggerState.name());
					//获取要执行的定时任务类名
					JobDetail jobDetail = scheduler.getJobDetail(jobKey);
					schedule.setClassName(jobDetail.getJobClass().getName());
					//判断trigger
					if (trigger instanceof SimpleTrigger) {
						SimpleTrigger simple = (SimpleTrigger) trigger;
						schedule.setCronExpression("重复次数:"
								+ (simple.getRepeatCount() == -1 ? "无限" : simple.getRepeatCount()) + ",重复间隔:"
								+ (simple.getRepeatInterval() / 1000L));
						schedule.setDescription(simple.getDescription());
					}
					if (trigger instanceof CronTrigger) {
						CronTrigger cron = (CronTrigger) trigger;
						schedule.setCronExpression(cron.getCronExpression());
						schedule.setDescription(cron.getDescription() == null ? ("触发器:" + trigger.getKey()) : cron
								.getDescription());
					}
					scheduleList.add(schedule);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scheduleList;
	}

	/**
	 * 获取所有运行中的任务
	 * @return 结果集合
	 */
	public List<Schedule> getAllRuningScheduleJob() {
		List<Schedule> scheduleList = null;
		try {
			List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
			scheduleList = new ArrayList<Schedule>(executingJobs.size());
			for (JobExecutionContext executingJob : executingJobs) {
				Schedule schedule = new Schedule();
				JobDetail jobDetail = executingJob.getJobDetail();
				JobKey jobKey = jobDetail.getKey();
				Trigger trigger = executingJob.getTrigger();
				schedule.setName(jobKey.getName());
				schedule.setGroup(jobKey.getGroup());
				//scheduleJob.setDescription("触发器:" + trigger.getKey());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				schedule.setStatus(triggerState.name());
				//获取要执行的定时任务类名
				schedule.setClassName(jobDetail.getJobClass().getName());
				//判断trigger
				if (trigger instanceof SimpleTrigger) {
					SimpleTrigger simple = (SimpleTrigger) trigger;
					schedule.setCronExpression("重复次数:"
							+ (simple.getRepeatCount() == -1 ? "无限" : simple.getRepeatCount()) + ",重复间隔:"
							+ (simple.getRepeatInterval() / 1000L));
					schedule.setDescription(simple.getDescription());
				}
				if (trigger instanceof CronTrigger) {
					CronTrigger cron = (CronTrigger) trigger;
					schedule.setCronExpression(cron.getCronExpression());
					schedule.setDescription(cron.getDescription());
				}
				scheduleList.add(schedule);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return scheduleList;
	}

	/**
	 * 获取所有的触发器
	 * @return 结果集合
	 */
	public List<Schedule> getTriggersInfo() {
		try {
			GroupMatcher<TriggerKey> matcher = GroupMatcher.anyTriggerGroup();
			Set<TriggerKey> Keys = scheduler.getTriggerKeys(matcher);
			List<Schedule> triggers = new ArrayList<Schedule>();

			for (TriggerKey key : Keys) {
				Trigger trigger = scheduler.getTrigger(key);
				Schedule schedule = new Schedule();
				schedule.setName(trigger.getJobKey().getName());
				schedule.setGroup(trigger.getJobKey().getGroup());
				schedule.setStatus(scheduler.getTriggerState(key) + "");
				if (trigger instanceof SimpleTrigger) {
					SimpleTrigger simple = (SimpleTrigger) trigger;
					schedule.setCronExpression("重复次数:"
							+ (simple.getRepeatCount() == -1 ? "无限" : simple.getRepeatCount()) + ",重复间隔:"
							+ (simple.getRepeatInterval() / 1000L));
					schedule.setDescription(simple.getDescription());
				}
				if (trigger instanceof CronTrigger) {
					CronTrigger cron = (CronTrigger) trigger;
					schedule.setCronExpression(cron.getCronExpression());
					schedule.setDescription(cron.getDescription());
				}
				triggers.add(schedule);
			}
			return triggers;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 暂停任务
	 * @param name 任务名
	 * @param group 任务组
	 */
	public void stopJob(String name, String group) {
		JobKey key = new JobKey(name, group);
		try {
			scheduler.pauseJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 恢复任务
	 * @param name 任务名
	 * @param group 任务组
	 */
	public void restartJob(String name, String group) {
		JobKey key = new JobKey(name, group);
		try {
			scheduler.resumeJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 立马执行一次任务
	 * @param name 任务名
	 * @param group 任务组
	 */
	public void startNowJob(String name, String group) {
		JobKey jobKey = JobKey.jobKey(name, group);
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除任务
	 * @param name 任务名
	 * @param group 任务组
	 */
	public void delJob(String name, String group) {
		JobKey key = new JobKey(name, group);
		try {
			scheduler.deleteJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改触发器时间
	 * @param name 任务名
	 * @param group 任务组
	 * @param cron cron表达式
	 */
	public void modifyTrigger(String name, String group, String cron) {
		try {
			TriggerKey key = TriggerKey.triggerKey(name, group);
			//Trigger trigger = scheduler.getTrigger(key);

			CronTrigger newTrigger = (CronTrigger) TriggerBuilder.newTrigger().withIdentity(key)
					.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
			scheduler.rescheduleJob(key, newTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 暂停调度器
	 */
	public void stopScheduler() {
		try {
			if (!scheduler.isInStandbyMode()) {
				scheduler.standby();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
