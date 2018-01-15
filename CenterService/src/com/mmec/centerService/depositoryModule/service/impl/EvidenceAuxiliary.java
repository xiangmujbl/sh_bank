package com.mmec.centerService.depositoryModule.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import com.google.gson.Gson;
import com.mmec.centerService.depositoryModule.dao.EvidenceBindAttachmentDao;
import com.mmec.centerService.depositoryModule.entity.EvidenceBindAttachmentEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ResultData;
import com.mmec.util.ConstantUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.PDFTool;
import com.mmec.util.SHA_MD;
import com.mmec.thrift.service.CssRMIServices.Iface;

/**
 * @author Administrator
 *
 */
public class EvidenceAuxiliary implements Runnable{
	private Logger log = Logger.getLogger(EvidenceAuxiliary.class);
	
	private EvidenceBindAttachmentDao ebaDao;
	private String[] filepathArray;
	private int count;
	private String czFilePath;
	private int evidenceId;
	private Iface cssRMIServices;
	private String serialNum;
	 
	public EvidenceAuxiliary(String[] filepathArray,int count,
			String czFilePath,int evidenceId,Iface cssRMIServices,
			EvidenceBindAttachmentDao ebaDao,String serialNum)
	{
		this.filepathArray = filepathArray;
		this.count = count;
		this.czFilePath = czFilePath;
		this.evidenceId = evidenceId;
		this.cssRMIServices = cssRMIServices;
		this.ebaDao = ebaDao;
		this.serialNum = serialNum;
	}
	@Override
	public void run(){
		Gson g = new Gson();
		try {
				System.out.println("ThreadName:"+Thread.currentThread().getName());
				File srcFile = new File(filepathArray[count]);
				if (!srcFile.exists()) {
					throw new ServiceException(
							ConstantUtil.DEPOSITORY_FIlE_NOT_EXIST[0],
							ConstantUtil.DEPOSITORY_FIlE_NOT_EXIST[1],
							ConstantUtil.DEPOSITORY_FIlE_NOT_EXIST[2]);
				}
				//拷贝文件到存证目录
				String czFileStr = czFilePath + srcFile.getName();
				log.info("czFileStr==="+czFileStr);
				File czFile =  new File(czFileStr);
				FileUtil.copyFolder(new File(filepathArray[count]),czFile);//拷贝
				String file_hash = SHA_MD.encodeFileSHA1(czFile).toHexString();
				// 签名值
				ResultData resData = cssRMIServices.signService(file_hash);
				// 时间戳
				String signJsonData = resData.pojo;
				Map mapData = g.fromJson(signJsonData, HashMap.class);
				String certificate = (String) mapData.get("certificate");// 证书信息
				String signature = (String) mapData.get("signature");// 签名信息
				String certFingerprint = (String) mapData.get("certFingerprint");// 证书指纹
				String certSerialNum = (String) mapData.get("serialNum");// 证书序列号
				ResultData timeStampRes = cssRMIServices.getTimestampService(serialNum, certFingerprint);
				EvidenceBindAttachmentEntity eba = new EvidenceBindAttachmentEntity();
				eba.setAttachmentpath(czFileStr);
				eba.setDelflag("1");
				eba.setUploadtime(new Date());
				eba.setUpdatetime(new Date());
				eba.setEvidence_id(evidenceId);
				eba.setDownloadtimes(0);
				eba.setFilehash(file_hash);
				eba.setCert(certificate);
				eba.setSignature(signature);
				eba.setCertserial(certSerialNum);
				eba.setSigntimestamp(timeStampRes.pojo.replaceAll("\r\n",""));
				// 处理图片
				List<String> fileList = new ArrayList<String>();
				//TODO
//				String imgPath = czFilePath + System.currentTimeMillis();
				String imgPath = czFilePath + FileUtil.getOrderIdByUUId();
				String pdfPath = czFileStr;
				if (czFile.getName().endsWith(".pdf")|| czFile.getName().endsWith(".doc") || czFile.getName().endsWith(".docx"))
				{
					// 如果是doc文件
					if (czFile.getName().endsWith(".doc")  || czFile.getName().endsWith(".docx")) 
					{
						log.info("docpath:" + czFile + "_"+ imgPath + ".pdf");
						log.info("imgpath==="+imgPath);
						String fileName = czFile.getName();
						File attrImgFile = new File(imgPath+"/");
						if (!attrImgFile.exists())
						{
							attrImgFile.mkdirs();
						}
						String tempFile = czFilePath+"/"+fileName.substring(0, fileName.lastIndexOf("."))+".pdf";
						log.info("word转pdf的路径为:"+tempFile);
						PDFTool.wordToPdf(czFileStr,tempFile);
						pdfPath = tempFile;
					}
					Map<String, String> pdfTomImgMap = new HashMap<String, String>();
					pdfTomImgMap.put("optFrom", "NULL");
					pdfTomImgMap.put("appId", "NULL");
					pdfTomImgMap.put("ucid", "NULL");
					pdfTomImgMap.put("IP", "NULL");
					PDFTool.pdfToImg(pdfPath, imgPath, pdfTomImgMap);
					File[] files = new File(imgPath).listFiles();
					if (null != files && files.length > 0) 
					{
						for (int j = 0; j < files.length; j++) 
						{
							if (files[j].getName().toUpperCase().endsWith(".PNG")
									|| files[j].getName().toUpperCase().endsWith(".JPG")
									|| files[j].getName().toUpperCase().endsWith(".JPEG")) 
							{
								fileList.add(files[j].getPath());
							}
						}
					}
				}
				if (czFile.getName().toUpperCase().endsWith(".PNG")
						|| czFile.getName().toUpperCase().endsWith(".JPG")) 
				{
					fileList.add(czFileStr);
				}
				eba.setToImgpath(g.toJson(fileList));
				ebaDao.save(eba);
				
		} catch (Exception e) {
			e.printStackTrace();
//			throw new ServiceException();
		} 
		
	}
}
