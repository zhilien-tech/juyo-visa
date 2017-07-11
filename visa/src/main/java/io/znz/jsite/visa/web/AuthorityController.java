/**
 * AuthorityController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.visa.dto.DeptJobForm;
import io.znz.jsite.visa.entity.company.CompanyEntity;
import io.znz.jsite.visa.forms.authority.AuthoritySqlForm;
import io.znz.jsite.visa.service.authority.AuthorityService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.db.dao.IDbDao;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @author   崔建斌
 * @Date	 2017年7月10日 	 
 */
@Controller
@RequestMapping("visa/authority")
public class AuthorityController {
	@Autowired
	private IDbDao dbDao;

	@Autowired
	private Dao nutDao;

	@Autowired
	private AuthorityService authorityService;

	/**
	 * 列表信息展示
	 */
	@RequestMapping(value = "companylist")
	@ResponseBody
	public Object companylist(@RequestBody AuthoritySqlForm sqlForm, Pager pager) {
		return authorityService.authoritylist(sqlForm, pager);
	}

	/**
	 * 根据职位id查询出该本公司下该职位的所有功能
	 * @param jobId
	 * @param session
	 */
	@RequestMapping(value = "queryComAllFunction")
	@ResponseBody
	public Object queryComAllFunction(long jobId, HttpServletRequest request) {
		return authorityService.queryComAllFunction(jobId, request);
	}

	/**
	 * 添加部门职位以及职位权限配置
	 * @param addForm
	 */
	@RequestMapping(value = "addDeptJob", method = RequestMethod.POST)
	@ResponseBody
	public Object addDeptJob(DeptJobForm addForm, final HttpSession session) {
		return authorityService.saveDeptJobData(addForm, session);
	}

	/**
	 * 回显公司数据
	 * @param updateForm
	 */
	@RequestMapping(value = "updatecompany")
	@ResponseBody
	public Object updatecompany(long comId) {
		return authorityService.updatecompany(comId);
	}

	/**
	 * 编辑保存公司信息
	 * @param updateForm
	 */
	@RequestMapping(value = "updateCompanySave", method = RequestMethod.POST)
	@ResponseBody
	public Object updateCompanySave(CompanyEntity updateForm) {
		return authorityService.updateCompanySave(updateForm);
	}

	/**
	 * 根据id删除单条数据
	 * @param id
	 */
	@RequestMapping(value = "deleteCompany")
	@ResponseBody
	public Object deleteCompany(Integer comId) {
		return authorityService.deleteCompany(comId);
	}
}
