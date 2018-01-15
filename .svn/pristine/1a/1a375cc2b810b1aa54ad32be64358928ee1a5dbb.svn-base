package com.mmec.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * 解析XML文件
 * 
 * @author [hl.Wei]
 * @date [2011-2-26 上午02:00:03]
 */
public class ContextXmlUtil {
	/**
	 * 属性列
	 */
	private Properties property = new Properties();

	private static ContextXmlUtil cxl = new ContextXmlUtil();

	public static ContextXmlUtil getInstance() {
		return cxl;
	}

	/**
	 * 
	 */
	private ContextXmlUtil() {
		org.jdom.input.SAXBuilder sb = new SAXBuilder();
		org.jdom.Document doc = null;
		org.jdom.Element root = null;

		String file = "config.xml";
		
		InputStream is = ContextXmlUtil.class.getClassLoader()
				.getResourceAsStream(file);

		try {
			doc = sb.build(is);

			is.close();

		} catch (JDOMException e1){
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		root = doc.getRootElement();

		for (Iterator iter = root.getChildren().iterator(); iter.hasNext();) {
			Element e = (Element) iter.next();

			if (0 == e.getChildren().size()) {
				property.put(e.getName().trim(), e.getText().trim());
			}
		}
	}

	/**
	 * 查询属性值
	 * 
	 * @key 属性名称
	 */
	public String getValue(String key) {
		Iterator<?> iterator = property.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator
					.next();

			if (key.equals(entry.getKey())) {

				// 返回查询属性值
				return entry.getValue();
			}
		}

		return null;
	}

	public static void main(String[] args) {
		try {
			System.out.println();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
