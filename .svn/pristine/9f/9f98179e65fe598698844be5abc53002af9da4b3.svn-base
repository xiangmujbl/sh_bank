package com.mmec.centerService.feeModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mmec.centerService.feeModule.entity.PlatformRoleEntity;

public interface PlatformRoleDao extends JpaRepository<PlatformRoleEntity,Integer> {
	/**
	 * 根据ID查询单个PlatformRoleEntity
	 */
	@Query(value="select  a  from  PlatformRoleEntity  a  where  a.platformid=?")
	public PlatformRoleEntity queryByPlatformId(int platformid);
	
	/**
	 * 根据ID查询List
	 */
	@Query(value="select  a  from  PlatformRoleEntity  a  where  a.platformid=?")
	public List<PlatformRoleEntity> queryListByPlatformId(int platformid);
}