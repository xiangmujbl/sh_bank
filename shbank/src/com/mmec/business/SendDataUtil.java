package com.mmec.business;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.mmec.thrift.service.ApsRMIServices;
import com.mmec.thrift.service.ContractRMIServices;
import com.mmec.thrift.service.CssRMIServices;
import com.mmec.thrift.service.InternelRMIServices;
import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ReturnData;
import com.mmec.thrift.service.SerialRMIServices;
import com.mmec.thrift.service.TempleteRMIServices;
import com.mmec.thrift.service.UserRMIServices;
import com.mmec.util.ConstantParam;

/**
 * 主要提供向中央承载系统请求接口
 * 
 * @author Administrator
 * 
 */
public class SendDataUtil {

	private TTransport transport;
	private TProtocol protocol;
	private UserRMIServices.Client userClient;
	private ContractRMIServices.Client contractClient;
	private TempleteRMIServices.Client templateClient;
	private ApsRMIServices.Client apsClient;
	private SerialRMIServices.Client serialClient;
	private CssRMIServices.Client cssClient;
	private InternelRMIServices.Client internelClient;
	private ReturnData resData;
	private Logger log = Logger.getLogger(SendDataUtil.class);

	public SendDataUtil(String serviceName) {

		// 中央承载的IP和PORT配在web.xml里面，不要在这里修改后提交SVN
		// 9006 192.168.10.182 ConstantParam.IP, ConstantParam.PORT

		transport = new TSocket(ConstantParam.IP, ConstantParam.PORT);
		//transport = new TSocket("192.168.10.19", 9010);
		protocol = new TBinaryProtocol(transport);

		// // 使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO。记得调用close释放资源
		// TTransport transport = new TFramedTransport(new
		// TSocket(ConstantParam.IP, ConstantParam.PORT, 100 * 1000));
		// // 高效率的、密集的二进制编码格式进行数据传输协议
		// TProtocol protocol = new TCompactProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, serviceName);// serviceName

		if (serviceName.equals("UserRMIServices")) {
			userClient = new UserRMIServices.Client(mp);
		} else if (serviceName.equals("ContractRMIServices")) {
			contractClient = new ContractRMIServices.Client(mp);
		} else if (serviceName.equals("ApsRMIServices")) {
			apsClient = new ApsRMIServices.Client(mp);
		} else if (serviceName.equals("SerialRMIServices")) {
			serialClient = new SerialRMIServices.Client(mp);
		} else if (serviceName.equals("CssRMIServices")) {
			cssClient = new CssRMIServices.Client(mp);
		}else if (serviceName.equals("InternelRMIServices")) {
			internelClient = new InternelRMIServices.Client(mp);
		}else if (serviceName.equals("TempleteRMIServices")) {
			templateClient = new TempleteRMIServices.Client(mp);
		}

		if (transport != null && !transport.isOpen()) {
			try {
				transport.open();
			} catch (TTransportException e) {
				log.error("打开transport.open()失败，请检查中央承载服务是否开启！", e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭资源
	 */
	public void closeRes() {
		if (transport != null && transport.isOpen()) {
			transport.close();
		}
	}

	public static void main(String[] args) throws TException {

		SendDataUtil usr = new SendDataUtil("ContractRMIServices");

		// 签署合同
		// Map<String, String> datamap = new HashMap<String, String>();

		// String signInfo =
		// "[{\"userId\":\"ucid003\",\"position\":\"1,3\",\"signUiType\":\"1\"},{\"userId\":\"ucid004\",\"position\":\"2\",\"signUiType\":\"2\"}]";
		//
		// datamap.put("appId", "Udz2ILyzx7");
		// datamap.put("orderId", "test_z2");
		// datamap.put("userId", "ucid003");
		// datamap.put("specialCharacter", "*");
		// datamap.put("specialCharacterNumber", signInfo);
		//
		// usr.addSignInfo(datamap);

		// UserServiceImpl us = new UserServiceImpl();
		// // us.sendSmsByTrans("13800001A11", "123456");
		//
		// SignServiceImpl ss = new SignServiceImpl();
		// Map<String, String> datamap = new HashMap<String, String>();
		// datamap.put("optFrom", ConstantParam.OPT_FROM);
		// datamap.put("appId", "LgX3vS6pb2");
		// datamap.put("mobile", "13800A00111");
		// datamap.put("smsType", "sign");
		// datamap.put("checkCode", "123456");
		// datamap.put("platformUserName", "test_n09");
		// datamap.put("requestIp", "192.168.10.10");
		//
		// ss.sendSmsByTrans(datamap);
	}

	/**
	 * 查询平台信息
	 */
	public ReturnData queryPlatForm(Map<String, String> datamap) {

		try {

			resData = userClient.platformQuery(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 查询平台回调信息
	 */
	public ReturnData queryPlateFormCallBack(Map<String, String> map) {

		try {

			resData = userClient.platformCallbackQuery(map);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		System.out.println(resData);
		return resData;
	}

	/**
	 * 用户登录
	 */
	public ReturnData userLogin(Map<String, String> map) {

		try {

			resData = userClient.userLogin(map);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		System.out.println(resData);
		return resData;
	}

	/**
	 * 证书查询
	 */
	public ReturnData certQuery(Map<String, String> map) {

		try {
			// Map datamap = new HashMap();
			// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd
			// HH:mm:ss");
			//
			// datamap.put("optFrom", "MMEC");// 必填
			// datamap.put("appId", "appid001");// 必填
			// datamap.put("platformUserName", "QQ123456");// 必填
			// datamap.put("certNum", "123");// 必填
			// datamap.put("certContent", "AAAAAAAAAAA");// 必填
			// datamap.put("certType", "3");// 必填
			resData = userClient.certQuery(map);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		System.out.println(resData);
		return resData;
	}

	/**
	 * 证书绑定
	 */
	public ReturnData certBund(Map<String, String> map) {

		try {

			resData = userClient.certRegister(map);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 证书解绑
	 */
	public ReturnData certUnbund(Map<String, String> map) {

		try {

			resData = userClient.certUnbund(map);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 证书查询
	 */
	public ReturnData certLogin(Map<String, String> map) {

		try {
			Map datamap = new HashMap();
			datamap.put("optFrom", "MMEC");// 必填
			datamap.put("appId", "appid001");// 必填（平台编码）
			datamap.put("certNum", "123");// 必填（证书编码）
			datamap.put("certContent", "AAAAAAAAAAA");// 必填(证书内容)
			resData = userClient.certLogin(map);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	// 插入日志信息
	// optFrom 云签传YUNSIGN_CLIENT 对接传MMEC_CLIENT
	// appId 传客户的appId
	// account/platformUserName/userid 三个字段选传一项 标识用户
	// serverIp 传客户端IP
	// returnData 传ReturnData转json的值
	// serviceException 传serviceException转json的值 如果客户调用成功没有异常时该项不传
	// optType 传方法名称
	/**
	 * 保存日志
	 */
	public ReturnData insertLog(Map<String, String> datamap) {

		try {

			resData = serialClient.insertLog(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 增加图章
	 */
	public ReturnData addSeal(Map<String, String> datamap) {

		try {

			resData = userClient.addSeal(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 查询图章
	 */
	public ReturnData querySeal(Map<String, String> datamap) {

		try {

			resData = userClient.querySeal(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 删除图章
	 */
	public ReturnData delSeal(Map<String, String> datamap) {

		try {

			resData = userClient.delSeal(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 改变合同状态
	 */
	public ReturnData modifyContractStatus(Map<String, String> datamap) {

		try {

			resData = contractClient.modifyContractStatus(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 增加签名位置信息
	 */
	public ReturnData addSignInfo(Map<String, String> datamap) {

		try {

			resData = contractClient.addPdfInfo(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 查询签名位置信息
	 */
	public ReturnData querySignInfoByUserId(Map<String, String> datamap) {

		try {

			resData = contractClient.queryPdfInfoByUserId(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 创建合同
	 */
	public ReturnData createContract(Map datamap) {

		try {

			resData = contractClient.createContract(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 授权创建
	 */
	public ReturnData authorCreate(Map datamap) {

		try {

			resData = contractClient.createContract(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 创建合同
	 */
	public ReturnData createContractFinance(Map datamap) {

		try {

			resData = contractClient.internetFinanceCreate(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 签署合同
	 */
	public ReturnData signContract(Map datamap) {

		try {

			resData = contractClient.signContract(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 签署合同
	 */
	public ReturnData authoritySignContract(Map datamap) {

		try {

			resData = contractClient.authoritySignContract(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 修改用户是否是管理员
	 */
	public ReturnData changeUserAdmin(Map<String, String> datamap) {

		try {

			resData = userClient.changeAppAdmin(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 发送微信消息
	 */
	public ReturnData sendWXMessage4Type(Map<String, String> dataMap) {

		try {

			resData = apsClient.sendWXMessage4Type(dataMap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 发送短信
	 */
	public ReturnData sendSms(Map<String, String> dataMap) {

		try {

			resData = apsClient.sendMessage4Type(dataMap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 统计短信接口
	 */
	/**
	 * 发送短信
	 */
	public ReturnData getSmsCodeList(Map<String, String> dataMap) {

		try {

			resData = apsClient.getSmsCodeList(dataMap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}
	
	
	/**
	 * 发送短信总数
	 */
	public ReturnData querySmsCodeCount(Map<String, String> dataMap) {

		try {

			resData = apsClient.querySmsCodeCount(dataMap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}
	
	/**
	 * 发送短信，旧接口
	 */
	public com.mmec.thrift.service.Result sendSms(String mobile, String message, String smsTrans,String appId) {

		com.mmec.thrift.service.Result result = null;

		try {

			result = apsClient.sendMessage4Trans(mobile, message, smsTrans,appId);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(result);
		return result;
	}

	/**
	 * 修改密码
	 */
	public ReturnData changePasswod(Map<String, String> datamap) {

		try {

			resData = userClient.userUpdate(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 用户查询
	 */
	public ReturnData userQuery(Map<String, String> map) {

		try {

			resData = userClient.userQuery(map);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 用户查询
	 */
	public ReturnData listAttn(Map<String, String> map) {

		try {

			resData = userClient.listMyAttn(map);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 用户资料修改
	 */
	public ReturnData userUpdate(Map<String, String> datamap) {

		try {

			resData = userClient.userUpdate(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 用户注册
	 * 
	 * @return
	 * @throws TException
	 */
	public ReturnData userRegister(Map<String, String> datamap) {

		try {

			resData = userClient.userRegister(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}
	
	/**
	 * 途牛用户注册
	 * 
	 * @return
	 * @throws TException
	 */
	public ReturnData userRegisterTUNIU(Map<String, String> datamap) {

		try {

			resData = userClient.userRegisterTUNIU(datamap);

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}

	/**
	 * 查询合同
	 * 
	 * @param javaBean
	 * @return
	 */
	public ReturnData queryContract(Map<String, String> datamap) {

		try {
			resData = contractClient.queryContract(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}
	/**
	 * 进入签署页面之前，先查询合同状态，检验是否签署过
	 * 
	 * @param javaBean
	 * @return
	 */
	public ReturnData signQueryContract(Map<String, String> datamap) {

		try {
			resData = contractClient.signQueryContract(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}

		System.out.println(resData);
		return resData;
	}
	/**
	 * 下载合同
	 * 
	 * @param javaBean
	 * @return
	 */
	public ReturnData downloadContract(Map<String, String> datamap) {

		ReturnData returnData = null;
		try {
			returnData = contractClient.downLoadContract(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return returnData;
	}

	/**
	 * 根据手机号码查询个人用户
	 * 
	 * @param datamap
	 * @return
	 */
	public ReturnData getCustomByMobile(Map<String, String> datamap) {

		ReturnData returnData = null;
		try {
			returnData = userClient.getCustomByMobile(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return returnData;
	}

	/**
	 * 根据邮箱号码查询企业用户
	 * 
	 * @param datamap
	 * @return
	 */
	public ReturnData getCompanyByEmai(Map<String, String> datamap) {

		ReturnData returnData = null;
		try {
			returnData = userClient.getCompanyByEmail(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return returnData;
	}

	/**
	 * 互动金融查询合同
	 * 
	 * @param javaBean
	 * @return
	 */
	public ReturnData getXtContract(Map<String, String> dataMap) {
		ReturnData returnData = null;
		try {
			returnData = contractClient.internetFinanceQueryContract(dataMap);
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnData;
	}

	/**
	 * 外部数据同步
	 * 
	 * @param javaBean
	 * @return
	 */
	public ReturnData addExternalDataImport(Map<String, String> dataMap){
		ReturnData returnData = new ReturnData();
		try {
			returnData = contractClient.saveExternalDataImport(dataMap);
			log.info("addExternalDataImport 中央承载返回结果:"+returnData.toString());
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			closeRes();
		}
		return returnData;		
	}

	/**
	 * 云签本地化扣次
	 * 
	 * @param javaBean
	 * @return
	 */
	public ReturnData localPay(Map<String, String> dataMap) {
		ReturnData rd = null;
		try {
			rd = serialClient.localPay(dataMap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return rd;
	}

	/**
	 * 云签本地化调服务器证书
	 * 
	 * @param javaBean
	 * @return
	 */
	public ResultData sign(String datasource) {
		ResultData rd = null;
		try {
			rd = cssClient.signService(datasource);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return rd;
	}

	/**
	 * 云签本地化调服务器时间戳
	 * 
	 * @param javaBean
	 * @return
	 */
	public ResultData getTimestamp(String contSerialNum, String certFingerprint) {
		ResultData rd = null;
		try {
			rd = cssClient.getTimestampService(contSerialNum, certFingerprint);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return rd;
	}

	/**
	 * HTTP下载合同
	 */
	public ReturnData downLoadPdfContract(Map datamap) {
		ReturnData returnData = null;
		try {
			returnData = contractClient.pdfDownLoadContract(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return returnData;
	}

	/**
	 * 证书验证
	 */
	public ResultData verifySignature(Map datamap) {
		ResultData rd = null;
		try {
			rd = cssClient.verifySignatureNoTimestamp(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return rd;
	}
	
	/**
	 * 获取合同列表（批量签署时候查询用户合同）
	 */
	public ReturnData getContractList(Map<String, String> datamap) {
		ReturnData returnData = null;
		try {
			returnData = contractClient.getContractList(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return returnData;
	}

	public static Map<String, String> Bean2Map(Object javaBean) {
		Map<String, String> ret = new HashMap<String, String>();
		try {
			Method[] methods = javaBean.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("get")) {
					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					Object value = method.invoke(javaBean, (Object[]) null);
					if (null != value && !"".equals(value)) {
						ret.put((String) field, (String) value);
					}
				}
			}
		} catch (Exception e) {
		}
		return ret;
	}
	/**
	 * 根据手机号码查询用户
	 */
	public ReturnData userQueryByMobile(Map<String, String> map) {
		try {
			resData = userClient.userQueryByMobile(map);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return resData;
	}
	
	/**
	 * 校验证书
	 */
	public ReturnData checkCert(Map<String, String> map) {
		try {
			resData = userClient.checkCert(map);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return resData;
	}
	/**
	 * 服务组签名
	 * @param appId
	 * @param time
	 * @param sign
	 * @param signType
	 * @param mobile
	 * @return
	 */
	public ResultData serverSign(String dataSource)
	{
		ResultData rd = null;
		try {
			rd = cssClient.signService(dataSource);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return rd;
	}
	/**
	 * 查询升级表
	 */
	public ReturnData upgradeQuery(Map<String, String> map) {

		try {
			resData = internelClient.upgradeQuery(map);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		System.out.println(resData);
		return resData;
	}
	/**
	 * 事件证书申请
	 */
	public ReturnData eventCertRequest(Map<String, String> map) {

		try {
			resData = internelClient.eventCertRequest(map);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		System.out.println(resData);
		return resData;
	}
	/**
	 * 服务器证书申请
	 */
	public ReturnData serverCertRequest() {

		try {
			resData = internelClient.serverCertRequest();
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		System.out.println(resData);
		return resData;
	}
	/**
	 * 自定义签署
	 */
	public ReturnData customizeSign(Map<String, String> map) {

		try {
			resData = internelClient.customizeSign(map);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		System.out.println(resData);
		return resData;
	}
	
	public ReturnData queryUserExamineStatus(Map<String, String> map) {
		
		try {
			resData = userClient.queryUserExamineStatus(map);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		System.out.println(resData);
		return resData;
	}

	public ReturnData synchronizationUserInfo(Map<String, String> datamap) {
		
		try {
			resData = userClient.synchronizationUserInfo(datamap);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		System.out.println(resData);
		return resData;
	}

	public ReturnData queryTemplateContract(Map<String, String> map) {
		try {
			resData = templateClient.queryTemplteDetail(map);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		System.out.println(resData);
		return resData;
	}
	/**
	 * 证书信息添加pdfFile
	 * @param map
	 * @return
	 */
	public ReturnData certInfoAppendPdfFile(Map<String, String> map) {
		ReturnData returnData = null;
		try {
			returnData = contractClient.certInfoAppendPdfFile(map);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			closeRes();
		}
		return returnData;
	}
}
