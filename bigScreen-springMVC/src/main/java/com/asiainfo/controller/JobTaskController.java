package com.asiainfo.controller;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.mybatis.po.PmScreenIndexmgrJobPo;
import com.asiainfo.pmcs.service.JobTaskService;
import com.asiainfo.pmcs.util.RetObj;
import com.asiainfo.pmcs.util.SpringUtils;
/**
 * 功能描述：控制台接收
 * @author yfy
 * @date 2017年5月12日
 */
@Controller
@RequestMapping("/task")
public class JobTaskController {
	// 日志记录器
	private static Logger logger = LoggerFactory.getLogger(JobTaskController.class);
	@Autowired
	private JobTaskService taskService;
	/**
	 * 功能描述：跳转任务管理页面
	 * @author yfy
	 * @date 2017年5月9日 下午8:01:31
	 * @param @param request
	 * @param @return 
	 * @return String
	 */
	@RequestMapping("taskList")
	public String taskList(HttpServletRequest request) {
		List<PmScreenIndexmgrJobPo> taskList = taskService.getAllTask();
		request.setAttribute("taskList", taskList);
		return "base/task/taskList";
	}
	/**
	 * 功能描述：新增定时任务
	 * @author yfy
	 * @date 2017年5月13日 下午5:22:09
	 * @param @param request
	 * @param @param scheduleJob
	 * @param @return 
	 * @return RetObj
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("add")
	@ResponseBody
	public RetObj taskList(HttpServletRequest request, PmScreenIndexmgrJobPo scheduleJob) {
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		try {
			CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
		} catch (Exception e) {
			retObj.setMsg("cron表达式有误，不能被解析！");
			return retObj;
		}
		Object obj = null;
		try {
			if (StringUtils.isNotBlank(scheduleJob.getSpringId())) {
				obj = SpringUtils.getBean(scheduleJob.getSpringId());
			} else {
				Class clazz = Class.forName(scheduleJob.getBeanClass());
				obj = clazz.newInstance();
			}
		} catch (Exception e) {
			// do nothing.........
		}
		if (obj == null) {
			retObj.setMsg("未找到目标类！");
			return retObj;
		} else {
			Class clazz = obj.getClass();
			Method method = null;
			try {
				method = clazz.getMethod(scheduleJob.getMethodName(), null);
			} catch (Exception e) {
				// do nothing.....
			}
			if (method == null) {
				retObj.setMsg("未找到目标方法！");
				return retObj;
			}
		}
		try {
			taskService.addTask(scheduleJob);
		} catch (Exception e) {
			e.printStackTrace();
			retObj.setFlag(false);
			retObj.setMsg("保存失败，检查 name group 组合是否有重复！");
			return retObj;
		}

		retObj.setFlag(true);
		return retObj;
	}
	/**
	 * 功能描述：修改任务状态
	 * @author yfy
	 * @date 2017年5月13日 上午10:31:48
	 * @param @param request
	 * @param @param jobId
	 * @param @param cmd
	 * @param @return 
	 * @return RetObj
	 */
	@RequestMapping("changeJobStatus")
	@ResponseBody
	public RetObj changeJobStatus(HttpServletRequest request, Long jobId, String cmd) {
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		try {
			taskService.changeStatus(jobId, cmd);
			logger.info("任务:"+jobId+"状态改变成功！");
		} catch (SchedulerException e) {
			logger.info(e.getMessage(), e);
			retObj.setMsg("任务状态改变失败！");
			return retObj;
		}
		retObj.setFlag(true);
		return retObj;
	}
	/**
	 * 功能描述：修改时间表达式
	 * @author yfy
	 * @date 2017年5月16日 上午10:32:31
	 * @param @param request
	 * @param @param jobId
	 * @param @param cron
	 * @param @return 
	 * @return RetObj
	 */
	@RequestMapping("updateCron")
	@ResponseBody
	public RetObj updateCron(HttpServletRequest request, Long jobId, String cron) {
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		try {
			CronScheduleBuilder.cronSchedule(cron);
		} catch (Exception e) {
			retObj.setMsg("cron表达式有误，不能被解析！");
			return retObj;
		}
		try {
			taskService.updateCron(jobId, cron);
			logger.info("任务:"+jobId+"cron更新成功！");
		} catch (SchedulerException e) {
			retObj.setMsg("cron更新失败！");
			return retObj;
		}
		retObj.setFlag(true);
		return retObj;
	}
	
	
	@RequestMapping("startAll")
	@ResponseBody
	public RetObj startAll() {
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		try {
			taskService.startAll();
			logger.info("任务:状态改变成功！");
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			retObj.setMsg("任务状态改变失败！");
			return retObj;
		}
		retObj.setFlag(true);
		return retObj;
	}
	
	@RequestMapping("stopAll")
	@ResponseBody
	public RetObj stopAll() {
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		try {
			taskService.stopAll();
			logger.info("任务:状态改变成功！");
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			retObj.setMsg("任务状态改变失败！");
			return retObj;
		}
		retObj.setFlag(true);
		return retObj;
	}
}
