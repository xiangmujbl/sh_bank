package com.mmec.centerService.contractModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.contractModule.entity.AuthorityContractEntity;



public interface AuthorityContractDao extends JpaRepository<AuthorityContractEntity,Integer>{
	
	/**
	 * 用来判断重复添加
	 * @param contractId
	 * @return
	 */
//	@Query(" SELECT c FROM PdfInfoEntity c WHERE c.Id= :id AND c.userId= :userId ")
//	public PdfInfoEntity findPdfInfoEntity(@Param("contractId")int id,@Param("userId")int userId);
	public AuthorityContractEntity findById(int id);
}
