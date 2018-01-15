package com.mmec.centerService.userModule.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.userModule.dao.UkeyInfoDao;
import com.mmec.centerService.userModule.entity.AttachmentEntity;
import com.mmec.centerService.userModule.entity.CompanyInfoEntity;
import com.mmec.centerService.userModule.entity.CustomInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformApplyRecordEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.entity.UkeyInfoEntity;
import com.mmec.centerService.userModule.service.UkeyService;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;
import com.mmec.util.JSONUtil;
@Service("ukeyService")
public class UkeyServiceImpl extends UserBaseService implements UkeyService{
	// 主动注入dao
	@Autowired
	private UkeyInfoDao ukeyInfoDao;

	//需求变更  一个账号可以绑定多个证书
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String saveUkey(Map<String,String> datamap) throws ServiceException{
		//判断平台是否存在
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String account = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(account,platformUserName,platformEntity);
		
		// TODO Auto-generated method stub
		String certNum = (String) datamap.get("certNum");
		String certContent = (String) datamap.get("certContent");
		
		//根据正式编码 证书内容 及状态判断 证书是否已经适用
		UkeyInfoEntity ukeyInfoEntity = checkUkey(certNum,certContent,identityEntity);
		
		//如果已经使用 不能新增
		if(null !=ukeyInfoEntity)
		{
			   //抛出异常   参数异常	shbank不校验公司名称
			//////////////6.12////////////////////
			if(2 == identityEntity.getType()&&datamap.get("shbank")==null)
			{
				String companyName = identityEntity.getCCompanyInfo().getCompanyName();
				String subjectStr = datamap.get("certSubject");
				if(-1 == subjectStr.indexOf(companyName))
				{
					throw new ServiceException(ConstantUtil.RETURN_COMPANYNAME_CERTSUBJECT_NOT_MATCH[0],
							ConstantUtil.RETURN_COMPANYNAME_CERTSUBJECT_NOT_MATCH[1], ConstantUtil.RETURN_COMPANYNAME_CERTSUBJECT_NOT_MATCH[2]);
				}else{
					throw new ServiceException(ConstantUtil.RETURN_CERT_EXIST[0],
							   ConstantUtil.RETURN_CERT_EXIST[1], ConstantUtil.RETURN_CERT_EXIST[2]);
				}
			}
			else if(1 == identityEntity.getType()&&datamap.get("shbank")==null)
			{
				String userName = identityEntity.getCCustomInfo().getUserName();
				String subjectStr = datamap.get("certSubject");
				if(-1 == subjectStr.indexOf(userName))
				{   
					//证书已绑定，且用户名一致
					throw new ServiceException(ConstantUtil.RETURN_USERNAME_CERTSUBJECT_NOT_MATCH[0],
							ConstantUtil.RETURN_USERNAME_CERTSUBJECT_NOT_MATCH[1], ConstantUtil.RETURN_USERNAME_CERTSUBJECT_NOT_MATCH[2]);
				}else{
					//证书已绑定，用户名不一致
					throw new ServiceException(ConstantUtil.RETURN_CERT_EXIST[0],
							   ConstantUtil.RETURN_CERT_EXIST[1], ConstantUtil.RETURN_CERT_EXIST[2]);
				}
			}
            //////////////6.12////////////////////
					
		}
		if(null == ukeyInfoEntity)
		{
			ukeyInfoEntity = new UkeyInfoEntity();
		}
		
		//2016.3.10 新增匹配规则
		//企业用户验证证书使用者是否包含企业名
		if(2 == identityEntity.getType()&&datamap.get("shbank")==null)
		{
			//企业信息不完整 没有企业名 不允许绑定
			if( null == identityEntity.getCCompanyInfo())
			{
				throw new ServiceException(ConstantUtil.RETURN_COMP_NOT_EXIST[0],
						ConstantUtil.RETURN_COMP_NOT_EXIST[1], ConstantUtil.RETURN_COMP_NOT_EXIST[2]);
			}
			else
			{
				String companyName = identityEntity.getCCompanyInfo().getCompanyName();
				String subjectStr = datamap.get("certSubject");
				if(-1 == subjectStr.indexOf(companyName))
				{
					throw new ServiceException(ConstantUtil.RETURN_COMPANYNAME_CERTSUBJECT_NOT_MATCH[0],
							ConstantUtil.RETURN_COMPANYNAME_CERTSUBJECT_NOT_MATCH[1], ConstantUtil.RETURN_COMPANYNAME_CERTSUBJECT_NOT_MATCH[2]);
				}
			}
		}
		//个人用户验证证书使用者是否包含用户名
		else if(1 == identityEntity.getType()&&datamap.get("shbank")==null)
		{
			//企业信息不完整 没有企业名 不允许绑定
			if( null == identityEntity.getCCustomInfo())
			{
				throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],
						ConstantUtil.RETURN_CUST_NOT_EXIST[1], ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
			}
			else
			{
				String userName = identityEntity.getCCustomInfo().getUserName();
				String subjectStr = datamap.get("certSubject");
				if(-1 == subjectStr.indexOf(userName))
				{
					throw new ServiceException(ConstantUtil.RETURN_USERNAME_CERTSUBJECT_NOT_MATCH[0],
							ConstantUtil.RETURN_USERNAME_CERTSUBJECT_NOT_MATCH[1], ConstantUtil.RETURN_USERNAME_CERTSUBJECT_NOT_MATCH[2]);
				}
			}
		}
		
		// 转成bean对象
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String startingDate = (String) datamap.get("startingDate");
		String expiringDate = (String) datamap.get("expiringDate");
		String certSubject = (String) datamap.get("certSubject");
		String signDate = (String) datamap.get("signDate");
		String signValue = (String) datamap.get("signValue");
		String type = (String) datamap.get("certType");
		ukeyInfoEntity.setCertContent(certContent);
		ukeyInfoEntity.setCertNum(certNum);
		ukeyInfoEntity.setStartingDate(new Date());
		ukeyInfoEntity.setExpiringDate(new Date());
		/*try {
			ukeyInfoEntity.setStartingDate(sdf.parse(startingDate));
			ukeyInfoEntity.setExpiringDate(sdf.parse(expiringDate));
		} catch (ParseException e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1], ConstantUtil.RETURN_FAIL_PARAMERROR[2]+e.getMessage());
		}*/
		ukeyInfoEntity.setSubject(certSubject);
		ukeyInfoEntity.setSignature(signDate);
		ukeyInfoEntity.setBindTime(new Date());
		ukeyInfoEntity.setCIdentity(identityEntity);
		ukeyInfoEntity.setType(Integer.parseInt(type));
		ukeyInfoEntity.setStatus((byte)1);
		try {
			ukeyInfoEntity = ukeyInfoDao.save(ukeyInfoEntity);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		JSONObject retObj = Bean2JSON(ukeyInfoEntity);
		
        return retObj.toString();
	}

	@Override
	public String queryUkey(Map<String,String> datamap) throws ServiceException{
		
		String retStr = "";
		
		String userId =  datamap.get("userId");
		if(null == userId || "".equals(userId))
		{
			// TODO Auto-generated method stub
			String certNum = (String) datamap.get("certNum");
			String certContent = (String) datamap.get("certContent");
			UkeyInfoEntity ukeyInfoEntity = checkUkey(certNum,certContent,null);
			//证书不存在则抛出异常
			if(null ==ukeyInfoEntity)
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_CERT_NOT_EXIST[0],
						ConstantUtil.RETURN_CERT_NOT_EXIST[1], ConstantUtil.RETURN_CERT_NOT_EXIST[2]);
			}
			retStr = Bean2JSON(ukeyInfoEntity).toString();
		}
		else
		{
			IdentityEntity identity = identityDao.findById(Integer.parseInt(userId));
			if(null == identity)
			{
				//抛出异常   参数异常
				throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
						ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
			}
			List<UkeyInfoEntity> datalist = ukeyInfoDao.queryUkeyInfoByIdentity(identity);
			//组装成JS对象输出
			JSONArray jsonArray = new JSONArray();
			for(UkeyInfoEntity i : datalist)
			{
				jsonArray.add(Bean2JSON(i));
			}
			retStr = jsonArray.toString();
		}
		return retStr;
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String updateUkey(Map<String,String> datamap) throws ServiceException{
		//判断平台是否存在
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String account = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(account,platformUserName,platformEntity);
		
		// TODO Auto-generated method stub
//		String certNum = (String) datamap.get("certNum");
//		String certContent = (String) datamap.get("certContent");
//		UkeyInfoEntity ukeyInfoEntity = checkUkey(certNum,certContent,identityEntity);
		

		String certId= (String) datamap.get("certId");
		UkeyInfoEntity ukeyInfoEntity = checkUkeyById(certId);
		//证书不存在则抛出异常
		if(null ==ukeyInfoEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_CERT_NOT_EXIST[0],
					ConstantUtil.RETURN_CERT_NOT_EXIST[1], ConstantUtil.RETURN_CERT_NOT_EXIST[2]);
		}

		// 转成bean对象
		try {
			BeanUtils.populate(ukeyInfoEntity, datamap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			// 抛出异常 参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1], e.getMessage());
		}
		//增加证书与用户关联关系  并保存
		ukeyInfoEntity.setCIdentity(identityEntity);
		try {
			ukeyInfoEntity.setStatus((byte)1);
			ukeyInfoDao.save(ukeyInfoEntity);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public String unbundUkey(Map<String,String> datamap) throws ServiceException{
		//判断平台是否存在
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		//查看用户账号或平台用户名称是否存在
		String account = (String) datamap.get("account");
		String platformUserName = (String) datamap.get("platformUserName");
		IdentityEntity identityEntity = checkIdentityEntity(account,platformUserName,platformEntity);
		
		// TODO Auto-generated method stub
		String certId= (String) datamap.get("certId");
		UkeyInfoEntity ukeyInfoEntity = null;
		//解绑证书
		try {
			 ukeyInfoEntity = checkUkeyById(certId);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		//证书不存在则抛出异常
		if(null ==ukeyInfoEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_CERT_NOT_EXIST[0],
					ConstantUtil.RETURN_CERT_NOT_EXIST[1], ConstantUtil.RETURN_CERT_NOT_EXIST[2]);
		}
		//解绑证书
		try {
			ukeyInfoDao.updateIdentityState(Integer.parseInt(certId));
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return "";
	}

	//验证证书信息
	public UkeyInfoEntity checkUkey(String certNum,String certContent,IdentityEntity identityEntity)throws ServiceException{
		
		if(null == certNum || "".equals(certNum) || null == certContent || "".equals(certContent) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"证书编码和证书内容不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" certNum or certContent is null!");
		}
		//先查看证书编码和证书内容是否已经存在
		UkeyInfoEntity UkeyInfoEntity = null;
		try {
//			if(null == identityEntity)
//			{
				//判断证书是否已经使用了
				UkeyInfoEntity = ukeyInfoDao.findByCertNumAndCertContentAndStatus(certNum, certContent,(byte)1);
//			}
//			else
//			{
//				UkeyInfoEntity = ukeyInfoDao.queryUkeyInfoByIdentityAndCert(certNum, certContent,identityEntity);
//			}
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return UkeyInfoEntity;
	}
	
	//验证证书信息
	public UkeyInfoEntity checkUkeyById(String certId)throws ServiceException{
		if(null == certId || "".equals(certId) )
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"证书Id不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" certId is null!");
		}
		//先查看证书编码和证书内容是否已经存在
		UkeyInfoEntity UkeyInfoEntity = null;
		try {
			 UkeyInfoEntity = ukeyInfoDao.findById(Integer.parseInt(certId));
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		return UkeyInfoEntity;
	}
}
