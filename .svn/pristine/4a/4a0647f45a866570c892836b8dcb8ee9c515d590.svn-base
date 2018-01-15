package com.mmec.centerService.feeModule.service;

import java.util.List;
import java.util.Map;

import org.icepdf.core.pobjects.Page;
import org.springframework.data.domain.Pageable;

import com.mmec.centerService.feeModule.entity.OrderRecordEntity;
import com.mmec.exception.ServiceException;
import com.mmec.util.OrderTradingShield;

public interface OrderRecordService{
	/**
	 * 保存一条订单信息
	 */
	public String saveOrderRecord(OrderRecordEntity o);
	
	/**
	 * 更改订单状态
	 */
	public String updateOrderRecord(OrderRecordEntity o);
	
	/**
	 * 查询订单信息不带分页
	 */
	public List<OrderRecordEntity> queryOrderByUserId(int userid);
	
	/**
	 * 查询订单信息带分页
	 */
	public List<OrderRecordEntity> queryOrderPageByUserId(int userid,Pageable page);
	
	/**
	 * 查询订单信息带paycode带分页
	 */
	public List<OrderRecordEntity> queryOrderByPayCode(int userid,String paycode,Pageable page);
	
	/**
	 * 查询订单信息带paycode的count数目
	 */
	public Long queryOrderNumByPayCode(int userid,String paycode);
	
	/**
	 * 返回订单数目
	 */
	public Long countOrderByUserId(int userid);
	
	/**
	 * 根据OrderId查询订单信息
	 */
	public OrderRecordEntity queryOrderByOrderId(String orderId);
	
	/**
	 * 查看带买卖盾的orderId信息
	 */
	public  OrderTradingShield  queryTradingShieldOrder(String orderId);
	
	
	/**
	 * for 云签
	 * @param paycode 服务码
	 * @param userid 用户表主键ID
	 * @param page 分页状况
	 * @return
	 */
	public List<OrderTradingShield> queryOrderTradingShield(String paycode,int userid,Pageable page);
}