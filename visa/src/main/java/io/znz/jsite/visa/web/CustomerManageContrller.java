/**
 * CustomerManageContrller.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.visa.entity.customer.CustomerManageEntity;
import io.znz.jsite.visa.forms.customerform.CustomerAddForm;
import io.znz.jsite.visa.forms.customerform.CustomerSqlForm;
import io.znz.jsite.visa.forms.customerform.CustomerUpdateForm;
import io.znz.jsite.visa.service.CustomerViewService;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * 客户管理控制类
 * @author   崔建斌
 * @Date	 2017年6月8日 	 
 */
@Controller
@RequestMapping("visa/customermanagement")
public class CustomerManageContrller extends BaseController {

	@Autowired
	private CustomerViewService customerViewService;

	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;

	/**
	 * 列表信息展示
	 */
	@RequestMapping(value = "customerlist")
	@ResponseBody
	public Object customerlist(@RequestBody CustomerSqlForm sqlForm, final HttpSession session) {
		//通过session获取公司的id
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getComId();//得到公司的id
		if (!Util.isEmpty(comId)) {
			sqlForm.setComId(comId);
		}
		Pager pager = new Pager();
		pager.setPageNumber(sqlForm.getPageNumber());
		pager.setPageSize(sqlForm.getPageSize());
		return customerViewService.listPage(sqlForm, pager);
	}

	/**
	 * 打开客户添加页面
	 */
	@At
	@GET
	@Ok("jsp")
	public Object addCustomer() {
		return null;
	}

	/**
	 * 添加数据操作
	 * @param addForm
	 */
	@RequestMapping(value = "addData", method = RequestMethod.POST)
	@ResponseBody
	public Object addData(CustomerAddForm addForm, final HttpSession session) {
		return customerViewService.addData(addForm, session);
	}

	/**
	 * 回显数据
	 * @param updateForm
	 */
	@RequestMapping(value = "updateData")
	@ResponseBody
	public Object updateData(long cid) {
		return customerViewService.updateDate(cid);
	}

	/**
	 * 编辑保存客户信息
	 * @param updateForm
	 */
	@RequestMapping(value = "updateDataSave")
	@ResponseBody
	public Object updateDataSave(CustomerUpdateForm updateForm) {
		return customerViewService.updateDataSave(updateForm);
	}

	/**
	 * 根据id删除单条数据
	 * @param id
	 */
	public Object deleteData(@PathVariable long cid) {
		return customerViewService.deleteById(cid);
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
