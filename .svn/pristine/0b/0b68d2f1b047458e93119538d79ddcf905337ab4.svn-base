package com.mmec.util;

import com.mmec.css.conf.IConf;
import com.mmec.util.CallWebServiceUtil;

public class CertSignUtil {
	 
    /**
     * 远程扣费
     * @param appId 我们会给每个平台分配一个,固定值
     * @param times	计费次数，本系统是按合同份数计费,所以都传 1
     * @param paycode 固定传 "contract"
     * @param paytype 计算方式 1次, 2份
     * @return
     */
    public static String remoteCharge(String appId, String times,String paycode, String paytype) {
        String result = "";
        String[] paramName = new String[]{"appId","times","paycode","paytype"};
        String[] paramValue = new String[]{appId, times,paycode,paytype};
        try {
            String url = IConf.getValue("LOCALPAY");
            System.out.println("url : " + url);
            result = CallWebServiceUtil.CallHttpsService(url, "http://wsdl.com/", "localPay", paramName, paramValue);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String[] args) {
    	/* String url = PropertiesUtil.getProperties().readValue("certSignUrl");
         System.out.println("url : " + url);*/
    	remoteCharge("8KD7Ssuzb2", "1",  "contract", "1");
	}
}
