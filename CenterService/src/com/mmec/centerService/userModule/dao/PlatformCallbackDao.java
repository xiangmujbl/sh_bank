package com.mmec.centerService.userModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.PlatformCallbackEntity;

public interface PlatformCallbackDao  extends JpaRepository<PlatformCallbackEntity,Integer> {
	    //查询平台用户下所以回调信息
		@Query(value="select p from PlatformCallbackEntity p where p.appId = :appId ")
		public List<PlatformCallbackEntity> queryAppPlatformCallbackList(@Param("appId")String appId);

}
