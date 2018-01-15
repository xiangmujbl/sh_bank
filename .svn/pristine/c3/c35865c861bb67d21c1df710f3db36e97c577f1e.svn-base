package com.mmec.centerService.feeModule.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.liuy.security.Base64;
import org.liuy.security.cert.KeyStoreSeal;
import org.springframework.beans.factory.annotation.Autowired;

import cfca.ra.common.vo.response.CertServiceResponseVO;
import cfca.x509.certificate.X509Cert;

import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.mmec.util.IndividualCertChain;
import com.mmec.util.CertUtil;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.dao.ContractPathDao;
import com.mmec.centerService.contractModule.dao.SignRecordDao;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractPathEntity;
import com.mmec.centerService.contractModule.entity.SignRecordEntity;
import com.mmec.centerService.feeModule.service.PdfService;
import com.mmec.centerService.userModule.dao.CustomInfoDao;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.css.conf.IConf;
import com.mmec.css.mmec.service.impl.CssRMIServicesImpl;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantUtil;
import com.mmec.util.GlobalData;
import com.mmec.util.SHA_MD;
import com.mmec.util.TummbPrintUtils;
import com.mmec.util.pdf.AddSignField;
import com.mmec.util.pdf.CertBean;
import com.mmec.util.pdf.Sign;

public class PdfServiceImpl implements PdfService{
	
	private static Logger log=Logger.getLogger(PdfServiceImpl.class);
	
	@Autowired
	private ContractDao contractDao;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private SignRecordDao signRecordDao;
	
	@Autowired
	private ContractPathDao contractPathDao;
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private CustomInfoDao customInfoDao;
	/**
	 * 签署业务逻辑规避
	 * @param appid
	 * @param ucid
	 * @param serialNum
	 * @param contract
	 * @param gson
	 * @return
	 */
	public ReturnData signcheck(String appid,String ucid,String serialNum,ContractEntity contract,Gson gson){
		log.info("start to check logic params");
		//合同不存在
		if(null==contract){
			return new ReturnData(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],
					ConstantUtil.CONTRACT_IS_NOT_EXISTS[2],"");
		}
		long currntime= new Date().getTime();
		Date offDate=contract.getDeadline();
		long offertime = offDate.getTime();
		//合同邀约时间过期
		if(offertime!=0){
	    	 if(currntime>offertime){	
	    		 {
	    			 return new ReturnData(ConstantUtil.CONTRACT_SOLICITATION_TIME_EXPIRED[0],
	    					 ConstantUtil.CONTRACT_SOLICITATION_TIME_EXPIRED[1],ConstantUtil.CONTRACT_SOLICITATION_TIME_EXPIRED[2],"");
	    		 }
	    	 }
		}
		int status=contract.getStatus();
		//签署完毕
		if(status==2){
			return new ReturnData(ConstantUtil.CONTRACT_HASBEEN_ALLSIGNED[0],
					ConstantUtil.CONTRACT_HASBEEN_ALLSIGNED[1],ConstantUtil.CONTRACT_HASBEEN_ALLSIGNED[2],"");
		}
		//被签署人拒绝
		if(status==3){
			return new ReturnData(ConstantUtil.CONTRACT_HASBEEN_REFUSED[0],
					ConstantUtil.CONTRACT_HASBEEN_REFUSED[1],ConstantUtil.CONTRACT_HASBEEN_REFUSED[2],"");
		}
		//被发起人撤销
		if(status==4){
			return new ReturnData(ConstantUtil.CONTRACT_HASBEEN_CANCELED[0],
					ConstantUtil.CONTRACT_HASBEEN_CANCELED[1],ConstantUtil.CONTRACT_HASBEEN_CANCELED[2],"");
		}
		//合同过期
		if(status==5){
			return new ReturnData(ConstantUtil.CONTRACT_IS_OUTOFDATE[0],
					ConstantUtil.CONTRACT_IS_OUTOFDATE[1],ConstantUtil.CONTRACT_IS_OUTOFDATE[2],"");
		}
		String otheruids=contract.getOperator();
		String [] otheruid = otheruids.split("-");
		int singercount=otheruid.length-1;
		//用户不是签署人
	    if(!com.mmec.util.StringUtil.isContain(ucid, otheruid)){
	    	return new ReturnData(ConstantUtil.USER_ISNOT_SIGNATORY[0],
	    			ConstantUtil.USER_ISNOT_SIGNATORY[1],ConstantUtil.USER_ISNOT_SIGNATORY[2],"");
	    }
		String signDataForDatabase=contract.getSignPlaintext();
		//用户已签署
		Map<String, HashMap<String,String>> mmap_database = new HashMap<String, HashMap<String,String>>();
	    if(!"".equals(signDataForDatabase))
		{
			HashMap<String, String> s = mmap_database.get(ucid);
			mmap_database = gson.fromJson(signDataForDatabase, HashMap.class);
			if(null !=mmap_database.get(ucid))
			{
				return new ReturnData(ConstantUtil.USER_HAS_SIGNED[0],ConstantUtil.USER_HAS_SIGNED[1],
						ConstantUtil.USER_HAS_SIGNED[2],"");
			}
		}
	    log.info("logic params check over");
	    return new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],
	    		ConstantUtil.RETURN_SUCC[2],String.valueOf(singercount));
	}
	
	/**
	 * 标准版签署
	 */
	public  ReturnData standardPdfSign(Map<String,String> map){
		log.info("entry method standardPdfSign,params"+map.toString());
		String serialNum=map.get("serialNum");
		String ucid=map.get("ucid");
		String appId = map.get("appId");
		Gson gson = new Gson();
		ContractEntity contract = contractDao.findContractBySerialNum(serialNum);
		ReturnData rd1=signcheck(appId, ucid, serialNum, contract, gson);
		if(!rd1.equals("0000")){
			return rd1;
		}
		int singercount=Integer.valueOf(rd1.getPojo());
	    //PDF文件签署模块
	    log.info("start to sign pdf");
		ContractPathEntity contractPath = contractPathDao.findContractPathByContractId(contract);
		String filepath=contractPath.getFilePath();
		String fieldNameNow="";
	    //如果是第一个人签署,先生成一次性添加了所有签名域的文件
	    if(filepath.indexOf("_")==-1){
	    	String signfilepath=
	    	filepath.substring(0,filepath.indexOf("."))+"signfield"+filepath.substring(filepath.indexOf("."));
	    	try {
				AddSignField.addSignFieldOnce(filepath,signfilepath, singercount);
			} catch (IOException | DocumentException e) {
				e.printStackTrace();
				log.info("生成一次性签名域文件失败");
				return new ReturnData(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],
						ConstantUtil.RETURN_SYSTEM_ERROR[2],"");
			}
	    	filepath=signfilepath;
	    	fieldNameNow="1";
	    }else{
	    	fieldNameNow=String.valueOf(
	    			Integer.valueOf(filepath.substring(filepath.indexOf("_")+1,filepath.indexOf(".")))+1);
	    }
	    String certPath=IConf.getValue("SERVERCERTPATH");//服务器证书路径
	    File certPathFile=new File(certPath);
	    //管理证书和密钥类
  		KeyStore ks = KeyStoreSeal.iniKeystore(certPath,"Mmec13572468");
  		//类别名
  		try
  		{
  		int signlevel=0;
  		int alreadysign=contract.getSignPlaintext().split(",").length;
  		if(alreadysign==(singercount-1)){
  			signlevel=3;
  		}
  		String dest="";
  		//如果用户已经签署
  		if(filepath.indexOf("_")>-1){
  			dest=filepath.substring(0,filepath.indexOf("_")+1)+String.valueOf(
  	    			Integer.valueOf(filepath.substring(filepath.indexOf("_")+1,filepath.indexOf(".")))+1)
  	    			+filepath.substring(filepath.indexOf("."));
  		}else{
  			//如果用户尚未签署
  			dest=filepath.substring(0,filepath.indexOf("."))+"_1"
  	    			+filepath.substring(filepath.indexOf("."));
  		}
  		HashMap<String,String> usermap=new HashMap<String,String>();
		List<SignRecordEntity> listSignRecord = signRecordDao.findCustomSignRecordByContractId(contract);
//		String OriginalData =  contract.getSignPlaintext();//签名原文
		//签名原文变更为获取上一个文件的hash值
		String OriginalData=SHA_MD.encodeFileSHA1(new File(contractPath.getFilePath())).toHexString();
		CertBean cb=cert(1,OriginalData,gson,serialNum,Integer.valueOf(ucid));
		PlatformEntity p=new PlatformEntity();
		p.setAppId(appId);
		IdentityEntity identityEntity =  identityDao.queryAppIdAndPlatformUserName(p,  ucid);
		//用关联查询identity
		//	Query query = entityManager.createQuery(" SELECT I　FROM IdentityEntity　I  ");
		//查询附件对原文进行sha1
		String contractinfo=cb.getTimeStamp()+" "+cb.getSignature()+" "+cb.getCertificate()+" "+OriginalData+" "+serialNum;
  		Sign.signField(filepath, signlevel, fieldNameNow, dest, cb.getChain(), cb.getKey(),DigestAlgorithms.SHA1, 
  	  			cb.getProvider().getName(), CryptoStandard.CMS,"","",contractinfo,usermap);
		Map<String,String> signData = new HashMap<String,String>();
		signData.put("sign", cb.getSignature());
		signData.put("data", OriginalData);//原文
		signData.put("cert", cb.getCertificate());
		signData.put("tsa", cb.getTimeStamp());
		Query query = entityManager.createQuery(" UPDATE SignRecordEntity d SET d.prevSha1=?,d.currentSha1=?,d.signTime=?,d.signdata=?,d.signStatus=?,d.mark=?,d.signMode=? where d.CContract=? and d.CIdentity=? ");
		query.setParameter(1, contract.getSha1());
		query.setParameter(2, OriginalData);
		query.setParameter(3,new Date());
		query.setParameter(4, signData);
		query.setParameter(5, 1);
		query.setParameter(6, "");
		query.setParameter(7, 2);//签署模式
		query.setParameter(8, contract);
		query.setParameter(9, identityEntity);
		int result = query.executeUpdate(); //影响的记录数
		if(result >0)
		{
			//签署成功
			if(null != listSignRecord && listSignRecord.size()==1)
			{
				//修改合同表状态为签署完成
			}
			else
			{
				//修改合同表状态为签署状态
			}
		}
		return new ReturnData();
  		}catch(Exception e){
  			e.printStackTrace();
  			return new ReturnData(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],
  					ConstantUtil.RETURN_SYSTEM_ERROR[2],e.toString());
  		}
	}
	
	/**
	 * 自定义版签署
	 */
	public  ReturnData customPdfSign(){
		return new ReturnData();
	}
	
	
	/**
	 * 证书获取
	 * @param certtype 1-服务器证书 /  2-事件证书测试版  
	 * @return
	 * @throws ServiceException 异常封装
	 */
	public CertBean cert(int certtype,String os,Gson gson,String serialnum,int userid) throws ServiceException{
		switch(certtype){
			case 1:
				try{
				String certPath=IConf.getValue("SERVERCERTPATH");//服务器证书路径
			    File certPathFile=new File(certPath);
		  		KeyStore ks = KeyStoreSeal.iniKeystore(certPath,"Mmec13572468");
		  		String alias = (String) ks.aliases().nextElement();
		  		PrivateKey key = (PrivateKey) ks.getKey(alias,
		  				"Mmec13572468".toCharArray());
		  		Certificate[] chain = ks.getCertificateChain(alias);
		  		Certificate c=ks.getCertificate(alias);
		  		BouncyCastleProvider provider = new BouncyCastleProvider();
		  		Security.addProvider(provider);
		  		X509Certificate xc=(X509Certificate)c;
		  		CssRMIServicesImpl cssRMIServices = new CssRMIServicesImpl();
		  		String signJsonData = "";
				ResultData resData= cssRMIServices.signService(os);
				if(resData.status!=101){
					throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
							ConstantUtil.RETURN_SYSTEM_ERROR[1]+"请求签名返回无效",ConstantUtil.RETURN_SYSTEM_ERROR[2]);
				}
				signJsonData = resData.pojo;
				Map mapData = gson.fromJson(signJsonData, HashMap.class);
				String certificate = (String)mapData.get("certificate");
				String signature = (String)mapData.get("signature");
				String certFingerprint = (String)mapData.get("certFingerprint");
				String contSerialNum = (String)mapData.get("serialNum");
				ResultData timeStampRes=cssRMIServices.getTimestampService(contSerialNum, certFingerprint);
				if(timeStampRes.status!=101){
					throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
							ConstantUtil.RETURN_SYSTEM_ERROR[1]+"请求时间戳无效",ConstantUtil.RETURN_SYSTEM_ERROR[2]);
				}
				String timeStamp = timeStampRes.pojo.replaceAll("\r\n", "");
				return new CertBean(certificate,signature,
						certFingerprint,contSerialNum,timeStamp,
						provider, key, chain);
				}catch(Exception e){
					throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
							ConstantUtil.RETURN_SYSTEM_ERROR[1]+"处理服务器证书失败",ConstantUtil.RETURN_SYSTEM_ERROR[2]);
				}
			case 2:
				try{
				StringBuffer subject=new StringBuffer();
				subject.append("CN=");
				//根据userid判断
				IdentityEntity i=identityDao.findById(userid);
				int type=i.getType();
				String usertype="";
				String name="";String num="";
				if(type==1){//个人用户
					name=i.getCCustomInfo().getUserName();
					num=i.getCCustomInfo().getIdentityCard();
					usertype="1";
				}else if(type==2){//企业用户
					name=i.getCCompanyInfo().getCompanyName();
					num=i.getCCompanyInfo().getBusinessLicenseNo();
					usertype="2";
				}else if(type==3){//平台用户
					if((null!=i.getCCustomInfo().getUserName())&&(!"".equals(i.getCCustomInfo().getUserName()))){
						name=i.getCCustomInfo().getUserName();
						num=i.getCCustomInfo().getIdentityCard();
						usertype="1";
					}else{
						if((null!=i.getCCompanyInfo().getCompanyName())&&(!"".equals(i.getCCompanyInfo().getCompanyName()))){
						name=i.getCCompanyInfo().getCompanyName();
						num=i.getCCompanyInfo().getBusinessLicenseNo();	
						usertype="2";
						}else{
							throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],
									ConstantUtil.RETURN_CUST_NOT_EXIST[1],ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
						}
					}
				}
				subject.append(name+"&"+num);
				subject.append(",OU=");
				//OU赋值其他内容
				subject.append("yunsin");
				subject.append(",O=");
				//O赋值单位名称 
				if((null!=i.getCCompanyInfo().getCompanyName())&&(!"".equals(i.getCCompanyInfo().getCompanyName()))){
					subject.append(i.getCCompanyInfo().getCompanyName());
				}else{
					subject.append("");	
				}
				//C赋值国家CN
				subject.append(",C=CN");
				String res="";
				PrivateKey key=null;
				HashMap<String, Object> kmap=new HashMap<String,Object>();
				kmap = com.mmec.util.CertUtil.genCSR(subject.toString());
				res=(String)kmap.get("code");
				key=(PrivateKey)kmap.get("key");
				HashMap<String,String> map=new HashMap<String,String>();
				//代表 证书更新并下载
				map.put("txCode", "1101");
				//证书类型 1:普通  2:高级 
				map.put("certType", "1");
				//证书类型:
				map.put("customerType", String.valueOf(usertype));
				//用户名称
				map.put("userName", name);
				if("1".equals(usertype)){
					map.put("identType", "Z");
					map.put("identNo", num);
				}else if ("2".equals(usertype)){
					map.put("identType", "Z");
					map.put("identNo", num);
				}
				//加密算法
				map.put("keyAlg", "RSA");
				//密钥长度
				map.put("keyLength", "1024");
				//机构编码 根机构
				map.put("branchCode", "678");
				//p10的值
				map.put("p10", res);
				HashMap<String,Object> camap=CertUtil.reqRA(map);
				String certinfo=((CertServiceResponseVO) camap.get("cert")).getSignatureCert();
				byte b[]=com.sun.org.apache.xml.internal.security.utils.Base64.decode(certinfo);
				X509Cert x=new X509Cert(b);
				String certFingerprintStr1 = TummbPrintUtils.getThumbprint(x);
				String signdata=com.mmec.util.RASign.getp1Sign(key,os);
				Certificate[] chain = new Certificate[3];
				CertificateFactory cf0 = CertificateFactory.getInstance("X.509");
				byte []bbb=Base64.decode(certinfo);
			    ByteArrayInputStream bais = new ByteArrayInputStream(bbb);
		        Certificate cert0= (Certificate)cf0.generateCertificate(bais);
		        String certPath=IConf.getValue("EVENTCERTPATH");
		        Certificate cert1=IndividualCertChain.getCfcaCert(certPath+"CFCA_TEST_OCA1.cer");
		        Certificate cert2=IndividualCertChain.getCfcaCert(certPath+"CFCA_TEST_OCA1.cer");
				chain[0]=cert0;
				chain[1]=cert1;
				chain[2]=cert2;
				BouncyCastleProvider provider = new BouncyCastleProvider();
				Security.addProvider(provider);
				CssRMIServicesImpl cssRMIServices = new CssRMIServicesImpl();
				String timeStamp=cssRMIServices.getTimestampService(serialnum, certFingerprintStr1).pojo;
				return new CertBean(certinfo,signdata,certFingerprintStr1,serialnum,timeStamp,
						provider,key,chain);
				}catch(Exception e){
					e.printStackTrace();
					throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
							ConstantUtil.RETURN_SYSTEM_ERROR[1]+"处理事件证书失败",ConstantUtil.RETURN_SYSTEM_ERROR[2]);
				}
			default:
				;
		}
		return null;
	}
	
	
	
	public static void main(String []args){
		String a="/home/tomcat/webapps/mmecservice/sharefile/contract/CP7242696137726598/20150724163601409_1.pdf";
		String e="/home/tomcat/webapps/mmecservice/sharefile/contract/CP7242696137726598/20150724163601409signfield.pdf";
		String d="/home/tomcat/webapps/mmecservice/sharefile/contract/CP7242696137726598/20150724163601409.pdf";
		String b=a.substring(0,a.lastIndexOf("."));
		String c=a.substring(a.lastIndexOf("."));
//		System.out.println(b);
//		System.out.println(c);
		String filepath=e;
		String signfilepath=filepath.substring(0,filepath.indexOf("."))+"signfield"+filepath.substring(filepath.indexOf("."));
//		System.out.println(signfilepath);
//		System.out.println(String.valueOf(
//    			Integer.valueOf(filepath.substring(filepath.indexOf("_")+1,filepath.indexOf(".")))+1));
		
		//原文件
//		System.out.println(filepath.substring(0,filepath.indexOf("_")+1)+String.valueOf(
//    			Integer.valueOf(filepath.substring(filepath.indexOf("_")+1,filepath.indexOf(".")))+1)
//    			+filepath.substring(filepath.indexOf(".")));
		//一次性签名域文件
//		System.out.println(filepath.substring(0,filepath.indexOf("."))+"_1"
//    			+filepath.substring(filepath.indexOf(".")));
		System.out.println(System.currentTimeMillis());
	}
}
