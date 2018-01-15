package com.mmec.business.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mmec.business.bean.SealBean;
import com.mmec.business.bean.UserBean;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.LogoService;
import com.mmec.business.service.SealService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.DateUtil;
import com.mmec.util.ErrorData;
import com.mmec.util.ImageHelper;
import com.mmec.util.LogUtil;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.RandomUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 * 图章类
 * 
 * @author Administrator
 */
@Controller
public class SealController {

	private static Logger log = Logger.getLogger(SealController.class);

	LogUtil logUtil = new LogUtil();

	@Autowired
	private UserService userService;

	@Autowired
	private SealService sealService;

	@Autowired
	private BaseService baseService;
	
	@Autowired
	LogoService logoService;

	/**
	 * 图章接口
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/seal.do")
	public String sealSet(HttpServletRequest request) {
		try
		{
			log.info("进入sealSet方法，seal.do");
		// 获取客户端请求IP
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);

		Gson gson = new Gson();
		HttpSession session = request.getSession();
		String md5 = StringUtil.nullToString(request.getParameter("sign"));
		String appid = StringUtil.nullToString(request.getParameter("appId"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String platformUserName = StringUtil.nullToString(request.getParameter("userId"));

		log.info("Access SealController.sealSet, Params: appId=" + appid + "; time=" + time + "; sign=" + md5
				+ "; userId=" + platformUserName);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("userId", platformUserName);
		paramMap.put("sign", md5);
		paramMap.put("time", time);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "sealSet";

		if ("".equals(appid)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appid, platformUserName, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(platformUserName)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			logUtil.saveInfoLog(appid, platformUserName, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}

		try {

			UserBean userStaus = (UserBean) request.getSession().getAttribute("user");
			if (userStaus == null) {

				if (!appid.equals(ConstantParam.YUNSIGNAPPID)) {

					// 判断平台是否有此接口的操作权限
					String md5Str = appid + "&" + time + "&" + platformUserName;
					Result auth = baseService.checkAuth(appid, Long.valueOf(time), md5, md5Str,
							ConstantParam.sealManage);
					if (!auth.getCode().equals(ErrorData.SUCCESS)) {
						returnStr = gson.toJson(auth);
						logUtil.saveInfoLog(appid, platformUserName, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						request.setAttribute("error", auth.getDesc());
						return "error";
					}
				}
			}

			ReturnData resDataUser = userService.userQuery(ConstantParam.OPT_FROM, appid, platformUserName);
			if (!ConstantParam.CENTER_SUCCESS.equals(resDataUser.retCode)) {
				returnStr = gson
						.toJson(new Result(resDataUser.getRetCode(), resDataUser.getDesc(), resDataUser.getPojo()));
				logUtil.saveInfoLog(appid, platformUserName, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", resDataUser.desc);
				return "error";
			}
			UserBean user = gson.fromJson(resDataUser.getPojo().toString(), UserBean.class);
			List<SealBean> lists = new ArrayList<SealBean>();

			if (!StringUtil.isNull(user.getUserName())) {
				ReturnData resData = sealService.querySeal(ConstantParam.OPT_FROM, appid, platformUserName);
				if (!ConstantParam.CENTER_SUCCESS.equals(resData.retCode)) {
					returnStr = gson.toJson(new Result(resData.getRetCode(), resData.getDesc(), resData.getPojo()));
					logUtil.saveInfoLog(appid, platformUserName, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					request.setAttribute("error", resDataUser.desc);
					return "error";
				}
				JSONObject jsonObj = new JSONObject();
				jsonObj = jsonObj.fromObject(resData.getPojo());
				SealBean seal = new SealBean();
				lists = str2List(jsonObj.getString("list"), seal);
			}
			Map yhMap = JSON.parseObject(resDataUser.getPojo(), Map.class);
			if (yhMap != null) {
				String type = yhMap.get("type").toString();
				if ("2".equals(type)) {
					String enterprisename = (String) yhMap.get("enterprisename");
					user.setCompanyName(enterprisename);
				}
			}
			String logoPath = logoService.queryLogo(appid);
			log.info("logo:"+logoPath);
			session.setAttribute("user", user);
			session.setAttribute("appid", appid);
			session.setAttribute("md5", md5);
			request.setAttribute("gSealList", lists);
			request.setAttribute("gSize", 1);
			request.setAttribute("platformUserName", platformUserName);
			request.setAttribute("appid", appid);
			request.setAttribute("logoPath", logoPath);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			Map<String, String> errorMap = new HashMap<String, String>();
			returnStr = PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION");
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appid, platformUserName, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return "error";
		}
		// 判断客户端来源是pc端还是移动端
		String ua = request.getHeader("User-Agent");
		if (ua != null) {
			if (ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1
					|| (ua.indexOf("Android") > -1 && ua.indexOf("WebKit") > -1)) {
				return "telSealList";
			} else {
				return "seal";
			}
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "seal";
	}

	public static <T> List<T> str2List(String str, T obj) {
		JSONArray jsonArray = JSONArray.fromObject(str);
		List<T> lists = (List) JSONArray.toCollection(jsonArray, obj.getClass());
		return lists;
	}

	/**
	 * 删除图章
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/delSeal.do")
	public String delSeal(HttpServletRequest request) {

		// 获取客户端请求IP
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);

		Gson gson = new Gson();

		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String appid = (String) request.getSession().getAttribute("appid");
		String sealId = StringUtil.nullToString(request.getParameter("imgid"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("sealId", sealId);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "delSeal";

		if ("".equals(appid)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(sealId)) {
			returnStr = PropertiesUtil.getProperties().readValue("SEAL_EMPTY");
			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SEAL_EMPTY"));
			return "error";
		}
		// 调用中央承载删除图章接口
		ReturnData resData = sealService.delSeal(ConstantParam.OPT_FROM, appid, user.getPlatformUserName(), sealId, ip);
		if (ConstantParam.CENTER_SUCCESS.equals(resData.retCode)) {

			// 保存图章后回调同步图章个数到对方平台
			ReturnData sealRet = sealService.querySeal(ConstantParam.OPT_FROM, appid, user.getPlatformUserName());
			if (sealRet != null && sealRet.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				JSONObject jsonObj = new JSONObject();
				jsonObj = jsonObj.fromObject(sealRet.getPojo());
				List lists = str2List(jsonObj.getString("list"), new SealBean());

				Map<String, String> syncMap = new HashMap<String, String>();
				syncMap.put("userId", user.getPlatformUserName());
				syncMap.put("userType", user.getType());
				syncMap.put("count", String.valueOf(lists.size()));

				log.info("-------------------Start Seal CallBack Process-------------------");
				baseService.syncData(appid, ConstantParam.CALLBACK_NAME_SEAL, ConstantParam.CALLBACK_TYPE_CB, syncMap);
				log.info("-------------------End Seal CallBack Process-------------------");
			}

			return "101";
		} else {
			returnStr = gson.toJson(new Result(resData.getRetCode(), resData.getDesc(), resData.getPojo()));
			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", resData.getDesc());
			return "error";
		}
	}

//	/**
//	 * 保存图章
//	 * 
//	 * @param request
//	 *            HttpServletRequest
//	 * @return String
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/saveImg.do")
//	public String saveImg(HttpServletRequest request) {
//
//		// 获取客户端请求IP
//		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
//		String ip = request.getHeader("x-forwarded-for");
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getRemoteAddr();
//		}
//		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);
//
//		Gson gson = new Gson();
//
//		UserBean u = (UserBean) request.getSession().getAttribute("user");
//		String appid = (String) request.getSession().getAttribute("appid");
//		String sealName = StringUtil.nullToString(request.getParameter("sealName"));
//		String imgfor = StringUtil.nullToString(request.getParameter("imgStr"));
//
//		Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("appId", appid);
//		paramMap.put("sealName", sealName);
//		paramMap.put("imgfor", imgfor);
//		String paramStr = gson.toJson(paramMap);
//
//		String returnStr = "";
//		String methodName = "saveImg";
//
//		if ("".equals(appid)) {
//			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
//			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
//			log.info("returnStr：" + returnStr);
//			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
//			return "error";
//		}
//		if ("".equals(sealName)) {
//			returnStr = PropertiesUtil.getProperties().readValue("SEALNAME_NULL");
//			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
//			log.info("returnStr：" + returnStr);
//			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SEALNAME_NULL"));
//			return "error";
//		}
//		BASE64Decoder decoder = new BASE64Decoder();
//		byte[] imgByte = null;
//		FileOutputStream os = null;
//		if (null != imgfor && !"".equals(imgfor)) {
//			try {
//				imgfor = imgfor.replaceAll(" ", "+");
//				log.info("===imgfor;==" + imgfor);
//				imgByte = decoder.decodeBuffer(imgfor);
//				for (int i = 0; i < imgByte.length; i++) {
//					imgByte[i] += 256;
//				}
//				String imgName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".png";
//				String bathpath = request.getSession().getServletContext().getRealPath(ConstantParam.IMAGE_PATH);
//				// String bathpath = "G://testP";
//				String imgPath = bathpath + File.separator + imgName;
//				os = new FileOutputStream(imgPath);
//				os.write(imgByte);
//				os.flush();
//				String accessImgPath = request.getContextPath() + ConstantParam.IMAGE_PATH + imgName;
//				// String accessImgPath = bathpath + File.separator + imgName;
//				String imgClearBgName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpg";
//				String imgClearBgPath = bathpath + File.separator + imgClearBgName;
//				ImageHelper.clearImgbg(imgPath, imgClearBgPath);
//				String accessclearImgPath = request.getContextPath() + ConstantParam.IMAGE_PATH + imgClearBgName;
//				// String accessclearImgPath = bathpath + File.separator +
//				// imgClearBgName;
//				String zoomImgName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpg";
//				String zoomImgPath = bathpath + File.separator + zoomImgName;
//				ImageHelper.zoomImage(imgClearBgPath, zoomImgPath, 1.5);
//				String imgClearBgName1 = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpg";
//				String imgClearBgPath1 = bathpath + File.separator + imgClearBgName1;
//				// datamap.put("cutPath", imgClearBgPath1);// 必填
//				ImageHelper.clearImgbg(zoomImgPath, imgClearBgPath1);
//				// String accessZoomImg = bathpath + File.separator +
//				// imgClearBgName1;
//				String accessZoomImg = request.getContextPath() + ConstantParam.IMAGE_PATH + imgClearBgName1;
//				String cutpath = ConstantParam.IMAGE_PATH + imgClearBgName1;
//				ReturnData resData = sealService.addSeal(ConstantParam.OPT_FROM, appid, u.getPlatformUserName(),
//						accessImgPath, accessclearImgPath, sealName, u.getType(), accessZoomImg, cutpath, ip);
//				if (ConstantParam.CENTER_SUCCESS.equals(resData.retCode)) {
//
//					// 保存图章后回调同步图章个数到对方平台
//					ReturnData sealRet = sealService.querySeal(ConstantParam.OPT_FROM, appid, u.getPlatformUserName());
//					if (sealRet != null && sealRet.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
//						JSONObject jsonObj = new JSONObject();
//						jsonObj = jsonObj.fromObject(sealRet.getPojo());
//						List lists = str2List(jsonObj.getString("list"), new SealBean());
//
//						Map<String, String> syncMap = new HashMap<String, String>();
//						syncMap.put("userId", u.getPlatformUserName());
//						syncMap.put("userType", u.getType());
//						syncMap.put("count", String.valueOf(lists.size()));
//
//						log.info("-------------------Start Seal CallBack Process-------------------");
//						baseService.syncData(appid, ConstantParam.CALLBACK_NAME_SEAL, ConstantParam.CALLBACK_TYPE_CB,
//								syncMap);
//						log.info("-------------------End Seal CallBack Process-------------------");
//					}
//					return "101";
//				} else {
//					returnStr = gson.toJson(new Result(resData.getRetCode(), resData.getDesc(), resData.getPojo()));
//					logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
//					log.info("returnStr：" + returnStr);
//					request.setAttribute("error", resData.getDesc());
//					return "error";
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				returnStr = ErrorData.SYSTEM_ERROR;
//				Map<String, String> errorMap = new HashMap<String, String>();
//				errorMap.put("errorDesc", ErrorData.SYSTEM_ERROR);
//				errorMap.put("detail", e.getMessage());
//				logUtil.saveErrorLog(appid, "", paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
//				log.info("returnStr：" + returnStr);
//				return ErrorData.SYSTEM_ERROR;
//			}
//		} else {
//			returnStr = PropertiesUtil.getProperties().readValue("NO_SEALINFO");
//			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
//			log.info("returnStr：" + returnStr);
//			request.setAttribute("error", PropertiesUtil.getProperties().readValue("NO_SEALINFO"));
//
//			return "error";
//		}
//	}

	
	/**
	 * 保存图章
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/saveImg.do")
	public String saveImg(HttpServletRequest request) {
		try
		{
		log.info("进入 saveImg.do");
		// 获取客户端请求IP
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);

		Gson gson = new Gson();

		UserBean u = (UserBean) request.getSession().getAttribute("user");
		String appid = (String) request.getSession().getAttribute("appid");
		String sealName = StringUtil.nullToString(request.getParameter("sealname"));
		//横向截取起点
		int xBegin = Integer.parseInt(request.getParameter("x1"));
		//纵向截取起点
		int yBegin = Integer.parseInt(request.getParameter("y1"));
		//横向截取终点
		int xEnd = Integer.parseInt(request.getParameter("x2"));
		//纵向截取终点
		int yEnd = Integer.parseInt(request.getParameter("y2"));
		//截取宽度
		int width = Integer.parseInt(request.getParameter("w"));
		//截取长度
		int height = Integer.parseInt(request.getParameter("h"));
		//缩放比
		double scale = Double.parseDouble(request.getParameter("scale"));
		//自定义图片大小 
		String size = request.getParameter("sealSize");
		int resizeWidth = 0;
		int resizeHeight = 0;
        //合5.8cm*5.8cm
		if("0".equals(size))
		{
			resizeWidth = 165;
			resizeHeight = 165;
		}
        //4.2cm*4.2cm
		else if("1".equals(size))
		{
			resizeWidth = 120;
			resizeHeight = 120;
		}
        //4.5cm*3.0cm
		else if("2".equals(size))
		{
			resizeWidth = 128;
			resizeHeight = 85;
		}
		else
		{
			
		}
		//图片路径
		String sealUploadFilePath = (String)request.getSession().getAttribute("sealUploadFilePath");
		System.out.println("sealUploadFilePath :" + sealUploadFilePath);
		//图片格式
		String sealUploadFileExp = (String)request.getSession().getAttribute("sealUploadFileExp");
		//图片名称
		String sealFileName = (String) request.getSession().getAttribute("sealFileName");

		String dealSealModelPath = sealUploadFilePath.replace("."+sealUploadFileExp, "") ;
		String dealSealModelPath1 = dealSealModelPath + "_1." + sealUploadFileExp;
		
		String dealSealModelPath2 = dealSealModelPath + "_2." + sealUploadFileExp;
		String dealSealModelPath3 = dealSealModelPath + "_3." + sealUploadFileExp;
		String imgClearBgName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + "."+sealUploadFileExp;
//		String accessZoomImg = request.getContextPath() + ConstantParam.IMAGE_PATH + imgClearBgName;
		String accessZoomImg = ConstantParam.IMAGE_PATH + imgClearBgName;
		try
		{
			
			log.info("缩放比例："+scale+",xBegin,yBegin,xEnd,yEnd,kuan,gao:"+xBegin+","+yBegin+","+xEnd+","+yEnd+","+width+","+height);
			//裁剪
			ImageHelper.cutImage(sealUploadFileExp,sealUploadFilePath,dealSealModelPath1,(int)(xBegin/scale),(int)(yBegin/scale),(int)(width/scale),(int)(height/scale));
			//缩放
			ImageHelper.zoomImage(dealSealModelPath1,dealSealModelPath2,scale);
			ImageHelper.clearImgbg(dealSealModelPath2, dealSealModelPath3);
			//clearImgbg(dealSealModelPath2,dealSealModelPath3)
			//dealSealModelPath2
			//重定义大小
			
			ImageHelper.resizeImage(dealSealModelPath3,accessZoomImg,resizeWidth,resizeHeight,true);
		} 
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("sealName", sealName);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "saveImg";

		if ("".equals(appid)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(sealName)) {
			returnStr = PropertiesUtil.getProperties().readValue("SEALNAME_NULL");
			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SEALNAME_NULL"));
			return "error";
		}
		
		
		try {
			String accessImgPath = request.getContextPath() + ConstantParam.IMAGE_PATH + sealFileName;
			String cutpath = ConstantParam.IMAGE_PATH + imgClearBgName;
			ReturnData resData = sealService.addSeal(ConstantParam.OPT_FROM, appid, u.getPlatformUserName(),
					accessImgPath, accessImgPath, sealName, u.getType(), accessZoomImg, cutpath, ip);
			if (ConstantParam.CENTER_SUCCESS.equals(resData.retCode)) {

				// 保存图章后回调同步图章个数到对方平台
				ReturnData sealRet = sealService.querySeal(ConstantParam.OPT_FROM, appid, u.getPlatformUserName());
				if (sealRet != null && sealRet.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
					JSONObject jsonObj = new JSONObject();
					jsonObj = jsonObj.fromObject(sealRet.getPojo());
					List lists = str2List(jsonObj.getString("list"), new SealBean());

					Map<String, String> syncMap = new HashMap<String, String>();
					syncMap.put("userId", u.getPlatformUserName());
					syncMap.put("userType", u.getType());
					syncMap.put("count", String.valueOf(lists.size()));

					log.info("-------------------Start Seal CallBack Process-------------------");
					baseService.syncData(appid, ConstantParam.CALLBACK_NAME_SEAL, ConstantParam.CALLBACK_TYPE_CB,
							syncMap);
					log.info("-------------------End Seal CallBack Process-------------------");
				}
				return "101";
			} else {
				returnStr = gson.toJson(new Result(resData.getRetCode(), resData.getDesc(), resData.getPojo()));
				logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", resData.getDesc());
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = ErrorData.SYSTEM_ERROR;
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", ErrorData.SYSTEM_ERROR);
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appid, "", paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return ErrorData.SYSTEM_ERROR;
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 移动端保存图章
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
//	@ResponseBody
	@RequestMapping(value = "/telSaveImg.do")
	public String telSaveImg(HttpServletRequest request) {
		try
		{
			log.info("移动端保存图章:telSaveImg");
		// 获取客户端请求IP
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);

		Gson gson = new Gson();

		UserBean u = (UserBean) request.getSession().getAttribute("user");
		String appid = (String) request.getSession().getAttribute("appid");
		String sealName = StringUtil.nullToString(request.getParameter("sealName"));
		String filedata = StringUtil.nullToString(request.getParameter("filedata"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("sealName", sealName);
		paramMap.put("filedata", filedata);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "telSaveImg";

		if ("".equals(appid)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(sealName)) {
			returnStr = PropertiesUtil.getProperties().readValue("SEALNAME_NULL");
			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SEALNAME_NULL"));
			return "error";
		}
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] imgByte = null;
		String[] fileList = null;
		FileOutputStream os = null;
		if (null != filedata && !"".equals(filedata)) {
			try {
				fileList = filedata.split(",");
				filedata = fileList[1];
				log.info("===filedata.length()===" + filedata.length());
				imgByte = decoder.decodeBuffer(filedata);
				for (int i = 0; i < imgByte.length; i++) {
					imgByte[i] += 256;
				}
				String imgName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".png";
				String bathpath = request.getSession().getServletContext().getRealPath(ConstantParam.IMAGE_PATH);
				// request.getSession().getServletContext().getRealPath("sharefile/yunsign/image");
				// String bathpath = "G://testP";
				String imgPath = bathpath + File.separator + imgName;
				os = new FileOutputStream(imgPath);
				os.write(imgByte);
				os.flush();
				String accessImgPath = request.getContextPath() + ConstantParam.IMAGE_PATH + imgName;
				// String accessImgPath = bathpath + File.separator + imgName;
				String imgClearBgName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpg";
				String imgClearBgPath = bathpath + File.separator + imgClearBgName;
				ImageHelper.clearImgbg(imgPath, imgClearBgPath);
				String accessclearImgPath = request.getContextPath() + ConstantParam.IMAGE_PATH + imgClearBgName;
				// String accessclearImgPath = bathpath + File.separator +
				// imgClearBgName;
				String zoomImgName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpg";
				String zoomImgPath = bathpath + File.separator + zoomImgName;
				// ImageHelper.zoomImage(imgClearBgPath, zoomImgPath, 1.5);
				String imgClearBgName1 = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpg";
				String imgClearBgPath1 = bathpath + File.separator + imgClearBgName1;
				// datamap.put("cutPath", imgClearBgPath1);// 必填
				ImageHelper.clearImgbg(imgClearBgPath, imgClearBgPath1);
				// String accessZoomImg = bathpath + File.separator +
				// imgClearBgName1;
				String accessZoomImg = request.getContextPath() + ConstantParam.IMAGE_PATH + imgClearBgName1;
				String cutpath = ConstantParam.IMAGE_PATH + imgClearBgName1;
				ReturnData resData = sealService.addSeal(ConstantParam.OPT_FROM, appid, u.getPlatformUserName(),
						accessImgPath, accessclearImgPath, sealName, u.getType(), accessZoomImg, cutpath, ip);
				if (ConstantParam.CENTER_SUCCESS.equals(resData.retCode)) {

					// 保存图章后回调同步图章个数到对方平台
					ReturnData sealRet = sealService.querySeal(ConstantParam.OPT_FROM, appid, u.getPlatformUserName());
					if (sealRet != null && sealRet.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
						JSONObject jsonObj = new JSONObject();
						jsonObj = jsonObj.fromObject(sealRet.getPojo());
						List lists = str2List(jsonObj.getString("list"), new SealBean());

						Map<String, String> syncMap = new HashMap<String, String>();
						syncMap.put("userId", u.getPlatformUserName());
						syncMap.put("userType", u.getType());
						syncMap.put("count", String.valueOf(lists.size()));

						log.info("-------------------Start CallBack Process-------------------");
						baseService.syncData(appid, ConstantParam.CALLBACK_NAME_SEAL, ConstantParam.CALLBACK_TYPE_CB,
								syncMap);
						log.info("-------------------End CallBack Process-------------------");
					}
					String time = new Date().getTime() + "";
					String md5 = (String) request.getSession().getAttribute("md5");

					return "redirect:/seal.do?md5=" + md5 + "&appId=" + appid + "&userId=" + u.getPlatformUserName()
							+ "&time=" + time;

				} else {
					returnStr = gson.toJson(new Result(resData.getRetCode(), resData.getDesc(), resData.getPojo()));
					logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					request.setAttribute("error", resData.getDesc());
					return "error";
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("error", e.getMessage());
				Map<String, String> errorMap = new HashMap<String, String>();
				errorMap.put("errorDesc", e.getMessage());
				errorMap.put("detail", e.getMessage());
				returnStr = e.getMessage();
				logUtil.saveErrorLog(appid, "", paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
				log.info("returnStr：" + returnStr);
				return "error";
			}
		} else {
			returnStr = PropertiesUtil.getProperties().readValue("NO_SEALINFO");
			logUtil.saveInfoLog(appid, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("NO_SEALINFO"));
			return "error";
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) throws IOException
	{
		//-缩放比例：0.35,xBegin,yBegin,xEnd,yEnd,kuan,gao:250,148,463,361,213,213
		double scale=0.35;
		ImageHelper.cutImage("jpg","D:\\GW.jpg","D:\\GW4.jpg",(int)(250/scale),(int)(148/scale),(int)(213/scale),(int)(213/scale));
	}
}
