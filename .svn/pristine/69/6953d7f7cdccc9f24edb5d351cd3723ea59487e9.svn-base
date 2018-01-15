package com.mmec.centerService.contractModule.service;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.userModule.service.LogService;
import com.mmec.centerService.vpt.service.VptService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.thrift.service.TempleteRMIServices.Iface;
import com.mmec.util.ConstantUtil;
@Component("templeteIface")
public class TempleteRMIServiceImpl implements Iface
{

	private static Logger  log = Logger.getLogger(TempleteRMIServiceImpl.class);
	@Autowired
	private ContractTempleteService contractTempleteService;	
	
	@Autowired
	private VptService vptService;	
	@Autowired
	private LogService logService;	
	
	@Transactional
	@Override
	public ReturnData addTemplte(Map<String, String> datamap) 
			throws TException
	{
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="addTemplte";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = contractTempleteService.addContractTemplete(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("addTemplte," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Transactional
	@Override
	public ReturnData modifyTemplte(Map<String, String> datamap)
			throws TException
	{
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="modifyTemplte";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = contractTempleteService.modContractTemplete(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("modifyTemplte," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Transactional
	@Override
	public ReturnData deleteTemplte(Map<String, String> datamap)
			throws TException
	{
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="deleteTemplte";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = contractTempleteService.delContractTemplete(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("deleteTemplte," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Transactional
	@Override
	public ReturnData queryTemplteList(Map<String, String> datamap)
			throws TException
	{
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="queryTemplteList";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = contractTempleteService.queryContractTempleteList(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("queryTemplteList," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Transactional
	@Override
	public ReturnData queryTemplteDetail(Map<String, String> datamap)
			throws TException
	{
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="queryTemplteDetail";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = contractTempleteService.queryContractTempleteDetail(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("queryTemplteDetail," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	
	@Transactional
	@Override
	public ReturnData statuTemplete(Map<String, String> datamap)
			throws TException
	{
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="statuTemplete";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = contractTempleteService.statusContractTemplete(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("addTemplte," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

}
