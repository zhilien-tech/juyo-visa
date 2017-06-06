package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.bean.Schedule;
import io.znz.jsite.core.service.ScheduleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.quartz.CronExpression;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 定时任务 controller
 * @author Chaly
 */
//@Controller
//@RequestMapping("sys/schedule")
public class ScheduleController extends BaseController {

	//    @Autowired
	private ScheduleService scheduleService;

	/**
	 * 默认页面
	 */
	@RequiresRoles("admin")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "sys/scheduleList";
	}

	/**
	 * 获取定时任务 json
	 */
	@RequiresRoles("admin")
	@RequestMapping("json")
	@ResponseBody
	public Map<String, Object> getAllJobs(Model model) {
		List<Schedule> schedules = scheduleService.getAllScheduleJob();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", schedules);
		map.put("total", schedules.size());
		return map;
	}

	/**
	 * 获取正在运行的定时任务
	 */
	@RequiresRoles("admin")
	@RequestMapping("running/json")
	@ResponseBody
	public Map<String, Object> getAllJobsRun(Model model) {
		List<Schedule> schedules = scheduleService.getAllRuningScheduleJob();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", schedules);
		map.put("total", schedules.size());
		return map;
	}

	/**
	 * 添加跳转
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm() {
		return getTpl("sys/schedule/form");
	}

	/**
	 * 添加
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid Schedule schedule) {
		schedule.setStatus("1");
		scheduleService.add(schedule);
		return "success";
	}

	/**
	 * 暂停任务
	 */
	@RequiresRoles("admin")
	@RequestMapping("/{name}/{group}/stop")
	@ResponseBody
	public String stop(@PathVariable String name, @PathVariable String group) {
		scheduleService.stopJob(name, group);
		return "success";
	}

	/**
	 * 删除任务
	 */
	@RequiresRoles("admin")
	@RequestMapping("/{name}/{group}/delete")
	@ResponseBody
	public String delete(@PathVariable String name, @PathVariable String group) {
		scheduleService.delJob(name, group);
		return "success";
	}

	/**
	 * 修改表达式
	 */
	@RequiresRoles("admin")
	@RequestMapping("/{name}/{group}/update")
	@ResponseBody
	public String update(@PathVariable String name, @PathVariable String group, @RequestParam String cronExpression) {
		//验证cron表达式
		if (CronExpression.isValidExpression(cronExpression)) {
			scheduleService.modifyTrigger(name, group, cronExpression);
			return "success";
		} else {
			return "Cron表达式不正确";
		}
	}

	/**
	 * 立即运行一次
	 */
	@RequiresRoles("admin")
	@RequestMapping("/{name}/{group}/startNow")
	@ResponseBody
	public String stratNow(@PathVariable String name, @PathVariable String group) {
		scheduleService.startNowJob(name, group);
		return "success";
	}

	/**
	 * 恢复
	 */
	@RequiresRoles("admin")
	@RequestMapping("/{name}/{group}/resume")
	@ResponseBody
	public String resume(@PathVariable String name, @PathVariable String group) {
		scheduleService.restartJob(name, group);
		return "success";
	}

	/**
	 * 获取所有trigger
	 */
	public void getTriggers(HttpServletRequest request) {
		List<Schedule> schedules = scheduleService.getTriggersInfo();
		System.out.println(schedules.size());
		request.setAttribute("triggers", schedules);
	}

	/**
	 * cron表达式生成页
	 */
	@RequestMapping("quartzCron")
	public String quartzCronCreate() {
		return "sys/quartzCron";
	}
}
