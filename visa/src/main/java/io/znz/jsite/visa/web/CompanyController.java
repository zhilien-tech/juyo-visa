/**
 * CompanyController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.visa.entity.company.CompanyEntity;
import io.znz.jsite.visa.entity.customer.CustomerManageEntity;
import io.znz.jsite.visa.forms.companyform.CompanySqlForm;
import io.znz.jsite.visa.service.company.CompanyService;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.mvc.annotation.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.db.dao.IDbDao;

/**
 * 公司
 * @author   崔建斌
 * @Date	 2017年7月5日 	 
 */
@Controller
@RequestMapping("visa/company")
public class CompanyController extends BaseController {
	@Autowired
	private IDbDao dbDao;

	@Autowired
	private Dao nutDao;

	@Autowired
	private CompanyService companyService;

	/**
	 * 列表信息展示
	 */
	@RequestMapping(value = "companylist")
	@ResponseBody
	public Object companylist(@RequestBody CompanySqlForm sqlForm, Pager pager) {
		return companyService.companylist(sqlForm, pager);
	}

	/**
	 * 添加公司数据
	 * @param addForm
	 */
	@RequestMapping(value = "addcompany", method = RequestMethod.POST)
	@ResponseBody
	public Object addcompany(CompanyEntity addForm, EmployeeEntity empForm) {
		return companyService.addcompany(addForm, empForm);
	}

	/**
	 * 回显公司数据
	 * @param updateForm
	 */
	@RequestMapping(value = "updatecompany")
	@ResponseBody
	public Object updatecompany(long comId) {
		return companyService.updatecompany(comId);
	}

	/**
	 * 编辑保存公司信息
	 * @param updateForm
	 */
	@RequestMapping(value = "updateCompanySave", method = RequestMethod.POST)
	@ResponseBody
	public Object updateCompanySave(CompanyEntity updateForm) {
		return companyService.updateCompanySave(updateForm);
	}

	/**
	 * 根据id删除单条数据
	 * @param id
	 */
	@RequestMapping(value = "deleteCompany")
	@ResponseBody
	public Object deleteCompany(Integer comId) {
		return companyService.deleteCompany(comId);
	}

	/**
	 * 手机号码唯一性校验
	 * @param telephone
	 */
	@RequestMapping(value = "checkTelephone")
	@ResponseBody
	@POST
	public Boolean checkTelephone(String telephone) {
		int count = nutDao.count(CustomerManageEntity.class, Cnd.where("telephone", "=", telephone));
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}
}
