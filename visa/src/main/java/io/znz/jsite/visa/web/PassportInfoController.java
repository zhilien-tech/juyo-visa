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
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.visa.dto.NewCustomerJpDto;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

	/**
	 * 回显美国护照信息数据
	 * @param request
	 */
	@RequestMapping(value = "listPassport")
	@ResponseBody
	public Object listPassport(HttpServletRequest request) {
		//根据当前登录用户id查询出个人信息
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute(Const.SESSION_NAME);
		long userId = 0;
		if (user == null) {
			throw new JSiteException("请登录后再试!");
		}
		if (!Util.isEmpty(user)) {
			userId = user.getId();
		}
		NewCustomerEntity customer = dbDao.fetch(NewCustomerEntity.class, Cnd.where("empid", "=", userId));
		return customer;
	}

	/**
	 * 美国护照信息修改保存
	 * 
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "updatePassportSave")
	@ResponseBody
	public Object updatePassportSave(@RequestBody NewCustomerEntity customer) {
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
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute("fetch");
		long userId = 0;
		if (user == null) {
			throw new JSiteException("请登录后再试!");
		}
		if (!Util.isEmpty(user)) {
			userId = user.getId();
		}
		NewCustomerJpEntity cusdto = dbDao.fetch(NewCustomerJpEntity.class, Cnd.where("empid", "=", userId));
		NewCustomerJpDto customer = new NewCustomerJpDto();
		if (!Util.isEmpty(cusdto)) {
			customer.setId(cusdto.getId());
			customer.setCountrynum(cusdto.getCountrynum());//国家码
			customer.setChinesefullname(cusdto.getChinesefullname());//姓名
			customer.setPassport(cusdto.getPassport());//护照号
			customer.setChinesexingen(cusdto.getChinesexingen());//中文姓拼音
			customer.setGender(cusdto.getGender());//性别
			customer.setBirthcountry(cusdto.getDocountry());//国籍
			customer.setBirthprovince(cusdto.getBirthprovince());//出生地点（省份）
			customer.setPassportsenddate(cusdto.getPassportsenddate());//签发日期
			customer.setPassportsendprovice(cusdto.getPassportsendplace());//签发地点（省份）
			customer.setPassporteffectdate(cusdto.getPassporteffectdate());//有效期至
			customer.setVisaoffice(cusdto.getPassportsendoffice());//签发机关
			customer.setPassportbooknum(cusdto.getPassportbooknum());//护照本号码
			customer.setPassportreadnum(cusdto.getPassportreadnum());//护照机读码
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
			cus.setChinesexingen(customer.getChinesexingen());//中文姓拼音
			cus.setGender(customer.getGender());//性别
			cus.setDocountry(customer.getBirthcountry());//国籍
			cus.setBirthprovince(customer.getBirthprovince());//出生地点（省份）
			cus.setPassportsenddate(customer.getPassportsenddate());//签发日期
			cus.setPassportsendplace(customer.getPassportsendprovice());//签发地点（省份）
			cus.setPassporteffectdate(customer.getPassporteffectdate());//有效期至
			cus.setPassportsendoffice(customer.getVisaoffice());//签发机关
			cus.setPassportbooknum(customer.getPassportbooknum());//护照本号码
			cus.setPassportreadnum(customer.getPassportreadnum());//护照机读码
			nutDao.updateIgnoreNull(cus);
		}
		return ResultObject.success("修改成功");
	}
}
