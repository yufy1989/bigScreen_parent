package com.asiainfo.mybatis.mapper.ext;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GetSourceDataPoMapper {
    /**
     * 功能描述：2003每30秒一次获取新单播报数据
     * @author yfy
     * @date 2017年5月16日 下午5:35:13
     * @param @return 
     * @return List<Map<String,Object>>
     */
	List<Map<String, Object>> selectNewsBroadcast(@Param("companyCode")String code);
	/**
	 * 功能描述：2010获取工单热点图
	 * @author yfy
	 * @date 2017年5月18日 下午3:47:50
	 * @param @param companyCode
	 * @param @return 
	 * @return List<Map<String,Object>>
	 */
	List<Map<String, Object>> selectWorkbookHotChart(String companyCode);
	/**
	 * 功能描述：2011小区分布图
	 * @author yfy
	 * @date 2017年5月18日 下午4:00:09
	 * @param @param companyCode
	 * @param @return 
	 * @return List<Map<String,Object>>
	 */
	List<Map<String, Object>> selectCellDistributionMap(String companyCode);
}