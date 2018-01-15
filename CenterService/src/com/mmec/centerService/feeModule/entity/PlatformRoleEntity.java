package com.mmec.centerService.feeModule.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the c_pay_service database table.
 * 
 */
@Entity
@Table(name="c_platform_role")
public class PlatformRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="platform_id")
	private int platformid;

	
	@Column(name="role_id")
	private int roleid;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createtime;
	
	
	@Column(name="type")
	private int type;


	public PlatformRoleEntity() {
		// TODO Auto-generated constructor stub
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getPlatformid() {
		return platformid;
	}


	public void setPlatformid(int platformid) {
		this.platformid = platformid;
	}


	public int getRoleid() {
		return roleid;
	}


	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}


	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}
	
	
	
}