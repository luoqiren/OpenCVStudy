package com.lqr.opencv.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import net.coobird.thumbnailator.Thumbnails;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月22日 下午2:44:21
* 类说明 图片尺寸不变，压缩文件大小 
*/
public class ImageCompress {

	public static void main(String[] args) throws IOException {
		File f = new File("C:\\Users\\lqr\\Desktop\\wyx_opencv\\1632280796147.jpg");
		System.out.println(Float.valueOf(f.length())/1024);
		System.out.println(f.length()/1024);
		 DecimalFormat df2 = new DecimalFormat("0.00");
		 
		if(f.length()/1024>500) {
			File newF = new File("C:\\Users\\lqr\\Desktop\\wyx_opencv\\"+System.currentTimeMillis()+".jpg");
			//图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
			String q =df2.format( 500/(Double.valueOf(f.length())/1024));
			System.out.println("q:"+q);
			Thumbnails.of(f).scale(1f).outputQuality(Float.parseFloat(q)).outputFormat("jpg").toFile(newF);
			System.out.println("压缩处理完成");
		}
		
		
	}

}
