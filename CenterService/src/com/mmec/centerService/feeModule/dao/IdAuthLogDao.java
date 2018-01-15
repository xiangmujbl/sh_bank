package com.mmec.centerService.feeModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmec.centerService.feeModule.entity.IdAuthLogEntity;

public interface IdAuthLogDao extends JpaRepository<IdAuthLogEntity,Integer> {
		
}
