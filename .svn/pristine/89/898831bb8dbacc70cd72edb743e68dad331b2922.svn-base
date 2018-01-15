package com.mmec.centerService.userModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;

public interface PlatformDao   extends JpaRepository<PlatformEntity,Integer> {
	//根据AppId查找
	@Query("SELECT s FROM PlatformEntity s WHERE s.appId =:appId")
	public PlatformEntity findPlatformByAppId(@Param("appId")String appId);
	
    //平台绑定企业用户
	@Modifying
	@Query(value="update PlatformEntity set CIdentity = :CIdentity where id = :id")
	public int updateIdentityParentId(@Param("CIdentity")IdentityEntity CIdentity, @Param("id")int uuid);
	
}
