package com.lqr.opencv.tesseract;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import net.sourceforge.tess4j.TesseractException;

/**
 * @author 作者 :lqr
 * @version 创建时间：2021年10月27日 下午2:10:03 类说明
 */
public class TestTesseract {
	static String xml = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\haarcascade_frontalface_alt.xml";
	static CascadeClassifier faceDetector;

	static {
		System.loadLibrary("opencv_java453");
		faceDetector = new CascadeClassifier(xml);
	}

	public static void main(String[] args) throws TesseractException {
		// String file = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\22.jpg";
		// ITesseract iTesseract = new Tesseract();
		// iTesseract.setDatapath("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\");
		// iTesseract.setLanguage("eng+chi_sim");//中英文结合用 + 分隔
		// long l = System.currentTimeMillis();
		// System.out.println(iTesseract.doOCR(new File(file)));//识别结果
		// System.out.println("用时："+(System.currentTimeMillis()-l) + "ms");

		String path = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\14.png";
		Mat mat = Imgcodecs.imread(path);
		cardUp(mat);
	}

	/**
	 * 身份证反面识别
	 */
	public static void cardDown(Mat mat) {
		// 灰度
		mat = OpenCVUtil.gray(mat);
		// 二值化
		mat = OpenCVUtil.binary(mat);
		// 腐蚀
		mat = OpenCVUtil.erode(mat, 3);
		// 膨胀
		mat = OpenCVUtil.dilate(mat, 3);

		// 检测是否有居民身份证字体，如有为正向，若没有则旋转图片
		for (int i = 0; i < 4; i++) {
			String temp = temp(mat);
			if (!temp.contains("居") && !temp.contains("民")) {
				mat = OpenCVUtil.rotate3(mat, 90);
			} else {
				break;
			}
		}

		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\result.jpg", mat);
		String organization = organization(mat);
		System.out.print("签发机关是：" + organization);

		String time = time(mat);
		System.out.print("有效期限是：" + time);
	}

	public static String temp(Mat mat) {
		Point point1 = new Point(mat.cols() * 0.30, mat.rows() * 0.25);
		Point point2 = new Point(mat.cols() * 0.30, mat.rows() * 0.25);
		Point point3 = new Point(mat.cols() * 0.90, mat.rows() * 0.45);
		Point point4 = new Point(mat.cols() * 0.90, mat.rows() * 0.45);
		List<Point> list = new ArrayList<>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		Mat temp = OpenCVUtil.shear(mat, list);

		List<MatOfPoint> nameContours = OpenCVUtil.findContours(temp);
		for (int i = 0; i < nameContours.size(); i++) {
			double area = OpenCVUtil.area(nameContours.get(i));
			if (area < 100) {
				Imgproc.drawContours(temp, nameContours, i, new Scalar(0, 0, 0), -1);
			}
		}
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\temp.jpg", temp);
		BufferedImage nameBuffer = OpenCVUtil.Mat2BufImg(temp, ".jpg");
		String nameStr = OCRUtil.getImageMessage(nameBuffer, "chi_sim");
		nameStr = nameStr.replace("\n", "");
		return nameStr;
	}

	public static String organization(Mat mat) {
		Point point1 = new Point(mat.cols() * 0.36, mat.rows() * 0.68);
		Point point2 = new Point(mat.cols() * 0.36, mat.rows() * 0.68);
		Point point3 = new Point(mat.cols() * 0.80, mat.rows() * 0.80);
		Point point4 = new Point(mat.cols() * 0.80, mat.rows() * 0.80);
		List<Point> list = new ArrayList<>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		Mat name = OpenCVUtil.shear(mat, list);

		List<MatOfPoint> nameContours = OpenCVUtil.findContours(name);
		for (int i = 0; i < nameContours.size(); i++) {
			double area = OpenCVUtil.area(nameContours.get(i));
			if (area < 100) {
				Imgproc.drawContours(name, nameContours, i, new Scalar(0, 0, 0), -1);
			}
		}
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\organization.jpg", name);
		BufferedImage nameBuffer = OpenCVUtil.Mat2BufImg(name, ".jpg");
		String nameStr = OCRUtil.getImageMessage(nameBuffer, "chi_sim");
		nameStr = nameStr.replace("\n", "");
		return nameStr + "\n";
	}

	public static String time(Mat mat) {
		Point point1 = new Point(mat.cols() * 0.38, mat.rows() * 0.82);
		Point point2 = new Point(mat.cols() * 0.38, mat.rows() * 0.82);
		Point point3 = new Point(mat.cols() * 0.85, mat.rows() * 0.92);
		Point point4 = new Point(mat.cols() * 0.85, mat.rows() * 0.92);
		List<Point> list = new ArrayList<>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		Mat time = OpenCVUtil.shear(mat, list);

		List<MatOfPoint> timeContours = OpenCVUtil.findContours(time);
		for (int i = 0; i < timeContours.size(); i++) {
			double area = OpenCVUtil.area(timeContours.get(i));
			if (area < 100) {
				Imgproc.drawContours(time, timeContours, i, new Scalar(0, 0, 0), -1);
			}
		}
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\time.jpg", time);

		// 起始日期
		Point startPoint1 = new Point(0, 0);
		Point startPoint2 = new Point(0, time.rows());
		Point startPoint3 = new Point(time.cols() * 0.47, 0);
		Point startPoint4 = new Point(time.cols() * 0.47, time.rows());
		List<Point> startList = new ArrayList<>();
		startList.add(startPoint1);
		startList.add(startPoint2);
		startList.add(startPoint3);
		startList.add(startPoint4);
		Mat start = OpenCVUtil.shear(time, startList);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\start.jpg", start);
		BufferedImage yearBuffer = OpenCVUtil.Mat2BufImg(start, ".jpg");
		String startStr = OCRUtil.getImageMessage(yearBuffer, "eng");
		startStr = startStr.replace("-", "");
		startStr = startStr.replace(" ", "");
		startStr = startStr.replace("\n", "");

		// 截止日期
		Point endPoint1 = new Point(time.cols() * 0.47, 0);
		Point endPoint2 = new Point(time.cols() * 0.47, time.rows());
		Point endPoint3 = new Point(time.cols(), 0);
		Point endPoint4 = new Point(time.cols(), time.rows());
		List<Point> endList = new ArrayList<>();
		endList.add(endPoint1);
		endList.add(endPoint2);
		endList.add(endPoint3);
		endList.add(endPoint4);
		Mat end = OpenCVUtil.shear(time, endList);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\end.jpg", end);
		BufferedImage endBuffer = OpenCVUtil.Mat2BufImg(end, ".jpg");
		String endStr = OCRUtil.getImageMessage(endBuffer, "chi_sim");
		if (!endStr.contains("长") && !endStr.contains("期")) {
			endStr = OCRUtil.getImageMessage(endBuffer, "eng");
			endStr = endStr.replace("-", "");
			endStr = endStr.replace(" ", "");
		}

		return startStr + "-" + endStr;
	}

	/**
	 * 身份证正面识别
	 */
	public static void cardUp(Mat mat) {
		Mat begin = mat.clone();
//		// 截取身份证区域，并校订旋转角度
//		mat = OpenCVUtil.houghLinesP(begin, mat);
//		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\houghLinesP.jpg", mat);
//		// 循环进行人脸识别,校订图片方向
//		mat = OpenCVUtil.faceLoop(mat, faceDetector);
//		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\face.jpg", mat);
		// 灰度
		mat = OpenCVUtil.gray(mat);
		// 二值化
		mat = OpenCVUtil.binary(mat);
//		// 腐蚀
		mat = OpenCVUtil.erode(mat, 1);
//		// 膨胀
		mat = OpenCVUtil.dilate(mat, 1);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\bbb.jpg", mat);
//		
//		BufferedImage nameBuffer = OpenCVUtil.Mat2BufImg(mat, ".png");
//		String allstr = OCRUtil.getImageMessage(nameBuffer, "chi_sim");
//		System.out.println(allstr);
		
		// 获取名称
		String name = name(mat);
		System.out.print("姓名是：" + name.replaceAll(" ", ""));
		// 获取性别
		String sex = sex(mat);
		System.out.print("性别是：" + sex.replaceAll(" ", ""));

		// 获取民族
		String nation = nation(mat);
		System.out.print("民族是：" + nation.replaceAll(" ", ""));

		// 获取出生日期
		String birthday = birthday(mat);
		System.out.print("出生日期是：" + birthday.replaceAll(" ", ""));

		// 获取住址
		String address = address(mat);
		System.out.print("住址是：" + address.replaceAll(" ", "").replaceAll("\\n", ""));
		System.out.println();
		// 获取身份证
		String card = card(mat);
		System.out.print("身份证号是：" + card.replaceAll(" ", ""));
	}

	public static String name(Mat mat) {

		Point point1 = new Point(mat.cols() * 0.18, mat.rows() * 0.11);
		Point point2 = new Point(mat.cols() * 0.18, mat.rows() * 0.24);
		Point point3 = new Point(mat.cols() * 0.4, mat.rows() * 0.11);
		Point point4 = new Point(mat.cols() * 0.4, mat.rows() * 0.24);
		List<Point> list = new ArrayList<>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		Mat name = OpenCVUtil.shear(mat, list);
		name = OpenCVUtil.drawContours(name, 50);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\name.jpg", name);
		BufferedImage nameBuffer = OpenCVUtil.Mat2BufImg(name, ".jpg");
		String nameStr = OCRUtil.getImageMessage(nameBuffer, "chi_sim");
		nameStr = nameStr.replace("\n", "");
		return nameStr + "\n";
	}

	public static String sex(Mat mat) {
		Point point1 = new Point(mat.cols() * 0.18, mat.rows() * 0.25);
		Point point2 = new Point(mat.cols() * 0.18, mat.rows() * 0.35);
		Point point3 = new Point(mat.cols() * 0.25, mat.rows() * 0.25);
		Point point4 = new Point(mat.cols() * 0.25, mat.rows() * 0.35);
		List<Point> list = new ArrayList<>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		Mat sex = OpenCVUtil.shear(mat, list);
		sex = OpenCVUtil.drawContours(sex, 50);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\sex.jpg", sex);
		BufferedImage sexBuffer = OpenCVUtil.Mat2BufImg(sex, ".jpg");
		String sexStr = OCRUtil.getImageMessage(sexBuffer, "chi_sim");
		sexStr = sexStr.replace("\n", "");
		return sexStr + "\n";
	}

	public static String nation(Mat mat) {
		Point point1 = new Point(mat.cols() * 0.39, mat.rows() * 0.25);
		Point point2 = new Point(mat.cols() * 0.39, mat.rows() * 0.36);
		Point point3 = new Point(mat.cols() * 0.55, mat.rows() * 0.25);
		Point point4 = new Point(mat.cols() * 0.55, mat.rows() * 0.36);
		List<Point> list = new ArrayList<>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		Mat nation = OpenCVUtil.shear(mat, list);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\nation.jpg", nation);
		BufferedImage nationBuffer = OpenCVUtil.Mat2BufImg(nation, ".jpg");
		String nationStr = OCRUtil.getImageMessage(nationBuffer, "chi_sim");
		nationStr = nationStr.replace("\n", "");
		return nationStr + "\n";
	}

	public static String birthday(Mat mat) {
		Point point1 = new Point(mat.cols() * 0.18, mat.rows() * 0.35);
		Point point2 = new Point(mat.cols() * 0.18, mat.rows() * 0.35);
		Point point3 = new Point(mat.cols() * 0.55, mat.rows() * 0.48);
		Point point4 = new Point(mat.cols() * 0.55, mat.rows() * 0.48);
		List<Point> list = new ArrayList<>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		Mat birthday = OpenCVUtil.shear(mat, list);
		birthday = OpenCVUtil.drawContours(birthday, 50);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\birthday.jpg", birthday);
		// 年份
		Point yearPoint1 = new Point(0, 0);
		Point yearPoint2 = new Point(0, birthday.rows());
		Point yearPoint3 = new Point(birthday.cols() * 0.29, 0);
		Point yearPoint4 = new Point(birthday.cols() * 0.29, birthday.rows());
		List<Point> yearList = new ArrayList<>();
		yearList.add(yearPoint1);
		yearList.add(yearPoint2);
		yearList.add(yearPoint3);
		yearList.add(yearPoint4);
		Mat year = OpenCVUtil.shear(birthday, yearList);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\year.jpg", year);
		BufferedImage yearBuffer = OpenCVUtil.Mat2BufImg(year, ".jpg");
		String yearStr = OCRUtil.getImageMessage(yearBuffer, "eng");

		// 月份
		Point monthPoint1 = new Point(birthday.cols() * 0.44, 0);
		Point monthPoint2 = new Point(birthday.cols() * 0.44, birthday.rows());
		Point monthPoint3 = new Point(birthday.cols() * 0.55, 0);
		Point monthPoint4 = new Point(birthday.cols() * 0.55, birthday.rows());
		List<Point> monthList = new ArrayList<>();
		monthList.add(monthPoint1);
		monthList.add(monthPoint2);
		monthList.add(monthPoint3);
		monthList.add(monthPoint4);
		Mat month = OpenCVUtil.shear(birthday, monthList);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\month.jpg", month);
		BufferedImage monthBuffer = OpenCVUtil.Mat2BufImg(month, ".jpg");
		String monthStr = OCRUtil.getImageMessage(monthBuffer, "eng");

		// 日期
		Point dayPoint1 = new Point(birthday.cols() * 0.69, 0);
		Point dayPoint2 = new Point(birthday.cols() * 0.69, birthday.rows());
		Point dayPoint3 = new Point(birthday.cols() * 0.80, 0);
		Point dayPoint4 = new Point(birthday.cols() * 0.80, birthday.rows());
		List<Point> dayList = new ArrayList<>();
		dayList.add(dayPoint1);
		dayList.add(dayPoint2);
		dayList.add(dayPoint3);
		dayList.add(dayPoint4);
		Mat day = OpenCVUtil.shear(birthday, dayList);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\day.jpg", day);
		BufferedImage dayBuffer = OpenCVUtil.Mat2BufImg(day, ".jpg");
		String dayStr = OCRUtil.getImageMessage(dayBuffer, "eng");

		String birthdayStr = yearStr + "年" + monthStr + "月" + dayStr + "日";
		birthdayStr = birthdayStr.replace("\n", "");
		return birthdayStr + "\n";
	}

	public static String address(Mat mat) {
		Point point1 = new Point(mat.cols() * 0.17, mat.rows() * 0.47);
		Point point2 = new Point(mat.cols() * 0.17, mat.rows() * 0.47);
		Point point3 = new Point(mat.cols() * 0.61, mat.rows() * 0.76);
		Point point4 = new Point(mat.cols() * 0.61, mat.rows() * 0.76);
		List<Point> list = new ArrayList<>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		Mat address = OpenCVUtil.shear(mat, list);
		address = OpenCVUtil.drawContours(address, 50);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\address.jpg", address);
		BufferedImage addressBuffer = OpenCVUtil.Mat2BufImg(address, ".jpg");
		return OCRUtil.getImageMessage(addressBuffer, "chi_sim") + "\n";
	}

	public static String card(Mat mat) {
		Point point1 = new Point(mat.cols() * 0.34, mat.rows() * 0.75);
		Point point2 = new Point(mat.cols() * 0.34, mat.rows() * 0.75);
		Point point3 = new Point(mat.cols() * 0.89, mat.rows() * 0.91);
		Point point4 = new Point(mat.cols() * 0.89, mat.rows() * 0.91);
		List<Point> list = new ArrayList<>();
		list.add(point1);
		list.add(point2);
		list.add(point3);
		list.add(point4);
		Mat card = OpenCVUtil.shear(mat, list);
		card = OpenCVUtil.drawContours(card, 50);
		Imgcodecs.imwrite("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\card.jpg", card);
		BufferedImage cardBuffer = OpenCVUtil.Mat2BufImg(card, ".jpg");
		return OCRUtil.getImageMessage(cardBuffer, "eng") + "\n";
	}

}
