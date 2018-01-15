package com.mmec.centerService.contractModule.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mmec.centerService.contractModule.dao.AuthorityContractDao;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.dao.ContractPathDao;
import com.mmec.centerService.contractModule.dao.SignRecordDao;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractPathEntity;
import com.mmec.centerService.contractModule.entity.SignRecordEntity;
import com.mmec.centerService.contractModule.service.DownloadService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.entity.CompanyInfoEntity;
import com.mmec.centerService.userModule.entity.CustomInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ComposePicture;
import com.mmec.util.ConstantUtil;
import com.mmec.util.CustomImages;
import com.mmec.util.DateUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.FtpUtil;
import com.mmec.util.GetAllFiles;
import com.mmec.util.SHA_MD;
import com.mmec.util.SecurityUtil;
import com.mmec.util.StringUtil;
import com.mmec.util.ZipUtil;

@Service("downloadService")
public class DownloadServiceImpl extends BaseContractImpl implements DownloadService  {
	
	private Logger log = Logger.getLogger(DownloadServiceImpl.class);
	
	@Autowired
	private PlatformDao platformDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractPathDao contractPathDao;
	
	@Autowired
	private SignRecordDao signRecordDao;
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private AuthorityContractDao authorityContractDao;
	
	public ReturnData compressionContract(ContractEntity contract,String ucid) throws ServiceException
	{
		ReturnData returnData = null;
		try
		{

			//2.0导过来的数据
			if(contract.getOptFrom() == 9)
			{
				returnData = zipDownload2(contract);
			}
			else
			{
				String customId = contract.getOtheruids();
				String [] customIds = customId.split(",");
				//互联网金融合同
//				if(contract.getOptFrom() != 4)
//				{
//					if(!StringUtil.isContain(ucid,customIds))
//					{
//						log.info("操作人不在缔约方范围内,没有权限操作");
//						throw new ServiceException(ConstantUtil.USER_ISNOT_SIGNATORY[0],ConstantUtil.USER_ISNOT_SIGNATORY[1], ConstantUtil.USER_ISNOT_SIGNATORY[2]);
//					}
//				}
				if(contract.getStatus() == 0 || contract.getStatus() == 1)
				{
					log.info("合同为签署完毕,不能下载");
					throw new ServiceException(ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[0],ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[1], ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[2]);
				}
				if(contract.getStatus() == 3)
				{
					log.info("合同已被拒绝");
					throw new ServiceException(ConstantUtil.CONTRACT_HASBEEN_REFUSED[0],ConstantUtil.CONTRACT_HASBEEN_REFUSED[1], ConstantUtil.CONTRACT_HASBEEN_REFUSED[2]);
				}
				if(contract.getStatus() == 4)
				{
					log.info("合同已被撤销");
					throw new ServiceException(ConstantUtil.CONTRACT_HASBEEN_CANCELED[0],ConstantUtil.CONTRACT_HASBEEN_CANCELED[1], ConstantUtil.CONTRACT_HASBEEN_CANCELED[2]);
				}
				if(contract.getStatus() == 5)
				{
					log.info("合同已被撤销");
					throw new ServiceException(ConstantUtil.CONTRACT_HASBEEN_CLOSED[0],ConstantUtil.CONTRACT_HASBEEN_CLOSED[1], ConstantUtil.CONTRACT_HASBEEN_CLOSED[2]);
				}
				String serialNum = contract.getSerialNum();
//				String downPath = GlobalData.TEMP_PATH+"sharefile/mmecserver/zipdownload/" + serialNum;
//				String downPath = "E:"+ File.separator + serialNum;
				String downPath = IConf.getValue("CONTRACT_ZIP") + serialNum;
//				ContractPathEntity contractPath = contractPathDao.findContractPathByContractId(contract);
				List<ContractPathEntity> listContractPath = contractPathDao.findListContractPathByContractId(contract);
				String attrName = ""; 
				String z_fujianPath = "";
				List<String> listAttrPath = new ArrayList<String>();//存放附件路径
				List<String> listAttrName = new ArrayList<String>();//附件名字
				if(null != listContractPath && !listContractPath.isEmpty())
				{
					for(int i=0;i<listContractPath.size();i++)
					{
						ContractPathEntity contractPath = listContractPath.get(i);
						//拷贝MP4文件
						if("MP4".equals(contractPath.getExtension().toUpperCase()))
						{
							//视频路径
							String videoPath = downPath+"/video";
							FileUtil.createDir(videoPath);
							FileUtil.copyFile(contractPath.getFilePath(), videoPath + "/"+new File(contractPath.getFilePath()).getName());
						}
						if(contractPath.getType() ==1)//合同文件
						{
							attrName = contractPath.getAttName();
		//					z_fujianPath = contractPath.getFilePath();
							if("Y".equals(contract.getIsPdfSign()))
							{
								z_fujianPath = contractPath.getFilePath();
							}
							else
							{
								z_fujianPath = contractPath.getOriginalFilePath();
							}
						}
						else //合同附件
						{								
							if("Y".equals(contract.getIsPdfSign()))
							{
								listAttrPath.add(contractPath.getFilePath());//附件路径
							}
							else
							{
								listAttrPath.add(contractPath.getOriginalFilePath());
							}
							listAttrName.add(contractPath.getAttName());
						}
					}
				}

				//最后打zip包的路径		
				//文件记录路径,这个路径存放合同文件
				String contractRecordPath = downPath + "/ContractRecord/Contract/";
				//存放js和index所需图片
				String images = downPath + "/images";
				//将pdf转为图片的路径
				String imgRecordPath = downPath + "/ContractImg/"+ attrName;
				//创建一个下载目录
				FileUtil.createDir(downPath);
				// contractRecordPath目录
				FileUtil.createDir(contractRecordPath);
				////将pdf转为图片的路径
				FileUtil.createDir(imgRecordPath);
				//存放js和index所需图片
				FileUtil.createDir(images);

				int z = 1;
				//复制合同主文件
				FileUtil.copyFile(z_fujianPath, contractRecordPath + "/Z_"+z+"_"+new File(z_fujianPath).getName());
				//复制合同附件
				for(int i=0;i<listAttrPath.size();i++)
				{
					FileUtil.copyFile(listAttrPath.get(i), contractRecordPath + "/F_"+(i+1)+"_"+new File(listAttrPath.get(i)).getName());
				}
				Date createTime = contract.getCreateTime();			
				String yeadMonth = FileUtil.getYearMonth(createTime);
				String temp_path = IConf.getValue("CONTRACTPATH")+yeadMonth+File.separator;
				FileUtil.copyFolder(new File(temp_path+serialNum+"/img/"+attrName),new File(imgRecordPath));//主合同图片
				//合同附件图片
				for(int i=0;i<listAttrPath.size();i++)
				{
					FileUtil.createDir(downPath + "/ContractImg/attachment/"+ listAttrName.get(i));
					FileUtil.copyFolder(new File(temp_path+serialNum+"/attachment/img/"+listAttrName.get(i)),new File(downPath + "/ContractImg/attachment/"+ listAttrName.get(i)));//附件图片
				}
				FileUtil.copyFolder(new File(temp_path+serialNum+"/sign/ContractRecordSHA1.txt"),new File(downPath+"/ContractRecordSHA1.txt"));
				FileUtil.copyFolder(new File(temp_path+serialNum+"/sign/SignRecordSHA1.txt"),new File(downPath+"/SignRecordSHA1.txt"));
				FileUtil.copyFolder(new File(temp_path+serialNum+"/sign/ServerSign.sg"),new File(downPath+"/ServerSign.sg"));
				FileUtil.copyFolder(new File(temp_path+serialNum+"/sign/ContractSHA1.txt"),new File(downPath+"/ContractRecord/ContractSHA1.txt"));
				FileUtil.copyFolder(new File(temp_path+serialNum+"/sign/UserGroupSign.sg"),new File(downPath+"/ContractRecord/UserGroupSign.sg"));
				FileUtil.copyFolder(new File(temp_path+serialNum+"/sign/Check.sg"),new File(downPath+"/Check.sg"));
				FileUtil.copyFolder(new File(IConf.getValue("CONTRACTPATH")+"images"),new File(images));
				//拷贝授权书到zip包里
				String authorityFileSha1 = "";
				File authorityFile = new File(temp_path+serialNum+"/authority");
//				AuthorityContractEntity authorityContractEntity = authorityContractDao.findById(contract.getAuthorityContractId());
				if(authorityFile.exists())
				{
					String tempPath = downPath + "/authority";
					FileUtil.createDir(tempPath);
					
					File fa[] = authorityFile.listFiles();
					for(int i=0;i<fa.length;i++)
					{
						File tempFile = fa[i];
						authorityFileSha1 = SHA_MD.encodeFileSHA1(tempFile).toHexString();
						byte[] encryptResult = SecurityUtil.encrypt(authorityFileSha1, IConf.getValue("AUTH_PASSWORD"));//AES加密用户数据							
						String encryptResultBase64 = SecurityUtil.encryptBASE64(encryptResult);
						FileUtil.writeTxtFile(encryptResultBase64, new File(tempPath+"/"+tempFile.getName().substring(0,tempFile.getName().lastIndexOf("."))+".txt"));
						FileUtil.copyFolder(tempFile,new File(tempPath+"/"+tempFile.getName()));
					}						
				}
	//			List<String> imgPath =  imgPath(temp_path+serialNum+"/"+"img/"+attrName);
				List<String> imgPath =  imgPath(imgRecordPath);
				//合成水印
				for(int i=0;i<imgPath.size();i++)
				{
	//				ComposePicture.composePic(imgRecordPath+"/"+i+".png", images+"/00.jpg", imgRecordPath+"/"+i+".png", 60, 10);
	//				ComposePicture.composePic(imgRecordPath+"/"+i+".png", images+"/11.png", imgRecordPath+"/"+i+".png", 1120, 350);
	//				ComposePicture.composePic(imgRecordPath+"/"+i+".png", images+"/22.png", imgRecordPath+"/"+i+".png", 1120, 800);
					//对接去掉水印
//					ComposePicture.composePic(imgRecordPath+"/"+i+".png", images+"/watermark.png", imgRecordPath+"/"+i+".png", 30, 10);
//					ComposePicture.composePic(imgRecordPath+"/"+i+".png", images+"/00.png", imgRecordPath+"/"+i+".png", 680, 20);
//					ComposePicture.composePic(imgRecordPath+"/"+i+".png", images+"/11.png", imgRecordPath+"/"+i+".png", 830, 250);
//					ComposePicture.composePic(imgRecordPath+"/"+i+".png", images+"/22.png", imgRecordPath+"/"+i+".jpg", 840, 600);
					
					
					String src[] = new String[2];//图片的本地路径
			        src[0] = imgRecordPath+"/"+i+".png";// 底边
//			        src[1] =  images+"/watermark.png";// 正身
			        src[1] = images+"/22.png";// 左袖子
//			        src[3] = "C:\\02.png";// 右袖子
//			        src[4] = "C:\\01.png";// 领子
			        int x[] = new int[2];// 存放X轴坐标的数组
			        x[0] = 0;
//			        x[1] = 30;
			        x[1] = 840;
//			        x[3] = 265;
//			        x[4] = 140;
			        int y[] = new int[2];// 存放Y轴坐标的数组
			        y[0] = 0;
//			        y[1] = 10;
			        y[1] = 600;
//			        y[3] = 68;
//			        y[4] = 40;
			        CustomImages[] imgo = new CustomImages[src.length];// 批量生成图片对象
			        for (int a = 0; a < imgo.length; a++) { // 构造方法测参数是X,Y轴的位置和图片本地路径
			            imgo[a] = new CustomImages(x[a], y[a], src[a]);
			        }
			        ComposePicture.mergeImg(imgo, imgRecordPath+"/"+i+".png");// 合成图片的方法
				}
				
	//			List<String> imgPath =  imgPath("f:/office/"+serialNum+"/img");
				Collections.sort(imgPath);
	//			File input = new File("E:/zipindex.html");
	//			File input = new File(IConf.getValue("CONTRACTPATH")+"zipindex.html");
				File input = new File(IConf.getValue("CONTRACTPATH")+"index.html");
				Document doc = null;;
				try {
					doc = Jsoup.parse(input, "UTF-8", "");
				} catch (IOException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.ZIP_DOWN_INDEX[0],ConstantUtil.ZIP_DOWN_INDEX[1], ConstantUtil.ZIP_DOWN_INDEX[2],FileUtil.getStackTrace(e));
				}
							
				doc.getElementById("serialNum").append(serialNum);
				doc.getElementById("title").append(contract.getTitle());
				doc.getElementById("createTime").append(DateUtil.toDateYYYYMMDDHHMM2(contract.getCreateTime()));
				doc.getElementById("offerTime").append(DateUtil.toDateYYYYMMDDHHMM2(contract.getDeadline()));
				for(int i=0;i<imgPath.size();i++)
				{
					doc.getElementById("mainContract").append("<volist name='pdf' id='vo'> <img class='contractimg' src='./ContractImg/"+attrName+"/"+i+"."+IConf.getValue("SUFFIX")+"' style='width: 840px;'> </volist>");
				}
				//显示附件
				if(null != listContractPath && !listContractPath.isEmpty())
				{
					for(int i=0;i<listContractPath.size();i++)
					{					
						ContractPathEntity tempContractPath = listContractPath.get(i);							
						if(tempContractPath.getType() ==2)
						{
							doc.getElementById("title"+(i+1)).append(StringUtil.nullToString(tempContractPath.getOriginalFileName()));
							String tempAttrName = tempContractPath.getAttName();
							if("MP4".equals(tempContractPath.getExtension().toUpperCase()))
							{
								doc.getElementById("attachment"+(i+1)).append("<volist name='pdf' id='vo'> <embed class='contractimg' autostart='true' loop='true' src='./video/"+tempAttrName+"."+tempContractPath.getExtension()+"' style='width: 840px;height:560px'> </volist>");
							}
							else
							{
								List<String> imgPathAttr =  imgPath(temp_path+serialNum+"/"+"attachment/img/"+tempAttrName);							
								for(int j=0;j<imgPathAttr.size();j++)
								{
									doc.getElementById("attachment"+(i+1)).append("<volist name='pdf' id='vo'> <img class='contractimg' src='./ContractImg/attachment/"+tempAttrName+"/"+j+"."+IConf.getValue("SUFFIX")+"' style='width: 840px;'> </volist>");
								}
							}
						}
					}
				}
				
				//查询服务组签署记录表
				List<SignRecordEntity> listServerSignRecord = signRecordDao.findServiceSignRecordByContractId(contract);
				//查询用户组签署记录表
				List<SignRecordEntity> listSignRecord = signRecordDao.querySignRecordByContractId(contract);
				//查询代签的签署记录
				List<SignRecordEntity> listAuthoritySignRecord = signRecordDao.queryAuthoritySignRecordByContractId(contract);
				List<Map<String,String>> listServer = new ArrayList<Map<String,String>>();//用户组电子签名值
				List<Map<String,String>> listCustomer = new ArrayList<Map<String,String>>();//用户组电子签名值
				List<Map<String,String>> listCert = new ArrayList<Map<String,String>>();//数字证书公钥
				Gson gson = new Gson();
//				Map<String,String> map = new HashMap<String,String>();
				if(null != listSignRecord && listSignRecord.size()>0)
				{
					//发起方
					Map<String,String> cert = new HashMap<String,String>();
					StringBuffer receiver = new StringBuffer("<tbody>");//接收方
					StringBuffer initiator = new StringBuffer("<tbody>");//发起方
					Map<String,String> customerSign = new HashMap<String,String>();//用户组电子签名值
					boolean flag = true;
					for(int i=0;i<listSignRecord.size();i++)
					{
						SignRecordEntity signRecord = listSignRecord.get(i);
						String signData = signRecord.getSigndata();
						//授权人的AuthorId存的是被授权人的userId,signdata存的是"请别人代签",所以在解析signData时会出错		
						if(signRecord.getAuthorId()==0) 
						{
							Map<String,String> signInfo = gson.fromJson(signData, Map.class);
							customerSign.put("Signature", signInfo.get("sign"));
							cert.put("Cert", signInfo.get("cert"));
						}
						IdentityEntity identity = signRecord.getCIdentity();
						CustomInfoEntity customInfo = identity.getCCustomInfo();
						String userName = "";
						String idCard = "";
						String signTime = signRecord.getSignTime() == null ? "": DateUtil.toDateYYYYMMDDHHMM2(signRecord.getSignTime());
						if(signRecord.getAuthorId() == 0 && null != identity)
//						if(contract.getCreator() != signRecord.getCIdentity().getId() && (signRecord.getAuthorId() != 0 || "".equals(signRecord.getMark())))
						{
							if(identity.getType() ==1)//个人用户
							{						
								if(null != customInfo)
								{
									userName = customInfo.getUserName();//姓名
									idCard = customInfo.getIdentityCard();
								}
								receiver.append("<tr><td><div class=\"sign-seal\">");
								receiver.append("<div class=\"sign-inner\">签 署 人:");
								receiver.append(userName);
								if(null != listAuthoritySignRecord && !listAuthoritySignRecord.isEmpty())
								{
									for(int j=0;j<listAuthoritySignRecord.size();j++)
									{
										if(listAuthoritySignRecord.get(j).getAuthorId()== identity.getId()&& !"".equals(signRecord.getMark()))
										{
											IdentityEntity tempIdentity =  listAuthoritySignRecord.get(j).getCIdentity();
											if(null != tempIdentity)
											{
												CustomInfoEntity custom = tempIdentity.getCCustomInfo();
												CompanyInfoEntity company = tempIdentity.getCCompanyInfo();
												String name = "";
												if(tempIdentity.getType() == 1)
												{
													if(null != custom)
													{
														name = custom.getUserName();
													}
												}
												else if(tempIdentity.getType() == 2)
												{
													if(null != company)
													{
														name = company.getCompanyName();
													}
												}
												receiver.append("<br>代 "+name+" 签署");
											}
										}
									}
								}
								receiver.append("<br>身份证号:");							
								receiver.append(StringUtil.nullToString(idCard));								
								receiver.append("<br>签署时间:");
								receiver.append(signTime);
								receiver.append("<i class=\"icon-personal\"></i></div>");
								receiver.append("</div></td><td><div class=\"timestamp\"><p>");
								receiver.append(signTime);
								receiver.append("</p> </div></td></tr>");
								
							}
							else if(identity.getType() ==2)//企业用户
							{
								CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
								String licenseNo = "";
								if(null != companyInfo)
								{
									userName = companyInfo.getCompanyName();//公司名
									licenseNo = companyInfo.getBusinessLicenseNo();
								}
								receiver.append("<td><div class=\"sign-seal\">");
								receiver.append("<div class=\"sign-inner\">企业名称:");
								receiver.append(userName);
								
								receiver.append("<br>工商营业执照号:");
								receiver.append(licenseNo);
//									receiver.append("<br>法定代表人:");
								receiver.append("<br>签署人:");
								receiver.append(identity.getCCustomInfo() == null ? "" : StringUtil.nullToString(identity.getCCustomInfo().getUserName()));
								if(null != listAuthoritySignRecord && !listAuthoritySignRecord.isEmpty())
								{	
									
									for(int j=0;j<listAuthoritySignRecord.size();j++)
									{
										int authorId = listAuthoritySignRecord.get(j).getAuthorId();
//											int authorId_sign = listAuthoritySignRecord.get(j).getCIdentity().getId();
//											String temp = String.valueOf(authorId)+authorId_sign;
//											if(map.containsKey(temp))
//											{
//												continue;
//											}
										
										//用于判断一人待多人签署时，重复显示的问题
//											map.put(temp, temp);
										if(authorId == identity.getId() && !"".equals(signRecord.getMark()))
										{
											
//												map.put(listAuthoritySignRecord.get(j).getAuthorId(), listAuthoritySignRecord.get(j).getAuthorId());												
											
											IdentityEntity tempIdentity =  listAuthoritySignRecord.get(j).getCIdentity();
											if(null != tempIdentity)
											{
												CustomInfoEntity custom = tempIdentity.getCCustomInfo();
												CompanyInfoEntity company = tempIdentity.getCCompanyInfo();
												String name = "";
												if(tempIdentity.getType() == 1)
												{
													if(null != custom)
													{
														name = custom.getUserName();
													}
												}
												else if(tempIdentity.getType() == 2)
												{
													if(null != company)
													{
														name = company.getCompanyName();
													}
												}
												receiver.append("<br>代 "+name+" 签署");
											}
										}
									}
								}
								receiver.append("<br>身份证号:");
								receiver.append(identity.getCCustomInfo()== null ? "" : StringUtil.nullToString(identity.getCCustomInfo().getIdentityCard()));
								receiver.append("<br>签署时间:");
								receiver.append(signTime);
								receiver.append("<i class=\"icon-company\"></i></div>");
								receiver.append("</div></td><td><div class=\"timestamp\"><p>");
								receiver.append(signTime);
								receiver.append("</p> </div></td></tr>");
							}
							receiver.append("</tbody>");
						}
//						else if(contract.getCreator() == signRecord.getCIdentity().getId())
//						{
//							//发起方
//							if(identity.getType() ==1)//个人用户
//							{						
//								if(null != customInfo)
//								{
//									userName = identity.getCCustomInfo().getUserName();//姓名
//									initiator.append("<tr><td><div class=\"sign-seal\">");
//									initiator.append("<div class=\"sign-inner\">签 署 人:");
//									initiator.append(userName);
//									if(signRecord.getAuthorId() != 0)
//									{
//										int id = signRecord.getAuthorId();
//										IdentityEntity tempIdentity =  identityDao.findById(id);
//										CustomInfoEntity custom = tempIdentity.getCCustomInfo();
//										CompanyInfoEntity company = tempIdentity.getCCompanyInfo();
//										String name = "";
//										if(tempIdentity.getType() == 1)
//										{
//											if(null != custom)
//											{
//												name = custom.getUserName();
//											}
//										}
//										else if(tempIdentity.getType() == 2)
//										{
//											if(null != company)
//											{
//												name = company.getCompanyName();
//											}
//										}
//										initiator.append("<br>由"+name+"代签");
//									}
//									initiator.append("<br>身份证号:");
//									initiator.append(customInfo.getIdentityCard());
//									initiator.append("<br>签署时间:");
//									initiator.append(signTime);
//									initiator.append("<i class=\"icon-personal\"></i></div>");
//									initiator.append("</div></td><td><div class=\"timestamp\"><p>");
//	//								initiator.append(signInfo.get("tsa"));
//									initiator.append(signTime);
//									initiator.append("</p> </div></td></tr>");
//								}
//							}
//							else if(identity.getType() ==2)//企业用户
//							{
//								CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
//								if(null != companyInfo)
//								{
//									userName = companyInfo.getCompanyName();//公司名
//									initiator.append("<td><div class=\"sign-seal\">");
//									initiator.append("<div class=\"sign-inner\">企业名称:");
//									initiator.append(userName);
//									if(signRecord.getAuthorId() != 0)
//									{
//										int id = signRecord.getAuthorId();
//										IdentityEntity tempIdentity =  identityDao.findById(id);
//										CustomInfoEntity custom = tempIdentity.getCCustomInfo();
//										CompanyInfoEntity company = tempIdentity.getCCompanyInfo();
//										String name = "";
//										if(tempIdentity.getType() == 1)
//										{
//											if(null != custom)
//											{
//												name = custom.getUserName();
//											}
//										}
//										else if(tempIdentity.getType() == 2)
//										{
//											if(null != company)
//											{
//												name = company.getCompanyName();
//											}
//										}
//										initiator.append("<br>由"+name+"代签");
//									}
//									initiator.append("<br>工商营业执照号:");
//									initiator.append(companyInfo.getBusinessLicenseNo());
////									initiator.append("<br>法定代表人:");
//									initiator.append("<br>签署人:");
//									initiator.append(identity.getCCustomInfo().getUserName());
//									initiator.append("<br>身份证号:");
//									initiator.append(identity.getCCustomInfo().getIdentityCard());
//									initiator.append("<br>签署时间:");
//									initiator.append(signTime);
//									initiator.append("<i class=\"icon-company\"></i></div>");
//									initiator.append("</div></td><td><div class=\"timestamp\"><p>");
//									initiator.append(signTime);
//									initiator.append("</p> </div></td></tr>");
//								}
//							}
//							initiator.append("</tbody>");
//						}
//						else if(signRecord.getAuthorId() != 0 && flag)
//						{								
//							flag = false;								
//							//发起方
//							if(identity.getType() ==1)//个人用户
//							{						
//								if(null != customInfo)
//								{
//									userName = identity.getCCustomInfo().getUserName();//姓名
//									initiator.append("<tr><td><div class=\"sign-seal\">");
//									initiator.append("<div class=\"sign-inner\">签 署 人:");
//									initiator.append(userName);
//									if(signRecord.getAuthorId() != 0)
//									{
//										int id = signRecord.getAuthorId();
//										IdentityEntity tempIdentity =  identityDao.findById(id);
//										CustomInfoEntity custom = tempIdentity.getCCustomInfo();
//										CompanyInfoEntity company = tempIdentity.getCCompanyInfo();
//										String name = "";
//										if(tempIdentity.getType() == 1)
//										{
//											if(null != custom)
//											{
//												name = custom.getUserName();
//											}
//										}
//										else if(tempIdentity.getType() == 2)
//										{
//											if(null != company)
//											{
//												name = company.getCompanyName();
//											}
//										}
//										initiator.append("<br>由"+name+"代签");
//									}
//									initiator.append("<br>身份证号:");
//									initiator.append(customInfo.getIdentityCard());
//									initiator.append("<br>签署时间:");
//									initiator.append(signTime);
//									initiator.append("<i class=\"icon-personal\"></i></div>");
//									initiator.append("</div></td><td><div class=\"timestamp\"><p>");
//	//								initiator.append(signInfo.get("tsa"));
//									initiator.append(signTime);
//									initiator.append("</p> </div></td></tr>");
//								}
//							}
//							else if(identity.getType() ==2)//企业用户
//							{
//								CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
//								if(null != companyInfo)
//								{
//									userName = companyInfo.getCompanyName();//公司名
//									initiator.append("<td><div class=\"sign-seal\">");
//									initiator.append("<div class=\"sign-inner\">企业名称:");
//									initiator.append(userName);
//									if(signRecord.getAuthorId() != 0)
//									{
//										int id = signRecord.getAuthorId();
//										IdentityEntity tempIdentity =  identityDao.findById(id);
//										CustomInfoEntity custom = tempIdentity.getCCustomInfo();
//										CompanyInfoEntity company = tempIdentity.getCCompanyInfo();
//										String name = "";
//										if(tempIdentity.getType() == 1)
//										{
//											if(null != custom)
//											{
//												name = custom.getUserName();
//											}
//										}
//										else if(tempIdentity.getType() == 2)
//										{
//											if(null != company)
//											{
//												name = company.getCompanyName();
//											}
//										}
//										initiator.append("<br>由"+name+"代签");
//									}
//									initiator.append("<br>工商营业执照号:");
//									initiator.append(companyInfo.getBusinessLicenseNo());
////									initiator.append("<br>法定代表人:");
//									initiator.append("<br>签署人:");
//									initiator.append(identity.getCCustomInfo().getUserName());
//									initiator.append("<br>身份证号:");
//									initiator.append(identity.getCCustomInfo().getIdentityCard());
//									initiator.append("<br>签署时间:");
//									initiator.append(signTime);
//									initiator.append("<i class=\"icon-company\"></i></div>");
//									initiator.append("</div></td><td><div class=\"timestamp\"><p>");
//									initiator.append(signTime);
//									initiator.append("</p> </div></td></tr>");
//								}
//							}
//							initiator.append("</tbody>");
//						}
						listCustomer.add(customerSign);
						listCert.add(cert);
						
					}
					doc.getElementById("receiver").append(receiver.toString());//接收方
//					doc.getElementById("initiator").append(initiator.toString());//发起方						
				}
				//服务组签名
				if(null != listServerSignRecord && listServerSignRecord.size()>0)
				{
					Map<String,String> serverSign = new HashMap<String,String>();//服务组电子签名值(含时间戳)
					for(int i=0;i<listServerSignRecord.size();i++)
					{
						SignRecordEntity signRecord = listServerSignRecord.get(i);
						String signData = signRecord.getSigndata();
						Map<String,String> signInfo = gson.fromJson(signData, Map.class);
						serverSign.put("Signature", signInfo.get("sign"));
						serverSign.put("TimeStamp", signInfo.get("tsa"));
						listServer.add(serverSign);
					}
				}
				String dataDigest = FileUtil.readTxtFile(downPath+"/ContractRecord/ContractSHA1.txt");
				log.info("dataDigest==="+dataDigest);
				String tempServer = gson.toJson(listServer);
				tempServer = tempServer.replaceAll("\"", "");
				String tempCustomer = gson.toJson(listCustomer);
				tempCustomer = tempCustomer.replaceAll("\"", "");
				String tempCert =  gson.toJson(listCert);
				tempCert = tempCert.replaceAll("\"", "");
				doc.getElementById("serverSign").append("<input id=\"v_p3\" type=\"hidden\" value='"+tempServer+"'/>");//服务组电子签名值(含时间戳)
				doc.getElementById("customerSign").append("<input id=\"v_p1\" type=\"hidden\" value='"+tempCustomer+"'/>");//用户组电子签名值
				doc.getElementById("publicKey").append("<input id=\"v_p2\" type=\"hidden\" value='"+tempCert+"'/>");//数字证书公钥
				doc.getElementById("dataDigest").append("<input id=\"v_p4\" type=\"hidden\" value='"+dataDigest+"'/>");//数据摘要值
				String content = doc.toString();			
	            FileUtil.writeTxtFile(content, new File(downPath+"/预览合同.html"));
	            File dir=new File(downPath); 
	            GetAllFiles outter = new GetAllFiles();
		        GetAllFiles.Inner inner = outter.new Inner();  //必须通过Outter对象来创建
		        inner.getAllFiles(dir, 0);
		        List<File> list  = outter.getList();    
		        Collections.sort(list);
	        	StringBuffer hash = new StringBuffer("123456789"); 
				for(int i=0;i<list.size();i++)
				{
					if(!"hash.txt".equals(list.get(i).getName()))
					{
						String tempImgSha1 = SHA_MD.encodeFileSHA1(list.get(i)).toHexString();
						hash.append(tempImgSha1);
						log.info("文件:"+list.get(i)+",哈希值:"+tempImgSha1);
					}						
				}
				String shaStr = SHA_MD.strSHA1(hash.toString());
				FileUtil.writeTxtFile(shaStr.toUpperCase(), new File(downPath+"/hash.txt"));
	//            String zipPath = "E:\\zip";
	            String zipPath = IConf.getValue("CONTRACT_ZIP");
	    		String zipFileName = serialNum + ".zip";
	    		log.info("downPath==="+downPath);
	            ZipUtil.zip(downPath, zipPath, zipFileName);
	            returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], zipPath+zipFileName);
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
	 * @param datamap
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public ReturnData zipDownload(Map<String, String> datamap) throws ServiceException 
	{
		ReturnData returnData = null;
		try
		{
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
			//先查看 平台ID是否已经存在
			PlatformEntity platformEntity = null;
			try {
				platformEntity = platformDao.findPlatformByAppId(appId);
			} catch (Exception e) {
				log.info("查询平台表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			//平台ID不存在  抛出异常
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			//查询IdentityEntity
			IdentityEntity identityEntity = null;
			try {
				identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
			} catch (Exception e) {
				log.info("查询用户表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == identityEntity)
			{
				log.info("签署用户不存在");
				throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
			}
			ContractEntity contract = null;
			try {
				contract = contractDao.findContractByAppIAndOrderId(orderId,platformEntity);
			} catch (Exception e) {
				log.info("查询合同表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != contract)
			{
				returnData = compressionContract(contract, ucid);
			}
			else
			{
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
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

	@Override
	public ReturnData internetFinanceDownload(Map<String, String> datamap) throws ServiceException 
	{
		ReturnData returnData = null;
		try
		{
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
			//先查看 平台ID是否已经存在
			PlatformEntity platformEntity = null;
			try {
				platformEntity = platformDao.findPlatformByAppId(appId);
			} catch (Exception e) {
				log.info("查询平台表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			//平台ID不存在  抛出异常
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			//查询IdentityEntity
			IdentityEntity identityEntity = null;
			try {
				identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
			} catch (Exception e) {
				log.info("查询用户表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == identityEntity)
			{
				log.info("签署用户不存在");
				throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
			}
			ContractEntity contract = null;
			try {
				contract = contractDao.findContractByAppIAndOrderId(orderId,platformEntity);
			} catch (Exception e) {
				log.info("查询合同表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != contract)
			{
				returnData = compressionContract(contract, ucid);
			}
			else
			{
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
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
	@Override
	public ReturnData baoquanDownload(String serialNum) throws ServiceException 
	{
		ReturnData returnData = null;
		FileInputStream input = null;
		FtpUtil ftpUtil = null;
		try
		{
			ftpUtil = new FtpUtil(IConf.getValue("PROTECT_FTP_IP"),
					Integer.parseInt(IConf.getValue("PROTECT_FTP_PORT")),
					IConf.getValue("PROTECT_ACCOUNT"), 
					IConf.getValue("PROTECT_PASSWORD"));

			ContractEntity contract = null;
			try {
				contract = contractDao.findContractBySerialNum(serialNum);
			} catch (Exception e) {
				log.info("查询合同表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != contract)
			{
				//将合同打zip包
				ReturnData  rd = compressionContract(contract, "");
				returnData = rd;
				//获取打好包的路径
				if(rd != null)
				{
					String zipPath = rd.getPojo();
					System.out.println("zipPath==="+zipPath);
					//将zip包传到公证处服务器,用ftp协议传输
					/*
					ftp = new FTPClient();
					ftp.connect(IConf.getValue("PROTECT_FTP_IP"), Integer.parseInt(IConf.getValue("PROTECT_FTP_PORT")));
					ftp.login(IConf.getValue("PROTECT_ACCOUNT"), IConf.getValue("PROTECT_PASSWORD"));
					ftp.setFileType(FTPClient.BINARY_FILE_TYPE);//防止zip包损坏
					int reply = ftp.getReplyCode();
					System.out.println("reply==="+reply);
					ftp.changeWorkingDirectory(IConf.getValue("PROTECT_PATH"));
					File file = new File(zipPath);      
					input = new FileInputStream(file);   					  
					boolean  b = ftp.storeFile(file.getName(), input);
					*/ 
					
					if(ftpUtil.ftpLogin())
					{
						boolean b =  ftpUtil.uploadFile(new File(zipPath), IConf.getValue("PROTECT_PATH"));
						if(b)
						{
							 returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],""); 	
						}
					}
					else
					{
						throw new ServiceException(ConstantUtil.CONNECT_SERVER_FAILURE[0],ConstantUtil.CONNECT_SERVER_FAILURE[1],ConstantUtil.CONNECT_SERVER_FAILURE[2]);
					}
				}
				else
				{
					throw new ServiceException(ConstantUtil.PROTECT_ZIP_FAILURE[0],ConstantUtil.PROTECT_ZIP_FAILURE[1],ConstantUtil.PROTECT_ZIP_FAILURE[2]);
				}
			}
			else
			{
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		finally
		{		
			if(null != ftpUtil)
			{
				ftpUtil.ftpLogOut();
			}
		}
		return returnData;
	}
	/**
	 * 2.0下载
	 * @param datamap
	 * @return
	 * @throws ServiceException
	 */
	public ReturnData zipDownload2(ContractEntity contractEntity) throws ServiceException
	{
//		String contractPath1 = "/sharefile/mmecommon/contract/";
//		String contractPath = "E:/office/201604/";
		String contractPath = IConf.getValue("CONTRACTPATH_2");
		ReturnData returnData = null;
		try{
			String serialNum = contractEntity.getSerialNum();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			
			
//			if (null != contractEntity) 
//			{
				Date date = contractEntity.getDeadline();
				
				String time = formatter.format(date);
				StringBuffer contractSHA1_content1 = new StringBuffer("ContSerialNum:");
				contractSHA1_content1.append(contractEntity.getSerialNum());
				contractSHA1_content1.append("\r\nTime:");
				contractSHA1_content1.append(time);
				contractSHA1_content1.append("\r\n\r\n");
				
				
				StringBuffer contractSHA1_content1_out=new StringBuffer();
				contractSHA1_content1_out.append(contractSHA1_content1);
				contractSHA1_content1_out.append("Version:Java");
								
				//生成UserGroupSign.sg文件内容
				StringBuffer sign_content = new StringBuffer("ContSerialNum:");
				sign_content.append(serialNum);
				sign_content.append("\r\nTime:");
				sign_content.append(time);
				sign_content.append("\r\nTitle:");
				sign_content.append(contractEntity.getTitle());
				sign_content.append("\r\n\r\n");
				File input = new File(contractPath+"zipindex.html");
				Document doc = Jsoup.parse(input, "UTF-8", "");
				List<SignRecordEntity> listRecord = signRecordDao.querySignRecordByContractId(contractEntity);
				if(null != listRecord && ! listRecord.isEmpty())
				{
					for(int i=0;i<listRecord.size();i++)
					{
						SignRecordEntity signRecord = listRecord.get(i);
						
						IdentityEntity identity = signRecord.getCIdentity();
						CustomInfoEntity customInfo = identity.getCCustomInfo();
						String userName = "";
						String signTime = signRecord.getSignTime() == null ? "": DateUtil.toDateYYYYMMDDHHMM2(signRecord.getSignTime());
						if(null != identity)
						{
							//发起方
							if(identity.getType() ==1)//个人用户
							{						
								if(null != customInfo)
								{
									userName = identity.getCCustomInfo().getUserName();//姓名
								}
							}
							else if(identity.getType() ==2)//企业用户
							{
								CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
								if(null != companyInfo)
								{
									userName = companyInfo.getCompanyName();//公司名
								}
							}
						}
						
						doc.getElementById("sign").append("<p><span class='gray inblock'>签&nbsp;&nbsp;署&nbsp;方：</span>"+userName+"</p><p><span class='gray'>签署时间：</span>"+signTime+"</p>");
						//新版zip包处理追加txt开始
						sign_content.append(signRecord.getSigndata());
						sign_content.append("\r\n\r\n");
 					}
				}
				StringBuffer contractSHA1_content2 = new StringBuffer("Name:ContractRecord/Contract/Z_");
				//查主合同
				ContractPathEntity contractPathEntity = contractPathDao.findContractPathByContractId(contractEntity);
				int z = 1;
				String z_fujianPath = contractPathEntity.getFilePath();
				contractSHA1_content2.append(z);   
				contractSHA1_content2.append("_");  
				contractSHA1_content2.append(contractPathEntity.getAttName());
				contractSHA1_content2.append(".");
				contractSHA1_content2.append(contractPathEntity.getExtension());
				contractSHA1_content2.append("\r\n");
				contractSHA1_content2.append("SHA1-Digest:");
				File f = new File(z_fujianPath);
				contractSHA1_content2.append(contractEntity.getSha1());
				contractSHA1_content2.append("\r\n\r\n");
				
				String downPath = IConf.getValue("CONTRACT_ZIP") + serialNum;
				String contractRecordPath = downPath + "/ContractRecord/Contract/";
				String name = contractPathEntity.getAttName(); //附件名字
				//存放js和index所需图片
				String images = downPath + "/images";
				//将pdf转为图片的路径
				String imgRecordPath = downPath + "/ContractImg/"+ name;
				//创建一个下载目录
				FileUtil.createDir(downPath);
				// contractRecordPath目录
				FileUtil.createDir(contractRecordPath);
				////将pdf转为图片的路径
				FileUtil.createDir(imgRecordPath);
				//存放js和index所需图片
				FileUtil.createDir(images);

				StringBuffer serverSign = new StringBuffer("ContSerialNum:");
				serverSign.append(serialNum);
				serverSign.append("\r\nTime:");
				serverSign.append(time);
				serverSign.append("\r\nServerIP:\r\n");
				
				StringBuffer contractSHA1_content = new StringBuffer();
				contractSHA1_content.append(contractSHA1_content1);
				contractSHA1_content.append(contractSHA1_content2);
		
				FileUtil.writeTxtFile(contractSHA1_content1_out.toString(),new File(downPath+ "/ContractRecordSHA1.txt"));
				FileUtil.writeTxtFile(serverSign.toString(), new File(downPath + "/ServerSign.sg"));
				
				//UserGroupSign.sg
				FileUtil.writeTxtFile(sign_content.toString(), new File(downPath + "/ContractRecord/UserGroupSign.sg"));							
				FileUtil.writeTxtFile(contractSHA1_content.toString(), new File(downPath + "/ContractRecord/ContractSHA1.txt"));
					//添加校验文件					
//				FileUtil.writeTxtFile(verifyStr, new File(downPath + "/ContractRecord/ForVerify"));
	
				//复制合同文件
				FileUtil.copyFile(z_fujianPath, contractRecordPath + "/Z_"+z+"_"+new File(z_fujianPath).getName());
				//复制图片文件
				FileUtil.copyFolder(new File(contractPath+serialNum+"/img/"+name),new File(imgRecordPath));

				List<String> imgPath =  imgPath(contractPath+serialNum+"/"+"img/"+name);

				Collections.sort(imgPath);

				doc.getElementById("serialNum").append(serialNum);
				for(int i=0;i<imgPath.size();i++)
				{
					doc.getElementById("mainContract").append("<volist name='pdf' id='vo'> <img class='contractimg' src='./ContractImg/"+name+"/"+i+"."+IConf.getValue("SUFFIX_2")+"' style='width: 840px;'> </volist>");
				}
				
				List<ContractPathEntity> listContractPath = contractPathDao.findListContractPathByContractId(contractEntity);//查询附件
				List<String> listAttrPath = new ArrayList<String>();//存放附件路径
				List<String> listAttrName = new ArrayList<String>();//附件名字
				List<String> listSuffix = new ArrayList<String>();//附件名字
				if(null != listContractPath && !listContractPath.isEmpty())
				{
					for(int i=0;i<listContractPath.size();i++)
					{
						ContractPathEntity tempContractPath = listContractPath.get(i);
						if(tempContractPath.getType() ==2)
						{
							String tempAttrName = tempContractPath.getAttName();
							List<String> imgPathAttr =  imgPath(contractPath+serialNum+"/"+"img/"+tempAttrName);						
							for(int j=0;j<imgPathAttr.size();j++)
							{
								doc.getElementById("attachment"+(j+1)).append("<volist name='pdf' id='vo'> <img class='contractimg' src='./ContractImg/"+tempAttrName+"/"+j+"."+IConf.getValue("SUFFIX_2")+"' style='width: 840px;'> </volist>");
							}
						}
						listAttrPath.add(tempContractPath.getFilePath());
						listAttrName.add(tempContractPath.getAttName());
						listSuffix.add(tempContractPath.getExtension());
					}
				}
				
				//复制合同附件和合同图片
				for(int i=0;i<listAttrPath.size();i++)
				{
					FileUtil.copyFile(listAttrPath.get(i), contractRecordPath + "/F_"+(i+1)+"_"+new File(listAttrPath.get(i)).getName());
//					if()
					FileUtil.copyFolder(new File(contractPath+serialNum+"/img/"+listAttrName.get(i)),new File(downPath + "/ContractImg/"+listAttrName.get(i)));
				}
				
				String content = doc.toString();
				
	            FileUtil.writeTxtFile(content, new File(downPath+"/预览合同.html"));

				String zipPath = IConf.getValue("CONTRACT_ZIP");
	    		String zipFileName = serialNum + ".zip";
	    		log.info("downPath==="+downPath);
	            ZipUtil.zip(downPath, zipPath, zipFileName);
	            returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], zipPath+zipFileName);
//			}
//			else
//			{
//				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
//			}
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));	
		}
		return returnData;
	}

	@Override
	public ReturnData pdfDownload(Map<String, String> datamap) throws ServiceException {
		ReturnData returnData = null;
		try
		{
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String orderId = StringUtil.nullToString(datamap.get("orderId"));
			String ucid = StringUtil.nullToString(datamap.get("ucid"));
			//先查看 平台ID是否已经存在
			PlatformEntity platformEntity = null;
			try {
				platformEntity = platformDao.findPlatformByAppId(appId);
			} catch (Exception e) {
				log.info("查询平台表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			//平台ID不存在  抛出异常
			if(null == platformEntity)
			{
				log.info("平台不存在");
				throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			//查询IdentityEntity
			IdentityEntity identityEntity = null;
			try {
				identityEntity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
			} catch (Exception e) {
				log.info("查询用户表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == identityEntity)
			{
				log.info("签署用户不存在");
				throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
			}
			ContractEntity contract = null;
			try {
				contract = contractDao.findContractByAppIAndOrderId(orderId,platformEntity);
			} catch (Exception e) {
				log.info("查询合同表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != contract)
			{
				if(contract.getStatus() == 0 || contract.getStatus() == 1)
				{
					log.info("合同为签署完毕,不能下载");
					throw new ServiceException(ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[0],ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[1], ConstantUtil.CONTRACT_HASNOT_ALLSIGNED[2]);
				}
				if(contract.getStatus() == 3)
				{
					log.info("合同已被拒绝");
					throw new ServiceException(ConstantUtil.CONTRACT_HASBEEN_REFUSED[0],ConstantUtil.CONTRACT_HASBEEN_REFUSED[1], ConstantUtil.CONTRACT_HASBEEN_REFUSED[2]);
				}
				if(contract.getStatus() == 4)
				{
					log.info("合同已被撤销");
					throw new ServiceException(ConstantUtil.CONTRACT_HASBEEN_CANCELED[0],ConstantUtil.CONTRACT_HASBEEN_CANCELED[1], ConstantUtil.CONTRACT_HASBEEN_CANCELED[2]);
				}
				if(contract.getStatus() == 5)
				{
					log.info("合同已被撤销");
					throw new ServiceException(ConstantUtil.CONTRACT_HASBEEN_CLOSED[0],ConstantUtil.CONTRACT_HASBEEN_CLOSED[1], ConstantUtil.CONTRACT_HASBEEN_CLOSED[2]);
				}
				ContractPathEntity contractPath = contractPathDao.findContractPathByContractId(contract);
				if(null != contractPath)
				{
					Map<String ,String> map = new HashMap<String ,String>();
					map.put("serialNum", contract.getSerialNum());
					map.put("pdfFile", contractPath.getFilePath());
					returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], new Gson().toJson(map));
				}
				else
				{
					returnData =  new ReturnData(ConstantUtil.RETURN_RESULT_EMPTY[0],ConstantUtil.RETURN_RESULT_EMPTY[1], ConstantUtil.RETURN_RESULT_EMPTY[2], "");
				}
			}
			else
			{
				//合同不存在
				throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
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
	
}
