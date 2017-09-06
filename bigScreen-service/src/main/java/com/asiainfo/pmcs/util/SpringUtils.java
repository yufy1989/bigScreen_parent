package com.asiainfo.pmcs.util; 

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/**
 * 功能描述：实现BeanFactoryPostProcessor接口,在spring实例化bean之后,初始化bean之前可做的操作
 * @author yfy
 * @date 2017年5月13日
 */
public class SpringUtils implements BeanFactoryPostProcessor {

	private static ConfigurableListableBeanFactory beanFactory; // Spring应用上下文环境
	/**
	 * 功能描述：注入beanFactory
	 * @author yfy
	 * @date 2017年5月12日 下午5:53:02
	 * @param @param beanFactory
	 * @param @throws BeansException
	 */
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		SpringUtils.beanFactory = beanFactory;
	}

	/**
	 * 功能描述：获得一个以所给名字注册的bean的实例
	 * @author yfy
	 * @date 2017年5月12日 下午5:49:52
	 * @param @param name
	 * @param @return
	 * @param @throws BeansException 
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {
		return (T) beanFactory.getBean(name);
	}

	/**
	 * 功能描述：获取类型为requiredType的对象
	 * @author yfy
	 * @date 2017年5月12日 下午5:50:19
	 * @param @param clz
	 * @param @return
	 * @param @throws BeansException 
	 * @return T
	 */
	public static <T> T getBean(Class<T> clz) throws BeansException {
		T result = (T) beanFactory.getBean(clz);
		return result;
	}

	/**
	 * 功能描述：如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
	 * @author yfy
	 * @date 2017年5月15日 下午5:50:39
	 * @param @param name
	 * @param @return 
	 * @return boolean
	 */
	public static boolean containsBean(String name) {
		return beanFactory.containsBean(name);
	}

	/**
	 * 功能描述：判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	 * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 * @author yfy
	 * @date 2017年5月15日 下午5:50:51
	 * @param @param name
	 * @param @return
	 * @param @throws NoSuchBeanDefinitionException 
	 * @return boolean
	 */
	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return beanFactory.isSingleton(name);
	}

	/**
	 * 功能描述：Class 注册对象的类型
	 * @author yfy
	 * @date 2017年5月15日 下午5:51:05
	 * @param @param name
	 * @param @return
	 * @param @throws NoSuchBeanDefinitionException 
	 * @return Class<?>
	 */
	public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return beanFactory.getType(name);
	}

	/**
	 * 功能描述： 如果给定的bean名字在bean定义中有别名，则返回这些别名
	 * @author yfy
	 * @date 2017年5月15日 下午5:51:16
	 * @param @param name
	 * @param @return
	 * @param @throws NoSuchBeanDefinitionException 
	 * @return String[]
	 */
	public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
		return beanFactory.getAliases(name);
	}

}
