package com.mmec.centerService.userModule.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.service.WXService;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;

@Service("wXService")
public class WXServiceImpl extends UserBaseService implements WXService {

	@Override
	public String bundingWX(Map<String, String> datamap)
			throws ServiceException {
		// TODO Auto-generated method stub
		PlatformEntity platformEntity = checkPlatform((String) datamap.get("appId"));
		//查看用户账号是否存在
		String account = (String) datamap.get("account");
		
		String wxOpenId = (String) datamap.get("wxOpenId");
		if(null == account || "".equals(account) || null == wxOpenId || "".equals(wxOpenId))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"用户账号和待绑定微信号不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" account or wxOpenId is null!");
		}
		//1、 手机号码登录 2、邮箱登录
		String accountType = (String) datamap.get("accountType");
		IdentityEntity identityEntity = null;
		try {
			//1
		    if("1".equals(accountType))
			{
				identityEntity = identityDao.queryAppIdAndMobileAndType(platformEntity, account,(byte)1);
			}
			else if("2".equals(accountType))
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
		
		try {
			//如果传入的密码不为空  那么修改密码
			if(null !=  datamap.get("password") && !"".equals( datamap.get("password")))
			{
				identityEntity.setPassword(datamap.get("password"));
			}
			//绑定微信号
			identityEntity.setWxOpenid(wxOpenId);
			identityDao.save(identityEntity);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}

	@Override
	public String unbundWX(Map<String, String> datamap) throws ServiceException {
		// TODO Auto-generated method stub
			PlatformEntity platformEntity = checkPlatform((String) datamap.get("appId"));
			//查看用户账号是否存在
			String userId = (String) datamap.get("userId");
			if(null == userId || "".equals(userId))
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"解绑的用户账号不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" userId is null!");
			}
			//1、 手机号码登录 2、邮箱登录
			IdentityEntity identityEntity = null;
			try {
					identityEntity = identityDao.findById(Integer.parseInt(userId));
				
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
			
			try {
				//解绑微信号
				identityDao.updateIdentityWxOpenid("",identityEntity.getId());
			} catch (Exception e) {
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			return "";
	}

}
