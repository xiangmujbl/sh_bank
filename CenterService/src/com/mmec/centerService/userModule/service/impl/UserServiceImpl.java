package com.mmec.centerService.userModule.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mmec.aps.service.NoteService;
import com.mmec.centerService.userModule.dao.AttachmentInfoDao;
import com.mmec.centerService.userModule.dao.CompanyInfoDao;
import com.mmec.centerService.userModule.dao.CustomInfoDao;
import com.mmec.centerService.userModule.dao.IdentityInviteInfoDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.dao.SealInfoDao;
import com.mmec.centerService.userModule.entity.AttachmentEntity;
import com.mmec.centerService.userModule.entity.CompanyInfoEntity;
import com.mmec.centerService.userModule.entity.CustomInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.IdentityInviteEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.entity.RoleEntity;
import com.mmec.centerService.userModule.entity.SealEntity;
import com.mmec.centerService.userModule.entity.SigningRoomEntity;
import com.mmec.centerService.userModule.entity.UkeyInfoEntity;
import com.mmec.centerService.userModule.entity.UserRoleRelationEntity;
import com.mmec.centerService.userModule.service.UserService;
import com.mmec.css.conf.IConf;
import com.mmec.css.security.Coder;
import com.mmec.exception.ServiceException;
import com.mmec.util.AES256Util;
import com.mmec.util.ConstantUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.HttpSender;
import com.mmec.util.ImageSealUtil;
import com.mmec.util.StringUtil;

@Service("usersService")
public class UserServiceImpl extends UserBaseService implements UserService {
	private Logger  log = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private CompanyInfoDao companyInfoDao;
	@Autowired 
	private CustomInfoDao customInfoDao;
	@Autowired
	private AttachmentInfoDao attachmentInfoDao;
	@Autowired
	private IdentityInviteInfoDao identityInviteInfoDao;
	@Autowired
	private NoteService noteSerive;
	@Autowired
	private PlatformDao platformDao;
	@Autowired
	private SealInfoDao sealInfoDao;
	
	
	
	private static String defaultYunsignAPPID = "78f8RlcB2o";
	
	//是否开启邀约增加诚信值
	private static boolean isInvited = true;
	
	//云签用户登录
	@Override
	public String yunsignUserLogin(Map<String,String> datamap)throws ServiceException {
		PlatformEntity platformEntity = checkPlatform((String) datamap.get("appId"));
		//查看用户账号或密码是否存在
		String account = (String) datamap.get("account");
		String password = (String) datamap.get("password");
		//1账号登录  2、 手机号码登录 3、邮箱登录
		String accountType = (String) datamap.get("accountType");
		if(null == account || "".equals(account) || null == password || "".equals(password))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"用户账号或密码不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" account or password is null!");
		}
		
		IdentityEntity identityEntity = null;
		try {
			//1
			if("1".equals(accountType))
			{
				identityEntity = identityDao.queryAppIdAndAccount(platformEntity, account);
			}
			else if("2".equals(accountType))
			{
				identityEntity = identityDao.queryAppIdAndMobileAndType(platformEntity, account,(byte)1);
			}
			else if("3".equals(accountType))
			{
				identityEntity = identityDao.queryAppIdAndEmailAndType(platformEntity, account,(byte)2);
			}
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}

		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		if( !password.equals(identityEntity.getPassword()))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[0],
					ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[1], ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[2]);
		}
		
		return toJSONStrFromIdentity(identityEntity);
	}

	//用户证书登录
	@Override
	public String certUserLogin(Map<String,String> datamap)throws ServiceException {
			PlatformEntity platformEntity = checkPlatform((String) datamap.get("appId"));
		
			//查看用户账号或密码是否存在
			String certNum = (String) datamap.get("certNum");
			String certContent = (String) datamap.get("certContent");
			if(null == certNum || "".equals(certNum) || null == certContent || "".equals(certContent) )
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"证书不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" certNum or certContent is null!");
			}
			
			UkeyInfoEntity ukeyInfoEntity =null;
			try {
				ukeyInfoEntity = ukeyInfoDao.findByCertNumAndCertContentAndStatus(certNum, certContent,(byte)1);
			} catch (Exception e) {
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(null == ukeyInfoEntity)
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_CERT_NOT_EXIST[0],
						ConstantUtil.RETURN_CERT_NOT_EXIST[1], ConstantUtil.RETURN_CERT_NOT_EXIST[2]);
			}
			IdentityEntity identityEntity = ukeyInfoEntity.getCIdentity();
			return toJSONStrFromIdentity(identityEntity);
	}
	/**
	 * 校验证书有效性
	 */
	@Override
	public String checkCert(Map<String,String> datamap) throws ServiceException 
	{
		PlatformEntity platformEntity = checkPlatform((String) datamap.get("appId"));
	
		//查看用户账号或密码是否存在
		String certNum = (String) datamap.get("certNum");
		String certContent = (String) datamap.get("certContent");
		String userId = StringUtil.nullToString(datamap.get("userId"));
		
		if(null == certNum || "".equals(certNum) || null == certContent || "".equals(certContent) || "".equals(userId))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"证书不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" certNum or certContent is null!");
		}
		
		//查询IdentityEntity
		IdentityEntity identity = null;
		try {
			identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,userId);
		} catch (Exception e) {
			log.info("查询用户表异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		if(null == identity)
		{
			log.info("用户不存在");
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		UkeyInfoEntity ukeyInfoEntity =null;
		try {
			ukeyInfoEntity = ukeyInfoDao.findByCertNumAndCertContentAndStatus(certNum, certContent,(byte)1);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null == ukeyInfoEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_CERT_NOT_EXIST[0],
					ConstantUtil.RETURN_CERT_NOT_EXIST[1], ConstantUtil.RETURN_CERT_NOT_EXIST[2]);
		}
		IdentityEntity identityEntity = ukeyInfoEntity.getCIdentity();				
		if(2 == identityEntity.getType())
		{
			//企业信息不完整 没有企业名 不允许绑定
			if( null == identityEntity.getCCompanyInfo() || null == identity.getCCompanyInfo())
			{
				throw new ServiceException(ConstantUtil.RETURN_COMP_NOT_EXIST[0],
						ConstantUtil.RETURN_COMP_NOT_EXIST[1], ConstantUtil.RETURN_COMP_NOT_EXIST[2]);
			}
			else if(datamap.get("shbank")==null)
			{
				String signerCompanyName = StringUtil.nullToString(identity.getCCompanyInfo().getCompanyName());
				String companyName = identityEntity.getCCompanyInfo().getCompanyName();
				String subjectStr = ukeyInfoEntity.getSubject(); //datamap.get("certSubject");
				log.info("证书绑定公司名:"+companyName+",签署人公司名:"+signerCompanyName+",证书主题项:"+subjectStr);
				if(-1 == subjectStr.indexOf(companyName) || !signerCompanyName.equals(companyName))
				{
					throw new ServiceException(ConstantUtil.RETURN_COMPANYNAME_CERTSUBJECT_NOT_MATCH[0],
							ConstantUtil.RETURN_COMPANYNAME_CERTSUBJECT_NOT_MATCH[1], ConstantUtil.RETURN_COMPANYNAME_CERTSUBJECT_NOT_MATCH[2]);
				}
			}
		}
		//个人用户验证证书使用者是否包含用户名
		else if(1 == identityEntity.getType())
		{
			//企业信息不完整 没有企业名 不允许绑定
			if( null == identityEntity.getCCustomInfo() || null == identity.getCCustomInfo())
			{
				throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],
						ConstantUtil.RETURN_CUST_NOT_EXIST[1], ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
			}
			else if(datamap.get("shbank")==null)
			{
				String userName = identityEntity.getCCustomInfo().getUserName();
				String signerName = StringUtil.nullToString(identity.getCCustomInfo().getUserName());
				String subjectStr = ukeyInfoEntity.getSubject();//datamap.get("certSubject");
				log.info("证书绑定姓名:"+userName+",签署人姓名:"+signerName+",证书主题项:"+subjectStr);
				if(-1 == subjectStr.indexOf(userName) || !signerName.equals(userName))
				{
					throw new ServiceException(ConstantUtil.RETURN_USERNAME_CERTSUBJECT_NOT_MATCH[0],
							ConstantUtil.RETURN_USERNAME_CERTSUBJECT_NOT_MATCH[1], ConstantUtil.RETURN_USERNAME_CERTSUBJECT_NOT_MATCH[2]);
				}
			}
		}
		else
		{
			throw new ServiceException(ConstantUtil.RETURN_USER_TYPE_ERROR[0],
					ConstantUtil.RETURN_USER_TYPE_ERROR[1], ConstantUtil.RETURN_USER_TYPE_ERROR[2]);
		}
		return "";
	}
	//平台用户登录
	@Override
	public String platformUserLogin(Map<String,String> datamap) throws ServiceException{
		PlatformEntity platformEntity = checkPlatform((String) datamap.get("appId"));
		
		//查看用户账号或密码是否存在
		String platformUserName = (String) datamap.get("platformUserName");
		String password = (String) datamap.get("password");
		if(null == platformUserName || "".equals(platformUserName) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"平台用户不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" platformUserName is null!");
		}

		IdentityEntity identityEntity = null;
		try {
			 identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == identityEntity || !password.equals(identityEntity.getPassword()))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[0],
					ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[1], ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[2]);
		}
		
		return toJSONStrFromIdentity(identityEntity);
	}

	//对接用户注册接口
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
//	@Transactional
	public String saveIdentity(Map<String,String> datamap) throws ServiceException {
		//判断平台是否存在
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		
		//判断参数完整性
		IdentityEntity identityEntity = new IdentityEntity();
		// 转成bean对象
		try {
			BeanUtils.populate(identityEntity, datamap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			// 抛出异常 参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1], e.getMessage());
		}
		
		//判断传参是够完整  ---  账号  平台用户名 和 密码
		String identCheck = identityEntity.isBeanLegal();
		if(!"".equals(identCheck))
		{
			throw new ServiceException(
					ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+identCheck,
					ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
		}
		
		//验证用户数据是否合法
		IdentityEntity identityEntity2 = null;
		//先查看 该平台下 账号或平台用户名 是否已经存在
		try {
			identityEntity2 = getIdentityEntity(identityEntity.getAccount(),identityEntity.getPlatformUserName(),platformEntity);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null != identityEntity2)
		{
			// 创建云签账号 必须是手机、邮箱不为空
			if(!"1".equals(platformEntity.getIsYunsignCreate()) 
					&& null != identityEntity.getEmail() && !"".equals(identityEntity.getEmail()) 
					&& null != identityEntity.getMobile() && !"".equals(identityEntity.getMobile()) )
			{
				identityEntity2.setEmail(identityEntity.getEmail());
				String retStr = yunsignRegister(identityEntity2, appId);
				if("success".equals(retStr))
				{
					//发送云签注册成功短信
					String message = "恭喜您成功注册云签对接平台，您的初始密码为" + identityEntity2.getPassword() + "，请妥善保存！访问地址 www.yunsign.com";
					System.out.println(message);
					System.out.println("mobile ==="+identityEntity.getMobile());
					String returnString = "";
					try {
						returnString = HttpSender.batchSend(IConf.getValue("CL_MSG_URL"),
								IConf.getValue("CL_ACCOUNT"), IConf.getValue("CL_ACCOUNT_PASSWOERD"),identityEntity.getMobile(), message, true,
								null);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.info("调用创蓝短信通道返回值为:"+returnString);
				}
			}
			return "duplicateRegister";
			//抛出异常   账号或平台用户名 已存在
//			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[0],
//					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[2]);
		}
		
		//根据用户类型做不同判断
		
		//个人用户判断 该平台下手机号码是否已经注册过
		if(1 == identityEntity.getType() && null != identityEntity.getMobile() && !"".equals(identityEntity.getMobile()))
		{
			//同一个平台下  手机不能重复
			try {
				identityEntity2 = null;
				identityEntity2 = identityDao.queryAppIdAndMobileAndType(platformEntity,identityEntity.getMobile(),(byte)1);
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(null != identityEntity2)
			{								
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
						ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
			}
		}
		
		//企业用户判断 该平台下邮箱地址是否已经注册过
		else if(2 == identityEntity.getType() && null != identityEntity.getEmail() && !"".equals(identityEntity.getEmail()))
		{
			//同一个平台下  邮箱不能重复
			/*try {
				identityEntity2 = null;
				identityEntity2 = identityDao.queryAppIdAndEmailAndType(platformEntity,identityEntity.getEmail(),(byte)2);
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(null != identityEntity2)
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_EMAIL_EXIST[0],
						ConstantUtil.RETURN_EMAIL_EXIST[1], ConstantUtil.RETURN_EMAIL_EXIST[2]);
			}*/
		}
		
		//判断个人用户 姓名是否存在
		if(isNotNull(datamap.get("userName")) && !"未知".equals(datamap.get("userName")) )
		{
			CustomInfoEntity customInfoEntity = new CustomInfoEntity();
			customInfoEntity.setUserName(datamap.get("userName"));
			if(isNotNull(datamap.get("nickname")))
			{
				customInfoEntity.setNickname(datamap.get("nickname"));
			}
			if(isNotNull(datamap.get("phoneNum")))
			{
				customInfoEntity.setPhoneNum(datamap.get("phoneNum"));
			}
			
			if(isNotNull(datamap.get("duty")))
			{
				customInfoEntity.setDuty(datamap.get("duty"));
			}
			if(isNotNull(datamap.get("customReseve1")))
			{
				customInfoEntity.setReseve1(datamap.get("customReseve1"));
			}
			if(isNotNull(datamap.get("customReseve2")))
			{
				customInfoEntity.setReseve2(datamap.get("customReseve2"));
			}
			if(isNotNull(datamap.get("customAddress")))
			{
				customInfoEntity.setAddress(datamap.get("customAddress"));
			}
			customInfoEntity.setAppId(appId);
			
			//判断用户是否已经提交身份资料
			int personCheckStatus = 0;
			//存在 判断身份证号是否存在
			if(isNotNull(datamap.get("identityCard")))
			{
				customInfoEntity.setIdentityCard(datamap.get("identityCard"));
				//存在 再 判断身份证号 对应给用户是否存在
					
				//取证件类型 没有没有则 默认为身份证 1
				customInfoEntity.setIdentityCard(datamap.get("identityCard"));
				if(isNotNull(datamap.get("cardType")))
				{
					customInfoEntity.setCardType(datamap.get("cardType"));
				}
				else
				{
					customInfoEntity.setCardType("1");
				}
				//先根据身份证号 查询 用户是否已经注册
				List<CustomInfoEntity> customInfoList = customInfoDao.findByIdentityCardAndCardType(datamap.get("identityCard"),customInfoEntity.getCardType());
				
				//存在 再 判断姓名是否一致
				if(null != customInfoList  &&  customInfoList.size() > 0)
				{
					boolean isNotMatch = true;
					for(CustomInfoEntity custom: customInfoList)
					{
						//身份证号与姓名是否一致
						if(customInfoEntity.getUserName().equals(custom.getUserName()))
						{
							customInfoEntity.setId(custom.getId());
							isNotMatch = false;
							//姓名与身份证号匹配 
							personCheckStatus = 1;
							//且身份证附件不为空 代表用户已经提交认证
							if(null != custom.getCAttachmentIdA() || null != custom.getCAttachmentIdB())
							{
								personCheckStatus = 2;
							}
							break;
						}
					}
					//不一致 返回错误提示
					if(isNotMatch)
					{
						throw new ServiceException(
								ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[0],
								ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[1],
								ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[2]);
					}
				}
			}
			//一致的话  判断传入的 身份证 正反面是否存在	
			try {
				if(personCheckStatus < 2)
				{
					//身份证号正、反面附件都存在
					if(isNotNull(datamap.get("idImgAPath")) && isNotNull(datamap.get("idImgBPath")))
					{
						//正面
						AttachmentEntity idImgAAttach = new AttachmentEntity();
						idImgAAttach.setAttachmentPath(datamap.get("idImgAPath"));
						idImgAAttach.setAttachmentName(datamap.get("idImgAName"));
						idImgAAttach.setAttachmentUri(datamap.get("idImgAPath"));
						idImgAAttach.setAttachmentThumbUri(datamap.get("idImgAPath"));
						idImgAAttach.setAttachmentExtension(datamap.get("idImgAExtension"));
						idImgAAttach.setAttachmentSource((byte)1);
						//是否有效:"0"无效；"1”有效
						idImgAAttach.setAttachmentStatus((byte)1);
						//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
						idImgAAttach.setAttachmentType((byte)1);
						
						idImgAAttach = attachmentInfoDao.save(idImgAAttach);
						customInfoEntity.setCAttachmentIdA(idImgAAttach);
					
					    //身份证号反面
					
						AttachmentEntity idImgBAttach = new AttachmentEntity();
						idImgBAttach.setAttachmentPath(datamap.get("idImgBPath"));
						idImgBAttach.setAttachmentName(datamap.get("idImgBName"));
						idImgBAttach.setAttachmentUri(datamap.get("idImgBPath"));
						idImgBAttach.setAttachmentThumbUri(datamap.get("idImgBPath"));
						idImgBAttach.setAttachmentExtension(datamap.get("idImgBExtension"));
						idImgBAttach.setAttachmentSource((byte)1);
						//是否有效:"0"无效；"1”有效
						idImgBAttach.setAttachmentStatus((byte)1);
						//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
						idImgBAttach.setAttachmentType((byte)1);
						
						idImgBAttach = attachmentInfoDao.save(idImgBAttach);
						customInfoEntity.setCAttachmentIdB(idImgBAttach);
						
						//存在 修改 原个人用户身份证正反面 
						customInfoEntity = customInfoDao.save(customInfoEntity);	
					}
				}	
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常  系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			customInfoEntity = customInfoDao.saveAndFlush(customInfoEntity);
			//保存用户信息
			identityEntity.setCCustomInfo(customInfoEntity);
		}
		
		//企业用户 判断公司名是否存在
		if(2==identityEntity.getType() && isNotNull(datamap.get("companyName")))
		{
			CompanyInfoEntity companyInfoEntity = new CompanyInfoEntity();
			companyInfoEntity.setCompanyName(datamap.get("companyName"));
			
			//手工转成bean对象
			if(isNotNull(datamap.get("companyType")))
			{
				companyInfoEntity.setCompanyType(datamap.get("companyType"));
			}
			if(isNotNull(datamap.get("companyReseve1")))
			{
				companyInfoEntity.setReseve1(datamap.get("companyReseve1"));
			}
			
			//存在判断 营业执照号是否存在
			if(isNotNull(datamap.get("businessLicenseNo")))
			{
				companyInfoEntity.setBusinessLicenseNo(datamap.get("businessLicenseNo"));
				
				//企业用户需要判断企业名称与企业营业执照号是否存在且一致
				//先根据企业营业执照号 查询 公司是否已经注册
				List<CompanyInfoEntity> companyInfoList = companyInfoDao.findByBusinessLicenseNo(companyInfoEntity.getBusinessLicenseNo());
				if(null != companyInfoList  &&  companyInfoList.size() > 0)
				{
					boolean isNotMatch = true;
					for(CompanyInfoEntity company: companyInfoList)
					{
						//判定公司名称是否一致
						if(companyInfoEntity.getCompanyName().equals(company.getCompanyName()))
						{
							companyInfoEntity.setId(company.getId());
							isNotMatch = false;
							break;
						}
					}
					//不一致 返回错误提示
					if(isNotMatch)
					{
							throw new ServiceException(
									ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[0],
									ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[1],
									ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[2]);
					}
				}
			}
			//一致  判断 传入的营业执照附件是否存在
			//营业执照附件
			if(isNotNull(datamap.get("businessNoPath")))
			{
				AttachmentEntity businessAttach = new AttachmentEntity();
				businessAttach.setAttachmentPath(datamap.get("businessNoPath"));
				businessAttach.setAttachmentName(datamap.get("businessNoName"));
				businessAttach.setAttachmentUri(datamap.get("businessNoPath"));
				businessAttach.setAttachmentThumbUri(datamap.get("businessNoPath"));
				businessAttach.setAttachmentExtension(datamap.get("businessNoExtension"));
				businessAttach.setAttachmentSource((byte)1);
				//是否有效:"0"无效；"1”有效
				businessAttach.setAttachmentStatus((byte)1);
				//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
				businessAttach.setAttachmentType((byte)2);
				businessAttach = attachmentInfoDao.save(businessAttach);
				companyInfoEntity.setCAttachmentBusi(businessAttach);
			}
			//代理证书附件
			if(isNotNull(datamap.get("proxyPhotoPath")))
			{
				AttachmentEntity proxyAttach = new AttachmentEntity();
				proxyAttach.setAttachmentPath(datamap.get("proxyPhotoPath"));
				proxyAttach.setAttachmentName(datamap.get("proxyPhotoName"));
				proxyAttach.setAttachmentUri(datamap.get("proxyPhotoPath"));
				proxyAttach.setAttachmentExtension(datamap.get("proxyPhotoExtension"));
				proxyAttach.setAttachmentThumbUri(datamap.get("proxyPhotoPath"));
				proxyAttach.setAttachmentSource((byte)1);
				//是否有效:"0"无效；"1”有效
				proxyAttach.setAttachmentStatus((byte)1);
				//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
				proxyAttach.setAttachmentType((byte)2);
				proxyAttach = attachmentInfoDao.save(proxyAttach);
				companyInfoEntity.setCAttachmentPhoto(proxyAttach);
			}
			//保存公司信息
			companyInfoEntity = companyInfoDao.save(companyInfoEntity);
			identityEntity.setCCompanyInfo(companyInfoEntity);
		}
		
		
		//构造 账号默认数据
		// 用户类型:"1"个人；"2”企业；"3"平台
		identityEntity.setType(identityEntity.getType());
		// 用户状态:"1"停用；"0”使用
		identityEntity.setStatus((byte) 0);
		// 是否实名:"0"未激活；"1”已激活
		identityEntity.setIsAuthentic((byte) 0);
		
		// 用户来源:"1"云签；"2”对接版；"3"本地版
		identityEntity.setSource((byte) 2);
//		//对接企业用户 默认身份为非平台管理员账号
//		if(isNotNull(datamap.get("businessAdmin")) && 2 == identityEntity.getType())
//		{
//			identityEntity.setBusinessAdmin(datamap.get("businessAdmin"));
//		}
//		else
//		{
			identityEntity.setBusinessAdmin("0");
//		}
		
		//根据规则 生成用户的uuid
		identityEntity.setUuid(toGeneralUUID(appId,identityEntity.getPlatformUserName(),identityEntity.getMobile()));
		//申请时间
		identityEntity.setRegistTime(new Date());
		
		identityEntity.setStatusTime(new Date());
		identityEntity.setCPlatform(platformEntity);
	
		identityEntity.setIsMobileVerified((byte)0);
		identityEntity.setIsEmailVerified((byte)0);
		//注册账号
		try {
			identityEntity = identityDao.save(identityEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
			// 抛出异常 系统错误
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		//是否生成图章
		String isMakeSeal=(String) datamap.get("isMakeSeal");;
		log.info("-----isMakeSeal-----:"+isMakeSeal+",type:"+identityEntity.getType());
		if(("Y".equals(isMakeSeal.toUpperCase()) && 2 == identityEntity.getType())||"YY".equals(isMakeSeal.toUpperCase())){
	
		
			String sealName=new Date().getTime()+"";
			
			String sealPath=IConf.getValue("SEALPATH")+sealName+".PNG";
			log.info("----承德远大----图章名称："+sealPath);
			SealEntity sealEntity = new SealEntity();
			sealEntity.setSealName(sealName);
			//sealEntity.setSealPath(sealPath);
			sealEntity.setSealType(identityEntity.getType());
			//sealEntity.setOriginalPath(sealPath);
			//sealEntity.setCutPath(sealPath);
			//sealEntity.setBgRemovedPath(sealPath);
			sealEntity.setIsActive((byte)0);
			sealEntity.setSealNum(new Date().getTime()+"");
			//个人用户
			if(1 == identityEntity.getType())
			{
				if(null == identityEntity.getCCustomInfo())
				{
					///////////6.12//////////////////
					throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],
							ConstantUtil.RETURN_CUST_NOT_EXIST[1], ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
					////////////////////////////////
					
				}
				sealEntity.setRelatedId(identityEntity.getCCustomInfo().getId());
				sealPath=ImageSealUtil.drawSeal(identityEntity.getCCustomInfo().getUserName(), sealPath);
				sealEntity.setSealPath(sealPath);
				sealEntity.setOriginalPath(sealPath);
				sealEntity.setBgRemovedPath(sealPath);
				sealEntity.setCutPath(sealPath);
			}
			//企业用户
			else if(2 == identityEntity.getType())
			{
				if(null == identityEntity.getCCompanyInfo())
				{
			         ///////////6.12//////////////////
					throw new ServiceException(ConstantUtil.RETURN_COMP_NOT_EXIST[0],
							ConstantUtil.RETURN_COMP_NOT_EXIST[1], ConstantUtil.RETURN_COMP_NOT_EXIST[2]);
				     ////////////////////////////////
					/*throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],
							ConstantUtil.RETURN_CUST_NOT_EXIST[1], ConstantUtil.RETURN_CUST_NOT_EXIST[2]);*/
				}
				sealEntity.setRelatedId(identityEntity.getCCompanyInfo().getId());
				sealPath=ImageSealUtil.drawSeal(identityEntity.getCCompanyInfo().getCompanyName(), sealPath);
				sealEntity.setSealPath(sealPath);
				sealEntity.setOriginalPath(sealPath);
				sealEntity.setBgRemovedPath(sealPath);
				sealEntity.setCutPath(sealPath);
			}
			// 保存 图章 信息
			try {
				sealEntity = sealInfoDao.save(sealEntity);
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], e.getMessage());
			}
		
		
		}
		
		
		
		//读取平台 配置项-- 是否创建云签账号
		// 创建云签账号 必须是手机、邮箱不为空
		if(!"1".equals(platformEntity.getIsYunsignCreate()) 
				&& null != identityEntity.getEmail() && !"".equals(identityEntity.getEmail()) 
				&& null != identityEntity.getMobile() && !"".equals(identityEntity.getMobile()) )
		{
			/*
			PlatformEntity platformYunsignEntity = platformDao.findPlatformByAppId(defaultYunsignAPPID);
			
			IdentityEntity checkIdentityYunsign = new IdentityEntity();
			try {
				//判断是否可以新增到云签下
				if((byte)1 == identityEntity.getType())
				{
					//个人用户 在云签平台下 手机号码不能重复
					checkIdentityYunsign = identityDao.queryAppIdAndMobileAndType(platformYunsignEntity,identityEntity.getMobile(),(byte)1);
				}
				else
				{
					//企业用户 在云签平台下  邮箱不能重复
					checkIdentityYunsign = identityDao.queryAppIdAndEmailAndType(platformYunsignEntity,identityEntity.getEmail(),(byte)2);
				}
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			//判断用户是否重复注册
			if(null == checkIdentityYunsign )
			{
				IdentityEntity identityEntityYunsign = new IdentityEntity();
				//根据规则 生成用户的uuid
				identityEntityYunsign.setUuid(toGeneralUUID(defaultYunsignAPPID,"autoCreate",identityEntity.getMobile()));
				String account = identityEntity.getAccount()+"_"+new Date().getTime();
				identityEntityYunsign.setAccount(account);
				//新增之前 需要判断 云签账号的platfromUserName 是否重复 这涉及到 图章、合同等  必须唯一
				String platfromUserName = appId +"_" +identityEntity.getPlatformUserName();
				identityEntityYunsign.setPlatformUserName(platfromUserName);


				// 用户状态:"1"停用；"0”使用
				identityEntityYunsign.setStatus((byte) 0);
				// 是否实名:"0"未激活；"1”已激活
				identityEntityYunsign.setIsAuthentic((byte) 0);
				
				// 用户来源:"1"云签；"2”对接版；"3"本地版
				identityEntityYunsign.setSource((byte) 1);
				//对接企业用户 默认身份为非平台管理员账号
				identityEntityYunsign.setBusinessAdmin("0");
				
				//申请时间
				identityEntityYunsign.setRegistTime(new Date());
				
				identityEntityYunsign.setStatusTime(new Date());
			
				identityEntityYunsign.setIsMobileVerified((byte)0);
				identityEntityYunsign.setIsEmailVerified((byte)0);
				
				identityEntityYunsign.setMobile(identityEntity.getMobile());
				identityEntityYunsign.setEmail(identityEntity.getEmail());
				identityEntityYunsign.setPassword(identityEntity.getPassword());
				identityEntityYunsign.setType(identityEntity.getType());
				
				if(null != identityEntity.getCCompanyInfo())
				{
					identityEntityYunsign.setCCompanyInfo(identityEntity.getCCompanyInfo());
				}
				if(null != identityEntity.getCCustomInfo())
				{
					identityEntityYunsign.setCCustomInfo(identityEntity.getCCustomInfo());
				}
				identityEntityYunsign.setCPlatform(platformYunsignEntity);
				try{
					identityEntityYunsign = identityDao.save(identityEntityYunsign);
					
					//个人用户自动绑定
					if((byte)1 == identityEntityYunsign.getType() && null != identityEntityYunsign.getCCustomInfo())
					{
						//自动绑定
						identityDao.bangdingCustomAccount(identityEntityYunsign.getId(),identityEntityYunsign.getCCustomInfo().getId(), identityEntityYunsign.getMobile(),identityEntity.getId());
					}
					//企业用户自动绑定
					else if((byte)2 == identityEntityYunsign.getType() && null != identityEntityYunsign.getCCompanyInfo())
					{
						if(null != identityEntityYunsign.getCCustomInfo())
						{
						//自动绑定
						identityDao.bangdingCompanyAccount(identityEntityYunsign.getId(),identityEntityYunsign.getCCustomInfo().getId(),identityEntityYunsign.getCCompanyInfo().getId(), identityEntityYunsign.getEmail(),identityEntity.getId());
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					// 抛出异常 系统错误
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
			}
			*/
			yunsignRegister(identityEntity, appId);
		}
		return "";
	}
	
		//对接用户注册接口
		@Override
		@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
		public String saveIdentityTUNIU(Map<String,String> datamap) throws ServiceException {
			//判断平台是否存在
			String appId = (String) datamap.get("appId");
			
			//获取数据来源
			String source=(String)datamap.get("source");
			
			PlatformEntity platformEntity = checkPlatform(appId);
			
			//判断参数完整性
			IdentityEntity identityEntity = new IdentityEntity();
			// 转成bean对象
			try {
				BeanUtils.populate(identityEntity, datamap);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
				// 抛出异常 参数异常
				throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1], e.getMessage());
			}
			
			//判断传参是够完整  ---  账号  平台用户名 和 密码
			String identCheck = identityEntity.isBeanLegal();
			if(!"".equals(identCheck))
			{
				throw new ServiceException(
						ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1]+identCheck,
						ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
			}
			
			//验证用户数据是否合法
			IdentityEntity identityEntity2 = null;
			//先查看 该平台下 账号或平台用户名 是否已经存在
			try {
				
					identityEntity2 = getIdentityEntity(identityEntity.getAccount(),identityEntity.getPlatformUserName(),platformEntity,source);

			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(null != identityEntity2)
			{
				// 创建云签账号 必须是手机、邮箱不为空
				/*
				if(!"1".equals(platformEntity.getIsYunsignCreate()) 
						&& null != identityEntity.getEmail() && !"".equals(identityEntity.getEmail()) 
						&& null != identityEntity.getMobile() && !"".equals(identityEntity.getMobile()) )
				{
					identityEntity2.setEmail(identityEntity.getEmail());
					String retStr = yunsignRegister(identityEntity2, appId);
					if("success".equals(retStr))
					{
						//发送云签注册成功短信
						String message = "恭喜您成功注册云签对接平台，您的初始密码为" + identityEntity2.getPassword() + "，请妥善保存！访问地址 www.yunsign.com";
						System.out.println(message);
						System.out.println("mobile ==="+identityEntity.getMobile());
						String returnString = "";
						try {
							returnString = HttpSender.batchSend(IConf.getValue("CL_MSG_URL"),
									IConf.getValue("CL_ACCOUNT"), IConf.getValue("CL_ACCOUNT_PASSWOERD"),identityEntity.getMobile(), message, true,
									null);
						} catch (Exception e) {
							e.printStackTrace();
						}
						log.info("调用创蓝短信通道返回值为:"+returnString);
					}
				}*/
				//抛出异常   账号或平台用户名 已存在
				throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[0],
						ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[2]);
				
			}
			
			//根据用户类型做不同判断
			
			//个人用户判断 该平台下手机号码是否已经注册过
			if(1 == identityEntity.getType() && null != identityEntity.getMobile() && !"".equals(identityEntity.getMobile()))
			{
				//同一个平台下  手机不能重复
				try {
					identityEntity2 = null;
					
				identityEntity2 = identityDao.queryAppIdAndMobileAndTypeAndStatus(platformEntity,identityEntity.getMobile(),(byte)1,(byte)0);
						
					
					
				} catch (Exception e) {
					e.printStackTrace();
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
				if(null != identityEntity2)
				{								
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
							ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
				}
			}
			
			//企业用户判断 该平台下邮箱地址是否已经注册过
			else if(2 == identityEntity.getType() && null != identityEntity.getEmail() && !"".equals(identityEntity.getEmail()))
			{
				//同一个平台下  邮箱不能重复
				try {
					identityEntity2 = null;
					
			
						identityEntity2 = identityDao.queryAppIdAndEmailAndTypeAndStatus(platformEntity,identityEntity.getEmail(),(byte)2,(byte)0);
					
				} catch (Exception e) {
					e.printStackTrace();
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
				if(null != identityEntity2)
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_EMAIL_EXIST[0],
							ConstantUtil.RETURN_EMAIL_EXIST[1], ConstantUtil.RETURN_EMAIL_EXIST[2]);
				}
			}
			
			//判断个人用户 姓名是否存在
			if(isNotNull(datamap.get("userName")) && !"未知".equals(datamap.get("userName")) )
			{
				CustomInfoEntity customInfoEntity = new CustomInfoEntity();
				customInfoEntity.setUserName(datamap.get("userName"));
				if(isNotNull(datamap.get("nickname")))
				{
					customInfoEntity.setNickname(datamap.get("nickname"));
				}
				if(isNotNull(datamap.get("phoneNum")))
				{
					customInfoEntity.setPhoneNum(datamap.get("phoneNum"));
				}
				
				if(isNotNull(datamap.get("duty")))
				{
					customInfoEntity.setDuty(datamap.get("duty"));
				}
				if(isNotNull(datamap.get("customReseve1")))
				{
					customInfoEntity.setReseve1(datamap.get("customReseve1"));
				}
				if(isNotNull(datamap.get("customReseve2")))
				{
					customInfoEntity.setReseve2(datamap.get("customReseve2"));
				}
				if(isNotNull(datamap.get("customAddress")))
				{
					customInfoEntity.setAddress(datamap.get("customAddress"));
				}
				customInfoEntity.setAppId(appId);
				
				//判断用户是否已经提交身份资料
				int personCheckStatus = 0;
				//存在 判断身份证号是否存在
				if(isNotNull(datamap.get("identityCard")))
				{
					customInfoEntity.setIdentityCard(datamap.get("identityCard"));
					//存在 再 判断身份证号 对应给用户是否存在
						
					//取证件类型 没有没有则 默认为身份证 1
					customInfoEntity.setIdentityCard(datamap.get("identityCard"));
					if(isNotNull(datamap.get("cardType")))
					{
						customInfoEntity.setCardType(datamap.get("cardType"));
					}
					else
					{
						customInfoEntity.setCardType("1");
					}
					//先根据身份证号 查询 用户是否已经注册
					List<CustomInfoEntity> customInfoList = customInfoDao.findByIdentityCardAndCardType(datamap.get("identityCard"),customInfoEntity.getCardType());
					
					//存在 再 判断姓名是否一致
					if(null != customInfoList  &&  customInfoList.size() > 0)
					{
						boolean isNotMatch = true;
						
						
					
							outer: 
							for(CustomInfoEntity custom: customInfoList)
							{
								//身份证号与姓名是否一致
								if(customInfoEntity.getUserName().equals(custom.getUserName()))
								{
									
									
									customInfoEntity.setId(custom.getId());
									isNotMatch = false;
									//姓名与身份证号匹配 
									personCheckStatus = 1;
									//且身份证附件不为空 代表用户已经提交认证
									if(null != custom.getCAttachmentIdA() || null != custom.getCAttachmentIdB())
									{
										personCheckStatus = 2;
									}
									
									
									//途牛专用逻辑，如果审核全部通过了，就不能改变已经审核过后的身份证图片；如果当前身份证所注册的账号内，都没有审核通过则改变所有账号绑定的身份证图片的路径。
									if("4".equals(source)){
										
										
										List<IdentityEntity> identitys=identityDao.findByCCustomInfo(custom,(byte)0);
										
										if(identitys.size()>0 && null!=identitys){
											
											for(IdentityEntity tempIdentity : identitys){
									
												if((byte)1==tempIdentity.getIsAuthentic()){
													
													customInfoEntity.setCAttachmentIdA(custom.getCAttachmentIdA());
													customInfoEntity.setCAttachmentIdB(custom.getCAttachmentIdB());
														personCheckStatus = 3;
														break outer;
												}else{
														personCheckStatus = 1;
												}
											}
										}
										
										personCheckStatus = 1;
									}
									
									
									
									break;
								}
								
								
							}
					
						//不一致 返回错误提示
						if(isNotMatch)
						{
							throw new ServiceException(
									ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[0],
									ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[1],
									ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[2]);
						}
					}
				}
				//一致的话  判断传入的 身份证 正反面是否存在	
				try {
					if(personCheckStatus < 2)
					{
						//身份证号正、反面附件都存在
						if(isNotNull(datamap.get("idImgAPath")) && isNotNull(datamap.get("idImgBPath")))
						{
							//正面
							AttachmentEntity idImgAAttach = new AttachmentEntity();
							idImgAAttach.setAttachmentPath(datamap.get("idImgAPath"));
							idImgAAttach.setAttachmentName(datamap.get("idImgAName"));
							idImgAAttach.setAttachmentUri(datamap.get("idImgAPath"));
							idImgAAttach.setAttachmentThumbUri(datamap.get("idImgAPath"));
							idImgAAttach.setAttachmentExtension(datamap.get("idImgAExtension"));
							
							//如果是途牛，数据来源就是4
							if("4".equals(source)){
								idImgAAttach.setAttachmentSource((byte)4);
							}else{
								idImgAAttach.setAttachmentSource((byte)1);
							}
							//是否有效:"0"无效；"1”有效
							idImgAAttach.setAttachmentStatus((byte)1);
							//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
							idImgAAttach.setAttachmentType((byte)1);
							
							idImgAAttach = attachmentInfoDao.save(idImgAAttach);
							customInfoEntity.setCAttachmentIdA(idImgAAttach);
						
						    //身份证号反面
						
							AttachmentEntity idImgBAttach = new AttachmentEntity();
							idImgBAttach.setAttachmentPath(datamap.get("idImgBPath"));
							idImgBAttach.setAttachmentName(datamap.get("idImgBName"));
							idImgBAttach.setAttachmentUri(datamap.get("idImgBPath"));
							idImgBAttach.setAttachmentThumbUri(datamap.get("idImgBPath"));
							idImgBAttach.setAttachmentExtension(datamap.get("idImgBExtension"));
							
							//如果是途牛，数据来源就是4
							if("4".equals(source)){
								idImgBAttach.setAttachmentSource((byte)4);
							}else{
								idImgBAttach.setAttachmentSource((byte)1);
							}
							//是否有效:"0"无效；"1”有效
							idImgBAttach.setAttachmentStatus((byte)1);
							//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
							idImgBAttach.setAttachmentType((byte)1);
							
							idImgBAttach = attachmentInfoDao.save(idImgBAttach);
							customInfoEntity.setCAttachmentIdB(idImgBAttach);
							
							//存在 修改 原个人用户身份证正反面 
							customInfoEntity = customInfoDao.save(customInfoEntity);	
						}
					}	
				} catch (Exception e) {
					e.printStackTrace();
					//抛出异常  系统错误
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
				customInfoEntity = customInfoDao.saveAndFlush(customInfoEntity);
				//保存用户信息
				identityEntity.setCCustomInfo(customInfoEntity);
			}
			
			//企业用户 判断公司名是否存在
			if(2==identityEntity.getType() && isNotNull(datamap.get("companyName")))
			{
				CompanyInfoEntity companyInfoEntity = new CompanyInfoEntity();
				companyInfoEntity.setCompanyName(datamap.get("companyName"));
				
				//手工转成bean对象
				if(isNotNull(datamap.get("companyType")))
				{
					companyInfoEntity.setCompanyType(datamap.get("companyType"));
				}
				if(isNotNull(datamap.get("companyReseve1")))
				{
					companyInfoEntity.setReseve1(datamap.get("companyReseve1"));
				}
				
				//存在判断 营业执照号是否存在
				if(isNotNull(datamap.get("businessLicenseNo")))
				{
					companyInfoEntity.setBusinessLicenseNo(datamap.get("businessLicenseNo"));
					
					//企业用户需要判断企业名称与企业营业执照号是否存在且一致
					//先根据企业营业执照号 查询 公司是否已经注册
					List<CompanyInfoEntity> companyInfoList = companyInfoDao.findByBusinessLicenseNo(companyInfoEntity.getBusinessLicenseNo());
					if(null != companyInfoList  &&  companyInfoList.size() > 0)
					{
						boolean isNotMatch = true;
						for(CompanyInfoEntity company: companyInfoList)
						{
							//判定公司名称是否一致
							if(companyInfoEntity.getCompanyName().equals(company.getCompanyName()))
							{
								companyInfoEntity.setId(company.getId());
								isNotMatch = false;
								break;
							}
						}
						//不一致 返回错误提示
						if(isNotMatch)
						{
								throw new ServiceException(
										ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[0],
										ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[1],
										ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[2]);
						}
					}
				}
				//一致  判断 传入的营业执照附件是否存在
				//营业执照附件
				if(isNotNull(datamap.get("businessNoPath")))
				{
					AttachmentEntity businessAttach = new AttachmentEntity();
					businessAttach.setAttachmentPath(datamap.get("businessNoPath"));
					businessAttach.setAttachmentName(datamap.get("businessNoName"));
					businessAttach.setAttachmentUri(datamap.get("businessNoPath"));
					businessAttach.setAttachmentThumbUri(datamap.get("businessNoPath"));
					businessAttach.setAttachmentExtension(datamap.get("businessNoExtension"));
					businessAttach.setAttachmentSource((byte)1);
					//是否有效:"0"无效；"1”有效
					businessAttach.setAttachmentStatus((byte)1);
					//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
					businessAttach.setAttachmentType((byte)2);
					businessAttach = attachmentInfoDao.save(businessAttach);
					companyInfoEntity.setCAttachmentBusi(businessAttach);
				}
				//代理证书附件
				if(isNotNull(datamap.get("proxyPhotoPath")))
				{
					AttachmentEntity proxyAttach = new AttachmentEntity();
					proxyAttach.setAttachmentPath(datamap.get("proxyPhotoPath"));
					proxyAttach.setAttachmentName(datamap.get("proxyPhotoName"));
					proxyAttach.setAttachmentUri(datamap.get("proxyPhotoPath"));
					proxyAttach.setAttachmentExtension(datamap.get("proxyPhotoExtension"));
					proxyAttach.setAttachmentThumbUri(datamap.get("proxyPhotoPath"));
					proxyAttach.setAttachmentSource((byte)1);
					//是否有效:"0"无效；"1”有效
					proxyAttach.setAttachmentStatus((byte)1);
					//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
					proxyAttach.setAttachmentType((byte)2);
					proxyAttach = attachmentInfoDao.save(proxyAttach);
					companyInfoEntity.setCAttachmentPhoto(proxyAttach);
				}
				//保存公司信息
				companyInfoEntity = companyInfoDao.save(companyInfoEntity);
				identityEntity.setCCompanyInfo(companyInfoEntity);
			}
			
			
			//构造 账号默认数据
			// 用户类型:"1"个人；"2”企业；"3"平台
			identityEntity.setType(identityEntity.getType());
			// 用户状态:"1"停用；"0”使用
			identityEntity.setStatus((byte) 0);
			// 是否实名:"0"未激活；"1”已激活
			identityEntity.setIsAuthentic((byte) 0);
			
			
			// 用户来源:"4"途牛的用户

			identityEntity.setSource(Byte.parseByte(source));
		
		

			identityEntity.setBusinessAdmin("0");
			
			//根据规则 生成用户的uuid
			identityEntity.setUuid(toGeneralUUID(appId,identityEntity.getPlatformUserName(),identityEntity.getMobile()));
			//申请时间
			identityEntity.setRegistTime(new Date());
			
			identityEntity.setStatusTime(new Date());
			identityEntity.setCPlatform(platformEntity);
		
			identityEntity.setIsMobileVerified((byte)0);
			identityEntity.setIsEmailVerified((byte)0);
			//注册账号
			try {
				identityEntity = identityDao.save(identityEntity);
				
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			//读取平台 配置项-- 是否创建云签账号
			// 创建云签账号 必须是手机、邮箱不为空
			/*if(!"1".equals(platformEntity.getIsYunsignCreate()) 
					&& null != identityEntity.getEmail() && !"".equals(identityEntity.getEmail()) 
					&& null != identityEntity.getMobile() && !"".equals(identityEntity.getMobile()) )
			{

				yunsignRegister(identityEntity, appId);
			}*/
			return "";
		}
	
	
	/**
	 * 在平台注册账号，如果设置为一起注册云签账号，就一起注册云签账号
	 * @param identityEntity
	 * @param appId
	 * @throws ServiceException
	 */
	private String yunsignRegister(IdentityEntity identityEntity,String appId) throws ServiceException
	{
		PlatformEntity platformYunsignEntity = platformDao.findPlatformByAppId(defaultYunsignAPPID);
		String retStr = "";
		IdentityEntity checkIdentityYunsign = new IdentityEntity();
		try {
			byte source=identityEntity.getSource();
			//判断是否可以新增到云签下
			if((byte)1 == identityEntity.getType())
			{
				//个人用户 在云签平台下 手机号码不能重复
				
				if("4".equals(source) || 4==source){
					checkIdentityYunsign = identityDao.queryAppIdAndMobileAndTypeAndStatus(platformYunsignEntity,identityEntity.getMobile(),(byte)1,(byte)0);
					
				}else{
					checkIdentityYunsign = identityDao.queryAppIdAndMobileAndType(platformYunsignEntity,identityEntity.getMobile(),(byte)1);
				}
			}
			else
			{	
				if("4".equals(source) || 4==source){
					checkIdentityYunsign = identityDao.queryAppIdAndEmailAndTypeAndStatus(platformYunsignEntity,identityEntity.getEmail(),(byte)2,(byte)0);
				}else{
					//企业用户 在云签平台下  邮箱不能重复
					checkIdentityYunsign = identityDao.queryAppIdAndEmailAndType(platformYunsignEntity,identityEntity.getEmail(),(byte)2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		//判断用户是否重复注册
		if(null == checkIdentityYunsign )
		{
			IdentityEntity identityEntityYunsign = new IdentityEntity();
			//根据规则 生成用户的uuid
			identityEntityYunsign.setUuid(toGeneralUUID(defaultYunsignAPPID,"autoCreate",identityEntity.getMobile()));
			String account = identityEntity.getAccount()+"_"+new Date().getTime();
			identityEntityYunsign.setAccount(account);
			//新增之前 需要判断 云签账号的platfromUserName 是否重复 这涉及到 图章、合同等  必须唯一
			String platfromUserName = appId +"_" +identityEntity.getPlatformUserName();
			identityEntityYunsign.setPlatformUserName(platfromUserName);


			// 用户状态:"1"停用；"0”使用
			identityEntityYunsign.setStatus((byte) 0);
			// 是否实名:"0"未激活；"1”已激活
			identityEntityYunsign.setIsAuthentic((byte) 0);
			
			// 用户来源:"1"云签；"2”对接版；"3"本地版
			identityEntityYunsign.setSource((byte) 1);
			//对接企业用户 默认身份为非平台管理员账号
			identityEntityYunsign.setBusinessAdmin("0");
			
			//申请时间
			identityEntityYunsign.setRegistTime(new Date());
			
			identityEntityYunsign.setStatusTime(new Date());
		
			identityEntityYunsign.setIsMobileVerified((byte)0);
			identityEntityYunsign.setIsEmailVerified((byte)0);
			
			identityEntityYunsign.setMobile(identityEntity.getMobile());
			identityEntityYunsign.setEmail(identityEntity.getEmail());
			identityEntityYunsign.setPassword(identityEntity.getPassword());
			identityEntityYunsign.setType(identityEntity.getType());
			
			if(null != identityEntity.getCCompanyInfo())
			{
				identityEntityYunsign.setCCompanyInfo(identityEntity.getCCompanyInfo());
			}
			if(null != identityEntity.getCCustomInfo())
			{
				identityEntityYunsign.setCCustomInfo(identityEntity.getCCustomInfo());
			}
			identityEntityYunsign.setCPlatform(platformYunsignEntity);
			int update = 0;
			try{
				identityEntityYunsign = identityDao.save(identityEntityYunsign);
				
				//个人用户自动绑定
				if((byte)1 == identityEntityYunsign.getType() && null != identityEntityYunsign.getCCustomInfo())
				{
					//自动绑定
					update = identityDao.bangdingCustomAccount(identityEntityYunsign.getId(),identityEntityYunsign.getCCustomInfo().getId(), identityEntityYunsign.getMobile(),identityEntity.getId());
				}
				//企业用户自动绑定
				else if((byte)2 == identityEntityYunsign.getType() && null != identityEntityYunsign.getCCompanyInfo())
				{
					if(null != identityEntityYunsign.getCCustomInfo())
					{
						//自动绑定
						update = identityDao.bangdingCompanyAccount(identityEntityYunsign.getId(),identityEntityYunsign.getCCustomInfo().getId(),identityEntityYunsign.getCCompanyInfo().getId(), identityEntityYunsign.getEmail(),identityEntity.getId());
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(update >0)
			{
				retStr = "success";
			}
		}
		return retStr;
	}
	
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String updateUserPassword(Map<String,String> datamap) throws ServiceException{

		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		
		//查看用户账号或密码是否存在
		String password = (String) datamap.get("password");
		
		String newPassword = (String) datamap.get("newpassword");
		if(null == newPassword || "".equals(newPassword) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"平台新密码不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" newPassword is null!");
		}
		//走分支  如果原密码存在 则验证密码
		if(isNotNull(datamap.get("password")))
		{
			if(!password.equals(identityEntity.getPassword()))
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_LOGIN_ERROT[0],
						ConstantUtil.RETURN_LOGIN_ERROT[1], ConstantUtil.RETURN_LOGIN_ERROT[2]);
			}
		}
		try {
			String aesPassword = newPassword;
			try {
				  byte[] data=AES256Util.getInstance().encrypt(newPassword.getBytes()); 
				  aesPassword = Coder.encryptBASE64(data).replace("\r\n", "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<IdentityEntity> idList = new ArrayList<IdentityEntity>();
			//个人用户 根据客户ID 拉出所有的信息
			if(1 == identityEntity.getType())
			{
				idList= identityDao.queryIdentityByCCustomInfoList(identityEntity.getCCustomInfo().getId());
			}
			//企业用户 根据根据公司ID和客户ID 拉出所有的列表信息
			else
			{
				idList= identityDao.queryIdentityByCCompanyInfoAndCCustomInfoList(identityEntity.getCCompanyInfo().getId(),identityEntity.getCCustomInfo().getId());
			}
			//修改同一用户下 所有的密码
			for(IdentityEntity idenInfo : idList)
			{
				identityDao.updateIdentityPassword(aesPassword, idenInfo.getUuid());
			}
			
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		return "";
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String updateUserMobile(Map<String,String> datamap) throws ServiceException{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		
		//查看用户账号或密码是否存在
		String mobile = (String) datamap.get("mobile");
		if(null == mobile || "".equals(mobile) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"手机号码不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" mobile is null!");
		}
		IdentityEntity checkIdentity = null;	
		if(1 == identityEntity.getType())
		{
			//同一个平台下  手机不能重复
			try {
				checkIdentity = identityDao.queryAppIdAndMobileAndType(platformEntity,mobile,(byte)1);
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(null != checkIdentity)
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
						ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
			}
		}
		try {
			
			identityDao.updateIdentityMobile(mobile, identityEntity.getUuid());
			/*
			 * 原则上 3.0云签账号 是所有对接账号的集合  只能多不可以少  所有判断手机是否重复 判断一次就够了
			 */
			//更新所有绑定账号的手机
			List<IdentityEntity> list = identityDao.findByBindedId(identityEntity.getId());
			for(IdentityEntity ident :list)
			{
				identityDao.updateIdentityMobile(mobile, ident.getUuid());
			}
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String updateUserEmail(Map<String,String> datamap) throws ServiceException{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);

		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		
		//查看用户账号或密码是否存在
		String type = (String) datamap.get("type");
		if(null == type || "".equals(type))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"变更类型不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" type is null!");
		}
		//查看用户账号或密码是否存在
		String param = (String) datamap.get("param");
		if(null == param || "".equals(param) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"邮箱或邮箱验证状态不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" email or email status is null!");
		}
		//1  邮箱变更  2 邮箱状态变更
		if("1".equals(type))
		{
			IdentityEntity identityEntity2 = null;
			try {
				identityEntity2 = identityDao.queryAppIdAndEmailAndType(platformEntity,param,(byte)2);
			} catch (Exception e) {
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(null != identityEntity2)
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_EMAIL_EXIST[0],
						ConstantUtil.RETURN_EMAIL_EXIST[1], ConstantUtil.RETURN_EMAIL_EXIST[2]);
			}
			try {
				identityDao.updateIdentityEmail(param, identityEntity.getUuid());
				/*
				 * 原则上 3.0云签账号 是所有对接账号的集合  只能多不可以少  所有判断邮件是否重复 判断一次就够了
				 */
				//更新所有绑定账号的手机
				List<IdentityEntity> list = identityDao.findByBindedId(identityEntity.getId());
				for(IdentityEntity ident :list)
				{
					identityDao.updateIdentityEmail(param, ident.getUuid());
				}
			} catch (Exception e) {
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
		}
		else if("2".equals(type))
		{
			try {
				identityDao.updateIdentityEmailStatus(param, identityEntity.getUuid());
			} catch (Exception e) {
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
		}
		return "";
	}

	//更新公司信息
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String updateCompanyInfo(Map<String,String> datamap)throws ServiceException {
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);

		if(isNotNull(datamap.get("email")))
		{
			//判断邮箱在本平台下是否已经适用
			if(!datamap.get("email").equals(identityEntity.getEmail()))
			{
				//个人用户 看邮箱是否重复
				IdentityEntity identityEntity2 = null;
				try {
					identityEntity2 = identityDao.queryAppIdAndEmailAndType(platformEntity,datamap.get("email"),(byte)2);
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}		
				if(null != identityEntity2)
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
							ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
				}		
				try {
					identityDao.updateIdentityEmail(datamap.get("email"),identityEntity.getUuid());
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}		
			}
		}
		if(isNotNull(datamap.get("mobile")))
		{
			identityEntity.setMobile(datamap.get("mobile"));
			//判断手机号在本平台下是否已经使用
			if(!datamap.get("mobile").equals(identityEntity.getMobile()))
			{
				//个人用户 看邮箱是否重复
				IdentityEntity identityEntity2 = null;
				try {
					identityEntity2 = identityDao.queryAppIdAndMobileAndType(platformEntity,datamap.get("mobile"),(byte)1);
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}		
				if(null != identityEntity2)
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
							ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
				}		
				try {
					identityDao.updateIdentityMobile(datamap.get("mobile"),identityEntity.getUuid());
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}		
			}
		}
		
		CustomInfoEntity customInfoEntity = identityEntity.getCCustomInfo();
		if(null == customInfoEntity)
		{
			customInfoEntity = new CustomInfoEntity();
		}
		CompanyInfoEntity companyInfoEntity = new CompanyInfoEntity();
		//手工转成bean对象
		if(isNotNull(datamap.get("businessLicenseNo")))
		{
			companyInfoEntity.setBusinessLicenseNo(datamap.get("businessLicenseNo"));
		}
		if(isNotNull(datamap.get("companyName")))
		{
			companyInfoEntity.setCompanyName(datamap.get("companyName"));
		}
		if(isNotNull(datamap.get("companyType")))
		{
			companyInfoEntity.setCompanyType(datamap.get("companyType"));
		}
		if(isNotNull(datamap.get("reseve1")))
		{
			companyInfoEntity.setReseve1(datamap.get("reseve1"));
		}
		if(isNotNull(datamap.get("userName")))
		{
			customInfoEntity.setUserName(datamap.get("userName"));
			//判断邮箱在本平台下是否已经适用
		}
		if(isNotNull(datamap.get("identityCard")))
		{
			customInfoEntity.setIdentityCard(datamap.get("identityCard"));
			//判断邮箱在本平台下是否已经适用
		}
		customInfoEntity.setAppId(appId);
		//判断传入参数是否完整
		String companyCheck = companyInfoEntity.isBeanLegal();
		if(!"".equals(companyCheck))
		{
			throw new ServiceException(
					ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+companyCheck,
					ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
		}
		companyInfoEntity.setId(identityEntity.getCCompanyInfo().getId());
		
		try {
			companyInfoDao.save(companyInfoEntity);
			//如果用户没有客户信息  添加后关联到账户表中
			if(0 == customInfoEntity.getId())
			{
				customInfoEntity = customInfoDao.save(customInfoEntity);
				identityEntity.setCCustomInfo(customInfoEntity);
			}
			else
			{
				customInfoEntity = customInfoDao.save(customInfoEntity);
			}
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		return "";
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String updateCustomInfo(Map<String,String> datamap)throws ServiceException  {
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);

		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);

		if(isNotNull(datamap.get("email")))
		{
			//判断邮箱在本平台下是否已经适用
			if(!datamap.get("email").equals(identityEntity.getEmail()))
			{
				//个人用户 看邮箱是否重复
				IdentityEntity identityEntity2 = null;
				try {
					identityEntity2 = identityDao.queryAppIdAndEmailAndType(platformEntity,datamap.get("email"),(byte)1);
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}		
				if(null != identityEntity2)
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
							ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
				}		
				try {
					identityDao.updateIdentityEmail(datamap.get("email"),identityEntity.getUuid());
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}		
			}
		}
		if(isNotNull(datamap.get("mobile")))
		{
			identityEntity.setMobile(datamap.get("mobile"));
			//判断手机号在本平台下是否已经使用
			if(!datamap.get("mobile").equals(identityEntity.getMobile()))
			{
				//个人用户 看邮箱是否重复
				IdentityEntity identityEntity2 = null;
				try {
					identityEntity2 = identityDao.queryAppIdAndMobileAndType(platformEntity,datamap.get("mobile"),(byte)1);
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}		
				if(null != identityEntity2)
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
							ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
				}		
				try {
					identityDao.updateIdentityMobile(datamap.get("mobile"),identityEntity.getUuid());
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}		
			}
		}
		CustomInfoEntity customInfoEntity = new CustomInfoEntity();

		customInfoEntity.setAppId(appId);
		//手工转成bean对象
		if(isNotNull(datamap.get("userName")))
		{
			customInfoEntity.setUserName(datamap.get("userName"));
		}
		if(isNotNull(datamap.get("nickname")))
		{
			customInfoEntity.setNickname(datamap.get("nickname"));
		}
		if(isNotNull(datamap.get("phoneNum")))
		{
			customInfoEntity.setPhoneNum(datamap.get("phoneNum"));
		}
		if(isNotNull(datamap.get("identityCard")))
		{
			customInfoEntity.setIdentityCard(datamap.get("identityCard"));
		}
		if(isNotNull(datamap.get("duty")))
		{
			customInfoEntity.setDuty(datamap.get("duty"));
		}
		if(isNotNull(datamap.get("reseve1")))
		{
			customInfoEntity.setReseve1(datamap.get("reseve1"));
		}
		if(isNotNull(datamap.get("reseve2")))
		{
			customInfoEntity.setReseve2(datamap.get("reseve2"));
		}
		if(isNotNull(datamap.get("address")))
		{
			customInfoEntity.setAddress(datamap.get("address"));
		}

		customInfoEntity.setId(identityEntity.getCCustomInfo().getId());
		
		//判断传参是够完整
		String customCheck = customInfoEntity.isBeanLegal();
		if( !"".equals(customCheck))
		{
			throw new ServiceException(
					ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+customCheck,
					ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
		}

		if(isNotNull(datamap.get("password")))
		{
			identityEntity.setPassword(datamap.get("password"));
		}
		String mobile = datamap.get("mobile");
		String email = datamap.get("email");
		
		IdentityEntity identityEntity2 = null;
		//个人用户验证手机号码
		if("1".equals(datamap.get("type")))
		{
			if(!mobile.equals(identityEntity.getMobile()))
			{
				//个人用户 看手机号码是否重复
				try {
					identityEntity2 = identityDao.queryAppIdAndMobileAndType(platformEntity,mobile,(byte)1);
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}			
				if(null != identityEntity2)
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
							ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
				}		
			}
		}
		//企业用户验证邮箱
		else
		{
			if(!email.equals(identityEntity.getEmail()))
			{
				//个人用户 看手机号码是否重复
				try {
					identityEntity2 = identityDao.queryAppIdAndEmailAndType(platformEntity,email,(byte)1);
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}		
				if(null != identityEntity2)
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_EMAIL_EXIST[0],
							ConstantUtil.RETURN_EMAIL_EXIST[1], ConstantUtil.RETURN_EMAIL_EXIST[2]);					
				}
			}
		}
		
		identityEntity.setMobile(mobile);
		identityEntity.setEmail(email);
		
		//保存用户资料信息
		try {
			customInfoEntity = customInfoDao.save(customInfoEntity);
			identityEntity.setCCustomInfo(customInfoEntity);
			identityEntity = identityDao.save(identityEntity);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}


	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String updateUserAdmin(Map<String,String> datamap) throws ServiceException {
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);

		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		
		//查看用户账号或密码是否存在
		String isAdminStr = (String) datamap.get("isAdmin");
		if(null == isAdminStr || "".equals(isAdminStr) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"管理员标识不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" isAdmin is null!");
		}
		int isAdmin;
		try {
			isAdmin = Integer.parseInt(isAdminStr);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"管理员标识转换错误", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" isAdmin is not correct!");
		}
		try {
			identityDao.updateIdentityIsAdmin(isAdmin, identityEntity.getUuid());
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String bandingMajorAccount(Map<String,String> datamap) throws ServiceException {
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		//查看用户主账号或平台主用户名称是否存在
		String bangdingUserId = (String) datamap.get("bangdingUserId");
		int userId = Integer.parseInt(bangdingUserId);
		IdentityEntity majorIdentityEntity =  checkYunsignIdentityEntityByUserId(userId);
		
		try {
			identityDao.updateIdentityBindedId(identityEntity.getId(),majorIdentityEntity.getId());
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}
	
	//用户查询
	@Transactional
	public String userQuery(Map<String,String> datamap)throws ServiceException{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		IdentityEntity identityEntity = null;
		if(isNotNull(datamap.get("wxOpenId")))
		{
			//根据微信编号查询用户资料
			String wxOpenId = (String) datamap.get("wxOpenId");
			identityEntity = checkIdentityEntityByOpenId(wxOpenId,platformEntity);
		}
		else if(isNotNull(datamap.get("userId")))
		{
			identityEntity = identityDao.findById(Integer.parseInt( datamap.get("userId")));
		}
		else
		{
			//查看用户账号或平台用户名称是否存在
			String userAccount = (String) datamap.get("account");
			String platformUserName = (String) datamap.get("platformUserName");
			identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		}
		return toJSONStrFromIdentity(identityEntity);
	}
	/**
	 * 根据手机号查询用户信息
	 */
	@Override
	public String userQueryByMobile(Map<String,String> datamap)throws ServiceException{
		//根据手机号查询用户
		String retStr = "";
		String mobile = StringUtil.nullToString(datamap.get("mobile"));
		String appId = StringUtil.nullToString(datamap.get("appId"));
		
		try
		{				
			PlatformEntity platformEntity = checkPlatform(appId);
			List<IdentityEntity> listIdentity = identityDao.queryUserByMobile(mobile,platformEntity);
			if(null != listIdentity && !listIdentity.isEmpty())
			{
				IdentityEntity identityEntity = listIdentity.get(0);
				retStr = toJSONStrFromIdentity(identityEntity);
			}
			else
			{
				log.info("用户不存在");
				throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
			}
		}catch(ServiceException e)
		{
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch(Exception e)
		{
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}		
		return retStr;
	}
	//用户实名认证激活
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String userActivat(Map<String,String> datamap)throws ServiceException{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		IdentityEntity identityEntity = checkIdentityEntityAnd(userAccount,platformEntity);

		String isAuthentic = (String) datamap.get("isAuthentic");
		//实名认证时  变更为 企业管理员  且 开通签约室 且签约室状态 通过
		try {
			if("1".equals(isAuthentic))
			{
				//认证状态 :"0" 未认证；"1”已认证 ；"2” 认证不通过
				identityDao.updateIdentityAuthenticPass(identityEntity.getUuid());
				//开通用户签约室
				SigningRoomEntity signingRoomEntity = new SigningRoomEntity();
				//手机号码
				signingRoomEntity.setLinktel(identityEntity.getMobile());
				//补充参数
				signingRoomEntity.setPlatform(platformEntity.getId());
				//创建时间
				signingRoomEntity.setDateline(new Date());
				//用户ID
				signingRoomEntity.setUserId(identityEntity.getId());
				//状态 ："0”表示使用，"1”表示停用
				signingRoomEntity.setStatus((byte)1);
				
				//用户姓名
				signingRoomEntity.setLinkname(identityEntity.getCCustomInfo().getUserName());
				
				//公司用户 公司名称
				if(2 == identityEntity.getType())
				{
					signingRoomEntity.setEmail(identityEntity.getEmail());
					signingRoomEntity.setCompanyName(identityEntity.getCCompanyInfo().getCompanyName());
					//变更为企业管理员 且设置权限 111
					identityEntity.setIsAdmin((byte)1);
					//保存权限信息
					RoleEntity roleEntity = roleDao.findByRoleName("CA111");
					if(null != roleEntity)
					{
						UserRoleRelationEntity userRoleRelationEntity = userRoleRelationDao.findByUserId(identityEntity.getId());
						if(null == userRoleRelationEntity)
						{
							userRoleRelationEntity = new UserRoleRelationEntity();
							userRoleRelationEntity.setRoleId(roleEntity.getId());
							userRoleRelationEntity.setUserId(identityEntity.getId());
							userRoleRelationDao.save(userRoleRelationEntity);
						}
					}
				}
				signingRoomEntity = signingRoomDao.save(signingRoomEntity);
				//更新签约室信息
				identityEntity.setCSigningRoom(signingRoomEntity);
				identityEntity.setIsAuthentic((byte)1);
				identityDao.save(identityEntity);
			}
			else if("2".equals(isAuthentic))
			{
				//认证状态 :"0" 未认证；"1”已认证 ；"2” 认证不通过
				identityDao.updateIdentityAuthenticNotPass(identityEntity.getUuid());
			}
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}
	
	//用户注销
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String userLogOut(Map<String,String> datamap)throws ServiceException{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		try {
			//修改用户状态 :"0"停用；"1”可用
			identityDao.updateIdentityState(0, identityEntity.getUuid());
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String saveYunsignIdentity(Map<String,String> datamap) throws ServiceException {
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		IdentityEntity identityEntity = new IdentityEntity();
		// 转成bean对象
		try {
			BeanUtils.populate(identityEntity, datamap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			// 抛出异常 参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1], e.getMessage());
		}

		//判断传参是够完整
		String identCheck = identityEntity.isBeanLegal();
		if(!"".equals(identCheck))
		{
			throw new ServiceException(
					ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+identCheck,
					ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
		}
		
		IdentityEntity identityEntity2 = null;
		//个人用户 验证手机号码是够重复
		if(1 == identityEntity.getType())
		{
			//判断该手机号码是否已经注册过用户
			try {
				identityEntity2 = identityDao.queryAppIdAndMobileAndType(platformEntity,identityEntity.getMobile(),identityEntity.getType());
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(null != identityEntity2)
			{
				//抛出异常   账号或用户ID已存在
				throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
						ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
			}
		}
		//企业用户验证邮箱是否重复
		else if(2 == identityEntity.getType())
		{
			//判断该手机号码是否已经注册过用户
			try {
				identityEntity2 = identityDao.queryAppIdAndEmailAndType(platformEntity,identityEntity.getEmail(),identityEntity.getType());
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			/*if(null != identityEntity2)
			{
				//抛出异常   账号或用户ID已存在
				throw new ServiceException(ConstantUtil.RETURN_EMAIL_EXIST[0],
						ConstantUtil.RETURN_EMAIL_EXIST[1], ConstantUtil.RETURN_EMAIL_EXIST[2]);
			}*/
			
			//企业用户 判断公司名是否存在
			if(isNotNull(datamap.get("companyName")))
			{
				CompanyInfoEntity companyInfoEntity = new CompanyInfoEntity();
				companyInfoEntity.setCompanyName(datamap.get("companyName"));
				
				//手工转成bean对象
				if(isNotNull(datamap.get("companyType")))
				{
					companyInfoEntity.setCompanyType(datamap.get("companyType"));
				}
				if(isNotNull(datamap.get("companyReseve1")))
				{
					companyInfoEntity.setReseve1(datamap.get("companyReseve1"));
				}
				
				//存在判断 营业执照号是否存在
				if(isNotNull(datamap.get("businessLicenseNo")))
				{
					companyInfoEntity.setBusinessLicenseNo(datamap.get("businessLicenseNo"));
					
					//企业用户需要判断企业名称与企业营业执照号是否存在且一致
					//先根据企业营业执照号 查询 公司是否已经注册
					List<CompanyInfoEntity> companyInfoList = companyInfoDao.findByBusinessLicenseNo(companyInfoEntity.getBusinessLicenseNo());
					if(null != companyInfoList  &&  companyInfoList.size() > 0)
					{
						boolean isNotMatch = true;
						for(CompanyInfoEntity company: companyInfoList)
						{
							//判定公司名称是否一致
							if(companyInfoEntity.getCompanyName().equals(company.getCompanyName()))
							{
								companyInfoEntity.setId(company.getId());
								isNotMatch = false;
								break;
							}
						}
						//不一致 返回错误提示
						if(isNotMatch)
						{
								throw new ServiceException(
										ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[0],
										ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[1],
										ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[2]);
						}
					}
				}
				//一致  判断 传入的营业执照附件是否存在
				//营业执照附件
				if(isNotNull(datamap.get("businessNoPath")))
				{
					AttachmentEntity businessAttach = new AttachmentEntity();
					businessAttach.setAttachmentPath(datamap.get("businessNoPath"));
					businessAttach.setAttachmentName(datamap.get("businessNoName"));
					businessAttach.setAttachmentUri(datamap.get("businessNoPath"));
					businessAttach.setAttachmentThumbUri(datamap.get("businessNoPath"));
					businessAttach.setAttachmentExtension(datamap.get("businessNoExtension"));
					businessAttach.setAttachmentSource((byte)1);
					//是否有效:"0"无效；"1”有效
					businessAttach.setAttachmentStatus((byte)1);
					//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
					businessAttach.setAttachmentType((byte)2);
					businessAttach = attachmentInfoDao.save(businessAttach);
					companyInfoEntity.setCAttachmentBusi(businessAttach);
				}
				//代理证书附件
				if(isNotNull(datamap.get("proxyPhotoPath")))
				{
					AttachmentEntity proxyAttach = new AttachmentEntity();
					proxyAttach.setAttachmentPath(datamap.get("proxyPhotoPath"));
					proxyAttach.setAttachmentName(datamap.get("proxyPhotoName"));
					proxyAttach.setAttachmentUri(datamap.get("proxyPhotoPath"));
					proxyAttach.setAttachmentExtension(datamap.get("proxyPhotoExtension"));
					proxyAttach.setAttachmentThumbUri(datamap.get("proxyPhotoPath"));
					proxyAttach.setAttachmentSource((byte)1);
					//是否有效:"0"无效；"1”有效
					proxyAttach.setAttachmentStatus((byte)1);
					//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
					proxyAttach.setAttachmentType((byte)2);
					proxyAttach = attachmentInfoDao.save(proxyAttach);
					companyInfoEntity.setCAttachmentPhoto(proxyAttach);
				}
				//保存公司信息
				companyInfoEntity = companyInfoDao.save(companyInfoEntity);
				identityEntity.setCCompanyInfo(companyInfoEntity);
			}
			
			
		}
		else
		{
			//抛出异常   
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"用户类型错误", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+"type is not correct");
		}
		
		if(isNotNull(datamap.get("userName")))
		{
			CustomInfoEntity customInfoEntity = new CustomInfoEntity();
			//手工转成bean对象
			customInfoEntity.setUserName(datamap.get("userName"));
			
			
			customInfoEntity.setAppId(appId);
			if(isNotNull(datamap.get("identityCard")))
			{
				customInfoEntity.setIdentityCard(datamap.get("identityCard"));
				//判断邀约用户是否已经提交身份资料
				int personCheckStatus = 0;
				
				//取证件类型 没有没有则 默认为身份证 1
				customInfoEntity.setIdentityCard(datamap.get("identityCard"));
				if(isNotNull(datamap.get("cardType")))
				{
					customInfoEntity.setCardType(datamap.get("cardType"));
				}
				else
				{
					customInfoEntity.setCardType("1");
				}
				//先根据身份证号 查询 用户是否已经注册
				List<CustomInfoEntity> customInfoList = customInfoDao.findByIdentityCardAndCardType(datamap.get("identityCard"),customInfoEntity.getCardType());
				
				if(null != customInfoList  &&  customInfoList.size() > 0)
				{
					boolean isNotMatch = true;
					for(CustomInfoEntity custom: customInfoList)
					{
						//身份证号与姓名是否一致
						if(customInfoEntity.getUserName().equals(custom.getUserName()))
						{
							customInfoEntity.setId(custom.getId());
							isNotMatch = false;
							//姓名与身份证号匹配 
							personCheckStatus = 1;
							//且身份证附件不为空 代表用户已经提交认证
							if(null != custom.getCAttachmentIdA() || null != custom.getCAttachmentIdB())
							{
								personCheckStatus = 2;
							}
							break;
						}
					}
					if(isNotMatch)
					{
						throw new ServiceException(
								ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[0],
								ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[1],
								ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[2]);
					}
				}
				
				
				if(personCheckStatus < 2)
				{
					//保存用户信息 可能只是一个姓名
					try {
						customInfoEntity = customInfoDao.save(customInfoEntity);
						identityEntity.setCCustomInfo(customInfoEntity);
					} catch (Exception e) {
						e.printStackTrace();
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
				}
			}
			customInfoEntity = customInfoDao.save(customInfoEntity);
			identityEntity.setCCustomInfo(customInfoEntity);
		}
		// 补充默认参数
		// 用户类型:"1"个人；"2”企业；"3"平台
		//identityEntity.setType((byte) 3);
		// 用户状态:"1"停用；"0”可用
		identityEntity.setStatus((byte) 0);
		// 是否实名:"0"未激活；"1”已激活
		identityEntity.setIsAuthentic((byte) 0);
		// 用户来源:"1"云签；"2”对接版；"3"本地版
		identityEntity.setSource((byte) 1);
		//是否业务管理员权限
		identityEntity.setBusinessAdmin("0");
		//根据规则 生成用户的uuid
		identityEntity.setUuid(toGeneralUUID(ConstantUtil.DEFAULT_YUNSIGN_APP_ID,identityEntity.getPlatformUserName(),identityEntity.getMobile()));
		//申请时间
		identityEntity.setRegistTime(new Date());
		
		identityEntity.setStatusTime(new Date());

		identityEntity.setIsMobileVerified((byte)1);
		identityEntity.setIsEmailVerified((byte)0);
		identityEntity.setCPlatform(platformEntity);
		try {
			identityEntity = identityDao.save(identityEntity);
		} catch (Exception e) {
			e.printStackTrace();
			// 抛出异常 系统错误
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		//为邀约人增加诚信值
		if(isInvited)
		{
			//判断邀约人ID是否存在
			if(isNotNull(datamap.get("invitorId")))
			{
				IdentityInviteEntity identityInviteEntity = new IdentityInviteEntity();
				identityInviteEntity.setInvitorId(Integer.parseInt(datamap.get("invitorId")));
				identityInviteEntity.setInvitedId(identityEntity.getId());
				identityInviteEntity.setInviteTime(new Date());
				identityInviteEntity.setPoint(50);
				identityInviteInfoDao.save(identityInviteEntity);
			}
		}
		
		return "";
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String updateYunsignCompanyInfo(Map<String, String> datamap)
			throws ServiceException {
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看注册手机号码和用户来源查询 该云签用户是否存在
		String account = (String) datamap.get("account");
		IdentityEntity identityEntity = checkYunsignIdentityEntity(platformEntity,account);

		if(null == identityEntity.getCCompanyInfo())
		{
			throw new ServiceException(ConstantUtil.RETURN_COMP_NOT_EXIST[0],
					ConstantUtil.RETURN_COMP_NOT_EXIST[1], ConstantUtil.RETURN_COMP_NOT_EXIST[2]);
		}
		
		if(null == identityEntity.getCCustomInfo())
		{
			throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],
					ConstantUtil.RETURN_CUST_NOT_EXIST[1], ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
		} 
		CompanyInfoEntity companyInfo = identityEntity.getCCompanyInfo();
		CustomInfoEntity  customInfo = identityEntity.getCCustomInfo();
		//企业信息修改 只修改四项  1、营业执照附件 2、代理证书附件 3、代理人身份证正面附件 4、代理人身份证背面附件
		
		AttachmentEntity attachmentEntity = null;
		//代理证书
		if(isNotNull(datamap.get("proxyPhotoPath")))
		{
			attachmentEntity = new AttachmentEntity();
			attachmentEntity.setAttachmentName( datamap.get("proxyPhotoName"));
			attachmentEntity.setAttachmentUri( datamap.get("proxyPhotoPath"));
			attachmentEntity.setAttachmentExtension( datamap.get("proxyPhotoExtension"));
			//图片来源:"1"账号申请；"2”签约室申请；"3"平台申请；
			attachmentEntity.setAttachmentSource((byte)1);
			//是否有效:"0"无效；"1”有效
			attachmentEntity.setAttachmentStatus((byte)1);
			//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
			attachmentEntity.setAttachmentType((byte)4);
			attachmentEntity = attachmentInfoDao.save(attachmentEntity);
			companyInfo.setCAttachmentPhoto(attachmentEntity);
		}
			
		//营业执照
		if(isNotNull(datamap.get("businessNoPath")))
		{
			attachmentEntity = new AttachmentEntity();
			attachmentEntity.setAttachmentName( datamap.get("businessNoName"));
			attachmentEntity.setAttachmentUri( datamap.get("businessNoPath"));
			attachmentEntity.setAttachmentExtension( datamap.get("businessNoExtension"));
			//图片来源:"1"账号申请；"2”签约室申请；"3"平台申请；
			attachmentEntity.setAttachmentSource((byte)1);
			//是否有效:"0"无效；"1”有效
			attachmentEntity.setAttachmentStatus((byte)1);
			//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
			attachmentEntity.setAttachmentType((byte)2);
			attachmentEntity = attachmentInfoDao.save(attachmentEntity);
			companyInfo.setCAttachmentBusi(attachmentEntity);
		}
		
		//判断  用户身份证信息是否存在
		if(isNotNull(datamap.get("idImgAPath")))
		{
			attachmentEntity = new AttachmentEntity();
			attachmentEntity.setAttachmentName( datamap.get("idImgAName"));
			attachmentEntity.setAttachmentUri( datamap.get("idImgAPath"));
			attachmentEntity.setAttachmentExtension( datamap.get("idImgAExtension"));
			//图片来源:"1"账号申请；"2”签约室申请；"3"平台申请；
			attachmentEntity.setAttachmentSource((byte)1);
			//是否有效:"0"无效；"1”有效
			attachmentEntity.setAttachmentStatus((byte)1);
			//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
			attachmentEntity.setAttachmentType((byte)1);
			attachmentEntity = attachmentInfoDao.save(attachmentEntity);
			customInfo.setCAttachmentIdA(attachmentEntity);
		}
		//判断  用户身份证信息是否存在
		if(isNotNull(datamap.get("idImgBPath")))
		{
			attachmentEntity = new AttachmentEntity();
			attachmentEntity.setAttachmentName( datamap.get("idImgBName"));
			attachmentEntity.setAttachmentUri( datamap.get("idImgBPath"));
			attachmentEntity.setAttachmentExtension( datamap.get("idImgBExtension"));
			//图片来源:"1"账号申请；"2”签约室申请；"3"平台申请；
			attachmentEntity.setAttachmentSource((byte)1);
			//是否有效:"0"无效；"1”有效
			attachmentEntity.setAttachmentStatus((byte)1);
			//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
			attachmentEntity.setAttachmentType((byte)1);
			attachmentEntity = attachmentInfoDao.save(attachmentEntity);
			customInfo.setCAttachmentIdB(attachmentEntity);
		}
		
		try {
			companyInfoDao.save(companyInfo);
			
			customInfoDao.save(customInfo);
			//重置实名认证状态
			identityDao.updateIdentityAuthenticWaitCheck(identityEntity.getUuid());
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		return "";
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String updateYunsignCustomInfo(Map<String, String> datamap)
			throws ServiceException {
		//查看注册手机号码和用户来源查询 该云签用户是否存在
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看注册手机号码和用户来源查询 该云签用户是否存在
		String account = (String) datamap.get("account");
		IdentityEntity identityEntity = checkYunsignIdentityEntity(platformEntity,account);
		
		if(null == identityEntity.getCCustomInfo())
		{
			throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],
					ConstantUtil.RETURN_CUST_NOT_EXIST[1], ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
		} 
		CustomInfoEntity  customInfo = identityEntity.getCCustomInfo();
		
		AttachmentEntity attachmentEntity = null;
		
		//判断  用户身份证信息是否存在
		if(isNotNull(datamap.get("idImgAPath")))
		{
			attachmentEntity = new AttachmentEntity();
			attachmentEntity.setAttachmentName( datamap.get("idImgAName"));
			attachmentEntity.setAttachmentUri( datamap.get("idImgAPath"));
			attachmentEntity.setAttachmentExtension( datamap.get("idImgAExtension"));
			//图片来源:"1"账号申请；"2”签约室申请；"3"平台申请；
			attachmentEntity.setAttachmentSource((byte)1);
			//是否有效:"0"无效；"1”有效
			attachmentEntity.setAttachmentStatus((byte)1);
			//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
			attachmentEntity.setAttachmentType((byte)1);
			attachmentEntity = attachmentInfoDao.save(attachmentEntity);
			customInfo.setCAttachmentIdA(attachmentEntity);
		}
		//判断  用户身份证信息是否存在
		if(isNotNull(datamap.get("idImgBPath")))
		{
			attachmentEntity = new AttachmentEntity();
			attachmentEntity.setAttachmentName( datamap.get("idImgBName"));
			attachmentEntity.setAttachmentUri( datamap.get("idImgBPath"));
			attachmentEntity.setAttachmentExtension( datamap.get("idImgBExtension"));
			//图片来源:"1"账号申请；"2”签约室申请；"3"平台申请；
			attachmentEntity.setAttachmentSource((byte)1);
			//是否有效:"0"无效；"1”有效
			attachmentEntity.setAttachmentStatus((byte)1);
			//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
			attachmentEntity.setAttachmentType((byte)1);
			attachmentEntity = attachmentInfoDao.save(attachmentEntity);
			customInfo.setCAttachmentIdB(attachmentEntity);
		}
		
		//保存用户资料信息
		try {
			customInfoDao.save(customInfo);
			//重置实名认证状态
			identityDao.updateIdentityAuthenticWaitCheck(identityEntity.getUuid());
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}
	
	//根据手机号码查询个人用户是否存在
	public String getCustomByMobile(Map<String,String> datamap)throws ServiceException
	{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		 //查看用户账号或平台用户名称是否存在
		if(null == datamap.get("mobile") || "".equals(datamap.get("mobile" )))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"手机号码不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" mobile is null!");
		}
		//查看用户账号或平台用户名称是否存在
		if(null == datamap.get("type") || "".equals(datamap.get("type" )))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"用户类型不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" type is null!");
		}
		
		IdentityEntity identityEntity = new IdentityEntity();
		try {
			identityEntity = identityDao.queryAppIdAndMobileAndType(platformEntity,datamap.get("mobile" ),(byte)1);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return toJSONStrFromIdentity(identityEntity);
	}
	
	//根据邮箱查询企业用户是否存在
	public String getCompanyByEmail(Map<String,String> datamap)throws ServiceException
	{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		 //查看用户账号或平台用户名称是否存在
		if(null == datamap.get("email") || "".equals(datamap.get("email" )))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"邮箱不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" email is null!");
		}
		//查看用户账号或平台用户名称是否存在
		if(null == datamap.get("type") || "".equals(datamap.get("type" )))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"用户类型不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" type is null!");
		}
			
		IdentityEntity identityEntity = new IdentityEntity();
		try {
			identityEntity = identityDao.queryAppIdAndEmailAndType(platformEntity,datamap.get("email" ),(byte)2);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		return toJSONStrFromIdentity(identityEntity);
	}
	
	@Override
	public String queryAllUser(Map<String,String> datamap)throws ServiceException
	{
		List<IdentityEntity> datalist =new ArrayList<IdentityEntity>();
		int page = 0;
	    int pageSize = 0;
		//分页标识
		if(null == datamap.get("isPage"))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_PAGE_ERROR[0],
					ConstantUtil.RETURN_PAGE_ERROR[1], ConstantUtil.RETURN_PAGE_ERROR[2]);
		}
		String	isPage = datamap.get("isPage");
		if("1".equals(isPage))
		{
			//分页明细  当前页与每页大小
			if(null == datamap.get("currentPage") || null == datamap.get("pageSize"))
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_PAGEINFO_ERROR[0],
						ConstantUtil.RETURN_PAGEINFO_ERROR[1], ConstantUtil.RETURN_PAGEINFO_ERROR[2]);
			}

			String currentPageStr = datamap.get("currentPage");
			String pageSizeStr = datamap.get("pageSize");
		    try {
		    	page = Integer.parseInt(currentPageStr);
			    pageSize = Integer.parseInt(pageSizeStr);
			} catch (Exception e) {
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_PAGEINFO_ERROR[0],
						ConstantUtil.RETURN_PAGEINFO_ERROR[1], ConstantUtil.RETURN_PAGEINFO_ERROR[2]);
			}
	 
		   //封装 分页参数
	      Pageable pageable = new PageRequest(page,pageSize);
	      pageable.getSort();
	      //获取分页列表
		  Page<IdentityEntity> list =  identityDao.findAll(pageable);
		  datalist = list.getContent();
		}
		//不分页 查询全部列表
		else
		{
			datalist = (List<IdentityEntity>) identityDao.findAll();
		}
		//组装成JS对象输出
		JSONArray jsonArray = new JSONArray();
		for(IdentityEntity i : datalist)
		{
			jsonArray.add(Bean2JSON(i));
		}
		JSONObject jo = new JSONObject();
		jo.put("list", jsonArray);
		return jo.toString();
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String openSigningRoom(Map<String, String> datamap)
			throws ServiceException {
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		
		SigningRoomEntity signingRoomEntity = new SigningRoomEntity();
		//手机号码
		signingRoomEntity.setLinktel(identityEntity.getMobile());
		//补充参数
		signingRoomEntity.setPlatform(platformEntity.getId());
		//创建时间
		signingRoomEntity.setDateline(new Date());
		//用户ID
		signingRoomEntity.setUserId(identityEntity.getId());
		//状态
		signingRoomEntity.setStatus((byte)0);
		//个人用户 用户姓名
		if(1 == identityEntity.getType())
		{
			signingRoomEntity.setLinkname(identityEntity.getCCustomInfo().getUserName());
		}
		//公司用户 公司名称
		else if(2 == identityEntity.getType())
		{
			signingRoomEntity.setEmail(identityEntity.getEmail());
			signingRoomEntity.setCompanyName(identityEntity.getCCompanyInfo().getCompanyName());
		}
			
		try {
			signingRoomDao.save(signingRoomEntity);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}

	@Override
	public String querySigningRoom(Map<String, String> datamap)
			throws ServiceException {
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		
		String queryType = (String) datamap.get("type");
		String queryparam = (String) datamap.get("param");
		String statusStr = (String) datamap.get("status");
		if(null == queryType || "".equals(queryType) || null == queryparam || "".equals(queryparam) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"查询类型和查询数据不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" type or param is null!");
		}
		
		byte status = 0;
		if(null != statusStr && !"".equals(statusStr))
		{
			try {
				status = Byte.parseByte(statusStr);
			} catch (NumberFormatException e) {
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"签约时状态字段转换异常", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" status is not correct!");
			}
			
		}
		SigningRoomEntity signingRoomEntity = new SigningRoomEntity();
		try {
			//根据手机号码查询
			if("1".equals(queryType))
			{
				signingRoomEntity = signingRoomDao.findByLinktelAndStatus(queryparam,status);
			}
			//根据邮箱查询
			else if("2".equals(queryType))
			{
				signingRoomEntity = signingRoomDao.findByEmailAndStatus(queryparam,status);
			}
			//根据用户ID查询
			else if("3".equals(queryType))
			{
				signingRoomEntity = signingRoomDao.findByUserIdAndStatus(Integer.parseInt(queryparam),status);
			}
			else
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"签约时状态字段错误", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" status is wrong!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		JSONObject signingRoomObj = Bean2JSON(signingRoomEntity);
         return signingRoomObj.toString();
	}

	//关闭签约室
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String closeSigningRoom(Map<String,String> datamap)throws ServiceException
	{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		SigningRoomEntity signingRoomEntity = new SigningRoomEntity();
			
		try {
			signingRoomEntity = signingRoomDao.findByUserIdAndStatus(identityEntity.getId(), (byte)0);
			signingRoomDao.updateSigningRoomEntityState(signingRoomEntity.getUserId());
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}
	
	//添加子账号
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String addChildAccount(Map<String,String> datamap)throws ServiceException
	{
		//先验证平台信息
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		
		String  account =  (String) datamap.get("account");
		String  platformUserName =  (String) datamap.get("platformUserName");
		
		IdentityEntity identityEntityMajor = null;
		//先查看 主账号是否存在
		try {
			identityEntityMajor = getIdentityEntity(account,platformUserName,platformEntity);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null == identityEntityMajor)
		{
			//抛出异常   账号或用户ID已存在
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
		
		IdentityEntity identityEntity = new IdentityEntity();
		String  cAccount =  (String) datamap.get("cAccount");
		String  cName =  (String) datamap.get("cName");
		String  cIdNumber =  (String) datamap.get("cIdNumber");
		String  cMobile =  (String) datamap.get("cMobile");
		String  cEmail =  (String) datamap.get("cEmail");		
		String  cPassword =  (String) datamap.get("cPassword");
		String  permision =  (String) datamap.get("permision");		
		
		CustomInfoEntity customInfoEntity = new CustomInfoEntity();
		customInfoEntity.setUserName(cName);
		customInfoEntity.setIdentityCard(cIdNumber);
		customInfoEntity.setAppId(appId);
		
		identityEntity.setAccount(cAccount);
		identityEntity.setMobile(cMobile);
		identityEntity.setEmail(cEmail);
		identityEntity.setPassword(cPassword);
		identityEntity.setParentId(identityEntityMajor.getId());
		try {
			//保存公司信息
			customInfoEntity = customInfoDao.save(customInfoEntity);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常  系统错误
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		identityEntity.setCCustomInfo(customInfoEntity);
		identityEntity.setCCompanyInfo(identityEntityMajor.getCCompanyInfo());
		
		IdentityEntity identityEntityCheck = null;
		//查询子账号邮箱是否已经注册过
		try {
			identityEntityCheck = identityDao.queryAppIdAndEmailAndType(platformEntity,identityEntity.getEmail(),(byte)2);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null != identityEntityCheck)
		{
			//抛出异常   账号或用户ID已存在
			throw new ServiceException(ConstantUtil.RETURN_EMAIL_EXIST[0],
					ConstantUtil.RETURN_EMAIL_EXIST[1], ConstantUtil.RETURN_EMAIL_EXIST[2]);
		}
		
		// 补充默认参数
		// 用户类型:"1"个人；"2”企业；"3"平台
		identityEntity.setType((byte) 2);
		// 用户状态:"1"停用；"0”可用
		identityEntity.setStatus((byte) 0);
		//如果有 创建合同权限 则为实名认证
		if("1".equals(permision.substring(0, 1)))
		{
			// 是否实名:"0"未激活；"1”已激活
			identityEntity.setIsAuthentic((byte) 1);
		}
		else
		{
			// 是否实名:"0"未激活；"1”已激活
			identityEntity.setIsAuthentic((byte) 1);
		}
		
		// 用户来源:"1"云签；"2”对接版；"3"本地版
		identityEntity.setSource((byte) 1);
		//根据规则 生成用户的uuid
		identityEntity.setUuid(toGeneralUUID(appId,cAccount,cMobile));
		//申请时间
		identityEntity.setRegistTime(new Date());
		identityEntity.setPlatformUserName(cAccount+"_p");
		identityEntity.setStatusTime(new Date());
		identityEntity.setCPlatform(platformEntity);

		identityEntity.setIsMobileVerified((byte)0);
		identityEntity.setIsEmailVerified((byte)0);
		
		try {
			identityEntity = identityDao.save(identityEntity);
			//保存权限信息
			RoleEntity roleEntity = roleDao.findByRoleName("CA"+permision);
			if(null != roleEntity)
			{
				UserRoleRelationEntity userRoleRelationEntity = new UserRoleRelationEntity();
				userRoleRelationEntity.setRoleId(roleEntity.getId());
				userRoleRelationEntity.setUserId(identityEntity.getId());
				try {
					userRoleRelationDao.save(userRoleRelationEntity);
				} catch (Exception e) {
					e.printStackTrace();
					// 抛出异常 系统错误
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 抛出异常 系统错误
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}
	//添加子账号
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String updateChildAccount(Map<String,String> datamap)throws ServiceException
	{
		//先验证平台信息
			String appId = (String) datamap.get("appId");
			PlatformEntity platformEntity = checkPlatform(appId);
			
			String  account =  (String) datamap.get("account");
			String  platformUserName =  (String) datamap.get("platformUserName");
			
			IdentityEntity identityEntityMajor = null;
			IdentityEntity identityEntity = null;
			int  cUserId =   Integer.parseInt(datamap.get("cUserID"));
			//先查看 主账号是否存在
			try {
				identityEntityMajor = checkIdentityEntity(account,platformUserName,platformEntity);
				identityEntity = identityDao.findById(cUserId);
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(null == identityEntityMajor || null == identityEntity)
			{
				//抛出异常   账号或用户ID已存在
				throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
						ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
			}
			//校验 账户是否同意
			if(identityEntityMajor.getId() != identityEntity.getParentId())
			{
				//抛出异常   账号或用户ID已存在
				throw new ServiceException(ConstantUtil.RETURN_YUNSIGN_CHILD_NOT_MATCH[0],
						ConstantUtil.RETURN_YUNSIGN_CHILD_NOT_MATCH[1], ConstantUtil.RETURN_YUNSIGN_CHILD_NOT_MATCH[2]);
			}
			String  cName =   datamap.get("cName");
			String  cIdNumber =   datamap.get("cIdNumber");
			String  cMobile =  datamap.get("cMobile");
			String  cEmail =  datamap.get("cEmail");		
//			String  cPassword =  datamap.get("cPassword");
			String  permision =  datamap.get("permision");		
			
			//校验邮箱是否已注册
			if(!cEmail.equals(identityEntity.getEmail()))
			{
				IdentityEntity identityEntityCheck = null;
				//查询子账号邮箱是否已经注册过
				try {
					identityEntityCheck = identityDao.queryAppIdAndEmailAndType(platformEntity,identityEntity.getEmail(),(byte)2);
				} catch (Exception e) {
					e.printStackTrace();
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
				if(null != identityEntityCheck)
				{
					//抛出异常   账号或用户ID已存在
					throw new ServiceException(ConstantUtil.RETURN_EMAIL_EXIST[0],
							ConstantUtil.RETURN_EMAIL_EXIST[1], ConstantUtil.RETURN_EMAIL_EXIST[2]);
				}
			}

			CustomInfoEntity customInfoEntity = identityEntity.getCCustomInfo();
			//用户姓名或身份证号有变更
			if(null == customInfoEntity || !cName.equals(customInfoEntity.getUserName()) || !cIdNumber.equals(customInfoEntity.getIdentityCard()))
			{
				customInfoEntity.setUserName(cName);
				customInfoEntity.setIdentityCard(cIdNumber);
				customInfoEntity.setAppId(appId);
				try {
					//保存个人信息
					customInfoEntity = customInfoDao.save(customInfoEntity);
				} catch (Exception e) {
					e.printStackTrace();
					//抛出异常  系统错误
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
			}
			//如果有 创建合同权限 则为实名认证
			if("1".equals(permision.substring(0, 1)))
			{
				// 是否实名:"0"未激活；"1”已激活
				identityEntity.setIsAuthentic((byte) 1);
			}
			else
			{
				// 是否实名:"0"未激活；"1”已激活
				identityEntity.setIsAuthentic((byte) 1);
			}
			//保存权限信息
			RoleEntity roleEntity = roleDao.findByRoleName("CA"+permision);
			if(null != roleEntity)
			{
				try {
					//获取用户角色信息
					UserRoleRelationEntity userRoleRelationEntity = userRoleRelationDao.findByUserId(identityEntity.getId());
					//查看用户角色是否变更 变更则替换原有的
					if(userRoleRelationEntity.getRoleId() != roleEntity.getId())
					{
						userRoleRelationEntity.setRoleId(roleEntity.getId());
						userRoleRelationDao.save(userRoleRelationEntity);
					} 
				}
				catch (Exception e) {
					e.printStackTrace();
					// 抛出异常 系统错误
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
			}
			identityEntity.setMobile(cMobile);
			identityEntity.setEmail(cEmail);
//			identityEntity.setPassword(cPassword);
			try {
				identityDao.save(identityEntity);
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			return "";
	}	
	//添加子账号
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String stopChildAccount(Map<String,String> datamap)throws ServiceException
	{
		//先验证平台信息
			String appId = (String) datamap.get("appId");
			PlatformEntity platformEntity = checkPlatform(appId);
			
			String  account =  (String) datamap.get("account");
			String  platformUserName =  (String) datamap.get("platformUserName");
			
			IdentityEntity identityEntityMajor = null;
			IdentityEntity identityEntity = null;
			String  cAccount =   datamap.get("cAccount");
			String  cEmail =  datamap.get("cEmail");		
			String  status =  datamap.get("status");
			//先查看 主账号是否存在
			try {
				identityEntityMajor = checkIdentityEntity(account,platformUserName,platformEntity);
				identityEntity = identityDao.queryAppIdAndAccount(platformEntity,cAccount);
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(null == identityEntityMajor || null == identityEntity)
			{
				//抛出异常   账号或用户ID已存在
				throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
						ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
			}
			//校验 账户是否同意
			if(identityEntityMajor.getId() != identityEntity.getParentId())
			{
				//抛出异常   账号或用户ID已存在
				throw new ServiceException(ConstantUtil.RETURN_YUNSIGN_CHILD_NOT_MATCH[0],
						ConstantUtil.RETURN_YUNSIGN_CHILD_NOT_MATCH[1], ConstantUtil.RETURN_YUNSIGN_CHILD_NOT_MATCH[2]);
			}
			identityEntity.setStatus(Byte.parseByte(status));
			try {
				identityDao.save(identityEntity);
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			return "";
	}	
	//查询子账号列表
	public String queryChildAccount(Map<String,String> datamap)throws ServiceException
	{
			//先验证平台信息
			String appId = (String) datamap.get("appId");
			PlatformEntity platformEntity = checkPlatform(appId);
				
		String  account =  (String) datamap.get("account");
		String  platformUserName =  (String) datamap.get("platformUserName");
		
		IdentityEntity identityEntityMajor = null;
		//先查看 主账号是否存在
		try {
			identityEntityMajor = checkIdentityEntity(account,platformUserName,platformEntity);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null == identityEntityMajor)
		{
			//抛出异常   账号或用户ID已存在
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
		//1 企业子账户列表查询  2 精确查询
		String retStr = "";
		String  queryType =  (String) datamap.get("queryType");
		if("1".equals(queryType))
		{
			List<IdentityEntity> datalist;
			try {
				datalist = identityDao.queryChildAccountList(platformEntity,identityEntityMajor.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//抛出异常   账号或用户ID已存在
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			//组装成JS对象输出
			JSONArray jsonArray = new JSONArray();
			JSONObject json = null;
			for(IdentityEntity i : datalist)
			{
				json = Bean2JSON(i);
				if(null != i.getCCustomInfo())
				{
					CustomInfoEntity custom = i.getCCustomInfo();
					if(null != custom.getUserName())
					{
						json.put("userName",custom.getUserName());
					}
					if(null != custom.getIdentityCard())
					{
						json.put("identityCard",custom.getIdentityCard());
					}
				}
				String authStr = "";
				try {
					UserRoleRelationEntity urEntity = userRoleRelationDao.findByUserId(i.getId());
					if(null != urEntity)
					{
						RoleEntity rentity = roleDao.findById(urEntity.getRoleId());
						authStr = rentity.getRoleName().replace("CA", "");
					}
				} catch (Exception e) {
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
				
				// 用户权限        格式 {"create":"1";"sign":"1";"preservation":"1"} 1为有此权限，0为无此权限
				char[] auths = authStr.toCharArray();
				StringBuffer retStr2 = new StringBuffer("");
				for(int j = 0;j<auths.length;j++)
				{
					if( j == 0)
					{
						retStr2.append(auths[j]);
					}
					else
					{
						retStr2.append(","+auths[j]);
					}
				}
				json.put("role",retStr2.toString());
				jsonArray.add(json);
			}
			JSONObject jo = new JSONObject();
			jo.put("list", jsonArray);
			retStr = jo.toString();
		}
		else
		{
			String  cName =  (String) datamap.get("cName");
			String  cEmail =  (String) datamap.get("cEmail");	
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String  createBeginTime =  (String) datamap.get("createBeginTime");
			String  createEndTime =  (String) datamap.get("createEndTime");		
			
			List<IdentityEntity> datalist;
			try {
				datalist = identityDao.queryChildAccountListByCondition(platformEntity,identityEntityMajor.getId(),cEmail,sdf.parse(createBeginTime),sdf.parse(createEndTime),cName);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//抛出异常   账号或用户ID已存在
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			//组装成JS对象输出
			JSONArray jsonArray = new JSONArray();
			JSONObject json = null;
			for(IdentityEntity i : datalist)
			{
				json = Bean2JSON(i);
				if(null != i.getCCustomInfo())
				{
					CustomInfoEntity custom = i.getCCustomInfo();
					if(null != custom.getUserName())
					{
						json.put("userName",custom.getUserName());
					}
				    if(null != custom.getIdentityCard())
					{
						json.put("identityCard",custom.getIdentityCard());
					}
				}
				String authStr = "";
				try {
					UserRoleRelationEntity urEntity = userRoleRelationDao.findByUserId(i.getId());
					if(null != urEntity)
					{
						RoleEntity rentity = roleDao.findById(urEntity.getRoleId());
						authStr = rentity.getRoleName().replace("CA", "");
					}
				} catch (Exception e) {
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
				
				// 用户权限        格式 {"create":"1";"sign":"1";"preservation":"1"} 1为有此权限，0为无此权限
				char[] auths = authStr.toCharArray();
				StringBuffer retStr2 = new StringBuffer("");
				for(int j = 0;j<auths.length;j++)
				{
					if( j == 0)
					{
						retStr2.append(auths[j]);
					}
					else
					{
						retStr2.append(","+auths[j]);
					}
				}
				json.put("role",retStr2.toString());
				jsonArray.add(json);
			}
			JSONObject jo = new JSONObject();
			jo.put("list", jsonArray);
			retStr = jo.toString();
		}
		return retStr;
	}	
	
	@Override
	public String queryBangAccountList(Map<String,String> datamap) throws ServiceException 
	{
		//判断平台是否存在
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		List<IdentityEntity> list = new ArrayList<IdentityEntity>();
		//个人用户
		if((byte)1 ==identityEntity.getType() )
		{
			list = identityDao.queryIdentityListBySameCustom(identityEntity.getCCustomInfo().getId(),identityEntity.getMobile(),identityEntity.getId());
		}
		//企业用户
		else if((byte)2 ==identityEntity.getType() )
		{
			list = identityDao.queryIdentityListBySameCompany(identityEntity.getCCustomInfo().getId(),identityEntity.getCCompanyInfo().getId(),identityEntity.getEmail(),identityEntity.getId());
		}
		
		//组装成JS对象输出
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		for(IdentityEntity i : list)
		{
			json = Bean2JSON(i);
			json.put("appName", i.getCPlatform().getProgram());
			json.put("appCreateTime",  sdf.format(i.getCPlatform().getCreateTime()));
			json.put("appAddress", i.getCPlatform().getAddress());
			jsonArray.add(json);
		}
		return jsonArray.toString();
	}
	
	//根据手机号码查询云签 企业用户
	public String getCompanyAccountByMobile(Map<String,String> datamap)throws ServiceException
	{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		 //查看用户账号或平台用户名称是否存在
		if(null == datamap.get("mobile") || "".equals(datamap.get("mobile" )))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"手机号码不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" mobile is null!");
		}

		 //查看用户账号或平台用户名称是否存在
		if(null == datamap.get("password") || "".equals(datamap.get("password" )))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"密码不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" password is null!");
		}
		IdentityEntity identityEntity = new IdentityEntity();
		try {
			identityEntity = identityDao.queryAppIdAndMobileAndEmailIsNull(platformEntity,datamap.get("mobile"));
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		
		if(null != identityEntity)
		{
			String pwd1 = datamap.get("password") ;
			String pwd2 = identityEntity.getPassword();
			if(!pwd1.equals(pwd2))
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[0],
						ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[1], ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[2]);
			}
		}

		return toJSONStrFromIdentity(identityEntity);
	}

	@Override
	//根据邮箱查询云签个人用户
	public String getCustomAccountByEmail(Map<String, String> datamap)
			throws ServiceException
	{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		 //查看用户账号或平台用户名称是否存在
		if(null == datamap.get("email") || "".equals(datamap.get("email" )))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"邮箱不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" email is null!");
		}

		 //查看用户账号或平台用户名称是否存在
		if(null == datamap.get("password") || "".equals(datamap.get("password" )))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"密码不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" password is null!");
		}
		IdentityEntity identityEntity = new IdentityEntity();
		try {
			identityEntity = identityDao.queryAppIdAndEmailAndMobileIsNull(platformEntity,datamap.get("email"));
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		
		if(null != identityEntity)
		{
			String pwd1 = datamap.get("password") ;
			String pwd2 = identityEntity.getPassword();
			if(!pwd1.equals(pwd2))
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[0],
						ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[1], ConstantUtil.RETURN_YUNSIGN_USER_LOGIN_FAILED[2]);
			}
		}

		return toJSONStrFromIdentity(identityEntity);
	}

	/*
	 * 固话注册接口(non-Javadoc)
	 * 用户使用固话注册 （云签除外）
	 * 允许手机号码为空  但涉及到短信验证签署前  需要做 手机号码补全
	 */
	@Override
	public String saveTelIdentity(Map<String, String> datamap)
			throws ServiceException
	{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		
		IdentityEntity identityEntity = new IdentityEntity();
		// 转成bean对象
		try {
			BeanUtils.populate(identityEntity, datamap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			// 抛出异常 参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1], e.getMessage());
		}

		//判断传参是够完整
		String identCheck = identityEntity.isBeanLegal();
		if(!"".equals(identCheck))
		{
			throw new ServiceException(
					ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+identCheck,
					ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
		}
		
		IdentityEntity identityEntity2 = null;
		//先查看 该平台下 账号或平台用户名 是否已经存在
		try {
			identityEntity2 = getIdentityEntity(identityEntity.getAccount(),identityEntity.getPlatformUserName(),platformEntity);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null != identityEntity2)
		{
			//抛出异常   账号或平台用户名 已存在
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[2]);
		}
		
		if(2 == identityEntity.getType())
		{
			if(null != identityEntity.getEmail() && !"".equals(identityEntity.getEmail()))
			{
				//同一个平台下  邮箱不能重复
				try {
					identityEntity2 = null;
					identityEntity2 = identityDao.queryAppIdAndEmailAndType(platformEntity,identityEntity.getEmail(),(byte)2);
				} catch (Exception e) {
					e.printStackTrace();
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
				if(null != identityEntity2)
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_EMAIL_EXIST[0],
							ConstantUtil.RETURN_EMAIL_EXIST[1], ConstantUtil.RETURN_EMAIL_EXIST[2]);
				}
			}
			CompanyInfoEntity companyInfoEntity = null;
			companyInfoEntity = new CompanyInfoEntity();
			//手工转成bean对象
			
			if(isNotNull(datamap.get("companyName")))
			{
				companyInfoEntity.setCompanyName(datamap.get("companyName"));
				
				if(isNotNull(datamap.get("companyType")))
				{
					companyInfoEntity.setCompanyType(datamap.get("companyType"));
				}
				if(isNotNull(datamap.get("companyReseve1")))
				{
					companyInfoEntity.setReseve1(datamap.get("companyReseve1"));
				}
				
				//判断传入的营业执照号是否存在  存在则作 数据校验
				if(isNotNull(datamap.get("businessLicenseNo")))
				{
					companyInfoEntity.setBusinessLicenseNo(datamap.get("businessLicenseNo"));
				
					//企业用户需要判断企业名称与企业营业执照号是否存在且一致
					//先根据企业营业执照号 查询 公司是否已经注册
					List<CompanyInfoEntity> companyInfoList = companyInfoDao.findByBusinessLicenseNo(companyInfoEntity.getBusinessLicenseNo());
					if(null != companyInfoList  &&  companyInfoList.size() > 0)
					{
						boolean isNotMatch = true;
						for(CompanyInfoEntity company: companyInfoList)
						{
							//判定公司名称是否一致
							if(companyInfoEntity.getCompanyName().equals(company.getCompanyName()))
							{
								companyInfoEntity.setId(company.getId());
								isNotMatch = false;
								break;
							}
						}
						if(isNotMatch)
						{
								throw new ServiceException(
										ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[0],
										ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[1],
										ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[2]);
						}
					}
				}
				try {
					//营业执照附件
					if(isNotNull(datamap.get("businessNoPath")))
					{
						AttachmentEntity businessAttach = new AttachmentEntity();
						businessAttach.setAttachmentPath(datamap.get("businessNoPath"));
						businessAttach.setAttachmentName(datamap.get("businessNoName"));
						businessAttach.setAttachmentUri(datamap.get("businessNoPath"));
						businessAttach.setAttachmentThumbUri(datamap.get("businessNoPath"));
						businessAttach.setAttachmentExtension(datamap.get("businessNoExtension"));
						businessAttach.setAttachmentSource((byte)1);
						//是否有效:"0"无效；"1”有效
						businessAttach.setAttachmentStatus((byte)1);
						//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
						businessAttach.setAttachmentType((byte)2);
						businessAttach = attachmentInfoDao.save(businessAttach);
						companyInfoEntity.setCAttachmentBusi(businessAttach);
					}
					//营业执照附件
					if(isNotNull(datamap.get("proxyPhotoPath")) && !defaultYunsignAPPID.equals(companyInfoEntity.getAppId()))
					{
						AttachmentEntity proxyAttach = new AttachmentEntity();
						proxyAttach.setAttachmentPath(datamap.get("proxyPhotoPath"));
						proxyAttach.setAttachmentName(datamap.get("proxyPhotoName"));
						proxyAttach.setAttachmentUri(datamap.get("proxyPhotoPath"));
						proxyAttach.setAttachmentExtension(datamap.get("proxyPhotoExtension"));
						proxyAttach.setAttachmentThumbUri(datamap.get("proxyPhotoPath"));
						proxyAttach.setAttachmentSource((byte)1);
						//是否有效:"0"无效；"1”有效
						proxyAttach.setAttachmentStatus((byte)1);
						//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
						proxyAttach.setAttachmentType((byte)2);
						proxyAttach = attachmentInfoDao.save(proxyAttach);
						companyInfoEntity.setCAttachmentPhoto(proxyAttach);
					}
					//保存公司信息
					companyInfoEntity = companyInfoDao.save(companyInfoEntity);
					identityEntity.setCCompanyInfo(companyInfoEntity);
				} catch (Exception e) {
					e.printStackTrace();
					//抛出异常  系统错误
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
			}
		}
		
		
		CustomInfoEntity customInfoEntity = new CustomInfoEntity();
		//手工转成bean对象
		if(isNotNull(datamap.get("userName")))
		{
			customInfoEntity.setUserName(datamap.get("userName"));
			if(isNotNull(datamap.get("nickname")))
			{
				customInfoEntity.setNickname(datamap.get("nickname"));
			}
			if(isNotNull(datamap.get("phoneNum")))
			{
				customInfoEntity.setPhoneNum(datamap.get("phoneNum"));
			}
			if(isNotNull(datamap.get("duty")))
			{
				customInfoEntity.setDuty(datamap.get("duty"));
			}
			if(isNotNull(datamap.get("customReseve1")))
			{
				customInfoEntity.setReseve1(datamap.get("customReseve1"));
			}
			if(isNotNull(datamap.get("customReseve2")))
			{
				customInfoEntity.setReseve2(datamap.get("customReseve2"));
			}
			if(isNotNull(datamap.get("customAddress")))
			{
				customInfoEntity.setAddress(datamap.get("customAddress"));
			}
			customInfoEntity.setAppId(appId);
			
			//根据传入的身份证号码是否为空 作 身份数据校验
			if(isNotNull(datamap.get("identityCard")))
			{
				//取证件类型 没有没有则 默认为身份证 1
				customInfoEntity.setIdentityCard(datamap.get("identityCard"));
				if(isNotNull(datamap.get("cardType")))
				{
					customInfoEntity.setCardType(datamap.get("cardType"));
				}
				else
				{
					customInfoEntity.setCardType("1");
				}
				//先根据身份证号 查询 用户是否已经注册
				List<CustomInfoEntity> customInfoList = customInfoDao.findByIdentityCardAndCardType(datamap.get("identityCard"),customInfoEntity.getCardType());
				
				if(null != customInfoList  &&  customInfoList.size() > 0)
				{
					boolean isNotMatch = true;
					for(CustomInfoEntity custom: customInfoList)
					{
						//身份证号与姓名是否一致
						if(customInfoEntity.getUserName().equals(custom.getUserName()))
						{
							customInfoEntity.setId(custom.getId());
							isNotMatch = false;
							break;
						}
					}
					if(isNotMatch)
					{
						throw new ServiceException(
								ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[0],
								ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[1],
								ConstantUtil.RETURN_USERNAME_IDNUM_NOT_MATCH[2]);
					}
				}
			}
			
			try {
				
				//身份证号正面
				if(isNotNull(datamap.get("idImgAPath")))
				{
					AttachmentEntity idImgAAttach = new AttachmentEntity();
					idImgAAttach.setAttachmentPath(datamap.get("idImgAPath"));
					idImgAAttach.setAttachmentName(datamap.get("idImgAName"));
					idImgAAttach.setAttachmentUri(datamap.get("idImgAPath"));
					idImgAAttach.setAttachmentThumbUri(datamap.get("idImgAPath"));
					idImgAAttach.setAttachmentExtension(datamap.get("idImgAExtension"));
					idImgAAttach.setAttachmentSource((byte)1);
					//是否有效:"0"无效；"1”有效
					idImgAAttach.setAttachmentStatus((byte)1);
					//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
					idImgAAttach.setAttachmentType((byte)1);
					
					idImgAAttach = attachmentInfoDao.save(idImgAAttach);
					customInfoEntity.setCAttachmentIdA(idImgAAttach);
				}
				//身份证号反面
				if(isNotNull(datamap.get("idImgBPath")))
				{
					AttachmentEntity idImgBAttach = new AttachmentEntity();
					idImgBAttach.setAttachmentPath(datamap.get("idImgBPath"));
					idImgBAttach.setAttachmentName(datamap.get("idImgBName"));
					idImgBAttach.setAttachmentUri(datamap.get("idImgBPath"));
					idImgBAttach.setAttachmentThumbUri(datamap.get("idImgBPath"));
					idImgBAttach.setAttachmentExtension(datamap.get("idImgBExtension"));
					idImgBAttach.setAttachmentSource((byte)1);
					//是否有效:"0"无效；"1”有效
					idImgBAttach.setAttachmentStatus((byte)1);
					//附件类型:"1"个人身份证；"2”印业执照号；"3”法人身份证； "4"代理证书
					idImgBAttach.setAttachmentType((byte)1);
					
					idImgBAttach = attachmentInfoDao.save(idImgBAttach);
					customInfoEntity.setCAttachmentIdB(idImgBAttach);
				}
				
				//保存用户信息
				customInfoEntity = customInfoDao.save(customInfoEntity);
				identityEntity.setCCustomInfo(customInfoEntity);
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常  系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
		}
		
		// 补充默认参数
		// 用户类型:"1"个人；"2”企业；"3"平台
		identityEntity.setType(identityEntity.getType());
		// 用户状态:"1"停用；"0”使用
		identityEntity.setStatus((byte) 0);
		// 是否实名:"0"未激活；"1”已激活
		identityEntity.setIsAuthentic((byte) 0);
		
		// 用户来源:"1"云签；"2”对接版；"3"本地版
		identityEntity.setSource((byte) 2);
//		if(isNotNull(datamap.get("BusinessAdmin")) && 2 == identityEntity.getType())
//		{
//			identityEntity.setBusinessAdmin(datamap.get("BusinessAdmin"));
//		}
//		else
//		{
			identityEntity.setBusinessAdmin("0");
//		}
		
		
		//根据规则 生成用户的uuid
		identityEntity.setUuid(toGeneralUUID(appId,identityEntity.getPlatformUserName(),identityEntity.getMobile()));
		//申请时间
		identityEntity.setRegistTime(new Date());
		
		identityEntity.setStatusTime(new Date());
		identityEntity.setCPlatform(platformEntity);

		identityEntity.setIsMobileVerified((byte)0);
		identityEntity.setIsEmailVerified((byte)0);
		try {
			identityEntity = identityDao.save(identityEntity);
		} catch (Exception e) {
			e.printStackTrace();
			// 抛出异常 系统错误
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		//固话注册的 不需要 绑定云签账号
		return "";
	}

	//用户注册时 资料可能不全  （无身份证、姓名、公司名、营业执照号等）
	@Override
	public String saveOtherIdentity(Map<String, String> datamap)
			throws ServiceException
	{
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		
		IdentityEntity identityEntity = new IdentityEntity();
		// 转成bean对象
		try {
			BeanUtils.populate(identityEntity, datamap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			// 抛出异常 参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1], e.getMessage());
		}

		//判断传参是够完整
		String identCheck = identityEntity.isBeanLegal();
		if(!"".equals(identCheck))
		{
			throw new ServiceException(
					ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+identCheck,
					ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
		}
		
		IdentityEntity identityEntity2 = null;
		//先查看 该平台下 账号或平台用户名 是否已经存在
		try {
			identityEntity2 = getIdentityEntity(identityEntity.getAccount(),identityEntity.getPlatformUserName(),platformEntity);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null != identityEntity2)
		{
			//抛出异常   账号或平台用户名 已存在
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[2]);
		}
		
		if(2 == identityEntity.getType())
		{
			//如果有传邮箱 那么验证 邮箱是否重复
			if(null != identityEntity.getEmail() && !"".equals(identityEntity.getEmail()))
			{
				//同一个平台下  邮箱不能重复
				try {
					identityEntity2 = null;
					identityEntity2 = identityDao.queryAppIdAndEmailAndType(platformEntity,identityEntity.getEmail(),(byte)2);
				} catch (Exception e) {
					e.printStackTrace();
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
				if(null != identityEntity2)
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_EMAIL_EXIST[0],
							ConstantUtil.RETURN_EMAIL_EXIST[1], ConstantUtil.RETURN_EMAIL_EXIST[2]);
				}
			}
			//否则判断手机号码是否重复  以待用户之后补全信息
			else
			{
				//同一个平台下  手机不能重复
				try {
					identityEntity2 = null;
					identityEntity2 = identityDao.queryAppIdAndMobileAndType(platformEntity,identityEntity.getMobile(),(byte)1);
				} catch (Exception e) {
					e.printStackTrace();
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
				if(null != identityEntity2)
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
							ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
				}
			}
		}
		else if(1 == identityEntity.getType())
		{
			//同一个平台下  手机不能重复
			try {
				identityEntity2 = null;
				identityEntity2 = identityDao.queryAppIdAndMobileAndType(platformEntity,identityEntity.getMobile(),(byte)1);
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			if(null != identityEntity2)
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],
						ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
			}
		}
		CompanyInfoEntity companyInfoEntity = null;
		//企业用户
		if(2==identityEntity.getType())
		{
			companyInfoEntity = new CompanyInfoEntity();
			//手工转成bean对象
			if(isNotNull(datamap.get("businessLicenseNo")))
			{
				companyInfoEntity.setBusinessLicenseNo(datamap.get("businessLicenseNo"));
			}
			if(isNotNull(datamap.get("companyName")))
			{
				companyInfoEntity.setCompanyName(datamap.get("companyName"));
			}
			if(isNotNull(datamap.get("companyType")))
			{
				companyInfoEntity.setCompanyType(datamap.get("companyType"));
			}
			if(isNotNull(datamap.get("companyReseve1")))
			{
				companyInfoEntity.setReseve1(datamap.get("companyReseve1"));
			}
			
			//判断传入参数是否完整
			String companyCheck = companyInfoEntity.isBeanLegal();
			if(!"".equals(companyCheck))
			{
				throw new ServiceException(
						ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1]+companyCheck,
						ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
			}
			
			//判断传入的营业执照号是否存在  存在则作 数据校验
			if(null != companyInfoEntity.getBusinessLicenseNo() &&!"".equals(companyInfoEntity.getBusinessLicenseNo()) )
			{
				//企业用户需要判断企业名称与企业营业执照号是否存在且一致
				//先根据企业营业执照号 查询 公司是否已经注册
				List<CompanyInfoEntity> companyInfoList = companyInfoDao.findByBusinessLicenseNo(companyInfoEntity.getBusinessLicenseNo());
				if(null != companyInfoList  &&  companyInfoList.size() > 0)
				{
					boolean isNotMatch = true;
					for(CompanyInfoEntity company: companyInfoList)
					{
						//判定公司名称是否一致
						if(companyInfoEntity.getCompanyName().equals(company.getCompanyName()))
						{
							companyInfoEntity.setId(company.getId());
							isNotMatch = false;
							break;
						}
					}
					if(isNotMatch)
					{
							throw new ServiceException(
									ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[0],
									ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[1],
									ConstantUtil.RETURN_COMPANYNAME_BUSNUM_NOT_MATCH[2]);
					}
				}
			}
		}
		
		//没有姓名和身份证等信息 就不保存用户基础信息
		
		// 补充默认参数
		// 用户类型:"1"个人；"2”企业；"3"平台
		identityEntity.setType(identityEntity.getType());
		// 用户状态:"1"停用；"0”使用
		identityEntity.setStatus((byte) 0);
		// 是否实名:"0"未激活；"1”已激活
		identityEntity.setIsAuthentic((byte) 0);
		
		// 用户来源:"1"云签；"2”对接版；"3"本地版
		
		identityEntity.setSource((byte) 2);
//		if(isNotNull(datamap.get("businessAdmin")) && 2 == identityEntity.getType())
//		{
//			identityEntity.setBusinessAdmin(datamap.get("businessAdmin"));
//		}
//		else
//		{
			identityEntity.setBusinessAdmin("0");
//		}
		
		
		//根据规则 生成用户的uuid
		identityEntity.setUuid(toGeneralUUID(appId,identityEntity.getPlatformUserName(),identityEntity.getMobile()));
		//申请时间
		identityEntity.setRegistTime(new Date());
		
		identityEntity.setStatusTime(new Date());
		identityEntity.setCPlatform(platformEntity);

		identityEntity.setIsMobileVerified((byte)0);
		identityEntity.setIsEmailVerified((byte)0);
		try {
			identityEntity = identityDao.save(identityEntity);
			platformEntity = platformDao.findPlatformByAppId(defaultYunsignAPPID);
		} catch (Exception e) {
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		//信息补全 不予绑定云签账号
			
		return "";
	}

	@Override
	public String getCustomMembersByCompanyName(Map<String, String> datamap)
			throws ServiceException
	{
		//验证APPID是否存在
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看传参  公司名是否为空
		if(isNotNull(datamap.get("companyName")))
		{
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+",公司名不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+", companyName is null");
		}
		
		//从参数中获取 待查询公司名
		String companyName = datamap.get("companyName");
		List<CompanyInfoEntity> companyList =  companyInfoDao.findByCompanyName(companyName);
		List<IdentityEntity> identityList = null;
		
		//组装成JS对象输出
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		//遍历根据公司名查询出来的 公司列表(3.0规范  营业执照号与公司名一致的公司 只存一次 但2.0很多数据不是这么的)
		for(CompanyInfoEntity companyInfo : companyList)
		{
			identityList = identityDao.queryIdentityByCCompanyInfoAndAppId(companyInfo, platformEntity);
			//遍历 公司下的账号信息
			for(IdentityEntity identityInfo : identityList)
			{
				json = new JSONObject();
				if(null != identityInfo.getCCustomInfo())
				{
					//姓名
					if(null != identityInfo.getCCustomInfo().getUserName())
					{
						json.put("userName", identityInfo.getCCustomInfo().getUserName());
					}
					//身份证号
					if(null != identityInfo.getCCustomInfo().getIdentityCard())
					{
						json.put("identityCard", identityInfo.getCCustomInfo().getIdentityCard());
					}
				}
				//用户编码
				json.put("userId", identityInfo.getId());
				//平台用户名
				json.put("platformUserName", identityInfo.getPlatformUserName());
				//邮箱
				json.put("email" , identityInfo.getEmail());
				//手机
				json.put("mobile", identityInfo.getMobile());
				jsonArray.add(json);
			}
		}
		
		return jsonArray.toString();
		
	}

	@Override
	public String changeAppAdmin(Map<String, String> datamap)
			throws ServiceException
	{
		PlatformEntity platformEntity = checkPlatform((String) datamap.get("appId"));
		
		//查看平台用户名是否存在
		String platformUserName = (String) datamap.get("platformUserName");
		if(null == platformUserName || "".equals(platformUserName) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"平台用户不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" platformUserName is null!");
		}

		//根据APPID和platformUserName 判断用户是否存在
		IdentityEntity identityEntity = null;
		try {
			 identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		
		List<IdentityEntity> listAdmin = identityDao.listAppAdmin(platformEntity);
		
		if(null != listAdmin && listAdmin.size() > 1)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_APP_ADMIN_EXIST_MORE[0],
					ConstantUtil.RETURN_APP_ADMIN_EXIST_MORE[1], ConstantUtil.RETURN_APP_ADMIN_EXIST_MORE[2]);
		}
		//将之前管理员账号变更为普通用户
		for(IdentityEntity adminIdent: listAdmin)
		{
			identityDao.updateIdentityBusinessAdmin("0",adminIdent.getUuid());
		}

		//更改管理员
		identityDao.updateIdentityBusinessAdmin("1",identityEntity.getUuid());
		
		return null;
	}

	@Transactional
	@Override
	public String completeInfo(Map<String, String> datamap)
			throws ServiceException
	{
		PlatformEntity platformEntity = checkPlatform((String) datamap.get("appId"));
		
		//查看平台用户名是否存在
		String platformUserName = (String) datamap.get("platformUserName");
		if(!isNotNull(platformUserName) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"平台用户不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" platformUserName is null!");
		}
		
		//根据APPID和platformUserName 判断用户是否存在
		IdentityEntity identityEntity = null;
		try {
			 identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		//获取补全类型
		/*
		 * 1、companyName
		 * 2、businessLicenseNo
		 * 3、mobile
		 * 4、email
		 * 5、userName
		 * 6、identityCard
		 */
		String completeType = datamap.get("completeType");
		
		String[] completeTypes = completeType.split("_");
		
		for(int i = 0; i < completeTypes.length; i++)
		{
			//补全 企业名称
			if(ConstantUtil.COMPLETE_TYPE_COMPANYNAME.equals(completeTypes[i]))
			{
				String  companyName = datamap.get("companyName");
				if(!isNotNull(companyName) )
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
							ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"：待补全企业名称不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" companyName is null!");
				}
				//原有企业名称不为空  不能补全
				if(null != identityEntity.getCCompanyInfo())
				{
					if(isNotNull(identityEntity.getCCompanyInfo().getCompanyName()))
					{
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_COMPLETE_COMPANYNAME_ERROR[0],
								ConstantUtil.RETURN_COMPLETE_COMPANYNAME_ERROR[1], ConstantUtil.RETURN_COMPLETE_COMPANYNAME_ERROR[2]);
					}
					
					try {
						companyInfoDao.updateCompanyInfoEntityCompanyName(companyName, identityEntity.getCCompanyInfo().getId());
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
				}
				else
				{
					CompanyInfoEntity companyInfoEntity = new CompanyInfoEntity();
					companyInfoEntity.setCompanyName(companyName);
					try {
						companyInfoEntity = companyInfoDao.save(companyInfoEntity);
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
					
					identityEntity.setCCompanyInfo(companyInfoEntity);
					try {
						identityDao.save(identityEntity);
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
				}
				
			}
			//补全 工商营业执照号
			else if(ConstantUtil.COMPLETE_TYPE_BUSINESLICEN.equals(completeTypes[i]))
			{
				String  businessLicenseNo = datamap.get("businessLicenseNo");
				if(!isNotNull(businessLicenseNo) )
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
							ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"：待补全营业执照号不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" businessLicenseNo is null!");
				}
				//原有工商营业执照号不为空  不能补全
				if(null != identityEntity.getCCompanyInfo())
				{
					if(isNotNull(identityEntity.getCCompanyInfo().getBusinessLicenseNo()))
					{
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_COMPLETE_BUSINESLICEN_ERROR[0],
								ConstantUtil.RETURN_COMPLETE_BUSINESLICEN_ERROR[1], ConstantUtil.RETURN_COMPLETE_BUSINESLICEN_ERROR[2]);
					}

					try {
						companyInfoDao.updateCompanyInfoEntityBusinessLicenNo(businessLicenseNo, identityEntity.getCCompanyInfo().getId());
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
					
				}
				else
				{
					CompanyInfoEntity companyInfoEntity = new CompanyInfoEntity();
					companyInfoEntity.setBusinessLicenseNo(businessLicenseNo);
					try {
						companyInfoEntity = companyInfoDao.save(companyInfoEntity);
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
					
					identityEntity.setCCompanyInfo(companyInfoEntity);
					try {
						identityDao.save(identityEntity);
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
				}
			}
			//补全 用户姓名
			else if(ConstantUtil.COMPLETE_TYPE_USERNAME.equals(completeTypes[i]))
			{
				String  userName = datamap.get("userName");
				if(!isNotNull(userName) )
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
							ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"：待补全姓名不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" userName is null!");
				}
				//原有 用户姓名不为空  不能补全
				if(null != identityEntity.getCCustomInfo())
				{
					if(isNotNull(identityEntity.getCCustomInfo().getUserName()) && !"未知".equals(identityEntity.getCCustomInfo().getUserName()))
					{
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_COMPLETE_USERNAME_ERROR[0],
								ConstantUtil.RETURN_COMPLETE_USERNAME_ERROR[1], ConstantUtil.RETURN_COMPLETE_USERNAME_ERROR[2]);
					}

					try {
						customInfoDao.updateCompanyInfoEntityUserName(userName, identityEntity.getCCustomInfo().getId());
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
					
				}
				else
				{
					CustomInfoEntity customInfoEntity = new CustomInfoEntity();
					customInfoEntity.setUserName(userName);
					try {
						customInfoEntity = customInfoDao.save(customInfoEntity);
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
					
					identityEntity.setCCustomInfo(customInfoEntity);
					try {
						identityDao.save(identityEntity);
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
				}
			}
			//补全 身份证号
			else if(ConstantUtil.COMPLETE_TYPE_IDENTITYCARD.equals(completeTypes[i]))
			{
				String  identityCard = datamap.get("identityCard");
				if(!isNotNull(identityCard) )
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
							ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"：待补全身份证不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" identityCard is null!");
				}
				//原有 用户姓名不为空  不能补全
				if(null != identityEntity.getCCustomInfo())
				{
					if(isNotNull(identityEntity.getCCustomInfo().getIdentityCard()))
					{
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_COMPLETE_IDENTITYCARD_ERROR[0],
								ConstantUtil.RETURN_COMPLETE_IDENTITYCARD_ERROR[1], ConstantUtil.RETURN_COMPLETE_IDENTITYCARD_ERROR[2]);
					}

					try {
						customInfoDao.updateCompanyInfoEntityIdentityCard(identityCard, identityEntity.getCCustomInfo().getId());
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
				}
				else
				{
					CustomInfoEntity customInfoEntity = new CustomInfoEntity();
					customInfoEntity.setIdentityCard(identityCard);
					try {
						customInfoEntity = customInfoDao.save(customInfoEntity);
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
					
					identityEntity.setCCustomInfo(customInfoEntity);
					try {
						identityDao.save(identityEntity);
					} catch (Exception e) {
						//抛出异常   参数异常
						throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
								ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
				}
				
			}
			//补全 手机
			else if(ConstantUtil.COMPLETE_TYPE_MOBILE.equals(completeTypes[i]))
			{
				String  mobile = datamap.get("mobile");
				if(!isNotNull(mobile) )
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
							ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"：待补全手机号码不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" mobile is null!");
				}
				
				//原有手机号不为空  不能补全
				if(isNotNull(identityEntity.getMobile()))
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_COMPLETE_MOBILE_ERROR[0],
							ConstantUtil.RETURN_COMPLETE_MOBILE_ERROR[1], ConstantUtil.RETURN_COMPLETE_MOBILE_ERROR[2]);
				}
				
				
				try {
					 identityDao.updateIdentityMobile(mobile, identityEntity.getUuid());
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
				
			}
			//补全 邮箱
			else if(ConstantUtil.COMPLETE_TYPE_EMAIL.equals(completeTypes[i]))
			{
				String  email = datamap.get("email");
				if(!isNotNull(email) )
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
							ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"：待补全邮箱不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" email is null!");
				}
				//原有邮箱不为空  不能补全
				if(isNotNull(identityEntity.getEmail()))
				{
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_COMPLETE_EMAIL_ERROR[0],
							ConstantUtil.RETURN_COMPLETE_EMAIL_ERROR[1], ConstantUtil.RETURN_COMPLETE_EMAIL_ERROR[2]);
				}
				
				try {
					 identityDao.updateIdentityEmail(email, identityEntity.getUuid());
				} catch (Exception e) {
					//抛出异常   参数异常
					throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
							ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
				}
			}
				
		}
		
		return null;
	}

	@Override
	public String queryUserExamineStatus(Map<String, String> datamap)throws ServiceException {
		
		String appId=datamap.get("appId");
		
		PlatformEntity pfe =checkPlatform(appId);
		String platFromUserNameGson=datamap.get("platformUserName");
		
		Gson gson=new Gson();
		
		List<Map<String,String>> results=new ArrayList<Map<String,String>>();
		
		List<Map<String,String>> platFromUserNameList=gson.fromJson(platFromUserNameGson, List.class);
		
		try{
			
			for(Map<String,String> platFromUserNames:platFromUserNameList){
				Map<String,String> result=new HashMap<String,String>();
				
				String platFromUserName=platFromUserNames.get("platFromUserName");
				IdentityEntity ide =identityDao.queryAppIdAndPlatformUserNameAndStatus(pfe,platFromUserName);
				
				
				if(ide!=null){
					Map<String,Object> temp=new HashMap<String,Object>();
					
					temp.put("id", ide.getId());
					temp.put("account", ide.getAccount());
					temp.put("bindedId", ide.getBindedId());
					temp.put("email", ide.getEmail());
					temp.put("isAdmin", ide.getIsAdmin());
					temp.put("isAuthentic", ide.getIsAuthentic());
					temp.put("mobile", ide.getMobile());
					temp.put("parentId", ide.getParentId());
					temp.put("password", ide.getPassword());
					temp.put("platformUserName", ide.getPlatformUserName());
					temp.put("registTime", ide.getRegistTime());
					temp.put("businessAdmin", ide.getBusinessAdmin());
					temp.put("source", ide.getSource());
					temp.put("status", ide.getStatus());
					temp.put("statusTime", ide.getStatusTime());
					temp.put("stopInfo", ide.getStopInfo());
					temp.put("stopInfo", ide.getStopInfo());
					temp.put("isMobileVerified", ide.getIsMobileVerified());
					temp.put("isEmailVerified", ide.getIsEmailVerified());
					temp.put("type", ide.getType());
					temp.put("uuid", ide.getUuid());
					temp.put("wxOpenid", ide.getWxOpenid());
					
					log.info(ide.getPlatformUserName());
					result.put("userInfo", gson.toJson(temp));
					result.put("userId", ide.getPlatformUserName());
					if(ide.getIsAuthentic()==1){
						//通过
						result.put("examineStatus", "1");
					}else if(ide.getIsAuthentic()==2){
						//这里是逻辑删除，可以恢复
						//未通过
						ide.setStatus(Byte.parseByte("0"));
						identityDao.save(ide);
						result.put("examineStatus", "0");
					}else{
						//未认证
						result.put("examineStatus", "3");
					}
					
					
				}else{
						//用户不存在
					result.put("examineStatus", "2");
					result.put("userId", platFromUserName);
					result.put("userInfo", gson.toJson(new HashMap<>()));
				}
				
				results.add(result);
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			
		}
		return gson.toJson(results);
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String synchronizationUserInfo(Map<String,String> datamap)throws ServiceException {
	
		String appId=datamap.get("appId");
		String platformUserName=datamap.get("platformUserName");
		
		String phone=datamap.get("phone");
		String userStatus=datamap.get("userStatus");
		

			
			PlatformEntity platformEntity = checkPlatform(appId);
			
			IdentityEntity identityEntity =identityDao.queryAppIdAndPlatformUserNameAndStatus(platformEntity, platformUserName);
			
			if(null!=identityEntity){
				if(!"".equals(phone) && null!=phone){
					
					//验证手机号是否唯一
					
					IdentityEntity identityEntityTemp = identityDao.queryAppIdAndMobileAndTypeAndStatus(platformEntity,phone,(byte)1,(byte)0);
					
					if(null!=identityEntityTemp){
						
						throw new ServiceException(ConstantUtil.RETURN_MOBL_EXIST[0],ConstantUtil.RETURN_MOBL_EXIST[1], ConstantUtil.RETURN_MOBL_EXIST[2]);
		
					}else{
						try{
						identityEntity.setMobile(phone);
						identityDao.save(identityEntity);
						}catch(Exception e){
							e.printStackTrace();
							throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
						}
					}
				}
				
				if(!"".equals(userStatus) && null!=userStatus){
					try{
						identityEntity.setStatus(Byte.parseByte(userStatus));
						identityDao.save(identityEntity);
					}catch(Exception e){
							e.printStackTrace();
							throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
					}
				}
			}else{
				throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
			}
			
		
		return "";
	}
}
