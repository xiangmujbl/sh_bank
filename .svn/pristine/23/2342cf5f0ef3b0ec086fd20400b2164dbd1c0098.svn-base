package com.mmec.centerService.feeModule.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.contractModule.service.ContractRMIServiceImpl;
import com.mmec.centerService.feeModule.dao.IdAuthConfigDao;
import com.mmec.centerService.feeModule.dao.IdAuthLogDao;
import com.mmec.centerService.feeModule.entity.IdAuthConfigEntity;
import com.mmec.centerService.feeModule.entity.IdAuthLogEntity;
import com.mmec.centerService.feeModule.service.IdAuthJudgeService;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;
@Service("idAuthJudgeService")
public class IdAuthJudgeServiceImpl implements IdAuthJudgeService
{
	private static Logger  log = Logger.getLogger(IdAuthJudgeServiceImpl.class);
	
	@Autowired
	private PlatformDao platformDao;
	@Autowired
	private IdAuthConfigDao idAuthConfigDao;
	
	@Autowired
	private IdAuthLogDao idAuthLogDao;

	@Override
	public IdAuthConfigEntity isAuthFee(Map<String, String> dataMap) throws ServiceException
	{
		String appId = dataMap.get("appId");
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
					ConstantUtil.RETURN_DB_ERROR[1]+",平台查询失败", ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		//平台ID不存在  抛出异常
		if(null == platformEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],
					ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
		}
		int authType =  Integer.parseInt(dataMap.get("authType"));
		//查询该平台下是否已为查询服务充值 
		IdAuthConfigEntity idAuthConfigEntity = null;
		try {
			idAuthConfigEntity = idAuthConfigDao.queryByAppIdAndAuthTypeAndStatus(appId, authType, 0);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1]+"平台认证充值记录查询", ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		//充值记录不存在
		if(null == idAuthConfigEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_IDAUTH_NOT_CHARGE[0],
					ConstantUtil.RETURN_IDAUTH_NOT_CHARGE[1], ConstantUtil.RETURN_IDAUTH_NOT_CHARGE[2]);
		}
		
		//业务类型 1包年 2包次
		if(1 == idAuthConfigEntity.getServiceType())
		{
			if(1 == compareDate(new Date(),idAuthConfigEntity.getAuthEndTime()))
			{
				log.info("费用过期");
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_IDAUTH_NOT_ENOUTH_TIMES[0],
						ConstantUtil.RETURN_IDAUTH_NOT_ENOUTH_TIMES[1], ConstantUtil.RETURN_IDAUTH_NOT_ENOUTH_TIMES[2]);
			}
				
		}
		else
		{
			if(idAuthConfigEntity.getAuthServiceTimes() <= 0)
			{
				log.info("费用不足");
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_IDAUTH_NOT_ENOUTH_TIMES[0],
						ConstantUtil.RETURN_IDAUTH_NOT_ENOUTH_TIMES[1], ConstantUtil.RETURN_IDAUTH_NOT_ENOUTH_TIMES[2]);
			}
		}
		
		return idAuthConfigEntity;
	}
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public int updateTimes(IdAuthConfigEntity idAuthConfigEntity) throws ServiceException
	{
		int ret = 0;
		try {
			//业务类型 1包年 2包次
			if(2 == idAuthConfigEntity.getServiceType())
			{
				idAuthConfigDao.updateAuthServiceTimes(idAuthConfigEntity.getId());
			}
			//记录使用次数
			idAuthConfigDao.updateAuthUseTimes(idAuthConfigEntity.getId());
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1]+"修改平台认证消费记录失败", ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return ret;
	}
	
	@Override	
	public void authLog(Map<String, String> dataMap,IdAuthConfigEntity idAuthConfigEntity) throws ServiceException
	{
		//填充日志数据
		IdAuthLogEntity idAuthLogEntity = new IdAuthLogEntity();
		idAuthLogEntity.setAppId(idAuthConfigEntity.getAppId());
		idAuthLogEntity.setServiceType(idAuthConfigEntity.getServiceType());
		
		if(dataMap.toString().length() > 2000)
		{
			idAuthLogEntity.setInParam(dataMap.toString().substring(0,2000));
		}
		else
		{
			idAuthLogEntity.setInParam(dataMap.toString());
		}
		int baseLenth = dataMap.toString().length();
		
		int subLength = idAuthLogEntity.getInParam().length();
		
		System.out.println("截取前长度："+baseLenth);
		System.out.println("截取后长度："+subLength);
		idAuthLogEntity.setOutParam("");
		idAuthLogEntity.setAuthConfigId(idAuthConfigEntity.getId());
		idAuthLogEntity.setAuthType(idAuthConfigEntity.getAuthType());
		idAuthLogEntity.setServiceTime(1);
		idAuthLogEntity.setOptTime(new Date());
		
		// 先扣除费用,再记日志
		try {
			idAuthLogDao.save(idAuthLogEntity);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1]+"保留认证日志失败", ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
	}
	public int compareDate(Date now, Date endDate) {
        if (now.getTime() > endDate.getTime()) {
            return 1;
        } 
        else
        {
            return 0;
        }
    }
}
