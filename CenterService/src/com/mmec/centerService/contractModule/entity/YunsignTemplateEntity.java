package com.mmec.centerService.contractModule.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 
 * 项目名称：CenterService
 * 类名称：YunsignTemplateEntity 
 * 类描述： 云签模板
 * 创建人：yangwei
 * 创建时间：2016-4-14 上午10:00:23
 * 修改人：Administrator 
 * 修改时间：2016-4-14 上午10:00:23
 * 修改备注： 
 * @version
 */
@Entity
@Table(name="c_yunsign_template")
public class YunsignTemplateEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/*
	 * 名称
	 */
	@Column(name="name")
	private String name;
	
	/*
	 * 类别
	 */
	@Column(name="kind")
	private String kind; 
	
	/*
	 * 合同模板存放地址
	 */
	@Column(name="url")
	private String url; 
	
	/*
	 * 表单存放地址
	 */
	@Column(name="form_url")
	private String form_url; 
	
	/*
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime; 
	
	/*
	 * 开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time")
	private Date startTime; 
	
	/*
	 * 停止时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="stop_time")
	private Date stopTime; 
	
	/*
	 * 更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;
	
	/*
	 * 使用者的id
	 */
	@Column(name="user_id")
	private int userId;

	/*
	 * 模板状态,0停用,1启用
	 */
	@Column(name="status")
	private int status;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getForm_url() {
		return form_url;
	}

	public void setForm_url(String form_url) {
		this.form_url = form_url;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
