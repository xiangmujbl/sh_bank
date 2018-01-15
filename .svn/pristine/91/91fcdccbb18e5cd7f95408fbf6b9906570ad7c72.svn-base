namespace java com.mmec.thrift.service

struct  CertKey{
1:string key;
}

struct Result{
1:i32 status;
2:string desc;
3:string pojo;
}

struct Imgpath{
1:i32 code;
2:map<string,map<i32,string>> paths;
}

struct Activate{
1:string certSerial;
2:string platformUserId;
3:string companyName;
4:string personName;
5:string mobile;
6:string appId
}

service ApsRMIServices {

// 1、PDF转图片
Imgpath pdfToImg(1:string filepath);

// 2、短信下发
Result sendMessage(1:string mobile, 2:string message);
// 3、短信下发
ReturnData sendMessage4Type(1: map<string,string> datamap);
// 4、微信下发
ReturnData sendWXMessage4Type(1: map<string,string> datamap);
// 5、微信下发
ReturnData sendWXMessage4User(1:string userId, 2:string message);
// 6、短信下发
Result sendMessage4Trans(1:string mobile, 2:string message, 3:string smsTrans, 4:string appId);
//7,获取短信列表
ReturnData getSmsCodeList(1: map<string,string> datamap);
//8,短信总数
ReturnData querySmsCodeCount(1: map<string,string> datamap);
}

struct DataResult{
1:string contSerialNum;
2:string title;
3:string zipSha1;
4:string zipSize;
5:list<map<string,string>> signer;
}

struct  ResultVerify{
1:string status;
2:DataResult data;
3:string desc;
}

struct ResultData{
1:i32 status;
2:string desc;
3:string pojo;
}

struct ReturnData{
1:string retCode;
2:string desc;
3:string descEn;
4:string pojo;
}

service CssRMIServices {

// 1、证书签名验签接口
ResultVerify verify(1:string zipPath,2:string filePath);

// 2、无证书签名接口
ResultData signService(1:string datasource);

// 3、获取时间戳接口
ResultData getTimestampService(1:string contSerialNum,2:string certFingerprint);

// 4、验证签名
ResultData verifySignature(1:string cert,2:string originalSignature,3:string signature,4:string timeStamp,5:string contractSerialNum);

// 5、验证pdf签名
ResultData verifyPDF(1:string pdfPath);
}

//用户模块接口
service UserRMIServices {
    // 1、用户登录  来源 有MMEC 云签 证书
	ReturnData userLogin(1: map<string,string> datamap),
     // 2、用户注册 
	ReturnData userRegister(1: map<string,string> datamap),
    // 3、用户更新  
	ReturnData userUpdate(1:map<string,string> datamap),
    // 4、用户激活
	ReturnData userActivat(1: map<string,string> datamap),
    // 5、用户注销
	ReturnData userLogOut(1: map<string,string> datamap),
    // 6、用户查询
	ReturnData userQuery(1:  map<string,string> datamap),
	// 7、证书登录
	ReturnData certLogin(1:  map<string,string> datamap),
	// 8、证书注册
	ReturnData certRegister(1:  map<string,string> datamap),
	// 9、证书查询
	ReturnData certQuery(1: map<string,string> datamap),
	// 10、证书激活
	ReturnData certActive(1: map<string,string> datamap),
	// 11、证书解绑
	ReturnData certUnbund(1: map<string,string> datamap),
	// 12、平台信息注册
	ReturnData platformRegister(1:map<string,string> datamap),
	// 13、平台信息认证
	ReturnData platformVerify(1:  map<string,string> datamap),
	// 14、平台信息查询
	ReturnData platformQuery(1:map<string,string> datamap),
	// 15、根据手机号码查询个人用户是否存在
	ReturnData getCustomByMobile(1:map<string,string> datamap),
	// 16、根据邮箱查询企业用户是否存在
	ReturnData getCompanyByEmail(1:map<string,string> datamap),
	// 17、新建签约室
	ReturnData openSigningRoom(1:map<string,string> datamap),
	// 18、查询签约室
	ReturnData querySigningRoom(1:map<string,string> datamap),
	// 19、停用签约室
	ReturnData closeSigningRoom(1:map<string,string> datamap),
	// 20、添加子账号
	ReturnData addChildAccount(1:map<string,string> datamap),
	// 21、修改子账号
	ReturnData updateChildAccount(1:map<string,string> datamap),
	// 22、停用子账号
	ReturnData stopChildAccount(1:map<string,string> datamap),
	// 23、查询企业下子账号
	ReturnData queryChildAccount(1:map<string,string> datamap),
	// 24、查询缔约室
	ReturnData queryNonYunSignPlatform(1:map<string,string> datamap),
	// 25、新增图章
	ReturnData addSeal(1:map<string,string> datamap),
	// 26、查询图章
	ReturnData querySeal(1:map<string,string> datamap),
	// 27、删除图章
	ReturnData delSeal(1:map<string,string> datamap),
	// 28、签约室申请
	ReturnData platformApply(1:  map<string,string> datamap),
	// 29、签约室审核
	ReturnData platformApplyCheck(1:  map<string,string> datamap),
	// 30、签约室列表查询
	ReturnData platformApplyQuery(1:  map<string,string> datamap),
	// 31、获取所有用户
	ReturnData getAllUser(1:map<string,string> datamap),
	// 32、待绑定账号列表
	ReturnData bangdingAccountList(1:map<string,string> datamap),
	// 33、平台回调信息查询
	ReturnData platformCallbackQuery(1:map<string,string> datamap),
	// 34、微信账号绑定
	ReturnData bangindWx(1:map<string,string> datamap),
	// 35、微信账号解绑
	ReturnData unbundWx(1:map<string,string> datamap),
	// 36、3.0迁移数据查询  根据手机号查询企业账号信息
	ReturnData getCompanyAccountByMobile(1:map<string,string> datamap),
	// 37、3.0迁移数据查询  根据手机号查询企业账号信息
	ReturnData getCustomAccountByEmail(1:map<string,string> datamap),
	// 38、添加联系人
	ReturnData addMyAttn(1:map<string,string> datamap),
	// 39、删除联系人
	ReturnData delMyAttn(1:map<string,string> datamap),
	// 40联系人列表
	ReturnData listMyAttn(1:map<string,string> datamap),
	// 41 根据公司名称查询 公司员工信息
	ReturnData listCompanyMember(1:map<string,string> datamap),
	// 42 变更平台管理员账号
	ReturnData changeAppAdmin(1:map<string,string> datamap)
	// 43 根据手机号查询用户
	ReturnData userQueryByMobile(1:map<string,string> datamap),
	// 45 证书校验
	ReturnData checkCert(1:map<string,string> datamap),

	
	//46 查询用户审核状态，如果审核不通过则直接删除用户信息，删除为逻辑删除
	ReturnData queryUserExamineStatus(1:map<string,string> datamap),
	
	//47 同步用户状态
	ReturnData synchronizationUserInfo(1:map<string,string> datamap),
	
	//48途牛用户注册
	ReturnData userRegisterTUNIU(1:map<string,string> datamap)
}

//计费模块接口
service FeeRMIServices {
		//用户账户接口
		
		//1查询用户账户余额
		ReturnData queryUserAccount(1:string userid),
		
		//2给用户账户充值
		ReturnData addMoney(1:map<string,string> datamap),
		
		//3给用户扣费
		ReturnData reduceMoney(1:map<string,string> datamap),
		
		//用户服务接口
		
		//4查询用户服务次数
		ReturnData queryUserServe(1:map<string,string> datamap),
		
		//5给用户某个服务加N次
		ReturnData addServeTimes(1:map<string,string> datamap),
		
		//6给用户某个服务减N次
		ReturnData reduceServeTimes(1:map<string,string> datamap),
		
		//服务类型接口
		
		//7查询当前服务种类
		ReturnData queryPayServe(1:map<string,string> datamap),
		
		//8增加一个服务种类
		ReturnData addPayServe(1:map<string,string> datamap),
		
		//9 更新服务名称
		ReturnData updatePayServe(1:map<string,string> datamap),
		
		//订单接口
		
		//10保存订单记录
		ReturnData saveOrder(1:map<string,string> datamap),
		
		//11查询消费记录
		ReturnData queryPayRecord(1:map<string,string> datamap);
}

//合同模块接口
service ContractRMIServices {
	ReturnData createContract(1: map<string,string> datamap),
	ReturnData signContract(1: map<string,string> datamap),
	//授权签署
	ReturnData authoritySignContract(1: map<string,string> datamap),
	ReturnData findContract(1: map<string,string> datamap),
	//签署之前查看合同的状态信息
	ReturnData signQueryContract(1: map<string,string> datamap),
	ReturnData queryContract(1: map<string,string> datamap),
	ReturnData modifyContractStatus(1: map<string,string> datamap),
	ReturnData downLoadContract(1: map<string,string> datamap),
	//pdf下载合同
	ReturnData pdfDownLoadContract(1: map<string,string> datamap),
	ReturnData getContractList(1: map<string,string> datamap),
	//云签查询草稿箱合同
	ReturnData getDraftContractList(1: map<string,string> datamap),
	
	ReturnData protectContract(1: map<string,string> datamap),
	ReturnData queryProtectContract(1: map<string,string> datamap),
	ReturnData getInternetFinanceContractList(1: map<string,string> datamap),
	ReturnData deleteContract(1: map<string,string> datamap),
	ReturnData internetFinanceCreate(1: map<string,string> datamap),
	ReturnData internetFinanceQueryContract(1: map<string,string> datamap),
	ReturnData queryAllYusignTemplate(1: map<string,string> datamap),
	ReturnData queryYusignTemplateByKind(1: map<string,string> datamap),
	ReturnData addPdfInfo(1: map<string,string> datamap),
	ReturnData queryPdfInfo(1: map<string,string> datamap),
	ReturnData queryPdfInfoByUserId(1: map<string,string> datamap),
	ReturnData saveExternalDataImport(1: map<string,string> datamap),
	ReturnData addSecurity()
	ReturnData certInfoAppendPdfFile(Map<String, String> datamap);
}

//存证接口
service DepositoryRMIServices{
	//1 注册用户
	ReturnData registerUser(1: map<string,string> datamap),
	
	//2存证/资料上传
	ReturnData uploadEvidence(1: map<string,string> datamap),
	
	//3查看存证报告
	ReturnData evidenceDetail(1: map<string,string> datamap),
	
	//4下载存证信息
	ReturnData downloadEvidence(1: map<string,string> datamap),
	
	//5信息审核
	ReturnData userCheck(1: map<string,string> datamap);
}


//自助模板接口
service TempleteRMIServices{
	//1 新增合同模板
	ReturnData addTemplte(1: map<string,string> datamap),
	
	//2 修改合同模板
	ReturnData modifyTemplte(1: map<string,string> datamap),
	
	//3 删除短信模板
	ReturnData deleteTemplte(1: map<string,string> datamap),
	
	//4 查询模板列表
	ReturnData queryTemplteList(1: map<string,string> datamap),
	
	//5 查询模板明细
	ReturnData queryTemplteDetail(1: map<string,string> datamap),
	
	//6 模板状态变更
	ReturnData statuTemplete(1: map<string,string> datamap)
}

//认证计费接口
service AuthFeeRMIServices{
	//1企业认证查询接口计费
	ReturnData enterpriseAuthFee(1:map<string,string> datamap)
}

service InternelRMIServices{
	//2.0到3.0转接接口
	ReturnData upgradeQuery(1:map<string,string> datamap),
	//申请事件证书
	ReturnData eventCertRequest(1:map<string,string> datamap),
	//事件证书吊销
	ReturnData eventCertRevoke(1:map<string,string> datamap),
	//返回签名值
	ReturnData customizeSign(1:map<string,string> datamap),

	ReturnData serverCertRequest()
}