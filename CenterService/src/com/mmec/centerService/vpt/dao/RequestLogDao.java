package com.mmec.centerService.vpt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.vpt.entity.RequestLogBean;
import com.mmec.centerService.vpt.entity.RequestTimesBean;

public interface RequestLogDao  extends JpaRepository<RequestLogBean,Integer> 
{
	public List<RequestLogBean> findAll();
	
	//查询APP小时访问次数
	@Query(value="select case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times from RequestLogBean where ip = :ip and year = :year and month = :month and day = :day and hour = :hour ")
	public int queryIpRequestTimesByHour(@Param("ip")String ip,@Param("year")int year,@Param("month")int month,@Param("day")int day,@Param("hour")int hour);
	//查询APP日访问次数
	@Query(value="select case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times from RequestLogBean where ip = :ip and year = :year and month = :month and day = :day ")
	public int queryIpRequestTimesByDay(@Param("ip")String ip,@Param("year")int year,@Param("month")int month,@Param("day")int day);
	//查询APP小时访问次数
	@Query(value="select case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times from RequestLogBean where appId = :appId and year = :year and month = :month and day = :day and hour = :hour ")
	public int queryAppRequestTimesByHour(@Param("appId")String appId,@Param("year")int year,@Param("month")int month,@Param("day")int day,@Param("hour")int hour);
	//查询APP日访问次数
	@Query(value="select case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times from RequestLogBean where appId = :appId and year = :year and month = :month and day = :day ")
	public int queryAppRequestTimesByDay(@Param("appId")String appId,@Param("year")int year,@Param("month")int month,@Param("day")int day);
	//查询User小时访问次数
	@Query(value="select case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times from RequestLogBean where userInfo = :userInfo and year = :year and month = :month and day = :day and hour = :hour ")
	public int queryUserRequestTimesByHour(@Param("userInfo")String userInfo,@Param("year")int year,@Param("month")int month,@Param("day")int day,@Param("hour")int hour);
	//查询User日访问次数
	@Query(value="select case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times from RequestLogBean where userInfo = :userInfo and year = :year and month = :month and day = :day ")
	public int queryUserRequestTimesByDay(@Param("userInfo")String userInfo,@Param("year")int year,@Param("month")int month,@Param("day")int day);
	
	//查询数据是否存在
	public RequestLogBean findByIpAndAppIdAndUserInfoAndYearAndMonthAndDayAndHour(String ip,String appId,String userInfo,int year,int month,int day,int hour);

		
	//增加访问次数
	@Modifying
	@Query(value="update RequestLogBean set requestTimes = requestTimes+1  where ip = :ip and  appId = :appId and userInfo = :userInfo and year = :year and month = :month and day = :day and hour = :hour")
	public int updateRequestLogTimes(@Param("ip")String ip,@Param("appId")String appId,@Param("userInfo")String userInfo,@Param("year")int year,@Param("month")int month,@Param("day")int day,@Param("hour")int hour);

	@Modifying
	@Query(value="update RequestLogBean set requestTimes = :requestTimes  where id = :id")
	public int updateRequestLogTimes(@Param("id")int id,@Param("requestTimes")int requestTimes);
	
	
	//初始化时 查询当前小时的访问数据
	//查询APP小时访问次数
	@Query(value="select new com.mmec.centerService.vpt.entity.RequestTimesBean('IP',ip, case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times) from RequestLogBean where year = :year and month = :month and day = :day and hour = :hour group by ip")
	public List<RequestTimesBean> queryRequestTimesByHourGroupByIp(@Param("year")int year,@Param("month")int month,@Param("day")int day,@Param("hour")int hour);
	//查询APP日访问次数
	@Query(value="select new com.mmec.centerService.vpt.entity.RequestTimesBean('IP',ip, case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times) from RequestLogBean where year = :year and month = :month and day = :day group by ip")
	public List<RequestTimesBean> queryRequestTimesByDayGroupByIp(@Param("year")int year,@Param("month")int month,@Param("day")int day);
	//查询APP小时访问次数
	@Query(value="select new com.mmec.centerService.vpt.entity.RequestTimesBean('APPIP',appId, case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times) from RequestLogBean where year = :year and month = :month and day = :day and hour = :hour group by appId")
	public List<RequestTimesBean> queryRequestTimesByHourGroupByAppId(@Param("year")int year,@Param("month")int month,@Param("day")int day,@Param("hour")int hour);
	//查询APP日访问次数
	@Query(value="select new com.mmec.centerService.vpt.entity.RequestTimesBean('APPIP',appId, case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times) from RequestLogBean where year = :year and month = :month and day = :day group by appId")
	public List<RequestTimesBean> queryRequestTimesByDayGroupByAppId(@Param("year")int year,@Param("month")int month,@Param("day")int day);
	//查询User小时访问次数
	@Query(value="select new com.mmec.centerService.vpt.entity.RequestTimesBean('USER',userInfo, case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times) from RequestLogBean where year = :year and month = :month and day = :day and hour = :hour group by userInfo")
	public List<RequestTimesBean> queryRequestTimesByHourGroupByUserInfo(@Param("year")int year,@Param("month")int month,@Param("day")int day,@Param("hour")int hour);
	//查询User日访问次数
	@Query(value="select new com.mmec.centerService.vpt.entity.RequestTimesBean('USER',userInfo, case when sum(requestTimes) is null then 0 else sum(requestTimes) end as times) from RequestLogBean where year = :year and month = :month and day = :day group by userInfo")
	public List<RequestTimesBean> queryRequestTimesByDayGroupByUserInfo(@Param("year")int year,@Param("month")int month,@Param("day")int day);
	

}
