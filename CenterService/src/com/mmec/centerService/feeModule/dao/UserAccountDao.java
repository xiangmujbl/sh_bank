package com.mmec.centerService.feeModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.*;
import java.util.List;

import com.mmec.centerService.feeModule.entity.UserAccountEntity;

public interface UserAccountDao extends JpaRepository<UserAccountEntity,Integer> {
	/**
	 * 根据用户ID查询用户余额表
	 */
	public List<UserAccountEntity> findByUserid(int userid);
	
	/**
	 * 根据用户ID查询用户余额根据Paycode
	 */
	public UserAccountEntity findByUseridAndPaycode(int userid,String paycode);
	
	
	/**
	 * 用户金额更新
	 */
	@Modifying
	@Query(value="update UserAccountEntity set banlance = :banlance where userid = :userid and paycode = :paycode")
	public int UpdateUserAccount(@Param("banlance")BigDecimal banlance, @Param("userid")int userid,@Param("paycode")String paycode);
	
	/**
	 * 用户记录删除
	 */
	@Modifying
	@Query(value="delete from UserAccountEntity s where s.userid = ?")
	public void delUserAccount(int userid);
}
