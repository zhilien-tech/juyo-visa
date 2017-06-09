/**
 * CustomerManageContrller.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.visa.bean.entity.CustomerManageEntity;
import io.znz.jsite.visa.forms.customerform.CustomerAddForm;
import io.znz.jsite.visa.forms.customerform.CustomerSqlForm;
import io.znz.jsite.visa.forms.customerform.CustomerUpdateForm;
import io.znz.jsite.visa.service.CustomerViewService;

import java.util.Date;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.db.dao.IDbDao;
import com.uxuexi.core.web.util.FormUtil;

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

	/**
	 * 列表信息展示
	 */
	@RequestMapping(value = "customerlist")
	@ResponseBody
	public Object customerlist(@RequestBody CustomerSqlForm sqlForm) {
		Pager pager = new Pager();
		pager.setPageNumber(sqlForm.getPageNumber());
		pager.setPageSize(sqlForm.getPageSize());
		return customerViewService.listPage(sqlForm, pager);
	}

	@At
	@GET
	@Ok("jsp")
	public Object addCustomer() {
		return null;
	}

	/**
	 * 添加数据
	 * @param addForm
	 */
	@RequestMapping(value = "addData")
	@ResponseBody
	@POST
	public Object addData(CustomerAddForm addForm) {
		CustomerManageEntity cusdto = FormUtil.add(dbDao, addForm, CustomerManageEntity.class);
		addForm.setCreateTime(new Date());
		addForm.setSerialNumber(cusdto.getId());
		return cusdto;
	}

	/**
	 * 更新数据
	 * @param updateForm
	 */
	public Object updateData(CustomerUpdateForm updateForm) {
		return customerViewService.updateSaveDate(updateForm);
	}

	/**
	 * 根据id删除单条数据
	 * @param id
	 */
	public Object deleteData(@PathVariable long cid) {
		return customerViewService.deleteById(cid);
	}
}
