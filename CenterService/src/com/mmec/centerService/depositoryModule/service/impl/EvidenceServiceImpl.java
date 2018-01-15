package com.mmec.centerService.depositoryModule.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.mmec.centerService.depositoryModule.dao.EvidenceBindAttachmentDao;
import com.mmec.centerService.depositoryModule.dao.EvidenceDao;
import com.mmec.centerService.depositoryModule.dao.EvidenceUserDao;
import com.mmec.centerService.depositoryModule.entity.EvidenceBindAttachmentEntity;
import com.mmec.centerService.depositoryModule.entity.EvidenceEntity;
import com.mmec.centerService.depositoryModule.entity.EvidenceUserEntity;
import com.mmec.centerService.depositoryModule.service.Criterion;
import com.mmec.centerService.depositoryModule.service.Criterion.Operator;
import com.mmec.centerService.depositoryModule.service.EvidenceService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.entity.CompanyInfoEntity;
import com.mmec.centerService.userModule.entity.CustomInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ReturnData;
import com.mmec.thrift.service.CssRMIServices.Iface;
import com.mmec.util.ComposePicture;
import com.mmec.util.ConstantUtil;
import com.mmec.util.CzMap;
import com.mmec.util.FileUtil;
import com.mmec.util.GetAllFiles;
import com.mmec.util.PDFTool;
import com.mmec.util.PageResult;
import com.mmec.util.SHA_MD;
import com.mmec.util.ZipUtil;
import com.mmec.util.GetAllFiles.Inner;

@Service("evidenceService")
public class EvidenceServiceImpl implements EvidenceService {

	private Logger log = Logger.getLogger(EvidenceServiceImpl.class);

	@Autowired
	private EvidenceBindAttachmentDao ebaDao;

	@Autowired
	private EvidenceDao eDao;

	@Autowired
	private EvidenceUserDao uDao;

	@Autowired
	private IdentityDao identityDao;

	@Autowired
	private PlatformDao platformDao;

	@Autowired
	private EvidenceUserDao evidenceUserDao;

	@Autowired
	private Iface cssRMIServices;

	@PersistenceContext(unitName = "uumsJPA")
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional
	public void saveEvidenceInfo(EvidenceEntity evidence) {
		entityManager.persist(evidence);
	};

	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public IdentityEntity findIdentity(int id) throws ServiceException {
		return identityDao.findById(id);
	}

	/**
	 * 下载存证信息
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData downloadEvidence(Map<String, String> map)
			throws ServiceException {
		log.info("downloadEvidence" + map.toString());
		Gson g = new Gson();
		ReturnData rd = new ReturnData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		EvidenceEntity e = new EvidenceEntity();
		// 云签
		String zipfilename = "";
		if (null != map.get("flag") && map.get("flag").equals("yunsign")) {
			e = eDao.findEvidenceById(Integer.valueOf(map.get("id")));
			zipfilename = e.getSerial() + ".zip";
			log.info("云签");
		} else {
			// 对接
			// appid校验
			log.info("对接");
			PlatformEntity p = platformDao
					.findPlatformByAppId(map.get("appid"));
			if (null == p) {
				throw new ServiceException(
						ConstantUtil.RETURN_PLAT_NOT_EXIST[0],
						ConstantUtil.RETURN_PLAT_NOT_EXIST[1],
						ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			// userid存在性校验
			IdentityEntity looker = identityDao.queryAppIdAndPlatformUserName(
					p, map.get("userId"));
			if (null == looker) {
				log.info("looker not exist");
				throw new ServiceException(
						ConstantUtil.RETURN_USER_NOTEXIST[0],
						ConstantUtil.RETURN_USER_NOTEXIST[1],
						ConstantUtil.RETURN_USER_NOTEXIST[2]);

			}
			e = eDao.findEvidenceByAppIdAndOrderId(map.get("appid"),
					map.get("orderid"));
			// 存证不存在
			if (null == e) {
				throw new ServiceException(
						ConstantUtil.DEPOSITORY_NOT_EXIST[0],
						ConstantUtil.DEPOSITORY_NOT_EXIST[1],
						ConstantUtil.DEPOSITORY_NOT_EXIST[2]);
			}
			// 下载包名称
			zipfilename = e.getSerial() + ".zip";
			// userid是否为存证人查看
			List<EvidenceUserEntity> parterlist = uDao
					.findEvidenceUserByEvidenceId(e.getId());
			List<Integer> parterIdlist = new ArrayList<Integer>();
			if (null != parterlist && parterlist.size() > 0) {
				for (int i = 0; i < parterlist.size(); i++) {
					parterIdlist.add(parterlist.get(i).getUserid());
				}
			}
			if (!parterIdlist.contains(looker.getId())) {
				throw new ServiceException(
						ConstantUtil.DEPOSITORY_USER_PERMISSION_DENIED[0],
						ConstantUtil.DEPOSITORY_USER_PERMISSION_DENIED[1],
						ConstantUtil.DEPOSITORY_USER_PERMISSION_DENIED[2]);
			}
		}
		// 下载次数
		int oldtimes = e.getDownloadtimes();
		e.setDownloadtimes(oldtimes + 1);
		eDao.save(e);
		List<EvidenceBindAttachmentEntity> list = ebaDao
				.findEvidenceByEvidenceId(e.getId());
		// 生成存证目录文件
		File parentpath = new File(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial());
		parentpath.deleteOnExit();
		FileUtil.createDir(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial());
		// 创建存证目录文件
		File imagepath = new File(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial() + File.separator + "images");
		if (!imagepath.exists()) {
			FileUtil.createDir(IConf.getValue("CZ_ZIP") + File.separator
					+ e.getSerial() + File.separator + "images");
		}
		// 创建根目录图片
		File pictruepath = new File(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial() + File.separator + "pictrues");
		if (!pictruepath.exists()) {
			FileUtil.createDir(IConf.getValue("CZ_ZIP") + File.separator
					+ e.getSerial() + File.separator + "pictrues");
		}
		// 图像文件copy
		FileUtil.copyFolder(
				new File(IConf.getImagePath()),
				new File(IConf.getValue("CZ_ZIP") + File.separator
						+ e.getSerial() + File.separator + "images"
						+ File.separator));
		// 存证存在时,打包下载
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				File f = new File(list.get(i).getAttachmentpath());
				log.info("filepath:" + list.get(i).getAttachmentpath());
				FileUtil.copyFile(
						list.get(i).getAttachmentpath(),
						IConf.getValue("CZ_ZIP") + File.separator
								+ e.getSerial() + File.separator + f.getName());
				// 文件转图片copy
				if (null != list.get(i).getToImgpath()
						&& !"".equals(list.get(i).getToImgpath())) {
					List<String> imglist = g.fromJson(list.get(i)
							.getToImgpath(), List.class);
					for (int m = 0; m < imglist.size(); m++) {
						{
							File imgfile = new File(imglist.get(m));
							log.info("imgfile:" + imglist.get(m));
							FileUtil.copyFile(
									imglist.get(m),
									IConf.getValue("CZ_ZIP") + File.separator
											+ e.getSerial() + File.separator
											+ "pictrues" + File.separator
											+ String.valueOf(i) + "_"
											+ imgfile.getName());
						}
					}
				}
			}
		}
		// 在该目录下制造index.html页面
		File input = new File(IConf.getIndexHtmlPath());
		Document doc = null;
		;
		try {
			doc = Jsoup.parse(input, "UTF-8", "");
		} catch (IOException s) {
			log.info(FileUtil.getStackTrace(s));
			throw new ServiceException(ConstantUtil.ZIP_DOWN_INDEX[0],
					ConstantUtil.ZIP_DOWN_INDEX[1],
					ConstantUtil.ZIP_DOWN_INDEX[2]);
		}
		String title = "";
		if (null != e.getTitle() && !"".equals(e.getTitle())) {
			title = e.getTitle();
		} else {
			if (null != list && list.size() > 0) {
				File f = new File(list.get(0).getAttachmentpath());
				title = f.getName();
			}
		}
		// 存证标题
		doc.getElementById("cz_title").append(title);
		// 存证编号
		doc.getElementById("top").append("编号: " + e.getSerial());
		doc.getElementById("cz_serial").append(e.getSerial());
		// 电子证据提交方
		if (null != e.getUsername()) {
			doc.getElementById("creator").append(e.getUsername());
		} else {
			doc.getElementById("creator").append("");
		}
		SimpleDateFormat cnsdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		// 提交存管时间
		doc.getElementById("time_cn").append(cnsdf.format(e.getUploadtime()));
		doc.getElementById("time_normal").append(sdf.format(e.getUploadtime()));
		// 提交方提交的身份信息
		List<EvidenceUserEntity> userlist = evidenceUserDao
				.findEvidenceUserByEvidenceId(e.getId());
		if (null != userlist && userlist.size() > 0) {
			for (int i = 0; i < userlist.size(); i++) {
				EvidenceUserEntity u = userlist.get(i);
				IdentityEntity iden = identityDao.findById(u.getUserid());
				if (null != iden) {
					// 个人
					if (iden.getType() == (byte) 1) {
						CustomInfoEntity c = iden.getCCustomInfo();
						if (null != c) {
							doc.getElementById("parterInfo").append(
									"<tr><td>姓名：" + c.getUserName()
											+ "</br>手机: " + iden.getMobile()
											+ "</br>身份证号码: "
											+ c.getIdentityCard()
											+ "</td></tr>");
						}
						// 企业
					} else if (iden.getType() == (byte) 2) {
						CompanyInfoEntity c = iden.getCCompanyInfo();
						doc.getElementById("parterInfo").append(
								"<tr><td>企业名称："
										+ c.getCompanyName()
										+ "</br>工商营业执照号: "
										+ c.getBusinessLicenseNo()
										+ "</br>"
										+ "法定代表人: "
										+ iden.getCCustomInfo().getUserName()
										+ "</br>身份证号: "
										+ iden.getCCustomInfo()
												.getIdentityCard() + "</br>"
										+ "手机号: " + iden.getMobile()
										+ "</td></tr>");
					}
				}
			}
		}
		// 存管时间处理
		doc.getElementById("savetime").append(sdf.format(e.getUploadtime()));
		// 提交时间
		doc.getElementById("submit_time").append(
				cnsdf.format(e.getUploadtime()));
		doc.getElementById("sign_time").append(sdf.format(e.getUploadtime()));
		// 提交方处理
		IdentityEntity iden = identityDao.findById(e.getUserid());
		if (null != iden) {
			// 个人
			if (iden.getType() == (byte) 1) {
				CustomInfoEntity c = iden.getCCustomInfo();
				if (null != c) {
					doc.getElementById("submitter_info").append("姓名：");
					doc.getElementById("submitter_info")
							.append(c.getUserName());
					doc.getElementById("submitter_info").append("<br>手机号: ");
					doc.getElementById("submitter_info").append(
							iden.getMobile());
					doc.getElementById("submitter_info").append("<br>身份证号码: ");
					doc.getElementById("submitter_info").append(
							c.getIdentityCard());
					doc.getElementById("submitter_info").append(
							"<i class=\"icon-personal\"></i>");
				}
				// 企业
			} else if (iden.getType() == (byte) 2) {
				CompanyInfoEntity c = iden.getCCompanyInfo();
				doc.getElementById("submitter_info").append("企业名称: ");
				doc.getElementById("submitter_info").append(c.getCompanyName());
				doc.getElementById("submitter_info").append("<br>工商营业执照号: ");
				doc.getElementById("submitter_info").append(
						c.getBusinessLicenseNo());
				doc.getElementById("submitter_info").append("<br>法定代表人: ");
				if (null != iden.getCCustomInfo()) {
					doc.getElementById("submitter_info").append(
							iden.getCCustomInfo().getUserName());
				}
				doc.getElementById("submitter_info").append("<br>身份证号: ");
				doc.getElementById("submitter_info").append(
						iden.getCCustomInfo().getIdentityCard());
				doc.getElementById("submitter_info").append("<br>手机号: ");
				doc.getElementById("submitter_info").append(iden.getMobile());
				doc.getElementById("submitter_info").append(
						"<i class=\"icon-company\"></i>");
			}
		}

		// v_p1 客户组签名
		String signature = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				signature = signature + "Signature:"
						+ list.get(i).getSignature() + "<br/>";
			}
		}
		doc.getElementById("v_p1").val(signature);

		// v_p2 证书
		String cert = "";
		if (null != list && list.size() > 0) {
			cert = "Cert:" + list.get(0).getCert() + "<br/>";
		}
		doc.getElementById("v_p2").val(cert);

		// v_p3 服务组签名
		String timestamp = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				timestamp = timestamp + "Timestamp:"
						+ list.get(i).getSigntimestamp() + "<br/>";
			}
		}
		doc.getElementById("v_p3").val(timestamp);

		// v_p4 签名原文
		String name = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				name = name + "Name:" + list.get(i).getAttachmentpath()
						+ "<br/>" + "SHA1-Digest:" + list.get(i).getFilehash()
						+ "<br/>";
			}
		}
		doc.getElementById("v_p4").val(name);
	
		// 处理追加图片
		File imgfiles = new File(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial() + File.separator + "pictrues" + File.separator);
		File[] imglistfiles = imgfiles.listFiles();
		List<String> flstr = new ArrayList<String>();
		// 排序
		// 背景图片和log图片地址
		// String
		// backgroudsrc=IConf.getValue("CZ_ZIP")+File.separator+e.getSerial()+File.separator+"images"+File.separator+"watermark.png";
		String logosrc = IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial() + File.separator + "images" + File.separator
				+ "yz.png";
		if (!new File(logosrc).exists()) {
			log.info("logsrc is not exists");
		}
		if (null != imglistfiles && imglistfiles.length > 0) {
			for (int i = 0; i < imglistfiles.length; i++) {
				// 合成背景
				// 合成水印
				try {
					// ComposePicture.composePic(imglistfiles[i].getPath(),
					// backgroudsrc,imglistfiles[i].getPath(), 30, 10);
					String filename = imglistfiles[i].getName();
					log.info("filename" + filename);
					String tempName = filename.substring(0,filename.indexOf("."));
//					String simplename = tempName+"_"+tempName.split("_")[1] + ".jpg";
					String simplename = tempName + ".jpg";
					log.info("simplename" + simplename);
					String x = new File(imglistfiles[i].getParent()).getParent() + File.separator + "compose"+ File.separator + simplename;
					FileUtil.createDir(new File(imglistfiles[i].getParent()).getParent() + File.separator + "compose"+ File.separator);
					ComposePicture.composePic(imglistfiles[i].getPath(),logosrc, x, 840, 600);
					flstr.add(simplename);
				} catch (Exception se) {
					se.printStackTrace();
					log.info("failed to transfor to img");
					throw new ServiceException(
							ConstantUtil.RETURN_SYSTEM_ERROR[0],
							ConstantUtil.RETURN_SYSTEM_ERROR[1],
							ConstantUtil.RETURN_SYSTEM_ERROR[2]);
				}
			}
		}
		Collections.sort(flstr);
		if (null != flstr && flstr.size() > 0) {
			for (int i = 0; i < flstr.size(); i++)
				doc.getElementById("mainContract").append(
						"<img class=\"contractimg\" src=\"compose/"
								+ flstr.get(i) + "\">");
		}
		String content = doc.toString();
		File indexhtml = new File(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial() + File.separator + "index.html");
		if (!indexhtml.exists()) {
			try {
				indexhtml.createNewFile();
			} catch (IOException io) {
				io.printStackTrace();
				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
						ConstantUtil.RETURN_SYSTEM_ERROR[1],
						ConstantUtil.RETURN_SYSTEM_ERROR[2]);
			}
		}
		FileUtil.writeTxtFile(content, indexhtml);
		File zipfile = new File(IConf.getValue("CZ_ZIP"), e.getSerial()
				+ ".zip");
		zipfile.deleteOnExit();
		if (null != imglistfiles && imglistfiles.length > 0) {
			for (int i = 0; i < imglistfiles.length; i++) {
				if (imglistfiles[i].getName().endsWith(".png")) {
//					imglistfiles[i].delete();
				}
			}
		}
		//生成防篡改文件
		String dirpath=IConf.getValue("CZ_ZIP") + File.separator + e.getSerial();
		File dirFile=new File(dirpath);
		GetAllFiles outter = new GetAllFiles();
        GetAllFiles.Inner inner = outter.new Inner();  //必须通过Outter对象来创建
        inner.getAllFiles(dirFile, 0);
        List<File> dirlistfiles  = outter.getList();    
        Collections.sort(dirlistfiles);
    	StringBuffer hash = new StringBuffer("123456789"); 
        for(int i=0;i<dirlistfiles.size();i++){
            String tempImgSha1 = SHA_MD.encodeFileSHA1(dirlistfiles.get(i)).toHexString();
    		hash.append(tempImgSha1);
        }
		String shaStr = SHA_MD.strSHA1(hash.toString());
		FileUtil.writeTxtFile(shaStr.toUpperCase(), new File(dirpath+"/hash.txt"));
		log.info("dirpath==="+dirpath);
		
		// 打包
		ZipUtil.zip(IConf.getValue("CZ_ZIP") + File.separator + e.getSerial(),
				IConf.getValue("CZ_ZIP") + File.separator, e.getSerial()
						+ ".zip");
		rd = new ReturnData(ConstantUtil.RETURN_SUCC[0],
				ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],
				IConf.getValue("CZ_ZIP") + File.separator + zipfilename);
		log.info(IConf.getValue("CZ_ZIP") + File.separator+zipfilename);
		return rd;
	}

	/**
	 * 保存存证信息(结果存证/过程存证 1 过程存证 2 结果存证)
	 * 
	 * @throws TException
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData saveEvidence_bak(Map<String, String> map)
			throws ServiceException, TException {
		log.info("saveEvidence" + map.toString());
		ReturnData rd = new ReturnData();
		Gson g = new Gson();
		// 第一次保存存证
		if (null != map.get("creatorname")) 
		{
			EvidenceEntity e = new EvidenceEntity();
			PlatformEntity p = platformDao
					.findPlatformByAppId(map.get("appid"));
			if (null == p) 
			{
				throw new ServiceException(
						ConstantUtil.RETURN_PLAT_NOT_EXIST[0],
						ConstantUtil.RETURN_PLAT_NOT_EXIST[1],
						ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}
			IdentityEntity identity = identityDao
					.queryAppIdAndPlatformUserName(p, map.get("creatorname"));
			if (null == identity) 
			{
				log.info("creator not exist");
				throw new ServiceException(
						ConstantUtil.RETURN_USER_NOTEXIST[0],
						ConstantUtil.RETURN_USER_NOTEXIST[1],
						ConstantUtil.RETURN_USER_NOTEXIST[2]);

			}
			// 如果该订单存在,返回异常
			if (null != eDao.findEvidenceByAppIdAndOrderId(map.get("appid"),
					map.get("orderid"))) 
			{
				log.info("appid:" + map.get("appid") + "orderid:"
						+ map.get("orderid") + "evidence is already exist");
				throw new ServiceException(
						ConstantUtil.DEPOSITORY_IS_ALREADY_EXIST[0],
						ConstantUtil.DEPOSITORY_IS_ALREADY_EXIST[1],
						ConstantUtil.DEPOSITORY_IS_ALREADY_EXIST[2]);
			}
			if (identity.getType() == (byte) 1) {
				// 个人
				if (null != identity.getCCustomInfo()) {
					e.setUsername(identity.getCCustomInfo().getUserName());
				}
			} else if (identity.getType() == (byte) 2) {
				// 企业
				e.setUsername(identity.getCCompanyInfo().getCompanyName());
			}
			if (null == e.getUsername()) {
				e.setUsername("");
			}
			e.setAppid(map.get("appid"));
			e.setOrder(map.get("orderid"));
			e.setUploadtime(new Date());
			e.setUpdatetime(new Date());
			e.setDownloadtimes(0);
			e.setUserid(identity.getId());
			e.setSerial("CZ" + System.currentTimeMillis());
			if (null != map.get("title")) {
				e.setTitle(map.get("title"));
			} else {
				e.setTitle("");
			}
			e.setType(map.get("type"));
			this.saveEvidenceInfo(e);
			int evidenceId = e.getId();

			// 处理存证和人员关联
			if (null != map.get("userstr") && !"".equals(map.get("userstr"))) {
				String[] useridStr = map.get("userstr").split(",");
				for (int i = 0; i < useridStr.length; i++) {
					IdentityEntity iden = identityDao
							.queryAppIdAndPlatformUserName(p, useridStr[i]);
					if (null == iden) {
						log.info("partor not exist");
						throw new ServiceException(
								ConstantUtil.RETURN_USER_NOTEXIST[0],
								ConstantUtil.RETURN_USER_NOTEXIST[1],
								ConstantUtil.RETURN_USER_NOTEXIST[2]);
					}
					EvidenceUserEntity u = new EvidenceUserEntity();
					u.setEvidence_id(evidenceId);
					u.setUserid(iden.getId());
					uDao.save(u);
				}
			}

			// 处理存证和文件关联--并且对文件签名
			if (null != map.get("filepathlist")
					&& !"".equals(map.get("filepathlist"))) {
				log.info("filepathlist:" + map.get("filepathlist"));
				String[] filepathArray = map.get("filepathlist").split(",");
				for (int i = 0; i < filepathArray.length; i++) {
					File f = new File(filepathArray[i]);
					if (!f.exists()) {
						throw new ServiceException(
								ConstantUtil.DEPOSITORY_FIlE_NOT_EXIST[0],
								ConstantUtil.DEPOSITORY_FIlE_NOT_EXIST[1],
								ConstantUtil.DEPOSITORY_FIlE_NOT_EXIST[2]);
					}
					String file_hash = SHA_MD.encodeFileSHA1(f).toHexString();
					// 签名值
					ResultData resData = cssRMIServices.signService(file_hash);
					// 时间戳
					String signJsonData = resData.pojo;
					Map mapData = null;
					mapData = g.fromJson(signJsonData, HashMap.class);
					String certificate = (String) mapData.get("certificate");// 证书信息
					String signature = (String) mapData.get("signature");// 签名信息
					String certFingerprint = (String) mapData
							.get("certFingerprint");// 证书指纹
					String certSerialNum = (String) mapData.get("serialNum");// 证书序列号
					ResultData timeStampRes = cssRMIServices
							.getTimestampService(e.getSerial(), certFingerprint);
					EvidenceBindAttachmentEntity eba = new EvidenceBindAttachmentEntity();
					eba.setAttachmentpath(filepathArray[i]);
					eba.setDelflag("1");
					eba.setUploadtime(new Date());
					eba.setUpdatetime(new Date());
					eba.setEvidence_id(Integer.valueOf(evidenceId));
					eba.setDownloadtimes(0);
					eba.setFilehash(file_hash);
					eba.setCert(certificate);
					eba.setSignature(signature);
					eba.setCertserial(certSerialNum);
					eba.setSigntimestamp(timeStampRes.pojo.replaceAll("\r\n",
							""));

					// 处理图片
					List<String> filelist = new ArrayList<String>();
					String path = f.getParent();
					log.info("filepath:" + path);
					String imgpath = path + File.separator
							+ System.currentTimeMillis();
					String pdfpath = filepathArray[i];
					log.info("pdfpath:" + pdfpath);
					if (f.getName().endsWith(".pdf")|| f.getName().endsWith(".doc") || f.getName().endsWith(".docx"))
					{
						// 如果是doc文件
						if (f.getName().endsWith(".doc")  || f.getName().endsWith(".docx")) 
						{
							log.info("docpath:" + filepathArray[i] + "_"
									+ imgpath + ".pdf");
//							PDFTool.htmlToPdfLibreOffice(filepathArray[i],imgpath);
							log.info("imgpath==="+imgpath);
							String fileName = f.getName();
							File attrFile = new File(imgpath+"/");
							if (!attrFile.exists())
							{
								attrFile.mkdirs();
							}
							String tempFile = imgpath+"/"+fileName.substring(0, fileName.lastIndexOf("."))+".pdf";
							log.info("word转pdf的路径为:"+tempFile);
							PDFTool.wordToPdf(filepathArray[i],tempFile);							
							File[] files = new File(imgpath).listFiles();
							pdfpath = files[0].getPath();
						}
						log.info("imgpath:" + imgpath);
						File imgfile = new File(imgpath);
						if (!imgfile.exists())
							imgfile.mkdir();
						log.info("pdfToImg:" + pdfpath + "_" + imgpath);
						Map<String, String> pdfTomImgMap = new HashMap<String, String>();
						pdfTomImgMap.put("optFrom", "NULL");
						pdfTomImgMap.put("appId", "NULL");
						pdfTomImgMap.put("ucid", "NULL");
						pdfTomImgMap.put("IP", "NULL");
						PDFTool.pdfToImg(pdfpath, imgpath, pdfTomImgMap);
						File[] files = imgfile.listFiles();
						if (null != files && files.length > 0) {
							for (int j = 0; j < files.length; j++) {
								if (files[j].getName().toUpperCase()
										.endsWith(".PNG")
										|| files[j].getName().toUpperCase()
												.endsWith(".JPG")
										|| files[j].getName().toUpperCase()
												.endsWith(".JPEG")) {
									filelist.add(files[j].getPath());
								}
							}
						}
					}
					if (f.getName().toUpperCase().endsWith(".PNG")
							|| f.getName().toUpperCase().endsWith(".JPG")) {
						filelist.add(filepathArray[i]);
					}
					eba.setToImgpath(g.toJson(filelist));
					ebaDao.save(eba);
				}
			}
			rd = new ReturnData(ConstantUtil.RETURN_SUCC[0],
					ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],
					g.toJson(e));
		} 
		else 
		{
			EvidenceEntity e = eDao.findEvidenceByAppIdAndOrderId(
					map.get("appid"), map.get("orderid"));
			if (null == e) {
				log.info("appid:" + map.get("appid") + ",orderid:"
						+ map.get("orderid") + "Evidence is not found");
				throw new ServiceException(
						ConstantUtil.DEPOSITORY_NOT_EXIST[0],
						ConstantUtil.DEPOSITORY_NOT_EXIST[1],
						ConstantUtil.DEPOSITORY_NOT_EXIST[2]);
			}
			int evidenceId = e.getId();
			File f = new File(map.get("filepath"));
			// 文件不存在
			if (!f.exists()) {
				throw new ServiceException("", "", "");
			}
			// 存证和文件关联--并且处理文件签名
			if (null != map.get("filepathlist")
					&& !"".equals(map.get("filepathlist"))) {
				String[] filepathArray = map.get("filepathlist").split(",");
				for (int i = 0; i < filepathArray.length; i++) {
					File ff = new File(filepathArray[i]);
					if (!ff.exists()) {
						throw new ServiceException("", "", "");
					}
					String file_hash = SHA_MD.encodeFileSHA1(f).toHexString();
					// 签名值
					ResultData resData = cssRMIServices.signService(file_hash);
					// 时间戳
					String signJsonData = resData.pojo;
					Map mapData = null;
					mapData = g.fromJson(signJsonData, HashMap.class);
					String certificate = (String) mapData.get("certificate");// 证书信息
					String signature = (String) mapData.get("signature");// 签名信息
					String certFingerprint = (String) mapData
							.get("certFingerprint");// 证书指纹
					String certSerialNum = (String) mapData.get("serialNum");// 证书序列号
					ResultData timeStampRes = cssRMIServices
							.getTimestampService(e.getSerial(), certFingerprint);
					EvidenceBindAttachmentEntity eba = new EvidenceBindAttachmentEntity();
					eba.setAttachmentpath(filepathArray[i]);
					eba.setDelflag("1");
					eba.setUploadtime(new Date());
					eba.setUpdatetime(new Date());
					eba.setEvidence_id(Integer.valueOf(map.get("evidenceId")));
					eba.setDownloadtimes(0);
					eba.setFilehash(file_hash);
					eba.setCert(certificate);
					eba.setSignature(signature);
					eba.setSigntimestamp(timeStampRes.pojo.replaceAll("\r\n",
							""));
					eba.setCertserial(certSerialNum);
					ebaDao.save(eba);
				}
			}

			PlatformEntity p = platformDao
					.findPlatformByAppId(map.get("appid"));
			// 平台不存在
			if (null == p) {
				throw new ServiceException(
						ConstantUtil.RETURN_PLAT_NOT_EXIST[0],
						ConstantUtil.RETURN_PLAT_NOT_EXIST[1],
						ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
			}

			// 处理存证和人员关联
			if (null != map.get("userstr") && !"".equals(map.get("userstr"))) {
				String[] useridStr = map.get("userstr").split(",");
				for (int i = 0; i < useridStr.length; i++) {
					IdentityEntity iden = identityDao
							.queryAppIdAndPlatformUserName(p, useridStr[i]);
					if (null == iden) {
						throw new ServiceException(
								ConstantUtil.RETURN_USER_NOTEXIST[0],
								ConstantUtil.RETURN_USER_NOTEXIST[1],
								ConstantUtil.RETURN_USER_NOTEXIST[2]);
					}
					EvidenceUserEntity u = new EvidenceUserEntity();
					u.setEvidence_id(evidenceId);
					u.setUserid(iden.getId());
					uDao.save(u);
				}
			}
		}
		return rd;
	}
	/**
	 * 保存存证信息(结果存证/过程存证 1 过程存证 2 结果存证)
	 * 
	 * @throws TException
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData saveEvidence(Map<String, String> map)
			throws ServiceException, TException {
		long s_time = System.currentTimeMillis();
		log.info("saveEvidence" + map.toString());
		ReturnData rd = new ReturnData();
		Gson g = new Gson();

		EvidenceEntity e = new EvidenceEntity();
		PlatformEntity p = platformDao.findPlatformByAppId(map.get("appid"));
		if (null == p) 
		{
			throw new ServiceException(
					ConstantUtil.RETURN_PLAT_NOT_EXIST[0],
					ConstantUtil.RETURN_PLAT_NOT_EXIST[1],
					ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
		}
		IdentityEntity identity = identityDao.queryAppIdAndPlatformUserName(p, map.get("creatorname"));
		if (null == identity) 
		{
			log.info("creator not exist");
			throw new ServiceException(
					ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1],
					ConstantUtil.RETURN_USER_NOTEXIST[2]);

		}
		// 如果该订单存在,返回异常
		if (null != eDao.findEvidenceByAppIdAndOrderId(map.get("appid"),
				map.get("orderid"))) 
		{
			log.info("appid:" + map.get("appid") + "orderid:"
					+ map.get("orderid") + "evidence is already exist");
			throw new ServiceException(
					ConstantUtil.DEPOSITORY_IS_ALREADY_EXIST[0],
					ConstantUtil.DEPOSITORY_IS_ALREADY_EXIST[1],
					ConstantUtil.DEPOSITORY_IS_ALREADY_EXIST[2]);
		}
		if (identity.getType() == (byte) 1) {
			// 个人
			if (null != identity.getCCustomInfo()) {
				e.setUsername(identity.getCCustomInfo().getUserName());
			}
		} else if (identity.getType() == (byte) 2) {
			// 企业
			e.setUsername(identity.getCCompanyInfo().getCompanyName());
		}
		if (null == e.getUsername()) {
			e.setUsername("");
		}
		e.setAppid(map.get("appid"));
		e.setOrder(map.get("orderid"));
		e.setUploadtime(new Date());
		e.setUpdatetime(new Date());
		e.setDownloadtimes(0);
		e.setUserid(identity.getId());
		String czSerial = "CZ" + FileUtil.getOrderIdByUUId();
		e.setSerial(czSerial);
		String czFilePath = FileUtil.createEvidenceFolder(czSerial);
		//动态添加数据到模板文件,另存为html文件
		File file = new File(czFilePath);
		if (!file.exists())
		{
			file.mkdirs();
		}
		
		if (null != map.get("title")) {
			e.setTitle(map.get("title"));
		} else {
			e.setTitle("");
		}
		e.setType(map.get("type"));
		this.saveEvidenceInfo(e);
		int evidenceId = e.getId();

		// 处理存证和人员关联
		if (null != map.get("userstr") && !"".equals(map.get("userstr"))) 
		{
			String[] useridStr = map.get("userstr").split(",");
			for (int i = 0; i < useridStr.length; i++) {
				IdentityEntity iden = identityDao
						.queryAppIdAndPlatformUserName(p, useridStr[i]);
				if (null == iden) {
					log.info("partor not exist");
					throw new ServiceException(
							ConstantUtil.RETURN_USER_NOTEXIST[0],
							ConstantUtil.RETURN_USER_NOTEXIST[1],
							ConstantUtil.RETURN_USER_NOTEXIST[2]);
				}
				EvidenceUserEntity u = new EvidenceUserEntity();
				u.setEvidence_id(evidenceId);
				u.setUserid(iden.getId());
				uDao.save(u);
			}
		}

		// 处理存证和文件关联--并且对文件签名
		if (null != map.get("filepathlist")&& !"".equals(map.get("filepathlist"))) 
		{
			log.info("filepathlist:" + map.get("filepathlist"));
			String[] filepathArray = map.get("filepathlist").split(",");
//			ThreadPoolExecutor t = 
			
			
//			Executor cachedThreadPool  = Executors.newFixedThreadPool(filepathArray.length);
//			Executor cachedThreadPool_1  = Executors.newCachedThreadPool();
			for(int count =0;count<filepathArray.length;count++)
			{
				ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("测试多线程_"+count).build();
				Future future = Executors.newCachedThreadPool(namedThreadFactory).submit(new EvidenceAuxiliary(filepathArray, count, czFilePath, 
						evidenceId,cssRMIServices,ebaDao,e.getSerial()));
				System.out.println("future:"+future.isDone());
//				cachedThreadPool_1 .execute(new EvidenceAuxiliary(filepathArray, count, czFilePath, 
//						evidenceId,cssRMIServices,ebaDao,e.getSerial()));
			}

			rd = new ReturnData(ConstantUtil.RETURN_SUCC[0],
					ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],
					g.toJson(e));
		}	
		long e_time = System.currentTimeMillis();
		System.out.println("耗时:"+(e_time-s_time));
		return rd;
	}
	
	public void test(String[] filepathArray,String czFilePath,int evidenceId,String serialNum) throws ServiceException, TException
	{
		Gson g = new Gson();
		for (int i = 0; i < filepathArray.length; i++) 
		{
			File srcFile = new File(filepathArray[i]);
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
			FileUtil.copyFolder(new File(filepathArray[i]),czFile);//拷贝
			String file_hash = SHA_MD.encodeFileSHA1(czFile).toHexString();
			// 签名值
			ResultData resData = cssRMIServices.signService(file_hash);
			// 时间戳
			String signJsonData = resData.pojo;
			Map mapData = null;
			mapData = g.fromJson(signJsonData, HashMap.class);
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
			
			String imgPath = czFilePath + System.currentTimeMillis();
			String pdfPath = czFileStr;
//				log.info("czFileStr:" + pdfpath);
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
//						File[] files = new File(imgPath).listFiles();
					pdfPath = tempFile;
				}
//					log.info("imgpath:" + imgPath);
//					log.info("pdfToImg:" + pdfpath + "_" + imgPath);
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
		}
	}
	/**
	 * 保存存证信息(结果存证/过程存证 1 过程存证 2 结果存证)
	 * 
	 * @throws TException
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData saveEvidence_new(Map<String, String> map)
			throws ServiceException, TException {
		log.info("saveEvidence" + map.toString());
		ReturnData rd = new ReturnData();
		Gson g = new Gson();

		EvidenceEntity e = new EvidenceEntity();
		PlatformEntity p = platformDao.findPlatformByAppId(map.get("appid"));
		if (null == p) 
		{
			throw new ServiceException(
					ConstantUtil.RETURN_PLAT_NOT_EXIST[0],
					ConstantUtil.RETURN_PLAT_NOT_EXIST[1],
					ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
		}
		IdentityEntity identity = identityDao
				.queryAppIdAndPlatformUserName(p, map.get("creatorname"));
		if (null == identity) 
		{
			log.info("creator not exist");
			throw new ServiceException(
					ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1],
					ConstantUtil.RETURN_USER_NOTEXIST[2]);

		}
		// 如果该订单存在,返回异常
		if (null != eDao.findEvidenceByAppIdAndOrderId(map.get("appid"),
				map.get("orderid"))) 
		{
			log.info("appid:" + map.get("appid") + "orderid:"
					+ map.get("orderid") + "evidence is already exist");
			throw new ServiceException(
					ConstantUtil.DEPOSITORY_IS_ALREADY_EXIST[0],
					ConstantUtil.DEPOSITORY_IS_ALREADY_EXIST[1],
					ConstantUtil.DEPOSITORY_IS_ALREADY_EXIST[2]);
		}
		if (identity.getType() == (byte) 1) {
			// 个人
			if (null != identity.getCCustomInfo()) {
				e.setUsername(identity.getCCustomInfo().getUserName());
			}
		} else if (identity.getType() == (byte) 2) {
			// 企业
			e.setUsername(identity.getCCompanyInfo().getCompanyName());
		}
		if (null == e.getUsername()) {
			e.setUsername("");
		}
		e.setAppid(map.get("appid"));
		e.setOrder(map.get("orderid"));
		e.setUploadtime(new Date());
		e.setUpdatetime(new Date());
		e.setDownloadtimes(0);
		e.setUserid(identity.getId());
		String czSerial = "CZ" + FileUtil.getOrderIdByUUId();
		e.setSerial(czSerial);
		String czFilePath = FileUtil.createEvidenceFolder(czSerial);
		//动态添加数据到模板文件,另存为html文件
		File file = new File(czFilePath);
		if (!file.exists())
		{
			file.mkdirs();
		}
		
		if (null != map.get("title")) {
			e.setTitle(map.get("title"));
		} else {
			e.setTitle("");
		}
		e.setType(map.get("type"));
		this.saveEvidenceInfo(e);
		int evidenceId = e.getId();

		// 处理存证和人员关联
		if (null != map.get("userstr") && !"".equals(map.get("userstr"))) 
		{
			String[] useridStr = map.get("userstr").split(",");
			for (int i = 0; i < useridStr.length; i++) {
				IdentityEntity iden = identityDao
						.queryAppIdAndPlatformUserName(p, useridStr[i]);
				if (null == iden) {
					log.info("partor not exist");
					throw new ServiceException(
							ConstantUtil.RETURN_USER_NOTEXIST[0],
							ConstantUtil.RETURN_USER_NOTEXIST[1],
							ConstantUtil.RETURN_USER_NOTEXIST[2]);
				}
				EvidenceUserEntity u = new EvidenceUserEntity();
				u.setEvidence_id(evidenceId);
				u.setUserid(iden.getId());
				uDao.save(u);
			}
		}

		// 处理存证和文件关联--并且对文件签名
		if (null != map.get("filepathlist")&& !"".equals(map.get("filepathlist"))) 
		{
			log.info("filepathlist:" + map.get("filepathlist"));
			String[] filepathArray = map.get("filepathlist").split(",");
			for (int i = 0; i < filepathArray.length; i++) 
			{
				File srcFile = new File(filepathArray[i]);
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
				FileUtil.copyFolder(new File(filepathArray[i]),czFile);//拷贝
				String file_hash = SHA_MD.encodeFileSHA1(czFile).toHexString();
				// 签名值
				ResultData resData = cssRMIServices.signService(file_hash);
				// 时间戳
				String signJsonData = resData.pojo;
				Map mapData = null;
				mapData = g.fromJson(signJsonData, HashMap.class);
				String certificate = (String) mapData.get("certificate");// 证书信息
				String signature = (String) mapData.get("signature");// 签名信息
				String certFingerprint = (String) mapData.get("certFingerprint");// 证书指纹
				String certSerialNum = (String) mapData.get("serialNum");// 证书序列号
				ResultData timeStampRes = cssRMIServices.getTimestampService(e.getSerial(), certFingerprint);
				EvidenceBindAttachmentEntity eba = new EvidenceBindAttachmentEntity();
				eba.setAttachmentpath(czFileStr);
				eba.setDelflag("1");
				eba.setUploadtime(new Date());
				eba.setUpdatetime(new Date());
				eba.setEvidence_id(Integer.valueOf(evidenceId));
				eba.setDownloadtimes(0);
				eba.setFilehash(file_hash);
				eba.setCert(certificate);
				eba.setSignature(signature);
				eba.setCertserial(certSerialNum);
				eba.setSigntimestamp(timeStampRes.pojo.replaceAll("\r\n",""));
				// 处理图片
				List<String> fileList = new ArrayList<String>();
				
				String imgPath = czFilePath + System.currentTimeMillis();
				String pdfPath = czFileStr;
//					log.info("czFileStr:" + pdfpath);
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
//							File[] files = new File(imgPath).listFiles();
						pdfPath = tempFile;
					}
//						log.info("imgpath:" + imgPath);
//						log.info("pdfToImg:" + pdfpath + "_" + imgPath);
					Map<String, String> pdfTomImgMap = new HashMap<String, String>();
					pdfTomImgMap.put("optFrom", "NULL");
					pdfTomImgMap.put("appId", "NULL");
					pdfTomImgMap.put("ucid", "NULL");
					pdfTomImgMap.put("IP", "NULL");
					PDFTool.pdfToImg(pdfPath, imgPath, pdfTomImgMap);
					File[] files = new File(imgPath).listFiles();
					if (null != files && files.length > 0) {
						for (int j = 0; j < files.length; j++) {
							if (files[j].getName().toUpperCase()
									.endsWith(".PNG")
									|| files[j].getName().toUpperCase()
											.endsWith(".JPG")
									|| files[j].getName().toUpperCase()
											.endsWith(".JPEG")) {
								fileList.add(files[j].getPath());
							}
						}
					}
				}
				if (czFile.getName().toUpperCase().endsWith(".PNG")
						|| czFile.getName().toUpperCase().endsWith(".JPG")) {
					fileList.add(czFileStr);
				}
				eba.setToImgpath(g.toJson(fileList));
				ebaDao.save(eba);
			}
			rd = new ReturnData(ConstantUtil.RETURN_SUCC[0],
					ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],
					g.toJson(e));
		}		
		return rd;
	}
	/**
	 * 查看单个存证信息
	 * 
	 * @param map
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData evidenceDetail(Map<String, String> map)
			throws ServiceException {
		log.info("evidenceDetail" + map.toString());
		ReturnData rd = new ReturnData();
		Gson g = new Gson();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		EvidenceEntity e = eDao.findEvidenceByAppIdAndOrderId(map.get("appid"),
				map.get("orderid"));
		if (null == e) {
			throw new ServiceException(ConstantUtil.DEPOSITORY_NOT_EXIST[0],
					ConstantUtil.DEPOSITORY_NOT_EXIST[1],
					ConstantUtil.DEPOSITORY_NOT_EXIST[2]);
		}
		List<EvidenceBindAttachmentEntity> listEba = ebaDao.findEvidenceByEvidenceId(e.getId());
		String signData = "";
		if(null != listEba && !listEba.isEmpty())
		{
			List<Map<String,String>> signList = new ArrayList<Map<String,String>>();
 			Map<String,String> sign = new HashMap<String,String>();
			for (int i = 0; i < listEba.size(); i++) {
				EvidenceBindAttachmentEntity eba = listEba.get(i);
				if(null != eba)
				{
					sign.put("TimeStamp:", eba.getSigntimestamp());
					sign.put("sign:", eba.getSignature());
					sign.put("cert:", eba.getCert());
					sign.put("data:", eba.getFilehash());
					signList.add(sign);
				}
			}
			signData = g.toJson(signList);
		}
		
		// appid校验
		PlatformEntity p = platformDao.findPlatformByAppId(map.get("appid"));
		if (null == p) {
			throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],
					ConstantUtil.RETURN_PLAT_NOT_EXIST[1],
					ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
		}
		// userid校验
		IdentityEntity looker = identityDao.queryAppIdAndPlatformUserName(p,
				map.get("userId"));
		if (null == looker) {
			log.info("looker not exist");
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1],
					ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}

		// userid是否为存证人查看
		List<EvidenceUserEntity> parterlist = uDao
				.findEvidenceUserByEvidenceId(e.getId());
		List<Integer> parterIdlist = new ArrayList<Integer>();
		if (null != parterlist && parterlist.size() > 0) {
			for (int i = 0; i < parterlist.size(); i++) {
				parterIdlist.add(parterlist.get(i).getUserid());
			}
		}
		if (!parterIdlist.contains(looker.getId())) {
			throw new ServiceException(
					ConstantUtil.DEPOSITORY_USER_PERMISSION_DENIED[0],
					ConstantUtil.DEPOSITORY_USER_PERMISSION_DENIED[1],
					ConstantUtil.DEPOSITORY_USER_PERMISSION_DENIED[2]);
		}
		//存证提交方
		IdentityEntity creatorIdentity = identityDao.findById(e.getUserid());
		List<EvidenceBindAttachmentEntity> attlist = ebaDao.findEvidenceByEvidenceId(e.getId());
		CzMap m = new CzMap();
		// 提交者
		m.setSubmiter(e.getUsername());
		// 上传时间
		m.setUploadtime(sdf.format(e.getUploadtime()));
		// 存证流水
		m.setSerial(e.getSerial());
		//title
		m.setTitle(e.getTitle());
		// 文件列表处理---先强转pdf,再转图片
		List<String> filelist = new ArrayList<String>();
		Map<String,Object> fileListMap = new HashMap<String,Object>();
		if (null != attlist && attlist.size() > 0) {
			for (int i = 0; i < attlist.size(); i++) 
			{
				// 先查询数据库查找图片地址
				if (null != attlist.get(i).getToImgpath() && !"".equals(attlist.get(i).getToImgpath())) 
				{
					String filepathjson = attlist.get(i).getToImgpath();
					List<String> imglist = g.fromJson(filepathjson, List.class);
					filelist.addAll(imglist);
					fileListMap.put("attrList_"+i, imglist);
				}
			}
		}
		Collections.sort(filelist);
		m.setFileListMap(g.toJson(fileListMap));
		m.setFilelist(g.toJson(filelist));
		// 提交者信息
		Map creatorInfo = new HashMap();
		String creatorName = "";//存证提交方
		String identityCard = "";//身份证号
		String companyName = "";//企业名称
		String businessNo = "";//工商营业执照号
		String creatorType = "";//提交方类型
		String mobile = "";
		String email = "";
		// 1--个人
		/*
		if (creatorIdentity.getType() == (byte) 1) {
			CustomInfoEntity c = creatorIdentity.getCCustomInfo();
			creatorInfo.put("name", c.getUserName());
			creatorInfo.put("card", c.getIdentityCard());
			creatorInfo.put("info", c.getPhoneNum());
			creatorInfo.put("type", "1");
			// 2--企业
		} else if (creatorIdentity.getType() == (byte) 2) {
			CompanyInfoEntity c = creatorIdentity.getCCompanyInfo();
			creatorInfo.put("name", c.getCompanyName());
			creatorInfo.put("card", c.getBusinessLicenseNo());
			creatorInfo.put("info", creatorIdentity.getEmail());
			creatorInfo.put("type", "2");
		}
		*/
		if(null != creatorIdentity)
		{
			CustomInfoEntity customInfo = creatorIdentity.getCCustomInfo();
			CompanyInfoEntity companyInfo = creatorIdentity.getCCompanyInfo();
			if(null != customInfo)
			{
				creatorName = customInfo.getUserName();
				identityCard = customInfo.getIdentityCard();
			}
			if(null != companyInfo)
			{
				businessNo = companyInfo.getBusinessLicenseNo();
				companyName = companyInfo.getCompanyName();
				
			}
			mobile = creatorIdentity.getMobile();
			email = creatorIdentity.getEmail();
			creatorType = String.valueOf(creatorIdentity.getType());
		}
		creatorInfo.put("creatorName", creatorName);
		creatorInfo.put("identityCard", identityCard);
		creatorInfo.put("mobile", mobile);
		creatorInfo.put("email", email);
		creatorInfo.put("type", creatorType);
		creatorInfo.put("businessNo", businessNo);
		creatorInfo.put("companyName", companyName);
		m.setSubmiterInfo(g.toJson(creatorInfo));
		
		m.setSignData(signData);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// 缔约方
		List<EvidenceUserEntity> u = evidenceUserDao
				.findEvidenceUserByEvidenceId(e.getId());
		if (null != u && u.size() > 0) {
			for (int i = 0; i < u.size(); i++) {
				EvidenceUserEntity user = u.get(i);
				if (null != user) 
				{
					log.info("user" + user);					
					Map<String, String> usermap = new HashMap<String, String>();
					IdentityEntity iden = identityDao.findById(user.getUserid());
					if (null != iden) 
					{						
						
						if (iden.getType() == (byte) 1) {
							CustomInfoEntity c = iden.getCCustomInfo();
							if (null != c) {
								usermap.put("name", c.getUserName());
								usermap.put("card", c.getIdentityCard());
								usermap.put("info", iden.getMobile());
								usermap.put("type", "1");
							} else {
								usermap.put("name", "");
								usermap.put("card", "");
								usermap.put("info", iden.getMobile());
								usermap.put("type", "1");
							}
							// 2--企业
						} else if (iden.getType() == (byte) 2) {
							CompanyInfoEntity c = iden.getCCompanyInfo();
							if (null != c) {
								usermap.put("name", c.getCompanyName());
								usermap.put("card", c.getBusinessLicenseNo());
								usermap.put("info", iden.getEmail());
								usermap.put("type", "2");
							} else {
								usermap.put("name", "");
								usermap.put("card", "");
								usermap.put("info", iden.getMobile());
								usermap.put("type", "2");
							}
						}						
					}
					list.add(usermap);
					
				}
			}
		}
		m.setParterInfo(g.toJson(list));
		log.info("result:" + g.toJson(m));
		rd = new ReturnData(ConstantUtil.RETURN_SUCC[0],
				ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],
				g.toJson(m));
		return rd;
	}

	/**
	 * 云签详细信息
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData evidenceDetailForYunSign(Map<String, String> map)
			throws ServiceException {
		log.info("evidenceDetailForYunSign" + map.toString());
		ReturnData rd = new ReturnData();
		Gson g = new Gson();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		EvidenceEntity e = eDao
				.findEvidenceById(Integer.valueOf(map.get("id")));
		if (null == e) {
			throw new ServiceException(ConstantUtil.DEPOSITORY_NOT_EXIST[0],
					ConstantUtil.DEPOSITORY_NOT_EXIST[1],
					ConstantUtil.DEPOSITORY_NOT_EXIST[2]);
		}
		List<EvidenceBindAttachmentEntity> listEba = ebaDao.findEvidenceByEvidenceId(e.getId());
		String signData = "";
		if(null != listEba && !listEba.isEmpty())
		{
			List<Map<String,String>> signList = new ArrayList<Map<String,String>>();
 			Map<String,String> sign = new HashMap<String,String>();
			for (int i = 0; i < listEba.size(); i++) {
				EvidenceBindAttachmentEntity eba = listEba.get(i);
				if(null != eba)
				{
					sign.put("TimeStamp:", eba.getSigntimestamp());
					sign.put("sign:", eba.getSignature());
					sign.put("cert:", eba.getCert());
					sign.put("data:", eba.getFilehash());
					signList.add(sign);
				}
			}
			signData = g.toJson(signList);
		}
		// userid是否为存证人查看
		List<EvidenceUserEntity> parterlist = uDao
				.findEvidenceUserByEvidenceId(e.getId());
		List<Integer> parterIdlist = new ArrayList<Integer>();
		if (null != parterlist && parterlist.size() > 0) {
			for (int i = 0; i < parterlist.size(); i++) {
				parterIdlist.add(parterlist.get(i).getUserid());
			}
		}

		// 提交者
		IdentityEntity identity = identityDao.findById(e.getUserid());
		List<EvidenceBindAttachmentEntity> attlist = ebaDao
				.findEvidenceByEvidenceId(e.getId());
		CzMap m = new CzMap();
		// ID
		m.setId(String.valueOf(e.getId()));
		// 提交者
		m.setSubmiter(e.getUsername());
		// 存证标题
		m.setFilename(e.getTitle());
		// 上传时间
		m.setUploadtime(sdf.format(e.getUploadtime()));
		// 更新时间
		m.setUpdatetime(sdf.format(e.getUpdatetime()));
		// 存证流水
		m.setSerial(e.getSerial());
		// 下载次数
		m.setDownloadtimes(String.valueOf(e.getDownloadtimes()));
		// 文件列表处理---先强转pdf,再转图片
//		List<String> filelist = new ArrayList<String>();
//		if (null != attlist && attlist.size() > 0) {
//			for (int i = 0; i < attlist.size(); i++) {
//				// 先查询数据库查找图片地址
//				if (null != attlist.get(i).getToImgpath()
//						&& !"".equals(attlist.get(i).getToImgpath())) {
//					String filepathjson = attlist.get(i).getToImgpath();
//					List<String> imglist = g.fromJson(filepathjson, List.class);
//					filelist.addAll(imglist);
//				}
//			}
//		}
//		Collections.sort(filelist);
//		m.setFilelist(g.toJson(filelist));
		List<String> filelist = new ArrayList<String>();
		Map<String,Object> fileListMap = new HashMap<String,Object>();
		if (null != attlist && attlist.size() > 0) {
			for (int i = 0; i < attlist.size(); i++) 
			{
				// 先查询数据库查找图片地址
				if (null != attlist.get(i).getToImgpath() && !"".equals(attlist.get(i).getToImgpath())) 
				{
					String filepathjson = attlist.get(i).getToImgpath();
					List<String> imglist = g.fromJson(filepathjson, List.class);
					filelist.addAll(imglist);
					fileListMap.put("attrList_"+i, imglist);
				}
			}
		}
		Collections.sort(filelist);
		m.setFileListMap(g.toJson(fileListMap));
		m.setFilelist(g.toJson(filelist));
		// 提交者信息
		Map creatorInfo = new HashMap();
		// 1--个人
	
		if (identity.getType() == (byte) 1) {
			CustomInfoEntity c = identity.getCCustomInfo();
			creatorInfo.put("creatorName", c.getUserName());
			creatorInfo.put("card", c.getIdentityCard());
			creatorInfo.put("info", identity.getMobile());
			creatorInfo.put("type", "1");
			// 2--企业
		} else if (identity.getType() == (byte) 2) {
			CompanyInfoEntity c = identity.getCCompanyInfo();
			// 公司名称
			creatorInfo.put("creatorName", c.getCompanyName());
			// 营业执照号
			creatorInfo.put("businessNo", c.getBusinessLicenseNo());
			// 邮箱
			creatorInfo.put("email", identity.getEmail());
			if (null != identity.getCCustomInfo()) {
				// 法定代表人
				creatorInfo.put("legalman", identity.getCCustomInfo()
						.getUserName());
				// 身份证号码
				creatorInfo.put("card", identity.getCCustomInfo()
						.getIdentityCard());
				// 手机号码
				creatorInfo
						.put("info", identity.getCCustomInfo().getPhoneNum());

			} else {
				// 法定代表人
				creatorInfo.put("legalman", "");
				// 身份证号码
				creatorInfo.put("card", "");
				// 手机号码
				creatorInfo.put("info", "");
			}

			creatorInfo.put("type", "2");
		}
		m.setSubmiterInfo(g.toJson(creatorInfo));
		m.setSignData(signData);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// 缔约方
		List<EvidenceUserEntity> u = evidenceUserDao
				.findEvidenceUserByEvidenceId(e.getId());
		if (null != u && u.size() > 0) {
			for (int i = 0; i < u.size(); i++) {
				EvidenceUserEntity user = u.get(i);
				if (null != user) {
					log.info("user" + user);
					Map<String, String> usermap = new HashMap<String, String>();
					IdentityEntity iden = identityDao
							.findById(user.getUserid());
					if (null != iden) {
						if (iden.getType() == (byte) 1) {
							CustomInfoEntity c = iden.getCCustomInfo();
							if (null != c) {
								usermap.put("name", c.getUserName());
								usermap.put("card", c.getIdentityCard());
								usermap.put("info", iden.getMobile());
								usermap.put("type", "1");
							} else {
								usermap.put("name", "");
								usermap.put("card", "");
								usermap.put("info", iden.getMobile());
								usermap.put("type", "1");
							}
							// 2--企业
						} else if (iden.getType() == (byte) 2) {
							CompanyInfoEntity c = iden.getCCompanyInfo();
							usermap.put("name", c.getCompanyName());
							// 营业执照号
							usermap.put("businessNo", c.getBusinessLicenseNo());
							// 邮箱
							usermap.put("email", identity.getEmail());
							if (null != identity.getCCustomInfo()) {
								// 法定代表人
								usermap.put("legalman", identity
										.getCCustomInfo().getUserName());
								// 身份证号码
								usermap.put("card", identity.getCCustomInfo()
										.getIdentityCard());
								// 手机号码
								usermap.put("info", identity.getCCustomInfo()
										.getPhoneNum());

							} else {
								// 法定代表人
								usermap.put("legalman", "");
								// 身份证号码
								usermap.put("card", "");
								// 手机号码
								usermap.put("info", "");
							}
							usermap.put("type", "2");
						}
					}
					list.add(usermap);
				}
			}
		}
		m.setSignData(signData);
		m.setParterInfo(g.toJson(list));
		log.info("result:" + g.toJson(m));
		rd = new ReturnData(ConstantUtil.RETURN_SUCC[0],
				ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],
				g.toJson(m));
		return rd;
	}

	/**
	 * 查看存证信息/分页
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData pageEvidence(Map<String, String> map)
			throws ServiceException {
		log.info("pageEvidence:"+map.toString());
		Gson g = new Gson();
		ReturnData rd = new ReturnData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//处理云签的绑定账号问题
		IdentityEntity ys_identity=identityDao.findById(Integer.valueOf(map.get("userid")));
		if(null==ys_identity){
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],
					ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		log.info("ysid:"+ys_identity.getId());
		List<IdentityEntity> idenList=new ArrayList<IdentityEntity>();
		try{
			idenList=identityDao.queryByBindedId(ys_identity.getId());
		}catch(Exception e){
			e.printStackTrace();
		}
		log.info(idenList.toString());
		List<Integer> idlist=new ArrayList<Integer>();
		if(null!=idenList&&idenList.size()>0){
		for(int i=0;i<idenList.size();i++){
			idlist.add(idenList.get(i).getId());
		}
		}
		idlist.add(ys_identity.getId());
		
		//idlist的值
		log.info("idlist:"+g.toJson(idlist));
		
		//定义List列表,定义总的数据长度,定义页码长度
		List<EvidenceEntity> elist=new ArrayList<EvidenceEntity>();
		Long totalL=0L;
		Long pageL=0L;
		
		//数据查询
		Sort sort = new Sort(Direction.DESC, "updatetime");
		int currentPage = Integer.valueOf(map.get("currentPage"));
		int pageSize = Integer.valueOf(map.get("pageSize"));
		PageRequest page = new PageRequest(currentPage, pageSize, sort);
		for(int i=0;i<idlist.size();i++){
			IdentityEntityCriteria<EvidenceEntity> spec = new IdentityEntityCriteria<EvidenceEntity>();
			if (null != map.get("querykey") && !"".equals(map.get("querykey"))) {
				String[] paramStr = { "title", "serial" };
				Operator[] Operators = { Operator.LIKE, Operator.LIKE };
				// 标题或者编号查询
				spec.add(new QueryCriterion(paramStr, map.get("querykey"),
						Operators));
			}
			try {
				if (null != map.get("startime") && !"".equals(map.get("startime"))) {
					spec.add(new QueryCriterion("uploadtime", (Date) sdf.parse(map
							.get("startime")), Operator.GTE));
				}
				if (null != map.get("endtime") && !"".equals(map.get("endtime"))) {
					spec.add(new QueryCriterion("uploadtime", (Date) sdf.parse(map
							.get("endtime")), Operator.LTE));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
						ConstantUtil.RETURN_FAIL_PARAMERROR[1],
						ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
			}
			if (null != map.get("type") && !"".equals(map.get("type"))) {
				spec.add(new QueryCriterion("type", map.get("type"), Operator.EQ));
			}
			spec.add(new QueryCriterion("userid",idlist.get(i), Operator.EQ));
			Page<EvidenceEntity> pagelist = eDao.findAll(spec, page);
			elist.addAll(pagelist.getContent());
			totalL=totalL+pagelist.getTotalElements();
			pageL=pageL+pagelist.getTotalPages();
		}
		if (null != elist && elist.size() > 0) {
			for (int i = 0; i < elist.size(); i++) {
				EvidenceEntity e = elist.get(i);
				e.setUploadtimestr(sdf.format(e.getUploadtime()));
				IdentityEntity identity = identityDao.findById(e.getUserid());
				e.setPlatformUserName(identity.getPlatformUserName());
			}
		}
		log.info("query result is:"+g.toJson(elist)+"totalCount:"+String.valueOf(totalL)+"pageL:"+String.valueOf(pageL));
		PageResult res = new PageResult(g.toJson(elist),
				String.valueOf(totalL),
				String.valueOf(pageL));
		rd = new ReturnData(ConstantUtil.RETURN_SUCC[0],
				ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],
				g.toJson(res));
		return rd;
	}

	/**
	 * 云签查看验真报告
	 */
	public ReturnData queryEvidenceReport(Map<String, String> map)
			throws ServiceException {
		Gson g = new Gson();
		log.info("queryEvidenceReport:"+map.toString());
		ReturnData rd = new ReturnData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		EvidenceEntity e = eDao
				.findEvidenceById(Integer.valueOf(map.get("id")));
		// 存证不存在
		if (null == e) {
			throw new ServiceException(ConstantUtil.DEPOSITORY_NOT_EXIST[0],
					ConstantUtil.DEPOSITORY_NOT_EXIST[1],
					ConstantUtil.DEPOSITORY_NOT_EXIST[2]);
		}
		String zipfilename = e.getSerial() + ".zip";
		// userid是否为存证人查看
		List<EvidenceUserEntity> parterlist = uDao
				.findEvidenceUserByEvidenceId(e.getId());
		List<Integer> parterIdlist = new ArrayList<Integer>();
		List<EvidenceBindAttachmentEntity> list = ebaDao
				.findEvidenceByEvidenceId(e.getId());
		// 生成存证目录文件
		File parentpath = new File(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial());
		parentpath.deleteOnExit();
		FileUtil.createDir(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial());
		// 创建存证目录文件
		File imagepath = new File(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial() + File.separator + "images");
		if (!imagepath.exists()) {
			FileUtil.createDir(IConf.getValue("CZ_ZIP") + File.separator
					+ e.getSerial() + File.separator + "images");
		}
		// 创建根目录图片
		File pictruepath = new File(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial() + File.separator + "pictrues");
		if (!pictruepath.exists()) {
			FileUtil.createDir(IConf.getValue("CZ_ZIP") + File.separator
					+ e.getSerial() + File.separator + "pictrues");
		}
		// 图像文件copy
		FileUtil.copyFolder(
				new File(IConf.getImagePath()),
				new File(IConf.getValue("CZ_ZIP") + File.separator
						+ e.getSerial() + File.separator + "images"
						+ File.separator));
		// 存证存在时,打包下载
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				File f = new File(list.get(i).getAttachmentpath());
				log.info("filepath:" + list.get(i).getAttachmentpath());
				FileUtil.copyFile(
						list.get(i).getAttachmentpath(),
						IConf.getValue("CZ_ZIP") + File.separator
								+ e.getSerial() + File.separator + f.getName());
				// 文件转图片copy
				if (null != list.get(i).getToImgpath()
						&& !"".equals(list.get(i).getToImgpath())) {
					List<String> imglist = g.fromJson(list.get(i)
							.getToImgpath(), List.class);
					for (int m = 0; m < imglist.size(); m++) {
						{
							File imgfile = new File(imglist.get(m));
							log.info("imgfile:" + imglist.get(m));
							FileUtil.copyFile(
									imglist.get(m),
									IConf.getValue("CZ_ZIP") + File.separator
											+ e.getSerial() + File.separator
											+ "pictrues" + File.separator
											+ String.valueOf(i) + "_"
											+ imgfile.getName());
						}
					}
				}
			}
		}
		// 在该目录下制造index.html页面
		File input = new File(IConf.getIndexHtmlPath());
		Document doc = null;
		;
		try {
			doc = Jsoup.parse(input, "UTF-8", "");
		} catch (IOException s) {
			log.info(FileUtil.getStackTrace(s));
			throw new ServiceException(ConstantUtil.ZIP_DOWN_INDEX[0],
					ConstantUtil.ZIP_DOWN_INDEX[1],
					ConstantUtil.ZIP_DOWN_INDEX[2]);
		}
		String title = "";
		if (null != e.getTitle() && !"".equals(e.getTitle())) {
			title = e.getTitle();
		} else {
			if (null != list && list.size() > 0) {
				File f = new File(list.get(0).getAttachmentpath());
				title = f.getName();
			}
		}
		// 存证标题
		doc.getElementById("cz_title").append(title);
		// 存证编号
		doc.getElementById("top").append("编号: " + e.getSerial());
		doc.getElementById("cz_serial").append(e.getSerial());
		// 电子证据提交方
		if (null != e.getUsername()) {
			doc.getElementById("creator").append(e.getUsername());
		} else {
			doc.getElementById("creator").append("");
		}
		SimpleDateFormat cnsdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		// 提交存管时间
		doc.getElementById("time_cn").append(cnsdf.format(e.getUploadtime()));
		doc.getElementById("time_normal").append(sdf.format(e.getUploadtime()));
		// 提交方提交的身份信息
		List<EvidenceUserEntity> userlist = evidenceUserDao
				.findEvidenceUserByEvidenceId(e.getId());
		if (null != userlist && userlist.size() > 0) {
			for (int i = 0; i < userlist.size(); i++) {
				EvidenceUserEntity u = userlist.get(i);
				IdentityEntity iden = identityDao.findById(u.getUserid());
				if (null != iden) {
					// 个人
					if (iden.getType() == (byte) 1) {
						CustomInfoEntity c = iden.getCCustomInfo();
						if (null != c) {
							doc.getElementById("parterInfo").append(
									"<tr><td>姓名：" + c.getUserName()
											+ "</br>手机: " + iden.getMobile()
											+ "</br>身份证号码: "
											+ c.getIdentityCard()
											+ "</td></tr>");
						}
						// 企业
					} else if (iden.getType() == (byte) 2) {
						CompanyInfoEntity c = iden.getCCompanyInfo();
						doc.getElementById("parterInfo").append(
								"<tr><td>企业名称："
										+ c.getCompanyName()
										+ "</br>工商营业执照号: "
										+ c.getBusinessLicenseNo()
										+ "</br>"
										+ "法定代表人: "
										+ iden.getCCustomInfo().getUserName()
										+ "</br>身份证号: "
										+ iden.getCCustomInfo()
												.getIdentityCard() + "</br>"
										+ "手机号: " + iden.getMobile()
										+ "</td></tr>");
					}
				}
			}
		}
		// 存管时间处理
		doc.getElementById("savetime").append(sdf.format(e.getUploadtime()));
		// 提交时间
		doc.getElementById("submit_time").append(
				cnsdf.format(e.getUploadtime()));
		doc.getElementById("sign_time").append(sdf.format(e.getUploadtime()));
		// 提交方处理
		IdentityEntity iden = identityDao.findById(e.getUserid());
		if (null != iden) {
			// 个人
			if (iden.getType() == (byte) 1) {
				CustomInfoEntity c = iden.getCCustomInfo();
				if (null != c) {
					doc.getElementById("submitter_info").append("姓名：");
					doc.getElementById("submitter_info")
							.append(c.getUserName());
					doc.getElementById("submitter_info").append("<br>手机号: ");
					doc.getElementById("submitter_info").append(
							iden.getMobile());
					doc.getElementById("submitter_info").append("<br>身份证号码: ");
					doc.getElementById("submitter_info").append(
							c.getIdentityCard());
					doc.getElementById("submitter_info").append(
							"<i class=\"icon-personal\"></i>");
				}
				// 企业
			} else if (iden.getType() == (byte) 2) {
				CompanyInfoEntity c = iden.getCCompanyInfo();
				doc.getElementById("submitter_info").append("企业名称: ");
				doc.getElementById("submitter_info").append(c.getCompanyName());
				doc.getElementById("submitter_info").append("<br>工商营业执照号: ");
				doc.getElementById("submitter_info").append(
						c.getBusinessLicenseNo());
				doc.getElementById("submitter_info").append("<br>法定代表人: ");
				if (null != iden.getCCustomInfo()) {
					doc.getElementById("submitter_info").append(
							iden.getCCustomInfo().getUserName());
				}
				doc.getElementById("submitter_info").append("<br>身份证号: ");
				doc.getElementById("submitter_info").append(
						iden.getCCustomInfo().getIdentityCard());
				doc.getElementById("submitter_info").append("<br>手机号: ");
				doc.getElementById("submitter_info").append(iden.getMobile());
				doc.getElementById("submitter_info").append(
						"<i class=\"icon-company\"></i>");
			}
		}

		// v_p1 客户组签名
		String signature = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				signature = signature + "Signature:"
						+ list.get(i).getSignature() + "<br/>";
			}
		}
		doc.getElementById("v_p1").val(signature);

		// v_p2 证书
		String cert = "";
		if (null != list && list.size() > 0) {
			cert = "Cert:" + list.get(0).getCert() + "<br/>";
		}
		doc.getElementById("v_p2").val(cert);

		// v_p3 服务组签名
		String timestamp = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				timestamp = timestamp + "Timestamp:"
						+ list.get(i).getSigntimestamp() + "<br/>";
			}
		}
		doc.getElementById("v_p3").val(timestamp);

		// v_p4 签名原文
		String name = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				name = name + "Name:" + list.get(i).getAttachmentpath()
						+ "<br/>" + "SHA1-Digest:" + list.get(i).getFilehash()
						+ "<br/>";
			}
		}
		doc.getElementById("v_p4").val(name);

		// 处理追加图片
		File imgfiles = new File(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial() + File.separator + "pictrues" + File.separator);
		File[] imglistfiles = imgfiles.listFiles();
		List<String> flstr = new ArrayList<String>();
		// 排序
		// 背景图片和log图片地址
		// String
		// backgroudsrc=IConf.getValue("CZ_ZIP")+File.separator+e.getSerial()+File.separator+"images"+File.separator+"watermark.png";
		String logosrc = IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial() + File.separator + "images" + File.separator
				+ "yz.png";
		if (!new File(logosrc).exists()) {
			log.info("logsrc is not exists");
		}
		if (null != imglistfiles && imglistfiles.length > 0) {
			for (int i = 0; i < imglistfiles.length; i++) {
				// 合成背景
				// 合成水印
				try {
					// ComposePicture.composePic(imglistfiles[i].getPath(),
					// backgroudsrc,imglistfiles[i].getPath(), 30, 10);
					String filename = imglistfiles[i].getName();
					log.info("filename" + filename);
					String simplename = filename.substring(0,
							filename.indexOf("."))
							+ ".jpg";
					log.info("simplename" + simplename);
					String x = imglistfiles[i].getParent() + File.separator
							+ simplename;
					ComposePicture.composePic(imglistfiles[i].getPath(),
							logosrc, x, 840, 600);
					flstr.add(simplename);
					imglistfiles[i].deleteOnExit();
				} catch (Exception se) {
					se.printStackTrace();
					log.info("failed to transfor to img");
					throw new ServiceException(
							ConstantUtil.RETURN_SYSTEM_ERROR[0],
							ConstantUtil.RETURN_SYSTEM_ERROR[1],
							ConstantUtil.RETURN_SYSTEM_ERROR[2]);
				}
			}
		}
		Collections.sort(flstr);
		if (null != flstr && flstr.size() > 0) {
			for (int i = 0; i < flstr.size(); i++)
				doc.getElementById("mainContract").append(
						"<img class=\"contractimg\" src=\"pictrues/"
								+ flstr.get(i) + "\">");
		}
		String content = doc.toString();
		File indexhtml = new File(IConf.getValue("CZ_ZIP") + File.separator
				+ e.getSerial() + File.separator + "index.html");
		if (!indexhtml.exists()) {
			try {
				indexhtml.createNewFile();
			} catch (IOException io) {
				io.printStackTrace();
				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
						ConstantUtil.RETURN_SYSTEM_ERROR[1],
						ConstantUtil.RETURN_SYSTEM_ERROR[2]);
			}
		}
		FileUtil.writeTxtFile(content, indexhtml);
		rd = new ReturnData(ConstantUtil.RETURN_SUCC[0],
				ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2],
				indexhtml.getPath());
		log.info("htmlpath:" + indexhtml.getPath());
		return rd;
	}

	/**
	 * 定义一个查询条件容器
	 * 
	 * @author lee
	 * 
	 * @param <T>
	 */
	class IdentityEntityCriteria<T> implements Specification<T>{  
	    private List<Criterion> criterions = new ArrayList<Criterion>();  
	    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,  
	            CriteriaBuilder builder) {  
	        if (!criterions.isEmpty()) {  
	            List<Predicate> predicates = new ArrayList<Predicate>();  
	            for(Criterion c : criterions){  
	                predicates.add(((QueryCriterion) c).toPredicate(root, query,builder));  
	            }  
	            // 将所有条件用 and 联合起来  
	            if (predicates.size() > 0) {  
	            	return builder.and(predicates.toArray(new Predicate[predicates.size()]));  	 
	            }  
	        }  
	        return builder.conjunction();  
	    }  
	    /** 
	     * 增加简单条件表达式 
	     * @Methods Name add 
	     * @Create In 2012-2-8 By lee 
	     * @param expression0 void 
	     */  
	    public void add(QueryCriterion criterion){  
	        if(criterion!=null){  
	            criterions.add(criterion);  
	        }  
	    }  
	    
	    /** 
	     * 增加简单条件表达式 
	     * @Methods Name add 
	     * @Create In 2012-2-8 By lee 
	     * @param expression0 void 
	     */  
	}  
	public static String suffix(File f)
	{
	  String fileName=f.getName();
	  String suffix=fileName.substring(0,fileName.lastIndexOf(".")+1);
	  return suffix;
	     
	}
	/**
	 * 
	 * @param args
	 * @throws ServiceException
	 */
	public static void main(String[] args) throws ServiceException {
		String filesrc = "C:\\0.png";
		String logosrc = "C:/test.png";
		String backgroudsrc = "C:\\Users\\Administrator\\Desktop\\chuli\\test\\watermark.png";
		String outsrc = "C:\\0.png";
		// int x=840;
		// int y=600;
		 ComposePicture.composePic(filesrc, logosrc, outsrc, 30, 10);
		// ComposePicture.composePic(outsrc, logosrc, outsrc2, 840, 600);
		String src = "abc.png";
//		System.out.println(new File("c:\2222.png"));
//		System.out.println(new File(filesrc).getName());
		
		String json = "[\"/sharefile/cz/file/1481524366059/0.png\",\"/sharefile/cz/file/1481524366059/1.png\",\"/sharefile/cz/file/1481524366059/2.png\"]";
		List list = new Gson().fromJson(json, List.class);
		System.out.println("******"+list.size());
	}
}
