package com.mmec.centerService.feeModule.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mmec.centerService.feeModule.entity.ContractDeductRecordEntity;
import com.mmec.exception.ServiceException;
import com.mmec.util.DeductRecord;
import com.mmec.util.OrderTradingShield;

public interface ContractDeductRecordService{
	/**
	 * 保存一条record记录
	 */
	public String saveRecord(ContractDeductRecordEntity cd);
	
	/**
	 * 根据paycode查询记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(String paycode);
	
	/**
	 * 根据userid查询记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(int userid);
	
	/**
	 * 根据paycode和userid查询记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(String paycode,int userid);
	
	/**
	 * 根据userid分页查询记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(int userid,Pageable page);
	
	/**
	 * 根据paycode分页查询记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(String paycode,Pageable page);
	
	/**
	 * 根据paycode和userid分页查询记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(String paycode,int userid,Pageable page);
	
	/**
	 * 根据userid查询记录条数
	 */
	public Long countRecord(int userid);
	
	/**
	 * 根据paycode查询记录条数
	 */
	public Long countRecord(String paycode);
	
	/**
	 * 根据userid和paycode查询记录条数
	 */
	public Long countRecord(String paycode,int userid);
	
	/**
	 * 根据指定的payid和paycode查询指定记录
	 */
	public List<ContractDeductRecordEntity> queryRecord(String paycode,String payId);
	
	/**
	 * 查询所有的记录条数
	 */
	public Long countAll();
	
	/**
	 * 查询所有的记录
	 * @return
	 */
	public List<ContractDeductRecordEntity> queryAll();
	
	/**
	 * 查询所有的记录带分页
	 * @return
	 */
	public List<ContractDeductRecordEntity> queryAllWithPage(Pageable page);
	
	/**
	 * 查询带consumeType
	 */
	public List<ContractDeductRecordEntity> queryType(String paycode,int userid,byte type);
	
	/**
	 * for 云签
	 * @param paycode
	 * @param payId
	 * @return
	 */
	public List<DeductRecord> queryWithContractInfo(String paycode,int userid,Pageable page,boolean flag);
	
}