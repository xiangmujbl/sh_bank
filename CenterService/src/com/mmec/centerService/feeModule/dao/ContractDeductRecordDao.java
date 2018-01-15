package com.mmec.centerService.feeModule.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.mmec.centerService.feeModule.entity.ContractDeductRecordEntity;

public interface ContractDeductRecordDao extends PagingAndSortingRepository<ContractDeductRecordEntity,Integer> {
	/**
	 * 查询所有记录不分页
	 */
	public List<ContractDeductRecordEntity> findAll();
	
	/**
	 * 查询所有的记录分页
	 */
	@Query(value="select a from ContractDeductRecordEntity a")
	public List<ContractDeductRecordEntity> findAllWithPage(Pageable page);
	
	/**
	 * 查询所有记录的条数
	 */
	@Query(value="select count(a) from ContractDeductRecordEntity a")
	public Long countAll();
	
	/**
	 * 根据用户Id查询记录
	 */
	public List<ContractDeductRecordEntity> findByUserid(int userid);
	
	@Query(value="select count(a) from ContractDeductRecordEntity a where a.userid=?")
	public Long count(int userid);
	
	/**
	 * 根据用户Paycode查询记录
	 */
	public List<ContractDeductRecordEntity> findByTypecode(String typecode);
	
	@Query(value="select count(a) from ContractDeductRecordEntity a where a.typecode=?")
	public Long count(String typecode);
	
	/**
	 * 根据用户paycode和userid查询记录
	 */
	public List<ContractDeductRecordEntity> findByTypecodeAndUserid(String typecode,int userid);
	
	@Query(value="select count(a) from ContractDeductRecordEntity a where a.typecode=? and a.userid=?")
	public Long count(String typecode,int userid);
	/**
	 * 分页查询
	 * @param userid
	 * @param page
	 * @return
	 */
	@Query(value="select a from ContractDeductRecordEntity a where a.userid=? order by a.id desc")
	public List<ContractDeductRecordEntity> findPageByUseridPage(int userid,Pageable page);
	
	/**
	 * 分页查询 
	 * @param typecode
	 * @param page
	 * @return
	 */
	@Query(value="select a from ContractDeductRecordEntity a where a.typecode=? order by a.id desc")
	public List<ContractDeductRecordEntity> findPageByTypecode(String typecode,Pageable page);
	
	
	/**
	 * 分页查询
	 * @param typecode
	 * @param userid
	 * @param page
	 * @return
	 */
	@Query(value="select a from ContractDeductRecordEntity a where a.typecode=? and a.userid=? order by a.id desc")
	public List<ContractDeductRecordEntity> findPageByTypecodeAndUserid(String typecode,int userid,Pageable page);
	
	/**
	 * 根据typecode和payId查询
	 * @param typecode
	 * @param payId
	 * @return
	 */
	@Query(value="select a from ContractDeductRecordEntity a where a.typecode=? and a.payId=?")
	public List<ContractDeductRecordEntity> findByPayId(String typecode,String payId);
	/**
	 * 根据typecode,payId,consumeType查询
	 * @param typecode
	 * @param payId
	 * @return
	 */
	@Query(value="select a from ContractDeductRecordEntity a where a.typecode=? and a.payId=? and a.consumeType = ?")
	public List<ContractDeductRecordEntity> findByPayId(String typecode,String payId,byte consume_type);
	/**
	 * 充值次数记录查询
	 */
	@Query(value="select a from ContractDeductRecordEntity a where a.typecode=? and a.userid=? and a.consumeType=?")
	public List<ContractDeductRecordEntity>  findByType(String paycode,int userid,byte consumeType);
	
}


