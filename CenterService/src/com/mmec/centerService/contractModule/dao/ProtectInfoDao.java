package com.mmec.centerService.contractModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mmec.centerService.contractModule.entity.ProtectInfoEntity;



public interface ProtectInfoDao extends JpaRepository<ProtectInfoEntity,Integer>{
	//查询签名域的个数
	@Query(" SELECT c FROM ProtectInfoEntity c WHERE c.serialNum = :serialNum AND c.status = 0 ")
	public ProtectInfoEntity findProtectInfoEntity(@Param("serialNum")String serialNum);
	
	
}
