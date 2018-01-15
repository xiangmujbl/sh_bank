package com.mmec.test.userClient;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.mmec.centerService.userModule.entity.AttachmentEntity;
import com.mmec.centerService.userModule.entity.PlatformApplyEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.thrift.service.UserRMIServices;

public class PlatformClient {

	/**
	 * 平台申请
	 * 
	 * @return
	 * @throws TException
	 */
	public static void test_platformRegister() throws TException {
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformApplyEntity platformApplyEntity = new PlatformApplyEntity();
		AttachmentEntity attachmentEntity = new AttachmentEntity();

		//附件信息  --  法人身份信息
		//名称
		attachmentEntity.setAttachmentName("平台申请人证件号11");
		//文件路径
		attachmentEntity.setAttachmentPath("aaa/bbb.jpg");
		//图片类型后缀名
		attachmentEntity.setAttachmentExtension("jpg");
		//图片存放地址
		attachmentEntity.setAttachmentUri("asad/ddv/asd.jpg");
		//图片缩略图存放地址
		attachmentEntity.setAttachmentThumbUri("asad/ddv/asd.jpg");

		//营业执照号
		platformApplyEntity.setBusinessLicenseNo("PA0001");
		//企业详细地址
		platformApplyEntity.setAddress("XXX市CCC区BBB号");
		//商家公司名称
		platformApplyEntity.setCompanyName("日月神教");
		//邮箱
		platformApplyEntity.setEmail("riyueshenjiao@rr.com");
		//联系方式
		platformApplyEntity.setLinkTel("13111111111");
		//联系人
		platformApplyEntity.setLinkName("东方不败");
		//委托人身份证件号
		platformApplyEntity.setIdentityCard("32001122202222222");
		//审批编号
		platformApplyEntity.setSerialNum("DFBB001");
		
//		Map datamap = Bean2Map(platformApplyEntity);
//		Map datamap2 = Bean2Map(attachmentEntity);
//		datamap.putAll(datamap2);
//		System.out.println(datamap2);

//		datamap.put("optFrom", "mmec");
//		System.out.println(service.platformRegister(datamap));
		transport.close();
	}

	/**
	 * 平台申请
	 * 
	 * @return
	 * @throws TException
	 */
	public static void test_platformVerify() throws TException {
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		PlatformEntity platformEntity = new PlatformEntity();
		AttachmentEntity attachmentEntity = new AttachmentEntity();

		//附件信息  --  法人身份信息
		//名称
		attachmentEntity.setAttachmentName("日月神教营业证11");
		//文件路径
		attachmentEntity.setAttachmentPath("aaa/bbb11111.jpg");
		//图片类型后缀名
		attachmentEntity.setAttachmentExtension("jpg");
		//图片存放地址
		attachmentEntity.setAttachmentUri("asad/ddv/asd1111.jpg");
		//图片缩略图存放地址
		attachmentEntity.setAttachmentThumbUri("asad/ddv/asd1111.jpg");

		//平台应用编码
		platformEntity.setAppId("APPPPP001");
		//密钥
		platformEntity.setAppSecretKey("add345sdadsda21");
		//对接项目名称
		platformEntity.setProgram("日月神教项目");
		/*下面不必填*/
		
//		//企业详细地址
//		platformEntity.setAddress("XXX市CCC区BBB号");
//		//商家公司名称
//		platformEntity.setCompanyName("日月神教");
//		//邮箱
//		platformEntity.setEmail("riyueshenjiao@rr.com");
//		//联系方式
//		platformEntity.setLinkTel("13111111111");
//		//联系人
//		platformEntity.setLinkName("东方不败"); 
//		//营业执照号
//		platformEntity.setBusinessLicenseNo("PA0001");
		Map datamap = new HashMap();
//		Map datamap = Bean2Map(platformEntity);
//		Map datamap2 = Bean2Map(attachmentEntity);
//		datamap.putAll(datamap2);
		//审批编号
		datamap.put("serialNum", "DFBB001");
		//审批状态 1:审核通过，2：审核未通过
		datamap.put("status", "1");
		//审批信息
		datamap.put("auditResultMark", "ppp");
		//审批信息
		datamap.put("auditResultMark", "ppp");
		//营业执照号
		datamap.put("businessLicenseNo","PA0001");
		//平台用户名
		datamap.put("platformUserName", "dongfangbubai");

		datamap.put("optFrom", "mmec");
		System.out.println(service.platformVerify(datamap));
		transport.close();
	}
	
	public static void test_platformApply(){
		try {
			TTransport transport = new TSocket("192.168.10.108",9010);
			transport.open();
			TBinaryProtocol protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
					"UserRMIServices");
			UserRMIServices.Client service = new UserRMIServices.Client(mp);
			Map<String,String> datamap = new HashMap<String,String>();
			datamap.put("optFrom", "YUNSIGN");
			datamap.put("companyName", "东善集团");
			datamap.put("contacts", "东善桥");
			datamap.put("department", "乡长");
			datamap.put("mobile", "13111111111");
			datamap.put("email", "13111111111@qq.com");
			datamap.put("remark", "申请缔约室");

			System.out.println(service.platformApply(datamap));
			transport.close();
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void test_platformApplyQuery(){
		try {
			TTransport transport = new TSocket("192.168.10.108",9010);
			transport.open();
			TBinaryProtocol protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
					"UserRMIServices");
			UserRMIServices.Client service = new UserRMIServices.Client(mp);
			Map<String,String> datamap = new HashMap<String,String>();
			datamap.put("optFrom", "YUNSIGN");
			datamap.put("currentPage", "0");
			datamap.put("pageSize", "5");

			System.out.println(service.platformApplyQuery(datamap));
			transport.close();
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public static void test_platformQuery(){
		try {
			TTransport transport = new TSocket("192.168.10.108",9010);
			transport.open();
			TBinaryProtocol protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
					"UserRMIServices");
			UserRMIServices.Client service = new UserRMIServices.Client(mp);
			Map<String,String> datamap = new HashMap<String,String>();
			datamap.put("optFrom", "MMEC");
			datamap.put("appId", "appid001");

			System.out.println(service.platformQuery(datamap));
			transport.close();
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test_platformCallbackQuery(){
		try {
			TTransport transport = new TSocket("192.168.10.182",9010);
			transport.open();
			TBinaryProtocol protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
					"UserRMIServices");
			UserRMIServices.Client service = new UserRMIServices.Client(mp);
			Map<String,String> datamap = new HashMap<String,String>();
			datamap.put("optFrom", "MMEC");
			datamap.put("appId", "upiWn15M7y");

			System.out.println(service.platformCallbackQuery(datamap));
			transport.close();
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		//test_platformApply();
		//test_platformApplyQuery();
		test_platformCallbackQuery();
	}
}
