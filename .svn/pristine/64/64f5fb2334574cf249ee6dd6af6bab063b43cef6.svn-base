package com.mmec.css.mmec.service;

import java.util.List;
import java.util.Map;

import com.mmec.css.certdn.form.CertForm;
import com.mmec.css.certdn.form.TSAFrom;
import com.mmec.css.mmec.form.ShowMessage;
import com.mmec.css.mmec.form.UserForm;

/**
 * 验证实现服务接口
 * @author liuy
 *
 */
public interface MMECVerifyService {
	
	/**
	 * 验证文件内容
	 * @param filePath  待验证的文件路径
	 * @return  ShowMessage内容
	 */
	public ShowMessage getContFileVF(String filePath);

	/**
	 * 解析证书内容
	 * @param certS  证书BASE64字符�?	 * @return       如果不存在返回null
	 */
	public CertForm getCertForm(String certStr);
	
	/**
	 * 解析时间信息
	 * @param tsaS  tsaS_BASE64信息
	 * @return  如果不存在返回null
	 */
	public TSAFrom getTSAFrom(String tsaStr);
	
	/**
	 * 解析MMEC中sg的用户信�?	 
	 * @param sgPath  sg文件�?��的路�?	 
	 * @return  如果不存在返回null
	 */
	public List<UserForm> getUserFormList(String sgPath);
	
	/**
	 * 取合同标题
	 * @param sgPath
	 * @return
	 */
	public Map<String,String> getTitel(String sgPath);
	
}
