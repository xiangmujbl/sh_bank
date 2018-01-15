package com.mmec.centerService.userModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.IdentityInviteEntity;

public interface IdentityInviteInfoDao extends PagingAndSortingRepository<IdentityInviteEntity,Integer>{
	
	//查询邀约人邀约记录
	public List<IdentityInviteEntity> findByInvitorId(int invitorId);
	
	//会员积分值
	@Query(value="select sum(point) as point from IdentityInviteEntity where invitor_id =:invitorId")
	public long queryTotalNumberByInvitorId(@Param("invitorId")int invitorId);
}
