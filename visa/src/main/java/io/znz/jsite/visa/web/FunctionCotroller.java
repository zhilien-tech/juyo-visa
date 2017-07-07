/**
 * FunctionCotroller.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.visa.entity.function.FunctionEntity;
import io.znz.jsite.visa.forms.function.FunctionSqlForm;
import io.znz.jsite.visa.service.function.FunctionService;

import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.db.dao.IDbDao;

/**
 * 功能管理
 * @author   崔建斌
 * @Date	 2017年7月6日 	 
 */
@Controller
@RequestMapping("visa/function")
public class FunctionCotroller extends BaseController {
	@Autowired
	private IDbDao dbDao;

	@Autowired
	private Dao nutDao;

	@Autowired
	private FunctionService functionService;

	/**
	 * 列表信息展示
	 */
	@RequestMapping(value = "functionlist")
	@ResponseBody
	public Object functionlist(@RequestBody FunctionSqlForm sqlForm, Pager pager) {
		return functionService.functionlist(sqlForm, pager);
	}

	/**
	 * 添加功能数据
	 * @param addForm
	 */
	@RequestMapping(value = "addfunction", method = RequestMethod.POST)
	@ResponseBody
	public Object addfunction(FunctionEntity addForm) {
		return functionService.addfunction(addForm);
	}

	/**
	 * 回显功能数据
	 * @param updateForm
	 */
	@RequestMapping(value = "updatefunction")
	@ResponseBody
	public Object updatefunction(long funId) {
		return functionService.updatefunction(funId);
	}

	/**
	 * 编辑保存功能信息
	 * @param updateForm
	 */
	@RequestMapping(value = "updateFunctionSave", method = RequestMethod.POST)
	@ResponseBody
	public Object updateFunctionSave(FunctionEntity updateForm) {
		return functionService.updateFunctionSave(updateForm);
	}

	/**
	 * 根据id删除单条数据
	 * @param id
	 */
	public Object deleteData(@PathVariable long funId) {
		//return functionService.deleteById(cid);
		return null;
	}

}
