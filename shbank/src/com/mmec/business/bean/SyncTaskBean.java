package com.mmec.business.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 *
 */
@Entity
@Table(name = "m_sync_task")
public class SyncTaskBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "platform_id")
	private String platformId;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "url")
	private String url;

	@Column(name = "callback_name")
	private String callbackName;

	@Column(name = "sync_num")
	private int syncNum;

	@Column(name = "info")
	private String info;

	@Column(name = "sync_time")
	private String syncTime;

	@Column(name = "status")
	private int status;

	@Column(name = "time")
	private String time;
	
	@Column(name = "hash")
	private String hash;

	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCallbackName() {
		return callbackName;
	}

	public void setCallbackName(String callbackName) {
		this.callbackName = callbackName;
	}

	public int getSyncNum() {
		return syncNum;
	}

	public void setSyncNum(int syncNum) {
		this.syncNum = syncNum;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(String syncTime) {
		this.syncTime = syncTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "SyncTaskBean [id=" + id + ", orderId=" + orderId + ", time=" + time + ", syncTime=" + syncTime
				+ ", status=" + status + ", callbackName=" + callbackName + ", platformId=" + platformId + ", syncNum="
				+ syncNum + ", url=" + url + ", info=" + info + "]";
	}
}
