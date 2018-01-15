package com.mmec.centerService.userModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.PlatformApplyEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;

public interface PlatformApplyDao   extends JpaRepository<PlatformApplyEntity,Integer>{
	   //修改审批状态
		@Modifying
		@Query(value="update PlatformApplyEntity set status = :status where serialNum = :serialNum")
		public int updateApplyStatus(@Param("status")byte status, @Param("serialNum")String serialNum);
		
		//根据审批批次编码 查询用户信息
		public PlatformApplyEntity findBySerialNum(String serialNum);
		   
		//修改审批状态
		@Modifying
		@Query(value="update PlatformApplyEntity set CPlatform = :CPlatform,auditResultMark= :auditResultMark, status = :status where serialNum = :serialNum")
		public int updateApplyPlatformBySerialNum(@Param("CPlatform")PlatformEntity CPlatform,@Param("auditResultMark")String auditResultMark,@Param("status")byte status, @Param("serialNum")String serialNum);
			
}
