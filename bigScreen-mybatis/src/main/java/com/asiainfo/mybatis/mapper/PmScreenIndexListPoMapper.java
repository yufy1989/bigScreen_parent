package com.asiainfo.mybatis.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.mybatis.po.PmScreenIndexListPo;

public interface PmScreenIndexListPoMapper {
    int deleteByPrimaryKey(@Param("listCode") BigDecimal listCode);

    int insert(PmScreenIndexListPo record);

    int insertSelective(PmScreenIndexListPo record);

	List<PmScreenIndexListPo> selectById(BigDecimal listCode);

	void deleteOldRecordByPrimaryKey(@Param("list_code")BigDecimal listCode);

	List<BigDecimal> getAllObjectIdByListCode(@Param("list_code") BigDecimal listCode);
}