/*
 * 签署类远程接口实现类
 * 
 */
package com.mmec.centerService.contractModule.service;

import java.io.File;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.contractModule.dao.AuthorityContractDao;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.dao.ContractPathDao;
import com.mmec.centerService.contractModule.dao.ExternalDataImportDao;
import com.mmec.centerService.contractModule.dao.PdfInfoDao;
import com.mmec.centerService.contractModule.entity.AuthorityContractEntity;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractPathEntity;
import com.mmec.centerService.contractModule.entity.ExternalDataImportEntity;
import com.mmec.centerService.contractModule.entity.PdfInfoEntity;
import com.mmec.centerService.contractModule.service.impl.BaseContractImpl;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.dao.SealInfoDao;
import com.mmec.centerService.userModule.dao.UserAuthorityDao;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.entity.SealEntity;
import com.mmec.centerService.userModule.entity.UserAuthorityEntity;
import com.mmec.centerService.userModule.service.LogService;
import com.mmec.css.conf.IConf;
import com.mmec.css.security.cert.CertificateCoder;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ContractRMIServices.Iface;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CacheUtil;
import com.mmec.util.ConstantUtil;
import com.mmec.util.DateUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.PDFTool;
import com.mmec.util.PdfUtil;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.StringUtil;
import com.mmec.util.ra.SignField;
import com.mmec.util.ra.SignOnPdfUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.json.JSONObject;

@ContextConfiguration({ "/applicationContext.xml" })
@Component("contractIface")
public class ContractRMIServiceImpl extends BaseContractImpl  implements Iface
{	
	private static Logger  log = Logger.getLogger(ContractRMIServiceImpl.class);
	
	@Autowired
	private SignContractService signContractService;
	
	@Autowired
	private CreateContractService createContractService;
	
	@Autowired
	private PdfInfoDao pdfInfoDao;
	@Autowired
	private InternetFinanceCreate internetFinanceCreate;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private DownloadService downloadService;
	
	@Autowired
	private LogService logService;
	
	@Autowired(required=true)
	private PlatformDao platformDao;
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private SealInfoDao sealInfoDao;
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private UserAuthorityDao userAuthorityDao;
	
	@Autowired
	private ContractPathDao contractPathDao;
	
	@Autowired
	private AuthorityContractDao authorityContractDao;
	
	@Autowired
	private ExternalDataImportDao importDao;
	/**
	 * 互联网金融创建合同方法
	 */
	@Override
	public ReturnData internetFinanceCreate(Map<String, String> datamap)
			throws TException {

		ReturnData returnData = checkMapData(datamap);
		String optType = "internetFinanceCreate";
//		String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
		String tempNumber = StringUtil.nullToString(datamap.get("tempNumber"));
		String attachmentFile = StringUtil.nullToString(datamap.get("attachmentFile"));
		String contractFile = StringUtil.nullToString(datamap.get("contractFile"));
		ServiceException retException = null;
		if(contractRepeatSubmit(datamap))
		{
			returnData.setRetCode(ConstantUtil.RETURN_REPEAT_SUBMIT[0]);
			returnData.setDesc(ConstantUtil.RETURN_REPEAT_SUBMIT[1]);
			returnData.setDescEn(ConstantUtil.RETURN_REPEAT_SUBMIT[2]);
		}
		else
		{
			try {				
				if(!"".equals(tempNumber))
				{
					if(!"".equals(attachmentFile))
					{
						log.info("internetFinanceCreateTempplateAndAttachment");
						returnData = internetFinanceCreate.internetFinanceCreateTempplateAndAttachment(datamap);		
					}
					else
					{
						log.info("internetFinanceCreate");
						returnData = internetFinanceCreate.internetFinanceCreate(datamap);
					}
				}
				else
				{
					log.info("internetFinanceCreateAttachment");
					returnData = internetFinanceCreate.internetFinanceCreateAttachment(datamap);	
				}				
			} catch (ServiceException e) {
				retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
				datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
				datamap.put("contract", "contract");
				returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");		
			}
			finally{
				try {			
					logService.log(datamap,optType, retException, returnData);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}			
		}
		log.info("internetFinanceCreate return data:"+returnData.toString());
		return returnData;
	}
	/**
	 * @param optFrom int 1,云签  2,对接
	 */
	@Override
	public ReturnData createContract(Map<String, String> datamap)
			throws TException {
		log.info("createContract input parameters:"+datamap.toString());
		ReturnData returnData = checkMapData(datamap);
		if(!"0000".equals(returnData.getRetCode()))
		{
			return returnData;
		}
		String optType = "";
		String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
		String tempNumber = StringUtil.nullToString(datamap.get("tempNumber"));
		String attachmentFile = StringUtil.nullToString(datamap.get("attachmentFile"));
		String contractFile = StringUtil.nullToString(datamap.get("contractFile"));
		String author = StringUtil.nullToString(datamap.get("author"));//授权创建合同(对接)
		String draft = StringUtil.nullToString(datamap.get("draft"));//云签草稿箱创建合同
		ServiceException retException = null;
		try{
			if(contractRepeatSubmit(datamap))
			{
				returnData.setRetCode(ConstantUtil.RETURN_REPEAT_SUBMIT[0]);
				returnData.setDesc(ConstantUtil.RETURN_REPEAT_SUBMIT[1]);
				returnData.setDescEn(ConstantUtil.RETURN_REPEAT_SUBMIT[2]);
			}
			else 
			{
				//云签
				if(getOptForm(optFrom) == 1)
				{
					if(!"".equals(tempNumber))
					{
						optType = "templateCreate";
						returnData = createContractService.templateCreate(datamap);	
					}else if("author".equals(author))
					{
						optType = "authorCreate";
						returnData = createContractService.authorCreate(datamap);	
					}
					else if("draft".equals(draft))
					{
						optType = "yunsignDraftCreate";
						returnData = createContractService.draftCreate(datamap);	
					}
					else
					{
						optType = "yunsignCreate";
						returnData = createContractService.yunsignCreate(datamap);	
					}
				}
				else
				{
					if(!"".equals(attachmentFile) && !"".equals(tempNumber))
					{				
						log.info("mmecCreateTempplateAndAttachment");
						optType = "mmecCreateTempplateAndAttachment";
						returnData = createContractService.mmecCreateTempplateAndAttachment(datamap);		
					}
					else if(!"".equals(contractFile))
					{
						log.info("mmecCreateAttachment");
						optType = "mmecCreateAttachment";
						returnData = createContractService.mmecCreateAttachment(datamap);
					}
					else if("author".equals(author))
					{
						optType = "authorCreate";
						returnData = createContractService.authorCreate(datamap);
					}
					else
					{
						log.info("mmecCreate");
						optType = "mmecCreate";
						returnData = createContractService.mmecCreate(datamap);	
					}
				}
			}
		}catch(ServiceException e)
		{
//			CacheManager.getInstance().shutdown();
//			CacheManager.getInstance().clearAll();
//			CacheManager.getInstance().removalAll();
//			CacheManager.getInstance();		
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");		
		}
		finally{
			try {			
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			Cache cache = CacheUtil.mmecCache;
			String checkStr = datamap.get("appId")+"#"+datamap.get("orderId");
			cache.remove(checkStr);
		}
		log.info("createContract return data:"+returnData.toString());
		return returnData;	
	}

	/**
	 * @param optFrom int 1,对接,2 云签	 
	 * @param signMode 
	 * @isPDF Y,N,PDFAUTO,ZIPAUTO
	 */
	@Override
//	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData signContract(Map<String, String> datamap)
			throws TException {
		
		ReturnData returnData = checkMapData(datamap);
		log.info("signContract input parameters"+datamap);
		if(!"0000".equals(returnData.getRetCode()))
		{
			return returnData;
		}
		String optType = "";
		ServiceException retException = null;
		//签署模式(signMode):1,服务器签署 2,事件证书签署 3,硬件证书签署
		String signMode_Str = datamap.get("signMode");
		int signMode = Integer.parseInt(signMode_Str);		
		
		String ucid = StringUtil.nullToString(datamap.get("ucid"));
		String appId = StringUtil.nullToString(datamap.get("appId"));
		String orderId = StringUtil.nullToString(datamap.get("orderId"));
		String isPDF = "";
		
		try {	
			signCheckParam(datamap);
			if(signSynchronousLock(datamap))
			{
				returnData.setRetCode(ConstantUtil.RETURN_REPEAT_SIGN[0]);
				returnData.setDesc(ConstantUtil.RETURN_REPEAT_SIGN[1]);
				returnData.setDescEn(ConstantUtil.RETURN_REPEAT_SIGN[2]);
			}
			else
			{
				//先查看 平台ID是否已经存在
				PlatformEntity platformEntity = null;
				try {
					platformEntity = platformDao.findPlatformByAppId(appId);
				} catch (Exception e) {
					log.info("查询平台表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				//平台ID不存在  抛出异常
				if(null == platformEntity)
				{
					log.info("平台不存在");
					throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
				}
				
				//查询IdentityEntity
				IdentityEntity identity = null;
				try {
					identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
				} catch (Exception e) {
					log.info("查询用户表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null == identity)
				{
					log.info("签署用户不存在");
					throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
				}
				ContractEntity contract = null;
				try {
					contract = contractDao.findContractByAppIAndOrderId(orderId,platformEntity);
				} catch (Exception e) {
					log.info("查询合同表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				
				if(null != contract)
				{
					//云签合同创建方没签署，只能自己查看,其他人不能查看
					if(contract.getOptFrom() == 1 && contract.getStatus() == 0)
					{
						if(contract.getIsShow() == 1 && contract.getCreator() != identity.getId())
						{
							throw new ServiceException(ConstantUtil.IS_SHOW[0],ConstantUtil.IS_SHOW[1],ConstantUtil.IS_SHOW[2]);
						}
					}
					checkSignStatus(contract,ucid);
					if(System.currentTimeMillis() > DateUtil.timeToTimestamp(DateUtil.toDateYYYYMMDDHHMM2(contract.getDeadline())))
					{
						log.info("当前时间大于过期时间");
						//关闭合同
						contractService.closeContract(String.valueOf(identity.getId()), contract.getSerialNum());
						returnData = new ReturnData(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2],"");
						return returnData;
					}
					isPDF = StringUtil.nullToString(contract.getIsPdfSign());
				}
				else
				{
					//合同不存在
					throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
				}
				if(signMode == 1)
				{
					//服务签署
					if("Y".equals(isPDF))
					{
						optType = "serverCertPdfSign";
						returnData = signContractService.serverCertPdfSign(datamap,contract,identity,platformEntity);
					}
					else
					{
						optType = "serverCertZipSign";
						returnData = signContractService.serverCertZipSign(datamap,contract,identity,platformEntity);
					}
				}				
				else if(signMode == 2)
				{
					//事件证书签署
					if("Y".equals(isPDF))
					{
						optType = "eventCertPdfSign";
						returnData = signContractService.eventCertPdfSign(datamap,contract,identity,platformEntity);
					}
					else
					{
						optType = "eventCertZipSign";
						returnData = signContractService.eventCertZipSign(datamap,contract,identity,platformEntity);
					}
				}
				else if(signMode == 3)
				{
					//硬件证书签署 
					if("Y".equals(isPDF))
					{
						optType = "hardCertPdfSign";
						returnData = signContractService.hardCertPdfSign(datamap,contract,identity,platformEntity);
					}
					else
					{
						optType = "hardCertZipSign";
						returnData = signContractService.hardCertZipSign(datamap,contract,identity,platformEntity);
					}
				}
				else
				{
					returnData = new ReturnData(ConstantUtil.SIGN_PARAM_SIGNTYPE[0],ConstantUtil.SIGN_PARAM_SIGNTYPE[1], ConstantUtil.SIGN_PARAM_SIGNTYPE[2], "");
				}
			}
		} catch (ServiceException e) {			
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");			
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			Cache cache = CacheUtil.signCache;
			String checkStr = datamap.get("appId")+"#"+datamap.get("orderId");
			cache.remove(checkStr);
		}
		log.info("signContract return data:"+returnData.toString());
		return returnData;
	}
	
	
/*	*//**
	 * 签署合同设定图章位置
	 *//*
	
	@Override
//	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData signContractByPositionChar(Map<String, String> datamap)
			throws TException {
		
		ReturnData returnData = checkMapData(datamap);
		log.info("signContract input parameters"+datamap);
		if(!"0000".equals(returnData.getRetCode()))
		{
			return returnData;
		}
		String optType = "";
		ServiceException retException = null;
		//签署模式(signMode):1,服务器签署 2,事件证书签署 3,硬件证书签署
		String signMode_Str = datamap.get("signMode");
		int signMode = Integer.parseInt(signMode_Str);		
		
		String ucid = StringUtil.nullToString(datamap.get("ucid"));
		String appId = StringUtil.nullToString(datamap.get("appId"));
		String orderId = StringUtil.nullToString(datamap.get("orderId"));
		String positionChar= StringUtil.nullToString(datamap.get("positionChar"));
		String isPDF = "";
		
		try {	
			signCheckParam(datamap);
			if(signSynchronousLock(datamap))
			{
				returnData.setRetCode(ConstantUtil.RETURN_REPEAT_SIGN[0]);
				returnData.setDesc(ConstantUtil.RETURN_REPEAT_SIGN[1]);
				returnData.setDescEn(ConstantUtil.RETURN_REPEAT_SIGN[2]);
			}
			else
			{
				//先查看 平台ID是否已经存在
				PlatformEntity platformEntity = null;
				try {
					platformEntity = platformDao.findPlatformByAppId(appId);
				} catch (Exception e) {
					log.info("查询平台表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				//平台ID不存在  抛出异常
				if(null == platformEntity)
				{
					log.info("平台不存在");
					throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
				}
				
				//查询IdentityEntity
				IdentityEntity identity = null;
				try {
					identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
				} catch (Exception e) {
					log.info("查询用户表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null == identity)
				{
					log.info("签署用户不存在");
					throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
				}
				ContractEntity contract = null;
				try {
					contract = contractDao.findContractByAppIAndOrderId(orderId,platformEntity);
				} catch (Exception e) {
					log.info("查询合同表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				
				if(null != contract)
				{
					//云签合同创建方没签署，只能自己查看,其他人不能查看
					if(contract.getOptFrom() == 1 && contract.getStatus() == 0)
					{
						if(contract.getIsShow() == 1 && contract.getCreator() != identity.getId())
						{
							throw new ServiceException(ConstantUtil.IS_SHOW[0],ConstantUtil.IS_SHOW[1],ConstantUtil.IS_SHOW[2]);
						}
					}
					checkSignStatus(contract,ucid);
					if(System.currentTimeMillis() > DateUtil.timeToTimestamp(DateUtil.toDateYYYYMMDDHHMM2(contract.getDeadline())))
					{
						log.info("当前时间大于过期时间");
						//关闭合同
						contractService.closeContract(String.valueOf(identity.getId()), contract.getSerialNum());
						returnData = new ReturnData(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2],"");
						return returnData;
					}
					isPDF = StringUtil.nullToString(contract.getIsPdfSign());
				}
				else
				{
					//合同不存在
					throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
				}
				if(signMode == 1)
				{
					//服务签署
					
						optType = "serverCertPdfSign";
						returnData = signContractService.serverCertPdfSign(datamap,contract,identity,platformEntity);
					
					
				}				
				else if(signMode == 2)
				{
					//事件证书签署
						optType = "eventCertPdfSign";
						returnData = signContractService.eventCertPdfSign(datamap,contract,identity,platformEntity);
				}
				else if(signMode == 3)
				{
					//硬件证书签署 
					
						optType = "hardCertPdfSign";
						returnData = signContractService.hardCertPdfSign(datamap,contract,identity,platformEntity);
					
						
				}
				else
				{
					returnData = new ReturnData(ConstantUtil.SIGN_PARAM_SIGNTYPE[0],ConstantUtil.SIGN_PARAM_SIGNTYPE[1], ConstantUtil.SIGN_PARAM_SIGNTYPE[2], "");
				}
			}
		} catch (ServiceException e) {			
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");			
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("signContract return data:"+returnData.toString());
		System.out.println(returnData.toString());
		return returnData;
	}
	
	
	*/
	
	
	
	
	
	/**
	 * 授权代签署合同
	 * @param optFrom int 1,对接,2 云签	 
	 * @param signMode 
	 * @isPDF Y,N,PDFAUTO,ZIPAUTO
	 */
	@Override
	public ReturnData authoritySignContract(Map<String, String> datamap)
			throws TException {
		
		ReturnData returnData = checkMapData(datamap);
		log.info("authoritySignContract input parameters"+datamap);
		if(!"0000".equals(returnData.getRetCode()))
		{
			return returnData;
		}
		String optType = "";
		ServiceException retException = null;
		//签署模式(signMode):1,服务器签署 2,事件证书签署 3,硬件证书签署
		String signMode_Str = datamap.get("signMode");
		int signMode = Integer.parseInt(signMode_Str);		
		
		String ucid = StringUtil.nullToString(datamap.get("ucid"));
		String appId = StringUtil.nullToString(datamap.get("appId"));
		String orderId = StringUtil.nullToString(datamap.get("orderId"));
		String isAuthor = StringUtil.nullToString(datamap.get("isAuthor"));//被授权人(代签人)
		String authorUserId = StringUtil.nullToString(datamap.get("authorUserId"));//授权人(被代签人)
		String isPDF = "";
		
		try {	
			signCheckParam(datamap);
			
			if(signSynchronousLock(datamap))
			{
				returnData.setRetCode(ConstantUtil.RETURN_REPEAT_SIGN[0]);
				returnData.setDesc(ConstantUtil.RETURN_REPEAT_SIGN[1]);
				returnData.setDescEn(ConstantUtil.RETURN_REPEAT_SIGN[2]);
			}
			else
			{
				//先查看 平台ID是否已经存在
				PlatformEntity platformEntity = null;
				try {
					platformEntity = platformDao.findPlatformByAppId(appId);
				} catch (Exception e) {
					log.info("查询平台表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				//平台ID不存在  抛出异常
				if(null == platformEntity)
				{
					log.info("平台不存在");
					throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
				}
				
				//查询IdentityEntity
				IdentityEntity identity = null;//被授权方(代签的人)
				try {
					identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
				} catch (Exception e) {
					log.info("查询用户表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null == identity)
				{
					log.info("签署用户不存在");
					throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
				}
				ContractEntity contract = null;
				try {
					contract = contractDao.findContractByAppIAndOrderId(orderId,platformEntity);
				} catch (Exception e) {
					log.info("查询合同表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				
				if(null != contract)
				{				
					checkAuthoritySignStatus(contract,ucid);
					
					/**
					 * 不是代签署,需要校验是不是在缔约方里
					 */
							
					if("Y".equals(isAuthor))
					{
						//查询签署授权表
						UserAuthorityEntity userAuthorityEntity = null;
						List<UserAuthorityEntity> listUserAuthority= null;
						try {
							listUserAuthority = userAuthorityDao.queryUserAuthoritys(platformEntity.getId(), identity.getId());
							if(null != listUserAuthority && listUserAuthority.size()>0)
							{
								if(listUserAuthority.size()>1)
								{
									IdentityEntity identityAuthor = null;//授权方(被代签的人)
									identityAuthor = identityDao.queryAppIdAndPlatformUserName(platformEntity,authorUserId);
									if(null == identityAuthor)
									{
										log.info("被代签的用户不存在");
										throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+",被代签的用户:"+authorUserId,ConstantUtil.RETURN_USER_NOTEXIST[2]);
									}
									else
									{
										userAuthorityEntity = userAuthorityDao.queryUserAuthorityByAuthorId(platformEntity.getId(), identity.getId(),identityAuthor.getId());
									}
								}
								else
								{
									userAuthorityEntity = listUserAuthority.get(0);
								}								
							}
							else
							{
								throw new ServiceException(ConstantUtil.AUTHOR_NOT_EXIST[0],ConstantUtil.AUTHOR_NOT_EXIST[1], ConstantUtil.AUTHOR_NOT_EXIST[2]);					
							}
							
						} catch (ServiceException e) {
							throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));	
						}catch (Exception e) {
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != userAuthorityEntity)
						{
//							if(contract.getAuthorityContractId() == 0)
//							{
//								contractDao.updataAuthorityContract(userAuthorityEntity.getAuthImgId(), contract.getSerialNum());
//							}
							
							String startTime =StringUtil.nullToString(userAuthorityEntity.getAuthTime().toString()) ;
							String endTime = StringUtil.nullToString(userAuthorityEntity.getAuthEndTime().toString());
							
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							try {
								Date startDate=sdf.parse(startTime);
								Date now =new Date();
								Date endDate=sdf.parse(endTime);
								if(now.compareTo(startDate)==-1 || now.compareTo(endDate)==1){
									throw new ServiceException(ConstantUtil.AUTHOR_OFFTIME[0],ConstantUtil.AUTHOR_OFFTIME[1],ConstantUtil.AUTHOR_OFFTIME[2]);
								}
								
							} catch (ParseException e) {
								e.printStackTrace();
								log.info(FileUtil.getStackTrace(e));
								throw new ServiceException(ConstantUtil.TIME_IS_ILLEGAL[0],ConstantUtil.TIME_IS_ILLEGAL[1],ConstantUtil.TIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
							}
							
							
							//查询授权合同是否生效,没生效则不能代签
							if(userAuthorityEntity.getAuthContractId() != 0)
							{
								ContractEntity tempContract = contractDao.findById(userAuthorityEntity.getAuthContractId());
								if(tempContract.getStatus() != 2)
								{
									throw new ServiceException(ConstantUtil.AUTHOR_FAILURE[0],ConstantUtil.AUTHOR_FAILURE[1],ConstantUtil.AUTHOR_FAILURE[2]);
								}
							}
							//拷贝授权文件
							AuthorityContractEntity authorityContractEntity = authorityContractDao.findById(userAuthorityEntity.getAuthImgId());
							if(null != authorityContractEntity)
							{
								ContractPathEntity contractPath = null;
								try {
									contractPath = contractPathDao.findContractPathByContractId(contract);
								} catch (Exception e) {
									log.info("查询合同附件表异常");
									log.info(FileUtil.getStackTrace(e));
									throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
								}
								String tempPath = contractPath.getContractPath()+"authority";
								
								FileUtil.createDir(tempPath);
								
								File authorityFile = new File(authorityContractEntity.getAuthorityContractPath());
								
								FileUtil.copyFolder(authorityFile,new File(tempPath+"/"+authorityFile.getName()));
							}
							
							
							datamap.put("userId", String.valueOf(userAuthorityEntity.getUserId()));
							datamap.put("author", "author");//添加代理签署标识位							
						}
						else
						{
							throw new ServiceException(ConstantUtil.AUTHOR_NOT_EXIST[0],ConstantUtil.AUTHOR_NOT_EXIST[1], ConstantUtil.AUTHOR_NOT_EXIST[2]);					
						}
					}
					else
					{
						String customId = contract.getOtheruids();
						String [] customIds = customId.split(",");			
						if(!StringUtil.isContain(ucid,customIds))
						{
							log.info("操作人不在缔约方范围内,没有权限操作");
							throw new ServiceException(ConstantUtil.USER_ISNOT_SIGNATORY[0],ConstantUtil.USER_ISNOT_SIGNATORY[1], ConstantUtil.USER_ISNOT_SIGNATORY[2]);
						}
					}
					if(System.currentTimeMillis() > DateUtil.timeToTimestamp(DateUtil.toDateYYYYMMDDHHMM2(contract.getDeadline())))
					{
						log.info("当前时间大于过期时间");
						//关闭合同
						contractService.closeContract(String.valueOf(identity.getId()), contract.getSerialNum());
						returnData = new ReturnData(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2],"");
						return returnData;
					}
					isPDF = StringUtil.nullToString(contract.getIsPdfSign());
				}
				else
				{
					//合同不存在
					throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
				}
				if(signMode == 1)
				{
					//服务签署
					if("Y".equals(isPDF))
					{
						optType = "serverCertPdfSign";
						returnData = signContractService.serverCertPdfSign(datamap,contract,identity,platformEntity);
					}
					else
					{
						optType = "serverCertZipSign";
						returnData = signContractService.serverCertZipSign(datamap,contract,identity,platformEntity);
					}
				}
				
				else if(signMode == 2)
				{
					//事件证书签署
					if("Y".equals(isPDF))
					{
						optType = "eventCertPdfSign";
						returnData = signContractService.eventCertPdfSign(datamap,contract,identity,platformEntity);
					}
					else
					{
						optType = "eventCertZipSign";
						returnData = signContractService.eventCertZipSign(datamap,contract,identity,platformEntity);
					}
				}
				else if(signMode == 3)
				{
					//硬件证书签署 
					if("Y".equals(isPDF))
					{
						optType = "hardCertPdfSign";
						returnData = signContractService.hardCertPdfSign(datamap,contract,identity,platformEntity);
					}
					else
					{
						optType = "hardCertZipSign";
						returnData = signContractService.hardCertZipSign(datamap,contract,identity,platformEntity);
					}
				}
				else
				{
					returnData = new ReturnData(ConstantUtil.SIGN_PARAM_SIGNTYPE[0],ConstantUtil.SIGN_PARAM_SIGNTYPE[1], ConstantUtil.SIGN_PARAM_SIGNTYPE[2], "");
				}
			}
		} catch (ServiceException e) {			
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");			
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			Cache cache = CacheUtil.signCache;
			String checkStr = datamap.get("appId")+"#"+datamap.get("orderId");
			cache.remove(checkStr);
		}
		log.info("signContract return data:"+returnData.toString());
		return returnData;
	}
	//下载合同
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData downLoadContract(Map<String, String> datamap) throws TException
	{		
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("downLoadContract input parameters:"+datamap.toString());
		String optType = "";
		ServiceException retException = null;
		try{
			optType = "contractDownload";
			returnData = downloadService.zipDownload(datamap);	
			return returnData;
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");				
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("downLoadContract return data:"+returnData.toString());
		return returnData;		
	}
	
	//下载合同
	@Override
	public ReturnData pdfDownLoadContract(Map<String, String> datamap) throws TException
	{		
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("pdfDownLoadContract input parameters:"+datamap.toString());
		String optType = "";
		ServiceException retException = null;
		try{
			optType = "pdfDownLoadContract";
			returnData = downloadService.pdfDownload(datamap);	
			return returnData;
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");				
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("pdfDownLoadContract return data:"+returnData.toString());
		return returnData;		
	}
	//签署之前查看合同
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData signQueryContract(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("signQueryContract input parameters:"+datamap.toString());
		String optType = "signQueryContract";
		ServiceException retException = null;
		try{
			returnData = contractService.signQueryContract(datamap);	
			return returnData;
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");					
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("signQueryContract return data:"+returnData.toString());
		return returnData;	
	}
	//查询合同
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData queryContract(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("queryContract input parameters:"+datamap.toString());
		String optType = "queryContract";
		ServiceException retException = null;
		try{
			returnData = contractService.queryContract(datamap);	
			return returnData;
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");					
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("queryContract return data:"+returnData.toString());
		return returnData;	
	}
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData modifyContractStatus(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("modifyContractStatus input parameters:"+datamap.toString());
		String optType = "modifyContractStatus";
		ServiceException retException = null;
		try{
			returnData = contractService.modifyContractStatus(datamap);	
		}catch(ServiceException e){
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");					
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("modifyContractStatus return data:"+returnData.toString());
		return returnData;	
	}
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData deleteContract(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("deleteContract input parameters:"+datamap.toString());
		String optType = "deleteContract";
		ServiceException retException = null;
		try{
			returnData = contractService.deleteContract(datamap);	
			return returnData;
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");		
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("deleteContract return data:"+returnData.toString());
		return returnData;	
	}
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData findContract(Map<String, String> datamap)
			throws TException {
		
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("findContract input parameters:"+datamap.toString());
		String optType = "findContract";
		ServiceException retException = null;
		try{
			returnData = contractService.findContract(datamap);	
			return returnData;
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");					
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("findContract return data:"+returnData.toString());
		return returnData;	
	}
	@Override
	public ReturnData getDraftContractList(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("getDraftContractList input parameters:"+datamap.toString());
		String optType = "getDraftContractList";
		ServiceException retException = null;
		try{
			returnData = contractService.getDraftContractList(datamap);			
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");	
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("getContractList return data:"+returnData.toString());
		return returnData;
	}
	@Override
//	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRES_NEW)
	public ReturnData getContractList(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("getContractList input parameters:"+datamap.toString());
		String optType = "getContractList";
		ServiceException retException = null;
		try{
			returnData = contractService.getContractList(datamap);			
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");	
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("getContractList return data:"+returnData.toString());
		return returnData;
	}
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData getInternetFinanceContractList(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("getInternetFinanceContractList input parameters:"+datamap.toString());
		String optType = "getInternetFinanceContractList";
		ServiceException retException = null;
		try{
			returnData = contractService.getInternetFinanceContractList(datamap);	
			
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");	
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("getInternetFinanceContractList return data:"+returnData.toString());
		return returnData;
	}
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData internetFinanceQueryContract(Map<String, String> datamap)
			throws TException {
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
		}
		log.info("internetFinanceQueryContract input parameters:"+datamap.toString());
		String optType = "internetFinanceQueryContract";
		ServiceException retException = null;
		try{
			returnData = contractService.internetFinanceQueryContract(datamap);	
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");
		
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("internetFinanceQueryContract return data:"+returnData.toString());
		return returnData;	
	}
	// TODO
	@Override
	public ReturnData queryAllYusignTemplate(Map<String, String> datamap)
			throws TException {
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
		}
		log.info("internetFinanceQueryContract input parameters:"+datamap.toString());
		String optType = "internetFinanceQueryContract";
		ServiceException retException = null;
		try{
			returnData = contractService.queryAllYusignTemplate(datamap);	
			return returnData;
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");
		
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("internetFinanceQueryContract return data:"+returnData.toString());
		return returnData;
	}
	// TODO
	@Override
	public ReturnData queryYusignTemplateByKind(Map<String, String> datamap)
			throws TException {
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("queryYusignTemplateByKind input parameters:"+datamap.toString());
		String optType = "queryYusignTemplateByKind";
		ServiceException retException = null;
		try{
			returnData = contractService.queryYusignTemplateByKind(datamap);	
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");
		
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("queryYusignTemplateByKind return data:"+returnData.toString());
		return returnData;
	}
	
	// TODO
		@Override
	public ReturnData protectContract(Map<String, String> datamap) throws TException {
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
		}
		log.info("protectContract input parameters:"+datamap.toString());
		String optType = "protectContract";
		ServiceException retException = null;
		try{
			if(protectSynchronousLock(datamap))
			{
				returnData = new ReturnData();
				returnData.setRetCode(ConstantUtil.RETURN_REPEAT_SIGN[0]);
				returnData.setDesc(ConstantUtil.RETURN_REPEAT_SIGN[1]);
				returnData.setDescEn(ConstantUtil.RETURN_REPEAT_SIGN[2]);
			}
			else
			{
				returnData = contractService.protectContract(datamap);
			}
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");		
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("protectContract return data:"+returnData.toString());
		return returnData;
	}
	@Override
	public ReturnData queryProtectContract(Map<String, String> datamap)
			throws TException {
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
		}
		log.info("queryProtectContract input parameters:"+datamap.toString());
		String optType = "queryProtectContract";
		ServiceException retException = null;
		try{
			returnData = contractService.queryProtectContract(datamap);
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");		
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("queryProtectContract return data:"+returnData.toString());
		return returnData;
	}

	/**
	 * 控制保全同步锁
	 * @param datamap
	 * @return
	 */
	public synchronized boolean protectSynchronousLock(Map<String, String> datamap)
	{
		String checkStr = datamap.get("serialNum");
		CacheUtil cacheUtil = new CacheUtil();
		//判断当前合同是否在保全
		if(null != (cacheUtil.getProtectKey(checkStr)))
		{
			return true;			
		}
		else
		{
			cacheUtil.setProtectKey(checkStr);
			return false;
		}		
	}
	
	@Override
	public ReturnData addPdfInfo(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = new ReturnData();
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("addPdfInfo input parameters:"+datamap.toString());
		String optType = "addPdfInfo";
		ServiceException retException = null;
		try{
			if(contractRepeatSubmit(datamap))
			{
				returnData.setRetCode(ConstantUtil.REPEAT_UPLOAD_EVIDENCE[0]);
				returnData.setDesc(ConstantUtil.REPEAT_UPLOAD_EVIDENCE[1]);
				returnData.setDescEn(ConstantUtil.REPEAT_UPLOAD_EVIDENCE[2]);
			}
			else 
			{
				returnData = contractService.addPdfInfo(datamap);	
			}
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");	
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			/*Cache cache = CacheUtil.mmecCache;
			String checkStr = datamap.get("appId")+"#"+datamap.get("orderId");
			cache.remove(checkStr);*/
		}
		log.info("addPdfInfo return data:"+returnData.toString());
		return returnData;
	}
	
	@Override
	public ReturnData queryPdfInfo(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("queryPdfInfo input parameters:"+datamap.toString());
		String optType = "queryPdfInfo";
		ServiceException retException = null;
		try{
			returnData = contractService.queryPdfInfo(datamap);			
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");	
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("queryPdfInfo return data:"+returnData.toString());
		return returnData;
	}
	
	@Override
	public ReturnData queryPdfInfoByUserId(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("queryPdfInfoByUserId input parameters:"+datamap.toString());
		String optType = "queryPdfInfo";
		ServiceException retException = null;
		try{
			returnData = contractService.queryPdfInfoByUserId(datamap);			
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");	
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("queryPdfInfoByUserId return data:"+returnData.toString());
		return returnData;
	}
	public ReturnData checkMapData(Map<String, String> datamap)
	{
//		log.info("datamap转json:"+JSON.toJSONString(datamap));
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		if(StringUtil.isNull(datamap.get("optFrom")))
		{
			returnData = new ReturnData(ConstantUtil.OPTFROM_PARAMETER[0],ConstantUtil.OPTFROM_PARAMETER[1], ConstantUtil.OPTFROM_PARAMETER[2], "");
			return returnData;
		}
		else
		{
			returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], "");
		}
		return returnData;
	}
	
	@Override
	public ReturnData addSecurity() throws TException {
		ReturnData returnData = null;
		try {
			returnData = contractService.addSecurity();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return returnData;
	}
	
	public synchronized boolean contractRepeatSubmit(Map<String, String> datamap)
	{
		String checkStr = datamap.get("appId")+"#"+datamap.get("orderId");
		//判断是否重复注册
		if(null != (new CacheUtil().get(checkStr)))
		{
			return true;			
		}
		else
		{
			new CacheUtil().set(checkStr);
			return false;
		}		
	}
	/**
	 * 控制签署同步锁
	 * @param datamap
	 * @return
	 */
	public synchronized boolean signSynchronousLock(Map<String, String> datamap)
	{
		String checkStr = datamap.get("appId")+"#"+datamap.get("orderId");
		CacheUtil cacheUtil = new CacheUtil();
//		cacheUtil.getSignAll();
		//判断当前合同是否在签署
		if(null != (cacheUtil.getSignKey(checkStr)))
		{
			return true;			
		}
		else
		{
			cacheUtil.setSignKey(checkStr);
			return false;
		}		
	}
	@Override
//	@Transactional
	public ReturnData saveExternalDataImport(Map<String, String> datamap) throws TException 
	{
		log.info("saveExternalDataImport enter params = "+datamap.toString());
		ReturnData rd = new ReturnData();
		try
		{
			ExternalDataImportEntity edImport=new ExternalDataImportEntity();
			edImport.setContractSha1(datamap.get("contractSha1"));
			if(!"".equals(StringUtil.nullToString(datamap.get("createTime"))))
			{
				edImport.setCreateTime(DateUtil.toDateYYYYMMDDHHMM3(datamap.get("createTime")));
			}
			edImport.setSerialNum(datamap.get("serialNum"));
			edImport.setSignData(datamap.get("signData"));
			edImport.setSignInformation(datamap.get("signInformation"));
			edImport.setSignPlaintext(datamap.get("signPlaintext"));
			edImport.setSignName(datamap.get("signName"));
			if(!"".equals(StringUtil.nullToString(datamap.get("signTime"))))
			{
				edImport.setSignTime(DateUtil.toDateYYYYMMDDHHMM3(datamap.get("signTime")));
			}
			edImport.setTitle(datamap.get("title"));
			edImport.setOrderid(datamap.get("orderid"));
			edImport.setSource(datamap.get("source"));
			edImport.setUpdateTime(new Date());
			importDao.save(edImport);
			/*
			if (null != importDao.save(edImport)) 
			{
				 rd=new ReturnData(ConstantUtil.RETURN_SUCC[0], 
	                     ConstantUtil.RETURN_SUCC[1], 
	                     ConstantUtil.RETURN_SUCC[2], "");
			}else{
				
				rd = new ReturnData(ConstantUtil.RETURN_IMPORT_FAILURE[0], 
	                    ConstantUtil.RETURN_IMPORT_FAILURE[1], 
	                    ConstantUtil.RETURN_IMPORT_FAILURE[2], "");
			}
			*/
			String appId_temp =  StringUtil.nullToString(IConf.getValue("zjns_bank_appId"));
			log.info("appId_temp==="+appId_temp);
			//资金农商银行保全
			if(appId_temp.equals(datamap.get("source")))
			{
				Map<String,String> baoquanMap = new HashMap<String,String>();				
				PlatformEntity platformEntity = null;
				try {
					platformEntity = platformDao.findPlatformByAppId(appId_temp);
				} catch (Exception e) {
					log.info("查询平台表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
	
				//平台ID不存在  抛出异常
				if(null == platformEntity)
				{
					log.info("平台不存在");
					throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
				}
				//查询IdentityEntity
				IdentityEntity identityEntity = null;
				try {
						identityEntity = identityDao.queryChargingIdentityByPlateformId(platformEntity);
				} catch (Exception e) {
					log.info("查询用户表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null == identityEntity)
				{
					log.info("签署用户不存在");
					throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
				}
				
				
				baoquanMap.put("serialNum", datamap.get("serialNum"));
				baoquanMap.put("userId", identityEntity.getPlatformUserName());
				baoquanMap.put("appId",appId_temp);
				
				baoquanMap.put("proTime", "5");
				baoquanMap.put("organization", "石城公证处");
				baoquanMap.put("hashCode", datamap.get("contractSha1"));
				try{
//					contractService.protectContract(baoquanMap);
					rd = contractService.zjnsBankProtectContract(baoquanMap);
				}catch(ServiceException e)
				{
					rd =  new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");
					log.info(FileUtil.getStackTrace(e));
				}
			}
			else
			{
				rd =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],"");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			try {
				log.info(FileUtil.getStackTrace(e));
			} catch (ServiceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		log.info("保全返回结果:"+rd.toString());
		return rd;
	}
	/**
	 * 将签名域和个人信息添加入到pdf内，生成hash返回
	 */
	@Override
	public ReturnData certInfoAppendPdfFile(Map<String, String> datamap) throws TException {
		ReturnData returnData = null;
		//IdentityEntity identity=null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		//根据orderId 去查询合同原路经,得到src
		String orderId=datamap.get("orderId");
		//String appId= datamap.get("appId");
		//String ucid=datamap.get("ucid");
		ContractEntity contract=contractDao.queryContractByid(orderId);
		ContractPathEntity contractpath=contractPathDao.queryContractPathByPath(contract);
		//得到dest
		String contractFolder = contractpath.getContractPath();
		String pdfSignFolder = contractFolder +"pdfsign";
		//根据ucid 去查询图章路径
		List<IdentityEntity> ide= new ArrayList<IdentityEntity>();
		ide= identityDao.queryimg(datamap.get("ucid"));
		
		List<SealEntity> list=new ArrayList<SealEntity>();
		list=sealInfoDao.querySealNum(ide.get(0).getCCompanyInfo().getId());
		String sealPath=list.get(0).getSealPath();
		//String sealNum=list.get(0).getSealNum();
		log.info("filePath:"+contractpath.getFilePath());
		log.info("图章sealPath "+sealPath);
		String filepath = contractpath.getContractPath()+"/" +"pdfsign";
		File pdfSign = new File(filepath);
		if(!pdfSign.exists())
		{
			pdfSign.mkdirs();
		}
		/*PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
		ContractEntity contractentity = contractDao.findContractByAppIAndOrderId(orderId,platformEntity);
		//
		for(IdentityEntity identityEntity:ide){
			identity=identityEntity;
		}
		//创建pdfSign文件 

		try {
			pdfSign( sealNum,  appId, contractentity,identity ,  pdfUIType, specialCharacter, specialCharacterNumber);
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block   ITEXT_SIGN_PDFERROR
			returnData =  new ReturnData(ConstantUtil.ITEXT_SIGN_PDFERROR[0],ConstantUtil.ITEXT_SIGN_PDFERROR[1], ConstantUtil.ITEXT_SIGN_PDFERROR[2],"");
		}*/
		//上海银行修改
		//String pdfSignFolder = contractFolder ;
		File f_src = new File(contractpath.getFilePath());
		String d_fileName=f_src.getName();
		String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
		String dest=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
//		String dest=pdfSignFolder+"/"+20171017+"."+suffix;
		//得到
		String cert=datamap.get("cert");
		//String cert="MIIECDCCAvCgAwIBAgIFEHMiCTYwDQYJKoZIhvcNAQEFBQAwITELMAkGA1UEBhMCQ04xEjAQBgNVBAoTCUNGQ0EgT0NBMTAeFw0xNzAxMDQwODI5MjZaFw0xODAxMDQwODI5MjZaMHUxCzAJBgNVBAYTAkNOMRIwEAYDVQQKEwlDRkNBIE9DQTExETAPBgNVBAsTCG15bWFpbWFpMRUwEwYDVQQLEwxJbmRpdmlkdWFsLTExKDAmBgNVBAMUHzA1MUB4dXlpbkAwMzIwMTA1MTk4NTEwMDIwODExQDEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQChXYMwnA3Rkq9k+5pH3jO5SYN7ScuFXk/kW3Jvva7wv9gMsYOVOtwLAVeXGSjoLDaRZS8FElTaP8j0efnZU8+UeMMBH4RWZPAIugFdaa4ITTmnNTe2rBiNhvIrGBdSBgyhEJIyENnqOxNuxYckjqXDQZKWLUvU/WW94JPcUpps/y+LQDjYQRtSDze0zmTyoZFFDlivD1xi3gwyFudBz2oLQXz4OvFxhWPsD5Qr6KZmzDsZreqk/EhiLb58CHWeFc7SxAp/6aYaW4APWevR9yzkWdZ2jvtkMfLaFCscEDuUIuq76OGWQ1BuvUWUzFxg1kCHIF7p8awd3UpDxHdt/YrDAgMBAAGjgfIwge8wHwYDVR0jBBgwFoAU0dvpiILl3RqPTKoAjL588qsb9tkwSAYDVR0gBEEwPzA9BghggRyG7yoBATAxMC8GCCsGAQUFBwIBFiNodHRwOi8vd3d3LmNmY2EuY29tLmNuL3VzL3VzLTE0Lmh0bTA3BgNVHR8EMDAuMCygKqAohiZodHRwOi8vY3JsLmNmY2EuY29tLmNuL1JTQS9jcmw1NTg2LmNybDALBgNVHQ8EBAMCA+gwHQYDVR0OBBYEFNbr+64S0QGq78gEYs3BLx9Yy+fVMB0GA1UdJQQWMBQGCCsGAQUFBwMCBggrBgEFBQcDBDANBgkqhkiG9w0BAQUFAAOCAQEAYg4ptZvbMGh5PczGVxp6Uu1JADJoWdDm3woD0XU1zT2R9AlHKfiTsLpYD+FW7WfUqOmTw/t9fQubtx9hCfgYzomW5Q9zSaQCsRdq4TvqZHTCtsNlydT1pF0WBMhvbRpdoNWj0jEyCKLVZ8oIYIIA5jDCIKOEjx0STjr4uGcHbGt6Uq0U07si9ayayhw0yoEzeS5RtTo3725BjidoLFKfmmmF7+bu8Sr73Yf9yY6Zuxe1euDZAXwh141h5bQBx2gT1mLGgbvOZ0V8OUU9nEJlph7+Z1QC2Fe6DrjkfAeiohfGlKUooVirJWpKkwIHV8wiHzCzqm0PSnwwLP+ds4B8bg==";
	    X509Certificate x509;
		try {
			x509 = CertificateCoder.getB64toCertificate(cert);
			 Certificate[] chain=new Certificate[1];
			    chain [0]= x509;
				Map<String,Object> map=SignField.addtxt(contractpath.getFilePath(),sealPath, dest, datamap, chain);
				JSONObject json=JSONObject.fromObject(map);
				returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnData;
	}
	/***
	 * 创建文件夹
	 * @param path
	 * @return
	 */
	
	/**
	 * 服务器证书PDF静默签署调用
	 * @param sealNum 图章编号
	 * @param appId 
	 * @param contract 合同对象
	 * @param identity
	 * @throws ServiceException
	 */
	public void pdfSign(String sealNum,String appId,ContractEntity contract,IdentityEntity identity,String pdfUIType,String specialCharacter,String specialCharacterNumber) throws ServiceException
	{
		log.info("pdfSign");	
		try
		{
			//查询附件对原文进行sha1
			ContractPathEntity contractPath = null;
			try {
				contractPath = contractPathDao.findContractPathByContractId(contract);
			} catch (Exception e) {
				log.info("查询合同附件表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == contractPath)
			{
				throw new ServiceException(ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[0],ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[1],ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[2]);
			}
			//源文件地址  服务端定义
//			String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";				
			
			Map<String,String> map=new HashMap<String,String>();
			
			String src_file=contractPath.getFilePath();
			
			
			String dest_file = "";	
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
						
			//签名原文
			map.put("mydata","");//服务器证书签署不要原文,因为已经做了服务器签名
			
			map.put("sign_method_name","server_certinpdf");
	
			
			//根据模板里表示的特殊符号查找坐标
//			PdfInfoEntity pdfinfo = pdfInfoDao.findPdfInfoEntity(contract.getId(), identity.getId());
			PdfUtil.clearArrays();
			List<float[]> listCoordinate = PdfUtil.getKeyWords(src_file,specialCharacter);
			if(null != listCoordinate && listCoordinate.size()>0)
			{
				for(int i=0;i<listCoordinate.size();i++)
				{	
					String[] scNumber=specialCharacterNumber.split(",");
					if (null!=scNumber&&!"".equals(scNumber)) 
					{
					     for (int j = 0; j < scNumber.length; j++)
					     {
					    	 
					    	 if(scNumber[j].equals(String.valueOf(i+1)))
					    	 {
					    		 float [] f_Coordinate = listCoordinate.get(i);
									map.put("x",String.valueOf((int)f_Coordinate[0]));
									map.put("y",String.valueOf((int)f_Coordinate[1]));
									map.put("page", String.valueOf((int)f_Coordinate[2]));
									dest_file=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
									//源文件路径
									map.put("src",src_file);
									//目标文件路径
									map.put("dest",dest_file);		
									float width = 120f,height=120f;
//									float zoom = 1.5f;
									if("1".equals(pdfUIType))//签名域插入图章
									{
										map.put("pdfUIType", pdfUIType);
										//查询图章
										SealEntity sealEntity = null;
										try {
											if(identity.getType() == 1)//个人
											{
												sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCustomInfo().getId(),sealNum);
											}
											else 
											{
												sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCompanyInfo().getId(),sealNum);
											}
										} catch (Exception e) {
											log.info("查询图章异常");
											log.info(FileUtil.getStackTrace(e));
											throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2],FileUtil.getStackTrace(e));
										}
										if(null != sealEntity)
										{
//											Map<String,Integer> mapImg = getImgWidthAndHeight(sealEntity.getCutPath());//获取绝对路径
//											width = mapImg.get("width")/zoom;
//											height = mapImg.get("height")/zoom;
											map.put("imgPath",sealEntity.getCutPath());
										}
										else
										{
											throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
										}
									}
									else if("2".equals(pdfUIType))
									{
										//签名域插入文字
										map.put("text",signInfo(identity));//插入文字信息
										map.put("pdfUIType", pdfUIType);
									}
									else
									{
										//文字和图片
										map.put("pdfUIType", pdfUIType);
										//签名域插入文字
										map.put("text",signInfo(identity));//插入文字信息
										//查询图章
										SealEntity sealEntity = null;
										try {
											if(identity.getType() == 1)//个人
											{
												sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCustomInfo().getId(),sealNum);
											}
											else 
											{
												sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCompanyInfo().getId(),sealNum);
											}
										} catch (Exception e) {
											log.info("查询图章异常");
											log.info(FileUtil.getStackTrace(e));
											throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2],FileUtil.getStackTrace(e));
										}
										if(null != sealEntity)
										{
//											Map<String,Integer> mapImg = getImgWidthAndHeight(sealEntity.getCutPath());//获取绝对路径
//											width = mapImg.get("width")/zoom;
//											height = mapImg.get("height")/zoom;
											map.put("imgPath",sealEntity.getCutPath());
										}
										else
										{
											throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
										}
									}
								
									map.put("height",String.valueOf((int)height));
									map.put("width",String.valueOf((int)width));
									try {
										SignOnPdfUtil.signNew(map);
										//修改原文
									} catch (Exception e) {
											log.info("PDF服务器异常");
											log.info(FileUtil.getStackTrace(e));
											throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
									}
									src_file = dest_file; 
					    	 }
					    	 
					    	    
					     }
					}
				}
			}
			else
			{
				throw new ServiceException(ConstantUtil.PFF_INFO_SPECIAL[0],ConstantUtil.PFF_INFO_SPECIAL[1],ConstantUtil.PFF_INFO_SPECIAL[2]);
			}
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", appId);
			pdfTomImgMap.put("ucid", identity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
		}
	}
	@Override
	public ReturnData updateContractMarkBySerialNum(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ReturnData queryContractMarkBySerialNum(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ReturnData updateContractApproveStatusById(
			Map<String, String> datamap) throws TException {
		return null;
	}
	@Override
	public ReturnData addContractClass(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ReturnData dropContractClass(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ReturnData updateContractClass(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ReturnData queryContractClass(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}
}
