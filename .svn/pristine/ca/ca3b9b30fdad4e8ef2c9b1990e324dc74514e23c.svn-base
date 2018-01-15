package com.mmec.centerService.userModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.CustomInfoEntity;

public interface CustomInfoDao extends JpaRepository<CustomInfoEntity,Integer> {
		//根据身份证号查询
		public List<CustomInfoEntity> findByIdentityCardAndCardType(String identityCard,String cardType);	
		
		
		//修改企业名称
		@Modifying
		@Query(value="update CustomInfoEntity set userName = :userName where id = :id")
		public int updateCompanyInfoEntityUserName(@Param("userName")String userName, @Param("id")int id);
		
		//修改工商执照号
		@Modifying
		@Query(value="update CustomInfoEntity set identityCard = :identityCard where id = :id")
		public int updateCompanyInfoEntityIdentityCard(@Param("identityCard")String identityCard, @Param("id")int id);
		
		
}
