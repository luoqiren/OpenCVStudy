package com.lqr.opencv.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.lqr.opencv.service.OpenCVCompareService;
import com.lqr.opencv.systemConfig.model.OpenCVCompareResult;
import com.lqr.opencv.systemConfig.model.OpenCVInitModel;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月21日 下午2:16:33
* 类说明 图片高质量压缩
*/
public class OpenCVCompareServiceImpl implements OpenCVCompareService {
	private final static Log logger = LogFactory.getLog(OpenCVCompareServiceImpl.class);
	@Autowired
	private OpenCVInitModel openCVInitModel;
	
	/**
	 * 整体流程思路：
	 * 1. 压缩图片<br/>
	 * 2. 灰度化（进一步减小图片大小）<br/>
	 * 3. 人脸识别[就算是传进来的图片直接是人脸，也可以识别，只识别第一个检测到的人脸，从图片左边到右边]<br/>
	 * 4. 人脸切割<br/>
	 * 5. 规一化(人脸直方图->图形学的概念)<br/>
	 * 6. 直方图相似度匹配 <br/>
	 * @throws IOException 
	 * 
	 */
	@Override
	public OpenCVCompareResult compare(File firstImgFile, File secondImgFile) throws Exception {
		//TODO a. 压缩图片到500kb， 以方便后续比对，提升效率
		
		//b. 获取人脸图像Mat结构
		Mat firstMat = convMat(firstImgFile);
		Mat secondMat = convMat(secondImgFile);

		if(ObjectUtils.isEmpty(firstMat)) {
			throw new Exception("图片:"+firstImgFile.getName() +", 没有包含人脸, 无法进行人脸识别.");
		}
		if(ObjectUtils.isEmpty(secondMat)) {
			throw new Exception("图片:"+secondImgFile.getName() +", 没有包含人脸, 无法进行人脸识别.");
		}
		
		//c. 直方图Mat结构初始化
		Mat firstHist = new Mat();
	    Mat secondHist = new Mat();
		
	    //d. 剩余直方图处理
	    // d.1. 规一化(人脸直方图) start
	    MatOfFloat ranges = new MatOfFloat(0f, 256f);
	    //直方图大小， 越大匹配越精确 (越慢)
	    MatOfInt histSize = new MatOfInt(this.openCVInitModel.getHistSize());
	    Imgproc.calcHist(Arrays.asList(firstMat), new MatOfInt(0), new Mat(), firstHist, histSize, ranges, true);
	    Imgproc.calcHist(Arrays.asList(secondMat), new MatOfInt(0), new Mat(), secondHist, histSize, ranges, true);
	    // d.1. 规一化(人脸直方图) end
	    
	    // d.2. 直方图相似度匹配， 值越大时表示相似度越高 
	    double similarityRate = Imgproc.compareHist(firstHist, secondHist, Imgproc.CV_COMP_CORREL);
	    
	    // d.3 值比较
	    OpenCVCompareResult openCVCompareResult = new OpenCVCompareResult();
	    openCVCompareResult.setSimilarityRate(similarityRate);
	    openCVCompareResult.setSuggestFlag(similarityRate>0.85d);
	    logger.info(openCVCompareResult);
		return openCVCompareResult;
	}
	
	private Mat convMat(File imgFile) throws IOException {
		//2. 灰度化（减小图片大小）
		Mat image = Imgcodecs.imread(imgFile.getAbsolutePath(), Imgproc.COLOR_BGR2GRAY);
		File targetXmlFile = new File(imgFile.getParent()+ File.separator + this.openCVInitModel.getXml().getFilename() );
		if(!targetXmlFile.exists()) {
			targetXmlFile.createNewFile();
		}
		FileUtils.copyInputStreamToFile( this.openCVInitModel.getXml().getInputStream(), targetXmlFile);
		// 3. 人脸识别
		CascadeClassifier faceDetector = new CascadeClassifier(targetXmlFile.toString());
		if (faceDetector.empty()) {
			throw new IOException("无法加载专用xml配置文件.");
		}
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);
		// 4. 人脸切割
		for (Rect rect : faceDetections.toArray()) {
			// 切割rect人脸
			Mat mat = new Mat(image, rect);
			return mat;
		}
		return null;
	}
}
