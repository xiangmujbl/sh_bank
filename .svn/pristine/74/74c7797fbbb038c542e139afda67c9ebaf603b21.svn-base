package com.mmec.centerService.userModule.service.impl;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.dao.RoleDao;
import com.mmec.centerService.userModule.dao.SigningRoomDao;
import com.mmec.centerService.userModule.dao.UkeyInfoDao;
import com.mmec.centerService.userModule.dao.UserRoleRelationDao;
import com.mmec.centerService.userModule.entity.AttachmentEntity;
import com.mmec.centerService.userModule.entity.CompanyInfoEntity;
import com.mmec.centerService.userModule.entity.CustomInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.entity.RoleEntity;
import com.mmec.centerService.userModule.entity.SigningRoomEntity;
import com.mmec.centerService.userModule.entity.UkeyInfoEntity;
import com.mmec.centerService.userModule.entity.UserRoleRelationEntity;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;

@Component("userBaseService") 
public class UserBaseService {
	@Autowired
	protected IdentityDao identityDao;
	@Autowired
	protected PlatformDao platformDao;
	@Autowired
	protected SigningRoomDao signingRoomDao;
	@Autowired
	protected UserRoleRelationDao userRoleRelationDao;
	@Autowired
	protected RoleDao roleDao;
	@Autowired
	protected UkeyInfoDao ukeyInfoDao;
	
	private static String defaultYunsignAPPID = "78f8RlcB2o";
	
	 private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	// 根据生成规则生成 用户的UUID :appId_platformUserId_time_mobile
	public String toGeneralUUID(Object appId, Object platformUserId,
			Object mobile) {
		String retStr = appId.toString() + charSplit + platformUserId.toString() + charSplit
				+ sdf.format(new Date());
		if (null != mobile && !"".equals(mobile)) {
			retStr += charSplit + mobile;
		}
		return retStr;
	}

	private String charSplit = "_";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	//验证平台信息
	public PlatformEntity checkPlatform(String  appId)throws ServiceException{
		if(null == appId || "".equals(appId))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"平台ID不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" AppId is null!");
		}
		
		//先查看 平台ID是否已经存在
		PlatformEntity platformEntity = null;
		try {
			platformEntity = platformDao.findPlatformByAppId(appId);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		//平台ID不存在  抛出异常
		if(null == platformEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],
					ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
		}
		return platformEntity;
	}
	
	//验证账户信息
	public IdentityEntity checkIdentityEntity(String  userAccount,String  platformUserName,PlatformEntity platformEntity )throws ServiceException{
		 //查看用户账号或平台用户名称是否存在
		if((null == userAccount || "".equals(userAccount) )&& (null == platformUserName || "".equals(platformUserName)))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"用户账号、平台用户名称不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" userAccount and platformUserName all null!");
		}
			
		IdentityEntity identityEntity = new IdentityEntity();
		try {
			
			if(null == userAccount || "".equals(userAccount) )
			{
				identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
			}
			else if(null == platformUserName || "".equals(platformUserName) )
			{
				identityEntity = identityDao.queryAppIdAndAccount(platformEntity, userAccount);
			}
			else
			{
				identityEntity = identityDao.queryAppIdAndPlatformUserNameAndAccount(platformEntity,userAccount, platformUserName);

			}
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
		return identityEntity;
	}


	//验证账户信息
	public IdentityEntity getIdentityEntity(String  userAccount,String  platformUserName,PlatformEntity platformEntity ,String... source)throws ServiceException{
		 //查看用户账号或平台用户名称是否存在
		if((null == userAccount || "".equals(userAccount) )&& (null == platformUserName || "".equals(platformUserName)))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"用户账号、平台用户名称不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" userAccount and platformUserName all null!");
		}
			
		IdentityEntity identityEntity = new IdentityEntity();
		try {
			if("4".equals(source)){
				
				if(null == userAccount || "".equals(userAccount) )
				{
					identityEntity = identityDao.queryAppIdAndPlatformUserNameAndStatus(platformEntity, platformUserName,(byte)0);
				}
				else if(null == platformUserName || "".equals(platformUserName) )
				{
					identityEntity = identityDao.queryAppIdAndAccountAndStatus(platformEntity, userAccount,(byte)0);
				}
				else
				{
					//identityEntity = identityDao.queryAppIdAndPlatformUserNameOrAccountAndStatus(platformEntity,userAccount, platformUserName,(byte)0);
					identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
				}
				
			}else{
				
				if(null == userAccount || "".equals(userAccount) )
				{
					identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
				}
				else if(null == platformUserName || "".equals(platformUserName) )
				{
					identityEntity = identityDao.queryAppIdAndAccount(platformEntity, userAccount);
				}
				else
				{
//					identityEntity = identityDao.queryAppIdAndPlatformUserNameOrAccount(platformEntity,userAccount, platformUserName);
					identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
	
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		return identityEntity;
	}
	
	//验证账户信息
	public IdentityEntity checkIdentityEntityByOpenId(String  wxOpenId,PlatformEntity platformEntity )throws ServiceException{
		
		IdentityEntity identityEntity = new IdentityEntity();
		try {
			identityEntity = identityDao.queryAppIdAndWxOpenId(platformEntity,wxOpenId);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
		return identityEntity;
	}
	
	//验证账户信息
	public IdentityEntity checkIdentityEntityAnd(String  userAccount,PlatformEntity platformEntity )throws ServiceException{
		 //查看用户账号或平台用户名称是否存在
		if(null == userAccount || "".equals(userAccount))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"用户账号、平台用户名称不能都为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" userAccount and platformUserName all null!");
		}
			
		IdentityEntity identityEntity = new IdentityEntity();
		try {
			identityEntity = identityDao.queryAppIdAndAccount(platformEntity,userAccount);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
		return identityEntity;
	}
	
	//验证账户信息
	public IdentityEntity checkYunsignIdentityEntity(PlatformEntity cPlatform,String platformUserName)throws ServiceException{
		IdentityEntity identityEntity = null;
		try {
			identityEntity = identityDao.queryAppIdAndAccount(cPlatform,platformUserName);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
			
		return identityEntity;
	}

	//验证账户信息
	public IdentityEntity checkYunsignIdentityEntityByUserId(int userId)throws ServiceException{
		IdentityEntity identityEntity = null;
		try {
			identityEntity = identityDao.findById(userId);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
			
		return identityEntity;
	}

	public JSONObject Bean2JSON(Object javaBean) {
		JSONObject ret = new JSONObject();
		try {
			Method[] methods = javaBean.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("get")) {
					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					Object value = method.invoke(javaBean, (Object[]) null);
					if(value instanceof String)
					{
						ret.put((String) field, (String) value);
					}
					else if(value instanceof Byte || value instanceof Integer)
					{
						ret.put((String) field, String.valueOf(value));
					}
					else if(value instanceof Timestamp)
					{
						ret.put((String) field, format.format(value));
					}
					else if(value instanceof Date)
					{
						ret.put((String) field, format.format(value));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String toJSONStrFromIdentity(IdentityEntity identityEntity) throws ServiceException
	{
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		
		JSONObject retJson = new JSONObject();
		  //中央承载ID
		retJson.put("id",identityEntity.getId()); 
		  //请求来源  来自平台 :"1"云签；“2”对接版；"3"本地版
		if((byte)1 == identityEntity.getSource())
		{
			retJson.put("optFrom","YUNSIGN"); 
		}
		else if((byte)2 == identityEntity.getSource())
		{
			retJson.put("optFrom","MMEC"); 
		}
		else if((byte)3 == identityEntity.getSource())
		{
			retJson.put("optFrom","LOCAL"); 
		}
		else
		{
			retJson.put("optFrom","UNKNOWN"); 
		}
		  //平台用户名
		retJson.put("platformUserName",filterString(identityEntity.getPlatformUserName())); 
		  //账号
		retJson.put("account",filterString(identityEntity.getAccount()));
		  //手机
		retJson.put("mobile",filterString(identityEntity.getMobile()));
		  //邮箱
		retJson.put("email",filterString(identityEntity.getEmail())); 
		  //用户类型
		retJson.put("type",identityEntity.getType()); 
		 //是否验证邮箱  2: No 1: YES
		retJson.put("emailValidate",identityEntity.getIsEmailVerified()); 
	    //密码
		//retJson.put("password",filterString(identityEntity.getPassword())); 
		 //是否验证手机2:No 1:Yes
		retJson.put("mobileValidate",identityEntity.getIsMobileVerified()); 
		  //实名审核状态  0"待审核"，1"已通过"，2"不通过"
		retJson.put("checkState",identityEntity.getIsAuthentic()); 
		//是否注销 0 ：NO 1：YES
		retJson.put("status",identityEntity.getStatus());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		  //激活注销的时间
		retJson.put("activeTime",filterString(sdf.format(identityEntity.getStatusTime()))); 
	     //是否为管理员
		retJson.put("isAdmin",identityEntity.getIsAdmin()); 
	     //是否为管理员
		retJson.put("businessAdmin",identityEntity.getBusinessAdmin()); 
		
		if(null != identityEntity.getWxOpenid() && ! "".equals(identityEntity.getWxOpenid()))
		{
			retJson.put("openId",identityEntity.getWxOpenid()); 
		}
		CompanyInfoEntity companyInfoEntity =null;
		//缔约室状态 1未申请 2审核中 3已通过 4已拒绝
		int contractroomCheck = 0;
		if(2==identityEntity.getType())
		{
			companyInfoEntity = identityEntity.getCCompanyInfo();
			if(null != companyInfoEntity)
			{
				if( null == companyInfoEntity.getCAttachmentBusi())
				{
					contractroomCheck = 1;
				}
				//工商注册号   //registernum
				retJson.put("businessNo",filterString(companyInfoEntity.getBusinessLicenseNo())); 
				  //企业ID
				retJson.put("enterpriseid",companyInfoEntity.getId()); 
				  //企业名称
				retJson.put("enterprisename",filterString(companyInfoEntity.getCompanyName())); 
				 //企业名称
				retJson.put("enterprisetype",filterString(companyInfoEntity.getCompanyType())); 
				//代理证书信息
				AttachmentEntity proxyPhoto = companyInfoEntity.getCAttachmentPhoto();
				if(null != proxyPhoto)
				{
					retJson.put("proxyPhotoPath",filterString(proxyPhoto.getAttachmentUri()));
				}
				//用户身份附件信息
				AttachmentEntity companyAttachmentEntity = companyInfoEntity.getCAttachmentBusi();
				if(null != companyAttachmentEntity)
				{
					retJson.put("businessNoPath",filterString(companyAttachmentEntity.getAttachmentUri()));
				}
			}
		}
		//父账号
		IdentityEntity parentEntity = null;
		
		if(0 != identityEntity.getParentId())
		{
			parentEntity = identityDao.findById(identityEntity.getParentId());
		}
		
		//获取用户资料
		CustomInfoEntity customInfoEntity = identityEntity.getCCustomInfo();
		if(null != customInfoEntity )
		{
			if( null == customInfoEntity.getCAttachmentIdA() && null == parentEntity)
			{
				if(contractroomCheck == 0)
				{
					contractroomCheck = 1;
				}
			}
			else
			{
				contractroomCheck = 2;
				//判断签约室状态
				SigningRoomEntity signingRoomEntity = null;
				//如果是子账号 返回主账号的签约室信息
				if(null != parentEntity)
				{
					signingRoomEntity = parentEntity.getCSigningRoom();
				}
				else
				{
					signingRoomEntity = identityEntity.getCSigningRoom();
				}
				if(null != signingRoomEntity)
				{
					//缔约室编号 对应云签   //serialnum
					retJson.put("contractroomId",signingRoomEntity.getId());
					  //缔约室名称
					if(2==identityEntity.getType())
					{
						retJson.put("contractroomName",companyInfoEntity.getCompanyName());
					}
					else
					{
						retJson.put("contractroomName",customInfoEntity.getUserName());
					}
					retJson.put("refuseseason",identityEntity.getReseve1());
					  //申请缔约室成功日期 
					retJson.put("contractroomDate",sdf.format(signingRoomEntity.getDateline()));
					  //申请缔约室后成功后会赋予的用户APPID
					retJson.put("userAppid",signingRoomEntity.getId()); 
				}
				
				//用户身份附件信息
				AttachmentEntity idImgA = customInfoEntity.getCAttachmentIdA();
		        if(null != idImgA)
				{
		        	 //身份证正面图片路径  attachmentUri 
		        	retJson.put("idImgAPath",filterString(idImgA.getAttachmentUri()));
				}
		        
		      //用户身份附件信息
				AttachmentEntity idImgB = customInfoEntity.getCAttachmentIdB();
		        if(null != idImgB)
				{
		        	 //身份证正面图片路径
		        	retJson.put("idImgBPath",filterString(idImgB.getAttachmentUri()));
				}
			}
			
			
			
			//子账号签约室状态对应为 该主账号的认证状态
			if(null != parentEntity)
			{
				if(1 == parentEntity.getIsAuthentic())
				{
					contractroomCheck = 3;
				}
				//如果已拒绝
				if(2 == parentEntity.getIsAuthentic())
				{
					contractroomCheck = 4;
				}
			}
			else
			{
				if(1 == identityEntity.getIsAuthentic())
				{
					contractroomCheck = 3;
				}
				//如果已拒绝
				if(2 == identityEntity.getIsAuthentic())
				{
					contractroomCheck = 4;
				}
			}
			
			
			retJson.put("contractroomCheck",contractroomCheck);
			 //姓名
			retJson.put("userName",filterString(customInfoEntity.getUserName()));
			 //姓名
			retJson.put("name",filterString(customInfoEntity.getUserName()));
			 //身份证
			retJson.put("identityCard",filterString(customInfoEntity.getIdentityCard())); 
			 //证件类型 1 身份证 2港澳通行证 3 台湾通行证 4护照 5军官证 6 士兵证
			retJson.put("cardType",filterString(customInfoEntity.getCardType())); 
			//固定电话
			retJson.put("phoneNumber",filterString(customInfoEntity.getPhoneNum())); 
			
		}
		else
		{
			retJson.put("contractroomCheck","1");
		}
     //待实现
//		  //买卖盾绑定，非空即为绑定
//		retJson.put("shieldValidate",filterString());
		if(0 != identityEntity.getParentId())
		{
			String authStr = "";
			try {
				UserRoleRelationEntity urEntity = userRoleRelationDao.findByUserId(identityEntity.getId());
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
			StringBuffer retStr = new StringBuffer("");
			for(int i = 0;i<auths.length;i++)
			{
				if( i == 0)
				{
					retStr.append(auths[i]);
				}
				else
				{
					retStr.append(","+auths[i]);
				}
			}
			retJson.put("role",retStr.toString());
		} 
		
		//根据用户ID 查询证书是否存在  绑定 1  未绑定 0  暂未实现
		List<UkeyInfoEntity> list = ukeyInfoDao.queryUkeyInfoByIdentityAndStatus(identityEntity,(byte)1);
		if(null ==list ||0 == list.size())
		{
			retJson.put("shieldValidate", "0");
		}
		else
		{
			retJson.put("shieldValidate", "1");
		}
		
		//查询绑定该账号的所有账号
		List<IdentityEntity> bandList = identityDao.queryByBindedId(identityEntity.getId());
		if(null != bandList && bandList.size() > 0)
		{
			JSONArray bandArray = new JSONArray();
			JSONObject jsonInfo = null;
			for(IdentityEntity bandInfo : bandList)
			{
				jsonInfo = new JSONObject();
				//只有平台管理员才行
				if("1".equals(bandInfo.getBusinessAdmin()))
				{
				
					jsonInfo.put("appId", bandInfo.getCPlatform().getAppId());
					if(null !=  bandInfo.getCPlatform().getProgram())
					{
						jsonInfo.put("program", bandInfo.getCPlatform().getProgram());
					}
					else
					{
						jsonInfo.put("program", bandInfo.getCPlatform().getCompanyName());
					}
					jsonInfo.put("platformUserName", bandInfo.getPlatformUserName());
					jsonInfo.put("bandUserId", bandInfo.getId()+"");
					bandArray.add(jsonInfo);
				}
			}
			retJson.put("bandList", bandArray.toString());
		}
		return retJson.toString();
	}
	
	String filterString(String str)
	{
		if(null == str )
		{
			return "";
		}
		else
		{
			return str;
		}
	}
	public  boolean isNotNull(String str)
	{
		if(null != str && !"".equals(str))
		{
			return true;
		}
		else
			return false;
	}
}
