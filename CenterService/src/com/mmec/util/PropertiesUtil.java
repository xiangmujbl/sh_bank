package com.mmec.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mmec.css.conf.IConf;
/**
 * 读取Properties综合类,默认绑定到classpath下的config.properties文件。
 */
public class PropertiesUtil {
    //配置文件的路径
    private String configPath=null;
    private static Logger  log = Logger.getLogger(PropertiesUtil.class);
    /**
     * 配置文件对象
     */
    private Properties props=null;
    
//    private static PropertiesUtil properties;  
    private static PropertiesUtil properties = new PropertiesUtil();  
    
    /**
     * 默认构造函数，用于sh运行，自动找到classpath下的config.properties。
     */
	private PropertiesUtil() {
		InputStream in = PropertiesUtil.class.getClassLoader()
				.getResourceAsStream("mmec.properties");
		props = new Properties();
		try {
			props.load(in);
			// 关闭资源
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static  PropertiesUtil getProperties() {
		if (properties == null) {
			properties = new PropertiesUtil();
		}
		return properties;
	}  
    /**
     * 根据key值读取配置的值
     * @param key key值
     * @return key 键对应的值 
     * @throws IOException 
     */
    public String readValue(String key){
        return  props.getProperty(key);
    }
    
    /**
     * 读取properties的全部信息
     * @throws FileNotFoundException 配置文件没有找到
     * @throws IOException 关闭资源文件，或者加载配置文件错误
     * 
     */
    public Map<String,String> readAllProperties() throws FileNotFoundException,IOException  {
        //保存所有的键值
        Map<String,String> map=new HashMap<String,String>();
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String Property = props.getProperty(key);
            map.put(key, Property);
        }
        return map;
    }

    /**
     * 设置某个key的值,并保存至文件
     * @param key key值
     * @return key 键对应的值 
     * @throws IOException 
     */
    public void setValue(String key,String value) throws IOException {
        Properties prop = new Properties();
        InputStream fis = new FileInputStream(this.configPath);
        // 从输入流中读取属性列表（键和元素对）
        prop.load(fis);
        // 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
        // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream fos = new FileOutputStream(this.configPath);
        prop.setProperty(key, value);
        // 以适合使用 load 方法加载到 Properties 表中的格式，
        // 将此 Properties 表中的属性列表（键和元素对）写入输出流
        prop.store(fos,"last update");
        //关闭文件
        fis.close();
        fos.close();
    }
    
    public static void main(String[] args) {
        PropertiesUtil p;
//        PropertiesUtil p2;
   
            p = PropertiesUtil.getProperties();
//            p2= PropertiesUtil.getProperties();
//            log.info("****"+(p==p2));
//            log.info(p.readAllProperties());
            
            log.info(IConf.getValue("PCSIP"));

    }
}
