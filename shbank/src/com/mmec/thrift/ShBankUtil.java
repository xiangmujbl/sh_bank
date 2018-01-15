package com.mmec.thrift;

import java.util.Date;

import com.mmec.util.CallWebServiceUtil;
import com.mmec.util.ConstantParam;
import com.mmec.util.DateUtil;

public class ShBankUtil {
	 
    /**
     * 上海银行硬签存云库
     * @param 
     * @return
     */
    public static String externalDataImport(String signInformation, String signData,String serialNum, String title,Date createTime,String signPlaintext
    		,String lastSha1,String orderid,String appId,String signName) {
        String result = "";
        String[] paramName = new String[]{"signInformation","signTime","signData","serialNum","title",
        		"createTime","signPlaintext","contractSha1","orderid","source","signName"};
        String[] paramValue = new String[]{signInformation,DateUtil.toDateYYYYMMDDHHMM2(new Date()),signData,
        		serialNum,title,DateUtil.toDateYYYYMMDDHHMM2(createTime),
        		signPlaintext,lastSha1,orderid,appId,signName};
		//String url = "https://www.yunsign.com/jxbank_cs/webservice/Internal?wsdl";
		String url = "https://test.yunsigntest.com/mmecserver3.0/webservice/Internal?wsdl";
        //String url = ConstantParam.EXTERNALDATAIMPORT;
        try {
        
            System.out.println("url : " + url);
            result = CallWebServiceUtil.CallHttpsService(url, "http://wsdl.com/", "externalDataImport", paramName, paramValue);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String[] args) {
    
    	externalDataImport("1","1","1", "上海银行", new Date(),"1", "aaa", "111", "8KD7Ssuzb2","shbank");
	}
}
