package com.mmec.centerService.feeModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.feeModule.entity.UserServiceEntity;

public interface UserServiceDao extends JpaRepository<UserServiceEntity,Integer> {
	
	/**
	 * 根据用户ID查询记录
	 */
	public List<UserServiceEntity> findByUserId(int userId);
	
	/**
	 * 根据用户ID和用户服务ID查询记录
	 */
	public UserServiceEntity findByUserIdAndPayCode(int userId,String payCode);
	
	/**
	 * 根据code查找list
	 */
	public List<UserServiceEntity> findByPayCode(String payCode);
	
	/**
	 * 更新次数
	 */
	@Modifying
    @Query("UPDATE UserServiceEntity d SET d.chargingTimes=? where  d.userId=? and d.payCode=?")
	public int updateUserServiceTimes(int chargingTimes,int userId,String payCode);
	
}
