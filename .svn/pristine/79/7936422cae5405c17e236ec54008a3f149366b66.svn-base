package com.mmec.business.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.bean.SealBean;
import com.mmec.business.bean.UserBean;
import com.mmec.business.service.ContractService;
import com.mmec.business.service.SealService;
import com.mmec.business.service.SignService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class WxController {

	private Logger log = Logger.getLogger(WxController.class);

	@Autowired
	private SignService signService;

	@Autowired
	private UserService userService;

	@Autowired
	private SealService sealService;

	@Autowired
	private ContractService contractService;

	@RequestMapping(value = "/wxRecord.do")
	public String wxRecord(HttpServletRequest request) throws Exception {
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			log.info("Proxy-Client-IP" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			log.info("WL-Proxy-Client-IP" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr()" + ip);
		}
		log.info("request.getServerName()" + request.getServerName());
		Gson gson = new Gson();
		List<String> nameList = new ArrayList<String>();

		HttpSession session = request.getSession();
		String orderId = request.getParameter("orderId");
		String ucid = request.getParameter("ucid");
		String appId = request.getParameter("appId");

		Map<String, String> contraMap = new HashMap<String, String>();
		contraMap.put("ucid", ucid);
		contraMap.put("appId", appId);
		contraMap.put("orderId", orderId);
		contraMap.put("requestIp", ip);
		ReturnData returnData = (new SendDataUtil("ContractRMIServices")).queryContract(contraMap);

		if (!(returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS))) {
			request.setAttribute("error", returnData.getDesc());
			return "error";
		}

		Map<String, String> contractMap = new HashMap<String, String>();
		String pojo = returnData.getPojo();
		contractMap = gson.fromJson(pojo, Map.class);
		String signRecord = contractMap.get("signRecord");
		String title = contractMap.get("title");
		List signList = gson.fromJson(signRecord, List.class);
		String creator = contractMap.get("creator");
		String finishTime = contractMap.get("finishTime");// 参数名未定
		Map<String, String> map = new HashMap();
		List userList = new ArrayList();
		String signerId = "";
		String status = contractMap.get("status");
		String operator = contractMap.get("operator");
		String finishtime = contractMap.get("finishtime");
		String attName = contractMap.get("attName");
		String serialNum = contractMap.get("serialNum");
		String createTime = (String) contractMap.get("createTime");
		String deadline = contractMap.get("deadline");
		String optFrom = contractMap.get("optFrom");
		for (int i = 0; i < signList.size(); i++) {
			map = (Map<String, String>) signList.get(i);
			signerId = map.get("signerId");
			if (status.equals("4") && signerId.equals(creator)) {
				map.put("signStatus", "4");
				map.put("signTime", finishtime);
			}
			if (status.equals("3") && signerId.equals(operator)) {
				map.put("signStatus", "3");
				map.put("signTime", finishtime);
			}
			userList.add(signerId);
		}
		String userName = "";
		String companyName = "";
		String user = "";
		/*
		 * for (int i = 0; i < userList.size(); i++) { user = (String)
		 * userList.get(i); returnData = contractInterface.findUserById(appId,
		 * ConstantParam.OPT_FROM, user); userMap =
		 * gson.fromJson(returnData.getPojo(), Map.class); String type =
		 * String.valueOf(userMap.get("type")); int type1 =
		 * (Float.valueOf(type)).intValue(); if (type1 == 1) { userName =
		 * userMap.get("name"); nameList.add(userName); } else { companyName =
		 * userMap.get("enterprisename"); nameList.add(companyName); } }
		 */
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");
		// Date createDate = format.parse(createTime);
		// String year = createDate.getYear() + 1900 + "";
		// String month = createDate.getMonth() + 1 < 10 ? "0" +
		// (createDate.getMonth() + 1)
		// : createDate.getMonth() + 1 + "";
		// String timeName = year + month;
		// List imageList = this.getPreviewPath(timeName, "", attName, request,
		// serialNum);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
		Date create = format.parse(createTime);
		SimpleDateFormat local = new SimpleDateFormat("yyyyMM");
		String ruleLocal = local.format(create);

		// String ph = ConstantParam.CONTRACT_PATH + ruleLocal + "/" + serialNum
		// + "/" + "img/" + attName;
		// String p =
		// "C:\\Users\\Administrator\\Desktop\\CP2258873756388424\\img\\20160225162537535";
		// List<String> imgPath = signService.getImgPath(ph);

		// 查询附件路径
		String listMapAttr = (String) contractMap.get("listMapAttr");
		List<Map> MapAttr = gson.fromJson(listMapAttr, List.class);
		List<String> fjList = null;

		String fjAttName = "";
		String extension = "";
		String filePath = "";
		List<String> afjList = new ArrayList<String>();
		List imgPath = new ArrayList();
		if (optFrom.equals("9")) {

			String tempName = (String) contractMap.get("attName");
			String tempExtension = (String) contractMap.get("extension");
			String tempFilePath = (String) contractMap.get("filePath");
			// List<Map<String, String>> atts =
			// gson.fromJson(contractMap.get("listMapAttr"),
			// List.class);
			// for (Map<String, String> map2 : atts) {
			//
			// String fjName = map2.get("attName");
			// String fjExtension = map2.get("extension");
			// }

			imgPath = signService.getOldImgPath(tempName, tempExtension, tempFilePath, request, serialNum);
			// 查询附件路径
			for (int i = 0; i < MapAttr.size(); i++) {
				fjList = new ArrayList<String>();
				fjAttName = (String) MapAttr.get(i).get("attName");
				extension = (String) MapAttr.get(i).get("extension");// 附件原始文件后缀名
				filePath = (String) MapAttr.get(i).get("originalPath");// 附件原始文件路径
				fjList = signService.getOldImgPath(fjAttName, extension, filePath, request, serialNum);
				for (int j = 0; j < fjList.size(); j++) {
					afjList.add(fjList.get(j));
				}

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
				fjList = signService.getFjImgPath(ruleLocal, extension, filePath, fjAttName, request, serialNum);
				for (int j = 0; j < fjList.size(); j++) {
					afjList.add(fjList.get(j));
				}

			}
			log.info("附件路径：" + afjList);
		}
		session.setAttribute("serialNum", serialNum);
		session.setAttribute("title", title);
		session.setAttribute("createTime", createTime);
		session.setAttribute("status", status);
		session.setAttribute("deadline", deadline);
		session.setAttribute("imageList", imgPath);
		session.setAttribute("signList", signList);
		session.setAttribute("nameList", nameList);
		session.setAttribute("attachmentName", attName);
		session.setAttribute("ruleLocal", ruleLocal);
		session.setAttribute("fjList", afjList);

		request.setAttribute("signList", signList);
		request.setAttribute("status", status);
		request.setAttribute("ruleLocal", ruleLocal);
		request.setAttribute("attachmentName", attName);
		request.setAttribute("createTime", createTime);
		request.setAttribute("imageList", imgPath);
		request.setAttribute("fjList", afjList);

		return "wxshowContract";
	}

	public List<String> getPreviewPath(String timePath, String path, String name, HttpServletRequest request,
			String num) throws Exception {

		List<String> list = null;
		List<String> list1 = null;

		String contractImagePath = ConstantParam.CONTRACT_PATH + timePath + "/" + num + "/img/" + name + "/";

		log.info("contractImagePath===" + contractImagePath);
		String webImagePath = request.getContextPath() + "/contract/" + timePath + "/" + num + "/img/" + name + "/";
		File rootFile = new File(contractImagePath);
		if (!rootFile.exists()) {
			log.error(contractImagePath + " not exists");
			return null;
		}
		File subFile[] = rootFile.listFiles();
		log.info("subFile length=" + subFile.length);
		File file = null;
		if (null != subFile && subFile.length > 0) {
			list = new ArrayList();
			for (int i = 0; i < subFile.length; i++) {
				file = subFile[i];
				if (file.isFile()) {
					if (file.getName().contains(".png")) {
						String Pname = file.getName();
						list.add(Pname);
						log.info("aaa:" + Pname);
					}
				}
			}
			int[] a = new int[list.size()];
			for (int j = 0; j < list.size(); j++) {
				a[j] = Integer.parseInt(list.get(j).substring(0, list.get(j).length() - 4));
				log.info("bbb:" + a[j]);
			}
			Arrays.sort(a);
			list1 = new ArrayList<String>();
			for (int i = 0; i < a.length; i++) {
				String srcImage = webImagePath + String.valueOf(a[i]) + ".png";
				log.info("src:" + srcImage);
				list1.add(srcImage);
			}
		}
		return list1;
	}

	@RequestMapping(value = "/wxSign.do")
	public String wxSign(HttpServletRequest request) {

		try {

			String orderId = StringUtil.nullToString(request.getParameter("orderId"));
			String appId = StringUtil.nullToString(request.getParameter("appId"));
			String ucid = StringUtil.nullToString(request.getParameter("ucid"));
			String sign = StringUtil.nullToString(request.getParameter("sign"));
			String sign_type = StringUtil.nullToString(request.getParameter("sign_type"));
			String time = StringUtil.nullToString(request.getParameter("time"));
			String validType = StringUtil.nullToString(request.getParameter("validType"));
			String param = "orderId:" + orderId + ",ucid:" + ucid + ",appId:" + appId + ",sign:" + sign + ",sign_type:"
					+ sign_type + ",time:" + time;
			log.info("WxSign access parameter:" + param);
			if ("".equals(appId)) {
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
				return "error";
			}
			if ("".equals(ucid)) {
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
				return "error";
			}
			if ("".equals(orderId)) {
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
				return "error";
			}
			ReturnData returnData = contractService.findContract(appId, ucid, orderId);
			if (!ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
				request.setAttribute("error", returnData.getDesc());
				return "error";
			} else {
				ReturnData resData = sealService.querySeal(ConstantParam.OPT_FROM, appId, ucid);
				Gson gson = new Gson();
				JSONObject jsonObj = new JSONObject();
				jsonObj = jsonObj.fromObject(resData.getPojo());
				SealBean seal = new SealBean();
				List<SealBean> lists = str2List(jsonObj.getString("list"), seal);

				ReturnData resDataUser = userService.userQuery(ConstantParam.OPT_FROM, appId, ucid);
				UserBean user = gson.fromJson(resDataUser.getPojo().toString(), UserBean.class);
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
				String signer = (String) contractMap.get("signRecord");
				String optFrom = (String) contractMap.get("optFrom");
				// 若合同已签署完成或已拒绝或已关闭，跳至错误页面
				String status = (String) contractMap.get("status");
				if (status.equals("2")) {
					request.setAttribute("error", "合同已签署完成");
					return "error";
				}
				if (status.equals("3")) {
					request.setAttribute("error", "合同已被拒绝");
					return "error";
				}
				if (status.equals("4")) {
					request.setAttribute("error", "合同已被撤销");
					return "error";
				}
				if (status.equals("5")) {
					request.setAttribute("error", "合同已关闭");
					return "error";
				}
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
				// List<String> imgPath = signService
				// .getImgPath(ConstantParam.CONTRACT_PATH + ruleLocal + "/" +
				// serialNum + "/" + "img/" + attName);

				String listMapAttr = (String) contractMap.get("listMapAttr");
				List<Map> MapAttr = gson.fromJson(listMapAttr, List.class);
				List<String> fjList = null;

				String fjAttName = "";
				String extension = "";
				String filePath = "";
				List<List> afjList = new ArrayList<List>();
				List imgPath = new ArrayList();
				if (optFrom.equals("9")) {

					String tempName = (String) contractMap.get("attName");
					String tempExtension = (String) contractMap.get("extension");
					String tempFilePath = (String) contractMap.get("filePath");
					// List<Map<String, String>> atts =
					// gson.fromJson(contractMap.get("listMapAttr"),
					// List.class);
					// for (Map<String, String> map2 : atts) {
					//
					// String fjName = map2.get("attName");
					// String fjExtension = map2.get("extension");
					// }

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

				// 获取签署固定位置
				String signInfo = signService.querySignInfo(appId, orderId, ucid);

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
				request.setAttribute("ucid", ucid);
				request.setAttribute("appId", appId);
				request.setAttribute("attachmentName", attName);
				log.info("合同图片路径:" + imgPath);
				request.setAttribute("imgPath", imgPath);
				request.setAttribute("validType", validType);
				request.setAttribute("email", email);
				request.setAttribute("fjList", afjList);
				request.setAttribute("signInfo", signInfo);
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return "wxSignature";
	}

	public static <T> List<T> str2List(String str, T obj) {
		JSONArray jsonArray = JSONArray.fromObject(str);
		List<T> lists = (List) JSONArray.toCollection(jsonArray, obj.getClass());
		return lists;
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
		// ��������
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// ����ڲ㻹������Ļ����������?
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

	@RequestMapping(value = "/wx.do")
	public String wx(HttpServletRequest request) {
		return null;
	}
}
