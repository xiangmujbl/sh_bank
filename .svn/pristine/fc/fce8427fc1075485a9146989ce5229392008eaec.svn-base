package com.mmec.business.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mmec.util.Result;

public interface SignService {
	public Result certSignagin(String appId, String userId, String orderId,
			String cert, String sign, String originalData,
			String certFingerprint, String imageData, String requestIp,String src,String dest);
	public List<String> getImgPath(String path);
	public Result sendSmscode(String mobile, String appid, String ucid,
			String orderId, String requestIp);
	public Result evidenceSendSmscode(String mobile, String appid, String ucid,
			String orderId, String requestIp);
	public String querySignInfo(String appId, String orderId, String userId);

	public Result addSignInfo(String appId, String orderId,
			String positionChar, String signInfo);
	public Result gettxt(String appId, String userId, String orderId,
			 String requestIp,String cert);
	public String signContract(String appId, String userId, String orderId,
			String certType, String sealNum, Map<String, String> codeInfo,
			String requestIp,String isCallBack);

	public String authoritySignContract(String appId, String userId,
			String orderId, String certType, String sealNum,
			Map<String, String> codeInfo, String requestIp, String isAuthor,
			String authorUserId);

	public Result signContractPage(String appId, String userId, String orderId,
			String certType, String imageData, String smsCode, String flag,
			String requestIp);

	public Result checkCode(String code, String orderId);

	public com.mmec.util.Result validateCode(String appId, String orderId,
			String userId, Map<String, String> code);
	public com.mmec.util.Result validateCode1(String appId, String orderId,
			String userId, Map<String, String> code);
	public Result sendEmail(String email, String appId, String ucid,
			String orderId);

	public Result certLogin(String appId, String certNum, String certContent);

	public Result checkCert(String appId, String certNum, String certContent,String userId);
	
	public Result creatSeal(String appId, String userId, String sealName,
			String sealPath, String originalPath, String cutPath,
			String bgRemovedPath, String sealType);

	public Result certSign(String appId, String userId, String orderId,
			String cert, String sign, String originalData,
			String certFingerprint, String imageData, String requestIp);

	public List<String> getImgPath(String timePath, String path, String name,
			HttpServletRequest request, String num);

	public List<String> getOldImgPath(String tempName, String tempExtension,
			String tempFilePath, HttpServletRequest request, String num);

	public List<String> getFjImgPath(String timePath, String extension,
			String filePath, String name, HttpServletRequest request, String num);

}
