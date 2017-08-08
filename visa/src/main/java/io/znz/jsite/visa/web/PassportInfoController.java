/**
 * PassportInfoController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.download.UploadService;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.visa.dto.NewCustomerJpDto;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
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

/**
 * 护照信息
 * @author   崔建斌
 * @Date	 2017年6月20日 	 
 */
@Controller
@RequestMapping("visa/passportinfo")
public class PassportInfoController extends BaseController {

	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;

	@Autowired
	private UploadService qiniuUploadService;//文件上传

	/**
	 * 回显美国护照信息数据
	 * @param request
	 */
	@RequestMapping(value = "listPassport")
	@ResponseBody
	public Object listPassport(HttpServletRequest request, String customerId) {
		if (Util.isEmpty(customerId)) {

			//根据当前登录用户id查询出个人信息
			EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute(Const.SESSION_NAME);
			long userId = 0;
			if (user == null) {
				throw new JSiteException("请登录后再试!");
			}
			if (!Util.isEmpty(user)) {
				userId = user.getId();
			}

			List<NewCustomerEntity> usalist = dbDao.query(NewCustomerEntity.class, Cnd
					.where("empid", "=", user.getId()).orderBy("createtime", "desc"), null);
			NewCustomerEntity customer = usalist.get(0);
			return customer;
		} else {
			NewCustomerEntity customer = dbDao.fetch(NewCustomerEntity.class, Long.valueOf(customerId));
			return customer;
		}
	}

	/**
	 * 美国护照信息修改保存
	 * 
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "updatePassportSave")
	@ResponseBody
	public Object updatePassportSave(@RequestBody NewCustomerEntity customer) {
		customer.setUpdatetime(new Date());
		dbDao.update(customer, null);
		return ResultObject.success("修改成功");
	}

	/**
	 * 日本护照信息回显
	 * @param request
	 */
	@RequestMapping(value = "listJPPassport")
	@ResponseBody
	public Object listJPPassport(HttpServletRequest request) {
		//根据当前登录用户id查询出个人信息
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute(Const.SESSION_NAME);
		long userId = 0;
		if (user == null) {
			throw new JSiteException("请登录后再试!");
		}
		if (!Util.isEmpty(user)) {
			userId = user.getId();
		}
		//NewCustomerJpEntity cusdto = dbDao.fetch(NewCustomerJpEntity.class, Cnd.where("empid", "=", userId));
		List<NewCustomerJpEntity> japanlist = dbDao.query(NewCustomerJpEntity.class,
				Cnd.where("empid", "=", user.getId()).orderBy("createtime", "desc"), null);
		NewCustomerJpEntity cusdto = japanlist.get(0);
		NewCustomerJpDto customer = new NewCustomerJpDto();
		if (!Util.isEmpty(cusdto)) {
			customer.setId(cusdto.getId());
			customer.setCountrynum(cusdto.getCountrynum());//国家码
			customer.setChinesefullname(cusdto.getChinesefullname());//姓名
			customer.setChinesexing(cusdto.getChinesexing());//中文姓
			customer.setChinesexingen(cusdto.getChinesexingen());//中文姓拼音
			customer.setChinesename(cusdto.getChinesename());//中文名
			customer.setChinesenameen(cusdto.getChinesenameen());//中文名拼音
			customer.setPassport(cusdto.getPassport());//护照号
			customer.setGender(cusdto.getGender());//性别
			customer.setDocountry(cusdto.getDocountry());//国籍
			customer.setBirthprovince(cusdto.getBirthprovince());//出生地点（省份）
			customer.setPassportsenddate(cusdto.getPassportsenddate());//签发日期
			customer.setPassportsendprovice(cusdto.getPassportsendplace());//签发地点（省份）
			customer.setPassporteffectdate(cusdto.getPassporteffectdate());//有效期至
			customer.setPassportsendoffice(cusdto.getPassportsendoffice());//签发机关
			customer.setPassportbooknum(cusdto.getPassportbooknum());//护照本号码
			customer.setPassportreadnum(cusdto.getPassportreadnum());//护照机读码
			customer.setPhoneurl(cusdto.getPhoneurl());//照片路径
		}
		return customer;
	}

	/**
	 * 日本护照信息编辑保存
	 * @param request
	 */
	@RequestMapping(value = "updateJPPassportSave")
	@ResponseBody
	public Object updateJPPassportSave(@RequestBody NewCustomerJpDto customer) {
		NewCustomerJpEntity cus = new NewCustomerJpEntity();
		if (!Util.isEmpty(customer)) {
			cus.setId(customer.getId());
			cus.setCountrynum(customer.getCountrynum());//国家码
			cus.setChinesefullname(customer.getChinesefullname());//姓名
			cus.setPassport(customer.getPassport());//护照号
			cus.setChinesexing(customer.getChinesexing());//中文姓
			cus.setChinesexingen(customer.getChinesexingen());//中文姓拼音
			cus.setChinesename(customer.getChinesename());//中文名
			cus.setChinesenameen(customer.getChinesenameen());//中文名拼音
			cus.setGender(customer.getGender());//性别
			cus.setDocountry(customer.getDocountry());//国籍
			cus.setBirthprovince(customer.getBirthprovince());//出生地点（省份）
			cus.setPassportsenddate(customer.getPassportsenddate());//签发日期
			cus.setPassportsendplace(customer.getPassportsendprovice());//签发地点（省份）
			cus.setPassporteffectdate(customer.getPassporteffectdate());//有效期至
			cus.setPassportsendoffice(customer.getPassportsendoffice());//签发机关
			cus.setPassportbooknum(customer.getPassportbooknum());//护照本号码
			cus.setPassportreadnum(customer.getPassportreadnum());//护照机读码
			cus.setPhoneurl(customer.getPhoneurl());//照片路径
			nutDao.updateIgnoreNull(cus);
		}
		return ResultObject.success("修改成功");
	}

	/**
	 * 文件上传
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "uploadFile")
	@ResponseBody
	public Object uploadFile(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding(io.znz.jsite.visa.util.Const.CHARACTER_ENCODING_PROJECT);//字符编码为utf-8
		String url = null;
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
					String originalFilename = file.getOriginalFilename();
					String ext = originalFilename.substring(originalFilename.indexOf(".") + 1,
							originalFilename.length());
					InputStream is = file.getInputStream();
					String shortUrl = qiniuUploadService.uploadImage(is, ext, null);
					url = io.znz.jsite.visa.util.Const.IMAGES_SERVER_ADDR + shortUrl;
				}
			}
		}
		return url;
	}
}
