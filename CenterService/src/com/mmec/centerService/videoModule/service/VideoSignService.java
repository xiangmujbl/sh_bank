package com.mmec.centerService.videoModule.service;

import java.util.List;
import java.util.Map;

import com.mmec.centerService.videoModule.entity.ContractVideoEntity;
import com.mmec.exception.ServiceException;

/*
 * 视频签署接口
 * 1、添加视频编码
 * 2、查询视频编码
 * 3、添加视频附件
 * 4、查询视频附件
 */
public interface VideoSignService
{
	public void registerVideoCode(Map<String, String> datamap)throws ServiceException;
	
	public String queryVideoCode(Map<String, String> datamap)throws ServiceException;
	
	public void addContractVideo(Map<String, String> datamap)throws ServiceException;
	
	public List<ContractVideoEntity> queryContractVideo(Map<String, String> datamap)throws ServiceException;
}
