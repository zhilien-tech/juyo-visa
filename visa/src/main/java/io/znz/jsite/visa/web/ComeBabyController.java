/**
 * ComeBabyController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.entity.company.CompanyEntity;
import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.download.UploadService;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpEntity;
import io.znz.jsite.visa.forms.comebaby.ComeBabySqlForm;
import io.znz.jsite.visa.service.comebaby.ComeBabyService;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;
import com.uxuexi.core.web.base.page.Pagination;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年7月25日 	 
 */
@Controller
@RequestMapping("visa/comebaby")
public class ComeBabyController {
	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;

	@Autowired
	private ComeBabyService comeBabyService;
	@Autowired
	private UploadService qiniuUploadService;//文件上传

	/**
	 * 日本招宝信息页展示
	 * @param request
	 */
	@RequestMapping(value = "comeList")
	@ResponseBody
	public Object comeList(@RequestBody ComeBabySqlForm form, final HttpSession session) {
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		if (!Util.isEmpty(company)) {
			long comId = company.getComId();
			form.setComId(comId);
		} else {
			form.setComId(-1);
		}
		Pager pager = new Pager();
		pager.setPageNumber(form.getPageNumber());
		pager.setPageSize(form.getPageSize());
		if (!Util.isEmpty(company)) {

			CompanyEntity comp = dbDao.fetch(CompanyEntity.class, company.getComId());
			Integer comType = null;
			if (!Util.isEmpty(comp)) {
				comType = comp.getComType();
				form.setComtype(comType);
			} else {
				form.setComtype(1);

			}
		}

		Pagination listPage = comeBabyService.listPage(form, pager);
		return listPage;
	}

	/**
	 * 日本招宝保存
	 * @param request
	 */
	@RequestMapping(value = "comesave")
	@ResponseBody
	public Object comesave(@RequestBody NewComeBabyJpEntity comebaby, final HttpSession session) {
		Long id = comebaby.getId();
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		if (!Util.isEmpty(company)) {
			long comId = company.getComId();
			comebaby.setComId(comId);
		} else {

			comebaby.setComId(Long.valueOf("-1"));
		}

		comebaby.setUpdateTime(new Date());
		if (!Util.isEmpty(id) && id.intValue() > 0) {
			dbDao.update(comebaby, null);
		} else {
			comebaby.setCreateTime(new Date());
			dbDao.insert(comebaby);
		}
		return ResultObject.success("添加成功");
	}

	/**
	 * 日本招宝编辑
	 * @param request
	 */
	@RequestMapping(value = "comefetch")
	@ResponseBody
	public Object comefetch(long comeid, final HttpSession session) {

		NewComeBabyJpEntity fetch = dbDao.fetch(NewComeBabyJpEntity.class, comeid);
		if (Util.isEmpty(fetch)) {
			fetch = new NewComeBabyJpEntity();
		}
		return fetch;
	}

	/**
	 * 上传文件
	 */
	@RequestMapping(value = "uploadFile")
	@ResponseBody
	public Object uploadFile(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding(io.znz.jsite.visa.util.Const.CHARACTER_ENCODING_PROJECT);//字符编码为utf-8
		//		response.setCharacterEncoding(Const.CHARACTER_ENCODING_PROJECT);
		/*	FileInputStream is = new FileInputStream(Filedata);
			String ext = FileUtil.getSuffix(Filedata);
			String str = Filedata.getName();
			String shortUrl = qiniuUploadService.uploadImage(is, ext, null);
			String url = Const.IMAGES_SERVER_ADDR + shortUrl;*/
		String url = null;
		long startTime = System.currentTimeMillis();
		//将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
				.getServletContext());
		//检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			//将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			//获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				//一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					//					String contentType = file.getContentType();
					String originalFilename = file.getOriginalFilename();
					//					String name = file.getName();
					String ext = originalFilename.substring(originalFilename.indexOf(".") + 1,
							originalFilename.length());
					InputStream is = file.getInputStream();

					String shortUrl = qiniuUploadService.uploadImage(is, ext, null);
					url = io.znz.jsite.visa.util.Const.IMAGES_SERVER_ADDR + shortUrl;

					System.out.println(url);
				}

			}

		}
		/*	long endTime = System.currentTimeMillis();
			System.out.println("方法三的运行时间：" + String.valueOf(endTime - startTime) + "ms");*/
		return url;

	}

	/**
	 * 日本招宝删除
	 * @param request
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public Object delete(long comeid) {

		if (!Util.isEmpty(comeid) && comeid > 0) {
			dbDao.delete(NewComeBabyJpEntity.class, comeid);
		}
		return ResultObject.success("删除成功");
	}
}
