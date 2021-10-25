package com.lqr.opencv.systemConfig.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月18日 下午4:14:30
* 类说明
*/
public class OpenCVInitModel {
	
	@Value("classpath:haarcascade_frontalface_alt.xml")
	private Resource xml;
	private String xmlFilePath;
	private String uploadTempFilePath;
	private int histSize;
	
	
	public Resource getXml() {
		return xml;
	}

	public void setXml(Resource xml) {
		this.xml = xml;
	}

	public String getXmlFilePath() {
		return xmlFilePath;
	}

	public void setXmlFilePath(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}

	public String getUploadTempFilePath() {
		return uploadTempFilePath;
	}

	public void setUploadTempFilePath(String uploadTempFilePath) {
		this.uploadTempFilePath = uploadTempFilePath;
	}
	public int getHistSize() {
		return histSize;
	}

	public void setHistSize(int histSize) {
		this.histSize = histSize;
	}
	
}
