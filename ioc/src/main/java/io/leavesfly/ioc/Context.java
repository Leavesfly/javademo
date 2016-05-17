package io.leavesfly.ioc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Context {

	// 用来保存解码得到的从对象ID到这个对象对应的对象的映射
	private Map<String, Object> container = new HashMap<String, Object>();

	/**
	 * 每一个XML文档对应一个Context对象 ，此Context对象中保存XML文档中声明的所有对象
	 * 在实例化该类时传入XML文件地址，根据XML文件地址将所有对象都解析放入Context对象中的容器当中
	 *
	 * @param path
	 */
	public Context(String path) {
		// 解析path路径对应的XML文档，得到beans对象
		XMLParser parser = new XMLParser();
		Beans beans = parser.parser(path);
		// 遍历Beans取出每个Bean，解码Bean
		// Map<String, Bean> beanMap = beans.getBeanMap();
		// // 1.2.得到迭代器
		// Iterator<String> it = beanMap.keySet().iterator();
		//
		Map<String, Bean> beanMap = beans.getBeanMap();
		Iterator<String> it = beanMap.keySet().iterator();

		// 1.3.遍历迭代器,取出其中的key和value
		while (it.hasNext()) {
			String beanID = it.next();
			Bean bean = beanMap.get(beanID);
			// 解码bean对象，得到Object对象
			Object obj = encoder(bean);
			// 将beanID和Obj放入beans当中
			this.setBean(beanID, obj);
		}
	}

	/**
	 * 解析Bean对象，生成对应的Object对象
	 *
	 * @param bean
	 * @return
	 */
	private Object encoder(Bean bean) {
		String id = bean.getID();
		String type = bean.getType();
		try {
			Class c = Class.forName(type);
			Object obj = c.newInstance();
			// 一.取出bean当中设置的属性信息，设置到Obejct对象当中
			// 1.取出所有属性
			// 1.1.取得Bean中设置的所有属性
			Map<String, Object> properties = bean.getProperties();
			// 1.2.得到迭代器
			Iterator<String> it = properties.keySet().iterator();
			// 1.3.遍历迭代器,取出其中的key和value
			while (it.hasNext()) {
				String fieldName = it.next();
				Object value = properties.get(fieldName);
				// 如果属性是值传递类型，此时value的值，就是在XML中对其设定的值
				// 如果属性是引用类型，value此时还是解析XML时存入在properties当中的一个个Bean对象
				if (value instanceof Bean) {
					// 取得value对应的Object对象
					value = this.getBean(((Bean) value).getID());
				}

				// 2.将取得的所有属性，调用对象的setter()方法，设置到对象当中
				// 2.1.根据属性名，取得对象的setter()方法名
				String methodName = getMethodName4Field(fieldName);
				// 2.1通过属性名，找到对应的属性的类型（通过方法名找方法时，需要方法的参数）
				Field field = c.getDeclaredField(fieldName);
				Class fType = field.getType();
				// 2.2.根据方法名，取得对应方法
				Method method = c.getMethod(methodName, fType);
				// 2.3.调用setter方法
				method.invoke(obj, value);
			}
			return obj;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据属性名，拼凑得到setter()方法名
	 *
	 * @param name
	 * @return
	 */
	private String getMethodName4Field(String name) {
		String methodName = "set" + name.substring(0, 1).toUpperCase()
				+ name.substring(1, name.length());
		return methodName;
	}

	/**
	 * 设置对象的ID，和这个ID对应的对象的映射关系
	 *
	 * @param id
	 * @param bean
	 * @return
	 */
	public void setBean(String id, Object bean) {
		container.put(id, bean);
	}

	/**
	 * 通过对象的ID，取得这个对象
	 *
	 * @param id
	 * @return
	 */
	public Object getBean(String id) {
		return container.get(id);
	}

}
