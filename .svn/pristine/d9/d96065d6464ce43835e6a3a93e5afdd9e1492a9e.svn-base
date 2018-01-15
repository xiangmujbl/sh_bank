package com.mmec.centerService.contractModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.contractModule.entity.SecurityEntity;

public interface SecurityDao extends JpaRepository<SecurityEntity,Integer>{
	
	@Query(" SELECT c FROM SecurityEntity c WHERE c.alias = :alias ")
	public SecurityEntity findSecurityEntity(@Param("alias")String alias);
}
