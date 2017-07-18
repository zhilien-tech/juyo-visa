/**
 * employeeController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.visa.entity.job.JobEntity;
import io.znz.jsite.visa.forms.employeeform.EmployeeAddForm;
import io.znz.jsite.visa.forms.employeeform.EmployeeSqlForm;
import io.znz.jsite.visa.forms.employeeform.EmployeeUpdateForm;
import io.znz.jsite.visa.service.EmployeeViewService;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.common.util.Util;
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
	public Object employeelist(@RequestBody EmployeeSqlForm sqlForm, final HttpSession session) {
		//通过session获取公司的id
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getComId();//得到公司的id
		sqlForm.setComId(comId);
		Pager pager = new Pager();
		pager.setPageNumber(sqlForm.getPageNumber());
		pager.setPageSize(sqlForm.getPageSize());
		return employeeViewService.listPage(sqlForm, pager);
	}

	/**
	 * 页面加载时查询出该公司下面的所有部门
	 * @param session
	 */
	@RequestMapping(value = "queryDeptName")
	@ResponseBody
	public Object queryDeptName(final HttpSession session) {
		return employeeViewService.queryDept(session);
	}

	/**
	 * 添加时查询出部门联动带出职位
	 */
	@RequestMapping(value = "selectJobName")
	@ResponseBody
	public Object selectJobName(String deptId) {
		Long parseLong = null;
		if (!Util.isEmpty(deptId)) {
			parseLong = Long.parseLong(deptId);
		}
		//根据前端传过来的部门id查询出职位
		return dbDao.query(JobEntity.class, Cnd.where("deptId", "=", parseLong), null);
	}

	/**
	 * 添加数据操作
	 * @param addForm
	 */
	@RequestMapping(value = "addUserData", method = RequestMethod.POST)
	@ResponseBody
	public Object addUserData(EmployeeAddForm addForm, long jobId, final HttpSession session) {
		return employeeViewService.addUserData(addForm, jobId, session);
	}

	/**
	 * 回显数据
	 * @param updateForm
	 */
	@RequestMapping(value = "updateData")
	@ResponseBody
	public Object updateData(long uid, final HttpSession session) {
		return employeeViewService.updateDate(uid, session);
	}

	/**
	 * 编辑保存员工信息
	 * @param updateForm
	 */
	@RequestMapping(value = "updateUserData")
	@ResponseBody
	public Object updateUserData(EmployeeUpdateForm updateForm, final HttpSession session) {
		return employeeViewService.updateDataSave(updateForm, session);
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
