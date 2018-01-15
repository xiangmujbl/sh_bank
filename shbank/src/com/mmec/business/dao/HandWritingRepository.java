package com.mmec.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.business.bean.HandWritingBean;

public interface HandWritingRepository extends PagingAndSortingRepository<HandWritingBean,Integer> {

	@Query("select c from HandWritingBean c where c.appId =? and c.orderId =? and c.userId =? order by id desc")
	public List queryHandWriting(String appId,String orderId,String userId );
	
	@Transactional
	@Modifying
	@Query("delete  from HandWritingBean c where c.appId =? and c.orderId =? and c.userId =?")
	public void delHandWriting(String appId,String orderId,String userId);
}
