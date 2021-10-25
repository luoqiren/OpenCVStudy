package com.lqr.opencv.tesseract;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月27日 下午4:06:44
* 类说明
*/
public class TestTesseract2 {

	public static void main(String[] args) throws TesseractException {
		String file = "C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\22.jpg";
		ITesseract iTesseract = new Tesseract();
		iTesseract.setDatapath("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\");
		iTesseract.setLanguage("eng+chi_sim");// 中英文结合用 + 分隔
		long l = System.currentTimeMillis();
		System.out.println(iTesseract.doOCR(new File(file)));// 识别结果
		System.out.println("用时：" + (System.currentTimeMillis() - l) + "ms");
	}

}
