package com.mmec.business.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mmec.business.bean.PlatformBean;
import com.mmec.business.bean.SealBean;
import com.mmec.business.bean.UserBean;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.ContractService;
import com.mmec.business.service.SealService;
import com.mmec.business.service.SignService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;
@Controller
public class LocalSignController{
	private Logger log = Logger.getLogger(LocalSignController.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	private SignService signService;

	@Autowired
	private SealService sealService;

	@Autowired
	private BaseService baseService;

	@Autowired
	private ContractService contractService;
	
	@RequestMapping(value = "/localsign.do")
	public String sign(HttpServletRequest request, HttpServletResponse response) {

		log.info("Access localsign method");
		Gson gson = new Gson();

		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String sign_type = StringUtil.nullToString(request.getParameter("signType"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String validType = StringUtil.nullToString(request.getParameter("validType"));
		String certType = StringUtil.nullToString(request.getParameter("certType"));
		String isPdf = StringUtil.nullToString(request.getParameter("isPdf"));
		String payType=StringUtil.nullToString(request.getParameter("payType"));
		//付费方式
		String param = "orderId:" + orderId + ",userId:" + userId + ",appId:" + appId + ",sign:" + sign + ",sign_type:"
				+ sign_type + ",time:" + time + ",validType:" + validType + ",certType:" + certType + ",isPdf:" + isPdf;

		log.info("Sign access parameter: ：" + param);

		if ("".equals(appId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(time)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("TIME_EMPTY"));
			return "error";
		}
		if ("".equals(sign)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SIGN_EMPTY"));
			return "error";
		}
		if ("".equals(sign_type)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"));
			return "error";
		}
		if ("".equals(userId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		if ("".equals(orderId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}

		try {

			// 校验MD5、时间戳、权限、PDF/ZIP签署权限
			Result res = baseService.checkAuthAndIsPdfSign(appId, 0, "", "", ConstantParam.signPage, "");
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				request.setAttribute("error", res.getDesc());
				return "error";
			}

			String isSendSms = "";
			// 调用中央承载查询平台接口
			ReturnData platData = userService.platformQuery(appId);
			if (platData != null && platData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				PlatformBean platBean = gson.fromJson(platData.getPojo(), PlatformBean.class);
				isSendSms = platBean.getIsSmsUse();
			}
//			if (validType.equals(ConstantParam.VALID_CODE_SMS) && !isSendSms.equals("1")) {
//				request.setAttribute("error", PropertiesUtil.getProperties().readValue("NO_SENDSMS"));
//				return "error";
//			}

			ReturnData resDataUser = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
			if (!ConstantParam.CENTER_SUCCESS.equals(resDataUser.getRetCode())) {
				request.setAttribute("error", resDataUser.getDesc());
				return "error";
			}
			UserBean user = gson.fromJson(resDataUser.getPojo().toString(), UserBean.class);
			String userName = user.getUserName();
			// 调用中央承载查询合同接口
			ReturnData returnData = contractService.findContract(appId, userId, orderId);
			if (!ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
				request.setAttribute("error", returnData.getDesc());
				return "error";
			} else {
				List<SealBean> lists = null;
				if (!StringUtil.isNull(userName)) {
					ReturnData resData = sealService.querySeal(ConstantParam.OPT_FROM, appId, userId);
					// List<SealBean> lists = null;
					if (ConstantParam.CENTER_SUCCESS.equals(resData.getRetCode())) {
						JSONObject jsonObj = new JSONObject();
						jsonObj = jsonObj.fromObject(resData.getPojo());
						SealBean seal = new SealBean();
						lists = str2List(jsonObj.getString("list"), seal);
					}
				}
				String fromCustom = user.getPlatformUserName();
				String email = user.getEmail();

				Map contractMap = JSON.parseObject(returnData.getPojo(), Map.class);
				int user_id = Integer.parseInt((String) contractMap.get("creator"));
				String serialNum = (String) contractMap.get("serialNum");
				String attName = (String) contractMap.get("attName");
				String title = (String) contractMap.get("title");
				String dateline = (String) contractMap.get("deadline");
				String createTime = (String) contractMap.get("createTime");

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
				Date create = format.parse(createTime);
				SimpleDateFormat local = new SimpleDateFormat("yyyyMM");
				String ruleLocal = local.format(create);
				String lastLocal = ConstantParam.CONTRACT_PATH + ruleLocal;
				String createName = null;
				String imgLocal = request.getContextPath() + "/contract/" + ruleLocal;
				String signer = (String) contractMap.get("signRecord");
				List<Map<String, Object>> getInfo = parseJSON2List(signer);
				List<UserBean> listOtherUser = new ArrayList<UserBean>();
				for (int i = 0; i < getInfo.size(); i++) {
					UserBean userInfo = new UserBean();
					userInfo.setUserName((String) getInfo.get(i).get("signerName"));
					if (user_id == Integer.parseInt((String) getInfo.get(i).get("signerId"))) {
						createName = (String) getInfo.get(i).get("signerName");
					}
					listOtherUser.add(userInfo);
				}
				String mobile = user.getMobile();
				String listMapAttr = (String) contractMap.get("listMapAttr");
				String optFrom = (String) contractMap.get("optFrom");
				List<Map> MapAttr = gson.fromJson(listMapAttr, List.class);
				List<String> fjList = null;

				String fjAttName = "";
				String extension = "";
				String filePath = "";
				List<List> afjList = new ArrayList<List>();
				List<String> imgPath = new ArrayList<String>();
				if (optFrom.equals("9")) {

					String tempName = (String) contractMap.get("attName");
					String tempExtension = (String) contractMap.get("extension");
					String tempFilePath = (String) contractMap.get("filePath");
					imgPath = signService.getOldImgPath(tempName, tempExtension, tempFilePath, request, serialNum);
					// 查询附件路径
					for (int i = 0; i < MapAttr.size(); i++) {
						fjList = new ArrayList<String>();
						fjAttName = (String) MapAttr.get(i).get("attName");
						extension = (String) MapAttr.get(i).get("extension");// 附件原始文件后缀名
						filePath = (String) MapAttr.get(i).get("originalPath");// 附件原始文件路径
						fjList = signService.getOldImgPath(fjAttName, extension, filePath, request, serialNum);
						afjList.add(fjList);

					}
					log.info("附件路径：" + afjList);

				} else {
					imgPath = signService.getImgPath(ruleLocal, "", attName, request, serialNum);
					// 查询附件路径
					for (int i = 0; i < MapAttr.size(); i++) {
						fjList = new ArrayList<String>();
						fjAttName = (String) MapAttr.get(i).get("attName");
						extension = (String) MapAttr.get(i).get("extension");// 附件原始文件后缀名
						filePath = (String) MapAttr.get(i).get("originalPath");// 附件原始文件路径
						fjList = signService.getFjImgPath(ruleLocal, extension, filePath, fjAttName, request,
								serialNum);
						afjList.add(fjList);

					}
					log.info("附件路径：" + afjList);
				}

				// 获取图章
				request.setAttribute("sealCompany", lists);
				request.setAttribute("serialNum", serialNum);
				request.setAttribute("orderId", orderId);
				request.setAttribute("orderid", orderId);
				request.setAttribute("title", title);
				request.setAttribute("dateline", dateline);
				request.setAttribute("createTime", createTime);
				request.setAttribute("mobile", mobile);
				request.setAttribute("ruleLocal", ruleLocal);
				request.setAttribute("lastLocal", lastLocal);
				request.setAttribute("listOtherUser", listOtherUser);
				request.setAttribute("fromCustom", fromCustom);
				request.setAttribute("createName", createName);
				request.setAttribute("ucid", userId);
				request.setAttribute("appId", appId);
				request.setAttribute("attachmentName", attName);
				log.info("合同图片路径:" + imgPath);
				request.setAttribute("imgPath", imgPath);
				request.setAttribute("validType", validType);
				request.setAttribute("email", email);
				request.setAttribute("isPdf", isPdf);
				request.setAttribute("certType", certType);
				request.setAttribute("fjList", afjList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "合同签署失败！");
			return "error";
		}
		// 判断请求客户端来源是pc端还是移动端
		String ua = request.getHeader("User-Agent");
		if (ua != null) {
			if (ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1
					|| (ua.indexOf("Android") > -1 && ua.indexOf("WebKit") > -1)) {
				return "wxSignature";
			} else {
				return "sign";
			}

		}
		return "sign";
	}
	
	public static List<Map<String, Object>> parseJSON2List(String jsonStr) {
		JSONArray jsonArr = JSONArray.fromObject(jsonStr);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Iterator<JSONObject> it = jsonArr.iterator();
		while (it.hasNext()) {
			JSONObject json2 = it.next();
			list.add(parseJSON2Map(json2.toString()));
		}
		return list;
	}

	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}
	

	public static <T> List<T> str2List(String str, T obj) {
		JSONArray jsonArray = JSONArray.fromObject(str);
		List<T> lists = (List) JSONArray.toCollection(jsonArray, obj.getClass());
		return lists;
	}
}