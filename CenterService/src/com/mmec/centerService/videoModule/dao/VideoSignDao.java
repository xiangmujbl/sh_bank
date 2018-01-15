package com.mmec.centerService.videoModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.videoModule.entity.VideoSignEntity;

public interface VideoSignDao extends PagingAndSortingRepository<VideoSignEntity,Integer>{

	//查询有效视频编码
	public List<VideoSignEntity> findByAppIdAndPlatformUserNameAndOrderIdAndStatus(String appId,String platformUserName,String orderId,int status);
	
	//查询有效视频编码
	public List<VideoSignEntity> findByAppIdAndOrderIdAndStatus(String appId,String orderId,int status);
		
		
	//将之前视频编码都失效掉
	@Modifying
	@Query(value="update VideoSignEntity set status = :status where appId = :appId and platformUserName = :platformUserName and orderId = :orderId ")
	public int updateVideoSignStatus(@Param("appId")String appId, @Param("platformUserName")String platformUserName, @Param("orderId")String orderId, @Param("status")int status);
	
	
}
