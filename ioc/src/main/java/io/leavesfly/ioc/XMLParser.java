package io.leavesfly.ioc;

import java.io.File;
import java.io.FileReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * 此类是个工具类 用来设置通过SAX解析配置的XML文件 得到其中的各个Bean的属性，并且保存到Beans当中去
 */
public class XMLParser {

    /**
     * 通过XML的文件路径，解析该XML文件，返回Beans对象
     *
     * @param path
     * @return
     */
    public static Beans parser(String path) {
        // 设置接收XML中返回的对象队列的队列
        // 因为一个XML文档对应一个Context对象，每个Context对象对应一个beans对象
        // 所以，在方法内部生成产生此beans
        Beans beans = new Beans();
        try {
            // 得到解析工厂对象
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // 得到一个解析对象
            SAXParser parser = factory.newSAXParser();
            // 得到解析器
            XMLReader reader = parser.getXMLReader();
            // 设置处理器
            // 此处是重点。利用值传递
            reader.setContentHandler(new SpringConfigContentHandler(beans));
            // 将文件路径封装为满足解析需要的InputSource类型
            InputSource source = new InputSource(new FileReader(new File(path)));
            // 设置需要解析的文档对象
            reader.parse(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

}
