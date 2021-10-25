package com.lqr.opencv.util;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * @author 作者 :lqr
 * @version 创建时间：2021年10月21日 下午3:04:26 类说明
 */
public class FaceCompareMain {
	static String xml = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\haarcascade_frontalface_alt.xml";
//	static String fp = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\upload_rename\\12\\8BB07C6939A84CDDB181B3C1C582D59B_1.jpg";
//	static String sp = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\upload_rename\\12\\4.jpg";
//	static String fp = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\erzi\\1.jpg";
//	static String sp = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\erzi\\2.jpg";
//	static String fp = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\1.jpg";
//	static String sp = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\3.jpg";
//	static String fp = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo_2\\1.jpg";
//	static String sp = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo_2\\12.jpg";
	static String fp = "C:\\Users\\lqr\\Desktop\\Z04022300140000202114954806[20211025093248]\\1.jpg";
	static String sp = "C:\\Users\\lqr\\Desktop\\Z04022300140000202114954806[20211025093248]\\1.jpg";
	// 初始化人脸探测器
	static CascadeClassifier faceDetector;

	static {
		System.loadLibrary("opencv_java453");
		faceDetector = new CascadeClassifier(xml);
	}

	// 1. 灰度化（减小图片大小）
	// 2. 人脸识别
	// 3. 人脸切割
	// 4. 规一化(人脸直方图)
	// 5. 直方图相似度匹配
	public static void main(String[] args) {
		System.out.println("start:"+LocalDateTime.now());
		double res = compare_image(fp, sp);
		System.out.println(res);
		System.out.println("end:"+LocalDateTime.now());
	}
	
	public static double compare_image(String img1AbsPath, String img2AbsPath) {
        Mat mat_1 = conv_Mat(img1AbsPath);
        Mat mat_2 = conv_Mat(img2AbsPath);
        Mat hist_1 = new Mat();
        Mat hist_2 = new Mat();
        //颜色范围  // 4. 规一化(人脸直方图) start
        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        //直方图大小， 越大匹配越精确 (越慢)
        MatOfInt histSize = new MatOfInt(100000);
        Imgproc.calcHist(Arrays.asList(mat_1), new MatOfInt(0), new Mat(), hist_1, histSize, ranges, true);
        Imgproc.calcHist(Arrays.asList(mat_2), new MatOfInt(0), new Mat(), hist_2, histSize, ranges, true);
        // 4. 规一化(人脸直方图) end
        
        // CORREL 相关系数 // 5. 直方图相似度匹配
        double res = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);
        return res;
    }

	private static Mat conv_Mat(String img_1) {
		Mat image0 = Imgcodecs.imread(img_1);
//		Mat image = new Mat();
		Mat image =  Imgcodecs.imread(img_1, Imgproc.COLOR_BGR2GRAY);
		// 灰度转换 // 1. 灰度化（减小图片大小）
		//Imgproc.cvtColor(image0, image, Imgproc.COLOR_BGR2GRAY);
		MatOfRect faceDetections = new MatOfRect();
		// 探测人脸 // 2. 人脸识别
		faceDetector.detectMultiScale(image, faceDetections);
		// rect中是人脸图片的范围 //3. 人脸切割 默认只切割第一个识别到的人脸
		for (Rect rect : faceDetections.toArray()) {
			// 切割rect人脸
			Mat mat = new Mat(image, rect);
			return mat;
		}
		return null;
	}
}
