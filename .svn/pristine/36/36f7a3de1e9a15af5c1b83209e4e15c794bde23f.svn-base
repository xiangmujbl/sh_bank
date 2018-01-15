package com.mmec.util.ra;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.PdfReader;
import com.mmec.exception.ServiceException;

import org.apache.log4j.Logger;

public class SignOnPdfUtil{

	private static Logger log = Logger.getLogger(SignOnPdfUtil.class);

	
	/**
	 * 判断签名域是否已存在
	 * @param fieldName
	 * @param src
	 * @return
	 * @throws ServiceException
	 */
	public static boolean  has_signfield(String fieldName,String src) throws ServiceException{
		try {
			PdfReader 	p = new PdfReader(src);
			AcroFields fields=p.getAcroFields();
			Map<String,Item> map=fields.getFields();
//			System.out.println(map.keySet());
			p.close();
			if(null==map.keySet()||0==map.keySet().size())
//			if(null != map)
			{
				Iterator it =  map.keySet().iterator();
				while(it.hasNext())
				{
					if(fieldName.equals(it.next()))
					{
						return true;
					}
				}
			}
			
//			if(null != map && fieldName.equals(map.keySet()))
//				return true;
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new ServiceException("exception","has_signfield方法IO异常","");
		}
	}
	
	public String print_str(Map map){
		return "获得"+map.keySet().toString();
	}
	
	/**
	 * 证书签署+水印签署集成
	 * @param srcfilename 源文件路径
	 * @param signtype   标识 证书签署还是水印处理
	 * @param pdf_sign_type 证书签署方法--反射调用
	 * @param appreance_map  签署时的UI参数处理
	 * @throws ServiceException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void sign(String srcfilename,String signtype,String pdf_sign_type,Map<String,Object> appreance_map) throws ServiceException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		String dest="C:\\Users\\Administrator\\Desktop";
		File sign_src_file=new File(dest+File.separator+srcfilename);
		SignField s1=new SignField(1,100f,100f,100,100,"1");
		SignField s2=new SignField(2,200f,200f,100,100,"2");
		List<SignField> list =new ArrayList<SignField>();
		list.add(s1);
		list.add(s2);
					//pdf证书签署
		Class<?> clazz = Class.forName("com.mmec.util.ra.SignField");
		Object obj = clazz.newInstance();
		if(signtype.toLowerCase().equals("certinpdf")){
					//如果文件没有签名域
			log.info("证书签署");
			if(!has_signfield("",dest+File.separator+srcfilename)){
				SignField.addSignFieldFree(dest+File.separator+srcfilename, dest+File.separator+"signfield.pdf", list);
				sign_src_file=new File(dest+File.separator+"signfield.pdf");
			}
					//反射处理pdf签名
			Map<String,Object> map=new HashMap<String,Object>();
					//源文件地址  服务端定义
			String src_file="C:\\Users\\Administrator\\Desktop\\signfield.pdf",
					//目标文件地址.服务端自定义
			dest_file="C:\\Users\\Administrator\\Desktop\\newBob.pdf",
					//签名域的名称 客户端入参
			field_name="1";
			//appreance显示参数
			Map<String,Object> param_map=new HashMap<String,Object>();
					//type 可以为文字或者图片 客户端入参
			param_map.put("type", "img");
					//width 图片的宽 客户端入参
			param_map.put("width", "100");
					//height 图片的高 客户端入参
			param_map.put("height", "100");
					// 图片的路径  你以后可能要根据图章ID来查询
			param_map.put("imgpath","C:\\Users\\Administrator\\Desktop\\test.png");
					//源文件路径
			map.put("src",src_file);
					//目标文件路径
			map.put("dest",dest_file);
					//签名域名称 客户端入参
			map.put("fieldname",field_name);
					//UI传的map参数  客户端入参
			map.put("param_map", param_map);
			Method method = clazz.getMethod(pdf_sign_type, new Class<?>[]{Map.class});
			method.invoke(obj, new Object[]{map});
		}else{
					//水印处理
			Map<String,Object> map=new HashMap<String,Object>();
					//源文件地址 服务端定义
			String src_file="C:\\Users\\Administrator\\Desktop\\bob.pdf",
					//目标文件地址  服务端定义
			dest_file="C:\\Users\\Administrator\\Desktop\\newBob.pdf";
					//
			map.put("srcpath",src_file);
			map.put("destpath",dest_file);
			        //UI的list
			List<Map<String,String>> list_map=new ArrayList<Map<String,String>>();
			Map<String,String> map_1=new HashMap<String,String>();
					//图片是文字  客户端入参
			map_1.put("type","img");
					//图片的路径 客户端入参
			map_1.put("path","C:\\Users\\Administrator\\Desktop\\test.png");
					//图片的x坐标 客户端入参
			map_1.put("x","100");
					//图片的y坐标 客户端入参
			map_1.put("y","100");
					//图片宽 客户端入参
			map_1.put("width","100");
					//图片高 客户端入参
			map_1.put("height","100");
			map_1.put("page", "1");
			list_map.add(map_1);
			map.put("list", list_map);
			Method method = clazz.getMethod(pdf_sign_type, new Class<?>[]{Map.class});
			method.invoke(obj, new Object[]{map});
		}
		log.info("pdf处理完毕");
	}
	
	
	
	
	/**
	 * 综合签署
	 * 仅仅为示例,证书签署方式还是水印方式
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws ServiceException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) {

//		try{
//		Map<String,Object> appreance_map =new HashMap<String,Object>();
//		//pdf服务器证书签署
////		sign("bob.pdf","certinpdf","server_certinpdf",appreance_map);
//		//pdf事件证书签署
//		sign("bob.pdf","certinpdf","event_certinpdf",appreance_map);
//		//加水印处理
////		sign("bob.pdf","watermarkinpdf","addMark",appreance_map);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		//客户端调用
		
		try{
			
			Map<String,Object> map=new HashMap<String,Object>();
			//源文件地址  服务端定义
			String src_file="C:\\Users\\Administrator\\Desktop\\bob.pdf",
			//目标文件地址.服务端自定义
			dest_file="C:\\Users\\Administrator\\Desktop\\bob.pdf",
			//生成中间文件地址
			mid_file="C:\\Users\\Administrator\\Desktop\\midfile.pdf",
			//签名域的名称 客户端入参
			field_name="1";
			//appreance显示参数
			Map<String,Object> param_map=new HashMap<String,Object>();
					//type 可以为文字或者图片 客户端入参
			param_map.put("type", "img");
					//width 图片的宽 客户端入参
			param_map.put("width", 100);
					//height 图片的高 客户端入参
			param_map.put("height", 100);
					// 图片的路径  你以后可能要根据图章ID来查询
			param_map.put("imgpath","C:\\Users\\Administrator\\Desktop\\test.png");
					//源文件路径
			map.put("src",src_file);
					//目标文件路径
			map.put("dest",dest_file);
			map.put("mid_file_path", mid_file);
					//签名域名称 客户端入参
			map.put("fieldname",field_name);
					//UI传的map参数  客户端入参
			map.put("param_map", param_map);
			
			map.put("certpath", "C:\\Users\\Administrator\\Desktop\\");
			
			//签名域的方位列表,你应该查出来的
			SignField s1=new SignField(1,100f,100f,100,100,"0");
			SignField s2=new SignField(2,200f,200f,100,100,"1");
			List<SignField> list =new ArrayList<SignField>();
//			list.add(s1);
			list.add(s2);
			map.put("listSignField", list);
			map.put("signtype","certinpdf");
			
/****************pdf服务器证书签署***************************	
 ********************************************************/
//			map.put("sign_method_name","server_certinpdf");
		
			
/****************pdf事件证书签署 ***************************
 * ***************************************************/
			String customerType="1";
			String name="孙策";
			String idcard="262626019107283568";
			String companyname="买卖网";
			String mydata="QIANMINGYUANWEN";
			
			map.put("customerType",customerType);
			map.put("name",name);
			map.put("idcard",idcard);
			map.put("companyname",companyname);
			map.put("mydata",mydata);
			map.put("sign_method_name","event_certinpdf");

			////////////////////////////////////
			Map m = sign(map);
			System.out.println("certStr==="+m.get("certStr"));
			
			//水印处理
			Map<String,Object> map_2=new HashMap<String,Object>();
			//源文件地址 服务端定义
			String src_file_1="C:\\Users\\Administrator\\Desktop\\bob.pdf",
					//目标文件地址  服务端定义
			dest_file_1="C:\\Users\\Administrator\\Desktop\\newBob.pdf";
					//
			map_2.put("srcpath",src_file_1);
			map_2.put("destpath",dest_file_1);
			        //UI的list
			List<Map<String,String>> list_map=new ArrayList<Map<String,String>>();
			Map<String,String> map_1=new HashMap<String,String>();
					//图片是文字  客户端入参
			map_1.put("type","img");
					//图片的路径 客户端入参
			map_1.put("path","C:\\Users\\Administrator\\Desktop\\test.png");
					//图片的x坐标 客户端入参
			map_1.put("x","100");
					//图片的y坐标 客户端入参
			map_1.put("y","100");
					//图片宽 客户端入参
			map_1.put("width","100");
					//图片高 客户端入参
			map_1.put("height","100");
			map_1.put("page", "1");
			list_map.add(map_1);
			map_2.put("list", list_map);
			
			
		}catch(Exception e){
			
		}

	}
	public static Map signNew(Map<String,String> map) throws ServiceException{		
		log.info("signNew==="+map.toString());
		Map retMap = null;
		log.info("enter signNew method");
		log.info("参数为:"+map.toString());
		try{
		
		//调用函数方法名称
		String sign_method_name=(String)map.get("sign_method_name");
		
		
		Class<?> clazz = Class.forName("com.mmec.util.ra.SignField");
		Object obj = clazz.newInstance();
		
		Method method = clazz.getMethod(sign_method_name, new Class<?>[]{Map.class});
		retMap = (Map) method.invoke(obj, new Object[]{map});	
		
		}catch (Exception e){
			e.printStackTrace();
			throw new ServiceException("", "");
		}
		return retMap;
	}
	public static Map sign(Map<String,Object> map) throws ServiceException{
		Map retMap = null;
		log.info("enter sign method");
		log.info("参数为:"+map.toString());
		try{
		
		//调用函数方法名称
		String sign_method_name=(String)map.get("sign_method_name");
		
		
		Class<?> clazz = Class.forName("com.mmec.util.ra.SignField");
		Object obj = clazz.newInstance();
		
		Method method = clazz.getMethod(sign_method_name, new Class<?>[]{Map.class});
		retMap = (Map) method.invoke(obj, new Object[]{map});	
		
		}catch (Exception e){
			e.printStackTrace();
			throw new ServiceException("", "");
		}
		return retMap;
	}
	
	public static Map<String,String> event_cert(Map<String,Object> map) throws ServiceException
	{
		Map<String,String> retMap = new HashMap<String,String>();
		String customerType=(String)map.get("customerType");
		String name=(String)map.get("name");
		String idcard=(String)map.get("idcard");
		String companyname=(String)map.get("companyname");
		String mydata=(String)map.get("mydata");
		RequestRaCert racert=RaCert.eventSign(customerType, name,idcard,companyname,mydata);
		String signdata=racert.getSigndata();
		String certFingerprintStr1=racert.getCertFingerprint();
		String certStr=racert.getCertInfo();
		retMap.put("signdata", signdata);
		retMap.put("certFingerprintStr1",certFingerprintStr1);
		retMap.put("certStr",certStr);
		return retMap;
	}
}