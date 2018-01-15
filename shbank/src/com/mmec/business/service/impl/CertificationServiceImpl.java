package com.mmec.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mmec.business.bean.HttpParam;
import com.mmec.business.controller.CertificationController;
import com.mmec.business.service.CertificationService;
import com.mmec.util.ErrorData;
import com.mmec.util.HttpSend;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
@Service("certificationService")
public class CertificationServiceImpl implements CertificationService {

	Logger log = Logger.getLogger(CertificationServiceImpl.class);
	public Result companyValidate( String keyword, String key) {
		List<HttpParam> list=new ArrayList<HttpParam>();
		Map<String,String> info=null;
		try{
		info=JSON.parseObject(keyword,Map.class);
		}catch(Exception e){
			e.printStackTrace();
			return new Result("101",
					PropertiesUtil.getProperties().readValue("PARAMETER_ERROR"), "");
		}
		String company_name=info.get("company_name");
		String business_license_no=info.get("business_license_no");
		if(company_name==null || company_name.trim().isEmpty()){
			return new Result("101",
					PropertiesUtil.getProperties().readValue("COMPANYNAME_EMPTY"), "");
		}
		if(business_license_no==null || business_license_no.trim().isEmpty()){
			return new Result("101",
					PropertiesUtil.getProperties().readValue("BUSINESSNO_EMPTY"), "");
		}
		String regex="^[0-9a-zA-Z]{18}$";
		if(!business_license_no.matches(regex) ){
			return new Result("203",
					PropertiesUtil.getProperties().readValue("NO_ERROR"), "");
		}
		HttpParam httpkeyword=new HttpParam("keyword",business_license_no);
		HttpParam httpkey=new HttpParam("key",key);
		list.add(httpkeyword);
		list.add(httpkey);
		String url="http://i.yjapi.com/ECISimple/Search";
		String result="";
		try{
		result = HttpSend.sendGet(url, list);
		}catch(Exception e){
			return new Result("104",
					PropertiesUtil.getProperties().readValue("ABNORMAL_SERVER"), "");
		}
		Map<String, Object> info2 = null;
		info2 = JSON.parseObject(result, Map.class);
		log.info("---------zzh---------:"+info2);
		String Status=info2.get("Status").toString();
		/*if("101".equals(Status)){
			return new Result("101",
					PropertiesUtil.getProperties().readValue("PARAMETER_ABNORMAL"), "");
		}*/
		if("201".equals(Status)){
			return new Result("201",
					PropertiesUtil.getProperties().readValue("COMPANYNAME_IS_NOT_EXIST"), "");
		}
		if(!"200".equals(Status)){
			return new Result("101",
					PropertiesUtil.getProperties().readValue("PARAMETER_ERROR"), "");
		}
//		List list0 = (List) info2.get("result");
//		((List) info2.get("result")).get(0);
		String result2=((List)(info2.get("Result"))).get(0).toString();
		Map<String,String> map=null;
		map=JSON.parseObject(result2,Map.class);
		String name=map.get("Name");
		String status=map.get("Status");
		if(!name.equals(company_name)){
			return new Result("202",
					PropertiesUtil.getProperties().readValue("COMPANYNAME_BUSINESSID_NOT_MATCH"), "");
		}
		if(!status.contains("存续") && !status.contains("在业") && !status.contains("迁入") && !status.contains("开业")
				&& !status.contains("正常") &&  !status.contains("登记") &&  !status.contains("在营")
				&&  !status.contains("登记成立") &&  !status.contains("个体转企业") &&  !status.contains("迁出")
				&&  !status.contains("迁移异地")  &&  !status.contains("迁往市外") &&  !status.contains("已迁出企业")){
			return new Result("203",
					PropertiesUtil.getProperties().readValue("COMPANYNAME_REGISTRATION_ABNORMAL"), "该公司状态为："+status);
		}
		
		return new Result("200", PropertiesUtil.getProperties().readValue("VAILD_SUCCESS"), "");
	}

}
