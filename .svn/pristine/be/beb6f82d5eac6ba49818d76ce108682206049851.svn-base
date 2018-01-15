package com.mmec.business.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.bean.AuthorityBean;
import com.mmec.business.bean.PlatformBean;
import com.mmec.business.bean.PlatformCallbackBean;
import com.mmec.business.bean.SyncTaskBean;
import com.mmec.business.dao.SyncTaskRepository;
import com.mmec.business.service.BaseService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.DateUtil;
import com.mmec.util.ErrorData;
import com.mmec.util.MD5Util;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

@Service("baseService")
public class BaseServiceImpl implements BaseService {

	private Logger log = Logger.getLogger(BaseServiceImpl.class);

	@Autowired
	private SyncTaskRepository syncTaskRepository;

	@Override
	public void syncData(String appId, String callbackName, String callbackType, Map<String, String> infoMap) {

		Gson gson = new Gson();

		String url = "";
		String jsonStr = "";
		String orderId = infoMap.get("orderId");
		orderId = (orderId == null) ? "" : orderId;
		String syncId = "" + System.currentTimeMillis() + orderId;

		log.info("首次回调，syncData方法入参: syncId=" + syncId + "; appId=" + appId + "; callbackName=" + callbackName
				+ "; callbackType=" + callbackType);
		try {
			/*
			info = {"signer":"金惠宝","status":"2","updateTime":"2016-09-18 15:19:13","userId":"20130000","orderId":"1608300323132911","syncOrderId":"14741831538661608300323132911"}
			*/
			String signer = StringUtil.nullToString(infoMap.get("signer"));
			String status = StringUtil.nullToString(infoMap.get("status"));
			String updateTime = StringUtil.nullToString(infoMap.get("updateTime"));
			String userId = StringUtil.nullToString(infoMap.get("userId"));
			ReturnData rd = platformQuery(appId);
			String appKey = "";
			if(null != rd)
			{
				String pojoJson = rd.getPojo();
				Map m = gson.fromJson(pojoJson, Map.class);
				appKey = StringUtil.nullToString((String)m.get("appSecretKey"));
			}
				
			/**
			 * 回调加密的拼接规则
			 * orderId +"&" + signer +"&" + status +"&" + updateTime +"&" + userId +"&" + syncOrderId +"&" + appSecretKey;
			 */
			String callbackMd5Str = orderId +"&" + signer +"&" + status +"&" + updateTime +"&" + userId +"&" + syncId +"&" + appKey;
			log.info("server md5 String before = " + callbackMd5Str);
			callbackMd5Str = callbackMd5Str.replaceAll("\r|\n", "");
			log.info("server md5 String after = " + callbackMd5Str);
	
			String callbackCheck = MD5Util.MD5Encode(callbackMd5Str, "UTF-8");
			log.info("callbackCheck =" + callbackCheck);
			infoMap.put("callbackCheck", callbackCheck);	

			url = getCallBackUrl(ConstantParam.OPT_FROM, appId, callbackName, callbackType);
			log.info("首次回调，地址URL:" + url);

			if (url.equals("")) {
				log.info("首次回调，地址为空，不回调！");
				return;
			}

			infoMap.put("syncOrderId", syncId);
			jsonStr = gson.toJson(infoMap);
			log.info("首次回调，参数json串:" + jsonStr);

			String lineStr = sendHttpRequest(url, jsonStr);

			log.info("首次回调，对方平台返回值：" + lineStr);

			if (lineStr.equals("") || lineStr.length() == 0) {
				SyncTaskBean task = new SyncTaskBean();
				task.setTime(DateUtil.toDateYYYYMMDDHHMM2());
				task.setStatus(0);
				task.setOrderId(syncId);
				task.setCallbackName(callbackName);
				task.setSyncTime(DateUtil.delayTime(2));
				task.setSyncNum(0);
				task.setPlatformId(appId);
				task.setUrl(url);
				task.setInfo(jsonStr);
				addSyncTask(task);
			} else {
				Map<String, Map<String, String>> map = gson.fromJson(lineStr, Map.class);
				Map<String, String> retMap = (Map<String, String>) map.get("data");
				Iterator<String> it = retMap.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String value = retMap.get(key);
					if (!value.equals("1")) {

						SyncTaskBean task = new SyncTaskBean();
						task.setTime(DateUtil.toDateYYYYMMDDHHMM2());
						task.setStatus(0);
						task.setOrderId(syncId);
						task.setCallbackName(callbackName);
						task.setSyncTime(DateUtil.delayTime(2));
						task.setSyncNum(0);
						task.setPlatformId(appId);
						task.setUrl(url);
						task.setInfo(jsonStr);
						addSyncTask(task);

						log.info("首次回调，对方平台返回值中，有失败数据，将回调数据推送至定时任务表中，URL=" + url + ", info = " + jsonStr + ", appId="
								+ appId + ", callbackName=" + callbackName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

			SyncTaskBean task = new SyncTaskBean();
			task.setTime(DateUtil.toDateYYYYMMDDHHMM2());
			task.setStatus(0);
			task.setOrderId(syncId);
			task.setCallbackName(callbackName);
			task.setSyncTime(DateUtil.delayTime(2));
			task.setSyncNum(0);
			task.setPlatformId(appId);
			task.setUrl(url);
			task.setInfo(jsonStr);
			addSyncTask(task);

			log.info("首次回调，发生异常，将回调数据推送至定时任务表中，URL=" + url + ", info = " + jsonStr + ", appId=" + appId
					+ ", callbackName=" + callbackName);
		}
	}

	@Override
	public void syncData() {

		log.info("################ MMEC同步定时器 START ################");
		Gson gson = new Gson();

		try {

			List<PlatformCallbackBean> callBackList = this.platformCallBackQuery(ConstantParam.OPT_FROM, "",
					ConstantParam.CALLBACK_TYPE_CB);
			for (int i = 0; i < callBackList.size(); i++) {

				PlatformCallbackBean callBack = (PlatformCallbackBean) callBackList.get(i);

				String appId = callBack.getAppId();
				String callbackName = callBack.getCallBackName();
				String url = callBack.getUrl();

				List<SyncTaskBean> syncList = getSyncTaskList(appId, callbackName, url);
				log.info("定时器获取平台下对应的同步数据, appid: " + appId + "; callbackName: " + callbackName + "; url: " + url
						+ "; 需要同步的数据条数: " + syncList.size());

				if (null != syncList && !syncList.isEmpty()) {

					String jsonStr = "";
					List<String> array = new ArrayList<String>();

					SyncTaskBean task = new SyncTaskBean();
					for (int j = 0; j < syncList.size(); j++) {
						task = (SyncTaskBean) syncList.get(j);
						jsonStr = task.getInfo();
						log.info("定时器获取同步数据的info:" + jsonStr);

						int syncNum = task.getSyncNum() + 1;
						int min = calculateMinute(syncNum);
						if (min != 0) {
							String syncTime = DateUtil.delayTime(new Date(), min);
							syncTaskRepository.updateSyncTaskTime(syncTime, syncNum, task.getId());
							log.info("定时器修改下一次同步的时间，syncTime =" + syncTime + ",syncNum=" + syncNum + ",orderId="
									+ task.getOrderId());
						}

						// 发送HTTP请求
						String lineStr = sendHttpRequest(url, jsonStr);

						log.info("定时器回调，对方平台返回值：" + lineStr);

						try {
							Map map = gson.fromJson(lineStr, Map.class);
							Map retMap = (Map) map.get("data");
							Iterator it = retMap.keySet().iterator();
							while (it.hasNext()) {
								String key = (String) it.next();
								String value = (String) retMap.get(key);
								if ("1".equals(value)) {
									array.add(key);
								}
							}

						} catch (Exception e) {
							log.info("定时器回调，解析对方平台返回值时，发生异常");
							e.printStackTrace();
						}
					}

					// 将同步成功的数据修改为成功状态
					updateSyncStatus(callbackName, array, appId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("MMEC同步定时器异常，" + e.getMessage());
		}

		// 将超过24小时的数据修改为停用状态
		updateSyncStop();
		log.info("################ MMEC同步定时器 END ################");
	}

	private String sendHttpRequest(String url, String jsonStr) throws Exception {

		// 获取同步数据
		HttpClient client = new HttpClient();
		HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();

		// 设置连接超时时间(单位毫秒)
		// managerParams.setConnectionTimeout(ConstantParam.CONN_TIMEOUT);
		// // 设置读数据超时时间(单位毫秒)
		// managerParams.setSoTimeout(ConstantParam.CONN_TIMEOUT);

		PostMethod method = new PostMethod(url);
		method.addParameter("info", URLEncoder.encode(jsonStr));
		HttpMethodParams param = method.getParams();
		param.setContentCharset("UTF-8");

		log.info("Send infoStr: " + URLEncoder.encode(jsonStr));

		client.executeMethod(method);
		log.info("HTTP请求已发送，对方平台响应状态：" + method.getStatusLine());

		// 获取服务端返回，返回值为 133122,133133,133121
		InputStream stream = method.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		StringBuffer buf = new StringBuffer();
		String line;
		while (null != (line = br.readLine())) {
			buf.append(line);
		}

		// 释放连接
		method.releaseConnection();
		return buf.toString();
	}

	public int calculateMinute(int i) {
		int minute = 0;
		switch (i) {
		case 1:
			minute = 2;
			break;
		case 2:
			minute = 2;
			break;
		case 3:
			minute = 10;
			break;
		case 4:
			minute = 60;
			break;
		case 5:
			minute = 10 * 60;
			break;
		case 6:
			minute = 10 * 60;
			break;
		default:
			minute = 0;
			break;
		}
		return minute;
	}

	public List<SyncTaskBean> getSyncTaskList(String appId, String callbackName, String url) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<SyncTaskBean> result = new ArrayList<SyncTaskBean>();

		try {

			result = syncTaskRepository.findSyncTaskByName(appId, callbackName, url, formatter.format(new Date()));

		} catch (Exception e) {
			e.printStackTrace();
			log.error("MMEC同步定时器，查询同步数据出现异常：" + e.getMessage());
		}
		return result;
	}

	private void updateSyncStop() {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		long delay = 24 * 60 * 60 * 1000;
		long l = date.getTime() - delay;
		date.setTime(l);

		int num = syncTaskRepository.updateSyncTaskStop(formatter.format(date));

		log.info("定时器24小时将超时数据修改为停止状态，修改影响数:" + num);
	}

	private void updateSyncStatus(String callbackName, List<String> list, String appId) {

		log.info("将定时器回调成功的数据修改为成功状态，成功数量:" + list.size());

		if (null != list && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {

				String orderId = list.get(i);

				syncTaskRepository.updateSyncTaskStatus2(callbackName, orderId, appId);

				log.info("定时器回调成功的数据修改为成功状态，appId=" + appId + "; callbackName=" + callbackName + "; syncOrderId="
						+ orderId);
			}
		}
	}

	@Override
	public int activateData(String callbackName, String appId) {

		log.info("激活同步数据，callbackName = " + callbackName + ", appId = " + appId);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = formatter.format(new Date());
		int num = syncTaskRepository.updateSyncTaskStatus(dateStr, dateStr, appId, callbackName);

		log.info("激活同步数据，callbackName = " + callbackName + ", appId = " + appId + ", 影响数: " + num);

		return num;
	}

	public void addSyncTask(SyncTaskBean bean) {
		syncTaskRepository.save(bean);
	}

	private List<PlatformCallbackBean> platformCallBackQuery(String optFrom, String appId, String callbackType) {

		Gson gson = new Gson();

		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", optFrom);
		map.put("appId", appId);
		map.put("callBackType", callbackType);

		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_USER)).queryPlateFormCallBack(map);
		log.info("queryPlatFormCallBack, call center model success. returnData:" + returnData);

		if (returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			PlatformBean retMap = gson.fromJson(returnData.getPojo(), PlatformBean.class);
			return retMap.getCallBackList();
		}
		return new ArrayList<PlatformCallbackBean>();
	}

	public String getCallBackUrl(String optFrom, String appId, String callbackName, String callbackType) {

		String url = "";

		try {
			List<PlatformCallbackBean> pfcallBackBeans = this.platformCallBackQuery(optFrom, appId, callbackType);

			for (PlatformCallbackBean pfcallBackBean : pfcallBackBeans) {

				if (pfcallBackBean.getCallBackName().equals(callbackName)) {
					url = pfcallBackBean.getUrl();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取回调或回跳地址失败，" + e.getMessage());
		}

		return url;
	}

	public ReturnData platformQuery(String appId) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", ConstantParam.OPT_FROM);
		map.put("appId", appId);

		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_USER)).queryPlatForm(map);
		log.info("queryPlatForm, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}
	
	@Override
	public Result check(long time, String md5Str, String appId, String sign) {

		Gson gson = new Gson();
		SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_USER);

		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", ConstantParam.OPT_FROM);
		map.put("appId", appId);
		ReturnData platInfo = sdu.queryPlatForm(map);

		if (!platInfo.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			return new Result(platInfo.getRetCode(), platInfo.getDesc(), platInfo.getPojo());
		}

		PlatformBean pfBean = gson.fromJson(platInfo.getPojo(), PlatformBean.class);

		String md5Str1 = md5Str + "&" + pfBean.getAppSecretKey();
		System.out.println("md5Str1_i===" + md5Str1);
		String md5 = MD5Util.MD5Encode(md5Str1, "GBK");
		log.info("input md5=" + sign + ",server md5=" + md5);
		if (!md5.equals(sign)) {
			log.info("check MD5,MD5 verification error.");
			return new Result(ErrorData.MD5_VALID_FAIL, PropertiesUtil.getProperties().readValue("MD5_ERROR"), "");
		}

		Date date = new Date();
		long currlongTime = date.getTime();

		log.info("check timestamp，server current time=" + DateUtil.toDateYYYYMMDDHHMM2(date));
		Date date1 = new Date();
		date1.setTime(time);
		log.info("check timestamp，input time=" + DateUtil.toDateYYYYMMDDHHMM2(date1));

		long before = currlongTime - (60 * 1000);
		long after = currlongTime + (60 * 1000);

		if (!(time >= before && time <= after)) {
			log.info("check timestamp, validate time error.");
			return new Result(ErrorData.TIME_VALID_FAIL, PropertiesUtil.getProperties().readValue("TIMESTAMP_ERROR"),
					"");
		}
		return new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("VAILD_SUCCESS"), "");
	}

	
	@Override
	public Result checkAuth(String appId, long time, String sign, String md5Str, String authNum) {

		Gson gson = new Gson();
		int i = 0;

		try {

			// 查询平台信息
			ReturnData retData = this.platformQuery(appId);
			if (!retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				return new Result(retData.getRetCode(), retData.getDesc(), "");
			}

			// 检查平台是否具有相应接口的操作权限
			PlatformBean pfBean = gson.fromJson(retData.getPojo(), PlatformBean.class);
			List<AuthorityBean> authList = pfBean.getAdminAuthList();
			for (AuthorityBean authorityBean : authList) {
				if (authorityBean.getAuthNum().equals(authNum)) {
					i++;
					break;
				}
			}

			if (i == 0) {
				log.info(PropertiesUtil.getProperties().readValue("NO_AUTH"));
				return new Result(ErrorData.NO_AUTH, PropertiesUtil.getProperties().readValue("NO_AUTH"),
						"appId: " + appId);
			}

			// 校验MD5、时间戳
			if (!StringUtil.isNull(sign) && !StringUtil.isNull(md5Str)) {
				Result res = this.checkMd5AndTime(time, md5Str, sign, pfBean.getAppSecretKey());
				if (!res.getCode().equals(ErrorData.SUCCESS)) {
					return res;
				}
			}

			return new Result(ErrorData.SUCCESS, "", "");
		} catch (Exception e) {
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
		}
	}
	@Override
	public Result checkAuth2(String appId, long time, String sign, String md5Str, String authNum) {

		Gson gson = new Gson();
		int i = 0;

		try {

			// 查询平台信息
			ReturnData retData = this.platformQuery(appId);
			if (!retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				return new Result(retData.getRetCode(), retData.getDesc(), "");
			}

			// 检查平台是否具有相应接口的操作权限
			PlatformBean pfBean = gson.fromJson(retData.getPojo(), PlatformBean.class);
			/*List<AuthorityBean> authList = pfBean.getAdminAuthList();
			for (AuthorityBean authorityBean : authList) {
				if (authorityBean.getAuthNum().equals(authNum)) {
					i++;
					break;
				}
			}

			if (i == 0) {
				log.info(PropertiesUtil.getProperties().readValue("NO_AUTH"));
				return new Result(ErrorData.NO_AUTH, PropertiesUtil.getProperties().readValue("NO_AUTH"),
						"appId: " + appId);
			}*/

			// 校验MD5、时间戳
			if (!StringUtil.isNull(sign) && !StringUtil.isNull(md5Str)) {
				Result res = this.checkMd5AndTime(time, md5Str, sign, pfBean.getAppSecretKey());
				if (!res.getCode().equals(ErrorData.SUCCESS)) {
					return res;
				}
			}

			return new Result(ErrorData.SUCCESS, "", "");
		} catch (Exception e) {
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
		}
	}
	
	
	@Override
	public Result checkAuth3(String appId, long time, String sign, String md5Str, String authNum) {

		Gson gson = new Gson();
		int i = 0;

		try {

			// 查询平台信息
			ReturnData retData = this.platformQuery(appId);
			if (!retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				return new Result(retData.getRetCode(), retData.getDesc(), "");
			}

			// 检查平台是否具有相应接口的操作权限
			PlatformBean pfBean = gson.fromJson(retData.getPojo(), PlatformBean.class);
			List<AuthorityBean> authList = pfBean.getAdminAuthList();
			for (AuthorityBean authorityBean : authList) {
				if (authorityBean.getAuthNum().equals(authNum)) {
					i++;
					break;
				}
			}

			if (i == 0) {
				return new Result(ErrorData.NO_AUTH, PropertiesUtil.getProperties().readValue("NO_AUTH"),
						"appId: " + appId);
			}

			return new Result(ErrorData.SUCCESS, "", "");
		} catch (Exception e) {
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
		}
	}
	
	@Override
	public Result checkAuthAndIsPdfSign(String appId, long time, String sign, String md5Str, String authNum,
			String pdfOrZip) {

		Gson gson = new Gson();
		int i = 0;

		try {

			// 查询平台信息
			ReturnData retData = this.platformQuery(appId);
			if (!retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				return new Result(retData.getRetCode(), retData.getDesc(), "");
			}

			// 检查平台是否具有相应接口的操作权限
			PlatformBean pfBean = gson.fromJson(retData.getPojo(), PlatformBean.class);
			List<AuthorityBean> authList = pfBean.getAdminAuthList();
			for (AuthorityBean authorityBean : authList) {
				if (authorityBean.getAuthNum().equals(authNum)) {
					i++;
					break;
				}
			}

			if (i == 0) {
				log.info(PropertiesUtil.getProperties().readValue("NO_AUTH"));
				return new Result(ErrorData.NO_AUTH, PropertiesUtil.getProperties().readValue("NO_AUTH"),
						"appId: " + appId);
			}

			// 校验平台是否有签署PDF合同的权限
			if (pdfOrZip.equals(ConstantParam.ISPDF) && pfBean.getIsPdfSign().equals("0")) {
				return new Result(ErrorData.NO_PDF_SIGN_AUTH,
						PropertiesUtil.getProperties().readValue("NO_PDF_SIGN_AUTH"), "appId: " + appId);
			} else if (pdfOrZip.equals(ConstantParam.ISZIP) && pfBean.getIsPdfSign().equals("1")) {
				return new Result(ErrorData.NO_ZIP_SIGN_AUTH,
						PropertiesUtil.getProperties().readValue("NO_ZIP_SIGN_AUTH"), "appId: " + appId);
			}

			// 校验MD5、时间戳
			if (!StringUtil.isNull(sign) && !StringUtil.isNull(md5Str)) {
				Result res = this.checkMd5AndTime(time, md5Str, sign, pfBean.getAppSecretKey());
				if (!res.getCode().equals(ErrorData.SUCCESS)) {
					return res;
				}
			}

			return new Result(ErrorData.SUCCESS, "", "");
		} catch (Exception e) {
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
		}
	}

	@Override
	public Result checkMd5AndTime(long time, String md5Str, String sign, String appKey) {

		// SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_USER);
		//
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("optFrom", ConstantParam.OPT_FROM);
		// map.put("appId", appId);
		// ReturnData platInfo = sdu.queryPlatForm(map);
		//
		// if (!platInfo.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
		// return new Result(platInfo.getRetCode(), platInfo.getDesc(),
		// platInfo.getPojo());
		// }
		//
		// PlatformBean pfBean = gson.fromJson(platInfo.getPojo(),
		// PlatformBean.class);

		String md5Str1 = md5Str + "&" + appKey;
		md5Str1 = md5Str1.replaceAll("\r|\n", "");
		log.info("server md5 String = " + md5Str1);

		String md5 = MD5Util.MD5Encode(md5Str1, "UTF-8");
		log.info("check MD5, input md5=" + sign + ", server md5=" + md5);

		if (!md5.equals(sign)) {
			log.info("check MD5, MD5 verification error.");
			return new Result(ErrorData.MD5_VALID_FAIL, PropertiesUtil.getProperties().readValue("MD5_ERROR"),
					"");
		}

		Date date = new Date();
		long currlongTime = date.getTime();

		Date date1 = new Date();
		date1.setTime(time);
		log.info("check timestamp, input time=" + DateUtil.toDateYYYYMMDDHHMM2(date1) + ", server current time="
				+ DateUtil.toDateYYYYMMDDHHMM2(date));

		long before = currlongTime - (60 * 1000);
		long after = currlongTime + (60 * 1000);

		if (!(time >= before && time <= after)) {
			log.info("check timestamp, validate time error.");
			return new Result(ErrorData.TIME_VALID_FAIL, PropertiesUtil.getProperties().readValue("TIMESTAMP_ERROR"),
					"");
		}
		return new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("VAILD_SUCCESS"), "");
	}
	
	@Override
	public Result checkAuthAndAppSecretKey(String appId,String sgin,String md5str, String appSecretKey,String authNum) {
		
		Gson gson = new Gson();
		int i = 0;

		try {

			// 查询平台信息
			ReturnData retData = this.platformQuery(appId);
			
			if (!retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				return new Result(retData.getRetCode(), retData.getDesc(), "");
			}

			// 检查平台是否具有相应接口的操作权限
			PlatformBean pfBean = gson.fromJson(retData.getPojo(), PlatformBean.class);
			List<AuthorityBean> authList = pfBean.getAdminAuthList();
			for (AuthorityBean authorityBean : authList) {
				if (authorityBean.getAuthNum().equals(authNum)) {
					i++;
					break;
				}
			}
			
			//检验md5
			String md5Str1 = md5str + "&" + pfBean.getAppSecretKey();
			md5Str1 = md5Str1.replaceAll("\r|\n", "");
			log.info("server md5 String = " + md5Str1);

			String md5 = MD5Util.MD5Encode(md5Str1, "UTF-8");
			log.info("check MD5, input md5=" + sgin + ", server md5=" + md5);

			if (!md5.equals(sgin)) {
				log.info("check MD5, MD5 verification error.");
				return new Result(ErrorData.MD5_VALID_FAIL, PropertiesUtil.getProperties().readValue("MD5_ERROR"),
						"");
			}
			

			if (i == 0) {
				log.info(PropertiesUtil.getProperties().readValue("NO_AUTH"));
				return new Result(ErrorData.NO_AUTH, PropertiesUtil.getProperties().readValue("NO_AUTH"),
						"appId: " + appId);
			}
			
			//检查appSecretKey是否是当前平台的秘钥
			if(!appSecretKey.equals(pfBean.getAppSecretKey())){
				
				log.info(PropertiesUtil.getProperties().readValue("NO_SECRETKEY_BY_APPID"));
				return new Result(ErrorData.NO_SECRETKEY_BY_APPID, PropertiesUtil.getProperties().readValue("NO_SECRETKEY_BY_APPID"),
						"appId: " + appId);
				
			}
		}catch(Exception e){
			
			e.printStackTrace();
			
			log.info(PropertiesUtil.getProperties().readValue("NO_SECRETKEY_BY_APPID"));
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"),
					"appId: " + appId);
		}
		
		return new Result(ErrorData.SUCCESS,PropertiesUtil.getProperties().readValue("VAILD_SUCCESS"),"");
	}
	
	@Override
	public Result queryUserExamineStatus(String appId, String platformUserName) {	
		
		Result result=null;
		
		Gson gson=new Gson();
		try{
			
			Map<String,String> map=new HashMap<String,String>();
			map.put("appId", appId);
			map.put("platformUserName", platformUserName);
			ReturnData res = (new SendDataUtil(ConstantParam.INTF_NAME_USER)).queryUserExamineStatus(map);
	
		if (!res.getRetCode().equals(ErrorData.SUCCESS)) {
			
			log.info("returnStr：" + gson.toJson(res));
			result=new Result(res.getRetCode(),res.getDesc(),res.getPojo());
			return result;
		}
		}catch(Exception e){
			e.printStackTrace();
			result = new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
			return result;
			
		}
		
		return result;
	}

	@Override
	public Result sendWXMessage4Type(String optFrom, String appId, String wxType, String orderId,
			String operPlatformUserName, String requestIp) {
		log.info("接口发送微信消息,参数：optFrom：" + optFrom + "appId:" + appId + "wxType:" + wxType + "orderId:" + orderId
				+ "operPlatformUserName:" + operPlatformUserName);
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("optFrom", optFrom);
		dataMap.put("appId", appId);
		dataMap.put("wxType", wxType);
		dataMap.put("orderId", orderId);
		dataMap.put("operPlatformUserName", operPlatformUserName);
		dataMap.put("requestIp", requestIp);
		ReturnData returnData = (new SendDataUtil("ApsRMIServices")).sendWXMessage4Type(dataMap);
		log.info(wxType + "接口发送微信消息. 中央承载返回：" + returnData);
		return new Result(returnData.retCode, returnData.getDesc(), "");
	}

	@Override
	public Result sendMessage4Type(String optFrom, String appId, String account, String mobile, String smsType,
			String checkCode, String platformUserName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("optFrom", optFrom);
		dataMap.put("appId", appId);
		if (!"".equals(account) && null != account) {
			dataMap.put("account", account);
		}
		dataMap.put("mobile", mobile);
		dataMap.put("smsType", smsType);
		dataMap.put("checkCode", checkCode);
		if (!"".equals(platformUserName) && null != platformUserName) {
			dataMap.put("platformUserName", platformUserName);
		}
		ReturnData returnData = (new SendDataUtil("ApsRMIServices")).sendWXMessage4Type(dataMap);
		log.info(smsType + "接口发送微信消息. 中央承载返回：" + returnData);
		return new Result(returnData.retCode, returnData.getDesc(), "");
	}

	@Override
	public String getIp(WebServiceContext context) {

		try {

			MessageContext ctx = context.getMessageContext();
			HttpServletRequest request = (HttpServletRequest) ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
			String ip = request.getRemoteAddr();

			log.info("客户端访问的IP地址：" + ip);
			return ip;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取客户端的IP地址失败：" + e);
			return "";
		}
	}

	@Override
	public Result changeMobile(String appId, String ucid, String mobile) {
		Map<String, String> datamap = new HashMap<String, String>();
		// datamap.put("optFrom", ConstantParam.OPT_FROM);// 必填
		datamap.put("optType", "changeMobile");
		datamap.put("appId", appId);
		datamap.put("platformUserName", ucid);
		datamap.put("mobile", mobile);
		ReturnData returnData = new SendDataUtil(ConstantParam.INTF_NAME_USER).userUpdate(datamap);
		log.info("userUpdate, call center model success. 中央承载返回：" + returnData);

		if (returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			return new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("MOBILE_CHANGE_SUCCESS"), "");
		}
		return new Result(returnData.getRetCode(), returnData.getDesc(), "");
	}

	@Override
	public Result synchronizationUserInfo(String appId,String platformUserName,String sign,String time,String appKey, String phone, String userStatus) {
		
		Map<String, String> datamap = new HashMap<String, String>();
		
		datamap.put("appId", appId);
		datamap.put("platformUserName", platformUserName);
		
		Gson gson = new Gson();
		
		//超时验证
		
		Date date = new Date();
		long currlongTime = date.getTime();

		log.info("check timestamp，server current time=" + DateUtil.toDateYYYYMMDDHHMM2(date));
		Date date1 = new Date();
		
		long time2=Long.parseLong(time);
		date1.setTime(time2);
		log.info("check timestamp，input time=" + DateUtil.toDateYYYYMMDDHHMM2(date1));

		long before = currlongTime - (120 * 1000);
		long after = currlongTime + (120 * 1000);

		if (!(time2 >= before && time2 <= after)) {
			log.info("check timestamp, validate time error.");
			return new Result(ErrorData.TIME_VALID_FAIL, PropertiesUtil.getProperties().readValue("TIMESTAMP_ERROR"),
					"");
		}
		
		//添加md5校验
		
		String md5Str = null;
		
		
		if(null==userStatus || "null".equals(userStatus)){
			
			userStatus="";
			
			md5Str=appId+"&"+platformUserName+"&"+time+"&"+phone;
		}
		
		if(null==phone || "null".equals(phone)){
			
			phone="";
			
			md5Str=appId+"&"+platformUserName+"&"+time+"&"+userStatus;
		}
		
		SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_USER);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", ConstantParam.OPT_FROM);
		map.put("appId", appId);
		ReturnData platInfo = sdu.queryPlatForm(map);

		if (!platInfo.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			return new Result(platInfo.getRetCode(), platInfo.getDesc(), platInfo.getPojo());
		}

		PlatformBean pfBean = gson.fromJson(platInfo.getPojo(), PlatformBean.class);

		md5Str = md5Str + "&" + pfBean.getAppSecretKey();
		
		md5Str = md5Str.replaceAll("\r|\n", "");
		String newSign=MD5Util.MD5Encode(md5Str,"UTF-8");
		
		if(!newSign.equals(sign) && sign!=null){
			
			log.info("生成的sign:"+newSign+",传过来的sign:"+sign);
			return new Result(ErrorData.MD5_VALID_FAIL, PropertiesUtil.getProperties().readValue("MD5_ERROR"), "");
			
		}
		
		
		datamap.put("userStatus", userStatus);
		datamap.put("phone", phone);
		
		ReturnData returnData = new SendDataUtil(ConstantParam.INTF_NAME_USER).synchronizationUserInfo(datamap);
		
		log.info("userUpdate, call center model success. 中央承载返回：" + returnData);

		if (returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			return new Result(ErrorData.SUCCESS, returnData.getDesc(), "");
		}
		return new Result(returnData.getRetCode(), returnData.getDesc(), "");
	}




}
