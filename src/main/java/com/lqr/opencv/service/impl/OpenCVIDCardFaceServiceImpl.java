package com.lqr.opencv.service.impl;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lqr.opencv.service.OpenCVIDCardFaceService;
import com.lqr.opencv.service.TransMultipartFileToTempFileService;
import com.lqr.opencv.systemConfig.model.OpenCVInitModel;
import com.lqr.opencv.util.OpenCVUtils;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月11日 下午5:22:48
* 类说明
*/
@Service
@Transactional
public class OpenCVIDCardFaceServiceImpl implements OpenCVIDCardFaceService {
	private final static Log logger = LogFactory.getLog(OpenCVIDCardFaceServiceImpl.class);
	@Autowired
	private OpenCVInitModel openCVInitModel;
	@Autowired
	private TransMultipartFileToTempFileService transMultipartFileToTempFileService;
	
	@Override
	public File cutIDCardFaceImg(MultipartFile file) throws Exception {
		//1.转换名字到对应的文件夹里
		File targetFile = this.transMultipartFileToTempFileService.reNameToEnglishName(file);
		String targetFileName = targetFile.getName().substring(0, targetFile.getName().indexOf("."));
		logger.info("文件上传成功 targetFileName:"+targetFileName);
		//2.转储xml文件到指定目录
		logger.info(targetFile.getParent());
		File targetXmlFile = new File(targetFile.getParent()+ File.separator + this.openCVInitModel.getXml().getFilename() );
		if(!targetXmlFile.exists()) {
			targetXmlFile.createNewFile();
		}
		FileUtils.copyInputStreamToFile( this.openCVInitModel.getXml().getInputStream(), targetXmlFile);
		CascadeClassifier faceDetector = new CascadeClassifier(targetXmlFile.toString());
		if (faceDetector.empty()) {
			throw new Exception("无法加载专用xml配置文件.");
		}
		logger.info("专用xml复制成功.");
		//3.抓图像
		logger.info("人脸检测开始……");
		// 3.1 读取创建的图片 targetFile
		Mat image = Imgcodecs.imread(targetFile.getAbsolutePath());
		MatOfRect faceDetections = new MatOfRect();
		// 3.2进行人脸检测
		faceDetector.detectMultiScale(image, faceDetections);
		int faceCounts = faceDetections.toArray().length;
		logger.info(String.format("检测到人脸： %s", faceCounts));
		if(faceCounts == 0) {
			throw new Exception("图片 [ "+file.getOriginalFilename()+" ] 无法检测到人脸.");
		}
		// 3.3制图将图填充到image中
		int i = 1;
		for(Rect rect : faceDetections.toArray()) {
			// 3.3.1.1 对原图画矩形 
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0), 3);
			// 3.3.1.2 进行图片裁剪以及保存到指定路径
			String cutImgFile = targetFile.getParent()+File.separator+targetFileName+"_"+i+".jpg";
			logger.info("cutImgFile:"+cutImgFile);
			OpenCVUtils.imageCut(targetFile.getAbsolutePath(), cutImgFile, rect.x, rect.y, rect.width, rect.height);
			i++;
		}
		// 3.3.2 删除targetXmlFile资源配置文件
		OpenCVUtils.deleteFoler(targetXmlFile);
		//TODO 3.3.3 将所有图片进行打包成压缩包并上传影像?
		
		// 3.4 返回给页面
		String filename = targetFile.getParent()+File.separator+file.getOriginalFilename();
		Imgcodecs.imwrite(filename, image);
		File imgFile = new File(filename);
		return imgFile;
	}

}
