package com.asiainfo.pmcs.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
* 类说明：获取spring上下文
* @author yfy
* @date 2017年5月17日 上午10:37:53
*/
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
	
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;  
	}
	public static ApplicationContext getContext() {
        return context;
    }
}
