package com.asiainfo.pmcs.util;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.mybatis.mapper.PmScreenIndexListPoMapper;
import com.asiainfo.mybatis.mapper.PmScreenIndexManagerPoMapper;
import com.asiainfo.mybatis.mapper.PmScreenListObjectsPoMapper;
import com.asiainfo.mybatis.mapper.ext.GetSourceDataPoMapper;
import com.asiainfo.mybatis.po.PmScreenIndexManagerPo;

/**
* 类说明：源数据查询前业务处理及mapper对象获取
* @author yfy
* @date 2017年5月17日 下午8:33:24
*/
@Service
public class MapperUtils {
	@Autowired
	private PmScreenIndexManagerPoMapper managerPoMapper;
	@Autowired
	private PmScreenIndexListPoMapper listPoMapper;
	@Autowired
	private PmScreenListObjectsPoMapper objectsPoMapper;
	@Autowired
	private GetSourceDataPoMapper getSourceDataPoMapper;
	@Autowired
	private DataUtil dataUtil;
	
	public PmScreenIndexManagerPo getListCode(BigDecimal INDEX_CODE) throws Exception{
		
		//根据INDEX_CODE查询manager表信息
		PmScreenIndexManagerPo result = managerPoMapper.selectByPrimaryKey(INDEX_CODE);
		
		if (result==null||result.getListCode()==null) 
			throw new Exception("任务["+INDEX_CODE+"]配置为空!");
		
		BigDecimal listCode=result.getListCode();
		
		//删除旧数据
		List<BigDecimal> list=listPoMapper.getAllObjectIdByListCode(listCode);
		listPoMapper.deleteOldRecordByPrimaryKey(listCode);
		if (list!=null&&list.size()!=0) {
			objectsPoMapper.deleteOldRecord(list);
		}
		
		//更新PM_SCREEN_INDEX_MANAGER表List_code字段
		result.setListCode(new BigDecimal(CommonUtil.getNextInt()));
		managerPoMapper.insert(result);
		return result;
	}

	public PmScreenIndexManagerPoMapper getManagerPoMapper() {
		return managerPoMapper;
	}

	public PmScreenIndexListPoMapper getListPoMapper() {
		return listPoMapper;
	}

	public PmScreenListObjectsPoMapper getObjectsPoMapper() {
		return objectsPoMapper;
	}

	public GetSourceDataPoMapper getGetSourceDataPoMapper() {
		return getSourceDataPoMapper;
	}

	public DataUtil getDataUtil() {
		return dataUtil;
	}

	public void setDataUtil(DataUtil dataUtil) {
		this.dataUtil = dataUtil;
	}

}
