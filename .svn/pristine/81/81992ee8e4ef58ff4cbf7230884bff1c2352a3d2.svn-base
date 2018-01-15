package com.mmec.centerService.contractModule.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import com.mmec.centerService.feeModule.entity.ContractDeductRecordEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the c_contract database table.
 * 
 */
@Entity
@Table(name="c_contract")
public class ContractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="contract_type")
	private String contractType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;

	private int creator;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deadline;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_time")
	private Date endTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date finishtime;

	private String keyword;

	private String mark;
	
	@Column(name="temp_number")
	private String tempNumber;

	private String operator;
	
	/**
	 * payment_type 付款类型
	 * 0,代付 1,自付
	 */
	@Column(name="payment_type")
	private byte paymentType;

	private String pname;
	
	@Column(name="price")
	private BigDecimal price;
	
	@Column(name="sha1")
	private String sha1;
	@Column(name="otheruids")
	private String otheruids;

	@Column(name="serial_num")
	private String serialNum;

	@Column(name="sign_plaintext")
	private String signPlaintext;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time")
	private Date startTime;

	/**
	 * 0:未签署
	 * 1:一人签署 
	 * 2:全部签署成功 
	 * 3:签署拒绝 
	 * 4:合同撤销 
	 * 5:合同关闭
	 */
	private byte status;

	private String title;

	private String type;
	
	@Column(name="isPdfSign")
	private String isPdfSign;
	
	@Column(name="orderid")
	private String orderId;

	@Column(name="opt_from")
	private byte optFrom;

	@Column(name="turn_img_status")
	private int turnImgStatus ;
	
	//bi-directional many-to-one association to PlatformEntity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="platform_id")
	private PlatformEntity CPlatform;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;
	
	@Column(name="parent_contract_id")
	private int parentContractId;	

	/*
	 * 云签用于删除合同,0未删除,1已删除,默认为0
	 */
	@Column(name="isdelete")
	private byte isDelete;
	
	/**
	 * 创建合同后是否可见,0表示可见，1表示不可见，默认为0,
	 */
	@Column(name="is_show")
	private byte isShow;
	
	/**
	 * 视频签署状态 空或者0 未签 1已签
	 */
	@Column(name="video_flag")
	private String videoFlag;
	
	
	public byte getIsShow() {
		return isShow;
	}

	public void setIsShow(byte isShow) {
		this.isShow = isShow;
	}

	public byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(byte isDelete) {
		this.isDelete = isDelete;
	}

	public int getParentContractId() {
		return parentContractId;
	}
	
	public String getTempNumber() {
		return tempNumber;
	}

	public void setTempNumber(String tempNumber) {
		this.tempNumber = tempNumber;
	}
	
	public byte getOptFrom() {
		return optFrom;
	}

	public void setOptFrom(byte optFrom) {
		this.optFrom = optFrom;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public void setParentContractId(int parentContractId) {
		this.parentContractId = parentContractId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public ContractEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContractType() {
		return this.contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getCreator() {
		return this.creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getFinishtime() {
		return this.finishtime;
	}

	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getMark() {
		return this.mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public byte getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(byte paymentType) {
		this.paymentType = paymentType;
	}

	public String getPname() {
		return this.pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}



	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public String getOtheruids() {
		return this.otheruids;
	}

	public void setOtheruids(String otheruids) {
		this.otheruids = otheruids;
	}

	public String getSerialNum() {
		return this.serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getSignPlaintext() {
		return this.signPlaintext;
	}

	public void setSignPlaintext(String signPlaintext) {
		this.signPlaintext = signPlaintext;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PlatformEntity getCPlatform() {
		return this.CPlatform;
	}

	public void setCPlatform(PlatformEntity CPlatform) {
		this.CPlatform = CPlatform;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getIsPdfSign() {
		return isPdfSign;
	}

	public void setIsPdfSign(String isPdfSign) {
		this.isPdfSign = isPdfSign;
	}

	public int getTurnImgStatus()
	{
		return turnImgStatus;
	}

	public void setTurnImgStatus(int turnImgStatus)
	{
		this.turnImgStatus = turnImgStatus;
	}

	public String getVideoFlag()
	{
		return videoFlag;
	}

	public void setVideoFlag(String videoFlag)
	{
		this.videoFlag = videoFlag;
	}
	
	
}