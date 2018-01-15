package com.mmec.centerService.depositoryModule.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="c_evidence_user")
public class EvidenceUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/**
	 * c_identity表ID
	 */
	@Column(name="user_id")
	private int userid;
	
	/**
	 * c_evidence表ID
	 */
	@Column(name="evidence_id")
	private int evidenceid;
	
	/**
	 * 备注
	 */
	@Column(name="remark")
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getEvidence_id() {
		return evidenceid;
	}

	public void setEvidence_id(int evidence_id) {
		this.evidenceid = evidence_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}