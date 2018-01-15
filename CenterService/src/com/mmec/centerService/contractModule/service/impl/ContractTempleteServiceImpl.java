package com.mmec.centerService.contractModule.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import com.mmec.centerService.contractModule.dao.ContractTemplateDao;
import com.mmec.centerService.contractModule.entity.ContractTemplateEntity;
import com.mmec.centerService.contractModule.service.ContractTempleteService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.service.impl.UserBaseService;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;
@Service("contractTempleteService")
public class ContractTempleteServiceImpl extends UserBaseService implements ContractTempleteService
{
	@Autowired
	protected ContractTemplateDao contractTemplateDao;
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public String addContractTemplete(Map<String, String> datamap)
			throws ServiceException
	{
		PlatformEntity platformEntity = checkPlatform(datamap.get("appId"));
		
		IdentityEntity identityEntity =  checkIdentityEntity(null,datamap.get("platformUserName"),platformEntity);
		
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
		//构造基本数据
		String templateName = datamap.get("templateName");
		String templateNum = datamap.get("templateNum");
		String fileName = datamap.get("fileName");
		String filePath = datamap.get("filePath");
		String originalName = datamap.get("originalName");
		ContractTemplateEntity conTempleteInfo = new ContractTemplateEntity();
		conTempleteInfo.setAppId(datamap.get("appId"));
		conTempleteInfo.setCreator(identityEntity);
		conTempleteInfo.setCPlatform(platformEntity);
		conTempleteInfo.setCreatTime(new Date());
		
		//状态：1启用，0未启用
		conTempleteInfo.setStatus((byte)1);
		
		//填充模板数据
		conTempleteInfo.setTemplateNum(templateNum);
		conTempleteInfo.setTemplateName(templateName);
		conTempleteInfo.setFileName(fileName);
		conTempleteInfo.setFilePath(filePath);
		conTempleteInfo.setOriginalName(originalName);
		
		//保存数据
		try {
			contractTemplateDao.save(conTempleteInfo);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}

	@Override
	public String modContractTemplete(Map<String, String> datamap)
			throws ServiceException
	{
		PlatformEntity platformEntity = checkPlatform(datamap.get("appId"));
		
		IdentityEntity identityEntity =  checkIdentityEntity(null,datamap.get("platformUserName"),platformEntity);
		
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
		
		//获取数据
		String templateNum = datamap.get("templateNum");
		if(null == templateNum || "".equals(templateNum))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.TEMPERNUM_IS_NULL[0],
					ConstantUtil.TEMPERNUM_IS_NULL[1], ConstantUtil.TEMPERNUM_IS_NULL[2]);
		}
		String templateName = datamap.get("templateName");
		String fileName = datamap.get("fileName");
		String filePath = datamap.get("filePath");
		String originalName = datamap.get("originalName");
		// TODO Auto-generated method stub
		ContractTemplateEntity conTempleteInfo = null;
		try
		{
			conTempleteInfo  = contractTemplateDao.findByTemplateNum(templateNum);
		} 
		catch (Exception e)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null == conTempleteInfo)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.TEMPER_NOT_EXIST[0],
					ConstantUtil.TEMPER_NOT_EXIST[1], ConstantUtil.TEMPER_NOT_EXIST[2]);
			
		}
		if(null != templateName && !"".equals(templateName))
		{
			conTempleteInfo.setTemplateName(templateName);
		}
		if(null != fileName && !"".equals(fileName))
		{
			conTempleteInfo.setFileName(fileName);
		}
		if(null != filePath && !"".equals(filePath))
		{
			conTempleteInfo.setFilePath(filePath);
		}
		if(null != originalName && !"".equals(originalName))
		{
			conTempleteInfo.setOriginalName(originalName);
		}
		//修改数据
		try {
			contractTemplateDao.save(conTempleteInfo);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}

	@Override
	public String delContractTemplete(Map<String, String> datamap)
			throws ServiceException
	{
		PlatformEntity platformEntity = checkPlatform(datamap.get("appId"));
		IdentityEntity identityEntity =  checkIdentityEntity(null,datamap.get("platformUserName"),platformEntity);
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
		
		//获取数据
		String templateNum = datamap.get("templateNum");
		if(null == templateNum || "".equals(templateNum))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.TEMPERNUM_IS_NULL[0],
					ConstantUtil.TEMPERNUM_IS_NULL[1], ConstantUtil.TEMPERNUM_IS_NULL[2]);
		}
		// TODO Auto-generated method stub
		ContractTemplateEntity conTempleteInfo = null;
		try
		{
			conTempleteInfo  = contractTemplateDao.findByTemplateNum(templateNum);
		} 
		catch (Exception e)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null == conTempleteInfo)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.TEMPER_NOT_EXIST[0],
					ConstantUtil.TEMPER_NOT_EXIST[1], ConstantUtil.TEMPER_NOT_EXIST[2]);
			
		}
		//删除数据
		try {
			contractTemplateDao.delete(conTempleteInfo);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}

	@Override
	public String queryContractTempleteList(Map<String, String> datamap)
			throws ServiceException
	{
		// TODO Auto-generated method stub
		PlatformEntity platformEntity = checkPlatform((String) datamap.get("appId"));
		//查询分两种  平台查询:app 用户查询:user
		String queryType = datamap.get("queryType");
		
		IdentityEntity identityEntity = null;
		List<ContractTemplateEntity> list = null;
		if("app".equals(queryType))
		{ 
			if(isNotNull(datamap.get("status")))
			{
				byte status = Byte.parseByte(datamap.get("status"));
				list = contractTemplateDao.queryContractTemplateListByAppIdAndStatus(platformEntity, status);
			}
			else
			{
				list = contractTemplateDao.queryContractTemplateListByAppId(platformEntity);
			}
		}
		else if("user".equals(queryType))
		{
			String userId = (String) datamap.get("userId");
			if(null == userId || "".equals(userId) )
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"用户编码不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" userId is null!");
			}
			
			try {
				 identityEntity = identityDao.findById(Integer.parseInt(userId));
			} catch (Exception e) {
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
			
			if(null == identityEntity )
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
						ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
			}
			if(isNotNull(datamap.get("status")))
			{
				byte status = Byte.parseByte(datamap.get("status"));
				list = contractTemplateDao.queryContractTemplateListByUserIdAndStatus(identityEntity.getId(), status);
			}
			else
			{
				list = contractTemplateDao.queryContractTemplateListByUserId(identityEntity.getId());
			}
			
		}
		else
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.QUERY_TYPE_ERROR[0],
					ConstantUtil.QUERY_TYPE_ERROR[1], ConstantUtil.QUERY_TYPE_ERROR[2]);
		}
		String retStr = "";
		if(null == list || list.size() == 0)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_RESULT_EMPTY[0],
					ConstantUtil.RETURN_RESULT_EMPTY[1], ConstantUtil.RETURN_RESULT_EMPTY[2]);
		}
		else
		{
			JSONArray templeteList = new JSONArray();
			JSONObject json = null;
			for(ContractTemplateEntity conTemplete : list)
			{
				 json = new JSONObject();
				 //合同信息
				 json.put("templeteId", conTemplete.getId());
				 json.put("creatTime", sdf.format(conTemplete.getCreatTime()));
				 json.put("templateName", conTemplete.getTemplateName());
				 json.put("templateNum", conTemplete.getTemplateNum());
				 json.put("fileName", conTemplete.getFileName());
				 json.put("filePath", conTemplete.getFilePath());
				 json.put("originalName", conTemplete.getOriginalName());
			     json.put("status", conTemplete.getStatus()+"");
			     if(null != conTemplete.getCreator())
			     {
					 //创建者信息
					 json.put("userId", String.valueOf(conTemplete.getCreator().getId()));
					 json.put("mobile", conTemplete.getCreator().getMobile());
					 json.put("email", conTemplete.getCreator().getEmail());
					 json.put("platformUserName", conTemplete.getCreator().getPlatformUserName());
					 json.put("appId", conTemplete.getCreator().getCPlatform().getAppId());
					 json.put("program", conTemplete.getCreator().getCPlatform().getProgram());
					 //姓名
					 if(null != conTemplete.getCreator().getCCustomInfo())
					 {
						 json.put("userName", conTemplete.getCreator().getCCustomInfo().getUserName());
					 }
					 //公司名
					 else if(null != conTemplete.getCreator().getCCompanyInfo())
					 {
						 json.put("ccompanyName", conTemplete.getCreator().getCCompanyInfo().getCompanyName());
					 }
			     }
				 
				 templeteList.add(json);
			}
			json = new JSONObject();
			json.put("templeteList", templeteList.toString());
			retStr = json.toString(); 
		}
		
		return retStr;
	}

	@Override
	public String queryContractTempleteDetail(Map<String, String> datamap)
			throws ServiceException
	{
		PlatformEntity platformEntity = checkPlatform(datamap.get("appId"));
		
		IdentityEntity identityEntity =  checkIdentityEntity(null,datamap.get("platformUserName"),platformEntity);
		
		if(null == identityEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[0],
					ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[1], ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST[2]);
		}
		//获取数据
		String templateNum = datamap.get("templateNum");
		if(null == templateNum || "".equals(templateNum))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.TEMPERNUM_IS_NULL[0],
					ConstantUtil.TEMPERNUM_IS_NULL[1], ConstantUtil.TEMPERNUM_IS_NULL[2]);
		}
		// TODO Auto-generated method stub
		ContractTemplateEntity conTemplete = null;
		try
		{
			conTemplete  = contractTemplateDao.findByTemplateNum(templateNum);
		} 
		catch (Exception e)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null == conTemplete)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.TEMPER_NOT_EXIST[0],
					ConstantUtil.TEMPER_NOT_EXIST[1], ConstantUtil.TEMPER_NOT_EXIST[2]);
			
		}
		
		JSONObject json =  new JSONObject();
	    //合同信息
	    json.put("templeteId", conTemplete.getId());
	    json.put("creatTime", sdf.format(conTemplete.getCreatTime()));
	    json.put("templateName", conTemplete.getTemplateName());
	    json.put("templateNum", conTemplete.getTemplateNum());
	    json.put("fileName", conTemplete.getFileName());
	    json.put("filePath", conTemplete.getFilePath());
        json.put("originalName", conTemplete.getOriginalName());
        json.put("status", conTemplete.getStatus()+"");
        if(null != conTemplete.getCreator())
        {
		    //创建者信息
		    json.put("mobile", conTemplete.getCreator().getMobile());
		    json.put("email", conTemplete.getCreator().getEmail());
		    json.put("platformUserName", conTemplete.getCreator().getPlatformUserName());
		    json.put("appId", conTemplete.getCreator().getCPlatform().getAppId());
		    json.put("program", conTemplete.getCreator().getCPlatform().getProgram());
		    //姓名
		    if(null != conTemplete.getCreator().getCCustomInfo())
		    {
		        json.put("userName", conTemplete.getCreator().getCCustomInfo().getUserName());
		    }
		    //公司名
		    else if(null != conTemplete.getCreator().getCCompanyInfo())
		    {
			    json.put("ccompanyName", conTemplete.getCreator().getCCompanyInfo().getCompanyName());
		    }
        }
		return json.toString();
	}

	@Override
	public String statusContractTemplete(Map<String, String> datamap)
			throws ServiceException
	{
		//获取数据
		String templateNum = datamap.get("templateNum");
		if(null == templateNum || "".equals(templateNum))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.TEMPERNUM_IS_NULL[0],
					ConstantUtil.TEMPERNUM_IS_NULL[1], ConstantUtil.TEMPERNUM_IS_NULL[2]);
		}
		// TODO Auto-generated method stub
		ContractTemplateEntity conTempleteInfo = null;
		try
		{
			conTempleteInfo  = contractTemplateDao.findByTemplateNum(templateNum);
		} 
		catch (Exception e)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		if(null == conTempleteInfo)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.TEMPER_NOT_EXIST[0],
					ConstantUtil.TEMPER_NOT_EXIST[1], ConstantUtil.TEMPER_NOT_EXIST[2]);
			
		}
		//状态：1启用，0未启用
		String status = datamap.get("status");
		//删除数据
		try {
			contractTemplateDao.updateTempleteStatus(Byte.parseByte(status),templateNum);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		
		return "";
	}
	
	/**
	 * 查询合同模版根据时间和页面
	 * @param datamap
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public String queryContractTempleteListByPage(Map<String, Object> datamap)
			throws ServiceException
	{
		PlatformEntity platformEntity = checkPlatform((String) datamap.get("appId"));
		String queryType="app";
		List<ContractTemplateEntity> list = null;
		String appId =datamap.get("appId").toString();
		String status=datamap.get("status").toString();
		Date endate=(Date)datamap.get("endate");
		String startime=datamap.get("startime").toString();
		Pageable page=(Pageable)datamap.get("page");
		String retStr = "";
		
		try
		{
			if(null!=status&&!"".equals(status)){
				if(null!=startime&&!"".equals(startime)){
				list=contractTemplateDao.queryContractTemplateByPage(appId,Byte.valueOf(status),sdf.parse(startime),endate,page);
				//count=recordDao.count_temp(userid,Byte.valueOf(status),sdf.parse(startime),endate);
				
				}else{
				list=contractTemplateDao.queryContractTemplateByPage1(appId,Byte.valueOf(status),endate,page);
				//	count=recordDao.count_temp(userid,Byte.valueOf(status),endate);
				}
			}else{
				if(null!=startime&&!"".equals(startime)){
					list=contractTemplateDao.queryContractTemplateByPage2(appId,sdf.parse(startime),endate,page);
					//count=recordDao.count_temp(appId,sdf.parse(startime),endate);
				}else{
					list=contractTemplateDao.queryContractTemplateByPage3(appId,endate,page);
					//count=recordDao.count_temp(appId,endate);
				}
			}
		}
		catch (Exception e)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
	
		if(null == list || list.size() == 0)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_RESULT_EMPTY[0],
					ConstantUtil.RETURN_RESULT_EMPTY[1], ConstantUtil.RETURN_RESULT_EMPTY[2]);
		}
		else
		{
			JSONArray templeteList = new JSONArray();
			JSONObject json = null;
			for(ContractTemplateEntity conTemplete : list)
			{
				 json = new JSONObject();
				 //合同信息
				 json.put("templeteId", conTemplete.getId());
				 json.put("creatTime", sdf.format(conTemplete.getCreatTime()));
				 json.put("templateName", conTemplete.getTemplateName());
				 json.put("templateNum", conTemplete.getTemplateNum());
				 json.put("fileName", conTemplete.getFileName());
				 json.put("filePath", conTemplete.getFilePath());
				 json.put("originalName", conTemplete.getOriginalName());
			     json.put("status", conTemplete.getStatus()+"");
			     if(null != conTemplete.getCreator())
			     {
					 //创建者信息
					 json.put("userId", String.valueOf(conTemplete.getCreator().getId()));
					 json.put("mobile", conTemplete.getCreator().getMobile());
					 json.put("email", conTemplete.getCreator().getEmail());
					 json.put("platformUserName", conTemplete.getCreator().getPlatformUserName());
					 json.put("appId", conTemplete.getCreator().getCPlatform().getAppId());
					 json.put("program", conTemplete.getCreator().getCPlatform().getProgram());
					 //姓名
					 if(null != conTemplete.getCreator().getCCustomInfo())
					 {
						 json.put("userName", conTemplete.getCreator().getCCustomInfo().getUserName());
					 }
					 //公司名
					 else if(null != conTemplete.getCreator().getCCompanyInfo())
					 {
						 json.put("ccompanyName", conTemplete.getCreator().getCCompanyInfo().getCompanyName());
					 }
			     }
				 
				 templeteList.add(json);
			}
			json = new JSONObject();
			json.put("templeteList", templeteList.toString());
			retStr = json.toString(); 
		}
		
		return retStr;
		
	}

	/**
	 * 查询合同模版总数
	 * 
	 */
	@Override
	public Long queryContractTempleteCount(Map<String, Object> datamap)
			throws ServiceException
	{
		Long count=0L;
		try
		{
		String appId =datamap.get("appId").toString();
		String status=datamap.get("status").toString();
		Date endate=(Date)datamap.get("endate");
		String startime=datamap.get("startime").toString();
		
		
			if(null!=status&&!"".equals(status)){
				if(null!=startime&&!"".equals(startime)){
				count=contractTemplateDao.queryContractTemplateCount(appId,Byte.valueOf(status),sdf.parse(startime),endate);
				
				}else{
					count=contractTemplateDao.queryContractTemplateCount3(appId,Byte.valueOf(status),endate);
				}
			}else{
				if(null!=startime&&!"".equals(startime)){
					count=contractTemplateDao.queryContractTemplateCount1(appId,sdf.parse(startime),endate);
				}else{
				count=contractTemplateDao.queryContractTemplateCount2(appId,endate);
				}
			}
		}
		catch (Exception e)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
	
		return count;
	}
}
