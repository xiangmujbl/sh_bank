package com.mmec.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

//import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.ImageData;
import com.aspose.words.License;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.SaveFormat;
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;

import net.sf.json.JSONArray;


public class FileUtil {
	private static Logger log = Logger.getLogger(FileUtil.class);
	
	//java 合并两个byte数组  
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
        return byte_3;  
    }  
	
	/**
     * 完整的堆栈信息
     *
     * @param e Exception
     * @return Full StackTrace
     */
    public static String getStackTrace(Exception e) throws ServiceException{
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    throw new ServiceException("3333","打印完整日志错误","getStackTrace");
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }
	
    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0     
        // 4 代表长度为4     
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }
	public static String getYearMonth(Date createTime)
	{		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String  str = format.format(createTime);  
		String [] s = str.split("-");
		return s[0]+s[1];
	}
	
	/*
	 *合同文件夹按年月分类
	 */
	public static String createContractFolder(String serialNum)
	{
		Calendar calendar = Calendar.getInstance();
		/**
         * 获取年份
         */
        int year = calendar.get(Calendar.YEAR);
        /**
         * 获取月份
         */
        int month = calendar.get(Calendar.MONTH) + 1;
//        month = (Integer) (month< 10 ? "0" + month : month);
		String monthStr = "";
		if(month <10)
		{
			monthStr = "0" +month;
		}
		else
		{
			monthStr = String.valueOf(month);
		}
		String filePath = IConf.getValue("CONTRACTPATH")+year+""+monthStr +"/"+serialNum+"/";
		return filePath;
	}
	/*
	 *存证文件夹按年月日分类
	 */
	public static String createEvidenceFolder(String serialNum)
	{
		Calendar calendar = Calendar.getInstance();
		/**
         * 获取年份
         */
        int year = calendar.get(Calendar.YEAR);
        /**
         * 获取月份
         */
        int month = calendar.get(Calendar.MONTH) + 1;
        
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
//        month = (Integer) (month< 10 ? "0" + month : month);
		String monthStr = "";
		String dayStr = "";
		if(month <10)
		{
			monthStr = "0" +month;
		}
		else
		{
			monthStr = String.valueOf(month);
		}
		if(day <10)
		{
			dayStr = "0" + day;
		}
		else
		{
			dayStr = String.valueOf(day);
		}
		String filePath = IConf.getValue("CZ_PATH")+year+"/"+monthStr +"/"+dayStr+"/"+serialNum+"/";
		return filePath;
	}
	/**
	 * Description: 向FTP服务器上传文件
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param path FTP服务器保存目录
	 * @param filename 上传到FTP服务器上的文件名
	 * @param input 输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String url,int port,String username, String password, String path, String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
//		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		try {
			int reply;
			ftp.connect(url, port);//连接FTP服务器
			//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);//登录
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, input);			
			
			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			log.info(e.getMessage());
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}
	
	
	//获取目录下的文件名
	public static List<String> getFileName(String path) {
        //String path = "F:/office"; // 路径
        File f = new File(path);
        if (!f.exists()) {
//        	log.info(path + " not exists");
            return null;
        }
        List<String> pathList = new ArrayList<String>();
        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
            	//log.info(fs.getName() + " [目录]");
            } else {
//            	log.info(fs.getName());
            	pathList.add(fs.getName());
            }
        }
        return pathList;
    }
	
	/**
	 * @param jsonData 传过来的json串 如{"":"","":""}
	 * @param src 模板路径
	 * @param desc 数据动态添加进去后生成的html路径
	 */
	//TODO
	public static boolean appendHtml_ZT(String jsonData,String src,String desc,String filePath) throws ServiceException
	{
		log.info("appendHtml_ZT jsonData======"+jsonData);
		if(!jsonData.isEmpty())
		{
			log.info("进入");
	        try {
			Gson gson = new Gson();
			Map<String, String> map = null;
			try {
				map = gson.fromJson(jsonData, Map.class);
			} catch (Exception e) {
				log.info("普通数据装载异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.TEMPLATE_DATA_ERROR[0],ConstantUtil.TEMPLATE_DATA_ERROR[1],ConstantUtil.TEMPLATE_DATA_ERROR[2],FileUtil.getStackTrace(e));
			}
			File input = new File(src);       
	        	Document doc = Jsoup.parse(input, "UTF-8", "");
	            for (String key : map.keySet()) 
	            {
	            	if(null != key && !"".equals(StringUtil.nullToString(key)))
		            {
	            		log.info("外围key:"+key);
	                    //log.info("外围map.get = " + map.get(key).toString());
	                	//String ss = key.substring(0,8);
	                    Element ele = doc.getElementById(key);//doc.getElementsByTag("");
	                    if(null != ele)
	                    {
		                    if("Delivery".equals(key))
		                    {
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("Delivery_1").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("Delivery_1").append("<img height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                    	if("2".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("Delivery_2").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("Delivery_2").append("<img height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                    }
		                    
		                    else if("ShipperYsfs".equals(key))
		                    {
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("ShipperYsfs_1").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("ShipperYsfs_1").append("<img height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                    	if("2".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("ShipperYsfs_2").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("ShipperYsfs_2").append("<img  height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                    	if("3".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("ShipperYsfs_3").append("<img  height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("ShipperYsfs_3").append("<img height='15' width='15'   src=\"../unchecked.png\"/>");
		                    	}
		                    }
		                    
		                    else if("HTSF_2".equals(key))
		                    {	
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("HTSF_2_1").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("HTSF_2_1").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("HTSF_2_2").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("HTSF_2_2").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("HTSF_2_3").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("HTSF_2_3").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("HTSF_2_4").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("HTSF_2_4").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("HTSF_2_5").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("HTSF_2_5").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	
		                    }
		                    else if("HTSF_21".equals(key))
		                    {	
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("HTSF_21_1").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("HTSF_21_1").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("HTSF_21_2").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("HTSF_21_2").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("HTSF_21_3").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("HTSF_21_3").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("HTSF_21_4").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("HTSF_21_4").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("HTSF_21_5").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("HTSF_21_5").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	
		                    }
		                    else if("hwsl".equals(key))
		                    {	                    	
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("hwsl_1").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("hwsl_1").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	if("2".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("hwsl_2").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("hwsl_2").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    }
		                    else if("PayType".equals(key) )
		                    {
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("PayType_1").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    		doc.getElementById("PayType_").append("先款后货");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("PayType_1").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    	if("4".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("PayType_4").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    		doc.getElementById("PayType_").append("先货后款");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("PayType_4").append("<img height='15' width='15'  src=\"../unchecked.png\"/>");
		                    	}
		                    }
		                    else if("fkxs".equals(key))
		                    {
		                    	if("1".equals(map.get(key).toString()) || "2".equals(map.get(key).toString()) || "3".equals(map.get(key).toString()) )
		                    	{
		                    		doc.getElementById("fkxs_1").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("fkxs_1").append("<img height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                    	if("4".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("fkxs_2").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    		doc.getElementById("cdhp").append(StringUtil.nullToString(map.get("cdhp")));	                    		
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("fkxs_2").append("<img height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                    	if("5".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("fkxs_3").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    		doc.getElementById("cdhp1").append(StringUtil.nullToString(map.get("cdhp1")));
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("fkxs_3").append("<img height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                    	if("6".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("fkxs_4").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("fkxs_4").append("<img height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                    	if("7".equals(map.get(key).toString()) || "8".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("fkxs_5").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    		doc.getElementById("tempfkxs").append(StringUtil.nullToString(map.get("tempfkxs")));
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("fkxs_5").append("<img height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                    }
		                    else if("bjfs".equals(key))
		                    {
		                    	if("1".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("bjfs_1").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("bjfs_1").append("<img height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                    	if("2".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("bjfs_2").append("<img height='15' width='15' src=\"../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("bjfs_2").append("<img height='15' width='15' src=\"../unchecked.png\"/>");
		                    	}
		                  
		                    }
		                    else if("fjtk".equals(key))
		                    {
		                    	if(!"".equals(map.get(key).toString()))
		                    	{
		                    		doc.getElementById("fjtk_").append("第十二条 附加条款&nbsp;"+map.get(key).toString());
		                    	}
		                    	else
		                    	{
		                    		doc.getElementById("fjtk_").append("");
		                    	}
		                    }
		                    else if("ewm".equals(key))
		                    {
			                     //生成二维码
			                   	//String content = "114560316058108439";
								String imgPath = filePath+"QRcode.png";  
								int width = 300, height = 300;
								ZxingEncoderHandler handler = new ZxingEncoderHandler();
								handler.encode(map.get(key), width, height, imgPath);
			                }
		                    else if("ContractNo".equals(key))
		                    {
		                   	 	//一维条码 barcode128
								int width = 280, height = 30;
								Code128Writer writer = new Code128Writer();
								File file = new File(filePath + "Barcode.jpg");	
								OutputStream out = null;	
								out = new FileOutputStream(file, true); // 是否追加	
								BitMatrix m = writer.encode(map.get(key),
								BarcodeFormat.CODE_128, width, height);
								MatrixToImageWriter.writeToStream(m, "JPEG", out);
			                }
		                    //创建表单元素 
		                    
		                    else if("zydetail".equals(key))
		                    {
		                    	StringBuffer table = new StringBuffer();
		                    	JSONArray jsonArray = JSONArray.fromObject(map.get(key));
		                		List<Map<String,Object>> mapListJson = (List)jsonArray;
		                		if(null != mapListJson && mapListJson.size()>0)
		                		{
			                		for (int i = 0; i < mapListJson.size(); i++) {
			                			Map<String,Object> obj=mapListJson.get(i);
			                			table.append("<tr>");
			                			for(Entry<String,Object> entry : obj.entrySet()){
			                	            String strkey1 = entry.getKey();
			                	            Object strval1 = entry.getValue();                	                            	     
			                	            table.append("<td>");
			                	            table.append(StringUtil.nullToString(strval1.toString()));
			                	            table.append("</td>");
			                	            log.info("KEY:"+strkey1+"  -->  Value:"+strval1+"\n");
			                	        }
			                		   table.append("</tr>");
			                		}
		                		}
		                		log.info("table.toString()==="+table.toString());
		                		ele.append(table.toString());
		                    }
		                    
							if ((!"zydetail".equals(key)) 
									&& (!"ewm".equals(key))
									&& (!"Delivery".equals(key))
									&& (!"ShipperYsfs".equals(key))
									&& (!"HTSF_2".equals(key))
									&& (!"HTSF_21".equals(key))
									&& (!"hwsl".equals(key))
									&& (!"PayType".equals(key))
									&& (!"fkxs".equals(key))
									&& (!"cdhp".equals(key))
									&& (!"cdhp1".equals(key))
									&& (!"tempfkxs".equals(key))
									&& (!"fjtk".equals(key))
									&& (!"bjfs".equals(key)))
		                    {
//								if(!"".equals(StringUtil.nullToString(map.get(key))))
//								{
									ele.html(StringUtil.nullToString(map.get(key)));
//								}
//		                    	ele.append(StringUtil.nullToString(map.get(key)));
//		                    	ele.parent().append(StringUtil.nullToString(map.get(key)));
//		                    	ele.remove();
		                    }
	                    }
//	                    else
//	                    {
//	                    	throw new ServiceException(ConstantUtil.TEMPLATE_DATA_IS_NOT_EXIST[0],"模板中"+key+"不存在",key +" does not exist.");
//	                    }
	                }
	            	else
	            	{
	            		throw new ServiceException(ConstantUtil.TEMPLATE_DATA_IS_NOT_EXIST[0],"模板中"+key+"不存在",key +" does not exist.");
	            	}
	            }           
	            String content = doc.toString();
				if(FileUtil.writeTxtFile(content, new File(desc)))
					return true;
			} catch (ServiceException e) {
				log.info("普通数据装载异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
			}catch (Exception e) {
				log.info("普通数据装载异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[0],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[1],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[2]);
			}
		}
		return false;
	}
	
	public static boolean appendHtml(String jsonData,String src,String desc,String filePath) throws ServiceException
	{
		log.info("jsonData======"+jsonData);
		if(!jsonData.isEmpty())
		{   
	        try {
				Gson gson = new Gson();
				Map<String, String> map = null;
				try {
					map = gson.fromJson(jsonData, Map.class);
				} catch (Exception e) {
					log.info("普通数据装载异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.TEMPLATE_DATA_ERROR[0],ConstantUtil.TEMPLATE_DATA_ERROR[1],ConstantUtil.TEMPLATE_DATA_ERROR[2],FileUtil.getStackTrace(e));
				}
				File input = new File(src);   
	        	Document doc = Jsoup.parse(input, "UTF-8", "");
	            for (String key : map.keySet()) 
	            {
	            	if(null != key && !"".equals(StringUtil.nullToString(key)))
		            {
	            		log.info("通用外围key:"+key);
	                    Element ele = doc.getElementById(key);
//	                    if(null != ele && !"".equals(StringUtil.nullToString(map.get(key))))
	                    if(null != ele)
	                    {
	                    	/*
		                    if(key.length()>=8 && "checkbox".equals(key.substring(0,8)))
		                    {
		                    	if("checked".equals(map.get(key).toString()))
		                    	{
		                    		ele.append("<img height='15' width='15' src=\"../../checked.png\"/>");
		                    	}
		                    	else
		                    	{
		                    		ele.append("<img height='15' width='15' src=\"../../unchecked.png\"/>");
		                    	}
		                    } 
		                    */                   
		                    //创建表单元素 
	                    	if(!"".equals(StringUtil.nullToString(map.get(key))))
	                    	{
	                    		ele.html(StringUtil.nullToString(map.get(key)));
	                    	}
//	                    	ele.append(StringUtil.nullToString(map.get(key)));
//	                    	ele.parent().append(StringUtil.nullToString(map.get(key)));
//	                    	ele.remove();		               
	                    }
	                    else
	                    {
	                    	throw new ServiceException(ConstantUtil.TEMPLATE_DATA_IS_NOT_EXIST[0],"模板中"+key+"不存在",key +" does not exist.");
	                    }
	                }
	            	else
	            	{
	            		throw new ServiceException(ConstantUtil.TEMPLATE_DATA_IS_NOT_EXIST[0],"模板中"+key+"不存在",key +" does not exist.");
	            	}
	            }     
	            String content = doc.toString();
				if(FileUtil.writeTxtFile(content, new File(desc)))
					return true;
			} catch (ServiceException e) {
				log.info("普通数据装载异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
			}catch (Exception e) {
				log.info("普通数据装载异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[0],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[1],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[2]);
			}
		}
		return false;
	}
	/**
	 * 往word模板里添加信息
	 * @param jsonData
	 * @param src
	 * @param desc
	 * @return
	 * @throws ServiceException
	 */
	public static boolean appendWord(String jsonData,String src,String desc) throws ServiceException
	{
		log.info("jsonData======"+jsonData);
		if(!jsonData.isEmpty())
		{   
	        try {
				Gson gson = new Gson();
				Map<String, String> map = null;
				try {
					map = gson.fromJson(jsonData, Map.class);
				} catch (Exception e) {
					log.info("普通数据装载异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.TEMPLATE_DATA_ERROR[0],ConstantUtil.TEMPLATE_DATA_ERROR[1],ConstantUtil.TEMPLATE_DATA_ERROR[2],FileUtil.getStackTrace(e));
				}
				String path = System.getProperty("user.dir") + File.separator +"conf"+ File.separator +"license.xml";
				File licenseFile = new File(path);
				if (!licenseFile.isFile()) {
	    			path = System.getProperty("user.dir") + File.separator + "src" +  File.separator + "license.xml";
	    		}
		    	InputStream license = new FileInputStream(licenseFile);
		    	License aposeLic = new License();
		        aposeLic.setLicense(license);
		        com.aspose.words.Document doc = new com.aspose.words.Document(src);
	            for (String key : map.keySet()) 
	            {
	            	if(null != key && !"".equals(StringUtil.nullToString(key)))
		            {
	            		log.info("通用外围key:"+key);
	                
	            		doc.getRange().replace(key, StringUtil.nullToString(map.get(key)), false, false);
	                }
	            	else
	            	{
	            		throw new ServiceException(ConstantUtil.TEMPLATE_DATA_IS_NOT_EXIST[0],"模板中"+key+"不存在",key +" does not exist.");
	            	}
	            } 
	            FileOutputStream fileOS = new FileOutputStream(desc);
	            doc.save(fileOS, SaveFormat.PDF);
//	            doc.save(desc);
				return true;
			} catch (ServiceException e) {
				log.info("普通数据装载异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
			}catch (Exception e) {
				log.info("普通数据装载异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[0],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[1],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[2]);
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param map 包含图片的路径,高度,宽度
	 * @param src 没有加载图片的html文件
	 * @param desc 加载图片后的html文件
	 * @throws ServiceException
	 */
	public static void appendImg(Map<String, String> map,String src,String desc) throws ServiceException
	{
		try{
			File input = new File(src);
			
			Document doc = Jsoup.parse(input, "UTF-8", "");
            
            String height = map.get("height");
            
            String width = map.get("width");
            
            String imgPath = map.get("imgPath");
            
            String contractPath = map.get("contractPath");
            
            System.out.println("height==="+height+",width==="+width);
            
            File img = new File(imgPath);
            
//            for (String key : map.keySet()) {
//            	if(!"".equals(StringUtil.nullToString(key)))
//	            {
                    Element ele = doc.getElementById(map.get("key"));                    
                    ele.append("<img height='"+height+"' width='"+width+"' src='./"+img.getName()+"'>");
//                    ele.append("<img height='"+height+"' width='"+width+"' src='../.."+contractPath+img.getName()+"'>");  	               
//                }
//            } 
            String content = doc.toString();
            System.out.println(content);
            FileUtil.writeTxtFile(content, new File(desc));
            
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[0],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[1],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[2]);
		}
    	
	}
	 public static boolean writeTxtFile(String content,File  fileName){
		boolean flag = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(content.getBytes("UTF-8"));
			flag = true;
		} catch (Exception e) {
			flag = false;
			log.info(e.getMessage());
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.info(e.getMessage());
				}
			}
		}
		return flag;
	}
	
	/**
	  * 创建文件
	  * @param fileName
	  * @return
	  */
	 public static boolean createFile(File fileName)throws Exception{
	  boolean flag = false;
	  try{
	   if(!fileName.exists()){
	    fileName.createNewFile();
	    flag=true;
	   }
	  }catch(Exception e){
	   log.info(e.getMessage());
	  }
	  return true;
	 }
	
	public static void Copy1(File oldfile, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			// File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldfile);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					log.info(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			log.info("error  ");
			log.info(e.getMessage());
		}

	}
	public static void Copy(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
//					log.info(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			log.info("error  ");
			log.info(e.getMessage());
		}
	}
	
	
  public static boolean move(String srcFile, String destPath){ 
	  // File (or directory) to be moved 
	  File file = new File(srcFile); 
	  
	  // Destination directory 
	  File dir = new File(destPath); 
	  
	  // Move file to new directory 
	  boolean success = file.renameTo(new File(dir, file.getName())); 
	  
	  return success; 
	} 
	
	/** 
    * 复制单个文件 
    * @param oldPath String 原文件路径 如：c:/fqf.txt 
    * @param newPath String 复制后路径 如：f:/fqf.txt 
    * @return boolean 
    */ 
  public static void copyFile(String oldPath, String newPath) { 
	  InputStream inStream = null;
	  FileOutputStream fs = null;
      try { 
          int bytesum = 0; 
          int byteread = 0; 
          File oldfile = new File(oldPath); 
          if (oldfile.exists()) {                  //文件存在时 
              inStream = new FileInputStream(oldPath);      //读入原文件 
              fs = new FileOutputStream(newPath); 
              byte[] buffer = new byte[1024 * 8]; 
              int length; 
              while ( (byteread = inStream.read(buffer)) != -1) { 
                  bytesum += byteread;            //字节数 文件大小 
                //  log.info(bytesum); 
                  fs.write(buffer, 0, byteread); 
              } 
          } 
      }  catch (Exception e) { 

    	  log.info("复制单个文件操作出错"); 

          log.info(e.getMessage()); 
      }
      finally
      {
    	  if (null != inStream)
    	  {
    		  try {
				inStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.info(e.getMessage());
			}
    	  }
    	  if (null != fs)
    	  {
    		  try {
				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.info(e.getMessage());
			}
    	  }
      }
  } 
	

	public static void copyFolder(File src, File dest) {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}
			String files[] = src.list();
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// 递归复制
				copyFolder(srcFile, destFile);
			}
		} else {
			InputStream in = null;
			OutputStream out = null;
			try {
				 in = new FileInputStream(src);
				 out = new FileOutputStream(dest);
	
				byte[] buffer = new byte[1024];
	
				int length;
				
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				
			
			}
			catch (Exception e)
			{
				log.info(e.getMessage());
			}
			finally
			{
				try{
					if (in != null)
					{
						in.close();
					}
					if (out != null)
					{
						out.close();
					}
				}catch (Exception e) {
					log.info(e.getMessage());
				}
				
			}
		}
	}
	
	public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if(file.exists()) {
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            return false;
        }
        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            if(!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            log.info(e.getMessage());
            return false;
        }
    }
   
   
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }
   
   
    public static String createTempFile(String prefix, String suffix, String dirName) {
        File tempFile = null;
        if (dirName == null) {
            try{
                //在默认文件夹下创建临时文件
                tempFile = File.createTempFile(prefix, suffix);
                //返回临时文件的路径
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                log.info(e.getMessage());
                return null;
            }
        } else {
            File dir = new File(dirName);
            //如果临时文件所在目录不存在，首先创建
            if (!dir.exists()) {
                if (!FileUtil.createDir(dirName)) {
                    return null;
                }
            }
            try {
                //在指定目录下创建临时文件
                tempFile = File.createTempFile(prefix, suffix, dir);
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                log.info(e.getMessage());
                return null;
            }
        }
    }
    /**
    *需要import的包有如下
    *import com.aspose.words.Document;
    *import com.aspose.words.DocumentBuilder;
    *import com.aspose.words.ImageData;
    *import com.aspose.words.Node;
    *import com.aspose.words.NodeCollection;
    *import com.aspose.words.NodeType;
    *import com.aspose.words.Shape;
    *import com.aspose.words.ShapeType;
    *
    *aspose.words.for.java 版本 3.1.0
    *
    * 将word文档中wmf图片导出并替换为标签
    *
    * @param fileName 文档完整名
    * @return 修改后的文件名
    */
    public static String exportWmfFromDoc(String imagePath,String fileName,String filePath) {
	    try {
	    	
	    	String path = System.getProperty("user.dir") + File.separator +"conf"+ File.separator +"license.xml";
			File licenseFile = new File(path);
			if (!licenseFile.isFile()) {
    			path = System.getProperty("user.dir") + File.separator + "src" +  File.separator + "license.xml";
    		}
	    	InputStream license = new FileInputStream(licenseFile);
	    	License aposeLic = new License();
            aposeLic.setLicense(license);
//			InputStream word = new FileInputStream(filePath + fileName);// 原始word路径
	    	com.aspose.words.Document doc = new com.aspose.words.Document(filePath + fileName);// 新建文档对象
	    	
//	    	NodeType.COMMENT;
	        NodeCollection shapeCollection = doc.getChildNodes(NodeType.SHAPE,true);// 查询文档中所有wmf图片
	    	
	        Node[] shapes = shapeCollection.toArray();// 序列化
	        
	        
//	        NodeCollection shapeCollection2 = doc.getChildNodes(NodeType.COMMENT,true);
//	        Node nn = shapeCollection2.get(0);
//	        System.out.println("eee"+nn.getText()); 
	        
	        String imgPath = "";
	        
	        if (shapes.length > 0) {// 如果文档存在图片
	            File file = new File(imagePath + fileName.substring(0, fileName.lastIndexOf(".")));
	            if (file != null) {
	                if (file.exists() || file.mkdir()) {// 创建文档图片保存文件夹
	                    imgPath = file.getAbsolutePath() + "\\";
	                } else {
	                    throw new Exception("文档图片保存路径不可写，请检查路径:\"" + imagePath+ "\"");
	                }
	            }
	            for (Node node : shapes) {
	                Shape shape = (Shape) node;
	                if (shape.getShapeType() == ShapeType.OLE_OBJECT) {// 如果shape类型是ole类型
	                    ImageData i = shape.getImageData();// 获得图片数据
//	                    String imageName = imageName() + ".wmf";
	                    String imageName = "test.wmf";
	                    i.save(imgPath + imageName);// 导出图片
	                    File f = new File(imgPath + imageName);
	                    if (f.exists()) {
//	                        imageName = wmfToPNG(f.getAbsolutePath());
//	                        log.debug("f.path--->" + f.getAbsolutePath());
//	                        Exec.saveMinPhoto(imageName, imageName, (double)38, (double)0);
//	                        if (f.canWrite()) {
//	                            f.delete();
//	                        }
	                    } else {
	                        log.error("图片不存在！");
	                        continue;
	                    }
	                    log.info("f.name--->" + f.getName());
	                    DocumentBuilder builder = new DocumentBuilder(doc);// 新建文档节点
	                    builder.moveTo(shape);// 移动到图片位置
	                    builder.write("[img]"
	                            + fileName.substring(0, fileName.lastIndexOf("."))
	                            + "/"
	                            + f.getName().substring(0,f.getName().lastIndexOf(".")) + ".png"
	                            + "[/img]");// 插入替换文本
	                    shape.remove();// 移除图形
	            
	                } else if (shape.getShapeType() == ShapeType.IMAGE) {// 如果shape类型是ole类型
	                    ImageData i = shape.getImageData();// 获得图片数据
//	                    String imageName = imageName() + ".png";
	                    String imageName = "yikuwang.png";
	                    i.save(imgPath + imageName);// 导出图片
	                    File f = new File(imgPath + imageName);
	                    if (f.exists()) {
	                        DocumentBuilder builder = new DocumentBuilder(doc);// 新建文档节点
	                        builder.moveTo(shape);// 移动到图片位置
//	                        builder.write("[img]"
//	                                + fileName.substring(0, fileName.lastIndexOf("."))
//	                                + "/"
//	                                + f.getName().substring(0,f.getName().lastIndexOf("."))
//	                                + ".png" + "[/img]");// 插入替换文本
	                        builder.write("111111111111");
	                        shape.remove();// 移除图形
	                    } else if(shape.getShapeType() == ShapeType.TEXT_SIMPLE) 
	                    {
	                    	System.out.println("1111111111");
	                    }
	                    else {
	                        log.error("图片不存在！");
	                        continue;
	                    }
	                }
	            }
	            String extName = fileName.substring(fileName.lastIndexOf("."));
	            String mainName = fileName.substring(0, fileName.lastIndexOf("."));
	            doc.save(filePath + mainName + "_done" + extName);// 保存修改后的文档
	            // log.info("filename---->" + mainName + "_done" + extName);
	            return mainName + "_done" + extName;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
    }
    /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
	public static String readTxtFile(String filePath) throws ServiceException
	{
		StringBuffer sb = new StringBuffer();
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
					sb.append("<br>");
				}
				read.close();
			} else {
				throw new ServiceException("readTxtFile","找不到指定的文件","");
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new ServiceException("readTxtFile","读取文件内容出错","");
		}
		return sb.toString();
	}
    
	public static void parseHtml(String src,String desc,String filePath) throws ServiceException
	{
		   
        try {
			Gson gson = new Gson();
			File input = new File(src);   
        	Document doc = Jsoup.parse(input, "UTF-8", "");
           
                   
               
              
            String content = doc.toString();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
    public static void main(String[] args) throws FileNotFoundException {
//        //创建目录
//        String dirName = "D:/work/temp/temp0/temp1";
//        FileUtil.createDir(dirName);
//        //创建文件
//        String fileName = dirName + "/temp2/tempFile.txt";
//        FileUtil.createFile(fileName);
//        //创建临时文件
//        String prefix = "temp";
//        String suffix = ".txt";
//        for (int i = 0; i < 10; i++) {
//            log.info("创建了临时文件："
//                    + FileUtil.createTempFile(prefix, suffix, dirName));
//        }
//        //在默认目录下创建临时文件
//        for (int i = 0; i < 10; i++) {
//            log.info("在默认目录下创建了临时文件："
//                    + FileUtil.createTempFile(prefix, suffix, null));
//        }
    	
		 //第一种方式：
//        GetAllFiles outter = new GetAllFiles();
//        GetAllFiles.Inner inner = outter.new Inner();  //必须通过Outter对象来创建
//        File dir1 = new File("F:\\office\\CPC024422449044526");
//        inner.getAllFiles(dir1, 0);
//        List<File> list1  = outter.getList();
//        System.out.println(list1.size()); 
//        for (File file : list1) {
//			System.out.println(file);
//		}
//    	String fileSrc = "F:\\office\\test\\0.jpg";
//    	String logoSrc = "F:\\office\\test\\log.png";
//    	String fileOut = "F:\\office\\test\\compose.jpg";
//    	String imgData = "{\"nw\":892,\"nh\":1263,\"w\":892,\"h\":300,\"data\":{\"0\":{\"y\":240,\"x\":342,\"sw\":206,\"sh\":246,\"snw\":206,\"snh\":246,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjA2IiBoZWlnaHQ9IjI0NiI+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAyNSAxIGMgLTAuMDIgMC4wOSAtMC45OSAzLjMzIC0xIDUgYyAtMC4zMSAzOS4zNiAtMC44NyA4Mi44MSAwIDEyMCBjIDAuMDYgMi42NSAzIDggMyA4Ii8+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAyNyA2IGMgMC4wOSAwLjAyIDMuODQgMC4zNCA1IDEgYyAwLjgyIDAuNDcgMS4wOCAyLjQyIDIgMyBjIDE4LjczIDExLjgxIDQyLjIxIDI2LjUxIDYzIDM4IGMgMy44NCAyLjEyIDkuMDggMi4zOSAxMyA0IGMgMS40NCAwLjU5IDIuNjMgMi4zOSA0IDMgYyAxLjQgMC42MiAzLjYgMC4zOCA1IDEgYyAxLjM3IDAuNjEgMi42MiAyLjUgNCAzIGMgMS45NSAwLjcxIDUuMTcgMC4yNyA3IDEgYyAxLjEgMC40NCAxLjkzIDIuMzkgMyAzIGMgMS4wNCAwLjU5IDIuOCAwLjQ1IDQgMSBjIDIuMzQgMS4wNyA0LjY2IDMuMDMgNyA0IGMgMS40OCAwLjYyIDMuNTMgMC4zMiA1IDEgYyA2LjIxIDIuODQgMTIuNDkgNy41MiAxOSAxMCBjIDcuMjEgMi43NSAxNS40NiAzLjU2IDIzIDYgYyA0LjgzIDEuNTcgMTMuMjQgNC43NCAxNCA2IGMgMC40NSAwLjc0IC01LjIxIDIuOTcgLTggNCBjIC02LjIxIDIuMyAtMTIuODkgNC40NyAtMTkgNiBjIC0xLjUzIDAuMzggLTMuNzcgLTAuNDYgLTUgMCBjIC0xLjA0IDAuMzkgLTEuOTMgMi4zOSAtMyAzIGMgLTEuMDQgMC41OSAtMi45NCAwLjMxIC00IDEgYyAtNS4xMiAzLjMzIC0xMC43OCA3LjY4IC0xNiAxMiBjIC00LjYyIDMuODIgLTkuNjggOC4yIC0xMyAxMiBjIC0wLjc3IDAuODkgLTAuMzEgMy4wMiAtMSA0IGwgLTYgNiIvPjxwYXRoIHN0cm9rZS1saW5lam9pbj0icm91bmQiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLXdpZHRoPSIzIiBzdHJva2U9IiMwMDAwMDAiIGZpbGw9Im5vbmUiIGQ9Ik0gMjIgMTIzIGMgMC4zMyAwLjAyIDEyLjk2IC0wLjA0IDE5IDEgYyA1LjMyIDAuOTEgMTAuNjkgMi45OSAxNiA1IGMgNy4yNCAyLjc0IDEzLjc0IDYuMyAyMSA5IGMgNy4zNCAyLjczIDE0LjUgNC44OSAyMiA3IGMgMy4zMSAwLjkzIDYuNjggMS4wNiAxMCAyIGMgOS44OSAyLjc5IDE5LjMyIDYuMzggMjkgOSBjIDIuNTcgMC42OSA1LjYgMC4yNSA4IDEgYyAyLjY3IDAuODMgNS4zMyAzLjI2IDggNCBjIDMgMC44MyAxMCAxIDEwIDEiLz48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDEyNCAxMSBjIC0wLjA3IDAuMDUgLTMuMjIgMS44MiAtNCAzIGMgLTAuOTkgMS40OSAtMS4wNyA0LjEzIC0yIDYgYyAtNC4wMyA4LjA3IC04LjE5IDE2LjE1IC0xMyAyNCBjIC00LjMxIDcuMDIgLTkuNTYgMTIuOTkgLTE0IDIwIGMgLTQuNCA2Ljk0IC03LjU0IDE0LjEgLTEyIDIxIGMgLTYuMDggOS40MiAtMTMuMTMgMTcuNjEgLTE5IDI3IGMgLTQuMjEgNi43NCAtNy4wNSAxNC4wMiAtMTEgMjEgYyAtMS44IDMuMTggLTQuMjMgNS44MSAtNiA5IGMgLTQuOTkgOC45OCAtOS42NiAxNy42MiAtMTQgMjcgYyAtNC4xNiA4Ljk5IC03LjA1IDE4LjA2IC0xMSAyNyBjIC0xLjA5IDIuNDcgLTMuMjEgNC41NSAtNCA3IGMgLTIuNDMgNy41MiAtMy41NCAxNi4wNiAtNiAyNCBjIC0xLjkyIDYuMiAtNyAxOCAtNyAxOCIvPjwvc3ZnPg==\"}}}";
//    	String imgData = "{\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":3335,\"x\":293,\"sw\":330,\"sh\":404,\"snw\":330,\"snh\":404,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMzMwIiBoZWlnaHQ9IjQwNCI+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAxIDEyNyBjIDAuMDIgMC4wOSAwLjk4IDMuMzMgMSA1IGMgMC4zMSAyNC45MyAtMC4zMSA1MS40OCAwIDc2IGMgMC4wMiAxLjMzIDAuMTggMy45NyAxIDQgYyAxMS4zMSAwLjM5IDM3LjI2IC0xLjg2IDU3IC0yIGMgNzYuODMgLTAuNTUgMTUzLjcxIDAuNiAyMjIgMCBjIDEuNjYgLTAuMDEgMy4zNiAtMS43OCA1IC0yIGMgMy4wNSAtMC40MSAxMCAwIDEwIDAiLz48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDIgMTI0IGMgNS43MiAwIDI3OC43IC0wLjU5IDMyNyAwIGMgMC42MSAwLjAxIC0wLjQ0IDIuOCAtMSA0IGMgLTEuNyAzLjY1IC0zLjczIDcuNTEgLTYgMTEgYyAtMi4wNiAzLjE3IC00LjkgNS43OSAtNyA5IGMgLTQuMyA2LjU3IC05LjI1IDEzLjgyIC0xMiAyMCBjIC0wLjgyIDEuODUgMC40OSA1LjAzIDAgNyBjIC0wLjQxIDEuNjQgLTIuMzIgMy4zMSAtMyA1IGMgLTAuNTkgMS40OCAtMC45MyAzLjMzIC0xIDUgYyAtMC4yNiA2LjU1IDAgMjAgMCAyMCIvPjxwYXRoIHN0cm9rZS1saW5lam9pbj0icm91bmQiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLXdpZHRoPSIzIiBzdHJva2U9IiMwMDAwMDAiIGZpbGw9Im5vbmUiIGQ9Ik0gMTU3IDEgYyAwIDIuNDIgMC44IDkwLjg5IDAgMTM4IGMgLTAuMjMgMTMuMzUgLTIuNDcgMjUuNjUgLTMgMzkgYyAtMC41IDEyLjYyIDAuNTIgMjQuOTYgMCAzNyBjIC0wLjEzIDMgLTEuNzkgNS45OCAtMiA5IGMgLTAuNzggMTEuNDkgLTAuMTUgMjMuMjUgLTEgMzUgYyAtMC40OSA2Ljc5IC0yLjY1IDEzLjI3IC0zIDIwIGMgLTAuNjUgMTIuMzIgLTAuNzcgMjUuOTkgMCAzNyBjIDAuMTQgMiAyLjkzIDQgMyA2IGMgMC44MyAyNC4xNSAwIDc5LjU4IDAgODEgYyAwIDAuMDkgLTAuMzMgLTMuNDYgMCAtNSBsIDMgLTkiLz48L3N2Zz4=\"}}}";
//    	String imgData = "{\"length\":2,\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":2584,\"x\":139,\"sw\":22,\"sh\":144,\"snw\":22,\"snh\":144,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjIiIGhlaWdodD0iMTQ0Ij48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDIxIDEgYyAwIDAuMzUgMC42MyAxMy42OCAwIDIwIGMgLTAuMzMgMy4zIC0yLjM1IDYuNTcgLTMgMTAgYyAtMi4wMyAxMC42NSAtMy42OCAyMS4wMyAtNSAzMiBjIC0xLjA3IDguODQgLTAuNjkgMTcuMjggLTIgMjYgYyAtMi43NSAxOC4zNyAtMTAgNTQgLTEwIDU0Ii8+PC9zdmc+\"},\"1\":{\"y\":2585,\"x\":431,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjA5IiBoZWlnaHQ9IjE0MCI+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAxIDM1IGMgMC4zMiAtMC4xNiAxMS43IC02LjE4IDE4IC05IGMgMTMuNjEgLTYuMSAyNy4wNiAtMTIuMzQgNDAgLTE3IGMgMi45OSAtMS4wOCA2Ljg5IC0wLjIyIDEwIC0xIGMgMy4zMyAtMC44MyA2LjYxIC0zLjMgMTAgLTQgYyA3LjYyIC0xLjU3IDE5LjI3IC0zLjU1IDI0IC0zIGMgMS4yNCAwLjE0IDEuNiAzLjk1IDIgNiBjIDEuMjYgNi41NyAyLjU4IDEzLjI3IDMgMjAgYyAwLjU4IDkuMzIgMS4yOCAxOS40IDAgMjggYyAtMC45MyA2LjIyIC00LjE3IDEzLjE0IC03IDE5IGMgLTEuNyAzLjUzIC01LjEzIDYuNzIgLTcgMTAgYyAtMC42MyAxLjExIC0wLjM5IDMuMDggLTEgNCBjIC0wLjU0IDAuOCAtMi4zMyAxLjEyIC0zIDIgYyAtMy4zNSA0LjM4IC02LjU0IDEwLjU2IC0xMCAxNSBjIC0wLjk2IDEuMjMgLTIuOSAxLjkgLTQgMyBjIC0wLjggMC44IC0xLjE1IDIuMzIgLTIgMyBjIC0yLjIzIDEuNzggLTUuNzcgMy4yMiAtOCA1IGMgLTAuODUgMC42OCAtMS4xOSAyLjE5IC0yIDMgYyAtMi4wOCAyLjA4IC00LjY4IDQuNTIgLTcgNiBjIC0xLjA0IDAuNjYgLTIuODkgMC4zNyAtNCAxIGMgLTMuMjggMS44NyAtMTAuOTUgNi42OCAtMTAgNyBjIDMuODIgMS4yNyAzMy4xNCA0LjYxIDQ5IDYgYyAyLjU5IDAuMjMgNS4yOSAtMC45IDggLTEgYyA2LjM4IC0wLjI0IDEyLjcgMC43NiAxOSAwIGMgMjkuODEgLTMuNTkgODkgLTEzIDg5IC0xMyIvPjwvc3ZnPg==\"}}}";
//    	ComposePicture.composeImg(imgData,"","");
//    	try {
//			String lineTxt = readTxtFile("E:/download/CP2239697556196518/ContractRecord/ContractSHA1.txt");
//			System.out.println(lineTxt);
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			log.info(e.getMessage());
//		}
//    	String jsonData = "{\"loops1\":\"中国\",\"loops2\":\"女排\",\"loops3\":\"加油\",\"loops4\":\"买卖网\",\"loops5\":\"中国云签\",\"loops6\":\"发工资\"}";
//    	String jsonData = "{\"loops1\":\"NYH-16-SCT-00056\",\"loops2\":\"\u5218\u632f\u521a\",\"loops3\":\"\u65e0\",\"loops4\":\"\u58f9\u4e07\u5706\",\"loops5\":\"10000.00\",\"loops6\":\"2016\",\"loops7\":\"07\",\"loops8\":\"01\",\"loops9\":10,\"loops10\":\"11111\",\"loops11\":\"100000.0000\",\"loops12\":\"\u4e2d\u56fd\u94f6\u884c\",\"loops13\":\"2016\",\"loops14\":\"08\",\"loops15\":\"17\",\"loops16\":\"2016\",\"loops17\":\"05\",\"loops18\":\"31\"}";
//    	try {
//			appendHtml(jsonData, "E:/office/143718886090505.html", "E:/office/tttttttt.html", "");
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	copyFolder(new File("d:/work/src"), new File("d:/work/desc"));
//    	ZipUtil.zip("d:\\work\\desc", "d:\\work", "test.zip");
//    	File authorityFile = new File("E:\\office\\201606\\CP6121352765713189");
//    	File fa[] = authorityFile.listFiles();
//    	System.out.println(fa.length);
//    	System.out.println(fa[0].getName().substring(0,fa[0].getName().lastIndexOf(".")));
//    	int b = 9;
//    	float a = 0.5f;
//    	float d = a+b;
//    	System.out.println(d);
//    	exportWmfFromDoc("D:/word/","test.docx","D:/word/");
//    	try {
//			appendWord(jsonData, "D:/word/AOP.doc", "D:/word/out.pdf");
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
//    	try {
//			parseHtml("d:/database.html", "", "");
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	Calendar calendar = Calendar.getInstance();
		/**
         * 获取年份
         */
        int year = calendar.get(Calendar.YEAR);
        /**
         * 获取月份
         */
        int month = calendar.get(Calendar.MONTH) + 1;
        int day =calendar.get(Calendar.DAY_OF_MONTH);
        
//        System.out.println(createEvidenceFolder("CZ001"));
        	writeCustomzieFile();
          readCustomzieFile();
//        byte[] b = new byte[]{};
//        byte[] b1 = new byte[10];
//        		b1 = "YFF".getBytes();
//        byte[] b2 ="vereion1".getBytes();
//        b = byteMerger(b1,b2);
//       
//        
//        String str = ",YFF,version1,1,,247733,"+
//        		new File("F:/test_pdf_sign.pdf").length();
//        
//        int a = str.getBytes().length+1;
//        str = a + str;
//        
//        System.out.println("b1.length=="+str.getBytes());
//        System.out.println(new String(str.getBytes()));
//        new File("F:/test_pdf_sign.pdf").length();
//        b1 = "1".getBytes(
//        	System.out.println(new String(byteMerger("cd".getBytes(),"ab".getBytes())));
//        	System.out.println(SHA_MD.encodeFileSHA1(new File("E:/download/Z_1_20160612105005908.pdf")).toHexString());
//        	System.out.println(SHA_MD.encodeFileSHA1(new File("E:/download/Z_1_20160612105005908_yff.pdf")).toHexString());
//        	System.out.println(SHA_MD.encodeFileSHA1(new File("E:/download/CP6129980565699134/ContractRecord/Contract/Z_1_20160612105005908.pdf")).toHexString());
    }
    
    public static void writeCustomzieFile() {  
        File srcFile = new File("E://download//CP6129980565699134.zip");  
        File dstFile = new File("E://download//CP6129980565699134.YFF");  
        BufferedInputStream in = null;  
        BufferedOutputStream out = null;          
        try {  
        	/**
        	 * 文件大小
    			byte[4]
        	 * 文件类型
    			byte[3] YFF
    			文件主版本号
    			byte[1]
    			文件副版本号
    			byte[1]
    			文件压缩方式
    			byte[1]   			
    			数据正文开始位置
    			byte[4]
    			数据正文
    			byte[N]
        	 */
        	String str = ",YFF,01,01,01,"+srcFile.length()+" ";
        	int a = str.getBytes().length+1;
        	str = a+str;
        	in = new BufferedInputStream(new FileInputStream(srcFile));  
            out = new BufferedOutputStream(new FileOutputStream(dstFile));  
          
            int len = -1;  
            byte[] b = new byte[1024]; 
            int temp = 0;
            while((len = in.read(b)) != -1) {
//            	if(temp>0)
//            	{
        			 byte[] b_temp = byteMerger(new byte[1],b);
            		 out.write(b_temp, 0, b_temp.length);
//            	}else{
//            		 byte[] b_temp = byteMerger(str.getBytes(),b);
//            		 out.write(b_temp, 0, b_temp.length);
//            		out.write(b, 0, b.length);
//            	}
                temp++;
            }
        }catch (Exception e) {  
            // TODO: handle exception  
            e.printStackTrace();  
        }finally {  
            if(in != null) {  
                try {  
                    in.close();  
                }catch (Exception e) {  
                    // TODO: handle exception  
                    e.printStackTrace();  
                }  
            }  
            if(out != null) {  
                try {  
                    out.close();  
                }catch (Exception e) {  
                    // TODO: handle exception  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
	public static void readCustomzieFile() {  
        File srcFile = new File("E://download//CP6129980565699134.YFF");  
        File dstFile = new File("E://download//CP6129980565699134_2.zip"); 
        BufferedInputStream in = null;  
        BufferedOutputStream out = null;          
        try {  
        	/**
        	 * 文件类型
    			byte[3] YFF
    			文件主版本号
    			byte[1]
    			文件副版本号
    			byte[1]
    			文件压缩方式
    			byte[1]
    			文件大小
    			byte[4]
    			数据正文开始位置
    			byte[4]
    			数据正文
    			byte[N]
        	 */
        	in = new BufferedInputStream(new FileInputStream(srcFile));  
        	
            out = new BufferedOutputStream(new FileOutputStream(dstFile));  
//            byte[] b_len = new byte[2];
//            in.read(b_len, 0, 2);
//            System.out.println(new String(b_len));
//            in.skip(Integer.parseInt(new String(b_len))-1);
            int len = -1;  
            byte[] b = new byte[1025];           
            while((len = in.read(b)) != -1) {     	
//            	if(temp>0)
//            	{
            		byte[] b_temp = new byte[1024];
            		System.arraycopy(b, 1, b_temp, 0, b_temp.length);  
           		 	out.write(b_temp, 0, b_temp.length);
//           		 	System.out.println("b_temp.length==="+b_temp.length);
//            		out.write(b, 1, len-1);	
//            	}          	   	
            }
        }catch (Exception e) { 
            e.printStackTrace();  
        }finally {  
            if(in != null) {  
                try {  
                    in.close();  
                }catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
            if(out != null) {  
                try {  
                    out.close();  
                }catch (Exception e) {  
                    // TODO: handle exception  
                    e.printStackTrace();  
                }  
            }  
        }  
    } 
}
