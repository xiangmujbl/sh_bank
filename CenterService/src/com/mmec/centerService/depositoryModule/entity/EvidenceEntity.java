package com.mmec.centerService.depositoryModule.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.itextpdf.text.zugferd.checkers.basic.DateFormatCode;


/**
 * The persistent class for the c_attachment database table.
 * 
 */
@Entity
@Table(name="c_evidence")
public class EvidenceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	/**
	 * c_identity表ID
	 */
	@Column(name="user_name")
	private String username;
	
	@Column(name="e_order")
	private String order;

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String platformUserName;
	
	public String getPlatformUserName() {
		return platformUserName;
	}

	public void setPlatformUserName(String platformUserName) {
		this.platformUserName = platformUserName;
	}


	/**
	 * c_identity表ID
	 */
	@Column(name="creator_id")
	private int userid;
	
	
	/**
	 * 存证标题
	 */
	@Column(name="title")
	private String title;
	
	
	/**
	 * 存证标题
	 */
	@Column(name="appid")
	private String appid;
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}


	/**
	 * 存证上传时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="uploadtime")
	private Date uploadtime;
	
	/**
	 * 存证更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updatetime")
	private Date updatetime;
	

	/**
	 * 流水
	 */
	@Column(name="e_serial")
	private String serial;

	/**
	 * 备注
	 */
	@Column(name="e_remark")
	private String remark;
	
	
	/**
	 * 类型
	 */
	@Column(name="e_type")
	private String type;
	
	public String uploadtimestr;
	
	public int downloadtimes;

	public int getDownloadtimes() {
		return downloadtimes;
	}

	public void setDownloadtimes(int downloadtimes) {
		this.downloadtimes = downloadtimes;
	}

	public String getUploadtimestr() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(uploadtime);
	}

	public void setUploadtimestr(String uploadtimestr) {
		this.uploadtimestr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(uploadtime);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}