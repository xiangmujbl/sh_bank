package com.mmec.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 基本类的使用
 * @author liuy
 * @version 2012-03-13
 * 
 */
public abstract class BasicBean {
	
	
	/**
	 * 将序列化的字符串，映射给对应的bean
	 * 例如：
	 * String s="a=1&b=2" 复制给：
	 * 
	 * public class c
	 * {
	 * 		private String a;
	 *      private String b;
	 *      setA()
	 *      ....
	 * }
	 * 
	 * @param bean  需要复制的bean
	 * @param properties  被复制的字符串
	 * @param fenge   字符串中的分割符号,默认为"&"
	 */
	public static Object getBean(Object bean, String properties)
	{
		return getBeanObj(bean,properties,null);	
	}
	
	/**
	 * 将序列化的字符串，映射给对应的bean
	 * 例如：
	 * String s="a=1&b=2" 复制给：
	 * 
	 * public class c
	 * {
	 * 		private String a;
	 *      private String b;
	 *      setA()
	 *      ....
	 * }
	 * 
	 * @param bean  需要复制的bean
	 * @param properties  被复制的字符串
	 * @param fenge   字符串中的分割符号,默认为"&"
	 */
	public static Object getBeanObj(Object bean, String properties,String fenge)
	{
		String p=StringUtil.replaceBlank(properties);
		String[] dnL=null;
		if(fenge==null)
		{
			dnL= p.split("&");
		}
		else
		{
			dnL= p.split(fenge);
		}
		HashMap<String, String> h = new HashMap<String, String>();
		//加载subject项
		for(int i=0;i<dnL.length;i++)
		{
			String[] dnlt=dnL[i].split("=");
			h.put(dnlt[0], dnlt[1]);
		}	
		setBean(bean,h);
		return bean;
		
	}
	
	/**
	 * map复制给bean
	 * 
	 */
	private static void setBean(Object bean, Map properties)
	{
		try {
			BeanUtils.populate(bean, properties);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 将对象序列成一个字符串
	 * 例如：
	 * @param bean  需要复制的bean
	 * @param properties  被复制的字符串
	 * @param fenge   字符串中的分割符号,默认为"&"
	 */
	public static String getBeanString(Object bean)
	{
		Map map=null;
		try {
			map = BeanUtils.describe(bean);
			Set keySet=map.keySet();
			String s1="";
			for (Iterator iter = keySet.iterator(); iter.hasNext();) 
			{
				Object element = (Object) iter.next();
				if(element!=null)
				{
					s1+=element+"="+(String)map.get(element)+"&";
				}
			}
			return s1;
		} catch (IllegalAccessException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return null;
	}
		
	
	/**
	 * 对比字符串不一样的地方
	 * 
	 */
	public static String[] getBYY(String yyxx,String gxxx)
	  {
			String[] sl1=yyxx.split("&");
			String[] sl2=gxxx.split("&");
			
			String sp1="",sp2="";
			for(int i=0;i<sl1.length;i++)
			{
				if(sl1[i].endsWith(sl2[i]))
				{
					
				}
				else
				{
					int m=sl1[i].indexOf(("servletWrapper"));
					if(m>=0)
					{
						
					}
					else
					{
						sp1+=sl1[i]+"&";
						sp2+=sl2[i]+"&";
					}
				}
			}
			String[] lis={sp1,sp2};
			return lis;
	  }
	
	/** 
     * 将一个 JavaBean 对象转化为一个  Map 
     * @param bean 要转化的JavaBean 对象 
     * @return 转化出来的  Map 对象 
     * @throws IntrospectionException 如果分析类属性失败 
     * @throws IllegalAccessException 如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败 
     */  
    @SuppressWarnings({ "rawtypes", "unchecked" })  
    public static Map convertBean(Object bean)  
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {  
        Class type = bean.getClass();  
        Map returnMap = new HashMap();  
        BeanInfo beanInfo = Introspector.getBeanInfo(type);  
  
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();  
        for (int i = 0; i< propertyDescriptors.length; i++) {  
            PropertyDescriptor descriptor = propertyDescriptors[i];  
            String propertyName = descriptor.getName();  
            if (!propertyName.equals("class")) {  
                Method readMethod = descriptor.getReadMethod();  
                Object result = readMethod.invoke(bean, new Object[0]);  
                if (result != null) {  
                    returnMap.put(propertyName, result);  
                } else {  
                    returnMap.put(propertyName, "");  
                }  
            }  
        }  
        return returnMap;  
    }  
    
    /** 
     * 将一个 Map 对象转化为一个 JavaBean 
     * @param type 要转化的类型 
     * @param map 包含属性值的 map 
     * @return 转化出来的 JavaBean 对象 
     * @throws IntrospectionException 如果分析类属性失败 
     * @throws IllegalAccessException 如果实例化 JavaBean 失败 
     * @throws InstantiationException 如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败 
     */  
    @SuppressWarnings("rawtypes")  
    public static Object convertMap(Class type, Map map)  
            throws IntrospectionException, IllegalAccessException,  
            InstantiationException, InvocationTargetException {  
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性  
        Object obj = type.newInstance(); // 创建 JavaBean 对象  
  
        // 给 JavaBean 对象的属性赋值  
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();  
        for (int i = 0; i< propertyDescriptors.length; i++) {  
            PropertyDescriptor descriptor = propertyDescriptors[i];  
            String propertyName = descriptor.getName();  
  
            if (map.containsKey(propertyName)) {  
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。  
                Object value = map.get(propertyName);  
  
                Object[] args = new Object[1];  
                args[0] = value;  
  
                descriptor.getWriteMethod().invoke(obj, args);  
            }  
        }  
        return obj;  
    }  
}
