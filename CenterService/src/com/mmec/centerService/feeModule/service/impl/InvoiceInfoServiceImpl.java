package com.mmec.centerService.feeModule.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.dao.ContractPathDao;
import com.mmec.centerService.contractModule.dao.SignRecordDao;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.SignRecordEntity;
import com.mmec.centerService.feeModule.dao.InvoiceInfoDao;
import com.mmec.centerService.feeModule.dao.UserServiceDao;
import com.mmec.centerService.feeModule.entity.ContractDeductRecordEntity;
import com.mmec.centerService.feeModule.entity.UserServiceEntity;
import com.mmec.centerService.feeModule.service.ContractDeductRecordService;
import com.mmec.centerService.feeModule.service.InvoiceInfoService;
import com.mmec.centerService.feeModule.service.UserAccountService;
import com.mmec.centerService.feeModule.service.UserService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.entity.CompanyInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.InvoiceInfoEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.service.LogService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CheckUtil;
import com.mmec.util.ConstantUtil;
import com.mmec.util.DateUtil;

@Service("invoiceInfoService")
public class InvoiceInfoServiceImpl implements InvoiceInfoService{
	
	private static Logger log=Logger.getLogger(InvoiceInfoServiceImpl.class);
	@Autowired
	private InvoiceInfoDao invoiceInfoDao;
	
	@Autowired
	private SignRecordDao signRecordDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractPathDao contractPathDao;
	
	@Autowired
	private PlatformDao platformDao;
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private UserServiceDao userServiceDao;
	
	@PersistenceContext(unitName = "uumsJPA")
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	@Autowired
	private ContractDeductRecordService contractDeductRecordService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	
	@Autowired
	private LogService logService;	
	
	
	/**
	 * 查询权限
	 */
	public ReturnData queryAuth(String appid) throws ServiceException{
		ReturnData rd=new ReturnData();
		return rd;
	}
	
	@Transactional
	public void saveInvoiceInfo(InvoiceInfoEntity invoice){
		entityManager.persist(invoice);
	};
	
	/**
	 * 充次
	 * userid--c_identity表主键
	 * times--充值次数
	 * paycode--合同充值传contract
	 * moneyStr--购买该次所扣金额
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public void addUserTimes(int userid,int times,String paycode,String moneyStr) throws ServiceException{
			BigDecimal money=new BigDecimal(moneyStr);
			userAccountService.checkUserAccount(userid, ConstantUtil.ZERO_MONEY,paycode);
			userAccountService.reduceMoney(userid, money,paycode);
			userService.checkUserService(userid, paycode,1);
			userService.addUserServiceTimes(userid, paycode, times);
			ContractDeductRecordEntity cd=new ContractDeductRecordEntity();
			cd.setBillNum(String.valueOf(System.currentTimeMillis()));
			cd.setUserid(userid);
			cd.setConsumeType((byte)2);
			cd.setUpdateTime(new Date());
			cd.setDeductSum(money);
			cd.setDeductTimes(times);
			cd.setTypecode(paycode);
			contractDeductRecordService.saveRecord(cd);
		}
	
	/**
	 * 根据合同查询数据
	 * @throws ServiceException 
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData querySerial(String serial) throws ServiceException{
		Gson gson=new Gson();
		Map<String,String> resmap=new HashMap<String,String>();
		ContractEntity c=contractDao.findContractBySerialNum(serial);
		if(null==c){
			log.info("serial:"+serial+"contract is not exists");
			throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],
					ConstantUtil.CONTRACT_IS_NOT_EXISTS[1], ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
			
		}
		List<SignRecordEntity> listSignRecord = signRecordDao.querySignRecordByContractId(c);
		if(null==listSignRecord&&listSignRecord.size()==0){
			log.info("serial:"+serial+"listSignRecord is not exists");
			throw new ServiceException(ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[0],
					ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[1],ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[2]);
		}
		//处理签署记录
		List<Map<String,String>> signlist=new ArrayList<Map<String,String>>();
		for(int i=0;i<listSignRecord.size();i++){
			SignRecordEntity signRecord = listSignRecord.get(i);
			Map<String,String> map=new HashMap<String,String>();
			IdentityEntity identity = signRecord.getCIdentity();
			if(null==identity){
				log.info("serial:"+serial+"identity is null");
				throw new ServiceException(ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[0],
						ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[1],ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[2]);
			}
			//个人 
			Map<String,String> signInfo = gson.fromJson(signRecord.getSigndata(), Map.class);
			map.put("cert", signInfo.get("cert"));
			if(identity.getType() ==1){
				if(null == identity.getCCustomInfo()){
					log.info("serial:"+serial+"customInfo is null");
					throw new ServiceException(ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[0],
							ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[1],ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[2]);
				}
				map.put("signer", identity.getCCustomInfo().getUserName());
				map.put("companyName","");
				map.put("num", identity.getCCustomInfo().getIdentityCard());
				map.put("mobile",identity.getMobile());
				map.put("title", c.getTitle());
				map.put("signtime", signRecord.getSignTime() == null ? "": DateUtil.toDateYYYYMMDDHHMM2(signRecord.getSignTime()));
			}
			//企业
			else if(identity.getType() ==2){
				CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
				if(null==companyInfo){
					log.info("serial:"+serial+"companyInfo is null");
					throw new ServiceException(ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[0],
							ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[1],ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[2]);
					
				}
				map.put("signer", identity.getCCompanyInfo().getCompanyName());
				map.put("companyName",identity.getCCompanyInfo().getCompanyName());
				map.put("mobile",identity.getMobile());
				map.put("title", c.getTitle());
				map.put("signtime", signRecord.getSignTime() == null ? "": DateUtil.toDateYYYYMMDDHHMM2(signRecord.getSignTime()));
				map.put("num", identity.getCCompanyInfo().getBusinessLicenseNo());
			}
			signlist.add(map);
		}
		return new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],
				ConstantUtil.RETURN_SUCC[2],gson.toJson(signlist));
	}
	
	
	/**
	 * 持久化UserService
	 */
	@Transactional
	public void saveUserService(UserServiceEntity u){
		entityManager.persist(u);
	};
	
	/**
	 * 创建本地部署版本的人员,及扣费
	 * @throws ServiceException 
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData localPay(String appId,int times,String paycode,int paytype) throws ServiceException{
		//查询平台
		log.info("localPay:"+"appId:"+appId+"times:"+String.valueOf("times")+"paycode:"+paycode+"paytype:"+String.valueOf(paytype));
		ReturnData rd=new ReturnData();
		PlatformEntity p=platformDao.findPlatformByAppId(appId);
		if(null==p){
			throw new ServiceException(ConstantUtil.RETURN_APP_NOT_EXIST[0],
					ConstantUtil.RETURN_APP_NOT_EXIST[1],ConstantUtil.RETURN_APP_NOT_EXIST[2]);
		}
		//查询平台的管理员账户
		IdentityEntity i=identityDao.queryChargingIdentityByPlateformId(p);
		if(null==i){
			throw new ServiceException(ConstantUtil.PLATFORM_ADMIN_NOT_EXIST[0],
					ConstantUtil.PLATFORM_ADMIN_NOT_EXIST[1],ConstantUtil.PLATFORM_ADMIN_NOT_EXIST[2]);
		}
		//扣费账户
		UserServiceEntity u=userService.queryByUserIdAndPayCode(i.getId(),paycode);
		//保存扣费账户
		if(null==u){
			userService.checkUserService(i.getId(),paycode,paytype);
			return new ReturnData(ConstantUtil.RETURN_AMNT_NOT_ENOUGH[0],ConstantUtil.RETURN_AMNT_NOT_ENOUGH[1],
					ConstantUtil.RETURN_AMNT_NOT_ENOUGH[2],"");
		}else{
			userService.reduceUserServiceTimes(i.getId(), paycode, times);
			ContractDeductRecordEntity cd=new ContractDeductRecordEntity();
			cd.setBillNum(String.valueOf(System.currentTimeMillis()));
			cd.setUserid(i.getId());
			cd.setConsumeType((byte)3);
			cd.setUpdateTime(new Date());
			cd.setDeductTimes(times);
			cd.setTypecode(paycode);
			cd.setDeductSum(ConstantUtil.ZERO_MONEY);
			cd.setPayId("");
			contractDeductRecordService.saveRecord(cd);
			return new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],
					ConstantUtil.RETURN_SUCC[2],"");
		}
		}
	
}