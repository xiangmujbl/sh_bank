package com.mmec.business.service;

import java.util.List;
import java.util.Map;

import com.mmec.thrift.service.ReturnData;
import com.mmec.util.Result;

public interface ContractService {

	public ReturnData createContract(String appId, String customsId, String templateId, String data, String userId,
			String title, String orderId, String offerTime, String requestIp);

	public ReturnData createContractFinance(String appId, String customsId, String templateId, String data,
			String userId, String title, String orderId, String offerTime, String attachmentFile, String requestIp);

	public ReturnData createContractYUNSIGN(String appId, String customsId, String userId, String title,
			String orderId, String offerTime, String startTime, String endTime, String pname, String price,
			String operator, String contractType, String contractFile, String attachmentFile, String requestIp,
			String signCost);

	public ReturnData authorCreate(String appId, String customsId, String authorize, String beAuthorized,
			String orderId, String startTime, String endTime, String contractFile, String requestIp);
	
	//互生金融定制要求，因为上面的改动地方太多，所以重新写一个
	public ReturnData authorCreate2(String appId, String customsId, String authorize, String beAuthorized,String title,
			String orderId, String startTime, String endTime, String contractFile, String attachment, String requestIp);

	// 本地部署版
	public ReturnData createContractYunsignLocal(String appId, String customsId, String userId, String title,
			String orderId, String offerTime, String startTime, String endTime, String pname, String price,
			String operator, String contractType, String contractFile, String attachmentFile, String requestIp,
			String chargeType);

	public ReturnData findContract(String appId, String userId, String orderId);
	
	public ReturnData signQueryContract(String appId, String userId, String orderId,String isAuthor,String serialNum,String authorUserId);

	public ReturnData cancelContract(String appId, String userId, String orderId, String requestIp);

	public ReturnData downloadContract(String appId, String userId, String orderId, String requestIp);

	public ReturnData getCompanyByEmail(String optFrom, String appId, String type, String email);

	public ReturnData getCustomByMobile(String optFrom, String appId, String type, String mobile);

	public ReturnData userRegister(String optFrom, String appId, String userName, String invitorId,
			String identityCard, String mobile, String platformUserName, String type, String account, String password,
			String requestIp);

	public List imgPath(String path);

	public ReturnData xtFindContract(String appId, String userId, String orderId);

	public ReturnData createContractWithFile(String appId, String customsId, String userId, String title,
			String orderId, String offerTime, String templateId, String data, String contractFile,
			String attachmentFile, String requestIp);

	public ReturnData downloadPdfContract(String appId, String userId, String orderId, String requestIp);

	public ReturnData getContractList(String appId, String userId, String currPage, String status, String title,
			String startTime, String endTime, String isDelete,String orderIds);
	
	public ReturnData queryContractTemplate(String appId, String platformUserName, String templateNum);
	
	public Map<String, String> newOperationFile(String filePath,String docname, String fileBase64, String fileType);
}