package com.mmec.centerService.userModule.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.userModule.dao.AttachmentInfoDao;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformApplyDao;
import com.mmec.centerService.userModule.dao.PlatformApplyRecordDao;
import com.mmec.centerService.userModule.dao.PlatformCallCertDao;
import com.mmec.centerService.userModule.dao.PlatformCallbackDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.dao.UserAuthDao;
import com.mmec.centerService.userModule.entity.AttachmentEntity;
import com.mmec.centerService.userModule.entity.AuthEntity;
import com.mmec.centerService.userModule.entity.PlatformApplyEntity;
import com.mmec.centerService.userModule.entity.PlatformApplyRecordEntity;
import com.mmec.centerService.userModule.entity.PlatformCallCertEntity;
import com.mmec.centerService.userModule.entity.PlatformCallbackEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.service.PlatformService;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;

@Service("platformService")
public class PlatformServiceImpl extends UserBaseService implements PlatformService{
	// 主动注入dao
	@Autowired
	private PlatformDao platformDao;
	@Autowired
	private AttachmentInfoDao attachmentInfoDao;
	@Autowired
	private PlatformApplyDao platformApplyDao;
	@Autowired
	private PlatformCallCertDao platformCallCertDao;
	@Autowired
	private PlatformApplyRecordDao platformApplyRecordDao;
	@Autowired
	private IdentityDao identityDao;
	@Autowired
	private PlatformCallbackDao platformCallbackDao;
	@Autowired
	private UserAuthDao userAuthDao;
	
	// 平台申请
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String platformRegister(Map<String,String> datamap) throws ServiceException {
		// 审批编号
		String serialNum = datamap.get("serialNum");

		// 根据审批编号 判断 申请信息是否存在
		PlatformApplyEntity platformApplyEntity = null;
		try {
			platformApplyEntity = platformApplyDao.findBySerialNum(serialNum);
		} catch (Exception e) {
			// 抛出异常 参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1],
					ConstantUtil.RETURN_DB_ERROR[2] + e.getMessage());
		}
		// 申请审批编号已存在
		if (null != platformApplyEntity) {
			throw new ServiceException(ConstantUtil.RETURN_PLAT_APPLY_EXIST[0],
					ConstantUtil.RETURN_PLAT_APPLY_EXIST[1],
					ConstantUtil.RETURN_PLAT_APPLY_EXIST[2]);
		}

		AttachmentEntity attachmentEntity = new AttachmentEntity();
		platformApplyEntity = new PlatformApplyEntity();
		// 转成bean对象
		try {
			BeanUtils.populate(attachmentEntity, datamap);
			BeanUtils.populate(platformApplyEntity, datamap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			// 抛出异常 参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1], e.getMessage());
		}

		//判断传参是够完整
		String attcCheck = attachmentEntity.isBeanLegal();
		String plapCheck = platformApplyEntity.isBeanLegal();
		if(!"".equals(attcCheck) || !"".equals(plapCheck))
		{
			throw new ServiceException(
					ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+attcCheck+" "+plapCheck,
					ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
		}
		
		// 补充默认参数
		// 是否有效:"0"无效；“1”有效
		attachmentEntity.setAttachmentStatus((byte) 1);
		// 图片来源:"1"账号申请；“2”签约室申请；“3“平台申请；
		attachmentEntity.setAttachmentSource((byte) 3);
		// 附件类型:"1"个人身份证；“2”印业执照号；“3”法人身份证；
		attachmentEntity.setAttachmentType((byte) 1);

		if (null == attachmentEntity.getAttachmentName()
				|| "".equals(attachmentEntity.getAttachmentName())) {
			if (null != datamap.get("linkname")) {
				attachmentEntity.setAttachmentName(datamap.get("linkname") + "身份证");
			} else {
				attachmentEntity.setAttachmentName("身份证");
			}
		}
		// 图片附件表中 保存身份证附件
		try {
			attachmentEntity = attachmentInfoDao.save(attachmentEntity);
		} catch (Exception e) {
			e.printStackTrace();
			// 抛出异常 系统错误
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
					ConstantUtil.RETURN_SYSTEM_ERROR[1], e.getMessage());
		}
		// 补充默认参数
		// 审核状态,1:审核通过，2：审核未通过， 0:待审核
		platformApplyEntity.setStatus((byte) 0);
		// 接入类型: 1:对接版
		platformApplyEntity.setType((byte) 1);
		// 申请时间
		platformApplyEntity.setApplyTime(new Date());
		// 身份证附件
		platformApplyEntity.setCAttachment(attachmentEntity);
		try {
			platformApplyDao.save(platformApplyEntity);
		} catch (Exception e) {
			e.printStackTrace();
			// 抛出异常 系统错误
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
					ConstantUtil.RETURN_SYSTEM_ERROR[1], e.getMessage());
		}
		return "";
	}

	// 平台审核
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String platformCheck(Map<String,String> datamap) throws ServiceException {
		// 审批编号
		String serialNum = (String) datamap.get("serialNum");

		// 根据审批编号 判断 申请信息是否存在
		PlatformApplyEntity platformApplyEntity = null;
		try {
			platformApplyEntity = platformApplyDao.findBySerialNum(serialNum);
		} catch (Exception e) {
			// 抛出异常 参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1],
					ConstantUtil.RETURN_DB_ERROR[2] + e.getMessage());
		}
		// 申请信息不存在
		if (null == platformApplyEntity) {
			throw new ServiceException(
					ConstantUtil.RETURN_PLAT_APPLY_NOT_EXIST[0],
					ConstantUtil.RETURN_PLAT_APPLY_NOT_EXIST[1],
					ConstantUtil.RETURN_PLAT_APPLY_NOT_EXIST[2]);
		}

		// 申请信息不存在
		if (null == datamap.get("status")) {
			throw new ServiceException(
					ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+":审批状态为空",
					ConstantUtil.RETURN_FAIL_PARAMERROR[2]+"check status is null");
		}
		// 审批状态 1:审核通过，2：审核未通过
		String status = (String) datamap.get("status");
		// 审核通过 添加 平台信息
		if ("1".equals(status)) {
			// 查看平台编码是够存在
			String appId = (String) datamap.get("appId");
			if (null == appId || "".equals(appId)) {
				// 抛出异常 参数异常
				throw new ServiceException(
						ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1] + "平台ID不能为空",
						ConstantUtil.RETURN_FAIL_PARAMERROR[2]
								+ " AppId is null!");
			}
			// 先查看 平台ID是否已经存在
			PlatformEntity platformEntity = null;
			try {
				platformEntity = platformDao.findPlatformByAppId(appId);
			} catch (Exception e) {
				// 抛出异常 参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1],
						ConstantUtil.RETURN_DB_ERROR[2] + e.getMessage());
			}

			// 平台ID已经存在 抛出异常
			if (null != platformEntity) {
				// 抛出异常 参数异常
				throw new ServiceException(ConstantUtil.RETURN_PLAT_EXIST[0],
						ConstantUtil.RETURN_PLAT_EXIST[1],
						ConstantUtil.RETURN_PLAT_EXIST[2]);
			}
			AttachmentEntity attachmentEntity = new AttachmentEntity();
			platformEntity = new PlatformEntity();
			// 转成bean对象
			try {
				BeanUtils.populate(attachmentEntity, datamap);
				BeanUtils.populate(platformEntity, datamap);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
				// 抛出异常 参数异常
				throw new ServiceException(
						ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1] + e.getMessage(),
						ConstantUtil.RETURN_FAIL_PARAMERROR[2] + e.getMessage());
			}
			//判断传参是够完整
			String attcCheck = attachmentEntity.isBeanLegal();
			String platCheck = platformEntity.isBeanLegal();
			if(!"".equals(attcCheck) || !"".equals(platCheck))
			{
				throw new ServiceException(
						ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1]+attcCheck+" "+platCheck,
						ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
			}

			// 补充默认参数
			// 是否有效:"0"无效；“1”有效
			attachmentEntity.setAttachmentStatus((byte) 1);
			// 图片来源:"1"账号申请；“2”签约室申请；“3“平台申请；
			attachmentEntity.setAttachmentSource((byte) 3);
			// 附件类型:"1"个人身份证；“2”印业执照号；“3”法人身份证；
			attachmentEntity.setAttachmentType((byte) 2);
			
			// 图片附件表中 保存身份证附件
			try {
				attachmentEntity = attachmentInfoDao.save(attachmentEntity);
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(
						ConstantUtil.RETURN_SYSTEM_ERROR[0],
						ConstantUtil.RETURN_SYSTEM_ERROR[1], e.getMessage());
			}

			// 如果 平台审批通过后 没有新的 联系人、联系电话等信息 默认取申请表中的对应信息
			if (null == platformEntity.getLinkName()
					&& null != platformApplyEntity.getLinkName()) {
				platformEntity.setLinkName(platformApplyEntity.getLinkName());
			}
			if (null == platformEntity.getLinkTel()
					&& null != platformApplyEntity.getLinkTel()) {
				platformEntity.setLinkTel(platformApplyEntity.getLinkTel());
			}
			if (null == platformEntity.getAddress()
					&& null != platformApplyEntity.getAddress()) {
				platformEntity.setAddress(platformApplyEntity.getAddress());
			}
			if (null == platformEntity.getEmail()
					&& null != platformApplyEntity.getEmail()) {
				platformEntity.setEmail(platformApplyEntity.getEmail());
			}
			if (null == platformEntity.getCompanyName()
					&& null != platformApplyEntity.getCompanyName()) {
				platformEntity.setCompanyName(platformApplyEntity
						.getCompanyName());
			}
			if (null == platformEntity.getBusinessLicenseNo()
					&& null != platformApplyEntity.getBusinessLicenseNo()) {
				platformEntity.setBusinessLicenseNo(platformApplyEntity
						.getBusinessLicenseNo());
			}
			if (null != attachmentEntity) {
				platformEntity.setCAttachment(attachmentEntity);
			}
			platformEntity.setCreateTime(new Date());
			// 保存平台信息
			try {
				platformEntity = platformDao.save(platformEntity);
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
						ConstantUtil.RETURN_SYSTEM_ERROR[1], e.getMessage());
			}

			String auditResultMark = "";
			if(null != datamap.get("auditResultMark")){
					auditResultMark = (String) datamap.get("auditResultMark");
			}
			// 变更申请状态 并 增加 平台关联关系
			platformApplyDao.updateApplyPlatformBySerialNum(
					platformEntity,auditResultMark, Byte.valueOf(status), serialNum);
		} else {
			String auditResultMark = "";
			if(null != datamap.get("auditResultMark")){
					auditResultMark = (String) datamap.get("auditResultMark");
			}
			// 变更申请状态
			platformApplyDao.updateApplyStatus(Byte.valueOf(status), serialNum);
		}
		return "";
	}

	// 根据生成规则生成 用户的UUID :appId_platformUserId_time_mobile
	public String toGeneralUUID(Object appId, Object platformUserId,
			Object mobile) {
		String retStr = appId.toString() + charSplit
				+ platformUserId.toString() + charSplit
				+ sdf.format(new Date());
		if (null != mobile && !"".equals(mobile)) {
			retStr += charSplit + mobile;
		}
		return retStr;
	}

	private String charSplit = "_";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String platformApply(Map datamap) throws ServiceException {
		// TODO Auto-generated method stub
		PlatformApplyRecordEntity platformApplyRecordEntity = new PlatformApplyRecordEntity();
		// 转成bean对象
		try {
			BeanUtils.populate(platformApplyRecordEntity, datamap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			// 抛出异常 参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1], e.getMessage());
		}
		platformApplyRecordEntity.setCreateTime(new Date());
		// 保存 签约室申请 信息
		try {
			platformApplyRecordEntity = platformApplyRecordDao.save(platformApplyRecordEntity);
		} catch (Exception e) {
			e.printStackTrace();
			// 抛出异常 系统错误
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], e.getMessage());
		}
		return "";
	}
	
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String platformApplyCheck(Map<String,String> datamap) throws ServiceException {
		// TODO Auto-generated method stub
			PlatformApplyRecordEntity platformApplyRecordEntity = null;
			// 转成bean对象
			int id = 0;
			String content = "";
			try {
				id = Integer.parseInt(datamap.get("applyId"));
				content = datamap.get("content");
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 参数异常
				throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1], e.getMessage());
			}
			// 保存 签约室申请 信息
			try {
				platformApplyRecordEntity  = platformApplyRecordDao.findById(id);
				if( null == platformApplyRecordEntity)
				{
					throw new ServiceException(ConstantUtil.RETURN_PLAT_APPLY_RECORD_NOT_EXIST[0],
							ConstantUtil.RETURN_PLAT_APPLY_RECORD_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_APPLY_RECORD_NOT_EXIST[2]);
				}
				platformApplyRecordDao.updatePlatformApplyRecordStatus(content, id);
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], e.getMessage());
			}
			return "";
	}
	
	@Override
	public String platformApplyList(Map<String,String> datamap) throws ServiceException {
		// TODO Auto-generated method stub
		
		int currentPage = Integer.parseInt(datamap.get("currentPage"));
		int pageSize = Integer.parseInt(datamap.get("pageSize"));
		Pageable pageable = new PageRequest(currentPage, pageSize);
		List<PlatformApplyRecordEntity> datalist = new ArrayList<PlatformApplyRecordEntity>();
		// 保存 签约室申请 信息
		try {
			Page data = platformApplyRecordDao.findAll(pageable);
			datalist = data.getContent();
		} catch (Exception e) {
			e.printStackTrace();
			// 抛出异常 系统错误
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], e.getMessage());
		}
		//组装成JS对象输出
		JSONArray jsonArray = new JSONArray();
		for(PlatformApplyRecordEntity i : datalist)
		{
			jsonArray.add(Bean2JSON(i));
		}
		JSONObject jo = new JSONObject();
		jo.put("list", jsonArray);
		return jo.toString();
	}
	
	@Override
	public String platformQuery(Map<String,String> datamap) throws ServiceException 
	{
		if(null != datamap.get("appId") && !"".equals(datamap.get("appId")))
		{
			String appId = (String) datamap.get("appId");
			PlatformEntity platformEntity = checkPlatform(appId);
			String isCheckPlatform=platformEntity.getIsCheckPlatform();
			List<PlatformCallbackEntity> datalist = platformCallbackDao.queryAppPlatformCallbackList(appId);
			JSONObject json = Bean2JSON(platformEntity);
			
			json.put("isCheckPlatform", isCheckPlatform);//////8.07////////
			//增加证书判断  只有双通道 才查询证书
			if(null == platformEntity.getCallType() || "".equals(platformEntity.getCallType()) || "1".equals(platformEntity.getCallType()))
			{
				json.put("callType", "1");
			}
			else
			{
				json.put("callType", "2");
				List<PlatformCallCertEntity> certList = platformCallCertDao.queryAppPlatformCallCertByAppId(appId);
				if(null == certList || certList.size() == 0 || certList.size() > 1)
				{
					
				}
				else
				{
					PlatformCallCertEntity certEntity = certList.get(0);
					json.put("callBackCertName", certEntity.getCertName());
					json.put("callBackCertUrl", certEntity.getCertUrl());
				}
			}
			
			//组装成JS对象输出
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonData = null;
			
			for(PlatformCallbackEntity i : datalist)
			{
				jsonData = new JSONObject();
	
				if(null != i.getCallbackUrl() && !"".equals(i.getCallbackUrl()))
				{
					jsonData.put("callBackId", i.getId());
					jsonData.put("appId", i.getAppId());
					jsonData.put("callBackType", "callback");
					jsonData.put("url", i.getCallbackUrl());
					jsonData.put("callBackName", i.getCInterface().getInfterfaceName());
					jsonArray.add(jsonData);
					
				}
				if(null != i.getForwardUrl() && !"".equals(i.getForwardUrl()))
				{
					jsonData.put("callBackId", i.getId());
					jsonData.put("appId", i.getAppId());
					jsonData.put("callBackType", "forward");
					jsonData.put("url", i.getForwardUrl());
					jsonData.put("callBackName", i.getCInterface().getInfterfaceName());
					jsonArray.add(jsonData);
				}
			}
			
			json.put("callBackList", jsonArray);
			jsonArray = new JSONArray();
			if(null != platformEntity.getAdminRoleId() && !"0".equals(platformEntity.getAdminRoleId()))
			{
				try
				{
					List<AuthEntity> authList = userAuthDao.queryMMECAuthByRoleId(platformEntity.getAdminRoleId());
					for(AuthEntity auth : authList)
					{
						jsonData = new JSONObject();
						jsonData.put("authId", auth.getId());
						jsonData.put("authNum", auth.getAuthNum());
						jsonData.put("authName", auth.getAuthName());
						jsonData.put("authDesc", auth.getAuthDesc());
						jsonArray.add(jsonData);
					}
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			json.put("adminAuthList", jsonArray);
	//		jsonArray = new JSONArray();
	//		if(null != platformEntity.getDefaultRoleId() && !"0".equals(platformEntity.getDefaultRoleId()))
	//		{
	//			try
	//			{
	//				List<AuthEntity> authList = userAuthDao.queryMMECAuthByRoleId(platformEntity.getDefaultRoleId());
	//				for(AuthEntity auth : authList)
	//				{
	//					jsonData = new JSONObject();
	//					jsonData.put("authId", auth.getId());
	//					jsonData.put("authNum", auth.getAuthNum());
	//					jsonData.put("authName", auth.getAuthName());
	//					jsonData.put("authDesc", auth.getAuthDesc());
	//					jsonArray.add(jsonData);
	//				}
	//			} catch (Exception e)
	//			{
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//		}
	//		json.put("userAuthList", jsonArray);
			return  json.toString();
		}
		else
		{
			List<PlatformEntity>  platList = platformDao.findAll();
			//组装成JS对象输出
			JSONArray dataArray = new JSONArray();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonData = null;
			JSONObject json = new JSONObject();
			for(PlatformEntity platformEntity : platList)
			{
				json = new JSONObject();
				json = Bean2JSON(platformEntity);
				json.put("isCheckPlatform", platformEntity.getIsCheckPlatform());//////8.07///////
				List<PlatformCallbackEntity> datalist = platformCallbackDao.queryAppPlatformCallbackList(platformEntity.getAppId());
				
				for(PlatformCallbackEntity i : datalist)
				{
					jsonData = new JSONObject();
		
					if(null != i.getCallbackUrl() && !"".equals(i.getCallbackUrl()))
					{
						jsonData.put("callBackId", i.getId());
						jsonData.put("appId", i.getAppId());
						jsonData.put("callBackType", "callback");
						jsonData.put("url", i.getCallbackUrl());
						jsonData.put("callBackName", i.getCInterface().getInfterfaceName());
						jsonArray.add(jsonData);
						
					}
					if(null != i.getForwardUrl() && !"".equals(i.getForwardUrl()))
					{
						jsonData.put("callBackId", i.getId());
						jsonData.put("appId", i.getAppId());
						jsonData.put("callBackType", "forward");
						jsonData.put("url", i.getForwardUrl());
						jsonData.put("callBackName", i.getCInterface().getInfterfaceName());
						jsonArray.add(jsonData);
					}
				}
				
				json.put("callBackList", jsonArray);
				jsonArray = new JSONArray();
				if(null != platformEntity.getAdminRoleId() && !"0".equals(platformEntity.getAdminRoleId()))
				{
					try
					{
						List<AuthEntity> authList = userAuthDao.queryMMECAuthByRoleId(platformEntity.getAdminRoleId());
						for(AuthEntity auth : authList)
						{
							jsonData = new JSONObject();
							jsonData.put("authId", auth.getId());
							jsonData.put("authNum", auth.getAuthNum());
							jsonData.put("authName", auth.getAuthName());
							jsonData.put("authDesc", auth.getAuthDesc());
							jsonArray.add(jsonData);
						}
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				json.put("adminAuthList", jsonArray);
				dataArray.add(json);
			}
			return  dataArray.toString();
		}
	}
	
	@Override
	public String platformCallbackQuery(Map<String,String> datamap)throws ServiceException
	{
		List<PlatformCallbackEntity> datalist =  new  ArrayList<PlatformCallbackEntity>();
		if(null == datamap.get("appId") || "".equals(datamap.get("appId")))
		{
			datalist = platformCallbackDao.findAll();
		}
		else
		{
			String appId = datamap.get("appId");
			datalist = platformCallbackDao.queryAppPlatformCallbackList(appId);
		}
		JSONObject json = new JSONObject();
		//组装成JS对象输出
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonData = null;
		
		String callBackType = "";
		if(null != datamap.get("callBackType"))
		{
			callBackType =  datamap.get("callBackType");
		}
		
		for(PlatformCallbackEntity i : datalist)
		{
			jsonData = new JSONObject();
			
			if("callback".equals(callBackType) )
			{
				if(null != i.getCallbackUrl() && !"".equals(i.getCallbackUrl()))
				{
					jsonData.put("callBackId", i.getId());
					jsonData.put("appId", i.getAppId());
					jsonData.put("callBackType", "callback");
					jsonData.put("url", i.getCallbackUrl());
					jsonData.put("callBackName", i.getCInterface().getInfterfaceName());
					jsonArray.add(jsonData);
				}
			}
			else if("forward".equals(callBackType) )
			{
				if(null != i.getForwardUrl() && !"".equals(i.getForwardUrl()))
				{
					jsonData.put("callBackId", i.getId());
					jsonData.put("appId", i.getAppId());
					jsonData.put("callBackType", "forward");
					jsonData.put("url", i.getForwardUrl());
					jsonData.put("callBackName", i.getCInterface().getInfterfaceName());
					jsonArray.add(jsonData);
				}
			}
			else
			{
				if(null != i.getCallbackUrl() && !"".equals(i.getCallbackUrl()))
				{
					jsonData.put("callBackId", i.getId());
					jsonData.put("appId", i.getAppId());
					jsonData.put("callBackType", "callback");
					jsonData.put("url", i.getCallbackUrl());
					jsonData.put("callBackName", i.getCInterface().getInfterfaceName());
					jsonArray.add(jsonData);
				}
				if(null != i.getForwardUrl() && !"".equals(i.getForwardUrl()))
				{
					jsonData.put("callBackId", i.getId());
					jsonData.put("appId", i.getAppId());
					jsonData.put("callBackType", "forward");
					jsonData.put("url", i.getForwardUrl());
					jsonData.put("callBackName", i.getCInterface().getInfterfaceName());
					jsonArray.add(jsonData);
				}
			}
		}
		json.put("callBackList", jsonArray);
		return json.toString();
	}
}
