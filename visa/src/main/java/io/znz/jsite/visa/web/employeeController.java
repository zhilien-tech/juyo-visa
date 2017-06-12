/**
 * employeeController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.visa.entity.user.SysUserEntity;
import io.znz.jsite.visa.enums.UserStatusEnum;
import io.znz.jsite.visa.forms.employeeform.UserAddForm;
import io.znz.jsite.visa.forms.employeeform.UserSqlForm;
import io.znz.jsite.visa.service.EmployeeViewService;

import java.util.Date;

import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.mvc.annotation.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.db.dao.IDbDao;
import com.uxuexi.core.web.util.FormUtil;

/**
 * 员工管理控制类
 * @author   崔建斌
 * @Date	 2017年6月11日 	 
 */
@Controller
@RequestMapping("visa/employeemanage")
public class employeeController extends BaseController {
	@Autowired
	private EmployeeViewService employeeViewService;

	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;

	/**
	 * 列表信息展示
	 */
	@RequestMapping(value = "employeelist")
	@ResponseBody
	public Object employeelist(@RequestBody UserSqlForm sqlForm) {
		Pager pager = new Pager();
		pager.setPageNumber(sqlForm.getPageNumber());
		pager.setPageSize(sqlForm.getPageSize());
		return employeeViewService.listPage(sqlForm, pager);
	}

	/**
	 * 添加数据操作
	 * @param addForm
	 */
	@RequestMapping(value = "addUserData")
	@ResponseBody
	@POST
	public Object addUserData(UserAddForm addForm) {
		addForm.setCreate_date(new Date());
		addForm.setDisableUserStatus(UserStatusEnum.VALID.intKey());//激活
		SysUserEntity userdto = FormUtil.add(dbDao, addForm, SysUserEntity.class);
		return userdto;
	}
}
