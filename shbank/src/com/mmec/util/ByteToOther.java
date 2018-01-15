package com.mmec.util;

/**
 * byte字节转换为其他编码方式.
 * <pre>
 * 示例：
 * byte[] b=...
 * ByteToOther bt=new ByteToOther(b);
 * String hex=bt.toHexString();
 * </pre>
 * @author liuy
 * @version 2011-07-22
 * 
 */
public class ByteToOther {
	
	
	private byte[] bArray=null;
	private ByteToOther(){}
	
	public ByteToOther(byte[] bArray)
	{
		this.bArray=bArray;
	}
	
	/**
	 * 保持不变
	 * @return
	 */
	public  byte[] toByteL() {
		return bArray;  
	}
	
	/**
	 * 准换成16进制
	 * @return
	 */
	public  String toHexString() {
		getHex(bArray,"");
	    return getHex(bArray,"");
	}
	
	/**
	 * 转换成base64
	 * @return
	 */
	public  String toB64String() {
	    return Base64.encode(bArray);
	}
	
	/**
	 * 转换成二进制打印
	 * @return
	 */
	public  String toX2String() {
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<bArray.length;i++)
		{
			String it=Integer.toBinaryString(bArray[i]);
			sb.append(it+" ");
		}
		return sb.toString();
	}
	
	 /** 
     * 将将证书摘要转换为16进制字符串，即得到证书微缩图。 
     *  
     * @param buf 
     * @param delimiter 
     * @return 
     */  
    private static String getHex(byte buf[], String delimiter) {  
        String result = "";  
        if (buf == null) {  
            return "";  
        }  
  
        String defaultDelimiter = "";  
        if (delimiter != null && delimiter.length() > 0) {  
            defaultDelimiter = delimiter;  
        }  
  
        for (int i = 0; i < buf.length; i++) {  
            if (i > 0) {  
                result += defaultDelimiter;  
            }  
  
            short sValue = buf[i];  
            int iValue = 0;  
            iValue += sValue;  
            String converted = Integer.toHexString(iValue);  
  
            if (converted.length() > 2) {  
                converted = converted.substring(converted.length() - 2);  
            }  
            // 只有1位数时，前面补0。  
            else if (converted.length() < 2) {  
                converted = ("0" + converted);  
            }  
            // 将微缩图都转换为大写字母。  
            result += converted.toUpperCase();  
        }  
        return result;  
    }  
}

