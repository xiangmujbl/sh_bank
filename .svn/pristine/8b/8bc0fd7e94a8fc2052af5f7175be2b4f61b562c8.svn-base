package com.mmec.centerService.videoModule.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.videoModule.dao.ContractVideoDao;
import com.mmec.centerService.videoModule.dao.VideoSignDao;
import com.mmec.centerService.videoModule.entity.ContractVideoEntity;
import com.mmec.centerService.videoModule.entity.VideoSignEntity;
import com.mmec.centerService.videoModule.service.VideoSignService;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;
@Service("videoSignService")
public class VideoSignServiceImpl implements VideoSignService
{

	@Autowired
	private ContractVideoDao contractVideoDao;
	
	@Autowired
	private VideoSignDao videoSignDao;

	@Autowired
	private ContractDao contractDao;
	
	@Override
	public void registerVideoCode(Map<String, String> datamap)
			throws ServiceException
	{
		// 获取数据
		String appId = datamap.get("appId");
		String platformUserName = datamap.get("platformUserName");
		String orderId = datamap.get("orderId");
		String videoCode = datamap.get("videoCode");
		// 校验数据完整性
		if( isNull(appId) ||  isNull(platformUserName) ||  isNull(orderId) ||  isNull(videoCode))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1] + "：平台编码、平台用户名、订单编码、视频编码字段都不能为空", 
					ConstantUtil.RETURN_FAIL_PARAMERROR[2] + "：appId、platformUserName、orderId、videoCode字段都不能为空");
		}
		// 校验数据的合法性
		
		
		VideoSignEntity videoSign = new VideoSignEntity();
		videoSign.setAppId(appId);
		videoSign.setOrderId(orderId);
		videoSign.setPlatformUserName(platformUserName);
		videoSign.setRegisterTime(new Date());
		videoSign.setStatus(0);
		videoSign.setVideoCode(videoCode);
		//先把之前此合同的视频编码设置为无效
		try
		{
			videoSignDao.updateVideoSignStatus(appId, platformUserName,orderId,1);
			videoSignDao.save(videoSign);
		} catch (Exception e)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
	}

	@Override
	public String queryVideoCode(Map<String, String> datamap)
			throws ServiceException
	{
		// 获取数据
		String appId = datamap.get("appId");
		String platformUserName = datamap.get("platformUserName");
		String orderId = datamap.get("orderId");
		// 校验数据完整性
		if( isNull(appId) ||  isNull(platformUserName) ||  isNull(orderId) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1] + "：平台编码、平台用户名、订单编码字段都不能为空", 
					ConstantUtil.RETURN_FAIL_PARAMERROR[2] + "：appId、platformUserName、orderId字段都不能为空");
		}
		List<VideoSignEntity> list =  videoSignDao.findByAppIdAndPlatformUserNameAndOrderIdAndStatus(appId,platformUserName,orderId,0);
		if(null == list || list.size() == 0)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_RESULT_EMPTY[0],
					ConstantUtil.RETURN_RESULT_EMPTY[1], ConstantUtil.RETURN_RESULT_EMPTY[2]);
		}
		else if(list.size() > 1)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]);
		}
		else
		{
			// TODO Auto-generated method stub
			return list.get(0).getVideoCode();
		}
	}

	@Override
	public void addContractVideo(Map<String, String> datamap)
			throws ServiceException
	{
		// 获取数据
		String appId = datamap.get("appId");
		String orderId = datamap.get("orderId");
		// 校验数据完整性
		if( isNull(appId) || isNull(orderId))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1] + "：平台编码、订单编码字段都不能为空", 
					ConstantUtil.RETURN_FAIL_PARAMERROR[2] + "：appId、orderId字段都不能为空");
		}
		//将合同表中的视频签署标识改成 已签署
		try
		{
			contractDao.updateConttractVideoFlag("1",appId,orderId);
		} catch (Exception e)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
	}

	@Override
	public List<ContractVideoEntity> queryContractVideo(Map<String, String> datamap)
			throws ServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isNull(String str)
	{
		if(null == str || "".equals(str))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
