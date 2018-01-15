package com.mmec.centerService.feeModule.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 短信记录表
 * 
 */
@Entity
@Table(name="c_sms_record")
public class SmsRecordEntity implements Serializable {
	public SmsRecordEntity(){
		
	}
	
	public SmsRecordEntity(String mobile, String message, String optfrom,
			Date sendTime, String receiveResult) {
		super();
		this.mobile = mobile;
		this.message = message;
		this.optfrom = optfrom;
		this.sendTime = sendTime;
		this.receiveResult = receiveResult;
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="mobile")
	private String mobile;

	@Column(name="message")
	private String message;
	
	@Column(name="optfrom")
	private String optfrom;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="send_time")
	private Date sendTime;
	
	@Column(name="receive_result")
	private String receiveResult;
	
	public String getReceiveResult() {
		return receiveResult;
	}

	public void setReceiveResult(String receiveResult) {
		this.receiveResult = receiveResult;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOptfrom() {
		return optfrom;
	}

	public void setOptfrom(String optfrom) {
		this.optfrom = optfrom;
	}


}