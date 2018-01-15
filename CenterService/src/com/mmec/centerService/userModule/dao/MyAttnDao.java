package com.mmec.centerService.userModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.MyAttnEntity;

public interface MyAttnDao  extends JpaRepository<MyAttnEntity,Integer> {

	//根据用戶ID查詢所有联系人
	@Query(value="select i from IdentityEntity i ,MyAttnEntity ma where i.id = ma.attnId and ma.userId = :userId ")
	public List<IdentityEntity> findMyAttnByUserId(@Param("userId")int userId);

	//删除我的所有联系人
	@Modifying
	@Query(value="delete from MyAttnEntity where userId = :userId")
	public int deleteAllAttnByUserId(@Param("userId")int userId);

	//删除我的联系人
	@Modifying
	@Query(value="delete from MyAttnEntity where userId = :userId and attnId = :attnId")
	public int deleteAllAttnByUserIdAndAttnId(@Param("userId")int userId,@Param("attnId")int attnId);
	
	//查询联系人
	@Query(value="select ma from MyAttnEntity ma where ma.userId = :userId and ma.attnId = :attnId ")
	public List<MyAttnEntity> findByUserIdAndAttnId(@Param("userId")int userId,@Param("attnId")int attnId);
	
	//根据用戶ID和查询条件(企业邮箱,个人手机)查詢所有联系人
	@Query(value="select i from IdentityEntity i ,MyAttnEntity ma where i.id = ma.attnId and ma.userId = ? and ( ( i.type = 1 and i.mobile like '%'||?||'%')  or ( i.type = 2 and i.email like '%'||?||'%') )")
	public List<IdentityEntity> findMyAttnByUserIdAndParam(int userId,String param,String param2);
	
}
