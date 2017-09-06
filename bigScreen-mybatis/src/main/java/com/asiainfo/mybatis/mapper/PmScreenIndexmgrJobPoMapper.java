package com.asiainfo.mybatis.mapper;

import java.util.List;

import com.asiainfo.mybatis.po.PmScreenIndexmgrJobPo;

public interface PmScreenIndexmgrJobPoMapper {
    int deleteByPrimaryKey(Long jobId);

    int insert(PmScreenIndexmgrJobPo record);

    int insertSelective(PmScreenIndexmgrJobPo record);

    PmScreenIndexmgrJobPo selectByPrimaryKey(Long jobId);

    int updateByPrimaryKeySelective(PmScreenIndexmgrJobPo record);

    int updateByPrimaryKey(PmScreenIndexmgrJobPo record);
    
	List<PmScreenIndexmgrJobPo> getAll();

	void startAll();

	void stopAll();
	
}