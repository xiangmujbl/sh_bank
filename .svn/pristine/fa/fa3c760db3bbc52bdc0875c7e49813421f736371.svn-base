package com.mmec.util;

import org.springframework.context.support.StaticApplicationContext;

public interface ErrorData {

	// 成功返回码
	static String SUCCESS = "000";

	// 参数appId不能为空
	static String APPID_IS_NULL = "001";

	// 参数time不能为空
	static String TIME_IS_NULL = "002";

	// 参数sign值不能为空
	static String SIGN_IS_NULL = "003";

	// 参数signType不能为空
	static String SIGNTYPE_IS_NULL = "004";

	// 参数userId不能为空
	static String USERID_IS_NULL = "005";

	// 参数orderId不能为空
	static String ORDERID_IS_NULL = "006";
	// 参数AUTHORUSERId不能为空
	////6.06//////
	static String AUTHORUSERId_IS_NULL = "016";
	////6.06/////
	
		// MD5校验失败
		static String MD5_VALID_FAIL = "007";
	
		// 时间戳校验失败
		static String TIME_VALID_FAIL = "008";

	// 系统异常
	static String SYSTEM_ERROR = "009";
	
	// 没有权限操作此接口
	static String NO_AUTH = "010";

	// 参数time格式不正确
	static String TIME_IS_INVALID = "011";

	// 次平台没有发送短信功能
	static String NO_SENDSMS = "012";

	// 没有签署PDF合同的权限
	static String NO_PDF_SIGN_AUTH = "013";
	// 没有签署ZIP合同的权限
	static String NO_ZIP_SIGN_AUTH = "014";

	// 参数info不能为空
	static String INFO_IS_NULL = "100";
	// 用户类型type不能为空
	static String USERTYPE_IS_NULL = "101";
	// 用户类型type不正确
	static String USERTYPE_IS_INVALID = "102";
	// 用户姓名userName不能为空
	static String USERNAME_IS_NULL = "103";
     ////////6.06///开始/////////
	static String USERNAME_IS_SIZE = "903";
    ////////6.06///结束/////////
	// 用户身份证号码identityCard不能为空
	static String IDCARD_IS_NULL = "104";
	// 用户身份证号码identityCard不是15或18位
	static String IDCARD_IS_INVALID = "105";
	// 用户手机号码mobile不能为空
	static String MOBILE_IS_NULL = "106";
	// 用户手机号码mobile格式不正确
	static String MOBILE_IS_INVALID = "107";
	// 邮箱email不能为空
	static String EMAIL_IS_NULL = "108";
	// 邮箱email格式不正确
	static String EMAIL_IS_INVALID = "109";
	// 企业营业执照licenseNo不能为空
	static String LICENSE_IS_NULL = "110";
	// 企业名称companyName不能为空
	static String COMPNAME_IS_NULL = "111";
	// 注册失败
	static String REGISTER_ERROR = "112";
	// 邮箱email长度过长
	static String EMAIL_IS_LONG = "113";
	// 是否是平台方isAdmin不能为空
	static String ISADMIN_IS_NULL = "114";
	// 是否是平台方isAdmin不正确
	static String ISADMIN_IS_INVALID = "115";
	// 是否是平台方isBusinessAdmin不能为空
	static String ISBUSIADMIN_IS_NULL = "116";
	// 是否是平台方isBusinessAdmin不正确
	static String ISBUSIADMIN_IS_INVALID = "117";
	static String NEW_MOBILE_IS_NULL = "125";
	static String NEW_MOBILE_IS_LIKE_OLD_MOBILE = "126";
	// 参数info格式不正确
	static String INFO_IS_INVALID = "018";
	// 营业执照号不正确
	static String LICENSE_IS_INVALID = "118";
	// 个人用户修改信息时，手机号不能为空
	static String UPDATE_MOBILE_IS_NULL = "120";
	// 手机号格式不正确
	static String UPDATE_MOBILE_IS_INVALID = "121";
	// 企业用户修改信息时，邮箱不能为空
	static String UPDATE_EMAIL_IS_NULL = "122";
	// 邮箱格式不正确
	static String UPDATE_EMAIL_IS_INVALID = "123";
	// 密码不能为空
	static String PWD_IS_NULL = "124";
	// 固定电话格式错误
	static String PHONENUMBER_IS_INVALID = "125";
	// 缔约各方customsId不能为空
	static String CUSTOM_IS_NULL = "200";
	// 模板编号templateId不能为空
	static String TEMPID_IS_NULL = "201";
	// 合同标题title不能为空
	static String TITLE_IS_NULL = "202";
	// 合同失效时间offerTime不能为空
	static String OFFTIME_IS_NULL = "203";
	// 合同模板装填参数data不能为空
	static String DATA_IS_NULL = "204";
	// 当前时间大于过期时间，不能创建合同
	static String TIME_IS_OVER = "205";
	// 缔约各方customsId格式错误
	static String CUSTOM_IS_WRONG = "206";
	// 过期时间格式错误
	static String OFFERTIME_IS_WRONG = "207";
	// 此合同发送的短信条数已经超过10条，不再继续发送
	static String SEACH_USER_ERROR="208";

	//证书信息为空
	static String CERT_IS_NULL="209";
	static String ORIGINAL_IS_NULL="210";
	static String SIGNATURE_IS_NULL="211";
	static String MSG_OVER_LIMIT = "300";
	// 短信验证码发送失败
	static String SEND_MSG_ERROR = "301";
	// 验证码validCode不能为空
	static String VALIDCODE_IS_NULL = "310";
	// 验证码validCode的格式不正确
	static String VALIDCODE_IS_INVALID = "311";
	// 没有发送手机验证码，或验证码填写错误
	static String MSGCODE_ISNOT_EXIT = "313";
	// 短信验证码错误，校验失败
	static String MSGCODE_IS_ERROR = "314";
	// 短信验证码已经过期，超过2分钟
	static String MSGCODE_IS_EXPIRED = "315";
	// 密码不正确，校验失败
	static String PWD_IS_ERROR = "316";
	// 没有发送邮箱验证码，或验证码填写错误
	static String EMAILCODE_ISNOT_EXIT = "317";
	// 邮箱验证码错误，校验失败
	static String EMAILCODE_IS_ERROR = "318";
	// 签名位置信息signInfo不能为空
	static String SIGNATURE_INFO_IS_NULL = "319";
	// 签名位置信息signInfo的格式不正确
	static String SIGNATURE_INFO_IS_INVALID = "320";
	// 签名位置信息signInfo不能有空值
	static String SIGNATURE_INFO_HAS_NULL = "321";
	// 签名位置信息signInfo里的signUiType取值不正确
	static String SIGNATURE_INFO_UITYPE_IS_WRONG = "325";
	// 邮件发送成功
	static String SEND_EMAIL_SUCCESS = "322";
	// 邮件发送失败
	static String SEND_EMAIL_FAIL = "323";
	// 邮箱验证码已过期，超过了5分钟
	static String EMAILCODE_IS_EXPIRED = "324";

	// 下载合同时，orderid不能为空
	static String ORDERIDS_IS_NULL = "350";
	// ftp连接失败
	static String FTP_FAILED = "351";
	
	static String MOBILE_CHECK_ERROR="352";
	
	static String IDCARD_CHECK_ERROR="353";
	
	// 查询证书失败
	static String QUERY_CERT_FAILED = "410";
	// 绑定证书失败
	static String BUND_CERT_FAILED = "411";
	// 解绑证书失败
	static String UNBUND_CERT_FAILED = "412";

	// 同步类型syncType不能为空
	static String SYNCTYPE_IS_NULL = "502";

	// 上传附件名不能为空
	static String ATTACHNAME_NULL = "503";

	// 上传附件个数最多为5
	static String ATTACHMENTFILE_MAX = "504";

	// 上传附件名格式错误
	static String ATTACHMENTFILE_INVALID = "505";

	// 上传文件格式支持docx,doc,pdf,jpg,jpeg,html,htm
	static String UPLOADFILE_FORMAT = "506";

	// 上传附件不能为空
	static String ATTACHINFO_NULL = "507";

	// 上传的文件个数与文件名个数不一致
	static String NUMBER_WRONG = "508";

	// 上传正文名不能为空
	static String FILENAME_NULL = "509";

	// 上传正文不能为空
	static String FILEINFO_NULL = "510";

	// 上传音像视频格式支持MP4
	static String UPLOADVIDEO_FORMAT = "511";

	// 上传文件格式支持docx,doc,pdf,jpg,jpeg,html,htm,MP4
	static String UPLOADATTACH_FORMAT = "512";

	// 上传文件大于10兆
	static String FILE_LARGE = "513";

	// 高度不能为空
	static String LENGTH_NULL = "514";

	// 宽度不能为空
	static String WIDTH_NULL = "515";

	// 图片宽大于400
	static String WIDTH_LARGE = "516";

	// 图片高大于70
	static String HEIGHT_LARGE = "519";
	// 图片处理失败
	static String IMAGEPROCESS_FAIL = "517";

	// 宽高格式不正确
	static String NUMBER_INVALID = "518";

	// 身份证号与姓名不匹配
	static String IDENTITY_MISMATCH = "519";
	// OCR姓名不匹配
	static String OCR_USERNAMEERROR = "520";
	// OCR身份证号不匹配
	static String OCR_IDCARDREEOR = "521";
	// OCR验证失败
	static String OCR_CHECKERROR = "522";
	// OCR验证失败
	static String IDCARDIMG_IS_NULL = "523";
	// orderId格式不正确，应包含字母或数字
	static String ORDERID_IS_INVALID = "524";
	//该手机号已注册
	static String MOBILE_IS_REGISTERED = "525";
	//该邮箱已注册
	static String EMAIL_IS_REGISTERED = "526";
	//证件号码不能为空
	static String CARDNUM_IS_NULL ="527";
	//证件类型不能为空
	static String CARDTYPE_IS_NULL ="528";
	//证件号码格式不正确
	static String CARDNUM_IS_INVALID = "529";
	//请填写需补全的信息
	static String COMPLETEINFO_IS_NULL = "530";
	//用户姓名已有值，无法补全
	static String NAME_NOT_NULL = "531";
	//用户身份证号已有值，无法补全
	static String USERIDENTITY_NOT_NULL = "532";
	//用户手机号已有值，无法补全
	static String MOBILE_NOT_NULL = "533";
	//用户邮箱已有值，无法补全
	static String EMAIL_NOT_NULL = "534";
	//回调失败
	static String CALLBACK_FAILURE = "535"; 
	//json格式不对
	static String JSON_FORMAT_ERROR = "536"; 
	//调用服务器签署
	static String SERVER_SIGN_ERROR = "537"; 
	//调用服务器签署
	static String TIME_STAMPS_ERROR = "538"; 
	//传文件和填写类型不一致
	static String UPLOADATTACH_ERROR = "539"; 
	//app
	static String NO_SECRETKEY_BY_APPID = "540"; 
	
	
	//被授权人不能为空
	static String RECIVE_NAME_NULL = "600"; 
	
	//授权开始时间不能为空
	static String RECIVTIME_START_TIME_IS_NULL="601";
	
	//授权结束时间不能为空
	static String RECIVTIME_END_TIME_IS_NULL="602";
	
	
	//授权开始时间格式错误
	static String RECIVTIME_START_TIME_IS_WRONG = "603";
	
	//授权结束时间格式错误
	static String RECIVTIME_END_TIME_IS_WRONG="604";
	
	
	//被授权方不是平台方
	static String AUTHENT_IS_NOT_PLATFORM="605";
	
	//合同不存在
	static String NO_CONTRACT="606";
	
	//合同不存在
	static String START_GREATER_END_TIME="607";
	
	//被授权方只能有一个
	static String AUTHENT_IS_ONE="608";
	
	//orderId过长，不能超过255个字符
	static String ORDER_IS_TO_LONG="609";
	
	//合同标题过长，不能超过255个字符
	static String TITLE_IS_TO_LONG="610";
	
	//缔约方只能是授权方或者授权方和被授权方
	static String CUSTOMID_MUST_ATUHOR_AND_USERID="611";
}
