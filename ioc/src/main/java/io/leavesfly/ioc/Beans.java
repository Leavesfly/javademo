package io.leavesfly.ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来保存配置文件中的所有Bean对象
 *
 */
public class Beans {

	// 保存所有从Bean对象ID到Bean对象的映射
	private Map<String, Bean> beanMap;

	public Beans() {
		beanMap = new HashMap<String, Bean>();
	}

	public Map<String, Bean> getBeanMap() {
		return beanMap;
	}

	/**
	 * 加入一个Bean对象
	 *
	 * @param name
	 *            对象名
	 * @param obj
	 *            对象
	 * @return 加入状态
	 */
	public boolean putBean(String name, Bean bean) {
		beanMap.put(name, bean);
		return true;
	}

	/**
	 * 根据Bean对象名，取得该Bean对象
	 *
	 * @param name
	 *            对象名
	 * @return 对象
	 */
	public Bean getBean(String name) {
		return beanMap.get(name);
	}
}
