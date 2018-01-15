package com.mmec.centerService.feeModule.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder.In;

import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.InvoiceInfoEntity;

import java.util.Date;


/**
 * The persistent class for the c_order_record database table.
 * 
 */
@Entity
@Table(name="c_order_record")
public class OrderRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/**
	 * 变更时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="change_time")
	private Date changeTime;
	
	/**
	 * 下单时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;
	
	/**
	 * 发票状态
	 */
	@Column(name="invoice_status")
	private int invoiceStatus;
	
	/**
	 * 后台操作管理员ID
	 */
	@Column(name="operate_id")
	private int operateId;

	/**
	 * 定单号
	 */
	@Column(name="order_id")
	private String orderId;

	/**
	 * 订单状态
	 */
	@Column(name="order_status")
	private int orderStatus;
	
	/**
	 * 订单类型
	 */
	@Column(name="order_type")
	private String orderType;

	/**
	 * 支付方式
	 */
	@Column(name="pay_method")
	private int payMethod;

	/**
	 * 第三方支付
	 */
	@Column(name="pay_way")
	private String payWay;
	
	/**
	 * 支付平台订单号
	 */
	@Column(name="payplam_order_id")
	private String payplamOrderId;
	
	/**
	 * 充值金额
	 */
	@Column(name="price")
	private int price;
	
	/**
	 * 拒绝原因
	 */
	@Column(name="refusal_reason")
	private String refusalReason;
	
	/**
	 * 充值描述
	 */
	@Column(name="remark")
	private String remark;
	
	/**
	 * 商品名称
	 */
	@Column(name="commodity")
	private String commodity;

	/**
	 * 备用字段
	 */
	@Column(name="reseve1")
	private String reseve1;
	
	/**
	 * 充值次数
	 */
	@Column(name="time")
	private int time;
	
	/**
	 * 交易类型
	 */
	@Column(name="trade_type")
	private int tradeType;
	
	/**
	 * 审核状态
	 */
	private int verified;
	
	/**
	 * 发票ID
	 */
	@Column(name="invoice_id")
	private int invoiceId;
	
	/**
	 * 资费平台ID
	 */
	@Column(name="pay_platform_id")
	private int payPlatformId;
	
	/**
	 * 用户ID
	 */
	@Column(name="user_id")
	private int userId;
	
	public void setUkeyId(Integer ukeyId) {
		this.ukeyId = ukeyId;
	}

	/**
	 * 套餐ID
	 */
	@Column(name="package_id")
	private int packageId;
	
	/**
	 * ukey ID
	 */
	@Column(name="ukey_id")
	private int ukeyId;
	
	
	public int getUkeyId() {
		return ukeyId;
	}

	public void setUkeyId(int ukeyId) {
		this.ukeyId = ukeyId;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public int getPayPlatformId() {
		return payPlatformId;
	}

	public void setPayPlatformId(int payPlatformId) {
		this.payPlatformId = payPlatformId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPackageId() {
		return packageId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	public OrderRecordEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Date getChangeTime() {
		return this.changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getInvoiceStatus() {
		return this.invoiceStatus;
	}

	public void setInvoiceStatus(int invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public int getOperateId() {
		return this.operateId;
	}

	public void setOperateId(int operateId) {
		this.operateId = operateId;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getPayMethod() {
		return this.payMethod;
	}

	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayWay() {
		return this.payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getPayplamOrderId() {
		return this.payplamOrderId;
	}

	public void setPayplamOrderId(String payplamOrderId) {
		this.payplamOrderId = payplamOrderId;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getRefusalReason() {
		return this.refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReseve1() {
		return this.reseve1;
	}

	public void setReseve1(String reseve1) {
		this.reseve1 = reseve1;
	}

	public int getTime() {
		return this.time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTradeType() {
		return this.tradeType;
	}

	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}

	public int getVerified() {
		return this.verified;
	}

	public void setVerified(int verified) {
		this.verified = verified;
	}
	
	public String getCommodity() {
		return commodity;
	}

	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
}