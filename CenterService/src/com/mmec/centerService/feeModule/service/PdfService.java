package com.mmec.centerService.feeModule.service;

import java.util.Map;

import com.google.gson.Gson;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.pdf.CertBean;

public interface PdfService{
	
	/**
	 * 标准版签署-服务器证书
	 * @param map
	 * @return
	 */
	public  ReturnData standardPdfSign(Map<String,String> map);
	
	/**
	 * 自定义版签署个数和位置
	 * @return
	 */
	public  ReturnData customPdfSign();
	
	
	/**
	 * 签署业务逻辑规避
	 * @param appid
	 * @param ucid
	 * @param serialNum
	 * @param contract
	 * @param gson
	 * @return
	 */
	public ReturnData signcheck(String appid,String ucid,String serialNum,ContractEntity contract,Gson gson);
	
	/**
	 * 证书获取
	 * @param certtype 1-服务器证书 /  2-事件证书测试版  /3-事件证书正式版
	 * @return
	 * @throws ServiceException 异常封装
	 */
	public CertBean cert(int certtype,String os,Gson gson,String serialnum,int userid) throws  ServiceException;

}