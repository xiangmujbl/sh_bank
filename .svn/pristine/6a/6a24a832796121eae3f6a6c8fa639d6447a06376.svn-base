package com.mmec.centerService.contractModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.contractModule.entity.PdfInfoEntity;



public interface PdfInfoDao extends JpaRepository<PdfInfoEntity,Integer>{
	
	/**
	 * 用来判断重复添加
	 * @param contractId
	 * @return
	 */
	@Query(" SELECT c FROM PdfInfoEntity c WHERE c.contractId= :contractId AND c.userId= :userId ")
	public PdfInfoEntity findPdfInfoEntity(@Param("contractId")int contractId,@Param("userId")int userId);
	
	@Query(" SELECT c FROM PdfInfoEntity c WHERE c.contractId= :contractId ")
	public List<PdfInfoEntity> findPdfInfoEntitys(@Param("contractId")int contractId);
}
