package com.mmec.centerService.userModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmec.centerService.userModule.entity.OptLogEntity;

public interface OptLogDao  extends JpaRepository<OptLogEntity,Integer> {
	
}
