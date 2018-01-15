package com.mmec.centerService.feeModule.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.feeModule.dao.ContractDeductRecordDao;
import com.mmec.centerService.feeModule.entity.ContractDeductRecordEntity;
import com.mmec.centerService.feeModule.service.ContractDeductRecordService;
import com.mmec.util.DeductRecord;
import com.mmec.util.OrderTradingShield;

@Service("contractDeductRecordService")
public class ContractDeductRecordServiceImpl implements ContractDeductRecordService{
	
	@Autowired
	private ContractDeductRecordDao contractDeductRecordDao;
	
	@Autowired
	private ContractDao contractDao;
	
	
	/**
	 * 保存记录 
	 */
	public String saveRecord(ContractDeductRecordEntity cd){
		contractDeductRecordDao.save(cd);
		return null;
	}
	
	/**
	 * 查询记录 根据paycode
 	 */
	public List<ContractDeductRecordEntity> queryRecord(String paycode){
		return contractDeductRecordDao.findByTypecode(paycode);
	}
	
	/**
	 * 查询记录 根据userid
	 */
	public List<ContractDeductRecordEntity> queryRecord(int userid){
		return contractDeductRecordDao.findByUserid(userid);
	}
	
	/**
	 * 查询记录 根据paycode和userid
	 */
	public List<ContractDeductRecordEntity> queryRecord(String paycode,int userid){
		return contractDeductRecordDao.findByTypecodeAndUserid(paycode,userid);
	}
	
	/**
	 * 分页查询记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(int userid,Pageable page){
		return contractDeductRecordDao.findPageByUseridPage(userid, page);
	}
	
	/**
	 * 根据paycode分页查询记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(String typecode,Pageable page){
		return contractDeductRecordDao.findPageByTypecode(typecode, page);
	}
	
	/**
	 * 根据paycode和userid分页查询记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(String typecode,int userid,Pageable page){
		return contractDeductRecordDao.findPageByTypecodeAndUserid(typecode, userid, page);
	}
	
	/**
	 * 根据userid查询记录条数
	 */
	public Long countRecord(int userid){
		return contractDeductRecordDao.count(userid);
	}
	
	/**
	 * 根据paycode查询记录条数
	 */
	public Long countRecord(String paycode){
		return contractDeductRecordDao.count(paycode);
	}
	
	/**
	 * 根据userid和paycode查询记录条数
	 */
	public Long countRecord(String paycode,int userid){
		return contractDeductRecordDao.count(paycode, userid);
	}
	
	/**
	 * 根据指定的payid和paycode查询指定记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(String paycode,String payId){
		return contractDeductRecordDao.findByPayId(paycode, payId);
	}
	
	/**
	 * for 云签
	 * @param paycode
	 * @param payId
	 * @return
	 */
	public List<DeductRecord> queryWithContractInfo(String paycode,int userid,Pageable page,boolean flag){
		Gson gson=new Gson();
		List<ContractDeductRecordEntity> li=contractDeductRecordDao.findPageByTypecodeAndUserid(paycode,userid,page);
		List<DeductRecord> reslist=new ArrayList<DeductRecord>();
		int a=0;
		if(flag)
		a=-1;
		for(int i=0;i<li.size();i++){
			ContractDeductRecordEntity cd=li.get(i);
			byte type=cd.getConsumeType();
			if(type==(byte)1||type==(byte)3||type==(byte)5){
			String serialnum=cd.getPayId();
			String bqtext=cd.getBqtext();
			if(null!=bqtext)
				bqtext=bqtext.trim();
			String title="";
			if(null!=serialnum){
				ContractEntity ce=contractDao.findContractBySerialNum(serialnum);
				if(null!=ce)
				title=ce.getTitle();
			}
			String bqYears="",bqStartDate="",bqEndDate="";
			if(null!=bqtext&&!"".equals(bqtext)){
				HashMap map=gson.fromJson(bqtext, HashMap.class);
				if(bqtext.indexOf("bqYears")>-1)
					bqYears=(String)map.get("bqYears");
				if(bqtext.indexOf("bqStartDate")>-1)
					bqStartDate=(String)map.get("bqStartDate");
				if(bqtext.indexOf("bqEndDate")>-1){
					bqEndDate=(String)map.get("bqEndDate");
				}
			}
			DeductRecord dr=new DeductRecord();
			dr.setId(String.valueOf(cd.getId()));
			dr.setBillNum(cd.getBillNum());
			dr.setBqEndDate(bqEndDate);
			dr.setBqStartDate(bqStartDate);
			dr.setBqtext(bqtext);
			dr.setBqYears(bqYears);
			dr.setConsumeType(String.valueOf(cd.getConsumeType()));
			dr.setContractSerinum(serialnum);
			dr.setContractTitle(title);
			dr.setDeductSum(cd.getDeductSum());
			dr.setDeductTimes(String.valueOf(cd.getDeductTimes()));
			dr.setPayId(cd.getPayId());
			dr.setTypecode(cd.getTypecode());
			dr.setUpdateTime(String.valueOf(cd.getUpdateTime()));
			dr.setUserid(String.valueOf(cd.getUserid()));
			reslist.add(dr);
		}
		}
		return reslist;
	}
	
	/**
	 * 查询所有的记录条数
	 */
	public Long countAll(){
		return contractDeductRecordDao.countAll();
	}
	
	/**
	 * 查询所有的记录
	 * @return
	 */
	public List<ContractDeductRecordEntity> queryAll(){
		return contractDeductRecordDao.findAll();
	}
	
	/**
	 * 查询所有的记录带分页
	 * @return
	 */
	public List<ContractDeductRecordEntity> queryAllWithPage(Pageable page){
		return contractDeductRecordDao.findAllWithPage(page);
	}
	
	/**
	 * 查询带consumeType
	 */
	public List<ContractDeductRecordEntity> queryType(String paycode,int userid,byte type){
		return contractDeductRecordDao.findByType(paycode, userid, type);
	}

}


