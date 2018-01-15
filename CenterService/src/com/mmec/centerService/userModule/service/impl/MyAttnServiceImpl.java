package com.mmec.centerService.userModule.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmec.centerService.userModule.dao.MyAttnDao;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.MyAttnEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.service.MyAttnService;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;

@Service("myAttnService")
public class MyAttnServiceImpl extends UserBaseService implements MyAttnService
{
	@Autowired
	private MyAttnDao myAttnDao;

	//添加联系人
	@Override
	public String addAttn(Map<String, String> datamap) throws ServiceException
	{
		//平台编码
		String appId = datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//用户平台用户名
		String platformUserName = datamap.get("platformUserName");
		
		if(null == platformUserName || "".equals(platformUserName) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"平台用户不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" platformUserName is null!");
		}
		//查询用户信息
		IdentityEntity identityEntity = null;
		try {
			 identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == identityEntity )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		//1账号  2、 手机号码 3、邮箱
		String accountType = (String) datamap.get("accountType");
		//联系人信息
		String attn = (String) datamap.get("attn");
		
		if(null == attn || "".equals(attn) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"联系人不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" attn is null!");
		}
		
		IdentityEntity attnIdentityEntity = null;
		try {
			//1
			if("1".equals(accountType))
			{
				attnIdentityEntity = identityDao.queryAppIdAndAccount(platformEntity, attn);
			}
			else if("2".equals(accountType))
			{
				attnIdentityEntity = identityDao.queryAppIdAndMobileAndType(platformEntity, attn,(byte)1);
			}
			else if("3".equals(accountType))
			{
				attnIdentityEntity = identityDao.queryAppIdAndEmailAndType(platformEntity, attn,(byte)2);
			}
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == attnIdentityEntity )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_ATTN_NOT_EXIST[0],
					ConstantUtil.RETURN_ATTN_NOT_EXIST[1], ConstantUtil.RETURN_ATTN_NOT_EXIST[2]);
		}
		
		List<MyAttnEntity> list = myAttnDao.findByUserIdAndAttnId(identityEntity.getId(), attnIdentityEntity.getId());
		if(list.size() > 0)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_ATTN_BEEN_EXIST[0],
					ConstantUtil.RETURN_ATTN_BEEN_EXIST[1], ConstantUtil.RETURN_ATTN_BEEN_EXIST[2]);
		}
		
		//添加联系人
		MyAttnEntity myAttnEntity = new MyAttnEntity();
		myAttnEntity.setUserId(identityEntity.getId());
		myAttnEntity.setAttnId(attnIdentityEntity.getId());
		myAttnEntity.setCreateTime(new Date());
		
		myAttnDao.save(myAttnEntity);
		
		return "";
	}

	//删除联系人
	@Override
	public String delAttn(Map<String, String> datamap) throws ServiceException
	{
		//平台编码
		String appId = datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//用户平台用户名
		String platformUserName = datamap.get("platformUserName");
		
		if(null == platformUserName || "".equals(platformUserName) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"平台用户不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" platformUserName is null!");
		}
		//查询用户信息
		IdentityEntity identityEntity = null;
		try {
			 identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == identityEntity )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		
		//联系人信息
		String attn = (String) datamap.get("attn");
		
		if(null == attn || "".equals(attn) )
		{
			//删除所有联系人
			myAttnDao.deleteAllAttnByUserId(identityEntity.getId());
		}
		else
		{
			IdentityEntity attnIdentityEntity = null;
			//1账号  2、 手机号码 3、邮箱
			String accountType = (String) datamap.get("accountType");
			try {
				//1
				if("1".equals(accountType))
				{
					attnIdentityEntity = identityDao.queryAppIdAndAccount(platformEntity, attn);
				}
				else if("2".equals(accountType))
				{
					attnIdentityEntity = identityDao.queryAppIdAndMobileAndType(platformEntity, attn,(byte)1);
				}
				else if("3".equals(accountType))
				{
					attnIdentityEntity = identityDao.queryAppIdAndEmailAndType(platformEntity, attn,(byte)2);
				}
			} catch (Exception e) {
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			
			if(null == attnIdentityEntity )
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_ATTN_NOT_EXIST[0],
						ConstantUtil.RETURN_ATTN_NOT_EXIST[1], ConstantUtil.RETURN_ATTN_NOT_EXIST[2]);
			}
			
			
			//删除联系人
			myAttnDao.deleteAllAttnByUserIdAndAttnId(identityEntity.getId(),attnIdentityEntity.getId());
		}
		
		return "";
	}

	//联系人列表
	@Override
	public String listAttn(Map<String, String> datamap) throws ServiceException
	{
		//平台编码
		String appId = datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//用户平台用户名
		String platformUserName = datamap.get("platformUserName");
		
		if(null == platformUserName || "".equals(platformUserName) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"平台用户不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" platformUserName is null!");
		}
		//查询用户信息
		IdentityEntity identityEntity = null;
		try {
			 identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == identityEntity )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		String param = datamap.get("param");
		List<IdentityEntity> list = null;
		if(null == param || "".equals(param) )
		{
			list = myAttnDao.findMyAttnByUserId(identityEntity.getId());
		}
		else
		{
			list = myAttnDao.findMyAttnByUserIdAndParam(identityEntity.getId(),param,param);
		}
		JSONArray jsArray = new JSONArray();
		JSONObject json = new JSONObject();
		for(IdentityEntity ident : list)
		{
			//个人用户
			if(1 == ident.getType())
			{
				//个人信息是否完善
				if(null != ident.getCCustomInfo())
				{
					json.put("userName", ident.getCCustomInfo().getUserName());
				}
				else
				{
					json.put("userName", ident.getMobile());
				}
			}
			//企业用户
			else if(2 == ident.getType())
			{
				//个人信息是否完善
				if(null != ident.getCCompanyInfo())
				{
					json.put("companyName", ident.getCCompanyInfo().getCompanyName());
				}
				else
				{
					json.put("companyName", ident.getEmail());
				}
				
				//个人信息是否完善
				if(null != ident.getCCustomInfo())
				{
					json.put("userName", ident.getCCustomInfo().getUserName());
				}
				else
				{
					json.put("userName", "");
				}
				
			}
			json.put("type", ident.getType()+"");
			json.put("userId", ident.getId()+"");
			json.put("account", ident.getAccount());
			json.put("platformName", ident.getPlatformUserName());
			json.put("mobile", ident.getMobile());
			json.put("email", ident.getEmail());
			jsArray.add(json);
		}

		json = new JSONObject();
		json.put("attnList",jsArray);
		return json.toString();
	}

	//根据手机号码查询个人用户 或根据邮箱名称查询 企业用户
	@Override
	public String queryMyAttn(Map<String, String> datamap)
			throws ServiceException
	{
		//平台编码
		String appId = datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//用户平台用户名
		String platformUserName = datamap.get("platformUserName");
		
		if(null == platformUserName || "".equals(platformUserName) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"平台用户不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" platformUserName is null!");
		}
		//查询用户信息
		IdentityEntity identityEntity = null;
		try {
			 identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity, platformUserName);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		if(null == identityEntity )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		
		List<IdentityEntity> list = myAttnDao.findMyAttnByUserId(identityEntity.getId());
		JSONArray jsArray = new JSONArray();
		JSONObject json = new JSONObject();
		for(IdentityEntity ident : list)
		{
			//个人用户
			if(1 == ident.getType())
			{
				//个人信息是否完善
				if(null != ident.getCCustomInfo())
				{
					json.put("userName", ident.getCCustomInfo().getUserName());
				}
				else
				{
					json.put("userName", ident.getMobile());
				}
				json.put("userId", ident.getId());
				json.put("account", ident.getAccount());
				json.put("platformName", ident.getPlatformUserName());
				json.put("mobile", ident.getMobile());
				json.put("email", ident.getEmail());
			}
			//企业用户
			else if(2 == ident.getType())
			{
				//个人信息是否完善
				if(null != ident.getCCompanyInfo())
				{
					json.put("userName", ident.getCCompanyInfo().getCompanyName());
				}
				else
				{
					json.put("userName", ident.getEmail());
				}
				json.put("userId", ident.getId());
				json.put("account", ident.getAccount());
				json.put("platformName", ident.getPlatformUserName());
				json.put("mobile", ident.getMobile());
				json.put("email", ident.getEmail());
			}
			jsArray.add(json);
		}
		json.put("attnList",jsArray);
		return json.toString();
	}

}
