



package com.lqr.opencv.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lqr.opencv.service.TransMultipartFileToTempFileService;
import com.lqr.opencv.systemConfig.model.OpenCVInitModel;
import com.lqr.opencv.util.OpenCVUtils;

/**
 * @author 作者 :lqr
 * @version 创建时间：2021年10月12日 上午9:47:29 类说明
 */
@Service
@Transactional
public class TransMultipartFileToTempFileServiceImpl implements TransMultipartFileToTempFileService {

	@Autowired
	private OpenCVInitModel openCVInitModel;

	@Override
	public File reNameToEnglishName(MultipartFile file) throws IOException {
		// String uploadRenamePath = openCVInitModel.getUploadTempFilePath();
		String newFileName = OpenCVUtils.getUUIDWithoutSpace();
		String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
		String newFolderName = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf("."));
		String newFolderPath = this.openCVInitModel.getUploadTempFilePath() + newFolderName;
		File newFolderPathFile = new File(newFolderPath);

		if (!newFolderPathFile.exists()) {
			newFolderPathFile.mkdir();
		} else {
			OpenCVUtils.deleteFoler(newFolderPathFile);
			newFolderPathFile.mkdir();
		}
		File newFile = new File(newFolderPath + File.separator + newFileName + fileSuffix);
		FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
		//是否将转换流水记录？
		return newFile;
	}

}

