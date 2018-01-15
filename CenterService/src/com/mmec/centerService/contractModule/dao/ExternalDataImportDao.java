package com.mmec.centerService.contractModule.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.contractModule.entity.ExternalDataImportEntity;



public interface ExternalDataImportDao extends JpaRepository<ExternalDataImportEntity,Integer> 
{
	//更新状态
	@Modifying
	@Query(" UPDATE ExternalDataImportEntity s SET s.updateTime = :updateTime,s.dataStatus = :dataStatus WHERE s.serialNum = :serialNum AND s.source =:source ")
	public int updataDataStatus(@Param("updateTime") Date updateTime,@Param("dataStatus") int dataStatus,
			@Param("serialNum") String serialNum,@Param("source") String source);
}
