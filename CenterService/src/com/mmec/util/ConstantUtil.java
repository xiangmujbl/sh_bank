/*
 * 常量表
 */
package com.mmec.util;

import java.math.BigDecimal;

public interface ConstantUtil {
	
	public final static String MAX_MONEY="999999999.99";
	
	public final static BigDecimal MAX_MONEY_DECIMAL=new BigDecimal(MAX_MONEY);
	
	public final static BigDecimal ZERO_MONEY=new BigDecimal("0");
	
	public final static String QUERY="query";
	
	public final static String ADD="add";
	
	public final static String UPDATE="update";
	
	public final static String DELETE="delete";

	public final static String DEFAULT_YUNSIGN_APP_ID="yunsign";
	
	String[] RETURN_SUCC = {"0000","返回成功","success"};
	
	String[] MAP_PARAMETER = {"0001","map参数为空","map parameter empty"};
	
	String[] OPTFROM_PARAMETER = {"0002","optFrom不能为空","optFrom is empty"};
	
	String[] RETURN_FAIL_PARAMERROR = {"9900", "参数错误", "parameter error："};

	String[] RETURN_RESULT_EMPTY = {"9901","查询结果为空","query empty !"};

	String[] RETURN_DB_ERROR = {"9902","数据库异常","DB exception."};
	
	String[] RETURN_PAGE_ERROR = {"9903","分页查询标识错误","pageFlag is null."};
	
	String[] RETURN_PAGEINFO_ERROR = {"9904","当前页或每页大小信息未给全","current or pagesize is null."};

	String[] QUERY_TYPE_ERROR ={"9905","查询类型错误","query type error"};
	
	String[] RETURN_NORIGHT_ERROR = {"9900","接口权限不足,请联系系统管理员","Permission denied."};
	
	String[] RETURN_REPEAT_REGISTER = {"9910","重复注册","repeat register."};
	
	String[] RETURN_SYSTEM_ERROR = {"9999","系统错误","system error."};
	
	String[] ITEXT_SIGN_PDF_IOERROR = {"9997","PDF签署IO错误","pdf sign error."};
	
	String[] ITEXT_SIGN_PDFERROR = {"9998","PDF签署错误","pdf sign error."};
	
	String[] RETURN_VPT_MAX = {"9800","访问次数超出,限制访问","request too more,limit request."};
	
	String[] RETURN_USER_NOTEXIST = {"1001","用户不存在","user is not exist."};
	
	String[] RETURN_USER_EXIST = {"1002","用户已存在","user is already existed! "};
	
	String[] RETURN_PLAT_NOT_EXIST = {"1003","第三方平台不存在","platform is not exist."};
	
	String[] RETURN_PLAT_EXIST = {"1004","第三方平台已存在","platform  is already existed! "};
	
	String[] RETURN_COMP_NOT_EXIST  = {"1005","公司不存在","company is not exist."};
	
	String[] RETURN_COMP_EXIST = {"1006","公司已存在","company is already existed! "};
	
	String[] RETURN_CERT_NOT_EXIST = {"1007","证书不存在","activated certificate is not exist."};
	
	String[] RETURN_CERT_EXIST = {"1008","证书已存在", "activated certificate is already existed!"};
	
	String[] RETURN_APP_NOT_EXIST = {"1009","对接系统不存在","appId is not exist."};
	
	String[] RETURN_SEAL_NOT_EXIST = {"1010","图章不存在","seal is not exist. "};
	
	String[] RETURN_SEAL_EXIST = {"1011","图章已存在","seal is already exist.  "};
	
	String[] RETURN_USER_PLAT_NOT_MATCH = {"1012","用户与第三方平台ID不一致","user is not match the platform."};
	
	String[] RETURN_CUST_NOT_EXIST = {"1013","用户资料不存在","custom is not exist."};
	
	String[] RETURN_USER_ACCOUNT_PLATUSERID_EXIST = {"1014","用户账号或平台用户ID已存在","user account or platformUserId is exist."};
	
	String[] RETURN_YUNSIGN_USER_LOGIN_FAILED = {"1015","登录失败，账号或密码错误","login failed, account or password not correct."};
	
	String[] RETURN_CERT_LOGIN_FAILED = {"1016","登录失败，证书错误","login failed, account or password not correct."};
	
	String[] RETURN_MMEC_LOGIN_FAILED = {"1017","登录失败，平台用户或密码错误","login failed, platformUserName or password not correct."};
	
	String[] RETURN_CUST_EXIST = {"1018","用户资料已存在","custom is already exist.   "};
	
	String[] RETURN_USER_COMPANY_NOT_MATCH = {"1019","用户与公司不一致","user is not match the company."};
	
	String[] RETURN_USER_ACCOUNT_PLATUSERID_NOT_EXIST = {"1020","用户账号或平台用户ID不存在","user account or platformUserId is not exist."};

	String[] RETURN_YUNSIGN_COMPANY_EXIST = {"1021","云签公司账号已经存在","yunsign company is exist."};

	String[] RETURN_YUNSIGN_CUSTOM_EXIST = {"1022","云签个人账户已经存在","yunsign custom is exist."};
	
	String[] RETURN_PLAT_APPLY_NOT_EXIST = {"1023","平台申请记录不存在","platform apply is not exist."};
	
	String[] RETURN_PLAT_APPLY_EXIST = {"1024","平台申请审批编号已存在","platform apply serial number is exist."};
	
	String[] RETURN_PLAT_APPLY_RECORD_NOT_EXIST = {"1025","缔约室申请记录不存在","platform apply record is not exist."};
	
	String[] RETURN_YUNSIGN_CHILD_NOT_MATCH = {"1026","子账号与主账号不同意","child account is not match."};
	
	String[] RETURN_USERNAME_IDNUM_NOT_MATCH = {"1027","证件号码已经录入,姓名与系统中的有冲突","ID number is existed,userName is not match."};
	
	String[] RETURN_COMPANYNAME_BUSNUM_NOT_MATCH = {"1028","营业执照号已经录入,公司名与系统中的有冲突","child account is not match."};
	
	String[] RETURN_COMPANYNAME_CERTSUBJECT_NOT_MATCH = {"1029","证书使用者与待绑定账号公司名不一致","certSubject is not match companyName."};
	
	String[] RETURN_USERNAME_CERTSUBJECT_NOT_MATCH = {"1030","证书使用者与待绑定账号用户姓名不一致","certSubject is not match userName."};
	
	String[] RETURN_MESSAGE_TEMPLETE_IS_NULL = {"1031","待发送信息模板不存在","message templete is null."};
	
	String[] RETURN_ATTN_NOT_EXIST = {"1032","联系人不存在","ATTN is not exist."};

	String[] RETURN_ATTN_BEEN_EXIST = {"1033","此联系人已经存在","ATTN is exist."};
	
	String[] RETURN_APP_ADMIN_EXIST_MORE = {"1034","数据异常:该平台下有多个平台账号,请联系我们","Data error.Platform has more admin."};
	
	String[] RETURN_COMPLETE_COMPANYNAME_ERROR = {"1035","企业名称已存在，不能补全","companyName exist,can't complete."};
	
	String[] RETURN_COMPLETE_BUSINESLICEN_ERROR = {"1037","工商执照号已存在，不能补全","businessLicenseNo exist,can't complete."};
	
	String[] RETURN_COMPLETE_USERNAME_ERROR = {"1038","姓名已存在，不能补全","userName exist,can't complete."};
	
	String[] RETURN_COMPLETE_IDENTITYCARD_ERROR = {"1039","证件已存在，不能补全","identityCard exist,can't complete."};

	String[] RETURN_COMPLETE_MOBILE_ERROR = {"1040","手机已存在，不能补全","mobile exist,can't complete."};

	String[] RETURN_COMPLETE_EMAIL_ERROR = {"1036","邮箱已存在，不能补全","email exist,can't complete."};

	String[] RETURN_USER_TYPE_ERROR = {"1037","用户类型错误","user type error."};
	
	String[] RETURN_MD5_FAIL = {"2001","验证MD5值失败.","check MD5 failed!"};
	
	String[] RETURN_USER_NOT_ACTV = {"2002","用户未激活","user is not activated."};
	
	String[] RETURN_USER_LOGOUT = {"2003", "用户已停用", "user is logout."};
	
	String[] RETURN_ACNT_NOT_EXIST = {"2004","用户账号不存在","user account is not exist."};
	
	String[] RETURN_LOGIN_ERROT = {"2005", "用户账号或密码错误","user account or password is not correct!"};
	
	String[] RETURN_EMAIL_ERROR = {"2006","邮箱格式错误","email is not correct!"};
	
	String[] RETURN_EMAIL_EXIST = {"2007","邮箱已使用","email is already exist. "};
	
	String[] RETURN_MOBL_ERROR = {"2008","手机号码格式错误","mobile is not correct!"};
	
	String[] RETURN_MOBL_EXIST = {"2009","手机号码已使用","mobile is already exist."};
	
	String[] RETURN_ORDER_NOT_EXIST = {"3001","订单号不存在","orderNum is not exist."};
	
	String[] RETURN_ORDER_EXIST = {"3002","订单号已存在","orderNum is already existed!"};
	
	String[] RETURN_AMNT_NOT_ENOUGH = {"3003","余额不足,请充值","insufficient account balance!"};
	
	String[] CHANGE_ORDERSTATUS_NOTALLOWED = {"3004","不允许变更订单状态","OrderStatus change is not allowed.!"};
	
	String[] RETURN_PAY_CODE_NOT_EXIST={"3005","编码对应的服务不存在","service of the code is not exist"};
	
	String[] PAYCODE_ALREADY_EXISTS={"3006","服务对应的编码已存在","ILLEAGLE PAYCODE"};
	
	String[] RETURN_TIMES_NOT_ENOUGH = {"3007","剩余次数不足,请购买服务次数","insufficient consume times!"};
	
	String[] RETURN_MONEY_NOT_LEGAL={"3008","充值的金额不合法","ILLEAGLE MONEY"};
	
	String[] SAVE_USER_EXCEPTION={"3009","保存一条用户出异常","save a new Record of userService exception"};
	
	String[] ROLE_NOT_EXISTS={"3010","角色不存在","platrole is not exists"};
	
	String[] AUTH_IS_NULL={"3011","配置权限数据为空","authlist is null"};
	
	String[] FEE_DEDUCTTION_FAILURE={"4000","扣费失败","fee deduction failure"};
	
	String[] CONTRACT_IS_NOT_EXISTS={"4001","合同不存在","contract is not exists"};
	
	String[] CONTRACT_HASNOT_ALLSIGNED={"4002","合同未签署完毕","contract is not signed yet"};
	
	String[] CONTRACT_HASBEEN_REFUSED={"4003","合同被签署人拒绝","contract is invalid because someone has refused to sign"};
	
	String[] CONTRACT_HASBEEN_CANCELED={"4004","合同被发起人撤销","contract is invalid because it has been canceled by the creator"};
	
	String[] CONTRACT_IS_OUTOFDATE={"4005","合同已经过期","contract is invalid because it has been canceled by the creator"};
	
	String[] CONTRACT_HASBEEN_ALLSIGNED={"4006","合同已经签署完毕","contract has been all signed"};
	
	String[] CONTRACT_SOLICITATION_TIME_EXPIRED={"4007","合同邀约时间过期_请矫正客户端PC时间","contract has been all signed"};
	
	String[] RETURN_CUSTOMID_IS_NULL = {"4008","缔约方为空","customid  is null."};	
	
	String[] UCID_IS_NULL={"4009","ucid为空","ucid  is null"};
	
	String[] ORDERID_IS_NULL={"4010","订单号为空","orderId  is null"};

	String[] OFFERTIME_IS_NULL={"4011","合同过期时间为空","offerTime is null"};
	
	String[] TEMPERNUM_IS_NULL={"4012","模板编号为空","tempNumber is null"};
	
	String[] TEMPERDATA_IS_NULL={"4013","模板数据为空","tempData is null"};
	
	String[] TEMPER_NOT_EXIST={"4014","模板不存在","tempplate does not exist"};
	
	String[] RETURN_CUSTOMID_HAS_DULP = {"4015","缔约方有重复值","customid has duplicate values."};
	
	String[] RETURN_CUSTOMID_IF_CONTAIN_UCID = {"4016","customid不包含ucid","customid does not contain ucid."};
	
	String[] OFFTIME_IS_ILLEGAL = {"4017","合同过期时间不合法","offtime is illegal."};
	
	String[] STARTTIME_IS_ILLEGAL = {"4018","合同开始时间不合法","starttime is illegal."};
	
	String[] ENDTIME_IS_ILLEGAL = {"4019","合同结束时间不合法","endtime is illegal."};
	
	String[] ORDERID_HAS_EXIST = {"4020","订单号已存在，不能重复创建合同","orderid has exist."};
	
	String[] CREATECONTRACT_EXCEPTION = {"4021","创建合同异常","create contract exception."};
	
	String[] CREATECONTRACT_FAIL = {"4022","创建创建初始化签署表失败","Failed to create an initial signature table."};	
	
	String[] TEMPLATE_DATA_LOAD_ERROR = {"4023","模板数据装填失败","template data load failure."};
	
	String[] TEMPLATE_DATA_ERROR = {"4024","模板数据格式不正确","template data format is not correct."};
	
	String[] DATA_SAVE_EXCEPTION = {"4023","数据保存失败","data save failure."};
	
	String[] DATA_QUERY_EXCEPTION = {"4023","数据查询异常","data query exception."};
	
	String[] CONTRACT_HASBEEN_CLOSED = {"4024","合同已关闭","contract has close."};
	
	String[] CONTRACT_SERIALNUM_IS_NULL = {"4025","合同编号为空","contract serialnum is null."};
	
	String[] MODIFY_CONTRACT_STATUS = {"4026","修改合同状态失败","modify contract status failure."};
	
	String[] CREATECONTRACT_ATTR_FAIL = {"4027","创建创建初始化附件表失败","Failed to create an initial attachment table."};

	String[] CREATECONTRACT_ATTR_IS_NULL = {"4028","合同附件表为空","Attachment table is null."};
	
	String[] DELETE_CONTRACT_STATUS = {"4026","删除合同失败","delete contract failure."};
	
	String[] OFFTIME_GREATER_CURRENTTIME = {"4027","合同已过期","The contract has expired."};

	String[] REVOKE_REFUSE={"4028","合同不能撤销或拒绝","contract can not revoke or refuse."};

	String[] CONTRACT_CHARGE_TYPE_IS_NULL={"4029","扣费方式不能空","deduction fee can not be empty"};
	
	String[] USER_ISNOT_SIGNATORY={"4200","该用户不是签署人","the user is not a signatory."};
	
	String[] USER_HAS_SIGNED={"4201","该用户已经签署","the user has signed."};
	
	String[] SIGN_PARAM_SIGNTYPE={"4202","签署类型不对","signType error."};

	String[] SIGN_PARAM_SIGNMODE={"4203","签署模式不对","signMode error."};
	
	String[] SIGN_SERVER_EXCEPTION={"4204","调用服务签署异常","sign server exception."};
	
	String[] SIGN_SERVER_ERROR={"4205","调用服务签署失败","sign server error."};
	
	String[] TIMESTAMP_EXCEPTION={"4206","调用时间戳服务异常","timestamp exception."};
	
	String[] TIMESTAMP_ERROR={"4207","获取时间戳失败","timestamp error."};
	
	String[] VERIFY_EXCEPTION={"4208","验证上一次签名异常","verifying last time signature exception."};
	
	String[] VERIFY_ERROR={"4209","验证签名失败","verifying last time signature error."};
	
	String[] QUERY_DATA_EXCEPTION={"4210","查询数据异常","query data exception."};
	
	String[] JSON_SYNTAX_EXCEPTION ={"4211","json转化异常","json transform exception."};
	
	String[] SERVER_SIGN_ERROR ={"4212","服务签名失败","service signature failure."};
	
	String[] ORIGINAL_NOT_EXIST ={"4213","合同原文不存在","original does not exist."};
	
	String[] SIGN_FAILURE ={"4214","签署失败","signature failure."};
	
	String[] COMPOSE＿IMAGE_FAILURE ={"4215","图片转换失败","compose image failure."};
	
	String[] SERVER_PDF_SIGN ={"4216","pdf签署异常","pdf sign exception."};
	
	String[] EVENT_ZIP_SIGN ={"4217","事件证书ZIP包签署异常","event cert zip sign exception."};
	
	
	String[] SIGNATRUE_NOT_EXIST ={"4218","签名域不存在","Signature domain does not exist."};
	
	String[] CONTRACT_HAS_SIGNED ={"4219","合同已签署完成","The contract has been signed and completed."};
	
	String[] CONTRACT_HAS_REVOKE ={"4220","合同已撤销","The contract has been revoked."};
	
	String[] CONTRACT_HAS_REFUSE ={"4221","合同已拒绝","The contract has been refused."};
	
	String[] CONTRACT_HAS_CLOSE ={"4222","合同已关闭","The contract has been closed."};
	
	String[] APPID_IS_NULL ={"4223","appId 为空","appId is null."};
	
	String[] PDFINFO_IS_NULL ={"4224","pdf签名坐标信息为空","pdf Signature coordinate information is empty."};
	
	String[] CONTRACT_CANNOT_AUTOSIGN ={"4225","合同状态不对,不能代签了","The contract can not auto sign."};
	
	String[] CANNOT_AUTOSIGN ={"4226","非合同创建人,不能代签了","The contract can not auto sign,because singer is not creater."};
	
	String[] WATERMARK_SIGN ={"4227","水印签署异常","watermark sign exception."};

	String[] SAVE_MESSAGE_EXCEPTION ={"4228","保存短信失败","save message exception."};
	
	String[] REFUND_EXCEPTION ={"4230","退费异常","refund exception."};
	
	String[] RETURN_REPEAT_SUBMIT ={"4231","重复创建合同","repeat created contract."};
	
	String[] DATA_ENCRYPTION_EXCEPTION ={"4232","数据加密异常","data encryption exception."};
	
	String[] FILE_READ_EXCEPTION ={"4233","文件读取关闭异常","file read off exception."};
	
	String[] QUERY_CHARGE_EXCEPTION ={"4229","查询计费帐号异常","query billing account exception."};
	
	String[] CHARGE_ACCOUNT_NOT_EXIST ={"4235","计费帐号不存在","billing account does not exist."};
	
	String[] CHARGE_PLATFORM_NOT_EXIST ={"4236","计费平台不存在","billing platform does not exist."};
	
	String[] CHARGE_TYPE_NOT_EXIST ={"4236","计费类型不存在","billing type does not exist."};
	
	String[] QUERY_EVENT_CERT_EXCEPTION ={"4237","查询事件证书计费帐号异常","query event certificate billing account exception."};
	
	String[] EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST ={"4238","事件证书计费帐号不存在","event certificate billing account does not exist."};
	
	String[] EVENT_CERT_CHARGE_EXCEPTION ={"4239","事件证书扣费异常","event certificate deduction exception."};
	
	String[] RETURN_REPEAT_SIGN ={"4240","该合同正在操作,请稍后再试","The contract is being signed, please later in the operation."};
		
	String[] ZIP_SILENT_SIGN ={"4241","zip静默签署异常","zip silent sign exception."};
	
	String[] IS_SHOW = {"4242","合同创建方没有签署,其他人不能操作该合同","the contract was not signed can not be viewed."};	
		
	String[] PROTECT_ZIP_FAILURE = {"4243","保全打包失败","Preservation Packaging Failure."};
	
	String[] CONNECT_SERVER_FAILURE = {"4244","连接FTP服务器失败","connect ftp server Failure."};
	
	String[] PFF_INFO_SPECIAL = {"4250","你所传的符号在合同里找不到","not find specialCharacter."};
	
	String[] ATTACHMENT_NUMBER = {"4247","附件个数不能超过5个","number of attachments can not be more than 5."};
	
	String[] PDF_INFO = {"4248","pdf签署信息已经存在,不能在添加","pdf info has exist."};
	
	String[] PDF_INFO_NOT_EXIST = {"4249","pdf签署信息不存在","pdf info doest not exist."};
	
	String[] CLOSE_CONTRACT_FAILURE = {"4028","关闭合同失败","close contract failure."};	
		
	String[] AUTHOR_NOT_EXIST = {"4249","被授权人不存在","author does not exist."};
	
	String[] AUTHOR_FAILURE ={"4029","授权合同没有签署完成,不能代签","authority failure."};
	
	String[] AUTHOR_HAS_EXIST = {"4230","授权已存在","author has exist."};
	
	String[] AUTHOR_OFFTIME = {"4232","授权期限不对","the authorization period is incorrect."};
	
	String[] TEMPLATE_DATA_IS_NOT_EXIST = {"4041","",""};
	
	String[] TIME_IS_ILLEGAL = {"4050","时间格式不合法","time format is not valid."};
	
	String[] ZIP_DOWN_INDEX ={"4300","读取index文件异常","read index file exception."};
	
	String[] JSONSYNTAXEXCEPTION ={"4301","json转换异常","JsonSyntaxException."};
	
	String[] READ_CN_FONT_ERROR ={"4302","读取中文字体文件异常","read cn font file error"};
	
	String[] CLOSE_PDF_STREAM ={"4303","关闭PDF签署流","close pdf stream"};
	
	String[] DEPOSITORY_NOT_EXIST ={"5001","存证不存在","depository is not exist."};
	
	String[] DEPOSITORY_FIlE_NOT_EXIST ={"5002","存证文件不存在","depository file is not exist."};
	
	String[] DEPOSITORY_IS_ALREADY_EXIST ={"5003","存证已经存在","depository is alreay exist."};
	
	String[] DEPOSITORY_USER_PERMISSION_DENIED ={"5004","操作人非存证人员","user_permission denied."};
	
	String[] HAS_PROTECT ={"5005","已经保全","has protect."};
	
	String[] PROTECT_FAILURE ={"5006","保全失败","protect failure."};
	
	String[] REPEAT_UPLOAD_EVIDENCE ={"5007","您已经提交，请勿重复提交","repeat upload in seconds is denied"};
	
	String[] PLATFORM_ADMIN_NOT_EXIST={"6001","系统管理员账号不存在,请先审核平台通过","platform_admin is not exist,please check the platform first."};
	
	String[] LOCAL_YUNSIGN_PAY_FAILED={"6002","本地化云签平台扣费失败","local yunsign pay failed"};
	
	String[] RETURN_IDAUTH_NOT_CHARGE = {"6003","平台未充值","Platform is not charge."};
	
	String[] RETURN_IDAUTH_NOT_ENOUTH_TIMES = {"6004","充值次数已用完","Platform is not enough times."};
	
	String[] RETURN_IMPORT_FAILURE = {"6005","外部数据导入失败","Import failure."};
	
	String[] EVENT_CERT_NOT_ENOUTH_TIMES ={"6006","事件证书费用为0，不足以支付","event certificate is not enough times."};
	
	/*
	 * optFrom在合同模块入库时使用int 1,对接合同 2,云签合同  5,OA的合同
	 */
	public static final String FROM_MMEC = "MMEC";
	public static final String  FROM_CERT = "CERT";
	public static final String  FROM_YUNSIGN = "YUNSIGN";
	public static final String  FROM_OA = "OA";
	
	/*
	 * 1、companyName
	 * 2、businessLicenseNo
	 * 3、mobile
	 * 4、email
	 * 5、userName
	 * 6、identityCard
	 */
	public static final String  COMPLETE_TYPE_COMPANYNAME = "companyName";
	public static final String  COMPLETE_TYPE_BUSINESLICEN = "businessLicenseNo";
	public static final String  COMPLETE_TYPE_USERNAME = "userName";
	public static final String  COMPLETE_TYPE_IDENTITYCARD = "identityCard";
	public static final String  COMPLETE_TYPE_MOBILE = "mobile";
	public static final String  COMPLETE_TYPE_EMAIL = "email";
	}
