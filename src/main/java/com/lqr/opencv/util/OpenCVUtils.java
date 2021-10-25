package com.lqr.opencv.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.UUID;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import net.coobird.thumbnailator.Thumbnails;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月11日 下午4:12:23
* 类说明
*/
public class OpenCVUtils {
	
	public static String getUUIDWithoutSpace() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}
	
	/**
	 * 删除文件夹或者文件
	 * @param file
	 */
	public static void deleteFoler(File file) {
		if(file.exists()) {
			if(file.isDirectory()) {
				File [] listFiles = file.listFiles();
				for(File f : listFiles) {
					deleteFoler(f);
				}
				file.delete();
			}else {
				file.delete();
			}
		}
	}
	
	/**
	 * 裁剪图片并保存到指定目录
	 * @param imagePath 源图片路径
	 * @param outFile  裁剪图片保存路径
	 * @param posX 源图片裁剪开始横坐标
	 * @param posY 源图片裁剪开始纵坐标
	 * @param width 源图片裁剪长度=》裁剪图片长度
	 * @param height 源图片裁剪高度=》裁剪图片高度
	 */
	public static void imageCut(String imagePath, String outFile, int posX, int posY, int width, int height) {
		// 原始图像
		Mat image = Imgcodecs.imread(imagePath);
		// 截取的区域：参数,坐标X,坐标Y,截图宽度,截图长度
		Rect rect = new Rect(posX, posY, width, height);
		// 两句效果一样
		Mat sub = image.submat(rect); // Mat sub = new Mat(image,rect);
		Mat mat = new Mat();
		Size size = new Size(width, height);
		Imgproc.resize(sub, mat, size);// 将人脸进行截图并保存
		Imgcodecs.imwrite(outFile, mat);
		System.out.println(String.format("图片裁切成功，裁切后图片文件为： %s", outFile));
	}
	
	/**
	 * 将文件转换成字节数组
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] fileToByteArray(File file) throws IOException {
		File f = file;
		if (!f.exists()) {
			throw new FileNotFoundException("file not exists");
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}
	
	/**
	 * 图片压缩工具，消耗很大内存，图片尺寸不变，压缩图片文件大小
	 * ref:https://www.cnblogs.com/miskis/p/5500822.html
	 * @param sourceImg
	 * @param targetImg
	 * @param targetSizeKB
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void imgCompress(File sourceImg, File targetImg, int targetSizeKB) throws NumberFormatException, IOException {
		if(sourceImg.length()/1024 > targetSizeKB) {
			DecimalFormat df2 = new DecimalFormat("0.00");
			//图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
			String q =df2.format( 500/ (Double.valueOf(sourceImg.length())/1024) );
			Thumbnails.of(sourceImg).scale(1f).outputQuality(Float.parseFloat(q)).outputFormat("jpg").toFile(targetImg);
		}
	}
	
	public static void main(String[] args) {
		File f = new File("C:\\Users\\lqr\\Desktop\\20211011 jar\\wyx-car-0.0.1-SNAPSHOT.war") ;
		System.out.println(f.getParent());
	}
}
