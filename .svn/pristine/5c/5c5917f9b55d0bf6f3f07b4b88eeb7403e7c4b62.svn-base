package com.mmec.centerService.userModule.entity;

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
 * The persistent class for the c_upgrade_transition database table.
 * 
 */
@Entity
@Table(name="c_upgrade_transition")
public class UpgradeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int Id;

	@Column(name="new_appid")
	private String newAppId;

	@Column(name="old_appid")
	private String oldAppId;

	@Column(name="old_appkey")
	private String oldAppkey;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upgrade_time")
	private Date upgradeTime;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getNewAppId() {
		return newAppId;
	}

	public void setNewAppId(String newAppId) {
		this.newAppId = newAppId;
	}

	public String getOldAppId() {
		return oldAppId;
	}

	public void setOldAppId(String oldAppId) {
		this.oldAppId = oldAppId;
	}

	public String getOldAppkey() {
		return oldAppkey;
	}

	public void setOldAppkey(String oldAppkey) {
		this.oldAppkey = oldAppkey;
	}

	public Date getUpgradeTime() {
		return upgradeTime;
	}

	public void setUpgradeTime(Date upgradeTime) {
		this.upgradeTime = upgradeTime;
	}

}