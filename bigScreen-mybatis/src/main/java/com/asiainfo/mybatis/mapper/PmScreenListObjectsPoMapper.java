package com.asiainfo.mybatis.mapper;

import com.asiainfo.mybatis.po.PmScreenListObjectsPo;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface PmScreenListObjectsPoMapper {
    int deleteByPrimaryKey(@Param("objectId") BigDecimal objectId, @Param("objectKey") String objectKey);

    int insert(PmScreenListObjectsPo record);

    int insertSelective(PmScreenListObjectsPo record);

    PmScreenListObjectsPo selectByPrimaryKey(@Param("objectId") BigDecimal objectId, @Param("objectKey") String objectKey);

    int updateByPrimaryKeySelective(PmScreenListObjectsPo record);

    int updateByPrimaryKey(PmScreenListObjectsPo record);
    /**
     * 功能描述：插入得到的数据
     * @author yfy
     * @date 2017年5月16日 下午8:26:30
     * @param @param objectId
     * @param @param data
     * @param @return 
     * @return int
     */
	int replaceCollData(BigDecimal objectId, List<Map<String, Object>> data);
	/**
	 * 功能描述：按object_id删除
	 * @author yfy
	 * @date 2017年5月18日 下午3:11:12
	 * @param @param list 
	 * @return void
	 */
	void deleteOldRecord(List<BigDecimal> list);
}