/**
 * PassportInfoController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.exception.JSiteException;
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
	 * 回显护照信息数据
	 * @param request
	 */
	@RequestMapping(value = "listPassport")
	@ResponseBody
	public Object listPassport(HttpServletRequest request) {
		//根据当前登录用户id查询出个人信息
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute("fetch");
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
	 * 护照信息修改保存
	 * 
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "updatePassportSave")
	@ResponseBody
	public Object updatePassportSave(@RequestBody NewCustomerEntity customer) {
		dbDao.update(customer, null);
		return ResultObject.success("修改成功");
	}
}
