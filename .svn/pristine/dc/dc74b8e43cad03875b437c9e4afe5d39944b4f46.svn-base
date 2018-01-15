package com.mmec.centerService.feeModule.service;

import java.util.Map;

import com.mmec.centerService.feeModule.entity.IdAuthConfigEntity;
import com.mmec.exception.ServiceException;

/*
 * 用于认证判断与计费
 */
public interface IdAuthJudgeService
{
	//判断平台是否支持认证操作
	public IdAuthConfigEntity isAuthFee(Map<String,String> dataMap) throws ServiceException;
	//扣减次数
	public int updateTimes(IdAuthConfigEntity idAuthConfigEntity) throws ServiceException;
	//计费日志
	public void authLog(Map<String,String> dataMap,IdAuthConfigEntity idAuthConfigEntity) throws ServiceException;	
}

