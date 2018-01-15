package com.mmec.centerService.feeModule.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.mmec.centerService.userModule.entity.InvoiceInfoEntity;


public interface InvoiceInfoDao extends PagingAndSortingRepository<InvoiceInfoEntity,Integer> {

}