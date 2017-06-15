/**
 * employeeController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.visa.forms.employeeform.EmployeeAddForm;
import io.znz.jsite.visa.forms.employeeform.EmployeeSqlForm;
import io.znz.jsite.visa.forms.employeeform.EmployeeUpdateForm;
import io.znz.jsite.visa.service.EmployeeViewService;

import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.mvc.annotation.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.db.dao.IDbDao;

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
	public Object employeelist(@RequestBody EmployeeSqlForm sqlForm) {
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
	public Object addUserData(EmployeeAddForm addForm) {
		return employeeViewService.addUserData(addForm);
	}

	/**
	 * 回显数据
	 * @param updateForm
	 */
	@RequestMapping(value = "updateData")
	@ResponseBody
	public Object updateData(long uid) {
		return employeeViewService.updateDate(uid);
	}

	/**
	 * 编辑保存员工信息
	 * @param updateForm
	 */
	@RequestMapping(value = "updateUserData")
	@ResponseBody
	public Object updateUserData(EmployeeUpdateForm updateForm) {
		return employeeViewService.updateDataSave(updateForm);
	}

	/**
	 * 删除单条数据
	 * @param userId
	 */
	@RequestMapping(value = "deleteUserData")
	@ResponseBody
	public boolean deleteUserData(Integer userId) {
		return employeeViewService.deleteUserData(userId);
	}

	/**
	 * 初始化密码操作
	 * @param userId
	 */
	@RequestMapping(value = "initpassword")
	@ResponseBody
	public boolean initpassword(Integer userId) {
		return employeeViewService.initpassword(userId);
	}
}