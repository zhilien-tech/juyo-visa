package io.znz.jsite.download.impl;

import io.znz.jsite.download.BaseUploadService;

import java.io.IOException;
import java.io.InputStream;

import org.csource.common.MyException;
import org.csource.upload.UploadFileUtil;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(name = "fdfsUploadService")
public class FdfsUploadServiceImpl extends BaseUploadService {

	@Override
	public String uploadImage(InputStream inStream, String file_ext_name, String dest_filename) {
		try {
			return UploadFileUtil.uploadFile(inStream, file_ext_name, null);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (MyException e) {
			e.printStackTrace();
			return "";
		}
	}
}
