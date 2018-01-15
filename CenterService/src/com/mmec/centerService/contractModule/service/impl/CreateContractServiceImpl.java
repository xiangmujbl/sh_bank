package com.mmec.centerService.contractModule.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mmec.centerService.contractModule.dao.AuthorityContractDao;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.dao.ContractPathDao;
import com.mmec.centerService.contractModule.dao.ContractTemplateDao;
import com.mmec.centerService.contractModule.dao.PdfInfoDao;
import com.mmec.centerService.contractModule.dao.SignRecordDao;
import com.mmec.centerService.contractModule.entity.AuthorityContractEntity;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractPathEntity;
import com.mmec.centerService.contractModule.entity.ContractTemplateEntity;
import com.mmec.centerService.contractModule.entity.SignRecordEntity;
import com.mmec.centerService.contractModule.service.CreateContractService;
import com.mmec.centerService.feeModule.dao.UserServiceDao;
import com.mmec.centerService.feeModule.entity.UserServiceEntity;
import com.mmec.centerService.feeModule.service.InvoiceInfoService;
import com.mmec.centerService.feeModule.service.UserAccountService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.dao.UserAuthorityDao;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.entity.UserAuthorityEntity;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CertSignUtil;
import com.mmec.util.ConstantUtil;
import com.mmec.util.DateUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.PDFTool;
import com.mmec.util.SHA_MD;
import com.mmec.util.StringUtil;

@Service("createContractService")
public class CreateContractServiceImpl extends BaseContractImpl implements CreateContractService {
	
	private Logger log = Logger.getLogger(CreateContractServiceImpl.class);
	@Autowired
	private PlatformDao platformDao;	
	
	@Autowired
	private ContractTemplateDao contractTemplateDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractPathDao contractPathDao;
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private SignRecordDao signRecordDao;
	
	@Autowired
	private UserServiceDao userServiceDao;
	
	@Autowired
	private PdfInfoDao pdfInfoDao;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private AuthorityContractDao authorityContractDao;
	
	@Autowired
	private UserAuthorityDao userAuthorityDao;
	
	@Autowired
	private InvoiceInfoService invoiceInfoService;
	
	/**
	 * 云签基于模板创建合同
	 * @param appid 平台ID,由云签平台提供
	 * @param time 当前时间戳(精确到秒)
	 * @param sign 由appid, flow, time, key生成
	 * @param ucid 创建人用户ID,第三方业务平台用户的唯一身份
	 * @param customid 各缔约方ID,缔约多方，必须是2个人以上
	 * @param fromcustom 接入系统名称,来自的平台名称
	 * @param orderid 接入系统业务流水号,由第三方系统生成流水号;必须唯一
	 * @param title 合同标题
	 * @param template 模板名称
	 * @param offertime 签署过期时间
	 * @param pname 项目名称
	 * @param tempNumber 模板编号
	 * @param data 项目名称 Data内容为json格式；参数命名按照模板中定义的键名依次传递 例如{“title”:”中国云签”,”company”:”买卖网”}
	 */
	//TODO
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData templateCreate(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
			checkParam(datamap);
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String customId = StringUtil.nullToString(datamap.get("customId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
	//		String fromCustom = StringUtil.nullToString(datamap.get("fromCustom"));
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String offerTime = StringUtil.nullToString(datamap.get("offerTime"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String pname = StringUtil.nullToString(datamap.get("pname"));
			String contractType = StringUtil.nullToString(datamap.get("contractType"));
			//云签基于模板创建合同,直接传模板路径
			String tempPath = StringUtil.nullToString(datamap.get("tempNumber"));
			String tempData = StringUtil.nullToString(datamap.get("tempData"));
			String serialNum = StringUtil.nullToString(datamap.get("serialNum"));
			String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
			String price =  StringUtil.nullToString(datamap.get("price"));
			int opt = this.getOptForm(optFrom);
			/**
			 * payment_type 付款类型
			 * 0,代付 1,自付
			 */
			String paymentType = StringUtil.nullToString(datamap.get("paymentType"));
			byte payType = 0;
			if(!"".equals(paymentType))
			{
				payType = Byte.valueOf(paymentType);
			}
	//		log.info("创建合同入参为,appId="+appId+",customId="+customId+",ucid="+ucid+",orderId"+orderId+",title="+title+",offerTime="+offerTime+",tempNumber="+tempNumber+",tempData="+tempData+",optFrom="+optFrom);
			//判断缔约方有重复值
			String [] customIds = customId.split(",");
			if(StringUtil.checkRepeat(customIds))
			{
				log.info("缔约方有重复值");
				returnData =  new ReturnData(ConstantUtil.RETURN_CUSTOMID_HAS_DULP[0],ConstantUtil.RETURN_CUSTOMID_HAS_DULP[1], ConstantUtil.RETURN_CUSTOMID_HAS_DULP[2], "");
				return returnData;
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				returnData =  new ReturnData(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2], "");
				return returnData;
			}
			
			//判断是否重复创建合同		
			ContractEntity contractDuplicate = null;
			ContractEntity contractDuplicate_SerialNum = null;
			try{
				contractDuplicate = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
				contractDuplicate_SerialNum = contractDao.findContractBySerialNum(serialNum);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			
			if((null != contractDuplicate) || (null != contractDuplicate_SerialNum))
			{
				log.info("订单号已经存在，不能重复创建合同");
				returnData =  new ReturnData(ConstantUtil.ORDERID_HAS_EXIST[0],ConstantUtil.ORDERID_HAS_EXIST[1], ConstantUtil.ORDERID_HAS_EXIST[2], "");
				return returnData;
			}
			//判断ucid是否包含在customId
			if(!StringUtil.isContain(ucid,customIds))
			{
				returnData =  new ReturnData(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2], ucid);
				return returnData;
//				throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2]);

			}
			//查询余额
	
//			returnData = templateLoad(optFrom,serialNum,tempNumber,tempData,platformEntity,ucid,offerTime, startTime,endTime, title, orderId,customIds,customId,price,Integer.parseInt(chargeType));
			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime))
			{
				log.info("当前时间大于过期时间");
				throw new ServiceException(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2]);
			}
						
			StringBuffer signData = null;
			if(!"".equals(tempPath))
			{	
				String fileName = DateUtil.toDateYYYYMMDDHHMM1();
				if(StringUtil.isNull(serialNum))
				{
					serialNum = getOrderCode();
				}
				//保存合同表,签署信息表
				IdentityEntity identityEntity = null;
				try{
					identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
				}catch(Exception e)
				{
					log.info("根据appid和平台用户名称查询用户异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null == identityEntity)
				{
					throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],ConstantUtil.RETURN_CUST_NOT_EXIST[1],ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
				}
				
				
				String filePath = FileUtil.createContractFolder(serialNum);
				//动态添加数据到模板文件,另存为html文件
				File file = new File(filePath);
				if (!file.exists())
				{
					file.mkdirs();
				}
				if(!FileUtil.appendHtml(tempData,tempPath,filePath+fileName+".html",filePath)) 
				{
					log.info("模板数据装载错误");
					throw new ServiceException(ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[0],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[1],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[2]);
				}
//				FileUtil.appendHtml(tempData,tempPath,filePath+fileName+".html",filePath);
				//将添加数据后的html文件转为pdf
				String pdfPathfile = filePath+fileName+".pdf";
				PDFTool.htmlToPdf(filePath+fileName+".html", pdfPathfile);
//				PDFTool.htmlToPdfLibreOffice(filePath+fileName+".html", filePath);
				//对pdf文件进行sha1加密
				File f = new File(pdfPathfile);
//				File f = new File("e:/office/MVC_sign.pdf");
//				File f = new File("/home/opt/test.pdf");	
				signData = new StringBuffer("Z_1_");
				signData.append(fileName);
				signData.append(".pdf");
				signData.append("=");
				String sha1 = SHA_MD.encodeFileSHA1(f).toHexString();
				signData.append(sha1);
				signData.append("&");

				File imgFolder = new File(filePath+"img"+"/"+fileName);
				if (!imgFolder.exists())
				{
					imgFolder.mkdirs();
				}
				Map<String, String> pdfTomImgMap = new HashMap<String, String>();
				pdfTomImgMap.put("optFrom", "NULL");
				pdfTomImgMap.put("appId", platformEntity.getAppId());
				pdfTomImgMap.put("ucid", identityEntity.getAccount());
				pdfTomImgMap.put("IP", "NULL");
				PDFTool.pdfToImg(pdfPathfile, filePath+"img"+"/"+fileName,pdfTomImgMap);
				ContractEntity contract = new ContractEntity();
				contract.setSerialNum(serialNum);
				contract.setType("");
				contract.setUpdateTime(new Date());
				contract.setOptFrom((byte)opt);
				contract.setCreateTime(new Date());
				try {
					contract.setDeadline(DateUtil.stringToDate(offerTime));
				} catch (ParseException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.OFFTIME_IS_ILLEGAL[0],ConstantUtil.OFFTIME_IS_ILLEGAL[1],ConstantUtil.OFFTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
				}
				if(!"".equals(startTime))
				{
					try {
						contract.setStartTime(DateUtil.stringToDate(startTime));//合同开始时间
					} catch (ParseException e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.STARTTIME_IS_ILLEGAL[0],ConstantUtil.STARTTIME_IS_ILLEGAL[1],ConstantUtil.STARTTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
					}
				}
				contract.setStatus((byte) 0);
				contract.setIsShow((byte)1);
				contract.setMark("");
				contract.setCPlatform(platformEntity);
				contract.setSignPlaintext(signData.toString());
				contract.setFinishtime(null);//创建初始化完成时间为null
				if(platformEntity.getIsPdfSign() == 0)
				{
					contract.setIsPdfSign("N");
				}
				else
				{
					contract.setIsPdfSign("Y");
				}
				if(!"".equals(endTime))
				{
					try {
						contract.setEndTime(DateUtil.stringToDate(endTime));//合同结束时间
					} catch (ParseException e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.ENDTIME_IS_ILLEGAL[0],ConstantUtil.ENDTIME_IS_ILLEGAL[1],ConstantUtil.ENDTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
					}
				}
				contract.setCreator(identityEntity.getId());
				contract.setKeyword("");
				contract.setPaymentType(payType);
				contract.setPname(pname);
				contract.setContractType(contractType);//合同类型
				contract.setOperator("");//
				contract.setSha1(sha1);
				contract.setTitle(title);
				contract.setOrderId(orderId);
				contract.setOtheruids(customId);
				contract.setTempNumber(tempPath);//云签存在是模板路径
				if(!"".equals(price))
				{	
					contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble(price)))));
				}
				else
				{
//					contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble("0.00")))));
					contract.setPrice(BigDecimal.valueOf(Double.parseDouble("0.00")));
				}
				
				ContractEntity cn = null;
				try{
					cn = contractDao.save(contract);
				}catch(Exception e)
				{
					log.info("数据保存到合同表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				
				if(null != cn)
				{
					//保存到附件表
					ContractPathEntity contractPathEntity =  new ContractPathEntity();
					contractPathEntity.setAttName(fileName);
					contractPathEntity.setType((byte) 1);
					contractPathEntity.setFilePath(pdfPathfile);
					contractPathEntity.setOriginalFilePath(pdfPathfile);
					contractPathEntity.setExtension("pdf");
					contractPathEntity.setContractSerialNum(serialNum);
					contractPathEntity.setContractPath(filePath);
					contractPathEntity.setOriginalFile("");
					contractPathEntity.setCContract(cn);
					try{
						contractPathDao.save(contractPathEntity);
					}catch(Exception e)
					{
						log.info("数据保存到附件表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
					//签署记录表
					SignRecordEntity signRecord = new SignRecordEntity();
					boolean flag = false;
					for(int i=0;i<customIds.length;i++)
					{
						//查询c_identity表
						IdentityEntity identity = null;
						try{
							identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,customIds[i]);
						}catch(Exception e)
						{
							log.info("根据appid和ucid查询用户表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null == identity)
						{
							//有缔约方没有注册
							log.info("有缔约方没有注册");
							throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+customIds[i],ConstantUtil.RETURN_USER_NOTEXIST[2]);
						}

						signRecord.setCContract(cn);
						signRecord.setCIdentity(identity);
						signRecord.setOrignalFilename("");
						signRecord.setPrevSha1("");
						signRecord.setCurrentSha1("");
						//signRecord.setSignTime(null);
						signRecord.setSigndata("");
						signRecord.setCUkeyInfo(null);
						signRecord.setSignStatus((byte) 0);
						signRecord.setMark("");
						signRecord.setSignMode((byte) 0);
						SignRecordEntity sr = null;
						try{
							sr = signRecordDao.save(signRecord);
						}catch(Exception e)
						{
							log.info("数据保存到签署表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != sr)
						{
							flag = true;
						}
					}
					if(flag)
					{
						log.info("合同创建成功");
						String contractStr = "";
						//查找计费帐号ID
						int chargingUserId = 0; 
						if(identityEntity.getParentId() == 0)
						{
							chargingUserId = identityEntity.getId();
						}
						else
						{
							chargingUserId = identityEntity.getParentId();
						}
						if(payType == 0)//代付
						{
							charging(serialNum,chargingUserId,customIds.length);
						}
						else
						{
							//自付 只扣自己的一次
							charging(serialNum,chargingUserId,1);
						}
						try {
							contractStr = JSON.toJSONString(cn);
						} catch (Exception e) {
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2]);
						}
						returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],contractStr);
					}
					else
					{
						log.info("创建创建初始化签署表失败");
						throw new ServiceException(ConstantUtil.CREATECONTRACT_FAIL[0],ConstantUtil.CREATECONTRACT_FAIL[1],ConstantUtil.CREATECONTRACT_FAIL[2]);
					}
				}
				else{
					log.info("保存合同表失败");
					throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2]);
				}

			}
			else
			{
				log.info("模板不存在");
				throw new ServiceException(ConstantUtil.TEMPER_NOT_EXIST[0],ConstantUtil.TEMPER_NOT_EXIST[1],ConstantUtil.TEMPER_NOT_EXIST[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));			
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	/**
	 * 云签创建
	 */
	//TODO
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData yunsignCreate(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
//			checkParam(datamap);
			Gson gson = new Gson();
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String customId = StringUtil.nullToString(datamap.get("customId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String offerTime = StringUtil.nullToString(datamap.get("offerTime"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String pname = StringUtil.nullToString(datamap.get("pname"));
			String price = StringUtil.nullToString(datamap.get("price"));
			String keyword = StringUtil.nullToString(datamap.get("keyword"));
			String contractType = StringUtil.nullToString(datamap.get("contractType"));
			String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
			String contractFile = StringUtil.nullToString(datamap.get("contractFile"));
			String attachmentFile = StringUtil.nullToString(datamap.get("attachmentFile"));
			/**
			 * payment_type 付款类型
			 * 0,代付 1,自付
			 */
			String paymentType = StringUtil.nullToString(datamap.get("paymentType"));
			byte payType = 0;
			if(!"".equals(paymentType))
			{
				payType = Byte.valueOf(paymentType);
			}
	//		String contractFile =  "{\"fileName\":\"test\",\"filePath\":\"F:/office/test.doc\"}";//StringUtil.nullToString(datamap.get("contract"));
	//		String attachmentFile = "[{\"attName\":\"att1\",\"attPath\":\"F:/office/att1.docx\"},{\"attName\":\"att2\",\"attPath\":\"F:/office/att2.docx\"},{\"attName\":\"att3\",\"attPath\":\"F:/office/att3.pdf\"}]";//StringUtil.nullToString(datamap.get("attachment"));
		
			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime))
			{
				log.info("当前时间大于过期时间");
				throw new ServiceException(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2]);
			}
	//		String contractFile =  "{\"fileName\":\"test\",\"filePath\":\"/home/core/centerService/test.doc\"}";//StringUtil.nullToString(datamap.get("contract"));
	//		String attachmentFile = "[{\"attName\":\"att1\",\"attPath\":\"/home/core/centerService/att1.docx\"},{\"attName\":\"att2\",\"attPath\":\"/home/core/centerService/att2.docx\"},{\"attName\":\"att3\",\"attPath\":\"/home/core/centerService/att3.pdf\"}]";//StringUtil.nullToString(datamap.get("attachment"));
			String otheruids = "";
			String parentContractId = StringUtil.nullToString(datamap.get("parentorderid"));
	
	//		log.info("创建合同入参为,appId="+appId+",customId="+customId+",ucid="+ucid+",orderId"+orderId+",title="+title+",offerTime="+offerTime);
			//判断缔约方有重复值
			String [] customIds = customId.split(",");
			if(StringUtil.checkRepeat(customIds))
			{
				log.info("缔约方有重复值");
				throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_HAS_DULP[0],ConstantUtil.RETURN_CUSTOMID_HAS_DULP[1], ConstantUtil.RETURN_CUSTOMID_HAS_DULP[2]);
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			String serialNum = getOrderCode();
			//保存合同表,签署信息表
			IdentityEntity identityEntity = null;
			try{
				identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
			}catch(Exception e)
			{
				log.info("根据appid和平台用户名称查询用户异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == identityEntity)
			{
				throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],ConstantUtil.RETURN_CUST_NOT_EXIST[1],ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
			}
			
			//判断是否重复创建合同		
			ContractEntity contractDuplicate = null;
			try{
				contractDuplicate = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2]);
			}
			if((null != contractDuplicate))
			{
				log.info("订单号已经存在，不能重复创建合同");
				throw new ServiceException(ConstantUtil.ORDERID_HAS_EXIST[0],ConstantUtil.ORDERID_HAS_EXIST[1], ConstantUtil.ORDERID_HAS_EXIST[2]);
			}
			//判断ucid是否包含在customId
			if(!StringUtil.isContain(ucid,customIds))
			{
				//ucid是否包含在customId,返回
				log.info("customId不包含ucid");
				returnData =  new ReturnData(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2], ucid);
				return returnData;
			}
			//查询余额
			//拷贝合同和附件
			//用List<Map> 保存所有附件，便于下一步存入附件表
			List<Map<String,String>> listFile = new ArrayList<Map<String,String>>();
			Map<String,String> attrMAP = new HashMap<String,String>();	
			String contractPath = FileUtil.createContractFolder(serialNum);
			//动态添加数据到模板文件,另存为html文件
			File file = new File(contractPath);
			if (!file.exists())
			{
				file.mkdirs();
			}
			String attachmentPath = contractPath +"attachment/";//附件目录
			File attrFile = new File(attachmentPath);
			if (!attrFile.exists())
			{
				attrFile.mkdirs();
			}
			Map<String,String> contractMessage = gson.fromJson(contractFile, Map.class);//主合同文件
			String contractFilePath = contractMessage.get("filePath");
			String contractFileName = contractMessage.get("fileName");
			File contractFileSha1 = new File(contractFilePath);
			String masterFile = contractPath+contractFileSha1.getName();
			FileUtil.copyFolder(new File(contractFilePath),new File(masterFile));//拷贝
			String pdfPath = "";
			File filePdf = null;//签名原文
			String imgType = IConf.getValue("IMGTYPE");			//图片类型
			if("HTML".equals(suffix(contractFileSha1).toUpperCase()) || "DOC".equals(suffix(contractFileSha1).toUpperCase()) || "DOCX".equals(suffix(contractFileSha1).toUpperCase()))
			{
				pdfPath = contractPath + contractFileName + ".pdf";
				if("HTML".equals(suffix(contractFileSha1).toUpperCase()))
				{
					PDFTool.htmlToPdf(masterFile, pdfPath);
				}
				else
				{
//					PDFTool.htmlToPdfLibreOffice(masterFile, contractPath);//合同文件转pdf
					PDFTool.wordToPdf(masterFile, pdfPath);//合同文件转pdf
				}					
				attrMAP.put("path", pdfPath);
				attrMAP.put("originalFilePath", masterFile);//原文路径
				filePdf = new File(masterFile);
			}
			else if(imgType.contains(suffix(contractFileSha1).toUpperCase()))
			{				
				Map<String, String> map = new HashMap<String, String>();
				map.put("masterFile", masterFile);
				Map<String,Integer> imgInfo = getImgWidthAndHeight(masterFile);
				double width = imgInfo.get("width");
				double height = imgInfo.get("height");				
				DecimalFormat df = new DecimalFormat("###.000");				
				double zoom = 0.0;
				int heightStr = 0;
				int widthStr = 0;
				if(height > width)
				{
					zoom = Double.valueOf(df.format(width / height)); // 合同图片缩放比例,保留小数点后三位  (小于1)
					if(height > 800)
					{
						heightStr = 842;
						widthStr = (int)(heightStr * zoom);
					}
					else
					{
						heightStr = (int)height;
						widthStr = (int)width;
					}
				}
				else
				{
					zoom = Double.valueOf(df.format(height/width)); // 合同图片缩放比例,保留小数点后三位 (小于1)
					if(width > 542)
					{
						widthStr = 542;
						heightStr = (int)(widthStr * zoom);
					}
					else
					{
						heightStr = (int) height;
						widthStr = (int) width;
					}
				}
				log.info("heightStr="+heightStr+"widthStr="+widthStr);				
				map.put("width", String.valueOf(widthStr));
				map.put("height", String.valueOf(heightStr));
				map.put("contractPath", contractPath);
				map.put("imgPath", masterFile);
				map.put("key", "masterFile");
				
				FileUtil.appendImg(map, IConf.getValue("CONTRACTPATH")+"wechatImg.html", contractPath+contractFileName+".html");
				pdfPath = contractPath + contractFileName + ".pdf";
//				PDFTool.htmlToPdfLibreOffice(contractPath+contractFileName+".html", contractPath);//合同文件转pdf
				PDFTool.htmlToPdf(contractPath+contractFileName+".html", pdfPath);//合同文件转pdf
				attrMAP.put("path", pdfPath);
				attrMAP.put("originalFilePath", masterFile);//原文路径				
				filePdf = new File(masterFile);
			}
			else
			{
				pdfPath = contractPath + contractFileSha1.getName();
				attrMAP.put("originalFilePath", pdfPath);//原文路径
				attrMAP.put("path", masterFile);
				filePdf = new File(pdfPath);
			}
			
			StringBuffer signOriginalText = new StringBuffer("Z_1_");
			signOriginalText.append(filePdf.getName());
			signOriginalText.append("=");
			String sha1 = SHA_MD.encodeFileSHA1(filePdf).toHexString();
			signOriginalText.append(sha1);
			signOriginalText.append("&");
			String attOriginalName_main = contractMessage.get("attOriginalName");
			attrMAP.put("originalFileName", attOriginalName_main);//主文件原文件名字
			attrMAP.put("originalFile", contractFilePath);//对接传来的主文件
			attrMAP.put("name", contractFileName);
	//		attrMAP.put("path", masterFile);
			attrMAP.put("type", "1");
	
			listFile.add(attrMAP);
			//将pdf转为图片
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", platformEntity.getAppId());
			pdfTomImgMap.put("ucid", identityEntity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(pdfPath, contractPath+"img"+"/"+contractFileName,pdfTomImgMap);
			//附件
			List<Map<String,String>> list = null;
			if(!"".equals(attachmentFile))
			{			
				try {
					list = gson.fromJson(attachmentFile, List.class);
				} catch (JsonSyntaxException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.JSONSYNTAXEXCEPTION[0],ConstantUtil.JSONSYNTAXEXCEPTION[1],ConstantUtil.JSONSYNTAXEXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null!=list && list.size()>0)
				{
					for(int i=0; i<list.size();i++)
					{
						Map<String,String> mapAttr = list.get(i);
						String attrName = mapAttr.get("attName");
						String attrPath = mapAttr.get("attPath");
						File attachment = new File(attrPath);
						String attPath = attachmentPath+attachment.getName();
						File attPathFile = new File(attPath);
//						File f = new File(attPath);
						FileUtil.copyFolder(attachment,attPathFile);//拷贝
						Map<String,String> attrMAPTemp = new HashMap<String,String>();
						attrMAPTemp.put("originalFilePath", attPath);//附件原文路径
						if("HTML".equals(suffix(attPathFile).toUpperCase()) || "DOC".equals(suffix(attPathFile).toUpperCase()) || "DOCX".equals(suffix(attPathFile).toUpperCase()))
						{						
							if("HTML".equals(suffix(attPathFile).toUpperCase()))
							{
								PDFTool.htmlToPdf(attPath, attachmentPath+attrName+".pdf");//合同文件
							}
							else
							{
//								PDFTool.htmlToPdfLibreOffice(attPath, attachmentPath);//合同文件
								PDFTool.wordToPdf(attPath, attachmentPath+attrName+".pdf");//合同文件
							}
							attrMAPTemp.put("path", attachmentPath+attrName+".pdf");
							PDFTool.pdfToImg(attachmentPath+attrName+".pdf", attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}
						else if(imgType.contains(suffix(attPathFile).toUpperCase()))
						{
							Map<String, String> map = new HashMap<String, String>();
							map.put("key", "fujian");
							Map<String,Integer> imgInfo = getImgWidthAndHeight(attPath);
//							map.put("width", String.valueOf(imgInfo.get("width")));
//							map.put("height", String.valueOf(imgInfo.get("height")));
							
							double width = imgInfo.get("width");
							double height = imgInfo.get("height");				
							DecimalFormat df = new DecimalFormat("###.000");				
							double zoom = 0.0;
							int heightStr = 0;
							int widthStr = 0;
							if(height > width)
							{
								zoom = Double.valueOf(df.format(width / height)); // 合同图片缩放比例,保留小数点后三位  (小于1)
								if(height > 800)
								{
									heightStr = 842;
									widthStr = (int)(heightStr * zoom);
								}
								else
								{
									heightStr = (int)height;
									widthStr = (int)width;
								}
							}
							else
							{
								zoom = Double.valueOf(df.format(height/width)); // 合同图片缩放比例,保留小数点后三位 (小于1)
								if(width > 542)
								{
									widthStr = 542;
									heightStr = (int)(widthStr * zoom);
								}
								else
								{
									heightStr = (int) height;
									widthStr = (int) width;
								}
							}
							log.info("heightStr="+heightStr+"widthStr="+widthStr);				
							map.put("width", String.valueOf(widthStr));
							map.put("height", String.valueOf(heightStr));
							map.put("contractPath", attachmentPath);
							map.put("imgPath", attPath);
							
							attrMAPTemp.put("path", attachmentPath+attrName+".pdf");
							FileUtil.appendImg(map, IConf.getValue("CONTRACTPATH")+"wechatImg.html", attachmentPath+attrName+".html");
//							PDFTool.htmlToPdfLibreOffice(attachmentPath+attrName+".html", attachmentPath);//合同文件转pdf
							PDFTool.htmlToPdf(attachmentPath+attrName+".html", attachmentPath+attrName+".pdf");//合同文件转pdf
							PDFTool.pdfToImg(attachmentPath+attrName+".pdf", attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}
						else
						{
							attrMAPTemp.put("path", attachmentPath+attPathFile.getName());
							PDFTool.pdfToImg(attPath, attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}					
						attrMAPTemp.put("name", attrName);
						attrMAPTemp.put("originalFile", attrPath);//对接传来的主文件
	//					attrMAPTemp.put("path", attachmentPath+f.getName());
						attrMAPTemp.put("type", "2");//附件
						String attOriginalName = mapAttr.get("attOriginalName");
						attrMAPTemp.put("originalFileName", attOriginalName);//附件原文件名字
						listFile.add(attrMAPTemp);
						String attrSha1 = SHA_MD.encodeFileSHA1(attPathFile).toHexString();
						signOriginalText.append("F_"+(i+1)+"_");
						signOriginalText.append(attPathFile.getName());
						signOriginalText.append("=");
						signOriginalText.append(attrSha1);
						signOriginalText.append("&");
						
					}
				}
			}
		
			ContractEntity contract = new ContractEntity();
			int opt = this.getOptForm(optFrom);
			contract.setSerialNum(serialNum);
			contract.setType("");
			if(!"".equals(price))
			{	
				contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble(price)))));
			}
			else
			{
//				contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble(price)))));
				contract.setPrice(BigDecimal.valueOf(Double.parseDouble("0.00")));
			}
			contract.setPname(pname);
			contract.setUpdateTime(new Date());
			contract.setOptFrom((byte)opt);
			contract.setCreator(identityEntity.getId());
			contract.setCreateTime(new Date());
			try {
				contract.setDeadline(DateUtil.stringToDate(offerTime));
			} catch (ParseException e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.OFFTIME_IS_ILLEGAL[0],ConstantUtil.OFFTIME_IS_ILLEGAL[1],ConstantUtil.OFFTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
			}
			if(!"".equals(startTime))
			{
				try {
					contract.setStartTime(DateUtil.stringToDate(startTime));//合同开始时间
				} catch (ParseException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.STARTTIME_IS_ILLEGAL[0],ConstantUtil.STARTTIME_IS_ILLEGAL[1],ConstantUtil.STARTTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
				}
			}
			if(!"".equals(parentContractId))
			{
				contract.setParentContractId(Integer.parseInt(parentContractId));
			}
			contract.setStatus((byte) 0);
			contract.setIsShow((byte)1);
			contract.setMark("");
			contract.setCPlatform(platformEntity);
			contract.setSignPlaintext(signOriginalText.toString());
			contract.setFinishtime(null);//创建初始化完成时间为null
			if(!"".equals(endTime))
			{
				try {
					contract.setEndTime(DateUtil.stringToDate(endTime));//合同结束时间
				} catch (ParseException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.ENDTIME_IS_ILLEGAL[0],ConstantUtil.ENDTIME_IS_ILLEGAL[1],ConstantUtil.ENDTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
				}
			}
			contract.setKeyword(keyword);
			contract.setPaymentType(payType);
			contract.setContractType(contractType);//合同类型
			contract.setOperator("");//
			contract.setSha1(sha1);
			contract.setTitle(title);
			contract.setOrderId(orderId);
			contract.setOtheruids(customId);
			ContractEntity cn = null;
			try{
				cn = contractDao.save(contract);
			}catch(Exception e)
			{
				log.info("数据保存到合同表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != cn)
			{
				log.info("保存附件表");
				//保存到附件表
				for (Map<String, String> map : listFile) {
					ContractPathEntity contractPathEntity =  new ContractPathEntity();
					contractPathEntity.setAttName(map.get("name"));
					contractPathEntity.setType((byte) Integer.parseInt(map.get("type")));
					contractPathEntity.setFilePath(map.get("path"));				
					String originalFile = map.get("originalFilePath");
					contractPathEntity.setExtension(suffix(new File(originalFile)));
					contractPathEntity.setContractSerialNum(serialNum);
					contractPathEntity.setContractPath(contractPath);
					contractPathEntity.setOriginalFilePath(map.get("originalFilePath"));
					contractPathEntity.setOriginalFileName(map.get("originalFileName"));
					contractPathEntity.setOriginalFile(map.get("originalFile"));
					contractPathEntity.setCContract(cn);
					try{
						contractPathDao.save(contractPathEntity);
					}catch(Exception e)
					{
						log.info("数据保存到附件表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
				}
				
				//签署记录表
				SignRecordEntity signRecord = new SignRecordEntity();
				boolean flag = false;
				for(int i=0;i<customIds.length;i++)
				{
					//查询c_identity表
					IdentityEntity identity = null;
					try{
						identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,customIds[i]);
					}catch(Exception e)
					{
						log.info("根据appid和ucid查询用户表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2]);
					}
					if(null == identity)
					{
						//有缔约方没有注册
						log.info("有缔约方没有注册");
						throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+customIds[i],ConstantUtil.RETURN_USER_NOTEXIST[2]);
					}
					signRecord.setCContract(cn);
					signRecord.setCIdentity(identity);
					signRecord.setOrignalFilename("");
					signRecord.setPrevSha1("");
					signRecord.setCurrentSha1("");
					//signRecord.setSignTime(null);
					signRecord.setSigndata("");
					signRecord.setCUkeyInfo(null);
					signRecord.setSignStatus((byte) 0);
					signRecord.setMark("");
					signRecord.setSignMode((byte) 0);
					SignRecordEntity sr = null;
					try{
						sr = signRecordDao.save(signRecord);
					}catch(Exception e)
					{
						log.info("数据保存到签署表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
					if(null != sr)
					{
						flag = true;
					}
				}
				if(flag)
				{
					log.info("合同创建成功");
					String contractStr = "";
					
					//查找计费帐号ID
					int chargingUserId = 0; 
					if(identityEntity.getParentId() == 0)
					{
						chargingUserId = identityEntity.getId();
					}
					else
					{
						chargingUserId = identityEntity.getParentId();
					}
					if(payType == 0)//代付
					{
						charging(serialNum,chargingUserId,customIds.length);
					}
					else
					{
						//自付 只扣自己的一次
						charging(serialNum,chargingUserId,1);
					}
					try {
						contractStr = JSON.toJSONString(cn);
					} catch (Exception e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2]);
					}
					returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],contractStr);
				}
				else
				{
					log.info("创建创建初始化签署表失败");
					throw new ServiceException(ConstantUtil.CREATECONTRACT_FAIL[0],ConstantUtil.CREATECONTRACT_FAIL[1],ConstantUtil.CREATECONTRACT_FAIL[2]);
				}
			}
			else{
				log.info("保存合同表失败");
				throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	/**
	 * 草稿创建合同
	 * @param appid 平台ID,由云签平台提供
	 * @param time 当前时间戳(精确到秒)
	 * @param sign 由appid, flow, time, key生成
	 * @param ucid 创建人用户ID,第三方业务平台用户的唯一身份
	 * @param customid 各缔约方ID,缔约多方，必须是2个人以上
	 * @param fromcustom 接入系统名称,来自的平台名称
	 * @param orderid 接入系统业务流水号,由第三方系统生成流水号;必须唯一
	 * @param title 合同标题
	 * @param template 模板名称
	 * @param offertime 签署过期时间
	 * @param pname 项目名称
	 * @param tempNumber 模板编号
	 * @param data 项目名称 Data内容为json格式；参数命名按照模板中定义的键名依次传递 例如{“title”:”中国云签”,”company”:”买卖网”}
	 */
	//TODO
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData draftCreate(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
//			checkParam(datamap);
			Gson gson = new Gson();
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String customId = StringUtil.nullToString(datamap.get("customId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String offerTime = StringUtil.nullToString(datamap.get("offerTime"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String pname = StringUtil.nullToString(datamap.get("pname"));
			String price = StringUtil.nullToString(datamap.get("price"));
			String keyword = StringUtil.nullToString(datamap.get("keyword"));
			String contractType = StringUtil.nullToString(datamap.get("contractType"));
			String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
			String contractFile = StringUtil.nullToString(datamap.get("contractFile"));
			String attachmentFile = StringUtil.nullToString(datamap.get("attachmentFile"));
			/**
			 * payment_type 付款类型
			 * 0,代付 1,自付
			 */
			String paymentType = StringUtil.nullToString(datamap.get("paymentType"));
			byte payType = 0;
			if(!"".equals(paymentType))
			{
				payType = Byte.valueOf(paymentType);
			}	
			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime))
			{
				log.info("当前时间大于过期时间");
				throw new ServiceException(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2]);
			}
			
			String parentContractId = StringUtil.nullToString(datamap.get("parentorderid"));

			//判断缔约方有重复值
			String [] customIds = customId.split(",");
			if(StringUtil.checkRepeat(customIds))
			{
				log.info("缔约方有重复值");
				throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_HAS_DULP[0],ConstantUtil.RETURN_CUSTOMID_HAS_DULP[1], ConstantUtil.RETURN_CUSTOMID_HAS_DULP[2]);
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			//保存合同表,签署信息表
			IdentityEntity identityEntity = null;
			try{
				identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
			}catch(Exception e)
			{
				log.info("根据appid和平台用户名称查询用户异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == identityEntity)
			{
				throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],ConstantUtil.RETURN_CUST_NOT_EXIST[1],ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
			}
			
			//判断是否重复创建合同		
			ContractEntity contractEntity = null;
			try{
				contractEntity = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2]);
			}
			if((null == contractEntity))
			{
				log.info("变更合同不存在");
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1], ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
			}
			//判断ucid是否包含在customId
			if(!StringUtil.isContain(ucid,customIds))
			{
				//ucid是否包含在customId,返回
				log.info("customId不包含ucid");
				returnData =  new ReturnData(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2], ucid);
				return returnData;
			}
			//拷贝合同和附件
			//用List<Map> 保存所有附件，便于下一步存入附件表
			List<Map<String,String>> listFile = new ArrayList<Map<String,String>>();
			Map<String,String> attrMAP = new HashMap<String,String>();
			String serialNum = contractEntity.getSerialNum();
			String contractPath = FileUtil.createContractFolder(serialNum);
			//动态添加数据到模板文件,另存为html文件
			File file = new File(contractPath);
			if (!file.exists())
			{
				file.mkdirs();
			}
			String attachmentPath = contractPath +"attachment/";//附件目录
			File attrFile = new File(attachmentPath);
			if (!attrFile.exists())
			{
				attrFile.mkdirs();
			}
			Map<String,String> contractMessage = gson.fromJson(contractFile, Map.class);//主合同文件
			String contractFilePath = contractMessage.get("filePath");
			String contractFileName = contractMessage.get("fileName");
			File contractFileSha1 = new File(contractFilePath);
			String masterFile = contractPath+contractFileSha1.getName();
			FileUtil.copyFolder(new File(contractFilePath),new File(masterFile));//拷贝
			String pdfPath = "";
			File filePdf = null;//签名原文
			String imgType = IConf.getValue("IMGTYPE");			//图片类型
			if("HTML".equals(suffix(contractFileSha1).toUpperCase()) || "DOC".equals(suffix(contractFileSha1).toUpperCase()) || "DOCX".equals(suffix(contractFileSha1).toUpperCase()))
			{
				pdfPath = contractPath + contractFileName + ".pdf";
				if("HTML".equals(suffix(contractFileSha1).toUpperCase()))
				{
					PDFTool.htmlToPdf(masterFile, pdfPath);
				}
				else
				{
//					PDFTool.htmlToPdfLibreOffice(masterFile, contractPath);//合同文件转pdf
					PDFTool.wordToPdf(masterFile, pdfPath);//合同文件转pdf
				}					
				attrMAP.put("path", pdfPath);
				attrMAP.put("originalFilePath", masterFile);//原文路径
				filePdf = new File(masterFile);
			}
			else if(imgType.contains(suffix(contractFileSha1).toUpperCase()))
			{				
				Map<String, String> map = new HashMap<String, String>();
				map.put("masterFile", masterFile);
				Map<String,Integer> imgInfo = getImgWidthAndHeight(masterFile);
				double width = imgInfo.get("width");
				double height = imgInfo.get("height");				
				DecimalFormat df = new DecimalFormat("###.000");				
				double zoom = 0.0;
				int heightStr = 0;
				int widthStr = 0;
				if(height > width)
				{
					zoom = Double.valueOf(df.format(width / height)); // 合同图片缩放比例,保留小数点后三位  (小于1)
					if(height > 800)
					{
						heightStr = 842;
						widthStr = (int)(heightStr * zoom);
					}
					else
					{
						heightStr = (int)height;
						widthStr = (int)width;
					}
				}
				else
				{
					zoom = Double.valueOf(df.format(height/width)); // 合同图片缩放比例,保留小数点后三位 (小于1)
					if(width > 542)
					{
						widthStr = 542;
						heightStr = (int)(widthStr * zoom);
					}
					else
					{
						heightStr = (int) height;
						widthStr = (int) width;
					}
				}
				log.info("heightStr="+heightStr+"widthStr="+widthStr);				
				map.put("width", String.valueOf(widthStr));
				map.put("height", String.valueOf(heightStr));
				map.put("contractPath", contractPath);
				map.put("imgPath", masterFile);
				map.put("key", "masterFile");
				
				FileUtil.appendImg(map, IConf.getValue("CONTRACTPATH")+"wechatImg.html", contractPath+contractFileName+".html");
				pdfPath = contractPath + contractFileName + ".pdf";
//				PDFTool.htmlToPdfLibreOffice(contractPath+contractFileName+".html", contractPath);//合同文件转pdf
				PDFTool.htmlToPdf(contractPath+contractFileName+".html", pdfPath);//合同文件转pdf
				attrMAP.put("path", pdfPath);
				attrMAP.put("originalFilePath", masterFile);//原文路径	
				filePdf = new File(masterFile);
			}
			else
			{
				pdfPath = contractPath + contractFileSha1.getName();
				attrMAP.put("originalFilePath", pdfPath);//原文路径
				attrMAP.put("path", masterFile);
				filePdf = new File(pdfPath);
			}
			
			StringBuffer signOriginalText = new StringBuffer("Z_1_");
			signOriginalText.append(filePdf.getName());
			signOriginalText.append("=");
			String sha1 = SHA_MD.encodeFileSHA1(filePdf).toHexString();
			signOriginalText.append(sha1);
			signOriginalText.append("&");
			String attOriginalName_main = contractMessage.get("attOriginalName");
			attrMAP.put("originalFileName", attOriginalName_main);//主文件原文件名字
			attrMAP.put("originalFile", contractFilePath);//对接传来的文件
			attrMAP.put("name", contractFileName);
	//		attrMAP.put("path", masterFile);
			attrMAP.put("type", "1");
	
			listFile.add(attrMAP);
			//将pdf转为图片
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", platformEntity.getAppId());
			pdfTomImgMap.put("ucid", identityEntity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(pdfPath, contractPath+"img"+"/"+contractFileName,pdfTomImgMap);
			//附件
			List<Map<String,String>> list = null;
			if(!"".equals(attachmentFile))
			{			
				try {
					list = gson.fromJson(attachmentFile, List.class);
				} catch (JsonSyntaxException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.JSONSYNTAXEXCEPTION[0],ConstantUtil.JSONSYNTAXEXCEPTION[1],ConstantUtil.JSONSYNTAXEXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null!=list && list.size()>0)
				{
					for(int i=0; i<list.size();i++)
					{
						Map<String,String> mapAttr = list.get(i);
						String attrName = mapAttr.get("attName");
						String attrPath = mapAttr.get("attPath");
						File attachment = new File(attrPath);
						String attPath = attachmentPath+attachment.getName();
						File attPathFile = new File(attPath);
//						File f = new File(attPath);
						FileUtil.copyFolder(attachment,attPathFile);//拷贝
						Map<String,String> attrMAPTemp = new HashMap<String,String>();
						attrMAPTemp.put("originalFilePath", attPath);//附件原文路径
						if("HTML".equals(suffix(attPathFile).toUpperCase()) || "DOC".equals(suffix(attPathFile).toUpperCase()) || "DOCX".equals(suffix(attPathFile).toUpperCase()))
						{						
							if("HTML".equals(suffix(attPathFile).toUpperCase()))
							{
								PDFTool.htmlToPdf(attPath, attachmentPath+attrName+".pdf");//合同文件
							}
							else
							{
//								PDFTool.htmlToPdfLibreOffice(attPath, attachmentPath);//合同文件
								PDFTool.wordToPdf(attPath, attachmentPath+attrName+".pdf");//合同文件
							}
							attrMAPTemp.put("path", attachmentPath+attrName+".pdf");
							PDFTool.pdfToImg(attachmentPath+attrName+".pdf", attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}
						else if(imgType.contains(suffix(attPathFile).toUpperCase()))
						{
							Map<String, String> map = new HashMap<String, String>();
							map.put("key", "fujian");
							Map<String,Integer> imgInfo = getImgWidthAndHeight(attPath);	
							double width = imgInfo.get("width");
							double height = imgInfo.get("height");				
							DecimalFormat df = new DecimalFormat("###.000");				
							double zoom = 0.0;
							int heightStr = 0;
							int widthStr = 0;
							if(height > width)
							{
								zoom = Double.valueOf(df.format(width / height)); // 合同图片缩放比例,保留小数点后三位  (小于1)
								if(height > 800)
								{
									heightStr = 842;
									widthStr = (int)(heightStr * zoom);
								}
								else
								{
									heightStr = (int)height;
									widthStr = (int)width;
								}
							}
							else
							{
								zoom = Double.valueOf(df.format(height/width)); // 合同图片缩放比例,保留小数点后三位 (小于1)
								if(width > 542)
								{
									widthStr = 542;
									heightStr = (int)(widthStr * zoom);
								}
								else
								{
									heightStr = (int) height;
									widthStr = (int) width;
								}
							}
							log.info("heightStr="+heightStr+"widthStr="+widthStr);				
							map.put("width", String.valueOf(widthStr));
							map.put("height", String.valueOf(heightStr));
							map.put("contractPath", attachmentPath);
							map.put("imgPath", attPath);
							
							attrMAPTemp.put("path", attachmentPath+attrName+".pdf");
							FileUtil.appendImg(map, IConf.getValue("CONTRACTPATH")+"wechatImg.html", attachmentPath+attrName+".html");
//							PDFTool.htmlToPdfLibreOffice(attachmentPath+attrName+".html", attachmentPath);//合同文件转pdf
							PDFTool.htmlToPdf(attachmentPath+attrName+".html", attachmentPath+attrName+".pdf");//合同文件转pdf
							PDFTool.pdfToImg(attachmentPath+attrName+".pdf", attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}
						else
						{
							attrMAPTemp.put("path", attachmentPath+attPathFile.getName());
							PDFTool.pdfToImg(attPath, attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}					
						attrMAPTemp.put("name", attrName);
	//					attrMAPTemp.put("path", attachmentPath+f.getName());
						attrMAPTemp.put("type", "2");//附件
						String attOriginalName = mapAttr.get("attOriginalName");
						attrMAPTemp.put("originalFileName", attOriginalName);//附件原文件名字						
						attrMAPTemp.put("originalFile", attrPath);//对接传来的主文件
						listFile.add(attrMAPTemp);
						String attrSha1 = SHA_MD.encodeFileSHA1(attPathFile).toHexString();
						signOriginalText.append("F_"+(i+1)+"_");
						signOriginalText.append(attPathFile.getName());
						signOriginalText.append("=");
						signOriginalText.append(attrSha1);
						signOriginalText.append("&");
						
					}
				}
			}
			BigDecimal tempPrice = BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble(price))));
			Date deadline = null;
			try {
				deadline = DateUtil.stringToDate(offerTime);
			} catch (ParseException e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.OFFTIME_IS_ILLEGAL[0],ConstantUtil.OFFTIME_IS_ILLEGAL[1],ConstantUtil.OFFTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
			}
			int update = contractDao.updateDraftContract(serialNum, title, keyword, pname, tempPrice, deadline, customId, (byte)0,new Date(),sha1,signOriginalText.toString());
			if(update > 0)
			{
				//先删除附件表
				int deleteInt = contractPathDao.deleteContractPath(contractEntity);
				if(deleteInt >0)
				{
					log.info("保存附件表");
					//保存到附件表
					for (Map<String, String> map : listFile) {
						ContractPathEntity contractPathEntity =  new ContractPathEntity();
						contractPathEntity.setAttName(map.get("name"));
						contractPathEntity.setType((byte) Integer.parseInt(map.get("type")));
						contractPathEntity.setFilePath(map.get("path"));				
						String originalFile = map.get("originalFilePath");
						contractPathEntity.setExtension(suffix(new File(originalFile)));
						contractPathEntity.setContractSerialNum(serialNum);
						contractPathEntity.setContractPath(contractPath);
						contractPathEntity.setOriginalFilePath(map.get("originalFilePath"));
						contractPathEntity.setOriginalFileName(map.get("originalFileName"));
						contractPathEntity.setOriginalFile(map.get("originalFile"));
						contractPathEntity.setCContract(contractEntity);
						try{
							contractPathDao.save(contractPathEntity);
						}catch(Exception e)
						{
							log.info("数据保存到附件表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
					}
				}
				//先删除原有的签署记录
				boolean flag = false;
				if(signRecordDao.deleteSignRecord(contractEntity)>0)
				{
					//签署记录表
					SignRecordEntity signRecord = new SignRecordEntity();					
					for(int i=0;i<customIds.length;i++)
					{
						//查询c_identity表
						IdentityEntity identity = null;
						try{
							identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,customIds[i]);
						}catch(Exception e)
						{
							log.info("根据appid和ucid查询用户表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2]);
						}
						if(null == identity)
						{
							//有缔约方没有注册
							log.info("有缔约方没有注册");
							throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+customIds[i],ConstantUtil.RETURN_USER_NOTEXIST[2]);
						}
						signRecord.setCContract(contractEntity);
						signRecord.setCIdentity(identity);
						signRecord.setOrignalFilename("");
						signRecord.setPrevSha1("");
						signRecord.setCurrentSha1("");
						//signRecord.setSignTime(null);
						signRecord.setSigndata("");
						signRecord.setCUkeyInfo(null);
						signRecord.setSignStatus((byte) 0);
						signRecord.setMark("");
						signRecord.setSignMode((byte) 0);
						SignRecordEntity sr = null;
						try{
							sr = signRecordDao.save(signRecord);
						}catch(Exception e)
						{
							log.info("数据保存到签署表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != sr)
						{
							flag = true;
						}
					}
				}
				if(flag)
				{
					log.info("合同创建成功");
					String contractStr = "";
					//变更合同不扣费
					/*
					//查找计费帐号ID
					int chargingUserId = 0; 
					if(identityEntity.getParentId() == 0)
					{
						chargingUserId = identityEntity.getId();
					}
					else
					{
						chargingUserId = identityEntity.getParentId();
					}
					
					if(payType == 0)//代付
					{
						charging(serialNum,chargingUserId,customIds.length);
					}
					else
					{
						//自付 只扣自己的一次
						charging(serialNum,chargingUserId,1);
					}
					*/
					try {
						contractStr = JSON.toJSONString(contractEntity);
					} catch (Exception e) {
						throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2]);
					}
					returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],contractStr);
				}
				else
				{
					log.info("创建创建初始化签署表失败");
					throw new ServiceException(ConstantUtil.CREATECONTRACT_FAIL[0],ConstantUtil.CREATECONTRACT_FAIL[1],ConstantUtil.CREATECONTRACT_FAIL[2]);
				}
			}
			else{
				log.info("保存合同表失败");
				throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));			
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	/**
	 * 模板创建合同
	 * @param appid 平台ID,由云签平台提供
	 * @param time 当前时间戳(精确到秒)
	 * @param sign 由appid, flow, time, key生成
	 * @param ucid 创建人用户ID,第三方业务平台用户的唯一身份
	 * @param customid 各缔约方ID,缔约多方，必须是2个人以上
	 * @param fromcustom 接入系统名称,来自的平台名称
	 * @param orderid 接入系统业务流水号,由第三方系统生成流水号;必须唯一
	 * @param title 合同标题
	 * @param template 模板名称
	 * @param offertime 签署过期时间
	 * @param pname 项目名称
	 * @param tempNumber 模板编号
	 * @param data 项目名称 Data内容为json格式；参数命名按照模板中定义的键名依次传递 例如{“title”:”中国云签”,”company”:”买卖网”}
	 * @param noapprovalid 免审批的ID，中铁专用
	 */
	//TODO
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData mmecCreateTempplateAndAttachment(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
			checkParam(datamap);
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String customId = StringUtil.nullToString(datamap.get("customId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
	//		String fromCustom = StringUtil.nullToString(datamap.get("fromCustom"));
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String offerTime = StringUtil.nullToString(datamap.get("offerTime"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String pname = StringUtil.nullToString(datamap.get("pname"));
			String tempNumber = StringUtil.nullToString(datamap.get("tempNumber"));
			String tempData = StringUtil.nullToString(datamap.get("tempData"));
			String serialNum = StringUtil.nullToString(datamap.get("serialNum"));
			String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
			String price =  StringUtil.nullToString(datamap.get("price"));
			//附件  String attachmentFile = "[{\"attName\":\"att1\",\"attPath\":\"F:/office/att1.docx\"},{\"attName\":\"att2\",\"attPath\":\"F:/office/att2.docx\"},{\"attName\":\"att3\",\"attPath\":\"F:/office/att3.pdf\"}]";//StringUtil.nullToString(datamap.get("attachment"));
			String attachmentFile = StringUtil.nullToString(datamap.get("attachmentFile"));
			String specialCharacterNumber =  StringUtil.nullToString(datamap.get("specialCharacterNumber")); //特殊字符用来签署PDF找坐标用 {"ucid1":"1,3,5","ucid2":"2,4,6"}
			String specialCharacter = StringUtil.nullToString(datamap.get("specialCharacter"));
			int opt = this.getOptForm(optFrom);
			/*
			 *扣费方式 
			 *0 扣平台方的钱
			 */
			String chargeType = StringUtil.nullToString(datamap.get("chargeType"));
			//判断缔约方有重复值
			String [] customIds = customId.split(",");
			if(StringUtil.checkRepeat(customIds))
			{
				log.info("缔约方有重复值");
				throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_HAS_DULP[0],ConstantUtil.RETURN_CUSTOMID_HAS_DULP[1], ConstantUtil.RETURN_CUSTOMID_HAS_DULP[2]);
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);

			}
			
			//判断是否重复创建合同		
			ContractEntity contractDuplicate = null;
			ContractEntity contractDuplicate_SerialNum = null;
			try{
				contractDuplicate = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
				contractDuplicate_SerialNum = contractDao.findContractBySerialNum(serialNum);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			
			if((null != contractDuplicate) || (null != contractDuplicate_SerialNum))
			{
				log.info("订单号已经存在，不能重复创建合同");
				throw new ServiceException(ConstantUtil.ORDERID_HAS_EXIST[0],ConstantUtil.ORDERID_HAS_EXIST[1], ConstantUtil.ORDERID_HAS_EXIST[2]);
			}
			//判断ucid是否包含在customId
			if(!StringUtil.isContain(ucid,customIds))
			{
				throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2]);
			}
			
			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime))
			{
				log.info("当前时间大于过期时间");
				throw new ServiceException(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2]);
			}
			Gson gson = new Gson();
			//根据模板编号,查询模板
			ContractTemplateEntity contractTemplate = null;
			try{
				contractTemplate = contractTemplateDao.findContractTemplateByTempNumber(tempNumber,platformEntity.getAppId());
			}catch(Exception e)
			{
				log.info("查询模板表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}			
			StringBuffer signData = null;
			if(null != contractTemplate)
			{	
				String tempPath = contractTemplate.getFilePath();
				String fileName = DateUtil.toDateYYYYMMDDHHMM1();
				if(StringUtil.isNull(serialNum))
				{
					serialNum = getOrderCode();
				}
				//保存合同表,签署信息表
				IdentityEntity identityEntity = null;
				try{
					identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
				}catch(Exception e)
				{
					log.info("根据appid和平台用户名称查询用户异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null == identityEntity)
				{
					throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],ConstantUtil.RETURN_CUST_NOT_EXIST[1],ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
				}
				
				String contractPath = FileUtil.createContractFolder(serialNum);
				//用List<Map> 保存所有附件，便于下一步存入附件表
				List<Map<String,String>> listFile = new ArrayList<Map<String,String>>();
				Map<String,String> attrMAP = new HashMap<String,String>();	
				//动态添加数据到模板文件,另存为html文件,合同文件
				File file = new File(contractPath);
				if (!file.exists())
				{
					file.mkdirs();
				}
				if(!FileUtil.appendHtml(tempData,tempPath,contractPath+fileName+".html",contractPath)) 
				{
					log.info("模板数据装载错误");
					throw new ServiceException(ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[0],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[1],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[2]);
				}
//				FileUtil.appendHtml(tempData,tempPath,contractPath+fileName+".html",contractPath);
				//将添加数据后的html文件转为pdf
				String pdfPathfile = contractPath+fileName+".pdf";
				PDFTool.htmlToPdf(contractPath+fileName+".html", pdfPathfile);
//				PDFTool.htmlToPdfLibreOffice(filePath+fileName+".html", filePath);
				//对pdf文件进行sha1加密
				File f = new File(pdfPathfile);
//				File f = new File("e:/office/MVC_sign.pdf");
//				File f = new File("/home/opt/test.pdf");	
				signData = new StringBuffer("Z_1_");
				signData.append(fileName);
				signData.append(".pdf");
				signData.append("=");
				String sha1 = SHA_MD.encodeFileSHA1(f).toHexString();
				signData.append(sha1);
				signData.append("&");
				attrMAP.put("path", pdfPathfile);
				attrMAP.put("originalFilePath", pdfPathfile);//原文路径
				attrMAP.put("name", fileName);
				attrMAP.put("originalFileName", fileName+".pdf");//原文件名字
				attrMAP.put("type", "1");			
				listFile.add(attrMAP);
				File imgFolder = new File(contractPath+"img"+"/"+fileName);
				if (!imgFolder.exists())
				{
					imgFolder.mkdirs();
				}
				Map<String, String> pdfTomImgMap = new HashMap<String, String>();
				pdfTomImgMap.put("optFrom", "NULL");
				pdfTomImgMap.put("appId", platformEntity.getAppId());
				pdfTomImgMap.put("ucid", identityEntity.getAccount());
				pdfTomImgMap.put("IP", "NULL");
				//pdf转图片
				PDFTool.pdfToImg(pdfPathfile, contractPath+"img"+"/"+fileName,pdfTomImgMap);
				//附件
				String imgType = IConf.getValue("IMGTYPE");//图片类型
				List<Map<String,String>> list = null;
				if(!"".equals(attachmentFile))
				{	
					String attachmentPath = contractPath +"attachment/";//附件目录
					File attrFile = new File(attachmentPath);
					if (!attrFile.exists())
					{
						attrFile.mkdirs();
					}
					try {
						list = gson.fromJson(attachmentFile, List.class);
					} catch (JsonSyntaxException e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.JSONSYNTAXEXCEPTION[0],ConstantUtil.JSONSYNTAXEXCEPTION[1],ConstantUtil.JSONSYNTAXEXCEPTION[2],FileUtil.getStackTrace(e));
					}
					if(null!=list && list.size()>0)
					{
						if(list.size()>5)
						{
							throw new ServiceException(ConstantUtil.ATTACHMENT_NUMBER[0],ConstantUtil.ATTACHMENT_NUMBER[1],ConstantUtil.ATTACHMENT_NUMBER[2]);							
						}
						for(int i=0; i<list.size();i++)
						{
							Map<String,String> mapAttr = list.get(i);
							String attrName = mapAttr.get("attName");
							String attOriginalName = mapAttr.get("attOriginalName");
							File attachment = new File(mapAttr.get("attPath"));
							String attPath = attachmentPath+attachment.getName();
							File attPathFile = new File(attPath);
							FileUtil.copyFolder(attachment,attPathFile);//拷贝
							Map<String,String> attrMAPTemp = new HashMap<String,String>();
							attrMAPTemp.put("originalFilePath", attPath);//附件原文路径
							attrMAPTemp.put("originalFileName", attOriginalName);//附件原文件名字
							if("HTML".equals(suffix(attPathFile).toUpperCase()) || "DOC".equals(suffix(attPathFile).toUpperCase()) || "DOCX".equals(suffix(attPathFile).toUpperCase()))
							{	
								if("HTML".equals(suffix(attPathFile).toUpperCase()))
								{
									PDFTool.htmlToPdf(attPath, attachmentPath+attrName+".pdf");
								}	
								else
								{
//									PDFTool.htmlToPdfLibreOffice(attPath, attachmentPath);//合同文件
									PDFTool.wordToPdf(attPath, attachmentPath+attrName+".pdf");//合同文件
								}
								attrMAPTemp.put("path", attachmentPath+attrName+".pdf");
								PDFTool.pdfToImg(attachmentPath+attrName+".pdf", attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
							}
							else if(imgType.contains(suffix(attPathFile).toUpperCase()))
							{
								Map<String, String> map = new HashMap<String, String>();
								map.put("key", "fujian");
								Map<String,Integer> imgInfo = getImgWidthAndHeight(attPath);
//								map.put("width", String.valueOf(imgInfo.get("width")));
//								map.put("height", String.valueOf(imgInfo.get("height")));
								
								double width = imgInfo.get("width");
								double height = imgInfo.get("height");				
								DecimalFormat df = new DecimalFormat("###.000");				
								double zoom = 0.0;
								int heightStr = 0;
								int widthStr = 0;
								if(height > width)
								{
									zoom = Double.valueOf(df.format(width / height)); // 合同图片缩放比例,保留小数点后三位  (小于1)
									if(height > 800)
									{
										heightStr = 842;
										widthStr = (int)(heightStr * zoom);
									}
									else
									{
										heightStr = (int)height;
										widthStr = (int)width;
									}
								}
								else
								{
									zoom = Double.valueOf(df.format(height/width)); // 合同图片缩放比例,保留小数点后三位 (小于1)
									if(width > 542)
									{
										widthStr = 542;
										heightStr = (int)(widthStr * zoom);
									}
									else
									{
										heightStr = (int) height;
										widthStr = (int) width;
									}
								}
//								System.out.println("heightStr="+heightStr+"widthStr="+widthStr);				
								map.put("width", String.valueOf(widthStr));
								map.put("height", String.valueOf(heightStr));
								map.put("contractPath", attachmentPath);
								map.put("imgPath", attPath);
								
								attrMAPTemp.put("path", attachmentPath+attrName+".pdf");
								FileUtil.appendImg(map, IConf.getValue("CONTRACTPATH")+"wechatImg.html", attachmentPath+attrName+".html");
								PDFTool.htmlToPdf(attachmentPath+attrName+".html", attachmentPath+attrName+".pdf");//合同文件转pdf
								PDFTool.pdfToImg(attachmentPath+attrName+".pdf", attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
							}
							else
							{
								attrMAPTemp.put("path", attachmentPath+attPathFile.getName());
								PDFTool.pdfToImg(attPath, attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
							}					
							attrMAPTemp.put("name", attrName);
		//					attrMAPTemp.put("path", attachmentPath+f.getName());
							attrMAPTemp.put("type", "2");//附件
							listFile.add(attrMAPTemp);
							String attrSha1 = SHA_MD.encodeFileSHA1(attPathFile).toHexString();
							signData.append("F_"+(i+1)+"_");
							signData.append(attPathFile.getName());
							signData.append("=");
							signData.append(attrSha1);
							signData.append("&");
							
						}
					}
				}
				
				ContractEntity contract = new ContractEntity();
				contract.setSerialNum(serialNum);
				contract.setType("");
				contract.setUpdateTime(new Date());
				contract.setOptFrom((byte)opt);
				contract.setCreateTime(new Date());
				try {
					contract.setDeadline(DateUtil.stringToDate(offerTime));
				} catch (ParseException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.OFFTIME_IS_ILLEGAL[0],ConstantUtil.OFFTIME_IS_ILLEGAL[1],ConstantUtil.OFFTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
				}
				if(!"".equals(startTime))
				{
					try {
						contract.setStartTime(DateUtil.stringToDate(startTime));//合同开始时间
					} catch (ParseException e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.STARTTIME_IS_ILLEGAL[0],ConstantUtil.STARTTIME_IS_ILLEGAL[1],ConstantUtil.STARTTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
					}
				}
				contract.setStatus((byte) 0);
				contract.setIsShow((byte)0);
				contract.setMark("");
				contract.setCPlatform(platformEntity);
				contract.setSignPlaintext(signData.toString());
				contract.setFinishtime(null);//创建初始化完成时间为null
				if(platformEntity.getIsPdfSign() == 0)
				{
					contract.setIsPdfSign("N");
				}
				else
				{
					contract.setIsPdfSign("Y");
				}
				if(!"".equals(endTime))
				{
					try {
						contract.setEndTime(DateUtil.stringToDate(endTime));//合同结束时间
					} catch (ParseException e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.ENDTIME_IS_ILLEGAL[0],ConstantUtil.ENDTIME_IS_ILLEGAL[1],ConstantUtil.ENDTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
					}
				}
				contract.setCreator(identityEntity.getId());
				contract.setKeyword("");
				contract.setPaymentType((byte) 0);
				contract.setContractType("");//合同类型
				contract.setOperator("");//
				contract.setSha1(sha1);
				contract.setTitle(title);
				contract.setOrderId(orderId);
				contract.setOtheruids(customId);
				contract.setTempNumber(tempNumber);
				if(!"".equals(price))
				{	
					contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble(price)))));
				}
				else
				{
//					contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble("0.00")))));
					contract.setPrice(BigDecimal.valueOf(Double.parseDouble("0.00")));
				}
				
				ContractEntity cn = null;
				try{
					cn = contractDao.save(contract);
				}catch(Exception e)
				{
					log.info("数据保存到合同表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				
				if(null != cn)
				{
					//保存到附件表
//					ContractPathEntity contractPathEntity =  new ContractPathEntity();
//					contractPathEntity.setAttName(fileName);
//					contractPathEntity.setType((byte) 1);
//					contractPathEntity.setFilePath(pdfPathfile);
//					contractPathEntity.setOriginalFilePath(pdfPathfile);
//					contractPathEntity.setExtension("pdf");
//					contractPathEntity.setContractSerialNum(serialNum);
//					contractPathEntity.setCContract(cn);
//					contractPathEntity.setContractPath(contractPath);
//					try{
//						contractPathDao.save(contractPathEntity);
//					}catch(Exception e)
//					{
//						log.info("数据保存到附件表异常");
//						log.info(FileUtil.getStackTrace(e));
//						throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
//					}
					//保存到附件表
					for (Map<String, String> map : listFile) {
						ContractPathEntity contractPathEntity =  new ContractPathEntity();
						contractPathEntity.setAttName(map.get("name"));
						contractPathEntity.setType((byte) Integer.parseInt(map.get("type")));
						contractPathEntity.setFilePath(map.get("path"));				
						String originalFile = map.get("originalFilePath");
						contractPathEntity.setExtension(suffix(new File(originalFile)));
						contractPathEntity.setContractSerialNum(serialNum);
						contractPathEntity.setOriginalFilePath(map.get("originalFilePath"));
						contractPathEntity.setOriginalFileName(map.get("originalFileName"));
						contractPathEntity.setContractPath(contractPath);
						contractPathEntity.setCContract(cn);
						try{
							contractPathDao.save(contractPathEntity);
						}catch(Exception e)
						{
							log.info("数据保存到附件表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
					}
					
//					Map<String,String> map = gson.fromJson(specialCharacterNumber, Map.class);
					boolean flag = false;
					for(int i=0;i<customIds.length;i++)
					{
						//查询c_identity表
						IdentityEntity identity = null;
						try{
							identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,customIds[i]);
						}catch(Exception e)
						{
							log.info("根据appid和ucid查询用户表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null == identity)
						{
							//有缔约方没有注册
							log.info("有缔约方没有注册");
							throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+customIds[i],ConstantUtil.RETURN_USER_NOTEXIST[2]);
						}
//						if(platformEntity.getIsPdfSign() == 0) 
//						{
//							
//						}
//						//pdf标识位表
//						
//						PdfInfoEntity pdfinfo = new PdfInfoEntity();
//						pdfinfo.setAppId(appId);
//						pdfinfo.setContractId(cn.getId());
//						pdfinfo.setNumber(map.get(customIds[i]));
//						pdfinfo.setPlateformId(identityEntity.getId());
//						pdfinfo.setSerialNum(serialNum);
//						pdfinfo.setUcid(customIds[i]);
//						pdfinfo.setUserId(identity.getId());
//						pdfinfo.setSpecialCharacter(specialCharacter);
//						PdfInfoEntity p =  pdfInfoDao.save(pdfinfo);
//						if(null == p)
//						{
//							throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2]);
//						}
					
						//签署记录表
						SignRecordEntity signRecord = new SignRecordEntity();
						signRecord.setCContract(cn);
						signRecord.setCIdentity(identity);
						signRecord.setOrignalFilename("");
						signRecord.setPrevSha1("");
						signRecord.setCurrentSha1("");
						//signRecord.setSignTime(null);
						signRecord.setSigndata("");
						signRecord.setCUkeyInfo(null);
						signRecord.setSignStatus((byte) 0);
						signRecord.setMark("");
						signRecord.setSignMode((byte) 0);
						SignRecordEntity sr = null;
						try{
							sr = signRecordDao.save(signRecord);
						}catch(Exception e)
						{
							log.info("数据保存到签署表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != sr)
						{
							flag = true;
						}
					}
					if(flag)
					{
						log.info("合同创建成功");
						String contractStr = "";
						/*扣费
						 * 如用用户为平台用户直接口平台的费用,否则扣本人账户的
						 */
//						if(identityEntity.getIsAdmin() == 1)//为1是admin权限,直接扣平台费用
						if(Integer.parseInt(chargeType) == 0)
						{
							//查询平台帐号进行扣费				
							IdentityEntity chargingIdentityEntity = null;
							try{
								chargingIdentityEntity = identityDao.queryChargingIdentityByPlateformId(platformEntity);
							}catch(Exception e)
							{
								log.info("平台计费帐号查询异常");
								log.info(FileUtil.getStackTrace(e));
								throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
							}
							if(null == chargingIdentityEntity)
							{
								throw new ServiceException(ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[0],ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[1],ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[2]);
							}
							
							UserServiceEntity userServiceEntity = null;
							try {
								userServiceEntity = userServiceDao.findByUserIdAndPayCode(chargingIdentityEntity.getId(), "contract");
							} catch (Exception e) {
								log.info(FileUtil.getStackTrace(e));
								throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
							}
							if(null != userServiceEntity)
							{
								if(userServiceEntity.getPayType() == 1)//按次计费
								{
									userAccountService.reduce_times(chargingIdentityEntity.getId(), customIds.length, "contract", serialNum);
								}
								else if(userServiceEntity.getPayType() == 2)//按份数计费
								{
									userAccountService.reduce_times(chargingIdentityEntity.getId(), 1, "contract", serialNum);
								}
								else
								{
									throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
								}
							}
							else
							{
								throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
							}
							
						}
						else
						{
							UserServiceEntity userServiceEntity = null;
							try {
								userServiceEntity = userServiceDao.findByUserIdAndPayCode(identityEntity.getId(), "contract");
							} catch (Exception e) {
								log.info(FileUtil.getStackTrace(e));
								throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
							}
							if(null != userServiceEntity)
							{
								if(userServiceEntity.getPayType() == 1)//按次计费
								{
									userAccountService.reduce_times(identityEntity.getId(), customIds.length, "contract", serialNum);
								}
								else if(userServiceEntity.getPayType() == 2)//按份数计费
								{
									userAccountService.reduce_times(identityEntity.getId(), 1, "contract", serialNum);
								}
								else
								{
									throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
								}
							}
							else
							{
								throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
							}
						}						
						try {
							contractStr = JSON.toJSONString(cn);
						} catch (Exception e) {
							throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2]);
						}						
						returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],contractStr);
					}
					else
					{
						log.info("创建创建初始化签署表失败");
						throw new ServiceException(ConstantUtil.CREATECONTRACT_FAIL[0],ConstantUtil.CREATECONTRACT_FAIL[1],ConstantUtil.CREATECONTRACT_FAIL[2]);
					}
				}
				else{
					log.info("保存合同表失败");
					throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2]);
				}

			}
			else
			{
				log.info("模板不存在");
				throw new ServiceException(ConstantUtil.TEMPER_NOT_EXIST[0],ConstantUtil.TEMPER_NOT_EXIST[1],ConstantUtil.TEMPER_NOT_EXIST[2]);
			}
		
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));			
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}

	/**
	 * 基于模板创建合同
	 * @param appid 平台ID,由云签平台提供
	 * @param time 当前时间戳(精确到秒)
	 * @param sign 由appid, flow, time, key生成
	 * @param ucid 创建人用户ID,第三方业务平台用户的唯一身份
	 * @param customid 各缔约方ID,缔约多方，必须是2个人以上
	 * @param fromcustom 接入系统名称,来自的平台名称
	 * @param orderid 接入系统业务流水号,由第三方系统生成流水号;必须唯一
	 * @param title 合同标题
	 * @param template 模板名称
	 * @param offertime 签署过期时间
	 * @param pname 项目名称
	 * @param tempNumber 模板编号
	 * @param data 项目名称 Data内容为json格式；参数命名按照模板中定义的键名依次传递 例如{“title”:”中国云签”,”company”:”买卖网”}
	 * @param noapprovalid 免审批的ID，中铁专用
	 */
	//TODO
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData mmecCreate(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
			checkParam(datamap);
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String customId = StringUtil.nullToString(datamap.get("customId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
	//		String fromCustom = StringUtil.nullToString(datamap.get("fromCustom"));
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String offerTime = StringUtil.nullToString(datamap.get("offerTime"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String pname = StringUtil.nullToString(datamap.get("pname"));
			String tempNumber = StringUtil.nullToString(datamap.get("tempNumber"));
			String tempData = StringUtil.nullToString(datamap.get("tempData"));
			String serialNum = StringUtil.nullToString(datamap.get("serialNum"));
			String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
			String price =  StringUtil.nullToString(datamap.get("price"));
			int opt = this.getOptForm(optFrom);
			/*
			 *扣费方式 
			 *0 扣平台方的钱
			 */
			String chargeType = StringUtil.nullToString(datamap.get("chargeType"));
	//		log.info("创建合同入参为,appId="+appId+",customId="+customId+",ucid="+ucid+",orderId"+orderId+",title="+title+",offerTime="+offerTime+",tempNumber="+tempNumber+",tempData="+tempData+",optFrom="+optFrom);
			//判断缔约方有重复值
			String [] customIds = customId.split(",");
			if(StringUtil.checkRepeat(customIds))
			{
				log.info("缔约方有重复值");
				returnData =  new ReturnData(ConstantUtil.RETURN_CUSTOMID_HAS_DULP[0],ConstantUtil.RETURN_CUSTOMID_HAS_DULP[1], ConstantUtil.RETURN_CUSTOMID_HAS_DULP[2], "");
				return returnData;
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				returnData =  new ReturnData(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2], "");
				return returnData;
			}
			
			//判断是否重复创建合同		
			ContractEntity contractDuplicate = null;
			ContractEntity contractDuplicate_SerialNum = null;
			try{
				contractDuplicate = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
				contractDuplicate_SerialNum = contractDao.findContractBySerialNum(serialNum);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			
			if((null != contractDuplicate) || (null != contractDuplicate_SerialNum))
			{
				log.info("订单号已经存在，不能重复创建合同");
				returnData =  new ReturnData(ConstantUtil.ORDERID_HAS_EXIST[0],ConstantUtil.ORDERID_HAS_EXIST[1], ConstantUtil.ORDERID_HAS_EXIST[2], "");
				return returnData;
			}
			//判断ucid是否包含在customId
			if(!StringUtil.isContain(ucid,customIds))
			{
				returnData =  new ReturnData(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2], ucid);
				return returnData;
//				throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2]);

			}
			//查询余额
	
//			returnData = templateLoad(optFrom,serialNum,tempNumber,tempData,platformEntity,ucid,offerTime, startTime,endTime, title, orderId,customIds,customId,price,Integer.parseInt(chargeType));
			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime))
			{
				log.info("当前时间大于过期时间");
				throw new ServiceException(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2]);
			}
			//根据模板编号,查询模板
			ContractTemplateEntity contractTemplate = null;
			try{
				contractTemplate = contractTemplateDao.findContractTemplateByTempNumber(tempNumber,platformEntity.getAppId());
			}catch(Exception e)
			{
				log.info("查询模板表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}			
			StringBuffer signData = null;
			if(null != contractTemplate)
			{	
				String tempPath = contractTemplate.getFilePath();
				String fileName = DateUtil.toDateYYYYMMDDHHMM1();
				if(StringUtil.isNull(serialNum))
				{
					serialNum = getOrderCode();
				}
				//保存合同表,签署信息表
				IdentityEntity identityEntity = null;
				try{
					identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
				}catch(Exception e)
				{
					log.info("根据appid和平台用户名称查询用户异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null == identityEntity)
				{
					throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],ConstantUtil.RETURN_CUST_NOT_EXIST[1],ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
				}
				
				
				String filePath = FileUtil.createContractFolder(serialNum);
				//动态添加数据到模板文件,另存为html文件
				File file = new File(filePath);
				if (!file.exists())
				{
					file.mkdirs();
				}
				if(!FileUtil.appendHtml(tempData,tempPath,filePath+fileName+".html",filePath)) 
				{
					log.info("模板数据装载错误");
					throw new ServiceException(ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[0],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[1],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[2]);
				}
				//将添加数据后的html文件转为pdf
				String pdfPathfile = filePath+fileName+".pdf";
				PDFTool.htmlToPdf(filePath+fileName+".html", pdfPathfile);
//				PDFTool.htmlToPdfLibreOffice(filePath+fileName+".html", filePath);
				//对pdf文件进行sha1加密
				File f = new File(pdfPathfile);	
				signData = new StringBuffer("Z_1_");
				signData.append(fileName);
				signData.append(".pdf");
				signData.append("=");
				String sha1 = SHA_MD.encodeFileSHA1(f).toHexString();
				signData.append(sha1);
				signData.append("&");

				File imgFolder = new File(filePath+"img"+"/"+fileName);
				if (!imgFolder.exists())
				{
					imgFolder.mkdirs();
				}
				Map<String, String> pdfTomImgMap = new HashMap<String, String>();
				pdfTomImgMap.put("optFrom", "NULL");
				pdfTomImgMap.put("appId", platformEntity.getAppId());
				pdfTomImgMap.put("ucid", identityEntity.getAccount());
				pdfTomImgMap.put("IP", "NULL");
				PDFTool.pdfToImg(pdfPathfile, filePath+"img"+"/"+fileName,pdfTomImgMap);
				ContractEntity contract = new ContractEntity();
				contract.setSerialNum(serialNum);
				contract.setType("");
				contract.setUpdateTime(new Date());
				contract.setOptFrom((byte)opt);
				contract.setCreateTime(new Date());
				try {
					contract.setDeadline(DateUtil.stringToDate(offerTime));
				} catch (ParseException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.OFFTIME_IS_ILLEGAL[0],ConstantUtil.OFFTIME_IS_ILLEGAL[1],ConstantUtil.OFFTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
				}
				if(!"".equals(startTime))
				{
					try {
						contract.setStartTime(DateUtil.stringToDate(startTime));//合同开始时间
					} catch (ParseException e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.STARTTIME_IS_ILLEGAL[0],ConstantUtil.STARTTIME_IS_ILLEGAL[1],ConstantUtil.STARTTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
					}
				}
				contract.setStatus((byte) 0);
				contract.setIsShow((byte)0);
				contract.setMark("");
				contract.setCPlatform(platformEntity);
				contract.setSignPlaintext(signData.toString());
				contract.setFinishtime(null);//创建初始化完成时间为null
				if(platformEntity.getIsPdfSign() == 0)
				{
					contract.setIsPdfSign("N");
				}
				else
				{
					contract.setIsPdfSign("Y");
				}
				if(!"".equals(endTime))
				{
					try {
						contract.setEndTime(DateUtil.stringToDate(endTime));//合同结束时间
					} catch (ParseException e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.ENDTIME_IS_ILLEGAL[0],ConstantUtil.ENDTIME_IS_ILLEGAL[1],ConstantUtil.ENDTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
					}
				}
				contract.setCreator(identityEntity.getId());
				contract.setKeyword("");
				contract.setPaymentType((byte) 0);
				contract.setContractType("");//合同类型
				contract.setOperator("");//
				contract.setSha1(sha1);
				contract.setTitle(title);
				contract.setOrderId(orderId);
				contract.setOtheruids(customId);
				contract.setTempNumber(tempNumber);
				if(!"".equals(price))
				{	
					contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble(price)))));
				}
				else
				{
//					contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble("0.00")))));
					contract.setPrice(BigDecimal.valueOf(Double.parseDouble("0.00")));
				}
				
				ContractEntity cn = null;
				try{
					cn = contractDao.save(contract);
				}catch(Exception e)
				{
					log.info("数据保存到合同表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				
				if(null != cn)
				{
					//保存到附件表
					ContractPathEntity contractPathEntity =  new ContractPathEntity();
					contractPathEntity.setAttName(fileName);
					contractPathEntity.setType((byte) 1);
					contractPathEntity.setFilePath(pdfPathfile);
					contractPathEntity.setOriginalFilePath(pdfPathfile);
					contractPathEntity.setExtension("pdf");
					contractPathEntity.setContractSerialNum(serialNum);
					contractPathEntity.setContractPath(filePath);
					contractPathEntity.setCContract(cn);
					try{
						contractPathDao.save(contractPathEntity);
					}catch(Exception e)
					{
						log.info("数据保存到附件表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
					//签署记录表
					SignRecordEntity signRecord = new SignRecordEntity();
					boolean flag = false;
					for(int i=0;i<customIds.length;i++)
					{
						//查询c_identity表
						IdentityEntity identity = null;
						try{
							identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,customIds[i]);
						}catch(Exception e)
						{
							log.info("根据appid和ucid查询用户表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null == identity)
						{
							//有缔约方没有注册
							log.info("有缔约方没有注册");
							throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+customIds[i],ConstantUtil.RETURN_USER_NOTEXIST[2]);
						}

						signRecord.setCContract(cn);
						signRecord.setCIdentity(identity);
						signRecord.setOrignalFilename("");
						signRecord.setPrevSha1("");
						signRecord.setCurrentSha1("");
						//signRecord.setSignTime(null);
						signRecord.setSigndata("");
						signRecord.setCUkeyInfo(null);
						signRecord.setSignStatus((byte) 0);
						signRecord.setMark("");
						signRecord.setSignMode((byte) 0);
						SignRecordEntity sr = null;
						try{
							sr = signRecordDao.save(signRecord);
						}catch(Exception e)
						{
							log.info("数据保存到签署表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != sr)
						{
							flag = true;
						}
					}
					if(flag)
					{
						log.info("合同创建成功");
						String contractStr = "";
						/*扣费
						 * 如用用户为平台用户直接口平台的费用,否则扣本人账户的
						 */
//						if(identityEntity.getIsAdmin() == 1)//为1是admin权限,直接扣平台费用
						if(Integer.parseInt(chargeType) == 0)
						{
							//查询平台帐号进行扣费				
							IdentityEntity chargingIdentityEntity = null;
							try{
								chargingIdentityEntity = identityDao.queryChargingIdentityByPlateformId(platformEntity);
							}catch(Exception e)
							{
								log.info("平台计费帐号查询异常");
								log.info(FileUtil.getStackTrace(e));
								throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
							}
							if(null == chargingIdentityEntity)
							{
								throw new ServiceException(ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[0],ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[1],ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[2]);
							}
							
							UserServiceEntity userServiceEntity = null;
							try {
								userServiceEntity = userServiceDao.findByUserIdAndPayCode(chargingIdentityEntity.getId(), "contract");
							} catch (Exception e) {
								log.info(FileUtil.getStackTrace(e));
								throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
							}
							if(null != userServiceEntity)
							{
								if(userServiceEntity.getPayType() == 1)//按次计费
								{
									userAccountService.reduce_times(chargingIdentityEntity.getId(), customIds.length, "contract", serialNum);
								}
								else if(userServiceEntity.getPayType() == 2)//按份数计费
								{
									userAccountService.reduce_times(chargingIdentityEntity.getId(), 1, "contract", serialNum);
								}
								else
								{
									throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
								}
							}
							else
							{
								throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
							}
							
						}
						else
						{
							UserServiceEntity userServiceEntity = null;
							try {
								userServiceEntity = userServiceDao.findByUserIdAndPayCode(identityEntity.getId(), "contract");
							} catch (Exception e) {
								log.info(FileUtil.getStackTrace(e));
								throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
							}
							if(null != userServiceEntity)
							{
								if(userServiceEntity.getPayType() == 1)//按次计费
								{
									userAccountService.reduce_times(identityEntity.getId(), customIds.length, "contract", serialNum);
								}
								else if(userServiceEntity.getPayType() == 2)//按份数计费
								{
									userAccountService.reduce_times(identityEntity.getId(), 1, "contract", serialNum);
								}
								else
								{
									throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
								}
							}
							else
							{
								throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
							}
						}						
						try {
							contractStr = JSON.toJSONString(cn);
						} catch (Exception e) {
							throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2]);
						}						
						returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],contractStr );
					}
					else
					{
						log.info("创建创建初始化签署表失败");
						throw new ServiceException(ConstantUtil.CREATECONTRACT_FAIL[0],ConstantUtil.CREATECONTRACT_FAIL[1],ConstantUtil.CREATECONTRACT_FAIL[2]);
					}
				}
				else{
					log.info("保存合同表失败");
					throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2]);
				}

			}
			else
			{
				log.info("模板不存在");
				throw new ServiceException(ConstantUtil.TEMPER_NOT_EXIST[0],ConstantUtil.TEMPER_NOT_EXIST[1],ConstantUtil.TEMPER_NOT_EXIST[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));			
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	/**
	 * 基于word模板创建合同
	 * @param appid 平台ID,由云签平台提供
	 * @param time 当前时间戳(精确到秒)
	 * @param sign 由appid, flow, time, key生成
	 * @param ucid 创建人用户ID,第三方业务平台用户的唯一身份
	 * @param customid 各缔约方ID,缔约多方，必须是2个人以上
	 * @param fromcustom 接入系统名称,来自的平台名称
	 * @param orderid 接入系统业务流水号,由第三方系统生成流水号;必须唯一
	 * @param title 合同标题
	 * @param template 模板名称
	 * @param offertime 签署过期时间
	 * @param pname 项目名称
	 * @param tempNumber 模板编号
	 * @param data 项目名称 Data内容为json格式；参数命名按照模板中定义的键名依次传递 例如{“title”:”中国云签”,”company”:”买卖网”}
	 * @param noapprovalid 免审批的ID，中铁专用
	 */
	//TODO
//	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData mmecWordTemplateCreate(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
			checkParam(datamap);
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String customId = StringUtil.nullToString(datamap.get("customId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
	//		String fromCustom = StringUtil.nullToString(datamap.get("fromCustom"));
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String offerTime = StringUtil.nullToString(datamap.get("offerTime"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String pname = StringUtil.nullToString(datamap.get("pname"));
			String tempNumber = StringUtil.nullToString(datamap.get("tempNumber"));
			String tempData = StringUtil.nullToString(datamap.get("tempData"));
			String serialNum = StringUtil.nullToString(datamap.get("serialNum"));
			String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
			String price =  StringUtil.nullToString(datamap.get("price"));
			int opt = this.getOptForm(optFrom);
			/*
			 *扣费方式 
			 *0 扣平台方的钱
			 */
			String chargeType = StringUtil.nullToString(datamap.get("chargeType"));
	//		log.info("创建合同入参为,appId="+appId+",customId="+customId+",ucid="+ucid+",orderId"+orderId+",title="+title+",offerTime="+offerTime+",tempNumber="+tempNumber+",tempData="+tempData+",optFrom="+optFrom);
			//判断缔约方有重复值
			String [] customIds = customId.split(",");
			if(StringUtil.checkRepeat(customIds))
			{
				log.info("缔约方有重复值");
				returnData =  new ReturnData(ConstantUtil.RETURN_CUSTOMID_HAS_DULP[0],ConstantUtil.RETURN_CUSTOMID_HAS_DULP[1], ConstantUtil.RETURN_CUSTOMID_HAS_DULP[2], "");
				return returnData;
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				returnData =  new ReturnData(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2], "");
				return returnData;
			}
			
			//判断是否重复创建合同		
			ContractEntity contractDuplicate = null;
			ContractEntity contractDuplicate_SerialNum = null;
			try{
				contractDuplicate = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
				contractDuplicate_SerialNum = contractDao.findContractBySerialNum(serialNum);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			
			if((null != contractDuplicate) || (null != contractDuplicate_SerialNum))
			{
				log.info("订单号已经存在，不能重复创建合同");
				returnData =  new ReturnData(ConstantUtil.ORDERID_HAS_EXIST[0],ConstantUtil.ORDERID_HAS_EXIST[1], ConstantUtil.ORDERID_HAS_EXIST[2], "");
				return returnData;
			}
			//判断ucid是否包含在customId
			if(!StringUtil.isContain(ucid,customIds))
			{
				returnData =  new ReturnData(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2], ucid);
				return returnData;
//				throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2]);

			}
			//查询余额
	
//			returnData = templateLoad(optFrom,serialNum,tempNumber,tempData,platformEntity,ucid,offerTime, startTime,endTime, title, orderId,customIds,customId,price,Integer.parseInt(chargeType));
			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime))
			{
				log.info("当前时间大于过期时间");
				throw new ServiceException(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2]);
			}
			//根据模板编号,查询模板
			ContractTemplateEntity contractTemplate = null;
			try{
				contractTemplate = contractTemplateDao.findContractTemplateByTempNumber(tempNumber,platformEntity.getAppId());
			}catch(Exception e)
			{
				log.info("查询模板表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}			
			StringBuffer signData = null;
			if(null != contractTemplate)
			{	
				String tempPath = contractTemplate.getFilePath();
				String fileName = DateUtil.toDateYYYYMMDDHHMM1();
				if(StringUtil.isNull(serialNum))
				{
					serialNum = getOrderCode();
				}
				//保存合同表,签署信息表
				IdentityEntity identityEntity = null;
				try{
					identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
				}catch(Exception e)
				{
					log.info("根据appid和平台用户名称查询用户异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null == identityEntity)
				{
					throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],ConstantUtil.RETURN_CUST_NOT_EXIST[1],ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
				}
				
				
				String filePath = FileUtil.createContractFolder(serialNum);
				//动态添加数据到模板文件,另存为html文件
				File file = new File(filePath);
				if (!file.exists())
				{
					file.mkdirs();
				}
				if(!FileUtil.appendWord(tempData,tempPath,filePath+fileName+".html")) 
				{
					log.info("模板数据装载错误");
					throw new ServiceException(ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[0],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[1],ConstantUtil.TEMPLATE_DATA_LOAD_ERROR[2]);
				}
				//将添加数据后的html文件转为pdf
				String pdfPathfile = filePath+fileName+".pdf";
				PDFTool.htmlToPdf(filePath+fileName+".html", pdfPathfile);
				//对pdf文件进行sha1加密
				File f = new File(pdfPathfile);	
				signData = new StringBuffer("Z_1_");
				signData.append(fileName);
				signData.append(".pdf");
				signData.append("=");
				String sha1 = SHA_MD.encodeFileSHA1(f).toHexString();
				signData.append(sha1);
				signData.append("&");

				File imgFolder = new File(filePath+"img"+"/"+fileName);
				if (!imgFolder.exists())
				{
					imgFolder.mkdirs();
				}
				Map<String, String> pdfTomImgMap = new HashMap<String, String>();
				pdfTomImgMap.put("optFrom", "NULL");
				pdfTomImgMap.put("appId", platformEntity.getAppId());
				pdfTomImgMap.put("ucid", identityEntity.getAccount());
				pdfTomImgMap.put("IP", "NULL");
				PDFTool.pdfToImg(pdfPathfile, filePath+"img"+"/"+fileName,pdfTomImgMap);
				ContractEntity contract = new ContractEntity();
				contract.setSerialNum(serialNum);
				contract.setType("");
				contract.setUpdateTime(new Date());
				contract.setOptFrom((byte)opt);
				contract.setCreateTime(new Date());
				try {
					contract.setDeadline(DateUtil.stringToDate(offerTime));
				} catch (ParseException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.OFFTIME_IS_ILLEGAL[0],ConstantUtil.OFFTIME_IS_ILLEGAL[1],ConstantUtil.OFFTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
				}
				if(!"".equals(startTime))
				{
					try {
						contract.setStartTime(DateUtil.stringToDate(startTime));//合同开始时间
					} catch (ParseException e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.STARTTIME_IS_ILLEGAL[0],ConstantUtil.STARTTIME_IS_ILLEGAL[1],ConstantUtil.STARTTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
					}
				}
				contract.setStatus((byte) 0);
				contract.setIsShow((byte)0);
				contract.setMark("");
				contract.setCPlatform(platformEntity);
				contract.setSignPlaintext(signData.toString());
				contract.setFinishtime(null);//创建初始化完成时间为null
				if(platformEntity.getIsPdfSign() == 0)
				{
					contract.setIsPdfSign("N");
				}
				else
				{
					contract.setIsPdfSign("Y");
				}
				if(!"".equals(endTime))
				{
					try {
						contract.setEndTime(DateUtil.stringToDate(endTime));//合同结束时间
					} catch (ParseException e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.ENDTIME_IS_ILLEGAL[0],ConstantUtil.ENDTIME_IS_ILLEGAL[1],ConstantUtil.ENDTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
					}
				}
				contract.setCreator(identityEntity.getId());
				contract.setKeyword("");
				contract.setPaymentType((byte) 0);
				contract.setContractType("");//合同类型
				contract.setOperator("");//
				contract.setSha1(sha1);
				contract.setTitle(title);
				contract.setOrderId(orderId);
				contract.setOtheruids(customId);
				contract.setTempNumber(tempNumber);
				if(!"".equals(price))
				{	
					contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble(price)))));
				}
				else
				{
//					contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble("0.00")))));
					contract.setPrice(BigDecimal.valueOf(Double.parseDouble("0.00")));
				}
				
				ContractEntity cn = null;
				try{
					cn = contractDao.save(contract);
				}catch(Exception e)
				{
					log.info("数据保存到合同表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				
				if(null != cn)
				{
					//保存到附件表
					ContractPathEntity contractPathEntity =  new ContractPathEntity();
					contractPathEntity.setAttName(fileName);
					contractPathEntity.setType((byte) 1);
					contractPathEntity.setFilePath(pdfPathfile);
					contractPathEntity.setOriginalFilePath(pdfPathfile);
					contractPathEntity.setExtension("pdf");
					contractPathEntity.setContractSerialNum(serialNum);
					contractPathEntity.setContractPath(filePath);
					contractPathEntity.setCContract(cn);
					try{
						contractPathDao.save(contractPathEntity);
					}catch(Exception e)
					{
						log.info("数据保存到附件表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
					//签署记录表
					SignRecordEntity signRecord = new SignRecordEntity();
					boolean flag = false;
					for(int i=0;i<customIds.length;i++)
					{
						//查询c_identity表
						IdentityEntity identity = null;
						try{
							identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,customIds[i]);
						}catch(Exception e)
						{
							log.info("根据appid和ucid查询用户表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null == identity)
						{
							//有缔约方没有注册
							log.info("有缔约方没有注册");
							throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+customIds[i],ConstantUtil.RETURN_USER_NOTEXIST[2]);
						}

						signRecord.setCContract(cn);
						signRecord.setCIdentity(identity);
						signRecord.setOrignalFilename("");
						signRecord.setPrevSha1("");
						signRecord.setCurrentSha1("");
						//signRecord.setSignTime(null);
						signRecord.setSigndata("");
						signRecord.setCUkeyInfo(null);
						signRecord.setSignStatus((byte) 0);
						signRecord.setMark("");
						signRecord.setSignMode((byte) 0);
						SignRecordEntity sr = null;
						try{
							sr = signRecordDao.save(signRecord);
						}catch(Exception e)
						{
							log.info("数据保存到签署表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != sr)
						{
							flag = true;
						}
					}
					if(flag)
					{
						log.info("合同创建成功");
						String contractStr = "";
						/*扣费
						 * 如用用户为平台用户直接口平台的费用,否则扣本人账户的
						 */
//						if(identityEntity.getIsAdmin() == 1)//为1是admin权限,直接扣平台费用
						if(Integer.parseInt(chargeType) == 0)
						{
							//查询平台帐号进行扣费				
							IdentityEntity chargingIdentityEntity = null;
							try{
								chargingIdentityEntity = identityDao.queryChargingIdentityByPlateformId(platformEntity);
							}catch(Exception e)
							{
								log.info("平台计费帐号查询异常");
								log.info(FileUtil.getStackTrace(e));
								throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
							}
							if(null == chargingIdentityEntity)
							{
								throw new ServiceException(ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[0],ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[1],ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[2]);
							}
							
							UserServiceEntity userServiceEntity = null;
							try {
								userServiceEntity = userServiceDao.findByUserIdAndPayCode(chargingIdentityEntity.getId(), "contract");
							} catch (Exception e) {
								log.info(FileUtil.getStackTrace(e));
								throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
							}
							if(null != userServiceEntity)
							{
								if(userServiceEntity.getPayType() == 1)//按次计费
								{
									userAccountService.reduce_times(chargingIdentityEntity.getId(), customIds.length, "contract", serialNum);
								}
								else if(userServiceEntity.getPayType() == 2)//按份数计费
								{
									userAccountService.reduce_times(chargingIdentityEntity.getId(), 1, "contract", serialNum);
								}
								else
								{
									throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
								}
							}
							else
							{
								throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
							}
							
						}
						else
						{
							UserServiceEntity userServiceEntity = null;
							try {
								userServiceEntity = userServiceDao.findByUserIdAndPayCode(identityEntity.getId(), "contract");
							} catch (Exception e) {
								log.info(FileUtil.getStackTrace(e));
								throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
							}
							if(null != userServiceEntity)
							{
								if(userServiceEntity.getPayType() == 1)//按次计费
								{
									userAccountService.reduce_times(identityEntity.getId(), customIds.length, "contract", serialNum);
								}
								else if(userServiceEntity.getPayType() == 2)//按份数计费
								{
									userAccountService.reduce_times(identityEntity.getId(), 1, "contract", serialNum);
								}
								else
								{
									throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
								}
							}
							else
							{
								throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
							}
						}						
						try {
							contractStr = JSON.toJSONString(cn);
						} catch (Exception e) {
							throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2]);
						}						
						returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],contractStr );
					}
					else
					{
						log.info("创建创建初始化签署表失败");
						throw new ServiceException(ConstantUtil.CREATECONTRACT_FAIL[0],ConstantUtil.CREATECONTRACT_FAIL[1],ConstantUtil.CREATECONTRACT_FAIL[2]);
					}
				}
				else{
					log.info("保存合同表失败");
					throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2]);
				}

			}
			else
			{
				log.info("模板不存在");
				throw new ServiceException(ConstantUtil.TEMPER_NOT_EXIST[0],ConstantUtil.TEMPER_NOT_EXIST[1],ConstantUtil.TEMPER_NOT_EXIST[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));			
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	/**
	 * 附件形式创建合同
	 */
	//TODO
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData mmecCreateAttachment(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
//			checkParam(datamap);
			Gson gson = new Gson();
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String customId = StringUtil.nullToString(datamap.get("customId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String offerTime = StringUtil.nullToString(datamap.get("offerTime"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String pname = StringUtil.nullToString(datamap.get("pname"));
			String price = StringUtil.nullToString(datamap.get("price"));
			String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
			String contractFile = StringUtil.nullToString(datamap.get("contractFile"));
			String attachmentFile = StringUtil.nullToString(datamap.get("attachmentFile"));
			/*
			 *扣费方式 
			 *0 扣平台方的钱
			 */
			String chargeType = StringUtil.nullToString(datamap.get("chargeType"));
	//		String contractFile =  "{\"fileName\":\"test\",\"filePath\":\"F:/office/test.doc\"}";//StringUtil.nullToString(datamap.get("contract"));
	//		String attachmentFile = "[{\"attName\":\"att1\",\"attPath\":\"F:/office/att1.docx\"},{\"attName\":\"att2\",\"attPath\":\"F:/office/att2.docx\"},{\"attName\":\"att3\",\"attPath\":\"F:/office/att3.pdf\"}]";//StringUtil.nullToString(datamap.get("attachment"));
		
			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime))
			{
				log.info("当前时间大于过期时间");
				throw new ServiceException(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2]);
			}
	//		String contractFile =  "{\"fileName\":\"test\",\"filePath\":\"/home/core/centerService/test.doc\"}";//StringUtil.nullToString(datamap.get("contract"));
	//		String attachmentFile = "[{\"attName\":\"att1\",\"attPath\":\"/home/core/centerService/att1.docx\"},{\"attName\":\"att2\",\"attPath\":\"/home/core/centerService/att2.docx\"},{\"attName\":\"att3\",\"attPath\":\"/home/core/centerService/att3.pdf\"}]";//StringUtil.nullToString(datamap.get("attachment"));
			//判断缔约方有重复值
			String [] customIds = customId.split(",");
			if(StringUtil.checkRepeat(customIds))
			{
				log.info("缔约方有重复值");
				throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_HAS_DULP[0],ConstantUtil.RETURN_CUSTOMID_HAS_DULP[1], ConstantUtil.RETURN_CUSTOMID_HAS_DULP[2]);
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			String serialNum = getOrderCode();
			//保存合同表,签署信息表
			IdentityEntity identityEntity = null;
			try{
				identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
			}catch(Exception e)
			{
				log.info("根据appid和平台用户名称查询用户异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == identityEntity)
			{
				throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],ConstantUtil.RETURN_CUST_NOT_EXIST[1],ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
			}
	
			
			//判断是否重复创建合同		
			ContractEntity contractDuplicate = null;
			try{
				contractDuplicate = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2]);
			}
			if((null != contractDuplicate))
			{
				log.info("订单号已经存在，不能重复创建合同");
				throw new ServiceException(ConstantUtil.ORDERID_HAS_EXIST[0],ConstantUtil.ORDERID_HAS_EXIST[1], ConstantUtil.ORDERID_HAS_EXIST[2]);
			}
			//判断ucid是否包含在customId
			if(!StringUtil.isContain(ucid,customIds))
			{
				//ucid是否包含在customId,返回
				log.info("customId不包含ucid");
				throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2]);
			}
			//拷贝合同和附件
			//用List<Map> 保存所有附件，便于下一步存入附件表
			List<Map<String,String>> listFile = new ArrayList<Map<String,String>>();
			Map<String,String> attrMAP = new HashMap<String,String>();	
			String contractPath = FileUtil.createContractFolder(serialNum);
			//动态添加数据到模板文件,另存为html文件
			File file = new File(contractPath);
			if (!file.exists())
			{
				file.mkdirs();
			}
			String attachmentPath = contractPath +"attachment/";//附件目录
			File attrFile = new File(attachmentPath);
			if (!attrFile.exists())
			{
				attrFile.mkdirs();
			}
			Map<String,String> contractMessage = gson.fromJson(contractFile, Map.class);//主合同文件
			String contractFilePath = contractMessage.get("filePath");
			String contractFileName = contractMessage.get("fileName");
			File contractFileSha1 = new File(contractFilePath);
			String masterFile = contractPath+contractFileSha1.getName();
			FileUtil.copyFolder(new File(contractFilePath),new File(masterFile));//拷贝
			String pdfPath = "";
			File filePdf = null;//签名原文
			String imgType = IConf.getValue("IMGTYPE");			//图片类型
			if("HTML".equals(suffix(contractFileSha1).toUpperCase()) || "DOC".equals(suffix(contractFileSha1).toUpperCase()) || "DOCX".equals(suffix(contractFileSha1).toUpperCase()))
			{
				pdfPath = contractPath + contractFileName + ".pdf";
				if("HTML".equals(suffix(contractFileSha1).toUpperCase()))
				{
					PDFTool.htmlToPdf(masterFile, pdfPath);
				}
				else
				{
//					PDFTool.htmlToPdfLibreOffice(masterFile, contractPath);//合同文件转pdf
					PDFTool.wordToPdf(masterFile, pdfPath);//合同文件转pdf
				}				
				attrMAP.put("path", pdfPath);
				attrMAP.put("originalFilePath", masterFile);//原文路径
				filePdf = new File(masterFile);
			}
			else if(imgType.contains(suffix(contractFileSha1).toUpperCase()))
			{				
				Map<String, String> map = new HashMap<String, String>();
				map.put("masterFile", masterFile);
				Map<String,Integer> imgInfo = getImgWidthAndHeight(masterFile);
				double width = imgInfo.get("width");
				double height = imgInfo.get("height");				
				DecimalFormat df = new DecimalFormat("###.000");				
				double zoom = 0.0;
				int heightStr = 0;
				int widthStr = 0;
				if(height > width)
				{
					zoom = Double.valueOf(df.format(width / height)); // 合同图片缩放比例,保留小数点后三位  (小于1)
					if(height > 800)
					{
						heightStr = 842;
						widthStr = (int)(heightStr * zoom);
					}
					else
					{
						heightStr = (int)height;
						widthStr = (int)width;
					}
				}
				else
				{
					zoom = Double.valueOf(df.format(height/width)); // 合同图片缩放比例,保留小数点后三位 (小于1)
					if(width > 542)
					{
						widthStr = 542;
						heightStr = (int)(widthStr * zoom);
					}
					else
					{
						heightStr = (int) height;
						widthStr = (int) width;
					}
				}
				System.out.println("heightStr="+heightStr+"widthStr="+widthStr);				
				map.put("width", String.valueOf(widthStr));
				map.put("height", String.valueOf(heightStr));
				map.put("contractPath", contractPath);
				map.put("imgPath", masterFile);
				map.put("key", "masterFile");
				FileUtil.appendImg(map, IConf.getValue("CONTRACTPATH")+"wechatImg.html", contractPath+contractFileName+".html");
				pdfPath = contractPath + contractFileName + ".pdf";
//				PDFTool.htmlToPdfLibreOffice(contractPath+contractFileName+".html", contractPath);//合同文件转pdf
				PDFTool.htmlToPdf(contractPath+contractFileName+".html", pdfPath);//合同文件转pdf
				attrMAP.put("path", pdfPath);
				attrMAP.put("originalFilePath", masterFile);//原文路径
				filePdf = new File(masterFile);
			}
			else
			{
				pdfPath = contractPath + contractFileSha1.getName();
				attrMAP.put("originalFilePath", pdfPath);//原文路径
				attrMAP.put("path", masterFile);
				filePdf = new File(pdfPath);
			}
			
			StringBuffer signOriginalText = new StringBuffer("Z_1_");
			signOriginalText.append(filePdf.getName());
			signOriginalText.append("=");
			String sha1 = SHA_MD.encodeFileSHA1(filePdf).toHexString();
			signOriginalText.append(sha1);
			signOriginalText.append("&");
			attrMAP.put("name", contractFileName);
	//		attrMAP.put("path", masterFile);
			attrMAP.put("type", "1");
			String attOriginalName_main = contractMessage.get("attOriginalName");
			attrMAP.put("originalFileName", attOriginalName_main);//主文件原文件名字
	
			listFile.add(attrMAP);
			//将pdf转为图片
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", platformEntity.getAppId());
			pdfTomImgMap.put("ucid", identityEntity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(pdfPath, contractPath+"img"+"/"+contractFileName,pdfTomImgMap);
			//附件
			List<Map<String,String>> list = null;
			if(!"".equals(attachmentFile))
			{			
				try {
					list = gson.fromJson(attachmentFile, List.class);
				} catch (JsonSyntaxException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.JSONSYNTAXEXCEPTION[0],ConstantUtil.JSONSYNTAXEXCEPTION[1],ConstantUtil.JSONSYNTAXEXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null!=list && list.size()>0)
				{
					if(list.size()>5)
					{
						throw new ServiceException(ConstantUtil.ATTACHMENT_NUMBER[0],ConstantUtil.ATTACHMENT_NUMBER[1],ConstantUtil.ATTACHMENT_NUMBER[2]);							
					}
					for(int i=0; i<list.size();i++)
					{
						Map<String,String> mapAttr = list.get(i);
						String attrName = mapAttr.get("attName");
						File attachment = new File(mapAttr.get("attPath"));
						String attPath = attachmentPath+attachment.getName();
						File attPathFile = new File(attPath);
//						File f = new File(attPath);
						FileUtil.copyFolder(attachment,attPathFile);//拷贝
						Map<String,String> attrMAPTemp = new HashMap<String,String>();
						attrMAPTemp.put("originalFilePath", attPath);//附件原文路径
						if("HTML".equals(suffix(attPathFile).toUpperCase()) || "DOC".equals(suffix(attPathFile).toUpperCase()) || "DOCX".equals(suffix(attPathFile).toUpperCase()))
						{	
							if("HTML".equals(suffix(attPathFile).toUpperCase()))
							{
								PDFTool.htmlToPdf(attPath, attachmentPath+attrName+".pdf");//合同文件
							}
							else
							{
//								PDFTool.htmlToPdfLibreOffice(attPath, attachmentPath);//合同文件
								PDFTool.wordToPdf(attPath, attachmentPath+attrName+".pdf");//合同文件
							}
							attrMAPTemp.put("path", attachmentPath+attrName+".pdf");
							PDFTool.pdfToImg(attachmentPath+attrName+".pdf", attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}
						else if(imgType.contains(suffix(attPathFile).toUpperCase()))
						{
							Map<String, String> map = new HashMap<String, String>();
							map.put("key", "fujian");
							Map<String,Integer> imgInfo = getImgWidthAndHeight(attPath);
//							map.put("width", String.valueOf(imgInfo.get("width")));
//							map.put("height", String.valueOf(imgInfo.get("height")));
							
							double width = imgInfo.get("width");
							double height = imgInfo.get("height");				
							DecimalFormat df = new DecimalFormat("###.000");				
							double zoom = 0.0;
							int heightStr = 0;
							int widthStr = 0;
							if(height > width)
							{
								zoom = Double.valueOf(df.format(width / height)); // 合同图片缩放比例,保留小数点后三位  (小于1)
								if(height > 800)
								{
									heightStr = 842;
									widthStr = (int)(heightStr * zoom);
								}
								else
								{
									heightStr = (int)height;
									widthStr = (int)width;
								}
							}
							else
							{
								zoom = Double.valueOf(df.format(height/width)); // 合同图片缩放比例,保留小数点后三位 (小于1)
								if(width > 542)
								{
									widthStr = 542;
									heightStr = (int)(widthStr * zoom);
								}
								else
								{
									heightStr = (int) height;
									widthStr = (int) width;
								}
							}
							System.out.println("heightStr="+heightStr+"widthStr="+widthStr);				
							map.put("width", String.valueOf(widthStr));
							map.put("height", String.valueOf(heightStr));
							map.put("contractPath", attachmentPath);
							map.put("imgPath", attPath);
							
							attrMAPTemp.put("path", attachmentPath+attrName+".pdf");
							FileUtil.appendImg(map, IConf.getValue("CONTRACTPATH")+"wechatImg.html", attachmentPath+attrName+".html");
//							PDFTool.htmlToPdfLibreOffice(attachmentPath+attrName+".html", attachmentPath);//合同文件转pdf
							PDFTool.htmlToPdf(attachmentPath+attrName+".html", attachmentPath+attrName+".pdf");//合同文件转pdf
							PDFTool.pdfToImg(attachmentPath+attrName+".pdf", attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}
						else
						{
							attrMAPTemp.put("path", attachmentPath+attPathFile.getName());
							PDFTool.pdfToImg(attPath, attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}					
						attrMAPTemp.put("name", attrName);
	//					attrMAPTemp.put("path", attachmentPath+f.getName());
						attrMAPTemp.put("type", "2");//附件
						String attOriginalName = mapAttr.get("attOriginalName");
						attrMAPTemp.put("originalFileName", attOriginalName);//附件原文件名字
						listFile.add(attrMAPTemp);
						String attrSha1 = SHA_MD.encodeFileSHA1(attPathFile).toHexString();
						signOriginalText.append("F_"+(i+1)+"_");
						signOriginalText.append(attPathFile.getName());
						signOriginalText.append("=");
						signOriginalText.append(attrSha1);
						signOriginalText.append("&");
						
					}
				}
			}
		
			ContractEntity contract = new ContractEntity();
			int opt = this.getOptForm(optFrom);
			contract.setSerialNum(serialNum);
			contract.setType("");
			if(!"".equals(price))
			{	
				contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble(price)))));
			}
			else
			{
//				contract.setPrice(BigDecimal.valueOf(Double.parseDouble(new DecimalFormat("0.00").format(Double.parseDouble(price)))));
				contract.setPrice(BigDecimal.valueOf(Double.parseDouble("0.00")));
			}
			contract.setUpdateTime(new Date());
			contract.setOptFrom((byte)opt);
			contract.setCreator(identityEntity.getId());
			contract.setCreateTime(new Date());
			try {
				contract.setDeadline(DateUtil.stringToDate(offerTime));
			} catch (ParseException e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.OFFTIME_IS_ILLEGAL[0],ConstantUtil.OFFTIME_IS_ILLEGAL[1],ConstantUtil.OFFTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
			}
			/*if(!"".equals(startTime))
			{
				try {
					contract.setStartTime(DateUtil.stringToDate(startTime));//合同开始时间
				} catch (ParseException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.STARTTIME_IS_ILLEGAL[0],ConstantUtil.STARTTIME_IS_ILLEGAL[1],ConstantUtil.STARTTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
				}
			}	*/
			//开始时间取服务器时间
			contract.setStartTime(new Date());
			contract.setStatus((byte) 0);
			contract.setIsShow((byte)1);
			contract.setMark("");
			contract.setCPlatform(platformEntity);
			contract.setSignPlaintext(signOriginalText.toString());
			contract.setFinishtime(null);//创建初始化完成时间为null
			if(!"".equals(endTime))
			{
				try {
					contract.setEndTime(DateUtil.stringToDate(endTime));//合同结束时间
				} catch (ParseException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.ENDTIME_IS_ILLEGAL[0],ConstantUtil.ENDTIME_IS_ILLEGAL[1],ConstantUtil.ENDTIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
				}
			}
			if(platformEntity.getIsPdfSign() == 0)
			{
				contract.setIsPdfSign("N");
			}
			else
			{
				contract.setIsPdfSign("Y");
			}
			contract.setKeyword("");
			contract.setPaymentType((byte) 0);
			contract.setContractType("");//合同类型
			contract.setOperator("");//
			contract.setSha1(sha1);
			contract.setTitle(title);
			contract.setOrderId(orderId);
			contract.setOtheruids(customId);
			ContractEntity cn = null;
			try{
				cn = contractDao.save(contract);
			}catch(Exception e)
			{
				log.info("数据保存到合同表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != cn)
			{
				log.info("保存附件表");
				//保存到附件表
				for (Map<String, String> map : listFile) {
					ContractPathEntity contractPathEntity =  new ContractPathEntity();
					contractPathEntity.setAttName(map.get("name"));
					contractPathEntity.setType((byte) Integer.parseInt(map.get("type")));
					contractPathEntity.setFilePath(map.get("path"));				
					String originalFile = map.get("originalFilePath");
					contractPathEntity.setExtension(suffix(new File(originalFile)));
					contractPathEntity.setContractSerialNum(serialNum);
					contractPathEntity.setContractPath(contractPath);
					contractPathEntity.setOriginalFilePath(map.get("originalFilePath"));
					contractPathEntity.setOriginalFileName(map.get("originalFileName"));
					contractPathEntity.setCContract(cn);
					try{
						contractPathDao.save(contractPathEntity);
					}catch(Exception e)
					{
						log.info("数据保存到附件表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
				}
				
				//签署记录表
				SignRecordEntity signRecord = new SignRecordEntity();
				boolean flag = false;
				for(int i=0;i<customIds.length;i++)
				{
					//查询c_identity表
					IdentityEntity identity = null;
					try{
						identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,customIds[i]);
					}catch(Exception e)
					{
						log.info("根据appid和ucid查询用户表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2]);
					}
					if(null == identity)
					{
						//有缔约方没有注册
						log.info("有缔约方没有注册");
						throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+customIds[i],ConstantUtil.RETURN_USER_NOTEXIST[2]);
					}
					signRecord.setCContract(cn);
					signRecord.setCIdentity(identity);
					signRecord.setOrignalFilename("");
					signRecord.setPrevSha1("");
					signRecord.setCurrentSha1("");
					//signRecord.setSignTime(null);
					signRecord.setSigndata("");
					signRecord.setCUkeyInfo(null);
					signRecord.setSignStatus((byte) 0);
					signRecord.setMark("");
					signRecord.setSignMode((byte) 0);
					SignRecordEntity sr = null;
					try{
						sr = signRecordDao.save(signRecord);
					}catch(Exception e)
					{
						log.info("数据保存到签署表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
					if(null != sr)
					{
						flag = true;
					}
				}
				if(flag)
				{
					log.info("合同创建成功");
					String contractStr = "";
					/*扣费
					 * 如用用户为平台用户直接口平台的费用,否则扣本人账户的
					 */
//					if(identityEntity.getIsAdmin() == 1)//为1是admin权限,直接扣平台费用
					/*if(Integer.parseInt(chargeType) == 0)
					{
						//查询平台帐号进行扣费				
						IdentityEntity chargingIdentityEntity = null;
						try{
							chargingIdentityEntity = identityDao.queryChargingIdentityByPlateformId(platformEntity);
						}catch(Exception e)
						{
							log.info("平台计费帐号查询异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null == chargingIdentityEntity)
						{
							throw new ServiceException(ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[0],ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[1],ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[2]);
						}
						
						UserServiceEntity userServiceEntity = null;
						try {
							userServiceEntity = userServiceDao.findByUserIdAndPayCode(chargingIdentityEntity.getId(), "contract");
						} catch (Exception e) {
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != userServiceEntity)
						{
							if(userServiceEntity.getPayType() == 1)//按次计费
							{
								userAccountService.reduce_times(chargingIdentityEntity.getId(), customIds.length, "contract", serialNum);
							}
							else if(userServiceEntity.getPayType() == 2)//按份数计费
							{
								userAccountService.reduce_times(chargingIdentityEntity.getId(), 1, "contract", serialNum);
							}
							else
							{
								throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
							}
						}
						else
						{
							throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
						}
						
					}
					else
					{
						UserServiceEntity userServiceEntity = null;
						try {
							userServiceEntity = userServiceDao.findByUserIdAndPayCode(identityEntity.getId(), "contract");
						} catch (Exception e) {
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != userServiceEntity)
						{
							if(userServiceEntity.getPayType() == 1)//按次计费
							{
								userAccountService.reduce_times(identityEntity.getId(), customIds.length, "contract", serialNum);
							}
							else if(userServiceEntity.getPayType() == 2)//按份数计费
							{
								userAccountService.reduce_times(identityEntity.getId(), 1, "contract", serialNum);
							}
							else
							{
								throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
							}
						}
						else
						{
							throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
						}
					}*/
					
					
					
					
					try {
						contractStr = JSON.toJSONString(cn);
					} catch (Exception e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2]);
					}					
					returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],contractStr);
				}
				else
				{
					log.info("创建创建初始化签署表失败");
					throw new ServiceException(ConstantUtil.CREATECONTRACT_FAIL[0],ConstantUtil.CREATECONTRACT_FAIL[1],ConstantUtil.CREATECONTRACT_FAIL[2]);
				}
			}
			else{
				log.info("保存合同表失败");
				throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	/**
	 * 授权创建合同
	 */
	//TODO
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData authorCreate(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		log.info("授权创建合同");
		try
		{
			Gson gson = new Gson();
			String appId = StringUtil.nullToString(datamap.get("appId"));
			
			String authorize = StringUtil.nullToString(datamap.get("authorize"));//授权人
			String beAuthorized = StringUtil.nullToString(datamap.get("beAuthorized"));//被授权人
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String title = StringUtil.nullToString(datamap.get("title"));
			String authStartTime =  StringUtil.nullToString(datamap.get("authStartTime"));//授权开始时间
			String authEndTime =  StringUtil.nullToString(datamap.get("authEndTime"));//授权结束时间
			String authType = StringUtil.nullToString(datamap.get("authType"));//授权类型1 单次;2 多次
			String authSerialNum = StringUtil.nullToString(datamap.get("authSerialNum"));//授权合同编号(单次授权)
			String customId = StringUtil.nullToString(datamap.get("customId"));
			String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
			String authContractFile = StringUtil.nullToString(datamap.get("authContractFile"));
			String attachmentFile = StringUtil.nullToString(datamap.get("attachmentFile"));
			/*
			 *扣费方式 
			 *0 扣平台方的钱
			 */
			String chargeType = StringUtil.nullToString(datamap.get("chargeType"));
			//判断缔约方有重复值
			String [] customIds = customId.split(",");
			if(StringUtil.checkRepeat(customIds))
			{
				log.info("缔约方有重复值");
				returnData =  new ReturnData(ConstantUtil.RETURN_CUSTOMID_HAS_DULP[0],ConstantUtil.RETURN_CUSTOMID_HAS_DULP[1], ConstantUtil.RETURN_CUSTOMID_HAS_DULP[2], "");
				return returnData;
			}
			//判断ucid是否包含在customId
			if(!StringUtil.isContain(authorize,customIds))
			{				
				throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[0],ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[1], ConstantUtil.RETURN_CUSTOMID_IF_CONTAIN_UCID[2]);
			}
			//查看平台ID是否已经存在
			PlatformEntity platformEntity = platformDao.findPlatformByAppId(appId);
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			String serialNum = getOrderCode();
			//保存合同表,签署信息表
			IdentityEntity identityEntity = null;
			try{
				identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,authorize);
			}catch(Exception e)
			{
				log.info("根据appid和平台用户名称查询用户异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == identityEntity)
			{
				throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],ConstantUtil.RETURN_CUST_NOT_EXIST[1]+","+authorize,ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
			}
			//查询被授权人
			IdentityEntity beIdentityEntity = null;
			try{
				beIdentityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,beAuthorized);
			}catch(Exception e)
			{
				log.info("根据appid和平台用户名称查询用户异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == beIdentityEntity)
			{
				throw new ServiceException(ConstantUtil.RETURN_CUST_NOT_EXIST[0],ConstantUtil.RETURN_CUST_NOT_EXIST[1]+","+beAuthorized,ConstantUtil.RETURN_CUST_NOT_EXIST[2]);
			}
			//判断是否重复创建合同		
			ContractEntity contractDuplicate = null;
			try{
				contractDuplicate = contractDao.findContractByAppIAndOrderId(orderId, platformEntity);
			}catch (Exception e) {
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2]);
			}
			if((null != contractDuplicate))
			{
				log.info("订单号已经存在，不能重复创建合同");
				throw new ServiceException(ConstantUtil.ORDERID_HAS_EXIST[0],ConstantUtil.ORDERID_HAS_EXIST[1], ConstantUtil.ORDERID_HAS_EXIST[2]);
			}
			//查询重复授权问题
			UserAuthorityEntity userAuthority = userAuthorityDao.queryUserAuthorityByAuthorId(platformEntity.getId(), beIdentityEntity.getId(), identityEntity.getId());
			if(null != userAuthority)
			{
				ContractEntity temp = contractDao.findById(userAuthority.getAuthContractId());
				String temOrderId = "";
				if(null != temp)
				{
					temOrderId = temp.getOrderId();
				}
				throw new ServiceException(ConstantUtil.AUTHOR_HAS_EXIST[0],ConstantUtil.AUTHOR_HAS_EXIST[1]+",授权合同号:"+temOrderId, ConstantUtil.AUTHOR_HAS_EXIST[2]);
			}
			//拷贝合同和附件
			//用List<Map> 保存所有附件，便于下一步存入附件表
			List<Map<String,String>> listFile = new ArrayList<Map<String,String>>();
			Map<String,String> attrMAP = new HashMap<String,String>();	
			String contractPath = FileUtil.createContractFolder(serialNum);
			//动态添加数据到模板文件,另存为html文件
			File file = new File(contractPath);
			if (!file.exists())
			{
				file.mkdirs();
			}
			String attachmentPath = contractPath +"attachment/";//附件目录
			File attrFile = new File(attachmentPath);
			if (!attrFile.exists())
			{
				attrFile.mkdirs();
			}
			Map<String,String> contractMessage = gson.fromJson(authContractFile, Map.class);//主合同文件
			String contractFilePath = contractMessage.get("filePath");
			String contractFileName = contractMessage.get("fileName");
			File contractFileSha1 = new File(contractFilePath);
			String masterFile = contractPath+contractFileSha1.getName();
			FileUtil.copyFolder(new File(contractFilePath),new File(masterFile));//拷贝
			String pdfPath = "";
			File filePdf = null;//签名原文
			String imgType = IConf.getValue("IMGTYPE");			//图片类型
			if("HTML".equals(suffix(contractFileSha1).toUpperCase()) || "DOC".equals(suffix(contractFileSha1).toUpperCase()) || "DOCX".equals(suffix(contractFileSha1).toUpperCase()))
			{
				pdfPath = contractPath + contractFileName + ".pdf";
				if("HTML".equals(suffix(contractFileSha1).toUpperCase()))
				{
					PDFTool.htmlToPdf(masterFile, pdfPath);
				}
				else
				{
//					PDFTool.htmlToPdfLibreOffice(masterFile, contractPath);//合同文件转pdfC
					PDFTool.wordToPdf(masterFile, pdfPath);//合同文件转pdf
				}					
				attrMAP.put("path", pdfPath);
				attrMAP.put("originalFilePath", masterFile);//原文路径
				filePdf = new File(masterFile);
			}
			else if(imgType.contains(suffix(contractFileSha1).toUpperCase()))
			{				
				Map<String, String> map = new HashMap<String, String>();
				map.put("masterFile", masterFile);
				Map<String,Integer> imgInfo = getImgWidthAndHeight(masterFile);
				double width = imgInfo.get("width");
				double height = imgInfo.get("height");				
				DecimalFormat df = new DecimalFormat("###.000");				
				double zoom = 0.0;
				int heightStr = 0;
				int widthStr = 0;
				if(height > width)
				{
					zoom = Double.valueOf(df.format(width / height)); // 合同图片缩放比例,保留小数点后三位  (小于1)
					if(height > 800)
					{
						heightStr = 842;
						widthStr = (int)(heightStr * zoom);
					}
					else
					{
						heightStr = (int)height;
						widthStr = (int)width;
					}
				}
				else
				{
					zoom = Double.valueOf(df.format(height/width)); // 合同图片缩放比例,保留小数点后三位 (小于1)
					if(width > 542)
					{
						widthStr = 542;
						heightStr = (int)(widthStr * zoom);
					}
					else
					{
						heightStr = (int) height;
						widthStr = (int) width;
					}
				}
				log.info("heightStr="+heightStr+"widthStr="+widthStr);				
				map.put("width", String.valueOf(widthStr));
				map.put("height", String.valueOf(heightStr));
				map.put("contractPath", contractPath);
				map.put("imgPath", masterFile);
				map.put("key", "masterFile");
				
				FileUtil.appendImg(map, IConf.getValue("CONTRACTPATH")+"wechatImg.html", contractPath+contractFileName+".html");
				pdfPath = contractPath + contractFileName + ".pdf";
//				PDFTool.htmlToPdfLibreOffice(contractPath+contractFileName+".html", contractPath);//合同文件转pdf
				PDFTool.htmlToPdf(contractPath+contractFileName+".html", pdfPath);//合同文件转pdf
				attrMAP.put("path", pdfPath);
				attrMAP.put("originalFilePath", masterFile);//原文路径
				String attOriginalName_main = contractMessage.get("attOriginalName");
				attrMAP.put("originalFileName", attOriginalName_main);//主文件原文件名字
				filePdf = new File(masterFile);
			}
			else
			{
				pdfPath = contractPath + contractFileSha1.getName();
				attrMAP.put("originalFilePath", pdfPath);//原文路径
				attrMAP.put("path", masterFile);
				filePdf = new File(pdfPath);
			}
			
			StringBuffer signOriginalText = new StringBuffer("Z_1_");
			signOriginalText.append(filePdf.getName());
			signOriginalText.append("=");
			String sha1 = SHA_MD.encodeFileSHA1(filePdf).toHexString();
			signOriginalText.append(sha1);
			signOriginalText.append("&");
			attrMAP.put("name", contractFileName);
			attrMAP.put("type", "1");
	
			listFile.add(attrMAP);
			//将pdf转为图片
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", platformEntity.getAppId());
			pdfTomImgMap.put("ucid", identityEntity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(pdfPath, contractPath+"img"+"/"+contractFileName,pdfTomImgMap);
			//附件
			List<Map<String,String>> list = null;
			if(!"".equals(attachmentFile))
			{			
				try {
					list = gson.fromJson(attachmentFile, List.class);
				} catch (JsonSyntaxException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.JSONSYNTAXEXCEPTION[0],ConstantUtil.JSONSYNTAXEXCEPTION[1],ConstantUtil.JSONSYNTAXEXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null!=list && list.size()>0)
				{
					for(int i=0; i<list.size();i++)
					{
						Map<String,String> mapAttr = list.get(i);
						String attrName = mapAttr.get("attName");
						File attachment = new File(mapAttr.get("attPath"));
						String attPath = attachmentPath+attachment.getName();
						File attPathFile = new File(attPath);
//						File f = new File(attPath);
						FileUtil.copyFolder(attachment,attPathFile);//拷贝
						Map<String,String> attrMAPTemp = new HashMap<String,String>();
						attrMAPTemp.put("originalFilePath", attPath);//附件原文路径
						if("HTML".equals(suffix(attPathFile).toUpperCase()) || "DOC".equals(suffix(attPathFile).toUpperCase()) || "DOCX".equals(suffix(attPathFile).toUpperCase()))
						{						
							if("HTML".equals(suffix(attPathFile).toUpperCase()))
							{
								PDFTool.htmlToPdf(attPath, attachmentPath+attrName+".pdf");//合同文件
							}
							else
							{
//								PDFTool.htmlToPdfLibreOffice(attPath, attachmentPath);//合同文件
								PDFTool.wordToPdf(attPath, attachmentPath+attrName+".pdf");//合同文件
							}
							attrMAPTemp.put("path", attachmentPath+attrName+".pdf");
							PDFTool.pdfToImg(attachmentPath+attrName+".pdf", attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}
						else if(imgType.contains(suffix(attPathFile).toUpperCase()))
						{
							Map<String, String> map = new HashMap<String, String>();
							map.put("key", "fujian");
							Map<String,Integer> imgInfo = getImgWidthAndHeight(attPath);
//							map.put("width", String.valueOf(imgInfo.get("width")));
//							map.put("height", String.valueOf(imgInfo.get("height")));
							
							double width = imgInfo.get("width");
							double height = imgInfo.get("height");				
							DecimalFormat df = new DecimalFormat("###.000");				
							double zoom = 0.0;
							int heightStr = 0;
							int widthStr = 0;
							if(height > width)
							{
								zoom = Double.valueOf(df.format(width / height)); // 合同图片缩放比例,保留小数点后三位  (小于1)
								if(height > 800)
								{
									heightStr = 842;
									widthStr = (int)(heightStr * zoom);
								}
								else
								{
									heightStr = (int)height;
									widthStr = (int)width;
								}
							}
							else
							{
								zoom = Double.valueOf(df.format(height/width)); // 合同图片缩放比例,保留小数点后三位 (小于1)
								if(width > 542)
								{
									widthStr = 542;
									heightStr = (int)(widthStr * zoom);
								}
								else
								{
									heightStr = (int) height;
									widthStr = (int) width;
								}
							}
							log.info("heightStr="+heightStr+"widthStr="+widthStr);				
							map.put("width", String.valueOf(widthStr));
							map.put("height", String.valueOf(heightStr));
							map.put("contractPath", attachmentPath);
							map.put("imgPath", attPath);
							
							attrMAPTemp.put("path", attachmentPath+attrName+".pdf");
							FileUtil.appendImg(map, IConf.getValue("CONTRACTPATH")+"wechatImg.html", attachmentPath+attrName+".html");
//							PDFTool.htmlToPdfLibreOffice(attachmentPath+attrName+".html", attachmentPath);//合同文件转pdf
							PDFTool.htmlToPdf(attachmentPath+attrName+".html", attachmentPath+attrName+".pdf");//合同文件转pdf
							PDFTool.pdfToImg(attachmentPath+attrName+".pdf", attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}
						else
						{
							attrMAPTemp.put("path", attachmentPath+attPathFile.getName());
							PDFTool.pdfToImg(attPath, attachmentPath+"img"+"/"+attrName,pdfTomImgMap);
						}					
						attrMAPTemp.put("name", attrName);
	//					attrMAPTemp.put("path", attachmentPath+f.getName());
						attrMAPTemp.put("type", "2");//附件
						String attOriginalName = mapAttr.get("attOriginalName");
						attrMAP.put("originalFileName", attOriginalName);//附件原文件名字
						listFile.add(attrMAPTemp);
						String attrSha1 = SHA_MD.encodeFileSHA1(attPathFile).toHexString();
						signOriginalText.append("F_"+(i+1)+"_");
						signOriginalText.append(attPathFile.getName());
						signOriginalText.append("=");
						signOriginalText.append(attrSha1);
						signOriginalText.append("&");
						
					}
				}
			}
		
			ContractEntity contract = new ContractEntity();
			int opt = this.getOptForm(optFrom);
			contract.setSerialNum(serialNum);
			contract.setType("");
			contract.setUpdateTime(new Date());
			contract.setOptFrom((byte)opt);
			contract.setCreator(identityEntity.getId());
			contract.setCreateTime(new Date());
			contract.setOtheruids(customId);
			
			if(!"".equals(authEndTime))
			{
				try {							
					contract.setDeadline(DateUtil.stringToDate(authEndTime));
				} catch (ParseException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.TIME_IS_ILLEGAL[0],ConstantUtil.TIME_IS_ILLEGAL[1],ConstantUtil.TIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
				}
			}
			contract.setStatus((byte) 0);
			contract.setIsShow((byte)1);
			contract.setMark("");
			contract.setCPlatform(platformEntity);
			contract.setSignPlaintext(signOriginalText.toString());
			contract.setFinishtime(null);//创建初始化完成时间为null
			
			contract.setKeyword("");
			contract.setPaymentType((byte) 0);
			contract.setContractType("");//合同类型
			contract.setOperator("");//
			contract.setSha1(sha1);
			contract.setTitle(title);
			contract.setOrderId(orderId);
			ContractEntity cn = null;
			try{
				cn = contractDao.save(contract);
			}catch(Exception e)
			{
				log.info("数据保存到合同表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != cn)
			{
				log.info("保存附件表");
				//保存到附件表
				for (Map<String, String> map : listFile) {
					ContractPathEntity contractPathEntity =  new ContractPathEntity();
					contractPathEntity.setAttName(map.get("name"));
					contractPathEntity.setType((byte) Integer.parseInt(map.get("type")));
					contractPathEntity.setFilePath(map.get("path"));				
					String originalFile = map.get("originalFilePath");
					contractPathEntity.setExtension(suffix(new File(originalFile)));
					contractPathEntity.setContractSerialNum(serialNum);
					contractPathEntity.setOriginalFilePath(map.get("originalFilePath"));
					contractPathEntity.setOriginalFileName(map.get("originalFileName"));
					contractPathEntity.setContractPath(contractPath);
					contractPathEntity.setCContract(cn);
					try{
						contractPathDao.save(contractPathEntity);
					}catch(Exception e)
					{
						log.info("数据保存到附件表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
				}
				//TODO
				//保存授权书表
				AuthorityContractEntity authorityContractEntity = new AuthorityContractEntity();
				authorityContractEntity.setAuthorityContractPath(contractFilePath);
				authorityContractEntity.setOriginalFilename(contractFileName);
				authorityContractEntity.setExtension(suffix(new File(contractFilePath)));
				AuthorityContractEntity saveAuthorityContractEntity = authorityContractDao.save(authorityContractEntity);
				//保存授权表
				if(null != saveAuthorityContractEntity)
				{
					UserAuthorityEntity userAuthorityEntity = new UserAuthorityEntity();
					userAuthorityEntity.setAuthContractId(cn.getId());
					userAuthorityEntity.setAuthImgId(saveAuthorityContractEntity.getId());
					userAuthorityEntity.setAuthType(Integer.parseInt(authType));
					userAuthorityEntity.setSerialNum(authSerialNum);
					userAuthorityEntity.setPlatformId(platformEntity.getId());
					userAuthorityEntity.setCreateTime(new Date());
					userAuthorityEntity.setUserId(identityEntity.getId());
					userAuthorityEntity.setAuthorId(beIdentityEntity.getId());
					if(!"".equals(authStartTime))
					{
						try {							
							userAuthorityEntity.setAuthTime(DateUtil.stringToDate(authStartTime));
						} catch (ParseException e) {
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.TIME_IS_ILLEGAL[0],ConstantUtil.TIME_IS_ILLEGAL[1],ConstantUtil.TIME_IS_ILLEGAL[2],FileUtil.getStackTrace(e));
						}
					}					
					userAuthorityEntity.setAuthEndTime(DateUtil.stringToDate(authEndTime));				
					userAuthorityDao.save(userAuthorityEntity);
				}				
				boolean flag = false;
				for(int i=0;i<customIds.length;i++)
				{
					//查询c_identity表
					IdentityEntity identity = null;
					try{
						identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,customIds[i]);
					}catch(Exception e)
					{
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
					if(null == identity)
					{
						//有缔约方没有注册
						log.info("有缔约方没有注册");
						throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1]+customIds[i],ConstantUtil.RETURN_USER_NOTEXIST[2]);
					}
					//签署记录表
					SignRecordEntity signRecord = new SignRecordEntity();
					signRecord.setCContract(cn);
					signRecord.setCIdentity(identity);
					signRecord.setOrignalFilename("");
					signRecord.setPrevSha1("");
					signRecord.setCurrentSha1("");
					//signRecord.setSignTime(null);
					signRecord.setSigndata("");
					signRecord.setCUkeyInfo(null);
					signRecord.setSignStatus((byte) 0);
					signRecord.setMark("");
					signRecord.setSignMode((byte) 0);
					SignRecordEntity sr = null;
					try{
						sr = signRecordDao.save(signRecord);
					}catch(Exception e)
					{
						log.info("数据保存到签署表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
					if(null != sr)
					{
						flag = true;
					}
				}				
				if(flag)
				{
					log.info("合同创建成功");
					String contractStr = "";
					
					//查找计费帐号ID
					if(Integer.parseInt(chargeType) == 0)
					{
						//查询平台帐号进行扣费				
						IdentityEntity chargingIdentityEntity = null;
						try{
							chargingIdentityEntity = identityDao.queryChargingIdentityByPlateformId(platformEntity);
						}catch(Exception e)
						{
							log.info("平台计费帐号查询异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null == chargingIdentityEntity)
						{
							throw new ServiceException(ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[0],ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[1],ConstantUtil.CHARGE_PLATFORM_NOT_EXIST[2]);
						}
						
						UserServiceEntity userServiceEntity = null;
						try {
							userServiceEntity = userServiceDao.findByUserIdAndPayCode(chargingIdentityEntity.getId(), "contract");
						} catch (Exception e) {
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != userServiceEntity)
						{
							if(userServiceEntity.getPayType() == 1)//按次计费
							{
								userAccountService.reduce_times(chargingIdentityEntity.getId(), customIds.length, "contract", serialNum);
							}
							else if(userServiceEntity.getPayType() == 2)//按份数计费
							{
								userAccountService.reduce_times(chargingIdentityEntity.getId(), 1, "contract", serialNum);
							}
							else
							{
								throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
							}
						}
						else
						{
							throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
						}
						
					}
					else
					{
						UserServiceEntity userServiceEntity = null;
						try {
							userServiceEntity = userServiceDao.findByUserIdAndPayCode(identityEntity.getId(), "contract");
						} catch (Exception e) {
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
						if(null != userServiceEntity)
						{
							if(userServiceEntity.getPayType() == 1)//按次计费
							{
								userAccountService.reduce_times(identityEntity.getId(), customIds.length, "contract", serialNum);
							}
							else if(userServiceEntity.getPayType() == 2)//按份数计费
							{
								userAccountService.reduce_times(identityEntity.getId(), 1, "contract", serialNum);
							}
							else
							{
								throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
							}
						}
						else
						{
							throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
						}
					}
					
					try {
						contractStr = JSON.toJSONString(cn);
					} catch (Exception e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2]);
					}
					
					returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],contractStr);
				}
				else
				{
					log.info("创建创建初始化签署表失败");
					throw new ServiceException(ConstantUtil.CREATECONTRACT_FAIL[0],ConstantUtil.CREATECONTRACT_FAIL[1],ConstantUtil.CREATECONTRACT_FAIL[2]);
				}
			}
			else{
				log.info("保存合同表失败");
				throw new ServiceException(ConstantUtil.DATA_SAVE_EXCEPTION[0],ConstantUtil.DATA_SAVE_EXCEPTION[1],ConstantUtil.DATA_SAVE_EXCEPTION[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	//获取文件后缀
	public String suffix(File f)
	{
//	  File f =new File(src);
	  String fileName=f.getName();
	  String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
	  return suffix;
	     
	}
	
	
	public ReturnData checkParam2(Map<String, String> datamap)
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1],ConstantUtil.MAP_PARAMETER[2],"");
			return returnData;	
		}
		String optFrom = datamap.get("optFrom");
		String appId = datamap.get("appId");
		String customId = datamap.get("customId");
		String ucid = datamap.get("ucid");
		String fromCustom = datamap.get("fromCustom");
		String orderId = datamap.get("orderId");
		String title = datamap.get("title");
		String offerTime = datamap.get("offerTime");		
		String pname = datamap.get("pname");
		String tempNumber = datamap.get("tempNumber");
		String tempData = datamap.get("tempData");
		if(StringUtil.isNull(appId))
		{
			returnData = new ReturnData(ConstantUtil.RETURN_APP_NOT_EXIST[0],ConstantUtil.RETURN_APP_NOT_EXIST[1],ConstantUtil.RETURN_APP_NOT_EXIST[2],"");
			return returnData;
		}
		if(StringUtil.isNull(customId))
		{
			returnData = new ReturnData(ConstantUtil.RETURN_CUSTOMID_IS_NULL[0],ConstantUtil.RETURN_CUSTOMID_IS_NULL[1],ConstantUtil.RETURN_CUSTOMID_IS_NULL[2],"");
			return returnData;
		}
		if(StringUtil.isNull(ucid))
		{
			returnData = new ReturnData(ConstantUtil.UCID_IS_NULL[0],ConstantUtil.UCID_IS_NULL[1],ConstantUtil.UCID_IS_NULL[2],"");
			return returnData;
		}
		if(StringUtil.isNull(orderId))
		{
			returnData = new ReturnData(ConstantUtil.ORDERID_IS_NULL[0],ConstantUtil.ORDERID_IS_NULL[1],ConstantUtil.ORDERID_IS_NULL[2],"");
			return returnData;
		}
		if(StringUtil.isNull(offerTime))
		{
			returnData = new ReturnData(ConstantUtil.OFFERTIME_IS_NULL[0],ConstantUtil.OFFERTIME_IS_NULL[1],ConstantUtil.OFFERTIME_IS_NULL[2],"");
			return returnData;
		}
		if("MMEC".equals(optFrom))
		{
			if(StringUtil.isNull(tempNumber))
			{
				returnData = new ReturnData(ConstantUtil.TEMPERNUM_IS_NULL[0],ConstantUtil.TEMPERNUM_IS_NULL[1],ConstantUtil.TEMPERNUM_IS_NULL[2],"");
				return returnData;
			}
			
			if(StringUtil.isNull(tempData))
			{
				returnData = new ReturnData(ConstantUtil.TEMPERDATA_IS_NULL[0],ConstantUtil.TEMPERDATA_IS_NULL[1],ConstantUtil.TEMPERDATA_IS_NULL[2],"");
				return returnData;
			}
		}
		returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],"");
		return returnData;
	}
	
//	/**
//	 * 
//	 * @param serialNum 合同编号
//	 * @param userId 扣费帐号ID
//	 * @param times 计费次数
//	 */
//	public void charging(String serialNum,int userId,int times) throws ServiceException
//	{
//		//扣费
//		UserServiceEntity userServiceEntity = null;
//		try {
//			userServiceEntity = userServiceDao.findByUserIdAndPayCode(userId, "contract");
//		} catch (Exception e) {
//			log.info(FileUtil.getStackTrace(e));
//			throw new ServiceException(ConstantUtil.CHARGE_EXCEPTION[0],ConstantUtil.CHARGE_EXCEPTION[1],ConstantUtil.CHARGE_EXCEPTION[2]);
//		}
//		if(null != userServiceEntity)
//		{
//			if(userServiceEntity.getPayType() == 1)//按次计费
//			{
//				userAccountService.reduce_times(userId, times, "contract", serialNum);
//			}
//			else if(userServiceEntity.getPayType() == 2)//按份数计费
//			{
//				userAccountService.reduce_times(userId, 1, "contract", serialNum);
//			}
//			else
//			{
//				throw new ServiceException(ConstantUtil.CHARGE_EXCEPTION[0],ConstantUtil.CHARGE_EXCEPTION[1],ConstantUtil.CHARGE_EXCEPTION[2]);
//			}
//		}
//		else
//		{
//			throw new ServiceException(ConstantUtil.CHARGE_EXCEPTION[0],ConstantUtil.CHARGE_EXCEPTION[1],ConstantUtil.CHARGE_EXCEPTION[2]);
//		}
//	}
	
}
