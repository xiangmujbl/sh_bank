package com.mmec.business.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mmec.business.SendDataUtil;
import com.mmec.business.service.SealService;
import com.mmec.thrift.service.ReturnData;

@Service("SealService")
public class SealServiceImpl implements SealService {
	Logger log = Logger.getLogger(ContractServiceImpl.class);

	@Override
	public ReturnData querySeal(String optFrom, String appId, String userId) {

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", optFrom);// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("platformUserName", userId);// 必填

		ReturnData returnData = (new SendDataUtil("UserRMIServices")).querySeal(datamap);
		log.info("querySeal(查询图章), call center model success. 中央承载返回：" + returnData);

		return returnData;
	}

	@Override
	public ReturnData delSeal(String optFrom, String appId, String userId, String sealId, String requestIp) {
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", optFrom);// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("sealId", sealId);// 必填
		datamap.put("platformUserName", userId);// 必填
		datamap.put("requestIp", requestIp);
		ReturnData returnData = (new SendDataUtil("UserRMIServices")).delSeal(datamap);
		log.info("delSeal(删除图章), call center model success. 中央承载返回：" + returnData);
		return returnData;
	}

	@Override
	public ReturnData addSeal(String optFrom, String appId, String userId, String originalPath, String bgRemovedPath,
			String sealName, String sealType, String sealPath, String cutPath, String requestIp) {
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", optFrom);// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("platformUserName", userId);// 必填
		datamap.put("originalPath", originalPath);// 必填
		datamap.put("bgRemovedPath", bgRemovedPath);// 必填
		datamap.put("sealName", sealName);// 必填
		datamap.put("sealType", sealType);// 必填
		datamap.put("sealPath", sealPath);// 必填
		datamap.put("cutPath", cutPath);
		datamap.put("requestIp", requestIp);
		ReturnData returnData = (new SendDataUtil("UserRMIServices")).addSeal(datamap);
		log.info("addSeal(添加图章), call center model success. 中央承载返回：" + returnData);

		return returnData;
	}
}
