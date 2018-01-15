package com.mmec.business.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.ContractService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.LogUtil;
import com.mmec.util.MD5Util;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

/**
 * 查看类
 * 
 * @author Administrator
 */
@Controller
public class ShowContractController {

	private Logger log = Logger.getLogger(ShowContractController.class);

	LogUtil logUtil = new LogUtil();

	@Autowired
	private ContractService contractService;

	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;
	
	
	/**
	 * 查看合同
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return String
	 * @throws Exception
	 *             Exception
	 */
	@RequestMapping(value = "/showContract.do")
	public String showContract(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String signType = StringUtil.nullToString(request.getParameter("signType"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		
		// 获取客户端请求IP
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);

		Gson gson = new Gson();
		Result result = null;
		HttpSession session = request.getSession();
		///////8.07//////////
		String md5str = appId + "&" + orderId + "&" + time + "&" + userId;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("userId", userId);
		paramMap.put("orderId", orderId);
		paramMap.put("md5str", md5str);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "showContract";

		log.info("appid:" + appId + "time:" + time + "sign:" + sign + "sign_type:" + signType + "userId:" + userId
				+ "orderid:" + orderId);

		try {

			if ("".equals(appId) || null == appId) {
				result = new Result(ErrorData.APPID_IS_NULL, PropertiesUtil.getProperties().readValue("APPID_EMPTY"),
						"");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
			if ("".equals(time) || null == time) {
				result = new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
			if ("".equals(sign) || null == sign) {
				result = new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
			if ("".equals(signType) || null == signType) {
				result = new Result(ErrorData.SIGNTYPE_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
			if ("".equals(userId) || null == userId) {
				result = new Result(ErrorData.USERID_IS_NULL,
						PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
			if ("".equals(orderId) || null == orderId) {
				result = new Result(ErrorData.ORDERID_IS_NULL,
						PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
		
			//md5校验 0不校验，1校验
			String isCheckPlatform ="";
			ReturnData platformData = userService.platformQuery(appId);
			if (platformData!=null&&!"".equals(platformData)) 
			{
				String platformPojo = platformData.pojo;
				Map<String,String> tempMap = gson.fromJson(platformPojo, Map.class);
				if(ConstantParam.CENTER_SUCCESS.equals(platformData.getRetCode()))
				{
					isCheckPlatform = tempMap.get("isCheckPlatform");
				}
			}
			if ("0".equals(isCheckPlatform)) 
			{
				// 判断平台是否有此接口的操作权限	
				Result auth = baseService.checkAuth(appId, 0, "", "", ConstantParam.queryContract);
				if (!auth.getCode().equals(ErrorData.SUCCESS)) {
					returnStr = gson.toJson(auth);
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					request.setAttribute("error", auth.getDesc());
					return "error";
				}
				 
			}
			
			if ("1".equals(isCheckPlatform)) 
			{
				// 判断平台是否有此接口的操作权限	
				Result auth = baseService.checkAuth(appId, Long.valueOf(time), sign, md5str, ConstantParam.queryContract);
				if (!auth.getCode().equals(ErrorData.SUCCESS)) {
					returnStr = gson.toJson(auth);
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					request.setAttribute("error", auth.getDesc());
					return "error";
				}
				
			}
			
				
			List<String> nameList = new ArrayList<String>();

			// 调用中央承载查询合同接口
			ReturnData returnData = contractService.findContract(appId, userId, orderId);
//			if (!(returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS))) {
			if (!(ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode()))) 
			{
				SendDataUtil sendData = new SendDataUtil("InternelRMIServices");
				Map<String,String> map = new HashMap<String,String>();
				map.put("appId",appId);
				ReturnData rd = sendData.upgradeQuery(map);
				if(null != rd)
				{
					String tempPojo = rd.pojo;
					Map<String,String> tempMap = gson.fromJson(tempPojo, Map.class);
					if(ConstantParam.CENTER_SUCCESS.equals(rd.getRetCode()))
					{
						//跳转2.0查询合同接口
						String url = PropertiesUtil.getProperties().readValue("contract.url");
						String appid = tempMap.get("oldAppId");
						String appkey = tempMap.get("oldAppkey");
						String md5Str = appid + "&" + orderId + "&" + time + "&" + userId;
						String md5Str1 = md5Str + "&" + appkey;
						log.info("md5Str1:" + md5Str1);

						String md5 = MD5Util.MD5Encode(md5Str1, "GBK");
						String jumpUrl =  "redirect:" + url + "/showContract.do?appid=" + tempMap.get("oldAppId") + "&ucid=" + userId + "&time=" + time
								+ "&sign=" + md5 + "&sign_type=" + signType + "&orderid=" + orderId;
						System.out.println("jumpUrl==="+jumpUrl);
						return jumpUrl;
					}
					else
					{
						returnStr = gson.toJson(new Result(returnData.getRetCode(), returnData.getDesc(), returnData.getPojo()));
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						request.setAttribute("error", returnData.getDesc());
						return "error";
					}
				}
				else
				{
					returnStr = gson.toJson(new Result(returnData.getRetCode(), returnData.getDesc(), returnData.getPojo()));
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					request.setAttribute("error", returnData.getDesc());
					return "error";
				}

			}
				
			String pojo = returnData.getPojo();
			Map<String, String> contractMap = gson.fromJson(pojo, Map.class);
			String signRecord = contractMap.get("signRecord");
			String optFrom = contractMap.get("optFrom");
			String title = contractMap.get("title");
			List<Map> signList = gson.fromJson(signRecord, List.class);
			List<Map> signAuthorList = new ArrayList<Map>();
			String creator = contractMap.get("creator");
			String finishTime = contractMap.get("finishTime");
			Map<String, String> map = new HashMap();
			Map<String, String> signmap = new HashMap();
			String signerName = "";
			String dSignName = "";
			String signerId = "";
			String signUserType = "";
			String authorId = "";
			String status = contractMap.get("status");
			String operator = contractMap.get("operator");
			String finishtime = contractMap.get("finishtime");
			String attName = contractMap.get("attName");
			String serialNum = contractMap.get("serialNum");
			String createTime = contractMap.get("createTime");
			List<Map> authorList = new ArrayList<Map>();
			int count = 0;
			for (int i = 0; i < signList.size(); i++) {
				map = (Map<String, String>) signList.get(i);
				if(creator.equals(map.get("signerId")))
				{
					count++;
				}
				String bauthorId = map.get("authorId");
				signUserType = map.get("signUserType");
				String signTime = map.get("signTime");
				if (!"0".equals(bauthorId)) {
					Map<String, String> authormap = new HashMap();
					String bsignerId = map.get("signerId");
					authormap.put("bauthorId", bsignerId);
					authormap.put("bsignerId", bauthorId);
					authormap.put("signTime", signTime);
					for (int j = 0; j < signList.size(); j++) {					
						signmap = (Map<String, String>) signList.get(j);
						if (bauthorId.equals(signmap.get("signerId"))) {
							if (signUserType.equals("1")) {
								dSignName = signmap.get("signerName");
							}
							if (signUserType.equals("2")) {
								dSignName = signmap.get("signerCompanyName");
							}
						}
					}
					signAuthorList.add(map);
					authormap.put("bSignName", dSignName);
					authorList.add(authormap);
				}
			}
			//用来判断是否有代签
			String isAuthor = "N";
			if(count>1)
			{
				isAuthor = "Y";
			}
			for (int i = 0; i < signList.size(); i++) {
				signerName = "";
				map = (Map<String, String>) signList.get(i);
				signerId = map.get("signerId");
				signUserType = map.get("signUserType");
				authorId = map.get("authorId");
				if (status.equals("4") && signerId.equals(creator)) {
					map.put("signStatus", "4");
					map.put("signTime", finishtime);
				}
				if (status.equals("3") && signerId.equals(operator)) {
					map.put("signStatus", "3");
					map.put("signTime", finishtime);
				}
				// userList.add(signerId);
				if (signUserType.equals("1")) {
					signerName = map.get("signerName");

				}
				if (signUserType.equals("2")) {
					signerName = map.get("signerCompanyName");
				}
				if (!"0".equals(authorId)) {
					for (int j = 0; j < authorList.size(); j++) {
						Map bmap = (Map<String, String>) authorList.get(j);
						String csignName = (String) bmap.get("bSignName");
						String csignerId = (String) bmap.get("bsignerId");
						String cauthorId = (String) bmap.get("bauthorId");
						if (authorId.equals(csignerId) && signerId.equals(cauthorId)) {
							signerName = signerName + "(" + csignName + "[代签署])";
						}
					}
				}
				nameList.add(signerName);
			}

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date createDate = format.parse(createTime);
			String year = createDate.getYear() + 1900 + "";
			String month = createDate.getMonth() + 1 < 10 ? "0" + (createDate.getMonth() + 1)
					: createDate.getMonth() + 1 + "";
			String timeName = year + month;
			List imageList = new ArrayList();
			String listMapAttr = contractMap.get("listMapAttr");
			List<Map> mapAttr = gson.fromJson(listMapAttr, List.class);
			List<String> fjList = null;

			String fjAttName = "";
			String extension = "";
			String filePath = "";
			List<String> afjList = new ArrayList<String>();
			List<String> videoList = new ArrayList();
			String videoPath = "";
			if (optFrom.equals("9")) {

				String tempName = contractMap.get("attName");
				String tempExtension = contractMap.get("extension");
				String tempFilePath = contractMap.get("filePath");
				// List<Map<String, String>> atts =
				// gson.fromJson(contractMap.get("listMapAttr"), List.class);
				// for (Map<String, String> map2 : atts) {
				//
				// String fjName = map2.get("attName");
				// String fjExtension = map2.get("extension");
				// }

				imageList = this.getPreviewPathOld(tempName, tempExtension, tempFilePath, request, serialNum);
				// 查询附件路径
				for (int i = 0; i < mapAttr.size(); i++) {
					fjList = new ArrayList<String>();
					fjAttName = (String) mapAttr.get(i).get("attName");
					extension = (String) mapAttr.get(i).get("extension");// 附件原始文件后缀名
					filePath = (String) mapAttr.get(i).get("originalPath");// 附件原始文件路径
					fjList = this.getPreviewPathOld(fjAttName, extension, filePath, request, serialNum);
					for (int j = 0; j < fjList.size(); j++) {
						afjList.add(fjList.get(j));
					}
				}
				log.info("附件路径：" + afjList);

			} else {
				imageList = this.getPreviewPath(timeName, "", attName, request, serialNum);

				// 查询附件路径
				for (int i = 0; i < mapAttr.size(); i++) {
					fjList = new ArrayList<String>();
					fjAttName = (String) mapAttr.get(i).get("attName");
					extension = (String) mapAttr.get(i).get("extension");// 附件原始文件后缀名
					filePath = (String) mapAttr.get(i).get("originalPath");// 附件原始文件路径
					if (extension.equals("mp4")) {
						videoPath = this.getVideoPath(filePath, request);
						videoList.add(videoPath);

					} else {
						fjList = this.getPreviewPathFj(timeName, extension, filePath, fjAttName, request, serialNum);
						for (int j = 0; j < fjList.size(); j++) {
							afjList.add(fjList.get(j));
						}
					}
				}
				log.info("附件路径：" + afjList);
				log.info("视频附件路径：" + videoList);
			}

			session.setAttribute("title", title);
			log.info("合同图片路径：" + imageList);
			session.setAttribute("imageList", imageList);
			session.setAttribute("signList", signList);
			session.setAttribute("nameList", nameList);
			session.setAttribute("fjList", afjList);
			session.setAttribute("createTime", createTime);
			session.setAttribute("creator", creator);
			session.setAttribute("status", status);
			session.setAttribute("videoList", videoList);
			session.setAttribute("authorList", authorList);
			session.setAttribute("signAuthorList", signAuthorList);
			session.setAttribute("serialNum", serialNum);
			session.setAttribute("isAuthor", isAuthor);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			returnStr = PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION");
			logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return "error";
		}

		// 对客户端浏览器和操作系统判断
		String ua = request.getHeader("User-Agent");
		if (ua != null) {
			if (ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1
					|| (ua.indexOf("Android") > -1 && ua.indexOf("WebKit") > -1)) {
				return "wxshowContractInfo";
			} else {
				return "viewContents1";
			}
		}
		return "viewContents1";
//		return "queryContract";
	}

	/**
	 * 获取合同图片路径
	 * 
	 * @param timePath
	 *            String
	 * @param path
	 *            String
	 * @param name
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @param num
	 *            String
	 * @return List
	 * @throws Exception
	 *             Exception
	 */
	public List<String> getPreviewPath(String timePath, String path, String name, HttpServletRequest request,
			String num) throws Exception {

		List<String> list = null;
		List<String> list1 = null;
		String contractImagePath = ConstantParam.CONTRACT_PATH + timePath + "/" + num + "/img/" + name + "/";
		// contractImagePath = "G://testP/11";
		log.info("contractImagePath===" + contractImagePath);
		String webImagePath = request.getContextPath() + contractImagePath;
		// webImagePath = request.getContextPath() + "/11/";
		File rootFile = new File(contractImagePath);
		if (!rootFile.exists()) {
			log.error(contractImagePath + " not exists");
			return null;
		}
		File[] subFile = rootFile.listFiles();
		log.info("合同文件夹下图片数目：" + subFile.length);

		File file = null;
		if (null != subFile && subFile.length > 0) {
			list = new ArrayList();
			for (int i = 0; i < subFile.length; i++) {
				file = subFile[i];
				if (file.isFile()) {
					if (file.getName().contains(".png")) {
						String pName = file.getName();
						list.add(pName);
						log.info("合同文件夹下png图片名:" + pName);
					}
				}
			}
			int[] a = new int[list.size()];
			for (int j = 0; j < list.size(); j++) {
				a[j] = Integer.parseInt(list.get(j).substring(0, list.get(j).length() - 4));
				log.info("合同文件夹下png图片去掉后缀名后的文件名:" + a[j]);
			}
			Arrays.sort(a);
			list1 = new ArrayList<String>();
			for (int i = 0; i < a.length; i++) {
				String srcImage = webImagePath + String.valueOf(a[i]) + ".png";
				log.info("srcImage:" + srcImage);
				list1.add(srcImage);
			}
		}
		return list1;
	}

	/**
	 * 获取旧合同图片路径
	 * 
	 * @param tempName
	 *            String
	 * @param tempExtension
	 *            String
	 * @param tempFilePath
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @param num
	 *            String
	 * @return List
	 * @throws Exception
	 *             Exception
	 */
	public List<String> getPreviewPathOld(String tempName, String tempExtension, String tempFilePath,
			HttpServletRequest request, String num) throws Exception {

		List<String> list = null;
		List<String> list1 = null;

		String contractImagePath = ConstantParam.CONTRACT_PATH_OLD + num + "/img/" + tempName + "/";

		if (tempExtension.toLowerCase().equals("jpg") || tempExtension.toLowerCase().equals("jpeg")
				|| tempExtension.toLowerCase().equals("png")) {

			contractImagePath = File.separator + tempFilePath;
			list1 = new ArrayList<String>();

			log.info("contractImagePath===" + contractImagePath);
			String webImagePath = request.getContextPath() + contractImagePath;
			log.info("webImagePath===" + webImagePath);

			list1.add(webImagePath);
		} else {
			log.info("contractImagePath===" + contractImagePath);
			String webImagePath = request.getContextPath() + contractImagePath;
			log.info("webImagePath===" + webImagePath);

			File rootFile = new File(contractImagePath);
			if (!rootFile.exists()) {
				log.error(contractImagePath + " not exists");
				return null;
			}
			File[] subFile = rootFile.listFiles();
			String[] files = new String[subFile.length];
			if (null != subFile) {
				log.info("合同文件夹下图片数目：" + subFile.length);
			}

			File file = null;
			if (null != subFile && subFile.length > 0) {
				list = new ArrayList();
				for (int i = 0; i < subFile.length; i++) {
					file = subFile[i];
					if (file.isFile()) {
						if (file.getName().toLowerCase().contains(".jpg")
								|| file.getName().toLowerCase().contains(".jpeg")
								|| file.getName().toLowerCase().contains(".png")) {
							String pName = file.getName();
							list.add(pName);
							files[i] = pName;
							log.info("合同文件夹下图片名:" + pName);

						}
					}
				}
				int[] a = new int[list.size()];
				for (int j = 0; j < list.size(); j++) {
					a[j] = Integer.parseInt(list.get(j).substring(0, list.get(j).length() - 4));
					log.info("合同文件夹下图片去掉后缀名后的文件名:" + a[j]);
				}
				Arrays.sort(files);
				list1 = new ArrayList<String>();
				for (int i = 0; i < files.length; i++) {

					String srcImage = webImagePath + files[i];
					log.info("srcImage:" + srcImage);
					list1.add(srcImage);
				}
			}
		}

		return list1;
	}

	/**
	 * 获取合同附件图片路径
	 * 
	 * @param timePath
	 *            String
	 * @param extension
	 *            String
	 * @param filePath
	 *            String
	 * @param name
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @param num
	 *            String
	 * @return List
	 * @throws Exception
	 *             Exception
	 */
	public List<String> getPreviewPathFj(String timePath, String extension, String filePath, String name,
			HttpServletRequest request, String num) throws Exception {

		List<String> list = null;
		List<String> list1 = null;
		String contractImagePath = ConstantParam.CONTRACT_PATH + timePath + "/" + num + "/attachment/img/" + name + "/";
		// contractImagePath = "G://testP/11";
		if (extension.toLowerCase().equals("jpg") || extension.toLowerCase().equals("jpeg")
				|| extension.toLowerCase().equals("png")) {
			contractImagePath = filePath;
			list1 = new ArrayList<String>();

			log.info("contractImagePath===" + contractImagePath);
			String webImagePath = request.getContextPath() + contractImagePath;
			log.info("webImagePath===" + webImagePath);
			list1.add(webImagePath);
		} else {

			log.info("contractImagePath===" + contractImagePath);
			String webImagePath = request.getContextPath() + contractImagePath;
			// webImagePath = request.getContextPath() + "/11/";
			File rootFile = new File(contractImagePath);
			if (!rootFile.exists()) {
				log.error(contractImagePath + " not exists");
				return null;
			}
			File[] subFile = rootFile.listFiles();
			if (null != subFile) {
				log.info("合同附件文件夹下图片数目：" + subFile.length);
			}
			File file = null;
			if (null != subFile && subFile.length > 0) {
				list = new ArrayList();
				for (int i = 0; i < subFile.length; i++) {
					file = subFile[i];
					if (file.isFile()) {
						if (file.getName().contains(".png")) {
							String pName = file.getName();
							list.add(pName);
							log.info("合同附件文件夹下png格式的图片文件名:" + pName);
						}
					}
				}
				int[] a = new int[list.size()];
				for (int j = 0; j < list.size(); j++) {
					a[j] = Integer.parseInt(list.get(j).substring(0, list.get(j).length() - 4));
					log.info("合同附件文件夹下图片去掉后缀名的文件名:" + a[j]);
				}
				Arrays.sort(a);
				list1 = new ArrayList<String>();
				for (int i = 0; i < a.length; i++) {
					String srcImage = webImagePath + String.valueOf(a[i]) + ".png";
					log.info("srcImage:" + srcImage);
					list1.add(srcImage);
				}
			}

		}
		return list1;
	}

	public String getVideoPath(String filePath, HttpServletRequest request) {
		String contractVideoPath = filePath;
		log.info("附件视频路径：" + contractVideoPath);
		String webVideoPath = request.getContextPath() + contractVideoPath;
		return webVideoPath;
	}

	/**
	 * 互金查看合同
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return String
	 * @throws Exception
	 *             Exception
	 */
	@RequestMapping(value = "/xtShowContract.do")
	public String xtShowContract(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Gson gson = new Gson();

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();

		}
		log.info("request.getRemoteAddr()，客户端访问的IP地址：" + ip);

		Result result = null;
		HttpSession session = request.getSession();

		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String signType = StringUtil.nullToString(request.getParameter("signType"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String md5Str = appId + "&" + orderId + "&" + time + "&" + userId;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("userId", userId);
		paramMap.put("orderId", orderId);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "xtShowContract";

		log.info("appid:" + appId + "time:" + time + "sign:" + sign + "sign_type:" + signType + "userId:" + userId
				+ "orderid:" + orderId);

		try {

			if ("".equals(appId) || null == appId) {
				result = new Result(ErrorData.APPID_IS_NULL, PropertiesUtil.getProperties().readValue("APPID_EMPTY"),
						"");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
			if ("".equals(time) || null == time) {
				result = new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
			if ("".equals(sign) || null == sign) {
				result = new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
			if ("".equals(signType) || null == signType) {
				result = new Result(ErrorData.SIGNTYPE_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
			if ("".equals(userId) || null == userId) {
				result = new Result(ErrorData.USERID_IS_NULL,
						PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
			if ("".equals(orderId) || null == orderId) {
				result = new Result(ErrorData.ORDERID_IS_NULL,
						PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}

			long time1 = Long.valueOf(time).longValue();
			baseService.checkMd5AndTime(time1, md5Str, appId, sign);
			Map<String, String> contractMap = new HashMap<String, String>();

			List<String> nameList = new ArrayList<String>();

			ReturnData returnData = contractService.xtFindContract(appId, userId, orderId);
			if (!(returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS))) {
				request.setAttribute("error", returnData.getDesc());
				returnStr = gson
						.toJson(new Result(returnData.getRetCode(), returnData.getDesc(), returnData.getPojo()));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return "error";
			}
			String pojo = returnData.getPojo();
			// if(!pojo.equals("")){
			contractMap = gson.fromJson(pojo, Map.class);
			String signRecord = contractMap.get("signRecord");

			String title = contractMap.get("title");
			List signList = gson.fromJson(signRecord, List.class);
			String creator = contractMap.get("creator");
			String finishTime = contractMap.get("finishTime");
			Map<String, String> map = new HashMap();
			String signerName = "";
			String signerId = "";
			String signUserType = "";
			String status = contractMap.get("status");
			String operator = contractMap.get("operator");
			String finishtime = contractMap.get("finishtime");
			String attName = contractMap.get("attName");
			String serialNum = contractMap.get("serialNum");

			String createTime = contractMap.get("createTime");

			for (int i = 0; i < signList.size(); i++) {
				map = (Map<String, String>) signList.get(i);
				signerId = map.get("signerId");
				signUserType = map.get("signUserType");

				if (status.equals("4") && signerId.equals(creator)) {
					map.put("signStatus", "4");
					map.put("signTime", finishtime);
				}
				if (status.equals("3") && signerId.equals(operator)) {
					map.put("signStatus", "3");
					map.put("signTime", finishtime);
				}
				// userList.add(signerId);
				if (signUserType.equals("1")) {
					signerName = map.get("signerName");

				}
				if (signUserType.equals("2")) {
					signerName = map.get("signerCompanyName");
				}
				nameList.add(signerName);
			}

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date createDate = format.parse(createTime);
			String year = createDate.getYear() + 1900 + "";
			String month = createDate.getMonth() + 1 < 10 ? "0" + (createDate.getMonth() + 1)
					: createDate.getMonth() + 1 + "";
			String timeName = year + month;
			List imageList = this.getPreviewPath(timeName, "", attName, request, serialNum);
			// 获取附件路径
			String listMapAttr = contractMap.get("listMapAttr");
			List<Map> mapAttr = gson.fromJson(listMapAttr, List.class);
			List<String> fjList = null;

			String fjAttName = "";
			String extension = "";
			String filePath = "";
			String videoPath = "";
			List videoList = new ArrayList();
			List<String> afjList = new ArrayList<String>();
			for (int i = 0; i < mapAttr.size(); i++) {
				fjList = new ArrayList<String>();
				fjAttName = (String) mapAttr.get(i).get("attName");
				extension = (String) mapAttr.get(i).get("extension");// 附件原始文件后缀名
				filePath = (String) mapAttr.get(i).get("originalPath");// 附件原始文件路径

				if (extension.equals("mp4")) {
					videoPath = this.getVideoPath(filePath, request);
					videoList.add(videoPath);
				} else {
					fjList = this.getPreviewPathFj(timeName, extension, filePath, fjAttName, request, serialNum);
					for (int j = 0; j < fjList.size(); j++) {
						afjList.add(fjList.get(j));
					}
				}
			}

			log.info("附件路径：" + afjList);
			log.info("视频路径：" + videoList);
			session.setAttribute("title", title);
			session.setAttribute("imageList", imageList);
			session.setAttribute("signList", signList);
			session.setAttribute("nameList", nameList);
			session.setAttribute("fjList", afjList);
			session.setAttribute("videoList", videoList);
			session.setAttribute("createTime", createTime);
			session.setAttribute("serialNum", serialNum);

		} catch (Exception e) {
			e.printStackTrace();

			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));

			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			returnStr = PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION");

			logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return "error";
		}
		return "viewContents1";
	}

}
