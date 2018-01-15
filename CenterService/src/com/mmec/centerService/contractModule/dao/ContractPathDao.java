package com.mmec.centerService.contractModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractPathEntity;

public interface ContractPathDao extends JpaRepository<ContractPathEntity,Integer>{
	//根据合同ID查询附件
	//查询主合同
	@Query(" SELECT c FROM ContractPathEntity c WHERE c.CContract = :CContract AND c.type = 1 ")
	public ContractPathEntity findContractPathByContractId(@Param("CContract")ContractEntity CContract);
	
	//查询合同附件(包含合同文件和附件)
	@Query(" SELECT c FROM ContractPathEntity c WHERE c.CContract = :CContract ")
	public List<ContractPathEntity> findListContractPathByContractId(@Param("CContract")ContractEntity CContract);
	
	@Modifying()
	@Query(" UPDATE ContractPathEntity c SET c.filePath =:filePath WHERE c.CContract = :CContract AND c.type = 1 ")
	public int updateMasterContractPath(@Param("filePath") String filePath,@Param("CContract")ContractEntity CContract);
	
	@Modifying()
	@Query(" DELETE ContractPathEntity c WHERE c.CContract = :CContract ")
	public int deleteContractPath(@Param("CContract")ContractEntity CContract);
	@Query(" SELECT c FROM ContractPathEntity c WHERE c.CContract = :CContract")
	public ContractPathEntity queryContractPathByPath(@Param("CContract") ContractEntity CContract);
}
