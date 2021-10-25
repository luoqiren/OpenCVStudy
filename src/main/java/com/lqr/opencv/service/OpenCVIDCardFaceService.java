package com.lqr.opencv.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月11日 下午5:20:24
* 类说明
*/
public interface OpenCVIDCardFaceService {
	File cutIDCardFaceImg(MultipartFile file) throws Exception;
}
