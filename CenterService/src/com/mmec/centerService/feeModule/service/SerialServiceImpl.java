package com.mmec.centerService.feeModule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.Severity;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.service.ContractRMIServiceImpl;
import com.mmec.centerService.contractModule.service.ContractService;
import com.mmec.centerService.contractModule.service.CreateContractService;
import com.mmec.centerService.contractModule.service.DownloadService;
import com.mmec.centerService.contractModule.service.InternetFinanceCreate;
import com.mmec.centerService.contractModule.service.SignContractService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.entity.AuthEntity;
import com.mmec.centerService.userModule.service.LogService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CacheUtil;
import com.mmec.util.CheckUtil;
import com.mmec.util.ConstantUtil;
import com.mmec.util.StringUtil;

@ContextConfiguration({ "/applicationContext.xml" })

@Component("serialIface")
public class SerialServiceImpl implements com.mmec.thrift.service.SerialRMIServices.Iface {
	
	private static Logger log=Logger.getLogger(SerialServiceImpl.class);
	
	@Autowired
	private InvoiceInfoService invoiceInfoService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private PlatformRoleService platformRoleService;
	
	@Autowired
	private SignContractService signContractService;
	
	@Autowired
	private CreateContractService createContractService;

	@Autowired
	private InternetFinanceCreate internetFinanceCreate;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private DownloadService downloadService;
	
	
	@Autowired(required=true)
	private PlatformDao platformDao;
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private LocalCreateContractService  localcreatecontractService; 
	
	/**
	 * 云签验真2.0版本数据---根据流水号向中央承载查询数据信息
	 */
	public ReturnData querySerial(Map<String,String> map){
		ReturnData rd=new ReturnData();
		try{
			rd=invoiceInfoService.querySerial(map.get("serial"));
		}catch(ServiceException e){
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
		}finally{
			
		}
		return rd;
	}
	
	/**
	 * 根据平台账号---连接远程服务器扣次数
	 */
	public ReturnData localPay(Map<String,String> map){
		//如果是本地部署版
		log.info("localPay:"+map.toString());
		ReturnData rd=new ReturnData();
		try{
			rd=invoiceInfoService.localPay(map.get("appid"), Integer.valueOf(map.get("times")),
					map.get("paycode"),Integer.valueOf(map.get("paytype")));
		}catch(ServiceException e){
			e.printStackTrace();
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
		}finally{
			
		}
		return rd;
	}
	
	
	/**
	 * 通过appId查询权限配置
	 */
	public ReturnData queryAuth(Map<String,String> map){
		log.info("queryAuth:"+map.toString());
		Gson gson=new Gson();
		ReturnData rd=new ReturnData();
		List<AuthEntity> authlist=new ArrayList<AuthEntity>();
		try{
			authlist=platformRoleService.queryAuth(Integer.valueOf(map.get("userid")));
		}catch(ServiceException e){
			e.printStackTrace();
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
			return rd;
		}finally{
			
		}
		rd=new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],
				ConstantUtil.RETURN_SUCC[2],gson.toJson(authlist));
		return rd;
	}
	
	/**
	 * 本地数据库中央承载扣费
	 */
	public ReturnData configPay(Map<String,String> datamap){
		ReturnData rd=new ReturnData();
		return rd;
	}
	
	/**
	 * 本地版本创建合同
	 */
	public ReturnData createContractLocal(Map<String,String> datamap) throws TException {
		ReturnData rd=new ReturnData();
		log.info("createContractLocal:"+datamap.toString());
		ReturnData returnData = checkMapData(datamap);
		if(!"0000".equals(returnData.getRetCode()))
		{
			return returnData;
		}
		//拦截本地部署版请求,请求webService接口
//		try{
//		new CheckUtil().remoteWSPay(datamap);
//		}catch(ServiceException e){
//			e.printStackTrace();
//			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");
//		}
		//后续
		String optType = "";
		String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
		ServiceException retException = null;
		try{
			if(contractRepeatSubmit(datamap))
			{
				returnData.setRetCode(ConstantUtil.RETURN_REPEAT_SUBMIT[0]);
				returnData.setDesc(ConstantUtil.RETURN_REPEAT_SUBMIT[1]);
				returnData.setDescEn(ConstantUtil.RETURN_REPEAT_SUBMIT[2]);
			}
			else 
			{
				if(optFrom.equals(ConstantUtil.FROM_MMEC))
				{
					optType = "mmecCreate";
					returnData = localcreatecontractService.mmecCreate(datamap);	
				}else if(optFrom.equals(ConstantUtil.FROM_YUNSIGN))
				{
					optType = "yunsignCreate";
					returnData = localcreatecontractService.yunsignCreate(datamap);	
				}
				else if(optFrom.equals(ConstantUtil.FROM_OA))
				{
					
				}
				else
				{
					returnData = new ReturnData("error", "没有匹配的创建接口", "", "");
				}
			}
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("contract", "contract");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");		
		}
		finally{
			try {			
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("createContract return data:"+returnData.toString());
		return returnData;	
	}
	
	/**
	 * 检查数据
	 * @param datamap
	 * @return
	 */
	public ReturnData checkMapData(Map<String, String> datamap)
	{
//		log.info("datamap转json:"+JSON.toJSONString(datamap));
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		if(StringUtil.isNull(datamap.get("optFrom")))
		{
			returnData = new ReturnData(ConstantUtil.OPTFROM_PARAMETER[0],ConstantUtil.OPTFROM_PARAMETER[1], ConstantUtil.OPTFROM_PARAMETER[2], "");
			return returnData;
		}
		else
		{
			returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], "");
		}
		return returnData;
	}
	
	/**
	 * 插入日志信息
	 * optFrom 云签传YUNSIGN_CLIENT 对接传MMEC_CLIENT
	 * appId 传客户的appId
	 * account/platformUserName/userid 三个字段选传一项 标识用户
	 * serverIp 传客户端IP
	 * returnData 传ReturnData转json的值
	 * serviceException 传serviceException转json的值  如果客户调用成功没有异常时该项不传
	 * optType 传方法名称
	 */
	public ReturnData insertLog(Map<String,String> map){
		ReturnData rd=new ReturnData();
		Gson g=new Gson();
		String optType="";
		Map map1=g.fromJson(map.get("param"),Map.class);
		if(null!=map.get("optType")){
			optType=map.get("optType");
		}
		if(null!=map.get("serverIp")&&!"".equals(map.get("serverIp"))){
			map1.put("serverIp", map.get("serverIp"));
		}
		if(null!=map.get("optFrom")&&!"".equals(map.get("optFrom"))){
			map1.put("optFrom", map.get("optFrom"));
		}
		ReturnData data=new ReturnData();
		if(null!=map.get("returnData")&&!"".equals(map.get("returnData"))){
			if(map.get("returnData").indexOf(",")>-1){
				ResultData res=g.fromJson(map.get("returnData"),ResultData.class);
				data.setRetCode(String.valueOf(res.getStatus()));
				data.setDesc(res.getDesc());
				data.setPojo(res.getPojo());
			}else{
				data.setDesc(map.get("returnData"));
			}
		}
		ServiceException se=new ServiceException();
		if(null!=map.get("serviceException")){
			se=g.fromJson(map.get("serviceException"), ServiceException.class);
		}
		try{
			log.info("returnData from MMEC:"+map.get("returnData"));
			logService.log(map1, optType, se,map.get("returnData"));
		}
		catch(ServiceException e){
			e.printStackTrace();
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
			return rd;
		}finally{
			
		}
		return new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],
				ConstantUtil.RETURN_SUCC[2],"") ;
	}
	
	public synchronized boolean contractRepeatSubmit(Map<String, String> datamap)
	{
		String checkStr = datamap.get("appId")+"#"+datamap.get("orderId");
		//判断是否重复注册
		if(null != (new CacheUtil().get(checkStr)))
		{
			return true;			
		}
		else
		{
			new CacheUtil().set(checkStr);
			return false;
		}		
	}
	
}