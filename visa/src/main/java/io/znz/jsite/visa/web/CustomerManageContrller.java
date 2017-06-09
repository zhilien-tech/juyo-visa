/**
 * CustomerManageContrller.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.visa.bean.entity.CustomerManageEntity;
import io.znz.jsite.visa.service.CustomerViewService;

import org.nutz.mvc.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

	/**
	 * 列表信息展示
	 */
	@RequestMapping(value = "customerlist")
	@ResponseBody
	private Object customerlist() {
		return customerViewService.customerListData();
	}

	/**
	 * 添加数据
	 * @param addForm
	 */
	private Object addData(@Param("..") final CustomerManageEntity addForm) {
		return customerViewService.addSaveDate(addForm);
	}

	/**
	 * 更新数据
	 * @param updateForm
	 */
	private Object updateData(@Param("..") final CustomerManageEntity updateForm) {
		return customerViewService.updateSaveDate(updateForm);
	}

	/**
	 * 根据id删除单条数据
	 * @param id
	 */
	private Object deleteData(@Param("id") final long id) {
		return customerViewService.deleteById(id);
	}
}
