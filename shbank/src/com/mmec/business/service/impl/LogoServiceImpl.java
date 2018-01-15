package com.mmec.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmec.business.bean.LogoBean;
import com.mmec.business.dao.LogoRepository;
import com.mmec.business.service.LogoService;

@Service("LogoService")
public class LogoServiceImpl implements LogoService {

    @Autowired
    LogoRepository logoRepository;
    
    @Override
    public void addLogo(String appId, String width, String height, String imageName,
    		String imagePath) {
    	LogoBean logoBean = new LogoBean();
    	logoBean.setAppId(appId);
    	logoBean.setHeight(height);
    	logoBean.setWidth(width);
    	logoBean.setName(imageName);
    	logoBean.setLogoPath(imagePath);
    	
    	logoRepository.save(logoBean);
    	
    }
    
    @Override
    public String queryLogo(String appId) {
    	List<LogoBean> list = logoRepository.queryLogoPath(appId);
    	String path = "";
    	if(list !=null && list.size()>0){
    		path = list.get(0).getLogoPath();
    	}
    	return path;
    }

}
