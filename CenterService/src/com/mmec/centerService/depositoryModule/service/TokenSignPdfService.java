package com.mmec.centerService.depositoryModule.service;

import java.util.Map;

import org.apache.thrift.TException;

import com.mmec.centerService.depositoryModule.entity.EvidenceEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.InvoiceInfoEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

/**
 * 带签署业务token签署Pdf
 * @author Administrator
 *
 */
public interface TokenSignPdfService{
	
	/**
	 * 获取签名原文
	 */
	public ReturnData token_signpdf_Data(Map<String,String> datamap) throws ServiceException;
	
	/**
	 * 完成签署
	 */
	public ReturnData token_signpdf_over(Map<String,String> datamap) throws ServiceException;
}