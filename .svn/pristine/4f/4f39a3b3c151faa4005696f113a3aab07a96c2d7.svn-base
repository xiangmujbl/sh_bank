package com.mmec.centerService.feeModule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.feeModule.dao.PlatformRoleDao;
import com.mmec.centerService.feeModule.entity.PlatformRoleEntity;
import com.mmec.centerService.feeModule.service.PlatformRoleService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.UserAuthDao;
import com.mmec.centerService.userModule.entity.AuthEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;

@Service("platformRoleService")
public class PlatformRoleServiceImpl implements PlatformRoleService{
	
	private static Logger log=Logger.getLogger(PlatformRoleServiceImpl.class);
	
	@Autowired
	private PlatformRoleDao proleDao;
	
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private UserAuthDao userAuthDao;
	
	
	/**
	 * 通过userid查询权限
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public List<AuthEntity> queryAuth(int userid) throws ServiceException{
		IdentityEntity i=identityDao.findById(userid);
		if(null==i){
			log.info("user is not exists");
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		int roleId=0;
		//平台角色
		if(i.getIsAdmin()==(byte)1){
			if(null!=i.getCPlatform().getAdminRoleId()&&!"".equals(i.getCPlatform().getAdminRoleId()))
					roleId=Integer.valueOf(i.getCPlatform().getAdminRoleId());
		}else{
		//非平台角色
			if(null!=i.getCPlatform().getDefaultRoleId()&&!"".equals(i.getCPlatform().getDefaultRoleId()))
					roleId=Integer.valueOf(i.getCPlatform().getDefaultRoleId());
		}
		List<AuthEntity> authlist=userAuthDao.queryAuthByPlatform(roleId);
		if(null==authlist||authlist.size()==0){
			log.info("role related authlist is not exists");
			throw new ServiceException(ConstantUtil.AUTH_IS_NULL[0],ConstantUtil.AUTH_IS_NULL[1],
					ConstantUtil.AUTH_IS_NULL[2]);
		}
		return authlist;
	}
}
