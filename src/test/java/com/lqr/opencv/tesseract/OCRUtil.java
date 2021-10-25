package com.lqr.opencv.tesseract;

import java.awt.image.BufferedImage;
import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月27日 下午3:41:35
* 类说明
*/
public class OCRUtil {
    /**
     * 识别图片信息
     * @param img
     * @return
     */
    public static String getImageMessage(BufferedImage img,String language){
        String result="end";
        try{
//            ITesseract instance = new Tesseract();
//            File tessDataFolder = LoadLibs.extractTessResources("tessdata");
//            instance.setLanguage(language);
//            instance.setDatapath(tessDataFolder.getAbsolutePath());
        	ITesseract iTesseract = new Tesseract();
    		iTesseract.setDatapath("C:\\Users\\lqr\\Desktop\\wyx_opencv\\wo\\");
    		iTesseract.setLanguage("eng+chi_sim");// 中英文结合用 + 分隔
            result = iTesseract.doOCR(img);
            //System.out.println(result);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }
}
