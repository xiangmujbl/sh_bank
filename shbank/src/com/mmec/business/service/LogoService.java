package com.mmec.business.service;

import java.util.List;

public interface LogoService {
	public void addLogo(String appId,String width,String height,String imageName,String imagePath);
	public String queryLogo(String appId);

}
