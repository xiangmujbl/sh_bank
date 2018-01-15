package com.mmec.centerService.vpt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 阈值配置信息
 */
@Entity
@Table(name="c_vpt_config")
public class VPTConfigEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	//阈值编码
	@Column(name="vpt_code")
	private String vptCode;
	//阈值类型名称
	@Column(name="vpt_name")
	private String vptName;
	//小时操作阈值
	@Column(name="vpt_hour_times")
	private int vptHourTimes;
	//每天操作阈值
	@Column(name="vpt_day_times")
	private int vptDayTimes;
	//小时操作限制请求阈值
	@Column(name="limit_hour_times")
	private int limitHourTimes;
	//每天操作限制请求阈值
	@Column(name="limit_day_times")
	private int limitDayTimes;
	//告警联系人
	@Column(name="warning_mobbile")
	private String warningMobbile;
	//告警间隔次数
	@Column(name="warning_interval_time")
	private int warningIntervalTime;
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getVptCode()
	{
		return vptCode;
	}

	public void setVptCode(String vptCode)
	{
		this.vptCode = vptCode;
	}

	public String getVptName()
	{
		return vptName;
	}

	public void setVptName(String vptName)
	{
		this.vptName = vptName;
	}

	public int getVptHourTimes()
	{
		return vptHourTimes;
	}

	public void setVptHourTimes(int vptHourTimes)
	{
		this.vptHourTimes = vptHourTimes;
	}

	public int getVptDayTimes()
	{
		return vptDayTimes;
	}

	public void setVptDayTimes(int vptDayTimes)
	{
		this.vptDayTimes = vptDayTimes;
	}

	public int getLimitHourTimes()
	{
		return limitHourTimes;
	}

	public void setLimitHourTimes(int limitHourTimes)
	{
		this.limitHourTimes = limitHourTimes;
	}

	public int getLimitDayTimes()
	{
		return limitDayTimes;
	}

	public void setLimitDayTimes(int limitDayTimes)
	{
		this.limitDayTimes = limitDayTimes;
	}

	public String getWarningMobbile()
	{
		return warningMobbile;
	}

	public void setWarningMobbile(String warningMobbile)
	{
		this.warningMobbile = warningMobbile;
	}

	public int getWarningIntervalTime()
	{
		return warningIntervalTime;
	}

	public void setWarningIntervalTime(int warningIntervalTime)
	{
		this.warningIntervalTime = warningIntervalTime;
	}
	
	
}
