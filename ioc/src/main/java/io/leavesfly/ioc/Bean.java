package io.leavesfly.ioc;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

/**
 * XML中，每一个bean对象的结构
 */
public class Bean {

	private String ID;// 对象名
	private String type;// 类型
	private Map<String, Object> properties = new HashMap<String, Object>();// 对象的各个属性列表

	/**
	 * 设置这个Bean的key属性，值为value
	 *
	 * @param key
	 * @param value
	 */
	public void putProperty(String key, Object value) {
		properties.put(key, value);
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	// ID、type对应的setter()、getter()方法
}
