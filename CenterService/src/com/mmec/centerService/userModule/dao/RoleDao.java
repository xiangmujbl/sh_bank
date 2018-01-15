package com.mmec.centerService.userModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmec.centerService.userModule.entity.RoleEntity;

public interface RoleDao  extends JpaRepository<RoleEntity,Integer> {
	public RoleEntity findByRoleName(String roleName);
	public RoleEntity findById(int id);
}
