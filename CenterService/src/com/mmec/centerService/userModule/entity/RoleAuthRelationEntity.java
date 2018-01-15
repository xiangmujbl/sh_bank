package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the c_role_auth_relation database table.
 * 
 */
@Entity
@Table(name="c_role_auth_relation")
public class RoleAuthRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="auth_id")
	private String authId;

	@Column(name="role_id")
	private String roleId;
	
	@Column(name="ralation_type")
	private int ralationtype;

	public int getRalationtype() {
		return ralationtype;
	}

	public void setRalationtype(int ralationtype) {
		this.ralationtype = ralationtype;
	}

	public RoleAuthRelationEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}