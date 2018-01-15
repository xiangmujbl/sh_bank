package com.mmec.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 二进制,字节数组,字符,十六进制
 * 
 * 
 */
public abstract class StringUtil 
{
	//云签查询多条记录查询SQL
	public final static String querySql = " SELECT t.id column0,t.title column1,t.serial_num column2,t.creator creator,t.is_show isShow,t.status status,t.type column3," +
			" t.platform_id column5,t.create_time column7,record.contract_id column8,record.singer_id column9,record.sign_time column10," +
			" record.sign_status column11,record.sign_mode column12,record.sign_type column13" +
			" FROM c_contract t LEFT JOIN  c_sign_record record on t.id = record.contract_id  where 1=1 " +
			" AND record.sign_type != 1 ";
	//云签草稿箱查询合同
	public final static String draftQuerySql = " SELECT t.id column0,t.title column1,t.serial_num column2,t.creator creator,t.is_show isShow,t.status status,t.type column3," +
			" t.platform_id column5,t.create_time column7,record.contract_id column8,record.singer_id column9,record.sign_time column10," +
			" record.sign_status column11,record.sign_mode column12,record.sign_type column13 " +
			" FROM c_contract t LEFT JOIN  c_sign_record record on t.id = record.contract_id  where 1=1 " +
			" AND t.creator = record.singer_id  AND t.status = 0 AND t.is_show = 1 AND record.sign_type != 1 ";
	//云签查询多条记录统计数量SQL
	public final static String countSql = " SELECT count(t.id) FROM c_contract t LEFT JOIN c_sign_record record ON t.id = record.contract_id  where 1=1 AND  record.sign_type != 1 ";
	
	//云签草稿箱查询多条记录统计数量SQL
	public final static String draftCountSql = " SELECT count(t.id) FROM c_contract t LEFT JOIN c_sign_record record ON t.id = record.contract_id  where 1=1 AND t.status = 0 AND t.creator = record.singer_id AND t.is_show = 1 AND record.sign_type != 1 ";

	//换联网金融批量查询SQL
	public final static String internetQuerySql = " SELECT platform.id column0,platform.program column1,t.id column2,t.title column3,t.serial_num column4,t.type column5,t.creator column6," +
			" t.platform_id column7,t.status column8,t.create_time column9,record.contract_id column10,record.singer_id column11,record.sign_time column12,record.sign_status column13,record.sign_mode column14,record.sign_type column15 " +
			" FROM  c_platform platform LEFT JOIN c_contract t  LEFT JOIN c_sign_record record ON t.id = record.contract_id ON platform.id = t.platform_id " +
			"  WHERE 1 = 1  AND record.sign_type != 1 ";
	
	public final static String internetCountSql = " SELECT count(t.id) FROM c_platform platform LEFT JOIN c_contract t  LEFT JOIN c_sign_record record ON t.id = record.contract_id ON platform.id = t.platform_id " +
			"  WHERE 1=1  AND record.sign_type != 1 ";
	
	public final static String protectQuerySql = " SELECT id,expire_time,title,organization,serial_num,protect_time,userid,status FROM c_protect_info WHERE 1=1 AND status = 0 ";
	
	public final static String protectCountSql = " SELECT count(id) FROM c_protect_info WHERE 1=1 AND status = 0 ";
	
//	public final static String internetQuerySql = " SELECT t.id column0,t.title column1,t.serial_num column2,t.type column3,t.creator column4," +
//			" t.platform_id column5,t.status column6,t.create_time column7,record.contract_id column8,record.singer_id column9,record.sign_time column10," +
//			" record.sign_status column11,record.sign_mode column12,record.sign_type column13" +
//			" FROM c_contract t LEFT JOIN  c_sign_record record on t.id = record.contract_id  where 1=1 " +
//			" AND record.sign_type != 1 AND t.platform_id = :plateformId ";
	
//	public final static String internetCountSql = " SELECT count(t.id) FROM c_contract t LEFT JOIN c_sign_record record ON t.id = record.contract_id  where 1=1  AND record.sign_type != 1 AND  t.platform_id = :plateformId ";



	/**
    * 把16进制字符串转换成字节数组
    * @param hex
    * @return
    */
	public final static byte[] hexStringToByte(String hex) {
	    int len = (hex.length() / 2);
	    byte[] result = new byte[len];
	    char[] achar = hex.toCharArray();
	    for (int i = 0; i < len; i++) {
	     int pos = i * 2;
	     result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
	    }
	    return result;
	}
	
	private static byte toByte(char c) {
	    byte b = (byte) "0123456789ABCDEF".indexOf(c);
	    return b;
	}
	
	/**
	    * 把字节数组转换成16进制字符串
	    * @param bArray
	    * @return
	    */
	public static final String bytesToHexString(byte[] bArray) {
		ByteToOther bt=new ByteToOther(bArray);
		return bt.toHexString();
	}
	
	/**
	    * 16进制转换为10进制
	    * @param hex
	    * @return
	    */
	public static final String hexToDecString(String hex) {
		hex=replaceBlank(hex);
		BigInteger bg=new BigInteger(hex,16); 
		return bg.toString(10);
	}
	
	/**
	    * 16进制转换为二进制
	    * @param hex
	    * @return
	    */
	public static String hexString2binaryString(String hexString)  
    {  
        if (hexString == null || hexString.length() % 2 != 0)  
            return null;  
        String bString = "", tmp;  
        for (int i = 0; i < hexString.length(); i++)  
        {  
            tmp = "0000"  
                    + Integer.toBinaryString(Integer.parseInt(hexString  
                            .substring(i, i + 1), 16));  
            bString += tmp.substring(tmp.length() - 4);  
        }  
        return bString;  
    }  
	
	/**
	    * 10进制转为16进制
	    * @param dec
	    * @return
	    */
	public static final String decTohexString(String dec) {
		BigInteger bg=new BigInteger(dec,10); 
		return bg.toString(16);
	}
	
	/**
	    * 把字节数组转换为对象
	    * @param bytes
	    * @return
	    * @throws IOException
	    * @throws ClassNotFoundException
	    */
	public static final Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
	    ObjectInputStream oi = new ObjectInputStream(in);
	    Object o = oi.readObject();
	    oi.close();
	    return o;
	}
	
	/**
	    * 把可序列化对象转换成字节数组
	    * @param s
	    * @return
	    * @throws IOException
	    */
	public static final byte[] objectToBytes(Serializable s) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream ot = new ObjectOutputStream(out);
	    ot.writeObject(s);
	    ot.flush();
	    ot.close();
	    return out.toByteArray();
	}
	
	public static final String objectToHexString(Serializable s) throws IOException{
	    return bytesToHexString(objectToBytes(s));
	}
	
	public static final Object hexStringToObject(String hex) throws IOException, ClassNotFoundException{
	    return bytesToObject(hexStringToByte(hex));
	}
	
	/**
	*
	* 转义符
    * 去除字符串中的空格、回车、换行符、制表符
    * 
    */
	public final static String replaceBlank(String s)
	{
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(s);
		return  m.replaceAll("");
	}
	
	/**
	    * 获取字符串中某个值，例如获取CN=test企业证书,GIVENNAME=税务登记证,OID.2.5.4.31=税务管理码,E=liuy@jsca.com.cn，中CN项
	    * @param s 传入的字符串
	    * @param key 截取的值
	    * @param fenge 分割符号，默认为","
	    */
		public final static String getOneKey(String s,String key)
		{
			return getOneKey(s,key,",");
		}
	
	/**
    * 获取字符串中某个值，例如获取CN=test企业证书,GIVENNAME=税务登记证,OID.2.5.4.31=税务管理码,E=liuy@jsca.com.cn，中CN项
    * @param s 传入的字符串
    * @param key 截取的值
    * @param fenge 分割符号，默认为","
    */
	public final static String getOneKey(String s,String key,String fenge)
	{
		s=replaceBlank(s).toLowerCase();
		String[] slist=s.split(fenge);
		for(int i=0;i<slist.length;i++)
		{
			String[] slp=slist[i].split("=");
			if(slp[0].equals(key.toLowerCase()))
			{
				return slp[1];
			}	
		}
		return  null;
	}
	
	/**
	 * 二进制字符串转为：10进制，例如将"0101"转为"5"
	 * @param binary
	 * @return
	 */
	public static int binaryToDecimal(String binary) 
	{
	   int result=0;
	   for (int i =0;i<binary.length(); i++) 
	   {
		   char x=binary.charAt(i);
		   if(x=='1')
		   {
			  result+=Math.pow(2, (binary.length()-1)-i);  
		   }
	   }
	   return result;
	}
	
	/**
	 * 判断字符是否为空，空值返回true，
	 * @param binary
	 * @return
	 */
	public static boolean isNull(String s)
	{
		if(null==s || s.length()==0 || s.equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 计算某个字符出现的次数
	 * @param binary
	 * @return
	 */
	public static int calChar(String s,char c)
	{   
		int y=0;
		for(int i=0;i<s.length();i++)
		{
			char cp=s.charAt(i);
			if(cp==c)
			{
				y++;
			}
		}
		return y;
	}
	public static String nullToString(String str)
	{
		if (null == str || "null".equals(str))
			return "";
		else
			return str;
	}
	
	/**
	* JAVA判断字符串数组中是否包含某字符串元素
	*
	* @param substring 某字符串
	* @param source 源字符串数组
	* @return 包含则返回true，否则返回false
	*/
	public static boolean isContain(String substring, String[] source) {
		if (source == null || source.length == 0) {
			return false;
		}
		for (int i = 0; i < source.length; i++) {
			String aSource = source[i];
			if (aSource.equals(substring)) {
				return true;
			}
		}
		return false;
	}
	
	//判断数组中是否有重复值
	public static boolean checkRepeat(String[] array){
	    Set<String> set = new HashSet<String>();
	    for(String str : array){
	        set.add(str);
	    }
	    if(set.size() != array.length){
	        return true;//有重复
	    }else{
	        return false;//不重复
	    }
		 
	}

}