package com.mmec.centerService.userModule.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.CompanyInfoEntity;
import com.mmec.centerService.userModule.entity.CustomInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;

public interface IdentityDao  extends PagingAndSortingRepository<IdentityEntity,Integer>{
	//根据主键ID 查询用户信息
	public IdentityEntity findById(int Id);
	//根据用户编码 查询用户信息
	public IdentityEntity findByUuid(String uuid);
	//根据用户编码 查询用户信息
	
	public List<IdentityEntity> findByParentId(int parentId);
	//根据用户编码 查询用户信息
	public List<IdentityEntity> findByBindedId(int parentId);
	
	//根据用户编码 查询用户信息
	@Query(value="select i from IdentityEntity i where i.CCustomInfo = :CCustomInfo and  i.status= :status")
	public List<IdentityEntity> findByCCustomInfo(@Param("CCustomInfo")CustomInfoEntity CCustomInfo,@Param("status")byte status);
	
	//查询服务组签名用户,用户类型为9,全表只有一个此用户,建库时初始化
	@Query(value="select i from IdentityEntity i where i.type = 9 ")
	public IdentityEntity findByUserType();

	//改密
	@Modifying
	@Query(value="update IdentityEntity set password = :password where uuid = :uuid")
	public int updateIdentityPassword(@Param("password")String password, @Param("uuid")String uuid);
	
	//绑定微信号
	@Modifying
	@Query(value="update IdentityEntity set wxOpenid = :wxOpenid where id = :id")
	public int updateIdentityWxOpenid(@Param("wxOpenid")String wxOpenid, @Param("id")int id);
	
	//修改手机号码
	@Modifying
	@Query(value="update IdentityEntity set moblie = :moblie where uuid = :uuid")
	public int updateIdentityMobile(@Param("moblie")String moblie, @Param("uuid")String uuid);
	//修改用户邮箱
	@Modifying
	@Query(value="update IdentityEntity set email = :email where uuid = :uuid")
	public int updateIdentityEmail(@Param("email")String email, @Param("uuid")String uuid);
	//修改用户邮箱验证状态
	@Modifying
	@Query(value="update IdentityEntity set isEmailVerified = :isEmailVerified where uuid = :uuid")
	public int updateIdentityEmailStatus(@Param("isEmailVerified")String isEmailVerified, @Param("uuid")String uuid);
	//修改用户状态 :"0"停用；“1”可用
	@Modifying
	@Query(value="update IdentityEntity set status = :status where uuid = :uuid")
	public int updateIdentityState(@Param("status")int status, @Param("uuid")String uuid);
	//实名认证通过
	@Modifying
	@Query(value="update IdentityEntity i set i.isAuthentic =1,i.isAdmin =1 where i.uuid = :uuid")
	public int updateIdentityAuthenticPass(@Param("uuid")String uuid);
	//实名认证拒绝通过
	@Modifying
	@Query(value="update IdentityEntity i set i.isAuthentic =2,i.isAdmin =0 where i.uuid = :uuid")
	public int updateIdentityAuthenticNotPass(@Param("uuid")String uuid);
	//重置实名认证状态
	@Modifying
	@Query(value="update IdentityEntity i set i.isAuthentic =0,i.isAdmin =0 where i.uuid = :uuid")
	public int updateIdentityAuthenticWaitCheck(@Param("uuid")String uuid);
	//子账号绑定is_authentic
	@Modifying
	@Query(value="update IdentityEntity set bindedId = :bindedId where id = :id")
	public int updateIdentityBindedId(@Param("bindedId")int bindedId, @Param("id")int id);
	//企业管理员变更
	@Modifying
	@Query(value="update IdentityEntity set isAdmin = :isAdmin where uuid = :uuid")
	public int updateIdentityIsAdmin(@Param("isAdmin")int isAdmin, @Param("uuid")String uuid);
	//修改公司信息
	@Modifying
	@Query(value="update IdentityEntity set CCompanyInfo = :CCompanyInfo where uuid = :uuid")
	public int updateIdentityCompany(@Param("CCompanyInfo")CompanyInfoEntity companyInfo, @Param("uuid")String uuid);
	//修改用户资料
	@Modifying
	@Query(value="update IdentityEntity set CCustomInfo = :CCustomInfo where uuid = :uuid")
	public int updateIdentityCustomer(@Param("CCustomInfo")CustomInfoEntity customInfo, @Param("uuid")String uuid);
	
	//个人用户账号自动绑定
	@Modifying
	@Query(value="update IdentityEntity i set i.bindedId = :bindedId  where i.type = 1 and i.CCustomInfo.id = :customId and i.mobile = :mobile and i.id = :primaryKeyId")
	public int bangdingCustomAccount(@Param("bindedId")int bindedId, @Param("customId")int customId, @Param("mobile")String mobile,@Param("primaryKeyId")int primaryKeyId);

	//企业用户账号自动绑定
	@Modifying
	@Query(value="update IdentityEntity i set i.bindedId = :bindedId  where i.type = 2 and i.CCustomInfo.id = :customId and i.CCompanyInfo.id = :companyId and i.email = :email and i.id = :primaryKeyId")
	public int bangdingCompanyAccount(@Param("bindedId")int bindedId, @Param("customId")int customId, @Param("companyId")int companyId, @Param("email")String email,@Param("primaryKeyId")int primaryKeyId);
	
	
	//根据appid和平台用户名称、用户账号 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and (i.account = :account and i.platformUserName = :platformUserName)")
	public IdentityEntity queryAppIdAndPlatformUserNameAndAccount(@Param("cPlatform")PlatformEntity cPlatform,@Param("account")String account,@Param("platformUserName")String platformUserName);
	
	//根据appid和平台用户名称、用户账号 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and (i.account = :account or i.platformUserName = :platformUserName)")
	public IdentityEntity queryAppIdAndPlatformUserNameOrAccount_bak(@Param("cPlatform")PlatformEntity cPlatform,@Param("account")String account,@Param("platformUserName")String platformUserName);

	//根据appid和平台用户名称、用户账号 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and (i.account = :account or i.platformUserName = :platformUserName) and i.status= :status")
	public IdentityEntity queryAppIdAndPlatformUserNameOrAccountAndStatus_bak(@Param("cPlatform")PlatformEntity cPlatform,@Param("account")String account,@Param("platformUserName")String platformUserName,@Param("status")byte status);

	
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and  i.platformUserName = :platformUserName")
	public IdentityEntity queryAppIdAndPlatformUserName(@Param("cPlatform")PlatformEntity cPlatform,@Param("platformUserName")String platformUserName);
	
	/**
	 * 返回途牛的不是删除的注册用户
	 * @param cPlatform
	 * @param platformUserName
	 * @return
	 */
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and  i.platformUserName = :platformUserName and i.status<>1 ")
	public IdentityEntity queryAppIdAndPlatformUserNameAndStatus(@Param("cPlatform")PlatformEntity cPlatform,@Param("platformUserName")String platformUserName);
	
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and  i.platformUserName = :platformUserName and i.status= :status")
	public IdentityEntity queryAppIdAndPlatformUserNameAndStatus(@Param("cPlatform")PlatformEntity cPlatform,@Param("platformUserName")String platformUserName,@Param("status")byte status);
		
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and  i.id = :userId")
	public IdentityEntity queryAppIdAndPlatformUserId(@Param("cPlatform")PlatformEntity cPlatform,@Param("userId")int userId);
	
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and  i.account = :account")
	public IdentityEntity queryAppIdAndAccount(@Param("cPlatform")PlatformEntity cPlatform,@Param("account")String account);
	
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and  i.account = :account and i.status= :status")
	public IdentityEntity queryAppIdAndAccountAndStatus(@Param("cPlatform")PlatformEntity cPlatform,@Param("account")String account,@Param("status")byte status);
		
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.account = :account and i.password = :password")
	public IdentityEntity queryAppIdAndAccountAndPassword(@Param("cPlatform")PlatformEntity cPlatform,@Param("account")String account,@Param("password")String password);
	
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.mobile = :mobile and i.password = :password and i.type = 1")
	public IdentityEntity queryAppIdAndMobileAndPassword(@Param("cPlatform")PlatformEntity cPlatform,@Param("mobile")String mobile,@Param("password")String password);
	
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.email = :email and i.password = :password and i.type = 2")
	public IdentityEntity queryAppIdAndEmailAndPassword(@Param("cPlatform")PlatformEntity cPlatform,@Param("email")String email,@Param("password")String password);
	
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.platformUserName = :platformUserName and i.password = :password and i.type = 2")
	public IdentityEntity queryAppIdAndPlatformUserNameAndPassword(@Param("cPlatform")PlatformEntity cPlatform,@Param("platformUserName")String platformUserName,@Param("password")String password);
	
	//根据appid和邮箱  查询 邮箱是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.email = :email and i.type = :type")
	public IdentityEntity queryAppIdAndEmailAndType(@Param("cPlatform")PlatformEntity cPlatform,@Param("email")String email,@Param("type")byte type);
	
	//根据appid和邮箱  查询 邮箱是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.email = :email and i.type = :type and i.status =:status ")
	public IdentityEntity queryAppIdAndEmailAndTypeAndStatus(@Param("cPlatform")PlatformEntity cPlatform,@Param("email")String email,@Param("type")byte type,@Param("status")byte status);
	
	
	//根据appid和手机号码 查询 手机号码是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.mobile = :mobile and i.type = :type ")
	public IdentityEntity queryAppIdAndMobileAndType(@Param("cPlatform")PlatformEntity cPlatform,@Param("mobile")String mobile,@Param("type")byte type);
	
	//根据appid和手机号码 查询 手机号码是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.mobile = :mobile and i.type = :type and i.status =:status ")
	public IdentityEntity queryAppIdAndMobileAndTypeAndStatus(@Param("cPlatform")PlatformEntity cPlatform,@Param("mobile")String mobile,@Param("type")byte type,@Param("status")byte status);
	
	//查询平台用户下所以子账号信息
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.parentId = :parentId ")
	public List<IdentityEntity> queryChildAccountList(@Param("cPlatform")PlatformEntity cPlatform,@Param("parentId")int parentId);
	//查询平台用户下所以子账号信息
	@Query(value="select i from IdentityEntity i inner join i.CPlatform inner join i.CCustomInfo where i.CPlatform = :cPlatform and i.parentId = :parentId and i.email like '%:email%' and i.registTime between :beginTime and :endTime and i.CCustomInfo.userName like '%:userName%'")
	public List<IdentityEntity> queryChildAccountListByCondition(@Param("cPlatform")PlatformEntity cPlatform,@Param("parentId")int parentId,@Param("email")String email,@Param("beginTime")Date beginTime,@Param("beginTime")Date endTime,@Param("parentId")String userName);
	
	//根据个人用户 姓名和密码 查询个人待绑定用户列表
	@Query(value="select i from IdentityEntity i where i.type = 1 and i.CCustomInfo.id = :customId and i.mobile = :mobile and i.id != :bindedId")
	public List<IdentityEntity> queryIdentityListBySameCustom(@Param("customId")int customId, @Param("mobile")String mobile,@Param("bindedId")int bindedId);
	//根据企业用户 名称和永业执照号 查询企业待绑定用户列表e
	@Query(value="select i from IdentityEntity i where i.type = 2 and i.CCustomInfo.id = :customId and i.CCompanyInfo.id = :companyId and i.email = :email and i.id != :bindedId")
	public List<IdentityEntity> queryIdentityListBySameCompany(@Param("customId")int customId, @Param("companyId")int companyId, @Param("email")String email,@Param("bindedId")int bindedId);
	
	//根据custom查看
	@Query(value="select i from IdentityEntity i inner join i.CCustomInfo c where  c.id = :customId and i.type = 1 ")
	public List<IdentityEntity> queryIdentityByCCustomInfo(@Param("customId")int customId);
	//根据company查看
	@Query(value="select i from IdentityEntity i inner join i.CCompanyInfo c where  c.id = :companyId and i.type = 2 ")
	public List<IdentityEntity> queryIdentityByCCompanyInfo(@Param("companyId")int companyId);
	//根据custom查看
	@Query(value="select i from IdentityEntity i inner join i.CPlatform p inner join i.CCustomInfo c where  c.id = :customId and p.appId = :appId and i.type = 1")
	public List<IdentityEntity> queryIdentityByCCustomInfoAndAppId(@Param("customId")int customId,@Param("appId")String appId);
	//根据custom查看
	@Query(value="select i from IdentityEntity i where  i.CCompanyInfo = :companyInfo and i.CPlatform = :platformInfo and i.type = 2 and i.status = 0")
	public List<IdentityEntity> queryIdentityByCCompanyInfoAndAppId(@Param("companyInfo")CompanyInfoEntity companyInfo,@Param("platformInfo")PlatformEntity platformInfo);
	//根据company查看
	@Query(value="select i from IdentityEntity i inner join i.CPlatform p inner join i.CCompanyInfo c inner join i.CCustomInfo t where  c.id = :companyId and p.appId = :appId and t.id = :customId and i.type = 2")
	public List<IdentityEntity> queryIdentityByCCompanyInfoAndCCustomInfoAndAppId(@Param("appId")String appId,@Param("companyId")int companyId,@Param("customId")int customId);
	//根据custom查看
	@Query(value="select i from IdentityEntity i inner join i.CPlatform p inner join i.CCustomInfo c where  c.id = :customId and i.type = 1")
	public List<IdentityEntity> queryIdentityByCCustomInfoList(@Param("customId")int customId);
	//根据company查看
	@Query(value="select i from IdentityEntity i inner join i.CPlatform p inner join i.CCompanyInfo c inner join i.CCustomInfo t where  c.id = :companyId and t.id = :customId and i.type = 2")
	public List<IdentityEntity> queryIdentityByCCompanyInfoAndCCustomInfoList(@Param("companyId")int companyId,@Param("customId")int customId);
	//查询平台计费帐号
	@Query(value="SELECT i FROM IdentityEntity i WHERE i.CPlatform = :CPlatform AND i.type = 5 ")
	public IdentityEntity queryChargingIdentityByPlateformId(@Param("CPlatform")PlatformEntity CPlatform);
	
	//根据appid和平台用户名称、用户账号 查询用户是否已经存在
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.wxOpenid = :wxOpenid ")
	public IdentityEntity queryAppIdAndWxOpenId(@Param("cPlatform")PlatformEntity cPlatform,@Param("wxOpenid")String wxOpenid);
	//根据appid和手机号码 查询企业用户信息
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.mobile = :mobile and i.type = 2 and (i.email is null or i.email = '') ")
	public IdentityEntity queryAppIdAndMobileAndEmailIsNull(@Param("cPlatform")PlatformEntity cPlatform,@Param("mobile")String mobile);
	//根据appid和邮箱查询个人用户信息
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where i.CPlatform = :cPlatform and i.email = :email and i.type = 1 and (i.mobile is null or i.mobile = '') ")
	public IdentityEntity queryAppIdAndEmailAndMobileIsNull(@Param("cPlatform")PlatformEntity cPlatform,@Param("email")String email);
	//查询binded_id
	@Query(value="select i from IdentityEntity i where i.bindedId = ? ")
	public List<IdentityEntity> queryByBindedId(int bindedId);
	
	//根据company查看
	@Query(value="select i from IdentityEntity i inner join i.CPlatform where  i.CPlatform = :CPlatform and  i.businessAdmin = '1'")
	public List<IdentityEntity> listAppAdmin(@Param("CPlatform")PlatformEntity CPlatform);

	//修改用户资料
	@Modifying
	@Query(value="update IdentityEntity set businessAdmin = :businessAdmin where uuid = :uuid")
	public int updateIdentityBusinessAdmin(@Param("businessAdmin")String businessAdmin, @Param("uuid")String uuid);
	//根据手机号查询用户
	@Query(value=" select i from IdentityEntity i where i.type = 1 AND i.mobile = :mobile AND i.CPlatform = :cPlatform ")
	public List<IdentityEntity> queryUserByMobile(@Param("mobile") String mobile,@Param("cPlatform")PlatformEntity cPlatform);
	
	//根据用户编码 查询图章
	@Query(value="select i from IdentityEntity i where i.platformUserName = :platformUserName ")
	public List<IdentityEntity> queryimg(@Param("platformUserName") String platformUserName);
	
};
