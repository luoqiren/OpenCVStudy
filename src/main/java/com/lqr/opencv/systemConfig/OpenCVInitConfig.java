package com.lqr.opencv.systemConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lqr.opencv.systemConfig.model.OpenCVInitModel;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月18日 下午3:57:22
* 类说明
*/
@Configuration
public class OpenCVInitConfig {
	
	private final static Log logger = LogFactory.getLog(OpenCVInitConfig.class);
	
	/**
	 * 项目启动的时候将opencv_java453版本加载到容器中
	 */
	@Bean
	public OpenCVInitModel initOpenCVLibLoad() {
		System.loadLibrary("opencv_java453");
		logger.info("加载opencv_java45 Library成功.");
		
		//TODO 此处可以改成数据库配置
		OpenCVInitModel openCVInitModel = new OpenCVInitModel();
		openCVInitModel.setXmlFilePath("C:\\Users\\lqr\\Desktop\\wyx_opencv\\");
		openCVInitModel.setUploadTempFilePath("C:\\Users\\lqr\\Desktop\\wyx_opencv\\upload_rename\\");
		openCVInitModel.setHistSize(10000);
		logger.info("openCVInitModel 初始化完成.");
		return openCVInitModel;
	}
}
