package com.mmec.centerService.feeModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.feeModule.entity.IdAuthConfigEntity;


public interface IdAuthConfigDao extends JpaRepository<IdAuthConfigEntity,Integer> {
	//
	@Query(value="select i from IdAuthConfigEntity i where i.appId = :appId and i.authType = :authType and i.status = :status")
	public IdAuthConfigEntity queryByAppIdAndAuthTypeAndStatus(@Param("appId")String appId,@Param("authType")int authType,@Param("status")int status);

	//扣减次数
	@Modifying
	@Query(value="update IdAuthConfigEntity set authServiceTimes = authServiceTimes - 1 where id = :id")
	public int updateAuthServiceTimes(@Param("id")int id);
	
	//扣减次数
	@Modifying
	@Query(value="update IdAuthConfigEntity set authUseTimes = authUseTimes + 1 where id = :id")
	public int updateAuthUseTimes(@Param("id")int id);
}
