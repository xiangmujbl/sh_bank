package com.mmec.util;

public interface CenterErrorData {

//	String[] MAP_PARAMETER = {"0001","map参数为空","map parameter empty"};
	
//	String[] OPTFROM_PARAMETER = {"0002","optFrom不能为空","optFrom is empty"};
	
//	String[] RETURN_FAIL_PARAMERROR = {"9900", "参数错误", "parameter error："};

	String[] RETURN_RESULT_EMPTY = {"9901","查询结果为空","query empty !"};

	String[] RETURN_DB_ERROR = {"9902","数据库异常","DB exception."};
	
//	String[] RETURN_PAGE_ERROR = {"9903","分页查询标识错误","pageFlag is null."};
	
//	String[] RETURN_PAGEINFO_ERROR = {"9904","当前页或每页大小信息未给全","current or pagesize is null."};
	
	String[] RETURN_SYSTEM_ERROR = {"9999","系统错误","system error."};
	
	String[] RETURN_USER_NOTEXIST = {"1001","用户不存在","user is not exist."};
	
	String[] RETURN_USER_EXIST = {"1002","用户已存在","user is already existed! "};
	
	String[] RETURN_PLAT_NOT_EXIST = {"1003","第三方平台不存在","platform is not exist."};
	
//	String[] RETURN_PLAT_EXIST = {"1004","第三方平台已存在","platform  is already existed! "};
	
//	String[] RETURN_COMP_NOT_EXIST  = {"1005","公司不存在","company is not exist."};
	
//	String[] RETURN_COMP_EXIST = {"1006","公司已存在","company is already existed! "};
	
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

	String[] RETURN_YUNSIGN_COMPANY_EXIST = {"1021","云签公司已经存在","yunsign company is exist."};

	String[] RETURN_YUNSIGN_CUSTOM_EXIST = {"1022","云签用户已经存在","yunsign custom is exist."};
	
//	String[] RETURN_PLAT_APPLY_NOT_EXIST = {"1023","平台申请记录不存在","platform apply is not exist."};
	
//	String[] RETURN_PLAT_APPLY_EXIST = {"1024","平台申请审批编号已存在","platform apply serial number is exist."};
	
	String[] RETURN_PLAT_APPLY_RECORD_NOT_EXIST = {"1025","缔约室申请记录不存在","platform apply record is not exist."};
	
	String[] RETURN_YUNSIGN_CHILD_NOT_MATCH = {"1026","子账号与主账号不同意","child account is not match."};
	
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
	
	String[] RETURN_AMNT_NOT_ENOUGH = {"3003","余额不足","insufficient account balance!"};
	
	String[] CHANGE_ORDERSTATUS_NOTALLOWED = {"3004","不允许变更订单状态","OrderStatus change is not allowed.!"};
	
	String[] RETURN_PAY_CODE_NOT_EXIST={"3005","编码对应的服务不存在","service of the code is not exist"};
	
	String[] PAYCODE_ALREADY_EXISTS={"3006","服务对应的编码已存在","ILLEAGLE PAYCODE"};
	
	String[] RETURN_TIMES_NOT_ENOUGH = {"3007","剩余次数不足,请购买服务次数","insufficient consume times!"};
	
	String[] RETURN_MONEY_NOT_LEGAL={"3008","充值的金额不合法","ILLEAGLE MONEY"};
	
	String[] SAVE_USER_EXCEPTION={"3009","保存一条用户出异常","save a new Record of userService exception"};
	
	
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
	
	String[] TEMPLATE_DATA_LOAD_ERROR = {"4022","模板数据装填失败","template data load failure."};
	
	String[] DATA_SAVE_EXCEPTION = {"4023","数据保存失败","data save failure."};
	
	String[] DATA_QUERY_EXCEPTION = {"4023","数据查询异常","data query exception."};
	
	String[] CONTRACT_HASBEEN_CLOSED = {"4024","合同已关闭","contract has close."};
	
	String[] CONTRACT_SERIALNUM_IS_NULL = {"4025","合同编号为空","contract serialnum is null."};
	
	String[] MODIFY_CONTRACT_STATUS = {"4026","修改合同状态失败","modify contract status failure."};
	
	String[] CREATECONTRACT_ATTR_FAIL = {"4027","创建创建初始化附件表失败","Failed to create an initial attachment table."};

	String[] CREATECONTRACT_ATTR_IS_NULL = {"4028","合同附件表为空","Attachment table is null."};
	
	String[] DELETE_CONTRACT_STATUS = {"4026","删除合同失败","delete contract failure."};
	
	String[] OFFTIME_GREATER_CURRENTTIME = {"4027","当前时间大于过期时间","the current time is greater than the expiration time."};

	String[] REVOKE_REFUSE={"4028","合同不能撤销或拒绝","contract can not revoke or refuse."};
	
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
	
	String[] SERVER_PDF_SIGN ={"4216","pdf事件证书签署异常","event cert pdf sign exception."};
	
	String[] EVENT_ZIP_SIGN ={"4217","事件证书ZIP包签署异常","event cert zip sign exception."};
	
	String[] SIGNATRUE_NOT_EXIST ={"4218","签名域不存在","Signature domain does not exist."};
	
	String[] CONTRACT_HAS_SIGNED ={"4219","合同已签署完成","The contract has been signed and completed."};
	
	String[] CONTRACT_HAS_REVOKE ={"4220","合同已撤销","The contract has been revoked."};
	
	String[] CONTRACT_HAS_REFUSE ={"4221","合同已拒绝","The contract has been refused."};
	
	String[] CONTRACT_HAS_CLOSE ={"4222","合同已关闭","The contract has been closed."};
	
	String[] ZIP_DOWN_INDEX ={"4300","读取index文件异常","read index file exception."};
	
	String[] JSONSYNTAXEXCEPTION ={"4301","json转换异常","JsonSyntaxException."};
	
}
