package com.asiainfo.pmcs.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.mybatis.mapper.PmScreenIndexListPoMapper;
import com.asiainfo.mybatis.mapper.PmScreenListObjectsPoMapper;
import com.asiainfo.mybatis.po.PmScreenIndexListPo;
import com.asiainfo.mybatis.po.PmScreenIndexManagerPo;
import com.asiainfo.mybatis.po.PmScreenListObjectsPo;

/**
* 类说明：
* @author yfy
* @date 2017年5月18日 下午2:42:13
*/
@Service
public class DataUtil {
	
	private static Logger logger = LoggerFactory.getLogger(DataUtil.class);
	
	@Autowired
	private PmScreenIndexListPoMapper listPoMapper;
	@Autowired
	private PmScreenListObjectsPoMapper objectsPoMapper;
	
	public void dealData(List<Map<String,Object>> data,PmScreenIndexManagerPo result) throws Exception{
		
		if (data==null||data.size()==0)
			throw  new Exception("需处理数据为空!!!!");
		
		for (Map<String, Object> map : data) {
			BigDecimal objectId = new BigDecimal(CommonUtil.getNextInt());
			//插入PM_SCREEN_INDEX_LIST表数据
			PmScreenIndexListPo pmScreenIndexListPo = new PmScreenIndexListPo();
			pmScreenIndexListPo.setListCode(result.getListCode());
			pmScreenIndexListPo.setObjectId(objectId);
			listPoMapper.insertSelective(pmScreenIndexListPo);
			
			for (String key : map.keySet()) {
				PmScreenListObjectsPo pmScreenListObjectsPo = new PmScreenListObjectsPo();
				pmScreenListObjectsPo.setObjectId(objectId);
				pmScreenListObjectsPo.setObjectKey(key);
				pmScreenListObjectsPo.setObjectValue(map.get(key)+"");
				objectsPoMapper.insert(pmScreenListObjectsPo);
			}
		}
		logger.info("数据处理成功,内容为:"+data.toString());
	}
}
