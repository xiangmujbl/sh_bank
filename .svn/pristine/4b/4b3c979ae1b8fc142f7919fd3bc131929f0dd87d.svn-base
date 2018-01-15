package com.mmec.business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfca.org.bouncycastle.util.encoders.Base64;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.service.ContractService;
import com.mmec.business.service.SignService;
import com.mmec.thrift.ShBankUtil;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.DateUtil;
import com.mmec.util.ErrorData;
import com.mmec.util.FileUtil;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.RandomUtil;
import com.mmec.util.Result;

@Service("contractService")
public class ContractServiceImpl implements ContractService {

	Logger log = Logger.getLogger(ContractServiceImpl.class);
	
	@Autowired
	SignService signService;

	public ReturnData cancelContract(String appId, String userId, String orderId, String requestIp) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("appId", appId);
		map.put("ucid", userId);
		map.put("orderId", orderId);
		map.put("requestIp", requestIp);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).modifyContractStatus(map);
		log.info("cancelContract, call center model success. 中央承载返回：" + returnData);

		return returnData;
	}

	@Override
	public ReturnData findContract(String appId, String userId, String orderId) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("appId", appId);
		map.put("ucid", userId);
		map.put("orderId", orderId);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).queryContract(map);
		log.info("queryContract, call center model success. 中央承载返回：" + returnData);

		return returnData;
	}
	/**
	 * 
	 * @param appId 
	 * @param userId 
	 * @param orderId 
	 * @param isAuthor 是否代签署,Y/N
	 * @param serialNum  可以根据合同编号查询合同，也可以根据appId和orderId查询合同
	 * @param authorUserId	被代签人的userId
	 * @return
	 */
	@Override
	public ReturnData signQueryContract(String appId, String userId, String orderId,String isAuthor,String serialNum,String authorUserId)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("appId", appId);
		map.put("ucid", userId);
		map.put("orderId", orderId);
		map.put("isAuthor", isAuthor);
		map.put("authorUserId", authorUserId);
		map.put("serialNum", serialNum);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).signQueryContract(map);
		log.info("queryContract, call center model success. 中央承载返回：" + returnData);

		return returnData;
	}
	@Override
	public ReturnData createContract(String appId, String customsId, String templateId, String data, String userId,
			String title, String orderId, String offerTime, String requestIp) {

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", "MMEC");// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("customId", customsId);// 必填
		datamap.put("tempNumber", templateId);// 必填
		datamap.put("tempData", data);// 必填
		datamap.put("ucid", userId);// 必填
		datamap.put("title", title);// 必填
		datamap.put("orderId", orderId);// 必填
		datamap.put("offerTime", offerTime);// 必填

		datamap.put("chargeType", "0");
		datamap.put("requestIp", requestIp);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).createContract(datamap);
		log.info("createContract, call center model success. 中央承载返回：" + returnData);

		return returnData;
	}

	@Override
	public ReturnData createContractYUNSIGN(String appId, String customsId, String userId, String title,
			String orderId, String offerTime, String startTime, String endTime, String pname, String price,
			String operator, String contractType, String contractFile, String attachmentFile, String requestIp,
			String signCost) {
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM_YS);// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("customId", customsId);// 必填（接收方，大于2个中间用‘，’隔开）
		datamap.put("ucid", userId);// 必填
		datamap.put("startTime", startTime);// 不必填（合同开始时间）
		datamap.put("endTime", endTime);// 不必填（合同结束时间）
		datamap.put("title", title);// 必填（合同标题）
		datamap.put("pname", pname);// 必填（项目名称）
		datamap.put("orderId", orderId);// 必填（）
		datamap.put("offerTime", offerTime);// 必填（签署截止时间）
		// datamap.put("paymentType", paymentType);//收付类型
		datamap.put("price", price);
		datamap.put("operator", operator);// 经办人
		datamap.put("contractType", contractType);// 合同分类
		datamap.put("contractFile", contractFile);// 必填（合同内容）
		datamap.put("attachmentFile", attachmentFile);// 非必填（合同附件）
		datamap.put("requestIp", requestIp);
		datamap.put("paymentType", signCost);
		log.info("createContractYUNSIGN,  中央承载入参：" + datamap);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).createContract(datamap);
		log.info("createContractYUNSIGN, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}

	@Override
	public ReturnData authorCreate(String appId, String customsId, String authorize, String beAuthorized,
			String orderId, String startTime, String endTime, String contractFile, String requestIp) {
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("customId", customsId);// 必填（接收方，大于2个中间用‘，’隔开）
		datamap.put("authorize", authorize);// 必填
		datamap.put("beAuthorized", beAuthorized);// 必填
		datamap.put("authStartTime", startTime);// 不必填（合同开始时间）
		datamap.put("authEndTime", endTime);// 不必填（合同结束时间）
		datamap.put("orderId", orderId);// 必填（）
		datamap.put("authContractFile", contractFile);// 必填（合同内容）
		datamap.put("requestIp", requestIp);
		datamap.put("author", "author");
		datamap.put("authType", "2");
		datamap.put("chargeType", "0");
		log.info("authorCreate,  中央承载入参：" + datamap);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).authorCreate(datamap);
		log.info("authorCreate, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}
	
	@Override
	public ReturnData authorCreate2(String appId, String customsId, String authorize, String beAuthorized,String title,
			String orderId, String startTime, String endTime, String contractFile,String attachmentFile, String requestIp) {
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("customId", customsId);// 必填（接收方，大于2个中间用‘，’隔开）
		datamap.put("authorize", authorize);// 必填
		datamap.put("beAuthorized", beAuthorized);// 必填
		datamap.put("authStartTime", startTime);// 不必填（合同开始时间）
		datamap.put("authEndTime", endTime);// 不必填（合同结束时间）
		datamap.put("orderId", orderId);// 必填（）
		datamap.put("authContractFile", contractFile);// 必填（合同内容）
		
		datamap.put("attachmentFile", attachmentFile);//（不必填）合同附件
		datamap.put("title", title);//合同标题
		
		datamap.put("requestIp", requestIp);
		datamap.put("author", "author");
		datamap.put("authType", "2");
		datamap.put("chargeType", "0");
		log.info("authorCreate,  中央承载入参：" + datamap);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).authorCreate(datamap);
		log.info("authorCreate, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}

	// 本地部署版
	public ReturnData createContractYunsignLocal(String appId, String customsId, String userId, String title,
			String orderId, String offerTime, String startTime, String endTime, String pname, String price,
			String operator, String contractType, String contractFile, String attachmentFile, String requestIp,
			String chargeType) {
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM_YS);// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("customId", customsId);// 必填（接收方，大于2个中间用‘，’隔开）
		datamap.put("ucid", userId);// 必填
		datamap.put("startTime", startTime);// 不必填（合同开始时间）
		datamap.put("endTime", endTime);// 不必填（合同结束时间）
		datamap.put("title", title);// 必填（合同标题）
		datamap.put("pname", pname);// 必填（项目名称）
		datamap.put("orderId", orderId);// 必填（）
		datamap.put("offerTime", offerTime);// 必填（签署截止时间）
		// datamap.put("paymentType", paymentType);//收付类型
		datamap.put("price", price);
		datamap.put("operator", operator);// 经办人
		datamap.put("contractType", contractType);// 合同分类
		datamap.put("contractFile", contractFile);// 必填（合同内容）
		datamap.put("attachmentFile", attachmentFile);// 非必填（合同附件）
		datamap.put("requestIp", requestIp);
		datamap.put("chargeType", chargeType);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).createContract(datamap);
		log.info("createContractYunsignLocal, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}

	@Override
	public ReturnData createContractFinance(String appId, String customsId, String templateId, String data,
			String userId, String title, String orderId, String offerTime, String attachmentFile, String requestIp) {

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", "MMEC");// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("customId", customsId);// 必填
		datamap.put("tempNumber", templateId);// 必填
		datamap.put("tempData", data);// 必填
		datamap.put("ucid", userId);// 必填
		datamap.put("title", title);// 必填
		datamap.put("orderId", orderId);// 必填
		datamap.put("offerTime", offerTime);// 必填
		datamap.put("chargeType", "0");
		datamap.put("attachmentFile", attachmentFile);// 非必填（合同附件）
		datamap.put("requestIp", requestIp);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).createContractFinance(datamap);
		log.info("createContractFinance, call center model success. 中央承载返回：" + returnData);

		return returnData;
	}

	@Override
	public ReturnData downloadContract(String appId, String userId, String orderId, String requestIp) {

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("appId", appId);
		datamap.put("orderId", orderId);
		datamap.put("ucid", userId);
		datamap.put("requestIp", requestIp);
		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_CONTRACT)).downloadContract(datamap);
		log.info("downloadContract, call center model success, 中央承载返回：" + returnData);

		return returnData;
	}

	@Override
	public ReturnData getCompanyByEmail(String optFrom, String appId, String type, String email) {
		Map<String, String> datamap = new HashMap<String, String>();

		datamap.put("optFrom", optFrom);
		datamap.put("appId", appId);
		datamap.put("type", type);
		datamap.put("email", email);
		SendDataUtil senddata = new SendDataUtil(ConstantParam.INTF_NAME_USER);
		ReturnData returnData = senddata.getCompanyByEmai(datamap);
		log.info("getCompanyByEmail, call center model success, 中央承载返回：" + returnData);

		return returnData;
	}

	@Override
	public ReturnData getCustomByMobile(String optFrom, String appId, String type, String mobile) {

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", optFrom);
		datamap.put("appId", appId);
		datamap.put("type", type);
		datamap.put("mobile", mobile);

		SendDataUtil senddata = new SendDataUtil(ConstantParam.INTF_NAME_USER);
		ReturnData returnData = senddata.getCustomByMobile(datamap);
		log.info("getCustomByMobile, call center model success, 中央承载返回：" + returnData);

		return returnData;
	}

	@Override
	public ReturnData userRegister(String optFrom, String appId, String userName, String invitorId,
			String identityCard, String mobile, String platformUserName, String type, String account, String password,
			String requestIp) {
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM_YS);// 必填
		datamap.put("appId", appId);// 必填（平台编码）

		datamap.put("invitorId", invitorId);// 必填（邀约人）

		datamap.put("mobile", mobile);// 必填（平台编码）
		datamap.put("platformUserName", platformUserName);// 必填（平台编码）
		datamap.put("type", type);// 必填（平台编码）
		datamap.put("account", account);// 必填；个人：手机_p；企业：邮箱_e
		datamap.put("password", password);// 必填
		if (!"".equals(userName) && null != userName) {
			datamap.put("userName", userName);// 必填（姓名）
		}
		if (!"".equals(identityCard) && null != identityCard) {
			datamap.put("identityCard", identityCard);// 选填（身份证号码）
		}
		datamap.put("requestIp", requestIp);
		SendDataUtil senddata = new SendDataUtil(ConstantParam.INTF_NAME_USER);
		ReturnData returnData = senddata.userRegister(datamap);
		return returnData;
	}

	@Override
	public List imgPath(String path) {
		List<String> rstPath = FileUtil.getFileName(path);
		List<String> imgPath = new ArrayList<String>();
		List<String> imgPath_ = new ArrayList<String>();
		if (rstPath != null && rstPath.size() > 0) {
			for (int i = 0; i < rstPath.size(); i++) {
				log.info(rstPath.get(i));
				imgPath.add(rstPath.get(i).substring(0, rstPath.get(i).lastIndexOf(".")));
			}
		}
		Collections.sort(imgPath, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return new Double((String) o1).compareTo(new Double((String) o2));
			}
		});
		for (int i = 0; i < imgPath.size(); i++) {
			imgPath_.add(imgPath.get(i) + ".jpg");
		}
		return imgPath_;

	}

	@Override
	public ReturnData xtFindContract(String appId, String userId, String orderId) {
		ReturnData rs = null;
		log.info("internetFinanceQueryContract,call center model success,params appId:" + appId + ",userId:" + userId
				+ ",orderId:" + orderId);
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("appId", appId);
		dataMap.put("ucid", userId);
		dataMap.put("orderId", orderId);
		SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_CONTRACT);
		rs = sdu.getXtContract(dataMap);
		log.info("xtQueryContract, call center model success, 中央承载返回：" + rs);
		return rs;
	}

	@Override
	public ReturnData createContractWithFile(String appId, String customsId, String userId, String title,
			String orderId, String offerTime, String templateId, String data, String contractFile,
			String attachmentFile, String requestIp) {
		// attachmentFile =
		// "[{\"attName\":\"222222\",\"attPath\":\"E:/office/222222.pdf\",\"attOriginalName\":\"222222.pdf\"},{\"attName\":\"014E24A77643\",\"attPath\":\"E:/office/143718886090505.html\",\"attOriginalName\":\"014E24A77643.html\"}]";
		// contractFile =
		// "{\"fileName\":\"MVC_sign\",\"filePath\":\"E:/office/143718886090505.html\",\"fileOriginalName\":\"1.html\"}";

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("customId", customsId);// 必填（接收方，大于2个中间用‘，’隔开）
		datamap.put("ucid", userId);// 必填
		datamap.put("title", title);// 必填（合同标题）
		datamap.put("orderId", orderId);// 必填（）
		datamap.put("offerTime", offerTime);// 必填（签署截止时间）
		datamap.put("chargeType", "0");// 收付类型
		datamap.put("tempNumber", templateId);// 模板编号
		datamap.put("tempData", data);// 合同模板数据
		datamap.put("contractFile", contractFile);// 必填（合同内容）
		datamap.put("attachmentFile", attachmentFile);// 非必填（合同附件）
		datamap.put("requestIp", requestIp);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).createContract(datamap);
		log.info("createContractWithFile, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}

	@Override
	public ReturnData downloadPdfContract(String appId, String userId, String orderId, String requestIp) {
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("appId", appId);
		datamap.put("orderId", orderId);
		datamap.put("ucid", userId);
		datamap.put("requestIp", requestIp);
		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_CONTRACT)).downLoadPdfContract(datamap);
		log.info("downloadContract, call center model success, 中央承载返回：" + returnData);

		return returnData;
	}

	@Override
	public ReturnData getContractList(String appId, String userId, String currPage, String status, String title,
			String startTime, String endTime, String isDelete,String orderIds) {
		Map<String, String> map = new HashMap<String, String>();
		// map.put("userId", userId);mcPkr944um MvKa9h96sI9kU4UOTVK7 300224
		map.put("userId", userId);
		map.put("appId", appId);
		map.put("countsPerPage", "10");
		map.put("currPage", "" + currPage);
		map.put("orderIds", orderIds);
		if (!"".equals(title)) {
			map.put("title", title);
		}
		if (!"".equals(startTime)) {
			map.put("startTime", startTime + " 00:00:00");
		}
		if (!"".equals(endTime)) {
			map.put("endTime", endTime + " 23:59:59");
		}
		if (!"".equals(isDelete)) {
			map.put("isDelete", isDelete);
		}
		if (!"".equals(status)) {
			map.put("status", status);
		}
		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_CONTRACT)).getContractList(map);
		log.info("downloadContract, call center model success, 中央承载返回：" + returnData);

		return returnData;
	}
	
	
	@Override
	public ReturnData queryContractTemplate(String appId, String platformUserName, String templateNum) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("appId", appId);
		map.put("platformUserName", platformUserName);
		map.put("templateNum", templateNum);
		
		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_TEMPLATE)).queryTemplateContract(map);
		log.info("downloadContract, call center model success, 中央承载返回：" + returnData);

		return returnData;
	}
	
	
	/**
	 * 对上传的文件进行处理 校验文件格式、判断文件的大小，图片文件不超过5M，word和pdf不超过10M
	 * 讲文件上传到服务器的共享目录下面，将文件的路径和文件名称记录下来传到中央承载保存
	 * 
	 * @param filePath
	 *            上传的文件路径
	 * @param file
	 *            上传的文件
	 * @param fileType
	 *            上传的文件类型（1：合同文件，2附件）
	 * @return map 合同路径及名称
	 */
	public Map<String, String> newOperationFile(String filePath,String docname, String fileBase64, String fileType) {
		
		Map<String, String> contractMap = new HashMap<String,String>();
		
		int n = docname.lastIndexOf(".");
		String hzm = docname.substring(n, docname.length());
		
		String fileNameNosuffix = "";
		String fileName = "";
		if (".doc".equals(hzm) || ".DOC".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".doc";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 4);
		} else if (".pdf".equals(hzm) || ".PDF".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".pdf";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 4);
		} else if (".docx".equals(hzm) || ".DOCX".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".docx";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 5);
		} else if (".png".equals(hzm) || ".PNG".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".png";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 4);
		} else if (".jpg".equals(hzm) || ".JPG".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpg";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 4);
		} else if (".jpeg".equals(hzm) || ".JPEG".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpeg";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 5);
		}else if (".html".equals(hzm) || ".HTML".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".html";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 5);
		}  else {
			contractMap.put("error", "上传文件格式只能为图片（png、jpg、jpeg）doc、docx、html或者pdf");
		}
		log.info("上传文件开始......");
		// ============================================合同上传================
		String hturl = filePath + fileName;
		File ht = new File(hturl);
		if (!docname.isEmpty() && !"".equals(docname) && null!=docname) {
			try {
				FileOutputStream os = new FileOutputStream(ht);
				
				byte[] contractBase64=Base64.decode(fileBase64);
				
				ByteArrayInputStream in = new ByteArrayInputStream(contractBase64);
				int b = 0;
				while ((b = in.read()) != -1) {
					os.write(b);
				}
				os.flush();
				os.close();
				in.close();
				if ("2".equals(fileType)) {
					contractMap.put("attName", fileNameNosuffix);
					contractMap.put("attPath", hturl);
					contractMap.put("attOriginalName", docname);
				} else {
					contractMap.put("fileName", fileNameNosuffix);
					contractMap.put("filePath", hturl);
					contractMap.put("attOriginalName", docname);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return contractMap;
	}
	
}
