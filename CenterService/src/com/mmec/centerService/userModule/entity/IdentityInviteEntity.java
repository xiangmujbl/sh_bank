package com.mmec.centerService.userModule.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="c_identity_invite")
public class IdentityInviteEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="invitor_id")
	private int invitorId;

	@Column(name="invited_id")
	private int invitedId;

	private int point;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="invite_time")
	private Date inviteTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInvitorId() {
		return invitorId;
	}

	public void setInvitorId(int invitorId) {
		this.invitorId = invitorId;
	}

	public int getInvitedId() {
		return invitedId;
	}

	public void setInvitedId(int invitedId) {
		this.invitedId = invitedId;
	}

	public Date getInviteTime() {
		return inviteTime;
	}

	public void setInviteTime(Date inviteTime) {
		this.inviteTime = inviteTime;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
	
}
