package com.lqr.opencv.controller;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lqr.opencv.service.OpenCVIDCardFaceService;
import com.lqr.opencv.util.OpenCVUtils;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月18日 下午4:10:58
* 类说明
*/
@RestController
public class OpenCVController {
	private final static Log logger = LogFactory.getLog(OpenCVController.class);
	
	@Autowired
	private OpenCVIDCardFaceService openCVIDCardFaceService;
	
	/**
	 * 进行人脸检测，并把人脸框起来
	 * @param response
	 * @param file
	 * @throws Exception
	 */
	@PostMapping("face")
	public void faceDetector(HttpServletResponse response, MultipartFile file) throws Exception {
		
		File resultFile = this.openCVIDCardFaceService.cutIDCardFaceImg(file);
		response.getOutputStream().write(OpenCVUtils.fileToByteArray(resultFile));
		response.getOutputStream().close();
		
		logger.info("人脸检测完成.");
	}
}
