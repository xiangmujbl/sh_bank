package com.mmec.centerService.depositoryModule.service;

import java.util.Map;

import org.apache.thrift.TException;

import com.mmec.centerService.depositoryModule.entity.EvidenceEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.InvoiceInfoEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface EvidenceService{
	/**
	 * 保存存证信息(结果/过程)
	 * @param map
	 * @return
	 * @throws TException 
	 */
	public ReturnData saveEvidence(Map<String,String> map) throws ServiceException, TException;
	
	/**
	 * 获取存证信息路径
	 * @param map
	 */
	public ReturnData downloadEvidence(Map<String,String> map) throws ServiceException;
	
	/**
	 * 保存信息
	 */
	public void saveEvidenceInfo(EvidenceEntity invoice);
	
	/**
	 * 分页查看存证信息
	 */
	public ReturnData pageEvidence(Map<String,String> map) throws ServiceException;
	
	/**
	 * 查看单个存证信息
	 */
	public ReturnData evidenceDetail(Map<String,String> map)  throws ServiceException;
	
	/**
	 * 查询单个人员信息
	 */
	public IdentityEntity findIdentity(int id)  throws ServiceException;
	
	/**
	 * 云签查看存证
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	public ReturnData evidenceDetailForYunSign(Map<String,String> map)  throws ServiceException;
	
	/**
	 * 云签查看验真报告
	 */
	public  ReturnData queryEvidenceReport(Map<String,String> datamap) throws ServiceException;
}
