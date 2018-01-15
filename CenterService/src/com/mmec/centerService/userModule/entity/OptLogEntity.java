package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 操作日志表
 */
@Entity
@Table(name="c_opt_log")
public class OptLogEntity  implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	//操作来源
	@Column(name="opt_from")
	private String optFrom;
	//操作平台
	@Column(name="opt_app_id")
	private String appId;
	//操作人
	@Column(name="opt_user_account")
	private String userAccount;
	//操作类型
	@Column(name="opt_type")
	private String optType;
	//操作时间
	@Column(name="opt_time")
	private Date optTime;
	//操作结果
	@Column(name="opt_result")
	private byte optResult;
	//日志级别
	@Column(name="level")
	private String level;
	//入参
	@Column(name="in_param")
	private String inParam;
	//出参
	@Column(name="out_param")
	private String outParam;
	//错误提示信息
	@Column(name="message")
	private String message;
	//错误详细信息
	@Column(name="detail")
	private String detail;
	
	//错误详细信息
	@Column(name="server_ip")
	private String serverIp;
	
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOptFrom() {
		return optFrom;
	}
	public void setOptFrom(String optFrom) {
		this.optFrom = optFrom;
	}
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public Date getOptTime() {
		return optTime;
	}
	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}
	public byte getOptResult() {
		return optResult;
	}
	public void setOptResult(byte optResult) {
		this.optResult = optResult;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getInParam() {
		return inParam;
	}
	public void setInParam(String inParam) {
		this.inParam = inParam;
	}
	public String getOutParam() {
		return outParam;
	}
	public void setOutParam(String outParam) {
		this.outParam = outParam;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
