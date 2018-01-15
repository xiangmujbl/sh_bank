package com.mmec.test.userClient;

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

import com.mmec.centerService.userModule.entity.PlatformApplyEntity;
import com.mmec.thrift.service.FeeRMIServices;
import com.mmec.thrift.service.ReturnData;
import com.mmec.thrift.service.UserRMIServices;

/**
 * 主要提供向中央承载系统请求接口
 * 
 * @author Administrator
 * 
 */
public class UserClient {
	private Logger log = Logger.getLogger(UserRMIServices.class);

	/**
	 * 企业用户注册
	 * 
	 * @return
	 * @throws TException
	 */
	public static void test_userRegister_companyUser() throws TException {
		TTransport transport = new TSocket("192.168.100.104", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();
		//营业执照号
		datamap.put("businessLicenseNo","12345678987654321");
		//公司名称
		datamap.put("companyName","武当山");
//	//法人身份证号
//		datamap.put("identityCard","110125115201022564");
////		//法人姓名
//		datamap.put("userName","虚竹");
		
		datamap.put("account","wuji001");
		datamap.put("password","123456");
		datamap.put("platformUserName","wuji001");
		datamap.put("mobile","15400000002");
		datamap.put("email","wuji@11.com");
		datamap.put("appId","N21Fs163FY");
		datamap.put("type","2");
		
//		appId=wiklOPFUtp, 
//				type=2, 
//				password=70468286, 
//				phoneNum=025-33323332, 
//				userName=ABC3332, 
//				isBusinessAdmin=1, 
//				businessLicenseNo=license3332, 
//				optFrom=MMEC, 
//				identityCard=ABCDEF333233323332, 
//				companyName=Name3332, email=, 
//				requestIp=192.168.10.101, account=ABC3332_e1467341727618, companyType=国企, isAdmin=1, platformUserName=ABC3332, mobile=19033323332
		datamap.put("optFrom", "MMEC");
		System.out.println(service.userRegister(datamap));//,customDataMap,companyDataMap));
		transport.close();
	}
	

	/**
	 * 企业用户注册
	 * 
	 * @return
	 * @throws TException
	 */
	public static void test_userRegister_YuncompanyUser() throws TException {
		TTransport transport = new TSocket("192.168.100.134", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();

		
		datamap.put("account","ahai0001222223222");
		datamap.put("password","123456");
		datamap.put("platformUserName","ahai00222213222");
		datamap.put("mobile","19000000055");
		datamap.put("email","ahai0032222222@111.com");
		datamap.put("appId","78f8RlcB2o");
		datamap.put("isAdmin","1");
		datamap.put("type","2");
		

		datamap.put("optFrom", "YUNSIGN");
		System.out.println(service.userRegister(datamap));//,customDataMap,companyDataMap));
		transport.close();
	}
	
	/**
	 * 个人用户注册
	 * 
	 * @return
	 * @throws TException
	 */
	public static void test_userRegister_customUser() throws TException {
		TTransport transport = new TSocket("192.168.100.134", 9020);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();
		Map customDataMap= new HashMap();

		//法人身份证号
		datamap.put("identityCard","110330101011110016");
//				//法人姓名
		datamap.put("userName","小桂子");
		
		datamap.put("account","xiaoguizi");
		datamap.put("password","123456");
		datamap.put("platformUserName","xiaozuizi");
		datamap.put("mobile","19000003015");
		datamap.put("email","xiaoguizi@111.com");
		datamap.put("appId","Z4QEXwfJkg");
		
		datamap.put("type","1");
		

		datamap.put("optFrom", "MMEC");
		System.out.println(service.userRegister(datamap));//,customDataMap,null));
		transport.close();
	}
	
	/**
	 * 修改密码
	 */
	public static void test_userUpdate_changePasswod() throws TException {
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();

		//查看用户账号或密码是否存在
		String password = (String) datamap.get("password");
		
		//账户信息
		datamap.put("account","jiwushi");
		datamap.put("password","654321xiugaihou");
		datamap.put("platformUserName","ryjws");
		datamap.put("appId","APP_RYSJ");
		
		datamap.put("optType", "changePassword");
		datamap.put("optFrom", "MMEC");
		System.out.println(service.userUpdate(datamap));//, null, null));
		transport.close();
	}

	/**
	 * 修改邮箱
	 */
	public static void test_userUpdate_changeEmail() throws TException {
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);

		Map datamap= new HashMap();

		//查看用户账号或密码是否存在
		String password = (String) datamap.get("password");
		
		//账户信息
		datamap.put("account","jiwushi");
		datamap.put("platformUserName","ryjws");
		datamap.put("email","jiwushiMM@222.com");
		datamap.put("appId","APP_RYSJ");

		datamap.put("optType", "changeEmail");
		datamap.put("optFrom", "MMEC");
		System.out.println(service.userUpdate(datamap));//, null, null));
		transport.close();
	}
	

	/**
	 * 修改手机
	 */
	public static void test_userUpdate_changeMobile() throws TException {
		TTransport transport = new TSocket("192.168.100.134", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();

		//账户信息
		datamap.put("optType","changeMobile");
		datamap.put("appId","78f8RlcB2o");
		datamap.put("optFrom","YUNSIGN");
		datamap.put("requestIp","192.168.10.55");

		datamap.put("account", "19011112222_p1463449007184_1463449007237");
		datamap.put("platformUserName", "fD6PWLbK3m_AAA777");
		datamap.put("mobile", "15996205816");
		System.out.println(service.userUpdate(datamap));//, null, null));
		transport.close();
	}
	
	/**
	 *  修改 管理员权限   未写完
	 */
	public static void test_userUpdate_updateUserAdmin() throws TException {
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();

		//查看用户账号或密码是否存在
		String password = (String) datamap.get("password");
		
		//账户信息
		datamap.put("account","jiwushi");
		datamap.put("platformUserName","ryjws");
		datamap.put("moblie","135333333333");
		datamap.put("appId","APP_RYSJ");

		datamap.put("optType", "changeAdmin");
		datamap.put("optFrom", "MMEC");
		System.out.println(service.userUpdate(datamap));//, null, null));
		transport.close();
	}
	
	/**
	 * 用户激活  --- 未完成
	 */
	public static void test_userUpdate_userActivat() throws TException {
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();

		//查看用户账号或密码是否存在
		String password = (String) datamap.get("password");
		
		//账户信息
		datamap.put("account","jiwushi");
		datamap.put("platformUserName","ryjws");
		datamap.put("moblie","135333333333");
		datamap.put("appId","APP_RYSJ");

		datamap.put("optType", "userActivat");
		datamap.put("optFrom", "MMEC");
		System.out.println(service.userUpdate(datamap));//, null, null));
		transport.close();
	}
	

	/**
	 * 用户注销  --- 未完成
	 */
	public static void test_userUpdate_userLogOut() throws TException {
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();

		//查看用户账号或密码是否存在
		String password = (String) datamap.get("password");
		
		//账户信息
		datamap.put("account","jiwushi");
		datamap.put("platformUserName","ryjws");
		datamap.put("moblie","135333333333");
		datamap.put("appId","APP_RYSJ");

		datamap.put("optType", "userLogOut");
		datamap.put("optFrom", "MMEC");
		System.out.println(service.userUpdate(datamap));//, null, null));
		transport.close();
	}
	
	/**
	 * 修改手机
	 */
	public static void test_userUpdate_custome() throws TException {
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();
		//查看用户账号或密码是否存在
		String password = (String) datamap.get("password");
		
		//账户信息
		datamap.put("account","jiwushi");
		datamap.put("platformUserName","ryjws");
		datamap.put("moblie","135333333333");
		datamap.put("appId","APP_RYSJ");

		datamap.put("optType", "changeCustom");
		datamap.put("optFrom", "MMEC");
		System.out.println(service.userUpdate(datamap));//, null, null));
		transport.close();
	}
	

	/**
	 * 用户查询
	 */
	public static void test_userQuery() throws TException {
		TTransport transport = new TSocket("192.168.100.104", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);

		Map datamap= new HashMap();
		//账户信息
		datamap.put("platformUserName","YUNSIGN_17000000112@163.com_e");
		datamap.put("appId","78f8RlcB2o");
		datamap.put("optFrom", "MMEC");
		System.out.println(service.userQuery(datamap));
		transport.close();
	}
	
	/**
	 * 用户查询
	 */
	public static void test_userActive() throws TException {
		TTransport transport = new TSocket("192.168.10.108", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();
		
		//账户信息
		datamap.put("optFrom","YUNSIGN");
		datamap.put("appId","78f8RlcB2o");
		
//	datamap.put("account","987@163.com_e");
//		datamap.put("account","111@163.com_e");
		datamap.put("account","977@163.com_e");

		datamap.put("platformUserName","YUNSIGN_977@163.com_e");
		datamap.put("isAuthentic","1");

		System.out.println(service.userActivat(datamap));
		transport.close();
	}
	
	public static void main(String[] args)  {
		//
//		test_platformApply() ;
		//
//		test_platformVerify();
		//
		
//		test_userRegister_customUser();
//		test_userUpdate_changePasswod();

		try {
//			test_getCustomByMobile();
//			test_addMyattn();
//			test_userRegister_companyUser();
//			test_userRegister_customUser();
//			test_userQuery();
//			test_userRegister_companyUser();
			test_userQuery();
//			test_companyMemberQuery();
//			test_getMyattnList();
//			test_delMyattn();
//			test_userRegister_customUser();
//		test_updatePassword();
//		test_userLogin();
		//test_bangdingAccountList();
//			test_userRegister_companyUser();
//			test_findCompanyByEmail();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		SealEntity ss = new SealEntity();
//		System.out.println(ss.getSealName());
	}

	/**
	 * 用户查询
	 */
	public static void test_getCustomByMobile() throws TException {
		TTransport transport = new TSocket("192.168.100.134", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);

		Map datamap= new HashMap();
		//账户信息
		datamap.put("appId","78f8RlcB2o");
		datamap.put("optFrom","YUNSIGN");
		datamap.put("type","1");
		datamap.put("mobile","13951850219");
//		datamap.put("requestIp","125.123.321.11");
		datamap.put("userId","yj111");
		
		System.out.println(service.getCustomByMobile(datamap));
		transport.close();
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
					if(null != value  && !"".equals(value))
					{
						ret.put((String) field, (String) value);
					}
				}
			}
		} catch (Exception e) {
		}
		return ret;
	}
	/**
	 * 个人用户注登录
	 * 
	 * @return
	 * @throws TException
	 */
	public static void test_userLogin() throws TException {
		TTransport transport = new TSocket("192.168.100.208", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();
	
		//账户信息
		datamap.put("account","qiaofeng001@111.com");
		datamap.put("password","123456");
		datamap.put("accountType","3");

		datamap.put("appId","78f8RlcB2o");

		datamap.put("optFrom", "YUNSIGN");
		System.out.println(service.userLogin(datamap));//,customDataMap,null));
		transport.close();
	}
	
	/**
	 * 个人用户修改密码
	 * 
	 * @return
	 * @throws TException
	 */
	public static void test_updatePassword() throws TException {
		TTransport transport = new TSocket("192.168.10.108", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();
	
		//账户信息
		datamap.put("newpassword","qwer14");
		datamap.put("account","fffff@fff.com_e");
		datamap.put("password","123456");
		datamap.put("optType","changePassword");
		datamap.put("appId","78f8RlcB2o");
		datamap.put("optFrom", "YUNSIGN");
		System.out.println(service.userUpdate(datamap));//,customDataMap,null));
		transport.close();
	}

	/**
	 * 待绑定用户列表查询
	 * 
	 * @return
	 * @throws TException
	 */
	public static void test_bangdingAccountList() throws TException {
		TTransport transport = new TSocket("192.168.10.108", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();

		Map datamap= new HashMap();
		//账户信息
		datamap.put("account","13100012329_p");
		datamap.put("appId","78f8RlcB2o");
		datamap.put("optFrom", "YUNSIGN");
		System.out.println(service.bangdingAccountList(datamap));//,customDataMap,null));
		transport.close();
	}
	
	/**
	 * 企业用户注册
	 * 
	 * @return
	 * @throws TException
	 */
	public static void test_findCompanyByEmail() throws TException {
		TTransport transport = new TSocket("192.168.10.182", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);

		Map datamap= new HashMap();
		//营业执照号
		datamap.put("businessLicenseNo","xixiuhai001");
		//公司名称
		datamap.put("companyName","星宿海");
	//法人身份证号
		datamap.put("identityCard","110110101011110006");
//		//法人姓名
		datamap.put("userName","阿海");
		
		datamap.put("account","ahai0001");
		datamap.put("password","123456");
		datamap.put("platformUserName","pppp_ahai001");
		datamap.put("mobile","19000000006");
		datamap.put("email","ahai001@111.com");
		datamap.put("appId","APP_RYSJ");
		datamap.put("type","2");
		

		datamap.put("optFrom", "mmec");
		System.out.println(service.getCompanyByEmail(datamap));//,customDataMap,companyDataMap));
		transport.close();
	}
	

	/**
	 * 用户查询
	 */
	public static void test_getMyattnList() throws TException {
		TTransport transport = new TSocket("192.168.100.134", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);

		Map datamap= new HashMap();
		//账户信息
		datamap.put("appId","78f8RlcB2o");
		datamap.put("optFrom","YUNSIGN");
		datamap.put("platformUserName","YUNSIGN_18052010739_p");
		datamap.put("param","");
//		{appId=78f8RlcB2o, optFrom=YUNSIGN, param=, platformUserName=}
		System.out.println(service.listMyAttn(datamap));
		transport.close();
	}
	


	/**
	 * 用户查询
	 */
	public static void test_addMyattn() throws TException {
		TTransport transport = new TSocket("192.168.100.134", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);

		Map datamap= new HashMap();
		//账户信息
		datamap.put("appId","78f8RlcB2o");
		datamap.put("optFrom","YUNSIGN");
		datamap.put("platformUserName","YUNSIGN_12341212121_p");
		datamap.put("accountType","2");
		datamap.put("attn","14785296354");
		//datamap.put("param","13697845698");
		//14785296354
		System.out.println(service.addMyAttn(datamap));
		transport.close();
	}
	

	/**
	 * 用户查询
	 */
	public static void test_delMyattn() throws TException {
		TTransport transport = new TSocket("192.168.100.134", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);

		Map datamap= new HashMap();
		//账户信息
		datamap.put("appId","78f8RlcB2o");
		datamap.put("optFrom","YUNSIGN");
		datamap.put("platformUserName","YUNSIGN_18052010739_p");
		datamap.put("accountType","2");
		datamap.put("attn","18061618040");
		//datamap.put("param","13697845698");
		//14785296354
		System.out.println(service.delMyAttn(datamap));
		transport.close();
	}
	

	/**
	 * 用户查询
	 */
	public static void test_companyMemberQuery() throws TException {
		TTransport transport = new TSocket("192.168.100.134", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);

		Map datamap= new HashMap();
		//账户信息
		datamap.put("appId","78f8RlcB2o");
		datamap.put("optFrom","YUNSIGN");
		datamap.put("companyName","测试企业3");
		System.out.println(service.listCompanyMember(datamap));
		//{appId=78f8RlcB2o, companyName=江苏买卖网测试1, optFrom=YUNSIGN}.out.println(service.listCompanyMember(datamap));
		transport.close();
	}
}
