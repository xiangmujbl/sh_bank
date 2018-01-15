package com.mmec.centerService.userModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.SigningRoomEntity;

public interface SigningRoomDao  extends JpaRepository<SigningRoomEntity,Integer> 
{
	    //根据邮箱 查询签约室信息 
		public SigningRoomEntity findByEmailAndStatus(String email,byte status);
	   //根据手机号码 查询签约室信息
		public SigningRoomEntity findByLinktelAndStatus(String linktel,byte status);
	    //根据用户账号 查询签约室信息
		public SigningRoomEntity findByUserIdAndStatus(int userId,byte status);
		//修改证书使用状态  0 停用  1 使用
		@Modifying
		@Query(value="update SigningRoomEntity u set u.status = 1 where u.id = :id")
		public int updateSigningRoomEntityState(@Param("id")int id);
		
}
