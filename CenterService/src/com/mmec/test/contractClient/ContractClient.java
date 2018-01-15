package com.mmec.test.contractClient;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;

import com.google.gson.Gson;
import com.mmec.thrift.service.ContractRMIServices.Client;
import com.mmec.util.SHA_MD;
import com.mmec.thrift.service.ReturnData;

public class ContractClient{
	private Logger log = Logger.getLogger(ContractClient.class);
	private TTransport transport = null;
	private TProtocol protocol = null;
	private Client client = null;
	
	public void init()
	{
		try {
			transport = new TSocket("192.168.10.61", 9006);
			transport.open();
			protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"ContractRMIServices");			
			client = new Client(mp);
		} catch (TTransportException e) {
			e.printStackTrace();
			log.error("打开transport.open()失败,请检查是否服务开启!", e);
		}
	}
	/**
	 * 关闭资源
	 */
	public void closeRes() {
		if (transport.isOpen()) {
			transport.close();
		}
	}
	@Test
	public void testCreateContract()
	{		
		init();
		ReturnData rd = null;
		Map<String,String> datamap = new HashMap<String,String>();
//		datamap.put("optFrom", "YUNSIGN");
		datamap.put("optFrom", "MMEC");
//		datamap.put("appId", "fD6PWLbK3m");
//		datamap.put("appId", "78f8RlcB2o");
		datamap.put("appId", "Udz2ILyzx7");
		datamap.put("customId", "ucid002,ucid003,ucid004");
//		datamap.put("customId", "AAA001,AAA002");
//		datamap.put("customId", "YUNSIGN_17000000011@163.com_e,YUNSIGN_17000000027_p");
//		datamap.put("price", "12.3");
		datamap.put("tempNumber", "MB1456369684531");
//		datamap.put("tempNumber", "MB1460959243437");
		datamap.put("chargeType", "1");
//		String data = "{\"PayType\":\"1\",\"ContractNo\":\"1436771117958\",\"ewm\":\"中国云签\",\"PickUpAddress\":\"test\",\"Sale_MBR_SPE_ACCT_NO\":null,\"Buy_MBR_SPE_ACCT_NO\":\"null\",\"fkxs\":\"7\",\"cdhp\":\"90\",\"cdhp1\":\"90\",\"tempfkxs\":\"中国银行\"}";
//		String data = "{\"checkbox1\":\"checked\",\"checkbox2\":\"unchecked\",\"loops8\":\"*\"}";
//		String data = "{\"loops1\":\"loops1\",\"loops2\":\"loops2\",\"loops8\":\"*\"}";
//		String data = "{\"loops10\":\"loops10\"}";
//		String data = "{\"loops1\":\"loops1\", \"loops2\":\"loops2\", \"loops3\":\"loops3\", \"loops4\":\"loops4\", \"loops5\":\"loops5\", \"loops6\":\"loops6\", \"loops7\":\"loops7\", \"loops8\":\"loops8\", \"loops9\":\"loops9\", \"loops10\":\"loops10\", \"loops11\":\"loops11\", \"loops12\":\"loops12\", \"loops13\":\"loops13\", \"loops14\":\"loops14\", \"loops15\":\"loops15\", \"loops16\":\"loops16\", \"loops17\":\"loops17\", \"loops18\":\"loops18\", \"loops19\":\"loops19\", \"loops20\":\"loops20\",\"loops21\":\"loops21\", \"loops22\":\"loops22\", \"loops23\":\"loops23\", \"loops24\":\"loops24\", \"loops25\":\"loops25\", \"loops26\":\"loops26\", \"loops27\":\"loops27\", \"loops28\":\"loops28\", \"loops29\":\"loops29\", \"loops30\":\"loops30\", \"loops31\":\"loops31\", \"loops32\":\"loops32\", \"loops33\":\"loops33\", \"loops34\":\"loops34\", \"loops35\":\"loops35\", \"loops36\":\"loops36\", \"loops37\":\"loops37\", \"loops38\":\"loops38\", \"loops39\":\"loops39\", \"loops40\":\"loops40\",\"loops41\":\"loops41\", \"loops42\":\"loops42\", \"loops43\":\"loops43\", \"loops44\":\"loops44\", \"loops45\":\"loops45\",\"loops46\":\"loops46\", \"loops47\":\"loops47\", \"loops48\":\"loops48\", \"loops49\":\"loops49\", \"loops50\":\"loops50\", \"loops51\":\"loops51\", \"loops52\":\"loops52\", \"loops53\":\"loops53\", \"loops54\":\"loops54\", \"loops55\":\"loops55\", \"loops56\":\"loops56\", \"loops57\":\"loops57\", \"loops58\":\"loops58\", \"loops59\":\"loops59\", \"loops60\":\"loops60\", \"loops61\":\"loops61\", \"loops62\":\"loops62\", \"loops63\":\"loops63\", \"loops64\":\"loops64\", \"loops65\":\"loops65\", \"loops66\":\"loops66\", \"loops67\":\"loops67\", \"loops68\":\"loops68\", \"loops69\":\"loops69\", \"loops70\":\"loops70\", \"loops71\":\"loops71\", \"loops72\":\"loops72\", \"loops73\":\"loops73\", \"loops74\":\"loops74\", \"loops75\":\"loops75\", \"loops76\":\"loops76\", \"loops77\":\"loops77\", \"loops78\":\"loops78\", \"loops79\":\"loops79\", \"loops80\":\"loops80\",\"loops81\":\"loops81\", \"loops82\":\"loops82\", \"loops83\":\"loops83\", \"loops84\":\"loops84\", \"loops85\":\"loops85\", \"loops86\":\"loops86\", \"loops87\":\"loops87\", \"loops88\":\"loops88\", \"loops89\":\"loops89\", \"loops90\":\"loops90\", \"loops91\":\"loops91\", \"loops92\":\"loops92\", \"loops93\":\"loops93\", \"loops94\":\"loops94\", \"loops95\":\"loops95\", \"loops96\":\"loops96\", \"loops97\":\"loops97\", \"loops98\":\"loops98\", \"loops99\":\"loops99\", \"loops100\":\"loops100\",\"loops101\":\"loops101\", \"loops102\":\"loops102\", \"loops103\":\"loops103\", \"loops104\":\"loops104\", \"loops105\":\"loops105\",\"loops106\":\"loops106\", \"loops107\":\"loops107\", \"loops108\":\"loops108\", \"loops109\":\"loops109\", \"loops110\":\"loops110\", \"loops111\":\"loops111\", \"loops112\":\"loops112\", \"loops113\":\"loops113\", \"loops114\":\"loops114\", \"loops115\":\"loops115\", \"loops116\":\"loops116\", \"loops117\":\"loops117\", \"loops118\":\"loops118\", \"loops119\":\"loops119\", \"loops120\":\"loops120\", \"loops121\":\"loops121\", \"loops122\":\"loops122\", \"loops123\":\"loops123\", \"loops124\":\"loops124\", \"loops125\":\"loops125\", \"loops126\":\"loops126\", \"loops127\":\"loops127\", \"loops128\":\"loops128\", \"loops129\":\"loops129\", \"loops130\":\"loops130\", \"loops131\":\"loops131\", \"loops132\":\"loops132\", \"loops133\":\"loops133\", \"loops134\":\"loops134\", \"loops135\":\"loops135\", \"loops136\":\"loops136\", \"loops137\":\"loops137\", \"loops138\":\"loops138\", \"loops139\":\"loops139\", \"loops140\":\"loops140\",\"loops141\":\"loops141\", \"loops142\":\"loops142\", \"loops143\":\"loops143\", \"loops144\":\"loops144\", \"loops145\":\"loops145\",\"loops146\":\"loops146\", \"loops147\":\"loops147\", \"loops148\":\"loops148\", \"loops149\":\"loops149\", \"loops150\":\"loops150\", \"loops151\":\"loops151\", \"loops152\":\"loops152\", \"loops153\":\"loops153\", \"loops154\":\"loops154\", \"loops155\":\"loops155\", \"loops156\":\"loops156\", \"loops157\":\"loops157\", \"loops158\":\"loops158\", \"loops159\":\"loops159\", \"loops160\":\"loops160\", \"loops161\":\"loops61\", \"loops162\":\"loops162\", \"loops163\":\"loops163\", \"loops164\":\"loops164\", \"loops165\":\"loops165\", \"loops166\":\"loops166\", \"loops167\":\"loops167\", \"loops168\":\"loops168\", \"loops169\":\"loops169\", \"loops170\":\"loops170\"}";
//		String data = "{\"checkbox1\":\"checked\",\"checkbox2\":\"unchecked\",\"checkbox3\":\"checked\",\"checkbox4\":\"unchecked\",\"checkbox5\":\"unchecked\",\"checkbox6\":\"unchecked\",\"checkbox7\":\"unchecked\",\"checkbox8\":\"unchecked\",checkbox9:\"checked\",\"checkbox10\":\"unchecked\",\"checkbox11\":\"checked\",\"checkbox12\":\"unchecked\",\"checkbox13\":\"unchecked\",\"checkbox14\":\"unchecked\",\"checkbox15\":\"unchecked\",\"checkbox16\":\"checked\",\"checkbox17\":\"unchecked\",\"loops1\":\"loops1\", \"loops2\":\"loops2\", \"loops3\":\"loops3\", \"loops4\":\"loops4\", \"loops5\":\"loops5\", \"loops6\":\"loops6\", \"loops7\":\"loops7\", \"loops8\":\"loops8\", \"loops9\":\"loops9\", \"loops10\":\"loops10\", \"loops11\":\"loops11\", \"loops12\":\"loops12\", \"loops13\":\"loops13\", \"loops14\":\"loops14\", \"loops15\":\"loops15\", \"loops16\":\"loops16\", \"loops17\":\"loops17\", \"loops18\":\"loops18\", \"loops19\":\"loops19\", \"loops20\":\"loops20\",\"loops21\":\"loops21\", \"loops22\":\"loops22\", \"loops23\":\"loops23\", \"loops24\":\"loops24\", \"loops25\":\"loops25\", \"loops26\":\"loops26\", \"loops27\":\"loops27\", \"loops28\":\"loops28\", \"loops29\":\"loops29\", \"loops30\":\"loops30\", \"loops31\":\"loops31\", \"loops32\":\"loops32\", \"loops33\":\"loops33\", \"loops34\":\"loops34\", \"loops35\":\"loops35\", \"loops36\":\"loops36\", \"loops37\":\"loops37\", \"loops38\":\"loops38\", \"loops39\":\"loops39\", \"loops40\":\"loops40\",\"loops41\":\"loops41\", \"loops42\":\"loops42\", \"loops43\":\"loops43\", \"loops44\":\"loops44\", \"loops45\":\"loops45\",\"loops46\":\"loops46\", \"loops47\":\"loops47\", \"loops48\":\"loops48\", \"loops49\":\"loops49\", \"loops50\":\"loops50\", \"loops51\":\"loops51\", \"loops52\":\"loops52\", \"loops53\":\"loops53\", \"loops54\":\"loops54\", \"loops55\":\"loops55\", \"loops56\":\"loops56\", \"loops57\":\"loops57\", \"loops58\":\"loops58\", \"loops59\":\"loops59\", \"loops60\":\"loops60\", \"loops61\":\"loops61\", \"loops62\":\"loops62\", \"loops63\":\"loops63\", \"loops64\":\"loops64\", \"loops65\":\"loops65\", \"loops66\":\"loops66\", \"loops67\":\"loops67\", \"loops68\":\"loops68\", \"loops69\":\"loops69\", \"loops70\":\"loops70\", \"loops71\":\"loops71\", \"loops72\":\"loops72\", \"loops73\":\"loops73\", \"loops74\":\"loops74\", \"loops75\":\"loops75\", \"loops76\":\"loops76\", \"loops77\":\"loops77\", \"loops78\":\"loops78\", \"loops79\":\"loops79\", \"loops80\":\"loops80\",\"loops81\":\"loops81\", \"loops82\":\"loops82\", \"loops83\":\"loops83\", \"loops84\":\"loops84\", \"loops85\":\"loops85\", \"loops86\":\"loops86\", \"loops87\":\"loops87\", \"loops88\":\"loops88\", \"loops89\":\"loops89\", \"loops90\":\"loops90\", \"loops91\":\"loops91\", \"loops92\":\"loops92\", \"loops93\":\"loops93\", \"loops94\":\"loops94\", \"loops95\":\"loops95\", \"loops96\":\"loops96\", \"loops97\":\"loops97\", \"loops98\":\"loops98\", \"loops99\":\"loops99\", \"loops100\":\"loops100\",\"loops101\":\"loops101\", \"loops102\":\"loops102\", \"loops103\":\"loops103\", \"loops104\":\"loops104\", \"loops105\":\"loops105\",\"loops106\":\"loops106\", \"loops107\":\"loops107\", \"loops108\":\"loops108\", \"loops109\":\"loops109\", \"loops110\":\"loops110\", \"loops111\":\"loops111\", \"loops112\":\"loops112\", \"loops113\":\"loops113\", \"loops114\":\"loops114\", \"loops115\":\"loops115\", \"loops116\":\"loops116\", \"loops117\":\"loops117\", \"loops118\":\"loops118\", \"loops119\":\"loops119\", \"loops120\":\"loops120\", \"loops121\":\"loops121\", \"loops122\":\"loops122\", \"loops123\":\"loops123\", \"loops124\":\"loops124\", \"loops125\":\"loops125\", \"loops126\":\"loops126\", \"loops127\":\"loops127\", \"loops128\":\"loops128\", \"loops129\":\"loops129\", \"loops130\":\"loops130\", \"loops131\":\"loops131\", \"loops132\":\"loops132\", \"loops133\":\"loops133\", \"loops134\":\"loops134\", \"loops135\":\"loops135\", \"loops136\":\"loops136\", \"loops137\":\"loops137\", \"loops138\":\"loops138\", \"loops139\":\"loops139\", \"loops140\":\"loops140\",\"loops141\":\"loops141\", \"loops142\":\"loops142\", \"loops143\":\"loops143\", \"loops144\":\"loops144\", \"loops145\":\"loops145\",\"loops146\":\"loops146\", \"loops147\":\"loops147\", \"loops148\":\"loops148\", \"loops149\":\"loops149\", \"loops150\":\"loops150\", \"loops151\":\"loops151\", \"loops152\":\"loops152\", \"loops153\":\"loops153\"}";
		String data = "{\"loops5\":\"loops5值\",\"loops1\":\"loops1值\",\"loops2\":\"loops2值\",\"loops3\":\"<input type='radio'>向仲裁机构申请仲裁。<input type='radio'checked/>向成都市锦江区人民法院提起诉讼。\"}";
		datamap.put("tempData", data);
		datamap.put("ucid", "ucid003");
		datamap.put("offerTime", "2017-02-16 13:40:10");
		datamap.put("startTime", "2016-01-01 10:10:10");
		datamap.put("endTime", "2017-01-01 10:10:10");
		datamap.put("title", "test");
		datamap.put("pname", "test");
//		String contractFile =  "{\"fileName\":\"201603241838548842sc32\",\"filePath\":\"E:/office/201603241838548842sc32.jpeg\"}";
//		String contractFile =  "{\"fileName\":\"MVC_sign\",\"filePath\":\"E:/office/MVC_sign.docx\",\"attOriginalName\":\"MVC_sign.docx\"}";//StringUtil.nullToString(datamap.get("contract"));
//		String attachmentFile = "[{\"attName\":\"20160518193651825\",\"attPath\":\"/sharefile/mmec_center_3/contract/20160518193651825.html\",\"attOriginalName\":\"20160518193651825.html\"},{\"attName\":\"20160518195149707\",\"attPath\":\"/sharefile/mmec_center_3/contract/20160518195149707.html\",\"attOriginalName\":\"20160518195149707.html\"},{\"attName\":\"20160518200932463\",\"attPath\":\"/sharefile/mmec_center_3/contract/20160518200932463.html\",\"attOriginalName\":\"20160518200932463.html\"},{\"attName\":\"20160518200405138\",\"attPath\":\"/sharefile/mmec_center_3/contract/20160518200405138.doc\",\"attOriginalName\":\"20160518200405138.doc\"},{\"attName\":\"20160518200339367\",\"attPath\":\"/sharefile/mmec_center_3/contract/20160518200339367.doc\",\"attOriginalName\":\"20160518200339367.doc\"}]";//StringUtil.nullToString(datamap.get("attachment"));
//		String contractFile =  "{\"fileName\":\"20160518111631734\",\"filePath\":\"/sharefile/mmec_center_3/contract/20160518111631734.doc\",\"attOriginalName\":\"20160518111631734.doc\"}";
		String attachmentFile =  "[{\"attName\":\"MVC_sign\",\"attPath\":\"E:/office/MVC_sign.docx\",\"attOriginalName\":\"MVC_sign.docx\"},{\"attName\":\"att1\",\"attPath\":\"E:/office/att1.docx\",\"attOriginalName\":\"att1.docx\"},{\"attName\":\"att3\",\"attPath\":\"E:/office/att3.pdf\",\"attOriginalName\":\"att3.pdf\"}]";
		
		String contractFile =  "{\"fileName\":\"MVC_sign\",\"filePath\":\"E:/office/MVC_sign.docx\"}";//StringUtil.nullToString(datamap.get("contract"));
//		String attachmentFile = "[{\"attName\":\"test\",\"attPath\":\"E:/office/201603241838548842sc32.jpeg\"},{\"attName\":\"014E24A77643\",\"attPath\":\"E:/office/014E24A77643.jpg\"}]";//StringUtil.nullToString(datamap.get("attachment"));
	
//		datamap.put("contractFile", contractFile);
		datamap.put("attachmentFile", attachmentFile);
//		datamap.put("feeType", "1");
//		datamap.put("feeCount", "1");
		
//		String specialCharacterNumber = "{\"userId001\":\"1,3,5\",\"userId002\":\"2,4,6\"}";
		//[{"ucid":"userId001","position":"1,3","signUiType":""},{"ucid":"userId002","position":"2","signUiType":""}]
//		String specialCharacterNumber = "[{\"ucid\":\"userId001\",\"position\":\"1,3\",\"signUiType\":\"\"},{\"ucid\":\"userId002\",\"position\":\"2\",\"signUiType\":\"\"}]";
		
//		datamap.put("specialCharacterNumber", specialCharacterNumber);
//		datamap.put("specialCharacter", "*");
//		datamap.put("character", "*");
//		datamap.put("feeType", "2");
//		datamap.put("feeCount", "3");
		
		String orderid = System.currentTimeMillis()+"";//"test20160115009";
		System.out.println(orderid);
		datamap.put("orderId", orderid);
//		datamap.put("orderId", "1462275959169");	
		try {			
			rd = client.createContract(datamap);
//			rd = client.internetFinanceCreate(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}finally{
			closeRes();
		}
	}
	@Test
	public void testServiceSign()
	{
		init();
		Map<String,String> datamap = new HashMap<String,String>();
		datamap.put("orderId", "1466413865882");
		datamap.put("ucid", "ucid002");
		datamap.put("authorUserId", "ucid004");
		datamap.put("appId", "Udz2ILyzx7");
//		String imgData = "{\"length\":2,\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":2684,\"x\":139,\"sw\":22,\"sh\":144,\"snw\":22,\"snh\":144,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjIiIGhlaWdodD0iMTQ0Ij48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDIxIDEgYyAwIDAuMzUgMC42MyAxMy42OCAwIDIwIGMgLTAuMzMgMy4zIC0yLjM1IDYuNTcgLTMgMTAgYyAtMi4wMyAxMC42NSAtMy42OCAyMS4wMyAtNSAzMiBjIC0xLjA3IDguODQgLTAuNjkgMTcuMjggLTIgMjYgYyAtMi43NSAxOC4zNyAtMTAgNTQgLTEwIDU0Ii8+PC9zdmc+\"},\"1\":{\"y\":2585,\"x\":431,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjA5IiBoZWlnaHQ9IjE0MCI+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAxIDM1IGMgMC4zMiAtMC4xNiAxMS43IC02LjE4IDE4IC05IGMgMTMuNjEgLTYuMSAyNy4wNiAtMTIuMzQgNDAgLTE3IGMgMi45OSAtMS4wOCA2Ljg5IC0wLjIyIDEwIC0xIGMgMy4zMyAtMC44MyA2LjYxIC0zLjMgMTAgLTQgYyA3LjYyIC0xLjU3IDE5LjI3IC0zLjU1IDI0IC0zIGMgMS4yNCAwLjE0IDEuNiAzLjk1IDIgNiBjIDEuMjYgNi41NyAyLjU4IDEzLjI3IDMgMjAgYyAwLjU4IDkuMzIgMS4yOCAxOS40IDAgMjggYyAtMC45MyA2LjIyIC00LjE3IDEzLjE0IC03IDE5IGMgLTEuNyAzLjUzIC01LjEzIDYuNzIgLTcgMTAgYyAtMC42MyAxLjExIC0wLjM5IDMuMDggLTEgNCBjIC0wLjU0IDAuOCAtMi4zMyAxLjEyIC0zIDIgYyAtMy4zNSA0LjM4IC02LjU0IDEwLjU2IC0xMCAxNSBjIC0wLjk2IDEuMjMgLTIuOSAxLjkgLTQgMyBjIC0wLjggMC44IC0xLjE1IDIuMzIgLTIgMyBjIC0yLjIzIDEuNzggLTUuNzcgMy4yMiAtOCA1IGMgLTAuODUgMC42OCAtMS4xOSAyLjE5IC0yIDMgYyAtMi4wOCAyLjA4IC00LjY4IDQuNTIgLTcgNiBjIC0xLjA0IDAuNjYgLTIuODkgMC4zNyAtNCAxIGMgLTMuMjggMS44NyAtMTAuOTUgNi42OCAtMTAgNyBjIDMuODIgMS4yNyAzMy4xNCA0LjYxIDQ5IDYgYyAyLjU5IDAuMjMgNS4yOSAtMC45IDggLTEgYyA2LjM4IC0wLjI0IDEyLjcgMC43NiAxOSAwIGMgMjkuODEgLTMuNTkgODkgLTEzIDg5IC0xMyIvPjwvc3ZnPg==\"}}}";
//		datamap.put("imageData", imageData);
		datamap.put("optFrom", "MMEC");
		datamap.put("signMode", "1");
		datamap.put("msgCode", "123456");
//		String imgData = "{\"nw\":1190,\"nh\":1684,\"w\":978,\"h\":1384,\"data\":{\"0\":{\"y\":1838,\"x\":308,\"sw\":345,\"sh\":345,\"snw\":345,\"snh\":345,\"img\":\"E:/office/014E24567E81.jpg\"}}}";
//		String imgData = "{\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":139,\"x\":58,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"E:/office/014E24567E81.jpg\"}}}";
//		String imgData = "{\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":139,\"x\":58,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"E:/office/014E24567E81.jpg\"}}}";
//		String imgData = "{\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":2584,\"x\":139,\"sw\":22,\"sh\":144,\"snw\":22,\"snh\":144,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjIiIGhlaWdodD0iMTQ0Ij48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDIxIDEgYyAwIDAuMzUgMC42MyAxMy42OCAwIDIwIGMgLTAuMzMgMy4zIC0yLjM1IDYuNTcgLTMgMTAgYyAtMi4wMyAxMC42NSAtMy42OCAyMS4wMyAtNSAzMiBjIC0xLjA3IDguODQgLTAuNjkgMTcuMjggLTIgMjYgYyAtMi43NSAxOC4zNyAtMTAgNTQgLTEwIDU0Ii8+PC9zdmc+\"},\"1\":{\"y\":2585,\"x\":431,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjA5IiBoZWlnaHQ9IjE0MCI+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAxIDM1IGMgMC4zMiAtMC4xNiAxMS43IC02LjE4IDE4IC05IGMgMTMuNjEgLTYuMSAyNy4wNiAtMTIuMzQgNDAgLTE3IGMgMi45OSAtMS4wOCA2Ljg5IC0wLjIyIDEwIC0xIGMgMy4zMyAtMC44MyA2LjYxIC0zLjMgMTAgLTQgYyA3LjYyIC0xLjU3IDE5LjI3IC0zLjU1IDI0IC0zIGMgMS4yNCAwLjE0IDEuNiAzLjk1IDIgNiBjIDEuMjYgNi41NyAyLjU4IDEzLjI3IDMgMjAgYyAwLjU4IDkuMzIgMS4yOCAxOS40IDAgMjggYyAtMC45MyA2LjIyIC00LjE3IDEzLjE0IC03IDE5IGMgLTEuNyAzLjUzIC01LjEzIDYuNzIgLTcgMTAgYyAtMC42MyAxLjExIC0wLjM5IDMuMDggLTEgNCBjIC0wLjU0IDAuOCAtMi4zMyAxLjEyIC0zIDIgYyAtMy4zNSA0LjM4IC02LjU0IDEwLjU2IC0xMCAxNSBjIC0wLjk2IDEuMjMgLTIuOSAxLjkgLTQgMyBjIC0wLjggMC44IC0xLjE1IDIuMzIgLTIgMyBjIC0yLjIzIDEuNzggLTUuNzcgMy4yMiAtOCA1IGMgLTAuODUgMC42OCAtMS4xOSAyLjE5IC0yIDMgYyAtMi4wOCAyLjA4IC00LjY4IDQuNTIgLTcgNiBjIC0xLjA0IDAuNjYgLTIuODkgMC4zNyAtNCAxIGMgLTMuMjggMS44NyAtMTAuOTUgNi42OCAtMTAgNyBjIDMuODIgMS4yNyAzMy4xNCA0LjYxIDQ5IDYgYyAyLjU5IDAuMjMgNS4yOSAtMC45IDggLTEgYyA2LjM4IC0wLjI0IDEyLjcgMC43NiAxOSAwIGMgMjkuODEgLTMuNTkgODkgLTEzIDg5IC0xMyIvPjwvc3ZnPg==\"}}}";
//    	String imgData = "{\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":2584,\"x\":139,\"sw\":22,\"sh\":144,\"snw\":22,\"snh\":144,\"img\":\"/home/core/centerService/logs/014E24567E81.jpg\"},\"1\":{\"y\":985,\"x\":431,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"/home/core/centerService/logs/014E24567E81.jpg\"}}}";
//		String imgData = "{\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":2584,\"x\":139,\"sw\":22,\"sh\":144,\"snw\":22,\"snh\":144,\"img\":\"E:/office/014E24A77643.jpg\"},\"1\":{\"y\":139,\"x\":58,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"E:/office/014E24567E81.jpg\"}}}";
		String imgData = "{\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":139,\"x\":58,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"E:/office/014E24567E81.jpg\"}}}";
		
		
		datamap.put("isAuthor", "Y");
		String sealNum = "1453453471697";
		datamap.put("sealNum", sealNum);
//		datamap.put("imageData", imgData);
		datamap.put("smsType", "sign");
//		String sealNum = "{\"name1\":\"中国云签\",\"name2\":\"19\",\"name3\":\"19\",\"name4\":\"水印测试\"}";//yunsign
//		String sealNum = "{\"gg1\":\"中国云签\",\"gg2\":\"1453453471696\"}";//yunsign
//		String sealNum = "{\"userId001\":\"1453453471696\",\"userId002\":\"14534534716967\"}";
//		String specialCharacterNumber = "{\"userId001\":\"1,3\",\"userId002\":\"2,4\"}";
//		String specialCharacterNumber = "1,2";
//		datamap.put("specialCharacterNumber",  "1,2");
//		datamap.put("specialCharacter", "*");
		datamap.put("pdfInfo", "{\"test1\":\"1453453471696\"}");
		ReturnData rd = null;
		try {
//			rd = client.signContract(datamap);
			rd = client.authoritySignContract(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}
		finally{
			closeRes();
		}
	}
	@Test
	public void testQueryPdfInfoByUserId()
	{
		init();
		Map<String,String> datamap = new HashMap<String,String>();
		datamap.put("appId", "Udz2ILyzx7");// 必填
		datamap.put("orderId", "1465804270451");// 必填1464147904136 1464153989091
		datamap.put("ucid", "ucid003");// 必填
		ReturnData rd = null;
		try {
			rd = client.queryPdfInfoByUserId(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}
		finally{
			closeRes();
		}		
	}
	@Test
	public void testAddPdfInfo()
	{
		init();
		Map<String,String> datamap = new HashMap<String,String>();
		datamap.put("orderId", "1466413793353");
		datamap.put("appId", "Udz2ILyzx7");
		String specialCharacterNumber = "[{\"userId\":\"ucid003\",\"position\":\"1,3\",\"signUiType\":\"3\"},{\"userId\":\"ucid004\",\"position\":\"2,4\",\"signUiType\":\"3\"}]";
//		String specialCharacterNumber = "[{\"userId\":\"ucid003\",\"position\":\"1\",\"signUiType\":\"1\"}]";
		
		datamap.put("specialCharacterNumber", specialCharacterNumber);
		datamap.put("specialCharacter", "*");
		ReturnData rd = null;
		try {
			rd = client.addPdfInfo(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}
		finally{
			closeRes();
		}		
	}
	
	@Test
	public void testDownloadZip()
	{
		init();
		Map<String,String> datamap = new HashMap<String,String>();
		datamap.put("orderId", "1466413865882");//1465887960131,1465178537357,1465182038500
		datamap.put("appId", "Udz2ILyzx7");
		datamap.put("ucid", "ucid003");
//		datamap.put("serialNum", "CP3242924758829189");
		ReturnData rd = null;
		try {
			rd = client.downLoadContract(datamap);
//			rd = client.pdfDownLoadContract(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}
		finally{
			closeRes();
		}
	}
	@Test
	public void testEventSign()
	{
		init();
		Map<String,String> datamap = new HashMap<String,String>();
		datamap.put("orderId", "1466156811392");
		datamap.put("ucid", "ucid003");
		datamap.put("appId", "Udz2ILyzx7");
		datamap.put("optFrom", "MMEC");
		datamap.put("signMode", "2");
		datamap.put("chargeType", "1");
		datamap.put("pdfUIType", "3");
		//{"name2":"19";"name1":"张三李四王五"}
//		String sealNum = "{\"name2\":\"1458283249050\",\"name1\":\"中国云签\"}";//yunsign
//		String sealNum = "{\"111\":\"1456476958531\"}";//yunsign
//		String sealNum = "{\"name3\":\"19\",\"name4\":\"中国云签\"}";
//		String sealNum = "{\"name5\":\"222\",\"name6\":\"54\"}";//yunsign
		String sealNum = "1453453471697";
		datamap.put("sealNum", sealNum);
//		String imageData = "{\"length\":2,\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":2684,\"x\":139,\"sw\":22,\"sh\":144,\"snw\":22,\"snh\":144,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjIiIGhlaWdodD0iMTQ0Ij48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDIxIDEgYyAwIDAuMzUgMC42MyAxMy42OCAwIDIwIGMgLTAuMzMgMy4zIC0yLjM1IDYuNTcgLTMgMTAgYyAtMi4wMyAxMC42NSAtMy42OCAyMS4wMyAtNSAzMiBjIC0xLjA3IDguODQgLTAuNjkgMTcuMjggLTIgMjYgYyAtMi43NSAxOC4zNyAtMTAgNTQgLTEwIDU0Ii8+PC9zdmc+\"},\"1\":{\"y\":2585,\"x\":431,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjA5IiBoZWlnaHQ9IjE0MCI+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAxIDM1IGMgMC4zMiAtMC4xNiAxMS43IC02LjE4IDE4IC05IGMgMTMuNjEgLTYuMSAyNy4wNiAtMTIuMzQgNDAgLTE3IGMgMi45OSAtMS4wOCA2Ljg5IC0wLjIyIDEwIC0xIGMgMy4zMyAtMC44MyA2LjYxIC0zLjMgMTAgLTQgYyA3LjYyIC0xLjU3IDE5LjI3IC0zLjU1IDI0IC0zIGMgMS4yNCAwLjE0IDEuNiAzLjk1IDIgNiBjIDEuMjYgNi41NyAyLjU4IDEzLjI3IDMgMjAgYyAwLjU4IDkuMzIgMS4yOCAxOS40IDAgMjggYyAtMC45MyA2LjIyIC00LjE3IDEzLjE0IC03IDE5IGMgLTEuNyAzLjUzIC01LjEzIDYuNzIgLTcgMTAgYyAtMC42MyAxLjExIC0wLjM5IDMuMDggLTEgNCBjIC0wLjU0IDAuOCAtMi4zMyAxLjEyIC0zIDIgYyAtMy4zNSA0LjM4IC02LjU0IDEwLjU2IC0xMCAxNSBjIC0wLjk2IDEuMjMgLTIuOSAxLjkgLTQgMyBjIC0wLjggMC44IC0xLjE1IDIuMzIgLTIgMyBjIC0yLjIzIDEuNzggLTUuNzcgMy4yMiAtOCA1IGMgLTAuODUgMC42OCAtMS4xOSAyLjE5IC0yIDMgYyAtMi4wOCAyLjA4IC00LjY4IDQuNTIgLTcgNiBjIC0xLjA0IDAuNjYgLTIuODkgMC4zNyAtNCAxIGMgLTMuMjggMS44NyAtMTAuOTUgNi42OCAtMTAgNyBjIDMuODIgMS4yNyAzMy4xNCA0LjYxIDQ5IDYgYyAyLjU5IDAuMjMgNS4yOSAtMC45IDggLTEgYyA2LjM4IC0wLjI0IDEyLjcgMC43NiAxOSAwIGMgMjkuODEgLTMuNTkgODkgLTEzIDg5IC0xMyIvPjwvc3ZnPg==\"}}}";
//		datamap.put("imageData", imageData);
		ReturnData rd = null;
		try {
			rd = client.signContract(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}
		finally{
			closeRes();
		}
	}
	
	@Test
	public void testGetContractList()
	{
		long t1 = System.currentTimeMillis();
		init();
		Map<String,String> datamap = new HashMap<String,String>();
//		datamap.put("userId", "300007");
//		datamap.put("appId", "Udz2ILyzx7");78f8RlcB2o
		datamap.put("userId", "105");
		datamap.put("appId", "78f8RlcB2o");
		datamap.put("countsPerPage", "20");
		datamap.put("currPage", "3");
//		datamap.put("isDelete", "0");
//		datamap.put("title", "te");
//		datamap.put("status", "5");
//		datamap.put("startTime", "2014-12-01 14:54:44");
//		datamap.put("endTime", "2015-12-03 14:54:45");
		ReturnData rd = null;
		try {
			rd = client.getContractList(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}
		finally{
			closeRes();
		}
		long t2 = System.currentTimeMillis();
		System.out.println(t2-t1);
	}
	@Test
	public void testInternetFinanceGetContractList()
	{
		init();
		Map<String,String> datamap = new HashMap<String,String>();
		datamap.put("userId", "5");
		datamap.put("appId", "appid001");
		datamap.put("countsPerPage", "20");
		datamap.put("currPage", "1");
		datamap.put("isDelete", "0");
		datamap.put("title", "te");
		datamap.put("status", "0");
		datamap.put("optFrom", "2");
		datamap.put("appName", "testqqq");
		datamap.put("startTime", "2015-12-01 14:54:44");
		datamap.put("endTime", "2015-12-03 14:54:45");
		ReturnData rd = null;
		try {
			rd = client.getInternetFinanceContractList(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}
		finally{
			closeRes();
		}
	}
	/**
	 * 
	 */
	@Test
	public void testQueryContract()
	{
		init();
		Map<String,String> datamap = new HashMap<String,String>();
		
		datamap.put("orderId", "1464326439126");
//		datamap.put("serialNum", "CP5104819462848676");
		datamap.put("appId", "Udz2ILyzx7");
		datamap.put("ucid", "ucid002");
//		datamap.put("kind", "财务?");
		
		
//		datamap.put("orderId", "1458090652204");
//		datamap.put("appId", "78f8RlcB2o");
//		datamap.put("ucid", "320106196901262434");
		
		
		ReturnData rd = null;
		try {
			rd = client.queryContract(datamap);
//			rd = client.internetFinanceQueryContract(datamap);
//			rd = client.queryYusignTemplateByKind(datamap);
//			rd = client.findContract(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}
		finally{
			closeRes();
		}
	}
	@Test
	public void testModityContractStatus()
	{
		init();
		Map<String,String> datamap = new HashMap<String,String>();
//		datamap.put("orderId", "1460515883216");
//		datamap.put("appId", "sGH46CDJzv");
//		datamap.put("ucid", "userId001");
		
		datamap.put("orderId", "test0411001ggr");
		datamap.put("appId", "Udz2ILyzx7");
		datamap.put("ucid", "wyw333");
		
		ReturnData rd = null;
		try {
			rd = client.modifyContractStatus(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}
		finally{
			closeRes();
		}
	}
	@Test
	public void testDeleteContract()
	{
		init();
		Map<String,String> datamap = new HashMap<String,String>();
		datamap.put("orderId", "1453189840283");
		datamap.put("appId", "appid001");
		datamap.put("ucid", "uid001");
		datamap.put("isDelete", "0");
		ReturnData rd = null;
		try {
			rd = client.deleteContract(datamap);
			System.out.println(rd);
		} catch (TException e) {
			e.printStackTrace();
		}
		finally{
			closeRes();
		}		
	}
	
	public static void main(String[] args){
		/*
		ContractClient c1 = new ContractClient("ContractClient1");
		ContractClient c2 = new ContractClient("ContractClient2");
		ContractClient c3 = new ContractClient("ContractClient3");
		ContractClient c4 = new ContractClient("ContractClient4");
		ContractClient c5 = new ContractClient("ContractClient5");
		testCtrateContract();
		Thread t1 = new Thread(c1);
		Thread t2 = new Thread(c2);
		Thread t3 = new Thread(c3);
		Thread t4 = new Thread(c4);
		Thread t5 = new Thread(c5);
		
		Thread t6 = new Thread(c1);
		Thread t7 = new Thread(c2);
		Thread t8 = new Thread(c3);
		Thread t9 = new Thread(c4);
		Thread t10 = new Thread(c5);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		t9.start();
		t10.start();
		*/
//		File f = new File("F:/office/MVC.docx");
//		System.out.println(f.getParent());
//		BigDecimal.
//		BigDecimal d = BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble("12.31234"))));
//		String s = new DecimalFormat("0.00").format("12.3");
//		System.out.println(d);
//		String s = "name1";
//		System.out.println(s.split(",").length);
//		System.out.println(s.split(",")[0]);
		String str="F:/文件夹名1/文件夹名2/文件夹名3";
		
//		System.out.println(str.substring(0, str.lastIndexOf("/")));
//		
//		String file = "D:\\My Documents\\My Pictures\\abc.jpg";
//		File f = new File(file);
//	    String[] s = file.split("\\\\");
//	    System.out.println(s[s.length - 1]);
//	    System.out.println(f.getAbsoluteFile());
	    try{
		InputStream is = new FileInputStream("F:/2.jpg");//通过文件名称读取
		BufferedImage buff = ImageIO.read(is);
		buff.getWidth();//得到图片的宽度
		buff.getHeight();//得到图片的高度
		System.out.println("width:"+buff.getWidth()+",height:"+buff.getHeight());
		is.close(); //关闭Stream
	    }catch (Exception e) {
			// TODO: handle exception
		}
	    
	    Map<String,String> m1 = new HashMap<String,String>();
	    m1.put("name1", "value1");
	    m1.put("name2", "value2");
	    Map<String,String> m2 = new HashMap<String,String>();
	    m2.put("name1", "value1");
	    m2.put("name2", "value2");
	    List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	    list.add(m1);
	    list.add(m2);
	    System.out.println(new Gson().toJson(list));
	    
	    String tempCustomer = "testesteee't'etett";
	    tempCustomer = tempCustomer.replaceAll("'", "");
	    System.out.println("tempCustomer==="+tempCustomer);
	    
	    String temp = "f:/of/test.doc";
	    //imgPath.add(rstPath.get(i).substring(0, rstPath.get(i).lastIndexOf(".")));
	    System.out.println(temp.substring(temp.lastIndexOf(".")+1,temp.length()));
//	    temp.substring(beginIndex, endIndex)
	    
	    String json = "{\"ucid1\":\"1,3,5\",\"ucid2\":\"2,4,6\"}";
	    Map m = new Gson().fromJson(json, Map.class);
	    System.out.println(m.get("ucid1"));
	    
	    String sha1 = SHA_MD.encodeFileSHA1(new File("E:\\download\\CP6129980565699134\\authority\\eeee.JPG")).toHexString();
	    System.out.println("sha1==="+sha1);
	}
//	@Override
//	public void run() {
//		try {
//			Thread.sleep(1000);
//			testCtrateContract();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
}