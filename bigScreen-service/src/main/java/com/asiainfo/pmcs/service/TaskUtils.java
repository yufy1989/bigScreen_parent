package com.asiainfo.pmcs.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.mybatis.mapper.ext.GetSourceDataPoMapper;
import com.asiainfo.mybatis.po.PmScreenIndexManagerPo;
import com.asiainfo.mybatis.po.PmScreenIndexmgrJobPo;
import com.asiainfo.pmcs.util.ApplicationContextHolder;
import com.asiainfo.pmcs.util.DataUtil;
import com.asiainfo.pmcs.util.MapperUtils;
import com.asiainfo.pmcs.util.SpringUtils;
/**
 * 功能描述：invoke
 * @author yfy
 * @date 2017年5月10日
 */
 @Service
public class TaskUtils {
	private static Logger logger = LoggerFactory.getLogger(TaskUtils.class);

	/**
	 * 功能描述：通过反射调用scheduleJob中定义的方法
	 * @author yfy
	 * @date 2017年5月15日 下午5:44:06
	 * @param @param scheduleJob 
	 * @return void
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void invokMethod(PmScreenIndexmgrJobPo scheduleJob) {
		Object object = null;
		Class clazz = null;
		try {
			object = SpringUtils.getBean(scheduleJob.getSpringId());
			clazz = object.getClass();
			long currentTimeMillis = System.currentTimeMillis();
			logger.info("任务开始执行---------"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			MapperUtils mapperUtils = ApplicationContextHolder.getContext().getBean(MapperUtils.class);
			PmScreenIndexManagerPo result = mapperUtils.getListCode(new BigDecimal(scheduleJob.getJobId()));
			
			clazz=GetSourceDataPoMapper.class;
			Method method1 = clazz.getMethod(scheduleJob.getMethodName(),String.class);
			List<Map<String,Object>> data =(List<Map<String, Object>>) method1.invoke(mapperUtils.getGetSourceDataPoMapper(),result.getCompanyCode());
			
			DataUtil dataUtil = ApplicationContextHolder.getContext().getBean(DataUtil.class);
			
			dataUtil.dealData(data, result);
			logger.info("任务 = [" + scheduleJob.getJobName() + "]----执行成功,耗时--------"+(System.currentTimeMillis()-currentTimeMillis)+"毫秒");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("任务 = [" + scheduleJob.getJobName() + "]----执行失败,错误===>"+e.getMessage());
		}
	}
}
