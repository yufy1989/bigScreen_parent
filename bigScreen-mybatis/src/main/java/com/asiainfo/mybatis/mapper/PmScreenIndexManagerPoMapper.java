package com.asiainfo.mybatis.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.mybatis.po.PmScreenIndexManagerPo;

public interface PmScreenIndexManagerPoMapper {
    int deleteByPrimaryKey(BigDecimal indexCode);

    int insert(PmScreenIndexManagerPo record);

    int insertSelective(PmScreenIndexManagerPo record);

    PmScreenIndexManagerPo selectByPrimaryKey(BigDecimal indexCode);

    int updateByPrimaryKeySelective(PmScreenIndexManagerPo record);

    int updateByPrimaryKey(PmScreenIndexManagerPo record);
    /**
     * 功能描述：每30秒一次获取新单播报数据
     * @author yfy
     * @date 2017年5月16日 下午5:35:13
     * @param @return 
     * @return List<Map<String,Object>>
     */
	List<Map<String, Object>> selectNewsBroadcast(@Param("companyCode")String code);
}