package com.lqr.opencv.service;

import java.io.File;

import com.lqr.opencv.systemConfig.model.OpenCVCompareResult;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月21日 下午1:38:30
* 类说明 opencv 图片对比
*/
public interface OpenCVCompareService {
	/**
	 * 两张图片进行比较
	 * @param firstImgFile
	 * @param secondImgFile
	 * @return OpenCVCompareModel
	 * @throws Exception 
	 */
	OpenCVCompareResult compare(File firstImgFile, File secondImgFile) throws Exception;
}
