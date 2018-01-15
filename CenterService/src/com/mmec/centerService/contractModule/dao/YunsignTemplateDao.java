package com.mmec.centerService.contractModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.contractModule.entity.YunsignTemplateEntity;

public interface YunsignTemplateDao extends JpaRepository<YunsignTemplateEntity,Integer>{
	
	@Query("SELECT s FROM YunsignTemplateEntity s WHERE s.status = 1 ")
	public List<YunsignTemplateEntity> queryAllTemplate();
	
//	@Query("SELECT s FROM YunsignTemplateEntity s WHERE s.kind LIKE '%:kind%' AND s.status = 1 ")
	@Query("SELECT s FROM YunsignTemplateEntity s WHERE s.status = 1 AND s.kind  LIKE ? ")
	public List<YunsignTemplateEntity> queryTemplateByKind(String kind);
}
