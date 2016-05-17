package io.leavesfly.ioc;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 用户解析SpringConfig类型的XML文件的ContentHandler类
 */
public class SpringConfigContentHandler extends DefaultHandler {
	private Beans beans;// 用来保存这个XML文件对应的Beans的Beans对应，通过构造方法传入

	public SpringConfigContentHandler(Beans beans) {
		this.beans = beans;
	}

	private Bean temp;// 每一个解析得到的Bean对象，先用temp存放
	private String tempID;// 这个temp的ID

	private String pName;// 用来暂时存放属性名的字符串
	private String pValue;// 用来暂时存放属性的字符串

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		pValue = new String(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		// 如果结束的是bean元素，则将此bean放入Beans当中
		if ("bean".equals(qName)) {
			beans.putBean(tempID, temp);
		}
		// 如果结束的新元素是value元素,则将该属性放入bean当中
		if ("value".equals(qName)) {
			temp.putProperty(pName, pValue);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	/**
	 * 回调方法，每当 开始解读一个元素时，调用此方法 uri是此元素的命名空间uri，如果没有声明命名空间，则为空
	 * localName是元素名（不带前缀），如果没有声明命名空间，则为空 qName是元素全名（带前缀）
	 * attributes是这个元素的属性构成的属性数组
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
							 Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		// 如果开始的新元素是bean元素，则意味着声明了一个新的对象，只是把temp指向一个新的引用
		if ("bean".equals(qName)) {
			temp = new Bean();
			// 取出bean元素中的所有属性，并设置到Bean对象当中
			for (int i = 0; i < attributes.getLength(); i++) {
				// 取出属性数组第i个位置的属性名与属性值
				String name = attributes.getQName(i);
				String value = attributes.getValue(i);
				// 判断属性名，设置属性值
				if ("id".equals(name)) {
					temp.setID(value);
					// 将这个的类的类名赋给tempID，在读取完一个bean将bean放入beans当中时需要用到
					tempID = value;
				}
				if ("type".equals(name)) {
					temp.setType(value);
				}
			}
		}
		// 如果开始的新元素是property元素，则解析该property，取出该属性，放入pName中
		if ("property".equals(qName)) {
			// 取出property元素name属性的值
			pName = attributes.getValue("name");
		}
		// 如果开始的新元素是value元素，则解析该value
		if ("value".equals(qName)) {

		}
		// 如果开始的新元素是ref元素，则取出ref中的属性ID，并在bean中设置property
		if ("ref".equals(qName)) {
			String refID = attributes.getValue("id");
			// 通过ref的id，取出改id对应的bean
			Bean bean = beans.getBean(refID);
			// 设置property
			// ！！！！此处需要注意
			// 因为使用SAX是一行一行的解析，所以被引用的对象要放在引用的对象后面，否则取出来的时候，会是空的
			// 如果用DOM解析，则不存在此问题
			temp.putProperty(pName, bean);
		}
	}

}
