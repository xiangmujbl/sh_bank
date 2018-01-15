package com.mmec.centerService.userModule.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.userModule.dao.SealInfoDao;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.entity.SealEntity;
import com.mmec.centerService.userModule.service.SealService;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;
@Service("sealService")
public class SealServiceImpl extends UserBaseService implements SealService{
	
	@Autowired
	public SealInfoDao sealInfoDao;
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String addSeal(Map<String,String> datamap) throws ServiceException {
		// TODO Auto-generated method stub
		//判断平台是否存在
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("userAccount");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		byte type = identityEntity.getType();
		
		SealEntity sealEntity = new SealEntity();
		String sealName = datamap.get("sealName");
		String sealPath = datamap.get("sealPath");
		String originalPath = datamap.get("originalPath");
		String cutPath = datamap.get("cutPath");
		String bgRemovedPath = datamap.get("bgRemovedPath");

		sealEntity.setSealName(sealName);
		sealEntity.setSealPath(sealPath);
		sealEntity.setSealType(type);
		sealEntity.setOriginalPath(originalPath);
		sealEntity.setCutPath(cutPath);
		sealEntity.setBgRemovedPath(bgRemovedPath);
		sealEntity.setIsActive((byte)0);
		sealEntity.setSealNum(new Date().getTime()+"");
		//个人用户
		if(1 == type)
		{
			if(null == identityEntity.getCCustomInfo())
			{
				///////////6.12//////////////////
				throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],
						ConstantUtil.RETURN_CUST_NOT_EXIST[1], ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
				////////////////////////////////
				
			}
			sealEntity.setRelatedId(identityEntity.getCCustomInfo().getId());
		}
		//企业用户
		else if(2 == type)
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
		
		return "sealId:"+sealEntity.getSealId();
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String delSeal(Map<String,String> datamap) throws ServiceException {
		//判断平台是否存在
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("userAccount");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		byte type = identityEntity.getType();
		int relatedId = 0;
		//个人用户
		if(1 == type)
		{
			relatedId = identityEntity.getCCustomInfo().getId();
		}
		//企业用户
		else if(2 == type)
		{
			relatedId = identityEntity.getCCompanyInfo().getId();
		}
		String[] delSealIds = datamap.get("sealId").split(",");
		for(int i=0;i<delSealIds.length;i++)
		{
			String delSealId = delSealIds[i];
			// 删除 图章 信息
			try {
				sealInfoDao.delSealInfo(Integer.parseInt(delSealId),type,relatedId);
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], e.getMessage());
			}
		}
		return "";
	}

	@Override
	public String querySeal(Map<String,String> datamap) throws ServiceException {
		//判断平台是否存在
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String userAccount = (String) datamap.get("userAccount");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(userAccount,platformUserName,platformEntity);
		
		String queryType = datamap.get("queryType"); 
		if(null == queryType || "".equals(queryType))
		{
			byte type = identityEntity.getType();
			int relatedId = 0;
			//个人用户
			if(1 == type)
			{
				relatedId = identityEntity.getCCustomInfo().getId();
			}
			//企业用户
			else if(2 == type)
			{
				relatedId = identityEntity.getCCompanyInfo().getId();
			}
			// 删除 图章 信息
			try {
				List<SealEntity> sealList =sealInfoDao.querySealList(relatedId,type);
				//组装成JS对象输出
				JSONArray jsonArray = new JSONArray();
				for(SealEntity i : sealList)
				{
					jsonArray.add(Bean2JSON(i));
				}
				JSONObject jo = new JSONObject();
				jo.put("list", jsonArray);
				return jo.toString();
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], e.getMessage());
			}
		}
		else
		{
			String  param =  datamap.get("param"); 
			if(null == param || "".equals(param))
			{
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"查询数据字段为空 ",ConstantUtil.RETURN_FAIL_PARAMERROR[2]+"param is null ");
			}
			// 删除 图章 信息
			try {
				SealEntity sealEntity = null;
				if("1".equals(queryType))
				{
					sealEntity =sealInfoDao.findBySealId(Integer.parseInt(param));
				}
				else if("2".equals(queryType))
				{
					sealEntity =sealInfoDao.findBySealNum(param);
				}
				//组装成JS对象输出
				JSONObject jo = new JSONObject();
				jo.put("sealInfo", Bean2JSON(sealEntity));
				return jo.toString();
			} catch (Exception e) {
				e.printStackTrace();
				// 抛出异常 系统错误
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], e.getMessage());
			}
		}
	}

}
