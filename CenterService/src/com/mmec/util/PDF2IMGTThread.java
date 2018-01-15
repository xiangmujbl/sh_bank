package com.mmec.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.ApplicationContext;

import com.mmec.centerService.contractModule.entity.ContractImgBean;
import com.mmec.centerService.contractModule.service.ContractService;

public class PDF2IMGTThread implements Runnable
{
	private ContractService contractService;
	
	private static ApplicationContext app;
	
	public PDF2IMGTThread(ApplicationContext app)
	{
		PDF2IMGTThread.app = app;
	}
	
	public static ApplicationContext getApp()
	{
		return app;
	}

	public static void setApp(ApplicationContext app)
	{
		PDF2IMGTThread.app = app;
	}
	
	private static int imgStatus = 0;
	private static int poolNumbers = 10;
	@Override
	public void run()
	{
		Date now = new Date();
		boolean isDealEnd = true;
		//启动线程池
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(poolNumbers);  
		if(null == contractService)
		{
			contractService = (ContractService)app.getBean("contractService");	
		}
		while(true && isDealEnd)
		{
			// TODO Auto-generated method stub
			try
			{	
				isDealEnd = false;
				//查询截止当前时间待转换图片的合同数据
				List<ContractImgBean> conList = contractService.queryWaitImgContractList(now, imgStatus);
				System.out.println("conList.size()======"+conList.size());
				//没有待转换的 等待一段时间再去搜索 暂定30秒
				if(null == conList  || conList.size() == 0)
				{
					System.out.println("I'm sleep");
					Thread.sleep(10000);
					isDealEnd = true;
				}
				//否则启一个线程池来完成
				else
				{
					System.out.println("I'm not sleep");
					for (int i = 0; i < conList.size(); i++) {  
						ImgTurnThread imgTurnThread = new ImgTurnThread(contractService);
						imgTurnThread.setContractImgBean(conList.get(i));
						System.out.println();
						fixedThreadPool.execute(imgTurnThread);  
					}
					Thread.sleep(10000);
					isDealEnd = true;
				}
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
}


class ImgTurnThread implements Runnable
{
	
	private ContractService contractService;
	
	private ContractImgBean contractImgBean;

	public ContractImgBean getContractImgBean()
	{
		return contractImgBean;
	}
	
	public ImgTurnThread(ContractService contractService)
	{
		this.contractService = contractService;
	}
	
	public void setContractImgBean(ContractImgBean contractImgBean)
	{
		this.contractImgBean = contractImgBean;
	}

	@Override
	public void run()
	{
//		String pdfPathfile = filePath+fileName+".pdf";
		//
		try {
			int  update = contractService.updateTurnContractStatus(new Date(), 1, contractImgBean.getContractEntity().getSerialNum());
			System.out.println("update======================"+update);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String pdfPathfile = contractImgBean.getContractPathEntity().getFilePath();
		
		
		System.out.println("BENCI:"+contractImgBean.getContractEntity().getSerialNum()+","+contractImgBean.getContractEntity().getTitle());
		
		
		Map<String, String> pdfTomImgMap = new HashMap<String, String>();
		pdfTomImgMap.put("optFrom", "NULL");
		pdfTomImgMap.put("appId", "NULL");
		pdfTomImgMap.put("ucid", contractImgBean.getIdentity().getAccount());
		pdfTomImgMap.put("IP", "NULL");
		String contractPath = contractImgBean.getContractPathEntity().getContractPath();
		String fileName = contractImgBean.getContractPathEntity().getAttName();
		
		if(contractImgBean.getContractPathEntity().getType() == 2)
		{
			PDFTool.pdfToImg(pdfPathfile, contractPath+"attachment/img"+"/"+fileName,pdfTomImgMap);
		}
		else
		{
			PDFTool.pdfToImg(pdfPathfile, contractPath+"img"+"/"+fileName,pdfTomImgMap);
		}
//		System.out.println("contractService==="+contractService);
		System.out.println("ssssss==="+contractImgBean.getContractEntity().getSerialNum());
		try {
			if(contractImgBean.getContractPathEntity().getType() == 1)
			{
				int  update = contractService.updateTurnContractStatus(new Date(), 2, contractImgBean.getContractEntity().getSerialNum());
				System.out.println("update======================"+update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}