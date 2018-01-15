package com.mmec.centerService.feeModule.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.feeModule.entity.PayServiceEntity;

public interface PayServiceDao extends JpaRepository<PayServiceEntity,Integer> {
	/**
	 * 根据Type_Code码查询记录
	 */
	public PayServiceEntity findByTypeCode(String typeCode);
	
	/**
	 * 查询所有的记录
	 */
	public List<PayServiceEntity> findAll();
	
	/**
	 * 更新Type_name码
	 */
	@Modifying
	@Query(value="update PayServiceEntity set typeName =? where typeCode =?")
	public int UpdatePayServiceTypeName(String typeName,String typeCode);
	
	/**
	 * 更新Type_Desc码
	 */
	@Modifying
	@Query(value="update PayServiceEntity set typeDesc =? where typeCode =?")
	public int UpdatePayServiceTypeDesc(String typeDesc,String typeCode);
	
	/**
	 * 更新表名称 
	 */
	@Transactional
	@Modifying
	@Query(value="update PayServiceEntity set typeContractname =? where typeCode =?")
	public int UpdatePayServiceTypeContractame(String typeContractname,String typeCode);
	
	/**
	 * 根据code删除记录
	 */
	@Transactional
	@Modifying
	@Query(value="delete PayServiceEntity s where s.typeCode=?")
	public void delPayServiceByCode(String typeCode);
}

