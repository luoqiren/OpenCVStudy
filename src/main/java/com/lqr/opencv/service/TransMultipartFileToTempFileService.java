package com.lqr.opencv.service;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
* @author 作者 :lqr
* @version 创建时间：2021年10月12日 上午9:47:10
* 类说明
*/
public interface TransMultipartFileToTempFileService {
	File reNameToEnglishName(MultipartFile file) throws IOException;
}
