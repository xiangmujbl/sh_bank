package com.mmec.centerService.feeModule.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.feeModule.dao.PayServiceDao;
import com.mmec.centerService.feeModule.entity.ContractDeductRecordEntity;
import com.mmec.centerService.feeModule.entity.PayServiceEntity;
import com.mmec.centerService.feeModule.service.ContractDeductRecordService;
import com.mmec.centerService.feeModule.service.PayService;
import com.mmec.centerService.feeModule.service.UserAccountService;
import com.mmec.centerService.userModule.service.LogService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CheckUtil;
import com.mmec.util.ConstantUtil;
import com.mmec.util.JSONUtil;

@Service("payService")
public class PayServiceImpl implements PayService{
	
	private static Logger log=Logger.getLogger(UserAccountServiceImpl.class);
	
	@Autowired
	private PayServiceDao payServiceDao;

	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private ContractDeductRecordService contractDeductRecordService;
	
	@Autowired
	private LogService logService;	
	
	/**
	 * 添加一条记录
	 */
	public String savePayService(PayServiceEntity p) throws ServiceException{
		//pay_code不能相同
		String paycode=p.getTypeCode();
		if(null!=payServiceDao.findByTypeCode(paycode)){
			throw new ServiceException(ConstantUtil.PAYCODE_ALREADY_EXISTS[0],
					p.getTypeCode()+ConstantUtil.PAYCODE_ALREADY_EXISTS[1],ConstantUtil.PAYCODE_ALREADY_EXISTS[2]);
		}
		payServiceDao.save(p);
		log.info("添加记录成功:"+p.toString());
		return null;
	}
	
	/**
	 * 更新一条记录
	 */
	public String updatePayService(Map<String,String> map){
		String typecode=map.get("typecode");
		//map中有typename
		if(null!=map.get("typename")){
			payServiceDao.UpdatePayServiceTypeName(map.get("typename"),typecode);
		}
		//map中有typedesc
		if(null!=map.get("typedesc")){
			payServiceDao.UpdatePayServiceTypeDesc(map.get("typedesc"), typecode);
		}
		//map中有typecontractname
		if(null!=map.get("typecontractname")){
			payServiceDao.UpdatePayServiceTypeContractame(map.get("typecontractname"), typecode);
		}
		log.info("更新记录成功:"+map.toString());
		return null;
	}
	
	
	/**
	 * 查询一条记录的信息
	 */
	public PayServiceEntity queryByPayCode(String paycode){
		return payServiceDao.findByTypeCode(paycode);
	}
	
	/**
	 * 查询所有服务code
	 */
	public List<PayServiceEntity> queryAll(){
		return payServiceDao.findAll();
	}
	
	
	
	/**
	 * 返回true有效
	 * 返回false的时候  returnData:金额参数无效
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public boolean reduceMoney(String useridStr,String moneyStr,String paycode,String payid) throws ServiceException{
		if(!CheckUtil.intCheck(useridStr).equals("success")){
			return false;
		}
		if(!CheckUtil.moneyCheck(moneyStr).equals("success")){
			return false;
		}
		if(null==this.queryByPayCode(paycode)){
			return false;
		}
		BigDecimal money=new BigDecimal(moneyStr);
		int userid=Integer.valueOf(useridStr);
		userAccountService.checkUserAccount(userid, ConstantUtil.ZERO_MONEY,paycode);
		userAccountService.reduceMoney(userid, money,paycode);
		ContractDeductRecordEntity cd=new ContractDeductRecordEntity();
		cd.setBillNum(String.valueOf(System.currentTimeMillis()));
		cd.setUserid(userid);
		cd.setConsumeType((byte)1);
		cd.setUpdateTime(new Date());
		cd.setDeductSum(money);
		cd.setTypecode(paycode);
		if(!"".equals(payid)){
			cd.setPayId(payid);
		}
		contractDeductRecordService.saveRecord(cd);
		return true;
	}
	
}