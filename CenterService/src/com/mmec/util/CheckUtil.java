package com.mmec.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;
import org.liuy.pdf.IConf;

import com.google.gson.Gson;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
/**
 * 入参校验类
 * @author 胡姚超
 *
 */
public class CheckUtil{
	
	public static Logger log = Logger.getLogger(CheckUtil.class);
	/**
	 * 对外参数名称及非空校验
	 * @param optType 
	 * @param datamap
	 * @param realParams datamap中的key组成的list
	 * @return
	 */
	public static ReturnData CheckParams(String optType,Map<String,String> datamap){
		ReturnData d=new ReturnData();
		if(null==optType){
			d=new ReturnData("9900","optType为null","optType is null","");
			return d;
		}
//		if(optType!=ConstantUtil.ADD&&optType!=ConstantUtil.QUERY&&optType!=ConstantUtil.DELETE&&optType!=ConstantUtil.UPDATE){
//			d=new ReturnData("9900","optType参数值填写错误","optType value error","");
//		}
		if(null==datamap){
			d=new ReturnData("9900","datamap为null","datamap is null","");
			return d;
		}
		d=new ReturnData("0000","成功","success","");
		return d;
	}
	
	/**
	 * Map的Key和Value非空校验/校验参数list
	 * @param datamap
	 * @param realParams
	 * @return
	 */
	public static ReturnData CheckMapParamsList(Map<String,String> datamap,List<String>keyParams,List<String>valueParams){
		ReturnData d=new ReturnData();
		if(null!=keyParams){
		for(int i=0;i<keyParams.size();i++){
			if(!datamap.containsKey(keyParams.get(i))){
				d=new ReturnData("9900","参数:"+keyParams.get(i)+"为null","param:"+keyParams.get(i)+"is null","");
				return d;
			}
			if(null==datamap.get(keyParams.get(i))){
				d=new ReturnData("9900","参数:"+keyParams.get(i)+"值为null",""+keyParams.get(i)+"value is null","");
				return d;
			}
			if("".equals(datamap.get(valueParams.get(i)))){
				d=new ReturnData("9900","参数:"+keyParams.get(i)+"值为空",""+keyParams.get(i)+"value is blank","");
				return d;
			}
		}
		}
		d=new ReturnData("0000","成功","success","");
		return d;
	}
	
	/**
	 * Map的Key和Value非空校验/校验参数String
	 * @param datamap
	 * @param realParams
	 * @return
	 */public static ReturnData CheckMapParams(Map<String,String> datamap,String keyParam,String valueParam){
		 	List<String> keyParams=new ArrayList<String>();
		 	List<String> valueParams=new ArrayList<String>();
		 	if(!"".equals(keyParam)){
		 		String[]keyArry=keyParam.split(",");
		 		for(int i=0;i<keyArry.length;i++){
		 			keyParams.add(keyArry[i]);
		 		}
		 	}
		 	if(!"".equals(valueParam)){
		 		String[]valueArry=valueParam.split(",");
		 		for(int i=0;i<valueArry.length;i++){
		 			keyParams.add(valueArry[i]);
		 		}
		 	}
			ReturnData d=new ReturnData();
			if(keyParams.size()>0){
			for(int i=0;i<keyParams.size();i++){
				if(!datamap.containsKey(keyParams.get(i))){
					d=new ReturnData("9900","参数:"+keyParams.get(i)+"为null","param:"+keyParams.get(i)+"is null","");
					return d;
				}
				if(null==datamap.get(keyParams.get(i))){
					d=new ReturnData("9900","参数:"+keyParams.get(i)+"值为null",""+keyParams.get(i)+"value is null","");
					return d;
				}
				if(valueParams.size()>0){
				if("".equals(datamap.get(valueParams.get(i)))){
					d=new ReturnData("9900","参数:"+keyParams.get(i)+"值为空",""+keyParams.get(i)+"value is blank","");
					return d;
				}
				}
			}
			}
			d=new ReturnData("0000","成功","success","");
			return d;
		}
	
	
	public 	static  String  moneyCheck(String money){
		ReturnData d=new ReturnData();
		BigDecimal bm=null;
		try{
		bm=new BigDecimal(money);
		}catch(NumberFormatException e){
			return "非法的金额格式:"+money;
		}
		if(money.startsWith("-")){
			return "不接受金额为负数:"+money;
		}
		if(bm.compareTo(new BigDecimal(ConstantUtil.MAX_MONEY))==1){
			return "金额数值过大:"+money;
		}
		return "success";
	}
	
	public static String moneyaddCheck(BigDecimal bm,BigDecimal b){
		BigDecimal bres=bm.add(b);
		if(bm.compareTo(ConstantUtil.MAX_MONEY_DECIMAL)==1){
			return "金额过大";
		}
		return "success";
	}
	
	public static ReturnData moneyreduceCheck(BigDecimal bm,BigDecimal b){
		BigDecimal bres=bm.subtract(b);
		return new ReturnData("0000","成功","success","");
	}
	
	public static String intCheck(String s){
		try{
			int a=Integer.valueOf(s);
		}catch(Exception e){
			return "系统错误:参数"+s+"转换格式为Int失败";
		}
		return "success";
	}
	
	public static String naturalnumberCheck(String s){
		try{
			if(s.startsWith("-")){
				return "参数"+s+"不能为负数";
			}
			int a=Integer.valueOf(s);
		}catch(Exception e){
			return "系统错误:参数"+s+"转换格式为Int失败";
		}
		return "success";
	};
	
	public static ReturnData checkMap(Map<String,String> map,String keyParam,String valueParam){
		if(null==map){
			return new ReturnData("9999","map参数为空","","");
		}
		List<String> keyParams=new ArrayList<String>();
	 	List<String> valueParams=new ArrayList<String>();
	 	if(!"".equals(keyParam)){
	 		String[]keyArry=keyParam.split(",");
	 		for(int i=0;i<keyArry.length;i++){
	 			keyParams.add(keyArry[i]);
	 		}
	 	}
	 	if(!"".equals(valueParam)){
	 		String[]valueArry=valueParam.split(",");
	 		for(int i=0;i<valueArry.length;i++){
	 			valueParams.add(valueArry[i]);
	 		}
	 	}
		ReturnData d=new ReturnData();
		if(keyParams.size()>0){
		for(int i=0;i<keyParams.size();i++){
			if(!map.containsKey(keyParams.get(i))){
				d=new ReturnData("9900","参数"+keyParams.get(i)+"为null","param:"+keyParams.get(i)+"is null","");
				return d;
			}
			if(null==map.get(keyParams.get(i))){
				d=new ReturnData("9900","参数"+keyParams.get(i)+"值为null",""+keyParams.get(i)+"value is null","");
				return d;
			}
			if(valueParams.size()>0){
			if("".equals(map.get(valueParams.get(i)))){
				d=new ReturnData("9900","参数"+keyParams.get(i)+"值为空",""+keyParams.get(i)+"value is blank","");
				return d;
			}
			}
		}
		}
		d=new ReturnData("0000","成功","success","");
		return d;
	}
	
	/**
	 * 远程连接扣费
	 */
	public  ReturnData remoteWSPay(Map<String,String> map) throws ServiceException{
		String endpoint=IConf.getValue("remoteWS");
//		String endpoint="http://127.0.0.1:8080/mmecserver3.0/webservice/LocalYunsign?wsdl";
		Gson g=new Gson();
		if(null!=endpoint&&!"".equals(endpoint)){
			try{
			String appid=map.get("appid");
			int times=Integer.valueOf(map.get("times"));
			String paycode=map.get("paycode");
			int paytype=Integer.valueOf(map.get("paytype"));
			Service service = new Service();
			Call call = (Call) service.createCall();  
		    call.setTargetEndpointAddress(new java.net.URL(endpoint));
		    call.addParameter("appId", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("times", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("paycode", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("paytype", XMLType.XSD_STRING, ParameterMode.IN);
		    call.setOperationName(new QName("http://wsdl.com/", "localPay"));
		    call.setReturnType(XMLType.XSD_STRING);
		    String result = call.invoke(new Object[]{appid,String.valueOf(times),paycode,
		    		String.valueOf(paytype)}).toString();
			return g.fromJson(result, ReturnData.class);
			}catch(Exception e){
				e.printStackTrace();
				throw  new ServiceException(ConstantUtil.LOCAL_YUNSIGN_PAY_FAILED[0],
						ConstantUtil.LOCAL_YUNSIGN_PAY_FAILED[1],ConstantUtil.LOCAL_YUNSIGN_PAY_FAILED[2]);
			}
		}else{
			log.info("remoteWS in mmec.properties is null");
			throw  new ServiceException(ConstantUtil.LOCAL_YUNSIGN_PAY_FAILED[0],
					ConstantUtil.LOCAL_YUNSIGN_PAY_FAILED[1],ConstantUtil.LOCAL_YUNSIGN_PAY_FAILED[2]);
		}
	}
	
	public static void main(String args[]) throws ServiceException{
		Gson g=new Gson();
		Map<String,String> map=new HashMap<String,String>();
		map.put("appid", "Udz2ILyzx7");
		map.put("times", "1");
		map.put("paycode","contract");
		map.put("paytype","1");
		log.info(g.toJson(new CheckUtil().remoteWSPay(map)));
//		Gson g=new Gson();
//		String endpoint="http://127.0.0.1:8080/mmecserver3.0/webservice/LocalYunsign?wsdl";
//		if(null!=endpoint&&!"".equals(endpoint)){
//			try{
//			String appid="Udz2ILyzx7";
//			int times=1;
//			String paycode="contract";
//			int paytype=1;
//			Service service = new Service();
//			Call call = (Call) service.createCall();  
//		    call.setTargetEndpointAddress(new java.net.URL(endpoint));
//		    call.addParameter("appId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("times", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("paycode", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("paytype", XMLType.XSD_STRING, ParameterMode.IN);
//		    call.setOperationName(new QName("http://wsdl.com/", "localPay"));
//		    call.setReturnType(XMLType.XSD_STRING);
//		    String result = call.invoke(new Object[]{appid,String.valueOf(times),paycode,
//		    		String.valueOf(paytype)}).toString();
//			System.out.println(result);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//
//		}
	}
}