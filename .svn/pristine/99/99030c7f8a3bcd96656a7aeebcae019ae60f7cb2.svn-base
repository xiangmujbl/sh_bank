package com.mmec.centerService.contractModule.service.impl;

import java.io.File;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.dao.ContractPathDao;
import com.mmec.centerService.contractModule.dao.ExternalDataImportDao;
import com.mmec.centerService.contractModule.dao.PdfInfoDao;
import com.mmec.centerService.contractModule.dao.ProtectInfoDao;
import com.mmec.centerService.contractModule.dao.SecurityDao;
import com.mmec.centerService.contractModule.dao.SignRecordDao;
import com.mmec.centerService.contractModule.dao.YunsignTemplateDao;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractImgBean;
import com.mmec.centerService.contractModule.entity.ContractPathEntity;
import com.mmec.centerService.contractModule.entity.PdfInfoEntity;
import com.mmec.centerService.contractModule.entity.ProtectInfoEntity;
import com.mmec.centerService.contractModule.entity.SecurityEntity;
import com.mmec.centerService.contractModule.entity.SignRecordEntity;
import com.mmec.centerService.contractModule.entity.YunsignTemplateEntity;
import com.mmec.centerService.contractModule.service.ContractService;
import com.mmec.centerService.contractModule.service.DownloadService;
import com.mmec.centerService.feeModule.service.FeeRMLService;
import com.mmec.centerService.feeModule.service.UserAccountService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.entity.CompanyInfoEntity;
import com.mmec.centerService.userModule.entity.CustomInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.videoModule.dao.VideoSignDao;
import com.mmec.centerService.videoModule.entity.VideoSignEntity;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CertificateCoder;
import com.mmec.util.ConstantUtil;
import com.mmec.util.DateUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.FtpUtil;
import com.mmec.util.PdfUtil;
import com.mmec.util.StringUtil;

import net.sf.json.JSONArray;
@Service("contractService")
public class ContractServiceImpl extends BaseContractImpl implements ContractService {
	
	private Logger log = Logger.getLogger(ContractServiceImpl.class);
	
	@Autowired
	private PlatformDao platformDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractPathDao contractPathDao;
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private SignRecordDao signRecordDao;
	
	@PersistenceContext
	private EntityManager entityManager;	
	
	@Autowired
	private SecurityDao securityDao;
	
	@Autowired
	private YunsignTemplateDao yunsignTemplateDao;
	
	@Autowired
	private DownloadService downloadService;
	
		
	@Autowired
	private PdfInfoDao pdfInfoDao;
	
	@Autowired
	private ProtectInfoDao protectInfoDao;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private FeeRMLService feeRMLService;
	
	@Autowired
	private VideoSignDao videoSignDao;
	
	@Autowired
	private ExternalDataImportDao externalDataImportDao;
	
	@Override
	public ReturnData signQueryContract(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
			if(null == datamap)
			{
				returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1],ConstantUtil.MAP_PARAMETER[2],"");
				return returnData;	
			}
			String appId = datamap.get("appId");
			String orderId = datamap.get("orderId");
			String ucid = datamap.get("ucid");
			String serialNum = datamap.get("serialNum");
			String isAuthor = datamap.get("isAuthor");
			String authorUserId = datamap.get("authorUserId");//被代签人的ucid
			if(StringUtil.isNull(orderId))
			{
				returnData = new ReturnData(ConstantUtil.ORDERID_IS_NULL[0],ConstantUtil.ORDERID_IS_NULL[1],ConstantUtil.ORDERID_IS_NULL[2],"");
				return returnData;
			}
			if(StringUtil.isNull(appId))
			{
				returnData = new ReturnData(ConstantUtil.RETURN_APP_NOT_EXIST[0],ConstantUtil.RETURN_APP_NOT_EXIST[1],ConstantUtil.RETURN_APP_NOT_EXIST[2],"");
				return returnData;
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				returnData =  new ReturnData(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2], "");
				return returnData;
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
			IdentityEntity identityAuthor = null;//授权方(被代签的人)
			if("Y".equals(isAuthor))
			{
				try {					
					identityAuthor = identityDao.queryAppIdAndPlatformUserName(platformEntity,authorUserId);
					if(null == identityAuthor)
					{
						log.info("被代签的用户不存在");
						throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+",被代签的用户:"+authorUserId,ConstantUtil.RETURN_USER_NOTEXIST[2]);
					}																
				} catch (ServiceException e) {
					throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));	
				}catch (Exception e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}			
			}			
			ContractEntity contractEntity = null;
			try{
				if(!"".equals(StringUtil.nullToString(serialNum)))
				{
					contractEntity = contractDao.findContractBySerialNum(serialNum);
				}
				else
				{
					contractEntity = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
				}
				
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == contractEntity)
			{
				log.info("合同不存在");
				returnData =  new ReturnData(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1], ConstantUtil.CONTRACT_IS_NOT_EXISTS[2], "");
				return returnData;
			}
			checkSignStatus(contractEntity, ucid);
			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(contractEntity.getDeadline()+""))
			{
				log.info("合同过期退费");
				/*
				 * 退费逻辑
				 */
				if(contractEntity.getStatus() < 2)
				{
					//修改合同状态					
					contractDao.updataContractStatus(new Date(),String.valueOf(identity.getId()),(byte)5, new Date(), contractEntity.getSerialNum());
					refund(contractEntity.getSerialNum());
				}
				throw new ServiceException(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2]);
			}
			//查询是否签署过	
			SignRecordEntity hasSignRecord = null;
			if("Y".equals(isAuthor))
			{
				hasSignRecord = signRecordDao.findSignRecordByAppIdUcid(contractEntity, identityAuthor);
			}
			else
			{
				hasSignRecord = signRecordDao.findSignRecordByAppIdUcid(contractEntity, identity);
			}

			if(null == hasSignRecord)
			{
				//已经签署过了
				log.info("已经签署过了");
				throw new ServiceException(ConstantUtil.USER_HAS_SIGNED[0],ConstantUtil.USER_HAS_SIGNED[1],ConstantUtil.USER_HAS_SIGNED[2]);
			}			
			returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],"");
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	@Override
	public ReturnData queryContract(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
			if(null == datamap)
			{
				returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1],ConstantUtil.MAP_PARAMETER[2],"");
				return returnData;	
			}
			String appId = datamap.get("appId");
			String orderId = datamap.get("orderId");
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
			String serialNum = datamap.get("serialNum");
			if(StringUtil.isNull(orderId))
			{
				returnData = new ReturnData(ConstantUtil.ORDERID_IS_NULL[0],ConstantUtil.ORDERID_IS_NULL[1],ConstantUtil.ORDERID_IS_NULL[2],"");
				return returnData;
			}
			if(StringUtil.isNull(appId))
			{
				returnData = new ReturnData(ConstantUtil.RETURN_APP_NOT_EXIST[0],ConstantUtil.RETURN_APP_NOT_EXIST[1],ConstantUtil.RETURN_APP_NOT_EXIST[2],"");
				return returnData;
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				returnData =  new ReturnData(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2], "");
				return returnData;
			}
			//查询IdentityEntity
			IdentityEntity identity = new IdentityEntity();
			if(!"".equals(ucid))
			{
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
			}
			ContractEntity contractEntity = null;
			try{
				if(!"".equals(StringUtil.nullToString(serialNum)))
				{
					contractEntity = contractDao.findContractBySerialNum(serialNum);
				}
				else
				{
					contractEntity = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
				}
				//上海银行
				if(contractEntity!=null && !"".equals(contractEntity.getOtheruids())){
					String[] str=contractEntity.getOtheruids().split(",");
					boolean flag=true;
					for(String s:str){
						if(s.equals(ucid)){
							flag=false;
						}
					}
					if(flag){
						log.info("用户 userid 非创建方和签署方,无法查看");
						returnData =  new ReturnData(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],"用户 userid 非创建方和签署方,无法查看", ConstantUtil.CONTRACT_IS_NOT_EXISTS[2], "");
						return returnData;
					}
				}
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == contractEntity)
			{
				log.info("合同不存在");
				returnData =  new ReturnData(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1], ConstantUtil.CONTRACT_IS_NOT_EXISTS[2], "");
				return returnData;
			}
			
			//云签合同创建方没签署，只能自己查看,其他人不能查看
			if(contractEntity.getOptFrom() == 1 && contractEntity.getStatus() == 0)
			{
				if(contractEntity.getIsShow() == 1 && contractEntity.getCreator() != identity.getId())
				{
					throw new ServiceException(ConstantUtil.IS_SHOW[0],ConstantUtil.IS_SHOW[1],ConstantUtil.IS_SHOW[2]);
				}
			}
			/*
			if(contractEntity.getOptFrom() != 9)  
			{
				String customId = contractEntity.getOtheruids();
				String [] customIds = customId.split(",");
				if(!StringUtil.isContain(ucid,customIds))
				{
					log.info("操作人不在缔约方范围内,没有权限操作");
					throw new ServiceException(ConstantUtil.USER_ISNOT_SIGNATORY[0],ConstantUtil.USER_ISNOT_SIGNATORY[1], ConstantUtil.USER_ISNOT_SIGNATORY[2]);
				}
			}
			*/
			
			
//			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(contractEntity.getDeadline()+""))
//			{
//				log.info("合同过期退费");
//				/*
//				 * 退费逻辑
//				 */
//				if(contractEntity.getStatus() < 2)
//				{
//					//修改合同状态
//					contractDao.updataContractStatus(new Date(),String.valueOf(identity.getId()),(byte)5, new Date(), contractEntity.getSerialNum());
//					refund(contractEntity.getSerialNum());
//				}
//			}
			//查询签署记录表
			List<SignRecordEntity> listSignRecord = signRecordDao.querySignRecordByContractId(contractEntity);

			String conStr = "";
			try{
				Map<String, String> pojoMap = pojoMap(contractEntity,listSignRecord);	
				//附件信息表
//					ContractPathEntity contractPath = contractPathDao.findContractPathByContractId(contractEntity);
				List<ContractPathEntity>  listContractPath = contractPathDao.findListContractPathByContractId(contractEntity);
				String attName = "";
				String mainFilePath = "";//主文件路径
				String mainExtension = "";//主文件后缀
				String originalPath = "";
				String originalFileName = "";
				String originalFile = "";//对接或云签传来的文件
				List<String> listAttrName = new ArrayList<String>();
				List<Map<String,String>> listMapAttr = new ArrayList<Map<String,String>>();
				if(null != listContractPath && !listContractPath.isEmpty())
				{

					for(int i = 0;i<listContractPath.size();i++)
					{
						
						ContractPathEntity contractPathEntity = listContractPath.get(i);
						if(contractPathEntity.getType() == 1)
						{
							attName = StringUtil.nullToString(contractPathEntity.getAttName());//主文件名字
							mainFilePath = StringUtil.nullToString(contractPathEntity.getFilePath());
							mainExtension = StringUtil.nullToString(contractPathEntity.getExtension());
							originalPath = StringUtil.nullToString(contractPathEntity.getOriginalFilePath());
							originalFileName = StringUtil.nullToString(contractPathEntity.getOriginalFileName());
							originalFile = StringUtil.nullToString(contractPathEntity.getOriginalFile());
						}
						else
						{
							Map<String,String> map = new HashMap<String,String>();
							listAttrName.add(contractPathEntity.getAttName());//附件名字
							map.put("attName", StringUtil.nullToString(contractPathEntity.getAttName()));
							map.put("filePath", StringUtil.nullToString(contractPathEntity.getFilePath()));
							map.put("extension", StringUtil.nullToString(contractPathEntity.getExtension()));
							map.put("originalPath", StringUtil.nullToString(contractPathEntity.getOriginalFilePath()));
							map.put("originalFileName", StringUtil.nullToString(contractPathEntity.getOriginalFileName()));
							map.put("originalFile", StringUtil.nullToString(contractPathEntity.getOriginalFile()));
							listMapAttr.add(map);
						}
					}
				}
			
				pojoMap.put("attName", attName);//主文件名字
				pojoMap.put("filePath", mainFilePath);
				pojoMap.put("extension", mainExtension);
				pojoMap.put("originalPath", originalPath);
				pojoMap.put("originalFileName", originalFileName);
				pojoMap.put("originalFile", originalFile);
				pojoMap.put("listMapAttr", new Gson().toJson(listMapAttr));
				
				//新增视频查询状态
				if(null == contractEntity.getVideoFlag() || "".equals(contractEntity.getVideoFlag()))
				{
					pojoMap.put("videoFlag", "0");
				}
				else
				{
					pojoMap.put("videoFlag", contractEntity.getVideoFlag());
					//视频状态为已签署的  增加视频签署编码信息
					List<VideoSignEntity> videoList = videoSignDao.findByAppIdAndOrderIdAndStatus(contractEntity.getCPlatform().getAppId(),contractEntity.getOrderId(),0);
					listMapAttr = new ArrayList<Map<String,String>>();
					if(null != videoList && !videoList.isEmpty())
					{
						Map<String,String> map = null;
						for(VideoSignEntity videoEntity : videoList)
						{
							map = new HashMap<String,String>();
							map.put("videoPlatformUserName", videoEntity.getPlatformUserName());
							map.put("videoCode", videoEntity.getVideoCode());
							listMapAttr.add(map);
						}
						pojoMap.put("listVideo", new Gson().toJson(listMapAttr));
					}
				}
				conStr = JSON.toJSONString(pojoMap);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], conStr);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	/**
	 * @param serialNum
	 * 根据合同编号查询合同
	 */
	@Override
	public ReturnData findContract(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
			if(null == datamap)
			{
				returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1],ConstantUtil.MAP_PARAMETER[2],"");
				return returnData;	
			}
			String serialNum = datamap.get("serialNum");
			ContractEntity contractEntity = null;
			try{
				if(!"".equals(StringUtil.nullToString(serialNum)))
				{
					contractEntity = contractDao.findContractBySerialNum(serialNum);
				}
				else
				{
					contractEntity = contractDao.findContractBySerialNum(serialNum);
				}
				
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == contractEntity)
			{
				log.info("合同不存在");
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1], ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
			}
			//查询签署记录表
			List<SignRecordEntity> listSignRecord = signRecordDao.querySignRecordByContractId(contractEntity);

			String conStr = "";
			try{
				Map<String, String> pojoMap = pojoMap(contractEntity,listSignRecord);	
				//附件信息表
//					ContractPathEntity contractPath = contractPathDao.findContractPathByContractId(contractEntity);
				List<ContractPathEntity>  listContractPath = contractPathDao.findListContractPathByContractId(contractEntity);
				String attName = "";
				String mainFilePath = "";//主文件路径
				String mainExtension = "";//主文件后缀
				String originalPath = "";
				String originalFileName = "";
				String originalFile = "";//对接或云签传来的文件
				List<String> listAttrName = new ArrayList<String>();
				List<Map<String,String>> listMapAttr = new ArrayList<Map<String,String>>();
				if(null != listContractPath && !listContractPath.isEmpty())
				{

					for(int i = 0;i<listContractPath.size();i++)
					{
						
						ContractPathEntity contractPathEntity = listContractPath.get(i);
						if(contractPathEntity.getType() == 1)
						{
							attName = StringUtil.nullToString(contractPathEntity.getAttName());//主文件名字
							mainFilePath = StringUtil.nullToString(contractPathEntity.getFilePath());
							mainExtension = StringUtil.nullToString(contractPathEntity.getExtension());
							originalPath = StringUtil.nullToString(contractPathEntity.getOriginalFilePath());
							originalFileName = StringUtil.nullToString(contractPathEntity.getOriginalFileName());
							originalFile = StringUtil.nullToString(contractPathEntity.getOriginalFile());
						}
						else
						{
							Map<String,String> map = new HashMap<String,String>();
							listAttrName.add(contractPathEntity.getAttName());//附件名字
							map.put("attName", StringUtil.nullToString(contractPathEntity.getAttName()));
							map.put("filePath", StringUtil.nullToString(contractPathEntity.getFilePath()));
							map.put("extension", StringUtil.nullToString(contractPathEntity.getExtension()));
							map.put("originalPath", StringUtil.nullToString(contractPathEntity.getOriginalFilePath()));
							map.put("originalFileName", StringUtil.nullToString(contractPathEntity.getOriginalFileName()));
							map.put("originalFile", StringUtil.nullToString(contractPathEntity.getOriginalFile()));
							listMapAttr.add(map);
						}
						
//							pojoMap.put("filePath", contractPathEntity.get);
					}
				}
				pojoMap.put("attName", attName);//主文件名字
				pojoMap.put("filePath", mainFilePath);
				pojoMap.put("extension", mainExtension);
				pojoMap.put("originalPath", originalPath);
				pojoMap.put("originalFileName", originalFileName);
				pojoMap.put("originalFile", originalFile);
				pojoMap.put("listMapAttr", new Gson().toJson(listMapAttr));
				
				//新增视频查询状态
				if(null == contractEntity.getVideoFlag() || "".equals(contractEntity.getVideoFlag()))
				{
					pojoMap.put("videoFlag", "0");
				}
				else
				{
					pojoMap.put("videoFlag", contractEntity.getVideoFlag());
					//视频状态为已签署的  增加视频签署编码信息
					List<VideoSignEntity> videoList = videoSignDao.findByAppIdAndOrderIdAndStatus(contractEntity.getCPlatform().getAppId(),contractEntity.getOrderId(),1);
					listMapAttr = new ArrayList<Map<String,String>>();
					if(null != videoList && !videoList.isEmpty())
					{
						Map<String,String> map = null;
						for(VideoSignEntity videoEntity : videoList)
						{
							map = new HashMap<String,String>();
							map.put("videoPlatformUserName", videoEntity.getPlatformUserName());
							map.put("videoCode", videoEntity.getVideoCode());
							listMapAttr.add(map);
						}
						pojoMap.put("listVideo", new Gson().toJson(listMapAttr));
					}
				}
				conStr = JSON.toJSONString(pojoMap);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], conStr);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	/**
	 * 互联网金融查看接口
	 */
	// TODO
	@Override
	public ReturnData internetFinanceQueryContract(Map<String, String> datamap) throws ServiceException 
	{
		ReturnData returnData = null;
		try
		{
			if(null == datamap)
			{
				throw new ServiceException(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1],ConstantUtil.MAP_PARAMETER[2]);	
			}
			String appId = datamap.get("appId");
			String orderId = datamap.get("orderId");
			String ucid = datamap.get("ucid");
			if(StringUtil.isNull(orderId))
			{
				throw new ServiceException(ConstantUtil.ORDERID_IS_NULL[0],ConstantUtil.ORDERID_IS_NULL[1],ConstantUtil.ORDERID_IS_NULL[2]);
			}
			if(StringUtil.isNull(appId))
			{
				throw new ServiceException(ConstantUtil.RETURN_APP_NOT_EXIST[0],ConstantUtil.RETURN_APP_NOT_EXIST[1],ConstantUtil.RETURN_APP_NOT_EXIST[2]);
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
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
				log.info("查询用户不存在");
				throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
			}
			ContractEntity contractEntity = null;
			try{
				contractEntity = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
				
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == contractEntity)
			{
				log.info("合同不存在");
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1], ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
			}
			/*
			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(contractEntity.getDeadline()+""))
			{
				log.info("合同过期");
				if(contractEntity.getStatus() < 2)
				{
					//修改合同状态
					contractDao.updataContractStatus(new Date(),String.valueOf(identity.getId()),(byte)5, new Date(), contractEntity.getSerialNum());
					// 退费逻辑
					refund(contractEntity.getSerialNum());
				}				
				throw new ServiceException(ConstantUtil.CONTRACT_IS_OUTOFDATE[0],ConstantUtil.CONTRACT_IS_OUTOFDATE[1], ConstantUtil.CONTRACT_IS_OUTOFDATE[2]);
			}
			*/
			//查询签署记录表
			List<SignRecordEntity> listSignRecord = signRecordDao.querySignRecordByContractId(contractEntity);
			String conStr = "";
			try{
				Map<String, String> pojoMap = pojoMap(contractEntity,listSignRecord);	
				//附件信息表
//				ContractPathEntity contractPath = contractPathDao.findContractPathByContractId(contractEntity);
				List<ContractPathEntity>  listContractPath = contractPathDao.findListContractPathByContractId(contractEntity);
				String attName = "";
				String mainFilePath = "";//主文件路径
				String mainExtension = "";//主文件后缀
				String originalPath = "";
				List<String> listAttrName = new ArrayList<String>();
				List<Map<String,String>> listMapAttr = new ArrayList<Map<String,String>>();
				if(null != listContractPath && !listContractPath.isEmpty())
				{

					for(int i = 0;i<listContractPath.size();i++)
					{
						
						ContractPathEntity contractPathEntity = listContractPath.get(i);
						if(contractPathEntity.getType() == 1)
						{
							attName = contractPathEntity.getAttName();//主文件名字
							mainFilePath = contractPathEntity.getFilePath();
							mainExtension = contractPathEntity.getExtension();
							originalPath = contractPathEntity.getOriginalFilePath();
						}
						else
						{
							Map<String,String> map = new HashMap<String,String>();
							listAttrName.add(contractPathEntity.getAttName());//附件名字
							map.put("attName", contractPathEntity.getAttName());
							map.put("filePath", contractPathEntity.getFilePath());
							map.put("extension", contractPathEntity.getExtension());
							map.put("originalPath", contractPathEntity.getOriginalFilePath());
							listMapAttr.add(map);
						}
						
//						pojoMap.put("filePath", contractPathEntity.get);
					}
				}
				
				pojoMap.put("attName", attName);//主文件名字
				pojoMap.put("filePath", mainFilePath);
				pojoMap.put("extension", mainExtension);
				pojoMap.put("originalPath", originalPath);
				pojoMap.put("listMapAttr", new Gson().toJson(listMapAttr));
				
				//新增视频查询状态
				if(null == contractEntity.getVideoFlag() || "".equals(contractEntity.getVideoFlag()))
				{
					pojoMap.put("videoFlag", "0");
				}
				else
				{
					pojoMap.put("videoFlag", contractEntity.getVideoFlag());
					//视频状态为已签署的  增加视频签署编码信息
					List<VideoSignEntity> videoList = videoSignDao.findByAppIdAndOrderIdAndStatus(contractEntity.getCPlatform().getAppId(),contractEntity.getOrderId(),1);
					listMapAttr = new ArrayList<Map<String,String>>();
					if(null != videoList && !videoList.isEmpty())
					{
						Map<String,String> map = null;
						for(VideoSignEntity videoEntity : videoList)
						{
							map = new HashMap<String,String>();
							map.put("videoPlatformUserName", videoEntity.getPlatformUserName());
							map.put("videoCode", videoEntity.getVideoCode());
							listMapAttr.add(map);
						}
						pojoMap.put("listVideo", new Gson().toJson(listMapAttr));
					}
				}
				conStr = JSON.toJSONString(pojoMap);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], conStr);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	//TODO
	public Map<String,String> pojoMap(ContractEntity contract,List<SignRecordEntity> listSignRecord)
	{
		Map<String, String> pojoMap = new HashMap<String, String>();
		pojoMap.put("Id", String.valueOf(contract.getId()));
		pojoMap.put("contractType", contract.getContractType());
		pojoMap.put("createTime", DateUtil.toDateYYYYMMDDHHMM2(contract.getCreateTime()));
		pojoMap.put("creator", String.valueOf(contract.getCreator()));
		pojoMap.put("optFrom", String.valueOf(contract.getOptFrom()));
		//查询创建人姓名
		String creatorCompanyName = "";
		String creatorUserName = "";
		String creatorEmail = "";
		String creatorMobileNum = "";
		String creatorCardId = "";
		String creatorBusinessLicenseNo = "";
		IdentityEntity identityCreator = identityDao.findById(contract.getCreator());
		if(identityCreator !=null)
		{
			pojoMap.put("creatorPlatformUserName", identityCreator.getPlatformUserName());
			pojoMap.put("creatorUserType", String.valueOf(identityCreator.getType()));
			CustomInfoEntity creatorCustomInfo = identityCreator.getCCustomInfo();
			CompanyInfoEntity creatorCompanyInfo = identityCreator.getCCompanyInfo();
			
//			if(identityCreator.getType() == 1)//个人
//			{
				if(creatorCustomInfo != null)
				{
					creatorUserName = StringUtil.nullToString(creatorCustomInfo.getUserName());
					creatorCardId = StringUtil.nullToString(creatorCustomInfo.getIdentityCard());
				}
				
//			}
//			if(identityCreator.getType() == 2)
//			{
				if(creatorCompanyInfo != null)
				{
					creatorCompanyName = StringUtil.nullToString(creatorCompanyInfo.getCompanyName());
					creatorBusinessLicenseNo = StringUtil.nullToString(creatorCompanyInfo.getBusinessLicenseNo());
					
				}
//			}
			
			creatorEmail = StringUtil.nullToString(identityCreator.getEmail());
			creatorMobileNum = StringUtil.nullToString(identityCreator.getMobile());
			
			
		}
		pojoMap.put("creatorCompanyName", creatorCompanyName);
		pojoMap.put("creatorUserName", creatorUserName);
		
		pojoMap.put("creatorEmail", creatorEmail);
		pojoMap.put("creatorMobileNum", creatorMobileNum);
		pojoMap.put("creatorCardId", creatorCardId);
		pojoMap.put("creatorBusinessLicenseNo", creatorBusinessLicenseNo);
		
		
		
		if(null != contract.getDeadline())
		{
			pojoMap.put("deadline", DateUtil.toDateYYYYMMDDHHMM2(contract.getDeadline()));
		}
		else
		{
			pojoMap.put("deadline", "");
		}
		if(null != contract.getEndTime())
		{
			pojoMap.put("endTime", DateUtil.toDateYYYYMMDDHHMM2(contract.getEndTime()));
		}
		else
		{
			pojoMap.put("endTime", "");
		}
		if(null != contract.getEndTime())
		{
			pojoMap.put("finishtime", DateUtil.toDateYYYYMMDDHHMM2(contract.getEndTime()));
		}
		else
		{
			pojoMap.put("finishtime", "");
		}
		pojoMap.put("keyword", contract.getKeyword());
		pojoMap.put("optFrom", String.valueOf(contract.getOptFrom()));
		pojoMap.put("operator", contract.getOperator());
		pojoMap.put("paymentType", String.valueOf(contract.getPaymentType()));
		pojoMap.put("pname", contract.getPname());
		pojoMap.put("serialNum", contract.getSerialNum());
		pojoMap.put("signPlaintext", contract.getSignPlaintext());
		if(contract.getStartTime() != null)
		{
			pojoMap.put("startTime", DateUtil.toDateYYYYMMDDHHMM2(contract.getStartTime()));
		}
		else
		{
			pojoMap.put("startTime", "");
		}
		pojoMap.put("status", String.valueOf(contract.getStatus()));
		pojoMap.put("title", contract.getTitle());
		pojoMap.put("remark", contract.getMark());
		pojoMap.put("type", contract.getType());
		pojoMap.put("orderId", contract.getOrderId());
		pojoMap.put("price", String.valueOf(contract.getPrice()));
		pojoMap.put("isDelete", String.valueOf(contract.getIsDelete()));
		pojoMap.put("parentorderid", String.valueOf(contract.getParentContractId()));
		PlatformEntity platformEntity = contract.getCPlatform();
		String appId = "";
		String appName = "";
		if(null != platformEntity)
		{
			appId = platformEntity.getAppId();
			appName = platformEntity.getProgram();	
		}
		pojoMap.put("appId", appId);
		pojoMap.put("appName", appName);
		
		String jsonRecord = "";
		String creatorBindedId = "0";
		//签署记录
		if(null != listSignRecord && listSignRecord.size()>0)
		{
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();			
			for(int i=0;i<listSignRecord.size();i++)
			{
				Map<String,String> signMap = new HashMap<String,String>();
				SignRecordEntity signRecord = listSignRecord.get(i);
				//查询签署人
				IdentityEntity identity = signRecord.getCIdentity();				
				String userName = "";
				String signerName = "";
				String companyName = "";
				String email = "";
				String mobileNum = "";
				String mobile = "";
				String cardId = "";
				String businessLicenseNo = "";
				
				
				if(null != identity)
				{
					CustomInfoEntity customInfo = identity.getCCustomInfo();
					CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
					email = StringUtil.nullToString(identity.getEmail());
					mobile = StringUtil.nullToString(identity.getMobile());
					mobileNum = StringUtil.nullToString(identity.getMobile());
					
//					if(identity.getIsAuthentic() == 0 && identity.getSource() ==1)//云签未实名认证
//					{
//						if(identity.getType() ==1)
//						{
//							userName = identity.getMobile();//未实名认证个人用户显示手机号码
//							
//						}else if(identity.getType() ==2)
//						{
//							userName = identity.getEmail();//未实名认证企业用户显示邮箱
//						}
//					}
//					else
//					{
//						if(identity.getType() ==1)//
//						{
//							if(null != customInfo)
//							{
//								userName = customInfo.getUserName();//姓名
//								mobileNum = customInfo.getPhoneNum();
//							}
//						}
//						else if(identity.getType() ==2)
//						{
////							CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
//							if(null != companyInfo)
//							{
//								userName = companyInfo.getCompanyName();//公司名
//								companyName = companyInfo.getCompanyName();
//							}
//						}
//					}
					if(null != customInfo)
					{
						signerName = StringUtil.nullToString(customInfo.getUserName());
					}
					//个人用户名字不为空则显示名字，否则显示手机号码,企业用户公司名为空则显示邮箱
					if(identity.getType() ==1)
					{
						if(null != customInfo && !"".equals(StringUtil.nullToString(customInfo.getUserName())))
						{
							userName = StringUtil.nullToString(customInfo.getUserName());//姓名
						}
						else
						{
							userName = StringUtil.nullToString(identity.getMobile());//显示电话号码
						}
					}
					else if(identity.getType() ==2)
					{
						if(null != companyInfo && !"".equals(StringUtil.nullToString(companyInfo.getCompanyName())))
						{
							userName = StringUtil.nullToString(companyInfo.getCompanyName());//公司名
							companyName = StringUtil.nullToString(companyInfo.getCompanyName());
						}
						else
						{
							userName = StringUtil.nullToString(identity.getEmail());
						}
					}
					
					if(null != customInfo)
					{						
						cardId = StringUtil.nullToString(customInfo.getIdentityCard());
					}
					if(null != companyInfo)
					{
						businessLicenseNo = StringUtil.nullToString(companyInfo.getBusinessLicenseNo());
					}
					
					
					
					if(contract.getCreator() == identity.getId())
					{
						creatorBindedId = StringUtil.nullToString(String.valueOf(identity.getBindedId()));
//						signMap.put("creatorBindedId", StringUtil.nullToString(String.valueOf(identity.getBindedId())));
					}
					signMap.put("mobile", StringUtil.nullToString(mobile));
					signMap.put("bindedId", StringUtil.nullToString(String.valueOf(identity.getBindedId())));
					signMap.put("signUserType", String.valueOf(identity.getType()));
					signMap.put("signMode", String.valueOf(signRecord.getSignMode()));
					signMap.put("signerId", String.valueOf(identity.getId()));
					signMap.put("plateformUserName", identity.getPlatformUserName());
					signMap.put("signStatus", String.valueOf(signRecord.getSignStatus()));
					if(null != signRecord.getSignTime())
					{
						signMap.put("signTime", DateUtil.toDateYYYYMMDDHHMM2(signRecord.getSignTime()));
					}
					else
					{
						signMap.put("signTime", "");
					}
					signMap.put("signerName", userName);
					signMap.put("signerName2", signerName);
					signMap.put("email", email);
					signMap.put("signerCompanyName", companyName);
					signMap.put("mobileNum", mobileNum);
					signMap.put("authorId", String.valueOf(signRecord.getAuthorId()));
					signMap.put("remark", signRecord.getMark());
					signMap.put("cardId", cardId);
					signMap.put("businessLicenseNo", businessLicenseNo);
				}
				list.add(signMap);
			}
			Gson gson = new Gson();
//			jsonRecord = JSON.toJSONString(list);
			jsonRecord = gson.toJson(list);
			log.info("jsonRecord==="+jsonRecord);
		}
		pojoMap.put("creatorBindedId",creatorBindedId);
		pojoMap.put("signRecord", jsonRecord);
		return pojoMap;
	}
	/*
	 *拒绝和撤销接口
	 */
	//TODO
	@Override
	public ReturnData modifyContractStatus(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
			String orderId = datamap.get("orderId");
			String appId = datamap.get("appId");
			String ucid = datamap.get("ucid");
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
			int status = 0;
			if(null != contract)
			{
				if(contract.getOptFrom() == 1 && contract.getStatus() == 0)
				{
					if(contract.getIsShow() == 1 && contract.getCreator() != identity.getId())
					{
						throw new ServiceException(ConstantUtil.IS_SHOW[0],ConstantUtil.IS_SHOW[1],ConstantUtil.IS_SHOW[2]);
					}
				}
				if(contract.getStatus() == 5)
				{
					throw new ServiceException(ConstantUtil.CONTRACT_HAS_CLOSE[0],ConstantUtil.CONTRACT_HAS_CLOSE[1],ConstantUtil.CONTRACT_HAS_CLOSE[2]);
				}
				if(System.currentTimeMillis() > DateUtil.timeToTimestamp(contract.getDeadline()+""))
				{
					log.info("合同过期退费");
					/*
					 * 退费逻辑
					 */
					if(contract.getStatus() < 2)
					{
						//修改合同状态
						contractDao.updataContractStatus(new Date(),String.valueOf(identity.getId()),(byte)5, new Date(), contract.getSerialNum());
						refund(contract.getSerialNum());
					}
					throw new ServiceException(ConstantUtil.CONTRACT_IS_OUTOFDATE[0],ConstantUtil.CONTRACT_IS_OUTOFDATE[1],ConstantUtil.CONTRACT_IS_OUTOFDATE[2]);
				}
				if(contract.getStatus() == 1 || contract.getStatus() == 0)
				{				
					//判断缔约方有重复值
					String customId = contract.getOtheruids();
					String [] customIds = customId.split(",");
					if(!StringUtil.isContain(ucid,customIds))
					{
						log.info("操作人不在缔约方范围内,没有权限操作");
						throw new ServiceException(ConstantUtil.USER_ISNOT_SIGNATORY[0],ConstantUtil.USER_ISNOT_SIGNATORY[1], ConstantUtil.USER_ISNOT_SIGNATORY[2]);
					}			
					
					String serialNum = contract.getSerialNum();
					int update = 0;
					if(contract.getCreator() == identity.getId())
					{
						status = 4;//撤销
					}
					else
					{
						status = 3;//拒绝
					}
					try{
						update = contractDao.updataContractStatus(new Date(),String.valueOf(identity.getId()),(byte)status, new Date(), serialNum);
					}catch (Exception e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
					}			
					
					if(update>0)
					{
						//退费
						refund(serialNum);
						//更新签署记录表中的signStatus
						signRecordDao.updateSignRecordStatus(new Date(), (byte)status, contract, identity);
						returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], "");
					}
					else
					{
						returnData = new ReturnData(ConstantUtil.MODIFY_CONTRACT_STATUS[0],ConstantUtil.MODIFY_CONTRACT_STATUS[1], ConstantUtil.MODIFY_CONTRACT_STATUS[2], "");
					}
				}
				else
				{
					throw new ServiceException(ConstantUtil.REVOKE_REFUSE[0],ConstantUtil.REVOKE_REFUSE[1],ConstantUtil.REVOKE_REFUSE[2]);
				}			
			}
			else
			{
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn());
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2]);
		}
		return returnData;		
	}
	
	//删除合同
	//TODO
	public ReturnData deleteContract(Map<String, String> datamap) throws ServiceException
	{
		ReturnData returnData = null;
		try
		{
			String orderId = datamap.get("orderId");
			String appId = datamap.get("appId");
			String ucid = datamap.get("ucid");
			String isDelete = datamap.get("isDelete");
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
			int status = 0;
			if(null != contract)
			{			
				//判断缔约方有重复值
				String customId = contract.getOtheruids();
				String [] customIds = customId.split(",");
				if(!StringUtil.isContain(ucid,customIds))
				{
					log.info("操作人不在缔约方范围内,没有权限操作");
					throw new ServiceException(ConstantUtil.USER_ISNOT_SIGNATORY[0],ConstantUtil.USER_ISNOT_SIGNATORY[1], ConstantUtil.USER_ISNOT_SIGNATORY[2]);
				}			
				
				int update = 0;
				try{
					update = contractDao.deleteContract(new Date(),Byte.valueOf(isDelete),contract.getId());
				}catch (Exception e) {
					log.info(FileUtil.getStackTrace(e));
					log.info("删除合同失败");
					throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
				}			
				if(update>0)
				{
					//更新签署记录表中的signStatus
	//				signRecordDao.updateSignRecordStatus(new Date(), (byte)status, contract, identity);
					returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], "");
				}
				else
				{
					returnData = new ReturnData(ConstantUtil.DELETE_CONTRACT_STATUS[0],ConstantUtil.DELETE_CONTRACT_STATUS[1], ConstantUtil.DELETE_CONTRACT_STATUS[2], "");
				}
			}
			else
			{
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	//TODO
	public ReturnData judgeContractStatus(ContractEntity contractEntity)
	{
		ReturnData returnData = null;
		/*
		if(contractEntity.getStatus() == 2)
		{
			log.info("合同已经全部签署完成");
			returnData =  new ReturnData(ConstantUtil.CONTRACT_HASBEEN_ALLSIGNED[0],ConstantUtil.CONTRACT_HASBEEN_ALLSIGNED[1], ConstantUtil.CONTRACT_HASBEEN_ALLSIGNED[2], "");
			return returnData;
		}
		*/
		if(contractEntity.getStatus() == 3)
		{
			log.info("合同已被拒绝");
			returnData =  new ReturnData(ConstantUtil.CONTRACT_HASBEEN_REFUSED[0],ConstantUtil.CONTRACT_HASBEEN_REFUSED[1], ConstantUtil.CONTRACT_HASBEEN_REFUSED[2], "");
			return returnData;
		}
		if(contractEntity.getStatus() == 4)
		{
			log.info("合同已被撤销");
			returnData =  new ReturnData(ConstantUtil.CONTRACT_HASBEEN_CANCELED[0],ConstantUtil.CONTRACT_HASBEEN_CANCELED[1], ConstantUtil.CONTRACT_HASBEEN_CANCELED[2], "");
			return returnData;
		}
		if(contractEntity.getStatus() == 5)
		{
			log.info("合同已被撤销");
			returnData =  new ReturnData(ConstantUtil.CONTRACT_HASBEEN_CLOSED[0],ConstantUtil.CONTRACT_HASBEEN_CLOSED[1], ConstantUtil.CONTRACT_HASBEEN_CLOSED[2], "");
			return returnData;
		}
		else
		{
			returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], "");
		}
		return returnData;
	}
	/**
	 * 云签批量查询接口
	 */
	// TODO
	@Override
	public ReturnData getContractList(Map<String, String> datamap)throws ServiceException 
	{
		ReturnData returnData = null;
		try
		{
			if(null == datamap)
			{
				returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1],ConstantUtil.MAP_PARAMETER[2],"");
				return returnData;	
			}
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String userId = StringUtil.nullToString(datamap.get("userId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String status = StringUtil.nullToString(datamap.get("status"));
			String currPageStr = StringUtil.nullToString(datamap.get("currPage"));
			String countsPerPageStr = StringUtil.nullToString(datamap.get("countsPerPage"));
			String isDelete = StringUtil.nullToString(datamap.get("isDelete"));
			String queryCount = StringUtil.nullToString(datamap.get("querycount"));
			if("".equals(currPageStr))
			{
				returnData = new ReturnData(ConstantUtil.RETURN_FAIL_PARAMERROR[0],ConstantUtil.RETURN_FAIL_PARAMERROR[1],ConstantUtil.RETURN_FAIL_PARAMERROR[2],"currPage");
				return returnData;	
			}
			if("".equals(countsPerPageStr))
			{
				returnData = new ReturnData(ConstantUtil.RETURN_FAIL_PARAMERROR[0],ConstantUtil.RETURN_FAIL_PARAMERROR[1],ConstantUtil.RETURN_FAIL_PARAMERROR[2],"countsPerPage");
				return returnData;	
			}
			//先查看 平台ID是否已经存在
			PlatformEntity platformEntity = null;
			int plateform_id = 0;
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
			else
			{
				plateform_id = platformEntity.getId();
			}
			
			StringBuffer querySql = new StringBuffer(StringUtil.querySql);
			StringBuffer whereSql = new StringBuffer("");
			Gson gson = new Gson();
			long stime = 0L;
			long etime = 0L;
	
			if("".equals(startTime))
			{
				stime = DateUtil.timeToTimestamp("1970-01-01 00:00:00")/1000;
			}
			else
			{
				stime = DateUtil.timeToTimestamp(startTime)/1000;
			}
			if("".equals(endTime))
			{
				etime = System.currentTimeMillis()/1000;
			}
			else
			{
			    etime = DateUtil.timeToTimestamp(endTime)/1000;
			}
			if(!"".equals(isDelete))
			{
				whereSql.append(" AND t.isdelete = :isDelete ");
			}
			if(!"".equals(userId))
			{
				whereSql.append("  AND ( record.singer_id = :userId OR record.singer_id IN ( SELECT id FROM c_identity WHERE binded_id = :userId2 ) ) ");
			}
			if(!"".equals(title))
			{
				whereSql.append(" AND t.title LIKE :title ");
			}
			if(!"".equals(status))
			{
				/*
			           等您签署 = 0
				我已签署 = 1
				签署成功 = 2
				签署拒绝 = 3 
				合同撤销 = 4
				签署关闭 = 5 
				未生效    = 9 
				*/
				if("0".equals(status) || "1".equals(status))//
				{
//					whereSql.append(" AND record.sign_status = 1 ");
					whereSql.append(" AND record.sign_status = :status ");
				}
				else if("9".equals(status))
				{
					whereSql.append(" AND t.status <> :status ");
					status = "2";
				}
				else
				{
					whereSql.append(" AND t.status = :status ");
				}
			}
//			else
//			{
//				whereSql.append(" AND t.status = :status ");
//			}		
	//		whereSql.append(" AND UNIX_TIMESTAMP(t.create_time) between '"+stime+"' and '"+etime+"' ");	
			whereSql.append(" AND UNIX_TIMESTAMP(t.create_time) between :stime and :etime ");	
			//增加orderid过滤
			try{
				String orderids=datamap.get("orderIds");
				if(orderids!=null&&!"".equals(orderids.trim()))
					appendQueryConditionByOrderId(whereSql,JSONArray.fromObject(orderids) );
			}catch(Exception e){
				
			}
			whereSql.append(" ORDER BY t.update_time DESC ");
			querySql.append(whereSql);
			log.info("querySql.toString()==="+querySql.toString());
			//只统计时不查询合同记录
			Query rs = null;
			List<Map> rtList = null;
			int countsPerPage = Integer.parseInt(countsPerPageStr);
			int count = 0;
			if(!"1".equals(queryCount))
			{
				rs = entityManager.createNativeQuery(querySql.toString());
			
				if(!"".equals(isDelete))
				{
					rs.setParameter("isDelete", Byte.valueOf(isDelete));
				}
				if(!"".equals(userId))
				{
					rs.setParameter("userId", Integer.parseInt(userId));
					rs.setParameter("userId2", Integer.parseInt(userId));
				}
				if(!"".equals(title))
				{
					rs.setParameter("title", "%"+title+"%");
				}	
				if(!"".equals(status))
				{
					rs.setParameter("status", Integer.parseInt(status));
				}
//				else
//				{
//					rs.setParameter("status", 1);
//				}
				rs.setParameter("stime", stime);
				rs.setParameter("etime", etime);
				
				int currPage = Integer.parseInt(currPageStr);
				if(currPage == 0)
				{
					currPage += 1;
				}
				rs.setFirstResult(countsPerPage*(currPage-1));
				rs.setMaxResults(countsPerPage);
		//		List<ContractEntity> rsList = rs.getResultList();
				List<?> rsList = rs.getResultList();
				Map<String,String> pojoMap = null;
				
				if(null != rsList && rsList.size()>0)
				{
					rtList = new ArrayList<Map>();
					for(int i = 0; i < rsList.size(); i++){
						Object[] obj = (Object[]) rsList.get(i);
						String serialNum = (String)obj[2];
						int creator = (int)obj[3];//创建人ID
						byte isShow = (byte)obj[4];
						byte con_status = (byte)obj[5];
//						if(!(Integer.parseInt(userId) != creator && isShow == 1 && con_status == 0))
//						{
//							count++;
							ContractEntity contract = contractDao.findContractBySerialNum(serialNum);
							//查询签署记录表
							List<SignRecordEntity> listSignRecord = signRecordDao.querySignRecordByContractId(contract);
							pojoMap = pojoMap(contract, listSignRecord);
							rtList.add(pojoMap);
//						}
					}
				}
			}
//			System.out.println("count======"+count);
			//统计总条数
			StringBuffer queryCountSql = new StringBuffer(StringUtil.countSql);
			queryCountSql.append(whereSql);
			log.info("queryCountSql==="+queryCountSql);
			Query rs_count = entityManager.createNativeQuery(queryCountSql.toString());
			if(!"".equals(isDelete))
			{
				rs_count.setParameter("isDelete", Byte.valueOf(isDelete));
			}
			if(!"".equals(userId))
			{
				rs_count.setParameter("userId", Integer.parseInt(userId));
				rs_count.setParameter("userId2", Integer.parseInt(userId));
			}
			if(!"".equals(title))
			{
				rs_count.setParameter("title", "%"+title+"%");
			}
			if(!"".equals(status))
			{
				rs_count.setParameter("status", Integer.parseInt(status));
			}
//			else
//			{
//				rs_count.setParameter("status", 1);
//			}
			rs_count.setParameter("stime", stime);
			rs_count.setParameter("etime", etime);
			Object countObj = rs_count.getSingleResult();
			int totalCount = ((BigInteger)countObj).intValue() - count; 
			int totalPage = countTotalPage(countObj,countsPerPage,count);
			log.info("totalCount==="+totalCount+",totalPage==="+totalPage);
			Map<String,String> returnMap = new HashMap<String,String>();
			returnMap.put("totalCount", String.valueOf(totalCount));
			returnMap.put("totalPage", String.valueOf(totalPage));
			returnMap.put("contractData", gson.toJson(rtList));
			String rd = gson.toJson(returnMap) == null ? "": gson.toJson(returnMap);
//			log.info("压缩前长度："+rd.length());
//			rd = ZipUtil.zip(rd);
//			log.info("压缩后长度："+rd.length());
			returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], rd);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	
	private void appendQueryConditionByOrderId(StringBuffer sql,JSONArray orderids){
		if(orderids!=null&&orderids.size()>0){
			sql.append(" and t.orderid in (");
			for(Object o:orderids){
				sql.append("'").append(o.toString()).append("'").append(",");
			}
			sql.deleteCharAt(sql.toString().length()-1).append(")");
		}
	}
	
	/**
	 * 云签草稿箱查询接口
	 */
	// TODO
	@Override
	public ReturnData getDraftContractList(Map<String, String> datamap)throws ServiceException 
	{
		ReturnData returnData = null;
		try
		{
			if(null == datamap)
			{
				returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1],ConstantUtil.MAP_PARAMETER[2],"");
				return returnData;	
			}
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String userId = StringUtil.nullToString(datamap.get("userId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String currPageStr = StringUtil.nullToString(datamap.get("currPage"));
			String countsPerPageStr = StringUtil.nullToString(datamap.get("countsPerPage"));
			String isDelete = StringUtil.nullToString(datamap.get("isDelete"));
			String queryCount = StringUtil.nullToString(datamap.get("querycount"));
			if("".equals(currPageStr))
			{
				returnData = new ReturnData(ConstantUtil.RETURN_FAIL_PARAMERROR[0],ConstantUtil.RETURN_FAIL_PARAMERROR[1],ConstantUtil.RETURN_FAIL_PARAMERROR[2],"currPage");
				return returnData;	
			}
			if("".equals(countsPerPageStr))
			{
				returnData = new ReturnData(ConstantUtil.RETURN_FAIL_PARAMERROR[0],ConstantUtil.RETURN_FAIL_PARAMERROR[1],ConstantUtil.RETURN_FAIL_PARAMERROR[2],"countsPerPage");
				return returnData;	
			}
			//先查看 平台ID是否已经存在
			PlatformEntity platformEntity = null;
			int plateform_id = 0;
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
			else
			{
				plateform_id = platformEntity.getId();
			}
			
			StringBuffer draftQuerySql = new StringBuffer(StringUtil.draftQuerySql);
			StringBuffer whereSql = new StringBuffer("");
			Gson gson = new Gson();
			long stime = 0L;
			long etime = 0L;
	
			if("".equals(startTime))
			{
				stime = DateUtil.timeToTimestamp("1970-01-01 00:00:00")/1000;
			}
			else
			{
				stime = DateUtil.timeToTimestamp(startTime)/1000;
			}
			if("".equals(endTime))
			{
				etime = System.currentTimeMillis()/1000;
			}
			else
			{
			    etime = DateUtil.timeToTimestamp(endTime)/1000;
			}
			if(!"".equals(isDelete))
			{
				whereSql.append(" AND t.isdelete = :isDelete ");
			}
			if(!"".equals(userId))
			{
				whereSql.append("  AND ( record.singer_id = :userId OR record.singer_id IN ( SELECT id FROM c_identity WHERE binded_id = :userId2 ) ) ");
			}
			if(!"".equals(title))
			{
				whereSql.append(" AND t.title LIKE :title ");
			}
					
	//		whereSql.append(" AND UNIX_TIMESTAMP(t.create_time) between '"+stime+"' and '"+etime+"' ");	
			whereSql.append(" AND UNIX_TIMESTAMP(t.create_time) between :stime and :etime ");	
			whereSql.append(" ORDER BY t.update_time DESC ");
			draftQuerySql.append(whereSql);
			log.info("draftQuerySql==="+draftQuerySql.toString());
			//只统计时不查询合同记录
			Query rs = null;
			List<Map> rtList = null;
			int countsPerPage = Integer.parseInt(countsPerPageStr);
			if(!"1".equals(queryCount))
			{
				rs = entityManager.createNativeQuery(draftQuerySql.toString());
			
				if(!"".equals(isDelete))
				{
					rs.setParameter("isDelete", Byte.valueOf(isDelete));
				}
				if(!"".equals(userId))
				{
					rs.setParameter("userId", Integer.parseInt(userId));
					rs.setParameter("userId2", Integer.parseInt(userId));
				}
				if(!"".equals(title))
				{
					rs.setParameter("title", "%"+title+"%");
				}	
				rs.setParameter("stime", stime);
				rs.setParameter("etime", etime);
				
				int currPage = Integer.parseInt(currPageStr);
				currPage += 1;
				rs.setFirstResult(countsPerPage*(currPage-1));
				rs.setMaxResults(countsPerPage);
				List<?> rsList = rs.getResultList();
				Map<String,String> pojoMap = null;
				
				if(null != rsList && rsList.size()>0)
				{
					rtList = new ArrayList<Map>();
					for(int i = 0; i < rsList.size(); i++){
						Object[] obj = (Object[]) rsList.get(i);
						String serialNum = (String)obj[2];
						int creator = (int)obj[3];//创建人ID
						byte isShow = (byte)obj[4];
						byte con_status = (byte)obj[5];
//						if(!(Integer.parseInt(userId) != creator && isShow == 1 && con_status == 0))
//						{
							ContractEntity contract = contractDao.findContractBySerialNum(serialNum);
							//查询签署记录表
							List<SignRecordEntity> listSignRecord = signRecordDao.querySignRecordByContractId(contract);
							pojoMap = pojoMap(contract, listSignRecord);
							rtList.add(pojoMap);
//						}
					}
				}
			}
			//统计总条数
			StringBuffer draftCountSql = new StringBuffer(StringUtil.draftCountSql);
			draftCountSql.append(whereSql);
			log.info("queryCountSql==="+draftCountSql);
			Query rs_count = entityManager.createNativeQuery(draftCountSql.toString());
			if(!"".equals(isDelete))
			{
				rs_count.setParameter("isDelete", Byte.valueOf(isDelete));
			}
			if(!"".equals(userId))
			{
				rs_count.setParameter("userId", Integer.parseInt(userId));
				rs_count.setParameter("userId2", Integer.parseInt(userId));
			}
			if(!"".equals(title))
			{
				rs_count.setParameter("title", "%"+title+"%");
			}
			rs_count.setParameter("stime", stime);
			rs_count.setParameter("etime", etime);
			Object countObj = rs_count.getSingleResult();
			int totalCount = ((BigInteger)countObj).intValue(); 
			int totalPage = countTotalPage(countObj,countsPerPage);
			log.info("totalCount==="+totalCount+",totalPage==="+totalPage);
			Map<String,String> returnMap = new HashMap<String,String>();
			returnMap.put("totalCount", String.valueOf(totalCount));
			returnMap.put("totalPage", String.valueOf(totalPage));
			returnMap.put("contractData", gson.toJson(rtList));
			String rd = gson.toJson(returnMap) == null ? "": gson.toJson(returnMap);
			returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], rd);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	/**
	 * 互联网金融批量查询接口
	 */
	// TODO
	@Override
	public ReturnData getInternetFinanceContractList(Map<String, String> datamap)throws ServiceException 
	{
		ReturnData returnData = null;
		try
		{
			if(null == datamap)
			{
				throw new ServiceException(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1],ConstantUtil.MAP_PARAMETER[2]);	
			}
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String appName = StringUtil.nullToString(datamap.get("appName"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String userId = StringUtil.nullToString(datamap.get("userId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String status = StringUtil.nullToString(datamap.get("status"));
			String currPageStr = StringUtil.nullToString(datamap.get("currPage"));
			String countsPerPageStr = StringUtil.nullToString(datamap.get("countsPerPage"));
			String isDelete = StringUtil.nullToString(datamap.get("isDelete"));
			String optFrom = StringUtil.nullToString(datamap.get("optFrom"));//0,全部, 1,云签  2,其他平台
	
			if("".equals(currPageStr))
			{
				returnData = new ReturnData(ConstantUtil.RETURN_FAIL_PARAMERROR[0],ConstantUtil.RETURN_FAIL_PARAMERROR[1],ConstantUtil.RETURN_FAIL_PARAMERROR[2],"currPage");
				return returnData;	
			}
			if("".equals(countsPerPageStr))
			{
				returnData = new ReturnData(ConstantUtil.RETURN_FAIL_PARAMERROR[0],ConstantUtil.RETURN_FAIL_PARAMERROR[1],ConstantUtil.RETURN_FAIL_PARAMERROR[2],"countsPerPage");
				return returnData;	
			}
			
			//查询信托方的人员信息 ---start
			PlatformEntity platformXT = null;
			try {
				platformXT = platformDao.findPlatformByAppId(appId);
			} catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2]);
			}
			if(null == platformXT)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			IdentityEntity identityXT = null;
			try {
				identityXT = identityDao.findById(Integer.parseInt(userId));
			} catch (Exception e) {
				log.info("查询用户表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2]);
			}
			if(null == identityXT)
			{
				log.info("签署用户不存在");
				throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
			}
			
			//信托方人员信息查询---end
			
			//先查看 平台ID是否已经存在
			PlatformEntity platformEntity = null;
			try {
				platformEntity = platformDao.findPlatformByAppId(appId);
			} catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			//平台ID不存在  抛出异常
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			
			StringBuffer querySql = new StringBuffer(StringUtil.internetQuerySql);
			StringBuffer whereSql = new StringBuffer("");
			Gson gson = new Gson();
			long stime = 0L;
			long etime = 0L;
	
			if("".equals(startTime))
			{
				stime = DateUtil.timeToTimestamp("1970-01-01 00:00:00")/1000;
			}
			else
			{
				stime = DateUtil.timeToTimestamp(startTime)/1000;
			}
			if("".equals(endTime))
			{
				etime = System.currentTimeMillis()/1000;
			}
			else
			{
			    etime = DateUtil.timeToTimestamp(endTime)/1000;
			}
			if(!"".equals(isDelete))
			{
				whereSql.append(" AND t.isdelete = :isDelete ");
			}
			if(!"".equals(userId))
			{
				whereSql.append(" AND t.creator = :userId ");
			}
			if(!"".equals(title))
			{
				whereSql.append(" AND t.title LIKE :title ");
			}
			if("1".equals(optFrom) && !"".equals(appId)) //1,云签
			{
				whereSql.append(" AND platform.app_id = :appId ");
			}
			else if("2".equals(optFrom))
			{
				whereSql.append(" AND platform.program LIKE :appName ");
			}
			if(!"".equals(status))
			{
				/*
			           等您签署 = 0
				我已签署 = 1
				签署成功 = 2
				签署拒绝 = 3 
				合同撤销 = 4
				签署关闭 = 5 
				未生效    = 9 
				*/
				if("1".equals(status))//
				{
					whereSql.append(" AND record.sign_status = 1 ");
				}
				else if("9".equals(status))
				{
					whereSql.append(" AND t.status <> 2 ");
				}
				else
				{
					whereSql.append(" AND t.status = :status ");
				}
			}		
			whereSql.append(" AND UNIX_TIMESTAMP(t.create_time) between :stime and :etime ");	
			whereSql.append(" ORDER BY t.update_time DESC ");
			querySql.append(whereSql);
			Query rs = entityManager.createNativeQuery(querySql.toString());
			if(!"".equals(isDelete))
			{
				rs.setParameter("isDelete", Byte.valueOf(isDelete));
			}
			if(!"".equals(userId))
			{
				rs.setParameter("userId", Integer.parseInt(userId));
			}
			if(!"".equals(title))
			{
				rs.setParameter("title", "%"+title+"%");
			}
			if("1".equals(optFrom) && !"".equals(appId)) //1,云签
			{
				rs.setParameter("appId", appId);
			}
			else if("2".equals(optFrom))
			{
				rs.setParameter("appName", appName);
			}
			if(!"".equals(status))
			{	
				if(!"1".equals(status))
				{
					rs.setParameter("status", Integer.parseInt(status));
				}
			}
			rs.setParameter("stime", stime);
			rs.setParameter("etime", etime);
			int countsPerPage = Integer.parseInt(countsPerPageStr);
			int currPage = Integer.parseInt(currPageStr);
			if(currPage == 0)
			{
				currPage += 1;
			}
			rs.setFirstResult(countsPerPage*(currPage-1));
			rs.setMaxResults(countsPerPage);
			List<?> rsList = rs.getResultList();
			Map<String,String> pojoMap = null;
			List<Map> rtList = null;
			if(null != rsList && rsList.size()>0)
			{
				rtList = new ArrayList<Map>();
				for(int i = 0; i < rsList.size(); i++){
					Object[] obj = (Object[]) rsList.get(i);
					String serialNum = (String)obj[4];
					ContractEntity contract = contractDao.findContractBySerialNum(serialNum);
					//查询签署记录表
					List<SignRecordEntity> listSignRecord = signRecordDao.querySignRecordByContractId(contract);
					pojoMap = pojoMap(contract, listSignRecord);
					rtList.add(pojoMap);
				}
			}
			//统计总条数
			StringBuffer queryCountSql = new StringBuffer(StringUtil.internetCountSql);
			queryCountSql.append(whereSql);
			Query rs_count = entityManager.createNativeQuery(queryCountSql.toString());
			if(!"".equals(isDelete))
			{
				rs_count.setParameter("isDelete", Byte.valueOf(isDelete));
			}
			if(!"".equals(userId))
			{
				rs_count.setParameter("userId", Integer.parseInt(userId));
			}
			if(!"".equals(title))
			{
				rs_count.setParameter("title", "%"+title+"%");
			}
			if("1".equals(optFrom) && !"".equals(appId)) //1,云签
			{
				rs_count.setParameter("appId", appId);
			}
			else if("2".equals(optFrom))
			{
				rs_count.setParameter("appName", appName);
			}
			if(!"".equals(status))
			{
				if(!"1".equals(status))//
				{
					rs_count.setParameter("status", Integer.parseInt(status));
				}
			}
//			rs_count.setParameter("appName", appName);
			rs_count.setParameter("stime", stime);
			rs_count.setParameter("etime", etime);
			Object countObj = rs_count.getSingleResult();
			int totalCount = ((BigInteger)countObj).intValue();
			int totalPage = countTotalPage(countObj,countsPerPage);
			log.info("totalCount==="+totalCount+",totalPage==="+totalPage);
			Map<String,String> returnMap = new HashMap<String,String>();
			returnMap.put("totalCount", String.valueOf(totalCount));
			returnMap.put("totalPage", String.valueOf(totalPage));
			returnMap.put("contractData", gson.toJson(rtList));
			returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], gson.toJson(returnMap));
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	/**
	 * 计算总页数
	 * @param count, countsPerPage
	 * @return countNum
	 * @return delCount 减去的记录
	 */
//	public int countTotalPage(int count, int countsPerPage) {
//		int countNum = 0;
//		if (count == 0){
//			countNum = 0;
//		} else if (count % countsPerPage == 0){
//			countNum = count/countsPerPage;
//		} else {
//			countNum = count/countsPerPage+1;
//		}
//		
//		return countNum;
//	}
	public int countTotalPage(Object count, int countsPerPage) {
		int countNum = 0;
		if (((BigInteger)count).intValue() == 0){
			countNum = 0;
		} else if (((BigInteger)count).intValue() % countsPerPage == 0){
			countNum = ((BigInteger)count).intValue()/countsPerPage;
		} else {
			countNum = ((BigInteger)count).intValue()/countsPerPage+1;
		}		
		return countNum;
	}
	public int countTotalPage(Object count, int countsPerPage,int delCount) {
		int countNum = 0;
		if (((BigInteger)count).intValue() == 0){
			countNum = 0;
		} else if ((((BigInteger)count).intValue() - delCount) % countsPerPage == 0){
			countNum = ((BigInteger)count).intValue()/countsPerPage;
		} else {
			countNum = (((BigInteger)count).intValue()- delCount)/countsPerPage+1;
		}		
		return countNum;
	}
	
	/**
	 * 添加证书信息到数据库
	 */
	// TODO
	public ReturnData addSecurity() throws ServiceException
	{
		ReturnData returnData = null;
		try{
			Calendar calendar = Calendar.getInstance();
	        int day = calendar.get(Calendar.DATE);       //日
	        int month = calendar.get(Calendar.MONTH) + 1;//月
	        int year = calendar.get(Calendar.YEAR);      //年
			String yearMonth = String.valueOf(year) + (String.valueOf(month).length() == 1 ? "0"+String.valueOf(month) : String.valueOf(month));
	
	 		String dname = "CN=mmec.yunsign.com,OU=maimaiwang,O=maimaiwang,L=NJ,ST=JS,C=CN";
			String alias = yearMonth;
			String keystorePath = IConf.getValue("CERTPATH")+"mmec.keystore";
			String keypass = RandomStringUtils.randomAlphanumeric(32);
			String storepass = IConf.getValue("STOREPASS");
			String dataPassword = RandomStringUtils.randomAlphanumeric(32); 
			String certificatePath = IConf.getValue("CERTPATH") +yearMonth+".cer";
			
			CertificateCoder.generateCert(dname, alias, keystorePath, keypass, storepass, certificatePath); 
			
			//查询是否存在证书信息
			SecurityEntity hasSecurityEntity =  securityDao.findSecurityEntity(alias);
			if(null == hasSecurityEntity)
			{
				SecurityEntity securityEntity = new SecurityEntity();
				securityEntity.setAlias(alias);
				securityEntity.setCertificatePath(certificatePath);
				securityEntity.setKeypass(keypass);
				securityEntity.setStorepass(storepass);
				securityEntity.setKeystorePath(keystorePath);
				securityEntity.setDataPassword(dataPassword);
				securityEntity.setCreateTime(new Date());
				if(null != securityDao.save(securityEntity))
				{
					returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],"");
				}
			}
			else
			{
				returnData =  new ReturnData("1234","加密数据已存在", "","");
			}
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	/**
	 * 查询所有云签模板
	 */
	// TODO
	@Override
	public ReturnData queryAllYusignTemplate(Map<String, String> datamap)
			throws ServiceException {
		
		ReturnData returnData = null;
		try{
			Gson gson = new Gson();
			List<YunsignTemplateEntity> list = yunsignTemplateDao.findAll();
			String retJson = gson.toJson(list);
			returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], retJson);
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	/**
	 * 根据类型查询云签模板
	 */
	// TODO
	@Override
	public ReturnData queryYusignTemplateByKind(Map<String, String> datamap)
			throws ServiceException {
		ReturnData returnData = null;
		try{
			String kind = datamap.get("kind");
			Gson gson = new Gson();
			List<YunsignTemplateEntity> list = yunsignTemplateDao.queryTemplateByKind(kind);
			String retJson = gson.toJson(list);
			returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], retJson);

		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
		}
		return returnData;
	}

	//TODO
	/**
	 *保全
	 */
//	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData protectContract(Map<String, String> datamap) throws ServiceException
	{
		ReturnData returnData = new ReturnData();
		PostMethod postMethod = new PostMethod(IConf.getValue("PROTECTURL"));
		try{
			String serialNum = StringUtil.nullToString(datamap.get("serialNum"));
			String userId = StringUtil.nullToString(datamap.get("userId"));
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String proTime = StringUtil.nullToString(datamap.get("proTime"));//保全时间
			String organization = StringUtil.nullToString(datamap.get("organization"));//公证处名称
			//先查是否已经保全
			ProtectInfoEntity q_protectInfoEntity = protectInfoDao.findProtectInfoEntity(serialNum);
			if(null != q_protectInfoEntity)
			{
				log.info("该合同已经保全");
				throw new ServiceException(ConstantUtil.HAS_PROTECT[0],ConstantUtil.HAS_PROTECT[1],ConstantUtil.HAS_PROTECT[2]);
			}
			
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
			IdentityEntity identityEntity = null;
			try {
//				identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
				
				identityEntity = identityDao.findById(Integer.parseInt(userId));
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
			ContractEntity contract = null;
			try {
				contract = contractDao.findContractBySerialNum(serialNum);
			} catch (Exception e) {
				log.info("查询合同表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			CustomInfoEntity customInfo = identityEntity.getCCustomInfo();
			CompanyInfoEntity companyInfo = identityEntity.getCCompanyInfo();
			String userName = "";
			String cardId = "";//身份证号码
			String companyName = "";
			String businessLicenseNo = "";//营业执照号
			if(null != companyInfo)
			{
				companyName = companyInfo.getCompanyName();//公司名
				businessLicenseNo = companyInfo.getBusinessLicenseNo();
			}
			if(null != customInfo)
			{
				userName = customInfo.getUserName();//姓名
				cardId = customInfo.getIdentityCard();//身份证号码				
			}
			DateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String protectDate = dateFromat.format(date);			
			Calendar calendar = Calendar.getInstance();		  						
			calendar.add(Calendar.YEAR, +Integer.parseInt(proTime));
			String expireDate = dateFromat.format(calendar.getTime());
			log.info("protectDate==="+protectDate+",expireDate==="+expireDate);
			//合同打包传输			
			ReturnData rd = downloadService.baoquanDownload(serialNum);			
			if("0000".equals(rd.getRetCode()))
			{
				postMethod.setParameter("p_no", serialNum);
	            postMethod.setParameter("p_filename", serialNum + ".zip");
	            postMethod.setParameter("p_apply_userid", String.valueOf(identityEntity.getId()));
	            postMethod.setParameter("p_apply_username", userName);
	            postMethod.setParameter("p_apply_idenid", cardId);//身份证号
				postMethod.setParameter("p_apply_compname",companyName);
				postMethod.setParameter("p_apply_licence",businessLicenseNo);
	            postMethod.setParameter("p_operatetime", protectDate);
	            postMethod.setParameter("p_expiredtime", expireDate);
	            postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	            /* 创建HttpClient实例 */
	    		HttpClient client = new HttpClient();
				/* 执行post方法 */
	            int statusCode = client.executeMethod(postMethod);
	            //TODO
				if (statusCode == HttpStatus.SC_OK) 
				{
					//保全扣费
					userAccountService.reduce_times(identityEntity.getId(), 1, "baoquan", serialNum);
					Map<String,String> map1 = new HashMap<String,String>();
					map1.put("userid", userId);
					map1.put("times", "1");
					map1.put("paycode", "baoquan");
					map1.put("bqYears", "5");					
					map1.put("bqStartDate", protectDate);
		            map1.put("bqEndDate", expireDate);
					map1.put("payid", serialNum);
					//新增预警
					map1.put("requestIp", "");

					feeRMLService.reduceServeTimes(map1);
					//入保全记录表
					ProtectInfoEntity protectInfoEntity = new ProtectInfoEntity();
					protectInfoEntity.setSerialNum(serialNum);
					protectInfoEntity.setExpireTime(DateUtil.stringToDate(expireDate));
					protectInfoEntity.setStatus(0);
					protectInfoEntity.setOrganization(organization);
					protectInfoEntity.setProtectTime(DateUtil.stringToDate(protectDate));
					protectInfoEntity.setUserId(identityEntity.getId());
					protectInfoEntity.setTitle(contract.getTitle());
					protectInfoDao.save(protectInfoEntity);
					returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],"");
				}
				else
				{
					returnData =  new ReturnData(ConstantUtil.PROTECT_FAILURE[0],ConstantUtil.PROTECT_FAILURE[1], ConstantUtil.PROTECT_FAILURE[2],"");
				}
			}
			else
			{
				returnData =  new ReturnData(ConstantUtil.PROTECT_FAILURE[0],ConstantUtil.PROTECT_FAILURE[1], ConstantUtil.PROTECT_FAILURE[2],"");
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		finally 
		{
			postMethod.releaseConnection();
		}
		return returnData;
	}
	//TODO
	/**ByZjnsBank
	 * 资金农商银行保全
	 *保全
	 */
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData zjnsBankProtectContract(Map<String, String> datamap) throws ServiceException
	{
		String baoquanUrl = IConf.getValue("PROTECTURL");
		log.info("baoquanUrl==="+baoquanUrl);
		ReturnData returnData = new ReturnData();
		PostMethod postMethod = new PostMethod(baoquanUrl);
		try{
			String serialNum = StringUtil.nullToString(datamap.get("serialNum"));
			String userId = StringUtil.nullToString(datamap.get("userId"));
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String proTime = StringUtil.nullToString(datamap.get("proTime"));//保全时间
			String organization = StringUtil.nullToString(datamap.get("organization"));//公证处名称
			String hashCode = StringUtil.nullToString(datamap.get("hashCode"));
			//先查是否已经保全
			ProtectInfoEntity q_protectInfoEntity = protectInfoDao.findProtectInfoEntity(serialNum);
			if(null != q_protectInfoEntity)
			{
				log.info("该合同已经保全");
				return  new ReturnData(ConstantUtil.HAS_PROTECT[0],ConstantUtil.HAS_PROTECT[1],ConstantUtil.HAS_PROTECT[2],"");
			}
			
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
			IdentityEntity identityEntity = null;
			try {
					identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,userId);
				
//				identityEntity = identityDao.findById(Integer.parseInt(userId));
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
			ContractEntity contract = null;
//			try {
//				contract = contractDao.findContractBySerialNum(serialNum);
//			} catch (Exception e) {
//				log.info("查询合同表异常");
//				log.info(FileUtil.getStackTrace(e));
//				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
//			}
			CustomInfoEntity customInfo = identityEntity.getCCustomInfo();
			CompanyInfoEntity companyInfo = identityEntity.getCCompanyInfo();
			String userName = "";
			String cardId = "";//身份证号码
			String companyName = "";
			String businessLicenseNo = "";//营业执照号
			if(null != companyInfo)
			{
				companyName = companyInfo.getCompanyName();//公司名
				businessLicenseNo = companyInfo.getBusinessLicenseNo();
			}
			if(null != customInfo)
			{
				userName = customInfo.getUserName();//姓名
				cardId = customInfo.getIdentityCard();//身份证号码				
			}
			DateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String protectDate = dateFromat.format(date);			
			Calendar calendar = Calendar.getInstance();		  						
			calendar.add(Calendar.YEAR, +Integer.parseInt(proTime));
			String expireDate = dateFromat.format(calendar.getTime());
			log.info("protectDate==="+protectDate+",expireDate==="+expireDate);
			//合同打包传输			
			//ReturnData rd = downloadService.baoquanDownload(serialNum);
			//将hash文写到txt文件
			String zjns_bank_hashFile = IConf.getValue("zjns_bank_hashFilePath")+serialNum+".txt";
			String ftp_ip = IConf.getValue("PROTECT_FTP_IP");
			int ftp_port = Integer.parseInt(IConf.getValue("PROTECT_FTP_PORT"));
			String ftp_account = IConf.getValue("PROTECT_ACCOUNT");
			String ftp_pwd = IConf.getValue("PROTECT_PASSWORD");
			log.info("ftp_ip==="+ftp_ip+",ftp_port==="+ftp_port+",ftp_account==="+ftp_account+",ftp_pwd==="+ftp_pwd);
			FtpUtil ftpUtil = new FtpUtil(ftp_ip,ftp_port,ftp_account,ftp_pwd);
			boolean insertBaoqun = true;
			boolean b = false;
			if(ftpUtil.ftpLogin())
			{
				FileUtil.writeTxtFile(hashCode, new File(zjns_bank_hashFile));
				b =  ftpUtil.uploadFile(new File(zjns_bank_hashFile), IConf.getValue("PROTECT_PATH"));
			}
			else
			{
				insertBaoqun = false;
				throw new ServiceException(ConstantUtil.CONNECT_SERVER_FAILURE[0],ConstantUtil.CONNECT_SERVER_FAILURE[1],ConstantUtil.CONNECT_SERVER_FAILURE[2]);
			}
			
			if(b)
			{
				postMethod.setParameter("p_no", serialNum);
	            postMethod.setParameter("p_filename", serialNum + ".txt");
	            postMethod.setParameter("p_apply_userid", String.valueOf(identityEntity.getId()));
	            postMethod.setParameter("p_apply_username", userName);
	            postMethod.setParameter("p_apply_idenid", cardId);//身份证号
				postMethod.setParameter("p_apply_compname",companyName);
				postMethod.setParameter("p_apply_licence",businessLicenseNo);
	            postMethod.setParameter("p_operatetime", protectDate);
	            postMethod.setParameter("p_expiredtime", expireDate);
	            postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	            /* 创建HttpClient实例 */
	    		HttpClient client = new HttpClient();
				/* 执行post方法 */
	            int statusCode = client.executeMethod(postMethod);
	            //TODO
				if (statusCode == HttpStatus.SC_OK) 
				{
					//保全扣费
					log.info("保全扣费ID:"+identityEntity.getId());
					userAccountService.reduce_times(identityEntity.getId(), 1, "baoquan", serialNum);
					Map<String,String> map1 = new HashMap<String,String>();
					map1.put("userid", String.valueOf(identityEntity.getId()));
					map1.put("times", "1");
					map1.put("paycode", "baoquan");
					map1.put("bqYears", "5");					
					map1.put("bqStartDate", protectDate);
		            map1.put("bqEndDate", expireDate);
					map1.put("payid", serialNum);
					//新增预警
					map1.put("requestIp", "");

					feeRMLService.reduceServeTimes(map1);
					//入保全记录表
					ProtectInfoEntity protectInfoEntity = new ProtectInfoEntity();
					protectInfoEntity.setSerialNum(serialNum);
					protectInfoEntity.setExpireTime(DateUtil.stringToDate(expireDate));
					protectInfoEntity.setStatus(0);
					protectInfoEntity.setOrganization(organization);
					protectInfoEntity.setProtectTime(DateUtil.stringToDate(protectDate));
					protectInfoEntity.setUserId(identityEntity.getId());
					protectInfoEntity.setTitle("资金农商银行保全");
					//protectInfoDao.save(protectInfoEntity);
					//保存
					if(protectInfoDao.save(protectInfoEntity) != null)
					{
						returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],"");
					}
				}
				else
				{
					insertBaoqun = false;
					returnData =  new ReturnData(ConstantUtil.PROTECT_FAILURE[0],ConstantUtil.PROTECT_FAILURE[1], ConstantUtil.PROTECT_FAILURE[2],"");
				}
			}
			else
			{
				insertBaoqun = false;
				returnData =  new ReturnData(ConstantUtil.PROTECT_FAILURE[0],ConstantUtil.PROTECT_FAILURE[1], ConstantUtil.PROTECT_FAILURE[2],"");
			}
//			if(insertBaoqun)
//			{
//				//更新外部数据表状态
//				externalDataImportDao.updataDataStatus(new Date(),1, serialNum, "资金农商银行"); 
//			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		finally 
		{
			postMethod.releaseConnection();
		}
		return returnData;
	}
	public ReturnData queryProtectContract(Map<String, String> datamap) throws ServiceException
	{
		ReturnData returnData = null;
		String userId = StringUtil.nullToString(datamap.get("userId"));
		String currPageStr = StringUtil.nullToString(datamap.get("currPage"));
		String countsPerPageStr = StringUtil.nullToString(datamap.get("countsPerPage"));
		try
		{
			StringBuffer protectQuerySql = new StringBuffer(StringUtil.protectQuerySql);
			StringBuffer protectCountSql = new StringBuffer(StringUtil.protectCountSql);
			StringBuffer whereSql = new StringBuffer("");
			if(!"".equals(userId))
			{
				whereSql.append("AND userid = :userId ");
			}
			whereSql.append(" ORDER BY protect_time DESC ");
			protectQuerySql.append(whereSql);
			Query rs = entityManager.createNativeQuery(protectQuerySql.toString());
			
			if(!"".equals(userId))
			{
				rs.setParameter("userId", Integer.parseInt(userId));
			}
			int countsPerPage = Integer.parseInt(countsPerPageStr);
			int currPage = Integer.parseInt(currPageStr)+1;

			rs.setFirstResult(countsPerPage*(currPage-1));
			rs.setMaxResults(countsPerPage);
			
			List<?> rsList = rs.getResultList();
			List<Map<String,String>> rtList = new ArrayList<Map<String,String>>();
			if(null != rsList && rsList.size()>0)
			{
				
				for(int i = 0; i < rsList.size(); i++)
				{
					Map<String,String> map = new HashMap<String,String>();
					Object[] obj = (Object[]) rsList.get(i);
					map.put("Id", String.valueOf((int) obj[0]));
					map.put("serialNum", (String) obj[4]);
					map.put("userId", String.valueOf((int) obj[6]));
					map.put("fileName", (String) obj[4]+".zip");
					map.put("organization", (String) obj[3]);
					map.put("protectTime", ((Timestamp) obj[5])+"");
					map.put("expireTime", ((Timestamp) obj[1])+"");
					rtList.add(map);
				}
			}			
			//统计记录
			protectCountSql.append(whereSql);
			Query rs_count = entityManager.createNativeQuery(protectCountSql.toString());
			if(!"".equals(userId))
			{
				rs_count.setParameter("userId", Integer.parseInt(userId));
			}
			
			Gson gson = new Gson();
			Object countObj = rs_count.getSingleResult();
			int totalCount = ((BigInteger)countObj).intValue();
			int totalPage = countTotalPage(countObj,countsPerPage);
			Map<String,String> returnMap = new HashMap<String,String>();
			returnMap.put("totalCount", String.valueOf(totalCount));
			returnMap.put("totalPage", String.valueOf(totalPage));
			returnMap.put("contractData", gson.toJson(rtList));
			returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], gson.toJson(returnMap));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	/**
	 * 添加pdf签署所需的坐标信息
	 * @param datamap
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData addPdfInfo(Map<String, String> datamap) throws ServiceException 
	{
		ReturnData returnData = null;
		try{
			Gson gson = new Gson();
			log.info("addPdfInfo method enter param："+datamap.toString());
			//pdf标识位表
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String specialCharacterNumber = StringUtil.nullToString(datamap.get("specialCharacterNumber"));
			String specialCharacter = StringUtil.nullToString(datamap.get("specialCharacter"));
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
				List<PdfInfoEntity> listPdfInfo = null;
				try {
					listPdfInfo = pdfInfoDao.findPdfInfoEntitys(contract.getId());
				} catch (Exception e) {
					log.info("查询用户表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null != listPdfInfo && !listPdfInfo.isEmpty())
				{
					throw new ServiceException(ConstantUtil.PDF_INFO[0],ConstantUtil.PDF_INFO[1],ConstantUtil.PDF_INFO[2]);
				}
				List<Map<String,String>> list = gson.fromJson(specialCharacterNumber, List.class);
				if(null != list && list.size()>0)
				{
					for(int i=0;i<list.size();i++)
					{
						Map<String,String> map = list.get(i);
						String ucid = StringUtil.nullToString(map.get("userId"));
						IdentityEntity identity = null;
						try {
							identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
						} catch (Exception e) {
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null == identity)
						{
							throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+","+ucid,ConstantUtil.RETURN_USER_NOTEXIST[2]);
						}
						//判断是否已存在，已存在不然添加
						PdfInfoEntity isExistPdfInfo = null;
						try {
							isExistPdfInfo = pdfInfoDao.findPdfInfoEntity(contract.getId(),identity.getId());
						} catch (Exception e) {
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != isExistPdfInfo)
						{
							throw new ServiceException(ConstantUtil.PDF_INFO[0],ConstantUtil.PDF_INFO[1],ConstantUtil.PDF_INFO[2]);
						}
						PdfInfoEntity pdfinfo = new PdfInfoEntity();
						pdfinfo.setAppId(appId);
						pdfinfo.setContractId(contract.getId());
						pdfinfo.setNumber(map.get("position"));
						pdfinfo.setPlateformId(platformEntity.getId());
						pdfinfo.setSerialNum(contract.getSerialNum());
						pdfinfo.setUcid(ucid);
						pdfinfo.setUserId(identity.getId());
						pdfinfo.setSpecialCharacter(specialCharacter);
						pdfinfo.setSignUiType(Integer.parseInt(map.get("signUiType")));
						pdfinfo.setOrderId(contract.getOrderId());
						PdfInfoEntity p =  pdfInfoDao.save(pdfinfo);
						if(null == p)
						{
							throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2]);
						}
					}
				}
				returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], "");
			}
			else
			{
				//合同不存在
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}

	/**
	 * 查找pdf签署所需的坐标信息
	 * @param datamap
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public ReturnData queryPdfInfo(Map<String, String> datamap) throws ServiceException 
	{
		ReturnData returnData = null;
		try{
			log.info("queryPdfInfo method enter param："+datamap.toString());
			//pdf标识位表
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String appId = StringUtil.nullToString(datamap.get("appId"));
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
				
//						String ucid = StringUtil.nullToString(datamap.get("ucid"));
//						IdentityEntity identity = null;
//						try {
//							identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
//						} catch (Exception e) {
//							log.info("查询用户表异常");
//							log.info(FileUtil.getStackTrace(e));
//							throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
//						}
				List<PdfInfoEntity> listPdfInfo = null;
				try {
					listPdfInfo = pdfInfoDao.findPdfInfoEntitys(contract.getId());
				} catch (Exception e) {
					log.info("查询用户表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				String rd = "";
				if(null != listPdfInfo && !listPdfInfo.isEmpty())
				{
					rd = new Gson().toJson(listPdfInfo);
				}
				else
				{
					rd = "";
				}
				returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], rd);
			}
			else
			{
				//合同不存在
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	/**
	 * 查找pdf签署所需的坐标信息
	 * @param datamap
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public ReturnData queryPdfInfoByUserId(Map<String, String> datamap) throws ServiceException 
	{
		ReturnData returnData = null;
		try{
			log.info("queryPdfInfoByUserId method enter param："+datamap.toString());
			//pdf标识位表
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
			String authorUserId = StringUtil.nullToString(datamap.get("authorUserId"));//授权人ucid
			String isAuthor = StringUtil.nullToString(datamap.get("isAuthor"));//是否授权 Y/N
			PlatformEntity platformEntity = null;
			try {
				platformEntity = platformDao.findPlatformByAppId(appId);
			} catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			//平台ID不存在  抛出异常
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			IdentityEntity identity = null;
			try {
				if("Y".equals(isAuthor))
				{
					identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,authorUserId);
				}
				else
				{
					identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
				}
			} catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == identity)
			{
				throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],ConstantUtil.RETURN_CUST_NOT_EXIST[1],ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
			}
			ContractEntity contract = null;
			try {
				contract = contractDao.findContractByAppIAndOrderId(orderId,platformEntity);
			} catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			
			if(null != contract)
			{	
				ContractPathEntity contractPath = null;
				try {
					contractPath = contractPathDao.findContractPathByContractId(contract);
				} catch (Exception e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null == contractPath)
				{
					throw new ServiceException(ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[0],ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[1],ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[2]);
				}
				
				PdfInfoEntity pdfInfo = null;
				try {
					pdfInfo = pdfInfoDao.findPdfInfoEntity(contract.getId(),identity.getId());
				} catch (Exception e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				float[]  f = null;
				String rd = "";
				List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
				if(null != pdfInfo)
				{
					PdfUtil.clearArrays();
					List<float[]> listCoordinate = PdfUtil.getKeyWords(contractPath.getFilePath(),pdfInfo.getSpecialCharacter());		
					String numbers = pdfInfo.getNumber();
					String [] arr_number = numbers.split(",");
					for(int j=0;j<arr_number.length;j++)
					{
						for(int i=0;i<listCoordinate.size();i++)
						{
							int tmp = Integer.parseInt(arr_number[j])-1;
							if(i== tmp)
							{
								f = listCoordinate.get(tmp);
	//							break;
								log.info("f[0]="+f[0]+",f[1]="+f[1]+",f[2]="+f[2]);
								int tem_x = (int) (f[0] * 1.5);
								int tem_y = (int)((842*((int)f[2] - 1) + (842-(int)(f[1]))) * 1.5);
	
								Map<String,String> map = new HashMap<String,String>();
								map.put("x", String.valueOf(tem_x));
								map.put("y", String.valueOf(tem_y));
								listMap.add(map);
							}
						}
					}
				}
				else
				{
					throw new ServiceException(ConstantUtil.PDF_INFO_NOT_EXIST[0],ConstantUtil.PDF_INFO_NOT_EXIST[1],ConstantUtil.PDF_INFO_NOT_EXIST[2]);
				}
//				log.info("f[0]="+f[0]+",f[1]="+f[1]+",f[2]="+f[2]);
//				int tem_x = (int) (f[0] * 1.5);
//				int tem_y = (int)((842*((int)f[2] - 1) + (842-(int)(f[1]))) * 1.5);
//
//				Map<String,String> map = new HashMap<String,String>();
//				map.put("x", String.valueOf(tem_x));
//				map.put("y", String.valueOf(tem_y));
//				map.put("page", String.valueOf((int)f[2]));
				String retStr = "";
				if(listMap.size()>0)
				{
					retStr = new Gson().toJson(listMap);
				}
				returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], retStr);
			}
			else
			{
				//合同不存在
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	@Override
	public List<ContractImgBean> queryWaitImgContractList(Date now,int imgStatus)
			throws ServiceException
	{
		System.out.println("now==="+now);
//		entityManager.flush();
		return  contractDao.findContractEntityBycreateTimeAndTurnStatus(imgStatus);
	}
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public int updateTurnContractStatus(Date updateTime,int imgStatus,String serialNum)
			throws ServiceException
	{
		int update = contractDao.updateTurnContractStatus(updateTime, imgStatus, serialNum);
		entityManager.flush();
		return update;
	}
	/**
	 * 关闭合同,退费
	 * @param identity
	 * @param contract
	 */
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public int closeContract(String userId,String serialNum)  throws ServiceException
	{
		int update = 0;
		try
		{
			update = contractDao.updataContractStatus(new Date(),userId,(byte) 5, new Date(), serialNum);
			refund(serialNum);
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.CLOSE_CONTRACT_FAILURE[0],ConstantUtil.CLOSE_CONTRACT_FAILURE[1],ConstantUtil.CLOSE_CONTRACT_FAILURE[2]);
		}
		return update;
	}
}
