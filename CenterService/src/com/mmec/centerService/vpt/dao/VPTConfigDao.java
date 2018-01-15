package com.mmec.centerService.vpt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmec.centerService.vpt.entity.VPTConfigEntity;

public interface VPTConfigDao  extends JpaRepository<VPTConfigEntity,Integer> 
{
	public List<VPTConfigEntity> findAll();
	
}
