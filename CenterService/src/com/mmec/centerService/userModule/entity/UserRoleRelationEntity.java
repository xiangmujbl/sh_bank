package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the c_user_role_relation database table.
 * 
 */
@Entity
@Table(name="c_user_role_relation")
public class UserRoleRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	//bi-directional many-to-one association to IdentityEntity
	@Column(name="user_id")
	private int userId;

	//bi-directional many-to-one association to RoleEntity
	@Column(name="role_id")
	private int roleId;

	public UserRoleRelationEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}