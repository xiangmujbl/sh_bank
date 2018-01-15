package com.mmec.centerService.userModule.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.PlatformApplyRecordEntity;
//缔约室申请
public interface PlatformApplyRecordDao   extends PagingAndSortingRepository<PlatformApplyRecordEntity,Integer>{
	//分页查询（含排序功能）
	public Page<PlatformApplyRecordEntity> findAll(Pageable pageable);
	//根据主键ID 查询用户信息
	public PlatformApplyRecordEntity findById(int Id);
	//改密
	@Modifying
	@Query(value="update PlatformApplyRecordEntity set content = :content ,status = 1 where id = :id")
	public int updatePlatformApplyRecordStatus(@Param("content")String content, @Param("id")int id);
}
