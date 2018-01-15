package com.mmec.business.service;

import com.mmec.thrift.service.ReturnData;

public interface SealService {

	public ReturnData querySeal(String optFrom, String appId, String userId);

	public ReturnData delSeal(String optFrom, String appId, String userId, String sealId, String requestIp);

	public ReturnData addSeal(String optFrom, String appId, String userId, String originalPath, String bgRemovedPath,
			String sealName, String sealType, String sealPath, String cutPath, String requestIp);

}
